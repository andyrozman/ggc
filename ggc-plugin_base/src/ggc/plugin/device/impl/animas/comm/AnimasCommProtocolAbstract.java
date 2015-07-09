package ggc.plugin.device.impl.animas.comm;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import ggc.plugin.comm.NRSerialCommunicationHandler;
import ggc.plugin.comm.SerialSettings;
import ggc.plugin.data.enums.PlugInExceptionType;
import ggc.plugin.device.PlugInBaseException;
import ggc.plugin.device.impl.animas.AnimasDeviceReader;
import ggc.plugin.device.impl.animas.data.AnimasDeviceData;
import ggc.plugin.device.impl.animas.enums.AnimasDeviceType;
import ggc.plugin.device.impl.animas.enums.AnimasImplementationType;
import ggc.plugin.device.impl.animas.util.AnimasUtils;
import ggc.plugin.output.OutputWriter;

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
 *  Filename:     AnimasCommProtocolAbstract
 *  Description:  This is abstract communication class. It contains just basic commands for
 *       serial communication, all other communication classes must extends this one.
 *
 *  Author: Andy Rozman {andy@atech-software.com}
 */

public abstract class AnimasCommProtocolAbstract
{

    public static final Log LOG = LogFactory.getLog(AnimasCommProtocolAbstract.class);

    // protected NRSerialPort serialDevice;
    // protected String portName;

    // protected InputStream inStream;
    // protected OutputStream outStream;
    protected AnimasDeviceType deviceType;

    protected boolean DEBUG = true;
    protected boolean debugCommunication = false;

    protected PlugInBaseException downloadProblem;

    protected AnimasDeviceData baseData;
    protected AnimasDeviceReader deviceReader;
    // private boolean deviceConnected;
    protected OutputWriter outputWriter;

    // messages
    public static final short START_MESSAGE_DEVICE = 192;
    public static final short END_MESSAGE_DEVICE = 193;
    public static final short CTL_MESSAGE_DEVICE = 125;

    NRSerialCommunicationHandler commHandler;


    public AnimasCommProtocolAbstract(String portName, AnimasDeviceType deviceType, AnimasDeviceReader deviceReader,
            OutputWriter outputWriter)
    {
        // this.portName = portName;
        this.deviceType = deviceType;
        // this.transferType = transferType;
        this.deviceReader = deviceReader;
        this.outputWriter = outputWriter;

        SerialSettings settings = new SerialSettings();
        settings.baudRate = 9600;

        commHandler = new NRSerialCommunicationHandler(portName, settings);

        // data.setPumpCommunicationInterface(this);
    }


    public void setDebugMode(boolean debugMode, boolean debugCommunication)
    {
        this.DEBUG = debugMode;
        this.debugCommunication = debugCommunication;
    }


    public boolean initSerialDevice()
    {

        return this.commHandler.connectAndInitDevice();
        // if ((this.serialDevice != null) && (this.serialDevice.isConnected()))
        // {
        // return true;
        // }
        //
        // this.serialDevice = new NRSerialPort(portName, 115200); // 9600
        //
        // this.deviceConnected = this.serialDevice.connect();
        //
        // LOG.debug("Connect: " + this.deviceConnected);
        //
        // if (!this.deviceConnected)
        // {
        // LOG.debug("Could not connect to port " + portName);
        //
        // return false;
        // }
        //
        //
        // try
        // {
        // SerialPort sport = this.serialDevice.getSerialPortInstance();
        // sport.setDTR(false);
        // sport.setRTS(true);
        //
        // inStream = this.serialDevice.getInputStream();
        // outStream = this.serialDevice.getOutputStream();
        // }
        // catch (Exception ex)
        // {
        // LOG.error("Error setting streams: " + ex, ex);
        // this.serialDevice = null;
        // return false;
        // }
        // return true;
    }


    public void disconnectDevice()
    {
        this.commHandler.disconnectDevice();

        // if (serialDevice != null)
        // {
        // if (this.serialDevice.isConnected())
        // {
        // this.serialDevice.disconnect();
        // this.serialDevice = null;
        // }
        // }
        //
        // this.deviceConnected = false;
    }


    public abstract AnimasImplementationType getImplementationType();


    public boolean hasDownloadErrors()
    {
        return downloadProblem != null;
    }


    public AnimasDeviceData getData()
    {
        return baseData;
    }


    public void setBaseData(AnimasDeviceData data)
    {
        this.baseData = data;
    }


    protected List<Short> readDataFromDeviceInternal_Old() throws PlugInBaseException
    {
        List<Short> tempData = new ArrayList<Short>();

        byte[] buffer = new byte[1024];

        int len = -1;
        try
        {

            while (this.commHandler.available() > 0)
            {
                len = this.commHandler.read(buffer);
                for (int i = 0; i < len; i++)
                {
                    tempData.add(AnimasUtils.getUnsignedShort(buffer[i]));
                }
            }

            return tempData;
        }
        catch (Exception e)
        {
            LOG.error("Error reading from device. Exception: " + e, e);
            throw new PlugInBaseException(PlugInExceptionType.CommunicationError, new Object[] { e.getMessage() });
        }
    }


    protected byte[] readDataFromDeviceInternal() throws PlugInBaseException
    {
        return this.commHandler.readAvailableData();

        // // List<Short> tempData = new ArrayList<Short>();
        //
        // byte[] outBuffer = null;
        // byte[] buffer = null;
        //
        // // int len = -1;
        // try
        // {
        // int available = 0;
        // while ((available = this.commHandler.available()) > 0)
        // {
        // buffer = new byte[available];
        // this.commHandler.read(buffer);
        //
        // outBuffer = concat(outBuffer, buffer);
        //
        // // for (int i = 0; i < len; i++)
        // // {
        // // tempData.add(AnimasUtils.getUnsignedShort(buffer[i]));
        // // }
        // }
        //
        // return outBuffer;
        // }
        // catch (Exception e)
        // {
        // LOG.error("Error reading from device. Exception: " + e, e);
        // throw new PlugInBaseException(PlugInExceptionType.CommunicationError,
        // new Object[] { e.getMessage() });
        // }
    }


    public byte[] concat(byte[] a, byte[] b)
    {
        if ((a == null) || (a.length == 0))
        {
            return b;
        }

        int aLen = a.length;
        int bLen = b.length;
        byte[] c = new byte[aLen + bLen];
        System.arraycopy(a, 0, c, 0, aLen);
        System.arraycopy(b, 0, c, aLen, bLen);
        return c;
    }


    protected boolean isDataAvailable()
    {
        return this.commHandler.isDataAvailable();
    }


    protected void sendToDevice(short c, boolean debug) throws PlugInBaseException
    {
        try
        {
            this.commHandler.write(c);
            if (debug)
            {
                LOG.debug("sendToDevice: " + c + " [" + (short) c + "]");
            }
        }
        catch (Exception ex)
        {
            throw new PlugInBaseException(PlugInExceptionType.CommunicationPortClosed);
        }
    }


    protected void sendMessageToDevice(char[] msgChars) throws PlugInBaseException
    {
        sendToDevice(START_MESSAGE_DEVICE);

        for (char character : msgChars)
        {
            sendToDevice((short) character);
        }

        calculateAndSendFletcher(msgChars);

        sendToDevice(END_MESSAGE_DEVICE);

        AnimasUtils.debugHexData(this.debugCommunication, msgChars, msgChars.length, "Sending: [%s]", LOG);
    }


    public void sendCharacterToDevice(char c) throws PlugInBaseException
    {
        try
        {
            this.commHandler.write(c);
        }
        catch (Exception ex)
        {
            throw new PlugInBaseException(PlugInExceptionType.CommunicationPortClosed);
        }
    }


    protected void sendToDevice(short c) throws PlugInBaseException
    {
        sendToDevice(c, false);
    }


    private void calculateAndSendFletcher(char[] msgChars) throws PlugInBaseException
    {
        short[] fletch = AnimasUtils.calculateFletcher16(msgChars, msgChars.length);

        for (short sh : fletch)
        {
            if (isSpecialCharacter(sh))
            {
                sendCharacterToDevice('}');
                sh = (short) (sh ^ 0x20);
            }

            sendToDevice(sh);
        }
    }


    private boolean isSpecialCharacter(short character)
    {
        return ((character == CTL_MESSAGE_DEVICE) || //
                (character == START_MESSAGE_DEVICE) || //
        (character == END_MESSAGE_DEVICE));
    }

}
