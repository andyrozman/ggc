package ggc.core.db.tool.transfer;

import ggc.core.util.DataAccess;

import java.io.File;
import java.util.Enumeration;
import java.util.GregorianCalendar;
import java.util.Hashtable;

import javax.swing.JMenu;

import com.atech.db.hibernate.transfer.BackupRestoreObject;
import com.atech.db.hibernate.transfer.BackupRestoreRunner;
import com.atech.db.hibernate.transfer.BackupRestoreWorkGiver;
import com.atech.db.hibernate.transfer.RestoreFileInfo;
import com.atech.i18n.I18nControlAbstract;
import com.atech.plugin.PlugInClient;
import com.atech.utils.file.PackFiles;

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
 *  Filename:     GGCBackupRestoreRunner
 *  Description:  GGC Backup Restore Runner
 * 
 *  Author: andyrozman {andy@atech-software.com}  
 */

public class GGCBackupRestoreRunner extends BackupRestoreRunner
{


    DataAccess da = DataAccess.getInstance();
    I18nControlAbstract ic = da.getI18nControlInstance();
    boolean restore_with_append = true;

    /**
     * Constructor
     * 
     * @param objects
     * @param giver
     */
    public GGCBackupRestoreRunner(Hashtable<String, BackupRestoreObject> objects, BackupRestoreWorkGiver giver)
    {
        super(objects, giver);
        // this.ht_backup_objects = objects;
    }

    /**
     * Constructor
     * 
     * @param objects
     * @param work_giver
     * @param special
     */
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
    
    
    
    
    /**
     * Execute Backup
     * 
     * @see com.atech.db.hibernate.transfer.BackupRestoreRunner#executeBackup()
     */
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

        if (this.isBackupObjectSelected(ic.getMessage("COLOR_SCHEMES")))
        {
            this.setTask(ic.getMessage("COLOR_SCHEMES"));
            GGCExporter ge = new GGCExporter(this);
            //ge.setBackupObject("ggc.core.db.hibernate.ColorSchemeH");
            //ge.export();
            ge.exportData("ggc.core.db.hibernate.ColorSchemeH");
            //ge.run();
            this.setStatus(100);
        }
        
        
        for(Enumeration<String> en= da.getPlugins().keys(); en.hasMoreElements(); )
        {
            String key = en.nextElement();
            PlugInClient pic = da.getPlugIn(key);
            
            if (pic.isBackupRestoreEnabled())
            {
                pic.getBackupRestoreHandler().doBackup(this);
            }
        }
        
        
        
//        DataAccess.getInstance().getPlugIn(DataAccess.PLUGIN_NUTRITION).getBackupRestoreHandler().doBackup(this); 
        
        /*
        
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
        }*/

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

    /*
    private boolean isAnyNutritionRestoreObjectSelected()
    {
        if ((this.isRestoreObjectSelected(ic.getMessage("USER_FOOD_GROUPS"))) ||
            (this.isRestoreObjectSelected(ic.getMessage("FOODS"))) ||
            (this.isRestoreObjectSelected(ic.getMessage("MEAL_GROUPS"))) ||
            (this.isRestoreObjectSelected(ic.getMessage("MEALS"))))
            return true;
        else
            return false;
    }*/
    
    
    private void zipAndRemoveBackupFiles()
    {
        String file_out = "../data/export/GGC_backup_" + getCurrentDate() + ".zip";

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
            "_" + da.getLeadingZero(gc.get(GregorianCalendar.DAY_OF_MONTH), 2) + "__" +
            da.getLeadingZero(gc.get(GregorianCalendar.HOUR_OF_DAY), 2) +
            "_" + da.getLeadingZero(gc.get(GregorianCalendar.MINUTE), 2) + "_" +
            da.getLeadingZero(gc.get(GregorianCalendar.SECOND), 2); 
        
        /*
        return gc.get(GregorianCalendar.DAY_OF_MONTH) + "." + (gc.get(GregorianCalendar.MONTH) + 1) + "."
                + gc.get(GregorianCalendar.YEAR) + "  " + da.getLeadingZero(gc.get(GregorianCalendar.HOUR_OF_DAY), 2)
                + "_" + da.getLeadingZero(gc.get(GregorianCalendar.MINUTE), 2) + "_"
                + da.getLeadingZero(gc.get(GregorianCalendar.SECOND), 2); */
    }
    
    
    /**
     * Execute Restore
     * 
     * @see com.atech.db.hibernate.transfer.BackupRestoreRunner#executeRestore()
     */
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

        
        if (this.isRestoreObjectSelected("ggc.core.db.hibernate.ColorSchemeH"))
        {
            //System.out.println("in color scheme");
            this.setTask(ic.getMessage("COLOR_SCHEMES"));
            GGCImporter ge = new GGCImporter(this, this.getRestoreObject("ggc.core.db.hibernate.ColorSchemeH"));
            ge.importData("ggc.core.db.hibernate.ColorSchemeH");
            //ge.run();
            this.setStatus(100);
        }
        
        
//        DataAccess.getInstance().getPlugIn(DataAccess.PLUGIN_NUTRITION).getBackupRestoreHandler().doRestore(this);
  
        
        for(Enumeration<String> en= da.getPlugins().keys(); en.hasMoreElements(); )
        {
            String key = en.nextElement();
            PlugInClient pic = da.getPlugIn(key);
            
            if (pic.isBackupRestoreEnabled())
            {
                pic.getBackupRestoreHandler().doRestore(this);
            }
        }
        
        
        
//        m_da.getPlugIn(DataAccess.PLUGIN_NUTRITION).getBackupRestoreHandler().doRestore(this); 

        
        
        //if (isAnyNutritionRestoreObjectSelected())
/*        {
            
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
            
        }*/

    }
    
    
    /**
     * Run
     * 
     * @see com.atech.db.hibernate.transfer.BackupRestoreRunner#run()
     */
    public void run()
    {
        super.run();
    }

}
