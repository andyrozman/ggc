package ggc.plugin.device.impl.minimed.comm.usb;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ggc.plugin.comm.Hid4JavaCommunicationHandler;
import ggc.plugin.device.PlugInBaseException;
import ggc.plugin.device.impl.minimed.comm.MinimedCommunicationAbstract;
import ggc.plugin.device.impl.minimed.handler.MinimedDataHandler;
import ggc.plugin.device.impl.minimed.util.MedtronicUtil;
import ggc.plugin.util.DataAccessPlugInBase;

/**
 * Implementation for Usb Hid Communication
 *
 * WORK STATUS: Planned (Phase 4 - 2016/17)
 */
public abstract class MinimedUsbHidCommunicationAbstract extends MinimedCommunicationAbstract
{

    private static final Logger LOG = LoggerFactory.getLogger(MinimedUsbHidCommunicationAbstract.class);

    protected Hid4JavaCommunicationHandler communicationHandler;


    /**
     * Constructor
     *
     * @param minimedDataHandler data Handler
     */
    public MinimedUsbHidCommunicationAbstract(DataAccessPlugInBase dataAccess, MinimedDataHandler minimedDataHandler)
    {
        super(dataAccess, minimedDataHandler);
    }


    @Override
    public void preInitInterface()
    {
    }


    public void initializeCommunicationInterface() throws PlugInBaseException
    {
        LOG.debug("# Opening device");

        communicationHandler = new Hid4JavaCommunicationHandler();
        communicationHandler.setTargetDevice(MedtronicUtil.getConnectionParameters().portName);

        boolean connected = communicationHandler.connectAndInitDevice();

        LOG.debug("Connected to ContourLinkNext: " + connected);
    }


    // public int initDevice() throws PlugInBaseException
    // {
    // return 0;
    // }
    //
    //
    // public int closeDevice() throws PlugInBaseException
    // {
    // return 0;
    // }

    public int closeCommunicationInterface() throws PlugInBaseException
    {
        LOG.debug("closeCommunicationInterface - disconnect CountourNextLink device");

        communicationHandler.disconnectDevice();
        return 0;
    }

    // public MinimedCommandReply executeCommandWithRetry(MinimedCommandType
    // commandType) throws PlugInBaseException
    // {
    // return null;
    // }
    //
    //
    // @Override
    // protected void executeHistoryCommandWithRetry(MinimedCommandType
    // commandType) throws PlugInBaseException
    // {
    //
    // }

}
