package ggc.pump.data.defs;

import java.util.Hashtable;

import ggc.pump.util.DataAccessPump;

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
 *  Filename:     PumpBasalSubType  
 *  Description:  Pump Basal Sub Types 
 * 
 *  Author: Andy {andy@atech-software.com}
 */


public class PumpBasalSubType
{

    DataAccessPump da = DataAccessPump.getInstance();
    I18nControlAbstract ic = da.getI18nControlInstance();

    /**
     * Basal Descriptions
     */
    public String[] basal_desc = { ic.getMessage("SELECT_BASAL_TYPE"),
                       ic.getMessage("BASAL_VALUE"),             
                       ic.getMessage("BASAL_PROFILE"),             
                       ic.getMessage("BASAL_TEMPORARY_BASAL_RATE"),             
                       ic.getMessage("BASAL_TEMPORARY_BASAL_RATE_PROFILE"),             
                       ic.getMessage("BASAL_PUMP_STATUS"),
                       ic.getMessage("BASAL_TEMPORARY_BASAL_RATE_ENDED"),
                       ic.getMessage("BASAL_TEMPORARY_BASAL_RATE_CANCELED")
    };
    
    
    Hashtable<String,String> basal_mapping = new Hashtable<String,String>(); 
    
    /**
     * Pump Basal: Value
     */
    public static final int PUMP_BASAL_VALUE = 1;

    /**
     * Pump Basal: Profile
     */
    public static final int PUMP_BASAL_PROFILE = 2;

    /**
     * Pump Basal: Temporary Basal Rate
     */
    public static final int PUMP_BASAL_TEMPORARY_BASAL_RATE = 3;

    /**
     * Pump Basal: Temporary Basal Rate Profile
     */
    public static final int PUMP_BASAL_TEMPORARY_BASAL_RATE_PROFILE = 4;

    /**
     * Pump Basal: Pump Status
     */
    public static final int PUMP_BASAL_PUMP_STATUS = 5;

    
    /**
     * Pump Basal: Temporary Basal Rate Ended
     */
    public static final int PUMP_BASAL_TEMPORARY_BASAL_RATE_ENDED = 6;
    

    /**
     * Pump Basal: Temporary Basal Rate Ended
     */
    public static final int PUMP_BASAL_TEMPORARY_BASAL_RATE_CANCELED = 7;
    
    
    
    /**
     * Constructor
     */
    public PumpBasalSubType()
    {
        this.basal_mapping.put(ic.getMessage("BASAL_VALUE"), "1");             
        this.basal_mapping.put(ic.getMessage("BASAL_PROFILE"), "2");             
        this.basal_mapping.put(ic.getMessage("BASAL_TEMPORARY_BASAL_RATE"), "3");             
        this.basal_mapping.put(ic.getMessage("BASAL_TEMPORARY_BASAL_RATE_PROFILE"), "4");             
        this.basal_mapping.put(ic.getMessage("BASAL_ON_OFF"), "5");
        this.basal_mapping.put(ic.getMessage("BASAL_TEMPORARY_BASAL_RATE_ENDED"), "6");
        this.basal_mapping.put(ic.getMessage("BASAL_TEMPORARY_BASAL_RATE_CANCELED"), "7");
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
        
        if (this.basal_mapping.containsKey(str))
            s = this.basal_mapping.get(str);
        
        return Integer.parseInt(s);
    }
    
    /**
     * Get Descriptions (array)
     * 
     * @return array of strings with description
     */
    public String[] getDescriptions()
    {
        return this.basal_desc;
    }
    
    
}
