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
import ggc.core.db.hibernate.pen.DayValueH;
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
 *  Filename:     ImportDailyValues  
 *  Description:  Import Daily Values (will be deprecated)
 * 
 *  Author: andyrozman {andy@atech-software.com}  
 */

public class ImportDailyValues extends ImportTool implements Runnable
{

    GGCDb m_db = null;
    // public String file_name;
    private static final Logger LOG = LoggerFactory.getLogger(ImportDailyValues.class);

    DataAccess m_da = DataAccess.getInstance();
    boolean clean_db = false;


    /**
     * Constructor
     * 
     * @param giver
     */
    public ImportDailyValues(BackupRestoreWorkGiver giver)
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
    public ImportDailyValues(BackupRestoreWorkGiver giver, RestoreFileInfo res)
    {
        super(DataAccess.getInstance().getDb().getHibernateConfiguration(), res);

        this.setStatusReceiver(giver);
        this.setTypeOfStatus(ImportExportStatusType.Special);
    }


    /**
     * @param file_name
     */
    public ImportDailyValues(String file_name)
    {
        this(file_name, true);
    }


    /**
     * Constructor
     * 
     * @param file_name
     * @param identify
     */
    public ImportDailyValues(String file_name, boolean identify)
    {
        super();

        m_db = new GGCDb();
        m_db.initDb();
        createHibernateUtil(m_db.getHibernateConfiguration());
        this.restore_file = new File(file_name);

        if (identify)
        {
            importDailyValues();
        }

        System.out.println();

    }


    /**
     * Constructor
     * 
     * @param cfg
     * @param file_name
     */
    public ImportDailyValues(HibernateConfiguration cfg, String file_name)
    {
        super(cfg);
        // m_db = new GGCDb();
        // m_db.initDb();
        this.restore_file = new File(file_name);

        importDailyValues();

        System.out.println();
    }


    /**
     * Set Import Clean
     * 
     * @param clean
     */
    public void setImportClean(boolean clean)
    {
        this.clean_db = clean;
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
     * Import Daily Values
     */
    public void importDailyValues()
    {

        String line = null;
        boolean append = false;

        try
        {

            if (clean_db)
            {
                this.clearExistingData("ggc.core.db.hibernate.pen.DayValueH");
            }
            else
            {
                append = true;
            }

            System.out.println("\nLoading DailyValues (5/dot)");

            this.openFileForReading(this.restore_file);

            // BufferedReader br = new BufferedReader(new
            // FileReader(this.restore_file)); //new File(file_name)));

            // int i = 0;

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

                DayValueH dvh = new DayValueH();

                // ; Columns: id,dt_info,bg,ins1,ins2,ch,meals_ids,act,comment

                // 1|200603250730|0|10.0|0.0|0.0|null|null|
                // id; dt_info; bg; ins1; ins2; ch; meals_ids; extended;
                // person_id; comment

                if (!append)
                {
                    long id = this.getLong(strtok.nextToken());

                    if (id != 0)
                    {
                        dvh.setId(id);
                    }
                }
                else
                {
                    strtok.nextToken();
                }

                dvh.setDtInfo(getLong(strtok.nextToken()));
                dvh.setBg(getInt(strtok.nextToken()));
                dvh.setIns1((int) getFloat(strtok.nextToken()));
                dvh.setIns2((int) getFloat(strtok.nextToken()));
                dvh.setCh(getFloat(strtok.nextToken()));
                dvh.setMealsIds(getString(strtok.nextToken()));
                dvh.setExtended(getString(strtok.nextToken()));

                int person_id = this.getInt(strtok.nextToken());

                if (person_id == 0)
                {
                    dvh.setPersonId(1);
                }
                else
                {
                    dvh.setPersonId(person_id);
                }

                dvh.setComment(getString(strtok.nextToken()));
                dvh.setChanged(getLong(strtok.nextToken()));

                /*
                 * String act = getString(strtok.nextToken());
                 * if (act != null) { dvh.setExtended("ACTIVITY=" + act); }
                 * String bef = "MTI"; // String bef = null;
                 * if (strtok.hasMoreElements()) { String comm =
                 * getString(strtok.nextToken());
                 * // remove if (bef!=null) comm += ";" + bef;
                 * if (comm!=null) dvh.setComment(comm); } else { if (bef!=null)
                 * dvh.setComment(bef); }
                 */

                this.hibernateUtil.addHibernate(dvh);

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
            LOG.error("Error on importDailyValues: \nData: " + line + "\nException: " + ex, ex);
            // ex.printStackTrace();
        }

    }


    /**
     * Thread Run
     */
    public void run()
    {
        this.importDailyValues();
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

        new ImportDailyValues(args[0]);
    }

}
