package ggc.cgms.device.abbott.libre.data;

import com.atech.utils.data.ATechDate;
import com.atech.utils.data.ATechDateType;

/**
 * Created by andy on 08/09/17.
 */
public class LibreRecordDto
{

    String[] values = null;


    public ATechDate getDateTime(String day, String month, String year, String hour, String minute, String second)
    {
        ATechDate date = new ATechDate(getInt(day), getInt(month), getInt(year) + 2000, getInt(hour), getInt(minute),
                getInt(second), ATechDateType.DateAndTimeSec);

        return date;
    }


    public ATechDate getDateTime(int day, int month, int year, int hour, int minute, int second)
    {
        ATechDate date = new ATechDate(getInt(day), getInt(month), getInt(year) + 2000, getInt(hour), getInt(minute),
                getInt(second), ATechDateType.DateAndTimeSec);

        return date;
    }


    public int getInt(String value)
    {
        return Integer.parseInt(value);
    }


    public int getInt(int index)
    {
        return getInt(values[index]);
    }


    public byte getByte(String value)
    {
        return Byte.parseByte(value);
    }


    public byte getByte(int index)
    {
        return getByte(values[index]);
    }


    public String getString(String value)
    {
        if (value.startsWith("\""))
        {
            value = value.substring(1);
        }

        if (value.endsWith("\""))
        {
            value = value.substring(0, value.length() - 1);
        }

        value = value.trim();

        if (value.length() == 0)
            return null;
        else
            return value;

    }


    public String getString(int index)
    {
        return getString(values[index]);
    }

}
