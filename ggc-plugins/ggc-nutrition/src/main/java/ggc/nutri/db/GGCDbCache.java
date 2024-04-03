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

package ggc.nutri.db;

import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;

import com.atech.graphics.dialogs.selector.SelectableInterface;

import ggc.nutri.data.GGCTreeRoot;
import ggc.nutri.db.datalayer.*;

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
 *  Filename:     GGCDbCache
 *  Description:  Used for holding information for nutrition and meals
 * 
 *  Author: andyrozman {andy@atech-software.com}  
 */

public class GGCDbCache
{

    /**
     * Object Load Status - None
     */
    public static final int OBJECT_LOADED_STATUS_NONE = 0;

    /**
     * Object Load Status - Loaded
     */
    public static final int OBJECT_LOADED_STATUS_OBJECT_LOADED = 1;

    /**
     * Object Load Status - Children Loaded
     */
    public static final int OBJECT_LOADED_STATUS_CHILDREN_LOADED = 2;

    GGCDbNutri m_db = null;

    /**
     * Tree Roots
     */
    public Map<String, GGCTreeRoot> tree_roots = null;

    /**
     * Data: Nutrition Definitions (Hashtable)
     */
    public Map<String, NutritionDefinition> nutrition_defs = null;

    /**
     * Data: Home Weight Definitions (Hashtable)
     */
    public Map<String, NutritionHomeWeightType> homeweight_defs = null;

    /**
     * Data: Nutrition Definitions (ArrayList)
     */
    public List<SelectableInterface> nutrition_defs_list = null;

    /**
     * Data: Home Weight Definitions (ArrayList)
     */
    public List<SelectableInterface> homeweight_defs_list = null;

    /**
     * Food Groups as Hashtable
     */
    public Map<String, Map<String, FoodGroup>> food_groups = null;

    /**
     * Foods as Hashtable
     */
    public Map<String, Map<String, FoodDescription>> foods = null;

    /**
     * Food Groups as Hashtable
     */
    public Map<String, Map<String, MealGroup>> meal_groups = null;

    /**
     * Foods as Hashtable
     */
    public Map<String, Map<String, Meal>> meals = null;


    /**
     * Constructor
     * 
     * @param db
     */
    public GGCDbCache(GGCDbNutri db)
    {
        this.m_db = db;

        food_groups = new HashMap<String, Map<String, FoodGroup>>();
        food_groups.put("1", new Hashtable<String, FoodGroup>());
        food_groups.put("2", new Hashtable<String, FoodGroup>());

        foods = new Hashtable<String, Map<String, FoodDescription>>();
        foods.put("1", new Hashtable<String, FoodDescription>());
        foods.put("2", new Hashtable<String, FoodDescription>());

        meal_groups = new Hashtable<String, Map<String, MealGroup>>();
        meal_groups.put("3", new Hashtable<String, MealGroup>());

        meals = new Hashtable<String, Map<String, Meal>>();
        meals.put("3", new Hashtable<String, Meal>());

        this.tree_roots = new Hashtable<String, GGCTreeRoot>();

        // food_groups_usda = new Hashtable<String, FoodGroup>();
        // foods_usda = new Hashtable<String, FoodDescription>();

        // food_groups_user = new Hashtable<String, FoodGroup>();
        // foods_user = new Hashtable<String, FoodDescription>();

        // meal_groups = new Hashtable<String, MealGroup>();
        // meals = new Hashtable<String, Meal>();

    }


    /**
     * Add Meal Group
     * 
     * @param mg
     */
    public void addMealGroup(MealGroup mg)
    {
        // debug("addMealGroup :: Not Implemented Yet !");

        this.addMealGroup2Tree(3, mg);

        /*
         * if (mg.getParentId() == 0)
         * {
         * this.m_meal_groups_ht.put("" + mg.getId(), mg);
         * this.m_meal_groups_tree.add(mg);
         * }
         * else
         * {
         * this.m_meal_groups_ht.put("" + mg.getId(), mg);
         * this.m_meal_groups_ht.get("" + mg.getParentId()).addChild(mg);
         * }
         */
    }


    /**
     * Add Food Group
     * 
     * @param fg
     */
    public void addFoodGroup(FoodGroup fg)
    {
        // debug("addFoodGroup :: Not Implemented Yet !");

        this.addFoodGroup2Tree(fg.getGroupType(), fg);

        /*
         * if (fg.getParentId() == 0)
         * {
         * this.m_groups_ht.put("" + fg.getId(), fg);
         * this.m_groups_tree.add(fg);
         * }
         * else
         * {
         * this.m_groups_ht.put("" + fg.getId(), fg);
         * this.m_groups_ht.get("" + fg.getParentId()).addChild(fg);
         * }
         */
    }

    /*
     * private void fillGroups()
     * {
     * if ((m_type == GGCTreeRoot.TREE_USDA_NUTRITION) || (m_type ==
     * GGCTreeRoot.TREE_USER_NUTRITION))
     * {
     * this.m_foods_ht = new Hashtable<String, FoodDescription>();
     * Iterator<FoodDescription> it2 = this.import1_foods.iterator();
     * while (it2.hasNext())
     * {
     * FoodDescription fd = it2.next();
     * this.m_foods_ht.put("" + fd.getId(), fd);
     * this.m_groups_ht.get("" + fd.getGroupId()).addChild(fd);
     * }
     * }
     * else if (m_type == GGCTreeRoot.TREE_MEALS)
     * {
     * this.m_meals_ht = new Hashtable<String, Meal>();
     * Iterator<Meal> it2 = this.import2_foods.iterator();
     * while (it2.hasNext())
     * {
     * Meal fd = it2.next();
     * this.m_meals_ht.put("" + fd.getId(), fd);
     * this.m_meal_groups_ht.get("" + fd.getGroupId()).addChild(fd);
     * }
     * }
     * }
     */


    // public GGCTreeRoot

    /**
     * Add Food To Tree
     * 
     * @param type
     * @param fd
     */
    public void addFood2Tree(int type, FoodDescription fd)
    {
        // debug("addFood2Tree :: Not Implemented Yet !");

        this.food_groups.get("" + type).get("" + fd.getGroup_id()).addChild(fd);
        this.foods.get("" + type).put("" + fd.getId(), fd);
    }


    /**
     * Add Food Group To Tree
     * 
     * @param type
     * @param fg
     */
    public void addFoodGroup2Tree(int type, FoodGroup fg)
    {
        // debug("addFoodGroup2Tree :: Not Implemented Yet !");
        this.food_groups.get("" + type).get("" + fg.getParentId()).addChild(fg);
        this.food_groups.get("" + type).put("" + fg.getId(), fg);
    }


    /**
     * Add Meal To Tree
     * 
     * @param type type of meal
     * @param _meal meal object
     */
    public void addMeal2Tree(int type, Meal _meal)
    {
        // debug("addMeal2Tree :: Not Implemented Yet !");
        this.meal_groups.get("" + type).get("" + _meal.getGroupId()).addChild(_meal);
        this.meals.get("" + type).put("" + _meal.getId(), _meal);
    }


    /**
     * Add Meal Group To Tree
     * 
     * @param type type of meal group
     * @param mg meal group
     */
    public void addMealGroup2Tree(int type, MealGroup mg)
    {
        // debug("addMealGroup2Tree :: Not Implemented Yet !");
        this.meal_groups.get("" + type).get("" + mg.getParentId()).addChild(mg);
        this.meal_groups.get("" + type).put("" + mg.getId(), mg);

    }


    /**
     * Remove Food From Tree
     * 
     * @param type type of food
     * @param _food food object
     * @param prev_group_id previous group id
     */
    public void removeFoodFromTree(int type, FoodDescription _food, long prev_group_id)
    {
        // debug("removeFoodFromTree :: Not Implemented Yet !");

        this.food_groups.get("" + type).get("" + prev_group_id).removeChild(_food);
        this.foods.get("" + type).remove(_food); // .get("" +
                                                 // _meal.getId()).removeChild(fg);

    }


    /**
     * Remove Food Group From Tree
     * 
     * @param type type of food group
     * @param fg food group object
     * @param prev_parent_id previous parent id
     */
    public void removeFoodGroupFromTree(int type, FoodGroup fg, long prev_parent_id)
    {
        // debug("removeFoodGroupFromTree :: Not Implemented Yet !");
        this.food_groups.get("" + type).get("" + prev_parent_id).removeChild(fg);
    }


    /**
     * Remove Meal From Tree
     * 
     * @param type meal type
     * @param _meal meal object
     * @param prev_group_id previous group id
     */
    public void removeMealFromTree(int type, Meal _meal, long prev_group_id)
    {
        // debug("removeMealFromTree :: Not Implemented Yet !");
        this.meal_groups.get("" + type).get("" + prev_group_id).removeChild(_meal);
        this.meals.get("" + type).remove(_meal); // .get("" +
                                                 // _meal.getId()).removeChild(fg);
    }


    /**
     * Remove Meal Group From Tree
     * 
     * @param type meal type
     * @param mg meal group
     * @param prev_parent_id previous parent id
     */
    public void removeMealGroupFromTree(int type, MealGroup mg, long prev_parent_id)
    {
        // debug("removeMealGroupFromTree :: Not Implemented Yet !");
        this.meal_groups.get("" + type).get("" + prev_parent_id).removeChild(mg);
    }


    /**
     * Find Food Group
     * 
     * @param type
     * @param group_id
     * @return
     */
    public FoodGroup findFoodGroup(int type, long group_id)
    {
        if (this.food_groups.get("" + type).containsKey("" + group_id))
            return this.food_groups.get("" + type).get("" + group_id);
        else
        {
            FoodGroup mg = m_db.getFoodGroupById(type, group_id);
            this.food_groups.get("" + type).put("" + mg.getId(), mg);
            return mg;
        }
    }


    /**
     * Find Food
     * 
     * @param type
     * @param food_id
     * @return
     */
    public FoodDescription findFood(int type, long food_id)
    {
        if (this.foods.get("" + type).containsKey("" + food_id))
            return this.foods.get("" + type).get("" + food_id);
        else
        {
            FoodDescription fd = m_db.getFoodDescriptionById(type, food_id);

            this.foods.get("" + type).put("" + food_id, fd);
            return fd;
        }

    }


    /**
     * Find Meal
     * 
     * @param type
     * @param meal_id
     * @return
     */
    public Meal findMeal(int type, long meal_id)
    {
        // debug("findMeal :: Not Implemented Yet !");

        if (this.meals.get("" + type).containsKey("" + meal_id))
            return this.meals.get("" + type).get("" + meal_id);
        else
        {
            Meal ml = m_db.getMealById(type, meal_id);
            this.meals.get("" + type).put("" + meal_id, ml);
            return ml;
        }

    }


    /**
     * Find Meal Group
     * 
     * @param type 
     * @param group_id 
     * @return 
     */
    public MealGroup findMealGroup(int type, long group_id)
    {
        if (this.meal_groups.get("" + type).containsKey("" + group_id))
            return this.meal_groups.get("" + type).get("" + group_id);
        else
        {
            MealGroup mg = m_db.getMealGroupById(group_id);
            this.meal_groups.get("" + type).put("" + mg.getId(), mg);
            return mg;
        }

    }


    /**
     * Get Children Food Group
     * 
     * @param type type of food
     * @param parent_id id of parent
     * @return
     */
    public List<FoodGroup> getChildrenFoodGroup(int type, long parent_id)
    {

        boolean not_loaded = false;

        if (this.food_groups.get("" + type).containsKey("" + parent_id))
        {
            FoodGroup grp = this.food_groups.get("" + type).get("" + parent_id);

            if (grp.getLoadStatus() == GGCDbCache.OBJECT_LOADED_STATUS_CHILDREN_LOADED)
                return grp.children_group;
            else
            {
                not_loaded = true;
            }

        }
        else
        {
            not_loaded = true;
        }

        if (not_loaded)
        {
            List<FoodGroup> lst = m_db.getFoodGroups(type, parent_id);

            for (int i = 0; i < lst.size(); i++)
            {
                FoodGroup mg = lst.get(i);
                if (!this.food_groups.get("" + type).containsKey("" + mg.getId()))
                {
                    this.food_groups.get("" + type).put("" + mg.getId(), mg);
                }
            }

            return lst;
        }
        else
            return null;

    }


    /**
     * Get Children Foods
     * 
     * @param type
     * @param parent_id
     * @return
     */
    public List<FoodDescription> getChildrenFoods(int type, long parent_id)
    {

        boolean not_loaded = false;

        if (this.food_groups.get("" + type).containsKey("" + parent_id))
        {
            FoodGroup grp = this.food_groups.get("" + type).get("" + parent_id);

            if (grp.getLoadStatus() == GGCDbCache.OBJECT_LOADED_STATUS_CHILDREN_LOADED)
                return grp.children_food;
            else
            {
                not_loaded = true;
            }

        }
        else
        {
            not_loaded = true;
        }

        if (not_loaded)
        {
            List<FoodDescription> lst = m_db.getFoodsByParent(type, parent_id);

            for (int i = 0; i < lst.size(); i++)
            {
                FoodDescription mg = lst.get(i);

                if (!this.foods.get("" + type).containsKey("" + mg.getId()))
                {
                    this.foods.get("" + type).put("" + mg.getId(), mg);
                }
            }

            return lst;
        }
        else
            return null;

    }


    /**
     * Get Children Meal Group
     * 
     * @param type
     * @param parent_id
     * @return
     */
    public List<MealGroup> getChildrenMealGroup(int type, long parent_id)
    {
        boolean not_loaded = false;

        if (this.meal_groups.get("" + type).containsKey("" + parent_id))
        {
            MealGroup grp = this.meal_groups.get("" + type).get("" + parent_id);

            if (grp.getLoadStatus() == GGCDbCache.OBJECT_LOADED_STATUS_CHILDREN_LOADED)
                return grp.children_group;
            else
            {
                not_loaded = true;
            }

        }
        else
        {
            not_loaded = true;
        }

        if (not_loaded)
        {
            List<MealGroup> lst = m_db.getMealGroups(parent_id);

            for (int i = 0; i < lst.size(); i++)
            {
                MealGroup mg = lst.get(i);
                if (!this.meal_groups.get("" + type).containsKey("" + mg.getId()))
                {
                    this.meal_groups.get("" + type).put("" + mg.getId(), mg);
                }
            }

            return lst;
        }
        else
            return null;
    }


    /**
     * Get Children Meals
     * 
     * @param type
     * @param parent_id
     * @return
     */
    public List<Meal> getChildrenMeals(int type, long parent_id)
    {
        boolean not_loaded = false;

        if (this.meal_groups.get("" + type).containsKey("" + parent_id))
        {
            MealGroup grp = this.meal_groups.get("" + type).get("" + parent_id);

            if (grp.getLoadStatus() == GGCDbCache.OBJECT_LOADED_STATUS_CHILDREN_LOADED)
                return grp.children_meal;
            else
            {
                not_loaded = true;
            }

        }
        else
        {
            not_loaded = true;
        }

        if (not_loaded)
        {
            List<Meal> lst = m_db.getMealsByParent(parent_id);

            for (int i = 0; i < lst.size(); i++)
            {
                Meal mg = lst.get(i);
                if (!this.meals.get("" + type).containsKey("" + mg.getId()))
                {
                    this.meals.get("" + type).put("" + mg.getId(), mg);
                }
            }

            return lst;
        }
        else
            return null;

    }

}
