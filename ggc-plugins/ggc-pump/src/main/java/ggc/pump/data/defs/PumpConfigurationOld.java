package main.java.ggc.pump.data.defs;

import java.util.Hashtable;

import com.atech.i18n.I18nControlAbstract;

import main.java.ggc.pump.util.DataAccessPump;

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

public class PumpConfigurationOld
{

    DataAccessPump da = DataAccessPump.getInstance();
    I18nControlAbstract ic = da.getI18nControlInstance();

    /**
     * Errors Description
     */
    private String[] errors_desc = { ic.getMessage("ERROR_UNKNOWN_ERROR"), ic.getMessage("ERROR_CARTRIDGE_EMPTY"),
                                    ic.getMessage("ERROR_BATTERY_DEPLETED"), ic.getMessage("ERROR_AUTOMATIC_OFF"),
                                    ic.getMessage("ERROR_NO_DELIVERY"), ic.getMessage("ERROR_END_OF_OPERATION"),
                                    ic.getMessage("ERROR_MECHANICAL_ERROR"), ic.getMessage("ERROR_ELECTRONIC_ERROR"),
                                    ic.getMessage("ERROR_POWER_INTERRUPT"), ic.getMessage("ERROR_CARTRIDGE_ERROR"),
                                    ic.getMessage("ERROR_SET_NOT_PRIMED"), ic.getMessage("ERROR_DATA_INTERRUPTED"),
                                    ic.getMessage("ERROR_LANGUAGE_ERROR"), ic.getMessage("ERROR_INSULIN_CHANGED"), };

    private String[] errors_compdesc = { ic.getMessage("SELECT_SUBTYPE"), ic.getMessage("ERROR_CARTRIDGE_EMPTY"),
                                        ic.getMessage("ERROR_BATTERY_DEPLETED"), ic.getMessage("ERROR_AUTOMATIC_OFF"),
                                        ic.getMessage("ERROR_NO_DELIVERY"), ic.getMessage("ERROR_END_OF_OPERATION"),
                                        ic.getMessage("ERROR_MECHANICAL_ERROR"),
                                        ic.getMessage("ERROR_ELECTRONIC_ERROR"),
                                        ic.getMessage("ERROR_POWER_INTERRUPT"), ic.getMessage("ERROR_CARTRIDGE_ERROR"),
                                        ic.getMessage("ERROR_SET_NOT_PRIMED"), ic.getMessage("ERROR_DATA_INTERRUPTED"),
                                        ic.getMessage("ERROR_LANGUAGE_ERROR"), ic.getMessage("ERROR_INSULIN_CHANGED"), };

    Hashtable<String, String> errors_mapping = new Hashtable<String, String>();

    /**
     * Pump Config Group: General
     */
    public static final int PUMP_CONFIG_GROUP_GENERAL = 1;

    /**
     * Pump Config Group: Insulin
     */
    public static final int PUMP_CONFIG_GROUP_INSULIN = 2;

    /**
     * Pump Config Group: Blood Glucose
     */
    public static final int PUMP_CONFIG_GROUP_BLOOD_GLUCOSE = 3;

    /**
     * Pump Config Group: Bolus Helper
     */
    public static final int PUMP_CONFIG_GROUP_BOLUS_HELPER = 4;

    /**
     * Pump Config Group: Other
     */
    public static final int PUMP_CONFIG_GROUP_OTHER = 5;

    // public static final int PUMP_CONFIG_GROUP_GENERAL = 1;
    // public static final int PUMP_CONFIG_GROUP_GENERAL = 1;
    // public static final int PUMP_CONFIG_GROUP_GENERAL = 1;

    /**
     * Constructor
     */
    public PumpConfigurationOld()
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
        {
            s = this.errors_mapping.get(str);
        }

        return Integer.parseInt(s);

    }

    /**
     * Get Descriptions (array)
     * 
     * @return array of strings with description
     */
    public String[] getDescriptions()
    {
        return this.errors_compdesc;
    }

    /**
     * Get Description by ID
     * 
     * @param id
     * @return
     */
    public String getDescriptionByID(int id)
    {
        // System.out.println("getDescriptionByID [" + id + "]: " +
        // this.errors_desc[id]);
        return this.errors_desc[id];
    }

}
