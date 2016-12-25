package main.java.ggc.pump.data.dto;

import java.util.Calendar;

import com.atech.utils.data.ATechDate;

/**
 * Created by andy on 21.05.15.
 */
public class BasalDTO
{

    public ATechDate dateTime;
    public double basalRate;
    public int duration; // minutes
    public int temporaryBasal;

    boolean tbrPercent = true;


    public ATechDate getPlannedTBREnd()
    {
        if (duration == 0)
            return null;
        else
        {
            ATechDate dd = dateTime.clone();
            dd.add(Calendar.MINUTE, duration);

            return dd;
        }
    }


    @Override
    public String toString()
    {
        return "BasalDTO{" + "dateTime=" + dateTime + ", basalRate=" + basalRate + ", duration=" + duration
                + ", temporaryBasal=" + temporaryBasal + ", tbrPercent=" + tbrPercent + '}';
    }
}
