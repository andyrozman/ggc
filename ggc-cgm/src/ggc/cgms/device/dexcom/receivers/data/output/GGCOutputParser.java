package ggc.cgms.device.dexcom.receivers.data.output;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.atech.i18n.I18nControlAbstract;
import com.atech.utils.data.ATechDate;

import ggc.cgms.data.CGMSValuesExtendedEntry;
import ggc.cgms.data.CGMSValuesSubEntry;
import ggc.cgms.data.CGMSValuesTableModel;
import ggc.cgms.data.defs.CGMSBaseDataType;
import ggc.cgms.data.defs.CGMSConfigurationGroup;
import ggc.cgms.data.defs.CGMSEvents;
import ggc.cgms.data.defs.extended.CGMSExtendedDataType;
import ggc.cgms.device.dexcom.receivers.DexcomDevice;
import ggc.cgms.device.dexcom.receivers.data.ReceiverDownloadData;
import ggc.cgms.device.dexcom.receivers.g4receiver.data.EGVRecord;
import ggc.cgms.device.dexcom.receivers.g4receiver.data.InsertionTimeRecord;
import ggc.cgms.device.dexcom.receivers.g4receiver.data.MeterDataRecord;
import ggc.cgms.device.dexcom.receivers.g4receiver.data.UserEventDataRecord;
import ggc.cgms.util.DataAccessCGMS;
import ggc.plugin.data.DeviceValueConfigEntry;
import ggc.plugin.device.DeviceIdentification;
import ggc.plugin.output.OutputWriter;

/**
 *  Application: GGC - GNU Gluco Control
 *  Plug-in: CGMS Tool (support for CGMS devices)
 *
 *  See AUTHORS for copyright information.
 *
 *  This program is free software; you can redistribute it and/or modify it under
 *  the terms of the GNU General Public License as published by the Free Software
 *  Foundation; either version 2 of the License, or (at your option) any later
 *  version.
 *
 *  This program is distributed in the hope that it will be useful, but WITHOUT
 *  ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 *  FOR A PARTICULAR PURPOSE. See the GNU General Public License for more
 *  details.
 *
 *  You should have received a copy of the GNU General Public License along with
 *  this program; if not, write to the Free Software Foundation, Inc., 59 Temple
 *  Place, Suite 330, Boston, MA 02111-1307 USA
 *
 *  Filename: CGMDataType
 *  Description: CGMS Data types, as used in database (undefined at this time)
 *
 *  Author: Andy {andy@atech-software.com}
 */

public class GGCOutputParser implements DataOutputParserInterface
{

    private static Log log = LogFactory.getLog(GGCOutputParser.class);
    OutputWriter outputWriter;
    CGMSValuesTableModel valuesModel;
    DataAccessCGMS dataAccess = DataAccessCGMS.getInstance();
    private boolean isIdentificationWriten = false;
    String source = "";
    DexcomDevice dexcomDevice;
    I18nControlAbstract i18nControl = DataAccessCGMS.getInstance().getI18nControlInstance();


    public GGCOutputParser(OutputWriter outputWriter, DexcomDevice dexcomDevice)
    {
        this.outputWriter = outputWriter;
        this.dexcomDevice = dexcomDevice;

        valuesModel = (CGMSValuesTableModel) dataAccess.getDeviceDataHandler().getDeviceValuesTableModel();
    }


    public void parse(DataOutputParserType parserType, ReceiverDownloadData data)
    {
        if (parserType == DataOutputParserType.Configuration)
        {
            writeDeviceIdentification(parserType, data);
        }

        this.source = this.dexcomDevice.getDescription() + " [" + data.getSerialNumber() + "]";

        switch (parserType)
        {
            case Configuration:
                {
                    // writeDeviceIdentification(parserType, data);
                    writeConfiguration(data);
                }
                break;

            case G4_EGVData_SensorReading:
            case G4_EGVData_SensorTrend:
                {
                    writeElements(DataOutputParserType.G4_EGVData, parserType, data);
                }
                break;

            case G4_EGVData:
            case G4_InsertionTime:
            case G4_MeterData:
            case G4_UserEventData:
                {
                    writeElements(parserType, parserType, data);
                }
                break;

            default:
                break;
        }

    }


    private void writeElements(DataOutputParserType dataType, DataOutputParserType parserType, ReceiverDownloadData data)
    {
        for (Object element : data.getDataByType(dataType))
        {
            writeElement(parserType, element);
        }
    }


    private void writeElement(DataOutputParserType parserType, Object element)
    {
        switch (parserType)
        {

            case G4_EGVData_SensorReading:
            case G4_EGVData_SensorTrend:
                writeEGVData((EGVRecord) element, parserType);
                break;
            case G4_InsertionTime:
                writeInsertionTime((InsertionTimeRecord) element);
                break;
            case G4_MeterData:
                writeMeterData((MeterDataRecord) element);
                break;
            case G4_UserEventData:
                writeUserEventData((UserEventDataRecord) element);
                break;
            default:
                log.debug("Unsupported Type for Writing: " + parserType.name());
                break;

        }

    }


    private void writeUserEventData(UserEventDataRecord record)
    {
        CGMSValuesExtendedEntry cvex = new CGMSValuesExtendedEntry();
        cvex.datetime = ATechDate.getATDateTimeFromDate(record.getDisplayDate(), ATechDate.FORMAT_DATE_AND_TIME_S);
        cvex.setSource(source);

        switch (record.getEventType())
        {
            case Carbs:
                {
                    cvex.setType(CGMSExtendedDataType.Carbs);
                    cvex.value = "" + record.getEventValue();
                }
                break;

            case Insulin:
                {
                    cvex.setType(CGMSExtendedDataType.Insulin);
                    cvex.value = "" + record.getEventValue();
                }
                break;

            case Health:
                {
                    cvex.setType(CGMSExtendedDataType.Health);
                    cvex.subType = record.eventSubType;
                }
                break;
            case Exercise:
                {
                    cvex.setType(CGMSExtendedDataType.Exercise);
                    cvex.subType = record.eventSubType;
                    cvex.value = "" + record.eventValue;
                }
            default:
                break;
        }

        this.valuesModel.addEntry(cvex);

    }


    private void writeMeterData(MeterDataRecord record)
    {
        // log.debug("DateTime meterdata device: " + record.getDisplayDate());

        CGMSValuesSubEntry sub = new CGMSValuesSubEntry();
        sub.setDateTime(ATechDate.getATDateTimeFromDate(record.getDisplayDate(), ATechDate.FORMAT_DATE_AND_TIME_S));
        sub.setType(CGMSBaseDataType.SensorCalibration);
        sub.value = record.getMeterValue();
        sub.setSource(source);

        addEntry(sub);
    }


    private void writeEGVData(EGVRecord record, DataOutputParserType parserType)
    {
        if (parserType == DataOutputParserType.G4_EGVData_SensorReading)
        {
            CGMSValuesSubEntry sub = new CGMSValuesSubEntry();
            sub.setDateTime(ATechDate.getATDateTimeFromDate(record.getDisplayDate(), ATechDate.FORMAT_DATE_AND_TIME_S));
            sub.setType(CGMSBaseDataType.SensorReading);
            sub.value = record.getGlucoseValue();
            sub.setSource(source);

            addEntry(sub);
        }

        if (parserType == DataOutputParserType.G4_EGVData_SensorTrend)
        {
            CGMSValuesSubEntry sub = new CGMSValuesSubEntry();
            sub.setDateTime(ATechDate.getATDateTimeFromDate(record.getDisplayDate(), ATechDate.FORMAT_DATE_AND_TIME_S));
            sub.setType(CGMSBaseDataType.SensorReadingTrend);
            sub.value = record.getTrendArrow().getValue();
            sub.setSource(source);

            addEntry(sub);
        }
    }


    private void writeInsertionTime(InsertionTimeRecord record)
    {
        CGMSValuesSubEntry sub = new CGMSValuesSubEntry();
        sub.setDateTime(ATechDate.getATDateTimeFromDate(record.getDisplayDate(), ATechDate.FORMAT_DATE_AND_TIME_S));
        sub.setType(CGMSBaseDataType.Event);
        sub.setSource(source);

        if (record.getIsInserted())
        {
            sub.value = CGMSEvents.SensorStart.getCode();
        }
        else
        {
            sub.value = CGMSEvents.SensorStop.getCode();
        }

        addEntry(sub);
    }


    private void writeDeviceIdentification(DataOutputParserType parserType, ReceiverDownloadData data)
    {
        if (this.isIdentificationWriten)
            return;

        DeviceIdentification di = this.outputWriter.getDeviceIdentification();
        di.device_serial_number = data.getSerialNumber();

        if (parserType == DataOutputParserType.Configuration)
        {
            if (data.containsConfiguration("FIRMWARE_VERSION"))
            {
                di.device_hardware_version = data.getConfigValueByKey("FIRMWARE_VERSION");
            }

            if (data.containsConfiguration("SOFTWARE_NUMBER"))
            {
                di.device_software_version = data.getConfigValueByKey("SOFTWARE_NUMBER");
            }
        }

        writeConfiguration(data);

        this.outputWriter.setDeviceIdentification(di);
        this.outputWriter.writeDeviceIdentification();

        this.isIdentificationWriten = true;
    }


    private void writeConfiguration(ReceiverDownloadData data)
    {
        String[] cfgs = { "API_VERSION", "PRODUCT_ID", //
                         "PRODUCT_NAME", "SOFTWARE_NUMBER", //
                         "FIRMWARE_VERSION", "PORT_VERSION", //
                         "RF_VERSION", "SYSTEM_TIME", //
                         "DISPLAY_TIME", "LANGUAGE", //
                         "GLUCOSE_UNIT", "CLOCK_MODE" };

        CGMSConfigurationGroup[] configGroup = { CGMSConfigurationGroup.Device, CGMSConfigurationGroup.Device,
                                                CGMSConfigurationGroup.Device, CGMSConfigurationGroup.Device,
                                                CGMSConfigurationGroup.Device, CGMSConfigurationGroup.Device,
                                                CGMSConfigurationGroup.Device, CGMSConfigurationGroup.General,
                                                CGMSConfigurationGroup.General, CGMSConfigurationGroup.General,
                                                CGMSConfigurationGroup.General, CGMSConfigurationGroup.General };

        for (int i = 0; i < cfgs.length; i++)
        {
            String cfgKey = cfgs[i];
            String value = data.getConfigValueByKey(cfgKey);

            if (value != null)
            {
                writeConfigurationData("CFG_BASE_" + cfgKey, value, configGroup[i]);
            }
        }

        writeConfigurationData("CFG_BASE_SERIAL_NUMBER", data.getSerialNumber(), CGMSConfigurationGroup.General);
    }


    private void writeConfigurationData(String key, String value, CGMSConfigurationGroup group)
    {
        outputWriter.writeConfigurationData(new DeviceValueConfigEntry(this.i18nControl.getMessage(key), value, group));

    }


    private void addEntry(CGMSValuesSubEntry entry)
    {
        this.valuesModel.addEntry(entry);
    }
}
