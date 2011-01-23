package ggc.plugin.device.impl.minimed.file;

import ggc.plugin.util.DataAccessPlugInBase;

import com.atech.utils.data.ATechDate;

public class MinimedCareLinkDate
{
    
    protected DataAccessPlugInBase m_da = null;

    public String date_delimiter = "";
    public String time_delimiter = "";
    
    public String[] codes = { null, null, null };
    
    public int[] month_search = { -1, -1 };
    
    public boolean first = true;
    
    public int month_part = -1;
    public int day_part = -1;
    public int year_part = -1;
    
    
    public MinimedCareLinkDate(DataAccessPlugInBase da)
    {
        this.m_da = da;
    }
    
    
    
    public boolean compareTwoDates(String d1, String d2)
    {
        if (first)
        {
            // create delimiter
            if (d1.contains("."))
                date_delimiter = ".";
            else
                date_delimiter = "/";
            
            
            String d11[] = m_da.splitString(d1, date_delimiter); 
            String d21[] = m_da.splitString(d2, date_delimiter); 
            int i=0;
            
            for(i=0; i<d11.length; i++)
            {
                if (!d11[i].equals(d21[i]))
                {
                    codes[i] = "d";
                    day_part = i;
                    break;
                }
            }
            
            // month search sett
            int j = 0;
            for(i=0; i<3; i++)
            {
                if (codes[i] == null)
                {
                    month_search[j] = i;
                    j++;
                }
            }
            
            first = false;
            return false;
            
        }
        else
        {
            // month searching
            
            String d11[] = m_da.splitString(d1, date_delimiter); 
            String d21[] = m_da.splitString(d2, date_delimiter); 
            
            // if we have 31 day, we skip this check because it might be 31 december
            if (d11[this.day_part].equals("31"))
                return false;
            
            if ((!d11[this.month_search[0]].equals(d21[this.month_search[0]])) ||
                (!d11[this.month_search[1]].equals(d21[this.month_search[1]])))
            {
                
                if (!d11[this.month_search[0]].equals(d21[this.month_search[0]]))
                {
                    codes[this.month_search[0]] = "m";
                    codes[this.month_search[1]] = "yy";
                    month_part = this.month_search[0];
                    year_part = this.month_search[1];
                }
                else
                {
                    codes[this.month_search[0]] = "yy";
                    codes[this.month_search[1]] = "m";
                    month_part = this.month_search[1];
                    year_part = this.month_search[0];
                }

                //System.out.println("d2: " + d2);
                //System.out.println("Codes: " + codes[0] + this.date_delimiter + codes[1] + this.date_delimiter + codes[2] );
                
                return true;
            }
            
            
            
            return false;
        }
        
        
        
    }
    
    
    public void resolveTime(String time)
    {
        if (time.contains(":"))
            time_delimiter = ":";
        else if (time.contains("."))
            time_delimiter = ".";
    }
    
    
    public long getAtechDateLong(String date, String time)
    {
        
        String[] time_p = m_da.splitString(time, time_delimiter);
        
        long dt = Integer.parseInt(time_p[2]);
        dt += Long.parseLong(time_p[1]) * 100;
        dt += Long.parseLong(time_p[0]) * 10000;
        
        String[] date_p = m_da.splitString(date, date_delimiter);
        
        dt += Long.parseLong(date_p[this.day_part]) * 1000000;
        dt += Long.parseLong(date_p[this.month_part]) * 100000000;
        
        
        String y = null;
        
        if (date_p[this.year_part].length()==2)
        {
            int y1 = Integer.parseInt(date_p[this.year_part]);
            
            if (y1>70) // if > 70 then we say it's 1970, if less, we say it's 20xx
                y = "19";
            else
                y = "20";
            
            y += date_p[this.year_part];
        }
        else
            y = date_p[this.year_part];
        
        
        dt += Long.parseLong(y) * 10000000000L;
        
        return dt;
    }
    
    
    public ATechDate getAtechDate(String date, String time)
    {
        return new ATechDate(ATechDate.FORMAT_DATE_AND_TIME_S, getAtechDateLong(date, time));
    }
    

}