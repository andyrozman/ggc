package ggc.core.data.graph;

import ggc.core.data.DailyValuesRow;
import ggc.core.data.GlucoValues;
import ggc.core.data.PlotData;
import ggc.core.util.DataAccess;

import java.awt.Color;
import java.awt.Rectangle;
import java.util.GregorianCalendar;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.FastScatterPlot;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.general.AbstractDataset;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;

import com.atech.graphics.graphs.AbstractGraphView;

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
 *  Filename:     GraphViewHbA1c  
 *  Description:  GraphView implementation for HbA1c, used by new graph framework
 * 
 *  Author: andyrozman {andy@atech-software.com}  
 */


public class GraphViewSpread extends AbstractGraphView 
{

    DataAccess da_local = null;
    XYSeriesCollection dataset = new XYSeriesCollection();
    
    GlucoValues gv;
    PlotData plot_data_old = null;
    PlotData plot_data = new PlotData();
    
    /**
     * Constructor
     */
    public GraphViewSpread()
    {
        super(DataAccess.getInstance());
        da_local = DataAccess.getInstance();
    }
    

    /**
     * Get Help Id
     * 
     * @return
     */
    public String getHelpId()
    {
        return "pages.GGC_BG_HbA1c";
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

    
    /**
     * Get Viewer Dialog Bounds (used by GraphViewer)
     * 
     * @return Rectangle object
     */
    public Rectangle getViewerDialogBounds()
    {
        return new Rectangle(100,100,600,500);
    }


    /**
     * Load Data
     */
    public void loadData()
    {
        System.out.println("loadData");
        //if ((plot_data_old==null) || 
        //    (plot_data.getTimeRangeFrom()!=plot_data_old.getTimeRangeFrom()) ||
        //    (plot_data.getTimeRangeTo()!=plot_data_old.getTimeRangeTo()))
            //this.gv = new GlucoValues(this.plot_data.getTimeRangeFrom(), this.plot_data.getTimeRangeTo());
            
            System.out.println("From: " + this.plot_data.getTimeRangeFrom());
            System.out.println("To: " + this.plot_data.getTimeRangeTo());
            
            GregorianCalendar from, to;
            
            from = new GregorianCalendar();
            from.add(GregorianCalendar.MONTH, -3);
            
            to = new GregorianCalendar();
            to.add(GregorianCalendar.MONTH, -2);
            
            this.gv = new GlucoValues(from, to, true);
            
            System.out.println("Dv rows: " + this.gv.getDailyValuesRowsCount());
            //System.out.println("rows: " + this.gv.getRowCount());
            
            
            //this.gv = new GlucoValues(this.plot_data.getTimeRangeFrom(), this.plot_data.getTimeRangeTo());            
            
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
        System.out.println("preprocessData");
        //TimeSeries ts = new TimeSeries("BG Entries", Minute.class);

        XYSeries xs = new XYSeries("BG");
        
        System.out.println("preprocessData: " + this.gv.getDailyValuesRowsCount());
        
        int count = this.gv.getDailyValuesRowsCount();
        
        for(int i=0; i<count; i++)
        {
            DailyValuesRow dv = this.gv.getDailyValueRow(i);
            //ts.addOrUpdate(new Minute(dv.getDateTimeAsDate()), dv.getBG());
            
            if (dv.getBG()>0)
                xs.add(dv.getDateT(), dv.getBG());
        }
        dataset.addSeries(xs);

        xs = new XYSeries("CH");
        
        for(int i=0; i<count; i++)
        {
            DailyValuesRow dv = this.gv.getDailyValueRow(i);
            //ts.addOrUpdate(new Minute(dv.getDateTimeAsDate()), dv.getBG());
            if (dv.getCH()>0)
                xs.add(dv.getDateT(), dv.getCH());
            
        }

        //org.jfree.data.time.
        dataset.addSeries(xs);
        
        /*
        dataset.clear();

        System.out.println("Read HbA1c data:\n" + hbValues.getPercentOfDaysInClass(0) + "\n" + hbValues.getPercentOfDaysInClass(1) + "\n"
                + hbValues.getPercentOfDaysInClass(2) + "\n" + hbValues.getPercentOfDaysInClass(3) + "\n"
                + hbValues.getPercentOfDaysInClass(4));
        dataset.insertValue(0, m_ic.getMessage("DAYS_WITH_READINGS_0_1"), hbValues.getPercentOfDaysInClass(0));
        dataset.insertValue(1, m_ic.getMessage("DAYS_WITH_READINGS_2_3"), hbValues.getPercentOfDaysInClass(1));
        dataset.insertValue(2, m_ic.getMessage("DAYS_WITH_READINGS_4_5"), hbValues.getPercentOfDaysInClass(2));
        dataset.insertValue(3, m_ic.getMessage("DAYS_WITH_READINGS_6_7"), hbValues.getPercentOfDaysInClass(3));
        dataset.insertValue(4, m_ic.getMessage("DAYS_WITH_READINGS_MORE_7"), hbValues.getPercentOfDaysInClass(4));
        */
    }

    /**
     * Set Plot
     * 
     * @param chart JFreeChart instance
     */
    public void setPlot(JFreeChart chart)
    {
        //FastScatterPlot plot = (FastScatterPlot) chart.getPlot();

        //chart.setRenderingHints(renderingHints);

//        plot.setBackgroundPaint(Color.white); //backgroundColor);
        //plot.setCircular(true);
        //plot.setBackgroundAlpha(0.5f);
//        plot.setForegroundAlpha(0.5f);
        
        
        //plot.s
        //plot.setSectionPaint(m_ic.getMessage("DAYS_WITH_READINGS_0_1"), Color.RED);
        //plot.setSectionPaint(m_ic.getMessage("DAYS_WITH_READINGS_2_3"), Color.BLUE);
        //plot.setSectionPaint(m_ic.getMessage("DAYS_WITH_READINGS_4_5"), Color.YELLOW);
        //plot.setSectionPaint(m_ic.getMessage("DAYS_WITH_READINGS_6_7"), Color.GREEN);
        //plot.setSectionPaint(m_ic.getMessage("DAYS_WITH_READINGS_MORE_7"), Color.MAGENTA);
        
    }

    /**
     * Create Chart
     */
    public void createChart()
    {
        this.chart = ChartFactory.createScatterPlot(null, "xAxisLabel", "yAxisLabel", dataset, PlotOrientation.VERTICAL, true, false, false);
    }


    /**
     * Create Chart Panel
     */
    public void createChartPanel()
    {
        this.chart_panel = new ChartPanel(this.getChart(), true, true, true, true, false);
    }

 
    
}
