package ggc.core.db.tool.transfer;

import java.io.File;
import java.util.*;

import com.atech.data.mng.DataDefinitionEntry;
import com.atech.db.hibernate.HibernateBackupObject;
import com.atech.db.hibernate.HibernateObject;
import com.atech.db.hibernate.tool.data.DatabaseImportStrategy;
import com.atech.db.hibernate.tool.data.management.impexp.DbExporter;
import com.atech.db.hibernate.transfer.BackupRestoreBase;
import com.atech.db.hibernate.transfer.BackupRestoreRunner;
import com.atech.db.hibernate.transfer.BackupRestoreWorkGiver;
import com.atech.db.hibernate.transfer.RestoreFileInfo;
import com.atech.i18n.I18nControlAbstract;
import com.atech.plugin.PlugInClient;
import com.atech.utils.ATDataAccessAbstract;
import com.atech.utils.file.zip.PackFiles;

import ggc.core.db.hibernate.doc.DoctorAppointmentH;
import ggc.core.db.hibernate.doc.DoctorH;
import ggc.core.db.hibernate.doc.DoctorTypeH;
import ggc.core.db.hibernate.settings.ColorSchemeH;
import ggc.core.db.hibernate.settings.SettingsH;
import ggc.core.db.tool.defs.GGCImportExportContext;
import ggc.core.db.tool.impexp.GGCDbImporter;
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

public class GGCBackupRestoreRunner extends BackupRestoreRunner
{

    DataAccess dataAccess = DataAccess.getInstance();
    I18nControlAbstract ic = dataAccess.getI18nControlInstance();
    boolean restoreWithAppend = false;
    private String lastBackupFile = null;

    Class singleBackupRestoreObjects[] = { //
                                           SettingsH.class, //
                                           ColorSchemeH.class //
    };

    Map<String, List<Class<? extends HibernateObject>>> groupedBackupRestoreObjects = null;
    GGCImportExportContext importExportContext = new GGCImportExportContext();
    Map<String, Class<? extends HibernateBackupObject>> backwardCompatibilityMatrix = null;


    /**
     * Constructor
     * 
     * @param objects
     * @param giver
     */
    public GGCBackupRestoreRunner(Map<String, BackupRestoreBase> objects, BackupRestoreWorkGiver giver)
    {
        super(objects, giver);
        // this.backupObjects = objects;
        // initGroups();
    }


    /**
     * Constructor
     * 
     * @param objects
     * @param work_giver
     * @param special
     */
    public GGCBackupRestoreRunner(Hashtable<String, RestoreFileInfo> objects, BackupRestoreWorkGiver work_giver,
            String special)
    {
        super(objects, work_giver, special);

        if (special != null)
        {
            if (special.equals("true"))
            {
                this.restoreWithAppend = true;
            }
            else
            {
                this.restoreWithAppend = false;
            }
        }
        // initGroups();

    }


    /**
     * Execute Backup
     * 
     * @see com.atech.db.hibernate.transfer.BackupRestoreRunner#executeBackup()
     */
    @Override
    public void executeBackup()
    {

        initGroups();

        if (this.isBackupObjectSelected(ic.getMessage("DAILY_VALUES")))
        {
            this.setTask(ic.getMessage("DAILY_VALUES"));
            ExportDailyValues edv = new ExportDailyValues(this);
            edv.run();
            this.setStatus(100);
            // this.doneBackupElements++;
        }

        // if (this.isBackupObjectSelected(i18nControl.getMessage("SETTINGS")))
        // {
        // this.setTask(i18nControl.getMessage("SETTINGS"));
        // ExportSettings edv = new ExportSettings(this);
        // edv.run();
        // this.setStatus(100);
        // // this.doneBackupElements++;
        // }

        // FIXME whats up with this
        // if (this.isBackupObjectSelected(i18nControl.getMessage("COLOR_SCHEMES")))
        // {
        // this.setTask(i18nControl.getMessage("COLOR_SCHEMES"));
        // GGCExporter ge = new GGCExporter(this);
        // // ge.setBackupObject("ggc.core.db.hibernate.settings.ColorSchemeH");
        // // ge.export();
        // ge.exportData("ggc.core.db.hibernate.settings.ColorSchemeH");
        // // ge.run();
        // this.setStatus(100);
        // }

        // single objects
        for (Class clazz : singleBackupRestoreObjects)
        {
            DataDefinitionEntry entry = dataAccess.getDataDefinitionManager().getEntry(clazz);

            if (this.isBackupObjectSelected(ic.getMessage(entry.getBackupTargetName())))
            {
                this.setTask(ic.getMessage(entry.getBackupTargetName()));
                DbExporter ge = new DbExporter(dataAccess.getHibernateDb(), this, importExportContext, false);
                ge.export(clazz, entry);
                this.setStatus(100);
            }
        }

        // grouped objects
        for (Map.Entry<String, List<Class<? extends HibernateObject>>> groupEntry : groupedBackupRestoreObjects
                .entrySet())
        {

            boolean isGroupSelected = false;

            // check if any selected
            for (Class clazz : groupEntry.getValue())
            {
                DataDefinitionEntry entry = dataAccess.getDataDefinitionManager().getEntry(clazz);

                if (this.isBackupObjectSelected(ic.getMessage(entry.getBackupTargetName())))
                {
                    isGroupSelected = true;
                    break;
                }
            }

            if (isGroupSelected)
            {
                for (Class clazz : groupEntry.getValue())
                {
                    DataDefinitionEntry entry = dataAccess.getDataDefinitionManager().getEntry(clazz);

                    this.setTask(ic.getMessage(entry.getBackupTargetName()));
                    DbExporter ge = new DbExporter(dataAccess.getHibernateDb(), this, importExportContext, false);
                    ge.export(clazz, entry);
                    this.setStatus(100);
                }
            }
        }

        // plugins
        for (PlugInClient pic : dataAccess.getPlugins().values())
        {
            //String key = en.nextElement();
            //PlugInClient pic = dataAccess.getPlugIn(key);

            if (pic.isBackupRestoreEnabled())
            {
                pic.getBackupRestoreHandler().doBackup(this);
            }
        }

        zipAndRemoveBackupFiles();

    }


    private void initGroups()
    {
        this.groupedBackupRestoreObjects = new HashMap<String, List<Class<? extends HibernateObject>>>();

        this.groupedBackupRestoreObjects.put("DoctorGroup",
            createList( //
                DoctorTypeH.class, //
                DoctorH.class, //
                DoctorAppointmentH.class));

        this.backwardCompatibilityMatrix = new HashMap<String, Class<? extends HibernateBackupObject>>();

        backwardCompatibilityMatrix.put("ggc.core.db.hibernate.SettingsH", SettingsH.class);

    }


    private List<Class<? extends HibernateObject>> createList(Class<? extends HibernateObject>... elements)
    {
        List<Class<? extends HibernateObject>> listOut = new ArrayList<Class<? extends HibernateObject>>();

        for (Class<? extends HibernateObject> hibernateObject : elements)
        {
            listOut.add(hibernateObject);
        }

        return listOut;
    }


    /*
     * private boolean isAnyNutritionBackupObjectSelected()
     * {
     * if
     * ((this.isBackupObjectSelected(i18nControl.getMessage("USER_FOOD_GROUPS"))
     * ) ||
     * (this.isBackupObjectSelected(i18nControl.getMessage("FOODS"))) ||
     * (this.isBackupObjectSelected(i18nControl.getMessage("MEAL_GROUPS"))) ||
     * (this.isBackupObjectSelected(i18nControl.getMessage("MEALS"))))
     * return true;
     * else
     * return false;
     * }
     */

    private void zipAndRemoveBackupFiles()
    {
        String file_out = ImpExpUtil.getExportPath() + "GGC_backup_" + getCurrentDate() + ".zip";

        this.lastBackupFile = file_out;

        File directory = new File(ImpExpUtil.getExportPathTemp());

        PackFiles.zipFilesInDirectory(directory, file_out);

        removeBackupFiles(directory);
    }


    private void removeBackupFiles(File root)
    {
        File[] files = root.listFiles();

        for (File file : files)
        {
            file.delete();
        }
    }


    protected String getCurrentDate()
    {
        GregorianCalendar gc = new GregorianCalendar();
        gc.setTimeInMillis(System.currentTimeMillis());

        return gc.get(Calendar.YEAR) + "_" + ATDataAccessAbstract.getLeadingZero(gc.get(Calendar.MONTH) + 1, 2) + "_"
                + ATDataAccessAbstract.getLeadingZero(gc.get(Calendar.DAY_OF_MONTH), 2) + "__"
                + ATDataAccessAbstract.getLeadingZero(gc.get(Calendar.HOUR_OF_DAY), 2) + "_"
                + ATDataAccessAbstract.getLeadingZero(gc.get(Calendar.MINUTE), 2) + "_"
                + ATDataAccessAbstract.getLeadingZero(gc.get(Calendar.SECOND), 2);

        /*
         * return gc.get(GregorianCalendar.DAY_OF_MONTH) + "." +
         * (gc.get(GregorianCalendar.MONTH) + 1) + "."
         * + gc.get(GregorianCalendar.YEAR) + "  " +
         * dataAccess.getLeadingZero(gc.get(GregorianCalendar.HOUR_OF_DAY), 2)
         * + "_" + dataAccess.getLeadingZero(gc.get(GregorianCalendar.MINUTE),
         * 2) + "_"
         * + dataAccess.getLeadingZero(gc.get(GregorianCalendar.SECOND), 2);
         */
    }


    /**
     * Execute Restore
     * 
     * @see com.atech.db.hibernate.transfer.BackupRestoreRunner#executeRestore()
     */
    @Override
    public void executeRestore()
    {
        initGroups();

        if (this.isRestoreObjectSelected("ggc.core.db.hibernate.pen.DayValueH"))
        {
            this.setTask(ic.getMessage("DAILY_VALUES"));
            ImportDailyValues edv = new ImportDailyValues(this,
                    this.getRestoreObject("ggc.core.db.hibernate.pen.DayValueH"));
            edv.setImportClean(!this.restoreWithAppend);
            edv.run();
            this.setStatus(100);
        }

        // FIXME: Remove sometime in future
        if (this.isRestoreObjectSelected("ggc.core.db.hibernate.DayValueH"))
        {
            this.setTask(ic.getMessage("DAILY_VALUES"));
            ImportDailyValues edv = new ImportDailyValues(this,
                    this.getRestoreObject("ggc.core.db.hibernate.DayValueH"));
            edv.setImportClean(!this.restoreWithAppend);
            edv.run();
            this.setStatus(100);
        }

        // if (this.isRestoreObjectSelected("ggc.core.db.hibernate.settings.SettingsH"))
        // {
        // this.setTask(i18nControl.getMessage("SETTINGS"));
        // ImportSettings edv = new ImportSettings(this,
        // this.getRestoreObject("ggc.core.db.hibernate.settings.SettingsH"));
        // edv.run();
        // this.setStatus(100);
        // }

        // Backward compatibility item because object class path changed)
        if (this.isRestoreObjectSelected("ggc.core.db.hibernate.SettingsH"))
        {
            this.setTask(ic.getMessage("SETTINGS"));
            ImportSettings edv = new ImportSettings(this, this.getRestoreObject("ggc.core.db.hibernate.SettingsH"));
            edv.run();
            this.setStatus(100);
        }

        // FIXME Backward compatibility

        for (Map.Entry<String, Class<? extends HibernateBackupObject>> oldClassEntry : backwardCompatibilityMatrix
                .entrySet())
        {
            DataDefinitionEntry entry = dataAccess.getDataDefinitionManager().getEntry(oldClassEntry.getValue());

            if (this.isBackupObjectSelected(ic.getMessage(entry.getBackupTargetName())))
            {
                DatabaseImportStrategy importStrategy = determineImportStrategy(entry);
                this.setTask(ic.getMessage(entry.getBackupTargetName()));
                GGCDbImporter ge = new GGCDbImporter(this, importExportContext,
                        this.getRestoreObject(oldClassEntry.getValue()));
                ge.importData(oldClassEntry.getValue(), entry, importStrategy);
                this.setStatus(100);
            }
        }

        // if
        // (this.isRestoreObjectSelected("ggc.core.db.hibernate.settings.ColorSchemeH"))
        // {
        // // System.out.println("in color scheme");
        // this.setTask(i18nControl.getMessage("COLOR_SCHEMES"));
        // GGCImporter ge = new GGCImporter(this,
        // this.getRestoreObject("ggc.core.db.hibernate.settings.ColorSchemeH"));
        // ge.importData("ggc.core.db.hibernate.settings.ColorSchemeH");
        // // ge.run();
        // this.setStatus(100);
        // }

        // single objects (all objects should be migrated into this code)
        for (Class clazz : singleBackupRestoreObjects)
        {
            DataDefinitionEntry entry = dataAccess.getDataDefinitionManager().getEntry(clazz);

            if (this.isBackupObjectSelected(ic.getMessage(entry.getBackupTargetName())))
            {
                DatabaseImportStrategy importStrategy = determineImportStrategy(entry);
                this.setTask(ic.getMessage(entry.getBackupTargetName()));
                GGCDbImporter ge = new GGCDbImporter(this, importExportContext, this.getRestoreObject(clazz));
                ge.importData(clazz, entry, importStrategy);
                this.setStatus(100);
            }
        }

        // grouped objects
        for (Map.Entry<String, List<Class<? extends HibernateObject>>> groupEntry : groupedBackupRestoreObjects
                .entrySet())
        {

            boolean isGroupSelected = false;

            // check if any selected
            for (Class clazz : groupEntry.getValue())
            {
                DataDefinitionEntry entry = dataAccess.getDataDefinitionManager().getEntry(clazz);

                if (this.isBackupObjectSelected(ic.getMessage(entry.getBackupTargetName())))
                {
                    isGroupSelected = true;
                    break;
                }
            }

            if (isGroupSelected)
            {
                GGCImporter gi = new GGCImporter(this);
                gi.importCleanupGroup(groupEntry.getValue());

                for (Class clazz : groupEntry.getValue())
                {
                    DataDefinitionEntry entry = dataAccess.getDataDefinitionManager().getEntry(clazz);

                    this.setTask(ic.getMessage(entry.getBackupTargetName()));
                    GGCDbImporter ge = new GGCDbImporter(this, importExportContext, this.getRestoreObject(clazz));
                    ge.importData(clazz, entry, DatabaseImportStrategy.DoNotCleanDb);
                    this.setStatus(100);
                }
            }
        }

        for (PlugInClient pic : dataAccess.getPlugins().values())
        {
            //String key = en.nextElement();
            //PlugInClient pic = dataAccess.getPlugIn(key);

            if (pic.isBackupRestoreEnabled())
            {
                pic.getBackupRestoreHandler().doRestore(this);
            }
        }

    }


    /**
     * Result can be always only Clean or Append (Clean -> Clean, Append -> Append, CleanOrAppend is determined by
     * restore with append parameter and can also return Clean or Append).
     *
     * @param entry DataDefinitionEntry instance
     * @return Clean or Append strategy
     */
    private DatabaseImportStrategy determineImportStrategy(DataDefinitionEntry entry)
    {
        DatabaseImportStrategy databaseImportStrategy = entry.getDatabaseTableConfiguration()
                .getDatabaseImportStrategy();

        if (databaseImportStrategy == DatabaseImportStrategy.CleanOrAppend)
        {
            return (this.restoreWithAppend) ? DatabaseImportStrategy.Append : DatabaseImportStrategy.Clean;
        }

        return databaseImportStrategy;
    }


    /**
     * Run
     * 
     * @see com.atech.db.hibernate.transfer.BackupRestoreRunner#run()
     */
    @Override
    public void run()
    {
        super.run();
    }


    public String getLastBackupFile()
    {
        return lastBackupFile;
    }
}
