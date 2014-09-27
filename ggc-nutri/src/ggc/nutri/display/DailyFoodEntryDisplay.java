package ggc.nutri.display;

import ggc.nutri.db.datalayer.DailyFoodEntry;

import com.atech.graphics.components.ATTableData;
import com.atech.i18n.I18nControlAbstract;

/**
 *  Application:   GGC - GNU Gluco Control
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
 *  Filename:     DailyFoodEntryDisplay  
 *  Description:  Class for displaying DailyFoodEntry
 * 
 *  Author: andyrozman {andy@atech-software.com}  
 */

public class DailyFoodEntryDisplay extends ATTableData
{

    private static String[] weight_type_description = null;
    DailyFoodEntry daily_food_entry = null;

    /**
     * Constructor
     * 
     * @param ic
     */
    public DailyFoodEntryDisplay(I18nControlAbstract ic)
    {
        super(ic);
        initStatic();
    }

    /**
     * Constructor
     * 
     * @param ic
     * @param part 
     */
    public DailyFoodEntryDisplay(I18nControlAbstract ic, DailyFoodEntry part)
    {
        super(ic);
        initStatic();

        this.daily_food_entry = part;

        /*
         * this.type_desc = MealPartsDisplay.type_description[part.getType() -
         * 1]; this.id = "" + part.getId(); this.description = part.getName();
         * this.amount = "" + part.getAmount();
         * this.meal_part = part;
         */
        // String[] col = { "TYPE", "ID", "DESCRIPTION", "AMOUNT" };
    }

    /*
     * public void setNutritionDefinition(NutritionDefinition def) { this.id =
     * "" + def.getId(); // this.name = def.getName(); //this.value =
     * def.get.getTag(); // this.weight_unit = def.getWeight_unit(); }
     */

    /**
     * Init Static
     */
    public void initStatic()
    {
        if (DailyFoodEntryDisplay.weight_type_description == null)
        {
            DailyFoodEntryDisplay.weight_type_description = new String[3];
            DailyFoodEntryDisplay.weight_type_description[0] = ic.getMessage("WEIGHT_LBL2");
            DailyFoodEntryDisplay.weight_type_description[1] = ic.getMessage("HOME_WEIGHT");
            DailyFoodEntryDisplay.weight_type_description[2] = ic.getMessage("AMOUNT_LBL");
        }

    }

    /**
     * Init
     * 
     * @see com.atech.graphics.components.ATTableData#init()
     */
    @Override
    public void init()
    {
        String[] col = { "NAME", "WEIGHT_TYPE", "HOME_WEIGHT", "AMOUNT_LBL" };
        float[] col_size = { 0.5f, 0.2f, 0.2f, 0.1f };

        init(col, col_size);
    }

    /**
     * Get Daily Food Entry
     * 
     * @return
     */
    public DailyFoodEntry getDailyFoodEntry()
    {
        return this.daily_food_entry;
    }

    /**
     * Get String For Db
     * 
     * @return
     */
    public String getStringForDb()
    {
        return this.daily_food_entry.getValueString();
    }

    /**
     * Get Column Value
     * 
     * @see com.atech.graphics.components.ATTableData#getColumnValue(int)
     */
    @Override
    public String getColumnValue(int column)
    {

        if (this.daily_food_entry == null)
            return null;

        switch (column)
        {
            case 1:
                return DailyFoodEntryDisplay.weight_type_description[this.daily_food_entry.getWeightType() - 1];

            case 2:
                return this.daily_food_entry.getHomeWeightDescription();

            case 3:
                return this.daily_food_entry.getAmountString();

            case 0:
            default:
                return this.daily_food_entry.getName();

        }
    }
}
