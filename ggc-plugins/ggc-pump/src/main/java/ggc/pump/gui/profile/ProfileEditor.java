package ggc.pump.gui.profile;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Hashtable;
import java.util.StringTokenizer;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import com.atech.graphics.components.DateTimeComponent;
import com.atech.help.HelpCapable;
import com.atech.i18n.I18nControlAbstract;
import com.atech.utils.ATSwingUtils;

import ggc.core.db.hibernate.pump.PumpProfileH;
import ggc.pump.data.graph.GraphViewBasalRateEstimator;
import ggc.pump.data.graph.GraphViewProfileEditor;
import ggc.pump.data.graph.bre.BREGraphsAbstract;
import ggc.pump.data.profile.ProfileSubEntry;
import ggc.pump.data.profile.ProfileSubPattern;
import ggc.pump.db.PumpProfile;
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
 *  Filename:     ProfileEditor
 *  Description:  Dialog for add/edit of profile. 
 * 
 *  Author: andyrozman {andy@atech-software.com}  
 */

public class ProfileEditor extends JDialog implements ActionListener, ChangeListener, HelpCapable
{

    // graph
    // check if all entries in range
    // help

    private static final long serialVersionUID = 3285623806907044947L;
    DataAccessPump m_da = DataAccessPump.getInstance();
    I18nControlAbstract m_ic = null;
    JList lst_basals; // lst_bgs, lst_basals_old, lst_basals_new, lst_ratios;

    GraphViewBasalRateEstimator gv;
    Hashtable<String, BREGraphsAbstract> m_graphs;

    ArrayList<ProfileSubEntry> list_data;
    JCheckBox cb_enabled_till;
    DateTimeComponent dtc_from, dtc_till;
    JTextField tf_name, tf_com;
    JButton help_button;
    JDialog parent = null;
    PumpProfileH m_profile;
    boolean m_action_done = false;
    JSpinner sp_base;
    GraphViewProfileEditor graphview_pe;


    /**
     * Constructor
     * 
     * @param parent 
     */
    public ProfileEditor(JDialog parent)
    {
        super(parent, "", true);
        ATSwingUtils.initLibrary();
        m_ic = m_da.getI18nControlInstance();

        list_data = new ArrayList<ProfileSubEntry>();
        m_profile = new PumpProfileH();
        init();

        this.setSize(780, 565);
    }


    /**
     * Constructor
     * 
     * @param parent 
     * @param prof 
     */
    public ProfileEditor(JDialog parent, PumpProfileH prof)
    {
        super(parent, "", true);
        ATSwingUtils.initLibrary();
        m_ic = m_da.getI18nControlInstance();
        m_profile = prof;

        list_data = new ArrayList<ProfileSubEntry>();
        init();
        load();

        this.setSize(780, 565);
    }


    private void load()
    {
        this.tf_name.setText(this.m_profile.getName());
        this.dtc_from.setDateTime(getDateCorrected(this.m_profile.getActiveFrom()));

        if (this.m_profile.getActiveTill() <= 0)
        {
            this.cb_enabled_till.setSelected(false);
            this.dtc_till.setDateTimeAsCurrent();
        }
        else
        {
            this.cb_enabled_till.setSelected(true);
            this.dtc_till.setDateTime(getDateCorrected(this.m_profile.getActiveTill()));
        }

        this.sp_base.setValue(this.m_profile.getBasalBase());
        this.loadSubEntries(this.m_profile.getBasalDiffs());

        if (this.m_profile.getComment() != null && !this.m_profile.getComment().equals("null"))
        {
            this.tf_com.setText(this.m_profile.getComment());
        }

        graphview_pe.refreshData();

    }


    private long getDateCorrected(long dt)
    {
        if (dt <= 0)
            return dt;
        else
        {
            String s = "" + dt;

            if (s.length() == 12)
            {
                dt *= 100;
            }

            return dt;
        }

    }


    private void loadSubEntries(String subs)
    {
        // 0-400=0.2;400-600=1.7
        StringTokenizer strtok = new StringTokenizer(subs, ";");

        while (strtok.hasMoreTokens())
        {
            list_data.add(new ProfileSubPattern(strtok.nextToken()));
        }

        Collections.sort(list_data);

        this.refreshList(1, list_data);
    }


    private void save()
    {
        this.m_profile.setName(this.tf_name.getText());
        this.m_profile.setActiveFrom(this.dtc_from.getDateTime());

        if (this.cb_enabled_till.isSelected())
        {
            this.m_profile.setActiveTill(this.dtc_till.getDateTime());
        }
        else
        {
            this.m_profile.setActiveTill(-1L);
        }

        this.m_profile.setBasalBase(m_da.getFloatValue(this.sp_base.getValue()));
        this.m_profile.setBasalDiffs(getSubEntries());
        this.m_profile.setComment(this.tf_com.getText());

        PumpProfile pp = new PumpProfile(this.m_profile);

        if (pp.getId() > 0)
        {
            m_da.getDb().edit(pp);
        }
        else
        {
            m_da.getDb().add(pp);
        }

        this.m_profile.setId(pp.getId());
    }


    private String getSubEntries()
    {
        StringBuffer sb = new StringBuffer();

        for (int i = 0; i < this.list_data.size(); i++)
        {
            if (i != 0)
            {
                sb.append(";");
            }

            sb.append(this.list_data.get(i).getPacked());
        }

        return sb.toString();
    }


    /**
     * Get Profile Entries (as ArrayList)
     * @return
     */
    public ArrayList<ProfileSubEntry> getProfileEntriesAL()
    {
        return this.list_data;
    }


    private void init()
    {
        // this.setLayout(null);
        this.setTitle(m_ic.getMessage("PROFILE_EDITOR"));
        // JLabel label;

        JPanel panel = new JPanel();
        panel.setLayout(null);
        this.add(panel);

        ATSwingUtils.getTitleLabel(m_ic.getMessage("PROFILE_EDITOR"), 0, 20, 780, 35, panel,
            ATSwingUtils.FONT_BIG_BOLD);

        ATSwingUtils.getLabel(m_ic.getMessage("NAME") + ":", 80, 75, 120, 25, panel, ATSwingUtils.FONT_NORMAL_BOLD);

        tf_name = ATSwingUtils.getTextField("", 190, 75, 120, 25, panel);
        tf_name.setEditable(false);

        ATSwingUtils.getButton("   " + m_ic.getMessage("SELECT"), 330, 75, 120, 25, panel, ATSwingUtils.FONT_NORMAL,
            "cancel.png", "select_profile", this, m_da);

        ATSwingUtils.getLabel(m_ic.getMessage("DATE_FROM") + ":", 80, 110, 250, 25, panel,
            ATSwingUtils.FONT_NORMAL_BOLD);

        dtc_from = new DateTimeComponent(m_da, DateTimeComponent.ALIGN_HORIZONTAL, 30,
                DateTimeComponent.TIME_MAXIMAL_SECOND);
        dtc_from.setBounds(190, 110, 120, 25);
        dtc_from.setDateTimeType(DateTimeComponent.TIME_MAXIMAL_SECOND);
        dtc_from.setDateTimeAsCurrent();
        // dtc_from.setD
        panel.add(dtc_from);

        cb_enabled_till = ATSwingUtils.getCheckBox(" " + m_ic.getMessage("DATE_TILL") + ":", 80, 150, 80, 20, panel,
            ATSwingUtils.FONT_NORMAL_BOLD);
        cb_enabled_till.addChangeListener(this);

        dtc_till = new DateTimeComponent(m_da, DateTimeComponent.ALIGN_HORIZONTAL, 30,
                DateTimeComponent.TIME_MAXIMAL_SECOND);
        dtc_till.setBounds(190, 145, 120, 25);
        dtc_till.setDateTimeType(DateTimeComponent.TIME_MAXIMAL_SECOND);
        dtc_till.setDateTimeAsCurrent();
        dtc_till.setEnabled(false);
        panel.add(dtc_till);

        ATSwingUtils.getLabel(m_ic.getMessage("COMMENT") + ":", 80, 180, 120, 25, panel, ATSwingUtils.FONT_NORMAL_BOLD);

        tf_com = ATSwingUtils.getTextField("", 190, 180, 240, 25, panel);
        tf_com.setEditable(true);

        ATSwingUtils.getLabel(m_ic.getMessage("BASE_BASAL") + ":", 560, 210, 200, 25, panel,
            ATSwingUtils.FONT_NORMAL_BOLD);

        sp_base = new JSpinner(new SpinnerNumberModel(0, 0, 25.0f, 0.1f));
        sp_base.setBounds(680, 210, 60, 25);
        panel.add(sp_base);

        this.lst_basals = new JList();
        JScrollPane scr = new JScrollPane(this.lst_basals);
        scr.setBounds(560, 250, 180, 210);
        panel.add(scr);

        /*
         * JPanel panel_graph = new JPanel();
         * panel_graph.setBounds(30, 220, 500, 280);
         * panel_graph.setBackground(new Color(65, 105, 225));
         * panel.add(panel_graph);
         */

        graphview_pe = new GraphViewProfileEditor(this);

        JPanel panel_graph = graphview_pe.getChartPanel();
        panel_graph.setBounds(20, 220, 510, 310);
        // panel_graph.setBackground(new Color(65, 105, 225));
        panel.add(panel_graph);

        JButton b = null;

        b = ATSwingUtils.getButton("", 575, 470, 30, 30, panel, ATSwingUtils.FONT_NORMAL, "document_add.png",
            "item_add", this, m_da);
        b.setToolTipText(m_ic.getMessage("ADD_BASAL_SUB_ENTRY"));

        b = ATSwingUtils.getButton("", 615, 470, 30, 30, panel, ATSwingUtils.FONT_NORMAL, "document_edit.png",
            "item_edit", this, m_da);
        b.setToolTipText(m_ic.getMessage("EDIT_BASAL_SUB_ENTRY"));

        b = ATSwingUtils.getButton("", 655, 470, 30, 30, panel, ATSwingUtils.FONT_NORMAL, "document_delete.png",
            "item_delete", this, m_da);
        b.setToolTipText(m_ic.getMessage("DELETE_BASAL_SUB_ENTRY"));

        b = ATSwingUtils.getButton("", 695, 470, 30, 30, panel, ATSwingUtils.FONT_NORMAL, "document_exchange.png",
            "import_profile", this, m_da);
        b.setToolTipText(m_ic.getMessage("IMPORT_BASAL_SUB_ENTRIES"));

        /*
         * JButton bt_item_1 = new
         * JButton(dataAccess.getImageIcon_22x22("document_add.png", this));
         * bt_item_1.setActionCommand("item_add");
         * bt_item_1.addActionListener(this);
         * bt_item_1.setBounds(585, 470, 30, 30);
         * panel.add(bt_item_1);
         * JButton bt_item_2 = new
         * JButton(dataAccess.getImageIcon_22x22("document_edit.png", this));
         * bt_item_2.setActionCommand("item_edit");
         * bt_item_2.addActionListener(this);
         * bt_item_2.setBounds(635, 470, 30, 30);
         * panel.add(bt_item_2);
         * JButton bt_item_3 = new
         * JButton(dataAccess.getImageIcon_22x22("document_delete.png", this));
         * bt_item_3.setActionCommand("item_delete");
         * bt_item_3.addActionListener(this);
         * bt_item_3.setBounds(685, 470, 30, 30);
         * panel.add(bt_item_3);
         */
        /*
         * JButton bt_item_3 = new
         * JButton(dataAccess.getImageIcon_22x22("document_delete.png", this));
         * bt_item_3.setActionCommand("item_delete");
         * bt_item_3.addActionListener(this);
         * bt_item_3.setBounds(685, 470, 30, 30);
         * panel.add(bt_item_3);
         * document_exchange.png
         * ATSwingUtils.getButton("   " + i18nControl.getMessage("IMPORT"),
         * 570, 445, 160, 22, panel,
         * ATSwingUtils.FONT_NORMAL, null, "import_profile", this, dataAccess);
         */

        ATSwingUtils.getButton("   " + m_ic.getMessage("OK"), 610, 75, 120, 25, panel, ATSwingUtils.FONT_NORMAL,
            "ok.png", "ok", this, m_da);

        ATSwingUtils.getButton("   " + m_ic.getMessage("CANCEL"), 610, 110, 120, 25, panel, ATSwingUtils.FONT_NORMAL,
            "cancel.png", "cancel", this, m_da);

        this.help_button = ATSwingUtils.createHelpButtonByBounds(610, 145, 120, 25, this, ATSwingUtils.FONT_NORMAL,
            m_da);
        panel.add(this.help_button);

        m_da.enableHelp(this);

    }


    private void refreshList(int type, ArrayList<?> lst)
    {

        DefaultListModel listModel = new DefaultListModel();

        for (int i = 0; i < lst.size(); i++)
        {
            Object o = lst.get(i);
            listModel.addElement(o.toString());
        }

        this.lst_basals.setModel(listModel);
        this.graphview_pe.refreshData();
    }


    /**
     * Action Performed
     */
    public void actionPerformed(ActionEvent e)
    {
        String action = e.getActionCommand();

        if (action.equals("item_add"))
        {
            ProfileEntryDialog ped = new ProfileEntryDialog(this);
            ped.setVisible(true);

            if (ped.actionSuccessful())
            {
                this.list_data.add(ped.getResult());
                Collections.sort(this.list_data);
                refreshList(1, this.list_data);
            }

        }
        else if (action.equals("item_edit"))
        {
            if (this.lst_basals.isSelectionEmpty())
            {
                JOptionPane.showConfirmDialog(this, m_ic.getMessage("SELECT_ITEM_FIRST"), m_ic.getMessage("ERROR"),
                    JOptionPane.CLOSED_OPTION);
                return;
            }

            ProfileSubEntry pse = this.list_data.get(this.lst_basals.getSelectedIndex());

            ProfileEntryDialog ped = new ProfileEntryDialog(pse, this);
            ped.setVisible(true);

            if (ped.actionSuccessful())
            {
                pse.setValues(ped.getResult());
                Collections.sort(this.list_data);
                refreshList(1, this.list_data);
            }
        }
        else if (action.equals("item_delete"))
        {
            if (this.lst_basals.isSelectionEmpty())
            {
                JOptionPane.showConfirmDialog(this, m_ic.getMessage("SELECT_ITEM_FIRST"), m_ic.getMessage("ERROR"),
                    JOptionPane.CLOSED_OPTION);
                return;
            }

            int ii = JOptionPane.showConfirmDialog(this, m_ic.getMessage("ARE_YOU_SURE_DELETE"),
                m_ic.getMessage("QUESTION"), JOptionPane.YES_NO_OPTION);

            if (ii == JOptionPane.YES_OPTION)
            {
                this.list_data.remove(this.lst_basals.getSelectedIndex());
                Collections.sort(this.list_data);
                refreshList(1, this.list_data);
            }
            else
                return;
        }
        else if (action.equals("cancel"))
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
        else if (action.equals("select_profile"))
        {
            ProfileSelectorPump psp = new ProfileSelectorPump(m_da, this);

            if (psp.wasAction())
            {
                this.tf_name.setText(psp.getSelectedObject().toString());
            }
        }
        else if (action.equals("import_profile"))
        {
            // this.parent -> this
            ProfileSelector ps = new ProfileSelector(DataAccessPump.getInstance(), this, true);

            if (ps.wasAction())
            {
                PumpProfile p = (PumpProfile) ps.getSelectedObject();
                loadSubEntries(p.getBasalDiffs());
            }
        }
        else
        {
            System.out.println(e.getActionCommand() + " is currently not supported.");
        }
    }


    /**
     * State Changed - of check box
     */
    public void stateChanged(ChangeEvent e)
    {
        JCheckBox cb = (JCheckBox) e.getSource();
        dtc_till.setEnabled(cb.isSelected());
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
    public PumpProfileH getResult()
    {
        return this.m_profile;
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
        return "PumpTool_Profile_Editor";
    }

}
