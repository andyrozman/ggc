package ggc.pump.data.graph.bre;

import ggc.pump.data.bre.BREData;
import ggc.pump.data.bre.BREDataCollection;
import ggc.pump.data.bre.BasalEstimationData;
import ggc.pump.gui.bre.BasalRateEstimator;

import java.awt.Rectangle;
import java.util.ArrayList;
import java.util.Calendar;

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

import com.atech.utils.data.ATechDate;

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

public class GraphViewBasalRate extends BREGraphsAbstract // implements
// GraphViewInterface,
// GraphViewDataProcessorInterface
{

    XYSeriesCollection dataset = new XYSeriesCollection();

    NumberAxis BGAxis;
    // private TimeSeriesCollection BGDataset = new TimeSeriesCollection();
    // private DailyValues data = new DailyValues();
    DateAxis dateAxis;
    NumberAxis insBUAxis;
    // private TimeSeriesCollection insBUDataset = new TimeSeriesCollection();
    private XYSeriesCollection insBUDataset = new XYSeriesCollection();
    private int m_type = 0;

    /**
     * Constructor
     *
     * @param type
     */
    public GraphViewBasalRate(int type)
    {
        super();

        System.out.println("TYpe: " + type);
        this.m_type = type;
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
        return this.dataset;
    }

    /**
     * Preprocess Data
     */
    public void preprocessData()
    {

        if (this.data_coll == null)
            return;

        dataset.removeAllSeries();
        insBUDataset.removeAllSeries();

        XYSeries bg = new XYSeries(this.m_ic.getMessage("MEASURED_BASAL_BG"), true, true); // ,
                                                                                           // Hour.class);

        XYSeries calc_basal = new XYSeries(this.m_ic.getMessage("CALCULATED_BASAL"), true, true); // ,
                                                                                                  // Hour.class);

        // XYSeries CHSeries = new XYSeries(this.m_ic.getMessage("CH_LONG"),
        // true, true); //, Hour.class);

        // int BGUnit = 1; // AAA da_local.getSettings().getBG_unit();

        if (this.m_type == BasalRateEstimator.GRAPH_OLD_RATE
                || this.m_type == BasalRateEstimator.GRAPH_BOTH_BASAL_RATES)
        {

            ArrayList<BREData> lst = this.data_coll.getDataByType(BREData.BRE_DATA_BG);

            for (int i = 0; i < lst.size(); i++)
            {
                BREData row = lst.get(i);

                long time = getTimeMs(row.time);

                bg.add(time, row.value);
            }

            dataset.addSeries(bg);

        }

        if (this.m_type == BasalRateEstimator.GRAPH_NEW_RATE
                || this.m_type == BasalRateEstimator.GRAPH_BOTH_BASAL_RATES)
        {
            ArrayList<BasalEstimationData> lst2 = this.data_coll.getBasalEstimationData();

            for (int i = 0; i < lst2.size(); i++)
            {
                BasalEstimationData row = lst2.get(i);

                long time = getTimeMs(row.time);

                calc_basal.add(time, row.insulin_value);
            }

            dataset.addSeries(calc_basal);

        }

    }

    // GregorianCalendar gcx = new GregorianCalendar();

    /**
     * Get Time in Ms
     */
    @Override
    public long getTimeMs(int time)
    {
        ATechDate atd = new ATechDate(ATechDate.FORMAT_TIME_ONLY_MIN, time);

        gcx.set(Calendar.HOUR_OF_DAY, atd.hourOfDay);
        gcx.set(Calendar.MINUTE, atd.minute);

        return gcx.getTimeInMillis();
    }

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

        // plot.s

        // ColorSchemeH colorScheme = graph_util.getColorScheme();

        // chart.setBackgroundPaint(graph_util.backgroundColor);

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
        defaultRenderer.setSeriesShapesVisible(0, true);
        insBURenderer.setSeriesShapesVisible(0, true);
        insBURenderer.setSeriesShapesVisible(1, true);
        insBURenderer.setSeriesShapesVisible(2, true);

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
     * Set Data
     */
    @Override
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
    @Override
    public void createChart()
    {
        chart = ChartFactory.createTimeSeriesChart(null, this.m_ic.getMessage("AXIS_TIME_LABEL"),
            String.format(this.m_ic.getMessage("AXIS_VALUE_LABEL"), this.graph_util.getUnitLabel()), dataset, true,
            true, false);

        this.setPlot(chart);
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
