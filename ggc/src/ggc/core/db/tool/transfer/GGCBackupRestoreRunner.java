package ggc.core.db.tool.transfer;

import ggc.core.util.DataAccess;

import java.io.File;
import java.util.GregorianCalendar;
import java.util.Hashtable;

import com.atech.db.hibernate.transfer.BackupRestoreObject;
import com.atech.db.hibernate.transfer.BackupRestoreRunner;
import com.atech.db.hibernate.transfer.BackupRestoreWorkGiver;
import com.atech.db.hibernate.transfer.RestoreFileInfo;
import com.atech.i18n.I18nControlAbstract;
import com.atech.utils.file.PackFiles;

/*
 * AddressDialog
 * 
 * This is dialog for adding PersonAddress or InternalAddress, to either Person
 * or InternalPerson.
 * 
 * This class is part of PIS (Parish Information System) package.
 * 
 * @author Andy (Aleksander) Rozman {andy@triera.net}
 * 
 * @version 1.0
 */

public class GGCBackupRestoreRunner extends BackupRestoreRunner
{

    private static final long serialVersionUID = 1L;

    DataAccess da = DataAccess.getInstance();
    I18nControlAbstract ic = da.getI18nControlInstance();
    boolean restore_with_append = true;

    public GGCBackupRestoreRunner(Hashtable<String, BackupRestoreObject> objects, BackupRestoreWorkGiver giver)
    {
        super(objects, giver);
        // this.ht_backup_objects = objects;
    }

    public GGCBackupRestoreRunner(Hashtable<String,RestoreFileInfo> objects, BackupRestoreWorkGiver work_giver, String special)
    {
        super(objects, work_giver, special);
        
        if (special!=null)
        {
            if (special.equals("true"))
                this.restore_with_append = true;
            else
                this.restore_with_append = false;
        }
            
        
    }
    
    
    
    
    public void executeBackup()
    {

        if (this.isBackupObjectSelected(ic.getMessage("DAILY_VALUES")))
        {
            this.setTask(ic.getMessage("DAILY_VALUES"));
            ExportDailyValues edv = new ExportDailyValues(this);
            edv.run();
            this.setStatus(100);
            // this.done_backup_elements++;
        }

        if (this.isBackupObjectSelected(ic.getMessage("SETTINGS")))
        {
            this.setTask(ic.getMessage("SETTINGS"));
            ExportSettings edv = new ExportSettings(this);
            edv.run();
            this.setStatus(100);
            // this.done_backup_elements++;
        }

        if (isAnyNutritionBackupObjectSelected())
        {
            ExportNutritionDb end = new ExportNutritionDb(this);

            if (this.isBackupObjectSelected(ic.getMessage("USER_FOOD_GROUPS")))
            {
                this.setStatus(0);
                this.setTask(ic.getMessage("USER_FOOD_GROUPS"));
                end.export_UserFoodGroups();
                this.setStatus(100);
                // this.done_backup_elements++;
            }

            if (this.isBackupObjectSelected(ic.getMessage("FOODS")))
            {
                this.setStatus(0);
                this.setTask(ic.getMessage("FOODS"));
                end.export_UserFoods();
                this.setStatus(100);
                // this.done_backup_elements++;
            }

            if (this.isBackupObjectSelected(ic.getMessage("MEAL_GROUPS")))
            {
                this.setStatus(0);
                this.setTask(ic.getMessage("MEAL_GROUPS"));
                end.export_MealGroups();
                this.setStatus(100);
                // this.done_backup_elements++;
            }

            if (this.isBackupObjectSelected(ic.getMessage("MEALS")))
            {
                this.setStatus(0);
                this.setTask(ic.getMessage("MEALS"));
                end.export_Meals();
                this.setStatus(100);
                // this.done_backup_elements++;
                // System.out.println("Meals YES");
            }
        }

        zipAndRemoveBackupFiles();

    }

    private boolean isAnyNutritionBackupObjectSelected()
    {
        if ((this.isBackupObjectSelected(ic.getMessage("USER_FOOD_GROUPS"))) ||
            (this.isBackupObjectSelected(ic.getMessage("FOODS"))) ||
            (this.isBackupObjectSelected(ic.getMessage("MEAL_GROUPS"))) ||
            (this.isBackupObjectSelected(ic.getMessage("MEALS"))))
            return true;
        else
            return false;
    }

    private boolean isAnyNutritionRestoreObjectSelected()
    {
        if ((this.isRestoreObjectSelected(ic.getMessage("USER_FOOD_GROUPS"))) ||
            (this.isRestoreObjectSelected(ic.getMessage("FOODS"))) ||
            (this.isRestoreObjectSelected(ic.getMessage("MEAL_GROUPS"))) ||
            (this.isRestoreObjectSelected(ic.getMessage("MEALS"))))
            return true;
        else
            return false;
    }
    
    
    private void zipAndRemoveBackupFiles()
    {
        String file_out = "../data/export/GGC backup " + getCurrentDate() + ".zip";

        File directory = new File("../data/export/tmp/");

        PackFiles.zipFilesInDirectory(directory, file_out);

        removeBackupFiles(directory);
    }

    private void removeBackupFiles(File root)
    {
        File[] files = root.listFiles();

        for (int i = 0; i < files.length; i++)
        {
            files[i].delete();
        }
    }

    protected String getCurrentDate()
    {
        GregorianCalendar gc = new GregorianCalendar();
        gc.setTimeInMillis(System.currentTimeMillis());

        return gc.get(GregorianCalendar.YEAR) + "_" + da.getLeadingZero((gc.get(GregorianCalendar.MONTH) + 1), 2) +
            "_" + da.getLeadingZero(gc.get(GregorianCalendar.DAY_OF_MONTH), 2) + " " +
            da.getLeadingZero(gc.get(GregorianCalendar.HOUR_OF_DAY), 2) +
            "_" + da.getLeadingZero(gc.get(GregorianCalendar.MINUTE), 2) + "_" +
            da.getLeadingZero(gc.get(GregorianCalendar.SECOND), 2); 
        
        /*
        return gc.get(GregorianCalendar.DAY_OF_MONTH) + "." + (gc.get(GregorianCalendar.MONTH) + 1) + "."
                + gc.get(GregorianCalendar.YEAR) + "  " + da.getLeadingZero(gc.get(GregorianCalendar.HOUR_OF_DAY), 2)
                + "_" + da.getLeadingZero(gc.get(GregorianCalendar.MINUTE), 2) + "_"
                + da.getLeadingZero(gc.get(GregorianCalendar.SECOND), 2); */
    }

    
    
    public void executeRestore()
    {

        if (this.isRestoreObjectSelected("ggc.core.db.hibernate.DayValueH"))
        {
            this.setTask(ic.getMessage("DAILY_VALUES"));
            ImportDailyValues edv = new ImportDailyValues(this, this.getRestoreObject("ggc.core.db.hibernate.DayValueH"));
            edv.setImportClean(!this.restore_with_append);
            edv.run();
            this.setStatus(100);
            // this.done_backup_elements++;
        }

        if (this.isRestoreObjectSelected("ggc.core.db.hibernate.SettingsH"))
        {
            this.setTask(ic.getMessage("SETTINGS"));
            ImportSettings edv = new ImportSettings(this, this.getRestoreObject("ggc.core.db.hibernate.SettingsH"));
            edv.run();
            this.setStatus(100);
            // this.done_backup_elements++;
        }

        //if (isAnyNutritionRestoreObjectSelected())
        {
            
            if (this.isRestoreObjectSelected("ggc.core.db.hibernate.FoodUserGroupH"))
            {
                this.setStatus(0);
                this.setTask(ic.getMessage("USER_FOOD_GROUPS"));
                ImportNutrition edv = new ImportNutrition(this, this.getRestoreObject("ggc.core.db.hibernate.FoodUserGroupH"));
                edv.run();
                //end.export_UserFoodGroups();
                this.setStatus(100);
                // this.done_backup_elements++;
            }

            if (this.isRestoreObjectSelected("ggc.core.db.hibernate.FoodUserDescriptionH"))
            {
                this.setStatus(0);
                this.setTask(ic.getMessage("FOODS"));
                ImportNutrition edv = new ImportNutrition(this, this.getRestoreObject("ggc.core.db.hibernate.FoodUserDescriptionH"));
                edv.run();
//                end.export_UserFoods();
                this.setStatus(100);
                // this.done_backup_elements++;
            }

            if (this.isRestoreObjectSelected("ggc.core.db.hibernate.MealGroupH"))
            {
                this.setStatus(0);
                this.setTask(ic.getMessage("MEAL_GROUPS"));
                ImportNutrition edv = new ImportNutrition(this, this.getRestoreObject("ggc.core.db.hibernate.MealGroupH"));
                edv.run();
//                end.export_MealGroups();
                this.setStatus(100);
                // this.done_backup_elements++;
            }

            if (this.isRestoreObjectSelected("ggc.core.db.hibernate.MealH"))
            {
                this.setStatus(0);
                this.setTask(ic.getMessage("MEALS"));
                ImportNutrition edv = new ImportNutrition(this, this.getRestoreObject("ggc.core.db.hibernate.MealH"));
                edv.run();
//                end.export_Meals();
                this.setStatus(100);
                // this.done_backup_elements++;
            }
            
        }

    }
    
    
    public void run()
    {
        super.run();
    }

}
