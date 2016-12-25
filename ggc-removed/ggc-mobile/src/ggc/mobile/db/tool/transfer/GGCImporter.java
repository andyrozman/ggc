package ggc.mobile.db.tool.transfer;

import ggc.mobile.db.GGCDbMobile;
import ggc.mobile.util.DataAccessMobile;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;


public class GGCImporter
{

    GGCDbMobile m_db = null;
    public String file_name;
//    private static Log log = LogFactory.getLog(GGCImporter.class); 
    String selected_class = null;
    DataAccessMobile m_da = DataAccessMobile.getInstance();
    
    
    
    public GGCImporter(String file_name)
    {
    	this.file_name = file_name;
	this.identifyAndImport();
    }
    
    
    
    public void identifyAndImport()
    {

    	this.checkFileTarget();
    	
    	System.out.println("Importing file '" + this.file_name + "'.");
    	
    	if (this.selected_class.equals("None"))
    	{
    	    System.out.println("Class type for export class was unidentified. Exiting !");
    	    //log.debug("File was not identified as valid import file !!!");
    	}
/*    	else if (this.selected_class.equals("ggc.core.db.hibernate.FoodUserDescriptionH"))
    	{
    	    log.debug("File was identified as 'ggc.core.db.hibernate.FoodUserDescriptionH'.");
    	    ImportNutrition in = new ImportNutrition(this.file_name, false);
    	    in.importUserFood();
    	}
    	else if (this.selected_class.equals("ggc.core.db.hibernate.FoodUserGroupH"))
    	{
    	    log.debug("File was identified as 'ggc.core.db.hibernate.FoodUserGroupH'.");
    	    ImportNutrition in = new ImportNutrition(this.file_name, false);
    	    in.importUserGroups();
    	}
    	else if (this.selected_class.equals("ggc.core.db.hibernate.MealH"))
    	{
    	    log.debug("File was identified as 'ggc.core.db.hibernate.MealH'.");
    	    ImportNutrition in = new ImportNutrition(this.file_name, false);
    	    in.importMeals();
    	}
    	else if (this.selected_class.equals("ggc.core.db.hibernate.MealGroupH"))
    	{
    	    log.debug("File was identified as 'ggc.core.db.hibernate.MealGroupH'.");
    	    ImportNutrition in = new ImportNutrition(this.file_name, false);
    	    in.importMealGroups();
    	} */
    	else if (this.selected_class.equals("ggc.core.db.hibernate.DayValueH"))
    	{
    	    System.out.println("File was identified as 'ggc.core.db.hibernate.DayValueH'.");
    	    ImportDailyValues idv = new ImportDailyValues(this.file_name, false);
    	    idv.importDailyValues();
    	}
    	
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
    
    
   
    public static void main(String args[])
    {
	if (args.length == 0)
	{
	    System.out.println("You need to specify import file !");
	    return;
	}
	
	new GGCImporter(args[0]);
    }
    
    
    

}
