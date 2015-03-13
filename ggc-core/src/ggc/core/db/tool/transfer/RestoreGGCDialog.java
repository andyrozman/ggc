package ggc.core.db.tool.transfer;

import javax.swing.JCheckBox;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.SwingConstants;

import com.atech.db.hibernate.transfer.BackupRestoreCollection;
import com.atech.db.hibernate.transfer.RestoreDialog;
import com.atech.utils.ATDataAccessAbstract;
import com.atech.utils.ATSwingUtils;

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
 *  Filename:     RestoreGGCDialog  
 *  Description:  GGC Dialog for Restore
 * 
 *  Author: andyrozman {andy@atech-software.com}  
 */

public class RestoreGGCDialog extends RestoreDialog
{

    private static final long serialVersionUID = 2514267810070964604L;
    JCheckBox cb_daily;

    /**
     * Constructor
     * 
     * @param parent
     * @param da
     * @param br_coll
     * @param filename
     */
    public RestoreGGCDialog(JDialog parent, ATDataAccessAbstract da, BackupRestoreCollection br_coll, String filename)
    {
        super(parent, da, br_coll, filename);
    }

    /**
     * Constructor
     * 
     * @param parent
     * @param da
     * @param br_coll
     * @param filename
     */
    public RestoreGGCDialog(JFrame parent, ATDataAccessAbstract da, BackupRestoreCollection br_coll, String filename)
    {
        super(parent, da, br_coll, filename);
    }

    /**
     * Init Special
     * 
     * @see com.atech.db.hibernate.transfer.RestoreDialog#initSpecial()
     */
    @Override
    public void initSpecial()
    {
        ATSwingUtils.initLibrary();

        this.cb_daily = new JCheckBox(ic.getMessage("DAILY_VALUES_APPEND"));
        this.cb_daily.setBounds(25, 390, 380, 70);
        this.cb_daily.setVerticalTextPosition(SwingConstants.TOP);
        this.cb_daily.setFont(ATSwingUtils.getFont(ATSwingUtils.FONT_NORMAL));
        this.cb_daily.setEnabled(this.restore_files.containsKey("ggc.core.db.hibernate.DayValueH"));
        panel.add(this.cb_daily);

        this.setBounds(130, 50, 450, 520);

    }

    /**
     * Perform Restore
     * 
     * @see com.atech.db.hibernate.transfer.RestoreDialog#performRestore()
     */
    @Override
    public void performRestore()
    {
        GGCBackupRestoreRunner gbrr = new GGCBackupRestoreRunner(this.restore_files, this, ""
                + this.cb_daily.isSelected());
        gbrr.start();
    }

    /*
     * public static void main(String args[])
     * {
     * JFrame fr = new JFrame();
     * fr.setSize(800,600);
     * DataAccess da = DataAccess.getInstance();
     * da.addComponent(fr);
     * GGCDb db = new GGCDb();
     * db.initDb();
     * da.setDb(db);
     * RestoreGGCDialog rgd = new RestoreGGCDialog(fr, da,
     * da.getBackupRestoreCollection(),
     * "d:/GGC backup 2008_07_23 14_02_25.zip");
     * rgd.showDialog();
     * }
     */

}
