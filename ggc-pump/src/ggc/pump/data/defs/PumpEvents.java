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
 *  Filename:     PumpEvents  
 *  Description:  Pump Events 
 * 
 *  Author: Andy {andy@atech-software.com}
 */


public class PumpEvents
{

    DataAccessPump da = DataAccessPump.getInstance();
    I18nControlAbstract ic = da.getI18nControlInstance();

    /**
     * Events Description
     */
    public String[] events_desc = { ic.getMessage("SELECT_SUBTYPE"),
                       ic.getMessage("EVENT_PRIME_INFUSION_SET"),             
                       ic.getMessage("EVENT_CARTRIDGE_CHANGED"),             
                       ic.getMessage("EVENT_BASAL_RUN"),             
                       ic.getMessage("EVENT_BASAL_STOP"),             
                       ic.getMessage("EVENT_POWER_DOWN"),             
                       ic.getMessage("EVENT_POWER_UP"),             
                       ic.getMessage("EVENT_DATETIME_SET"),             
                       ic.getMessage("EVENT_DATETIME_CORRECT"),             
                       ic.getMessage("EVENT_DATETIME_CORRECT_TIME_SHIFT_BACK"),             
                       ic.getMessage("EVENT_DATETIME_CORRECT_TIME_SHIFT_FORWARD"),             
    };
    
    
    Hashtable<String,String> events_mapping = new Hashtable<String,String>(); 
    
    
    // infussion sets
    /**
     * Pump Event: Prime Infusion Set
     */
    public static final int PUMP_EVENT_PRIME_INFUSION_SET = 1;

    /**
     * Pump Event: Cartridge Changed
     */
    public static final int PUMP_EVENT_CARTRIDGE_CHANGED = 2;
    
    // start / end

    /**
     * Pump Event: Basal Run
     */
    public static final int PUMP_EVENT_BASAL_RUN = 20;

    /**
     * Pump Event: Basal Stop
     */
    public static final int PUMP_EVENT_BASAL_STOP = 21;

    /**
     * Pump Event: Power Down
     */
    public static final int PUMP_EVENT_POWER_DOWN = 22;

    /**
     * Pump Event: Power Up
     */
    public static final int PUMP_EVENT_POWER_UP = 23;
    
    
    // date/time

    /**
     * Pump Event: Date/Time Set
     */
    public static final int PUMP_EVENT_DATETIME_SET = 40;

    /**
     * Pump Event: Date/Time Correct
     */
    public static final int PUMP_EVENT_DATETIME_CORRECT = 41;

    /**
     * Pump Event: Date/Time Correct Time Shift Back
     */
    public static final int PUMP_EVENT_DATETIME_CORRECT_TIME_SHIFT_BACK = 42;

    /**
     * Pump Event: Date/Time Correct Time Shift Forward
     */
    public static final int PUMP_EVENT_DATETIME_CORRECT_TIME_SHIFT_FORWARD = 43;
    
    
    /**
     * Constructor
     */
    public PumpEvents()
    {
        events_mapping.put(ic.getMessage("EVENT_PRIME_INFUSION_SET"), "1");             
        events_mapping.put(ic.getMessage("EVENT_CARTRIDGE_CHANGED"),  "2");            
        events_mapping.put(ic.getMessage("EVENT_BASAL_RUN"), "20");             
        events_mapping.put(ic.getMessage("EVENT_BASAL_STOP"), "21");             
        events_mapping.put(ic.getMessage("EVENT_POWER_DOWN"), "22");             
        events_mapping.put(ic.getMessage("EVENT_POWER_UP"), "23");             
        events_mapping.put(ic.getMessage("EVENT_DATETIME_SET"), "40");             
        events_mapping.put(ic.getMessage("EVENT_DATETIME_CORRECT"), "41");            
        events_mapping.put(ic.getMessage("EVENT_DATETIME_CORRECT_TIME_SHIFT_BACK"), "42");             
        events_mapping.put(ic.getMessage("EVENT_DATETIME_CORRECT_TIME_SHIFT_FORWARD"), "43");             
    }
    

    /**
     * Get Type from Description
     * 
     * @param str type as string
     * @return type as int
     */
    public int getTypeFromDescription(String str)
    {
        String s = "0";
        
        if (this.events_mapping.containsKey(str))
            s = this.events_mapping.get(str);
        
        return Integer.parseInt(s);
        
    }
    
    /**
     * Get Descriptions (array)
     * 
     * @return array of strings with description
     */
    public String[] getDescriptions()
    {
        return this.events_desc;
    }

    
}
