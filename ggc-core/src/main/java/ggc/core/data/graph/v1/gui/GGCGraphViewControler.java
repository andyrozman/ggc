package ggc.core.data.graph.v1.gui;

import java.awt.*;

import javax.swing.*;

import com.atech.graphics.calendar.DateRangeSelectionPanel;
import com.atech.graphics.graphs.AbstractGraphViewControler;
import com.atech.graphics.graphs.GraphViewInterface;
import com.atech.graphics.layout.TableLayoutUtil;
import com.atech.utils.ATSwingUtils;

import ggc.core.data.graph.v1.GGCGraphType;
import ggc.core.data.graph.v1.data.PlotSelectorData;
import ggc.core.util.DataAccess;
import info.clearthought.layout.TableLayout;

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
    GGCGraphType graphType;
    DateRangeSelectionPanel date_panel;
    JPanel buttonPanel;
    JButton drawButton, closeButton;


    /**
     * Constructor
     *
     * @param _graph_view
     * @param _graph_type
     */
    public GGCGraphViewControler(GraphViewInterface _graph_view, GGCGraphType _graph_type)
    {
        super(DataAccess.getInstance(), _graph_view, _graph_type);
    }


    /**
     * Init
     *
     * @see AbstractGraphViewControler#init()
     */
    @Override
    public void init()
    {
        JPanel mainPanel = new JPanel();

        mainPanel.setLayout(TableLayoutUtil.createVerticalLayout(TableLayout.FILL, 40));

        JPanel panelControls = new JPanel();
        panelControls.setLayout(TableLayoutUtil.createHorizontalLayout(220, TableLayout.FILL));

        mainPanel.add(panelControls, "0, 0");

        this.graphType = (GGCGraphType) this.parameters;
        // this.graph_type = ((Integer) this.parameters).intValue();
        ATSwingUtils.initLibrary();

        date_panel = new DateRangeSelectionPanel(m_da);
        panelControls.add(date_panel, "0, 0");

        if (this.graphType == GGCGraphType.Course)
        {
            selection_panel = new PlotSelectorPanel(DataPlotSelectorPanel.BG_AVG_MASK);
            selection_panel.disableChoice(
                DataPlotSelectorPanel.BG_MASK | DataPlotSelectorPanel.CH_MASK | DataPlotSelectorPanel.INS1_MASK
                        | DataPlotSelectorPanel.INS2_MASK | DataPlotSelectorPanel.INS_TOTAL_MASK);
            selection_panel.setSelectionMode(PlotSelectorPanel.SELECTION_MODE_MULTIPLE);
        }
        else if (this.graphType == GGCGraphType.Spread)
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
        else if (this.graphType == GGCGraphType.Frequency)
        {
            selection_panel = new PlotSelectorPanel(DataPlotSelectorPanel.BG_MASK);
            selection_panel.removeChoice(DataPlotSelectorPanel.BG_AVG_MASK | DataPlotSelectorPanel.BG_READINGS_MASK
                    | DataPlotSelectorPanel.CH_AVG_MASK | DataPlotSelectorPanel.CH_SUM_MASK
                    | DataPlotSelectorPanel.INS1_AVG_MASK | DataPlotSelectorPanel.INS2_AVG_MASK
                    | DataPlotSelectorPanel.INS1_SUM_MASK | DataPlotSelectorPanel.INS2_SUM_MASK
                    | DataPlotSelectorPanel.INS_PER_CH_MASK | DataPlotSelectorPanel.INS_TOTAL_AVG_MASK
                    | DataPlotSelectorPanel.INS_TOTAL_SUM_MASK | DataPlotSelectorPanel.MEALS_MASK
                    | DataPlotSelectorPanel.INS_TOTAL_MASK);
            selection_panel.setSelectionMode(PlotSelectorPanel.SELECTION_MODE_SINGLE);
        }
        else
        {
            System.out.println(
                "GGC Graph View Controler needs to be implemented for type [" + this.graphType.name() + "]");
            selection_panel = new PlotSelectorPanel(DataPlotSelectorPanel.BG_MASK);
            selection_panel.setSelectionMode(PlotSelectorPanel.SELECTION_MODE_SINGLE);
        }

        panelControls.add(selection_panel, "1, 0");

        buttonPanel = new JPanel();

        double sizes[][] = { { 0.2, 0.2, 5, 0.2, 5, 0.2, 0.2 }, { 5, TableLayout.FILL, 5 } };

        buttonPanel.setLayout(new TableLayout(sizes));

        helpButton = ATSwingUtils.createHelpButtonByBounds(450, 7, 120, 25, buttonPanel, ATSwingUtils.FONT_NORMAL_BOLD,
            m_da.getImagesRoot(), m_ic);
        buttonPanel.add(helpButton, "1, 1");

        drawButton = ATSwingUtils.getButton("    " + m_ic.getMessage("DRAW"), 180, 7, 120, 25, null,
            ATSwingUtils.FONT_NORMAL_BOLD, "paint.png", "draw", this, m_da, buttonPanel);
        buttonPanel.add(drawButton, "3, 1");

        closeButton = ATSwingUtils.getButton("    " + m_ic.getMessage("CLOSE"), 315, 7, 120, 25, null,
            ATSwingUtils.FONT_NORMAL_BOLD, "cancel.png", "close", this, m_da, buttonPanel);
        buttonPanel.add(closeButton, "5, 1");

        mainPanel.add(buttonPanel, "0, 1");

        this.control_panel = mainPanel;
    }


    /**
     * Run Draw
     * @see AbstractGraphViewControler#runDraw()
     */
    @Override
    public void runDraw()
    {
        PlotSelectorData psd = this.selection_panel.getPlotData();
        psd.setDateRangeData(this.date_panel.getDateRangeData());

        this.getGraphView().getProcessor().setControllerData(psd);
        this.getGraphView().getProcessor().reloadData();
    }


    public Rectangle getControlerBounds()
    {
        return new Rectangle(0, 0, 750, 190);
    }


    public void resizeController(int width)
    {
    }

}
