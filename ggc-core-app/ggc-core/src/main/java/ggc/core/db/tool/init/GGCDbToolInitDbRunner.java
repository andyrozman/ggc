package ggc.core.db.tool.init;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import com.atech.db.hibernate.HibernateUtil;
import com.atech.db.hibernate.tool.app.DbToolApplicationInterface;
import com.atech.db.hibernate.tool.data.dto.DbInitTaskDto;
import com.atech.db.hibernate.tool.data.management.init.DbToolInitDbRunner;
import com.atech.db.hibernate.transfer.BackupRestoreRunner;
import com.atech.db.hibernate.transfer.BackupRestoreWorkGiver;
import com.atech.i18n.I18nControlAbstract;

import ggc.core.db.hibernate.food.FoodDescriptionH;
import ggc.core.db.hibernate.food.NutritionHomeWeightTypeH;
import ggc.core.util.DataAccess;

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

public class GGCDbToolInitDbRunner extends DbToolInitDbRunner
{

    DataAccess da = DataAccess.getInstance();
    I18nControlAbstract ic = da.getI18nControlInstance();
    // boolean restore_with_append = true;
    // private String lastBackupFile = null;

    private Map<String, NutritionHomeWeightTypeH> homeWeightTypeMap;
    private Map<Long, FoodDescriptionH> foodDescriptionsMap;


    /**
     * Constructor
     *
    
     * @param giver
     */
    public GGCDbToolInitDbRunner(Map<String, DbInitTaskDto> dbInitTasks, HibernateUtil hibernateUtil,
            BackupRestoreWorkGiver giver, DbToolApplicationInterface applicationInterface)
    {
        // FIXME
        super(dbInitTasks, hibernateUtil, giver, applicationInterface);
        // super(objects, giver);
        // this.backupObjects = objects;
    }


    /**
     * Execute Backup
     *
     * @see BackupRestoreRunner#executeBackup()
     */

    public void executeCustomDbInit()
    {
        List<String> tasks = Arrays.asList( //
            "DBTT_SETTINGS", //
            "DBTT_DOCTOR_TYPES", //
            "DBTT_FOOD_GROUPS", //
            "DBTT_FOOD_DESCRIPTION", //
            "DBTT_NUTRITION_DEFINITIONS", //
            "DBTT_NUTRITION_DATA", //
            "DBTT_HOME_WEIGHT_TYPES", //
            "DBTT_HOME_WEIGHT_DATA");

        for (String task : tasks)
        {
            DbInitTaskDto dbInitTaskDto = this.dbInitTasks.get(task);

            if (dbInitTaskDto != null)
            {
                this.setTask(ic.getMessage(task));
                GGCCustomDbInitTasks taskRunner = new GGCCustomDbInitTasks(this.dbInitTasks.get(task), this,
                        applicationInterface.getImportExportContext());
                taskRunner.run();
                this.setStatus(100);
            }
        }

    }


    /**
     * Run
     *
     * @see BackupRestoreRunner#run()
     */
    @Override
    public void run()
    {
        super.run();
    }


    public Map<String, NutritionHomeWeightTypeH> getHomeWeightTypeMap()
    {
        return homeWeightTypeMap;
    }


    public void setHomeWeightTypeMap(Map<String, NutritionHomeWeightTypeH> homeWeightTypeMap)
    {
        this.homeWeightTypeMap = homeWeightTypeMap;
    }


    public void setFoodDescriptionsMap(Map<Long, FoodDescriptionH> foodDescriptionsMap)
    {
        this.foodDescriptionsMap = foodDescriptionsMap;
    }


    public Map<Long, FoodDescriptionH> getFoodDescriptionsMap()
    {
        return foodDescriptionsMap;
    }
}
