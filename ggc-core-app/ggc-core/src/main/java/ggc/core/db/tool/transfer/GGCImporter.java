package ggc.core.db.tool.transfer;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.atech.data.mng.DataDefinitionEntry;
import com.atech.db.hibernate.HibernateBackupSelectableObject;
import com.atech.db.hibernate.HibernateConfiguration;
import com.atech.db.hibernate.HibernateObject;
import com.atech.db.hibernate.tool.data.management.common.ImportExportStatusType;
import com.atech.db.hibernate.transfer.BackupRestoreObject;
import com.atech.db.hibernate.transfer.BackupRestoreWorkGiver;
import com.atech.db.hibernate.transfer.ImportTool;
import com.atech.db.hibernate.transfer.RestoreFileInfo;
import com.atech.plugin.PlugInClient;

import ggc.core.db.GGCDb;
import ggc.core.db.datalayer.DailyValue;
import ggc.core.db.datalayer.SettingsColorScheme;
import ggc.core.db.hibernate.GGCHibernateBackupSelectableObject;
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
 *  Filename:     GGCImporter
 *  Description:  GGC Importer (this is importer, which will be used for all exports,
 *                when all objects will be fully implemented)
 * 
 *  Author: andyrozman {andy@atech-software.com}  
 */

public class GGCImporter extends ImportTool implements Runnable
{

    GGCDb m_db = null;
    private String file_name;
    private static final Logger LOG = LoggerFactory.getLogger(GGCImporter.class);
    String selected_class = null;
    DataAccess m_da = DataAccess.getInstance();


    /**
     * Constructor
     * 
     * @param giver
     */
    public GGCImporter(BackupRestoreWorkGiver giver)
    {
        super(DataAccess.getInstance().getDb().getHibernateConfiguration());

        // checkPrerequisitesForResAutoBackup();

        this.setStatusReceiver(giver);
        this.setTypeOfStatus(ImportExportStatusType.Special);
        this.setRootPath("../data/import/");

        // exportAll();
    }


    /**
     * Constructor
     * 
     * @param cfg
     */
    public GGCImporter(HibernateConfiguration cfg)
    {
        super(cfg);

        this.setTypeOfStatus(ImportExportStatusType.Dot);
        this.setRootPath("../data/import/");

        // checkPrerequisites();
        // exportAll();
    }


    /**
     * Constructor
     * 
     * @param giver
     * @param res
     */
    public GGCImporter(BackupRestoreWorkGiver giver, RestoreFileInfo res)
    {
        super(DataAccess.getInstance().getDb().getHibernateConfiguration(), res);

        this.setStatusReceiver(giver);
        this.setTypeOfStatus(ImportExportStatusType.Special);
        this.setRootPath("../data/import/");
    }


    /**
     * Constructor
     * 
     * @param file_name
     */
    public GGCImporter(String file_name)
    {
        this.file_name = file_name;
        this.identifyAndImport();
    }


    /**
     * Identify and Import
     */
    public void identifyAndImport()
    {

        this.checkFileTarget();

        LOG.debug("Importing file '" + this.file_name + "'.");

        if (this.selected_class.equals("None"))
        {
            System.out.println("Class type for export class was unidentified. Exiting !");
            LOG.debug("File was not identified as valid import file !!!");
        }
        else if (this.selected_class.equals("ggc.core.db.hibernate.food.FoodUserDescriptionH"))
        {
            LOG.debug("File was identified as 'ggc.core.db.hibernate.food.FoodUserDescriptionH'.");
            ImportNutrition in = new ImportNutrition(this.file_name, false);
            in.importUserFood();
        }
        else if (this.selected_class.equals("ggc.core.db.hibernate.food.FoodUserGroupH"))
        {
            LOG.debug("File was identified as 'ggc.core.db.hibernate.food.FoodUserGroupH'.");
            ImportNutrition in = new ImportNutrition(this.file_name, false);
            in.importUserGroups();
        }
        else if (this.selected_class.equals("ggc.core.db.hibernate.food.MealH"))
        {
            LOG.debug("File was identified as 'ggc.core.db.hibernate.food.MealH'.");
            ImportNutrition in = new ImportNutrition(this.file_name, false);
            in.importMeals();
        }
        else if (this.selected_class.equals("ggc.core.db.hibernate.food.MealGroupH"))
        {
            LOG.debug("File was identified as 'ggc.core.db.hibernate.food.MealGroupH'.");
            ImportNutrition in = new ImportNutrition(this.file_name, false);
            in.importMealGroups();
        }
        else if (this.selected_class.equals("ggc.core.db.hibernate.pen.DayValueH"))
        {
            LOG.debug("File was identified as 'ggc.core.db.hibernate.pen.DayValueH'.");
            ImportDailyValues idv = new ImportDailyValues(this.file_name, false);
            idv.importDailyValues();
        }
        else
        {
            LOG.error("File was not identified: " + this.selected_class);
        }

        System.out.println();

    }


    /**
     * Check File Target
     */
    public void checkFileTarget()
    {
        selected_class = "None";

        try
        {
            BufferedReader br = new BufferedReader(new FileReader(new File(file_name)));

            String line;

            while ((line = br.readLine()) != null)
            {
                // ; Class: ggc.core.db.hibernate.food.FoodUserDescriptionH

                if (line.contains("Class:"))
                {
                    String[] cls = m_da.splitString(line, " ");
                    this.selected_class = cls[2];
                }

            }

        }
        catch (Exception ex)
        {

        }
    }


    private BackupRestoreObject getBackupRestoreObject(BackupRestoreObject bro)
    {
        return getBackupRestoreObject(bro.getBackupClassName());
    }


    private BackupRestoreObject getBackupRestoreObject(String class_name)
    {
        if (class_name.equals("ggc.core.db.hibernate.pen.DayValueH"))
            // DayValueH eh = (DayValueH)obj;
            return new DailyValue();
        else if (class_name.equals("ggc.core.db.hibernate.settings.ColorSchemeH"))
            // ColorSchemeH eh = (ColorSchemeH)obj;
            return new SettingsColorScheme();

        for (Enumeration<String> en = m_da.getPlugins().keys(); en.hasMoreElements();)
        {
            String key = en.nextElement();
            System.out.println("key: " + key);
            PlugInClient pic = m_da.getPlugIn(key);

            System.out.println("PlugInClient: " + pic);
            System.out.println("pic.getBackupRestoreHandler(): " + pic.getBackupRestoreHandler());

            if (pic.getBackupRestoreHandler() != null
                    && pic.getBackupRestoreHandler().doesContainBackupRestoreObject(class_name))
                return pic.getBackupRestoreHandler().getBackupRestoreObject(class_name);
        }

        // FIXME old types, and remove this method, or at least change it

        return null;

    }


    /**
     * Import data (object name)
     * 
     * @param object_class_name
     */
    public void importData(String object_class_name)
    {
        importData(getBackupRestoreObject(object_class_name));
    }


    /**
     * Import data (object name)
     *
     * @param clazz
     * @param definitionEntry
     */
    public void importData(Class clazz, DataDefinitionEntry definitionEntry)
    {
        importData(clazz, definitionEntry, true);
    }


    /**
     * Import data (object)
     *
     * @param bro BackupRestoreObject
     */
    public void importData(BackupRestoreObject bro)
    {
        importData(bro, true);
    }


    /**
     * Import data (object)
     * 
     * @param bro BackupRestoreObject
     * @param clean_db if database should be cleaned
     */
    public void importData(BackupRestoreObject bro, boolean clean_db)
    {

        String line = null;
        boolean append = false;

        try
        {

            if (bro.hasToBeCleaned() || clean_db)
            {
                this.clearExistingData(bro.getBackupClassName());
            }
            else
            {
                append = true;
            }

            this.openFileForReading(new File(this.getRootPath() + bro.getBackupFile() + ".dbe"));

            int dot_mark = 5;
            int count = 0;
            Map<String, String> headers = new HashMap<String, String>();

            while ((line = this.bufferedReader.readLine()) != null)
            {
                if (line.startsWith(";") || line.trim().length() == 0)
                {
                    addHeaders(headers, line);
                    continue;
                }

                BackupRestoreObject bro_new = this.getBackupRestoreObject(bro);

                if (append)
                {
                    bro_new.dbImport(bro.getTableVersion(), line, new Object[1]);
                }
                else
                {
                    if (bro.isNewImport())
                        bro_new.dbImport(bro.getTableVersion(), line, headers);
                    else
                        bro_new.dbImport(bro.getTableVersion(), line);
                }

                this.hibernateUtil.add(bro_new);

                count++;
                this.writeStatus(dot_mark, count);

            }

            this.closeFile();

        }
        catch (Exception ex)
        {
            LOG.error("Error on importData: \nData: " + line + "\nException: " + ex, ex);
        }

    }


    public void importCleanupGroup(List<Class<? extends HibernateObject>> classes)
    {
        for (int i = classes.size() - 1; i >= 0; i--)
        {
            clearExistingData(classes.get(i));
        }
    }


    public void importData(Class<? extends GGCHibernateBackupSelectableObject> clazz,
            DataDefinitionEntry definitionEntry, boolean clean_db)
    {

        String line = null;
        boolean append = false;

        try
        {

            if (clean_db)
            {
                if (definitionEntry.hasToBeCleaned())
                    this.clearExistingData(clazz.getSimpleName());
            }
            else
            {
                append = true;
            }

            this.openFileForReading(new File(this.getRootPath() + clazz.getSimpleName() + ".dbe"));

            int dot_mark = 5;
            int count = 0;
            Map<String, String> headers = new HashMap<String, String>();

            while ((line = this.bufferedReader.readLine()) != null)
            {
                if (line.startsWith(";") || line.trim().length() == 0)
                {
                    addHeaders(headers, line);
                    continue;
                }

                HibernateBackupSelectableObject newEntry = clazz.getConstructor().newInstance();

                if (append)
                {
                    // this is only for DailyValueH, should be removed
                    newEntry.dbImport(definitionEntry.getTableVersion(), line, new Object[1]);
                }
                else
                {
                    newEntry.dbImport(definitionEntry.getTableVersion(), line, headers);
                }

                this.hibernateUtil.add(newEntry);

                count++;
                this.writeStatus(dot_mark, count);

            }

            this.closeFile();

        }
        catch (Exception ex)
        {
            LOG.error("Error on importData: \nData: " + line + "\nException: " + ex, ex);
        }

    }


    private void addHeaders(Map<String, String> headers, String line)
    {
        if (!line.contains(":"))
        {
            return;
        }

        String ln = line.substring(2);

        String[] pars = ln.split(":");

        headers.put(pars[0], pars[1].trim());
    }


    /**
     * @param args
     */
    public static void main(String args[])
    {
        if (args.length == 0)
        {
            System.out.println("You need to specify import file !");
            return;
        }

        new GGCImporter(args[0]);
    }


    /**
     * Get Active Session
     */
    @Override
    public int getActiveSession()
    {
        return 2;
    }


    /**
     * Thread Run method
     */
    public void run()
    {
        // TODO Auto-generated method stub

    }

}
