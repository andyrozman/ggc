package ggc.core.db.tool.transfer;

import ggc.core.db.GGCDb;
import ggc.core.db.hibernate.DayValueH;
import ggc.core.util.DataAccess;

import java.io.File;
import java.util.Iterator;

import com.atech.db.hibernate.transfer.BackupRestoreWorkGiver;
import com.atech.db.hibernate.transfer.ExportTool;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.cfg.Configuration;

public class ExportDailyValues extends ExportTool implements Runnable
{

    public ExportDailyValues(BackupRestoreWorkGiver giver)
    {
        super(DataAccess.getInstance().getDb().getConfiguration());

        checkPrerequisitesForAutoBackup();

        this.setStatusReceiver(giver);
        this.setTypeOfStatus(ExportTool.STATUS_SPECIAL);

        // exportAll();
    }

    public ExportDailyValues(Configuration cfg)
    {
        super(cfg);

        this.setTypeOfStatus(ExportNutritionDb.STATUS_DOT);

        checkPrerequisites();
        exportAll();
    }

    private void checkPrerequisites()
    {
        File f = new File("../data");

        if (!f.exists())
            f.mkdir();

        f = new File("../data/export");

        if (!f.exists())
            f.mkdir();

        this.setRootPath("../data/export/");
        this.setFileLastPart("_" + getCurrentDateForFile());
    }

    private void checkPrerequisitesForAutoBackup()
    {
        File f = new File("../data");

        if (!f.exists())
            f.mkdir();

        f = new File("../data/export");

        if (!f.exists())
            f.mkdir();

        f = new File("../data/export/tmp");

        if (!f.exists())
            f.mkdir();

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
     * 
     * } }
     */

    @SuppressWarnings("unchecked")
    private void export_DailyValues()
    {
        openFile(this.getRootPath() + "DayValueH" + this.getFileLastPart() + ".dbe");
        // "../data/export/DayValueH_" + getCurrentDateForFile() + ".txt");
        writeHeader("ggc.core.db.hibernate.DayValueH",
            "id; dt_info; bg; ins1; ins2; ch; meals_ids; extended; person_id; comment; changed", DataAccess
                    .getInstance().current_db_version);

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

    public void run()
    {
        exportAll();
    }

    public static void main(String[] args)
    {
        GGCDb db = new GGCDb();
        db.initDb();
        new ExportDailyValues(db.getConfiguration());
    }

}