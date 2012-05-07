/*
 *  GGC - GNU Gluco Control
 *
 *  A pure java app to help you manage your diabetes.
 *
 *  See AUTHORS for copyright information.
 *
 *  This program is free software; you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation; either version 2 of the License, or
 *  (at your option) any later version.
 *
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with this program; if not, write to the Free Software
 *  Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA
 *
 *  Filename: JFDailyGraphView.java
 *  Purpose:  Drawing all data for one day. 
 *
 *  Author:   rumbi
 */

package ggc.gui.graphs;

import ggc.core.data.DailyValues;
import ggc.core.data.DailyValuesRow;
import ggc.core.db.hibernate.ColorSchemeH;
import ggc.core.util.DataAccess;
import ggc.core.util.MathUtils;

import java.awt.BorderLayout;
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
import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer;
import org.jfree.data.Range;
import org.jfree.data.time.DateRange;
import org.jfree.data.time.Hour;
import org.jfree.data.time.TimeSeries;
import org.jfree.data.time.TimeSeriesCollection;

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
 *  Filename:     ###--###  
 *  Description:  ###--###
 * 
 *  Author: andyrozman {andy@atech-software.com}  
 */


/**
 * This is a replacement for DailyGraphView using JFreeChart.
 * 
 * @author rumbi
 */
public class DailyGraphView extends JFAbstractGraphView
{
    private static final long serialVersionUID = -8136217038535558623L;
    NumberAxis BGAxis;
    private TimeSeriesCollection BGDataset = new TimeSeriesCollection();
    private DailyValues data = new DailyValues();
    DateAxis dateAxis;
    NumberAxis insBUAxis;
    private TimeSeriesCollection insBUDataset = new TimeSeriesCollection(); //TimeZone.getTimeZone(""));

    /**
     * Calls <code>{@link DailyGraphView#DailyGraphView(DailyValues)}</code>
     * with values for the current day.
     */
    public DailyGraphView()
    {
        this(DataAccess.getInstance().getDayStats(new GregorianCalendar()));
    }

    /**
     * Initialize and draw this graph with the passed
     * <code>{@link DailyValues data}</code>.
     * 
     * @param data
     *            The <code>{@link DailyValues}</code> to use to draw this
     *            graph.
     */
    public DailyGraphView(DailyValues data)
    {
        this(DataAccess.getInstance().getSettings().getSelectedColorScheme(), data);
    }

    /**
     * Initialize and draw this graph with the passed
     * <code>{@link DailyValues data}</code> using the passed
     * <code>{@link ColorSchemeH color scheme}</code>.
     * 
     * @param cs
     *            The <code>{@link ColorSchemeH}</code> to use to draw this
     *            graph.
     * @param dv
     *            The <code>{@link DailyValues}</code> to use to draw this
     *            graph.
     */
    public DailyGraphView(ColorSchemeH cs, DailyValues dv)
    {
        super();
        colorScheme = cs;
        data = dv;
        setBackground(backgroundColor);

        m_chart = ChartFactory.createTimeSeriesChart(null, translator.getMessage("AXIS_TIME_LABEL"), String.format(
            translator.getMessage("AXIS_VALUE_LABEL"), unitLabel), BGDataset, true, true, false);
        chartPanel = new ChartPanel(m_chart, false, true, true, false, true);
        chartPanel.setDomainZoomable(false);
        chartPanel.setRangeZoomable(true);
        setLayout(new BorderLayout());
        add(chartPanel, BorderLayout.CENTER);
        redraw();
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * ggc.gui.view.JFAbstractGraphView#drawFramework(org.jfree.chart.JFreeChart
     * )
     */
    @Override
    protected void drawFramework(JFreeChart _chart)
    {
        if ((data == null) || (_chart == null))
        {
            return;
        }

        XYPlot plot = _chart.getXYPlot();
        XYLineAndShapeRenderer defaultRenderer = (XYLineAndShapeRenderer) plot.getRenderer();
        XYLineAndShapeRenderer insBURenderer = new XYLineAndShapeRenderer();
        dateAxis = (DateAxis) plot.getDomainAxis();
        BGAxis = (NumberAxis) plot.getRangeAxis();
        insBUAxis = new NumberAxis();


        _chart.setBackgroundPaint(backgroundColor);
        _chart.setRenderingHints(renderingHints);
        _chart.setBorderVisible(false);

        plot.setRangeAxis(1, insBUAxis);
        plot.setRangeAxisLocation(1, AxisLocation.BOTTOM_OR_RIGHT);
        plot.setDataset(1, insBUDataset);
        plot.mapDatasetToRangeAxis(1, 1);
        plot.setRenderer(1, insBURenderer);
        applyMarkers(plot);
        plot.setRangeGridlinesVisible(false);
        plot.setDomainGridlinesVisible(false);

        defaultRenderer.setSeriesPaint(0, dataAccessInst.getColor(colorScheme.getColor_bg()));
        insBURenderer.setSeriesPaint(0, dataAccessInst.getColor(colorScheme.getColor_ch()));
        insBURenderer.setSeriesPaint(1, dataAccessInst.getColor(colorScheme.getColor_ins1()));
        insBURenderer.setSeriesPaint(2, dataAccessInst.getColor(colorScheme.getColor_ins2()));
        defaultRenderer.setSeriesShapesVisible(0, true);
        insBURenderer.setSeriesShapesVisible(0, true);
        insBURenderer.setSeriesShapesVisible(1, true);
        insBURenderer.setSeriesShapesVisible(2, true);

        dateAxis.setDateFormatOverride(new SimpleDateFormat(translator.getMessage("FORMAT_DATE_HOURS")));
        dateAxis.setAutoRange(false);
        GregorianCalendar dayStart = new GregorianCalendar(2006, 3, 4, 0, 0, 0);      //        (new ATechDate(data.getDate() * 10000)).getGregorianCalendar();
        GregorianCalendar dayEnd = new GregorianCalendar(2006, 3, 4, 23, 59, 59); //(new ATechDate(data.getDate() * 10000 + 2359)).getGregorianCalendar();
        //dayStart.setTime(dataAccessInst.getDateTimeAsDateObject(data.getDate() * 10000));
        //dayEnd.setTime(dataAccessInst.getDateTimeAsDateObject(data.getDate() * 10000 + 2359));
        //dayStart.set(Calendar.SECOND, 0);
        //dayEnd.set(Calendar.SECOND, 59);
        dateAxis.setRange(new DateRange(dayStart.getTime(), dayEnd.getTime()));
        //dateAxis.setDefaultAutoRange(new Range(dayStart.getTime(), dayEnd.getTime()));

        BGAxis.setAutoRangeIncludesZero(true);

        insBUAxis.setLabel(translator.getMessage("CH_LONG") + " / " + translator.getMessage("INSULIN"));
        insBUAxis.setAutoRangeIncludesZero(true);
        
        /*
        System.out.println("TimeZones\n================================\n");
        
        String tz[] = TimeZone.getAvailableIDs();
        
        for(int i =0; i<tz.length; i++)
        {
            System.out.println(tz[i]);
        }*/
        
        
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * ggc.gui.view.JFAbstractGraphView#drawValues(org.jfree.chart.JFreeChart)
     */
    @SuppressWarnings("deprecation")
    @Override
    protected void drawValues(JFreeChart chart)
    {
        DailyValuesRow row;
        Hour time;

        if ((BGDataset == null) || (insBUDataset == null))
        {
            return;
        }

        BGDataset.removeAllSeries();
        insBUDataset.removeAllSeries();

        if ((data == null) || (chart == null))
        {
            return;
        }

        TimeSeries BGSeries = new TimeSeries(translator.getMessage("BLOOD_GLUCOSE"), Hour.class);
        TimeSeries CHSeries = new TimeSeries(translator.getMessage("CH_LONG"), Hour.class);
        TimeSeries ins1Series = new TimeSeries(settings.getIns1Name(), Hour.class);
        TimeSeries ins2Series = new TimeSeries(settings.getIns2Name(), Hour.class);

        for (int i = 0; i < data.getRowCount(); i++)
        {
            row = data.getRow(i);
            time = new Hour(row.getDateTimeAsDate());

            if (row.getBG(BGUnit) > 0)
            {
                //System.out.println("BGUnit = " + time);
                if (BGSeries.getDataItem(time) == null)
                {
                    BGSeries.add(time, row.getBG(BGUnit));
                }
                else
                {
                    BGSeries.addOrUpdate(time, MathUtils.getAverage(row.getBG(BGUnit), BGSeries.getDataItem(time).getValue()));
                }
            }
            
            if (row.getCH() > 0)
            {
                if (CHSeries.getDataItem(time) == null)
                {
                    CHSeries.add(time, row.getCH());
                }
                else
                {
                    CHSeries.addOrUpdate(time, MathUtils.getAverage(row.getCH(), CHSeries.getDataItem(time).getValue()));
                }
            }
            
            if (row.getIns1() > 0)
            {
                if (ins1Series.getDataItem(time) == null)
                {
                    ins1Series.add(time, row.getIns1());
                }
                else
                {
                    ins1Series.addOrUpdate(time, MathUtils.getAverage(row.getIns1(), ins1Series.getDataItem(time)
                            .getValue()));
                }
            }
            if (row.getIns2() > 0)
            {
                if (ins2Series.getDataItem(time) == null)
                {
                    ins2Series.add(time, row.getIns2());
                }
                else
                {
                    ins2Series.addOrUpdate(time, MathUtils.getAverage(row.getIns2(), ins2Series.getDataItem(time)
                            .getValue()));
                }
            }
        }

        BGDataset.addSeries(BGSeries);
        insBUDataset.addSeries(CHSeries);
        insBUDataset.addSeries(ins1Series);
        insBUDataset.addSeries(ins2Series);
    }

    /**
     * Set the <code>{@link DailyValues data}</code> to be used for drawing this
     * graph and trigger a redraw.
     * 
     * @param dV
     *            The <code>{@link DailyValues}</code> to use for drawing this
     *            graph.
     */
    public void setDailyValues(DailyValues dV)
    {
        data = dV;
        redraw();
    }
}
