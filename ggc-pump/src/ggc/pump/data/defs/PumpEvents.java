package ggc.pump.data.defs;


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


public class PumpEvents extends PumpDefsAbstract
{

    
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
     * Pump Event: Bolus Cancelled
     */
    public static final int PUMP_EVENT_BOLUS_CANCELLED   = 80;
    
    
    /**
     * Pump Event: Bolus Wizard
     */
    public static final int PUMP_EVENT_BOLUS_WIZARD      = 81;
    
    
    /**
     * Constructor
     */
    public PumpEvents()
    {
        super();
        setDataDesc(PumpEvents.PUMP_EVENT_PRIME_INFUSION_SET, "EVENT_PRIME_INFUSION_SET");
        setDataDesc(PumpEvents.PUMP_EVENT_CARTRIDGE_CHANGED, "EVENT_CARTRIDGE_CHANGED");     
        setDataDesc(PumpEvents.PUMP_EVENT_CARTRIDGE_REWIND, "EVENT_REWIND_INFUSION_SET");
        setDataDesc(PumpEvents.PUMP_EVENT_RESERVOIR_LOW, "EVENT_RESERVOIR_LOW");
        setDataDesc(PumpEvents.PUMP_EVENT_RESERVOIR_LOW_DESC, "EVENT_RESERVOIR_LOW_DESC");
        setDataDesc(PumpEvents.PUMP_EVENT_SET_TEMPORARY_BASAL_RATE_TYPE, "EVENT_SET_TEMPORARY_BASAL_RATE_TYPE");
        setDataDesc(PumpEvents.PUMP_EVENT_SET_BASAL_PATTERN, "EVENT_SET_BASAL_PATTERN");
        
        setDataDesc(PumpEvents.PUMP_EVENT_BASAL_RUN, "EVENT_BASAL_RUN");
        setDataDesc(PumpEvents.PUMP_EVENT_BASAL_STOP, "EVENT_BASAL_STOP");
        setDataDesc(PumpEvents.PUMP_EVENT_POWER_DOWN, "EVENT_POWER_DOWN");
        setDataDesc(PumpEvents.PUMP_EVENT_POWER_UP, "EVENT_POWER_UP");
        
        setDataDesc(PumpEvents.PUMP_EVENT_SELF_TEST, "EVENT_SELF_TEST");
        setDataDesc(PumpEvents.PUMP_EVENT_DOWNLOAD, "EVENT_DOWNLOAD");
        
        setDataDesc(PumpEvents.PUMP_EVENT_DATETIME_SET, "EVENT_DATETIME_SET");
        setDataDesc(PumpEvents.PUMP_EVENT_DATETIME_CORRECTED, "EVENT_DATETIME_CORRECT");
        
        setDataDesc(PumpEvents.PUMP_EVENT_SET_MAX_BASAL, "EVENT_SET_MAX_BASAL");
        setDataDesc(PumpEvents.PUMP_EVENT_SET_MAX_BOLUS, "EVENT_SET_MAX_BOLUS");

        setDataDesc(PumpEvents.PUMP_EVENT_BATERRY_REMOVED, "EVENT_BATERRY_REMOVED");
        setDataDesc(PumpEvents.PUMP_EVENT_BATERRY_REPLACED, "EVENT_BATERRY_REPLACED");
        setDataDesc(PumpEvents.PUMP_EVENT_BATERRY_LOW, "EVENT_BATERRY_LOW");
        setDataDesc(PumpEvents.PUMP_EVENT_BATERRY_LOW_DESC, "EVENT_BATERRY_LOW_DESC");

        setDataDesc(PumpEvents.PUMP_EVENT_BG_FROM_METER, "EVENT_BG_FROM_METER");
        // EVENT_SET_MAX_BASAL, EVENT_SET_MAX_BOLUS
        // EVENT_BATERRY_REMOVED, EVENT_BATERRY_REPLACED, EVENT_BATERRY_LOW, EVENT_BATERRY_LOW_DESC, EVENT_BG_FROM_METER

        setDataDesc(PumpEvents.PUMP_EVENT_BOLUS_CANCELLED, "ALARM_BOLUS_CANCELED");
        
        String s = "BG=%s;CH=%s;CH_UNIT=%s;CH_INS_RATIO=%s;BG_INS_RATIO=%s;BG_TARGET_LOW=%s;";
        s += "BG_TARGET_HIGH=%s;BOLUS_TOTAL=%s;BOLUS_CORRECTION=%s;BOLUS_FOOD=%s;";
        s += "UNABSORBED_INSULIN=%s";

        
        setDataDesc(PumpEvents.PUMP_EVENT_BOLUS_WIZARD, "EVENT_BOLUS_WIZARD", s);
        
        
        // EVENT_RESERVOIR_LOW, EVENT_RESERVOIR_LOW_DESC, EVENT_SET_TEMPORARY_BASAL_RATE_TYPE, EVENT_SET_BASAL_PATTERN
     // EVENT_SELF_TEST, EVENT_DOWNLOAD
        
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
