package ggc.core.db.tool.impexp;

import java.io.File;
import java.util.Iterator;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.criterion.Order;

import com.atech.db.hibernate.HibernateConfiguration;
import com.atech.db.hibernate.tool.data.DatabaseTableConfiguration;
import com.atech.db.hibernate.tool.data.management.common.ImportExportContext;
import com.atech.db.hibernate.tool.data.management.common.ImportExportStatusType;
import com.atech.db.hibernate.tool.data.management.export.ExportTool;
import com.atech.db.hibernate.transfer.BackupRestoreWorkGiver;

import ggc.core.db.GGCDb;
import ggc.core.db.hibernate.food.FoodUserGroupH;
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
 *  Filename:     ExportNutritionDb
 *  Description:  Export Nutrition Database (will be deprecated)
 * 
 *  Author: andyrozman {andy@atech-software.com}  
 */

public class GGCDbExporter extends ExportTool
{

    String databaseVersion = null;


    /**
     * Constructor
     *
     * @param giver
     */
    public GGCDbExporter(BackupRestoreWorkGiver giver, String databaseVersion, ImportExportContext importExportContext)
    {
        super(DataAccess.getInstance().getDb().getHibernateConfiguration(), importExportContext);

        this.databaseVersion = databaseVersion;
        checkPrerequisites(true);

        this.setStatusReceiver(giver);
        this.setTypeOfStatus(ImportExportStatusType.Special);
        exportAll();
    }


    /**
     * Constructor
     *
     * @param cfg
     */
    public GGCDbExporter(HibernateConfiguration cfg, String databaseVersion, ImportExportContext importExportContext)
    {
        super(cfg, importExportContext);

        this.setTypeOfStatus(ImportExportStatusType.Dot);
        this.databaseVersion = databaseVersion;

        checkPrerequisites(false);
        exportAll();
    }


    public void exportAll()
    {
        List<DatabaseTableConfiguration> tablesForDatabase = importExportContext.getDatabaseVersionConfiguration()
                .getTablesForDatabase(this.databaseVersion);

        for (DatabaseTableConfiguration table : tablesForDatabase)
        {
            export(table);
        }
    }


    private void checkPrerequisites(boolean withAutoBackup)
    {
        String path = this.importExportContext.getExportLocation();

        if (withAutoBackup)
        {
            path += "/tmp";
        }

        File f = new File(importExportContext.getExportLocation());

        if (!f.exists())
        {
            f.mkdirs();
        }

        this.setRootPath(path);
        this.setFileLastPart(withAutoBackup ? "" : "_" + getCurrentDateForFile());
    }


    /**
     * Get Active Session
     */
    @Override
    public int getActiveSession()
    {
        return 2;
    }

    // private void exportAll()
    // {
    // export_UserFoodGroups();
    // export_UserFoods();
    //
    // export_MealGroups();
    // export_Meals();
    // }

    GGCDbExportConverter converter = new GGCDbExportConverter();


    public void export(DatabaseTableConfiguration tableConfiguration)
    {
        openFile(this.getRootPath() + tableConfiguration.getObjectName() + this.getFileLastPart() + ".dbe");

        writeHeader(tableConfiguration, GGCDb.CURRENT_DB_VERSION);

        Criteria criteria = getSession().createCriteria(tableConfiguration.getObjectClass()).addOrder(Order.asc("id"));

        List resultsData = criteria.list();

        this.statusSetMaxEntry(resultsData.size());

        Iterator it = resultsData.iterator();

        int dot_mark = 5;
        int count = 0;

        while (it.hasNext())
        {
            this.writeToFile(converter.convert(tableConfiguration, it.next()) + "\n");
            count++;
            this.writeStatus(dot_mark, count);
        }

        closeFile();
    }

    /**
     * Export User Food Group
     */
    // @SuppressWarnings("unchecked")
    // @Deprecated
    // public void export_UserFoodGroups()
    // {
    // openFile(this.getRootPath() + "FoodUserGroupH" + this.getFileLastPart() +
    // ".dbe");
    //
    // // openFile("../data/export/food_user_group.txt");
    // writeHeader("ggc.core.db.hibernate.food.FoodUserGroupH", "id; name;
    // name_i18n; description; parent_id; changed",
    // GGCDb.CURRENT_DB_VERSION);
    //
    // Session sess = getSession();
    //
    // Query q = sess.createQuery("select grp from
    // ggc.core.db.hibernate.food.FoodUserGroupH as grp order by grp.id");
    //
    // this.statusSetMaxEntry(q.list().size());
    //
    // Iterator it = q.iterate();
    //
    // int dot_mark = 5;
    // int count = 0;
    //
    // while (it.hasNext())
    // {
    // FoodUserGroupH eh = (FoodUserGroupH) it.next();
    //
    // this.writeToFile(eh.getId() + "|" + eh.getName() + "|" +
    // eh.getName_i18n() + "|" + eh.getDescription() + "|"
    // + eh.getParent_id() + "|" + eh.getChanged() + "\n");
    //
    // count++;
    // this.writeStatus(dot_mark, count);
    // }
    //
    // closeFile();
    // }

    /**
     * Export User Foods
     */
    // @SuppressWarnings("unchecked")
    // public void export_UserFoods()
    // {
    // openFile(this.getRootPath() + "FoodUserDescriptionH" +
    // this.getFileLastPart() + ".dbe");
    // // openFile("../data/export/food_user_foods.txt");
    // writeHeader("ggc.core.db.hibernate.food.FoodUserDescriptionH",
    // "id; name; name_i18n; group_id; refuse; description; home_weights;
    // nutritions; changed",
    // GGCDb.CURRENT_DB_VERSION);
    //
    // Session sess = getSession();
    //
    // Query q = sess
    // .createQuery("select grp from
    // ggc.core.db.hibernate.food.FoodUserDescriptionH as grp order by grp.id");
    //
    // this.statusSetMaxEntry(q.list().size());
    //
    // Iterator it = q.iterate();
    //
    // int dot_mark = 5;
    // int count = 0;
    //
    // while (it.hasNext())
    // {
    // FoodUserDescriptionH eh = (FoodUserDescriptionH) it.next();
    //
    // String nutr = eh.getNutritions();
    // if (nutr == null)
    // {
    // nutr = "";
    // }
    // else
    // {
    // nutr = nutr.replace(",", ".");
    // }
    //
    // this.writeToFile(eh.getId() + "|" + eh.getName() + "|" +
    // eh.getName_i18n() + "|" + eh.getGroup_id() + "|"
    // + eh.getRefuse() + "|" + eh.getDescription() + "|" + eh.getHome_weights()
    // + "|" + nutr + "|"
    // + eh.getChanged() + "\n");
    //
    // count++;
    // this.writeStatus(dot_mark, count);
    // }
    //
    // closeFile();
    // }

    /**
     * Export Meal Groups
     */
    // @SuppressWarnings("unchecked")
    // public void export_MealGroups()
    // {
    // openFile(this.getRootPath() + "MealGroupH" + this.getFileLastPart() +
    // ".dbe");
    // // openFile("../data/export/meal_groups.txt");
    // writeHeader("ggc.core.db.hibernate.food.food.MealGroupH",
    // "id; name; name_i18n; description; parent_id; changed",
    // GGCDb.CURRENT_DB_VERSION);
    //
    // Session sess = getSession();
    //
    // Query q = sess.createQuery("select grp from
    // ggc.core.db.hibernate.food.MealGroupH as grp order by grp.id");
    //
    // this.statusSetMaxEntry(q.list().size());
    //
    // Iterator it = q.iterate();
    //
    // int dot_mark = 5;
    // int count = 0;
    //
    // while (it.hasNext())
    // {
    // MealGroupH eh = (MealGroupH) it.next();
    //
    // writeToFile(eh.getId() + "|" + eh.getName() + "|" + eh.getName_i18n() +
    // "|" + eh.getDescription() + "|"
    // + eh.getParent_id() + "|" + eh.getChanged() + "\n");
    //
    // count++;
    // this.writeStatus(dot_mark, count);
    // }
    //
    // closeFile();
    // }


    /**
     * Export Meals
     */
    // @SuppressWarnings("unchecked")
    // public void export_Meals()
    // {
    // openFile(this.getRootPath() + "MealH" + this.getFileLastPart() + ".dbe");
    // // openFile("../data/export/meal_meals.txt");
    // writeHeader("ggc.core.db.hibernate.food.MealH",
    // "id; name; name_i18n; group_id; description; parts;" + "nutritions;
    // extended; comment; changed",
    // GGCDb.CURRENT_DB_VERSION);
    //
    // Session sess = getSession();
    //
    // Query q = sess.createQuery("select grp from
    // ggc.core.db.hibernate.food.MealH as grp order by grp.id");
    //
    // this.statusSetMaxEntry(q.list().size());
    //
    // Iterator it = q.iterate();
    //
    // int dot_mark = 5;
    // int count = 0;
    //
    // while (it.hasNext())
    // {
    // MealH eh = (MealH) it.next();
    //
    // this.writeToFile(eh.getId() + "|" + eh.getName() + "|" +
    // eh.getName_i18n() + "|" + eh.getGroup_id() + "|"
    // + eh.getDescription() + "|" + eh.getParts() + "|" + eh.getNutritions() +
    // "|" + eh.getExtended()
    // + "|" + eh.getComment() + "|" + eh.getChanged() + "\n");
    //
    // count++;
    // this.writeStatus(dot_mark, count);
    //
    // }
    //
    // closeFile();
    // }

    /**
     * @param args
     */
    public static void main(String[] args)
    {
        // GGCDb db = new GGCDb();
        // db.initDb();
        // new GGCDbExporter(db.getHibernateConfiguration());

        FoodUserGroupH fg = new FoodUserGroupH();

        System.out.print(fg.getClass().getName());

        File f = new File("../data5/export/");

        if (f.exists())
        {
            System.out.println("Directory already exists");
        }
        else
        {
            System.out.print(f.mkdirs());
        }

    }

}
