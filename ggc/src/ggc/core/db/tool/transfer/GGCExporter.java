package ggc.core.db.tool.transfer;

import ggc.core.db.GGCDb;
import ggc.core.db.datalayer.DailyValue;
import ggc.core.db.datalayer.SettingsColorScheme;
import ggc.core.db.hibernate.ColorSchemeH;
import ggc.core.db.hibernate.DayValueH;
import ggc.core.util.DataAccess;

import java.io.File;
import java.util.Iterator;

import org.hibernate.Query;
import org.hibernate.Session;

import com.atech.db.hibernate.HibernateConfiguration;
import com.atech.db.hibernate.transfer.BackupRestoreObject;
import com.atech.db.hibernate.transfer.BackupRestoreWorkGiver;
import com.atech.db.hibernate.transfer.ExportTool;

public class GGCExporter extends ExportTool implements Runnable
{

    public GGCExporter(BackupRestoreWorkGiver giver)
    {
        super(DataAccess.getInstance().getDb().getHibernateConfiguration());

        checkPrerequisitesForAutoBackup();

        this.setStatusReceiver(giver);
        this.setTypeOfStatus(ExportTool.STATUS_SPECIAL);

        // exportAll();
    }

    public GGCExporter(HibernateConfiguration cfg)
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
        System.out.println("export: all");

        if (this.backup_object!=null)
        {
            exportData(this.getBackupRestoreObject(this.backup_object));
            this.backup_object = null;
        }
        //export(_DailyValues();
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
    

    private BackupRestoreObject getBackupRestoreObject(String class_name)
    {
        if (class_name.equals("ggc.core.db.hibernate.DayValueH"))
        {
            //return new DailyValue();
            return null;
        }
        else if (class_name.equals("ggc.core.db.hibernate.ColorSchemeH"))
        {
            return new SettingsColorScheme();
        }
        else
            return null;
           
            
    }
    
    
    private BackupRestoreObject getBackupRestoreObject(Object obj, BackupRestoreObject bro)
    {
        if (bro.getBackupClassName().equals("ggc.core.db.hibernate.DayValueH"))
        {
            DayValueH eh = (DayValueH)obj;
            return new DailyValue(eh);
        }
        else if (bro.getBackupClassName().equals("ggc.core.db.hibernate.ColorSchemeH"))
        {
            ColorSchemeH eh = (ColorSchemeH)obj;
            return new SettingsColorScheme(eh);
        }
        else
            return null;
           
            
    }
    
    
    
    public void exportData(String name)
    {
        exportData(this.getBackupRestoreObject(name));
    }
    
    
    public void exportData(BackupRestoreObject bro)
    {
        System.out.println("export: first");
    
        openFile(this.getRootPath() + bro.getBackupFile() + this.getFileLastPart() + ".dbe");

        writeHeader(bro, DataAccess.getInstance().current_db_version);

        Session sess = getSession();

        Query q = sess.createQuery("select grp from " + bro.getClassName() + " as grp order by grp.id asc");

        this.statusSetMaxEntry(q.list().size());

        Iterator<?> it = q.iterate();

        int dot_mark = 5;
        int count = 0;

        while (it.hasNext())
        {
            System.out.println("export: next");
            BackupRestoreObject bt = getBackupRestoreObject(it.next(), bro);
            
            //DayValueH eh = (DayValueH) it.next();

            this.writeToFile(bt);
            
            /*
            this.writeToFile(eh.getId() + "|" + eh.getDt_info() + "|" + eh.getBg() + "|" + eh.getIns1() + "|"
                    + eh.getIns2() + "|" + eh.getCh() + "|" + eh.getMeals_ids() + "|" + eh.getExtended() + "|"
                    + eh.getPerson_id() + "|" + eh.getComment() + "|" + eh.getChanged() + "\n");
            */
            // sleep(25);
            count++;
            this.writeStatus(dot_mark, count);
        }

        closeFile();
    }

    boolean running = true;
    String backup_object = null;
    
    public void setBackupObject(String name)
    {
        this.backup_object = name;
    }
    
    
    public void run()
    {
        while (running)
        {
            exportAll();
            try
            {
                Thread.sleep(2000);
            }
            catch(Exception ex) {}
        }
    }

    public void stopThread()
    {
        this.running = false;
    }
    
    public static void main(String[] args)
    {
        GGCDb db = new GGCDb();
        db.initDb();
        new ExportDailyValues(db.getHibernateConfiguration());
    }

}