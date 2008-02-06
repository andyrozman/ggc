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
 *  Filename: FoodDescription
 *  Purpose:  This is datalayer file (data file, with methods to work with database or in 
 *      this case Hibernate). 
 *      This one is used for description of food.
 *
 *  Author:   andyrozman
 */


package ggc.db.datalayer;

import ggc.util.DataAccess;


public class MealPart 
{

    public boolean debug = false;
    private int meal_type = 0;
    private long meal_type_id = 0L;

    private Meal meal_obj_meal = null;
    private FoodDescription meal_obj_food = null;
    
    

    public MealPart(String id)
    {
    }


    public MealPart()
    {
    }


    public void loadMealPart()
    {
	//DataAccess.getInstance().getDb().getMeals();
	
    }


    @Override
    public String toString()
    {
        return "MealPart";
    }


}


