package ggc.nutri.display;

import ggc.nutri.db.datalayer.MealPart;

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
 *  Filename:     MealFoodDisplay  
 *  Description:  Meal Food Display
 * 
 *  Author: andyrozman {andy@atech-software.com}  
 */

public class MealFoodDisplay extends ATTableData
{

    private String type_desc;
    private String id;
    private String description;
    private String amount;

    // private static String[] type_description = null;

    MealPart meal_part = null;

    /**
     * Constructor
     * 
     * @param ic
     */
    public MealFoodDisplay(I18nControlAbstract ic)
    {
        super(ic);
        initStatic();
    }

    /**
     * Constructor
     * 
     * @param ic
     * @param full
     */
    public MealFoodDisplay(I18nControlAbstract ic, String full)
    {
        super(ic);
        int index = full.indexOf("=");
        this.id = full.substring(0, index);
        // this.value = full.substring(index+1);
        initStatic();
    }

    /**
     * Constructor
     * 
     * @param ic
     * @param part
     */
    public MealFoodDisplay(I18nControlAbstract ic, MealPart part)
    {
        super(ic);
        initStatic();

        this.type_desc = MealPartsDisplay.type_description[part.getType() - 1];
        this.id = "" + part.getId();
        this.description = part.getName();
        this.amount = "" + part.getAmount();

        this.meal_part = part;

        // String[] col = { "TYPE", "ID", "DESCRIPTION", "AMOUNT" };

    }

    /**
     * Get Meal Part
     * 
     * @return
     */
    public MealPart getMealPart()
    {
        return this.meal_part;
    }

    /*
     * public void setNutritionDefinition(NutritionDefinition def) { this.id =
     * "" + def.getId(); // this.name = def.getName(); //this.value =
     * def.get.getTag(); // this.weight_unit = def.getWeightUnit(); }
     */

    private void initStatic()
    {
        if (MealPartsDisplay.type_description == null)
        {

            MealPartsDisplay.type_description = new String[3];
            MealPartsDisplay.type_description[0] = ic.getMessage("USDA_NUTRITION");
            MealPartsDisplay.type_description[1] = ic.getMessage("USER_NUTRITION");
            MealPartsDisplay.type_description[2] = ic.getMessage("MEAL");
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
     * Get Id
     * @return
     */
    public String getId()
    {
        return this.id;
    }

    /**
     * Set Amount
     * 
     * @param amount
     */
    public void setAmount(float amount)
    {
        this.amount = "" + amount;
        this.meal_part.setAmount(amount);
    }

    /**
     * Get Save Data
     * 
     * @return
     */
    public String getSaveData()
    {
        return this.meal_part.getType() + ":" + this.meal_part.getId() + "=" + this.amount;
    }

    /**
     * Get Column Value
     * 
     * @see com.atech.graphics.components.ATTableData#getColumnValue(int)
     */
    @Override
    public String getColumnValue(int column)
    {
        switch (column)
        {
            case 1:
                return this.description;

            case 2:
                return this.amount;

            case 0:
            default:
                return "" + this.type_desc;

        }
    }
}
