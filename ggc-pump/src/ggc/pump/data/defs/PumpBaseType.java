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
 *  Filename:     PumpBaseType  
 *  Description:  Pump Base Types 
 * 
 *  Author: Andy {andy@atech-software.com}
 */


public class PumpBaseType
{

    DataAccessPump da = DataAccessPump.getInstance();
    I18nControlAbstract ic = da.getI18nControlInstance();

    public String[] basetype_desc = { 
                                     ic.getMessage("NONE"),
                                     ic.getMessage("BASAL_DOSE"),
                                     ic.getMessage("BOLUS_DOSE"),
                                     ic.getMessage("EVENT"),
                                     ic.getMessage("ALARM"),
                                     ic.getMessage("ERROR"),
                                     ic.getMessage("REPORT"),
                                     ic.getMessage("PEN_INJECTION_BASAL"),
                                     ic.getMessage("PEN_INJECTION_BOLUS"),
                                     ic.getMessage("ADDITIONAL_DATA"),
    };
    
    
    
    Hashtable<String,String> basetype_mapping = new Hashtable<String,String>(); 
    
    
    public static final int PUMP_DATA_NONE = 0;
    public static final int PUMP_DATA_BASAL = 1;
    public static final int PUMP_DATA_BOLUS = 2;
    public static final int PUMP_DATA_EVENT = 3;
    public static final int PUMP_DATA_ALARM = 4;
    public static final int PUMP_DATA_ERROR = 5;
    public static final int PUMP_DATA_REPORT = 6;
    public static final int PUMP_DATA_PEN_INJECTION_BASAL = 7;
    public static final int PUMP_DATA_PEN_INJECTION_BOLUS = 8;
    public static final int PUMP_DATA_ADDITIONAL_DATA = 9;
    

    /**
     * Constructor
     */
    public PumpBaseType()
    {
        this.basetype_mapping.put(ic.getMessage("BASAL_DOSE"), "1");
        this.basetype_mapping.put(ic.getMessage("BOLUS_DOSE"), "2");
        this.basetype_mapping.put(ic.getMessage("EVENT"), "3");
        this.basetype_mapping.put(ic.getMessage("ALARM"), "4");
        this.basetype_mapping.put(ic.getMessage("ERROR"), "5");
        this.basetype_mapping.put(ic.getMessage("REPORT"), "6");
        this.basetype_mapping.put(ic.getMessage("PEN_INJECTION_BASAL"), "7");
        this.basetype_mapping.put(ic.getMessage("PEN_INJECTION_BOLUS"), "8");
        this.basetype_mapping.put(ic.getMessage("ADDITIONAL_DATA"), "9");
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
        
        if (this.basetype_mapping.containsKey(str))
            s = this.basetype_mapping.get(str);
        
        return Integer.parseInt(s);
    }
    
    /**
     * Get Descriptions (array)
     * 
     * @return array of strings with description
     */
    public String[] getDescriptions()
    {
        return this.basetype_desc;
    }
    
    
    
}
