package ggc.cgms.device.abbott.libre.data;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.atech.i18n.I18nControlAbstract;
import com.atech.utils.data.ATechDate;
import com.atech.utils.data.ATechDateType;

import ggc.cgms.data.defs.CGMSConfigurationGroup;
import ggc.cgms.data.writer.CGMSValuesWriter;
import ggc.cgms.data.writer.CGMSValuesWriter2;
import ggc.cgms.device.abbott.libre.enums.LibreTextCommand;
import ggc.cgms.device.abbott.libre.enums.ResultValueType;
import ggc.cgms.util.DataAccessCGMS;
import ggc.plugin.data.DeviceValueConfigEntry;
import ggc.plugin.device.DeviceIdentification;
import ggc.plugin.device.impl.abbott.hid.AbbottHidDataConverter;
import ggc.plugin.device.impl.abbott.hid.AbbottHidRecordDto;
import ggc.plugin.device.impl.abbott.hid.AbbottHidTextCommand;
import ggc.plugin.output.OutputWriter;

/**
 * Created by andy on 08/09/17.
 */
public class LibreDataConverter implements AbbottHidDataConverter
{

    private static final Logger LOG = LoggerFactory.getLogger(LibreDataConverter.class);

    OutputWriter outputWriter;
    AbbottHidRecordDto process = new AbbottHidRecordDto();
    CGMSValuesWriter cgmsValuesWriter;
    CGMSValuesWriter2 cgmsValuesWriter2;
    DataAccessCGMS dataAccess = DataAccessCGMS.getInstance();
    I18nControlAbstract i18nControl = dataAccess.getI18nControlInstance();

    DeviceIdentification deviceIdentification;
    boolean isIdentificationWriten;


    public LibreDataConverter(OutputWriter outputWriter)
    {
        this.outputWriter = outputWriter;
        this.cgmsValuesWriter = CGMSValuesWriter.getInstance(outputWriter);
        this.cgmsValuesWriter2 = CGMSValuesWriter2.getInstance(outputWriter);

        this.deviceIdentification = this.outputWriter.getDeviceIdentification();
    }

    public void convertData(AbbottHidTextCommand textCommand, String value)
    {
        convertData((LibreTextCommand)textCommand, value);
    }

    public void convertData(LibreTextCommand textCommand, String value)
    {
        switch (textCommand)
        {
            case SerialNumber:
                convertDirectEntry("CFG_BASE_SERIAL_NUMBER", value, CGMSConfigurationGroup.General);
                this.deviceIdentification.deviceSerialNumber = value;
                break;

            case SoftwareVersion:
                convertDirectEntry("CFG_BASE_FIRMWARE_VERSION", value, CGMSConfigurationGroup.General);
                this.deviceIdentification.deviceHardwareVersion = value;
                break;

            case Date:
                convertDateTime(value);
                break;

            case Time: // not used
                break;

            case NtSound:
                convertNtSound(value);
                break;

            case BtSound:
                convertBtSound(value);
                break;

            case ComputerDateTime:
                convertDirectEntry("CFG_BASE_COMPUTER_TIME", value, CGMSConfigurationGroup.General);
                break;

            case History:
                convertHistory(value);
                break;

            case OtherHistory:
                convertOtherHistory(value);
                break;

            default:
                LOG.warn("NOT DONE ........ key={}, value={}", textCommand.name(), value);

        }

    }


    private void convertOtherHistory(String value)
    {
        String[] values = value.split(",");

        int type = process.getInt(values[1]);

        switch (type)
        {
            case 2:
                convertManualEntry(values);
                break;

            case 5:
                convertTimeChangeEntry(values);
                break;

            default:
                {
                    LOG.error("Unsupported entry. PLEASE CONTACT GGC TEAM WITH FOLLOWING INFORMATION:");
                    LOG.error("Type: {}: {}", type, value);
                }
                break;

        }
    }


    private void convertTimeChangeEntry(String[] values)
    {
        TimeChangeDto dto = new TimeChangeDto(values);

        // FIXME removed
        //System.out.println(dto.toString());

        cgmsValuesWriter.writeObject("Event_DateTimeChanged", dto.dateTimeOld, dto.dateTimeNew.getDateTimeString());
    }


    private void convertManualEntry(String[] values)
    {
        ManualMeasurementDto dto = new ManualMeasurementDto(values);

        if (dto.readingType == ResultValueType.Scan || dto.readingType == ResultValueType.Glucose)
        {
            Float f = dto.value;
            cgmsValuesWriter.writeObject("ManualReading", dto.getDateTime(), f.intValue());
        }
        else
        {
            cgmsValuesWriter2.writeObject("AdditionalData_Ketones", dto.getDateTime(), dto.value);
        }

        if (dto.foodFlag == 1)
        {
            cgmsValuesWriter2.writeObject("AdditionalData_Carbs", dto.getDateTime(), dto.foodCarbohydratesGrams);
        }

        if (dto.sportsFlag == 1)
        {
            cgmsValuesWriter2.writeObject("AdditionalData_Exercise_Undefined", dto.getDateTime(), (String)null);
        }

        if (dto.medicationFlag == 1)
        {
            cgmsValuesWriter2.writeObject("AdditionalData_Health_Medication", dto.getDateTime(), (String)null);
        }

        if (dto.rapidActingInsulinFlag == 1)
        {
            String value = dataAccess.getFormatedValue(dto.valueRapidActingInsulin, 2);
            cgmsValuesWriter2.writeObject("AdditionalData_InsulinShortActing", dto.dateTime2, value);
        }

        if (dto.longActingInsulinFlag == 1)
        {
            String value = dataAccess.getFormatedValue(dto.valueLongActingInsulin, 2);
            cgmsValuesWriter2.writeObject("AdditionalData_InsulinLongActing", dto.getDateTime(), value);
        }

    }


    private void convertBtSound(String value)
    {
        String[] values = value.split(",");

        convertDirectEntry("CFG_SOUND_TYPE_VOLUME", values[1].equals("1") ? "CFG_SOUND_VOLUME_HIGH"
                : "CFG_SOUND_VOLUME_LOW", CGMSConfigurationGroup.Sound);

        convertDirectEntry("CFG_SOUND_TYPE_TOUCH_TONE", values[0].equals("1") ? "CCFG_OPTION_ON" : "CCFG_OPTION_OFF",
            CGMSConfigurationGroup.Sound);
    }


    private void convertNtSound(String value)
    {
        String[] values = value.split(",");

        convertDirectEntry("CFG_SOUND_TYPE_NOTIFICATION_TONE", values[0].equals("1") ? "CCFG_OPTION_ON"
                : "CCFG_OPTION_OFF", CGMSConfigurationGroup.Sound);

        convertDirectEntry("CFG_SOUND_TYPE_NOTIFICATION_VIBRATE", values[1].equals("1") ? "CCFG_OPTION_ON"
                : "CCFG_OPTION_OFF", CGMSConfigurationGroup.Sound);
    }


    private void convertHistory(String value)
    {
        AutoMeasurementDto dto = new AutoMeasurementDto(value);

        cgmsValuesWriter.writeObject("SensorReading", dto.getDateTime(), dto.value);
    }


    private void convertDirectEntry(String key, String value, CGMSConfigurationGroup group)
    {
        outputWriter.writeConfigurationData(createConfigEntry(key, value, group));
    }


    private void convertDateTime(String value)
    {
        String dateParts[] = value.split(",");

        ATechDate aTechDate = ATechDate.getATechDateFromParts(getInt(dateParts[1]), getInt(dateParts[0]),
            getInt(dateParts[2]), getInt(dateParts[3]), getInt(dateParts[4]), 0, ATechDateType.DateAndTimeSec);

        outputWriter.writeConfigurationData(createConfigEntry("CFG_BASE_DEVICE_TIME", aTechDate.toString(),
            CGMSConfigurationGroup.General));

    }


    private DeviceValueConfigEntry createConfigEntry(String key, String value, CGMSConfigurationGroup group)
    {
        return new DeviceValueConfigEntry(i18nControl.getMessage(key), i18nControl.getMessage(value), group);
    }


    private int getInt(String datePart)
    {
        return Integer.parseInt(datePart);
    }






}
