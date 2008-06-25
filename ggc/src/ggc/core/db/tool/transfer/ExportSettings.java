package ggc.core.db.tool.transfer;

import ggc.core.db.GGCDb;
import ggc.core.db.hibernate.SettingsH;
import ggc.core.util.DataAccess;

import java.io.File;
import java.util.Iterator;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.cfg.Configuration;

import com.atech.db.hibernate.transfer.BackupRestoreWorkGiver;
import com.atech.db.hibernate.transfer.ExportTool;


public class ExportSettings extends ExportTool implements Runnable
{
    

    
    
    public ExportSettings(BackupRestoreWorkGiver giver)
    {
	super(DataAccess.getInstance().getDb().getConfiguration());
	
	checkPrerequisitesForAutoBackup();
	
	this.setStatusReceiver(giver);
	this.setTypeOfStatus(ExportTool.STATUS_SPECIAL);
	
	//exportAll();
    }
    
    
    public ExportSettings(Configuration cfg)
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
	export_Settings();
    }

    /*
    private void sleep(long ms)
    {
	try
	{
	    Thread.sleep(ms);
	}
	catch(Exception ex)
	{
	    
	}
    }
    */
    
    

    
    
    @SuppressWarnings("unchecked")
    private void export_Settings()
    {
	openFile(this.getRootPath() + "SettingsH" + this.getFileLastPart() + ".txt");
//		"../data/export/DayValueH_" + getCurrentDateForFile() + ".txt");
	writeHeader("ggc.core.db.hibernate.SettingsH", 
		    "id; key; value; type; description; person_id", 
		    DataAccess.getInstance().currentDbVersion);
	
        Session sess = getSession();

        Query q = sess.createQuery("select grp from ggc.core.db.hibernate.SettingsH as grp order by grp.id asc");

        this.statusSetMaxEntry(q.list().size());
        
        Iterator it = q.iterate();
        
        int dot_mark = 5;
        int count = 0;

        while (it.hasNext())
        {
            SettingsH eh = (SettingsH)it.next();

            this.writeToFile(eh.getId() + "|" + 
        	             eh.getKey() + "|" +
        	             eh.getValue() + "|" +
        	             eh.getType() + "|" +
        	             eh.getDescription() + "|" +
        	             eh.getPerson_id() 
        	             + "\n");

            //sleep(25);
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
	new ExportSettings(db.getConfiguration());
    }
    
    
}