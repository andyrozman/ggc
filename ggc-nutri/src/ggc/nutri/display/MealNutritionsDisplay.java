package ggc.nutri.display;

import ggc.nutri.db.datalayer.MealNutrition;
import ggc.nutri.db.datalayer.NutritionDefinition;
import ggc.core.util.DataAccess;

import java.util.Comparator;

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
 *  Filename:     MealNutritionsDisplay  
 *  Description:  Meal Nutritions Display 
 * 
 *  Author: andyrozman {andy@atech-software.com}  
 */

public class MealNutritionsDisplay extends ATTableData implements Comparator<MealNutritionsDisplay>
{

    private String id;
    private String value;
    private String name;
    private String weight_unit;
    private String decimal_places;
    //private float calculated_value;

    /**
     * Constructor
     * 
     * @param ic
     */
    public MealNutritionsDisplay(I18nControlAbstract ic)
    {
        super(ic);
    }


    /**
     * Constructor
     * 
     * @param ic
     * @param mn
     */
    public MealNutritionsDisplay(I18nControlAbstract ic, MealNutrition mn)
    {
        super(ic);
        this.id = "" + mn.getId();
        this.value = "" + mn.getAmountSum(); // proc v1 .getCalculatedAmount();
        // this.calculated_value = mn.getAmount();

    }

    
    /**
     * Set Nutrition Definition
     * 
     * @param def
     */
    public void setNutritionDefinition(NutritionDefinition def)
    {
        this.id = "" + def.getId();
        this.name = def.getResolvedName(); // .getName();
        // this.value = def.get.getTag();
        this.weight_unit = def.getWeight_unit();
        this.decimal_places = def.getDecimal_places();
    }

    /**
     * Init
     * 
     * @see com.atech.graphics.components.ATTableData#init()
     */
    public void init()
    {
        String[] cols = { "NUTRITION", "AMOUNT_LBL", "UNITS" };
        float[] cols_size = { 0.6f, 0.2f, 0.2f };

        init(cols, cols_size);
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
     * Get Value
     * 
     * @return
     */
    public String getValue()
    {
        float fl = Float.parseFloat(this.value);
        return DataAccess.getFloatAsString(fl, this.decimal_places);
    }

    
    /**
     * Get Column Value
     * 
     * @see com.atech.graphics.components.ATTableData#getColumnValue(int)
     */
    public String getColumnValue(int column)
    {

        switch (column)
        {
        case 1:
            return this.getValue();
        case 2:
            return this.weight_unit;

        case 0:
        default:
            return this.name;

        }

    }

    
    /**
     * Compare
     * 
     * @see java.util.Comparator#compare(java.lang.Object, java.lang.Object)
     */
    public int compare(MealNutritionsDisplay mnd1, MealNutritionsDisplay mnd2)
    {
        return mnd1.name.compareTo(mnd2.name);
    }

    
    /**
     * Get Save Data
     * 
     * @return
     */
    public String getSaveData()
    {
        return this.id + "=" + this.getValue();
    }


    /**
     * To String
     * 
     * @see java.lang.Object#toString()
     */
    public String toString()
    {
        return getSaveData();
    }

}