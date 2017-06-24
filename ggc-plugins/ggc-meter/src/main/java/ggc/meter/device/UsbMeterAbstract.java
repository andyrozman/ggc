package ggc.meter.device;

import java.io.EOFException;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.atech.utils.data.ATechDate;
import com.atech.utils.data.ATechDateType;

import ggc.core.data.defs.GlucoseUnitType;
import ggc.meter.data.GlucoseMeterMarkerDto;
import ggc.meter.data.MeterValuesEntry;
import ggc.meter.defs.device.MeterDeviceDefinition;
import ggc.meter.util.DataAccessMeter;
import ggc.plugin.comm.Hid4JavaCommunicationHandler;
import ggc.plugin.comm.cfg.USBDevice;
import ggc.plugin.device.DeviceIdentification;
import ggc.plugin.device.PlugInBaseException;
import ggc.plugin.device.v2.DeviceDefinition;
import ggc.plugin.output.OutputWriter;

// TODO
/**
 * This is abstract class for Usb meters, that works with new framework (MeterInterface2).
 */
public abstract class UsbMeterAbstract
{

    private static final Logger LOG = LoggerFactory.getLogger(UsbMeterAbstract.class);

    protected DataAccessMeter dataAccess;
    protected OutputWriter outputWriter;
    protected Hid4JavaCommunicationHandler communicationHandler;
    protected boolean active = false;
    protected String serialNumber;
    protected MeterDeviceDefinition deviceDefinition;
    protected GlucoseUnitType glucoseUnitType;
    private int receiveTimeout;
    protected List<USBDevice> allowedDevicesList;


    public UsbMeterAbstract(DataAccessMeter dataAccess, OutputWriter outputWriter,
            MeterDeviceDefinition deviceDefinition)
    {
        this.dataAccess = dataAccess;
        this.outputWriter = outputWriter;
        this.deviceDefinition = deviceDefinition;
        createAllowedDevicesList();
    }


    /**
     * Creates list of Allowed USB devices for this implementation.
     */
    protected abstract void createAllowedDevicesList();


    public void readData() throws PlugInBaseException
    {
        // checkIfDevicePresentOnConfiguredPort();
        connectDevice();
        preInitDevice();
        readDeviceData();
    }


    private void connectDevice() throws PlugInBaseException
    {
        communicationHandler = new Hid4JavaCommunicationHandler();
        communicationHandler.setAllowedDevices(allowedDevicesList);
        communicationHandler.setDelayForTimedReading(100);
        communicationHandler.connectAndInitDevice();
        this.active = true;
    }


    public void closeDevice() throws PlugInBaseException
    {
        communicationHandler.disconnectDevice();
    }


    protected abstract void preInitDevice() throws PlugInBaseException;


    private void checkIfDevicePresentOnConfiguredPort() throws PlugInBaseException
    {
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
            throw new EOFException("Serial port timeout on " + this.communicationHandler.getSelectedDevice());
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
                throw new EOFException("Serial port timeout on " + this.communicationHandler.getSelectedDevice());
            }

            throw new InterruptedException();
        }
        return (char) rv;
    }


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
    }


    public int writeWithReturn(byte[] buffer) throws PlugInBaseException
    {
        return this.communicationHandler.writeWithReturn(buffer);
    }


    protected void disconnectDevice()
    {
        this.communicationHandler.disconnectDevice();
        this.active = false;
    }


    protected void write(byte[] buf) throws PlugInBaseException
    {
        this.communicationHandler.write(buf);
    }


    protected void write(String message) throws PlugInBaseException
    {
        this.communicationHandler.write(message.getBytes());
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


    protected void read(byte[] data) throws PlugInBaseException
    {
        communicationHandler.read(data);
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
        return allowedDevicesList;
    }


    protected void writeDeviceIdentification(String serialNumber)
    {
        DeviceIdentification deviceIdentification = new DeviceIdentification(dataAccess.getI18nControlInstanceBase());
        deviceIdentification.setDeviceDefinition(this.deviceDefinition);
        deviceIdentification.device_serial_number = serialNumber;
        this.serialNumber = serialNumber;

        outputWriter.setDeviceIdentification(deviceIdentification);
        outputWriter.writeDeviceIdentification();
    }


    public void writeBGData(float bg, ATechDate aTechDate)
    {
        MeterValuesEntry mve = new MeterValuesEntry();
        mve.setBgValue(bg, glucoseUnitType);
        mve.setDateTimeObject(aTechDate);

        this.outputWriter.writeData(mve);
    }


    public void writeBGData(float bg, GlucoseUnitType unitType, ATechDate aTechDate)
    {
        MeterValuesEntry mve = new MeterValuesEntry();
        mve.setBgValue(bg, unitType);
        mve.setDateTimeObject(aTechDate);

        this.outputWriter.writeData(mve);
    }


    public void writeBGData(float bg, GlucoseUnitType unitType, ATechDate aTechDate,
            List<GlucoseMeterMarkerDto> markers)
    {
        MeterValuesEntry mve = new MeterValuesEntry();
        mve.setBgValue(bg, unitType);
        mve.setDateTimeObject(aTechDate);

        for (GlucoseMeterMarkerDto marker : markers)
            mve.addGlucoseMeterMarker(marker);

        this.outputWriter.writeData(mve);
    }


    public void writeBGData(float bg, int year, int month, int day, int hour, int minute,
            List<GlucoseMeterMarkerDto> markers)
    {
        MeterValuesEntry mve = new MeterValuesEntry();
        mve.setBgValue(bg, glucoseUnitType);
        mve.setDateTimeObject(getATechDate(year, month, day, hour, minute));

        for (GlucoseMeterMarkerDto marker : markers)
            mve.addGlucoseMeterMarker(marker);

        this.outputWriter.writeData(mve);
    }

}
