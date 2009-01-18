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
 *  Filename:     PumpBolusType  
 *  Description:  Pump Bolus Types 
 * 
 *  Author: Andy {andy@atech-software.com}
 */


public class PumpBolusType
{

    DataAccessPump da = DataAccessPump.getInstance();
    I18nControlAbstract ic = da.getI18nControlInstance();

    /**
     * Bolus Descriptions
     */
    public String[] bolus_desc = { ic.getMessage("SELECT_BOLUS_TYPE"),
                       ic.getMessage("BOLUS_STANDARD"),             
                       ic.getMessage("BOLUS_SCROLL"),             
                       ic.getMessage("BOLUS_EXTENDED"),             
                       ic.getMessage("BOLUS_MULTIWAVE"),             
    };
    
    
    Hashtable<String,String> bolus_mapping = new Hashtable<String,String>(); 
    
    
    /**
     * Pump Bolus: None
     */
    public static final int PUMP_BOLUS_NONE = 0;

    /**
     * Pump Bolus: Standard
     */
    public static final int PUMP_BOLUS_STANDARD = 1;

    /**
     * Pump Bolus: Scrool
     */
    public static final int PUMP_BOLUS_SCROLL = 2;

    /**
     * Pump Bolus: Extended
     */
    public static final int PUMP_BOLUS_EXTENDED = 3;

    /**
     * Pump Bolus: Multiwave
     */
    public static final int PUMP_BOLUS_MULTIWAVE = 4;
    
    
    /**
     * Constructor
     */
    public PumpBolusType()
    {
        this.bolus_mapping.put(ic.getMessage("BOLUS_STANDARD"), "1");             
        this.bolus_mapping.put(ic.getMessage("BOLUS_SCROLL"), "2");             
        this.bolus_mapping.put(ic.getMessage("BOLUS_EXTENDED"), "3");             
        this.bolus_mapping.put(ic.getMessage("BOLUS_MULTIWAVE"), "4");             
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
        
        if (this.bolus_mapping.containsKey(str))
            s = this.bolus_mapping.get(str);
        
        return Integer.parseInt(s);
    }
    
    /**
     * Get Descriptions (array)
     * 
     * @return array of strings with description
     */
    public String[] getDescriptions()
    {
        return this.bolus_desc;
    }
    

}
