/*
 *  GGC - GNU Gluco Control
 *
 *  A pure java app to help you manage your diabetes.
 *
 *  See AUTHORS for copyright information.
 *
 *  This program is free software; you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation; either version 2 of the License, or
 *  (at your option) any later version.
 *
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with this program; if not, write to the Free Software
 *  Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA
 *
 *  Filename: CourseGraphFrame.java
 *  Purpose:  Frame for the CourseGraphView and some controls.
 *
 *  Author:   schultd
 */

package ggc.core.data.graph;

import ggc.core.util.DataAccess;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;

import javax.swing.JButton;
import javax.swing.JPanel;

import com.atech.graphics.calendar.DateRangeSelectionPanel;
import com.atech.graphics.graphs.AbstractGraphViewControler;
import com.atech.graphics.graphs.GraphViewInterface;

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
 *  Filename:     ###--###  
 *  Description:  ###--###
 * 
 *  Author: andyrozman {andy@atech-software.com}  
 */


public class GGCGraphViewControler extends AbstractGraphViewControler   
{

    private static final long serialVersionUID = 8111521124871307877L;
    
    PlotSelectorPanel selectionPanel;
    int graph_type = 0;
    private DateRangeSelectionPanel dRS;
    
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
    public void init()
    {
        JPanel cPanel = new JPanel(new BorderLayout());
        
        this.graph_type = ((Integer)this.parameters).intValue();
        

        dRS = new DateRangeSelectionPanel(m_da);

        if (this.graph_type==GGCGraphViewControler.GRAPH_COURSE)
        {
            selectionPanel = new PlotSelectorPanel(DataPlotSelectorPanel.BG_AVG_MASK);
            selectionPanel.disableChoice(DataPlotSelectorPanel.BG_MASK | DataPlotSelectorPanel.CH_MASK
                    | DataPlotSelectorPanel.INS1_MASK | DataPlotSelectorPanel.INS2_MASK
                    | DataPlotSelectorPanel.INS_TOTAL_MASK);
            //cGV.setData(selectionPanel.getPlotData());
            selectionPanel.setSelectionMode(PlotSelectorPanel.SELECTION_MODE_MULTIPLE);
        }
        else if (this.graph_type==GGCGraphViewControler.GRAPH_SPREAD)
        {
            selectionPanel = new PlotSelectorPanel(DataPlotSelectorPanel.BG_MASK);
            selectionPanel.removeChoice(DataPlotSelectorPanel.BG_AVG_MASK | DataPlotSelectorPanel.BG_READINGS_MASK
                    | DataPlotSelectorPanel.CH_AVG_MASK | DataPlotSelectorPanel.CH_SUM_MASK
                    | DataPlotSelectorPanel.INS1_AVG_MASK| DataPlotSelectorPanel.INS2_AVG_MASK
                    | DataPlotSelectorPanel.INS1_SUM_MASK| DataPlotSelectorPanel.INS2_SUM_MASK
                    | DataPlotSelectorPanel.INS_PER_CH_MASK| DataPlotSelectorPanel.INS_TOTAL_AVG_MASK
                    | DataPlotSelectorPanel.INS_TOTAL_SUM_MASK| DataPlotSelectorPanel.MEALS_MASK);
            selectionPanel.setSelectionMode(PlotSelectorPanel.SELECTION_MODE_SINGLE);
        }
        else
        {
            System.out.println("GGC Graph View Controler needs to be implemented for type [" + this.graph_type + "]");
            selectionPanel = new PlotSelectorPanel(DataPlotSelectorPanel.BG_MASK);
            /*selectionPanel.disableChoice(DataPlotSelectorPanel.BG_MASK | DataPlotSelectorPanel.CH_MASK
                    | DataPlotSelectorPanel.INS1_MASK | DataPlotSelectorPanel.INS2_MASK
                    | DataPlotSelectorPanel.INS_TOTAL_MASK); */
            selectionPanel.setSelectionMode(PlotSelectorPanel.SELECTION_MODE_SINGLE);
            
        }

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));

        help_button = m_da.createHelpButtonBySize(120, 25, cPanel);
        buttonPanel.add(help_button);

        // Dimension dim = new Dimension(80, 20);
        Dimension dim = new Dimension(120, 25);
        JButton drawButton = new JButton("    " + m_ic.getMessage("DRAW"));
        drawButton.setPreferredSize(dim);
        drawButton.setIcon(m_da.getImageIcon_22x22("paint.png", cPanel));
        drawButton.setActionCommand("draw");
        drawButton.addActionListener(this);

        JButton closeButton = new JButton("    " + m_ic.getMessage("CLOSE"));
        closeButton.setPreferredSize(dim);
        closeButton.setActionCommand("close");
        closeButton.setIcon(m_da.getImageIcon_22x22("cancel.png", cPanel));
        closeButton.addActionListener(this);
        buttonPanel.add(drawButton);
        buttonPanel.add(closeButton);

        cPanel.add(dRS, BorderLayout.WEST);
        cPanel.add(selectionPanel, BorderLayout.CENTER);
        cPanel.add(buttonPanel, BorderLayout.SOUTH);

        this.control_panel = cPanel;
    }



    /**
     * Run Draw
     * @see com.atech.graphics.graphs.AbstractGraphViewControler#runDraw()
     */
    @Override
    public void runDraw()
    {
        PlotSelectorData psd = this.selectionPanel.getPlotData();
        psd.setDateRangeData(this.dRS.getDateRangeData());

        System.out.println("runDraw: dataRange: " + this.dRS.getDateRangeData().toString());

        
        System.out.println("runDraw: " + psd.isPlotBG());
        
        this.getGraphView().getProcessor().setControllerData(psd);
        this.getGraphView().getProcessor().reloadData();
    }

}
