package ggc.pump.data;

import java.util.HashMap;

public class PumpValuesHour
{
    int hour;
    HashMap<Long, Float> bgs = new HashMap<Long, Float>();

    public void addBGEntry(PumpValuesEntryExt extData)
    {
        bgs.put(extData.getDateTime(), Float.parseFloat(extData.getValue()));
    }
}
