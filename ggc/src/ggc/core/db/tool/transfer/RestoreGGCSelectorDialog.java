package ggc.core.db.tool.transfer;

import ggc.core.util.DataAccess;

import javax.swing.JDialog;
import javax.swing.JFrame;

import com.atech.db.hibernate.transfer.RestoreSelectorDialog;
import com.atech.utils.ATDataAccessAbstract;

/*
 * 
 * @author Andy (Aleksander) Rozman {andy@triera.net}
 * 
 * @version 1.0
 */

public class RestoreGGCSelectorDialog extends RestoreSelectorDialog 
{
    

    public RestoreGGCSelectorDialog(JDialog parent, ATDataAccessAbstract da)
    {
        super(parent, da);
    }

    public RestoreGGCSelectorDialog(JFrame parent, ATDataAccessAbstract da)
    {
        super(parent, da);
    }



    
    public void cmdNextStep()
    {
        RestoreGGCDialog rgd = new RestoreGGCDialog((JFrame)this.my_parent, this.m_da, this.m_da.getBackupRestoreCollection(), this.tf_file.getText());
        rgd.showDialog();
    }
    
    

    
    public static void main(String args[])
    {
        JFrame fr = new JFrame();
        fr.setSize(800,600);
        
        RestoreGGCSelectorDialog rsd = new RestoreGGCSelectorDialog(new JDialog(), DataAccess.getInstance());
        rsd.showDialog();
    }
    
    
    
}
