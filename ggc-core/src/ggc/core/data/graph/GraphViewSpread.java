package ggc.core.data.graph;

import ggc.core.data.DailyValuesRow;
import ggc.core.data.GlucoValues;
import ggc.core.util.DataAccess;

import java.awt.Color;
import java.awt.Rectangle;
import java.util.Calendar;
import java.util.GregorianCalendar;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.DateAxis;
import org.jfree.chart.axis.NumberAxis;
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
import com.atech.utils.ATDataAccessAbstract;
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
 *  Filename:     GraphViewSpread
 *  Description:  GraphView implementation for Pen Spread, used by ATech Graph Framework
 *
 *  Author: andyrozman {andy@atech-software.com}
 */

public class GraphViewSpread extends AbstractGraphViewAndProcessor
{

    DataAccess da_local = null;
    XYSeriesCollection dataset = new XYSeriesCollection();

    GlucoValues gv;
    PlotSelectorData plot_data = null; // new PlotData();
    GGCGraphViewControler controler = null;
    DateAxis dateAxis;
    NumberAxis BGAxis;

    GregorianCalendar gc_from, gc_to;
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

        graph_util = GGCGraphUtil.getInstance(da_local);

        this.controler = new GGCGraphViewControler(this, GGCGraphViewControler.GRAPH_SPREAD);
    }

    /**
     * Get Help Id
     *
     * @return
     */
    public String getHelpId()
    {
        return "GGC_BG_Graph_Spread";
    }

    /**
     * Get Title (used by GraphViewer)
     * 
     * @return title as string
     */
    @Override
    public String getTitle()
    {
        return m_ic.getMessage("SPREAD_GRAPH") + " [" + m_ic.getMessage("NOT_TESTED_100PRO") + "]";
    }

    /**
     * Get Viewer Dialog Bounds (used by GraphViewer)
     *
     * @return Rectangle object
     */
    @Override
    public Rectangle getViewerDialogBounds()
    {
        return new Rectangle(100, 100, 750, 500);
    }

    /**
     * Set Controller Data
     *
     * @see com.atech.graphics.graphs.AbstractGraphViewAndProcessor#setControllerData(java.lang.Object)
     */
    @Override
    public void setControllerData(Object obj)
    {
        plot_data = (PlotSelectorData) obj;
    }

    /**
     * Load Data
     */
    public void loadData()
    {
        System.out.println("Reload Data");

        boolean changed = false;

        if (gc_from == null
                || !m_da.compareGregorianCalendars(ATDataAccessAbstract.GC_COMPARE_DAY, gc_from, this.plot_data
                        .getDateRangeData().getRangeFrom()))
        {
            gc_from = this.plot_data.getDateRangeData().getRangeFrom();
            changed = true;
        }

        if (gc_to == null
                || !m_da.compareGregorianCalendars(ATDataAccessAbstract.GC_COMPARE_DAY, gc_to, this.plot_data
                        .getDateRangeData().getRangeTo()))
        {
            gc_to = this.plot_data.getDateRangeData().getRangeTo();
            changed = true;
        }

        if (changed)
        {
            System.out.println(gc_from.get(Calendar.DAY_OF_MONTH) + "." + gc_from.get(Calendar.MONTH) + "."
                    + gc_from.get(Calendar.YEAR) + " " + gc_from.get(Calendar.HOUR_OF_DAY) + ":"
                    + gc_from.get(Calendar.MINUTE));
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
        return this.dataset;
    }

    /**
     * Preprocess Data
     */
    public void preprocessData()
    {
        XYSeries xs = new XYSeries("BG");

        int count = this.gv.getDailyValuesRowsCount();
        System.out.println("Preprocess Data [rows=" + count + "]");
        dataset.removeAllSeries();

        if (va != null)
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
                va.setLabel(m_ic.getMessage("INS_1") + "+" + m_ic.getMessage("INS_2") + " ["
                        + m_ic.getMessage("UNIT_SHORT") + "]");
                graph_util.setShapeAndColor(GraphUtil.SHAPE_TRIANGLE_DOWN, Color.blue, renderer);
            }

        }

        if (this.plot_data.isPlotBG() || this.plot_data.isPlotCH() || this.plot_data.isPlotIns1()
                || this.plot_data.isPlotIns2() || this.plot_data.isPlotInsTotal())
        {

            for (int i = 0; i < count; i++)
            {
                DailyValuesRow dv = this.gv.getDailyValueRow(i);
                long time = getFakeDateMs(dv.getDateTimeAsATDate());

                if (this.plot_data.isPlotBG() && dv.getBG() > 0)
                {
                    xs.add(time, dv.getBG());
                }
                else if (this.plot_data.isPlotCH() && dv.getCH() > 0)
                {
                    xs.add(time, dv.getCH());
                }
                else if (this.plot_data.isPlotIns1() && dv.getIns1() > 0)
                {
                    xs.add(getFakeDateMs(dv.getDateTimeAsATDate()), dv.getIns1());
                }
                else if (this.plot_data.isPlotIns2() && dv.getIns2() > 0)
                {
                    xs.add(getFakeDateMs(dv.getDateTimeAsATDate()), dv.getIns2());
                }
                else if (this.plot_data.isPlotInsTotal() && (dv.getIns1() > 0 || dv.getIns2() > 0))
                {
                    xs.add(getFakeDateMs(dv.getDateTimeAsATDate()), dv.getIns1() + dv.getIns2());
                }

            } // for
        }
        else
        {
            System.out.println("Error: Wrong plot type");
        }

        dataset.addSeries(xs);

    }

    GregorianCalendar gc_temp = new GregorianCalendar(2008, 1, 1, 0, 0);

    private long getFakeDateMs(ATechDate date)
    {
        gc_temp.set(Calendar.HOUR_OF_DAY, date.getHourOfDay());
        gc_temp.set(Calendar.MINUTE, date.minute);

        return gc_temp.getTimeInMillis();
    }

    /**
     * Set Plot
     *
     * @param chart
     *            JFreeChart instance
     */
    @SuppressWarnings("deprecation")
    public void setPlot(JFreeChart chart)
    {
        XYPlot plot = chart.getXYPlot();

        renderer = (XYLineAndShapeRenderer) plot.getRenderer();
        renderer.setShapesVisible(true);
        renderer.setDrawOutlines(false);
        renderer.setUseFillPaint(true);
        setRendererStuff(renderer);

        plot.setWeight(1);
        plot.setNoDataMessage(this.m_ic.getMessage("GRAPH_NO_DATA_AVAILABLE_CHANGE"));

        this.va = plot.getRangeAxis();
    }

    private void setRendererStuff(XYLineAndShapeRenderer renderer)
    {

        if (this.plot_data.isPlotBG())
        {
            graph_util.setShapeAndColor(GraphUtil.SHAPE_SQUARE, Color.green.darker(), renderer);
        }
        else if (this.plot_data.isPlotCH())
        {
            graph_util.setShapeAndColor(GraphUtil.SHAPE_CIRCLE, Color.red, renderer);
        }
        else if (this.plot_data.isPlotIns1())
        {
            graph_util.setShapeAndColor(GraphUtil.SHAPE_TRIANGLE_UP, Color.magenta, renderer);
        }
        else if (this.plot_data.isPlotIns2())
        {
            graph_util.setShapeAndColor(GraphUtil.SHAPE_RHOMB, Color.orange, renderer);
        }
        else if (this.plot_data.isPlotInsTotal())
        {
            graph_util.setShapeAndColor(GraphUtil.SHAPE_TRIANGLE_DOWN, Color.blue, renderer);
        }

    }

    /**
     * Create Chart
     */
    @Override
    public void createChart()
    {
        this.chart = ChartFactory.createScatterPlot(null, m_ic.getMessage("AXIS_TIME_LABEL"), m_ic.getMessage("BG")
                + "  [" + da_local.getSettings().getBG_unitString() + "]", dataset, PlotOrientation.VERTICAL, false,
            false, false);
        XYPlot plot = this.chart.getXYPlot();
        plot.setDomainAxis(new DateAxis());
    }

    /**
     * Create Chart Panel
     */
    @Override
    public void createChartPanel()
    {
        this.chart_panel = new ChartPanel(this.getChart(), true, true, true, true, false);
    }

    /**
     * Get Controler Interface instance
     *
     * @return GraphViewControlerInterface instance or null
     */
    @Override
    public GraphViewControlerInterface getControler()
    {
        return this.controler;
    }

}
