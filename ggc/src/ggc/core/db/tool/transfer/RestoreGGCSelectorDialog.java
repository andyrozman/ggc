package ggc.core.db.tool.transfer;

import ggc.core.util.DataAccess;

import javax.swing.JDialog;
import javax.swing.JFrame;

import com.atech.db.hibernate.transfer.RestoreSelectorDialog;
import com.atech.utils.ATDataAccessAbstract;

/*
 * AddressDialog
 * 
 * This is dialog for adding PersonAddress or InternalAddress, to either Person
 * or InternalPerson.
 * 
 * This class is part of PIS (Parish Information System) package.
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


    /*
     * Displays title for dialog
     */
/*    public void showTitle(boolean backup)
    {
        {
            this.setTitle(ic.getMessage("RESTORE_DB_TITLE"));
            label_title.setText(ic.getMessage("RESTORE_DB_TITLE_SHORT"));
        }
    }
*/

    
    public void cmdNextStep()
    {
        
        
    }
    
    

    
    public static void main(String args[])
    {
        JFrame fr = new JFrame();
        fr.setSize(800,600);
        
        RestoreGGCSelectorDialog rsd = new RestoreGGCSelectorDialog(new JDialog(), DataAccess.getInstance());
        rsd.showDialog();
    }
    
    
    
}
