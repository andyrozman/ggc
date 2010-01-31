package ggc.pump.data.profile;

import ggc.pump.util.DataAccessPump;

import com.atech.utils.ATechDate;




// this is now used instead ProfileSubEntry, but we need to keep all variables, methods and constructors
public class ProfileSubPattern extends ProfileSubEntry
{

    
    
    /**
     * The time_start.
     */
    public int time_start;
    
    /**
     * The time_end.
     */
    public int time_end;

    
    /**
     * The time_start.
     */
    public long dt_start;
    
    /**
     * The time_end.
     */
    public long dt_end;
    
    
    /**
     * The amount.
     */
    public float amount;
    
    
    
    /**
     * Instantiates a new profile sub entry.
     */
    public ProfileSubPattern()
    {
    }
    
    /**
     * Instantiates a new profile sub entry.
     * 
     * @param input the input
     */
    public ProfileSubPattern(String input)
    {
        String[] ss = input.split("[-=]");
        
        DataAccessPump da = DataAccessPump.getInstance();
        
        time_start = da.getIntValueFromString(ss[0]);
        time_end = da.getIntValueFromString(ss[1]);
        amount = da.getFloatValueFromString(ss[2]);
    }
    
    
    /**
     * Sets the values.
     * 
     * @param pse the new values
     */
    public void setValues(ProfileSubPattern pse)
    {
        this.time_start = pse.time_start;
        this.time_end = pse.time_end;
        this.amount = pse.amount;
    }
    
    /**
     * Gets the packed.
     * 
     * @return the packed
     */
    public String getPacked()
    {
        return time_start + "-" + time_end + "=" + DataAccessPump.Decimal2Format.format(amount);
    }
    
    
    /** 
     * toString
     */
    public String toString()
    {
        return ATechDate.getTimeString(ATechDate.FORMAT_TIME_ONLY_MIN, time_start) + " - " + ATechDate.getTimeString(ATechDate.FORMAT_TIME_ONLY_MIN, time_end) + " = " + DataAccessPump.Decimal2Format.format(amount);
    }

    /**
     * Compare.
     * 
     * @param pse1 the pse1
     * @param pse2 the pse2
     * 
     * @return the int
     */
    public int compare(ProfileSubPattern pse1, ProfileSubPattern pse2)
    {
        if (pse1.time_start == pse2.time_start)
            return (pse1.time_end - pse2.time_end);
        else
            return (pse1.time_start - pse2.time_start);
    }

    /** 
     * compareTo
     */
    public int compareTo(ProfileSubPattern arg0)
    {
        return compare(this, arg0);
    }

    @Override
    public int getType()
    {
        return ProfileSubEntry.PROFILE_SUB_PATTERN;
    }
    
    
    
}