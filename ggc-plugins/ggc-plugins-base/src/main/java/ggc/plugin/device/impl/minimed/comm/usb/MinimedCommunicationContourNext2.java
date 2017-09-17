package ggc.plugin.device.impl.minimed.comm.usb;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.util.concurrent.TimeoutException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ggc.plugin.device.PlugInBaseException;
import ggc.plugin.device.impl.minimed.comm.usb.contournext.data.message.ContourNextLinkMessage;
import ggc.plugin.device.impl.minimed.comm.usb.contournext.data.message.series5xx.MedtronicSendMessage5xx;
import ggc.plugin.device.impl.minimed.comm.usb.contournext.data.message.series6xx.ContourNextLinkBinaryMessage;
import ggc.plugin.device.impl.minimed.comm.usb.contournext.enums.CommandType;
import ggc.plugin.device.impl.minimed.data.MinimedCommandReply;
import ggc.plugin.device.impl.minimed.enums.MinimedCommInterfaceType;
import ggc.plugin.device.impl.minimed.enums.MinimedCommandType;
import ggc.plugin.device.impl.minimed.handler.MinimedDataHandler;
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

        sendMessage(commandType);

        // FIXME do we get 2 responses or one
        // Read the 0x81
        readMessage();

        // Read the 0x80
        MinimedCommandReply minimedCommandReply = getMinimedCommandReply(commandType, readMessage());

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
        sendMessage(new ContourNextLinkBinaryMessage(CommandType.OPEN_CONNECTION, session, null));
        // FIXME - We need to care what the response message is - wrong MAC and
        // all that
        readMessage();
        LOG.debug("Finished openConnection");
    }
}
