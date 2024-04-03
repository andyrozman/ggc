package ggc.plugin.device.impl.minimed.comm.usb;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.util.concurrent.TimeoutException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ggc.plugin.data.enums.PlugInExceptionType;
import ggc.plugin.device.PlugInBaseException;
import ggc.plugin.device.impl.minimed.comm.usb.contournext.data.message.ContourNextLinkMessage;
import ggc.plugin.device.impl.minimed.comm.usb.contournext.data.message.series5xx.ContourNextLinkBinaryMessage5xx;
import ggc.plugin.device.impl.minimed.comm.usb.contournext.data.message.series5xx.MedtronicReceiveMessage5xx;
import ggc.plugin.device.impl.minimed.comm.usb.contournext.data.message.series5xx.MedtronicSendMessage5xx;
import ggc.plugin.device.impl.minimed.comm.usb.contournext.enums.CommandType;
import ggc.plugin.device.impl.minimed.data.MinimedCommandReply;
import ggc.plugin.device.impl.minimed.enums.MinimedCommInterfaceType;
import ggc.plugin.device.impl.minimed.enums.MinimedCommandType;
import ggc.plugin.device.impl.minimed.handler.MinimedDataHandler;
import ggc.plugin.device.impl.minimed.util.MedtronicUtil;
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

public class MinimedCommunicationContourNext2 extends MinimedCommunicationContourNextAbstract
{

    private static final Logger LOG = LoggerFactory.getLogger(MinimedCommunicationContourNext2.class);


    /**
     * Constructor
     *
     * @param dataAccess
     * @param minimedDataHandler data Handler
     */
    public MinimedCommunicationContourNext2(DataAccessPlugInBase dataAccess, MinimedDataHandler minimedDataHandler)
    {
        super(dataAccess, minimedDataHandler);

        // responseConverter = new ContourNextLink2ResponseConverter(session);
    }


    @Override
    public MinimedCommInterfaceType getInterfaceType()
    {
        return MinimedCommInterfaceType.ContourNextLink2;
    }


    public int initDevice() throws PlugInBaseException
    {
        try
        {
            enterControlMode();
            enterPassthroughMode();
            openConnection();
        }
        catch (PlugInBaseException e)
        {
            e.printStackTrace();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }

        return 0;
    }


    public int closeDevice() throws PlugInBaseException
    {
        try
        {
            closeConnection();
            endPassthroughMode();
            endControlMode();
        }
        catch (PlugInBaseException e)
        {
            e.printStackTrace();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }

        return 0;
    }


    public void sendMessage(ContourNextLinkMessage message) throws PlugInBaseException
    {
        sendMessage(message.encode());
    }


    public MinimedCommandReply executeCommandWithRetry(MinimedCommandType commandType) throws PlugInBaseException
    {
        LOG.debug("executeCommandWithRetry (commandType={}) - START", commandType.name());

        byte[] response = null;
        int tryCount = 0;

        do
        {

            tryCount++;
            System.out.println("  Send command (try=" + tryCount + ")");

            MedtronicUtil.sleepMs(1000);

            sendMessage(commandType);
            response = readMessage();

        } while (response.length < 34 && tryCount < 3);

        if (tryCount == 3)
        {
            throw new PlugInBaseException(PlugInExceptionType.NoResponseFromDeviceForIssuedCommand,
                    new Object[] { commandType.name() });
        }

        MedtronicReceiveMessage5xx responseMessage = MedtronicReceiveMessage5xx.fromBytes(response);

        // System.out.println("Full:    " + bitUtils.getByteArrayHex(response));
        //
        // byte[] subArray = bitUtils.getByteSubArray(response, 33, response.length-33);
        //
        // System.out.println("Subarry  " + bitUtils.getByteArrayHex(subArray));
        //
        // System.out.println("Suuuuuu  " +
        // bitUtils.getByteArrayHex(responseMessage.getResponsePayload()));

        MinimedCommandReply minimedCommandReply = getMinimedCommandReply(commandType,
            responseMessage.getResponsePayload());

        LOG.debug("executeCommandWithRetry (commandType={}) - END", commandType.name());

        return minimedCommandReply;
    }


    private MinimedCommandReply getMinimedCommandReply(MinimedCommandType commandType, byte[] rawData)
            throws PlugInBaseException
    {
        MinimedCommandReply reply = new MinimedCommandReply(commandType);

        reply.setRawData(rawData);
        return reply;
    }


    @Override
    protected void executeHistoryCommandWithRetry(MinimedCommandType commandType) throws PlugInBaseException
    {

    }


    private void sendMessage(MinimedCommandType commandType) throws PlugInBaseException
    {
        sendMessage(new MedtronicSendMessage5xx(commandType, session));
    }


    public void openConnection() throws IOException, TimeoutException, NoSuchAlgorithmException, PlugInBaseException
    {
        LOG.debug("Begin openConnection");
        sendMessage(new ContourNextLinkBinaryMessage5xx(CommandType.OPEN_CONNECTION, session, null));
        // FIXME - We need to care what the response message is - wrong MAC and
        // all that
        byte[] res = readMessage();

        // if (res.length==0)
        res = readMessage();

        LOG.debug("Finished openConnection");
    }
}
