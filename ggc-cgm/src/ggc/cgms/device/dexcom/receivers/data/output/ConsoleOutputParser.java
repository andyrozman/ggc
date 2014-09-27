package ggc.cgms.device.dexcom.receivers.data.output;

import ggc.cgms.device.dexcom.receivers.data.ReceiverDownloadData;
import ggc.cgms.device.dexcom.receivers.data.ReceiverDownloadDataConfig;

public class ConsoleOutputParser implements DataOutputParserInterface
{

    public void parse(DataOutputParserType parserType, ReceiverDownloadData data)
    {

        System.out.println("Serial Number: " + data.getSerialNumber());

        switch (parserType)
        {
            case Configuration:
                {
                    for (ReceiverDownloadDataConfig cfg : data.getConfigs())
                    {
                        System.out.println(cfg);
                    }
                }
                break;
            case G4_EGVData:
            case G4_InsertionTime:
            case G4_MeterData:
            case G4_UserEventData:
                {
                    for (Object dtEl : data.getDataByType(parserType))
                    {
                        System.out.println(dtEl);
                    }
                }
                break;
            default:
                break;

        }

    }

}
