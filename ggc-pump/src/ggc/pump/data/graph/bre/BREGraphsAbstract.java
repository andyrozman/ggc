package ggc.pump.data.graph.bre;

import ggc.core.data.graph.GGCGraphUtil;
import ggc.pump.data.bre.BREData;
import ggc.pump.data.bre.BREDataCollection;
import ggc.pump.util.DataAccessPump;

import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.GregorianCalendar;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.DateAxis;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer;
import org.jfree.data.general.AbstractDataset;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;

import com.atech.graphics.graphs.AbstractGraphViewAndProcessor;
import com.atech.utils.ATechDate;

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
 *  Filename:     GraphViewDaily  
 *  Description:  GraphView implementation for Daily, used by new graph framework.
 *          Most of rumbi's code from DailyGraphView was reused and extended.
 * 
 *  Author: andyrozman {andy@atech-software.com}  
 *  Author: rumbi   
 */


public abstract class BREGraphsAbstract extends AbstractGraphViewAndProcessor //implements GraphViewInterface, GraphViewDataProcessorInterface 
{

    protected DataAccessPump da_local = DataAccessPump.getInstance();
    protected GGCGraphUtil graph_util = GGCGraphUtil.getInstance(da_local);
    
    protected BREDataCollection data_coll;

    GregorianCalendar gcx = new GregorianCalendar();
    
    
    /**
     * Constructor
     */
    public BREGraphsAbstract()
    {
        super(DataAccessPump.getInstance());
    }
    

    /**
     * Get Help Id
     * 
     * @return
     */
    public String getHelpId()
    {
        return null; //"pages.GGC_BG_HbA1c";
    }

    

    
    /**
     * Get Viewer Dialog Bounds (used by GraphViewer)
     * 
     * @return Rectangle object
     */
    public Rectangle getViewerDialogBounds()
    {
        return new Rectangle(100,100,500,400);
    }



    
    public long getTimeMs(int time)
    {
        ATechDate atd = new ATechDate(ATechDate.FORMAT_TIME_ONLY_MIN, time);
        
        System.out.println("Ms: " + atd.hour_of_day + ":" + atd.minute);
        
        if (atd.minute == 99)
        {
            
        }
        
        gcx.set(GregorianCalendar.HOUR_OF_DAY, atd.hour_of_day);
        gcx.set(GregorianCalendar.MINUTE, atd.minute);
        
        return gcx.getTimeInMillis();
    }

    
    public void setData(BREDataCollection data_coll)
    {
        this.data_coll = data_coll;
        this.loadData();
        this.preprocessData();
        
        this.setPlot(chart);
    }
 
    
    /**
     * Create Chart Panel
     */
    public void createChartPanel()
    {
        this.chart_panel = new ChartPanel(getChart(), true, true, true, true, false);
    }
    

    /**
     * Get Title (used by GraphViewer)
     * 
     * @return title as string 
     */
    public String getTitle()
    {
        return null;
    }
    
    
    
}