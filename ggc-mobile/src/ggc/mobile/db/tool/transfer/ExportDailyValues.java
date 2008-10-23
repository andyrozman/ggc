package ggc.mobile.db.tool.transfer;

import ggc.mobile.db.GGCDbMobile;
import ggc.mobile.db.objects.DayValueDAO;
import ggc.mobile.util.DataAccessMobile;

import java.io.File;
import java.sql.Connection;
import java.sql.ResultSet;

import com.atech.mobile.db.transfer.BackupRestoreWorkGiver;
import com.atech.mobile.db.transfer.ExportTool;

public class ExportDailyValues extends ExportTool implements Runnable
{

    
    public ExportDailyValues(BackupRestoreWorkGiver giver)
    {
        super(DataAccessMobile.getInstance().getDb().getConnection());

        checkPrerequisitesForAutoBackup();

        this.setStatusReceiver(giver);
        this.setTypeOfStatus(ExportTool.STATUS_SPECIAL);

        // exportAll();
    }

    public ExportDailyValues(Connection conn)
    {
        super(conn);

        this.setTypeOfStatus(ExportTool.STATUS_DOT);

        checkPrerequisites();
        exportAll();
    }
    
    /*
    public ExportDailyValues(HibernateConfiguration cfg)
    {
//        this.setTypeOfStatus(ExportTool.STATUS_DOT);

        checkPrerequisites();
        exportAll();
    }
    */

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

    
    public int getActiveSession()
    {
        return 2;
    }
    
    
    
    private void export_DailyValues() 
    {
        try
        {
        openFile(this.getRootPath() + "DayValueH" + this.getFileLastPart() + ".dbe");
        // "../data/export/DayValueH_" + getCurrentDateForFile() + ".txt");
        writeHeader("ggc.core.db.hibernate.DayValueH",
            "id; dt_info; bg; ins1; ins2; ch; meals_ids; extended; person_id; comment; changed", DataAccessMobile
                    .getInstance().current_db_version);

        ResultSet rs = this.dbExecuteQuery(this.connection, 
            "select grp from ggc.core.db.hibernate.DayValueH as grp order by grp.id asc"); 
        
//        Query q = sess.createQuery("select grp from ggc.core.db.hibernate.DayValueH as grp order by grp.id asc");

// XX        this.statusSetMaxEntry(rs.q.list().size());


        int dot_mark = 5;
        int count = 0;

        while (rs.next())
        {
            DayValueDAO eh = new DayValueDAO();
            eh.dbGet(rs);

            this.writeToFile(eh.getId() + "|" + eh.getDt_info() + "|" + eh.getBg() + "|" + eh.getIns1() + "|"
                    + eh.getIns2() + "|" + eh.getCh() + "|" + eh.getMeals_ids() + "|" + eh.getExtended() + "|"
                    + eh.getPerson_id() + "|" + eh.getComment() + "|" + eh.getChanged() + "\n");

            // sleep(25);
            count++;
            this.writeStatus(dot_mark, count);
        }

        closeFile();
        }
        catch(Exception ex)
        {
            System.out.println("Exception: " + ex);
        }
    }

    public void run()
    {
        exportAll();
    }

    public static void main(String[] args)
    {
        GGCDbMobile db = new GGCDbMobile();
        db.initDb();
        new ExportDailyValues(db.getConnection());
    }

}