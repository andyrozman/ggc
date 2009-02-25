package ggc.pump.data.graph.bre;

import ggc.pump.data.bre.BREData;
import ggc.pump.data.bre.RatioData;

import java.util.ArrayList;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.DateAxis;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.plot.XYPlot;
import org.jfree.data.general.AbstractDataset;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;

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


public class GraphViewRatios extends BREGraphsAbstract //implements GraphViewInterface, GraphViewDataProcessorInterface 
{

    XYSeriesCollection dataset = new XYSeriesCollection();
    //TimeSeriesCollection dataset = new TimeSeriesCollection();
    //DefaultCategoryDataset dataset = new DefaultCategoryDataset(); 

    NumberAxis BGAxis;
//    private TimeSeriesCollection BGDataset = new TimeSeriesCollection();
    //private DailyValues data = new DailyValues();
    DateAxis dateAxis;
    NumberAxis insBUAxis;
    //private TimeSeriesCollection insBUDataset = new TimeSeriesCollection();
    //private XYSeriesCollection insBUDataset = new XYSeriesCollection();
    
    

    
    
    /**
     * Constructor
     */
    public GraphViewRatios()
    {
        super();
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
     * Load Data
     */
    public void loadData()
    {
        if (this.data_coll==null)
            return;
        
        
        
/*        if (gluco_values==null)
        {
            this.gluco_values = new GlucoValues(this.gc, (GregorianCalendar)this.gc.clone(), true);
            
            System.out.println("Gluco Values: " + this.gluco_values.getDailyValuesRowsCount());
            
            GregorianCalendar gc_prev = (GregorianCalendar)this.gc.clone();
            gc_prev.add(GregorianCalendar.DAY_OF_YEAR, -1);
            this.gluco_values_prev = new GlucoValues(gc_prev, gc_prev, true);
        }
        */
    }

    
    /**
     * Get Data Set
     * 
     * @return AbstractDataset instance
     */
    public AbstractDataset getDataSet()
    {
        return this.dataset;
    }

    /**
     * Preprocess Data
     */
    public void preprocessData()
    {
        
        if (this.data_coll==null)
            return;
        
        dataset.removeAllSeries();
        
        XYSeries ratio_ch_ins = new XYSeries(this.m_ic.getMessage("CH_INS_RATIO"), true, true); //, Hour.class);
        XYSeries ratio_bg_ins = new XYSeries(this.m_ic.getMessage("BG_INS_RATIO"), true, true); //, Hour.class);
        XYSeries ratio_ch_bg = new XYSeries(this.m_ic.getMessage("CH_BG_RATIO"), true, true); //, Hour.class);
        
        ArrayList<RatioData> lst = this.data_coll.getRatiosCollection(BREData.BRE_DATA_BASAL_RATIO);
        
        for (int i = 0; i < lst.size(); i++)
        {
            RatioData rd = lst.get(i);
            
            long time = getTimeMs(rd.time_start);
            
            ratio_ch_ins.add(time, rd.ratio_ch_insulin);
            ratio_bg_ins.add(time, rd.ratio_bg_insulin);
            ratio_ch_bg.add(time, rd.ratio_ch_bg);
            
            time = getTimeMs(rd.time_end);

            ratio_ch_ins.add(time, rd.ratio_ch_insulin);
            ratio_bg_ins.add(time, rd.ratio_bg_insulin);
            ratio_ch_bg.add(time, rd.ratio_ch_bg);
        }
        
        
        dataset.addSeries(ratio_ch_ins);
        dataset.addSeries(ratio_bg_ins);
        dataset.addSeries(ratio_ch_bg); 
        
    }

    
    
    
    
    /**
     * Set Plot
     * 
     * @param chart JFreeChart instance
     */
    public void setPlot(JFreeChart chart)
    {

        
        XYPlot plot = chart.getXYPlot();
        //XYLineAndShapeRenderer defaultRenderer = (XYLineAndShapeRenderer) plot.getRenderer();
        //XYLineAndShapeRenderer insBURenderer = new XYLineAndShapeRenderer();
        dateAxis = (DateAxis) plot.getDomainAxis();
        BGAxis = (NumberAxis) plot.getRangeAxis();
        insBUAxis = new NumberAxis();
        
        //plot.s

        //ColorSchemeH colorScheme = graph_util.getColorScheme();

        //chart.setBackgroundPaint(graph_util.backgroundColor);
        
        /*
        RenderingHints rh = graph_util.getRenderingHints();
        
        if (rh!=null)
            chart.setRenderingHints(rh);
        
        chart.setBorderVisible(false);*/

        /*
        plot.setRangeAxis(1, insBUAxis);
        plot.setRangeAxisLocation(1, AxisLocation.BOTTOM_OR_RIGHT);
        plot.setDataset(1, insBUDataset);
        plot.mapDatasetToRangeAxis(1, 1);
        plot.setRenderer(1, insBURenderer); */
/*
        graph_util.applyMarkers(plot);
        plot.setRangeGridlinesVisible(false);
        plot.setDomainGridlinesVisible(false);

        defaultRenderer.setSeriesPaint(0, da_local.getColor(colorScheme.getColor_bg()));
        insBURenderer.setSeriesPaint(0, da_local.getColor(colorScheme.getColor_ch()));
        insBURenderer.setSeriesPaint(1, da_local.getColor(colorScheme.getColor_ins1()));
        insBURenderer.setSeriesPaint(2, da_local.getColor(colorScheme.getColor_ins2()));
*/        
        // fdfdd defaultRenderer.setSeriesShapesVisible(0, true);
        //insBURenderer.setSeriesShapesVisible(0, true);
        //insBURenderer.setSeriesShapesVisible(1, true);
        //insBURenderer.setSeriesShapesVisible(2, true);

// AX        dateAxis.setDateFormatOverride(new SimpleDateFormat(m_ic.getMessage("FORMAT_DATE_HOURS")));
// AX       dateAxis.setAutoRange(false);
/*        dateAxis.setRange(this.gluco_values.getRangeFrom().getTime(), 
            this.gluco_values.getRangeTo().getTime());
        dateAxis.setDefaultAutoRange(new DateRange(this.gluco_values.getRangeFrom().getTime(), 
            this.gluco_values.getRangeTo().getTime()));
  */      
        //BGAxis.setAutoRangeIncludesZero(true);

// ax        insBUAxis.setLabel(m_ic.getMessage("CH_LONG") + " / " + m_ic.getMessage("INSULIN"));
// ax        insBUAxis.setAutoRangeIncludesZero(true);
        
    }

    
    
    /**
     * Create Chart
     */
    public void createChart()
    {
        chart = ChartFactory.createTimeSeriesChart(null, 
            this.m_ic.getMessage("AXIS_TIME_LABEL"), 
            String.format(this.m_ic.getMessage("AXIS_VALUE_LABEL"), this.graph_util.getUnitLabel()), 
            this.dataset, 
            false, 
            true, 
            false);
        
        this.setPlot(chart);
    }



    
}