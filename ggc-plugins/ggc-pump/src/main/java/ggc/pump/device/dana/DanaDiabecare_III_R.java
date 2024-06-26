package ggc.pump.device.dana;

import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Hashtable;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.atech.utils.data.ATechDate;
import com.atech.utils.data.HexUtils;

import ggc.plugin.data.DeviceValueConfigEntry;
import ggc.plugin.data.DeviceValuesWriter;
import ggc.plugin.device.DeviceIdentification;
import ggc.plugin.device.DownloadSupportType;
import ggc.plugin.device.PlugInBaseException;
import ggc.plugin.manager.DeviceImplementationStatus;
import ggc.plugin.manager.company.AbstractDeviceCompany;
import ggc.plugin.output.AbstractOutputWriter;
import ggc.plugin.output.OutputWriter;
import ggc.plugin.protocol.SerialProtocol;
import ggc.plugin.util.DataAccessPlugInBase;
import ggc.pump.data.PumpValuesEntryProfile;
import ggc.pump.data.PumpWriterValues;
import ggc.pump.data.defs.*;
import ggc.pump.data.profile.ProfileSubPattern;
import ggc.pump.device.AbstractBlueToothPump;
import ggc.pump.device.dana.impl.data.defs.DanaDataType;
import ggc.pump.manager.PumpDevicesIds;
import ggc.pump.manager.company.Sooil;
import ggc.pump.util.DataAccessPump;
import gnu.io.SerialPort;
import gnu.io.SerialPortEvent;

/**
 * Application: GGC - GNU Gluco Control Plug-in: Pump Tool (support for Pump
 * devices)
 *
 * See AUTHORS for copyright information.
 *
 * This program is free software; you can redistribute it and/or modify it under
 * the terms of the GNU General Public License as published by the Free Software
 * Foundation; either version 2 of the License, or (at your option) any later
 * version.
 *
 * This program is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU General Public License for more
 * details.
 *
 * You should have received a copy of the GNU General Public License along with
 * this program; if not, write to the Free Software Foundation, Inc., 59 Temple
 * Place, Suite 330, Boston, MA 02111-1307 USA
 *
 * Filename: DanaDiabecare_III Description: Dana Diabecare R/III device
 * implementation
 *
 * Author: Andy {andy@atech-software.com}
 */

public class DanaDiabecare_III_R extends AbstractBlueToothPump
{

    HexUtils hex_utils = new HexUtils();
    private static final Logger LOG = LoggerFactory.getLogger(DanaDiabecare_III_R.class);

    private boolean device_communicating = true;
    int entries_current = 0;
    int entries_max = 100;
    byte[] check_response = { 0x7e, 0x7e, (byte) 0xf2 };
    int error_count = 0;
    int glucose_mode = 1;
    int current_basal_profile = 1;
    boolean config_mode = false;


    /**
     * Constructor
     */
    public DanaDiabecare_III_R()
    {
        super();
        loadPumpSpecificValues();
    }


    /**
     * Constructor
     *
     * @param cmp
     */
    public DanaDiabecare_III_R(AbstractDeviceCompany cmp, DataAccessPlugInBase da)
    {
        super(cmp, da);
        // loadPumpSpecificValues();
    }


    /**
     * Constructor
     *
     * @param params
     * @param writer
     */
    public DanaDiabecare_III_R(String params, OutputWriter writer)
    {
        this(params, writer, DataAccessPump.getInstance());
        // super(); //DataAccessPump.getInstance());
    }


    /**
     * Constructor
     *
     * @param params
     * @param writer
     * @param da
     */
    public DanaDiabecare_III_R(String params, OutputWriter writer, DataAccessPlugInBase da)
    {
        super(params, writer, da);

        // communcation settings for this meter(s)
        this.setCommunicationSettings(19200, SerialPort.DATABITS_8, SerialPort.STOPBITS_1, SerialPort.PARITY_NONE,
            SerialPort.FLOWCONTROL_NONE,
            SerialProtocol.SERIAL_EVENT_BREAK_INTERRUPT | SerialProtocol.SERIAL_EVENT_OUTPUT_EMPTY);

        loadPumpSpecificValues();

        // this.i18nControl =
        // DataAccessPump.getInstance().getI18nControlInstance();

        this.error_count = 0;

        // output writer, this is how data is returned (for testing new devices,
        // we can use Consol
        this.outputWriter = writer;
        // this.outputWriter.getOutputUtil().setMaxMemoryRecords(this.getMaxMemoryRecords());

        // set meter type (this will be deprecated in future, but it's needed
        // for now
        this.setPumpType("Sooil (Dana)", this.getName());

        // set device company (needed for now, will also be deprecated)
        this.setDeviceCompany(new Sooil(da));

        // settting serial port in com library
        try
        {
            this.setSerialPort(params);

            if (!open())
            {
                setDeviceStopped();
                return;
            }

            this.outputWriter.writeHeader();

        }
        catch (javax.comm.NoSuchPortException ex)
        {
            LOG.error("Port [" + params + "] not found");
            setDeviceStopped();
        }
        catch (Exception ex)
        {
            LOG.error("Exception on create:" + ex, ex);
            this.setDeviceStopped();
        }

    }


    /**
     * getName - Get Name of device
     *
     * @return name of device
     */
    public String getName()
    {
        return "Diabcare II R (III)";
    }


    /**
     * getIconName - Get Icon of device
     *
     * @return icon name
     */
    public String getIconName()
    {
        return "so_danaIII.jpg";
    }


    /**
     * getDeviceId - Get Device Id, within MgrCompany class Should be
     * implemented by device class.
     *
     * @return id of device within company
     */
    public int getDeviceId()
    {
        return PumpDevicesIds.PUMP_DANA_DIABECARE_III_R;
    }


    /**
     * getInstructions - get instructions for device Should be implemented by
     * meter class.
     *
     * @return instructions for reading data
     */
    public String getInstructions()
    {
        return "INSTRUCTIONS_DANA_III_R";
    }


    /**
     * getComment - Get Comment for device
     *
     * @return comment or null
     */
    public String getComment()
    {
        return null;
    }


    /**
     * getImplementationStatus - Get Implementation Status
     *
     * @return implementation status as number
     * @see ggc.plugin.manager.DeviceImplementationStatus
     */
    public DeviceImplementationStatus getImplementationStatus()
    {
        return DeviceImplementationStatus.Done;
    }


    /**
     * getDeviceClassName - Get Class name of device implementation, used by
     * Reflection at later time
     *
     * @return class name as string
     */
    public String getDeviceClassName()
    {
        return "ggc.pump.device.dana.DanaDiabecare_III_R";
    }


    /**
     * Get Max Memory Records
     *
     * @return
     */
    public int getMaxMemoryRecords()
    {
        return -1;
    }


    private void connect() throws Exception
    {
        try
        {
            this.writeData(DanaDataType.Connect.getCommand());
            waitTime(1000);
            this.readData(new byte[0x100]);
            waitTime(200);
        }
        catch (Exception ex)
        {
            LOG.error("Error on connect. Ex: " + ex, ex);
            throw ex;
        }
    }


    private void disconnect() throws Exception
    {
        try
        {
            this.writeData(DanaDataType.Disconnect.getCommand());
            waitTime(1000);
            this.readData(new byte[0x100]);
            waitTime(200);
        }
        catch (Exception ex)
        {
            LOG.error("Error on disconnect. Ex: " + ex, ex);
            throw ex;
        }
    }

    Hashtable<String, String> dtypes = new Hashtable<String, String>();

    ATechDate atd_1 = new ATechDate(ATechDate.FORMAT_DATE_AND_TIME_S, 0);
    byte old_record_code = -1;
    int old_record_value = -1;


    /**
     * Gets the device record.
     *
     * @param command
     * @param write
     *
     *
     * @throws Exception
     *             the exception
     */
    public void getDeviceRecord(DanaDataType dataType, boolean write) throws Exception
    {
        try
        {
            boolean flag;

            LOG.debug("getDeviceRecord (" + dataType.getCommand()[1] + "/" + dataType.getDescription() + "):Start");

            byte[] buffer = new byte[0x200];
            int num2 = 0;

            this.writeData(dataType.getCommand());
            waitTime(1000);

            flag = true;

            while (flag)
            {

                try
                {
                    num2 = (this.readData(buffer) - 2) / 10;
                }
                catch (Exception exception1)
                {
                    LOG.error("getDeviceRecord. Ex: " + exception1.getMessage(), exception1);
                    LOG.debug("getDeviceRecord(" + dataType.getDescription() + "):End");
                    flag = false;
                    break;
                }

                if (num2 == 0)
                {
                    flag = false;
                    break;
                }

                hex_utils.readByteArray(buffer);

                if (!this.checkIfValid(buffer))
                {
                    this.incrementError();
                    // return;
                }

                ATechDate atd = getDateTime(hex_utils.getByteFromArray(7), hex_utils.getByteFromArray(8), // m
                    hex_utils.getByteFromArray(9), // d
                    hex_utils.getByteFromArray(10), // h
                    hex_utils.getByteFromArray(11), // m
                    hex_utils.getByteFromArray(12) // s
                );

                byte record_code = hex_utils.getByteFromArray(13);
                int record_value = hex_utils.getIntFromArray(14);

                if (atd.equals(this.atd_1) && (old_record_code == record_code) && (old_record_value == record_value))
                {
                    break;
                }
                else
                {
                    this.atd_1 = atd;
                    this.old_record_code = record_code;
                    this.old_record_value = record_value;
                }

                String key = hex_utils.getByteFromArray(6) + "_" + record_code;

                if (write)
                {
                    if (!writeData(key, atd, DanaDataType.getByCode(hex_utils.getByteFromArray(6)), record_value))
                    {
                        System.out.println(
                            "date: " + atd.getDateTimeString() + ", code: " + record_code + ", value=" + record_value); // +",
                                                                                                                        // value2="
                                                                                                                        // +
                                                                                                                        // record_value2);
                    }
                }

                waitTime(num2 * 100);
            }

        }
        catch (Exception ex)
        {
            throw new Exception("getDeviceRecord (" + dataType.getDescription() + ") Exception: " + ex.getMessage());
        }
        finally
        {
            LOG.debug("getDeviceRecord(" + dataType.getDescription() + "):End");
        }

    }


    private boolean writeData(String key, ATechDate atd, DanaDataType type, int value)
    {
        String v = "";

        if ((type == DanaDataType.Bolus) || (type == DanaDataType.DailyInsulin) || (type == DanaDataType.Prime))
        {
            v = DataAccessPlugInBase.Decimal1Format.format(value / 100.0f);
        }
        else if (type == DanaDataType.Alarms)
        {
            return this.getWriter().writeObject(key, atd, getCorrectCode(CODE_TYPE_ALARM, value), null);
        }
        else if (type == DanaDataType.Errors)
        {
            return this.getWriter().writeObject(key, atd, getCorrectCode(CODE_TYPE_ERROR, value), null);
        }
        else
        {
            v = "" + value;
        }

        return this.getWriter().writeObject(key, atd, v);

    }

    int current_year;


    private int getYear(int year)
    {
        year += 2000;

        if (year > current_year)
        {
            return current_year;
        }
        else
        {
            return year;
        }
    }


    private ATechDate getDateTime(byte y, byte m, byte d, byte h, byte min, byte s)
    {
        GregorianCalendar gc;
        try
        {
            gc = new GregorianCalendar(getYear(y), m - 1, d, h, min, s);
        }
        catch (Exception ex)
        {
            gc = new GregorianCalendar(2000, 0, 1, 0, 0, 0);
        }

        return new ATechDate(ATechDate.FORMAT_DATE_AND_TIME_S, gc);

    }


    /**
     * open
     */
    @Override
    public boolean open()
    {

        try
        {
            byte[] buffer = new byte[0x100];

            if (!super.open())
            {
                return false;
            }

            waitTime(2000); // wait for device to settle

            this.readData(buffer);
            waitTime(200);

            this.readData(buffer);
            waitTime(200);

            this.readData(buffer);
            waitTime(200);

            waitTime(1000);

            // PacketStreamReader reader = new PacketStreamReader(buffer);

            // byte num4 = reader.getCommand();
            // byte num5 = reader.getSubCommand();
            if (!(buffer[4] == (byte) 3) && (buffer[5] == (byte) 3))
            {
                throw new Exception("Port (" + this.port_name + ") is not for DANA Diabecare R");
            }

            this.readLine();
            waitTime(200);

            return true;
        }
        catch (Exception ex)
        {
            LOG.error("Exception on open: " + ex, ex);
            this.setDeviceStopped();
            return false;
        }
    }


    /**
     * Read data.
     *
     * @param buffer
     *            the buffer
     * @return the int
     * @throws Exception
     *             the exception
     */
    public int readData(byte[] buffer) throws Exception
    {
        int num = 0;
        try
        {
            if (this.read(buffer, 0, 4) == 0)
            {}

            num = buffer[2] - 1;
            this.read(buffer, 4, num + 4);
            int v = this.createCRC(buffer, 3, num + 1);
            byte num4 = (byte) ((v >> 8) & 0xff);
            byte num5 = (byte) (v & 0xff);
            if ((buffer[4 + num] != num4) || (buffer[4 + num + 1] != num5))
            {}

        }
        catch (Exception ex)
        {
            LOG.error("readData(). Exception: " + ex, ex);
        }
        return num;
    }

    DeviceValuesWriter dvw = null;


    private DeviceValuesWriter getWriter()
    {

        if (dvw == null)
        {
            createDeviceValuesWriter();
        }

        return dvw;

    }


    private void writeData(byte[] buffer) throws Exception
    {
        this.writeData(buffer, 0, buffer.length);
    }


    private void writeData(byte[] buffer, int offset, int length) throws Exception
    {
        byte[] destinationArray = new byte[length + 8];
        try
        {
            // logger.debug("writeData:Start");
            destinationArray[0] = 0x7e;
            destinationArray[1] = 0x7e;
            destinationArray[2] = (byte) ((length + 1) & 0xff);
            // logger.debug("Send length: " + length);
            destinationArray[3] = (byte) 0xf1;
            System.arraycopy(buffer, offset, destinationArray, 4, length);
            int v = this.createCRC(buffer, offset, length);
            destinationArray[4 + length] = (byte) ((v >> 8) & 0xff);
            destinationArray[5 + length] = (byte) (v & 0xff);
            // logger.debug("CRC: " + DanaUtil.toHexString(v));
            destinationArray[6 + length] = 0x2e;
            destinationArray[7 + length] = 0x2e;
            this.write(destinationArray, 0, destinationArray.length);
            // logger.debug("writeData:End");
        }
        catch (Exception exception)
        {
            throw exception;
        }
    }


    /**
     * serialEvent
     *
     * @param event
     */
    public void serialEvent(SerialPortEvent event)
    {
        // not used
    }


    /**
     * Get Download Support Type
     *
     * @return
     */
    public DownloadSupportType getDownloadSupportType()
    {
        return DownloadSupportType.Download_Data_Config;
    }


    /**
     * Get Download Support Type for Configuration
     *
     * @return
     */
    /*
     * public int getDownloadSupportTypeConfiguration() { return
     * DownloadSupportType.DOWNLOAD_YES; }
     */

    /**
     * How Many Months Of Data Stored
     *
     * @return
     */
    public int howManyMonthsOfDataStored()
    {
        return -1;
    }


    /**
     * Get Temporary Basal Type Definition "TYPE=Unit;STEP=0.1"
     * "TYPE=Procent;STEP=10;MIN=0;MAX=200"
     * "TYPE=Both;STEP_UNIT=0.1;STEP=10;MIN=0;MAX=200"
     *
     * @return
     */
    public String getTemporaryBasalTypeDefinition()
    {
        // return "TYPE=Unit;STEP=0.1";
        return null;
    }


    /**
     * Get Bolus Step (precission)
     *
     * @return
     */
    public float getBolusStep()
    {
        return 0.1f;
    }


    /**
     * Get Basal Step (precission)
     *
     * @return
     */
    public float getBasalStep()
    {
        return 0.1f;
    }


    /**
     * Are Pump Settings Set (Bolus step, Basal step and TBR settings)
     *
     * @return
     */
    public boolean arePumpSettingsSet()
    {
        return false;
    }


    /**
     * Map pump specific alarms to PumpTool specific alarm codes
     *
     * @return
     */
    public Hashtable<String, PumpAlarms> getAlarmMappings()
    {
        return alarm_map;
    }


    /**
     * Get Error Mappings - Map pump specific errors to Pump Tool specific event
     * codes
     *
     * @return
     */
    public Hashtable<String, PumpErrors> getErrorMappings()
    {
        return this.error_map;
    }

    Hashtable<String, PumpErrors> error_map;
    Hashtable<String, PumpAlarms> alarm_map;


    /**
     * loadPumpSpecificValues - should be called from constructor of any
     * AbstractPump classes and should create, AlarmMappings and EventMappings
     * and any other pump constants.
     */
    public void loadPumpSpecificValues()
    {
        // FIXME
        error_map = new Hashtable<String, PumpErrors>();
        error_map.put("17030", PumpErrors.BatteryDepleted);
        error_map.put("26410", PumpErrors.CartridgeEmpty);

        alarm_map = new Hashtable<String, PumpAlarms>();
        alarm_map.put("17030", PumpAlarms.BatteryLow);
        alarm_map.put("26410", PumpAlarms.CartridgeLow);
    }


    /**
     * hasSpecialProgressStatus - in most cases we read data directly from
     * device, in this case we have normal progress status, but with some
     * special devices we calculate progress through other means.
     *
     * @return true is progress status is special
     */
    @Override
    public boolean hasSpecialProgressStatus()
    {
        return true;
    }


    /**
     * This is method for reading configuration
     *
     * @throws PlugInBaseException
     */
    @Override
    public void readConfiguration() throws PlugInBaseException
    {
        try
        {
            config_mode = true;

            this.open();

            if (!this.device_communicating)
            {
                return;
            }

            this.connect();

            getDeviceConfiguration(DanaDataType.SettingsGeneral);
            this.readingEntryStatus(25);
            getDeviceConfiguration(DanaDataType.SettingsMaxValues);
            this.readingEntryStatus(50);
            getDeviceConfiguration(DanaDataType.SettingsGlucomode);
            this.readingEntryStatus(75);
            getDeviceConfiguration(DanaDataType.SettingsBolusHelper);
            this.readingEntryStatus(100);

            this.outputWriter.setStatus(AbstractOutputWriter.STATUS_DOWNLOAD_FINISHED);
            this.outputWriter.endOutput();
        }
        catch (Exception ex)
        {
            LOG.error("ReadConfiguration(). Exception: " + ex, ex);
        }
        finally
        {
            try
            {
                // if (this.device_communicating)
                {
                    disconnect();
                    this.close();
                }
            }
            catch (Exception exx)
            {
                LOG.error("readDeviceDataFull.disconnect(): " + exx, exx);
            }

            config_mode = false;
        }

    }


    /**
     * Read Device Data Full
     */
    public void readDeviceDataFull() throws PlugInBaseException
    {
        this.current_year = new GregorianCalendar().get(Calendar.YEAR);

        if (!this.device_communicating)
        {
            return;
        }

        try
        {

            this.open();

            if (!this.device_communicating)
            {
                return;
            }

            this.connect();

            this.readingEntryStatus(2);
            this.getDeviceRecord(DanaDataType.Bolus, false);
            this.readingEntryStatus(4);
            this.getDeviceRecord(DanaDataType.Bolus, false);
            this.readingEntryStatus(6);

            waitTime(1000);
            this.readLine();

            this.readInfo();

            if (this.device_communicating)
            {
                this.getDeviceRecord(DanaDataType.Bolus, true);
                this.readingEntryStatus(20);
                this.getDeviceRecord(DanaDataType.DailyInsulin, true);
                this.readingEntryStatus(30);
                this.getDeviceRecord(DanaDataType.Prime, true);
                this.readingEntryStatus(40);
                this.getDeviceRecord(DanaDataType.Glucose, true);
                this.readingEntryStatus(50);
                this.getDeviceRecord(DanaDataType.Alarms, true);
                this.readingEntryStatus(60);
                this.getDeviceRecord(DanaDataType.Errors, true);
                this.readingEntryStatus(70);
                this.getDeviceRecord(DanaDataType.Carbohydrates, true);
                this.readingEntryStatus(80);

                readProfiles();
                this.readingEntryStatus(100);

                this.outputWriter.setStatus(AbstractOutputWriter.STATUS_DOWNLOAD_FINISHED);

            }
        }
        catch (Exception ex)
        {
            LOG.error("readDeviceDataFull: " + ex, ex);
            this.setDeviceStopped();
        }
        finally
        {
            try
            {
                // if (this.device_communicating)
                {
                    disconnect();
                    this.close();
                }
            }
            catch (Exception exx)
            {
                LOG.error("readDeviceDataFull.disconnect(): " + exx, exx);
            }

        }

    }


    /**
     * This is method for reading partitial data from device. All reading from
     * actual device should be done from here. Reading can be done directly
     * here, or event can be used to read data.
     */
    @Override
    public void readDeviceDataPartitial() throws PlugInBaseException
    {
    }


    /**
     * This is for reading device information. This should be used only if
     * normal dump doesn't retrieve this information (most dumps do).
     *
     * @throws PlugInBaseException
     */
    @Override
    public void readInfo() throws PlugInBaseException
    {

        try
        {

            DeviceIdentification di = this.outputWriter.getDeviceIdentification();

            byte[] buffer = new byte[0x200];
            LOG.debug("readInfo() - Start");

            this.readingEntryStatus(7);

            this.writeData(DanaDataType.Shipping.getCommand()); // 50, 7
            waitTime(1000);

            this.readData(buffer);
            // this.hex_utils.showByteArrayHex(buffer);
            this.hex_utils.readByteArray(buffer);

            if (!checkIfValid(buffer))
            {
                // setDeviceStopped();
                this.incrementError();
                return;
            }

            String sn = this.hex_utils.getStringFromArray(6, 10);

            di.deviceSerialNumber = sn;

            this.readingEntryStatus(9);

            this.outputWriter.writeDeviceIdentification();

            waitTime(200);
        }
        catch (Exception ex)
        {
            LOG.error("readInfo(). Ex: " + ex, ex);
        }
        finally
        {
            LOG.debug("readInfo() - End");
        }

    }


    private boolean checkIfValid(byte[] arr)
    {
        if ((arr[0] == check_response[0]) && (arr[1] == check_response[1]) && (arr[3] == check_response[2]))
        {
            return true;
        }
        else
        {
            return false;
        }
    }


    private void incrementError()
    {
        this.error_count++;
        this.outputWriter
                .setSubStatus(String.format(i18nControlAbstract.getMessage("ERROR_COUNT"), "" + this.error_count));
    }


    private void readProfiles()
    {

        try
        {
            byte[] buffer = new byte[0x100];
            // int num = 0;
            LOG.debug("getProfiles() :Start");

            getDeviceConfiguration(DanaDataType.SettingsGeneral);

            this.writeData(DanaDataType.SettingsBasalPattern.getCommand());
            waitTime(200);

            this.readData(buffer);

            if (!checkIfValid(buffer))
            {
                this.incrementError();
                // this.setDeviceStopped();
                return;
            }

            this.hex_utils.readByteArray(buffer);

            // float[] all_data = new float[24];

            PumpValuesEntryProfile pvep = new PumpValuesEntryProfile();
            pvep.setName("" + this.current_basal_profile);

            ATechDate atd = new ATechDate(ATechDate.FORMAT_DATE_AND_TIME_S, new GregorianCalendar());
            pvep.setActiveFrom(atd.getATDateTimeAsLong());
            pvep.setActiveTill(atd.getATDateTimeAsLong());

            for (int i = 0; i < 24; i++)
            {
                ProfileSubPattern pse = new ProfileSubPattern();
                pse.timeStart = i * 100;
                pse.timeEnd = pse.timeStart + 59;
                pse.amount = this.hex_utils.getIntFromArray(6 + (i * 2)) / 100.0f;

                // all_data[i] = (this.hex_utils.getIntFromArray(6 + (i*2)) /
                // 100.0f);
                // System.out.println(i + ":00 = " + pse.amount);

                pvep.addProfileSubEntry(pse);
            }
            pvep.endEntry();

            this.outputWriter.writeData(pvep);

            waitTime(200);
        }
        catch (Exception ex2)
        {
            LOG.error("getProfiles(). Ex: " + ex2.getMessage());
            this.setDeviceStopped();
        }
        finally
        {
            LOG.debug("getProfiles() :End");
        }

    }


    private void readingEntryStatus(int cur_ent)
    {
        // this.entries_current = cur_ent;
        // float proc_read = ((this.entries_current*1.0f) / this.entries_max);
        // float proc_total = 6 + (94 * proc_read);
        // this.outputWriter.setSpecialProgress((int)proc_total);
        // //.setSubStatus(subStatus)
        this.outputWriter.setSpecialProgress(cur_ent); // .setSubStatus(subStatus)

    }


    private void setDeviceStopped()
    {
        this.device_communicating = false;
        // System.out.println("Device not communicating");
        this.outputWriter.setStatus(AbstractOutputWriter.STATUS_STOPPED_DEVICE);
    }


    private void createDeviceValuesWriter()
    {
        this.dvw = new DeviceValuesWriter();
        this.dvw.setOutputWriter(this.outputWriter);

        // added isNumeric, could cause problem

        // bolus - standard
        this.dvw.put("1_66",
            new PumpWriterValues(PumpWriterValues.OBJECT_BASE, PumpBaseType.Bolus, PumpBolusType.Normal, true));

        // bolus - wave (this is unhandled, data is not all available)
        this.dvw.put("1_69",
            new PumpWriterValues(PumpWriterValues.OBJECT_BASE, PumpBaseType.Bolus, PumpBolusType.Multiwave, false));

        // daily insulin record
        this.dvw.put("2_68",
            new PumpWriterValues(PumpWriterValues.OBJECT_BASE, PumpBaseType.Report, PumpReport.InsulinTotalDay, true));

        // CH (carbohydrates)
        this.dvw.put("8_82",
            new PumpWriterValues(PumpWriterValues.OBJECT_EXT, PumpAdditionalDataType.Carbohydrates, 0, true));
        // prime
        this.dvw.put("3_80", new PumpWriterValues(PumpWriterValues.OBJECT_BASE, PumpBaseType.Event,
                PumpEventType.PrimeInfusionSet, false));

        // BG
        this.dvw.put("6_71",
            new PumpWriterValues(PumpWriterValues.OBJECT_EXT, PumpAdditionalDataType.BloodGlucose, 0, true));

        // alarm
        this.dvw.put("5_66",
            new PumpWriterValues(PumpWriterValues.OBJECT_BASE, PumpBaseType.Alarm.getCode(), 0, false));

        // error
        this.dvw.put("4_2", new PumpWriterValues(PumpWriterValues.OBJECT_BASE, PumpBaseType.Error.getCode(), 0, false));

    }

    /**
     * Code Type: Alarm
     */
    public static final int CODE_TYPE_ALARM = 1;

    /**
     * Code Type: Error
     */
    public static final int CODE_TYPE_ERROR = 2;


    private int getCorrectCode(int type, int code)
    {
        if (type == CODE_TYPE_ALARM)
        {
            // s("CodeTypeAlarm" + code);
            if (this.alarm_map.containsKey("" + code))
            {
                // s("CodeTypeAlarm: found. Code: " + code);
                return this.alarm_map.get("" + code).getCode();
            }
            else
            {
                LOG.info("DanaDiabecare_III_R: Unknown Alarm [type=" + type + ", code=" + code + "]");
                return PumpAlarms.UnknownAlarm.getCode();
            }
        }
        else
        {
            if (this.error_map.containsKey("" + code))
            {
                // s("CodeTypeAlarm: found. Code: " + code);
                return this.error_map.get("" + code).getCode();
            }
            else
            {
                LOG.info("DanaDiabecare_III_R: Unknown Error [type=" + type + ", code=" + code + "]");
                return PumpErrors.UnknownError.getCode();
            }

        }

    }


    private int crc16(byte data, int crc)
    {
        int num = 0;
        num = ((crc >> 8) & 0xff) | (crc << 8);
        num ^= data & 0xff;
        num ^= ((num & 0xff) >> 4) & 0xfff;
        num ^= num << 8 << 4;
        return num ^ (((num & 0xff) << 5) | ((((num & 0xff) >> 3) & 0x1fff) << 8));
    }


    /**
     * Creates the crc.
     *
     * @param data
     *            the data
     * @param offset
     *            the offset
     * @param length
     *            the length
     *
     * @return the int
     */
    public int createCRC(byte[] data, int offset, int length)
    {
        int crc = 0;
        for (int i = 0; i < length; i++)
        {
            crc = crc16(data[offset + i], crc);
        }
        return crc;
    }


    private void getDeviceConfiguration(DanaDataType cmd /* byte[] cmd */) // throws
                                                                           // Exception
    {

        String param = "";

        try
        {
            byte[] buffer = new byte[0x100];
            LOG.debug("getDeviceInfo(" + cmd.name() + "):Start");

            this.writeData(cmd.getCommand());
            // waitTime(200);

            this.readData(buffer);

            hex_utils.readByteArray(buffer);

            if (!checkIfValid(buffer))
            {
                this.incrementError();
                return;
            }

            if (cmd == DanaDataType.SettingsGeneral)
            {
                writeConfiguration("PCFG_BASAL_INCREMENT", "" + (hex_utils.getByteFromArray(6) / 100.0f),
                    PumpConfigurationGroup.Basal); // PumpConfigurationOld.PUMP_CONFIG_GROUP_INSULIN);
                writeConfiguration("PCFG_BOLUS_INCREMENT", "" + (hex_utils.getByteFromArray(7) / 100.0f),
                    PumpConfigurationGroup.Bolus); // PumpConfigurationOld.PUMP_CONFIG_GROUP_INSULIN);
                writeConfiguration("PCFG_BOLUS_PRESET", "" + getTrueOrFalse(hex_utils.getByteFromArray(8)),
                    PumpConfigurationGroup.Bolus); // PumpConfigurationOld.PUMP_CONFIG_GROUP_INSULIN);
                writeConfiguration("PCFG_BOLUS_ALARM", "" + hex_utils.getByteFromArray(9),
                    PumpConfigurationGroup.Bolus); // PumpConfigurationOld.PUMP_CONFIG_GROUP_INSULIN);
                writeConfiguration("PCFG_BOLUS_BLOCK", "" + hex_utils.getByteFromArray(10),
                    PumpConfigurationGroup.Bolus); // PumpConfigurationOld.PUMP_CONFIG_GROUP_INSULIN);

                if (hex_utils.getByteFromArray(11) == 0)
                {
                    writeConfiguration("PCFG_BASAL_UNIT", i18nControlAbstract.getMessage("UNIT_PER_HOUR"),
                        PumpConfigurationGroup.Basal); // PumpConfigurationOld.PUMP_CONFIG_GROUP_INSULIN);
                }
                else
                {
                    writeConfiguration("PCFG_BASAL_UNIT", i18nControlAbstract.getMessage("UNIT_PER_DAY"),
                        PumpConfigurationGroup.Basal); // PumpConfigurationOld.PUMP_CONFIG_GROUP_INSULIN);
                }

                // PROFILE = 12
                this.current_basal_profile = hex_utils.getByteFromArray(12) + 1;

            }
            else if (cmd == DanaDataType.SettingsMaxValues)
            {
                writeConfiguration("PCFG_MAX_BOLUS", "" + (hex_utils.getIntFromArray(6) / 100.0f),
                    PumpConfigurationGroup.Insulin); // PumpConfigurationOld.PUMP_CONFIG_GROUP_INSULIN);
                writeConfiguration("PCFG_MAX_BASAL", "" + (hex_utils.getIntFromArray(8) / 100.0f),
                    PumpConfigurationGroup.Insulin); // PumpConfigurationOld.PUMP_CONFIG_GROUP_INSULIN);
                writeConfiguration("PCFG_MAX_DAILY", "" + (hex_utils.getIntFromArray(10) / 100.0f),
                    PumpConfigurationGroup.Insulin); // PumpConfigurationOld.PUMP_CONFIG_GROUP_INSULIN);
            }
            else if (cmd == DanaDataType.SettingsGlucomode)
            {
                if (hex_utils.getByteFromArray(6) == 1)
                {
                    writeConfiguration("CFG_BASE_GLUCOSE_UNIT", i18nControlAbstract.getMessage("GLUCOSE_UNIT_MMOLL"),
                        PumpConfigurationGroup.General); // PumpConfigurationOld.PUMP_CONFIG_GROUP_BLOOD_GLUCOSE);
                }
                else
                {
                    writeConfiguration("CFG_BASE_GLUCOSE_UNIT", i18nControlAbstract.getMessage("GLUCOSE_UNIT_MGDL"),
                        PumpConfigurationGroup.General); // PumpConfigurationOld.PUMP_CONFIG_GROUP_BLOOD_GLUCOSE);
                }

                writeConfiguration("PCFG_EASY_MODE", "" + getTrueOrFalse(hex_utils.getByteFromArray(8)),
                    PumpConfigurationGroup.General); // PumpConfigurationOld.PUMP_CONFIG_GROUP_GENERAL);
            }
            else if (cmd == DanaDataType.SettingsBolusHelper)
            {

                if (glucose_mode == 0)
                {
                    writeConfiguration("PCFG_CH_INS_RATIO",
                        "" + DataAccessPlugInBase.Decimal0Format.format(hex_utils.getIntFromArray(6)),
                        PumpConfigurationGroup.Bolus); // PumpConfigurationOld.PUMP_CONFIG_GROUP_BOLUS_HELPER);
                    writeConfiguration("PCFG_BG_INS_RATIO",
                        "" + DataAccessPlugInBase.Decimal0Format.format(hex_utils.getIntFromArray(8)),
                        PumpConfigurationGroup.Bolus); // PumpConfigurationOld.PUMP_CONFIG_GROUP_BOLUS_HELPER);
                    writeConfiguration("PCFG_TARGET_BG",
                        "" + DataAccessPlugInBase.Decimal0Format.format(hex_utils.getIntFromArray(12)),
                        PumpConfigurationGroup.Bolus); // PumpConfigurationOld.PUMP_CONFIG_GROUP_BOLUS_HELPER);

                }
                else
                {
                    writeConfiguration("PCFG_CH_INS_RATIO",
                        "" + DataAccessPlugInBase.Decimal0Format.format(hex_utils.getIntFromArray(6) / 100.0f),
                        PumpConfigurationGroup.Bolus); // PumpConfigurationOld.PUMP_CONFIG_GROUP_BOLUS_HELPER);
                    writeConfiguration("PCFG_BG_INS_RATIO",
                        "" + DataAccessPlugInBase.Decimal0Format.format(hex_utils.getIntFromArray(8) / 100.0f),
                        PumpConfigurationGroup.Bolus); // PumpConfigurationOld.PUMP_CONFIG_GROUP_BOLUS_HELPER);
                    writeConfiguration("PCFG_TARGET_BG",
                        "" + DataAccessPlugInBase.Decimal0Format.format(hex_utils.getIntFromArray(12) / 100.0f),
                        PumpConfigurationGroup.Bolus); // PumpConfigurationOld.PUMP_CONFIG_GROUP_BOLUS_HELPER);
                }

                writeConfiguration("PCFG_ACTIVE_INSULIN_RATE",
                    "" + DataAccessPlugInBase.Decimal0Format.format(hex_utils.getIntFromArray(10) / 100.0f),
                    PumpConfigurationGroup.Insulin); // .PUMP_CONFIG_GROUP_BOLUS_HELPER);
                writeConfiguration("PCFG_ACTIVE_INSULIN_DECREMENT_RATIO", "" + (hex_utils.getByteFromArray(14) * 5),
                    PumpConfigurationGroup.Insulin); // .PUMP_CONFIG_GROUP_BOLUS_HELPER

            }

            waitTime(200);
            LOG.debug("getDeviceInfo(" + param + "):End");
        }
        catch (Exception ex)
        {
            LOG.error("getDeviceConfiguration(). Exception: " + ex, ex);
        }
    }


    private void writeConfiguration(String key, String value, PumpConfigurationGroup config_group)
    {
        if (!config_mode)
        {
            return;
        }

        DeviceValueConfigEntry pvec = new DeviceValueConfigEntry(i18nControlAbstract.getMessage(key), value,
                config_group);
        this.outputWriter.writeConfigurationData(pvec);

        // System.out.println(i18nControl.getMessage(key) + " = " + value);
    }


    private String getTrueOrFalse(byte val)
    {
        if (val == 0)
        {
            return i18nControlAbstract.getMessage("FALSE");
        }
        else
        {
            return i18nControlAbstract.getMessage("TRUE");
        }
    }

}
