package ggc.core.db.dto;

import java.util.List;

import com.atech.i18n.I18nControlAbstract;

import ggc.core.data.defs.WeekDay;
import ggc.core.util.DataAccess;

/**
 * Created by andy on 14.02.16.
 */
public class WorkingTimeDTO
{

    private List<WeekDay> weekDays;

    private List<WorkingTimeTimeEntry> timeEntries;

    private long dateFrom;
    private long dateTill;

    private String comment;


    public List<WeekDay> getWeekDays()
    {
        return weekDays;
    }


    public void setWeekDays(List<WeekDay> weekDays)
    {
        this.weekDays = weekDays;
    }


    public List<WorkingTimeTimeEntry> getTimeEntries()
    {
        return timeEntries;
    }


    public void setTimeEntries(List<WorkingTimeTimeEntry> timeEntries)
    {
        this.timeEntries = timeEntries;
    }


    public long getDateFrom()
    {
        return dateFrom;
    }


    public void setDateFrom(long dateFrom)
    {
        this.dateFrom = dateFrom;
    }


    public long getDateTill()
    {
        return dateTill;
    }


    public void setDateTill(long dateTill)
    {
        this.dateTill = dateTill;
    }


    public String getComment()
    {
        return comment;
    }


    public void setComment(String comment)
    {
        this.comment = comment;
    }


    public String toString()
    {
        StringBuilder sb = new StringBuilder();
        DataAccess dataAccess = DataAccess.getInstance();
        I18nControlAbstract ic = dataAccess.getI18nControlInstance();

        for (WeekDay day : weekDays)
        {
            DataAccess.appendToStringBuilder(sb, ic.getMessage(day.getDayI18nKey()).substring(0, 3), ", ");
        }

        sb.append(": ");

        StringBuilder sb2 = new StringBuilder();

        for (WorkingTimeTimeEntry entry : this.timeEntries)
        {
            DataAccess.appendToStringBuilder(sb2, entry.toString(), ", ");
        }

        return sb.toString() + sb2.toString();
    }

}
