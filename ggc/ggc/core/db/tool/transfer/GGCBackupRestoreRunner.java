package ggc.core.db.tool.transfer;

import ggc.core.util.DataAccess;

import java.io.File;
import java.util.GregorianCalendar;
import java.util.Hashtable;

import com.atech.db.hibernate.transfer.BackupRestoreObject;
import com.atech.db.hibernate.transfer.BackupRestoreRunner;
import com.atech.db.hibernate.transfer.BackupRestoreWorkGiver;
import com.atech.i18n.I18nControlAbstract;
import com.atech.utils.file.PackFiles;


/**
 *  AddressDialog 
 * 
 *  This is dialog for adding PersonAddress or InternalAddress, to either Person 
 *  or InternalPerson.
 * 
 *  This class is part of PIS (Parish Information System) package.
 * 
 *  @author      Andy (Aleksander) Rozman {andy@triera.net}
 *  @version     1.0
 */

public class GGCBackupRestoreRunner extends BackupRestoreRunner
{

    private static final long serialVersionUID = 1L;
    
    DataAccess da = DataAccess.getInstance();
    I18nControlAbstract ic = da.getI18nControlInstance(); 
    
    
    public GGCBackupRestoreRunner(Hashtable<String,BackupRestoreObject> objects, BackupRestoreWorkGiver giver)
    {
	super(objects, giver);
        //this.ht_backup_objects = objects;
    }

    
    public void executeBackupRestore()
    {
	
	if (this.isBackupRestoreObjectSelected(ic.getMessage("DAILY_VALUES")))
	{
	    this.setTask(ic.getMessage("DAILY_VALUES"));
	    ExportDailyValues edv = new ExportDailyValues(this);
	    edv.run();
	    this.setStatus(100);
	    //this.done_backup_elements++;
	}
	
	
	if (isAnyNutritionObjectSelected())
	{
	    ExportNutritionDb end = new ExportNutritionDb(this);
	    
	    
	    if (this.isBackupRestoreObjectSelected(ic.getMessage("USER_FOOD_GROUPS")))
	    {
		this.setStatus(0);
		this.setTask(ic.getMessage("USER_FOOD_GROUPS"));
		end.export_UserFoodGroups();
		this.setStatus(100);
		//this.done_backup_elements++;
		//System.out.println("Food Groups YES");
	    }

	    if (this.isBackupRestoreObjectSelected(ic.getMessage("FOODS")))
	    {
		this.setStatus(0);
		this.setTask(ic.getMessage("FOODS"));
		end.export_UserFoods();
		this.setStatus(100);
		//this.done_backup_elements++;
//		System.out.println("Foods YES");
	    }
	
	
	    if (this.isBackupRestoreObjectSelected(ic.getMessage("MEAL_GROUPS")))
	    {
		this.setStatus(0);
		this.setTask(ic.getMessage("MEAL_GROUPS"));
		end.export_MealGroups();
		this.setStatus(100);
		//this.done_backup_elements++;
		//System.out.println("Meal Groups YES");
	    }
	
	
	    if (this.isBackupRestoreObjectSelected(ic.getMessage("MEALS")))
	    {
		this.setStatus(0);
		this.setTask(ic.getMessage("MEALS"));
		end.export_Meals();
		this.setStatus(100);
		//this.done_backup_elements++;
		//System.out.println("Meals YES");
	    }
	}
	
	
	zipAndRemoveBackupFiles();
	
    }
    
    
    
    
    

    private boolean isAnyNutritionObjectSelected()
    {
	if ((this.isBackupRestoreObjectSelected(ic.getMessage("USER_FOOD_GROUPS"))) ||
	    (this.isBackupRestoreObjectSelected(ic.getMessage("FOODS"))) ||
	    (this.isBackupRestoreObjectSelected(ic.getMessage("MEAL_GROUPS"))) ||
	    (this.isBackupRestoreObjectSelected(ic.getMessage("MEALS"))))
	    return true;
	else
	    return false;
	
	
    }
    
    
    
    private void zipAndRemoveBackupFiles()
    {
        String file_out = "../data/export/backup " + getCurrentDate() + ".zip";
        
        File directory = new File("../data/export/tmp/");
        
        PackFiles.zipFilesInDirectory(directory, file_out);
        
        removeBackupFiles(directory);
    }
    
    
    private void removeBackupFiles(File root)
    {
        File[] files = root.listFiles();
        
        for(int i=0; i<files.length; i++)
        {
            files[i].delete();
        }
    }
    

    protected String getCurrentDate()
    {
        GregorianCalendar gc = new GregorianCalendar();
        gc.setTimeInMillis(System.currentTimeMillis());

        return gc.get(GregorianCalendar.DAY_OF_MONTH)  + "." + (gc.get(GregorianCalendar.MONTH) +1) + "." + gc.get(GregorianCalendar.YEAR) + "  " +
        da.getLeadingZero(gc.get(GregorianCalendar.HOUR_OF_DAY), 2) + "_" + da.getLeadingZero(gc.get(GregorianCalendar.MINUTE), 2) + "_" + da.getLeadingZero(gc.get(GregorianCalendar.SECOND), 2);
    }    
    
    
    
    
    public void run()
    {
	super.run();
    }
    
    



}


