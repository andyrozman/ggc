package ggc.pump.data;

import ggc.pump.data.defs.PumpBaseType;
import ggc.pump.util.DataAccessPump;

import java.util.HashMap;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.atech.utils.data.ATechDate;

public class PumpValuesHour
{
    int hour;

    private HashMap<Long, Float> bgs = new HashMap<Long, Float>();
    private StringBuffer sbBgs = new StringBuffer();
    private float chs = 0.0f;
    private float bolus = 0.0f;
    private static final Log LOG = LogFactory.getLog(PumpValuesHour.class);
    DataAccessPump dataAccessPump;

    public PumpValuesHour(DataAccessPump da)
    {
        this.dataAccessPump = da;
    }

    public void addBGEntry(PumpValuesEntryExt pumpValuesEntryExt)
    {
        this.bgs.put(pumpValuesEntryExt.getDateTime(), Float.parseFloat(pumpValuesEntryExt.getValue()));

        sbBgs.append(" ");
        sbBgs.append(getTimeAsString(pumpValuesEntryExt.getDateTime()));
        sbBgs.append("=");
        sbBgs.append(dataAccessPump.getDisplayedBGString(pumpValuesEntryExt.getValue()));
        sbBgs.append(",");
    }

    private String getTimeAsString(long dt)
    {
        ATechDate atd = new ATechDate(ATechDate.FORMAT_DATE_AND_TIME_S, dt);
        String s = atd.getTimeString();
        s = s.substring(0, s.lastIndexOf(":"));

        return s;
    }

    public HashMap<Long, Float> getBgs()
    {
        return bgs;
    }

    public float getBgProcessedValue()
    {
        float sum = 0.0f;

        for (Float f : bgs.values())
        {
            sum += f.floatValue();
        }

        if (bgs.size() > 1)
        {
            return sum / (bgs.size() * 1.0f);
        }

        return sum;
    }

    public void addCHEntry(String chValue)
    {
        if (StringUtils.isNotBlank(chValue))
        {
            chs += parseFloat(chValue, "CH");
        }
    }

    private float parseFloat(String value, String valueType)
    {
        try
        {
            return Float.parseFloat(value);
        }
        catch (Exception ex)
        {
            LOG.warn("Problem parsing " + valueType + " value: " + value);
            return 0.0f;
        }
    }

    public float getCH()
    {
        return chs;
    }

    public void addBolus(PumpValuesEntry pve)
    {

        if (pve.base_type == PumpBaseType.PUMP_DATA_PEN_INJECTION_BOLUS)
        {
            bolus += parseFloat(pve.getValue(), "Pen-Bolus");
        }
        else
        {
            // special boluses
            // pump values
            bolus += parseFloat(pve.getValue(), "Pump-Bolus");
        }

    }

    public float getBolus()
    {
        return bolus;
    }

    public String getMultipleBgs()
    {
        String mBgs = sbBgs.substring(0, sbBgs.length() - 1);
        mBgs.trim();

        return mBgs;
    }

}
