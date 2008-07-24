package ggc.core.db.tool.transfer; 

import ggc.core.util.DataAccess;

import javax.swing.JDialog;
import javax.swing.JFrame;

import com.atech.db.hibernate.transfer.BackupRestoreDialog;
import com.atech.utils.ATDataAccessAbstract;



public class BackupDialog extends BackupRestoreDialog 
{

    public BackupDialog(JDialog parent, ATDataAccessAbstract da /*, BackupRestoreCollection br_coll*/)
    {
    	super(parent, da, da.getBackupRestoreCollection());
    	enableHelp("pages.GGC_Tools_Backup");
    	showDialog();
    }
    
    
    public BackupDialog(JFrame parent, ATDataAccessAbstract da /*, BackupRestoreCollection br_coll*/)
    {
    	super(parent, da, da.getBackupRestoreCollection());
    	enableHelp("pages.GGC_Tools_Backup");
    	showDialog();
    }
    
    
    /* 
     * performBackup
     */
    @Override
    public void performBackup()
    {
    	GGCBackupRestoreRunner gbrr = new GGCBackupRestoreRunner(this.ht_backup_objects, this);
    	gbrr.start();
    }

    
    public static void main(String args[])
    {
        JFrame fr = new JFrame();
        fr.setSize(800,600);
        
        BackupDialog rsd = new BackupDialog(new JDialog(), DataAccess.getInstance());
        rsd.showDialog();
    }
    
    
}


