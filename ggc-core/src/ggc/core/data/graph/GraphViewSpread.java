package ggc.core.data.graph;

import ggc.core.data.DailyValuesRow;
import ggc.core.data.GlucoValues;
import ggc.core.util.DataAccess;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Rectangle;
import java.text.SimpleDateFormat;
import java.util.GregorianCalendar;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.DateAxis;
import org.jfree.chart.axis.ValueAxis;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer;
import org.jfree.data.general.AbstractDataset;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;

import com.atech.graphics.graphs.AbstractGraphViewAndProcessor;
import com.atech.graphics.graphs.GraphUtil;
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
    ValueAxis va = null;
    XYLineAndShapeRenderer renderer;
    GGCGraphUtil graph_util;
    
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
        
        graph_util = GGCGraphUtil.getInstance(da_local);
        //GregorianCalendar gc = new GregorianCalendar();
        //gc.add(GregorianCalendar.MONTH, -1);
        
        //plot_data.setTimeRangeFrom(gc);
        //plot_data.setTimeRangeFrom(new GregorianCalendar());
        
        
        //this.gc_from = new GregorianCalendar();
        //this.gc_from.add(GregorianCalendar.MONTH, -1);
        //this.gc_to = new GregorianCalendar();
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
        System.out.println("\n");
        plot_data = (PlotSelectorData)obj;
        
        System.out.println("BG in: " + plot_data.isPlotBG());
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
        
        if ((gc_from==null) || (!m_da.compareGregorianCalendars(DataAccess.GC_COMPARE_DAY, gc_from, this.plot_data.getDateRangeData().getRangeFrom())))
        {
            System.out.println("From changed");
            gc_from = this.plot_data.getDateRangeData().getRangeFrom();
            changed = true;
        }
        
        if ((gc_to==null) || (!m_da.compareGregorianCalendars(DataAccess.GC_COMPARE_DAY, gc_to, this.plot_data.getDateRangeData().getRangeTo())))
        {
            System.out.println("To changed");
            gc_to = this.plot_data.getDateRangeData().getRangeTo();
            changed = true;
        }

        if (changed)
        {
            this.gv = new GlucoValues(gc_from, gc_to, true);
            System.out.println("Reread data [rows=" + this.gv.getDailyValuesRowsCount() + "]");
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
        //System.out.println("preprocessData");
        //TimeSeries ts = new TimeSeries("BG Entries", Minute.class);

        
        XYSeries xs = new XYSeries("BG");
        
        int count = this.gv.getDailyValuesRowsCount();

        System.out.println("preprocessData: " + count);
        dataset.removeAllSeries();
        
        System.out.println("BG Plot: " + this.plot_data.isPlotBG());
        
        if (va!=null)
        {
            if (this.plot_data.isPlotBG()) 
            {
                String s = m_ic.getMessage("BG") + "  [" + da_local.getSettings().getBG_unitString() + "]";
                va.setLabel(s);
                graph_util.setShapeAndColor(GraphUtil.SHAPE_SQUARE, Color.green.darker(), renderer);
            }
            else if (this.plot_data.isPlotCH()) 
            {
                va.setLabel(m_ic.getMessage("CH") + "  [g]");
                graph_util.setShapeAndColor(GraphUtil.SHAPE_CIRCLE, Color.red, renderer);
            }
            else if (this.plot_data.isPlotIns1()) 
            {
                va.setLabel(m_ic.getMessage("INSULIN_1") + " [" + m_ic.getMessage("UNIT_SHORT") + "]");
                graph_util.setShapeAndColor(GraphUtil.SHAPE_TRIANGLE_UP, Color.magenta, renderer);
            }
            else if (this.plot_data.isPlotIns2()) 
            {
                va.setLabel(m_ic.getMessage("INSULIN_2") + " [" + m_ic.getMessage("UNIT_SHORT") + "]");
                graph_util.setShapeAndColor(GraphUtil.SHAPE_RHOMB, Color.orange, renderer);
            }
            else if (this.plot_data.isPlotInsTotal()) 
            {
                va.setLabel(m_ic.getMessage("INS_1") + "+" + m_ic.getMessage("INS_2")+ " [" + m_ic.getMessage("UNIT_SHORT") + "]");
                graph_util.setShapeAndColor(GraphUtil.SHAPE_TRIANGLE_DOWN, Color.blue, renderer);
            }
            
            /*
            else if (this.plot_data.isPlotBGDayAvg()) 
            {
                va.setLabel(m_ic.getMessage("BG") + "  [" + da_local.getSettings().getBG_unitString() + "]");
                graph_util.setShapeAndColor(GraphUtil.SHAPE_SQUARE, Color.blue, renderer);
            }
            else if (this.plot_data.isPlotCHDayAvg()) 
            {
                va.setLabel(m_ic.getMessage("CH") + "  [g]");
                graph_util.setShapeAndColor(GraphUtil.SHAPE_CIRCLE, Color.blue, renderer);
            }
            else if (this.plot_data.isPlotIns1DayAvg()) 
            {
                va.setLabel(m_ic.getMessage("INSULIN_1") + " [" + m_ic.getMessage("UNIT_SHORT") + "]");
                graph_util.setShapeAndColor(GraphUtil.SHAPE_TRIANGLE_UP, Color.blue, renderer);
            }
            else if (this.plot_data.isPlotIns2DayAvg()) 
            {
                va.setLabel(m_ic.getMessage("INSULIN_2") + " [" + m_ic.getMessage("UNIT_SHORT") + "]");
                graph_util.setShapeAndColor(GraphUtil.SHAPE_RHOMB, Color.blue, renderer);
            }
            else if (this.plot_data.isPlotInsTotalDayAvg()) 
            {
                va.setLabel(m_ic.getMessage("INS_1") + "+" + m_ic.getMessage("INS_2")+ " [" + m_ic.getMessage("UNIT_SHORT") + "]");
                graph_util.setShapeAndColor(GraphUtil.SHAPE_TRIANGLE_DOWN, Color.blue, renderer);
            }
            else if (this.plot_data.isPlotBGReadings()) 
            {
                va.setLabel(m_ic.getMessage("READINGS"));
                graph_util.setShapeAndColor(GraphUtil.SHAPE_ELIPSE, Color.magenta, renderer);
            }
            else if (this.plot_data.isPlotCHSum()) 
            {
                va.setLabel(m_ic.getMessage("CH") + "  [g]");
                graph_util.setShapeAndColor(GraphUtil.SHAPE_CIRCLE, Color.magenta, renderer);
            }
            else if (this.plot_data.isPlotIns1Sum()) 
            {
                va.setLabel(m_ic.getMessage("INSULIN_1") + " [" + m_ic.getMessage("UNIT_SHORT") + "]");
                graph_util.setShapeAndColor(GraphUtil.SHAPE_TRIANGLE_UP, Color.magenta, renderer);
            }
            else if (this.plot_data.isPlotIns2Sum()) 
            {
                va.setLabel(m_ic.getMessage("INSULIN_2") + " [" + m_ic.getMessage("UNIT_SHORT") + "]");
                graph_util.setShapeAndColor(GraphUtil.SHAPE_RHOMB, Color.magenta, renderer);
            }
            else if (this.plot_data.isPlotInsTotalSum()) 
            {
                va.setLabel(m_ic.getMessage("INS_1") + "+" + m_ic.getMessage("INS_2")+ " [" + m_ic.getMessage("UNIT_SHORT") + "]");
                graph_util.setShapeAndColor(GraphUtil.SHAPE_TRIANGLE_DOWN, Color.magenta, renderer);
            }
            else if (this.plot_data.isPlotInsPerCH()) 
            {
                va.setLabel(m_ic.getMessage("INS_SLASH_BU") + " [" + m_ic.getMessage("UNIT_SHORT") + "]");
                graph_util.setShapeAndColor(GraphUtil.SHAPE_RECTANGLE_UP, Color.cyan, renderer);
            }
            else if (this.plot_data.isPlotMeals()) 
            {
                va.setLabel(m_ic.getMessage("MEALS_PER_DAY"));
                graph_util.setShapeAndColor(GraphUtil.SHAPE_TRIANGLE_DOWN_RIGHT, Color.cyan, renderer);
            }*/
        
        }

        if ((this.plot_data.isPlotBG()) || 
            (this.plot_data.isPlotCH()) ||
            (this.plot_data.isPlotIns1()) ||
            (this.plot_data.isPlotIns2()) ||
            (this.plot_data.isPlotInsTotal()))
        {

        
        
            for(int i=0; i<count; i++)
            {
                DailyValuesRow dv = this.gv.getDailyValueRow(i);
                //ts.addOrUpdate(new Minute(dv.getDateTimeAsDate()), dv.getBG());
                
                //long time = getFakeDate(dv.getDateTimeAsATDate()).getTimeInMillis();
                
                //System.out.println("DateT: " + dv.getDateT());
                
                if ((this.plot_data.isPlotBG()) && (dv.getBG()>0)) 
                {
                    xs.add(getFakeDate(dv.getDateTimeAsATDate()).getTimeInMillis(), dv.getBG());
                }
                else if ((this.plot_data.isPlotCH()) && (dv.getCH()>0))
                {
                    xs.add(getFakeDate(dv.getDateTimeAsATDate()).getTimeInMillis(), dv.getCH());
                }
                else if ((this.plot_data.isPlotIns1()) && (dv.getIns1()>0))
                {
                    xs.add(getFakeDate(dv.getDateTimeAsATDate()).getTimeInMillis(), dv.getIns1());
                }
                else if ((this.plot_data.isPlotIns2()) && (dv.getIns2()>0))
                {
                    xs.add(getFakeDate(dv.getDateTimeAsATDate()).getTimeInMillis(), dv.getIns2());
                }
                else if ((this.plot_data.isPlotInsTotal()) && ((dv.getIns1()>0) || (dv.getIns2()>0)))
                {
                    xs.add(getFakeDate(dv.getDateTimeAsATDate()).getTimeInMillis(),(dv.getIns1()+dv.getIns2()));
                }
            
            /*
                else if ((this.plot_data.isPlotIns1DayAvg()) && (dv.getIns1()>0))
                {
                    
                    // ins 1 avg
                    if (xs.getDataItem(time) == null)
                    {
                        ins1AvgSeries.add(time, row.getIns1());
                    }
                    else
                    {
                        ins1AvgSeries.addOrUpdate(time, MathUtils.getAverage(row.getIns1(), ins1AvgSeries.getDataItem(
                            time).getValue()));
                    }
                }
                else if ((this.plot_data.isPlotIns1Sum()) && (dv.getIns1()>0))
                {
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
                    } */
                } // for
            
            
            
            
            
        }
        else
        {
            /*
            for(int i=0; i<this.gv.getDailyValuesItemsCount(); i++)
            {
                DailyValues dv = this.gv.getDailyValuesItem(i);
                
                if ((this.plot_data.isPlotBGDayAvg()) || (dv.getAvgBG()>0))
                {
                    xs.add(getFakeDate(dv.getDateTimeAsATDate()).getTimeInMillis(), dv.getBG());
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
                
                
               
                
            }
            */
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
    @SuppressWarnings("deprecation")
    public void setPlot(JFreeChart chart)
    {
        //XYPlot plot = (XYPlot)chart.getPlot();
        XYPlot plot = chart.getXYPlot();
        dateAxis = (DateAxis) plot.getDomainAxis();
        va = plot.getRangeAxis();
        
        dateAxis.setDateFormatOverride(new SimpleDateFormat(m_ic.getMessage("FORMAT_DATE_HOURS")));
        dateAxis.setAutoRange(false);
        dateAxis.setRange(this.dt_from.getTime(), this.dt_to.getTime());
        //dateAxis.setTimeline(new Timeline());
        
        dateAxis.setLabel(m_ic.getMessage("AXIS_TIME_LABEL"));
        
        //XYItemRenderer rend = plot.getRenderer();
        //rend.setPaint(new Paint());
        
        //XYDotRenderer rend = (XYDotRenderer)plot.getRenderer(); //new XYDotRenderer();
        
        renderer = (XYLineAndShapeRenderer) plot.getRenderer(); 
        renderer.setShapesVisible(true);
        renderer.setDrawOutlines(false); 
        renderer.setUseFillPaint(true);
        renderer.setFillPaint(Color.green.darker());
        renderer.setOutlineStroke(new BasicStroke(0.75f));
        //renderer.setSeriesStroke(0, new BasicStroke(1.5f));
        //Shape sh = new Shape();
        //r1.setShape(sh);
        //.getItemShapeFilled(0, 2);
        //r1.setShapesVisible(true);
/*        
        DefaultDrawingSupplier dds = new DefaultDrawingSupplier();
        Shape sh[] = dds.createStandardSeriesShapes();
       
        
        renderer.setShape(sh[5]);  // 2
        */
        //this.dataset.
        
        //rend.setDotHeight(1);
        //rend.setDotWidth(1);
        //rend.setFillPaint()
        
        
        //plot.setRenderer(rend);
        
/*            else if ((this.plot_data.isPlotBG()) && (dv.getBG()>0))
        {
            xs.add(getFakeDate(dv.getDateTimeAsATDate()).getTimeInMillis(), dv.getBG());
        }
        else if ((this.plot_data.isPlotBG()) && (dv.getBG()>0))
        {
            xs.add(getFakeDate(dv.getDateTimeAsATDate()).getTimeInMillis(), dv.getBG());
        }
        */
        
        
        
        //FastScatterPlot plot = (FastScatterPlot) chart.getPlot();

        //chart.setRenderingHints(renderingHints);

//        plot.setBackgroundPaint(Color.white); //backgroundColor);
        //plot.setCircular(true);
        //plot.setBackgroundAlpha(0.5f);
//        plot.setForegroundAlpha(0.5f);
        
        
        
    }

    /**
     * Create Chart
     */
    public void createChart()
    {
        this.chart = ChartFactory.createScatterPlot(null, 
                        m_ic.getMessage("AXIS_TIME_LABEL"), 
                        m_ic.getMessage("BG") + "  [" + da_local.getSettings().getBG_unitString() + "]", 
                        dataset, 
                        PlotOrientation.VERTICAL, 
                        false, 
                        false, 
                        false);
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
