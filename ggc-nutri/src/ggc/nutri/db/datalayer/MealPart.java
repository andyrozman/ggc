package ggc.nutri.db.datalayer;

import java.util.ArrayList;
import java.util.StringTokenizer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ggc.nutri.data.GGCTreeRoot;
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
 *  Filename:     ###---###  
 *  Description:  ###---###
 * 
 *  Author: andyrozman {andy@atech-software.com}  
 */

public class MealPart
{

    private static final Logger LOG = LoggerFactory.getLogger(MealPart.class);

    DataAccessNutri m_da = DataAccessNutri.getInstance();
    // private boolean debug = false;
    private int meal_type = 0;
    // private long meal_type_id = 0L;

    private Meal meal_obj_meal = null;
    private FoodDescription meal_obj_food = null;
    private float amount = 0.0f;

    private ArrayList<MealNutrition> nutritions = null;


    /**
     * Constructor
     * 
     * @param meal_str
     */
    public MealPart(String meal_str)
    {
        // 1:122=1.0

        StringTokenizer strtok = new StringTokenizer(meal_str, "=");

        String meal_id_in = strtok.nextToken();
        String amount_in = strtok.nextToken().replace(',', '.');

        StringTokenizer strtok2 = new StringTokenizer(meal_id_in, ":");

        String type_in = strtok2.nextToken();
        String type_id_in = strtok2.nextToken();

        this.meal_type = Integer.parseInt(type_in);

        loadMealPart(this.meal_type, type_id_in);

        this.amount = Float.parseFloat(amount_in);

    }


    /**
     * This constructor is used for Printing Meal data (it unpacks weight data)
     * 
     * @param meal_str
     * @param print_data 
     */
    public MealPart(String meal_str, boolean print_data)
    {
        // 1:122=1.0

        StringTokenizer strtok = new StringTokenizer(meal_str, "=");

        String meal_id_in = strtok.nextToken();
        String amount_in = strtok.nextToken().replace(',', '.');

        StringTokenizer strtok2 = new StringTokenizer(meal_id_in, ":");

        String type_in = strtok2.nextToken();
        String type_id_in = strtok2.nextToken();

        this.meal_type = Integer.parseInt(type_in);

        loadMealPart(this.meal_type, type_id_in);

        this.amount = Float.parseFloat(amount_in);

    }


    /**
     * Constructor
     */
    public MealPart()
    {
        LOG.warn("MealPart was created with empty constructor");
    }


    /**
     * Constructor
     * 
     * @param type
     * @param obj
     * @param amount
     */
    public MealPart(int type, Object obj, float amount)
    {
        this.meal_type = type;

        if (this.meal_type == GGCTreeRoot.TREE_USDA_NUTRITION || this.meal_type == GGCTreeRoot.TREE_USER_NUTRITION)
        {
            this.meal_obj_food = (FoodDescription) obj;
            // this.meal_type_id = this.meal_obj_food.getId();
            this.amount = amount;
        }
        else
        {
            this.meal_obj_meal = (Meal) obj;
            // this.meal_type_id = this.meal_obj_meal.getId();
            this.amount = amount;
        }

    }


    private void loadMealPart(int type, String id)
    {
        if (type == GGCTreeRoot.TREE_USDA_NUTRITION || type == GGCTreeRoot.TREE_USER_NUTRITION)
        {
            // this.meal_obj_food = dataAccess.tree_roots.get("" +
            // type).m_foods_ht.get(id);
            this.meal_obj_food = m_da.getDbCache().tree_roots.get("" + type).findFood(type, Long.parseLong(id));
            System.out.println("MealPart [Food]: " + this.meal_obj_food + ",type=" + type + ",id=" + id);
        }
        else
        {
            this.meal_obj_meal = m_da.getDbCache().tree_roots.get("" + type).findMeal(3, Long.parseLong(id));
            // this.meal_obj_meal =
            // dataAccess.tree_roots.get("3").m_meals_ht.get(id);
            System.out.println("MealPart [Meal]: " + this.meal_obj_food + ",id=" + id);
        }

    }


    /**
     * Get Id
     * 
     * @return
     */
    public long getId()
    {
        if (this.meal_type == GGCTreeRoot.TREE_USDA_NUTRITION || this.meal_type == GGCTreeRoot.TREE_USER_NUTRITION)
            return this.meal_obj_food.getId();
        else
            return this.meal_obj_meal.getId();
    }


    /**
     * Get Name
     * 
     * @return
     */
    public String getName()
    {
        if (this.meal_type == GGCTreeRoot.TREE_USDA_NUTRITION || this.meal_type == GGCTreeRoot.TREE_USER_NUTRITION)
            return this.meal_obj_food.getName();
        else
            return this.meal_obj_meal.getName();
    }


    /**
     * Get Type
     * 
     * @return
     */
    public int getType()
    {
        return this.meal_type;
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
     * @param amount
     */
    public void setAmount(float amount)
    {
        this.amount = amount;
    }


    /**
     * Get Food Object
     * 
     * @return
     */
    public FoodDescription getFoodObject()
    {
        return this.meal_obj_food;
    }


    /**
     * Get Meal Object
     * 
     * @return
     */
    public Meal getMealObject()
    {
        return this.meal_obj_meal;
    }


    /*
     * public DailyFoodEntry getDailyFoodEntry() { if ((this.meal_type ==
     * GGCTreeRoot.TREE_USDA_NUTRITION) || (this.meal_type ==
     * GGCTreeRoot.TREE_USER_NUTRITION)) { return new DailyFoodEntry() return
     * this.meal_obj_food.getId(); } else { return this.meal_obj_meal.getId(); }
     * return null; }
     */

    private void loadNutritions()
    {
        String nutr;

        if (this.meal_type == GGCTreeRoot.TREE_USDA_NUTRITION || this.meal_type == GGCTreeRoot.TREE_USER_NUTRITION)
        {
            nutr = this.meal_obj_food.getNutritions();
        }
        else
        {
            nutr = this.meal_obj_meal.getNutritions();
        }

        this.nutritions = new ArrayList<MealNutrition>();

        StringTokenizer strtok = new StringTokenizer(nutr, ";");

        while (strtok.hasMoreTokens())
        {
            MealNutrition mn = new MealNutrition(strtok.nextToken(), true);
            this.nutritions.add(mn);
        }

    }


    /**
     * Get Nutrients
     * 
     * @return
     */
    public ArrayList<MealNutrition> getNutrients()
    {
        if (this.nutritions == null)
        {
            loadNutritions();
        }

        return this.nutritions;
    }


    /**
     * To String
     * 
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString()
    {
        return "MealPart";
    }

}
