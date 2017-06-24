package ggc.meter.device;

import java.io.EOFException;
import java.util.List;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.atech.utils.data.ATechDate;
import com.atech.utils.data.ATechDateType;

import ggc.core.data.ExtendedDailyValueType;
import ggc.core.data.defs.GlucoseUnitType;
import ggc.meter.data.GlucoseMeterMarkerDto;
import ggc.meter.data.MeterValuesEntry;
import ggc.meter.defs.device.MeterDeviceDefinition;
import ggc.meter.util.DataAccessMeter;
import ggc.plugin.comm.NRSerialCommunicationHandler;
import ggc.plugin.comm.cfg.SerialSettings;
import ggc.plugin.data.enums.PlugInExceptionType;
import ggc.plugin.device.DeviceIdentification;
import ggc.plugin.device.PlugInBaseException;
import ggc.plugin.output.OutputWriter;

/**
 * This is abstract class for serial meters, that works with new CommunicationHandler
 * framework (MeterInterface2).
 */
public abstract class SerialMeterAbstract
{

    private static final Logger LOG = LoggerFactory.getLogger(SerialMeterAbstract.class);

    public static final char STX = '\002';
    public static final char ETX = '\003';
    public static final char EOT = '\004';
    public static final char ENQ = '\005';
    public static final char ACK = '\006';
    public static final char ETB = '\027';
    public static final char LF = '\n';
    public static final char CR = '\r';
    public static final char NAK = '\025';
    public static final char CAN = '\030';

    protected DataAccessMeter dataAccess;
    protected OutputWriter outputWriter;
    protected NRSerialCommunicationHandler communicationHandler;
    protected String portName;
    protected boolean active = false;
    protected String serialNumber;
    protected MeterDeviceDefinition deviceDefinition;
    protected GlucoseUnitType glucoseUnitType = GlucoseUnitType.mg_dL;
    private int receiveTimeout;


    public SerialMeterAbstract(DataAccessMeter dataAccess, OutputWriter outputWriter, String portName,
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


    public void closeDevice()
    {
        this.communicationHandler.disconnectDevice();
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


    protected char readChar() throws PlugInBaseException
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
                throw new PlugInBaseException("Serial port timeout on " + this.portName);
            }

            throw new PlugInBaseException(PlugInExceptionType.TimeoutReadingData);
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
        if (this.communicationHandler != null)
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


    protected void writeDeviceIdentification(String serialNumber)
    {
        DeviceIdentification deviceIdentification = new DeviceIdentification(dataAccess.getI18nControlInstanceBase());
        deviceIdentification.setDeviceDefinition(this.deviceDefinition);
        deviceIdentification.device_serial_number = serialNumber;
        this.serialNumber = serialNumber;

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


    public void writeUrineData(Float value, GlucoseUnitType unitType, ATechDate adt)
    {
        if (value == null)
            return;

        MeterValuesEntry mve = new MeterValuesEntry();

        mve.setDateTimeObject(adt);

        float urineMmoL = value;

        if (unitType == GlucoseUnitType.mg_dL)
        {
            urineMmoL = dataAccess.getBGValueByType(GlucoseUnitType.mg_dL, GlucoseUnitType.mmol_L, value);
        }

        mve.setUrine(ExtendedDailyValueType.Urine_mmolL, dataAccess.getFormatedValue(urineMmoL, 1));

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


    public ATechDate getATechDate(int year, int month, int day, int hour, int minute)
    {
        return new ATechDate(day, month, year, hour, minute, ATechDateType.DateAndTimeMin);
    }


    protected void read(int[] data) throws PlugInBaseException
    {
        communicationHandler.read(data);
    }


    public void readUntilCharacterReceived(char character) throws PlugInBaseException
    {
        char c;

        while ((c = readChar()) != character)
            ;
    }


    public void readUntilACKReceived() throws PlugInBaseException
    {
        readUntilCharacterReceived(ACK);
    }

}
