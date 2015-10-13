package ggc.plugin.graph.data;

import java.awt.*;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.CollectionUtils;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.AxisLocation;
import org.jfree.chart.axis.DateAxis;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.ValueMarker;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer;
import org.jfree.data.RangeType;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;

import com.atech.graphics.graphs.GraphUtil;
import com.atech.i18n.I18nControlAbstract;
import com.atech.utils.data.ATechDate;
import com.atech.utils.data.ATechDateType;

import ggc.core.data.graph.GGCGraphUtil;
import ggc.core.db.hibernate.cgms.CGMSDataH;
import ggc.plugin.db.PluginDb;
import ggc.plugin.report.data.cgms.CGMSDayData;
import ggc.plugin.report.data.cgms.CGMSDayDataEntry;
import ggc.plugin.util.DataAccessPlugInBase;

public class CGMSGraphDataHandler
{

    public Map<Long, CGMSDayData> getCGMSReadingsRange(DataAccessPlugInBase dataAccess, GregorianCalendar gcFrom,
            GregorianCalendar gcTill)
    {
        Map<Long, CGMSDayData> cgmsDataMap = new HashMap<Long, CGMSDayData>();

        PluginDb db = dataAccess.getPlugInDb();

        List<CGMSDataH> rangeCGMSValuesRaw = db.getRangeCGMSValuesRaw(gcFrom, gcTill, "dv.base_type=1");

        for (CGMSDataH rawData : rangeCGMSValuesRaw)
        {
            CGMSDayData dayData = new CGMSDayData(rawData);
            cgmsDataMap.put(dayData.getDateTime(), dayData);
        }

        return cgmsDataMap;
    }


    public JFreeChart createDailyChartForReport(DataAccessPlugInBase dataAccess, CGMSDayData cgmsDayData)
    {
        I18nControlAbstract m_ic = dataAccess.getI18nControlInstance();

        GGCGraphUtil graphUtil = GGCGraphUtil.getInstance(dataAccess);

        XYSeriesCollection collection = new XYSeriesCollection();
        collection.addSeries(getXYSeriesFromCGMSDayData(cgmsDayData, dataAccess));

        JFreeChart chart = ChartFactory.createScatterPlot(null, m_ic.getMessage("AXIS_TIME_LABEL"),
            String.format(m_ic.getMessage("AXIS_VALUE_LABEL"), graphUtil.getUnitLabel()), collection,
            PlotOrientation.VERTICAL, false, false, false);

        this.setPlot(chart, graphUtil, cgmsDayData.getDateTimeObject().getGregorianCalendar(), dataAccess, true);
        return chart;
    }


    public XYSeries getCGMSDailyReadings(DataAccessPlugInBase dataAccess, GregorianCalendar currentCalendar)
    {
        PluginDb db = dataAccess.getPlugInDb();

        List<CGMSDataH> rangeCGMSValuesRaw = db.getRangeCGMSValuesRaw(currentCalendar, currentCalendar,
            "dv.base_type=1");

        if (CollectionUtils.isNotEmpty(rangeCGMSValuesRaw))
        {
            CGMSDayData dayData = new CGMSDayData(rangeCGMSValuesRaw.get(0));
            return getXYSeriesFromCGMSDayData(dayData, dataAccess);
        }

        return null;
    }


    public XYSeries getXYSeriesFromCGMSDayData(CGMSDayData cgmsDayData, DataAccessPlugInBase dataAccess)
    {
        XYSeries bgReadingsSeries = new XYSeries(dataAccess.getI18nControlInstance().getMessage("CGMS_DEVICE_SHORT"),
                true, true);

        long dateInAtechDate = cgmsDayData.getDateTime() * 1000000;

        for (CGMSDayDataEntry entry : cgmsDayData.getSubEntryList())
        {
            if (entry.value < 20)
                continue;

            long time = getDatetimeInMs(dateInAtechDate, entry.time); // .getDatetimeInMs();

            bgReadingsSeries.add(time, dataAccess.getDisplayedBG(entry.value));
        }

        return bgReadingsSeries;
    }


    public long getDatetimeInMs(long dateInAtechDate, int time)
    {
        return ATechDate.getGregorianCalendar(ATechDateType.DateAndTimeSec, (dateInAtechDate + time)).getTimeInMillis();
    }


    /**
     * Set Plot
     *
     * @param chart JFreeChart instance
     */
    public void setPlot(JFreeChart chart, GGCGraphUtil graphUtil, GregorianCalendar calendarToday,
            DataAccessPlugInBase dataAccess, boolean isForReport)
    {
        XYPlot plot = chart.getXYPlot();

        XYLineAndShapeRenderer defaultRenderer = (XYLineAndShapeRenderer) plot.getRenderer();

        DateAxis dateAxis;

        plot.setRangeGridlinePaint(Color.darkGray);
        plot.setDomainGridlinePaint(Color.gray);

        RenderingHints rh = graphUtil.getRenderingHints();

        if (rh != null)
        {
            chart.setRenderingHints(rh);
        }

        chart.setBorderVisible(false);

        plot.setRangeAxisLocation(1, AxisLocation.BOTTOM_OR_RIGHT);
        plot.mapDatasetToRangeAxis(1, 1);

        plot.setRangeGridlinesVisible(true);
        plot.setDomainGridlinesVisible(true);

        if (isForReport)
        {
            chart.setBackgroundPaint(Color.white);
            plot.setBackgroundPaint(Color.white);

            defaultRenderer.setSeriesPaint(0, Color.green.darker());
            // defaultRenderer.setSeriesShape(0, );
            defaultRenderer.setSeriesShapesVisible(0, false);
            defaultRenderer.setSeriesLinesVisible(0, true);
            defaultRenderer.setBasePaint(Color.white);
        }
        else
        {
            chart.setBackgroundPaint(Color.white);
            plot.setBackgroundPaint(new Color(197, 230, 241));

            defaultRenderer.setSeriesPaint(0, Color.green.darker());
            defaultRenderer.setSeriesPaint(1, Color.blue);
            defaultRenderer.setSeriesShape(1, graphUtil.getShape(GraphUtil.SHAPE_TRIANGLE_UP));
            defaultRenderer.setBasePaint(Color.white);

        }

        dateAxis = graphUtil.prepareDateAxis(new DateAxis(), calendarToday);

        NumberAxis bgAxis = (NumberAxis) plot.getRangeAxis();
        bgAxis.setAutoRangeIncludesZero(true);
        bgAxis.setAutoRange(false);
        // bgAxis.setDefaultAutoRange(new Range(0,
        // dataAccess.getDisplayedBG(400)));
        bgAxis.setRangeType(RangeType.POSITIVE);
        bgAxis.setRange(0, dataAccess.getDisplayedBG(400));

        plot.setDomainAxis(dateAxis);

        // FIXME fix hardcoded values
        ValueMarker markerHigh = new ValueMarker(dataAccess.getDisplayedBG(240));
        markerHigh.setPaint(Color.red);

        ValueMarker markerLow = new ValueMarker(dataAccess.getDisplayedBG(80));
        markerLow.setPaint(Color.magenta);

        plot.addRangeMarker(markerHigh);
        plot.addRangeMarker(markerLow);
    }

}
