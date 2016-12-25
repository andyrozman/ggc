package main.java.ggc.pump.gui.bre;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Hashtable;

import javax.swing.*;

import com.atech.graphics.graphs.GraphViewerPanel;
import com.atech.utils.ATSwingUtils;

import main.java.ggc.pump.data.bre.BREData;
import main.java.ggc.pump.data.bre.BREDataCollection;
import main.java.ggc.pump.data.graph.GraphViewBasalRateEstimator;
import main.java.ggc.pump.data.graph.bre.BREGraphsAbstract;
import main.java.ggc.pump.data.graph.bre.GraphViewBasalRate;
import main.java.ggc.pump.data.graph.bre.GraphViewBasals;
import main.java.ggc.pump.data.graph.bre.GraphViewRatios;
import main.java.ggc.pump.util.DataAccessPump;

// TODO: Auto-generated Javadoc
/**
 * The Class BasalRateEstimator.
 */
public class BasalRateEstimator extends JDialog implements ActionListener
{
    private static final long serialVersionUID = 3285623806907044947L;

    /**
     * The dataAccess.
     */
    DataAccessPump m_da = DataAccessPump.getInstance();

    /**
     * The lst_ratios.
     */
    JList lst_bgs, lst_basals_old, lst_basals_new, lst_ratios;

    /**
     * The gv.
     */
    GraphViewBasalRateEstimator gv;

    /**
     * The bre_algorithm.
     */
    BasalRateEstimatorAlgorithm bre_algorithm;

    /**
     * The m_graphs.
     */
    Hashtable<String, BREGraphsAbstract> m_graphs;

    /**
     * Instantiates a new basal rate estimator.
     */
    public BasalRateEstimator()
    {
        ATSwingUtils.initLibrary();
        init();

        testData();

        this.setSize(900, 550);
    }

    private void init()
    {
        // this.setLayout(null);
        this.setTitle("Basal Rate Estimator");
        // JLabel label;

        JPanel panel = new JPanel();
        panel.setLayout(null);
        this.add(panel);

        ATSwingUtils.getTitleLabel("Basal Rate Estimator", 0, 20, 900, 35, panel, ATSwingUtils.FONT_BIG_BOLD);

        JTabbedPane tabbed = new JTabbedPane();

        JPanel panel_tab = new JPanel();
        panel_tab.setLayout(null);

        ATSwingUtils.getLabel("List of BGs:", 14, 20, 200, 25, panel_tab, ATSwingUtils.FONT_NORMAL_BOLD);

        lst_bgs = new JList();
        JScrollPane scr = new JScrollPane(lst_bgs);
        scr.setBounds(12, 55, 220, 230);
        panel_tab.add(scr);

        tabbed.addTab("BG", panel_tab);

        panel_tab = new JPanel();
        panel_tab.setLayout(null);

        ATSwingUtils.getLabel("List of Old Basals:", 14, 20, 200, 25, panel_tab, ATSwingUtils.FONT_NORMAL_BOLD);

        this.lst_basals_old = new JList();
        scr = new JScrollPane(this.lst_basals_old);
        scr.setBounds(12, 55, 220, 230);
        panel_tab.add(scr);

        tabbed.addTab("Basals", panel_tab);

        panel_tab = new JPanel();
        panel_tab.setLayout(null);

        ATSwingUtils.getLabel("List of Ratios:", 14, 20, 200, 25, panel_tab, ATSwingUtils.FONT_NORMAL_BOLD);

        this.lst_ratios = new JList();
        scr = new JScrollPane(this.lst_ratios);
        scr.setBounds(12, 55, 220, 230);
        panel_tab.add(scr);

        JButton b = ATSwingUtils.getButton("Configure Ratios", 12, 300, 220, 25, panel_tab, ATSwingUtils.FONT_NORMAL,
            null, "change_ratios", this, m_da);
        b.setEnabled(false);

        tabbed.addTab("Ratios", panel_tab);

        panel_tab = new JPanel();
        panel_tab.setLayout(null);

        ATSwingUtils.getLabel("List of New Basals:", 14, 20, 200, 25, panel_tab, ATSwingUtils.FONT_NORMAL_BOLD);

        this.lst_basals_new = new JList();
        scr = new JScrollPane(this.lst_basals_new);
        scr.setBounds(12, 55, 220, 230);
        panel_tab.add(scr);

        ATSwingUtils.getButton("Refresh", 12, 330, 220, 25, panel_tab, ATSwingUtils.FONT_NORMAL, null, "algorithm",
            this, m_da);

        tabbed.addTab("New Basals", panel_tab);

        tabbed.setBounds(30, 80, 250, 400);
        panel.add(tabbed);

        // ---
        // --- Graphs
        // ---

        this.m_graphs = new Hashtable<String, BREGraphsAbstract>();

        JTabbedPane tabbed_graphs = new JTabbedPane();
        tabbed_graphs.setTabPlacement(SwingConstants.BOTTOM);
        tabbed_graphs.setBounds(300, 120, 560, 360);
        panel.add(tabbed_graphs);

        /*
         * JPanel panel_graph = new JPanel();
         * panel_graph.setBounds(300, 120, 560, 360);
         * tabbed_graphs.addTab("Both Basal Graphs", panel_graph);
         */

        /*
         * JPanel panel_graph = new JPanel();
         * panel_graph.setBounds(300, 120, 560, 360);
         * gv = new GraphViewBasalRateEstimator();
         * GraphViewerPanel gvp = new GraphViewerPanel(gv);
         * gvp.setMinimumSize(new Dimension(550, 320)); // 450, 460
         * gvp.setPreferredSize(gvp.getMinimumSize());
         * panel_graph.add(gvp, BorderLayout.CENTER);
         * //panel.add(panel_graph);
         */

        tabbed_graphs.addTab("Both Basal Graphs", this.createPanelGraph(BasalRateEstimator.GRAPH_BOTH_BASAL_RATES));

        tabbed_graphs.addTab("Current Basal Graph", this.createPanelGraph(BasalRateEstimator.GRAPH_OLD_RATE));

        tabbed_graphs.addTab("Estimated Basal Graph", this.createPanelGraph(BasalRateEstimator.GRAPH_NEW_RATE));

        tabbed_graphs.addTab("Ratio's", this.createPanelGraph(BasalRateEstimator.GRAPH_RATIO));

        tabbed_graphs.addTab("Basal Rates", this.createPanelGraph(BasalRateEstimator.GRAPH_BASALS));

        bre_algorithm = new BasalRateEstimatorAlgorithm(m_graphs);

        ATSwingUtils.getLabel("Date of display: ", 350, 80, 200, 25, panel, ATSwingUtils.FONT_NORMAL_BOLD);
        ATSwingUtils.getLabel("02/02/2009", 500, 80, 200, 25, panel, ATSwingUtils.FONT_NORMAL);
        ATSwingUtils.getButton("Change Date", 670, 80, 190, 25, panel, ATSwingUtils.FONT_NORMAL, null, "change_date",
            this, m_da);

        // ATSwingUtils.getButton("Algorithm", 570, 250, 190, 25, panel,
        // ATSwingUtils.FONT_NORMAL, null, "algorithm", this, dataAccess);

        // ATSwingUtils.getButton("Change Date", 500, 50, 150, 25, panel,
        // ATSwingUtils.FONT_NORMAL, null, "change_date", this, dataAccess);

    }

    /**
     * The Constant GRAPH_BOTH_BASAL_RATES.
     */
    public static final int GRAPH_BOTH_BASAL_RATES = 1;

    /**
     * The Constant GRAPH_OLD_RATE.
     */
    public static final int GRAPH_OLD_RATE = 2;

    /**
     * The Constant GRAPH_NEW_RATE.
     */
    public static final int GRAPH_NEW_RATE = 3;

    /**
     * The Constant GRAPH_RATIO.
     */
    public static final int GRAPH_RATIO = 4;

    /**
     * The Constant GRAPH_BASALS.
     */
    public static final int GRAPH_BASALS = 5;

    private JPanel createPanelGraph(int type)
    {

        JPanel panel_graph = new JPanel();
        // panel_graph.setBounds(300, 120, 560, 360);
        BREGraphsAbstract _gv = null;

        if (type >= 1 && type <= 3)
        {
            _gv = new GraphViewBasalRate(type);
        }
        else if (type == 4)
        {
            _gv = new GraphViewRatios();
        }
        else
        {
            _gv = new GraphViewBasals();
        }

        m_graphs.put("" + type, _gv);

        GraphViewerPanel gvp = new GraphViewerPanel(gv);
        gvp.setMinimumSize(new Dimension(550, 310)); // 450, 460
        gvp.setPreferredSize(gvp.getMinimumSize());
        panel_graph.add(gvp, BorderLayout.CENTER);

        return panel_graph;
    }

    /*
     * private void init_v2()
     * {
     * //this.setLayout(null);
     * this.setTitle("Basal Rate Estimator");
     * JLabel label;
     * JPanel panel = new JPanel();
     * panel.setLayout(null);
     * this.add(panel);
     * ATSwingUtils.getTitleLabel("Basal Rate Estimator", 0, 20, 800, 35, panel,
     * ATSwingUtils.FONT_BIG_BOLD);
     * ATSwingUtils.getLabel("List of BGs:", 50, 85, 200, 25, panel,
     * ATSwingUtils.FONT_NORMAL_BOLD);
     * lst_bgs = new JList();
     * JScrollPane scr = new JScrollPane(lst_bgs);
     * scr.setBounds(40, 110, 160, 100);
     * panel.add(scr);
     * ATSwingUtils.getLabel("List of Old Basals:", 220, 85, 200, 25, panel,
     * ATSwingUtils.FONT_NORMAL_BOLD);
     * this.lst_basals_old = new JList();
     * scr = new JScrollPane(this.lst_basals_old);
     * scr.setBounds(210, 110, 160, 100);
     * panel.add(scr);
     * ATSwingUtils.getLabel("List of Ratios:", 390, 85, 200, 25, panel,
     * ATSwingUtils.FONT_NORMAL_BOLD);
     * this.lst_ratios = new JList();
     * scr = new JScrollPane(this.lst_ratios);
     * scr.setBounds(380, 110, 160, 100);
     * panel.add(scr);
     * ATSwingUtils.getLabel("List of New Basals:", 50, 280, 200, 25, panel,
     * ATSwingUtils.FONT_NORMAL_BOLD);
     * this.lst_basals_new = new JList();
     * scr = new JScrollPane(this.lst_basals_new);
     * scr.setBounds(40, 310, 160, 240);
     * panel.add(scr);
     * JPanel panel_graph = new JPanel();
     * panel_graph.setBorder(new TitledBorder("Basal Rate Display"));
     * panel_graph.setBounds(210, 220, 560, 320);
     * //panel_graph.setBackground(new Color(0, 191, 255));
     * gv = new GraphViewBasalRateEstimator();
     * GraphViewerPanel gvp = new GraphViewerPanel(gv);
     * gvp.setMinimumSize(new Dimension(550, 280)); // 450, 460
     * gvp.setPreferredSize(gvp.getMinimumSize());
     * panel_graph.add(gvp, BorderLayout.CENTER);
     * panel.add(panel_graph);
     * bre_algorithm = new BasalRateEstimatorAlgorithm(gv);
     * ATSwingUtils.getLabel("Date of display: ", 570, 80, 200, 25, panel,
     * ATSwingUtils.FONT_NORMAL_BOLD);
     * ATSwingUtils.getLabel("02/02/2009", 690, 80, 200, 25, panel,
     * ATSwingUtils.FONT_NORMAL);
     * ATSwingUtils.getButton("Change Date", 570, 130, 190, 25, panel,
     * ATSwingUtils.FONT_NORMAL, null, "change_date", this, dataAccess);
     * JButton b = ATSwingUtils.getButton("Configure Ratios", 570, 170, 190, 25,
     * panel, ATSwingUtils.FONT_NORMAL, null, "change_ratios", this, dataAccess);
     * b.setEnabled(false);
     * //ATSwingUtils.getButton("Algorithm", 570, 250, 190, 25, panel,
     * ATSwingUtils.FONT_NORMAL, null, "algorithm", this, dataAccess);
     * //ATSwingUtils.getButton("Change Date", 500, 50, 150, 25, panel,
     * ATSwingUtils.FONT_NORMAL, null, "change_date", this, dataAccess);
     * }
     */

    /*
     * private void init_1()
     * {
     * //this.setLayout(null);
     * this.setTitle("Basal Rate Estimator");
     * JLabel label;
     * JPanel panel = new JPanel();
     * panel.setLayout(null);
     * this.add(panel);
     * ATSwingUtils.getTitleLabel("Basal Rate Estimator", 0, 20, 800, 35, panel,
     * ATSwingUtils.FONT_BIG_BOLD);
     * ATSwingUtils.getLabel("List of BGs:", 50, 85, 200, 25, panel,
     * ATSwingUtils.FONT_NORMAL_BOLD);
     * lst_bgs = new JList();
     * JScrollPane scr = new JScrollPane(lst_bgs);
     * scr.setBounds(40, 110, 160, 160);
     * panel.add(scr);
     * ATSwingUtils.getLabel("List of Old Basals:", 220, 85, 200, 25, panel,
     * ATSwingUtils.FONT_NORMAL_BOLD);
     * this.lst_basals_old = new JList();
     * scr = new JScrollPane(this.lst_basals_old);
     * scr.setBounds(210, 110, 160, 160);
     * panel.add(scr);
     * ATSwingUtils.getLabel("List of Ratios:", 390, 85, 200, 25, panel,
     * ATSwingUtils.FONT_NORMAL_BOLD);
     * this.lst_ratios = new JList();
     * scr = new JScrollPane(this.lst_ratios);
     * scr.setBounds(380, 110, 160, 160);
     * panel.add(scr);
     * ATSwingUtils.getLabel("List of New Basals:", 50, 280, 200, 25, panel,
     * ATSwingUtils.FONT_NORMAL_BOLD);
     * this.lst_basals_new = new JList();
     * scr = new JScrollPane(this.lst_basals_new);
     * scr.setBounds(40, 310, 160, 240);
     * panel.add(scr);
     * /*
     * JPanel panel_graph = new JPanel();
     * panel_graph.setBorder(new TitledBorder("Basal Rate Display"));
     * panel_graph.setBounds(210, 280, 560, 270);
     * panel_graph.setBackground(new Color(0, 191, 255));
     * gv = new GraphViewBasalRateEstimator();
     * GraphViewerPanel gvp = new GraphViewerPanel(gv);
     * gvp.setMinimumSize(new Dimension(550, 240)); // 450, 460
     * gvp.setPreferredSize(gvp.getMinimumSize());
     * panel_graph.add(gvp, BorderLayout.CENTER);
     * panel.add(panel_graph);
     * bre_algorithm = new BasalRateEstimatorAlgorithm(gv);
     */

    /*
     * ATSwingUtils.getLabel("Date of display: ", 570, 120, 200, 25, panel,
     * ATSwingUtils.FONT_NORMAL_BOLD);
     * ATSwingUtils.getLabel("02/02/2009", 690, 120, 200, 25, panel,
     * ATSwingUtils.FONT_NORMAL);
     * ATSwingUtils.getButton("Change Date", 570, 170, 190, 25, panel,
     * ATSwingUtils.FONT_NORMAL, null, "change_date", this, dataAccess);
     * JButton b = ATSwingUtils.getButton("Configure Ratios", 570, 210, 190, 25,
     * panel, ATSwingUtils.FONT_NORMAL, null, "change_ratios", this, dataAccess);
     * b.setEnabled(false);
     * ATSwingUtils.getButton("Algorithm", 570, 250, 190, 25, panel,
     * ATSwingUtils.FONT_NORMAL, null, "algorithm", this, dataAccess);
     * //ATSwingUtils.getButton("Change Date", 500, 50, 150, 25, panel,
     * ATSwingUtils.FONT_NORMAL, null, "change_date", this, dataAccess);
     * }
     */

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
        // gv.setData(coll);

        // float ratio_ch_insulin, float ratio_bg_insulin, float ratio_ch_bg
        // ArrayList<RatioData> lst = new ArrayList<RatioData>();
        // lst.add(new RatioData(0000, 6.25f, 1.25f, 5.0f));
        coll.add(0000, 6.25f, 1.25f, 5.0f);
        coll.processRatios();

        refreshList(BREData.BRE_DATA_BASAL_RATIO, coll.getRatiosCollection(BREData.BRE_DATA_BASAL_RATIO));

        bre_algorithm.setData(coll);
    }

    private void refreshList(int type, ArrayList<?> lst)
    {
        DefaultListModel listModel = new DefaultListModel();

        for (int i = 0; i < lst.size(); i++)
        {
            Object o = lst.get(i);
            listModel.addElement(o.toString());
        }

        switch (type)
        {
            case BREData.BRE_DATA_BASAL_NEW:
                {
                    this.lst_basals_new.setModel(listModel);
                }
                break;

            case BREData.BRE_DATA_BASAL_OLD:
                {
                    this.lst_basals_old.setModel(listModel);
                }
                break;

            case BREData.BRE_DATA_BG:
                {
                    this.lst_bgs.setModel(listModel);
                }
                break;

            case BREData.BRE_DATA_BASAL_RATIO:
                {
                    this.lst_ratios.setModel(listModel);
                }
                break;
        }

    }

    /**
     * The main method.
     * 
     * @param args the arguments
     */
    public static void main(String args[])
    {
        BasalRateEstimator bre = new BasalRateEstimator();

        bre.setVisible(true);

    }

    /** 
     * actionPerformed
     */
    public void actionPerformed(ActionEvent e)
    {
        System.out.println(e.getActionCommand() + " is currently not supported.");
    }

}
