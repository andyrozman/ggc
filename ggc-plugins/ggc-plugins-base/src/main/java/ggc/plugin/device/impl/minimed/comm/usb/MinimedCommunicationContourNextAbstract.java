package ggc.plugin.device.impl.minimed.comm.usb;

import java.io.ByteArrayOutputStream;
import java.nio.ByteBuffer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ggc.plugin.data.enums.ASCII;
import ggc.plugin.data.enums.PlugInExceptionType;
import ggc.plugin.device.PlugInBaseException;
import ggc.plugin.device.impl.minimed.comm.usb.contournext.data.MedtronicCnlSession;
import ggc.plugin.device.impl.minimed.comm.usb.contournext.data.message.ContourNextLinkCommandMessage;
import ggc.plugin.device.impl.minimed.comm.usb.contournext.data.message.ContourNextLinkMessage;
import ggc.plugin.device.impl.minimed.comm.usb.contournext.data.message.series6xx.ContourNextLinkBinaryMessage;
import ggc.plugin.device.impl.minimed.comm.usb.contournext.data.message.series6xx.MedtronicMessage;
import ggc.plugin.device.impl.minimed.comm.usb.contournext.enums.CommandType;
import ggc.plugin.device.impl.minimed.comm.usb.contournext.tmp.HexDump;
import ggc.plugin.device.impl.minimed.enums.MinimedCommInterfaceType;
import ggc.plugin.device.impl.minimed.handler.MinimedDataHandler;
import ggc.plugin.device.impl.minimed.util.MinimedUtil;
import ggc.plugin.util.DataAccessPlugInBase;

/**
 * Created by andy on 13.06.17.
 */

/**
 * This class was taken from 600SeriesAndroidUploader project, which
 * is loacted at https://github.com/pazaan/600SeriesAndroidUploader.
 *
 * Original author: lgoedhart on 26/03/2016.
 *
 * Comment: this file has just minor changes
 */

public abstract class MinimedCommunicationContourNextAbstract extends MinimedUsbHidCommunicationAbstract

{

    private static Logger LOG = LoggerFactory.getLogger(MinimedCommunicationContourNextAbstract.class);

    private static final int USB_BLOCKSIZE = 64;
    private static final int READ_TIMEOUT_MS = 5000;
    private static final String BAYER_USB_HEADER = "ABC";

    protected MedtronicCnlSession session;


    /**
     * Constructor
     *
     * @param dataAccess
     * @param minimedDataHandler data Handler
     */
    public MinimedCommunicationContourNextAbstract(DataAccessPlugInBase dataAccess,
            MinimedDataHandler minimedDataHandler)
    {
        super(dataAccess, minimedDataHandler);
        createSession();
    }


    private void createSession()
    {
        MedtronicCnlSession sess = new MedtronicCnlSession();
        sess.setMinimedCommunicationInterfaceType(getInterfaceType());
        // sess.setStickSerial("C16325");
        sess.setPumpSerial(MinimedUtil.getConnectionParameters().serialNumber);

        session = sess;

        // FIXME
        // MinimedUtil.setMedtronicCnlSession(sess);
    }


    public abstract MinimedCommInterfaceType getInterfaceType();


    public void enterControlMode() throws PlugInBaseException
    {
        boolean doRetry = false;

        try
        {
            sendMessage(new ContourNextLinkCommandMessage(ASCII.NAK));
            checkControlMessage(readMessage(), ASCII.EOT);

            sendMessage(new ContourNextLinkCommandMessage(ASCII.ENQ.getValue()));
            checkControlMessage(readMessage(), ASCII.ACK);
        }
        catch (PlugInBaseException e2)
        {
            // Terminate comms with the pump, then try again
            sendMessage(new ContourNextLinkCommandMessage(ASCII.EOT.getValue()));
            doRetry = true;
        }
        finally
        {
            if (doRetry)
            {
                enterControlMode();
            }
        }
    }


    public void enterPassthroughMode() throws PlugInBaseException
    {
        LOG.debug("Begin enterPasshtroughMode");
        sendMessage(new ContourNextLinkCommandMessage("W|"));
        checkControlMessage(readMessage(), ASCII.ACK);
        sendMessage(new ContourNextLinkCommandMessage("Q|"));
        checkControlMessage(readMessage(), ASCII.ACK);
        sendMessage(new ContourNextLinkCommandMessage("1|"));
        checkControlMessage(readMessage(), ASCII.ACK);
        LOG.debug("Finished enterPasshtroughMode");
    }


    public void endPassthroughMode() throws PlugInBaseException
    {
        LOG.debug("Begin endPassthroughMode");
        sendMessage(new ContourNextLinkCommandMessage("W|"));
        checkControlMessage(readMessage(), ASCII.ACK);
        sendMessage(new ContourNextLinkCommandMessage("Q|"));
        checkControlMessage(readMessage(), ASCII.ACK);
        sendMessage(new ContourNextLinkCommandMessage("0|"));
        checkControlMessage(readMessage(), ASCII.ACK);
        LOG.debug("Finished endPassthroughMode");
    }


    public void endControlMode() throws PlugInBaseException
    {
        LOG.debug("Begin endControlMode");
        sendMessage(new ContourNextLinkCommandMessage(ASCII.EOT.getValue()));
        checkControlMessage(readMessage(), ASCII.ENQ);
        LOG.debug("Finished endControlMode");
    }


    public static void checkControlMessage(byte[] msg, ASCII controlCharacter) throws PlugInBaseException
    {
        if (msg.length != 1 || controlCharacter.getValue() != msg[0])
        {
            throw new PlugInBaseException(PlugInExceptionType.DeviceInvalidResponseCommand,
                    new Object[] { (int) msg[0], (int) controlCharacter.getValue() });
        }
    }


    public byte[] readMessage() throws PlugInBaseException
    {
        ByteArrayOutputStream responseMessage = new ByteArrayOutputStream();

        byte[] responseBuffer = new byte[USB_BLOCKSIZE];
        int bytesRead;
        int messageSize = 0;

        do
        {
            bytesRead = communicationHandler.read(responseBuffer, READ_TIMEOUT_MS);

            if (bytesRead == -1)
            {
                throw new PlugInBaseException(PlugInExceptionType.TimeoutReadingData);
            }
            else if (bytesRead > 0)
            {
                // Validate the header
                ByteBuffer header = ByteBuffer.allocate(3);
                header.put(responseBuffer, 0, 3);
                String headerString = new String(header.array());
                if (!headerString.equals(BAYER_USB_HEADER))
                {
                    throw new PlugInBaseException(PlugInExceptionType.DeviceInvalidResponse);
                }
                messageSize = responseBuffer[3];
                responseMessage.write(responseBuffer, 4, messageSize);
            }
            else
            {
                LOG.warn("readMessage: got a zero-sized response.");
            }
        } while (bytesRead > 0 && messageSize == 60);

        String responseString = HexDump.dumpHexString(responseMessage.toByteArray());
        LOG.debug("READ: " + responseString);

        return responseMessage.toByteArray();
    }


    public void sendMessage(ContourNextLinkMessage message) throws PlugInBaseException
    {
        sendMessage(message.encode());

        if (message instanceof ContourNextLinkBinaryMessage)
        {
            session.incrBayerSequenceNumber();
        }

        if (message instanceof MedtronicMessage)
        {
            session.incrMedtronicSequenceNumber();
        }
    }


    public void sendMessage(byte[] message) throws PlugInBaseException
    {
        int pos = 0;

        while (message.length > pos)
        {
            ByteBuffer outputBuffer = ByteBuffer.allocate(USB_BLOCKSIZE);
            int sendLength = (pos + 60 > message.length) ? message.length - pos : 60;
            outputBuffer.put(BAYER_USB_HEADER.getBytes());
            outputBuffer.put((byte) sendLength);
            outputBuffer.put(message, pos, sendLength);

            communicationHandler.write(outputBuffer.array()); // ,
                                                              // PlugInBaseException
            pos += sendLength;

            String outputString = HexDump.dumpHexString(outputBuffer.array());
            LOG.debug("WRITE: " + outputString);
        }
    }


    protected void closeConnection() throws Exception
    {
        LOG.debug("Begin closeConnection");
        sendMessage(new ContourNextLinkBinaryMessage(CommandType.CLOSE_CONNECTION, session, null));
        // FIXME - We need to care what the response message is - wrong MAC and
        // all that
        readMessage();
        LOG.debug("Finished closeConnection");

    }

}
