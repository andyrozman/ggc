package ggc.meter.device;

import java.io.EOFException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.atech.utils.data.ATechDate;
import com.atech.utils.data.ATechDateType;

import ggc.core.data.defs.GlucoseUnitType;
import ggc.meter.data.MeterValuesEntry;
import ggc.meter.data.defs.MeterDeviceDefinition;
import ggc.meter.util.DataAccessMeter;
import ggc.plugin.comm.NRSerialCommunicationHandler;
import ggc.plugin.comm.cfg.SerialSettings;
import ggc.plugin.comm.cfg.USBDevice;
import ggc.plugin.data.GGCPlugInFileReaderContext;
import ggc.plugin.data.enums.PlugInExceptionType;
import ggc.plugin.device.DeviceIdentification;
import ggc.plugin.device.DownloadSupportType;
import ggc.plugin.device.PlugInBaseException;
import ggc.plugin.device.v2.DeviceDefinition;
import ggc.plugin.output.OutputWriter;

/**
 * This is abstract class for Usb meters, that works with new framework (MeterInterface2).
 */
public abstract class UsbMeterAbstract
{

    private static final Logger LOG = LoggerFactory.getLogger(SerialMeterAbstract.class);

    protected DataAccessMeter dataAccess;
    protected OutputWriter outputWriter;
    protected NRSerialCommunicationHandler communicationHandler;
    protected String portName;
    protected boolean active = false;
    protected String serialNumber;
    protected MeterDeviceDefinition deviceDefinition;
    protected GlucoseUnitType glucoseUnitType;
    private int receiveTimeout;


    public UsbMeterAbstract(DataAccessMeter dataAccess, OutputWriter outputWriter,
            MeterDeviceDefinition deviceDefinition)
    {
        this.dataAccess = dataAccess;
        this.outputWriter = outputWriter;
        this.portName = portName;
        this.deviceDefinition = deviceDefinition;
    }


    public void readData() throws PlugInBaseException
    {
        checkIfDevicePresentOnConfiguredPort();
        connectDevice();
        preInitDevice();
        readDeviceData();
    }


    private void connectDevice()
    {
        communicationHandler = new NRSerialCommunicationHandler(portName, getSerialSettings());
        communicationHandler.connectAndInitDevice();
        this.active = true;
    }


    protected abstract void preInitDevice() throws PlugInBaseException;


    private void checkIfDevicePresentOnConfiguredPort() throws PlugInBaseException
    {
        Set<String> availablePorts = NRSerialCommunicationHandler.getAvailablePorts();

        boolean found = false;

        for (String port : availablePorts)
        {
            if (port.equalsIgnoreCase(portName))
            {
                found = true;
                break;
            }
        }

        if (!found)
        {
            throw new PlugInBaseException(PlugInExceptionType.DeviceNotFoundOnConfiguredPort);
            // if (availablePorts.size() > 0)
            // {
            //
            // }
            // else
            // {
            // throw new
            // PlugInBaseException(PlugInExceptionType.DeviceNotFoundOnConfiguredPort);
            // }
        }
    }


    public abstract void readDeviceData() throws PlugInBaseException;


    protected void setReceiveTimeout(int timeout) throws PlugInBaseException
    {
        this.receiveTimeout = timeout;
        this.communicationHandler.setReceiveTimeout(timeout);
    }


    protected byte readByte() throws Exception
    {
        int rv = -1;
        long startMs = System.currentTimeMillis();

        do
        {
            rv = this.communicationHandler.read();

            if (rv != -1)
            {
                return (byte) rv;
            }

            if ((System.currentTimeMillis() - startMs) > this.receiveTimeout)
            {
                break;
            }

        } while (rv == -1);

        if (this.active)
        {
            throw new EOFException("Serial port timeout on " + this.portName);
        }

        return -1;
    }


    protected char readChar() throws Exception
    {
        int rv = -1;

        try
        {
            rv = this.communicationHandler.read();
        }
        catch (PlugInBaseException ex)
        {
            throw ex;
        }

        if (rv == -1)
        {
            if (this.active)
            {
                throw new EOFException("Serial port timeout on " + this.portName);
            }

            throw new InterruptedException();
        }
        return (char) rv;
    }


    public abstract SerialSettings getSerialSettings();


    public String right(String s, int size)
    {
        if (s == null)
            return null;
        if (s.length() <= size)
        {
            return s;
        }
        return s.substring(s.length() - size);
    }


    public int unsignedByteToInt(byte b)
    {
        return b & 0xFF;
    }


    public String unsignedByteToIntAsHexString(byte data)
    {
        return Integer.toHexString(this.unsignedByteToInt(data));
    }


    protected void write(int[] buf) throws PlugInBaseException
    {
        this.communicationHandler.write(buf);
        this.communicationHandler.flush();
    }


    protected void disconnectDevice()
    {
        this.communicationHandler.disconnectDevice();
        this.active = false;
    }


    protected void write(byte[] buf) throws PlugInBaseException
    {
        this.communicationHandler.write(buf);
        this.communicationHandler.flush();
    }


    protected void writeDeviceIdentification()
    {
        DeviceIdentification deviceIdentification = new DeviceIdentification(dataAccess.getI18nControlInstanceBase());
        deviceIdentification.setDeviceDefinition(this.deviceDefinition);
        deviceIdentification.device_serial_number = serialNumber;

        outputWriter.setDeviceIdentification(deviceIdentification);
        outputWriter.writeDeviceIdentification();
    }


    public void setGlucoseUnitType(GlucoseUnitType glucoseUnitType)
    {
        this.glucoseUnitType = glucoseUnitType;
    }


    public void writeBGData(float bg, int year, int month, int day, int hour, int minute)
    {
        MeterValuesEntry mve = new MeterValuesEntry();
        mve.setBgValue(bg, glucoseUnitType);
        mve.setDateTimeObject(getATechDate(year, month, day, hour, minute));

        this.outputWriter.writeData(mve);
    }


    public ATechDate getATechDate(int year, int month, int day, int hour, int minute)
    {
        return new ATechDate(day, month, year, hour, minute, ATechDateType.DateAndTimeMin);
    }


    protected void read(int[] data) throws PlugInBaseException
    {
        communicationHandler.read(data);
    }


    public void readConfiguration(DeviceDefinition definition, Object connectionParameters, OutputWriter outputWriter)
            throws PlugInBaseException
    {
    }


    public List<USBDevice> getAllowedDevicesList()
    {
        List<USBDevice> usbDeviceList = new ArrayList<USBDevice>();
        usbDeviceList.add(new USBDevice("Contour USB", 0x1a79, 0x6002));
        usbDeviceList.add(new USBDevice("Contour USB", 0x1a79, 0x7390)); // ?
        usbDeviceList.add(new USBDevice("Contour Next", 0x1a79, 0x7350));
        usbDeviceList.add(new USBDevice("Contour Next USB", 0x1a79, 0x7410));
        usbDeviceList.add(new USBDevice("Contour Next Link", 0x1a79, 0x6300));
        usbDeviceList.add(new USBDevice("Contour Next Link", 0x1a79, 0x6200)); // ?

        return usbDeviceList;
    }


    public List<GGCPlugInFileReaderContext> getFileDownloadContexts(DownloadSupportType downloadSupportType)
    {
        return null;
    }

}
