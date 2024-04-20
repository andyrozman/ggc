package ggc.core.db.tool.transfer;

import java.util.Iterator;

import org.hibernate.Query;
import org.hibernate.Session;

import com.atech.db.hibernate.HibernateConfiguration;
import com.atech.db.hibernate.tool.data.management.common.ImportExportStatusType;
import com.atech.db.hibernate.transfer.BackupRestoreWorkGiver;
import com.atech.db.hibernate.transfer.ExportTool;

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
 *  Filename:     ExportSettings  
 *  Description:  Export Settings (will be deprecated)
 * 
 *  Author: andyrozman {andy@atech-software.com}  
 */

public class ExportSettings extends ExportTool implements Runnable
{

    /**
     * Constructor
     * 
     * @param giver
     */
    public ExportSettings(BackupRestoreWorkGiver giver)
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
    public ExportSettings(HibernateConfiguration cfg)
    {
        super(cfg);

        this.setTypeOfStatus(ImportExportStatusType.Dot);

        checkPrerequisites();
        exportAll();
    }


    private void checkPrerequisites() {
        this.setRootPath(ImpExpUtil.getExportPath());
        this.setFileLastPart("_" + getCurrentDateForFile());
    }


    private void checkPrerequisitesForAutoBackup() {
        this.setRootPath(ImpExpUtil.getExportPathTemp());
        this.setFileLastPart("");
    }


    /**
     * Get Active Session
     */
    @Override
    public int getActiveSession()
    {
        return 2;
    }


    private void exportAll()
    {
        export_Settings();
    }


    /*
     * private void sleep(long ms) { try { Thread.sleep(ms); } catch(Exception
     * ex) {
     * } }
     */

    @SuppressWarnings("unchecked")
    private void export_Settings()
    {
        openFile(this.getRootPath() + "SettingsH" + this.getFileLastPart() + ".dbe");
        // "../data/export/DayValueH_" + getCurrentDateForFile() + ".txt");
        writeHeader("ggc.core.db.hibernate.settings.SettingsH", "id; key; value; type; description; person_id",
            GGCDb.CURRENT_DB_VERSION);

        Session sess = getSession();

        Query q = sess.createQuery("select grp from ggc.core.db.hibernate.SettingsH as grp order by grp.id asc");

        this.statusSetMaxEntry(q.list().size());

        Iterator it = q.iterate();

        int dot_mark = 5;
        int count = 0;

        while (it.hasNext())
        {
            SettingsH eh = (SettingsH) it.next();

            this.writeToFile(eh.getId() + "|" + eh.getKey() + "|" + eh.getValue() + "|" + eh.getType() + "|"
                    + eh.getDescription() + "|" + eh.getPersonId() + "\n");

            // sleep(25);
            count++;
            this.writeStatus(dot_mark, count);
        }

        closeFile();
    }


    /**
     * Run
     */
    public void run()
    {
        exportAll();
    }


    /**
     * @param args
     */
    public static void main(String[] args)
    {
        GGCDb db = new GGCDb();
        db.initDb();
        new ExportSettings(db.getHibernateConfiguration());
    }

}
