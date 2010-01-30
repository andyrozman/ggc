package ggc.cgms.data;

import ggc.cgms.util.DataAccessCGMS;

import com.atech.misc.statistics.StatisticsCollection;
import com.atech.utils.ATDataAccessAbstract;

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
        super(DataAccessCGMS.getInstance(), new CGMSValuesEntry());
    }
    

    /**
     * Process Special Statistics
     * 
     * @see com.atech.misc.statistics.StatisticsCollection#processSpecialStatistics()
     */
    public void processSpecialStatistics()
    {
        DataAccessCGMS.notImplemented("CGMSDailyStatistics::processSpecialStatistics()");

/*        
        if (this.items.size()==0)
        {
            this.special_processed = true;
            return;
        }
        
        //System.out.println("SS: " + da_pump.getBGMeasurmentType());
        if (da_pump.getBGMeasurmentType()==DataAccessPump.BG_MMOL)
        {
            //setValue(PumpValuesEntry.BG_AVG, da_pump.getBGValueByType(DataAccessPump.BG_MGDL, output_type, bg_value))
            setBGValue(PumpValuesEntry.BG_AVG);
            setBGValue(PumpValuesEntry.BG_MIN);
            setBGValue(PumpValuesEntry.BG_MAX);
        }
        
        
        setValue(PumpValuesEntry.BG_STD_DEV, getStandardDeviation()); 
        
        ArrayList<PumpValuesEntry> lst = new ArrayList<PumpValuesEntry>();
        
        for(int i=0; i<this.items.size(); i++)
        {
            PumpValuesEntry pve = (PumpValuesEntry)this.items.get(i);
            
            if (pve.base_type==PumpBaseType.PUMP_DATA_BASAL)
                lst.add(pve);
        }

        //float v = this.stat_objects.get(index-1).sum;

        //int count = 0;
        float sum = 0;
        
        
        for(int i=0; i<lst.size(); i++)
        {
            PumpValuesEntry pve = lst.get(i);
         
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
                PumpValuesEntry pve2 = lst.get(i+1);

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
            
        }
        
        
        this.stat_objects.get(PumpValuesEntry.INS_SUM_BASAL-1).setSum(sum);
        this.stat_objects.get(PumpValuesEntry.INS_AVG_BASAL-1).setSum(sum/24.0f);
        this.stat_objects.get(PumpValuesEntry.INS_DOSES_BASAL-1).setSum(lst.size());

        this.stat_objects.get(PumpValuesEntry.INS_SUM_TOGETHER-1).setSum(this.getValueInternal(PumpValuesEntry.INS_SUM_BASAL) + this.getValueInternal(PumpValuesEntry.INS_SUM_BOLUS));

        //System.out.println("Avg Basal: " + this.getValueInternal(PumpValuesEntry.INS_AVG_BASAL) + "Avg Bolus: " + this.getValueInternal(PumpValuesEntry.INS_AVG_BOLUS));
        
        //this.stat_objects.get(PumpValuesEntry.INS_AVG_TOGETHER-1).setSum(this.getValueInternal(PumpValuesEntry.INS_AVG_BASAL) + this.getValueInternal(PumpValuesEntry.INS_AVG_BOLUS));
        this.stat_objects.get(PumpValuesEntry.INS_DOSES_TOGETHER-1).setSum(this.getValueInternal(PumpValuesEntry.INS_DOSES_BASAL) + this.getValueInternal(PumpValuesEntry.INS_DOSES_BOLUS));
        
        
        //this.stat_objects.get(PumpValuesEntry.INS_SUM_BASAL-1).setCount(lst.size());

        //this.stat_objects.get(PumpValuesEntry.INS_AVG_BASAL-1).setSum(sum/24.0f);
        //this.stat_objects.get(PumpValuesEntry.INS_SUM_BASAL-1).setCount(lst.size());
        
        
//        public static final int BG_AVG =13;
//        public static final int BG_MAX =14;
//        public static final int BG_MIN =15;
//        public static final int BG_COUNT =16;
//        public static final int BG_STD_DEV =17;
        
        
        
        
        
        
        this.special_processed = true;
        */
    }
    
    
    @SuppressWarnings("unused")
    private boolean isCurrentlyIgnoredEntry(CGMSValuesEntry pve)
    {
        ATDataAccessAbstract.notImplemented("CGMSDailyStatistics::isCurrentlyIgnoredEntry()");
        
        /*
        if (pve.base_type == PumpBaseType.PUMP_DATA_BASAL)
        {
            return (pve.sub_type!=PumpBasalSubType.PUMP_BASAL_VALUE);
        }
        else
            return false;*/
        
        return true;
    }
    
    
    
    @SuppressWarnings("unused")
    private float getStandardDeviation()
    {

        /*
        float f = this.getValueInternal(PumpValuesEntry.BG_AVG) - this.getValueInternal(PumpValuesEntry.BG_MIN);

        if (f < 0)
        {
            setValue(PumpValuesEntry.BG_MIN, 0.0f);
            return 0.0f;
        }
        else
            return f;
            */
        
        return 0.0f;
    }
    
    
    
    @SuppressWarnings("unused")
    private void setBGValue(int index)
    {
        /*
        float v = this.stat_objects.get(index-1).sum;
        float new_val = da_pump.getBGValueDifferent(DataAccessPump.BG_MGDL, v);
        
        setValue(index, new_val);
        */
    }
    
    @SuppressWarnings("unused")
    private float getValueInternal(int index)
    {
        return this.stat_objects.get(index-1).getStatistics();
    }
    
    
    @SuppressWarnings("unused")
    private void setValue(int index, float val)
    {
        this.stat_objects.get(index-1).sum = val;
    }
    
    
	
}	
