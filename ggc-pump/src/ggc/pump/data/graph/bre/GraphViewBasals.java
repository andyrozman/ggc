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


public class GraphViewBasals extends BREGraphsAbstract //implements GraphViewInterface, GraphViewDataProcessorInterface 
{

    GregorianCalendar gc;
//    private GlucoValues gluco_values;
    @SuppressWarnings("unused")
//    private GlucoValues gluco_values_prev;
    XYSeriesCollection dataset = new XYSeriesCollection();
    //TimeSeriesCollection dataset = new TimeSeriesCollection();
    //DefaultCategoryDataset dataset = new DefaultCategoryDataset(); 

    NumberAxis BGAxis;
//    private TimeSeriesCollection BGDataset = new TimeSeriesCollection();
    //private DailyValues data = new DailyValues();
    DateAxis dateAxis;
    NumberAxis insBUAxis;
    //private TimeSeriesCollection insBUDataset = new TimeSeriesCollection();
    private XYSeriesCollection insBUDataset = new XYSeriesCollection();
    
    

    
    
    /**
     * Constructor
     * 
     * @param gc 
     */
    public GraphViewBasals()
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
     * Get Viewer Dialog Bounds (used by GraphViewer)
     * 
     * @return Rectangle object
     */
    public Rectangle getViewerDialogBounds()
    {
        return new Rectangle(100,100,500,400);
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
        
        /*
        //XYSeries xs = new XYSeries("BG");
        TimeSeries ts = new TimeSeries("BG", Minute.class);
        
        //System.out.println("preprocessData: " + this.gv.getDailyValuesRowsCount());
        
        int count = this.gluco_values.getDailyValuesRowsCount();
        
        for(int i=0; i<count; i++)
        {
            DailyValuesRow dv = this.gluco_values.getDailyValueRow(i);
            //ts.addOrUpdate(new Minute(dv.getDateTimeAsDate()), dv.getBG());
            
            if (dv.getBG()>0)
                ts.addOrUpdate(new Minute(dv.getDateTimeAsDate()), dv.getBG());
        }
        dataset.addSeries(ts);

        ts = new TimeSeries("CH");
        
        for(int i=0; i<count; i++)
        {
            DailyValuesRow dv = this.gluco_values.getDailyValueRow(i);
            //ts.addOrUpdate(new Minute(dv.getDateTimeAsDate()), dv.getBG());
            if (dv.getCH()>0)
                ts.addOrUpdate(new Minute(dv.getDateTimeAsDate()), dv.getCH());
//                xs.add(dv.getDateT(), dv.getCH());
            
        }

        //org.jfree.data.time.
        dataset.addSeries(ts);
        
        
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
        
        dataset.removeAllSeries();
        insBUDataset.removeAllSeries();
        
        /*
        TimeSeries BGSeries = new TimeSeries(this.m_ic.getMessage("BLOOD_GLUCOSE"), Hour.class);
        TimeSeries CHSeries = new TimeSeries(this.m_ic.getMessage("CH_LONG"), Hour.class);
        TimeSeries ins1Series = new TimeSeries(da_local.getSettings().getIns1Name(), Hour.class);
        TimeSeries ins2Series = new TimeSeries(da_local.getSettings().getIns2Name(), Hour.class);
*/

        XYSeries bg = new XYSeries(this.m_ic.getMessage("MEASURED_BASAL_BG"), true, true); //, Hour.class);
        
        XYSeries CHSeries = new XYSeries(this.m_ic.getMessage("CH_LONG"), true, true); //, Hour.class);
//        XYSeries ins1Series = new XYSeries(da_local.getSettings().getIns1Name(), true, true); //, Hour.class);
//        XYSeries ins2Series = new XYSeries(da_local.getSettings().getIns2Name(), true, true); //, Hour.class);
        
        
        int BGUnit = 1; // AAA da_local.getSettings().getBG_unit();
        
        ArrayList<BREData> lst = this.data_coll.getDataByType(BREData.BRE_DATA_BG);  
        
        for (int i = 0; i < lst.size(); i++)
        {
            //DailyValuesRow row = this.gluco_values.getDailyValueRow(i);
            //Hour time = new Hour(row.getDateTimeAsDate());
            //int time = row.getDateT();
            
            BREData row = lst.get(i); 
            
            long time = getTimeMs(row.time);

            bg.add(time, row.value);
        }
        
        
        dataset.addSeries(bg);

            //System.out.println(row.getDateTimeAsDate());
            
            //if (row.getBG(BGUnit) > 0)
            //{
                //BGSeries.getD
                
/*                
                
                if (BGSeries..getDataItem(time) == null)
                {
                    BGSeries.add(time, row.getBG(BGUnit));
                }
                else
                {
                    BGSeries.addOrUpdate(time, MathUtils.getAverage(row.getBG(BGUnit), BGSeries.getDataItem(time).getY()));
                } */
  /*          }
            
            if (row.getCH() > 0)
            {
                CHSeries.add(time, row.getCH());
/*
                if (CHSeries.getDataItem(time) == null)
                {
                    CHSeries.add(time, row.getCH());
                }
                else
                {
                    CHSeries.addOrUpdate(time, MathUtils.getAverage(row.getCH(), CHSeries.getDataItem(time).getYValue()));
                }
                */
/*            }
            if (row.getIns1() > 0)
            {
                ins1Series.add(time, row.getIns1());
/*                if (ins1Series.getDataItem(time) == null)
                {
                    ins1Series.add(time, row.getIns1());
                }
                else
                {
                    ins1Series.addOrUpdate(time, MathUtils.getAverage(row.getIns1(), ins1Series.getDataItem(time)
                            .getYValue()));
                } */
/*            }
            
            if (row.getIns2() > 0)
            {
                ins2Series.add(time, row.getIns2());
/*
                if (ins2Series.getDataItem(time) == null)
                {
                    ins2Series.add(time, row.getIns2());
                }
                else
                {
                    ins2Series.addOrUpdate(time, MathUtils.getAverage(row.getIns2(), ins2Series.getDataItem(time)
                            .getYValue()));
                } */
/*            }
        }

        dataset.addSeries(BGSeries);
        insBUDataset.addSeries(CHSeries);
        insBUDataset.addSeries(ins1Series);
        insBUDataset.addSeries(ins2Series);
  */      
        
        
    }


    GregorianCalendar gcx = new GregorianCalendar();
    
    public long getTimeMs(int time)
    {
        ATechDate atd = new ATechDate(ATechDate.FORMAT_TIME_ONLY_MIN, time);
        
        gcx.set(GregorianCalendar.HOUR_OF_DAY, atd.hour_of_day);
        gcx.set(GregorianCalendar.MINUTE, atd.minute);
        
        return gcx.getTimeInMillis();
    }
    
    
    /**
     * Get Title (used by GraphViewer)
     * 
     * @return title as string 
     */
    public String getTitle()
    {
        return m_ic.getMessage("DAILYGRAPHFRAME");
    }
    
    
    /**
     * Set Plot
     * 
     * @param chart JFreeChart instance
     */
    public void setPlot(JFreeChart chart)
    {

        
        XYPlot plot = chart.getXYPlot();
        XYLineAndShapeRenderer defaultRenderer = (XYLineAndShapeRenderer) plot.getRenderer();
        XYLineAndShapeRenderer insBURenderer = new XYLineAndShapeRenderer();
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
        defaultRenderer.setSeriesShapesVisible(0, true);
        insBURenderer.setSeriesShapesVisible(0, true);
        insBURenderer.setSeriesShapesVisible(1, true);
        insBURenderer.setSeriesShapesVisible(2, true);

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

    
    public void setData(BREDataCollection data_coll)
    {
        this.data_coll = data_coll;
        this.loadData();
        this.preprocessData();
        
        this.setPlot(chart);
    }
    
    
    /**
     * Create Chart
     */
    public void createChart()
    {
        chart = ChartFactory.createTimeSeriesChart(null, 
            this.m_ic.getMessage("AXIS_TIME_LABEL"), 
            String.format(this.m_ic.getMessage("AXIS_VALUE_LABEL"), this.graph_util.getUnitLabel()), 
            dataset, 
            true, 
            true, 
            false);
        
        this.setPlot(chart);
    }


    /**
     * Create Chart Panel
     */
    public void createChartPanel()
    {
        this.chart_panel = new ChartPanel(getChart(), true, true, true, true, false);
    }

    
}