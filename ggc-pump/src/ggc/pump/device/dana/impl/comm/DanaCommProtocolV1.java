package ggc.pump.device.dana.impl.comm;

import com.atech.utils.data.ATechDate;
import com.atech.utils.data.HexUtils;
import ggc.plugin.data.DeviceValueConfigEntry;
import ggc.plugin.data.DeviceValuesWriter;
import ggc.plugin.device.DeviceIdentification;
import ggc.plugin.device.PlugInBaseException;
import ggc.plugin.output.AbstractOutputWriter;
import ggc.plugin.output.OutputWriter;
import ggc.plugin.protocol.SerialProtocol;
import ggc.plugin.protocol.reader.AbstractDeviceReader;
import ggc.plugin.util.DataAccessPlugInBase;
import ggc.pump.data.PumpValuesEntryProfile;
import ggc.pump.data.PumpWriterValues;
import ggc.pump.data.defs.*;
import ggc.pump.data.profile.ProfileSubPattern;
import ggc.pump.device.dana.impl.data.defs.DanaDataType;
import gnu.io.SerialPort;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Hashtable;

/**
 * Created by andy on 11.03.15.
 */
public class DanaCommProtocolV1 extends DanaCommProtocolAbstract
{
    private static final Log LOG = LogFactory.getLog(DanaCommProtocolV1.class);

    HexUtils hex_utils = new HexUtils();

    private boolean device_communicating = true;
    int entries_current = 0;
    int entries_max = 100;

    int error_count = 0;
    int glucose_mode = 1;
    int current_basal_profile = 1;
    boolean config_mode = false;



    public DanaCommProtocolV1(OutputWriter outputWriter, AbstractDeviceReader reader, String portName)
    {

        super(outputWriter, reader, portName);
        //loadPumpSpecificValues();
    }








    Hashtable<String, String> dtypes = new Hashtable<String, String>();

    ATechDate atd_1 = new ATechDate(ATechDate.FORMAT_DATE_AND_TIME_S, 0);
    byte old_record_code = -1;
    int old_record_value = -1;

    /**
     * Gets the device record.
     *
     * @param dataType
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
                        System.out.println("date: " + atd.getDateTimeString() + ", code: " + record_code + ", value="
                                + record_value); // +", value2=" +
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




    DeviceValuesWriter dvw = null;

    private DeviceValuesWriter getWriter()
    {

        if (dvw == null)
        {
            createDeviceValuesWriter();
        }

        return dvw;

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

    public boolean hasSpecialProgressStatus()
    {
        return true;
    }

    /**
     * This is method for reading configuration
     *
     * @throws ggc.plugin.device.PlugInBaseException
     */
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
                    this.commHandler.close();
                    //this.close();
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
        this.currentYear = new GregorianCalendar().get(Calendar.YEAR);

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
            this.commHandler.readLine();

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
                    this.commHandler.close();
                }
            }
            catch (Exception exx)
            {
                LOG.error("readDeviceDataFull.disconnect(): " + exx, exx);
            }

        }

    }


    /**
     * This is for reading device information. This should be used only if
     * normal dump doesn't retrieve this information (most dumps do).
     *
     * @throws PlugInBaseException
     */

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

            di.device_serial_number = sn;

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
            pvep.setActive_from(atd.getATDateTimeAsLong());
            pvep.setActive_till(atd.getATDateTimeAsLong());

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
        // //.setSubStatus(sub_status)
        this.outputWriter.setSpecialProgress(cur_ent); // .setSubStatus(sub_status)

    }


    private void createDeviceValuesWriter()
    {
        this.dvw = new DeviceValuesWriter();
        this.dvw.setOutputWriter(this.outputWriter);

        // added isNumeric, could cause problem

        // bolus - standard
        this.dvw.put("1_66", new PumpWriterValues(PumpWriterValues.OBJECT_BASE, PumpBaseType.Bolus,
                PumpBolusType.Normal, true));

        // bolus - wave (this is unhandled, data is not all available)
        this.dvw.put("1_69", new PumpWriterValues(PumpWriterValues.OBJECT_BASE, PumpBaseType.Bolus,
                PumpBolusType.Multiwave, false));

        // daily insulin record
        this.dvw.put("2_68", new PumpWriterValues(PumpWriterValues.OBJECT_BASE, PumpBaseType.Report,
                PumpReport.InsulinTotalDay, true));

        // CH (carbohydrates)
        this.dvw.put("8_82", new PumpWriterValues(PumpWriterValues.OBJECT_EXT, PumpAdditionalDataType.Carbohydrates, 0,
                true));
        // prime
        this.dvw.put("3_80", new PumpWriterValues(PumpWriterValues.OBJECT_BASE, PumpBaseType.Event,
                PumpEvents.PrimeInfusionSet, false));

        // BG
        this.dvw.put("6_71", new PumpWriterValues(PumpWriterValues.OBJECT_EXT, PumpAdditionalDataType.BloodGlucose, 0,
                true));

        // alarm
        this.dvw.put("5_66", new PumpWriterValues(PumpWriterValues.OBJECT_BASE, PumpBaseType.Alarm.getCode(), 0, false));

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


    private void getDeviceConfiguration(DanaDataType cmd /*    byte[] cmd*/) // throws Exception
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
                        PumpConfigurationGroup.Basal); //PumpConfigurationOld.PUMP_CONFIG_GROUP_INSULIN);
                writeConfiguration("PCFG_BOLUS_INCREMENT", "" + (hex_utils.getByteFromArray(7) / 100.0f),
                        PumpConfigurationGroup.Bolus); //PumpConfigurationOld.PUMP_CONFIG_GROUP_INSULIN);
                writeConfiguration("PCFG_BOLUS_PRESET", "" + getTrueOrFalse(hex_utils.getByteFromArray(8)),
                        PumpConfigurationGroup.Bolus); //PumpConfigurationOld.PUMP_CONFIG_GROUP_INSULIN);
                writeConfiguration("PCFG_BOLUS_ALARM", "" + hex_utils.getByteFromArray(9),
                        PumpConfigurationGroup.Bolus); //PumpConfigurationOld.PUMP_CONFIG_GROUP_INSULIN);
                writeConfiguration("PCFG_BOLUS_BLOCK", "" + hex_utils.getByteFromArray(10),
                        PumpConfigurationGroup.Bolus); //PumpConfigurationOld.PUMP_CONFIG_GROUP_INSULIN);

                if (hex_utils.getByteFromArray(11) == 0)
                {
                    writeConfiguration("PCFG_BASAL_UNIT", i18nControl.getMessage("UNIT_PER_HOUR"),
                            PumpConfigurationGroup.Basal); //PumpConfigurationOld.PUMP_CONFIG_GROUP_INSULIN);
                }
                else
                {
                    writeConfiguration("PCFG_BASAL_UNIT", i18nControl.getMessage("UNIT_PER_DAY"),
                            PumpConfigurationGroup.Basal); //PumpConfigurationOld.PUMP_CONFIG_GROUP_INSULIN);
                }

                // PROFILE = 12
                this.current_basal_profile = hex_utils.getByteFromArray(12) + 1;

            }
            else if (cmd == DanaDataType.SettingsMaxValues)
            {
                writeConfiguration("PCFG_MAX_BOLUS", "" + (hex_utils.getIntFromArray(6) / 100.0f),
                        PumpConfigurationGroup.Insulin); //PumpConfigurationOld.PUMP_CONFIG_GROUP_INSULIN);
                writeConfiguration("PCFG_MAX_BASAL", "" + (hex_utils.getIntFromArray(8) / 100.0f),
                        PumpConfigurationGroup.Insulin); //PumpConfigurationOld.PUMP_CONFIG_GROUP_INSULIN);
                writeConfiguration("PCFG_MAX_DAILY", "" + (hex_utils.getIntFromArray(10) / 100.0f),
                        PumpConfigurationGroup.Insulin); //PumpConfigurationOld.PUMP_CONFIG_GROUP_INSULIN);
            }
            else if (cmd == DanaDataType.SettingsGlucomode)
            {
                if (hex_utils.getByteFromArray(6) == 1)
                {
                    writeConfiguration("CFG_BASE_GLUCOSE_UNIT", i18nControl.getMessage("GLUCOSE_UNIT_MMOLL"),
                            PumpConfigurationGroup.General); //PumpConfigurationOld.PUMP_CONFIG_GROUP_BLOOD_GLUCOSE);
                }
                else
                {
                    writeConfiguration("CFG_BASE_GLUCOSE_UNIT", i18nControl.getMessage("GLUCOSE_UNIT_MGDL"),
                            PumpConfigurationGroup.General); //PumpConfigurationOld.PUMP_CONFIG_GROUP_BLOOD_GLUCOSE);
                }

                writeConfiguration("PCFG_EASY_MODE", "" + getTrueOrFalse(hex_utils.getByteFromArray(8)),
                        PumpConfigurationGroup.General); //PumpConfigurationOld.PUMP_CONFIG_GROUP_GENERAL);
            }
            else if (cmd == DanaDataType.SettingsBolusHelper)
            {

                if (glucose_mode == 0)
                {
                    writeConfiguration("PCFG_CH_INS_RATIO",
                            "" + DataAccessPlugInBase.Decimal0Format.format(hex_utils.getIntFromArray(6)),
                            PumpConfigurationGroup.Bolus); //PumpConfigurationOld.PUMP_CONFIG_GROUP_BOLUS_HELPER);
                    writeConfiguration("PCFG_BG_INS_RATIO",
                            "" + DataAccessPlugInBase.Decimal0Format.format(hex_utils.getIntFromArray(8)),
                            PumpConfigurationGroup.Bolus); //PumpConfigurationOld.PUMP_CONFIG_GROUP_BOLUS_HELPER);
                    writeConfiguration("PCFG_TARGET_BG",
                            "" + DataAccessPlugInBase.Decimal0Format.format(hex_utils.getIntFromArray(12)),
                            PumpConfigurationGroup.Bolus); //PumpConfigurationOld.PUMP_CONFIG_GROUP_BOLUS_HELPER);

                }
                else
                {
                    writeConfiguration("PCFG_CH_INS_RATIO",
                            "" + DataAccessPlugInBase.Decimal0Format.format(hex_utils.getIntFromArray(6) / 100.0f),
                            PumpConfigurationGroup.Bolus); //PumpConfigurationOld.PUMP_CONFIG_GROUP_BOLUS_HELPER);
                    writeConfiguration("PCFG_BG_INS_RATIO",
                            "" + DataAccessPlugInBase.Decimal0Format.format(hex_utils.getIntFromArray(8) / 100.0f),
                            PumpConfigurationGroup.Bolus); //PumpConfigurationOld.PUMP_CONFIG_GROUP_BOLUS_HELPER);
                    writeConfiguration("PCFG_TARGET_BG",
                            "" + DataAccessPlugInBase.Decimal0Format.format(hex_utils.getIntFromArray(12) / 100.0f),
                            PumpConfigurationGroup.Bolus); //PumpConfigurationOld.PUMP_CONFIG_GROUP_BOLUS_HELPER);
                }

                writeConfiguration("PCFG_ACTIVE_INSULIN_RATE",
                        "" + DataAccessPlugInBase.Decimal0Format.format(hex_utils.getIntFromArray(10) / 100.0f),
                        PumpConfigurationGroup.Insulin); //.PUMP_CONFIG_GROUP_BOLUS_HELPER);
                writeConfiguration("PCFG_ACTIVE_INSULIN_DECREMENT_RATIO", "" + (hex_utils.getByteFromArray(14) * 5),
                        PumpConfigurationGroup.Insulin); //.PUMP_CONFIG_GROUP_BOLUS_HELPER

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

        DeviceValueConfigEntry pvec = new DeviceValueConfigEntry(i18nControl.getMessage(key), value, config_group);
        this.outputWriter.writeConfigurationData(pvec);

        // System.out.println(m_ic.getMessage(key) + " = " + value);
    }

    private String getTrueOrFalse(byte val)
    {
        if (val == 0)
        {
            return i18nControl.getMessage("FALSE");
        }
        else
        {
            return i18nControl.getMessage("TRUE");
        }
    }



}
