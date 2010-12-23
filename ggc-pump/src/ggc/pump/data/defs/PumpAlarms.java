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
 *  Filename:  ###---###  
 *  Description:
 * 
 *  Author: Andy {andy@atech-software.com}
 */


public class PumpAlarms extends PumpDefsAbstract
{


    /**
     * Alarm Descriptions
     */
/*    private String[] alarms_desc = {  
                       ic.getMessage("ALARM_UNKNOWN"),
                       ic.getMessage("ALARM_CARTRIDGE_LOW"),             
                       ic.getMessage("ALARM_BATTERY_LOW"),             
                       ic.getMessage("ALARM_REVIEW_DATETIME"),             
                       ic.getMessage("ALARM_ALARM_CLOCK"),             
                       ic.getMessage("ALARM_PUMP_TIMER"),             
                       ic.getMessage("ALARM_TEMPORARY_BASAL_RATE_CANCELED"),             
                       ic.getMessage("ALARM_TEMPORARY_BASAL_RATE_OVER"),             
                       ic.getMessage("ALARM_BOLUS_CANCELED"),             
    };*/
    
/*
    private String[] alarms_desc_comp = {  
                                     ic.getMessage("SELECT_SUBTYPE"),
                                     ic.getMessage("ALARM_CARTRIDGE_LOW"),             
                                     ic.getMessage("ALARM_BATTERY_LOW"),             
                                     ic.getMessage("ALARM_REVIEW_DATETIME"),             
                                     ic.getMessage("ALARM_ALARM_CLOCK"),             
                                     ic.getMessage("ALARM_PUMP_TIMER"),             
                                     ic.getMessage("ALARM_TEMPORARY_BASAL_RATE_CANCELED"),             
                                     ic.getMessage("ALARM_TEMPORARY_BASAL_RATE_OVER"),             
                                     ic.getMessage("ALARM_BOLUS_CANCELED"),             
                  };
  */  
    
    
    //Hashtable<String,String> alarms_mapping = new Hashtable<String,String>(); 
    

    /**
     * Pump Alarm: Cartridge Low
     */
    public final static int PUMP_ALARM_UNKNOWN = 0;
    
    
    /**
     * Pump Alarm: Cartridge Low
     */
    public final static int PUMP_ALARM_CARTRIDGE_LOW = 1;

    /**
     * Pump Alarm: Battery Low
     */
    public final static int PUMP_ALARM_BATTERY_LOW = 2;

    /**
     * Pump Alarm: Review Datetime
     */
    public final static int PUMP_ALARM_REVIEW_DATETIME = 3;

    /**
     * Pump Alarm: Alarm Clock
     */
    public final static int PUMP_ALARM_ALARM_CLOCK = 4;

    /**
     * Pump Alarm: Pump Timer
     */
    public final static int PUMP_ALARM_PUMP_TIMER = 5;

    /**
     * Pump Alarm: Temporary Basal Rate Canceled
     */
    public final static int PUMP_ALARM_TEMPORARY_BASAL_RATE_CANCELED = 6;

    /**
     * Pump Alarm: Temporary Basal Rate Over
     */
    public final static int PUMP_ALARM_TEMPORARY_BASAL_RATE_OVER = 7;

    /**
     * Pump Alarm: Bolus Canceled
     */
    public final static int PUMP_ALARM_BOLUS_CANCELED = 8;
    

    /**
     * Pump Alarm: No Delivery
     */
    public final static int PUMP_ALARM_NO_DELIVERY = 10;
    
    
    
    /**
     * Constructor
     */
    public PumpAlarms()
    {
        super();
        this.setDataDesc(PumpAlarms.PUMP_ALARM_UNKNOWN,ic.getMessage("ALARM_UNKNOWN"));             
        this.setDataDesc(PumpAlarms.PUMP_ALARM_CARTRIDGE_LOW, ic.getMessage("ALARM_CARTRIDGE_LOW"));             
        this.setDataDesc(PumpAlarms.PUMP_ALARM_BATTERY_LOW, ic.getMessage("ALARM_BATTERY_LOW"));             
        this.setDataDesc(PumpAlarms.PUMP_ALARM_REVIEW_DATETIME, ic.getMessage("ALARM_REVIEW_DATETIME"));             
        this.setDataDesc(PumpAlarms.PUMP_ALARM_ALARM_CLOCK, ic.getMessage("ALARM_ALARM_CLOCK"));             
        this.setDataDesc(PumpAlarms.PUMP_ALARM_PUMP_TIMER, ic.getMessage("ALARM_PUMP_TIMER"));             
        this.setDataDesc(PumpAlarms.PUMP_ALARM_TEMPORARY_BASAL_RATE_CANCELED, ic.getMessage("ALARM_TEMPORARY_BASAL_RATE_CANCELED"));            
        this.setDataDesc(PumpAlarms.PUMP_ALARM_TEMPORARY_BASAL_RATE_OVER, ic.getMessage("ALARM_TEMPORARY_BASAL_RATE_OVER"));             
        this.setDataDesc(PumpAlarms.PUMP_ALARM_BOLUS_CANCELED, ic.getMessage("ALARM_BOLUS_CANCELED"));
        this.setDataDesc(PumpAlarms.PUMP_ALARM_NO_DELIVERY, ic.getMessage("ALARM_NO_DELIVERY"));
    }

    
    /**
     * Get Type from Description
     * 
     * @param str type as string
     * @return type as int
     */
/*    public int getTypeFromDescription(String str)
    {
        String s = "0";
        
        if (this.alarms_mapping.containsKey(str))
            s = this.alarms_mapping.get(str);
        else
            s = "0";
        
        return Integer.parseInt(s);
    }*/
    
    /**
     * Get Descriptions
     * 
     * @return
     */
/*    public String[] getDescriptions()
    {
        return alarms_desc_comp;
    }*/
    
    
    
    
    
    
    
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