package ggc.cgms.data.graph;

import java.awt.*;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.AxisLocation;
import org.jfree.chart.axis.DateAxis;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.ValueMarker;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.XYItemRenderer;
import org.jfree.data.general.AbstractDataset;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;

import com.atech.graphics.graphs.AbstractGraphViewAndProcessor;
import com.atech.graphics.graphs.DateAxisSupportInterface;
import com.atech.graphics.graphs.GraphUtil;
import com.atech.utils.data.ATechDate;

import ggc.cgms.data.CGMSValuesSubEntry;
import ggc.cgms.data.defs.CGMSBaseDataType;
import ggc.cgms.util.CGMSUtil;
import ggc.cgms.util.DataAccessCGMS;
import ggc.core.data.graph.v1.GGCGraphUtil;
import ggc.plugin.data.DeviceValuesDay;

/**
 *  Application: GGC - GNU Gluco Control
 *  Plug-in: CGMS Tool (support for CGMS devices)
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
 *  Filename: CGMDataType
 *  Description: CGMS Data types, as used in database (undefined at this time)
 *
 *  Author: Andy {andy@atech-software.com}
 */

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

// FIXME
// Configuration for CGMS:
// Color for background,
// Color for reading,
// Color for Calibration,
// Low Glucose Range,
// High Glucose Range

public class CGMSGraphViewDaily extends AbstractGraphViewAndProcessor implements DateAxisSupportInterface
{

    GregorianCalendar currentCalendar;
    XYSeriesCollection dataset = new XYSeriesCollection();

    DataAccessCGMS da_local = DataAccessCGMS.getInstance();
    GGCGraphUtil graph_util = GGCGraphUtil.getInstance(da_local);
    private ArrayList<CGMSValuesSubEntry> dayDataList;

    GregorianCalendar fromDate, tillDate;
    private long dateInAtechDate;


    /**
     * Constructor
     *
     * @param currentCalendar
     */
    public CGMSGraphViewDaily(GregorianCalendar currentCalendar)
    {
        super(DataAccessCGMS.getInstance());
        this.currentCalendar = currentCalendar;
        initDates();
    }


    /**
     * Constructor
     *
     * @param currentCalendar
     */
    public CGMSGraphViewDaily(GregorianCalendar currentCalendar, ArrayList<CGMSValuesSubEntry> dailyDataList)
    {
        super(DataAccessCGMS.getInstance());
        this.currentCalendar = currentCalendar;
        this.dayDataList = dailyDataList;
        initDates();
    }


    /**
     * Get Help Id
     * 
     * @return
     */
    public String getHelpId()
    {
        return "GGC_BG_Graph_Daily";
    }


    /**
     * Get Viewer Dialog Bounds (used by GraphViewer)
     * 
     * @return Rectangle object
     */
    @Override
    public Rectangle getViewerDialogBounds()
    {
        return new Rectangle(0, 0, 640, 480);
    }


    /**
     * Load Data
     */
    public void loadData()
    {
        if (dayDataList == null)
        {
            DeviceValuesDay dayData = da_local.getDb().getDailyCGMSValues(this.currentCalendar);
            dayData.sort();

            dayDataList = CGMSUtil.getDataList(dayData.getList());
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
        // System.out.println("Preprocess Data");

        dataset.removeAllSeries();

        XYSeries bgReadingsSeries = new XYSeries(this.m_ic.getMessage("CGMS_READING"), true, true);
        XYSeries bgCalibrationSeries = new XYSeries(this.m_ic.getMessage("CALIBRATION_READINGS"), true, true);

        for (CGMSValuesSubEntry entry : dayDataList)
        {
            if ((entry.getTypeObject() != CGMSBaseDataType.SensorReading)
                    && (entry.getTypeObject() != CGMSBaseDataType.SensorCalibration))
            {
                continue;
            }

            int value = Integer.parseInt(entry.value);

            if (value < 20)
                continue;

            long time = getDatetimeInMs(entry.time); // .getDatetimeInMs();

            if (entry.getTypeObject() == CGMSBaseDataType.SensorReading)
            {
                bgReadingsSeries.add(time, CGMSUtil.getBGInCorrectFormat(value));
            }
            else
            {
                bgCalibrationSeries.add(time, CGMSUtil.getBGInCorrectFormat(value));
            }
        }

        dataset.addSeries(bgReadingsSeries);
        dataset.addSeries(bgCalibrationSeries);
    }


    /**
     * Get Title (used by GraphViewer)
     * 
     * @return title as string 
     */
    @Override
    public String getTitle()
    {
        return m_ic.getMessage("CGMS_DAILY_GRAPH") + " [" + currentCalendar.get(Calendar.DAY_OF_MONTH) + "."
                + (currentCalendar.get(Calendar.MONTH) + 1) + "." + currentCalendar.get(Calendar.YEAR) + "]";
    }


    public void setPlot(JFreeChart chart)
    {
        setPlotStatic(chart);
    }


    /**
     * Set Plot
     * 
     * @param chart JFreeChart instance
     */
    public void setPlotStatic(JFreeChart chart)
    {
        XYPlot plot = chart.getXYPlot();
        XYItemRenderer defaultRenderer = (XYItemRenderer) plot.getRenderer();

        DateAxis dateAxis;
        NumberAxis BGAxis;

        BGAxis = (NumberAxis) plot.getRangeAxis();

        GGCGraphUtil graphUtil = GGCGraphUtil.getInstance(this.m_da);

        // ColorSchemeH colorScheme = graph_util.getColorScheme();

        chart.setBackgroundPaint(Color.white);

        plot.setBackgroundPaint(new Color(197, 230, 241));
        plot.setRangeGridlinePaint(Color.darkGray);
        plot.setDomainGridlinePaint(Color.gray);

        RenderingHints rh = graph_util.getRenderingHints();

        if (rh != null)
        {
            chart.setRenderingHints(rh);
        }

        chart.setBorderVisible(false);

        plot.setRangeAxisLocation(1, AxisLocation.BOTTOM_OR_RIGHT);
        plot.mapDatasetToRangeAxis(1, 1);

        plot.setRangeGridlinesVisible(true);
        plot.setDomainGridlinesVisible(true);

        defaultRenderer.setSeriesPaint(0, Color.green.darker());
        defaultRenderer.setSeriesPaint(1, Color.blue);

        defaultRenderer.setSeriesShape(1, this.graph_util.getShape(GraphUtil.SHAPE_TRIANGLE_UP));

        defaultRenderer.setBasePaint(Color.white);

        dateAxis = graphUtil.prepareDateAxis(new DateAxis(), this);

        BGAxis.setAutoRangeIncludesZero(true);

        plot.setDomainAxis(dateAxis);

        ValueMarker markerHigh = new ValueMarker(240); // position is the value
                                                       // on
        // the axis
        markerHigh.setPaint(Color.red);

        ValueMarker markerLow = new ValueMarker(80); // position is the value on
                                                     // the axis
        markerLow.setPaint(Color.magenta);

        plot.addRangeMarker(markerHigh);
        plot.addRangeMarker(markerLow);
    }


    private void initDates()
    {
        fromDate = GraphUtil.prepareFromCalendar(this.currentCalendar);
        tillDate = GraphUtil.prepareTillCalendar(this.currentCalendar);

        dateInAtechDate = ATechDate.getATDateTimeFromGC(fromDate, ATechDate.FORMAT_DATE_AND_TIME_S);

        // GraphUtil.setDataAccess(this.da_local);
    }


    public long getDatetimeInMs(int time)
    {
        return ATechDate.getGregorianCalendar(ATechDate.FORMAT_DATE_AND_TIME_S, (dateInAtechDate + time))
                .getTimeInMillis();
    }


    /**
     * Create Chart
     */
    @Override
    public void createChart()
    {
        chart = ChartFactory.createScatterPlot(null, this.m_ic.getMessage("AXIS_TIME_LABEL"),
            String.format(this.m_ic.getMessage("AXIS_VALUE_LABEL"), this.graph_util.getUnitLabel()), dataset,
            PlotOrientation.VERTICAL, true, true, true);

        this.setPlot(chart);
    }


    /**
     * Create Chart Panel
     */
    @Override
    public void createChartPanel()
    {
        this.chart_panel = new ChartPanel(getChart(), true, true, true, true, true);
    }


    public GregorianCalendar getFrom()
    {
        return this.fromDate;
    }


    public GregorianCalendar getTill()
    {
        return this.tillDate;
    }
}
