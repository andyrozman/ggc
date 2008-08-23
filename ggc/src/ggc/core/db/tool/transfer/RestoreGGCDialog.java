package ggc.core.db.tool.transfer;

import ggc.core.db.GGCDb;
import ggc.core.util.DataAccess;

import javax.swing.JCheckBox;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.SwingConstants;

import com.atech.db.hibernate.transfer.BackupRestoreCollection;
import com.atech.db.hibernate.transfer.RestoreDialog;
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

    private static final long serialVersionUID = 2514267810070964604L;
    JCheckBox cb_daily;
    
    
    public RestoreGGCDialog(JDialog parent, ATDataAccessAbstract da, BackupRestoreCollection br_coll, String filename)
    {
        super(parent, da, br_coll, filename);
    }

    
    public RestoreGGCDialog(JFrame parent, ATDataAccessAbstract da, BackupRestoreCollection br_coll, String filename)
    {
        super(parent, da, br_coll, filename);
    }
    
    
    
    public void initSpecial()
    {
        this.cb_daily = new JCheckBox(ic.getMessage("DAILY_VALUES_APPEND"));
        this.cb_daily.setBounds(25, 390, 380, 70);
        this.cb_daily.setVerticalTextPosition(SwingConstants.TOP);
        this.cb_daily.setFont(m_da.getFont(DataAccess.FONT_NORMAL));
        this.cb_daily.setEnabled(this.restore_files.containsKey("ggc.core.db.hibernate.DayValueH"));
        panel.add(this.cb_daily);
        
        this.setBounds(130, 50, 450, 520); 

    }
    
    

    public void performRestore()
    {
        GGCBackupRestoreRunner gbrr = new GGCBackupRestoreRunner(this.restore_files, this, "" + this.cb_daily.isSelected());
        gbrr.start();
    }

    
    
    
    
    
        
    
    
    


    public static void main(String args[])
    {
        JFrame fr = new JFrame();
        fr.setSize(800,600);
        
        DataAccess da = DataAccess.getInstance();
        da.addComponent(fr);
        
        GGCDb db = new GGCDb();
        db.initDb();
        
        da.setDb(db);
        
        
        RestoreGGCDialog rgd = new RestoreGGCDialog(fr, da, da.getBackupRestoreCollection(), "d:/GGC backup 2008_07_23 14_02_25.zip");
        rgd.showDialog();
        
    }




}


