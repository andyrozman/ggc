package ggc.nutri.display;

import com.atech.graphics.components.ATTableData;
import com.atech.i18n.I18nControlAbstract;

import ggc.nutri.db.datalayer.NutritionDefinition;
import ggc.nutri.util.DataAccessNutri;

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
 *  Filename:     NutritionDataDisplay
 *  Description:  Nutrition Data Display
 * 
 *  Author: andyrozman {andy@atech-software.com}  
 */

public class NutritionDataDisplay extends ATTableData
{

    private String id;
    private String value;
    private String name;
    private String weight_unit;
    private String decimal_places;


    /**
     * Constructor
     * 
     * @param ic
     */
    public NutritionDataDisplay(I18nControlAbstract ic)
    {
        super(ic);
    }


    /**
     * Constructor
     * 
     * @param ic
     * @param full
     */
    public NutritionDataDisplay(I18nControlAbstract ic, String full)
    {
        super(ic);
        // System.out.println("Nutr: " + full);
        int index = full.indexOf("=");
        this.id = full.substring(0, index);
        this.value = full.substring(index + 1);

        NutritionDefinition def = DataAccessNutri.getInstance().getDbCache().nutrition_defs.get(id);
        setNutritionDefinition(def);

    }


    /**
     * Constructor
     * 
     * @param ic
     * @param def
     * @param value
     */
    public NutritionDataDisplay(I18nControlAbstract ic, NutritionDefinition def, float value)
    {
        super(ic);
        setNutritionDefinition(def);
        this.setAmount(value);
    }


    /**
     * Set Nutrition Definition
     * 
     * @param def
     */
    public void setNutritionDefinition(NutritionDefinition def)
    {
        if (def == null)
            return;

        this.id = "" + def.getId();
        this.name = def.getResolvedName(); // .getName();
        // this.value = def.get.getTag();
        this.weight_unit = def.getWeightUnit();
        this.decimal_places = def.getDecimalPlaces();
    }


    /**
     * Init
     * 
     * @see com.atech.graphics.components.ATTableData#init()
     */
    @Override
    public void init()
    {
        String[] col = { "ID", "NUTRITION", "AMOUNT_LBL", "UNITS" };
        float[] col_size = { 0.1f, 0.5f, 0.2f, 0.2f };

        init(col, col_size);
    }


    /**
     * Get Id
     * 
     * @return
     */
    public String getId()
    {
        return this.id;
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
                return this.name;
            case 2:
                return this.value;
            case 3:
                return this.weight_unit;

            case 0:
            default:
                return "" + this.id;

        }
    }


    /**
     * Get Name
     * 
     * @return
     */
    public String getName()
    {
        return this.name;
    }


    /**
     * Get Amount
     * 
     * @return
     */
    public float getAmount()
    {

        // return DataAccessNutri.getFloatAsString(this.value,
        // this.decimal_places);
        float f = 0.0f;
        try
        {
            f = Float.parseFloat(this.value.replace(",", "."));
        }
        catch (Exception ex)
        {
            System.out.println("Float parse ex: " + ex);
        }

        return f;
    }


    /**
     * Get Weight Unit
     * 
     * @return
     */
    public String getWeightUnit()
    {
        return this.weight_unit;
    }


    /**
     * Set Amount
     * 
     * @param amount
     */
    public void setAmount(float amount)
    {
        this.value = DataAccessNutri.getInstance().getDecimalHandler().getDecimalAsString(amount,
            Integer.parseInt(this.decimal_places));
    }


    /**
     * Get Save Data
     * 
     * @return
     */
    public String getSaveData()
    {
        return this.id + "=" + this.value;
    }

}
