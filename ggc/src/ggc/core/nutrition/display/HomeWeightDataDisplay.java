package ggc.core.nutrition.display;

import ggc.core.util.NutriI18nControl;
import ggc.core.db.datalayer.NutritionHomeWeightType;

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
 *  Filename:     HomeWeightDataDisplay
 *  Description:  Home Weight Data Display
 * 
 *  Author: andyrozman {andy@atech-software.com}  
 */


public class HomeWeightDataDisplay extends ATTableData
{

    private String id;
    private String weight_type;
    private float amount = 1.0f;
    // private String value;
    private float weight;
    private NutriI18nControl nic;

    /**
     * Constructor
     * 
     * @param ic
     */
    public HomeWeightDataDisplay(I18nControlAbstract ic)
    {
        super(ic);

        nic = (NutriI18nControl) ic;
    }

    /**
     * Constructor
     * 
     * @param ic
     * @param full
     */
    public HomeWeightDataDisplay(I18nControlAbstract ic, String full)
    {
        super(ic);
        nic = (NutriI18nControl) ic;

        int index = full.indexOf("=");
        this.id = full.substring(0, index);

        String val = full.substring(index + 1);

        if (val.indexOf("*") > -1)
        {
            index = val.indexOf("*");
            this.amount = Float.parseFloat(val.substring(0, index));

            String ww = val.substring(index + 1);

            if (ww.startsWith("."))
                ww = "0" + ww;

            this.weight = Float.parseFloat(ww);
        }
        else
        {
            this.amount = 1.0f;
            this.weight = Float.parseFloat(val);
        }
    }

    /**
     * Constructor
     * 
     * @param ic
     * @param def
     * @param amount
     * @param weight
     */
    public HomeWeightDataDisplay(I18nControlAbstract ic, NutritionHomeWeightType def, float amount, float weight)
    {
        super(ic);
        nic = (NutriI18nControl) ic;

        this.id = "" + def.getId();
        this.setHomeWeightDefinition(def.getResolvedName()); //.getName());
        // this.setAmount(amount);

        this.amount = amount;
        this.weight = weight;
    }

    /**
     * Init
     * 
     * @see com.atech.graphics.components.ATTableData#init()
     */
    public void init()
    {
        String[] cols = { /* "ID", */"WEIGHT_TYPE", "AMOUNT_LBL", "WEIGHT" };
        float[] cols_size = { /* 0.1f, */0.5f, 0.25f, 0.25f };

        init(cols, cols_size);
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
            return "" + this.amount;

        case 2:
            return "" + this.weight;

        case 0:
        default:
            return this.weight_type;

        }
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
     * Get Name
     * 
     * @return
     */
    public String getName()
    {
        return this.weight_type;
    }

    /**
     * Set Home Weight Definition
     * 
     * @param name
     */
    public void setHomeWeightDefinition(String name)
    {
        // this.weight_type = ic.getMessage(name);
        this.weight_type = nic.getPartitialTranslation(name, "_");
    }

    /**
     * Get Amount
     * 
     * @return
     */
    public float getAmount()
    {
        return this.amount;
        /*
         * //return DataAccess.getFloatAsString(this.value,
         * this.decimal_places); float f = 0.0f; try { f =
         * Float.parseFloat(this.value.replace(',', '.')); } catch(Exception ex)
         * { System.out.println("Float parse ex: " + ex); }
         * 
         * return f;
         */
    }

    /**
     * Set Amount
     * 
     * @param val
     */
    public void setAmount(float val)
    {
        this.amount = val; // .value = DataAccess.getFloatAsString(val,
                           // this.decimal_places);
    }

    /**
     * Get Weight
     * 
     * @return
     */
    public float getWeight()
    {
        return this.weight;
    }

    /**
     * Set Weight
     * 
     * @param weight
     */
    public void setWeight(float weight)
    {
        this.weight = weight;
    }

    /**
     * Get Save Data
     * 
     * @return
     */
    public String getSaveData()
    {

        if (this.amount != 1.0f)
        {
            return this.id + "=" + this.amount + "*" + weight;
        }
        else
        {
            return this.id + "=" + this.weight;
        }
    }

}
