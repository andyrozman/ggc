package ggc.pump.gui.profile;

import ggc.pump.data.bre.BREData;
import ggc.pump.data.bre.BREDataCollection;
import ggc.pump.data.graph.GraphViewBasalRateEstimator;
import ggc.pump.data.graph.bre.BREGraphsAbstract;
import ggc.pump.data.graph.bre.GraphViewBasalRate;
import ggc.pump.data.graph.bre.GraphViewBasals;
import ggc.pump.data.graph.bre.GraphViewRatios;
import ggc.pump.util.DataAccessPump;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Hashtable;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;

import com.atech.graphics.components.DateTimeComponent;
import com.atech.graphics.graphs.GraphViewerPanel;
import com.atech.i18n.I18nControlAbstract;
import com.atech.utils.ATSwingUtils;

/**
 * @author Andy
 *
 */
public class ProfileEditor extends JDialog implements ActionListener
{
    private static final long serialVersionUID = 3285623806907044947L;
    DataAccessPump m_da = DataAccessPump.getInstance();
    I18nControlAbstract m_ic = null;
    JList lst_bgs, lst_basals_old, lst_basals_new, lst_ratios;
    
    GraphViewBasalRateEstimator gv;
    Hashtable<String, BREGraphsAbstract> m_graphs;
    
    /**
     * Constructor
     */
    public ProfileEditor()
    {
        ATSwingUtils.initLibrary();
        m_ic = m_da.getI18nControlInstance();
        
        init();
        testData();
        
        this.setSize(780, 565);
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
        
        ATSwingUtils.getTextField("", 160, 75, 120, 25, panel);
        
        ATSwingUtils.getLabel(m_ic.getMessage("FROM") + ":", 80, 110, 200, 25, panel, ATSwingUtils.FONT_NORMAL_BOLD); 

        DateTimeComponent dtc_from = new DateTimeComponent(m_da, DateTimeComponent.ALIGN_HORIZONTAL, 30, DateTimeComponent.TIME_MAXIMAL_SECOND);
        dtc_from.setBounds(160, 110, 120, 25);
        panel.add(dtc_from);
        
        ATSwingUtils.getCheckBox(" " + m_ic.getMessage("TILL") + ":", 80, 150, 80, 20, panel, ATSwingUtils.FONT_NORMAL_BOLD);
        
        DateTimeComponent dtc_till = new DateTimeComponent(m_da, DateTimeComponent.ALIGN_HORIZONTAL, 30, DateTimeComponent.TIME_MAXIMAL_SECOND);
        dtc_till.setBounds(160, 145, 120, 25);
        panel.add(dtc_till);
        
        
        ATSwingUtils.getLabel("Base Basal:", 560, 210, 200, 25, panel, ATSwingUtils.FONT_NORMAL_BOLD);
        
        JSpinner spin = new JSpinner(new SpinnerNumberModel(0, 0, 25.0f, 0.1f));
        spin.setBounds(680, 210, 60, 25);
        panel.add(spin);
        
        
        this.lst_basals_new = new JList();
        JScrollPane scr = new JScrollPane(this.lst_basals_new);
        scr.setBounds(560, 250, 180, 210);
        panel.add(scr);
        
        
        JPanel panel_graph = new JPanel();
        panel_graph.setBounds(30, 200, 500, 300);
        panel_graph.setBackground(Color.green);
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
    
    private void testData()
    {
        BREDataCollection coll = new BREDataCollection();
        
        coll.add(BREData.BRE_DATA_BG, 1154, 95);
        coll.add(BREData.BRE_DATA_BG, 1402, 92);
        coll.add(BREData.BRE_DATA_BG, 1606, 80);
        coll.add(BREData.BRE_DATA_BG, 1805, 64);
        coll.add(BREData.BRE_DATA_BG, 2004, 76);
        coll.add(BREData.BRE_DATA_BG, 2358, 112);

        refreshList(BREData.BRE_DATA_BG, coll.getDataByType(BREData.BRE_DATA_BG));
        
        coll.add(BREData.BRE_DATA_BASAL_OLD, 900, 0.8f);
        coll.add(BREData.BRE_DATA_BASAL_OLD, 1200, 0.9f);
        coll.add(BREData.BRE_DATA_BASAL_OLD, 1500, 0.7f);
        coll.add(BREData.BRE_DATA_BASAL_OLD, 1830, 0.6f);

        refreshList(BREData.BRE_DATA_BASAL_OLD, coll.getDataByType(BREData.BRE_DATA_BASAL_OLD));
        coll.processOldBasals();
        
        coll.add(BREData.BRE_DATA_BASAL_NEW, 900, 0.8f);
        coll.add(BREData.BRE_DATA_BASAL_NEW, 1200, 0.8f);
        coll.add(BREData.BRE_DATA_BASAL_NEW, 1500, 0.5f);
        coll.add(BREData.BRE_DATA_BASAL_NEW, 1830, 0.8f);

        refreshList(BREData.BRE_DATA_BASAL_NEW, coll.getDataByType(BREData.BRE_DATA_BASAL_NEW));
        coll.processNewBasals();
        //gv.setData(coll);
        
        // float ratio_ch_insulin, float ratio_bg_insulin, float ratio_ch_bg
        //ArrayList<RatioData> lst = new ArrayList<RatioData>();
        //lst.add(new RatioData(0000, 6.25f, 1.25f, 5.0f));
        coll.add(0000, 6.25f, 1.25f, 5.0f);
        coll.processRatios();
        
        refreshList(BREData.BRE_DATA_BASAL_RATIO, coll.getRatiosCollection(BREData.BRE_DATA_BASAL_RATIO));
        
        //bre_algorithm.setData(coll);
    }
    
    
    private void refreshList(int type, ArrayList<?> lst)
    {
        /*
        DefaultListModel listModel = new DefaultListModel();
        
        for(int i=0; i<lst.size(); i++)
        {
            Object o = lst.get(i);
            listModel.addElement(o.toString());
        }

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
     * Main
     * @param args
     */
    public static void main(String args[])
    {
        ProfileEditor bre = new ProfileEditor();
        
        bre.setVisible(true);
        
        
        
    }

    /**
     * Action Performed
     */
    public void actionPerformed(ActionEvent e)
    {
        System.out.println(e.getActionCommand() + " is currently not supported.");
    }
    
    
    
    
    
    
    
}

