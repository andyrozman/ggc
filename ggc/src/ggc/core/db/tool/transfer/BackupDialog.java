package ggc.core.db.tool.transfer; 

import javax.swing.JDialog;
import javax.swing.JFrame;

import com.atech.db.hibernate.transfer.BackupRestoreDialog;
import com.atech.utils.ATDataAccessAbstract;



public class BackupDialog extends BackupRestoreDialog 
{

    private static final long serialVersionUID = -9066907986768713200L;


    /**
     * Constructor
     * 
     * @param parent
     * @param da
     */
    public BackupDialog(JDialog parent, ATDataAccessAbstract da /*, BackupRestoreCollection br_coll*/)
    {
    	super(parent, da, da.getBackupRestoreCollection());
    	enableHelp("pages.GGC_Tools_Backup");
    	showDialog();
    }
    
    
    /**
     * Constructor
     * 
     * @param parent
     * @param da
     */
    public BackupDialog(JFrame parent, ATDataAccessAbstract da /*, BackupRestoreCollection br_coll*/)
    {
    	super(parent, da, da.getBackupRestoreCollection());
    	enableHelp("pages.GGC_Tools_Backup");
    	showDialog();
    }
    
    
    /** 
     * Perform Backup
     */
    @Override
    public void performBackup()
    {
    	GGCBackupRestoreRunner gbrr = new GGCBackupRestoreRunner(this.ht_backup_objects, this);
    	gbrr.start();
    }

    
}


