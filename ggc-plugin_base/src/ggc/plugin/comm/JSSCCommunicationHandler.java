package ggc.plugin.comm;

import java.util.Set;

import jssc.SerialPort;
import jssc.SerialPortException;
import jssc.SerialPortTimeoutException;

import org.apache.commons.lang.NotImplementedException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.atech.utils.data.BitUtils;

import ggc.plugin.comm.cfg.SerialSettings;
import ggc.plugin.comm.cfg.SerialSettingsType;
import ggc.plugin.data.enums.PlugInExceptionType;
import ggc.plugin.device.PlugInBaseException;
import gnu.io.NRSerialPort;

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
 *  Filename:      JSSCCommunicationHandler
 *  Description:   Java Serial API (not used, just for testing)
 *
 *  Author: Andy {andy@atech-software.com}
 */

/**
 * This is handler for JSSC. We don't use it, but it was considered as replacement. It will stay
 * here for now. But don't use unless you have a real reason. You should use NRSerialJavaHandler
 * (this is official Serial API for GGC for now (since 2014)).
 */
public class JSSCCommunicationHandler extends SerialCommunicationAbstract
{

    private static final Logger LOG = LoggerFactory.getLogger(JSSCCommunicationHandler.class);

    protected SerialPort serialDevice;
    protected SerialSettings serialSettings;
    String portName;
    boolean deviceConnected;
    private BitUtils bitUtils = new BitUtils();

    int delay = 60000;


    public JSSCCommunicationHandler(String portName)
    {
        this(portName, null);
    }


    public JSSCCommunicationHandler(String portName, SerialSettings serialSettings)
    {
        this.portName = portName;
        this.serialSettings = serialSettings;
    }


    public void setSerialSettings(SerialSettings settings)
    {
        this.serialSettings = serialSettings;
    }


    public SerialSettings createDefaultSerialSettings()
    {
        return new SerialSettings();
    }


    // FIXME we need to use SerialSettings when creating device
    public boolean connectAndInitDevice()
    {
        if ((this.serialDevice != null) && (this.serialDevice.isOpened()))
        {
            return true;
        }

        if (this.serialSettings == null)
        {
            this.serialSettings = createDefaultSerialSettings();
        }

        LOG.debug("{}" + serialSettings.toString());

        this.serialDevice = new SerialPort(portName);

        // NRSerialPort(portName, this.serialSettings.baudRate);
        // this.serialDevice.setParams()

        try
        {

            this.deviceConnected = this.serialDevice.openPort(); // .connect();

            LOG.debug("Connect: " + this.deviceConnected);

            if (!this.deviceConnected)
            {
                LOG.debug("Could not connect to port " + portName);
                return false;
            }

            // serialPortRaw = this.serialDevice.getSerialPortInstance();
            // serialPortRaw.setDTR(this.serialSettings.dtr);
            // serialPortRaw.setRTS(this.serialSettings.rts);

            serialDevice.setParams(this.serialSettings.baudRate, getIntegerValue(this.serialSettings.dataBits),
                getIntegerValue(this.serialSettings.stopBits), getIntegerValue(this.serialSettings.parity));

            serialDevice.setFlowControlMode(getIntegerValue(this.serialSettings.flowControl));
            // serialPortRaw.setFlowControlMode();

            // inputStream = this.serialDevice.et.getInputStream();
            // outputStream = this.serialDevice.getOutputStream();
        }
        catch (Exception ex)
        {
            LOG.error("Error setting streams: " + ex, ex);
            this.serialDevice = null;
            return false;
        }
        return true;
    }


    public boolean isDeviceConnected()
    {
        return (this.serialDevice != null && this.serialDevice.isOpened());
    }


    public Integer getIntegerValue(SerialSettingsType type)
    {
        switch (type)
        {
            case StopBits1:
                return SerialPort.STOPBITS_1;

            case StopBits1_5:
                return SerialPort.STOPBITS_1_5;

            case StopBits2:
                return SerialPort.STOPBITS_2;

            case ParityNone:
                return SerialPort.PARITY_NONE;

            case ParityOdd:
                return SerialPort.PARITY_ODD;

            case ParityEven:
                return SerialPort.STOPBITS_1;

            case ParityMark:
                return SerialPort.PARITY_MARK;

            case ParitySpace:
                return SerialPort.PARITY_SPACE;

            case DataBits5:
                return SerialPort.DATABITS_5;

            case DataBits6:
                return SerialPort.DATABITS_6;

            case DataBits7:
                return SerialPort.DATABITS_7;

            case DataBits8:
                return SerialPort.DATABITS_8;

            case FlowControlNone:
                return SerialPort.FLOWCONTROL_NONE;

            case FlowControlRtsCtsIn:
                return SerialPort.FLOWCONTROL_RTSCTS_IN;

            case FlowControlRtsCtsOut:
                return SerialPort.FLOWCONTROL_RTSCTS_OUT;

            case FlowControlXonXoffIn:
                return SerialPort.FLOWCONTROL_XONXOFF_IN;

            case FlowControlXonXoffOut:
                return SerialPort.FLOWCONTROL_XONXOFF_OUT;

            default:
                return 0;
        }
    }


    public void disconnectDevice()
    {

        if (serialDevice != null)
        {

            if (this.serialDevice.isOpened())
            {
                try
                {
                    this.serialDevice.closePort();
                }
                catch (SerialPortException e)
                {
                    LOG.error("Error on closing port: {}", e.getMessage(), e);
                }

                this.serialDevice = null;
            }
        }

        this.deviceConnected = false;
    }


    public boolean isDataAvailable()
    {
        try
        {
            return (this.serialDevice.getInputBufferBytesCount() > 0);
        }
        catch (SerialPortException e)
        {
            LOG.error("Error on isDataAvailable: {}", e.getMessage(), e);
            return false;
        }
    }


    public int available() throws PlugInBaseException
    {
        try
        {
            return this.serialDevice.getInputBufferBytesCount();
        }
        catch (SerialPortException e)
        {
            LOG.error("Error on available: {}", e.getMessage(), e);
            throw new PlugInBaseException(e);
        }

    }


    public int read() throws PlugInBaseException
    {
        try
        {
            // this.serialDevice.readIntArray(1);
            int[] ret = this.serialDevice.readIntArray(1, delay);

            LOG.debug("read(): {}", bitUtils.getHex(ret));

            return ret[0];
        }
        catch (SerialPortException e)
        {
            LOG.error("Error on read(int): {}", e.getMessage(), e);
            throw new PlugInBaseException(e);
        }
        catch (SerialPortTimeoutException e)
        {
            LOG.debug("Problem: delay");
            // LOG.error("Error on read(int): {}", e.getMessage(), e);
            // throw new PlugInBaseException(e);
            return -1;
        }
    }


    @Deprecated
    public int read(byte[] buffer) throws PlugInBaseException
    {
        throw new NotImplementedException();
    }


    public int[] readIntArray() throws PlugInBaseException
    {
        try
        {
            int[] ret = this.serialDevice.readIntArray();
            return ret;
        }
        catch (SerialPortException e)
        {
            LOG.error("Error on readIntArray(int): {}", e.getMessage(), e);
            throw new PlugInBaseException(e);
        }
    }


    public void write(byte toWrite) throws PlugInBaseException
    {
        try
        {
            this.serialDevice.writeByte(toWrite);
        }
        catch (SerialPortException e)
        {
            LOG.error("Error on write(int): {}", e.getMessage(), e);
        }
    }


    public void write(int toWrite) throws PlugInBaseException
    {
        try
        {
            this.serialDevice.writeInt(toWrite);
        }
        catch (SerialPortException e)
        {
            LOG.error("Error on write(int): {}", e.getMessage(), e);
        }
    }


    public byte[] readAvailableData() throws PlugInBaseException
    {
        byte[] outBuffer = null;
        byte[] buffer = null;

        // int len = -1;
        try
        {
            int available = 0;
            while ((available = available()) > 0)
            {
                buffer = new byte[available];
                read(buffer);

                outBuffer = bitUtils.concat(outBuffer, buffer);
            }

            return outBuffer;
        }
        catch (Exception e)
        {
            LOG.error("Error reading from device. Exception: " + e, e);
            throw new PlugInBaseException(PlugInExceptionType.CommunicationError, new Object[] { e.getMessage() });
        }
    }


    public void write(byte[] buffer) throws PlugInBaseException
    {
        try
        {
            this.serialDevice.writeBytes(buffer);
        }
        catch (SerialPortException e)
        {
            LOG.error("Error on write(byte[]):  {}", e.getMessage(), e);
        }

    }


    @Deprecated
    public void write(byte[] b, int off, int len) throws PlugInBaseException
    {
    }


    @Deprecated
    public byte[] readLineBytes() throws PlugInBaseException
    {
        return new byte[0];
    }


    @Deprecated
    public String readLine() throws PlugInBaseException
    {
        return null;
    }


    @Deprecated
    public int readByteTimed() throws PlugInBaseException
    {
        return 0;
    }


    @Deprecated
    public void setDelayForTimedReading(int ms)
    {

    }


    public void write(int[] cmd) throws PlugInBaseException
    {
        try
        {
            this.serialDevice.writeIntArray(cmd);
        }
        catch (SerialPortException e)
        {
            LOG.error("Error on write(int[]): {}", e.getMessage(), e);
            throw new PlugInBaseException(e);
        }

    }


    @Deprecated
    public int getReceiveTimeout()
    {
        return -1;
        // return serialPortRaw.getReceiveTimeout();
    }


    @Deprecated
    public void setReceiveTimeout(int timeout) throws PlugInBaseException
    {

        try
        {
            // serialPortRaw.enableReceiveTimeout(timeout);
        }
        catch (Exception e)
        {
            e.printStackTrace();
            throw new PlugInBaseException(e);
        }
    }


    public static Set<String> getAvailablePorts() throws Exception
    {
        return NRSerialPort.getAvailableSerialPorts();
    }


    public int read(int[] buffer) throws PlugInBaseException
    {
        try
        {
            int buf[] = serialDevice.readIntArray(buffer.length);

            for (int i = 0; i < buf.length; i++)
            {
                buffer[i] = buf[i];
            }

            return buf.length;

        }
        catch (SerialPortException e)
        {
            throw new PlugInBaseException(e);
        }

    }


    public void activate()
    {
        // try
        // {
        // // this.serialDevice.setDTR()
        //
        // //LOG.debug("CTS: {}", this.serialDevice.isCTS());
        // //LOG.debug("DSR: {}", this.serialDevice.isDSR());
        // }
        // catch (SerialPortException e)
        // {
        // e.printStackTrace();
        // }
    }

}
