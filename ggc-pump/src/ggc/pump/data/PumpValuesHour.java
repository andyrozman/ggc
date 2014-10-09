package ggc.pump.data;

import java.util.HashMap;

public class PumpValuesHour
{
    int hour;

    private HashMap<Long, Float> bgs = new HashMap<Long, Float>();
    private float chs = 0.0f;
    private float bolus = 0.0f;

    public void addBGEntry(PumpValuesEntryExt pumpValuesEntryExt)
    {
        this.bgs.put(pumpValuesEntryExt.getDateTime(), Float.parseFloat(pumpValuesEntryExt.getValue()));
    }

    public HashMap<Long, Float> getBgs()
    {
        return bgs;
    }

    public void setBgs(HashMap<Long, Float> bgs)
    {
        this.bgs = bgs;
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
        try
        {
            chs += Float.parseFloat(chValue);
        }
        catch (Exception ex)
        {}
    }

    public float getCH()
    {
        return chs;
    }

    public void addBolus(String bolusIn)
    {
        try
        {
            bolus += Float.parseFloat(bolusIn);
        }
        catch (Exception ex)
        {
            System.out.println("Parse Bolus. " + bolusIn + ", Ex.: " + ex);
        }
    }

    public float getBolus()
    {
        return bolus;
    }

}
