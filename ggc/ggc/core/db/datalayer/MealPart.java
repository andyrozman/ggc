/*
 *  GGC - GNU Gluco Control
 *
 *  A pure java app to help you manage your diabetes.
 *
 *  See AUTHORS for copyright information.
 *
 *  This program is free software; you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation; either version 2 of the License, or
 *  (at your option) any later version.
 *
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with this program; if not, write to the Free Software
 *  Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA
 *
 *  Filename: MealPart
 *  Purpose:  This is datalayer file (data file, with methods). 
 *      This one is used for Meal Parts data
 *
 *  Author:   andyrozman  {andy@atech-software.com}
 */


package ggc.core.db.datalayer;

import ggc.core.db.GGCDb;
import ggc.core.nutrition.GGCTreeRoot;
import ggc.core.util.DataAccess;

import java.util.ArrayList;
import java.util.StringTokenizer;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;



public class MealPart 
{

    private static Log log = LogFactory.getLog(MealPart.class); 
    
    DataAccess m_da = DataAccess.getInstance();
    public boolean debug = false;
    private int meal_type = 0;
    //private long meal_type_id = 0L;

    private Meal meal_obj_meal = null;
    private FoodDescription meal_obj_food = null;
    private float amount = 0.0f;
    
    private ArrayList<MealNutrition> nutritions = null; 
    

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


    public MealPart()
    {
	log.warn("MealPart was created with empty constructor");
    }

    
    public MealPart(int type, Object obj, float amount)
    {
	this.meal_type = type;
	
	if ((this.meal_type == GGCTreeRoot.TREE_USDA_NUTRITION) ||
	    (this.meal_type == GGCTreeRoot.TREE_USER_NUTRITION))
	{
	    this.meal_obj_food = (FoodDescription)obj;
//	    this.meal_type_id = this.meal_obj_food.getId();
	    this.amount = amount;
	}
	else
	{
	    this.meal_obj_meal = (Meal)obj;
//	    this.meal_type_id = this.meal_obj_meal.getId();
	    this.amount = amount;
	}
	
    }

    
    public void loadMealPart(int type, String id)
    {
	if ((type == GGCTreeRoot.TREE_USDA_NUTRITION) ||
	    (type == GGCTreeRoot.TREE_USER_NUTRITION))
	{
	    this.meal_obj_food = m_da.tree_roots.get("" + type).m_foods_ht.get(id);
	    System.out.println("MealPart [Food]: " + this.meal_obj_food + ",type=" + type + ",id=" + id);
	}
	else
	{
	    this.meal_obj_meal = m_da.tree_roots.get("3").m_meals_ht.get(id);
	    System.out.println("MealPart [Meal]: " + this.meal_obj_food + ",id=" + id);
	}
	
    }
    
    /*
    p    ublic void createDailiyFoodEntry()
    {
	if ((this.meal_type == GGCTreeRoot.TREE_USDA_NUTRITION) ||
		    (this.meal_type == GGCTreeRoot.TREE_USER_NUTRITION))
	{
	    DailyFoodEntry dfe = new DailyFoodEntry()
	    
	}
	else
	{
	}
		
		
    }
    */
    
    
    public long getId()
    {
	if ((this.meal_type == GGCTreeRoot.TREE_USDA_NUTRITION) ||
	    (this.meal_type == GGCTreeRoot.TREE_USER_NUTRITION))
	{
	    return this.meal_obj_food.getId();
	}
	else
	{
	    return this.meal_obj_meal.getId();
	}
    }

    
    public String getName()
    {
	if ((this.meal_type == GGCTreeRoot.TREE_USDA_NUTRITION) ||
	    (this.meal_type == GGCTreeRoot.TREE_USER_NUTRITION))
	{
	    return this.meal_obj_food.getName();
	}
	else
	{
	    return this.meal_obj_meal.getName();
	}
    }
    
    public int getType()
    {
	return this.meal_type;
    }
    
    
    
    
    public float getAmount()
    {
	return this.amount;
    }
    
    
    public void setAmount(float amount)
    {
	this.amount = amount;
    }
    

    public FoodDescription getFoodObject()
    {
	return this.meal_obj_food;
    }
    
    
    public Meal getMealObject()
    {
	return this.meal_obj_meal;
    }
    
    /*
    public DailyFoodEntry getDailyFoodEntry()
    {
	if ((this.meal_type == GGCTreeRoot.TREE_USDA_NUTRITION) ||
	    (this.meal_type == GGCTreeRoot.TREE_USER_NUTRITION))
	{
	    return new DailyFoodEntry()
	    return this.meal_obj_food.getId();
		}
		else
		{
		    return this.meal_obj_meal.getId();
		}
	
	
	
	return null;
    }
    */
    
    


    private void loadNutritions()
    {
	String nutr;
	
	if ((this.meal_type == GGCTreeRoot.TREE_USDA_NUTRITION) ||
            (this.meal_type == GGCTreeRoot.TREE_USER_NUTRITION))
	{
	    nutr = this.meal_obj_food.getNutritions();
	}
	else
	{
	    nutr = this.meal_obj_meal.getNutritions();
	}
	
	this.nutritions = new ArrayList<MealNutrition>();
	
	StringTokenizer strtok = new StringTokenizer(nutr, ";");
	
	while(strtok.hasMoreTokens())
	{
	    MealNutrition mn = new MealNutrition(strtok.nextToken(), true);
	    this.nutritions.add(mn);
	}
	
    }
    
    
    public ArrayList<MealNutrition> getNutrients()
    {
	if (this.nutritions == null)
	    loadNutritions();
	
	return this.nutritions;
    }
    
    
    
    @Override
    public String toString()
    {
        return "MealPart";
    }


}


