package ggc.core.data.graph;

import ggc.core.util.DataAccess;

import java.awt.Rectangle;

import javax.swing.JButton;
import javax.swing.JPanel;

import com.atech.graphics.calendar.DateRangeSelectionPanel;
import com.atech.graphics.graphs.AbstractGraphViewControler;
import com.atech.graphics.graphs.GraphViewInterface;
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
 *  Filename:     GGCGraphViewControler  
 *  Description:  Controller for GGC Graphs
 * 
 *  Author: andyrozman {andy@atech-software.com}  
 */

public class GGCGraphViewControler extends AbstractGraphViewControler
{

    PlotSelectorPanel selection_panel;
    int graph_type = 0;
    DateRangeSelectionPanel date_panel;
    JPanel button_panel;
    JButton draw_button, close_button;

    protected JButton bts[]; // / = null;
    protected boolean inited = false;

    /**
     * Graph: None
     */
    public static final int GRAPH_NONE = 0;

    /**
     * Graph: Course
     */
    public static final int GRAPH_COURSE = 1;

    /**
     * Graph: Spread
     */
    public static final int GRAPH_SPREAD = 2;

    /**
     * Graph: Frequency
     */
    public static final int GRAPH_FREQUENCY = 3;

    /**
     * Constructor
     * 
     * @param _graph_view
     * @param _graph_type
     */
    public GGCGraphViewControler(GraphViewInterface _graph_view, int _graph_type)
    {
        super(DataAccess.getInstance(), _graph_view, new Integer(_graph_type));
    }

    /**
     * Init
     * 
     * @see com.atech.graphics.graphs.AbstractGraphViewControler#init()
     */
    @Override
    public void init()
    {
        // in_resize = true;

        JPanel cPanel = new JPanel();
        cPanel.setLayout(null);
        // cPanel.setBackground(Color.green);
        cPanel.setSize(750, 170);

        this.graph_type = ((Integer) this.parameters).intValue();
        ATSwingUtils.initLibrary();

        date_panel = new DateRangeSelectionPanel(m_da);
        date_panel.setBounds(0, 0, 200, 130);
        cPanel.add(date_panel);

        if (this.graph_type == GGCGraphViewControler.GRAPH_COURSE)
        {
            selection_panel = new PlotSelectorPanel(DataPlotSelectorPanel.BG_AVG_MASK);
            selection_panel.disableChoice(DataPlotSelectorPanel.BG_MASK | DataPlotSelectorPanel.CH_MASK
                    | DataPlotSelectorPanel.INS1_MASK | DataPlotSelectorPanel.INS2_MASK
                    | DataPlotSelectorPanel.INS_TOTAL_MASK);
            // cGV.setData(selectionPanel.getPlotData());
            selection_panel.setSelectionMode(PlotSelectorPanel.SELECTION_MODE_MULTIPLE);
        }
        else if (this.graph_type == GGCGraphViewControler.GRAPH_SPREAD)
        {
            selection_panel = new PlotSelectorPanel(DataPlotSelectorPanel.BG_MASK);
            selection_panel.removeChoice(DataPlotSelectorPanel.BG_AVG_MASK | DataPlotSelectorPanel.BG_READINGS_MASK
                    | DataPlotSelectorPanel.CH_AVG_MASK | DataPlotSelectorPanel.CH_SUM_MASK
                    | DataPlotSelectorPanel.INS1_AVG_MASK | DataPlotSelectorPanel.INS2_AVG_MASK
                    | DataPlotSelectorPanel.INS1_SUM_MASK | DataPlotSelectorPanel.INS2_SUM_MASK
                    | DataPlotSelectorPanel.INS_PER_CH_MASK | DataPlotSelectorPanel.INS_TOTAL_AVG_MASK
                    | DataPlotSelectorPanel.INS_TOTAL_SUM_MASK | DataPlotSelectorPanel.MEALS_MASK);
            selection_panel.setSelectionMode(PlotSelectorPanel.SELECTION_MODE_SINGLE);
        }
        else
        {
            System.out.println("GGC Graph View Controler needs to be implemented for type [" + this.graph_type + "]");
            selection_panel = new PlotSelectorPanel(DataPlotSelectorPanel.BG_MASK);
            /*
             * selectionPanel.disableChoice(DataPlotSelectorPanel.BG_MASK |
             * DataPlotSelectorPanel.CH_MASK
             * | DataPlotSelectorPanel.INS1_MASK |
             * DataPlotSelectorPanel.INS2_MASK
             * | DataPlotSelectorPanel.INS_TOTAL_MASK);
             */
            selection_panel.setSelectionMode(PlotSelectorPanel.SELECTION_MODE_SINGLE);
        }

        selection_panel.setBounds(200, 0, 548, 130);
        // selection_panel.setBackground(Color.magenta);

        cPanel.add(selection_panel);

        button_panel = new JPanel();
        button_panel.setLayout(null);
        button_panel.setBounds(0, 130, 750, 40);
        // button_panel.setBackground(Color.blue);

        help_button = ATSwingUtils.createHelpButtonByBounds(450, 7, 120, 25, button_panel, ATSwingUtils.FONT_NORMAL_BOLD, m_da.getImagesRoot(), m_ic);
        button_panel.add(help_button);

        draw_button = ATSwingUtils.getButton("    " + m_ic.getMessage("DRAW"), 180, 7, 120, 25, button_panel,
            ATSwingUtils.FONT_NORMAL_BOLD, "paint.png", "draw", this, m_da);
        close_button = ATSwingUtils.getButton("    " + m_ic.getMessage("CLOSE"), 315, 7, 120, 25, button_panel,
            ATSwingUtils.FONT_NORMAL_BOLD, "cancel.png", "close", this, m_da);

        cPanel.add(button_panel);

        this.control_panel = cPanel;

        this.inited = true;
    }

    /*
     * public void init()
     * {
     * JPanel cPanel = new JPanel(new BorderLayout());
     * this.graph_type = ((Integer)this.parameters).intValue();
     * dRS = new DateRangeSelectionPanel(m_da);
     * if (this.graph_type==GGCGraphViewControler.GRAPH_COURSE)
     * {
     * selectionPanel = new
     * PlotSelectorPanel(DataPlotSelectorPanel.BG_AVG_MASK);
     * selectionPanel.disableChoice(DataPlotSelectorPanel.BG_MASK |
     * DataPlotSelectorPanel.CH_MASK
     * | DataPlotSelectorPanel.INS1_MASK | DataPlotSelectorPanel.INS2_MASK
     * | DataPlotSelectorPanel.INS_TOTAL_MASK);
     * //cGV.setData(selectionPanel.getPlotData());
     * selectionPanel.setSelectionMode(PlotSelectorPanel.SELECTION_MODE_MULTIPLE)
     * ;
     * }
     * else if (this.graph_type==GGCGraphViewControler.GRAPH_SPREAD)
     * {
     * selectionPanel = new PlotSelectorPanel(DataPlotSelectorPanel.BG_MASK);
     * selectionPanel.removeChoice(DataPlotSelectorPanel.BG_AVG_MASK |
     * DataPlotSelectorPanel.BG_READINGS_MASK
     * | DataPlotSelectorPanel.CH_AVG_MASK | DataPlotSelectorPanel.CH_SUM_MASK
     * | DataPlotSelectorPanel.INS1_AVG_MASK|
     * DataPlotSelectorPanel.INS2_AVG_MASK
     * | DataPlotSelectorPanel.INS1_SUM_MASK|
     * DataPlotSelectorPanel.INS2_SUM_MASK
     * | DataPlotSelectorPanel.INS_PER_CH_MASK|
     * DataPlotSelectorPanel.INS_TOTAL_AVG_MASK
     * | DataPlotSelectorPanel.INS_TOTAL_SUM_MASK|
     * DataPlotSelectorPanel.MEALS_MASK);
     * selectionPanel.setSelectionMode(PlotSelectorPanel.SELECTION_MODE_SINGLE);
     * }
     * else
     * {
     * System.out.println(
     * "GGC Graph View Controler needs to be implemented for type [" +
     * this.graph_type + "]");
     * selectionPanel = new PlotSelectorPanel(DataPlotSelectorPanel.BG_MASK);
     * /*selectionPanel.disableChoice(DataPlotSelectorPanel.BG_MASK |
     * DataPlotSelectorPanel.CH_MASK
     * | DataPlotSelectorPanel.INS1_MASK | DataPlotSelectorPanel.INS2_MASK
     * | DataPlotSelectorPanel.INS_TOTAL_MASK);
     */
    /*
     * selectionPanel.setSelectionMode(PlotSelectorPanel.SELECTION_MODE_SINGLE);
     * }
     * JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
     * help_button = m_da.createHelpButtonBySize(120, 25, cPanel);
     * buttonPanel.add(help_button);
     * // Dimension dim = new Dimension(80, 20);
     * Dimension dim = new Dimension(120, 25);
     * JButton drawButton = new JButton("    " + m_ic.getMessage("DRAW"));
     * drawButton.setPreferredSize(dim);
     * drawButton.setIcon(m_da.getImageIcon_22x22("paint.png", cPanel));
     * drawButton.setActionCommand("draw");
     * drawButton.addActionListener(this);
     * JButton closeButton = new JButton("    " + m_ic.getMessage("CLOSE"));
     * closeButton.setPreferredSize(dim);
     * closeButton.setActionCommand("close");
     * closeButton.setIcon(m_da.getImageIcon_22x22("cancel.png", cPanel));
     * closeButton.addActionListener(this);
     * buttonPanel.add(drawButton);
     * buttonPanel.add(closeButton);
     * cPanel.add(dRS, BorderLayout.WEST);
     * cPanel.add(selectionPanel, BorderLayout.CENTER);
     * cPanel.add(buttonPanel, BorderLayout.SOUTH);
     * this.control_panel = cPanel;
     * }
     */

    /**
     * Run Draw
     * @see com.atech.graphics.graphs.AbstractGraphViewControler#runDraw()
     */
    @Override
    public void runDraw()
    {
        PlotSelectorData psd = this.selection_panel.getPlotData();
        psd.setDateRangeData(this.date_panel.getDateRangeData());

        // System.out.println("runDraw: dataRange: " +
        // this.date_panel.getDateRangeData().toString());
        // System.out.println("runDraw: " + psd.isPlotBG());

        this.getGraphView().getProcessor().setControllerData(psd);
        this.getGraphView().getProcessor().reloadData();
    }

    public Rectangle getControlerBounds()
    {
        return new Rectangle(0, 0, 750, 195);
        // TODO Auto-generated methd stub

        // return new Rectangle(100,100,750,500);

        // return null;
    }

    public void resizeController(int width)
    {
        Rectangle r = this.control_panel.getBounds();
        r.width = width;
        this.control_panel.setBounds(r);

        r = this.selection_panel.getBounds();
        r.width = width - this.date_panel.getBounds().width;
        this.selection_panel.setBounds(r);

        r = this.button_panel.getBounds();
        r.width = width;
        this.button_panel.setBounds(r);

        double s = 0.48 * width;
        s = s / 3.0;

        double sp = 0.04 * width;
        sp = sp / 2;

        double divide = width / 2.0;

        r = this.close_button.getBounds();
        r.width = (int) s;
        r.x = (int) (divide - s / 2);
        this.close_button.setBounds(r);

        r = this.draw_button.getBounds();
        r.width = (int) s;
        r.x = (int) (divide - s / 2 - sp - s);
        this.draw_button.setBounds(r);

        r = this.help_button.getBounds();
        r.width = (int) s;
        r.x = (int) (divide + s / 2 + sp);
        this.help_button.setBounds(r);

    }

}
