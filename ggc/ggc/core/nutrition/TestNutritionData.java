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
 *  Filename: NutritionTreeDialog
 *  Purpose:  Main class for displaying nutrition information.
 *
 *  Author:   andyrozman
 */

package ggc.core.nutrition;
 
import ggc.core.db.GGCDb;
import ggc.core.db.datalayer.FoodDescription;
import ggc.core.db.datalayer.FoodGroup;
import ggc.core.db.datalayer.Meal;
import ggc.core.db.datalayer.MealGroup;
import ggc.core.db.hibernate.FoodUserDescriptionH;
import ggc.core.db.hibernate.FoodUserGroupH;
import ggc.core.db.hibernate.MealGroupH;
import ggc.core.db.hibernate.MealH;
import ggc.core.util.DataAccess;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.StringTokenizer;



public class TestNutritionData  
{
    private DataAccess m_da = null;

    //private I18nControl ic = null;
    private GGCDb db = null;

    public TestNutritionData(int type) 
    {

	m_da = DataAccess.getInstance();
	
	db = new GGCDb(m_da);
	db.initDb();
	m_da.setDb(db);
	
	//m_da.setI18nControlInstance(I18nControl.getInstance());
	
	
	db.loadNutritionDbBase();
	
	if (type==1)
	{
	    System.out.println("Load Nutrition #1");
	    db.loadNutritionDb1();
	}
	else if (type==2)
	{
	    System.out.println("Load Nutrition #2");
	    db.loadNutritionDb2();
	}
	else
	{
	    
	    System.out.println("Load Nutrition #1 (Meals)");
	    db.loadNutritionDb1();
	    
	    /*
	    System.out.println("Load Nutrition #2 (Meals)");
	    db.loadNutritionDb2();
	    */
	    
	    System.out.println("Load Meals");
	    db.loadMealsDb();
	}

	
	// I don't know load
	//db.loadNutritionDefinitions();

	//createFakeData_Meals();
	//createFakeData_User();
	
	
	//System.out.println("m_da: " + m_da);
	
//	new FoodPartMainSelectorDialog(m_da, FoodPartMainSelectorDialog.SELECTOR_NUTRITION, null);
	
//	this.getKeywords(db);
	//new NutritionTreeDialog(m_da, type);
	
	/*JFrame fr = new JFrame();
	//fr.setBounds(0,0,640,480);
	//fr.setVisible(true);
	//da.setParent(fr); */
	//da.m_nutrition_treeroot = new GGCTreeRoot(1);

	/*NutritionTreeDialog ntd =*/
	
	// for dialog checking
	new NutritionTreeDialog(m_da, type);
	
	// for partitial translation checking
	//FoodPartSelectorDialog fd = new FoodPartSelectorDialog(m_da, 2, null);
	
	
    }


    public void createFakeData_Meals()
    {
	ArrayList<MealGroup> lst = new ArrayList<MealGroup>();
	
	
	lst.add(getMealGroup(1, 0));
	lst.add(getMealGroup(2, 1));
	lst.add(getMealGroup(3, 1));
	lst.add(getMealGroup(4, 3));
	lst.add(getMealGroup(5, 0));
	
	//Hashtable<String,ArrayList<FoodDescription>> ht = new Hashtable<String,ArrayList<FoodDescription>>();
	
	ArrayList<Meal> lst1 = new ArrayList<Meal>();
	lst1.add(this.getMealDescription(1, 1));
	lst1.add(this.getMealDescription(2, 3));
	
	
	GGCTreeRoot gtr = new GGCTreeRoot(GGCTreeRoot.TREE_MEALS, true);
	gtr.manualCreate2(lst, lst1);
	
	m_da.tree_roots.put(""+GGCTreeRoot.TREE_MEALS, gtr);
	
    }
    
    
    
    
    public void createFakeData_User()
    {
	ArrayList<FoodGroup> lst = new ArrayList<FoodGroup>();
	
	
	lst.add(getUserGroup(1, 0));
	lst.add(getUserGroup(2, 1));
	lst.add(getUserGroup(3, 1));
	lst.add(getUserGroup(4, 3));
	lst.add(getUserGroup(5, 0));
	
	//Hashtable<String,ArrayList<FoodDescription>> ht = new Hashtable<String,ArrayList<FoodDescription>>();
	
	ArrayList<FoodDescription> lst1 = new ArrayList<FoodDescription>();
	lst1.add(this.getUserFoodDescription(1, 1));
	lst1.add(this.getUserFoodDescription(2, 3));
	
	
	GGCTreeRoot gtr = new GGCTreeRoot(GGCTreeRoot.TREE_USER_NUTRITION, true);
	gtr.manualCreate(lst, lst1);
	
	m_da.tree_roots.put("2", gtr);
	
    }

    
    public FoodGroup getUserGroup(long id, long parent_id)
    {
	
//	    public FoodUserGroupH(String name, String name_i18n, String description, long parent_id) {

	FoodUserGroupH f = new FoodUserGroupH("Group " + id, "GROUP_" + id, "Group " + id, parent_id);
	f.setId(id);
	
	db.addHibernate(f);
	
	
	return new FoodGroup(f);
	
    }
    
    public FoodDescription getUserFoodDescription(int id, int group_id)
    {
	
//	    public FoodUserDescriptionH(long group_id, String name, String name_i18n, String description, float refuse, String nutritions, String home_weights) {

	FoodUserDescriptionH fuh = new FoodUserDescriptionH(group_id, "Food " + id, "FOOD_" + id, "Food " + id, 0.0f, "", "");
	fuh.setId(id);

	db.addHibernate(fuh);
	
	return new FoodDescription(fuh);
    }
    

    
    public MealGroup getMealGroup(long id, long parent_id)
    {
	    //public MealGroupH(String name, String name_i18n, String description, long parent_id) {

	MealGroupH f = new MealGroupH("Group " + id, "GROUP_" + id, "Group " + id, parent_id);
	f.setId(id);
	
	db.addHibernate(f);
	
	
	return new MealGroup(f);
	
    }
    
    public Meal getMealDescription(int id, long group_id)
    {
	//new MealH(group_id, "Meal " + id, "MEAL_" + id, "", "", "", "");
//	    public MealH(long group_id, String name, String name_i18n, String description, String parts, String values, String extended, String comment) {
	
	MealH fuh = new MealH(group_id, "Meal " + id, "MEAL_" + id, "", "", "", "", "");
	fuh.setId(id);

	db.addHibernate(fuh);
	
	return new Meal(fuh);
    }
    
    
    public void getKeywords(GGCDb db)
    {
	
	ArrayList<FoodDescription> lst = db.getUSDAFoodDescriptions();
	// ArrayList<SelectableInterface> lst = sb.getNutritionHomeWeights();
	
	Hashtable ht = new Hashtable();
	
	for(int i=0; i< lst.size(); i++)
	{
	    //NutritionHomeWeightType nh = (NutritionHomeWeightType)lst.get(i);
	    
	    //StringTokenizer st = new StringTokenizer(nh.getName(), "_");
	    
	    StringTokenizer st = new StringTokenizer(lst.get(i).getName_i18n(), ",");
	    
	    while (st.hasMoreTokens())
	    {
		String s = process((String)st.nextToken());
		
		if (s==null)
		    continue;

		StringTokenizer st1 = new StringTokenizer(s, " ");
		
		while(st1.hasMoreTokens())
		{

		    String ss = st1.nextToken().toUpperCase();
		    
		    if (!ht.containsKey(ss))
		    {
			ht.put(ss,ss);
		    }
		}
	    }
	}
	
	System.out.println("Keywords: " + ht.size());
	
	//ArrayList lst 
	
	ArrayList<String> lst1 = new ArrayList<String>();
	
	for(Enumeration en=ht.keys(); en.hasMoreElements(); )
	{
	    String s = (String)en.nextElement();
	    
	    lst1.add(s);
	    //s = s.replace(",", "");
	    //System.out.println(s);
	}
	
	//Collections.sort(lst1);
	
	java.util.Collections.sort(lst1);

	try
	{
	
	    java.io.BufferedWriter bw = new BufferedWriter(new FileWriter(new File("foods.ttx")));
        	
	    //BufferedFileWriter 
        	
	    for(int i=0; i< lst1.size(); i++)
	    {
		String s = lst1.get(i);
		//System.out.println(s);
		bw.write(s);
		bw.newLine();
		bw.flush();
	    }
	    
	    bw.close();
	}
	catch(Exception ex)
	{
	    
	}
	
	
    }
    
    
    public String process(String input)
    {
//	input = input.replaceAll(",", "");
	input = input.replace("(", "");
	input = input.replace(")", "");
	input = input.replace("\"", "");

	
	if (isNumber(input))
	{
	    return null;
	}
	
	
	return input;
    }
    
    
    public boolean isNumber(String input)
    {
	try
	{
	    Float.parseFloat(input);
	    return true;
	}
	catch(Exception ex)
	{
	    return false;
	}
	
    }
    
    

    public static void main(String args[])
    {
	
	new TestNutritionData(GGCTreeRoot.TREE_USER_NUTRITION);
	//new TestNutritionData(GGCTreeRoot.TREE_MEALS);
    }


}
