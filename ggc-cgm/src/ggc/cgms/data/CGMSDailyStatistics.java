package ggc.cgms.data;

import ggc.cgms.util.DataAccessCGMS;

import java.util.ArrayList;

import com.atech.misc.statistics.StatisticsCollection;

/**
 *  Application:   GGC - GNU Gluco Control
 *  Plug-in:       Pump Tool (support for Pump devices)
 *
 *  See AUTHORS for copyright information.
 * 
 *  This program is free software; you can redistribute it and/or modify it under
 *  the terms of the GNU General Public License as published by the Free Software
 *  Foundation; either version 2 of the License, or (at your option) any later
 *  version.
 * 
 *  This program is distributed in the hope that it will be useful, but WITHOUT
 *  ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 *  FOR A PARTICULAR PURPOSE. See the GNU General Public License for more
 *  details.
 * 
 *  You should have received a copy of the GNU General Public License along with
 *  this program; if not, write to the Free Software Foundation, Inc., 59 Temple
 *  Place, Suite 330, Boston, MA 02111-1307 USA
 * 
 *  Filename:  ###---###  
 *  Description:
 * 
 *  Author: Andy {andy@atech-software.com}
 */


public class CGMSDailyStatistics extends StatisticsCollection 
{
    //public int bg_type = -1;
    DataAccessCGMS da_pump = DataAccessCGMS.getInstance();
    
    /**
     * Constructor
     */
    public CGMSDailyStatistics()
    {
        super(DataAccessCGMS.getInstance(), new CGMSValuesSubEntry());
    }
    

    /**
     * Process Special Statistics
     * 
     * @see com.atech.misc.statistics.StatisticsCollection#processSpecialStatistics()
     */
    public void processSpecialStatistics()
    {
        //DataAccessCGMS.notImplemented("CGMSDailyStatistics::processSpecialStatistics()");

        
        if (this.items.size()==0)
        {
            this.special_processed = true;
            return;
        }
        
        //System.out.println("SS: " + da_pump.getBGMeasurmentType());
        if (da_pump.getBGMeasurmentType()==DataAccessCGMS.BG_MMOL)
        {
            setBGValue(CGMSValuesSubEntry.STAT_AVG_BG1);
            setBGValue(CGMSValuesSubEntry.STAT_MIN_BG1);
            setBGValue(CGMSValuesSubEntry.STAT_MAX_BG1);
            setBGValue(CGMSValuesSubEntry.STAT_AVG_BG2);
            setBGValue(CGMSValuesSubEntry.STAT_MIN_BG2);
            setBGValue(CGMSValuesSubEntry.STAT_MAX_BG2);
        }
        
        
        setValue(CGMSValuesSubEntry.STAT_STD_DEV_BG1, getStandardDeviation(1)); 
        setValue(CGMSValuesSubEntry.STAT_STD_DEV_BG2, getStandardDeviation(2)); 
        
        ArrayList<CGMSValuesSubEntry> lst = new ArrayList<CGMSValuesSubEntry>();
        
        /*
        for(int i=0; i<this.items.size(); i++)
        {
            CGMSValuesSubEntry pve = (CGMSValuesSubEntry)this.items.get(i);
            
            if (pve.base_type==PumpBaseType.PUMP_DATA_BASAL)
                lst.add(pve);
        }*/

        //float v = this.stat_objects.get(index-1).sum;

        //int count = 0;
        float sum = 0;
        
        
        for(int i=0; i<lst.size(); i++)
        {
            CGMSValuesSubEntry pve = lst.get(i);
         
            if (isCurrentlyIgnoredEntry(pve))
                continue;
            
//            if ((pve.base_type == PumpBaseType.PUMP_DATA_BASAL) && 
//                (pve.sub_type == PumpBasalSubType.PUMP_BASAL_PROFILE))
//                continue;

            if ((i+1)==lst.size())
            {
                int s = 24 - pve.getDateTimeObject().hour_of_day;
                float val = m_da.getFloatValueFromString(pve.getValue());
                sum += s * val; 
                
            //    System.out.println("Time diff: " + s + ", val=" + val);
            }
            else
            {
                CGMSValuesSubEntry pve2 = lst.get(i+1);

                if (isCurrentlyIgnoredEntry(pve2))
                    continue;
                
//                if ((pve2.base_type == PumpBaseType.PUMP_DATA_BASAL) && 
//                    (pve2.sub_type == PumpBasalSubType.PUMP_BASAL_PROFILE))
//                      continue;
                
                
                //System.out.println("Hour: " + pve2.getDateTimeObject().hour_of_day + ", hour2=" + pve.getDateTimeObject().hour_of_day);

                //System.out.println("pve2: " + pve2.getBaseTypeString() + pve2.getSubTypeString());
                
                int s = pve2.getDateTimeObject().hour_of_day - pve.getDateTimeObject().hour_of_day;
                float val = da_pump.getFloatValueFromString(pve.getValue());
                sum += s * val; 

                //System.out.println("Time diff: " + s + ", val=" + val);
            }
         
            System.out.println("Sum: " + sum);
        }
        
        
        
        //this.stat_objects.get(PumpValuesEntry.INS_SUM_BASAL-1).setCount(lst.size());

        //this.stat_objects.get(PumpValuesEntry.INS_AVG_BASAL-1).setSum(sum/24.0f);
        //this.stat_objects.get(PumpValuesEntry.INS_SUM_BASAL-1).setCount(lst.size());
        
        
//        public static final int BG_AVG =13;
//        public static final int BG_MAX =14;
//        public static final int BG_MIN =15;
//        public static final int BG_COUNT =16;
//        public static final int BG_STD_DEV =17;
        
        
        
        
        
        
        this.special_processed = true;

    }
    
    
    private boolean isCurrentlyIgnoredEntry(CGMSValuesSubEntry pve)
    {
        return false;
    }
    
    
    private float getStandardDeviation(int type)
    {

        if (type==1)
        {
            float f = this.getValueInternal(CGMSValuesSubEntry.STAT_AVG_BG1) - this.getValueInternal(CGMSValuesSubEntry.STAT_MIN_BG1);

            if (f < 0)
            {
                setValue(CGMSValuesSubEntry.STAT_MIN_BG1, 0.0f);
                return 0.0f;
            }
            else
                return f;
        }
        else
        {
            float f = this.getValueInternal(CGMSValuesSubEntry.STAT_AVG_BG2) - this.getValueInternal(CGMSValuesSubEntry.STAT_MIN_BG2);

            if (f < 0)
            {
                setValue(CGMSValuesSubEntry.STAT_MIN_BG2, 0.0f);
                return 0.0f;
            }
            else
                return f;
        }
        
    }
    
    
    private void setBGValue(int index)
    {
        float v = this.stat_objects.get(index-1).sum;
        float new_val = da_pump.getBGValueDifferent(DataAccessCGMS.BG_MGDL, v);
        
        setValue(index, new_val);
    }
    
    private float getValueInternal(int index)
    {
        return this.stat_objects.get(index-1).getStatistics();
    }
    
    
    private void setValue(int index, float val)
    {
        this.stat_objects.get(index-1).sum = val;
    }
    
    
	
}	
