package ggc.pump.data.bre;

import ggc.pump.util.DataAccessPump;

import com.atech.utils.ATechDate;

public class BREData 
{
    
    public static final int BRE_DATA_NONE = 0;
    public static final int BRE_DATA_BG = 1;
    public static final int BRE_DATA_BASAL_OLD = 2;
    public static final int BRE_DATA_BASAL_NEW = 3;

    public static final int BRE_DATA_BASAL_RATIO = 4;
    
    public static final int BRE_DATA_BASAL_RATIO_GRAPH = 100;
    
    
    /**
     * Constructor
     * 
     * @param time_i
     * @param value
     * @param type
     */
    public BREData(int time_i, float value, int type)
    {
        this.time = time_i;
        this.data_type = type;
        this.value = value;
    }

  
    
    public int data_type = 0;
    public int time;
    public int time_end;
    public float value = 0.0f;
    
    
    public boolean areWeInTimeRange(int time_q)
    {
        if ((time_q >= time) && (time_q <= time_end))
            return true;
        else
            return false;
    }
    
    
    
    
    public String toString()
    {
        ATechDate atd = new ATechDate(ATechDate.FORMAT_TIME_ONLY_MIN, time);
        
        StringBuffer sb = new StringBuffer("<html>");
        sb.append(atd.getTimeString());
        sb.append("&nbsp;&nbsp;&nbsp;&nbsp;");
        
        switch(data_type)
        {
            case BREData.BRE_DATA_BG:
            case BREData.BRE_DATA_BASAL_OLD:
                sb.append("<font color=\"green\">");
                break;
                
            case BREData.BRE_DATA_BASAL_NEW:
                sb.append("<font color=\"blue\">");
                break;
                
             default:
                sb.append("<font color=\"black\">");
                break;
                    
        }
        
        sb.append(DataAccessPump.Decimal2Format.format(value));
        sb.append("</font></html>");
        
        return sb.toString();
//        return atd.getTimeString() + "           " + DataAccessPump.Decimal1Format.format(value);
    }
    
    
}

