package ggc.pump.data.graph;

import java.awt.*;
import java.text.SimpleDateFormat;
import java.util.Calendar;
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
import org.jfree.data.Range;
import org.jfree.data.general.AbstractDataset;
import org.jfree.data.time.DateRange;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.atech.i18n.I18nControlAbstract;
import com.atech.utils.data.TimeZoneUtil;

import ggc.core.data.graph.v1.view.GraphViewCGMSDailyAbstract;
import ggc.core.db.hibernate.settings.ColorSchemeH;
import ggc.core.util.DataAccess;
import ggc.plugin.data.DeviceValuesDay;
import ggc.pump.data.PumpValuesEntry;
import ggc.pump.data.PumpValuesEntryExt;
import ggc.pump.data.defs.PumpAdditionalDataType;
import ggc.pump.data.defs.PumpBaseType;
import ggc.pump.data.defs.PumpBolusType;
import ggc.pump.data.dto.BasalRatesDayDTO;
import ggc.pump.data.util.PumpBasalManager;
import ggc.pump.data.util.PumpBolusManager;
import ggc.pump.db.GGCPumpDb;
import ggc.pump.util.DataAccessPump;

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

public class GraphViewDailyPump extends GraphViewCGMSDailyAbstract
{

    private static final Logger LOG = LoggerFactory.getLogger(GraphViewDailyPump.class);

    private XYSeriesCollection datasetBasalBolusExtBG = new XYSeriesCollection();
    private XYSeriesCollection datasetBolusCH = new XYSeriesCollection();
    NumberAxis BGAxis;
    DateAxis dateAxis;
    NumberAxis insBUAxis;

    DataAccessPump dataAccessPump = DataAccessPump.getInstance();
    // GGCGraphUtil graphUtil = GGCGraphUtil.getInstance(dataAccessPump);

    PumpBasalManager basalManager = dataAccessPump.getBasalManager();
    PumpBolusManager bolusManager = dataAccessPump.getBolusManager();

    public boolean data_loaded = false;
    DeviceValuesDay dvd_data;


    /**
     * Constructor
     * 
     * @param currentCalendar
     */
    public GraphViewDailyPump(GregorianCalendar currentCalendar)
    {
        super(DataAccess.getInstance());
        this.currentCalendar = currentCalendar;
    }


    /**
     * Get Help Id
     * 
     * @return
     */
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
        return new Rectangle(100, 100, 600, 400);
    }


    /**
     * Load Data
     */
    public void loadData()
    {
        if (!data_loaded)
        {
            GGCPumpDb db = dataAccessPump.getDb();
            dvd_data = db.getDailyPumpValues(currentCalendar);
        }

        loadCGMSData();
    }


    /**
     * Get Data Set
     * 
     * @return AbstractDataset instance
     */
    public AbstractDataset getDataSet()
    {
        return this.datasetBasalBolusExtBG;
    }


    /**
     * Preprocess Data
     */
    public void preprocessData()
    {
        // CGMS

        datasetBasalBolusExtBG.removeAllSeries();
        datasetBolusCH.removeAllSeries();

        I18nControlAbstract ic = dataAccessPump.getI18nControlInstance();

        XYSeries BGSeries = new XYSeries(this.m_ic.getMessage("BLOOD_GLUCOSE"), true, true);
        XYSeries CHSeries = new XYSeries(this.m_ic.getMessage("CH_LONG"), true, true);
        XYSeries basalInsulinSeries = new XYSeries(ic.getMessage("BASAL"), true, true);
        XYSeries bolusInsulinSeries = new XYSeries(ic.getMessage("BOLUS"), true, true);
        XYSeries bolusExtInsulinSeries = new XYSeries(ic.getMessage("BOLUS") + " (Ext)", true, true);

        PumpValuesEntry pve = null;

        for (int i = 0; i < this.dvd_data.getRowCount(); i++)
        {
            pve = (PumpValuesEntry) this.dvd_data.getRowAt(i);

            long time = pve.getDateTimeObject().getGregorianCalendar().getTimeInMillis();

            // Bolus
            if (pve.getBaseType() == PumpBaseType.Bolus)
            {
                PumpBolusType bolusType = PumpBolusType.getByCode(pve.getSubType());

                if (bolusType == PumpBolusType.Normal || bolusType == PumpBolusType.Audio)
                {
                    createWiderBar(bolusInsulinSeries, time, bolusManager.getValue(pve), 20);
                }
                else if ((bolusType == PumpBolusType.Extended) || (bolusType == PumpBolusType.Multiwave))
                {
                    if (bolusType == PumpBolusType.Multiwave)
                    {
                        createWiderBar(bolusInsulinSeries, time, bolusManager.getValue(pve), 10);
                    }

                    float val = bolusManager.getExtendedValue(pve);
                    int dur = bolusManager.getDuration(pve);

                    // we make timepoints for 15 minutes and we divide amount
                    // between those elements
                    float durX = dur / 15;
                    val = val / durX;

                    bolusExtInsulinSeries.add(time - 1000, 0);
                    createLine(bolusExtInsulinSeries, time, val, dur);
                    bolusExtInsulinSeries.add(time + (1000 * 60 * dur) + 1000, 0);
                }
                else
                {
                    LOG.warn("Bolus Type: " + bolusType.name() + " not supported yet.");
                }
            }

            // Blood Glucose
            PumpAdditionalDataType additionalKeyWord = PumpAdditionalDataType.BloodGlucose;

            if (pve.getAdditionalData().containsKey(additionalKeyWord))
            {
                PumpValuesEntryExt pvext = pve.getAdditionalData().get(additionalKeyWord);

                createWiderBar(BGSeries, time, dataAccessPump.getDisplayedBG(pvext.getValue()), 10);
            }

            // Carbohydrates
            additionalKeyWord = PumpAdditionalDataType.Carbohydrates;

            if (pve.getAdditionalData().containsKey(additionalKeyWord))
            {
                PumpValuesEntryExt pvext = pve.getAdditionalData().get(additionalKeyWord);

                createWiderBar(CHSeries, time, dataAccessPump.getFloatValueFromString(pvext.getValue()), 20);
            }

        }

        BasalRatesDayDTO basalRates = basalManager.getBasalRatesForDate(currentCalendar, dvd_data);

        if (basalRates != null)
        {
            for (int i = 0; i < 24; i++)
            {
                GregorianCalendar gcBasal = getCalendarRange(true);
                gcBasal.add(Calendar.HOUR_OF_DAY, i);

                float basalForHour = basalRates.getBasalForHour(i);

                createLine(basalInsulinSeries, gcBasal.getTimeInMillis(), basalForHour, 60);

            }
        }

        datasetBasalBolusExtBG.addSeries(BGSeries);
        datasetBasalBolusExtBG.addSeries(basalInsulinSeries);
        datasetBasalBolusExtBG.addSeries(bolusExtInsulinSeries);

        datasetBolusCH.addSeries(bolusInsulinSeries);
        datasetBolusCH.addSeries(CHSeries);

        if (useCGMSData)
        {
            datasetBasalBolusExtBG.addSeries(CGMSSeries);
        }

    }


    private void createLine(XYSeries series, long timeMs, float value, int widthMinutes)
    {
        int widthSeconds = widthMinutes * 60;

        for (int k1 = 0; k1 < widthSeconds; k1++)
        {
            series.add(timeMs, value);
            timeMs += 1000;
        }
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
     * Set Plot
     * 
     * @param chart JFreeChart instance
     */
    public void setPlot(JFreeChart chart)
    {
        XYPlot plot = chart.getXYPlot();

        // renderer init
        XYLineAndShapeRenderer defaultRenderer = (XYLineAndShapeRenderer) plot.getRenderer();
        XYBarRenderer barRenderer = new XYBarRenderer();

        // axis
        dateAxis = (DateAxis) plot.getDomainAxis();
        BGAxis = (NumberAxis) plot.getRangeAxis();
        insBUAxis = new NumberAxis();

        // Bars test

        ColorSchemeH colorScheme = graphUtil.getColorScheme();

        RenderingHints rh = graphUtil.getRenderingHints();

        if (rh != null)
        {
            chart.setRenderingHints(rh);
        }

        plot.setRangeAxis(1, insBUAxis);
        plot.setRangeAxisLocation(1, AxisLocation.BOTTOM_OR_RIGHT);
        plot.setDataset(1, datasetBolusCH);
        plot.mapDatasetToRangeAxis(1, 1);
        plot.setRenderer(1, barRenderer);

        // graph_util.applyMarkers(plot);
        // plot.setRangeGridlinesVisible(false);
        // plot.setDomainGridlinesVisible(false);

        // XYLineAndShapeRenderer
        defaultRenderer.setSeriesPaint(0, getColor(colorScheme.getColor_bg()));
        defaultRenderer.setSeriesShapesVisible(0, true);

        defaultRenderer.setSeriesPaint(1, getColor(colorScheme.getColor_ins2()));
        defaultRenderer.setSeriesLinesVisible(1, true);

        defaultRenderer.setSeriesPaint(2, getColor(colorScheme.getColor_ins1()));
        defaultRenderer.setSeriesLinesVisible(2, true);

        if (useCGMSData)
        {
            defaultRenderer.setSeriesPaint(3, Color.magenta);
            defaultRenderer.setSeriesLinesVisible(3, true);
            defaultRenderer.setSeriesShapesVisible(3, false);
        }

        // BarRenderer
        barRenderer.setSeriesPaint(0, getColor(colorScheme.getColor_ins1()));
        barRenderer.setSeriesPaint(1, getColor(colorScheme.getColor_ch()));

        // Date Axis
        SimpleDateFormat sdf = new SimpleDateFormat(m_ic.getMessage("FORMAT_DATE_HOURS"));
        sdf.setTimeZone(TimeZoneUtil.getInstance().getEmptyTimeZone());

        dateAxis.setDateFormatOverride(sdf);
        dateAxis.setAutoRange(false);
        Range range = new DateRange(getCalendarRange(true).getTime(), getCalendarRange(false).getTime());
        dateAxis.setRange(range);
        dateAxis.setDefaultAutoRange(range);
        dateAxis.setTimeZone(TimeZoneUtil.getInstance().getEmptyTimeZone());

        // BG Axis
        BGAxis.setAutoRangeIncludesZero(true);

        // Insulin/CH Axis
        insBUAxis.setLabel(m_ic.getMessage("CH_LONG") + " / " + m_ic.getMessage("INSULIN"));
        insBUAxis.setAutoRangeIncludesZero(true);
    }


    /**
     * Create Chart
     */
    @Override
    public void createChart()
    {
        chart = ChartFactory.createTimeSeriesChart(null, this.m_ic.getMessage("AXIS_TIME_LABEL"),
            String.format(this.m_ic.getMessage("AXIS_VALUE_LABEL"), this.graphUtil.getUnitLabel()),
            datasetBasalBolusExtBG, true, true, false);

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
