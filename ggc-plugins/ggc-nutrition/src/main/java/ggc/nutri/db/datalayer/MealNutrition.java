package ggc.nutri.db.datalayer;

import java.util.Enumeration;
import java.util.Hashtable;

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
 *  Filename:     MealNutrition  
 *  Description:  Meal Nutrient handling. This is used internally for calculation 
 *                of nutrients
 * 
 *  Author: andyrozman {andy@atech-software.com}  
 */

public class MealNutrition implements DAOObject
{

    // private boolean debug = false;
    private int nutrition_type_id = 0;
    private String nutrition_desc;
    private float amount = 0.0f;
    private float calculated_amount = 0.0f;

    // private float food_value = 0.0f;

    private Hashtable<String, String> multiplier;
    private String component_id;
    private float amount_sum = 0.0f;

    DataAccessNutri m_da = DataAccessNutri.getInstance();


    /**
     * Constructor (value pack: id=amount)
     * 
     * @param packed
     * @param load_description
     */
    public MealNutrition(String packed, boolean load_description)
    {
        int index = packed.indexOf("=");

        this.nutrition_type_id = Integer.parseInt(packed.substring(0, index));
        this.amount = Float.parseFloat(packed.substring(index + 1).replace(',', '.'));

        if (load_description)
        {
            this.nutrition_desc = DataAccessNutri.getInstance().getDbCache().nutrition_defs
                    .get("" + this.nutrition_type_id).getName();
        }

        // System.out.println("Meal Nutrition [id=" + this.nutrition_type_id +
        // ",desc="+ this.nutrition_desc + ",amount=" + this.amount);
        init();
    }


    /**
     * Constructor
     * 
     * @param mn
     */
    public MealNutrition(MealNutrition mn)
    {
        this.nutrition_type_id = mn.nutrition_type_id;
        this.amount = mn.amount;
        this.nutrition_desc = mn.nutrition_desc;

        init();
    }


    /**
     * Constructor
     * 
     * @param id
     * @param amount
     * @param desc
     */
    public MealNutrition(int id, float amount, String desc)
    {
        this.nutrition_type_id = id;
        this.amount = amount;
        this.nutrition_desc = desc;

        init();
        // this.no_desc = true;
    }


    private void init()
    {
        createId();
        // x this.food_value = this.amount;
        addMultiplier(this.component_id, this.amount);
    }


    private void createId()
    {
        this.component_id = m_da.getNewComponentId();
    }


    /**
     * Add Multiplier
     * 
     * @param id
     * @param _multiplier
     */
    public void addMultiplier(String id, float _multiplier)
    {
        if (this.multiplier == null)
        {
            this.multiplier = new Hashtable<String, String>();
        }

        if (!this.multiplier.containsKey(id))
        {
            this.multiplier.put(id, "" + _multiplier);
        }
    }


    /**
     * Add Multipliers (Hashtable)
     * 
     * @param multipliers
     */
    public void addMultipliers(Hashtable<String, String> multipliers)
    {
        // System.out.println("Adding multiplier !!!");

        if (this.multiplier == null)
        {
            this.multiplier = new Hashtable<String, String>();
        }

        for (Enumeration<String> en = multipliers.keys(); en.hasMoreElements();)
        {
            String key = en.nextElement();

            if (!this.multiplier.containsKey(key))
            {
                this.multiplier.put(key, multipliers.get(key));
            }
        }

        // System.out.println("Current multiplier: [id=" +
        // this.nutrition_type_id + ",multiplier=" + this.multiplier);

    }


    /**
     * Get Id
     * @return
     */
    public int getId()
    {
        return this.nutrition_type_id;
    }


    /**
     * Get Amount
     * 
     * @return
     */
    public float getAmount()
    {
        return this.amount;
    }


    /**
     * Set Amount
     * 
     * @param val
     */
    public void setAmount(float val)
    {
        this.amount = val;
    }


    /**
     * Add To Calculated Amount
     * @param val
     */
    public void addToCalculatedAmount(float val)
    {
        this.calculated_amount += val;
        // System.out.println("Curnt calculated amount: " +
        // this.calculated_amount);
    }

    /*
     * public float getCalculatedAmount() { return this.calculated_amount; }
     */


    /*
     * public void clearCalculated() { this.calculated_amount = 0.0f; }
     */

    /**
     * Clear Sum
     */
    public void clearSum()
    {
        this.amount_sum = 0.0f;
    }


    /**
     * Add Amount To Sum
     * 
     * @param _amount
     */
    public void addAmountToSum(float _amount)
    {
        // System.out.println("addAmountToSum: nutr=" + this.nutrition_desc +
        // "old=" + amount_sum + ",add=" + amount);
        this.amount_sum += _amount;
    }


    /**
     * Get Amount Sum
     * @return
     */
    public float getAmountSum()
    {
        return this.amount_sum;
    }


    /**
     * Get Calculated Amount
     * 
     * @return
     */
    public float getCalculatedAmount()
    {
        float f = 1.0f; // this.food_value;

        // System.out.println("getCalcuklatedAnount: food_value=" + f);

        if (this.multiplier != null)
        {
            for (Enumeration<String> en = this.multiplier.keys(); en.hasMoreElements();)
            {
                String val = this.multiplier.get(en.nextElement());
                // System.out.println("multiplikator = " + val);
                f *= Float.parseFloat(val);
            }
        }

        return f;
    }


    /**
     * Get Description
     * 
     * @return
     */
    public String getDescription()
    {
        return this.nutrition_desc;
    }


    /*
     * private void loadMealPart()
     * {
     * // DataAccessNutri.getInstance().getDb().getMeals();
     * }
     */

    /**
     * Is Glycemic Nutrient (GI, GL)
     * @return
     */
    public boolean isGlycemicNutrient()
    {
        return nutrition_type_id >= 4000 && nutrition_type_id <= 4005;
    }


    /**
     * To String
     * 
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString()
    {
        return "MealNutrient [id=" + this.nutrition_type_id + ",name=" + this.nutrition_desc + ",amount=" + this.amount
                + ",calc_amount=" + this.calculated_amount + ",multipliers=" + this.multiplier + "]\n";
    }

}
