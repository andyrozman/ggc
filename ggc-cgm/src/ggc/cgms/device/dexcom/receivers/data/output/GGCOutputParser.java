package ggc.cgms.device.dexcom.receivers.data.output;

import java.io.File;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.atech.utils.data.ATechDate;

import ggc.cgms.data.CGMSValuesExtendedEntry;
import ggc.cgms.data.CGMSValuesSubEntry;
import ggc.cgms.data.CGMSValuesTableModel;
import ggc.cgms.data.defs.CGMSEvents;
import ggc.cgms.data.defs.extended.CGMSExtendedDataType;
import ggc.cgms.device.dexcom.DexcomCGMS;
import ggc.cgms.device.dexcom.receivers.DexcomDevice;
import ggc.cgms.device.dexcom.receivers.data.ReceiverDownloadData;
import ggc.cgms.device.dexcom.receivers.g4receiver.data.EGVRecord;
import ggc.cgms.device.dexcom.receivers.g4receiver.data.InsertionTimeRecord;
import ggc.cgms.device.dexcom.receivers.g4receiver.data.MeterDataRecord;
import ggc.cgms.device.dexcom.receivers.g4receiver.data.UserEventDataRecord;
import ggc.cgms.device.dexcom.receivers.g4receiver.enums.Exercise;
import ggc.cgms.util.DataAccessCGMS;
import ggc.core.data.defs.Health;
import ggc.plugin.device.DeviceIdentification;
import ggc.plugin.output.OutputWriter;

public class GGCOutputParser implements DataOutputParserInterface
{
    private static Log log = LogFactory.getLog(GGCOutputParser.class);
    OutputWriter outputWriter;
    CGMSValuesTableModel valuesModel;
    DataAccessCGMS dataAccess = DataAccessCGMS.getInstance();
    private boolean isIdentificationWriten = false;
    String source = "";
    DexcomDevice dexcomDevice;
    
    public GGCOutputParser(OutputWriter outputWriter, DexcomDevice dexcomDevice)
    {
        this.outputWriter = outputWriter;
        this.dexcomDevice = dexcomDevice;
        
        valuesModel = (CGMSValuesTableModel)dataAccess.getDeviceDataHandler().getDeviceValuesTableModel();
    }
    
    
    
    

    public void parse(DataOutputParserType parserType, ReceiverDownloadData data)
    {
        if (parserType==DataOutputParserType.Configuration)
        {
            writeDeviceIdentification(parserType, data);
        }
        
        this.source = this.dexcomDevice.getDescription() + " [" + data.getSerialNumber() + "]";
        
        switch(parserType)
        {
            case Configuration:
                {
                    writeDeviceIdentification(parserType, data);
                }  break;

            case G4_EGVData_SensorReading:
            case G4_EGVData_SensorTrend:
                {
                    writeElements(DataOutputParserType.G4_EGVData, parserType, data);
                } break;
                
            case G4_EGVData:
            case G4_InsertionTime:
            case G4_MeterData:
            case G4_UserEventData:
                {
                    writeElements(parserType, parserType, data);
                } break;
            
            default:
                break;
        }

    }





    private void writeElements(DataOutputParserType dataType, DataOutputParserType parserType, ReceiverDownloadData data)
    {
        for(Object element : data.getDataByType(dataType))
        {   
            writeElement(parserType, element);
        }
    }

    
    
    
    
    private void writeElement(DataOutputParserType parserType, Object element)
    {
        switch(parserType)
        {
        
        
        case G4_EGVData_SensorReading:
        case G4_EGVData_SensorTrend:
            writeEGVData((EGVRecord)element, parserType);
            break;
        case G4_InsertionTime:
            writeInsertionTime((InsertionTimeRecord)element);
            break;
        case G4_MeterData:
            writeMeterData((MeterDataRecord)element);
            break;
        case G4_UserEventData:
            writeUserEventData((UserEventDataRecord)element);
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
        
        
        switch(record.getEventType())
        {
            case Carbs:
                {
                    cvex.setType(CGMSExtendedDataType.Carbs);
                    cvex.value = "" + record.getEventValue();
                } break;
            
            case Insulin:
                {
                    cvex.setType(CGMSExtendedDataType.Insulin);
                    cvex.value = "" + record.getEventValue();
                } break;   

            case Health:
            {
                cvex.setType(CGMSExtendedDataType.Health);
                cvex.subType = record.eventSubType;
            } break;
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
        //log.debug("DateTime meterdata device: " + record.getDisplayDate());
        
        CGMSValuesSubEntry sub = new CGMSValuesSubEntry();
        sub.setDateTime(ATechDate.getATDateTimeFromDate(record.getDisplayDate(), ATechDate.FORMAT_DATE_AND_TIME_S));
        sub.setType(CGMSValuesSubEntry.METER_CALIBRATION_READING);
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
            sub.setType(CGMSValuesSubEntry.CGMS_BG_READING);
            sub.value = record.getGlucoseValue();
            sub.setSource(source);
            
            addEntry(sub);
        }
        
        if (parserType == DataOutputParserType.G4_EGVData_SensorTrend)
        {
            CGMSValuesSubEntry sub = new CGMSValuesSubEntry();
            sub.setDateTime(ATechDate.getATDateTimeFromDate(record.getDisplayDate(), ATechDate.FORMAT_DATE_AND_TIME_S));
            sub.setType(CGMSValuesSubEntry.CGMS_TREND);
            sub.value = record.getTrendArrow().getValue();
            sub.setSource(source);
            
            addEntry(sub);
        }
    }
    

    private void writeInsertionTime(InsertionTimeRecord record)
    {
        CGMSValuesSubEntry sub = new CGMSValuesSubEntry();
        sub.setDateTime(ATechDate.getATDateTimeFromDate(record.getDisplayDate(), ATechDate.FORMAT_DATE_AND_TIME_S));
        sub.setType(CGMSValuesSubEntry.CGMS_EVENT);
        sub.setSource(source);
        
        if (record.getIsInserted())
        {
            sub.value = CGMSEvents.CGMS_EVENT_SENSOR_START;
        }
        else
        {
            sub.value = CGMSEvents.CGMS_EVENT_SENSOR_STOP;
        }
        
        addEntry(sub);
    }


    private void writeDeviceIdentification(DataOutputParserType parserType, ReceiverDownloadData data)
    {
        if (this.isIdentificationWriten)
            return;
        
        
        DeviceIdentification di = this.outputWriter.getDeviceIdentification();
        di.device_serial_number = data.getSerialNumber();

        if (parserType==DataOutputParserType.Configuration)
        {
            if (data.containsConfiguration("FIRMWARE_VERSION"))
                di.device_hardware_version = data.getConfigValueByKey("FIRMWARE_VERSION");
            
            if (data.containsConfiguration("SOFTWARE_NUMBER"))
                di.device_software_version = data.getConfigValueByKey("SOFTWARE_NUMBER");
        }
        
        
        this.outputWriter.setDeviceIdentification(di);
        this.outputWriter.writeDeviceIdentification();
        
        this.isIdentificationWriten = true;
    }

    
    private void addEntry(CGMSValuesSubEntry entry) {
        this.valuesModel.addEntry(entry);
    }
}