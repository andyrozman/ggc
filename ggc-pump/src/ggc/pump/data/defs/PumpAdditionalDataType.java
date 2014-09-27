package ggc.pump.data.defs;

import ggc.pump.data.PumpValuesEntryExt;
import ggc.pump.util.DataAccessPump;

import java.util.ArrayList;
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
 *  Filename:     PumpAdditionalDataType  
 *  Description:  Pump Additional Data Types 
 * 
 *  Author: Andy {andy@atech-software.com}
 */

public class PumpAdditionalDataType
{

    DataAccessPump da = DataAccessPump.getInstance();
    I18nControlAbstract ic = da.getI18nControlInstance();

    /**
     * Additional data description
     */
    public String[] addata_desc = { ic.getMessage("SELECT_ADDITIONAL_DATA"), ic.getMessage("ADD_DATA_ACTIVITY"),
                                   ic.getMessage("ADD_DATA_COMMENT"), ic.getMessage("ADD_DATA_BG"),
                                   ic.getMessage("ADD_DATA_URINE"), ic.getMessage("ADD_DATA_CH"),
                                   ic.getMessage("ADD_DATA_FOOD_DB"), ic.getMessage("ADD_DATA_FOOD_DESC"), };

    Hashtable<String, String> addata_mapping = new Hashtable<String, String>();

    /**
     * Pump Additional Data Type: Activity
     */
    public static final int PUMP_ADD_DATA_ACTIVITY = 1;

    /**
     * Pump Additional Data Type: Comment
     */
    public static final int PUMP_ADD_DATA_COMMENT = 2;

    /**
     * Pump Additional Data Type: BG
     */
    public static final int PUMP_ADD_DATA_BG = 3;

    /**
     * Pump Additional Data Type: Urine
     */
    public static final int PUMP_ADD_DATA_URINE = 4;

    /**
     * Pump Additional Data Type: CH (Carbohydrates)
     */
    public static final int PUMP_ADD_DATA_CH = 5;

    /**
     * Pump Additional Data Type: Food Db
     */
    public static final int PUMP_ADD_DATA_FOOD_DB = 6;

    /**
     * Pump Additional Data Type: Food Db
     */
    public static final int PUMP_ADD_DATA_FOOD_DESC = 7;

    /**
     * Constructor
     */
    public PumpAdditionalDataType()
    {
        this.addata_mapping.put(ic.getMessage("ADD_DATA_ACTIVITY"), "1");
        this.addata_mapping.put(ic.getMessage("ADD_DATA_COMMENT"), "2");
        this.addata_mapping.put(ic.getMessage("ADD_DATA_BG"), "3");
        this.addata_mapping.put(ic.getMessage("ADD_DATA_URINE"), "4");
        this.addata_mapping.put(ic.getMessage("ADD_DATA_CH"), "5");
        this.addata_mapping.put(ic.getMessage("ADD_DATA_FOOD_DB"), "6");
        this.addata_mapping.put(ic.getMessage("ADD_DATA_FOOD_DESC"), "7");
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

        if (this.addata_mapping.containsKey(str))
        {
            s = this.addata_mapping.get(str);
        }

        return Integer.parseInt(s);

    }

    /**
     * Get Type Description
     * 
     * @param idx index
     * @return
     */
    public String getTypeDescription(int idx)
    {
        return this.addata_desc[idx];
    }

    /**
     * Get Descriptions (array)
     * 
     * @return array of strings with description
     */
    public String[] getDescriptions()
    {
        return this.addata_desc;
    }

    /**
     * Create Items (for Combo Box)
     * 
     * @param old_data
     * @return
     */
    public Object[] createItems(Hashtable<String, PumpValuesEntryExt> old_data)
    {
        ArrayList<String> items = new ArrayList<String>();

        for (int i = 1; i < this.addata_desc.length; i++)
        {
            if (!old_data.containsKey(this.addata_desc[i]))
            {
                items.add(this.addata_desc[i]);
            }
        }

        return items.toArray();

    }

}
