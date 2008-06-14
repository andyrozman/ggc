package ggc.core.db.tool.transfer;

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
 *  Filename: ImportDailyValues
 *  Purpose:  Imports daily values (from export from Meter Tool Import, or some export) 
 *
 *  Author:   andyrozman  {andy@atech-software.com}
 */


import ggc.core.db.GGCDb;
import ggc.core.db.hibernate.FoodUserDescriptionH;
import ggc.core.db.hibernate.FoodUserGroupH;
import ggc.core.db.hibernate.MealGroupH;
import ggc.core.db.hibernate.MealH;
import ggc.core.util.DataAccess;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.StringTokenizer;

import com.atech.db.hibernate.transfer.ImportTool;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.cfg.Configuration;


public class ImportNutrition extends ImportTool
{

    GGCDb m_db = null;
    public String file_name;
    private static Log log = LogFactory.getLog(ImportDailyValues.class); 

    DataAccess m_da = DataAccess.getInstance();

    public ImportNutrition(String file_name)
    {
	this(file_name, true);
    }

    
    public ImportNutrition(String file_name, boolean identify)
    {
	super(false);

    	m_db = new GGCDb();
    	m_db.initDb();
    	this.file_name = file_name;

    	if (identify)
    	    this.identifyAndImport();
    }
    
    
    public ImportNutrition(Configuration cfg, String file_name)
    {
	super(cfg);
    	//m_db = new GGCDb();
    	//m_db.initDb();
    	this.file_name = file_name;

    	//importDailyValues();
    	//importUserFood();
    	
    	//System.out.println();
    	
    	this.identifyAndImport();
    }

    String selected_class = null;
    
    
    public void identifyAndImport()
    {

    	this.checkFileTarget();
    	
    	if (this.selected_class.equals("None"))
    	{
    	    System.out.println("Class type for export class was unidentified. Exiting !");
    	}
    	else if (this.selected_class.equals("ggc.core.db.hibernate.FoodUserDescriptionH"))
    	{
    	    this.importUserFood();
    	}
    	else if (this.selected_class.equals("ggc.core.db.hibernate.FoodUserGroupH"))
    	{
    	    this.importUserGroups();
    	}
    	else if (this.selected_class.equals("ggc.core.db.hibernate.MealH"))
    	{
    	    this.importMeals();
    	}
    	else if (this.selected_class.equals("ggc.core.db.hibernate.MealGroupH"))
    	{
    	    this.importMealGroups();
    	}
    	
    	
//    	importUserFood();
    	//importDailyValues();
    
    	System.out.println();
	
	
    }
    
    
    
    
    
    public void checkFileTarget()
    {
	selected_class = "None";
	
	try
	{
	    BufferedReader br = new BufferedReader(new FileReader(new File(file_name)));

	    String line;
	    
	    while ((line=br.readLine())!=null) 
	    {
		//; Class: ggc.core.db.hibernate.FoodUserDescriptionH
		
		if (line.contains("Class:"))
		{
		    String[] cls = m_da.splitString(line, " ");
		    this.selected_class = cls[2];
		}
		
	    }
	    
	    
	}
	catch(Exception ex)
	{
	    
	}
    }
    
    

    public void importUserFood()
    {
	
	String line = null;

	try 
	{
	    System.out.println("\nLoading UserFoodDescription (5/dot)");

	    BufferedReader br = new BufferedReader(new FileReader(new File(file_name)));
	    
	    int i=0;

	    
	    while ((line=br.readLine())!=null) 
	    {
		if (line.startsWith(";"))
		    continue;
	
		line = m_da.replaceExpression(line, "||","| |");
		
		StringTokenizer strtok = new StringTokenizer(line, "|");

		
		
		//DayValueH dvh = new DayValueH();
		
		FoodUserDescriptionH fud = new FoodUserDescriptionH();
		
		// ; Columns: id; name; name_i18n; group_id; refuse; description; home_weights; nutritions; changed 
		long id = this.getLong(strtok.nextToken());
		
		if (id!=0)
		    fud.setId(id);
		   
		fud.setName(getString(strtok.nextToken()));
		fud.setName_i18n(getString(strtok.nextToken()));
		
		int group = getInt(strtok.nextToken());
		
		if (group==0)  // root can't have elements
		    group=1;
		
		fud.setGroup_id(group);
		
		fud.setRefuse(getFloat(strtok.nextToken()));
		fud.setDescription(getString(strtok.nextToken()));
		fud.setHome_weights(getString(strtok.nextToken()));
		fud.setNutritions(getString(strtok.nextToken()));
		fud.setChanged(getLong(strtok.nextToken()));

		m_db.addHibernate(fud);
		
		i++;
		
		if (i%5==0)
		    System.out.print(".");
	    }

	} 
	catch (Exception ex) 
	{
	    log.error("Error on importUserFood: \nData: " + line +"\nException: " + ex, ex);
	    //ex.printStackTrace();
	}
	
    }
    

    
    public void importUserGroups()
    {
	
	String line = null;

	try 
	{
	    System.out.println("\nLoading UserGroups (2/dot)");

	    BufferedReader br = new BufferedReader(new FileReader(new File(file_name)));
	    
	    int i=0;

	    
	    while ((line=br.readLine())!=null) 
	    {
		if (line.startsWith(";"))
		    continue;
	
		line = m_da.replaceExpression(line, "||","| |");
		
		StringTokenizer strtok = new StringTokenizer(line, "|");

		//; Columns: id; name; name_i18n; description; parent_id; changed 
		
		FoodUserGroupH fug = new FoodUserGroupH(); 
		
		long id = this.getLong(strtok.nextToken());
		
		if (id!=0)
		    fug.setId(id);
		   
		fug.setName(getString(strtok.nextToken()));
		fug.setName_i18n(getString(strtok.nextToken()));
		fug.setDescription(getString(strtok.nextToken()));

		
		int parent_id = getInt(strtok.nextToken());
		
		//if (group==0)  // root can't have elements
		//    group=1;
		
		fug.setParent_id(parent_id);
		fug.setChanged(getLong(strtok.nextToken()));

		m_db.addHibernate(fug);
		
		i++;
		
		if (i%2==0)
		    System.out.print(".");
	    }

	} 
	catch (Exception ex) 
	{
	    log.error("Error on importUserGroup: \nData: " + line +"\nException: " + ex, ex);
	    //ex.printStackTrace();
	}
	
    }
    
    

    
    public void importMeals()
    {
	
	String line = null;

	try 
	{
	    System.out.println("\nLoading MealsDescription (5/dot)");

	    BufferedReader br = new BufferedReader(new FileReader(new File(file_name)));
	    
	    int i=0;

	    
	    while ((line=br.readLine())!=null) 
	    {
		if (line.startsWith(";"))
		    continue;
	
		line = m_da.replaceExpression(line, "||","| |");
		
		StringTokenizer strtok = new StringTokenizer(line, "|");

		MealH ml = new MealH();
		
		// ; Columns: id; name; name_i18n; group_id; description; parts;nutritions; extended; comment; changed 
		long id = this.getLong(strtok.nextToken());
		
		if (id!=0)
		    ml.setId(id);
		   
		ml.setName(getString(strtok.nextToken()));
		ml.setName_i18n(getString(strtok.nextToken()));
		
		int group = getInt(strtok.nextToken());
		
		if (group==0)  // root can't have elements
		    group=1;
		
		ml.setGroup_id(group);
		
		ml.setDescription(getString(strtok.nextToken()));
		ml.setParts(getString(strtok.nextToken()));
		ml.setNutritions(getString(strtok.nextToken()));
		ml.setExtended(getString(strtok.nextToken()));
		ml.setComment(getString(strtok.nextToken()));
		ml.setChanged(getLong(strtok.nextToken()));

		m_db.addHibernate(ml);
		
		i++;
		
		if (i%5==0)
		    System.out.print(".");
	    }

	} 
	catch (Exception ex) 
	{
	    log.error("Error on importMeal: \nData: " + line +"\nException: " + ex, ex);
	    //ex.printStackTrace();
	}
	
    }
    
    
    
    
    public void importMealGroups()
    {
	
	String line = null;

	try 
	{
	    System.out.println("\nLoading MealGroups (2/dot)");

	    BufferedReader br = new BufferedReader(new FileReader(new File(file_name)));
	    
	    int i=0;

	    
	    while ((line=br.readLine())!=null) 
	    {
		if (line.startsWith(";"))
		    continue;
	
		line = m_da.replaceExpression(line, "||","| |");
		
		StringTokenizer strtok = new StringTokenizer(line, "|");

		// ; Columns: id; name; name_i18n; description; parent_id; changed 
		
		MealGroupH mg = new MealGroupH(); 
		
		long id = this.getLong(strtok.nextToken());
		
		if (id!=0)
		    mg.setId(id);
		   
		mg.setName(getString(strtok.nextToken()));
		mg.setName_i18n(getString(strtok.nextToken()));
		mg.setDescription(getString(strtok.nextToken()));

		
		int parent_id = getInt(strtok.nextToken());
		
		//if (group==0)  // root can't have elements
		//    group=1;
		
		mg.setParent_id(parent_id);
		mg.setChanged(getLong(strtok.nextToken()));

		m_db.addHibernate(mg);
		
		i++;
		
		if (i%2==0)
		    System.out.print(".");
	    }

	} 
	catch (Exception ex) 
	{
	    log.error("Error on importMealGroup: \nData: " + line +"\nException: " + ex, ex);
	    //ex.printStackTrace();
	}
	
    }
    
    
    
    
    
    
    public static void main(String args[])
    {
	if (args.length == 0)
	{
	    System.out.println("You need to specify import file !");
	    return;
	}
	
	//GGCDb db = new GGCDb();
	
	new ImportNutrition(args[0]);
    }


}
