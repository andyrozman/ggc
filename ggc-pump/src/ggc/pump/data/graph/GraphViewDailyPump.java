package ggc.pump.data.graph;

import java.awt.*;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
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

import com.atech.graphics.graphs.AbstractGraphViewAndProcessor;
import com.atech.utils.data.TimeZoneUtil;

import ggc.core.data.graph.GGCGraphUtil;
import ggc.core.db.hibernate.ColorSchemeH;
import ggc.core.util.DataAccess;
import ggc.plugin.data.DeviceValuesDay;
import ggc.plugin.util.DataAccessPlugInBase;
import ggc.pump.data.PumpValuesEntry;
import ggc.pump.data.PumpValuesEntryExt;
import ggc.pump.data.db.GGCPumpDb;
import ggc.pump.data.defs.PumpAdditionalDataType;
import ggc.pump.data.defs.PumpBasalType;
import ggc.pump.data.defs.PumpBaseType;
import ggc.pump.data.defs.PumpBolusType;
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

public class GraphViewDailyPump extends AbstractGraphViewAndProcessor // implements
// GraphViewInterface,
// GraphViewDataProcessorInterface
{

    Log LOG = LogFactory.getLog(GraphViewDailyPump.class);

    private XYSeriesCollection datasetBG = new XYSeriesCollection();
    private XYSeriesCollection datasetInsCH = new XYSeriesCollection();

    GregorianCalendar gc;
    // private GlucoValues gluco_values;
    // private GlucoValues gluco_values_prev;
    // TimeSeriesCollection datasetBG = new TimeSeriesCollection();
    // DefaultCategoryDataset datasetBG = new DefaultCategoryDataset();

    NumberAxis BGAxis;
    // private TimeSeriesCollection BGDataset = new TimeSeriesCollection();
    // private DailyValues data = new DailyValues();
    DateAxis dateAxis;
    NumberAxis insBUAxis;
    // private TimeSeriesCollection datasetInsCH = new TimeSeriesCollection();

    DataAccessPump da_local = DataAccessPump.getInstance();
    GGCGraphUtil graph_util = GGCGraphUtil.getInstance(da_local);

    DataAccess da_core = DataAccess.getInstance();

    /**
     * Data Loaded
     */
    public boolean data_loaded = false;

    DeviceValuesDay dvd_data;


    /**
     * Constructor
     * 
     * @param gc 
     */
    public GraphViewDailyPump(GregorianCalendar gc)
    {
        super(DataAccess.getInstance());
        this.gc = gc;
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
            GGCPumpDb db = da_local.getDb();
            dvd_data = db.getDailyPumpValues(gc);
        }
    }


    /**
     * Get Data Set
     * 
     * @return AbstractDataset instance
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

        // BG

        // Bolus

        // Basal

        // CH

        // CGMS

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
         * datasetBG.addSeries(ts);
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
         * datasetBG.addSeries(ts);
         * /*
         * datasetBG.clear();
         * System.out.println("Read HbA1c data:\n" +
         * hbValues.getPercentOfDaysInClass(0) + "\n" +
         * hbValues.getPercentOfDaysInClass(1) + "\n"
         * + hbValues.getPercentOfDaysInClass(2) + "\n" +
         * hbValues.getPercentOfDaysInClass(3) + "\n"
         * + hbValues.getPercentOfDaysInClass(4));
         * datasetBG.insertValue(0, m_ic.getMessage("DAYS_WITH_READINGS_0_1"),
         * hbValues.getPercentOfDaysInClass(0));
         * datasetBG.insertValue(1, m_ic.getMessage("DAYS_WITH_READINGS_2_3"),
         * hbValues.getPercentOfDaysInClass(1));
         * datasetBG.insertValue(2, m_ic.getMessage("DAYS_WITH_READINGS_4_5"),
         * hbValues.getPercentOfDaysInClass(2));
         * datasetBG.insertValue(3, m_ic.getMessage("DAYS_WITH_READINGS_6_7"),
         * hbValues.getPercentOfDaysInClass(3));
         * datasetBG.insertValue(4,
         * m_ic.getMessage("DAYS_WITH_READINGS_MORE_7"),
         * hbValues.getPercentOfDaysInClass(4));
         */

        datasetBG.removeAllSeries();
        datasetInsCH.removeAllSeries();

        /*
         * TimeSeries BGSeries = new
         * TimeSeries(this.m_ic.getMessage("BLOOD_GLUCOSE"), Hour.class);
         * TimeSeries CHSeries = new TimeSeries(this.m_ic.getMessage("CH_LONG"),
         * Hour.class);
         * TimeSeries ins1Series = new
         * TimeSeries(da_local.getSettings().getIns1Name(), Hour.class);
         * TimeSeries ins2Series = new
         * TimeSeries(da_local.getSettings().getIns2Name(), Hour.class);
         */

        XYSeries BGSeries = new XYSeries(this.m_ic.getMessage("BLOOD_GLUCOSE"), true, true); // ,
                                                                                             // Hour.class);

        XYSeries CHSeries = new XYSeries(this.m_ic.getMessage("CH_LONG"), true, true); // ,
                                                                                       // Hour.class);
        XYSeries basalInsulinSeries = new XYSeries("Basal insulin", true, true); // ,
                                                                                 // //
                                                                                 // Hour.class);
        XYSeries bolusInsulinSeries = new XYSeries("Bolus insulin", true, true); // ,
                                                                                 // //
                                                                                 // Hour.class);

        // int BGUnit = da_local.m_BG_unit;

        // DailyValuesRow

        // System.out.println("Rows: " +
        // this.gluco_values.getDailyValuesRowsCount());

        // DeviceValuesDay dvd = dvd_data..getRowAt(0);

        // BGSeries.add(0, 0.0f);
        // BGSeries.add(2400, 0.0f);

        PumpValuesEntry pve = null;

        List<PumpValuesEntry> tbrs = new ArrayList<PumpValuesEntry>();

        boolean basalEntryFound = false;

        for (int i = 0; i < this.dvd_data.getRowCount(); i++)
        {
            pve = (PumpValuesEntry) this.dvd_data.getRowAt(i);

            long time = pve.getDateTimeObject().getGregorianCalendar().getTimeInMillis();
            // long time = pve.getDateTimeObject().getGregorianCalendar();

            // System.out.println("Time: " + time);

            // Bolus
            if (pve.getBaseType() == PumpBaseType.Bolus)
            {
                PumpBolusType bolusType = PumpBolusType.getByCode(pve.getSubType());

                if (bolusType == PumpBolusType.Normal || bolusType == PumpBolusType.Audio)
                {
                    bolusInsulinSeries.add(time, da_local.getFloatValueFromString(pve.getValue()));
                }
                else
                {
                    LOG.warn("Bolus Type: " + bolusType.name() + " not supported yet.");
                }
            }

            // Basal
            // FIXME Basal two-passes check
            if (pve.getBaseType() == PumpBaseType.Basal)
            {
                // this might be problematic ?

                PumpBasalType basalType = PumpBasalType.getByCode(pve.getSubType());

                if (basalType == PumpBasalType.Value)
                {
                    bolusInsulinSeries.add(time, da_local.getFloatValueFromString(pve.getValue()));
                    basalEntryFound = true;
                }
                else if (basalType == PumpBasalType.Value)
                {
                    // CHANGE
                    LOG.warn("Basal Type: " + basalType.name() + " not supported yet.");
                }
                else if (PumpBasalType.isTemporaryBasalType(basalType))
                {
                    if (basalEntryFound)
                    {
                        // FIXME if basal - value or valueChange we plot TBR at
                        // once
                        LOG.warn("Basal Type: " + basalType.name() + " not supported yet.");
                    }
                    else
                    {
                        tbrs.add(pve);
                    }
                }
            }

            // Blood Glucose
            String additionalKeyWord = PumpAdditionalDataType.BloodGlucose.getTranslation();

            if (pve.getAdditionalData().containsKey(additionalKeyWord))
            {
                PumpValuesEntryExt pvext = pve.getAdditionalData().get(additionalKeyWord);
                BGSeries.add(time,
                    da_local.getBGValueByType(DataAccessPlugInBase.BG_MGDL, da_local.m_BG_unit, pvext.getValue()));
            }

            // Carbohydrates
            additionalKeyWord = PumpAdditionalDataType.Carbohydrates.getTranslation();
            if (pve.getAdditionalData().containsKey(additionalKeyWord))
            {
                PumpValuesEntryExt pvext = pve.getAdditionalData().get(additionalKeyWord);
                CHSeries.add(time, da_local.getFloatValueFromString(pvext.getValue()));
            }

        }

        if (tbrs.size() > 0)
        {
            LOG.warn("Basal Type: TBR not supported yet.");
        }

        // {
        // GregorianCalendar gc1 = (GregorianCalendar)gc.clone();
        // gc.set(Calendar.HOUR_OF_DAY, 0);
        // gc.set(Calendar.MINUTE, 0);
        // gc.set(Calendar.SECOND, 0);
        //
        // BGSeries.add(gc.getTimeInMillis(), 0.0f);
        //
        // gc.set(Calendar.HOUR_OF_DAY, 23);
        // gc.set(Calendar.MINUTE, 59);
        // gc.set(Calendar.SECOND, 59);
        //
        // BGSeries.add(gc.getTimeInMillis(), 0.0f);
        //
        // }

        datasetBG.addSeries(BGSeries);

        datasetInsCH.addSeries(CHSeries);
        datasetInsCH.addSeries(bolusInsulinSeries);
        datasetInsCH.addSeries(basalInsulinSeries);
        // basal

        /*
         * datasetInsCH.addSeries(CHSeries);
         * datasetInsCH.addSeries(ins1Series);
         * datasetInsCH.addSeries(ins2Series);
         */

    }


    private GregorianCalendar getGCRange(boolean start)
    {
        GregorianCalendar gc1 = (GregorianCalendar) gc.clone();

        if (start)
        {
            gc.set(Calendar.HOUR_OF_DAY, 0);
            gc.set(Calendar.MINUTE, 0);
            gc.set(Calendar.SECOND, 0);
            gc.set(Calendar.MILLISECOND, 0);
        }
        else
        {
            gc.set(Calendar.HOUR_OF_DAY, 23);
            gc.set(Calendar.MINUTE, 59);
            gc.set(Calendar.SECOND, 59);
            gc.set(Calendar.MILLISECOND, 59);
        }

        return gc;
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

        // chart.

        XYPlot plot = chart.getXYPlot();

        XYLineAndShapeRenderer defaultRenderer = (XYLineAndShapeRenderer) plot.getRenderer();
        XYLineAndShapeRenderer insBURenderer = new XYLineAndShapeRenderer();

        dateAxis = (DateAxis) plot.getDomainAxis();

        BGAxis = (NumberAxis) plot.getRangeAxis();
        insBUAxis = new NumberAxis();

        // Bars test
        // CategoryPlot cp = chart.getCategoryPlot();
        XYBarRenderer barRenderer = new XYBarRenderer();

        // cp.setRangeAxis(1, insBUAxis);
        // cp.setRangeAxisLocation(1, AxisLocation.BOTTOM_OR_RIGHT);
        // cp.setDataset(1, datasetInsCH);
        // cp.mapDatasetToRangeAxis(1, 1);

        ColorSchemeH colorScheme = graph_util.getColorScheme();

        RenderingHints rh = graph_util.getRenderingHints();

        if (rh != null)
        {
            chart.setRenderingHints(rh);
        }

        // chart.setBorderVisible(false);

        plot.setRangeAxis(1, insBUAxis);
        plot.setRangeAxisLocation(1, AxisLocation.BOTTOM_OR_RIGHT);
        plot.setDataset(1, datasetInsCH);
        plot.mapDatasetToRangeAxis(1, 1);
        plot.setRenderer(1, barRenderer);

        // graph_util.applyMarkers(plot);
        // plot.setRangeGridlinesVisible(false);
        // plot.setDomainGridlinesVisible(false);

        defaultRenderer.setSeriesPaint(0, da_core.getColor(colorScheme.getColor_bg()));

        barRenderer.setSeriesPaint(0, da_core.getColor(colorScheme.getColor_ch()));
        barRenderer.setSeriesPaint(1, da_core.getColor(colorScheme.getColor_ins1()));
        barRenderer.setSeriesPaint(2, da_core.getColor(colorScheme.getColor_ins2()));

        defaultRenderer.setSeriesShapesVisible(0, true);

        barRenderer.setSeriesShape(0, XYBarRenderer.DEFAULT_SHAPE);
        barRenderer.setSeriesShape(1, XYBarRenderer.DEFAULT_SHAPE);
        barRenderer.setSeriesShape(2, XYBarRenderer.DEFAULT_SHAPE);
        // barRenderer.setSeriesShapesVisible(1, true);
        // insBURenderer.setSeriesShapesVisible(2, true);

        insBURenderer.setSeriesLinesVisible(0, false);
        insBURenderer.setSeriesLinesVisible(1, false);
        insBURenderer.setSeriesLinesVisible(2, false);

        // datasetInsCH.addSeries(CHSeries);
        // datasetInsCH.addSeries(bolusInsulinSeries);
        // datasetInsCH.addSeries(basalInsulinSeries);

        // Date Axis
        SimpleDateFormat sdf = new SimpleDateFormat(m_ic.getMessage("FORMAT_DATE_HOURS"));
        sdf.setTimeZone(TimeZoneUtil.getInstance().getEmptyTimeZone());

        dateAxis.setDateFormatOverride(sdf);
        dateAxis.setAutoRange(false);
        Range range = new DateRange(getGCRange(true).getTime(), getGCRange(false).getTime());
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
            String.format(this.m_ic.getMessage("AXIS_VALUE_LABEL"), this.graph_util.getUnitLabel()), datasetBG, true,
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
