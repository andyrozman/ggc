package ggc.core.db.tool.transfer;

import javax.swing.*;

import com.atech.db.hibernate.transfer.RestoreSelectorDialog;
import com.atech.utils.ATDataAccessAbstract;

/**
 *  Application:   GGC - GNU Gluco Control
 *
 *  See AUTHORS for copyright information.
 * 
 *  This program is free software; you can redistribute it and/or modify it under
 *  the terms of the GNU General Public License as published by the Free Software
 *  Foundation; either version 2 of the License, or (at your option) any later
 *  version.
 * 
 *  This program is distributed in the hope that it will be useful, but WITHOUT
 *  ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 *  FOR A PARTICULAR PURPOSE. See the GNU General Public License for more
 *  details.
 * 
 *  You should have received a copy of the GNU General Public License along with
 *  this program; if not, write to the Free Software Foundation, Inc., 59 Temple
 *  Place, Suite 330, Boston, MA 02111-1307 USA
 * 
 *  Filename:     RestoreGGCSelectorDialog
 *  Description:  Restore GGC SelectorDialog (forst dialog for restore, where you select
 *                files).
 * 
 *  Author: andyrozman {andy@atech-software.com}  
 */

public class RestoreGGCSelectorDialog extends RestoreSelectorDialog
{

    private static final long serialVersionUID = 3536165659702725457L;


    /**
     * Constructor
     * 
     * @param parent
     * @param da
     */
    public RestoreGGCSelectorDialog(JDialog parent, ATDataAccessAbstract da)
    {
        super(parent, da);
        this.enableHelp("GGC_Tools_Restore_File_Selector");
    }


    /**
     * Constructor
     * 
     * @param parent
     * @param da
     */
    public RestoreGGCSelectorDialog(JFrame parent, ATDataAccessAbstract da)
    {
        super(parent, da);
        this.enableHelp("GGC_Tools_Restore_File_Selector");
    }


    /**
     * Get Browse Startup Directory
     * 
     * @see com.atech.db.hibernate.transfer.RestoreSelectorDialog#getBrowseStartupDirectory()
     */
    @Override
    public String getBrowseStartupDirectory()
    {
        return "../data/export/";
    }


    /**
     * Command Next Step
     * 
     * @see com.atech.db.hibernate.transfer.RestoreSelectorDialog#cmdNextStep()
     */
    @Override
    public void cmdNextStep()
    {
        // System.out.println("Res Coll: " +
        // this.dataAccess.getBackupRestoreCollection());
        RestoreGGCDialog rgd = new RestoreGGCDialog((JFrame) this.my_parent, this.m_da,
                this.m_da.getBackupRestoreCollection(), this.tf_file.getText());
        rgd.enableHelp("GGC_Tools_Restore");
        rgd.showDialog();
    }

    /*
     * public static void main(String args[])
     * {
     * JFrame fr = new JFrame();
     * fr.setSize(800,600);
     * RestoreGGCSelectorDialog rsd = new RestoreGGCSelectorDialog(new
     * JDialog(), DataAccess.getInstance());
     * rsd.showDialog();
     * }
     */

}
