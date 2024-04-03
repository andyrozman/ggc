package ggc.plugin.device.impl.minimed.comm.serial;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ggc.plugin.comm.JSSCCommunicationHandler;
import ggc.plugin.comm.NRSerialCommunicationHandler;
import ggc.plugin.comm.SerialCommunicationAbstract;
import ggc.plugin.comm.cfg.SerialSettings;
import ggc.plugin.data.enums.PlugInExceptionType;
import ggc.plugin.device.PlugInBaseException;
import ggc.plugin.device.impl.minimed.comm.MinimedCommunicationAbstract;
import ggc.plugin.device.impl.minimed.data.MinimedCommandReply;
import ggc.plugin.device.impl.minimed.enums.MinimedCommandType;
import ggc.plugin.device.impl.minimed.handler.MinimedDataHandler;
import ggc.plugin.device.impl.minimed.util.MedtronicUtil;
import ggc.plugin.util.DataAccessPlugInBase;

/**
 *  Application:   GGC - GNU Gluco Control
 *  Plug-in:       GGC PlugIn Base (base class for all plugins)
 *
 *  See AUTHORS for copyright information.
 *
 *  This program is free software; you can redistribute it and/or modify it under
 *  the terms of the GNU General Public License as published by the Free Software
 *  Foundation; either version 2 of the License, or (at your option) any later
 *  version.
 *
 *  This program is distributed in the hope that it will be useful, but WITHOUT
 *  ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 *  FOR A PARTICULAR PURPOSE. See the GNU General Public License for more
 *  details.
 *
 *  You should have received a copy of the GNU General Public License along with
 *  this program; if not, write to the Free Software Foundation, Inc., 59 Temple
 *  Place, Suite 330, Boston, MA 02111-1307 USA
 *
 *  Filename:     MinimedSerialCommunicationAbstract
 *  Description:  Abstract communication class for Serial Communication.
 *
 *  Author: Andy {andy@atech-software.com}
 */

public abstract class MinimedSerialCommunicationAbstract extends MinimedCommunicationAbstract
{

    private static Logger LOG = LoggerFactory.getLogger(MinimedSerialCommunicationAbstract.class);

    protected SerialCommunicationAbstract commHandler;

    protected int deviceTimeout;

    // 1 = NRSerialCommunicationHandler, 2 = JSSCCommunicationHandler
    protected int serialCommunicationType = 1;
    protected static final int DEVICE_CONNECT_RETRY = 4;
    protected static final int COMMAND_RETRIES = 2;

    protected static final byte RESPONSE_ACK = 85;
    protected static final byte RESPONSE_NAK = 102;


    /**
     * Constructor
     *
     * @param minimedDataHandler data Handler
     */
    public MinimedSerialCommunicationAbstract(DataAccessPlugInBase dataAccess, MinimedDataHandler minimedDataHandler)
    {
        super(dataAccess, minimedDataHandler);
    }


    public abstract SerialSettings getSerialSettings();


    public int getDeviceTimeout()
    {
        return this.commHandler.getReceiveTimeout();
    }


    public void setDeviceTimeout(int timeout) throws PlugInBaseException
    {
        this.deviceTimeout = timeout;
        // this.commHandler.setReceiveTimeout(timeout);
    }


    public void readUntilDrained() throws PlugInBaseException
    {
        if (lowLevelDebug)
            LOG.debug("readUntilDrained");

        try
        {
            int j = 0;
            MedtronicUtil.sleepPhysicalCommunication();

            while (commHandler.available() > 0)
            {
                byte[] b = new byte[commHandler.available()];
                this.commHandler.read(b);
                j += b.length;
            }

            if (lowLevelDebug)
                LOG.debug("drained " + j + " elements");
        }
        catch (Exception ex)
        {
            LOG.warn("readUntilDrained ex.:" + ex, ex);
        }
    }


    public NRSerialCommunicationHandler getNRSHandler()
    {
        return (NRSerialCommunicationHandler) this.commHandler;
    }


    public JSSCCommunicationHandler getJSSCHandler()
    {
        return (JSSCCommunicationHandler) this.commHandler;
    }


    public byte[] readDataUntilEmpty() throws PlugInBaseException
    {
        if (serialCommunicationType == 1)
        {
            return readDataUntilEmpty_NRJS();
        }
        else
        {
            return readDataUntilEmpty_JSSC();
        }
    }


    public byte[] readDataUntilEmpty_NRJS() throws PlugInBaseException
    {
        if (lowLevelDebug)
            LOG.debug("readDataUntilEmpty_NRJS -- START");

        byte[] j = this.getNRSHandler().readAvailableData();

        LOG.debug("readDataUntilEmpty_NRJS (" + bitUtils.getHexCompact(j) + ")" + (lowLevelDebug ? " -- END" : ""));

        return j;
    }


    public byte[] readDataUntilEmpty_JSSC() throws PlugInBaseException
    {
        if (lowLevelDebug)
            LOG.debug("readDataUntilEmpty_JSSC -- START");

        byte[] data = getJSSCHandler().readAvailableData();

        LOG.debug("readDataUntilEmpty_JSSC (" + bitUtils.getHexCompact(data) + ")" + (lowLevelDebug ? " -- END" : ""));

        return data;
    }


    protected int readFromPhysicalPortBlocking() throws PlugInBaseException
    {
        if (this.serialCommunicationType == 1)
        {
            return readFromPhysicalPortBlocking_NRJS(false);
        }
        else
        {
            return readFromPhysicalPortBlocking_JSSC();
        }
    }


    protected int readFromPhysicalPortBlockingAndAvailable() throws PlugInBaseException
    {
        if (this.serialCommunicationType == 1)
        {
            return readFromPhysicalPortBlocking_NRJS(true);
        }
        else
        {
            return readFromPhysicalPortBlocking_JSSC();
        }
    }


    private int readFromPhysicalPortBlocking_JSSC() throws PlugInBaseException
    {
        MedtronicUtil.sleepMs(200);

        long startTime = System.currentTimeMillis();

        int data = this.commHandler.read();

        debugRead(data, startTime);

        return data;
    }


    private int readFromPhysicalPortBlocking_NRJS(boolean checkAvailable) throws PlugInBaseException
    {
        return readFromPhysicalPortBlockingWithTimeout(false, checkAvailable);
    }


    private int readFromPhysicalPortBlockingWithTimeout(boolean withTimeout, boolean checkAvailable)
            throws PlugInBaseException
    {
        int data;

        MedtronicUtil.sleepMs(200);

        long startTime = System.currentTimeMillis();
        long end = startTime + this.deviceTimeout;

        if (!withTimeout)
        {
            end += 60 * 1000;
        }

        if (checkAvailable)
        {
            LOG.debug("Available: " + this.commHandler.available());

            if (this.commHandler.available() == 0)
            {
                return -1;
            }
        }

        do
        {
            data = this.commHandler.read();

        } while ((data == -1) && (System.currentTimeMillis() < end));

        debugRead(data, startTime);

        return data;
    }


    public boolean createCommunicatorHandlerInstance()
    {
        if (this.commHandler == null)
        {
            if (serialCommunicationType == 1)
            {
                this.commHandler = new NRSerialCommunicationHandler(MedtronicUtil.getConnectionParameters().portName,
                        getSerialSettings());
            }
            else
            {
                this.commHandler = new JSSCCommunicationHandler(MedtronicUtil.getConnectionParameters().portName,
                        getSerialSettings());
            }
            return false;
        }
        else if (this.commHandler.isDeviceConnected())
        {
            LOG.debug("Physical (Serial) Port is already connected.");
            return true;
        }

        return false;
    }


    public void createPhysicalPort() throws PlugInBaseException
    {
        if (createCommunicatorHandlerInstance())
            return;

        LOG.debug("Create Physical (Serial) Connection Port");

        for (int retry = 0; retry < DEVICE_CONNECT_RETRY; retry++)
        {
            try
            {
                this.commHandler.connectAndInitDevice();
                this.setDeviceTimeout(500);
            }
            catch (Exception ex)
            {
                if (retry == (DEVICE_CONNECT_RETRY - 1))
                    throw new PlugInBaseException(ex);

                LOG.info("Retry [" + retry + "/] to create Serial Port. Ex.: " + ex);

                MedtronicUtil.sleepMs(4000);
            }
        }

        // this.readUntilDrained();
        MedtronicUtil.sleepPhysicalCommunication();
    }


    public void writeToPhysicalPort(byte data) throws PlugInBaseException
    {
        MedtronicUtil.sleepPhysicalCommunication();
        long startTime = System.currentTimeMillis();
        this.commHandler.write(data);
        debugWrite(data, startTime);
        MedtronicUtil.sleepPhysicalCommunication();
    }


    public void writeToPhysicalPort(byte[] data) throws PlugInBaseException
    {
        MedtronicUtil.sleepPhysicalCommunication();
        long startTime = System.currentTimeMillis();
        this.commHandler.write(data);
        debugWrite(data, startTime);
        MedtronicUtil.sleepPhysicalCommunication();
    }


    // refactor
    public int readIntTimeout() throws PlugInBaseException
    {
        return this.readIntTimeout(this.deviceTimeout);
    }


    private int readIntTimeout(long ms) throws PlugInBaseException
    {
        // System.out.println("readIntTimeout " + ms);

        try
        {
            int i = -1;

            i = this.commHandler.read();

            if (i != -1)
            {
                // LOG.debug(" <- " + )
                return i;
            }

            long end_time = System.currentTimeMillis() + ms + 1000;

            while (System.currentTimeMillis() < end_time)
            {
                i = this.commHandler.read();

                if (i != -1)
                    return i;
            }

            return -1;
        }
        catch (Exception ex)
        {
            throw new PlugInBaseException(ex);
        }

    }


    protected Integer executeCommandWithIntegerReply(MinimedCommandType commandType, String errorMsg)
            throws PlugInBaseException
    {
        return (Integer) executeCommandWithReply(commandType, errorMsg);
    }


    protected String executeCommandWithStringReply(MinimedCommandType commandType, String errorMsg)
            throws PlugInBaseException
    {
        return (String) executeCommandWithReply(commandType, errorMsg);
    }


    protected void debugRead(int i, long startTime)
    {
        getLocalLog().debug("Read from device: ({} ms):  {} ({})", (System.currentTimeMillis() - startTime),
            bitUtils.getHex(i), i);
    }


    protected void debugWrite(int i, long startTime)
    {
        getLocalLog().debug("Writen to device: ({} ms):  {} ({})", (System.currentTimeMillis() - startTime),
            bitUtils.getHex(i), i);
    }


    protected void debugWrite(byte[] i, long startTime)
    {
        getLocalLog().debug("Writen to device: ({} ms):  {}", (System.currentTimeMillis() - startTime),
            bitUtils.getHex(i));
    }


    public abstract Logger getLocalLog();


    protected Object executeCommandWithReply(MinimedCommandType commandType, String errorMsg) throws PlugInBaseException
    {
        try
        {
            MinimedCommandReply minimedCommandReply = executeCommandWithRetry(commandType);

            return this.dataHandler.getReplyValue(minimedCommandReply);
        }
        catch (PlugInBaseException ex)
        {
            LOG.error("Error on device communication [" + errorMsg + "]. Ex.: " + ex, ex);

            throw new PlugInBaseException(PlugInExceptionType.ErrorWithDeviceCommunicationDescription,
                    new Object[] { errorMsg, ex.getMessage() }, ex);
        }
    }


    protected byte[] readWithSize(int size) throws PlugInBaseException
    {
        byte[] data = new byte[size];

        if (serialCommunicationType == 1)
            this.getNRSHandler().read(data);
        else
            this.getJSSCHandler().read(data);

        return data;
    }

}
