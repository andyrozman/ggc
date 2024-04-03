package ggc.meter.data;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

import com.atech.i18n.I18nControlAbstract;

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
    static Set<Integer> allowedPumpTypes;
    static HashMap<MeterValuesEntryDataType, String> processorTypes;
    boolean isProcessor;
    String translation;
    static boolean translated = false;


    public static void translateKeywords(I18nControlAbstract ic)
    {
        if (translated)
            return;

        for (MeterValuesEntryDataType pbt : values())
        {
            pbt.translation = ic.getMessage(pbt.description);
        }

        translated = true;
    }


    MeterValuesEntryDataType(String description, Integer pumpExtCode, boolean isProcessor)
    {
        this.description = description;
        this.pumpExtCode = pumpExtCode;
        this.isProcessor = isProcessor;
    }


    public static Set<Integer> getAllowedPumpTypes()
    {
        if (allowedPumpTypes == null)
        {
            allowedPumpTypes = new HashSet<Integer>();

            for (MeterValuesEntryDataType mpid : values())
            {
                if (mpid.pumpExtCode != null)
                {
                    allowedPumpTypes.add(mpid.pumpExtCode);
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


    public String getTranslation()
    {
        return this.translation;
    }
}
