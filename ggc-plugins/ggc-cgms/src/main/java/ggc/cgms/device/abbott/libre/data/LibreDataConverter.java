package ggc.cgms.device.abbott.libre.data;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.atech.utils.data.ATechDate;
import com.atech.utils.data.ATechDateType;

import ggc.cgms.data.defs.CGMSConfigurationGroup;
import ggc.cgms.device.abbott.libre.enums.LibreTextCommand;
import ggc.plugin.data.DeviceValueConfigEntry;
import ggc.plugin.output.OutputWriter;

/**
 * Created by andy on 08/09/17.
 */
public class LibreDataConverter
{

    private static final Logger LOG = LoggerFactory.getLogger(LibreDataConverter.class);

    OutputWriter outputWriter;
    LibreRecordDto process = new LibreRecordDto();


    public LibreDataConverter(OutputWriter outputWriter)
    {
        this.outputWriter = outputWriter;
    }


    // public E <? extends LibreRecordDto> convert(LibreHidReportDto hidResponse, Class<E> clazz)
    // {
    // return null;
    // }

    public void convertData(LibreTextCommand textCommand, String value)
    {
        switch (textCommand)
        {
            case SerialNumber:
                convertDirectEntry("CFG_BASE_SERIAL_NUMBER", value, CGMSConfigurationGroup.General);
                break;

            case SoftwareVersion:
                convertDirectEntry("CFG_BASE_FIRMWARE_VERSION", value, CGMSConfigurationGroup.General);
                break;

            case Date:
                convertDateTime(value);
                break;

            case Time: // not used
                break;

            case History:
                convertHistory(value);
                break;
            //
            // case GlucoseUnits:
            // break;

            case NtSound:
                convertNtSound(value);
                break;

            case BtSound:
                convertBtSound(value);
                break;

            case ComputerDateTime:
                convertDirectEntry("CFG_BASE_COMPUTER_TIME", value, CGMSConfigurationGroup.General);
                break;

            // case PatientName:
            // case PatientId:
            // case DatabaseRecordNumber:

            // case GlucoseUnits:
            // case NtSound:
            // //break;
            // case BtSound:
            // //break;
            // case Language:
            // break;
            // case AllLanguages:
            // break;
            case Test:
                break;
            // case GlucoseUnits:
            // break;
            // case FoodUnits:
            // break;
            // case ClockType:
            // break;
            // case BgTargets:
            // break;
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

        System.out.println(dto.toString());
    }


    private void convertManualEntry(String[] values)
    {
        ManualMeasurementDto dto = new ManualMeasurementDto(values);

        System.out.println(dto.toString());
    }


    private void convertBtSound(String value)
    {
        String[] values = value.split(",");

        convertDirectEntry("CFG_SOUND_TYPE_VOLUME",
            values[1].equals("1") ? "CFG_SOUND_VOLUME_HIGH" : "CFG_SOUND_VOLUME_LOW", CGMSConfigurationGroup.Sound);

        convertDirectEntry("CFG_SOUND_TYPE_TOUCH_TONE", values[0].equals("1") ? "CCFG_OPTION_ON" : "CCFG_OPTION_OFF",
            CGMSConfigurationGroup.Sound);
    }


    private void convertNtSound(String value)
    {
        String[] values = value.split(",");

        convertDirectEntry("CFG_SOUND_TYPE_NOTIFICATION_TONE",
            values[0].equals("1") ? "CCFG_OPTION_ON" : "CCFG_OPTION_OFF", CGMSConfigurationGroup.Sound);

        convertDirectEntry("CFG_SOUND_TYPE_NOTIFICATION_VIBRATE",
            values[1].equals("1") ? "CCFG_OPTION_ON" : "CCFG_OPTION_OFF", CGMSConfigurationGroup.Sound);
    }


    private void convertHistory(String value)
    {
        AutoMeasurementDto dto = new AutoMeasurementDto(value);

        System.out.println(dto.toString());

        // LOG.warn("NOT IMPLEMENTED HISTORY line={}", value);
    }


    private void convertDirectEntry(String key, String value, CGMSConfigurationGroup group)
    {
        outputWriter.writeConfigurationData(createConfigEntry(key, value, group));
    }


    private void convertDateTime(String value)
    {
        // DateTime.strptime("#{date} #{readTextResponse}", '%m,%d,%y %H,%M')
        String dateParts[] = value.split(",");

        ATechDate aTechDate = ATechDate.getATechDateFromParts(getInt(dateParts[1]), getInt(dateParts[0]),
            getInt(dateParts[2]), getInt(dateParts[3]), getInt(dateParts[4]), 0, ATechDateType.DateAndTimeSec);

        outputWriter.writeConfigurationData(
            createConfigEntry("CFG_BASE_DEVICE_TIME", aTechDate.toString(), CGMSConfigurationGroup.General));

    }


    private DeviceValueConfigEntry createConfigEntry(String key, String value, CGMSConfigurationGroup group)
    {
        return new DeviceValueConfigEntry(key, value, group);
    }


    private int getInt(String datePart)
    {
        return Integer.parseInt(datePart);
    }

}
