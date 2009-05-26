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
                       ic.getMessage("EVENT_CARTRIDGE_REWIND")
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

    /**
     * Pump Event: Cartridge Changed
     */
    public static final int PUMP_EVENT_CARTRIDGE_REWIND = 3;
    
    
    /**
     * Pump Event: Reservoir Low
     */
    public static final int PUMP_EVENT_RESERVOIR_LOW = 4;
    
    /**
     * Pump Event: Reservoir Low
     */
    public static final int PUMP_EVENT_RESERVOIR_LOW_DESC = 5;
    
    /**
     * Pump Event: Temporary Basal Rate, Unit setting (1=%, 0=U)
     */
    public static final int PUMP_EVENT_SET_TEMPORARY_BASAL_RATE_TYPE = 10;
    
    /**
     * Pump Event: Basal Pattern Set
     */
    public static final int PUMP_EVENT_SET_BASAL_PATTERN = 15;
    
    
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
    
        
    /**
     * Pump Event: Self Test
     */
    public static final int PUMP_EVENT_SELF_TEST = 30;

    
    /**
     * Pump Event: Download data
     */
    public static final int PUMP_EVENT_DOWNLOAD = 31;
    
    
    // date/time

    /**
     * Pump Event: Date/Time Set
     */
    public static final int PUMP_EVENT_DATETIME_SET = 40;

    /**
     * Pump Event: Date/Time Correct
     */
    public static final int PUMP_EVENT_DATETIME_CORRECTED = 41;

    
    
    /**
     * Pump Event: Set Max Basal
     */
    public static final int PUMP_EVENT_SET_MAX_BASAL = 50;
    
    /**
     * Pump Event: Set Max Bolus
     */
    public static final int PUMP_EVENT_SET_MAX_BOLUS = 51;
    
    
    /**
     * Pump Event: Battery Removed
     */
    public static final int PUMP_EVENT_BATERRY_REMOVED = 55;
    
    /**
     * Pump Event: Battery Replaced
     */
    public static final int PUMP_EVENT_BATERRY_REPLACED = 56;
    
    /**
     * Pump Event: Battery Low
     */
    public static final int PUMP_EVENT_BATERRY_LOW = 57;
    
    /**
     * Pump Event: Battery Low (Desc)
     */
    public static final int PUMP_EVENT_BATERRY_LOW_DESC = 58;

    
    /**
     * Pump Event: BG From Meter
     */
    public static final int PUMP_EVENT_BG_FROM_METER = 70;
    
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
        events_mapping.put(ic.getMessage("EVENT_REWIND_INFUSION_SET"), "3");
    }
    

    /**
     * Get Type from Description
     * 
     * @param str type as string
     * @return type as int
     */
    public int getTypeFromDescription(String str)
    {
        // TODO
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

    
    /**
     * Shows if this type of event has value associated with it (most don't) 
     * @param type
     * @return
     */
    public boolean hasValue(int type)
    {
        switch (type)
        {
            case PUMP_EVENT_SET_MAX_BASAL:
            case PUMP_EVENT_SET_MAX_BOLUS:
            case PUMP_EVENT_BATERRY_LOW_DESC:
                return true;
        
            default:
                return false;
        }
    }
    
    
}
