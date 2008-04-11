package ggc.core.db.tool.export;

import ggc.core.db.GGCDb;
import ggc.core.db.hibernate.FoodUserDescriptionH;
import ggc.core.db.hibernate.FoodUserGroupH;
import ggc.core.db.hibernate.MealGroupH;
import ggc.core.db.hibernate.MealH;

import java.util.Iterator;

import com.atech.db.hibernate.transfer.ExportTool;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.cfg.Configuration;


public class ExportNutritionDb extends ExportTool
{
    
    
    public ExportNutritionDb(Configuration cfg)
    {
	super(cfg);
	
	exportAll();
    }

    
    private void exportAll()
    {
	export_UserFoodGroups();
	export_UserFoods();
	
	export_MealGroups();
	export_Meals();
    }

    
    @SuppressWarnings("unchecked")
    private void export_UserFoodGroups()
    {
	openFile("../data/export/food_user_group.txt");
	writeHeader("ggc.core.db.hibernate.FoodUserGroupH", 
		    "id; name; name_i18n; description; parent_id");
	
        Session sess = getSession();

        Query q = sess.createQuery("select grp from ggc.core.db.hibernate.FoodUserGroupH as grp");

        Iterator it = q.iterate();

        while (it.hasNext())
        {
            FoodUserGroupH eh = (FoodUserGroupH)it.next();
            
            this.writeToFile(eh.getId() + ";" + eh.getName() + ";" + 
        	             eh.getName_i18n() + ";" + eh.getDescription() + 
        	             ";" + eh.getParent_id() + "\n");
        }
        
        closeFile();
    }
    
    
    @SuppressWarnings("unchecked")
    private void export_UserFoods()
    {
	openFile("../data/export/food_user_foods.txt");
	writeHeader("ggc.core.db.hibernate.FoodUserDescriptionH", 
		    "id; name; name_i18n; group_id; refuse; description; home_weights; nutritions");
	
        Session sess = getSession();

        Query q = sess.createQuery("select grp from ggc.core.db.hibernate.FoodUserDescriptionH as grp");

        Iterator it = q.iterate();

        while (it.hasNext())
        {
            FoodUserDescriptionH eh = (FoodUserDescriptionH)it.next();
            
            this.writeToFile(eh.getId() + ";" + eh.getName() + ";" + 
        	             eh.getName_i18n() + ";" + eh.getGroup_id() + ";" +
        	             eh.getRefuse() + ";" + eh.getDescription() + ";" +
        	             eh.getHome_weights() + ";" + eh.getNutritions() +
        	             "\n");
        }
        
        closeFile();
    }
    
    
    
    
    @SuppressWarnings("unchecked")
    private void export_MealGroups()
    {
	openFile("../data/export/meal_groups.txt");
	writeHeader("ggc.core.db.hibernate.MealGroupH", 
		    "id; name; name_i18n; description; parent_id");
	
        Session sess = getSession();

        Query q = sess.createQuery("select grp from ggc.core.db.hibernate.MealGroupH as grp");

        Iterator it = q.iterate();

        while (it.hasNext())
        {
            MealGroupH eh = (MealGroupH)it.next();
            
            writeToFile(eh.getId() + ";" + eh.getName() + ";" + 
        	        eh.getName_i18n() + ";" + eh.getDescription() + 
        	        ";" + eh.getParent_id() + "\n");
        }
        
        closeFile();
    }


    @SuppressWarnings("unchecked")
    private void export_Meals()
    {
	openFile("../data/export/meal_meals.txt");
	writeHeader("ggc.core.db.hibernate.MealH", 
		    "id; name; name_i18n; group_id; description; parts;" +
		    "nutritions; extended; comment");
	
        Session sess = getSession();

        Query q = sess.createQuery("select grp from ggc.core.db.hibernate.MealH as grp");

        Iterator it = q.iterate();

        while (it.hasNext())
        {
            MealH eh = (MealH)it.next();
            
            this.writeToFile(eh.getId() + ";" + eh.getName() + ";" + 
        	             eh.getName_i18n() + ";" + eh.getGroup_id() + ";" +
        	             eh.getDescription() + ";" + eh.getParts() + ";" + 
        	             eh.getNutritions() + ";" + eh.getExtended() + ";" +
        	             eh.getComment() + "\n" );
        }
        
        closeFile();
    }
    
    
    public static void main(String[] args)
    {
	GGCDb db = new GGCDb();
	new ExportNutritionDb(db.getConfiguration());
    }
    
    
}