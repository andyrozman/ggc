package ggc.plugin.comm;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Set;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.atech.utils.data.BitUtils;

import ggc.plugin.data.enums.PlugInExceptionType;
import ggc.plugin.device.PlugInBaseException;
import gnu.io.NRSerialPort;
import gnu.io.SerialPort;
import gnu.io.UnsupportedCommOperationException;

/**
 * Created by andy on 11.03.15.
 */
public class NRSerialCommunicationHandler implements SerialCommunicationInterface
{

    private static final Log LOG = LogFactory.getLog(NRSerialCommunicationHandler.class);

    protected NRSerialPort serialDevice;
    SerialSettings serialSettings;
    String portName;
    boolean deviceConnected;
    private InputStream inputStream;
    private OutputStream outputStream;
    private BitUtils bitUtils = new BitUtils();
    SerialPort serialPortRaw;


    public NRSerialCommunicationHandler(String portName)
    {
        this(portName, null);
    }


    public NRSerialCommunicationHandler(String portName, SerialSettings serialSettings)
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
        if ((this.serialDevice != null) && (this.serialDevice.isConnected()))
        {
            return true;
        }

        if (this.serialSettings == null)
        {
            this.serialSettings = createDefaultSerialSettings();
        }

        LOG.debug(serialSettings);

        this.serialDevice = new NRSerialPort(portName, this.serialSettings.baudRate);

        this.deviceConnected = this.serialDevice.connect();

        LOG.debug("Connect: " + this.deviceConnected);

        if (!this.deviceConnected)
        {
            LOG.debug("Could not connect to port " + portName);
            return false;
        }

        try
        {
            serialPortRaw = this.serialDevice.getSerialPortInstance();
            serialPortRaw.setDTR(this.serialSettings.dtr);
            serialPortRaw.setRTS(this.serialSettings.rts);

            serialPortRaw.setSerialPortParams(this.serialSettings.baudRate,
                getIntegerValue(this.serialSettings.dataBits), getIntegerValue(this.serialSettings.stopBits),
                getIntegerValue(this.serialSettings.parity));

            serialPortRaw.setFlowControlMode(getIntegerValue(this.serialSettings.flowControl));

            inputStream = this.serialDevice.getInputStream();
            outputStream = this.serialDevice.getOutputStream();
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

        return (this.serialDevice != null && this.serialDevice.isConnected());
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
        try
        {
            if (this.inputStream != null)
                this.inputStream.close();
        }
        catch (IOException e)
        {
            LOG.warn("error closing input stream");

        }

        try
        {
            if (this.outputStream != null)
                this.outputStream.close();
        }
        catch (IOException e)
        {
            LOG.warn("error closing output stream");
        }

        if (serialDevice != null)
        {

            if (this.serialDevice.isConnected())
            {

                this.serialDevice.disconnect();
                this.serialPortRaw.close();

                this.serialDevice = null;
            }
        }

        this.deviceConnected = false;
    }


    public boolean isDataAvailable()
    {
        try
        {
            return inputStream.available() > 0;
        }
        catch (IOException e)
        {
            return false;
        }
    }


    public int available() throws PlugInBaseException
    {
        try
        {

            return inputStream.available();

        }
        catch (IOException ex)
        {
            throw new PlugInBaseException(ex);
        }
    }


    public int read() throws PlugInBaseException
    {
        try
        {
            return inputStream.read();
        }
        catch (IOException ex)
        {
            throw new PlugInBaseException(ex);
        }
    }


    public int read(byte[] buffer) throws PlugInBaseException
    {
        try
        {
            return inputStream.read(buffer);
        }
        catch (IOException ex)
        {
            throw new PlugInBaseException(ex);
        }
    }


    public int read(byte[] b, int off, int len) throws PlugInBaseException
    {
        // fixme
        return 0;
    }


    public void write(int toWrite) throws PlugInBaseException
    {
        try
        {
            outputStream.write(toWrite);
        }
        catch (IOException ex)
        {
            throw new PlugInBaseException(ex);
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
            outputStream.write(buffer);
        }
        catch (IOException ex)
        {
            throw new PlugInBaseException(ex);
        }
    }


    public void write(byte[] b, int off, int len) throws PlugInBaseException
    {
        // fixme
    }


    public byte[] readLineBytes() throws PlugInBaseException
    {
        // fixme
        return new byte[0];
    }


    public String readLine() throws PlugInBaseException
    {
        // fixme
        return null;
    }


    public int readByteTimed() throws PlugInBaseException
    {
        // fixme
        return 0;
    }


    public void setDelayForTimedReading(int ms)
    {
        // fixme

    }


    public void write(int[] cmd) throws PlugInBaseException
    {

        try
        {
            for (int bb : cmd)
            {
                System.out.println("OS: " + this.outputStream);
                this.outputStream.write(bb);
            }
        }
        catch (IOException ex)
        {
            ex.printStackTrace();
            throw new PlugInBaseException(ex);
        }

    }


    public int getReceiveTimeout()
    {
        return serialPortRaw.getReceiveTimeout();
    }


    public void setReceiveTimeout(int timeout) throws PlugInBaseException
    {
        try
        {
            serialPortRaw.enableReceiveTimeout(timeout);
        }
        catch (UnsupportedCommOperationException e)
        {
            e.printStackTrace();
            throw new PlugInBaseException(e);
        }
    }


    public static Set<String> getAvailablePorts()
    {
        return NRSerialPort.getAvailableSerialPorts();
    }

}
