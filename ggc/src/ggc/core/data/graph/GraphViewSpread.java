package ggc.core.data.graph;

import ggc.core.data.DailyValuesRow;
import ggc.core.data.GlucoValues;
import ggc.core.util.DataAccess;

import java.awt.Rectangle;
import java.text.SimpleDateFormat;
import java.util.GregorianCalendar;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.DateAxis;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.XYPlot;
import org.jfree.data.general.AbstractDataset;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;

import com.atech.graphics.graphs.AbstractGraphViewAndProcessor;
import com.atech.graphics.graphs.GraphViewControlerInterface;
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
 *  Filename:     GraphViewHbA1c  
 *  Description:  GraphView implementation for HbA1c, used by new graph framework
 * 
 *  Author: andyrozman {andy@atech-software.com}  
 */


public class GraphViewSpread extends AbstractGraphViewAndProcessor 
{

    DataAccess da_local = null;
    XYSeriesCollection dataset = new XYSeriesCollection();
    
    GlucoValues gv;
    PlotSelectorData plot_data_old = null;
    PlotSelectorData plot_data = null; //new PlotData();
    GGCGraphViewControler controler = null;
    //PlotData plot_data = null;
    DateAxis dateAxis;
    
    int time_range = 0;
    GregorianCalendar gc_from, gc_to;
    
    GregorianCalendar dt_from = null;
    GregorianCalendar dt_to = null;
    
    /**
     * Constructor
     */
    public GraphViewSpread()
    {
        super(DataAccess.getInstance());
        da_local = DataAccess.getInstance();
        plot_data = new PlotSelectorData();
        plot_data.setPlotBG(true);
        //plot_data.setTimeRangeType(PlotData.TIME_RANGE_1_MONTH);
        
        //GregorianCalendar gc = new GregorianCalendar();
        //gc.add(GregorianCalendar.MONTH, -1);
        
        //plot_data.setTimeRangeFrom(gc);
        //plot_data.setTimeRangeFrom(new GregorianCalendar());
        
        this.dt_from = new GregorianCalendar(2008, 1, 1, 0, 0);
        this.dt_to = new GregorianCalendar(2008, 1, 1, 23, 59);
        
        //Date d = new Date(2008,)
        
        this.controler = new GGCGraphViewControler(this, GGCGraphViewControler.GRAPH_SPREAD);
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
        return m_ic.getMessage("SPREAD_GRAPH");
    }

    
    /**
     * Get Viewer Dialog Bounds (used by GraphViewer)
     * 
     * @return Rectangle object
     */
    public Rectangle getViewerDialogBounds()
    {
        return new Rectangle(100,100,750,500);
    }

    
    
    /**
     * Set Controller Data
     * 
     * @see com.atech.graphics.graphs.AbstractGraphViewAndProcessor#setControllerData(java.lang.Object)
     */
    public void setControllerData(Object obj)
    {
        plot_data = (PlotSelectorData)obj;
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
            
//            System.out.println("From: " + this.plot_data.getTimeRangeFrom());
//            System.out.println("To: " + this.plot_data.getTimeRangeTo());
            
        boolean changed = false;
        
        if ((gc_from==null) || (!m_da.compareGregorianCalendars(gc_from, this.plot_data.getDateRangeData().getRangeFrom())))
        {
            gc_from = this.plot_data.getDateRangeData().getRangeFrom();
            changed = true;
        }
        
        if ((gc_to==null) || (!m_da.compareGregorianCalendars(gc_to, this.plot_data.getDateRangeData().getRangeTo())))
        {
            gc_to = this.plot_data.getDateRangeData().getRangeTo();
            changed = true;
        }

        if (changed)
        {
            this.gv = new GlucoValues(gc_from, gc_to, true);
            System.out.println("Reread data [rows=" + this.gv.getDailyValuesRowsCount());
        }
        
        
        /*
        GregorianCalendar from, to;
            
        from = new GregorianCalendar();
        from.add(GregorianCalendar.MONTH, -3);
            
        to = new GregorianCalendar();
        to.add(GregorianCalendar.MONTH, -2); */
            
            
        //System.out.println("Dv rows: " + this.gv.getDailyValuesRowsCount());
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
            
            System.out.println("DateT: " + dv.getDateT());
            
            if (this.plot_data.isPlotBG())
            {
                if (dv.getBG()>0)
                    xs.add(getFakeDate(dv.getDateTimeAsATDate()).getTimeInMillis(), dv.getBG());
            }
            
        }

        dataset.addSeries(xs);

        /*
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
        */
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

    private GregorianCalendar getFakeDate(ATechDate date)
    {
        return new GregorianCalendar(2008, 1, 1, date.hour_of_day, date.minute);
    }
    
    
    /**
     * Set Plot
     * 
     * @param chart JFreeChart instance
     */
    public void setPlot(JFreeChart chart)
    {
        //XYPlot plot = (XYPlot)chart.getPlot();
        XYPlot plot = chart.getXYPlot();
        DateAxis dateAxis = (DateAxis) plot.getDomainAxis();

        dateAxis.setDateFormatOverride(new SimpleDateFormat(m_ic.getMessage("FORMAT_DATE_HOURS")));
        dateAxis.setAutoRange(false);
        dateAxis.setRange(this.dt_from.getTime(), this.dt_to.getTime());
        //dateAxis.setTimeline(new Timeline());
        
        dateAxis.setLabel(m_ic.getMessage("AXIS_TIME_LABEL"));
        
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
        this.chart = ChartFactory.createScatterPlot(null, m_ic.getMessage("AXIS_TIME_LABEL"), "yAxisLabel", dataset, PlotOrientation.VERTICAL, false, false, false);
        XYPlot plot = this.chart.getXYPlot();
        plot.setDomainAxis(new DateAxis());
    }


    /**
     * Create Chart Panel
     */
    public void createChartPanel()
    {
        this.chart_panel = new ChartPanel(this.getChart(), true, true, true, true, false);
    }

 
    
    /**
     * Get Controler Interface instance
     * 
     * @return GraphViewControlerInterface instance or null
     */
    public GraphViewControlerInterface getControler()
    {
        return this.controler;
    }
    
    
}
