package ggc.pump.gui.profile;

import ggc.core.db.hibernate.pump.PumpProfileH;
import ggc.pump.data.db.PumpProfile;
import ggc.pump.data.graph.GraphViewBasalRateEstimator;
import ggc.pump.data.graph.bre.BREGraphsAbstract;
import ggc.pump.data.graph.bre.GraphViewBasalRate;
import ggc.pump.data.graph.bre.GraphViewBasals;
import ggc.pump.data.graph.bre.GraphViewRatios;
import ggc.pump.data.profile.ProfileSubEntry;
import ggc.pump.util.DataAccessPump;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.StringTokenizer;

import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JDialog;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSpinner;
import javax.swing.JTextField;
import javax.swing.SpinnerNumberModel;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import com.atech.graphics.components.DateTimeComponent;
import com.atech.graphics.graphs.GraphViewerPanel;
import com.atech.help.HelpCapable;
import com.atech.i18n.I18nControlAbstract;
import com.atech.utils.ATSwingUtils;

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
    JList lst_basals; //lst_bgs, lst_basals_old, lst_basals_new, lst_ratios;
    
    GraphViewBasalRateEstimator gv;
    Hashtable<String, BREGraphsAbstract> m_graphs;

    ArrayList<ProfileSubEntry> list_data;
    JCheckBox cb_enabled_till;
    DateTimeComponent dtc_from, dtc_till;
    JTextField tf_name;
    JButton help_button;    
    JDialog parent = null;
    PumpProfileH m_profile;
    boolean m_action_done = false;
    JSpinner sp_base;
    
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
        this.dtc_from.setDateTime(this.m_profile.getActive_from());
        this.dtc_till.setDateTime(this.m_profile.getActive_till());
        this.sp_base.setValue(this.m_profile.getBasal_base());
        this.loadSubEntries(this.m_profile.getBasal_diffs());
    }
    
    private void loadSubEntries(String subs)
    {
        // 0-400=0.2;400-600=1.7
        StringTokenizer strtok = new StringTokenizer(subs, ";");
        
        while (strtok.hasMoreTokens())
        {
            list_data.add(new ProfileSubEntry(strtok.nextToken()));
        }
        
        this.refreshList(1, list_data);
    }
    
    private void save()
    {
        this.m_profile.setName(this.tf_name.getText()); 
        this.m_profile.setActive_from(this.dtc_from.getDateTime()); 
        this.m_profile.setActive_till(this.dtc_till.getDateTime()); 
        this.m_profile.setBasal_base(m_da.getFloatValue(this.sp_base.getValue())); 
        this.m_profile.setBasal_diffs(getSubEntries());
        
        PumpProfile pp = new PumpProfile(this.m_profile);
        
        if (pp.getId()>0)
            m_da.getDb().edit(pp);
        else
            m_da.getDb().add(pp);
        
        this.m_profile.setId(pp.getId());
    }
    
    private String getSubEntries()
    {
        StringBuffer sb = new StringBuffer();
        
        for(int i=0; i<this.list_data.size(); i++)
        {
            if (i!=0)
                sb.append(";");
                
            sb.append(this.list_data.get(i).getPacked());
        }
        
        return sb.toString();
    }
    

    private void init()
    {
        //this.setLayout(null);
        this.setTitle(m_ic.getMessage("PROFILE_EDITOR"));
        //JLabel label;
        
        JPanel panel = new JPanel();
        panel.setLayout(null);
        this.add(panel);
        
        ATSwingUtils.getTitleLabel(m_ic.getMessage("PROFILE_EDITOR"), 0, 20, 780, 35, panel, ATSwingUtils.FONT_BIG_BOLD); 
        

        ATSwingUtils.getLabel(m_ic.getMessage("NAME") + ":", 80, 75, 120, 25, panel, ATSwingUtils.FONT_NORMAL_BOLD); 
        
        tf_name = ATSwingUtils.getTextField("", 190, 75, 120, 25, panel);
        
        ATSwingUtils.getLabel(m_ic.getMessage("DATE_FROM") + ":", 80, 110, 200, 25, panel, ATSwingUtils.FONT_NORMAL_BOLD); 

        dtc_from = new DateTimeComponent(m_da, DateTimeComponent.ALIGN_HORIZONTAL, 30, DateTimeComponent.TIME_MAXIMAL_SECOND);
        dtc_from.setBounds(190, 110, 120, 25);
        dtc_from.setDateTimeType(DateTimeComponent.TIME_MAXIMAL_MINUTE);
        dtc_from.setDateTimeAsCurrent();
        //dtc_from.setD
        panel.add(dtc_from);
        
        cb_enabled_till = ATSwingUtils.getCheckBox(" " + m_ic.getMessage("DATE_TILL") + ":", 80, 150, 80, 20, panel, ATSwingUtils.FONT_NORMAL_BOLD);
        cb_enabled_till.addChangeListener(this);
        
        dtc_till = new DateTimeComponent(m_da, DateTimeComponent.ALIGN_HORIZONTAL, 30, DateTimeComponent.TIME_MAXIMAL_SECOND);
        dtc_till.setBounds(190, 145, 120, 25);
        dtc_till.setDateTimeType(DateTimeComponent.TIME_MAXIMAL_MINUTE);
        dtc_till.setDateTimeAsCurrent();
        dtc_till.setEnabled(false);
        panel.add(dtc_till);
        
        
        ATSwingUtils.getLabel(m_ic.getMessage("BASE_BASAL") + ":", 560, 210, 200, 25, panel, ATSwingUtils.FONT_NORMAL_BOLD);
        
        sp_base = new JSpinner(new SpinnerNumberModel(0, 0, 25.0f, 0.1f));
        sp_base.setBounds(680, 210, 60, 25);
        panel.add(sp_base);
        
        
        this.lst_basals = new JList();
        JScrollPane scr = new JScrollPane(this.lst_basals);
        scr.setBounds(560, 250, 180, 210);
        panel.add(scr);
        
        
        JPanel panel_graph = new JPanel();
        panel_graph.setBounds(30, 200, 500, 300);
        panel_graph.setBackground(new Color(65, 105, 225));
        panel.add(panel_graph);

        
        
        JButton bt_item_1 = new JButton(m_da.getImageIcon_22x22("document_add.png", this));
        bt_item_1.setActionCommand("item_add");
        bt_item_1.addActionListener(this);
        bt_item_1.setBounds(585, 470, 30, 30);
        panel.add(bt_item_1);
        
        JButton bt_item_2 = new JButton(m_da.getImageIcon_22x22("document_edit.png", this));
        bt_item_2.setActionCommand("item_edit");
        bt_item_2.addActionListener(this);
        bt_item_2.setBounds(635, 470, 30, 30);
        panel.add(bt_item_2);
        
        JButton bt_item_3 = new JButton(m_da.getImageIcon_22x22("document_delete.png", this));
        bt_item_3.setActionCommand("item_delete");
        bt_item_3.addActionListener(this);
        bt_item_3.setBounds(685, 470, 30, 30);
        panel.add(bt_item_3);
        
        ATSwingUtils.getButton(m_ic.getMessage("OK"), 
            630, 80, 100, 25, panel, 
            ATSwingUtils.FONT_NORMAL_BOLD, "ok.png", "ok", this, m_da);

        ATSwingUtils.getButton(m_ic.getMessage("CANCEL"), 
            630, 115, 100, 25, panel, 
            ATSwingUtils.FONT_NORMAL_BOLD, "cancel.png", "cancel", this, m_da);

        this.help_button = this.m_da.createHelpButtonByBounds(630, 150, 100, 25, this);
        panel.add(this.help_button);
        
        
        //---
        //--- Graphs
        //---
        
        /*
        this.m_graphs = new Hashtable<String, BREGraphsAbstract>();
        
        JTabbedPane tabbed_graphs = new JTabbedPane();
        tabbed_graphs.setTabPlacement(JTabbedPane.BOTTOM);
        tabbed_graphs.setBounds(300, 120, 560, 360);
        panel.add(tabbed_graphs);
        */
        
        
        //tabbed_graphs.addTab("Both Basal Graphs", panel_graph);
        
        
        
        
        /*
        JPanel panel_graph = new JPanel();
        panel_graph.setBounds(300, 120, 560, 360);
        tabbed_graphs.addTab("Both Basal Graphs", panel_graph);
        */

        
        
        /*
        JPanel panel_graph = new JPanel();
        panel_graph.setBounds(300, 120, 560, 360);
        
        gv = new GraphViewBasalRateEstimator();
        
        GraphViewerPanel gvp = new GraphViewerPanel(gv);
        gvp.setMinimumSize(new Dimension(550, 320)); // 450, 460
        gvp.setPreferredSize(gvp.getMinimumSize());
        panel_graph.add(gvp, BorderLayout.CENTER);
        //panel.add(panel_graph); */
        
        /*
        tabbed_graphs.addTab("Both Basal Graphs", this.createPanelGraph(BasalRateEstimator.GRAPH_BOTH_BASAL_RATES));

        tabbed_graphs.addTab("Current Basal Graph", this.createPanelGraph(BasalRateEstimator.GRAPH_OLD_RATE));
        
        tabbed_graphs.addTab("Estimated Basal Graph", this.createPanelGraph(BasalRateEstimator.GRAPH_NEW_RATE));

        tabbed_graphs.addTab("Ratio's", this.createPanelGraph(BasalRateEstimator.GRAPH_RATIO));
        
        tabbed_graphs.addTab("Basal Rates", this.createPanelGraph(BasalRateEstimator.GRAPH_BASALS));
        */
        
        
        
        //ATSwingUtils.getLabel("Date of display: ", 350, 80, 200, 25, panel, ATSwingUtils.FONT_NORMAL_BOLD); 
        //ATSwingUtils.getLabel("02/02/2009", 500, 80, 200, 25, panel, ATSwingUtils.FONT_NORMAL); 
        //ATSwingUtils.getButton("Change Date", 670, 80, 190, 25, panel, ATSwingUtils.FONT_NORMAL, null, "change_date", this, m_da);
        

        //ATSwingUtils.getButton("Algorithm", 570, 250, 190, 25, panel, ATSwingUtils.FONT_NORMAL, null, "algorithm", this, m_da);
        
        //ATSwingUtils.getButton("Change Date", 500, 50, 150, 25, panel, ATSwingUtils.FONT_NORMAL, null, "change_date", this, m_da);
        
    }

    
    
    
    @SuppressWarnings("unused")
    private JPanel createPanelGraph(int type)
    {
        
        JPanel panel_graph = new JPanel();
        //panel_graph.setBounds(300, 120, 560, 360);
        @SuppressWarnings("hiding")
        BREGraphsAbstract gv = null;
        
        if ((type>=1) && (type <=3))
        {
            gv = new GraphViewBasalRate(type);
        }
        else if (type==4)
        {
            gv = new GraphViewRatios();
        }
        else
        {
            gv = new GraphViewBasals();
            
        }
        
        m_graphs.put("" + type, gv);
        
        GraphViewerPanel gvp = new GraphViewerPanel(gv);
        gvp.setMinimumSize(new Dimension(550, 310)); // 450, 460
        gvp.setPreferredSize(gvp.getMinimumSize());
        panel_graph.add(gvp, BorderLayout.CENTER);
        
        return panel_graph;
    }
    
    
    
    /*
    private void init_v2()
    {
        //this.setLayout(null);
        this.setTitle("Basal Rate Estimator");
        JLabel label;
        
        JPanel panel = new JPanel();
        panel.setLayout(null);
        this.add(panel);
        
        ATSwingUtils.getTitleLabel("Basal Rate Estimator", 0, 20, 800, 35, panel, ATSwingUtils.FONT_BIG_BOLD); 
        
        ATSwingUtils.getLabel("List of BGs:", 50, 85, 200, 25, panel, ATSwingUtils.FONT_NORMAL_BOLD); 
            
        lst_bgs = new JList();
        JScrollPane scr = new JScrollPane(lst_bgs);
        scr.setBounds(40, 110, 160, 100);
        panel.add(scr);
        
        ATSwingUtils.getLabel("List of Old Basals:", 220, 85, 200, 25, panel, ATSwingUtils.FONT_NORMAL_BOLD); 

        this.lst_basals_old = new JList();
        scr = new JScrollPane(this.lst_basals_old);
        scr.setBounds(210, 110, 160, 100);
        panel.add(scr);

        ATSwingUtils.getLabel("List of Ratios:", 390, 85, 200, 25, panel, ATSwingUtils.FONT_NORMAL_BOLD); 
        
        this.lst_ratios = new JList();
        scr = new JScrollPane(this.lst_ratios);
        scr.setBounds(380, 110, 160, 100);
        panel.add(scr);

        ATSwingUtils.getLabel("List of New Basals:", 50, 280, 200, 25, panel, ATSwingUtils.FONT_NORMAL_BOLD); 
        
        this.lst_basals_new = new JList();
        scr = new JScrollPane(this.lst_basals_new);
        scr.setBounds(40, 310, 160, 240);
        panel.add(scr);
        
        JPanel panel_graph = new JPanel();
        panel_graph.setBorder(new TitledBorder("Basal Rate Display"));
        panel_graph.setBounds(210, 220, 560, 320);
        //panel_graph.setBackground(new Color(0, 191, 255));
        
        gv = new GraphViewBasalRateEstimator();
        
        GraphViewerPanel gvp = new GraphViewerPanel(gv);
        gvp.setMinimumSize(new Dimension(550, 280)); // 450, 460
        gvp.setPreferredSize(gvp.getMinimumSize());
        panel_graph.add(gvp, BorderLayout.CENTER);
        panel.add(panel_graph);

        bre_algorithm = new BasalRateEstimatorAlgorithm(gv);
        
        ATSwingUtils.getLabel("Date of display: ", 570, 80, 200, 25, panel, ATSwingUtils.FONT_NORMAL_BOLD); 
        

        
        ATSwingUtils.getLabel("02/02/2009", 690, 80, 200, 25, panel, ATSwingUtils.FONT_NORMAL); 
        
        ATSwingUtils.getButton("Change Date", 570, 130, 190, 25, panel, ATSwingUtils.FONT_NORMAL, null, "change_date", this, m_da);
        
        JButton b = ATSwingUtils.getButton("Configure Ratios", 570, 170, 190, 25, panel, ATSwingUtils.FONT_NORMAL, null, "change_ratios", this, m_da);
        b.setEnabled(false);

        //ATSwingUtils.getButton("Algorithm", 570, 250, 190, 25, panel, ATSwingUtils.FONT_NORMAL, null, "algorithm", this, m_da);
        
        //ATSwingUtils.getButton("Change Date", 500, 50, 150, 25, panel, ATSwingUtils.FONT_NORMAL, null, "change_date", this, m_da);
        
    }
    */
    
    /*
    private void init_1()
    {
        //this.setLayout(null);
        this.setTitle("Basal Rate Estimator");
        JLabel label;
        
        JPanel panel = new JPanel();
        panel.setLayout(null);
        this.add(panel);
        
        ATSwingUtils.getTitleLabel("Basal Rate Estimator", 0, 20, 800, 35, panel, ATSwingUtils.FONT_BIG_BOLD); 
        
        ATSwingUtils.getLabel("List of BGs:", 50, 85, 200, 25, panel, ATSwingUtils.FONT_NORMAL_BOLD); 
            
        lst_bgs = new JList();
        JScrollPane scr = new JScrollPane(lst_bgs);
        scr.setBounds(40, 110, 160, 160);
        panel.add(scr);
        
        ATSwingUtils.getLabel("List of Old Basals:", 220, 85, 200, 25, panel, ATSwingUtils.FONT_NORMAL_BOLD); 

        this.lst_basals_old = new JList();
        scr = new JScrollPane(this.lst_basals_old);
        scr.setBounds(210, 110, 160, 160);
        panel.add(scr);

        ATSwingUtils.getLabel("List of Ratios:", 390, 85, 200, 25, panel, ATSwingUtils.FONT_NORMAL_BOLD); 
        
        this.lst_ratios = new JList();
        scr = new JScrollPane(this.lst_ratios);
        scr.setBounds(380, 110, 160, 160);
        panel.add(scr);

        ATSwingUtils.getLabel("List of New Basals:", 50, 280, 200, 25, panel, ATSwingUtils.FONT_NORMAL_BOLD); 
        
        this.lst_basals_new = new JList();
        scr = new JScrollPane(this.lst_basals_new);
        scr.setBounds(40, 310, 160, 240);
        panel.add(scr);
        
        
        
        /*
        JPanel panel_graph = new JPanel();
        panel_graph.setBorder(new TitledBorder("Basal Rate Display"));
        panel_graph.setBounds(210, 280, 560, 270);
        panel_graph.setBackground(new Color(0, 191, 255));
        
        gv = new GraphViewBasalRateEstimator();
        
        GraphViewerPanel gvp = new GraphViewerPanel(gv);
        gvp.setMinimumSize(new Dimension(550, 240)); // 450, 460
        gvp.setPreferredSize(gvp.getMinimumSize());
        panel_graph.add(gvp, BorderLayout.CENTER);
        panel.add(panel_graph);

        bre_algorithm = new BasalRateEstimatorAlgorithm(gv);
*/        
        
      /*  
        
        ATSwingUtils.getLabel("Date of display: ", 570, 120, 200, 25, panel, ATSwingUtils.FONT_NORMAL_BOLD); 
        ATSwingUtils.getLabel("02/02/2009", 690, 120, 200, 25, panel, ATSwingUtils.FONT_NORMAL); 
        ATSwingUtils.getButton("Change Date", 570, 170, 190, 25, panel, ATSwingUtils.FONT_NORMAL, null, "change_date", this, m_da);
        
        JButton b = ATSwingUtils.getButton("Configure Ratios", 570, 210, 190, 25, panel, ATSwingUtils.FONT_NORMAL, null, "change_ratios", this, m_da);
        b.setEnabled(false);

        ATSwingUtils.getButton("Algorithm", 570, 250, 190, 25, panel, ATSwingUtils.FONT_NORMAL, null, "algorithm", this, m_da);
        
        //ATSwingUtils.getButton("Change Date", 500, 50, 150, 25, panel, ATSwingUtils.FONT_NORMAL, null, "change_date", this, m_da);
        
    }*/
    
    
    
    private void refreshList(int type, ArrayList<?> lst)
    {
        
        DefaultListModel listModel = new DefaultListModel();
        
        for(int i=0; i<lst.size(); i++)
        {
            Object o = lst.get(i);
            listModel.addElement(o.toString());
        }

        this.lst_basals.setModel(listModel);
        
        /*
        switch(type)
        {
            case BREData.BRE_DATA_BASAL_NEW:
            {
                this.lst_basals_new.setModel(listModel);
            } break;
            
            case BREData.BRE_DATA_BASAL_OLD:
            {
                this.lst_basals_old.setModel(listModel);
            } break;
                
            case BREData.BRE_DATA_BG:
            {
                this.lst_bgs.setModel(listModel);
            } break;

            case BREData.BRE_DATA_BASAL_RATIO:
            {
                this.lst_ratios.setModel(listModel);
            } break;
        }
        */
        
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
                //Collections.sort(this.list_data);
                refreshList(1, this.list_data);
            }
            
        }
        else if (action.equals("item_edit"))
        {
            if (this.lst_basals.isSelectionEmpty())
            {
                JOptionPane.showConfirmDialog(this, m_ic.getMessage("SELECT_ITEM_FIRST"), m_ic.getMessage("ERROR"), JOptionPane.CLOSED_OPTION);
                return;
            }

            ProfileSubEntry pse = this.list_data.get(this.lst_basals.getSelectedIndex());

            ProfileEntryDialog ped = new ProfileEntryDialog(pse, this);
            ped.setVisible(true);

            if (ped.actionSuccessful())
            {
                pse.setValues(ped.getResult());
                //Collections.sort(this.list_data);
                refreshList(1, this.list_data);
            }
        }
        else if (action.equals("item_delete"))
        {
            if (this.lst_basals.isSelectionEmpty())
            {
                JOptionPane.showConfirmDialog(this, m_ic.getMessage("SELECT_ITEM_FIRST"), m_ic.getMessage("ERROR"), JOptionPane.CLOSED_OPTION);
                return;
            }

            int ii = JOptionPane.showConfirmDialog(this, m_ic.getMessage("ARE_YOU_SURE_DELETE"), m_ic.getMessage("QUESTION"), JOptionPane.YES_NO_OPTION);

            if (ii==JOptionPane.YES_OPTION)
            {
                this.list_data.remove(this.lst_basals.getSelectedIndex());
                //Collections.sort(this.list_data);
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
            
        else
            System.out.println(e.getActionCommand() + " is currently not supported.");
    }


    /**
     * State Changed - of check box
     */
    public void stateChanged(ChangeEvent e)
    {
        JCheckBox cb = (JCheckBox)e.getSource();
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
        // FIXME
        return "";
    }
    
    
    
    
}

