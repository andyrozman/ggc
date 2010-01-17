package ggc.core.data.graph;

import ggc.core.data.DailyValuesRow;
import ggc.core.data.GlucoValues;
import ggc.core.util.DataAccess;
import ggc.core.util.GGCProperties;
import ggc.core.util.MathUtils;

import java.awt.Rectangle;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.AxisLocation;
import org.jfree.chart.axis.DateAxis;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.axis.ValueAxis;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer;
import org.jfree.data.general.AbstractDataset;
import org.jfree.data.time.Day;
import org.jfree.data.time.TimeSeries;
import org.jfree.data.time.TimeSeriesCollection;

import com.atech.graphics.graphs.AbstractGraphViewAndProcessor;
import com.atech.graphics.graphs.GraphViewControlerInterface;
import com.atech.graphics.graphs.GraphViewDataProcessorInterface;
import com.atech.i18n.I18nControlAbstract;

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
 *  Filename:     GraphViewCourse  
 *  Description:  GraphView implementation for Course, uses ATech Graph Framework
 * 
 *  Author: andyrozman {andy@atech-software.com}  
 */


public class GraphViewCourse extends AbstractGraphViewAndProcessor 
{

    DataAccess da_local = null;
    I18nControlAbstract  m_ic = null;
    GlucoValues gv;
    
    //PlotSelectorData plot_data_old = null;
    PlotSelectorData plot_data = null; //new PlotData();
    GGCGraphViewControler controler = null;
    GGCProperties settings = null;
    
    ValueAxis va = null;
    XYLineAndShapeRenderer renderer;
    int BGUnit = DataAccess.BG_MGDL;
    
    
    
    GGCGraphUtil graph_util;
    GregorianCalendar gc_from, gc_to;

    private TimeSeriesCollection BGDataset = new TimeSeriesCollection();
    private TimeSeriesCollection readingsDataset = new TimeSeriesCollection();
    private TimeSeriesCollection sumDataset = new TimeSeriesCollection();
    private TimeSeriesCollection averageDataset = new TimeSeriesCollection();
    
    
    /**
     * Constructor
     */
    public GraphViewCourse()
    {
        super(DataAccess.getInstance());
        da_local = DataAccess.getInstance();
        m_ic = da_local.getI18nControlInstance();
        settings = da_local.getSettings();
        BGUnit = settings.getBG_unit();
        
        plot_data = new PlotSelectorData();
        graph_util = GGCGraphUtil.getInstance(da_local);
        
        this.controler = new GGCGraphViewControler(this, GGCGraphViewControler.GRAPH_COURSE);
        
    }
    

    /**
     * Get Help Id
     * 
     * @return
     */
    public String getHelpId()
    {
        return "GGC_BG_Graph_Course";
    }

    /**
     * Get Title (used by GraphViewer)
     * 
     * @return title as string 
     */
    public String getTitle()
    {
        return m_ic.getMessage("COURSE_GRAPH") + " [" + m_ic.getMessage("NOT_TESTED_100PRO") + "]";
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
     * Get Processor
     * 
     * @return GraphViewDataProcessorInterface instance (typed)
     */
    public GraphViewDataProcessorInterface getProcessor()
    {
        return this;
    }
    
    
    /**
     * Load Data
     */
    public void loadData()
    {

        boolean changed = false;
        
        if ((gc_from==null) || (!m_da.compareGregorianCalendars(DataAccess.GC_COMPARE_DAY, gc_from, this.plot_data.getDateRangeData().getRangeFrom())))
        {
            gc_from = this.plot_data.getDateRangeData().getRangeFrom();
            changed = true;
        }
        
        if ((gc_to==null) || (!m_da.compareGregorianCalendars(DataAccess.GC_COMPARE_DAY, gc_to, this.plot_data.getDateRangeData().getRangeTo())))
        {
            gc_to = this.plot_data.getDateRangeData().getRangeTo();
            changed = true;
        }

        if (changed)
        {
            System.out.println(gc_from.get(Calendar.DAY_OF_MONTH ) + "."+ gc_from.get(Calendar.MONTH) + "."+ gc_from.get(Calendar.YEAR) + " "+ gc_from.get(Calendar.HOUR_OF_DAY ) + ":" + gc_from.get(Calendar.MINUTE ));
            this.gv = new GlucoValues(gc_from, gc_to, true);
            System.out.println("Reread data [rows=" + this.gv.getDailyValuesRowsCount() + "]");
        }
        
    }

    
    /**
     * Get Data Set
     * 
     * @return AbstractDataset instance
     */
    public AbstractDataset getDataSet()
    {
        return this.BGDataset;
    }

    /**
     * Preprocess Data
     */
    public void preprocessData()
    {
        int count = this.gv.getDailyValuesRowsCount();

        System.out.println("preprocessData: " + count);
        
        DailyValuesRow row;
        Day time;

        BGDataset.removeAllSeries();
        averageDataset.removeAllSeries();
        sumDataset.removeAllSeries();
        readingsDataset.removeAllSeries();

        TimeSeries BGAvgSeries = new TimeSeries(this.m_ic.getMessage("AVG_BG_PER_DAY"), Day.class);
        TimeSeries BGReadingsSeries = new TimeSeries(this.m_ic.getMessage("READINGS"), Day.class);
        TimeSeries CHAvgSeries = new TimeSeries(this.m_ic.getMessage("AVG_MEAL_SIZE"), Day.class);
        TimeSeries CHSumSeries = new TimeSeries(this.m_ic.getMessage("SUM_BU"), Day.class);
        TimeSeries ins1AvgSeries = new TimeSeries(this.m_ic.getMessage("AVG") + " Ins1", Day.class);
        TimeSeries ins1SumSeries = new TimeSeries(this.m_ic.getMessage("SUM") + " Ins1", Day.class);
        TimeSeries ins2AvgSeries = new TimeSeries(this.m_ic.getMessage("AVG") + " Ins2", Day.class);
        TimeSeries ins2SumSeries = new TimeSeries(this.m_ic.getMessage("SUM") + " Ins2", Day.class);
        TimeSeries insAvgSeries = new TimeSeries(this.m_ic.getMessage("AVG_INS"), Day.class);
        TimeSeries insSumSeries = new TimeSeries(this.m_ic.getMessage("SUM_INSULIN"), Day.class);
        TimeSeries insPerCHSeries = new TimeSeries(this.m_ic.getMessage("INS_SLASH_BU"), Day.class);
        TimeSeries mealsSeries = new TimeSeries(this.m_ic.getMessage("MEALS"), Day.class);

        //int days = data.getDailyValuesItemsCount();
        
        
        for (int i = 0; i < this.gv.getRowCount(); i++)
        {
            row = this.gv.getDailyValueRow(i);
            time = new Day(row.getDateTimeAsDate());

            if (row.getBG(BGUnit) > 0)
            {
                if (BGAvgSeries.getDataItem(time) == null)
                {
                    BGAvgSeries.add(time, row.getBG(BGUnit));
                }
                else
                {
                    BGAvgSeries.addOrUpdate(time, MathUtils.getAverage(row.getBG(BGUnit), BGAvgSeries.getDataItem(time).getValue()));
                }
                
                
                if (BGReadingsSeries.getDataItem(time) == null)
                {
                    BGReadingsSeries.add(time, 1);
                }
                else
                {
                    BGReadingsSeries.addOrUpdate(time, MathUtils.add(1, BGReadingsSeries.getDataItem(time)
                            .getValue()));
                }
            }

            if (row.getCH() > 0)
            {
                //System.out.println(".");
                if (CHAvgSeries.getDataItem(time) == null)
                {
                    CHAvgSeries.add(time, row.getCH());
                }
                else
                {
                    CHAvgSeries.addOrUpdate(time, MathUtils.getAverage(row.getCH(), CHAvgSeries.getDataItem(time)
                            .getValue()));
                }
            }
            if (row.getCH() > 0)
            {
                if (CHSumSeries.getDataItem(time) == null)
                {
                    CHSumSeries.add(time, row.getCH());
                }
                else
                {
                    CHSumSeries.addOrUpdate(time, MathUtils.add(row.getCH(), CHSumSeries.getDataItem(time)
                            .getValue()));
                }
            }

            if (row.getIns1() > 0)
            {
                // ins 1 avg
                if (ins1AvgSeries.getDataItem(time) == null)
                {
                    ins1AvgSeries.add(time, row.getIns1());
                }
                else
                {
                    ins1AvgSeries.addOrUpdate(time, MathUtils.getAverage(row.getIns1(), ins1AvgSeries.getDataItem(
                        time).getValue()));
                }

                // ins 1 sum
                if (ins1SumSeries.getDataItem(time) == null)
                {
                    ins1SumSeries.add(time, row.getIns1());
                }
                else
                {
                    float sum = ((Number)ins1SumSeries.getDataItem(time).getValue()).floatValue();
                    sum += row.getIns1();
                        
                    ins1SumSeries.addOrUpdate(time, sum); 
                }
                
                
            }
            

            if (row.getIns2() > 0)
            {
                
                // ins 2 avg
                if (ins2AvgSeries.getDataItem(time) == null)
                {
                    ins2AvgSeries.add(time, row.getIns2());
                }
                else
                {
                    ins2AvgSeries.addOrUpdate(time, MathUtils.getAverage(row.getIns2(), ins2AvgSeries.getDataItem(
                        time).getValue()));
                }
                
                // ins 2 sum
                if (ins2SumSeries.getDataItem(time) == null)
                {
                    ins2SumSeries.add(time, row.getIns2());
                }
                else
                {
                    ins2SumSeries.addOrUpdate(time, MathUtils.add(row.getIns2(), ins2SumSeries.getDataItem(time)
                            .getValue()));
                }
                
            }

            if ((row.getIns1() > 0) || (row.getIns2() > 0))
            {
                // ins avg
                if (insAvgSeries.getDataItem(time) == null)
                {
                    insAvgSeries.add(time, row.getIns1() + row.getIns2());
                }
                else
                {
                    insAvgSeries.addOrUpdate(time, MathUtils.getAverage(row.getIns1() + row.getIns2(), insAvgSeries
                            .getDataItem(time).getValue()));
                }

                // ins sum
                if (insSumSeries.getDataItem(time) == null)
                {
                    insSumSeries.add(time, row.getIns1() + row.getIns2());
                }
                else
                {
                    insSumSeries.addOrUpdate(time, MathUtils.add(row.getIns1() + row.getIns2(), insSumSeries
                            .getDataItem(time).getValue()));
                }
            }

            // TODO check 
            if ((CHSumSeries.getDataItem(time) != null)
                    && (CHSumSeries.getDataItem(time).getValue().doubleValue() > 0))
            {

                double a=0.0f,b=0.0f;
                
                if (insSumSeries.getDataItem(time)!=null)
                {
                    a = insSumSeries.getDataItem(time).getValue().doubleValue();
                }
                    
                if (CHSumSeries.getDataItem(time)!=null)
                {
                    b = CHSumSeries.getDataItem(time).getValue().doubleValue();
                }
                
                if ((a!=0.0d) && (b!=0.0d))
                {
                    double ins_ch = a/b;
                    insPerCHSeries.addOrUpdate(time, ins_ch);
                }
            }
            if (row.getCH() > 0)
            {
                if (mealsSeries.getDataItem(time) == null)
                {
                    mealsSeries.add(time, 1);
                }
                else
                {
                    mealsSeries.addOrUpdate(time, MathUtils.add(1, mealsSeries.getDataItem(time).getValue()));
                }
            }
        }

        
        if (this.plot_data.isPlotBGDayAvg())
            BGDataset.addSeries(BGAvgSeries);
//            BGAvgSeries = new TimeSeries(this.m_ic.getMessage("AVG_BG_PER_DAY"), Day.class);
        
        if (this.plot_data.isPlotBGReadings())
            readingsDataset.addSeries(BGReadingsSeries);
//            BGReadingsSeries = new TimeSeries(this.m_ic.getMessage("READINGS"), Day.class);

        if (this.plot_data.isPlotCHDayAvg())
            averageDataset.addSeries(CHAvgSeries);
//            CHAvgSeries = new TimeSeries(this.m_ic.getMessage("AVG_MEAL_SIZE"), Day.class);
        
        if (this.plot_data.isPlotCHSum())
            sumDataset.addSeries(CHSumSeries);
//            CHSumSeries = new TimeSeries(this.m_ic.getMessage("SUM_BU"), Day.class);
        
        if (this.plot_data.isPlotIns1DayAvg())
            averageDataset.addSeries(ins1AvgSeries);
//            ins1AvgSeries = new TimeSeries(this.m_ic.getMessage("AVG") + " " + settings.getIns1Name(), Day.class);
        
        if (this.plot_data.isPlotIns1Sum())
            sumDataset.addSeries(ins1SumSeries);
//            ins1SumSeries = new TimeSeries(this.m_ic.getMessage("SUM") + " " + settings.getIns1Name(), Day.class);
        
        if (this.plot_data.isPlotIns2DayAvg())
            averageDataset.addSeries(ins2AvgSeries);
//            ins2AvgSeries = new TimeSeries(this.m_ic.getMessage("AVG") + " " + settings.getIns2Name(), Day.class);

        if (this.plot_data.isPlotIns2Sum())
            sumDataset.addSeries(ins2SumSeries);
//            ins2SumSeries = new TimeSeries(this.m_ic.getMessage("SUM") + " " + settings.getIns2Name(), Day.class);
  
        if (this.plot_data.isPlotInsTotalDayAvg())
            averageDataset.addSeries(insAvgSeries);
//            insAvgSeries = new TimeSeries(this.m_ic.getMessage("AVG_INS"), Day.class);

        if (this.plot_data.isPlotInsTotalSum())
            sumDataset.addSeries(insSumSeries);
//            insSumSeries = new TimeSeries(this.m_ic.getMessage("SUM_INSULIN"), Day.class);
        
        if (this.plot_data.isPlotInsPerCH())
            averageDataset.addSeries(insPerCHSeries);
//            insPerCHSeries = new TimeSeries(this.m_ic.getMessage("INS_SLASH_BU"), Day.class);

        if (this.plot_data.isPlotMeals())
            readingsDataset.addSeries(mealsSeries);
//            mealsSeries = new TimeSeries(this.m_ic.getMessage("MEALS"), Day.class);

//        BGDataset.addSeries(BGAvgSeries);
//        sumDataset.addSeries(CHSumSeries);
//        sumDataset.addSeries(ins1SumSeries);
//        sumDataset.addSeries(ins2SumSeries);
//        sumDataset.addSeries(insSumSeries);
//        averageDataset.addSeries(CHAvgSeries);
//        averageDataset.addSeries(ins1AvgSeries);
//        averageDataset.addSeries(ins2AvgSeries);
//        averageDataset.addSeries(insAvgSeries);
//        averageDataset.addSeries(insPerCHSeries);
//        readingsDataset.addSeries(BGReadingsSeries);
//        readingsDataset.addSeries(mealsSeries);
        
        setPlot(chart);
        
        
        
    }

    /**
     * Set Plot
     * 
     * @param chart JFreeChart instance
     */
    public void setPlot(JFreeChart chart)
    {
        
        if (chart==null)
            return;

       
        XYPlot plot = chart.getXYPlot();
        XYLineAndShapeRenderer defaultRenderer = (XYLineAndShapeRenderer) plot.getRenderer();
        XYLineAndShapeRenderer averageRenderer = new XYLineAndShapeRenderer();
        XYLineAndShapeRenderer sumRenderer = new XYLineAndShapeRenderer();
        XYLineAndShapeRenderer readingsRenderer = new XYLineAndShapeRenderer();
        NumberAxis BGAxis = (NumberAxis) plot.getRangeAxis();
        NumberAxis averageAxis = new NumberAxis();
        NumberAxis sumAxis = new NumberAxis();
        NumberAxis readingsAxis = new NumberAxis();
        DateAxis dateAxis = (DateAxis) plot.getDomainAxis();

        if (plot_data.isPlotCHDayAvg() || plot_data.isPlotIns1DayAvg() || plot_data.isPlotIns2DayAvg()
                || plot_data.isPlotInsTotalDayAvg() || plot_data.isPlotInsPerCH())
        {
            plot.setRangeAxis(1, averageAxis);
            plot.setRangeAxisLocation(1, AxisLocation.BOTTOM_OR_RIGHT);
            plot.setDataset(1, averageDataset);
            plot.mapDatasetToRangeAxis(1, 1);
            plot.setRenderer(1, averageRenderer);
        }

        if (plot_data.isPlotCHSum() || plot_data.isPlotIns1Sum() || plot_data.isPlotIns2Sum()
                || plot_data.isPlotInsTotalSum())
        {
            plot.setRangeAxis(2, sumAxis);
            plot.setRangeAxisLocation(2, AxisLocation.BOTTOM_OR_RIGHT);
            plot.setDataset(2, sumDataset);
            plot.mapDatasetToRangeAxis(2, 2);
            plot.setRenderer(2, sumRenderer);
        }

        if (plot_data.isPlotMeals() || plot_data.isPlotBGReadings())
        {
            plot.setRangeAxis(3, readingsAxis);
            plot.setRangeAxisLocation(3, AxisLocation.BOTTOM_OR_LEFT);
            plot.setDataset(3, readingsDataset);
            plot.mapDatasetToRangeAxis(3, 3);
            plot.setRenderer(3, readingsRenderer);
        }

        //graph_util.applyMarkers(plot);
        //plot.setRangeGridlinesVisible(false);
        //plot.setDomainGridlinesVisible(false);

        plot.setRangeGridlinesVisible(true);
        plot.setDomainGridlinesVisible(true);
        
        
        dateAxis.setDateFormatOverride(new SimpleDateFormat(m_ic.getMessage("FORMAT_DATE_DAYS")));

        defaultRenderer.setSeriesPaint(0, this.da_local.getColor(settings.getSelectedColorScheme().getColor_bg_avg()));

        averageRenderer.setSeriesPaint(0, this.da_local.getColor(settings.getSelectedColorScheme().getColor_ch())
                .darker());
        averageRenderer.setSeriesPaint(1, this.da_local.getColor(settings.getSelectedColorScheme().getColor_ins1())
                .darker());
        averageRenderer.setSeriesPaint(2, this.da_local.getColor(settings.getSelectedColorScheme().getColor_ins2())
                .darker());
        averageRenderer.setSeriesPaint(3, this.da_local.getColor(settings.getSelectedColorScheme().getColor_ins())
                .darker());
        averageRenderer.setSeriesPaint(4, this.da_local.getColor(settings.getSelectedColorScheme()
                .getColor_ins_perbu()));

        sumRenderer.setSeriesPaint(0, this.da_local.getColor(settings.getSelectedColorScheme().getColor_ch()));
        sumRenderer.setSeriesPaint(1, this.da_local.getColor(settings.getSelectedColorScheme().getColor_ins1()));
        sumRenderer.setSeriesPaint(2, this.da_local.getColor(settings.getSelectedColorScheme().getColor_ins2()));
        sumRenderer.setSeriesPaint(3, this.da_local.getColor(settings.getSelectedColorScheme().getColor_ins()));

        readingsRenderer.setSeriesPaint(0, this.da_local.getColor(settings.getSelectedColorScheme().getColor_bg()));
        readingsRenderer.setSeriesPaint(1, this.da_local.getColor(settings.getSelectedColorScheme().getColor_ch())
                .brighter());

        //chart.setBackgroundPaint(backgroundColor);
//        chart.setRenderingHints(renderingHints);
        chart.setBorderVisible(false);

        BGAxis.setAutoRangeIncludesZero(true);
        
        plot.setNoDataMessage(this.m_ic.getMessage("GRAPH_NO_DATA_AVAILABLE_CHANGE"));
        
        
    }

    /**
     * Create Chart
     */
    public void createChart()
    {
        chart = ChartFactory.createTimeSeriesChart(null, m_ic.getMessage("AXIS_TIME_LABEL"), String.format(
            m_ic.getMessage("AXIS_VALUE_LABEL"), da_local.getBGMeasurmentTypeString()), BGDataset, true, true, false);        
    }


    /**
     * Create Chart Panel
     */
    public void createChartPanel()
    {
        this.chart_panel = new ChartPanel(this.getChart(), false, true, true, false, true);  
        this.chart_panel.setDomainZoomable(true);
        this.chart_panel.setRangeZoomable(true);
    }


    /**
     * Set Controller Data (Processor)
     * 
     * @see com.atech.graphics.graphs.AbstractGraphViewAndProcessor#setControllerData(java.lang.Object)
     */
    public void setControllerData(Object data)
    {
        plot_data = (PlotSelectorData)data;
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
