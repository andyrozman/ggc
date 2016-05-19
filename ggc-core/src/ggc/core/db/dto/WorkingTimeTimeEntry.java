package ggc.core.db.dto;

import com.atech.utils.data.ATechDate;
import com.atech.utils.data.ATechDateType;

/**
 * Created by andy on 14.02.16.
 */
public class WorkingTimeTimeEntry
{

    // time for 11:00 would be shown as 1100
    private int timeFrom;
    private int timeTill;
    private String timeFromToString;


    public int getTimeFrom()
    {
        return timeFrom;
    }


    public void setTimeFrom(int timeFrom)
    {
        this.timeFrom = timeFrom;
        setTimeString();
    }


    public int getTimeTill()
    {
        return timeTill;
    }


    public void setTimeTill(int timeTill)
    {
        this.timeTill = timeTill;
        setTimeString();
    }


    private void setTimeString()
    {
        timeFromToString = ATechDate.getTimeString(ATechDateType.TimeOnlyMin, timeFrom) + "-"
                + ATechDate.getTimeString(ATechDateType.TimeOnlyMin, timeTill);
    }


    public String toString()
    {
        return timeFromToString;
    }

}
