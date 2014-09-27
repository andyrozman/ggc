package ggc.nutri.plugin;

import ggc.core.db.datalayer.DailyValue;
import ggc.core.db.hibernate.DayValueH;
import ggc.core.db.tool.transfer.ExportNutritionDb;
import ggc.core.db.tool.transfer.ImportNutrition;
import ggc.nutri.util.DataAccessNutri;

import com.atech.db.hibernate.transfer.BackupRestoreObject;
import com.atech.db.hibernate.transfer.BackupRestoreRunner;
import com.atech.i18n.I18nControlAbstract;
import com.atech.plugin.BackupRestorePlugin;

/**
 *  Application:   GGC - GNU Gluco Control
 *  Plug-in:       GGC PlugIn Base (base class for all plugins)
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
 *  Filename:     PluginDb  
 *  Description:  This is master class for using Db instance within plug-in. In most cases, we 
 *                would want data to be handled by outside authority (GGC), but in some cases
 *                we wouldn't want that.
 * 
 *  Author: Andy {andy@atech-software.com}
 */

public class BackupRestoreNutriHandler extends BackupRestorePlugin
{

    I18nControlAbstract ic = DataAccessNutri.getInstance().getI18nControlInstance();

    // Hashtable<String, Backup>ht_restore_objects

    /**
     * Constructor
     */
    public BackupRestoreNutriHandler()
    {
    }

    /**
     * Do Backup
     */
    @Override
    public void doBackup(BackupRestoreRunner brr)
    {
        if (isAnyNutritionBackupObjectSelected(brr))
        {
            ExportNutritionDb end = new ExportNutritionDb(brr);

            // System.out.println(brr.isBackupObjectSelected(ic.getMessage("USER_FOOD_GROUPS")));
            // System.out.println(brr.isBackupObjectSelected(ic.getMessage("FOODS"))
            // + ic.getMessage("FOODS"));
            // System.out.println(brr.isBackupObjectSelected(ic.getMessage("MEAL_GROUPS")));
            // System.out.println(brr.isBackupObjectSelected(ic.getMessage("MEALS")));

            if (brr.isBackupObjectSelected(ic.getMessage("USER_FOOD_GROUPS")))
            {
                brr.setStatus(0);
                brr.setTask(ic.getMessage("USER_FOOD_GROUPS"));
                end.export_UserFoodGroups();
                brr.setStatus(100);
                // this.done_backup_elements++;
            }

            if (brr.isBackupObjectSelected(ic.getMessage("FOODS")))
            {
                brr.setStatus(0);
                brr.setTask(ic.getMessage("FOODS"));
                end.export_UserFoods();
                brr.setStatus(100);
                // this.done_backup_elements++;
            }

            if (brr.isBackupObjectSelected(ic.getMessage("MEAL_GROUPS")))
            {
                brr.setStatus(0);
                brr.setTask(ic.getMessage("MEAL_GROUPS"));
                end.export_MealGroups();
                brr.setStatus(100);
                // this.done_backup_elements++;
            }

            if (brr.isBackupObjectSelected(ic.getMessage("MEALS")))
            {
                brr.setStatus(0);
                brr.setTask(ic.getMessage("MEALS"));
                end.export_Meals();
                brr.setStatus(100);
                // this.done_backup_elements++;
                // System.out.println("Meals YES");
            }
        }

    }

    private boolean isAnyNutritionBackupObjectSelected(BackupRestoreRunner brr)
    {
        if (brr.isBackupObjectSelected(ic.getMessage("USER_FOOD_GROUPS"))
                || brr.isBackupObjectSelected(ic.getMessage("FOODS"))
                || brr.isBackupObjectSelected(ic.getMessage("MEAL_GROUPS"))
                || brr.isBackupObjectSelected(ic.getMessage("MEALS")))
            return true;
        else
            return false;
    }

    /**
     * Do Restore
     */
    @Override
    public void doRestore(BackupRestoreRunner brr)
    {

        if (brr.isRestoreObjectSelected("ggc.core.db.hibernate.FoodUserGroupH"))
        {
            brr.setStatus(0);
            brr.setTask(ic.getMessage("USER_FOOD_GROUPS"));
            ImportNutrition edv = new ImportNutrition(brr, brr.getRestoreObject("ggc.core.db.hibernate.FoodUserGroupH"));
            edv.run();
            // end.export_UserFoodGroups();
            brr.setStatus(100);
            // this.done_backup_elements++;
        }

        if (brr.isRestoreObjectSelected("ggc.core.db.hibernate.FoodUserDescriptionH"))
        {
            brr.setStatus(0);
            brr.setTask(ic.getMessage("FOODS"));
            ImportNutrition edv = new ImportNutrition(brr,
                    brr.getRestoreObject("ggc.core.db.hibernate.FoodUserDescriptionH"));
            edv.run();
            // end.export_UserFoods();
            brr.setStatus(100);
            // this.done_backup_elements++;
        }

        if (brr.isRestoreObjectSelected("ggc.core.db.hibernate.MealGroupH"))
        {
            brr.setStatus(0);
            brr.setTask(ic.getMessage("MEAL_GROUPS"));
            ImportNutrition edv = new ImportNutrition(brr, brr.getRestoreObject("ggc.core.db.hibernate.MealGroupH"));
            edv.run();
            // end.export_MealGroups();
            brr.setStatus(100);
            // this.done_backup_elements++;
        }

        if (brr.isRestoreObjectSelected("ggc.core.db.hibernate.MealH"))
        {
            brr.setStatus(0);
            brr.setTask(ic.getMessage("MEALS"));
            ImportNutrition edv = new ImportNutrition(brr, brr.getRestoreObject("ggc.core.db.hibernate.MealH"));
            edv.run();
            // end.export_Meals();
            brr.setStatus(100);
            // this.done_backup_elements++;
        }

        // System.out.println("BackupRestoreNutriHandler::doRestore()");
    }

    /** 
     * Get Backup Restore Object
     */
    @Override
    public BackupRestoreObject getBackupRestoreObject(String class_name)
    {
        if (class_name.equals("ggc.core.db.hibernate.DayValueH"))
            return new DailyValue();

        return null;

    }

    /** 
     * Get Backup Restore Object
     */
    @Override
    public BackupRestoreObject getBackupRestoreObject(Object obj, BackupRestoreObject bro)
    {
        if (bro.getBackupClassName().equals("ggc.core.db.hibernate.DayValueH"))
        {
            DayValueH eh = (DayValueH) obj;
            return new DailyValue(eh);
        }

        return null;

    }

    /** 
     * Does Contain Backup Restore Object
     */
    @Override
    public boolean doesContainBackupRestoreObject(String bro_name)
    {
        return false;
    }

    /*
     * public RestoreFileInfo getRestoreObject(String key)
     * {
     * if (this.ht_restore_objects==null)
     * return null;
     * if (this.ht_restore_objects.containsKey(key))
     * return this.ht_restore_objects.get(key);
     * else
     * return null;
     * }
     */

}
