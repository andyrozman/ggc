package ggc.pump.data.defs;

import ggc.pump.util.DataAccessPump;

import java.util.Hashtable;

import com.atech.i18n.I18nControlAbstract;

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


public class PumpAlarms
{

    DataAccessPump da = DataAccessPump.getInstance();
    I18nControlAbstract ic = da.getI18nControlInstance();

    public String[] alarms_desc = { ic.getMessage("SELECT_SUBTYPE"),
                       ic.getMessage("ALARM_CARTRIDGE_LOW"),             
                       ic.getMessage("ALARM_BATTERY_LOW"),             
                       ic.getMessage("ALARM_REVIEW_DATETIME"),             
                       ic.getMessage("ALARM_ALARM_CLOCK"),             
                       ic.getMessage("ALARM_PUMP_TIMER"),             
                       ic.getMessage("ALARM_TEMPORARY_BASAL_RATE_CANCELED"),             
                       ic.getMessage("ALARM_TEMPORARY_BASAL_RATE_OVER"),             
                       ic.getMessage("ALARM_BOLUS_CANCELED"),             
    };
    
    
    Hashtable<String,String> alarms_mapping = new Hashtable<String,String>(); 
    
    
    
    public final static int PUMP_ALARM_CARTRIDGE_LOW = 1;
    public final static int PUMP_ALARM_BATTERY_LOW = 2;
    public final static int PUMP_ALARM_REVIEW_DATETIME = 3;
    public final static int PUMP_ALARM_ALARM_CLOCK = 4;
    public final static int PUMP_ALARM_PUMP_TIMER = 5;
    public final static int PUMP_ALARM_TEMPORARY_BASAL_RATE_CANCELED = 6;
    public final static int PUMP_ALARM_TEMPORARY_BASAL_RATE_OVER = 7;
    public final static int PUMP_ALARM_BOLUS_CANCELED = 8;
    

    
    public PumpAlarms()
    {
        alarms_mapping.put(ic.getMessage("ALARM_CARTRIDGE_LOW"), "1");             
        alarms_mapping.put(ic.getMessage("ALARM_BATTERY_LOW"), "2");             
        alarms_mapping.put(ic.getMessage("ALARM_REVIEW_DATETIME"), "3");             
        alarms_mapping.put(ic.getMessage("ALARM_ALARM_CLOCK"), "4");             
        alarms_mapping.put(ic.getMessage("ALARM_PUMP_TIMER"), "5");             
        alarms_mapping.put(ic.getMessage("ALARM_TEMPORARY_BASAL_RATE_CANCELED"), "6");             
        alarms_mapping.put(ic.getMessage("ALARM_TEMPORARY_BASAL_RATE_OVER"), "7");             
        alarms_mapping.put(ic.getMessage("ALARM_BOLUS_CANCELED"), "8");             
    }
    
    
    public int getTypeFromDescription(String str)
    {
        String s = "0";
        
        if (this.alarms_mapping.containsKey(str))
            s = this.alarms_mapping.get(str);
        
        return Integer.parseInt(s);
    }
    
    public String[] getDescriptions()
    {
        return this.alarms_desc;
    }
    
    
    
    
    
/*  
 * 
 *   Minimed

 No Delivery (4)

 
 
    Sensor Alert: High Glucose (101)
    Sensor Alert: Low Glucose (102)
    Sensor Alert: Meter BG Now (104)
    Sensor Alarm (105)
    Sensor Alert: Calibration Error (106)
    Sensor Alert: Sensor End (107)
    Sensor Alert: Change Sensor (108)
    Sensor Alert: Sensor Error (109)
    Sensor Alert: Weak Signal (112)
    Sensor Alert: Lost Sensor (113)
    Sensor Alert: High Glucose Predicted (114)
    Sensor Alert: Low Glucose Predicted (115)
*/
    

}
