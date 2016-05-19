package ggc.core.data.graph.v1.view;

import java.awt.*;
import java.text.SimpleDateFormat;
import java.util.GregorianCalendar;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.AxisLocation;
import org.jfree.chart.axis.DateAxis;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.XYBarRenderer;
import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer;
import org.jfree.data.general.AbstractDataset;
import org.jfree.data.time.DateRange;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.atech.utils.data.TimeZoneUtil;

import ggc.core.data.DailyValuesRow;
import ggc.core.data.GlucoValues;
import ggc.core.data.defs.GlucoseUnitType;
import ggc.core.db.hibernate.ColorSchemeH;
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
 *  Filename:     GraphViewDaily  
 *  Description:  GraphView implementation for Daily, used by new graph framework.
 *          Most of rumbi's code from DailyGraphView was reused and extended. Later added CGMS data.
 * 
 *  Author: andyrozman {andy@atech-software.com}  
 *  Author: rumbi   
 */

public class GraphViewDaily extends GraphViewCGMSDailyAbstract
{

    private static final Logger LOG = LoggerFactory.getLogger(GraphViewDaily.class);

    private GlucoValues gluco_values;

    private XYSeriesCollection datasetBG = new XYSeriesCollection();
    private XYSeriesCollection datasetInsCH = new XYSeriesCollection();

    NumberAxis BGAxis;
    DateAxis dateAxis;
    NumberAxis insBUAxis;


    /**
     * Constructor
     * 
     * @param currentCalendar
     */
    public GraphViewDaily(GregorianCalendar currentCalendar)
    {
        super(DataAccess.getInstance());
        this.currentCalendar = currentCalendar;
    }


    /**
     * {@inheritDoc}
     */
    public String getHelpId()
    {
        return "GGC_BG_Graph_Daily";
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public Rectangle getViewerDialogBounds()
    {
        return new Rectangle(0, 0, 600, 400);
    }


    /**
     * Load Data
     */
    public void loadData()
    {
        if (gluco_values == null)
        {
            this.gluco_values = new GlucoValues(this.currentCalendar, (GregorianCalendar) this.currentCalendar.clone(),
                    true);
        }

        loadCGMSData();

    }


    /**
     * {@inheritDoc}
     */
    public AbstractDataset getDataSet()
    {
        return this.datasetBG;
    }


    /**
     * Preprocess Data
     */
    public void preprocessData()
    {

        datasetBG.removeAllSeries();
        datasetInsCH.removeAllSeries();

        XYSeries BGSeries = new XYSeries(this.m_ic.getMessage("BLOOD_GLUCOSE"), true, true);
        XYSeries CHSeries = new XYSeries(this.m_ic.getMessage("CH_LONG"), true, true);
        XYSeries ins1Series = new XYSeries(dataAccessCore.getConfigurationManagerWrapper().getIns1Name(), true, true);
        XYSeries ins2Series = new XYSeries(dataAccessCore.getConfigurationManagerWrapper().getIns2Name(), true, true);

        GlucoseUnitType glucoseUnitType = dataAccessCore.getGlucoseUnitType();

        for (int i = 0; i < this.gluco_values.getDailyValuesRowsCount(); i++)
        {
            DailyValuesRow row = this.gluco_values.getDailyValueRow(i);

            long time = row.getDateTimeMs();

            if (row.getBG(glucoseUnitType) > 0)
            {
                createWiderBar(BGSeries, time, row.getBG(glucoseUnitType), 10);
            }

            if (row.getCH() > 0)
            {
                createWiderBar(CHSeries, time, row.getCH(), 20);
            }

            if (row.getIns1() > 0)
            {
                createWiderBar(ins1Series, time, row.getIns1(), 20);
            }

            if (row.getIns2() > 0)
            {
                createWiderBar(ins2Series, time, row.getIns2(), 20);
            }
        }

        datasetBG.addSeries(BGSeries);
        datasetInsCH.addSeries(ins1Series);
        datasetInsCH.addSeries(ins2Series);
        datasetInsCH.addSeries(CHSeries);

        if (useCGMSData)
        {
            datasetBG.addSeries(CGMSSeries);
        }

    }


    /**
     * {@inheritDoc}
     */
    public void setPlot(JFreeChart chart)
    {
        XYPlot plot = chart.getXYPlot();

        dateAxis = (DateAxis) plot.getDomainAxis();

        BGAxis = (NumberAxis) plot.getRangeAxis();
        insBUAxis = new NumberAxis();

        ColorSchemeH colorScheme = graphUtil.getColorScheme();

        chart.setBackgroundPaint(graphUtil.backgroundColor);

        RenderingHints rh = graphUtil.getRenderingHints();

        if (rh != null)
        {
            chart.setRenderingHints(rh);
        }

        chart.setBorderVisible(false);

        XYLineAndShapeRenderer defaultRenderer = (XYLineAndShapeRenderer) plot.getRenderer();
        XYBarRenderer barRenderer = new XYBarRenderer();

        plot.setRangeAxis(1, insBUAxis);
        plot.setRangeAxisLocation(1, AxisLocation.BOTTOM_OR_RIGHT);
        plot.setDataset(1, datasetInsCH);
        plot.mapDatasetToRangeAxis(1, 1);
        plot.setRenderer(1, barRenderer);

        defaultRenderer.setSeriesPaint(0, getColor(colorScheme.getColor_bg()));
        defaultRenderer.setSeriesShapesVisible(0, true);

        if (useCGMSData)
        {
            defaultRenderer.setSeriesPaint(1, Color.MAGENTA);
            defaultRenderer.setSeriesShapesVisible(1, false);
        }

        barRenderer.setSeriesPaint(0, getColor(colorScheme.getColor_ins1()));
        barRenderer.setSeriesPaint(1, getColor(colorScheme.getColor_ins2()));
        barRenderer.setSeriesPaint(2, getColor(colorScheme.getColor_ch()));

        SimpleDateFormat sdf = new SimpleDateFormat(m_ic.getMessage("FORMAT_DATE_HOURS"));
        sdf.setTimeZone(TimeZoneUtil.getInstance().getEmptyTimeZone());

        dateAxis.setDateFormatOverride(sdf);
        dateAxis.setAutoRange(false);
        dateAxis.setRange(this.gluco_values.getRangeFrom().getTime(), this.gluco_values.getRangeTo().getTime());
        dateAxis.setDefaultAutoRange(
            new DateRange(this.gluco_values.getRangeFrom().getTime(), this.gluco_values.getRangeTo().getTime()));
        dateAxis.setTimeZone(TimeZoneUtil.getInstance().getEmptyTimeZone());

        // BG Axis
        BGAxis.setAutoRangeIncludesZero(true);

        // Insulin/CH Axis
        insBUAxis.setLabel(m_ic.getMessage("CH_LONG") + " / " + m_ic.getMessage("INSULIN"));
        insBUAxis.setAutoRangeIncludesZero(true);

    }


    private void createWiderBar(XYSeries series, long timeMs, float value, int widthMin)
    {
        for (int k1 = 0; k1 < widthMin; k1++)
        {
            series.add(timeMs, value);
            timeMs += 60000;
        }
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public void createChart()
    {
        chart = ChartFactory.createTimeSeriesChart(null, this.m_ic.getMessage("AXIS_TIME_LABEL"),
            String.format(this.m_ic.getMessage("AXIS_VALUE_LABEL"), this.graphUtil.getUnitLabel()), datasetBG, true,
            true, true);

        this.setPlot(chart);
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public void createChartPanel()
    {
        this.chart_panel = new ChartPanel(getChart(), true, true, true, true, true);
    }

}
