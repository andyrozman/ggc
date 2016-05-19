package ggc.core.data.graph.v1.view;

import java.awt.*;
import java.util.Calendar;
import java.util.GregorianCalendar;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.DateAxis;
import org.jfree.chart.axis.ValueAxis;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer;
import org.jfree.data.general.AbstractDataset;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.atech.graphics.graphs.GraphUtil;
import com.atech.utils.data.ATechDate;

import ggc.core.data.DailyValuesRow;
import ggc.core.data.graph.v1.GGCGraphType;
import ggc.core.data.graph.v1.db.GraphV1DbRetriever;
import ggc.core.util.DataAccess;

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
public class GraphViewSpread extends GraphViewAbstract
{

    private static final Logger LOG = LoggerFactory.getLogger(GraphViewSpread.class);

    XYSeriesCollection dataset = new XYSeriesCollection();
    ValueAxis valueAxis = null;
    XYLineAndShapeRenderer renderer;


    /**
     * Constructor
     */
    public GraphViewSpread(Dimension startSize, GraphV1DbRetriever dbRetriever)
    {
        super(DataAccess.getInstance(), GGCGraphType.Spread, dbRetriever, startSize);

        plotData.setPlotBG(true);
    }


    /**
     * {@inheritDoc}
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

        int count = this.glucoValues.getDailyValuesRowsCount();

        LOG.debug("Preprocess Data [entries: {}]", count);

        dataset.removeAllSeries();

        if (valueAxis != null)
        {

            if (this.plotData.isPlotBG())
            {
                String s = m_ic.getMessage("BG") + "  [" + dataAccesLocal.getGlucoseUnitType().getTranslation() + "]";
                valueAxis.setLabel(s);
                graphUtil.setShapeAndColor(GraphUtil.SHAPE_SQUARE, Color.green.darker(), renderer);
            }
            else if (this.plotData.isPlotCH())
            {
                valueAxis.setLabel(m_ic.getMessage("CH") + "  [g]");
                graphUtil.setShapeAndColor(GraphUtil.SHAPE_CIRCLE, Color.red, renderer);
            }
            else if (this.plotData.isPlotIns1())
            {
                valueAxis.setLabel(m_ic.getMessage("INSULIN_1") + " [" + m_ic.getMessage("UNIT_SHORT") + "]");
                graphUtil.setShapeAndColor(GraphUtil.SHAPE_TRIANGLE_UP, Color.magenta, renderer);
            }
            else if (this.plotData.isPlotIns2())
            {
                valueAxis.setLabel(m_ic.getMessage("INSULIN_2") + " [" + m_ic.getMessage("UNIT_SHORT") + "]");
                graphUtil.setShapeAndColor(GraphUtil.SHAPE_RHOMB, Color.orange, renderer);
            }
            else if (this.plotData.isPlotInsTotal())
            {
                valueAxis.setLabel(m_ic.getMessage("INS_1") + "+" + m_ic.getMessage("INS_2") + " ["
                        + m_ic.getMessage("UNIT_SHORT") + "]");
                graphUtil.setShapeAndColor(GraphUtil.SHAPE_TRIANGLE_DOWN, Color.blue, renderer);
            }

        }

        if (this.plotData.isPlotBG() || this.plotData.isPlotCH() || this.plotData.isPlotIns1()
                || this.plotData.isPlotIns2() || this.plotData.isPlotInsTotal())
        {

            for (int i = 0; i < count; i++)
            {
                DailyValuesRow dv = this.glucoValues.getDailyValueRow(i);
                long time = getFakeDateMs(dv.getDateTimeAsATDate());

                if (this.plotData.isPlotBG() && dv.getBG() > 0)
                {
                    xs.add(time, dv.getBG());
                }
                else if (this.plotData.isPlotCH() && dv.getCH() > 0)
                {
                    xs.add(time, dv.getCH());
                }
                else if (this.plotData.isPlotIns1() && dv.getIns1() > 0)
                {
                    xs.add(getFakeDateMs(dv.getDateTimeAsATDate()), dv.getIns1());
                }
                else if (this.plotData.isPlotIns2() && dv.getIns2() > 0)
                {
                    xs.add(getFakeDateMs(dv.getDateTimeAsATDate()), dv.getIns2());
                }
                else if (this.plotData.isPlotInsTotal() && (dv.getIns1() > 0 || dv.getIns2() > 0))
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
     * {@inheritDoc}
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

        this.valueAxis = plot.getRangeAxis();
    }


    private void setRendererStuff(XYLineAndShapeRenderer renderer)
    {

        if (this.plotData.isPlotBG())
        {
            graphUtil.setShapeAndColor(GraphUtil.SHAPE_SQUARE, Color.green.darker(), renderer);
        }
        else if (this.plotData.isPlotCH())
        {
            graphUtil.setShapeAndColor(GraphUtil.SHAPE_CIRCLE, Color.red, renderer);
        }
        else if (this.plotData.isPlotIns1())
        {
            graphUtil.setShapeAndColor(GraphUtil.SHAPE_TRIANGLE_UP, Color.magenta, renderer);
        }
        else if (this.plotData.isPlotIns2())
        {
            graphUtil.setShapeAndColor(GraphUtil.SHAPE_RHOMB, Color.orange, renderer);
        }
        else if (this.plotData.isPlotInsTotal())
        {
            graphUtil.setShapeAndColor(GraphUtil.SHAPE_TRIANGLE_DOWN, Color.blue, renderer);
        }

    }


    /**
     * {@inheritDoc}
     */
    @Override
    public void createChart()
    {
        this.chart = ChartFactory.createScatterPlot(null, m_ic.getMessage("AXIS_TIME_LABEL"),
            m_ic.getMessage("BG") + "  [" + dataAccesLocal.getGlucoseUnitType().getTranslation() + "]", dataset,
            PlotOrientation.VERTICAL, false, false, false);
        XYPlot plot = this.chart.getXYPlot();
        plot.setDomainAxis(new DateAxis());
    }

}
