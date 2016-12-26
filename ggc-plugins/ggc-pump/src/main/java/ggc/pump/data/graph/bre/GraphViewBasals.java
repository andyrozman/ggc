package ggc.pump.data.graph.bre;

import java.awt.*;
import java.util.ArrayList;

import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.DateAxis;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.plot.CombinedDomainXYPlot;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.XYStepAreaRenderer;
import org.jfree.data.Range;
import org.jfree.data.general.AbstractDataset;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;

import ggc.pump.data.bre.BREData;
import ggc.pump.data.bre.BREDataCollection;

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

public class GraphViewBasals extends BREGraphsAbstract // implements
                                                       // GraphViewInterface,
                                                       // GraphViewDataProcessorInterface
{

    // GregorianCalendar gc;
    // private GlucoValues gluco_values;
    // private GlucoValues gluco_values_prev;
    XYSeriesCollection dataset_old = new XYSeriesCollection();
    XYSeriesCollection dataset_new = new XYSeriesCollection();

    XYStepAreaRenderer xy_step2;
    XYStepAreaRenderer xy_step;

    // TimeSeriesCollection dataset = new TimeSeriesCollection();
    // DefaultCategoryDataset dataset = new DefaultCategoryDataset();

    // NumberAxis BGAxis;
    // private TimeSeriesCollection BGDataset = new TimeSeriesCollection();
    // private DailyValues data = new DailyValues();
    // DateAxis dateAxis;
    // NumberAxis insBUAxis;
    // private TimeSeriesCollection insBUDataset = new TimeSeriesCollection();
    // private XYSeriesColleion insBUDataset = new XYSeriesCollection();

    CombinedDomainXYPlot comb_plot;
    double basal_old_max = 1.5d;
    double basal_new_max = 1.5d;
    double basal_display = 1.5d;

    NumberAxis numberAxis;


    /**
     * Constructor
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
    @Override
    public String getHelpId()
    {
        return null;
    }


    /**
     * Get Viewer Dialog Bounds (used by GraphViewer)
     * 
     * @return Rectangle object
     */
    @Override
    public Rectangle getViewerDialogBounds()
    {
        return new Rectangle(100, 100, 500, 400);
    }


    /**
     * Load Data
     */
    public void loadData()
    {
        if (this.data_coll == null)
            return;

        /*
         * if (gluco_values==null)
         * {
         * this.gluco_values = new GlucoValues(this.gc,
         * (GregorianCalendar)this.gc.clone(), true);
         * System.out.println("Gluco Values: " +
         * this.gluco_values.getDailyValuesRowsCount());
         * GregorianCalendar gc_prev = (GregorianCalendar)this.gc.clone();
         * gc_prev.add(GregorianCalendar.DAY_OF_YEAR, -1);
         * this.gluco_values_prev = new GlucoValues(gc_prev, gc_prev, true);
         * }
         */
    }


    /**
     * Get Data Set
     * 
     * @return AbstractDataset instance
     */
    public AbstractDataset getDataSet()
    {
        return this.dataset_old;
    }


    /**
     * Preprocess Data
     */
    public void preprocessData()
    {

        if (this.data_coll == null)
            return;

        dataset_old.removeAllSeries();
        dataset_new.removeAllSeries();

        XYSeries basals_old = new XYSeries(this.m_ic.getMessage("CH_INS_RATIO"), false, false); // ,
                                                                                                // Hour.class);
        XYSeries basals_new = new XYSeries(this.m_ic.getMessage("BG_INS_RATIO"), true, true); // ,
                                                                                              // Hour.class);
        // XYSeries ratio_ch_bg = new
        // XYSeries(this.m_ic.getMessage("CH_BG_RATIO"), true, true); //,
        // Hour.class);

        ArrayList<BREData> lst = this.data_coll.getDataByType(BREData.BRE_DATA_BASAL_OLD);

        basal_old_max = 1.5d;
        basal_new_max = 1.5d;

        for (int i = 0; i < lst.size(); i++)
        {
            BREData rd = lst.get(i);

            long time = getTimeMs(rd.time);
            basals_old.add(time, rd.value);

            time = getTimeMs(rd.time_end);
            basals_old.add(time, rd.value);

            if (rd.value > basal_old_max)
            {
                basal_old_max = rd.value;
            }

            System.out.println("Old Start: " + rd.time + " End: " + rd.time_end + "  Value: " + rd.value);
            // System.out.println("Old End: " + rd.time_end);
        }

        dataset_old.addSeries(basals_old);

        lst = this.data_coll.getDataByType(BREData.BRE_DATA_BASAL_NEW);

        for (int i = 0; i < lst.size(); i++)
        {
            BREData rd = lst.get(i);

            long time = getTimeMs(rd.time);
            basals_new.add(time, rd.value);
            System.out.println("New Start: " + rd.time + "  Value: " + rd.value);

            time = getTimeMs(rd.time_end);
            basals_new.add(time, rd.value);
            System.out.println("New End: " + rd.time_end);

            if (rd.value > basal_new_max)
            {
                basal_new_max = rd.value;
            }

        }

        dataset_new.addSeries(basals_new);

        if (basal_new_max > basal_old_max)
        {
            basal_display = basal_new_max;
        }
        else
        {
            basal_display = basal_old_max;
        }

        basal_display += 0.5d;

        /*
         * //XYSeries xs = new XYSeries("BG");
         * TimeSeries ts = new TimeSeries("BG", Minute.class);
         * //System.out.println("preprocessData: " +
         * this.gv.getDailyValuesRowsCount());
         * int count = this.gluco_values.getDailyValuesRowsCount();
         * for(int i=0; i<count; i++)
         * {
         * DailyValuesRow dv = this.gluco_values.getDailyValueRow(i);
         * //ts.addOrUpdate(new Minute(dv.getDateTimeAsDate()), dv.getBG());
         * if (dv.getBG()>0)
         * ts.addOrUpdate(new Minute(dv.getDateTimeAsDate()), dv.getBG());
         * }
         * dataset.addSeries(ts);
         * ts = new TimeSeries("CH");
         * for(int i=0; i<count; i++)
         * {
         * DailyValuesRow dv = this.gluco_values.getDailyValueRow(i);
         * //ts.addOrUpdate(new Minute(dv.getDateTimeAsDate()), dv.getBG());
         * if (dv.getCH()>0)
         * ts.addOrUpdate(new Minute(dv.getDateTimeAsDate()), dv.getCH());
         * // xs.add(dv.getDateT(), dv.getCH());
         * }
         * //org.jfree.data.time.
         * dataset.addSeries(ts);
         * /*
         * dataset.clear();
         * System.out.println("Read HbA1c data:\n" +
         * hbValues.getPercentOfDaysInClass(0) + "\n" +
         * hbValues.getPercentOfDaysInClass(1) + "\n"
         * + hbValues.getPercentOfDaysInClass(2) + "\n" +
         * hbValues.getPercentOfDaysInClass(3) + "\n"
         * + hbValues.getPercentOfDaysInClass(4));
         * dataset.insertValue(0, m_ic.getMessage("DAYS_WITH_READINGS_0_1"),
         * hbValues.getPercentOfDaysInClass(0));
         * dataset.insertValue(1, m_ic.getMessage("DAYS_WITH_READINGS_2_3"),
         * hbValues.getPercentOfDaysInClass(1));
         * dataset.insertValue(2, m_ic.getMessage("DAYS_WITH_READINGS_4_5"),
         * hbValues.getPercentOfDaysInClass(2));
         * dataset.insertValue(3, m_ic.getMessage("DAYS_WITH_READINGS_6_7"),
         * hbValues.getPercentOfDaysInClass(3));
         * dataset.insertValue(4, m_ic.getMessage("DAYS_WITH_READINGS_MORE_7"),
         * hbValues.getPercentOfDaysInClass(4));
         */

    }


    // GregorianCalendar gcx = new GregorianCalendar();
    /*
     * public long getTimeMs(int time)
     * {
     * ATechDate atd = new ATechDate(ATechDate.FORMAT_TIME_ONLY_MIN, time);
     * gcx.set(GregorianCalendar.HOUR_OF_DAY, atd.hour_of_day);
     * gcx.set(GregorianCalendar.MINUTE, atd.minute);
     * return gcx.getTimeInMillis();
     * }
     */

    /**
     * Get Title (used by GraphViewer)
     * 
     * @return title as string 
     */
    @Override
    public String getTitle()
    {
        return m_ic.getMessage("DAILYGRAPHFRAME");
    }


    /*
     * private JFreeChart createCombinedChartOld()
     * {
     * // create subplot 1...
     * //IntervalXYDataset data1 = createDataset1();
     * XYItemRenderer renderer1 = new XYBarRenderer(0.20);
     * XYPlot subplot1 = new XYPlot(dataset_old, new DateAxis("Date"), null,
     * renderer1);
     * // create subplot 2...
     * //XYDataset data2 = createDataset2();
     * XYItemRenderer renderer2 = new StandardXYItemRenderer();
     * XYPlot subplot2 = new XYPlot(dataset_new, new DateAxis("Date"), null,
     * renderer2);
     * // create a parent plot...
     * CombinedRangeXYPlot plot = new CombinedRangeXYPlot(new
     * NumberAxis("Value"));
     * // add the subplots...
     * plot.add(subplot1, 1);
     * plot.add(subplot2, 1);
     * // return a new chart containing the overlaid plot...
     * return new JFreeChart(null,
     * JFreeChart.DEFAULT_TITLE_FONT, plot, true);
     * }
     */

    /**
     * Set Plot
     * 
     * @param chart JFreeChart instance
     */
    public void setPlot(JFreeChart chart)
    {

        this.numberAxis.setRange(new Range(0.0d, this.basal_display));

        xy_step.setBaseFillPaint(Color.green);
        xy_step.setBaseOutlinePaint(Color.black);
        xy_step.setBasePaint(Color.green);

        xy_step2.setBaseFillPaint(Color.blue);

        /*
         * NumberAxis numberAxis = new NumberAxis("Value");
         * numberAxis.setAutoRangeIncludesZero(true);
         * numberAxis.setRange(new Range(0.0d, 1.5d));
         */

        // chart.getPlot();

        /*
         * XYPlot plot = chart.getXYPlot();
         * XYLineAndShapeRenderer defaultRenderer = (XYLineAndShapeRenderer)
         * plot.getRenderer();
         * XYLineAndShapeRenderer insBURenderer = new XYLineAndShapeRenderer();
         * dateAxis = (DateAxis) plot.getDomainAxis();
         * BGAxis = (NumberAxis) plot.getRangeAxis();
         * insBUAxis = new NumberAxis();
         */
        // plot.s
        /*
         * jdskdsk
         * XYStepAreaRenderer xy_step = new
         * XYStepAreaRenderer(XYStepAreaRenderer.AREA);
         * XYPlot plot = chart.getXYPlot();
         * //ColorSchemeH colorScheme = graph_util.getColorScheme();
         * //CombinedRangeXYPlot plot = (CombinedRangeXYPlot)chart.getXYPlot();
         * //CombinedRangeXYPlot plot = new
         * CombinedRangeXYPlot(chart.getXYPlot());
         * BGAxis = (NumberAxis) plot.getRangeAxis();
         * BGAxis.setAutoRangeIncludesZero(true);
         * CombinedRangeXYPlot comb_plot = new CombinedRangeXYPlot(BGAxis);
         * comb_plot.setGap(10.0);
         */
        // CombinedRangeXYPlot pl = new CombinedRangeXYPlot();

        // plot.add(subplot1, 1);
        // plot.add(subplot2, 1);
        // plot.setOrientation(PlotOrientation.VERTICAL);
        /*
         * dsjhdjs
         * comb_plot.add(new XYPlot(dataset_old, plot.getDomainAxis(), BGAxis,
         * xy_step), 1);
         * comb_plot.add(new XYPlot(dataset_new, plot.getDomainAxis(), BGAxis,
         * xy_step), 1);
         * comb_plot.setOrientation(PlotOrientation.VERTICAL);
         */
        // new XYPlot(dataset, plot.getDomainAxis(), BGAxis, xy_step);
        // XYItemRenderer renderer);

        // chart.setBackgroundPaint(graph_util.backgroundColor);

        // plot.setRenderer(new XYStepAreaRenderer();)

        // plot.setRenderer(xy_step);
        /*
         * RenderingHints rh = graph_util.getRenderingHints();
         * if (rh!=null)
         * chart.setRenderingHints(rh);
         * chart.setBorderVisible(false);
         */

        /*
         * plot.setRangeAxis(1, insBUAxis);
         * plot.setRangeAxisLocation(1, AxisLocation.BOTTOM_OR_RIGHT);
         * plot.setDataset(1, insBUDataset);
         * plot.mapDatasetToRangeAxis(1, 1);
         * plot.setRenderer(1, insBURenderer);
         */
        /*
         * graph_util.applyMarkers(plot);
         * plot.setRangeGridlinesVisible(false);
         * plot.setDomainGridlinesVisible(false);
         * defaultRenderer.setSeriesPaint(0,
         * da_local.getColor(colorScheme.getColor_bg()));
         * insBURenderer.setSeriesPaint(0,
         * da_local.getColor(colorScheme.getColor_ch()));
         * insBURenderer.setSeriesPaint(1,
         * da_local.getColor(colorScheme.getColor_ins1()));
         * insBURenderer.setSeriesPaint(2,
         * da_local.getColor(colorScheme.getColor_ins2()));
         */
        // defaultRenderer.setSeriesShapesVisible(0, true);
        // insBURenderer.setSeriesShapesVisible(0, true);
        // insBURenderer.setSeriesShapesVisible(1, true);
        // insBURenderer.setSeriesShapesVisible(2, true);

        // AX dateAxis.setDateFormatOverride(new
        // SimpleDateFormat(m_ic.getMessage("FORMAT_DATE_HOURS")));
        // AX dateAxis.setAutoRange(false);
        /*
         * dateAxis.setRange(this.gluco_values.getRangeFrom().getTime(),
         * this.gluco_values.getRangeTo().getTime());
         * dateAxis.setDefaultAutoRange(new
         * DateRange(this.gluco_values.getRangeFrom().getTime(),
         * this.gluco_values.getRangeTo().getTime()));
         */
        // BGAxis.setAutoRangeIncludesZero(true);

        // ax insBUAxis.setLabel(m_ic.getMessage("CH_LONG") + " / " +
        // m_ic.getMessage("INSULIN"));
        // ax insBUAxis.setAutoRangeIncludesZero(true);

    }


    /**
     * Creates a combined XYPlot chart.
     *
     * @return the combined chart.
     */
    @SuppressWarnings("deprecation")
    private JFreeChart createCombinedChart()
    {

        xy_step = new XYStepAreaRenderer(XYStepAreaRenderer.AREA);
        // xy_step.setBaseFillPaint(new Paint(Color.green));

        // xy_step.setBaseFillPaint(Color.green);
        xy_step.setBaseOutlinePaint(Color.black);
        // xy_step.setBasePaint(Color.green);
        xy_step.setPaint(Color.green.darker());

        xy_step2 = new XYStepAreaRenderer(XYStepAreaRenderer.AREA);
        // xy_step2.setBaseFillPaint(Color.blue);
        xy_step2.setPaint(Color.blue);
        // Paint p = xy_step.getBaseFillPaint();

        // xy_step.s

        // new Paint();

        // XYPlot plot = chart.getXYPlot();
        // ColorSchemeH colorScheme = graph_util.getColorScheme();

        // CombinedRangeXYPlot plot = (CombinedRangeXYPlot)chart.getXYPlot();
        // CombinedRangeXYPlot plot = new
        // CombinedRangeXYPlot(chart.getXYPlot());

        numberAxis = new NumberAxis("Value");
        numberAxis.setAutoRangeIncludesZero(true);
        numberAxis.setRange(new Range(0.0d, 1.5d));

        comb_plot = new CombinedDomainXYPlot(new DateAxis("Date"));
        comb_plot.setGap(10.0);

        // CombinedRangeXYPlot pl = new CombinedRangeXYPlot();

        // plot.add(subplot1, 1);
        // plot.add(subplot2, 1);
        // plot.setOrientation(PlotOrientation.VERTICAL);

        comb_plot.add(new XYPlot(dataset_old, null, numberAxis, xy_step), 1);
        comb_plot.add(new XYPlot(dataset_new, null, numberAxis, xy_step2), 1);
        comb_plot.setOrientation(PlotOrientation.VERTICAL);

        // return a new chart containing the overlaid plot...
        return new JFreeChart(null, JFreeChart.DEFAULT_TITLE_FONT, comb_plot, false);

    }


    /**
     * Set Data for Graph (all handling done after that)
     */
    @Override
    public void setData(BREDataCollection data_coll)
    {
        this.data_coll = data_coll;
        this.loadData();
        this.preprocessData();

        // this.setPlot(chart);
    }


    /**
     * Create Chart
     */
    @Override
    public void createChart()
    {
        chart = this.createCombinedChart();

        /*
         * chart = ChartFactory.createTimeSeriesChart(null,
         * this.m_ic.getMessage("AXIS_TIME_LABEL"),
         * String.format(this.m_ic.getMessage("AXIS_VALUE_LABEL"),
         * this.graph_util.getUnitLabel()),
         * dataset_old,
         * false,
         * false,
         * false);
         * //ChartFactory.
         */
        // this.setPlot(chart);
    }


    /**
     * Create Chart Panel
     */
    @Override
    public void createChartPanel()
    {
        this.chart_panel = new ChartPanel(getChart(), true, true, true, true, false);
    }

}
