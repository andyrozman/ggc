package ggc.meter.data;

import java.util.HashMap;

/**
 * Created by andy on 02.05.15.
 */
public enum MeterValuesEntryDataType
{
    None("NO_DATA", null, false), //
    BG("BG", 3, true), //
    CH("CH", 5, true), //
    Urine("URINE", 4, true), //
    InvalidData("INVALID_DATA", null, false), //
    Multiple("MULTIPLE", null, false) //
    ;

    String description;
    Integer pumpExtCode;
    static HashMap<Integer, String> allowedPumpTypes;
    static HashMap<MeterValuesEntryDataType, String> processorTypes;
    boolean isProcessor;


    MeterValuesEntryDataType(String description, Integer pumpExtCode, boolean isProcessor)
    {
        this.description = description;
        this.pumpExtCode = pumpExtCode;
        this.isProcessor = isProcessor;
    }


    public static HashMap<Integer, String> getAllowedPumpTypes()
    {
        if (allowedPumpTypes == null)
        {
            allowedPumpTypes = new HashMap<Integer, String>();

            for (MeterValuesEntryDataType mpid : values())
            {
                if (mpid.pumpExtCode != null)
                {
                    allowedPumpTypes.put(mpid.pumpExtCode, null);
                }
            }
        }

        return allowedPumpTypes;
    }


    public static HashMap<MeterValuesEntryDataType, String> getProcessorTypes()
    {
        if (processorTypes == null)
        {
            processorTypes = new HashMap<MeterValuesEntryDataType, String>();

            for (MeterValuesEntryDataType mpid : values())
            {
                if (mpid.isProcessor)
                {
                    processorTypes.put(mpid, null);
                }
            }
        }

        return processorTypes;
    }


    public String getDescription()
    {
        return description;
    }
}
