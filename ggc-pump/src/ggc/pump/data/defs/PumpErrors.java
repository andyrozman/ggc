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
 *  Filename:     PumpErrors  
 *  Description:  Pump Errors 
 * 
 *  Author: Andy {andy@atech-software.com}
 */


public class PumpErrors
{
    
    DataAccessPump da = DataAccessPump.getInstance();
    I18nControlAbstract ic = da.getI18nControlInstance();

    /**
     * Errors Description
     */
    public String[] errors_desc = { ic.getMessage("SELECT_SUBTYPE"),
                       ic.getMessage("ERROR_UNKNOWN_ERROR"),             
                       ic.getMessage("ERROR_CARTRIDGE_EMPTY"),             
                       ic.getMessage("ERROR_BATTERY_DEPLETED"),             
                       ic.getMessage("ERROR_AUTOMATIC_OFF"),             
                       ic.getMessage("ERROR_NO_DELIVERY"),             
                       ic.getMessage("ERROR_END_OF_OPERATION"),             
                       ic.getMessage("ERROR_MECHANICAL_ERROR"),             
                       ic.getMessage("ERROR_ELECTRONIC_ERROR"),             
                       ic.getMessage("ERROR_POWER_INTERRUPT"),             
                       ic.getMessage("ERROR_CARTRIDGE_ERROR"),             
                       ic.getMessage("ERROR_SET_NOT_PRIMED"),             
                       ic.getMessage("ERROR_DATA_INTERRUPTED"),             
                       ic.getMessage("ERROR_LANGUAGE_ERROR"),             
                       ic.getMessage("ERROR_INSULIN_CHANGED"),             
    };
    
    
    Hashtable<String,String> errors_mapping = new Hashtable<String,String>(); 
    
    /**
     * Pump Error: Unknown Error
     */
    public static final int PUMP_ERROR_UNKNOWN_ERROR  = 0; //__________________________151

    /**
     * Pump Error: Cartridge Empty
     */
    public static final int PUMP_ERROR_CARTRIDGE_EMPTY  = 1; //__________________________151
    
    /**
     * Pump Error: Battery Depleted
     */
    public static final int PUMP_ERROR_BATTERY_DEPLETED =2;//__________________________152
    
    /**
     * Pump Error: Automatic Off
     */
    public static final int PUMP_ERROR_AUTOMATIC_OFF =3; //_____________________________152
    
    /**
     * Pump Error: No Delivery
     */
    public static final int PUMP_ERROR_NO_DELIVERY = 4;  // minimed 'No Delivery'=4, roche 'Occlusion'=4

    /**
     * Pump Error: End of Operation
     */
    public static final int PUMP_ERROR_END_OF_OPERATION = 5; //__________________________154
    
    /**
     * Pump Error: Mechanical Error
     */
    public static final int PUMP_ERROR_MECHANICAL_ERROR = 6; //_________________________155
    
    /**
     * Pump Error: Electronic Error
     */
    public static final int PUMP_ERROR_ELECTRONIC_ERROR = 7; //_________________________156
    
    /**
     * Pump Error: Power Interrupt
     */
    public static final int PUMP_ERROR_POWER_INTERRUPT = 8; //__________________________157
    
    /**
     * Pump Error: Cartridge Error
     */
    public static final int PUMP_ERROR_CARTRIDGE_ERROR = 10; // _________________________158
    
    /**
     * Pump Error: Set Not Primed
     */
    public static final int PUMP_ERROR_SET_NOT_PRIMED = 11; //___________________________158
    
    /**
     * Pump Error: Data Interrupted
     */
    public static final int PUMP_ERROR_DATA_INTERRUPTED = 12; //_________________________159
    
    /**
     * Pump Error: Language Error
     */
   public static final int PUMP_ERROR_LANGUAGE_ERROR = 13; //__________________________160

   /**
    * Pump Error: Insulin Changed
    */
    public static final int PUMP_ERROR_INSULIN_CHANGED = 14; //__________________________    
    
    
    
    /**
     * Constructor
     */
    public PumpErrors()
    {
        this.errors_mapping.put(ic.getMessage("ERROR_UNKNOWN_ERROR"), "0");             
        this.errors_mapping.put(ic.getMessage("ERROR_CARTRIDGE_EMPTY"), "1");             
        this.errors_mapping.put(ic.getMessage("ERROR_BATTERY_DEPLETED"), "2");             
        this.errors_mapping.put(ic.getMessage("ERROR_AUTOMATIC_OFF"), "3");             
        this.errors_mapping.put(ic.getMessage("ERROR_NO_DELIVERY"), "4");             
        this.errors_mapping.put(ic.getMessage("ERROR_END_OF_OPERATION"), "5");             
        this.errors_mapping.put(ic.getMessage("ERROR_MECHANICAL_ERROR"), "6");             
        this.errors_mapping.put(ic.getMessage("ERROR_ELECTRONIC_ERROR"), "7");             
        this.errors_mapping.put(ic.getMessage("ERROR_POWER_INTERRUPT"), "8");             
        this.errors_mapping.put(ic.getMessage("ERROR_CARTRIDGE_ERROR"), "10");             
        this.errors_mapping.put(ic.getMessage("ERROR_SET_NOT_PRIMED"), "11");             
        this.errors_mapping.put(ic.getMessage("ERROR_DATA_INTERRUPTED"), "12");             
        this.errors_mapping.put(ic.getMessage("ERROR_LANGUAGE_ERROR"), "13");             
        this.errors_mapping.put(ic.getMessage("ERROR_INSULIN_CHANGED"), "14");             
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
        
        if (this.errors_mapping.containsKey(str))
            s = this.errors_mapping.get(str);
        
        return Integer.parseInt(s);
        
    }
    
    /**
     * Get Descriptions (array)
     * 
     * @return array of strings with description
     */
    public String[] getDescriptions()
    {
        return this.errors_desc;
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
