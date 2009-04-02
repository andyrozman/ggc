package ggc.pump.data.profile;

import ggc.pump.util.DataAccessPump;

import java.util.Comparator;

import com.atech.utils.ATechDate;


public class ProfileSubEntry implements Comparable<ProfileSubEntry>   // Comparator<ProfileSubEntry>
{
    public int time_start;
    public int time_end;
    
    public float amount;
    
    public ProfileSubEntry()
    {
    }
    
    public ProfileSubEntry(String input)
    {
        String[] ss = input.split("[-=]");
        
        DataAccessPump da = DataAccessPump.getInstance();
        
        time_start = da.getIntValueFromString(ss[0]);
        time_end = da.getIntValueFromString(ss[1]);
        amount = da.getFloatValueFromString(ss[2]);
    }
    
    
    public void setValues(ProfileSubEntry pse)
    {
        this.time_start = pse.time_start;
        this.time_end = pse.time_end;
        this.amount = pse.amount;
    }
    
    public String getPacked()
    {
        return time_start + "-" + time_end + "=" + DataAccessPump.Decimal2Format.format(amount);
    }
    
    
    public String toString()
    {
        return ATechDate.getTimeString(ATechDate.FORMAT_TIME_ONLY_MIN, time_start) + " - " + ATechDate.getTimeString(ATechDate.FORMAT_TIME_ONLY_MIN, time_end) + " = " + DataAccessPump.Decimal2Format.format(amount);
    }

    public int compare(ProfileSubEntry pse1, ProfileSubEntry pse2)
    {
        if (pse1.time_start == pse2.time_start)
            return (pse1.time_end - pse2.time_end);
        else
            return (pse1.time_start - pse2.time_start);
    }

    public int compareTo(ProfileSubEntry arg0)
    {
        return compare(this, arg0);
    }
    
    
}
