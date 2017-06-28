package ggc.pump.gui.profile;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.*;

import com.atech.graphics.components.TimeComponent;
import com.atech.help.HelpCapable;
import com.atech.i18n.I18nControlAbstract;
import com.atech.utils.ATSwingUtils;

import ggc.pump.data.profile.ProfileSubEntry;
import ggc.pump.data.profile.ProfileSubPattern;
import ggc.pump.util.DataAccessPump;

/**
 *  Application:   GGC - GNU Gluco Control
 *  Plug-in:       Pump Tool (support for Pump devices)
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
 *  Filename:     ProfileEntryDialog
 *  Description:  Adding/editing Basal entry in profile
 * 
 *  Author: andyrozman {andy@atech-software.com}  
 */

public class ProfileEntryDialog extends JDialog implements ActionListener, HelpCapable
{

    private static final long serialVersionUID = -6972977617367401931L;
    private DataAccessPump m_da = DataAccessPump.getInstance();
    private I18nControlAbstract m_ic = m_da.getI18nControlInstance();

    private boolean m_action_done = false;
    JButton help_button;
    JLabel label_title = new JLabel();
    TimeComponent tc_from, tc_till;
    // private Container m_parent = null;
    ProfileSubEntry m_pse;
    JSpinner sp_amount;


    /**
     * Constructor 
     * 
     * @param dialog
     */
    public ProfileEntryDialog(JDialog dialog)
    {
        super(dialog, "", true);
        // m_parent = dialog;
        ATSwingUtils.initLibrary();
        // initParameters(ndV,nDate);

        m_pse = new ProfileSubPattern();
        init();
    }


    /**
     * Constructor
     * 
     * @param pse
     * @param dialog
     */
    public ProfileEntryDialog(ProfileSubEntry pse, JDialog dialog)
    {
        super(dialog, "", true);
        // m_parent = dialog;
        ATSwingUtils.initLibrary();
        // initParameters(ndV,nDate);
        m_pse = pse;
        init();
        load();
    }


    private void load()
    {
        this.tc_from.setTime(m_pse.timeStart);
        this.tc_till.setTime(m_pse.timeEnd);
        this.sp_amount.setValue(m_pse.amount);
    }


    private void save()
    {
        m_pse.timeStart = this.tc_from.getTime();
        m_pse.timeEnd = this.tc_till.getTime();
        m_pse.amount = ((Float) this.sp_amount.getValue()).floatValue();
    }


    private void init()
    {
        ATSwingUtils.initLibrary();

        // FIXME
        // dataAccess.enableHelp(this);
        m_da.addComponent(this);

        // FIXME
        int x = 100;
        int y = 100;
        int width = 390;
        int height = 280;

        // FIXME
        /*
         * Rectangle bnd = m_parent.getBounds();
         * x = (bnd.width/2) + bnd.x - (width/2);
         * y = (bnd.height/2) + bnd.y - (height/2);
         */
        this.setBounds(x, y, width, height);

        JPanel panel = new JPanel();
        panel.setBounds(0, 0, width, height);
        panel.setLayout(null);

        this.getContentPane().add(panel);

        label_title.setFont(ATSwingUtils.getFont(ATSwingUtils.FONT_BIG_BOLD));
        label_title.setHorizontalAlignment(SwingConstants.CENTER);
        label_title.setBounds(0, 15, width, 35);
        label_title.setText(m_ic.getMessage("PROFILE_BASAL_ENTRY"));
        panel.add(label_title);

        this.setTitle(m_ic.getMessage("PROFILE_BASAL_ENTRY"));

        ATSwingUtils.getLabel(m_ic.getMessage("TIME_FROM") + ":", 30, 78, 100, 25, panel);

        ATSwingUtils.getLabel(m_ic.getMessage("TIME_TILL") + ":", 30, 110, 100, 25, panel);

        ATSwingUtils.getLabel(m_ic.getMessage("BASAL_AMOUNT") + ":", 30, 145, 100, 25, panel);

        tc_from = new TimeComponent();
        tc_from.setBounds(160, 75, 100, 35);
        panel.add(tc_from);

        tc_till = new TimeComponent();
        tc_till.setBounds(160, 110, 100, 35);
        panel.add(tc_till);

        SpinnerNumberModel model = new SpinnerNumberModel(new Float(0.0f), new Float(0.0f),
                new Float(m_da.getMaxBasalValue()), new Float(m_da.getBasalStep()));

        sp_amount = new JSpinner(model);
        sp_amount.setBounds(160, 145, 55, 25);
        panel.add(sp_amount);

        ATSwingUtils.getButton("  " + m_ic.getMessage("OK"), 30, 200, 100, 25, panel, ATSwingUtils.FONT_NORMAL,
            "ok.png", "ok", this, m_da);

        ATSwingUtils.getButton("  " + m_ic.getMessage("CANCEL"), 140, 200, 100, 25, panel, ATSwingUtils.FONT_NORMAL,
            "cancel.png", "cancel", this, m_da);

        this.help_button = ATSwingUtils.createHelpButtonByBounds(250, 200, 100, 25, this, ATSwingUtils.FONT_NORMAL,
            m_da);
        panel.add(this.help_button);

        m_da.enableHelp(this);

        /*
         * String button_command[] = { "update_ch",
         * i18nControl.getMessage("UPDATE_FROM_FOOD"),
         * "edit", i18nControl.getMessage("EDIT"),
         * "ok", i18nControl.getMessage("OK"),
         * "cancel", i18nControl.getMessage("CANCEL"),
         * "help", i18nControl.getMessage("HELP")
         * };
         * int button_coord[] = { 210, 228, 120, 0,
         * 230, 258, 100, 0,
         * 50, 390, 80, 1,
         * 140, 390, 80, 1,
         * 250, 390, 80, 0
         * };
         * JButton button;
         * //int j=0;
         * for (int i=0, j=0; i<button_coord.length; i+=4, j+=2)
         * {
         * button = new JButton(button_command[j+1]);
         * button.setActionCommand(button_command[j]);
         * button.addActionListener(this);
         * if (button_coord[i+3]==0)
         * {
         * button.setEnabled(false);
         * }
         * addComponent(button, button_coord[i], button_coord[i+1],
         * button_coord[i+2], panel);
         * }
         */
    }


    /**
     * Invoked when an action occurs.
     */
    public void actionPerformed(ActionEvent e)
    {
        String action = e.getActionCommand();

        if (action.equals("cancel"))
        {
            m_action_done = false;
            m_da.removeComponent(this);
            this.dispose();
        }
        else if (action.equals("ok"))
        {
            save();
            m_action_done = true;
            m_da.removeComponent(this);
            this.dispose();
        }

    }


    /**
     * Was Action Successful
     * 
     * @return true if action was succesful (dialog closed with OK)
     */
    public boolean actionSuccessful()
    {
        return m_action_done;
    }


    /**
     * Get Result - returns result of this dialog
     * 
     * @return
     */
    public ProfileSubEntry getResult()
    {
        return this.m_pse;
    }


    // ****************************************************************
    // ****** HelpCapable Implementation *****
    // ****************************************************************

    /**
     * getComponent - get component to which to attach help context
     * 
     * @return 
     */
    public Component getComponent()
    {
        return this.getRootPane();
    }


    /**
     * getHelpButton - get Help button
     * 
     * @return 
     */
    public JButton getHelpButton()
    {
        return this.help_button;
    }


    /**
     * getHelpId - get id for Help
     * 
     * @return 
     */
    public String getHelpId()
    {
        return "PumpTool_Profile_Entry_Editor";
    }


    /**
     * For Test
     * @param args
     */
    public static void main(String[] args)
    {
        JDialog d = new JDialog();
        ProfileEntryDialog ped = new ProfileEntryDialog(d);
        ped.setVisible(true);

    }

}
