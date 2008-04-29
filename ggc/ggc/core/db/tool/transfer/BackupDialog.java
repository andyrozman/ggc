package ggc.core.db.tool.transfer; 

import javax.swing.JDialog;
import javax.swing.JFrame;

import com.atech.db.hibernate.transfer.BackupRestoreCollection;
import com.atech.db.hibernate.transfer.BackupRestoreDialog;
import com.atech.utils.ATDataAccessAbstract;



public class BackupDialog extends BackupRestoreDialog 
{

    private static final long serialVersionUID = 1L;
    
    int m_error = 0;

    int lastAction = 0;  // no event
    
    
    public BackupDialog(JDialog parent, ATDataAccessAbstract da, BackupRestoreCollection br_coll)
    {
    	super(parent, da, br_coll);
    	//this.ic = this.m_da.getI18nControlInstance();
    }

    
    public BackupDialog(JFrame parent, ATDataAccessAbstract da, BackupRestoreCollection br_coll)
    {
    	super(parent, da, br_coll);
    	//this.ic = this.m_da.getI18nControlInstance();
    }
    
    






    
    /* 
     * performBackup
     */
    @Override
    public void performBackup()
    {
	GGCBackupRestoreRunner gbrr = new GGCBackupRestoreRunner(this.ht_backup_objects, this);
	gbrr.start();
	
	/*
	if (this.isBackupRestoreObjectSelected(ic.getMessage("DAILY_VALUES")))
	{
	    ExportDailyValues edv = new ExportDailyValues(this);
	    //System.out.println("Daily Values YES");
	    //edv.setPriority(Thread.MIN_PRIORITY);
	    //edv.start();
	    edv.run();
	    this.setStatus(100);
	    this.done_backup_elements++;
	}
	
	if (this.isBackupRestoreObjectSelected(ic.getMessage("USER_FOOD_GROUPS")))
	{
	    System.out.println("Food Groups YES");
	}

	if (this.isBackupRestoreObjectSelected(ic.getMessage("FOODS")))
	{
	    System.out.println("Foods YES");
	}
	
	
	if (this.isBackupRestoreObjectSelected(ic.getMessage("MEAL_GROUPS")))
	{
	    System.out.println("Meal Groups YES");
	}
	
	
	if (this.isBackupRestoreObjectSelected(ic.getMessage("MEALS")))
	{
	    System.out.println("Meals YES");
	}*/
	
    }


}


