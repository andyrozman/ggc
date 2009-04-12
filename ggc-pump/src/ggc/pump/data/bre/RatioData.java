package ggc.pump.data.bre;

import ggc.pump.util.DataAccessPump;

import com.atech.utils.ATechDate;

// TODO: Auto-generated Javadoc
/**
 * The Class RatioData.
 */
public class RatioData 
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
     * The ratio_ch_insulin.
     */
    public float ratio_ch_insulin = 0.0f;
    
    /**
     * The ratio_bg_insulin.
     */
    public float ratio_bg_insulin = 0.0f;
    
    /**
     * The ratio_ch_bg.
     */
    public float ratio_ch_bg = 0.0f;
    
    
    
    
    /**
     * Constructor
     * 
     * @param time_i
     * @param ratio_ch_insulin 
     * @param ratio_bg_insulin 
     * @param ratio_ch_bg 
     */
    public RatioData(int time_i, float ratio_ch_insulin, float ratio_bg_insulin, float ratio_ch_bg)
    {
        this.time_start = time_i;
        this.ratio_ch_insulin = ratio_ch_insulin;
        this.ratio_bg_insulin = ratio_bg_insulin;
        this.ratio_ch_bg = ratio_ch_bg;
    }

  
    
    /** 
     * toString
     */
    public String toString()
    {
        ATechDate atd = new ATechDate(ATechDate.FORMAT_TIME_ONLY_MIN, time_start);
        return "<html>" + atd.getTimeString() + "&nbsp;&nbsp;&nbsp;&nbsp;" + "<font color=\"red\">CH/I: &nbsp;&nbsp;&nbsp;&nbsp;" + DataAccessPump.Decimal2Format.format(ratio_ch_insulin) + "</font><br>" +
        atd.getTimeString() + "&nbsp;&nbsp;&nbsp;&nbsp;" + "<font color=\"blue\">BG/I: &nbsp;&nbsp;&nbsp;&nbsp;" + DataAccessPump.Decimal2Format.format(this.ratio_bg_insulin) + "</font><br>" + 
        atd.getTimeString() + "&nbsp;&nbsp;&nbsp;&nbsp;" + "<font color=\"green\">CH/BG: &nbsp;&nbsp;&nbsp;&nbsp;" + DataAccessPump.Decimal2Format.format(this.ratio_ch_bg) + "</font><br></html>";
    }
    
    
}

