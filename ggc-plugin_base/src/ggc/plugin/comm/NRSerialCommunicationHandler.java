package ggc.plugin.comm;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.atech.utils.data.BitUtils;

import ggc.plugin.data.enums.PlugInExceptionType;
import ggc.plugin.device.PlugInBaseException;
import gnu.io.NRSerialPort;
import gnu.io.SerialPort;

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
        // 9600, 8,n,1, no flow control
        SerialSettings ss = new SerialSettings();
        ss.baudrate = 9600;
        ss.databits = 8;
        ss.parity = 0; // none
        ss.stopbits = 1;

        return null;
    }


    // FIXME we need to use SerialSettings when creating device
    public boolean connectAndInitDevice()
    {
        if ((this.serialDevice != null) && (this.serialDevice.isConnected()))
        {
            return true;
        }

        this.serialDevice = new NRSerialPort(portName, 9600); // 9600 , -
                                                              // 115200

        this.deviceConnected = this.serialDevice.connect();

        LOG.debug("Connect: " + this.deviceConnected);

        if (!this.deviceConnected)
        {
            LOG.debug("Could not connect to port " + portName);

            return false;
        }

        try
        {
            SerialPort sport = this.serialDevice.getSerialPortInstance();
            sport.setDTR(false);
            sport.setRTS(true);

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


    public void disconnectDevice()
    {
        if (serialDevice != null)
        {
            if (this.serialDevice.isConnected())
            {
                this.serialDevice.disconnect();
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

}
