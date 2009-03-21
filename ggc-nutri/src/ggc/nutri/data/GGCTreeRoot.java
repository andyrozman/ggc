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
 *  Filename: GGCTreeRoot
 *  Purpose:  Used for holding tree information for nutrition and meals
 *
 *  Author:   andyrozman
 */

package ggc.nutri.data;

import ggc.nutri.db.datalayer.FoodDescription;
import ggc.nutri.db.datalayer.FoodGroup;
import ggc.nutri.db.datalayer.Meal;
import ggc.nutri.db.datalayer.MealGroup;
import ggc.nutri.db.GGCDbNutri;


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
 *  Filename:     GGCTreeRoot
 *  Description:  Used for holding tree information for nutrition and meals
 * 
 *  Author: andyrozman {andy@atech-software.com}  
 */


public abstract class GGCTreeRoot
{

    /**
     * Tree: USDA Nutritions Db
     */
    public static int TREE_USDA_NUTRITION = 1;
    /**
     * Tree: User Nutritions Db
     */
    public static int TREE_USER_NUTRITION = 2;
    /**
     * Tree: Meals Db
     */
    public static int TREE_MEALS = 3;


    protected GGCDbNutri m_db = null;

    
    protected int m_type = TREE_USDA_NUTRITION;
    
    
    
    /**
     * Constructor
     * 
     * @param type
     * @param db
     */
    public GGCTreeRoot(int type, GGCDbNutri db)
    {
        this.m_type = type;
        this.m_db = db;
    }

    
    /**
     * Debug - printout
     * 
     * @param text
     */
    public void debug(String text)
    {
        System.out.println(text);
    }
    
    
    /**
     * Get Child
     * 
     * @param index index of child
     * @return
     */
    public abstract Object getChild(int index);
    
    
    /**
     * Get Child Count
     * 
     * @return
     */
    public abstract int getChildCount();
    
    
    /**
     * Index Of
     * 
     * @param child child object
     * @return
     */
    public abstract int indexOf(Object child);
    
    
    /**
     * Find Food Group
     * 
     * @param type
     * @param group_id
     * @return
     */
    public abstract FoodGroup findFoodGroup(int type, long group_id);

    
    /**
     * Add Food To Tree
     * 
     * @param type
     * @param fd
     */
    public abstract void addFood2Tree(int type, FoodDescription fd);
    
    
    /**
     * Remove Food From Tree
     * 
     * @param type type of food
     * @param _food food object
     * @param prev_group_id previous group id
     */
    public abstract void removeFoodFromTree(int type, FoodDescription _food, long prev_group_id);

    
    /**
     * Find Meal Group
     * 
     * @param type 
     * @param group_id 
     * @return 
     */
    public abstract MealGroup findMealGroup(int type, long group_id);

    
    /**
     * Remove Food Group From Tree
     * 
     * @param type type of food group
     * @param fg food group object
     * @param prev_parent_id previous parent id
     */
    public abstract void removeFoodGroupFromTree(int type, FoodGroup fg, long prev_parent_id);

    
    /**
     * Remove Meal Group From Tree
     * 
     * @param type meal type
     * @param mg meal group
     * @param prev_parent_id previous parent id
     */
    public abstract void removeMealGroupFromTree(int type, MealGroup mg, long prev_parent_id);

    
    /**
     * Add Food Group To Tree
     * 
     * @param type
     * @param fg
     */
    public abstract void addFoodGroup2Tree(int type, FoodGroup fg);

    
    /**
     * Add Meal Group To Tree
     * 
     * @param type type of meal group
     * @param mg meal group
     */
    public abstract void addMealGroup2Tree(int type, MealGroup mg);

    
    /**
     * Add Meal To Tree
     * 
     * @param type type of meal
     * @param _meal meal object
     */
    public abstract void addMeal2Tree(int type, Meal _meal);

    
    /**
     * Remove Meal From Tree
     * 
     * @param type meal type
     * @param _meal meal object
     * @param prev_group_id previous group id
     */
    public abstract void removeMealFromTree(int type, Meal _meal, long prev_group_id);
    
    
    /**
     * Find Food
     * 
     * @param type
     * @param food_id
     * @return
     */
    public abstract FoodDescription findFood(int type, long food_id);
    
    
    /**
     * Find Meal
     * 
     * @param type
     * @param meal_id
     * @return
     */
    public abstract Meal findMeal(int type, long meal_id);
    
}
