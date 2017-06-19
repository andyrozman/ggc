/*
 * GGC - GNU Gluco Control
 * 
 * A pure java app to help you manage your diabetes.
 * 
 * See AUTHORS for copyright information.
 * 
 * This program is free software; you can redistribute it and/or modify it under
 * the terms of the GNU General Public License as published by the Free Software
 * Foundation; either version 2 of the License, or (at your option) any later
 * version.
 * 
 * This program is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU General Public License for more
 * details.
 * 
 * You should have received a copy of the GNU General Public License along with
 * this program; if not, write to the Free Software Foundation, Inc., 59 Temple
 * Place, Suite 330, Boston, MA 02111-1307 USA
 * 
 * Filename: ImportDailyValues Purpose: Imports daily values (from export from
 * Meter Tool Import, or some export)
 * 
 * Author: andyrozman {andy@atech-software.com}
 */

package ggc.core.db.tool.transfer;

import java.io.File;
import java.util.StringTokenizer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.atech.db.hibernate.HibernateConfiguration;
import com.atech.db.hibernate.tool.data.management.common.ImportExportStatusType;
import com.atech.db.hibernate.transfer.BackupRestoreWorkGiver;
import com.atech.db.hibernate.transfer.ImportTool;
import com.atech.db.hibernate.transfer.RestoreFileInfo;
import com.atech.utils.ATDataAccessAbstract;

import ggc.core.db.GGCDb;
import ggc.core.db.hibernate.settings.SettingsH;
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
 *  Filename:     ImportSettings
 *  Description:  Import Settings (will be deprecated)
 * 
 *  Author: andyrozman {andy@atech-software.com}  
 */

public class ImportSettings extends ImportTool implements Runnable
{

    GGCDb m_db = null;
    // public String file_name;
    private static final Logger LOG = LoggerFactory.getLogger(ImportSettings.class);

    DataAccess m_da = DataAccess.getInstance();


    /**
     * Constructor
     * 
     * @param file_name
     */
    public ImportSettings(String file_name)
    {
        this(file_name, true);
    }


    /**
     * Constructor
     * 
     * @param file_name
     * @param identify
     */
    public ImportSettings(String file_name, boolean identify)
    {
        super();

        m_db = new GGCDb();
        m_db.initDb();
        createHibernateUtil(m_db.getHibernateConfiguration());
        // this.file_name = file_name;
        this.restore_file = new File(file_name);

        if (identify)
        {
            importSettings();
        }

        System.out.println();

    }


    /**
     * Constructor
     * 
     * @param cfg
     * @param file_name
     */
    public ImportSettings(HibernateConfiguration cfg, String file_name)
    {
        super(cfg);
        // m_db = new GGCDb();
        // m_db.initDb();
        // this.file_name = file_name;
        this.restore_file = new File(file_name);

        importSettings();

        System.out.println();
    }


    /**
     * Constructor
     * 
     * @param giver
     */
    public ImportSettings(BackupRestoreWorkGiver giver)
    {
        super(DataAccess.getInstance().getDb().getHibernateConfiguration());

        this.setStatusReceiver(giver);
        this.setTypeOfStatus(ImportExportStatusType.Special);
    }


    /**
     * Constructor
     * 
     * @param giver
     * @param res
     */
    public ImportSettings(BackupRestoreWorkGiver giver, RestoreFileInfo res)
    {
        super(DataAccess.getInstance().getDb().getHibernateConfiguration(), res);

        this.setStatusReceiver(giver);
        this.setTypeOfStatus(ImportExportStatusType.Special);
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
     * Import Settings
     */
    public void importSettings()
    {

        String line = null;

        try
        {

            this.clearExistingData("ggc.core.db.hibernate.settings.SettingsH");

            System.out.println("\nLoading Settings (5/dot)");

            this.openFileForReading(this.restore_file);

            int dot_mark = 5;
            int count = 0;

            while ((line = this.bufferedReader.readLine()) != null)
            {
                if (line.startsWith(";"))
                {
                    continue;
                }

                // line = line.replaceAll("||", "| |");
                line = ATDataAccessAbstract.replaceExpression(line, "||", "| |");

                StringTokenizer strtok = new StringTokenizer(line, "|");

                SettingsH dvh = new SettingsH();

                // ; Columns: id; key; value; type; description; person_id

                long id = this.getLong(strtok.nextToken());

                if (id != 0)
                {
                    dvh.setId(id);
                }

                dvh.setKey(strtok.nextToken());
                dvh.setValue(strtok.nextToken());
                dvh.setType(getInt(strtok.nextToken()));
                dvh.setDescription(strtok.nextToken());

                int person_id = this.getInt(strtok.nextToken());

                if (person_id == 0)
                {
                    dvh.setPersonId(1);
                }
                else
                {
                    dvh.setPersonId(person_id);
                }

                this.hibernate_util.addHibernate(dvh);

                count++;
                this.writeStatus(dot_mark, count);

                /*
                 * i++;
                 * if (i % 5 == 0)
                 * System.out.print(".");
                 */
            }

            this.closeFile();

        }
        catch (Exception ex)
        {
            // System.err.println("Error on loadDailyValues: " + ex);
            LOG.error("Error on importSettings: \nData: " + line + "\nException: " + ex, ex);
            // ex.printStackTrace();
        }

    }


    /**
     * Thread Run
     */
    public void run()
    {
        this.importSettings();
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

        // GGCDb db = new GGCDb();

        new ImportSettings(args[0]);
    }

}
