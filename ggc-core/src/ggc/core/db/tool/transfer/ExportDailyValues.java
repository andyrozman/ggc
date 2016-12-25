package ggc.core.db.tool.transfer;

import java.io.File;
import java.util.Iterator;

import org.hibernate.Query;
import org.hibernate.Session;

import com.atech.db.hibernate.HibernateConfiguration;
import com.atech.db.hibernate.tool.data.management.common.ImportExportAbstract;
import com.atech.db.hibernate.tool.data.management.common.ImportExportStatusType;
import com.atech.db.hibernate.transfer.BackupRestoreWorkGiver;
import com.atech.db.hibernate.transfer.ExportTool;

import ggc.core.db.GGCDb;
import ggc.core.db.hibernate.DayValueH;
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
 *  Filename:     ExportDailyValues
 *  Description:  Export Daily Values (will be deprecated)
 * 
 *  Author: andyrozman {andy@atech-software.com}  
 */

public class ExportDailyValues extends ExportTool implements Runnable
{

    /**
     * Constructor
     * 
     * @param giver
     */
    public ExportDailyValues(BackupRestoreWorkGiver giver)
    {
        super(DataAccess.getInstance().getDb().getHibernateConfiguration());

        checkPrerequisitesForAutoBackup();

        this.setStatusReceiver(giver);
        this.setTypeOfStatus(ImportExportStatusType.Special);

        // exportAll();
    }


    /**
     * Constructor
     * 
     * @param cfg
     */
    public ExportDailyValues(HibernateConfiguration cfg)
    {
        super(cfg);

        this.setTypeOfStatus(ImportExportStatusType.Dot);

        checkPrerequisites();
        exportAll();
    }


    private void checkPrerequisites()
    {
        File f = new File("../data");

        if (!f.exists())
        {
            f.mkdir();
        }

        f = new File("../data/export");

        if (!f.exists())
        {
            f.mkdir();
        }

        this.setRootPath("../data/export/");
        this.setFileLastPart("_" + getCurrentDateForFile());
    }


    private void checkPrerequisitesForAutoBackup()
    {
        File f = new File("../data");

        if (!f.exists())
        {
            f.mkdir();
        }

        f = new File("../data/export");

        if (!f.exists())
        {
            f.mkdir();
        }

        f = new File("../data/export/tmp");

        if (!f.exists())
        {
            f.mkdir();
        }

        this.setRootPath("../data/export/tmp/");
        this.setFileLastPart("");
    }


    private void exportAll()
    {
        export_DailyValues();
    }


    /*
     * private void sleep(long ms) { try { Thread.sleep(ms); } catch(Exception
     * ex) {
     * } }
     */

    /**
     * Get Active Session
     * 
     * @see ImportExportAbstract#getActiveSession()
     */
    @Override
    public int getActiveSession()
    {
        return 2;
    }


    @SuppressWarnings("unchecked")
    private void export_DailyValues()
    {
        openFile(this.getRootPath() + "DayValueH" + this.getFileLastPart() + ".dbe");
        // "../data/export/DayValueH_" + getCurrentDateForFile() + ".txt");
        writeHeader("ggc.core.db.hibernate.DayValueH",
            "id; dt_info; bg; ins1; ins2; ch; meals_ids; extended; person_id; comment; changed",
            GGCDb.CURRENT_DB_VERSION);

        Session sess = getSession();

        Query q = sess.createQuery("select grp from ggc.core.db.hibernate.DayValueH as grp order by grp.id asc");

        this.statusSetMaxEntry(q.list().size());

        Iterator it = q.iterate();

        int dot_mark = 5;
        int count = 0;

        while (it.hasNext())
        {
            DayValueH eh = (DayValueH) it.next();

            this.writeToFile(eh.getId() + "|" + eh.getDt_info() + "|" + eh.getBg() + "|" + eh.getIns1() + "|"
                    + eh.getIns2() + "|" + eh.getCh() + "|" + eh.getMeals_ids() + "|" + eh.getExtended() + "|"
                    + eh.getPerson_id() + "|" + eh.getComment() + "|" + eh.getChanged() + "\n");

            // sleep(25);
            count++;
            this.writeStatus(dot_mark, count);
        }

        closeFile();
    }


    /**
     * Run
     * 
     * @see java.lang.Runnable#run()
     */
    public void run()
    {
        exportAll();
    }

    /*
     * public static void main(String[] args)
     * {
     * GGCDb db = new GGCDb();
     * db.initDb();
     * new ExportDailyValues(db.getHibernateConfiguration());
     * }
     */
}
