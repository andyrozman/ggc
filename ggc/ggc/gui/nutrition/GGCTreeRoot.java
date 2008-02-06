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

package ggc.gui.nutrition;

import ggc.db.GGCDb;
import ggc.db.datalayer.FoodDescription;
import ggc.db.datalayer.FoodGroup;
import ggc.db.datalayer.Meal;
import ggc.db.datalayer.MealGroup;
import ggc.util.DataAccess;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Iterator;

public class GGCTreeRoot 
{

//    public static final int TREE_ROOT_NUTRITION = 1;
//    public static final int TREE_ROOT_MEALS = 2;

    private int m_type = TREE_USDA_NUTRITION;

//    public ArrayList<FoodGroup> m_foodGroups = null;
//    public Hashtable<String, ArrayList<FoodDescription>> m_foodDescByGroup = null;

    private ArrayList<FoodGroup> import1_grp = null;
    private ArrayList<FoodDescription> import1_foods= null;
    
    private ArrayList<MealGroup> import2_grp = null;
    private ArrayList<Meal> import2_foods= null;
    
    
    public ArrayList<FoodGroup> m_groups = null;
    public Hashtable<String,FoodGroup> m_groups_ht = null;
    public ArrayList<FoodGroup> m_groups_tree = null;
    
    public Hashtable<String, ArrayList<FoodDescription>> m_food_desc_by_group = null;
    
    

    public ArrayList<MealGroup> m_meal_groups = null;
    public Hashtable<String,MealGroup> m_meal_groups_ht = null;
    //public Hashtable<String, ArrayList<Meal>> m_meal_desc_by_group = null;
    public ArrayList<MealGroup> m_meal_groups_tree = null;

    
    
    
    
    //    public ArrayList<Object> m_groups = null;
//    public Hashtable<String, ArrayList<Object>> m_desc_by_group = null;
    

    public static int TREE_USDA_NUTRITION = 1;
    public static int TREE_USER_NUTRITION = 2;
    public static int TREE_MEALS = 3;

    GGCDb m_db = null;
    public boolean debug = true;
    public boolean dev = false;
    
    public GGCTreeRoot(int type, GGCDb db) 
    {
        m_type = type;
        this.m_db = db;

        //loadTypeData();
        readDbData(type);
        
	initReceivedData();
	createGroupTree();
	fillGroups();
    }


    public GGCTreeRoot(int type) 
    {
	this(type, false);
    }


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
    
    
    
    public void manualCreate(ArrayList<FoodGroup> lst_grp, ArrayList<FoodDescription> lst_food)
    {
	
	this.import1_grp = lst_grp;
	this.import1_foods = lst_food;
	
	initReceivedData();
	createGroupTree();
	fillGroups();
    }

    
    public void readDbData(int type)
    {
	System.out.println("ReadDbData [type=" + type + "]");
	
	if (dev==true)
	    return;

	System.out.println("After dev");
	
	if (type == GGCTreeRoot.TREE_USDA_NUTRITION)
	{
	    this.import1_grp = this.m_db.getUSDAFoodGroups();
	    this.import1_foods = this.m_db.getUSDAFoodDescriptions();
	    //System.out.println("USDA Load Failed !!!!!!!!");
	}
	else if (type == GGCTreeRoot.TREE_USER_NUTRITION)
	{
	    //System.out.println("USer nutrition loading !!!!!!!!");
	    this.import1_grp = this.m_db.getUserFoodGroups();
	    this.import1_foods = this.m_db.getUserFoodDescriptions();
	}
	else if (type == GGCTreeRoot.TREE_MEALS)
	{
	    System.out.println("Meals Load Failed !!!!!!!!");
	}
	else
	    System.out.println("Unknown database type: " + type);

	
    }
    
    
    public void loadTypeData()
    {
	/*
        if (m_type== GGCTreeRoot.TREE_USDA_NUTRITION)
        {
	    m_groups = m_db.getUSDAFoodGroups();
	    Iterator<FoodGroup> it = m_groups.iterator();

	    this.m_food_desc_by_group = new Hashtable<String, ArrayList<FoodDescription>>();

	    while (it.hasNext())
	    {
		//FoodGroup fg = (FoodGroup)it.next();
		FoodGroup fg = it.next();
		this.m_food_desc_by_group.put(""+fg.getId(), new ArrayList<FoodDescription>());
	    }

	    
	    ArrayList<FoodDescription> list = m_db.getFoodDescriptions();
	    Iterator<FoodDescription> it2 = list.iterator();

	    while (it2.hasNext())
	    {
		FoodDescription fd = (FoodDescription)it2.next();

		ArrayList<FoodDescription> al = this.m_food_desc_by_group.get(""+fd.getFood_group_id());
		al.add(fd);
	    }

        }
        else if (m_type == GGCTreeRoot.TREE_USER_NUTRITION)
        {
            System.out.println("Tree User Nutrition load fails. Not Implemented yet.");
            
	    m_groups = m_db.getUserFoodGroups();
	    Iterator<FoodGroup> it = m_groups.iterator();

	    this.m_food_desc_by_group = new Hashtable<String, ArrayList<FoodDescription>>();

	    while (it.hasNext())
	    {
		//FoodGroup fg = (FoodGroup)it.next();
		FoodGroup fg = it.next();
		this.m_food_desc_by_group.put(""+fg.getId(), new ArrayList<FoodDescription>());
	    }

	    
	    ArrayList<FoodDescription> list = m_db.getFoodDescriptions();
	    Iterator<FoodDescription> it2 = list.iterator();

	    while (it2.hasNext())
	    {
		FoodDescription fd = (FoodDescription)it2.next();

		ArrayList<FoodDescription> al = this.m_food_desc_by_group.get(""+fd.getFood_group_id());
		al.add(fd);
	    }
            
            
        }
        else
        {
	    // meals -- Not implemented yet
            System.out.println("Tree Meals data load fails. Not Implemented yet.");
            
	    m_groups = m_db.getUSDAFoodGroups();
	    Iterator<FoodGroup> it = m_groups.iterator();

	    this.m_food_desc_by_group = new Hashtable<String, ArrayList<FoodDescription>>();

	    while (it.hasNext())
	    {
		//FoodGroup fg = (FoodGroup)it.next();
		FoodGroup fg = it.next();
		this.m_food_desc_by_group.put(""+fg.getId(), new ArrayList<FoodDescription>());
	    }

	    
	    ArrayList<FoodDescription> list = m_db.getFoodDescriptions();
	    Iterator<FoodDescription> it2 = list.iterator();

	    while (it2.hasNext())
	    {
		FoodDescription fd = (FoodDescription)it2.next();

		ArrayList<FoodDescription> al = this.m_food_desc_by_group.get(""+fd.getFood_group_id());
		al.add(fd);
	    }
            
            
        }
*/	
    }
    

    // create group list (for tree) and group hashtable (for editing)
    public void initReceivedData()
    {
	if ((this.m_type==GGCTreeRoot.TREE_USDA_NUTRITION) ||
	    (this.m_type==GGCTreeRoot.TREE_USER_NUTRITION))
	{
	    this.m_groups_tree = new ArrayList<FoodGroup>();
	    this.m_groups_ht = new Hashtable<String,FoodGroup>();

	    System.out.println("Db output: " + this.import1_grp);
	    
	    
	    // create group hashtable and tree
	    Iterator<FoodGroup> it = this.import1_grp.iterator();

	    while (it.hasNext())
	    {
		FoodGroup fg = it.next();
		this.m_groups_ht.put("" + fg.getId(), fg);
		this.m_groups_tree.add(fg);
	    }
	}
/*	else if (this.m_type==GGCTreeRoot.TREE_USER_NUTRITION)
	{

	    this.m_groups_tree = new ArrayList<FoodGroup>();
	    this.m_groups_ht = new Hashtable<String,FoodGroup>();

	    // create group hashtable and tree
	    Iterator<FoodGroup> it = this.import1_grp.iterator();

	    while (it.hasNext())
	    {
		FoodGroup fg = it.next();
		this.m_groups_ht.put("" + fg.getId(), fg);
		this.m_groups_tree.add(fg);
	    }
	    
	    
	    
	    
	    this.m_groups_ht = new Hashtable<String,FoodGroup>();

	    // create group hashtable
	    Iterator<FoodGroup> it = this.import1_grp.iterator();

	    while (it.hasNext())
	    {
		FoodGroup fg = it.next();
		this.m_groups_ht.put("" + fg.getId(), fg);
	    }
	    
	    
	    this.m_groups_tree = new ArrayList<FoodGroup>();
	    for(int i=0; i<this.m_groups.size(); i++)
	    {
		FoodGroup fg = this.m_groups.get(i);
		//int id = this.m_groups.get(i).getId();
		
		for(int j=0; j<this.m_groups.size(); j++)
		{
		    if (j==i)
			continue;
		    
		    FoodGroup fg2 = this.m_groups.get(j);
		    
		    if (fg2.getParentId()==fg.getId())
			fg.addChild(fg2);
		}
		
		if (fg.getParentId()==0)
		    this.m_groups_tree.add(fg);
	    }
	} */
	else if (this.m_type==GGCTreeRoot.TREE_MEALS)
	{
	    System.out.println("CreateTree Meals failed");
	}
	
    }
    
    
    public void createGroupTree()
    {
/*	if (this.m_type==GGCTreeRoot.TREE_USDA_NUTRITION)
	{
	    this.m_groups_tree = this.import1_grp; 
	}
	else*/ if (this.m_type==GGCTreeRoot.TREE_USER_NUTRITION)  
		 //(this.m_type==GGCTreeRoot.TREE_USDA_NUTRITION)) 
	{
	    //this.m_groups_tree = new ArrayList<FoodGroup>();

	    ArrayList<FoodGroup> rt = new ArrayList<FoodGroup>();
	    
	    for(int i=0; i< this.import1_grp.size(); i++)
	    {
		FoodGroup fg = this.import1_grp.get(i);
		
/*		if (fg.getGroupType()==1)
		{
		    rt.add(fg);
		}
		else */
		{
		    if (fg.getParentId()==0)
			rt.add(fg);
		    else
		    {
			this.m_groups_ht.get("" + fg.getParentId()).addChild(fg);
		    }
		}
		
	    }
	    
	    this.m_groups_tree = rt;
	    
	    /*
	    this.m_groups_tree = new ArrayList<FoodGroup>();
	    this.m_groups_ht = new Hashtable<String,FoodGroup>();

	    // create group hashtable and tree
	    Iterator<FoodGroup> it = this.import1_grp.iterator();

	    while (it.hasNext())
	    {
		FoodGroup fg = it.next();
		this.m_groups_ht.put("" + fg.getId(), fg);
		this.m_groups_tree.add(fg);
	    }*/
	    
	}
	else if (this.m_type==GGCTreeRoot.TREE_MEALS)
	{
	    System.out.println("CreateTree Meals failed");
	}
	
    }
    
    
    public void fillGroups()
    {
	if ((m_type == GGCTreeRoot.TREE_USDA_NUTRITION) || (m_type == GGCTreeRoot.TREE_USER_NUTRITION))
	{

	    Iterator<FoodDescription> it2 = this.import1_foods.iterator();

	    System.out.println("Groups HT: " + this.m_groups_ht);
	    
	    while (it2.hasNext())
	    {
		FoodDescription fd = it2.next();
//		System.out.println("FoodDescription: group=" + fd.getFood_group_id() + "id=" + fd.getId() );
		this.m_groups_ht.get("" + fd.getFood_group_id()).addChild(fd);
	    }
	    
	    
	    //System.out.println("fillGroups::nutrition done (I Hope) !!!");
	}
	else if (m_type == GGCTreeRoot.TREE_MEALS)
	{
	    System.out.println("fillGroups::meal failed !!!");
	}
    }
    
    
    public void addMealsData()
    {
	
    }
    
    public int getType()
    {
	return m_type;
    }
    
    
    @Override
    public String toString()
    {

	if (m_type==GGCTreeRoot.TREE_USDA_NUTRITION)
            return DataAccess.getInstance().m_i18n.getMessage("USDA_NUTRITION_DB");
	else if (m_type==GGCTreeRoot.TREE_USER_NUTRITION)
            return DataAccess.getInstance().m_i18n.getMessage("USER_NUTRITION_DB");
        else
            return DataAccess.getInstance().m_i18n.getMessage("MEALS_DB");

    }


}
