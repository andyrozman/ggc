package ggc.pump.data;

import ggc.pump.data.defs.PumpBaseType;
import ggc.pump.data.defs.PumpBolusType;
import ggc.pump.util.DataAccessPump;

import java.util.HashMap;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.atech.utils.data.ATechDate;
import com.atech.utils.data.ExceptionHandling;

public class PumpValuesHour
{
    int hour;

    private HashMap<Long, Float> bgs = new HashMap<Long, Float>();
    private StringBuffer sbBgs = new StringBuffer();
    private float chs = 0.0f;
    private float bolus = 0.0f;
    private float bolusSpecial = 0.0f;
    private StringBuffer sbBolus = new StringBuffer();

    private static final Log LOG = LogFactory.getLog(PumpValuesHour.class);
    DataAccessPump dataAccessPump;

    public PumpValuesHour(DataAccessPump da)
    {
        this.dataAccessPump = da;
        this.dataAccessPump.setNumberParsingExceptionHandling(ExceptionHandling.THROW_EXCEPTION);
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
            return dataAccessPump.getFloatValueFromStringWithException(value, 0.0f);
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

        if (pve.baseType == PumpBaseType.PenInjectionBolus)
        {
            bolus += parseFloat(pve.getValue(), "Pen-Bolus");
        }
        else
        {
            // special boluses
            // pump values

            switch (PumpBolusType.getByCode(pve.getSubType()))
            {
                case Normal:
                case Audio:
                    {
                        this.bolus += this.parseFloat(pve.getValue(), "Pump-Bolus");
                    }
                    break;

                case Extended:
                    {
                        addBolusSpecial(getTimeAsString(pve.getDateTime()) + "=" + pve.getValue());

                        String val = pve.getValue();
                        val = val.substring("AMOUNT_SQUARE=".length(), val.indexOf(";DURATION="));

                        this.bolusSpecial += this.parseFloat(val, "Pump-Bolus-Square");
                        // pve.setValue("AMOUNT_SQUARE=" + amount + ";DURATION="
                        // + e);
                    }
                    break;

                case Multiwave:
                    {
                        // immediate (as normal)
                        String val = pve.getValue();

                        // System.out.println("MW: " + val);

                        val = val.substring("AMOUNT=".length(), val.indexOf(";AMOUNT_SQUARE="));

                        // System.out.println("MW-2: " + val);

                        this.bolus += this.parseFloat(val, "Pump-Bolus-Multiwave(Imm)");

                        // square
                        val = pve.getValue();
                        val = val.substring(val.indexOf("AMOUNT_SQUARE="));

                        addBolusSpecial(getTimeAsString(pve.getDateTime()) + "=" + val);

                        // String val = pve.getValue();
                        val = val.substring("AMOUNT_SQUARE=".length(), val.indexOf(";DURATION="));

                        this.bolusSpecial += this.parseFloat(val, "Pump-Bolus-Multiwave(Sq)");

                        // pve.setValue(String.format("AMOUNT=%s;AMOUNT_SQUARE=%s;DURATION=%s",
                        // pve.setValue("AMOUNT_1=%s;AMOUNT_2=%s
                        // pve.setValue("IMMEDIATE_AMOUNT=%s;AMOUNT_SQUARE=%s;DURATION=%s"

                    }
                    break;

                default:
                    {}
                    break;
            }
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

    public float getBolusSpecialForSum()
    {
        return bolusSpecial;
    }

    public void setBolusSpecial(float bolusSpecial)
    {
        this.bolusSpecial = bolusSpecial;
    }

    public void addBolusSpecial(String blsSpecial)
    {
        // BOLUS_SQUARE
        // AMOUNT_SQUARE=%s;DURATION=%s

        blsSpecial = blsSpecial.replaceAll("AMOUNT_SQUARE=",
            this.dataAccessPump.getI18nControlInstance().getMessage("BOLUS_SQUARE") + " ");
        blsSpecial = blsSpecial.replaceAll(";DURATION=", " (");
        blsSpecial += ")";

        sbBolus.append(" ");
        sbBolus.append(blsSpecial);
        sbBolus.append(",");
    }

    public boolean hasBolusSpecial()
    {
        return (sbBolus.length() > 0);
    }

    public String getBolusSpecial()
    {
        String mSpBol = sbBolus.substring(0, sbBolus.length() - 1);
        mSpBol.trim();

        return mSpBol;
    }

}
