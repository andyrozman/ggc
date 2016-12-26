package ggc.pump.data.util;

import java.util.StringTokenizer;

import ggc.pump.data.PumpValuesEntry;
import ggc.pump.data.defs.PumpBolusType;
import ggc.pump.util.DataAccessPump;

/**
 * Created by andy on 01.10.15.
 */
public class PumpBolusManager
{

    DataAccessPump dataAccess;


    public PumpBolusManager(DataAccessPump dataAccessPump)
    {
        this.dataAccess = dataAccessPump;
    }


    public float getValue(PumpValuesEntry entry)
    {
        switch (PumpBolusType.getByCode(entry.getSubType()))
        {
            case Normal:
            case Audio:
                return dataAccess.getFloatValueFromString(entry.getValue());

            case Extended:
                return getExtendedValue(entry);

            case Multiwave:
                return dataAccess.getFloatValueFromString(getParameterValue("AMOUNT=", entry.getValue()));

            default:
            case None:
                return 0.0f;
        }
    }


    public float getExtendedValue(PumpValuesEntry entry)
    {
        switch (PumpBolusType.getByCode(entry.getSubType()))
        {
            case Extended:
            case Multiwave:
                return dataAccess.getFloatValueFromString(getParameterValue("AMOUNT_SQUARE=", entry.getValue()));

            default:
                return 0.0f;
        }
    }


    public int getDuration(PumpValuesEntry entry)
    {
        switch (PumpBolusType.getByCode(entry.getSubType()))
        {
            case Extended:
            case Multiwave:
                return dataAccess.getIntValueFromString(getParameterValue("DURATION=", entry.getValue()));

            default:
                return 0;
        }

    }


    // Extended(3, "BOLUS_SQUARE", "AMOUNT_SQUARE=%s;DURATION=%s"), //
    // Multiwave(4, "BOLUS_MULTIWAVE", "AMOUNT=%s;AMOUNT_SQUARE=%s;DURATION=%s")

    public String getParameterValue(String name, String encodedValue)
    {
        StringTokenizer stringTokenizer = new StringTokenizer(encodedValue, ";");

        while (stringTokenizer.hasMoreTokens())
        {
            String tk = stringTokenizer.nextToken();

            if (tk.startsWith(name))
            {
                return tk.substring(tk.indexOf("=") + 1);
            }
        }

        return null;
    }

}
