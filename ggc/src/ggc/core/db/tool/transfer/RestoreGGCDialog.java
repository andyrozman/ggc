package ggc.core.db.tool.transfer;

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.io.File;
import java.util.Enumeration;

import javax.swing.JDialog;
import javax.swing.JFrame;

import com.atech.db.hibernate.transfer.BackupRestoreObject;
import com.atech.db.hibernate.transfer.RestoreDialog;
import com.atech.utils.ATDataAccess;
import com.atech.utils.ATDataAccessAbstract;


/**
 *  AddressDialog 
 * 
 *  This is dialog for adding PersonAddress or InternalAddress, to either Person 
 *  or InternalPerson.
 * 
 *  This class is part of PIS (Parish Information System) package.
 * 
 *  @author      Andy (Aleksander) Rozman {andy@triera.net}
 *  @version     1.0
 */

public class RestoreGGCDialog extends RestoreDialog
{

    
    
    public RestoreGGCDialog(JDialog parent, ATDataAccessAbstract da)
    {
        super(parent, da);
    }

    
    public RestoreGGCDialog(JFrame parent, ATDataAccessAbstract da)
    {
        super(parent, da);
    }
    
    

    
    
    
    
    
    
    /**
     *   Displays title for dialog
     */
/*    public void showTitle(boolean backup)
    {
        if (backup)
        {
            this.setTitle(ic.getMessage("BACKUP_DB_TITLE"));
            label_title.setText(ic.getMessage("BACKUP_DB_TITLE_SHORT"));
        }
        else
        {
            this.setTitle(ic.getMessage("RESTORE_DB_TITLE"));
            label_title.setText(ic.getMessage("RESTORE_DB_TITLE_SHORT"));
        }
    }
*/





    
    

    public void actionPerformed(ActionEvent e)
    {
        String cmd = e.getActionCommand();
        
        if (cmd.equals("backup"))
        {
            cleanBackupDirectory();
            preprocesData();
            
            if (this.count_of_backup_elements==0)
                return;
            
            performBackup();
        }
        else if (cmd.equals("close"))
        {
            this.m_da.removeComponent(this);
            this.dispose();
        }
            
        
    }


    private void cleanBackupDirectory()
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
        
        File[] all_files = f.listFiles();
        
        for(int i=0; i<all_files.length; i++)
        {
            all_files[i].delete();
        }
    }
        
    
    public void packBackupFiles()
    {
        
    }
    
    
    
/*    
    cleanBackupDirectory();
    packBackupFiles();
 */   

    
    private void preprocesData()
    {
        traverseTree(this.backuprestore_root);
        
        int elements_count = 0;
        
        for(Enumeration<String> en = this.ht_backup_objects.keys(); en.hasMoreElements(); )
        {
            BackupRestoreObject bro = (BackupRestoreObject)this.ht_backup_objects.get(en.nextElement());
            
            if (bro.isSelected())
                elements_count++;
        }
        
//        System.out.println("Elements counts: " + elements_count);
        this.count_of_backup_elements = elements_count;
    }
    
    
    public boolean isBackupRestoreObjectSelected(String key)
    {
        if (this.ht_backup_objects.containsKey(key))
            return this.ht_backup_objects.get(key).isSelected();
        else
            return false;
    }
    
    
    
    
    
    public void performBackup()
    {
        
    }
    
    
    


    /**
     *  Returns object saved
     * 
     *  @return object of type of Object
     */
    public Object getObject()
    {
	return null;
    }


    String help_id = null;
    
    /* 
     * enableHelp
     */
    public void enableHelp(String help_id)
    {
        this.help_id = help_id;
        m_da.enableHelp(this);
    }


    /* 
     * getComponent
     */
    public Component getComponent()
    {
        return this;
    }




    public static void main(String args[])
    {
        JFrame fr = new JFrame();
        fr.setSize(800,600);
        
        new RestoreDialog(fr, ATDataAccess.getInstance());
    }




}


