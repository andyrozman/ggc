package ggc.core.data.graph.v1.view;

import java.awt.*;
import java.util.HashMap;
import java.util.Map;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.axis.ValueAxis;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.XYBarRenderer;
import org.jfree.data.general.AbstractDataset;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.atech.graphics.graphs.GraphUtil;

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
 *  Filename:     GraphViewFrequency
 *  Description:  GraphView implementation for Frequency, used by new graph framework
 * 
 *  Author: andyrozman {andy@atech-software.com}  
 */

// WIP: Enum, Types, Series names (Types), Cleanup

public class GraphViewFrequency extends GraphViewAbstract
{

    private static final Logger LOG = LoggerFactory.getLogger(GraphViewFrequency.class);

    XYSeriesCollection xySeriesCollection = new XYSeriesCollection();


    /**
     * Constructor
     */
    public GraphViewFrequency(Dimension startSize, GraphV1DbRetriever dbRetriever)
    {
        super(DataAccess.getInstance(), GGCGraphType.Frequency, dbRetriever, startSize);
    }


    /**
     * Preprocess Data
     */
    @SuppressWarnings("deprecation")
    public void preprocessData()
    {
        int count = this.glucoValues.getDailyValuesRowsCount();

        LOG.debug("Preprocess Data [entries: {}]", count);

        DailyValuesRow row;

        xySeriesCollection.removeAllSeries();

        Map<String, Integer> mapOfFrequency = new HashMap<String, Integer>();

        for (int i = 0; i < this.glucoValues.getRowCount(); i++)
        {
            row = this.glucoValues.getDailyValueRow(i);

            if (this.plotData.isPlotBG())
            {
                if (row.getBG(glucoseUnitType) > 0)
                {
                    addToMapOfFrequency(mapOfFrequency, row.getBGAsString(glucoseUnitType));
                }
            }
            else if (this.plotData.isPlotCH())
            {
                if (row.getCH() > 0)
                {
                    addToMapOfFrequency(mapOfFrequency, row.getCHAsString());
                }
            }
            else if (this.plotData.isPlotIns1())
            {
                if (row.getBolusInsulin() > 0)
                {
                    addToMapOfFrequency(mapOfFrequency, row.getBolusInsulinAsString());
                }
            }
            else if (this.plotData.isPlotIns2())
            {
                if (row.getBasalInsulin() > 0)
                {
                    addToMapOfFrequency(mapOfFrequency, row.getBasalInsulinAsString());
                }
            }
        }

        XYSeries xySeries = new XYSeries("Freq: ", true, false);

        for (Map.Entry<String, Integer> entry : mapOfFrequency.entrySet())
        {
            Float f = Float.parseFloat(entry.getKey());
            xySeries.add(f, entry.getValue());
        }

        xySeriesCollection.addSeries(xySeries);

        setPlot(chart);

    }


    private void addToMapOfFrequency(Map<String, Integer> mapOfFrequency, String value)
    {
        if (mapOfFrequency.containsKey(value))
        {
            Integer countc = mapOfFrequency.get(value);
            countc++;

            mapOfFrequency.put(value, countc);
        }
        else
        {
            mapOfFrequency.put(value, 1);
        }
    }


    public AbstractDataset getDataSet()
    {
        return null;
    }


    /**
     * {@inheritDoc}
     */
    public void setPlot(JFreeChart chart)
    {
        if (chart == null)
            return;

        // TODO

        XYPlot plot = (XYPlot) chart.getPlot();
        plot.setDomainZeroBaselineVisible(false);
        plot.getDomainAxis().setStandardTickUnits(NumberAxis.createIntegerTickUnits());
        plot.getRangeAxis().setStandardTickUnits(NumberAxis.createIntegerTickUnits());

        XYBarRenderer renderer = (XYBarRenderer) plot.getRenderer();

        renderer.setShadowVisible(false);
        renderer.setMargin(0.9);
        renderer.setSeriesPaint(0, Color.blue.brighter());

        plot.setNoDataMessage(this.m_ic.getMessage("GRAPH_NO_DATA_AVAILABLE_CHANGE"));

        ValueAxis valueAxis = plot.getDomainAxis();

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

    }


    /**
     * {@inheritDoc}
     */
    @Override
    public void createChart()
    {
        chart = ChartFactory.createHistogram(null, null, i18nControl.getMessage("ITEMS_AXIS"), xySeriesCollection,
            PlotOrientation.VERTICAL, false, false, false);
    }

}
