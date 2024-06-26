package ggc.plugin.comm;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.atech.utils.data.BitUtils;

import ggc.plugin.comm.cfg.SerialSettings;
import ggc.plugin.comm.cfg.SerialSettingsType;
import ggc.plugin.data.enums.PlugInExceptionType;
import ggc.plugin.device.PlugInBaseException;
import gnu.io.NRSerialPort;
import gnu.io.SerialPort;

/**
 * Created by andy on 11.03.15.
 */
public class NRSerialCommunicationHandler extends SerialCommunicationAbstract
{

    private static final Logger LOG = LoggerFactory.getLogger(NRSerialCommunicationHandler.class);

    protected NRSerialPort serialDevice;
    SerialSettings serialSettings;
    String portName;
    boolean deviceConnected;
    private InputStream inputStream;
    private OutputStream outputStream;
    private BitUtils bitUtils = new BitUtils();
    // RXTXPort serialPortRaw;
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
        this.serialSettings = settings;
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

        LOG.debug("{}" + serialSettings.toString());

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


    public void activateBlockingReads()
    {
        try
        {
            // this.serialPortRaw.enableReceiveThreshold(1);
            // this.serialPortRaw.disableReceiveTimeout();

            // this.serialPortRaw.s;

            this.serialPortRaw.setFlowControlMode(SerialPort.FLOWCONTROL_RTSCTS_IN | SerialPort.FLOWCONTROL_RTSCTS_OUT);

            this.serialPortRaw.notifyOnCTS(true);
            // this.serialPortRaw.notifyOnCD(true);
            this.serialPortRaw.notifyOnDSR(true);

            // this.serialPortRaw.notifyOnCTS(true);
            // this.serialPortRaw.notifyOnCD(true);
            // this.serialPortRaw.notifyOnDSR(true);

            // this.serialPortRaw.staticSetDSR(portName, true);
            // this.serialPortRaw.staticS.staticSetCD(portName, true);

            // this.serialPortRaw.sendEvent(4, true);

            LOG.debug("Is CD: {}", this.serialPortRaw.isCD());
            LOG.debug("Is CTS: {}", this.serialPortRaw.isCTS());
            LOG.debug("Is DSR: {}", this.serialPortRaw.isDSR());

            // this.serialPortRaw.

        }
        catch (Exception ex)
        {
            LOG.error("Problem on activateBlockingReads: " + ex);
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


    public void sleepMs(int time)
    {
        try
        {
            Thread.sleep(time);
        }
        catch (InterruptedException e)
        {
            // e.printStackTrace();
        }
    }


    public byte[] readAvailableData() throws PlugInBaseException
    {
        byte[] outBuffer = null;
        byte[] buffer = null;

        // int len = -1;

        do
        {
            sleepMs(5);
        } while (available() == 0);

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


    public int[] readAvailableDataInt() throws PlugInBaseException
    {
        byte[] data = readAvailableData();

        int[] dataOut = new int[data.length];

        for (int i = 0; i < data.length; i++)
        {
            int o = data[i];

            if (o < 0)
                o += 256;

            dataOut[i] = o;
        }

        return dataOut;

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
                // System.out.println("OS: " + this.outputStream);
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
        catch (Exception e)
        {
            e.printStackTrace();
            throw new PlugInBaseException(e);
        }
    }


    public static Set<String> getAvailablePorts() throws PlugInBaseException
    {
        try
        {
            return NRSerialPort.getAvailableSerialPorts();
        }
        catch (Exception ex)
        {
            throw new PlugInBaseException(PlugInExceptionType.InterfaceProblem,
                    new Object[] { "Problem getting available Serial Ports." }, ex);
        }
    }


    public void write(byte toWrite) throws PlugInBaseException
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


    public int read(int[] buffer) throws PlugInBaseException
    {
        byte[] byteArray = new byte[buffer.length];

        int x = read(byteArray);

        convertByteArrayToIntArray(byteArray, buffer);

        return x;
    }


    public void flush() throws PlugInBaseException
    {
        try
        {
            outputStream.flush();
        }
        catch (IOException e)
        {
            throw new PlugInBaseException(e);
        }
    }

}
