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

package ggc.core.nutrition;

import ggc.core.db.GGCDb;
import ggc.core.db.datalayer.FoodDescription;
import ggc.core.db.datalayer.FoodGroup;
import ggc.core.db.datalayer.Meal;
import ggc.core.db.datalayer.MealGroup;
import ggc.core.util.DataAccess;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Iterator;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

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


public class GGCTreeRoot
{

    private Log log = LogFactory.getLog(GGCTreeRoot.class);

    private int m_type = TREE_USDA_NUTRITION;

    private ArrayList<FoodGroup> import1_grp = null;
    private ArrayList<FoodDescription> import1_foods = null;

    private ArrayList<MealGroup> import2_grp = null;
    private ArrayList<Meal> import2_foods = null;

    /**
     * Food Groups
     */
    public ArrayList<FoodGroup> m_groups = null;

    /**
     * Food Groups as Hashtable
     */
    public Hashtable<String, FoodGroup> m_groups_ht = null;
    
    /**
     * Food Groups Tree
     */
    public ArrayList<FoodGroup> m_groups_tree = null;
    
    /**
     * Foods Hashtable
     */
    public Hashtable<String, FoodDescription> m_foods_ht = null;


    /**
     * Meal Groups
     */
    public ArrayList<MealGroup> m_meal_groups = null;

    /**
     * Meal Groups Hashtable
     */
    public Hashtable<String, MealGroup> m_meal_groups_ht = null;
    /**
     * Meal Groups Tree
     */
    public ArrayList<MealGroup> m_meal_groups_tree = null;
    /**
     * Meals Hashtable
     */
    public Hashtable<String, Meal> m_meals_ht = null;


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

    GGCDb m_db = null;
    //private boolean debug = true;
    private boolean dev = false;

    /**
     * Constructor
     * 
     * @param type
     * @param db
     */
    public GGCTreeRoot(int type, GGCDb db)
    {
        m_type = type;
        this.m_db = db;

        // loadTypeData();
        readDbData(type);

        initReceivedData();
        createGroupTree();
        fillGroups();
    }

    
    /**
     * Constructor
     * 
     * @param type
     */
    public GGCTreeRoot(int type)
    {
        this(type, false);
    }

    /**
     * Constructor
     * 
     * @param type
     * @param dev
     */
    public GGCTreeRoot(int type, boolean dev)
    {
        m_type = type;
        this.m_db = DataAccess.getInstance().getDb();

        if (!dev)
        {
            readDbData(type);

            initReceivedData();
            createGroupTree();
            fillGroups();
        }
    }

    /*
    public void manualCreate(ArrayList<FoodGroup> lst_grp, ArrayList<FoodDescription> lst_food)
    {

        this.import1_grp = lst_grp;
        this.import1_foods = lst_food;

        initReceivedData();
        createGroupTree();
        fillGroups();
    }

    public void manualCreate2(ArrayList<MealGroup> lst_grp, ArrayList<Meal> lst_food)
    {

        this.import2_grp = lst_grp;
        this.import2_foods = lst_food;

        initReceivedData();
        createGroupTree();
        fillGroups();
    }
*/
    
    /**
     * Read Db Data
     * 
     * @param type
     */
    public void readDbData(int type)
    {
        // System.out.println("ReadDbData [type=" + type + "]");

        if (dev == true)
            return;

        if (type == GGCTreeRoot.TREE_USDA_NUTRITION)
        {
            log.trace("USDA Db Load !");
            //log.trace("USDA Load Started !");
            this.import1_grp = this.m_db.getUSDAFoodGroups();
            this.import1_foods = this.m_db.getUSDAFoodDescriptions();
            //log.trace("USDA Load Ended !");
        }
        else if (type == GGCTreeRoot.TREE_USER_NUTRITION)
        {
            log.trace("USER Db Load !");
            //log.trace("USER Load Started !");
            this.import1_grp = this.m_db.getUserFoodGroups();
            this.import1_foods = this.m_db.getUserFoodDescriptions();
            //log.trace("USDA Load Ended !");
        }
        else if (type == GGCTreeRoot.TREE_MEALS)
        {
            log.trace("Meals Db Load !");
            //log.trace("Meals Load Started !");
            this.import2_grp = this.m_db.getMealGroups();
            this.import2_foods = this.m_db.getMeals();
            //log.trace("Meal Load Ended !");
        }
        else
            log.error("Unknown database type: " + type);

    }

    // create group list (for tree) and group hashtable (for editing)
    private void initReceivedData()
    {
        if ((this.m_type == GGCTreeRoot.TREE_USDA_NUTRITION) || (this.m_type == GGCTreeRoot.TREE_USER_NUTRITION))
        {
            this.m_groups_tree = new ArrayList<FoodGroup>();
            this.m_groups_ht = new Hashtable<String, FoodGroup>();

            // create group hashtable and tree
            Iterator<FoodGroup> it = this.import1_grp.iterator();

            while (it.hasNext())
            {
                FoodGroup fg = it.next();
                this.m_groups_ht.put("" + fg.getId(), fg);
                this.m_groups_tree.add(fg);
            }
        }
        else if (this.m_type == GGCTreeRoot.TREE_MEALS)
        {

            this.m_meal_groups_tree = new ArrayList<MealGroup>();
            this.m_meal_groups_ht = new Hashtable<String, MealGroup>();

            // create group hashtable and tree
            Iterator<MealGroup> it = this.import2_grp.iterator();

            while (it.hasNext())
            {
                MealGroup fg = it.next();
                this.m_meal_groups_ht.put("" + fg.getId(), fg);
                this.m_meal_groups_tree.add(fg);
            }

        }

    }

    private void createGroupTree()
    {
        if (this.m_type == GGCTreeRoot.TREE_USER_NUTRITION)
        {

            ArrayList<FoodGroup> rt = new ArrayList<FoodGroup>();

            for (int i = 0; i < this.import1_grp.size(); i++)
            {
                FoodGroup fg = this.import1_grp.get(i);

                if (fg.getParentId() == 0)
                    rt.add(fg);
                else
                {
                    this.m_groups_ht.get("" + fg.getParentId()).addChild(fg);
                }
            }

            this.m_groups_tree = rt;

        }
        else if (this.m_type == GGCTreeRoot.TREE_MEALS)
        {
            ArrayList<MealGroup> rt = new ArrayList<MealGroup>();

            for (int i = 0; i < this.import2_grp.size(); i++)
            {
                MealGroup fg = this.import2_grp.get(i);

                if (fg.getParent_id() == 0)
                    rt.add(fg);
                else
                {
                    this.m_meal_groups_ht.get("" + fg.getParent_id()).addChild(fg);
                }
            }

            this.m_meal_groups_tree = rt;
        }

    }

    
    /**
     * Add Meal Group
     * 
     * @param mg
     */
    public void addMealGroup(MealGroup mg)
    {
        if (mg.getParent_id() == 0)
        {
            this.m_meal_groups_ht.put("" + mg.getId(), mg);
            this.m_meal_groups_tree.add(mg);
        }
        else
        {
            this.m_meal_groups_ht.put("" + mg.getId(), mg);
            this.m_meal_groups_ht.get("" + mg.getParent_id()).addChild(mg);
        }
    }

    /**
     * Add Food Group
     * 
     * @param fg
     */
    public void addFoodGroup(FoodGroup fg)
    {
        if (fg.getParentId() == 0)
        {
            this.m_groups_ht.put("" + fg.getId(), fg);
            this.m_groups_tree.add(fg);
        }
        else
        {
            this.m_groups_ht.put("" + fg.getId(), fg);
            this.m_groups_ht.get("" + fg.getParentId()).addChild(fg);
        }
    }

    private void fillGroups()
    {
        if ((m_type == GGCTreeRoot.TREE_USDA_NUTRITION) || (m_type == GGCTreeRoot.TREE_USER_NUTRITION))
        {

            this.m_foods_ht = new Hashtable<String, FoodDescription>();

            Iterator<FoodDescription> it2 = this.import1_foods.iterator();

            while (it2.hasNext())
            {
                FoodDescription fd = it2.next();
                this.m_foods_ht.put("" + fd.getId(), fd);
                this.m_groups_ht.get("" + fd.getGroup_id()).addChild(fd);
            }

        }
        else if (m_type == GGCTreeRoot.TREE_MEALS)
        {
            this.m_meals_ht = new Hashtable<String, Meal>();

            Iterator<Meal> it2 = this.import2_foods.iterator();

            while (it2.hasNext())
            {
                Meal fd = it2.next();
                this.m_meals_ht.put("" + fd.getId(), fd);
                this.m_meal_groups_ht.get("" + fd.getGroup_id()).addChild(fd);
            }

        }
    }

    /**
     * Get Type
     * 
     * @return
     */
    public int getType()
    {
        return m_type;
    }

    /**
     * To String
     * 
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString()
    {
        if (m_type == GGCTreeRoot.TREE_USDA_NUTRITION)
            return DataAccess.getInstance().getNutriI18nControl().getMessage("USDA_NUTRITION_DB");
        else if (m_type == GGCTreeRoot.TREE_USER_NUTRITION)
            return DataAccess.getInstance().getNutriI18nControl().getMessage("USER_NUTRITION_DB");
        else
            return DataAccess.getInstance().getNutriI18nControl().getMessage("MEALS_DB");
    }

}
