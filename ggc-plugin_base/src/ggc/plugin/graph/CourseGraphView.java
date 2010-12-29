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
 *  Filename: CourseGraphView.java
 *  Purpose:  View to draw the course.
 *
 *  Author:   schultd
 *  Author:   reini
 *  
 */

package ggc.plugin.graph;

import ggc.core.data.DailyValuesRow;
import ggc.core.data.GlucoValues;
import ggc.core.data.PlotData;
import ggc.core.data.ReadablePlotData;
import ggc.core.util.MathUtils;

import java.awt.BorderLayout;
import java.text.SimpleDateFormat;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.AxisLocation;
import org.jfree.chart.axis.DateAxis;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer;
import org.jfree.data.time.Day;
import org.jfree.data.time.TimeSeries;
import org.jfree.data.time.TimeSeriesCollection;

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


public class CourseGraphView extends JFAbstractGraphView
{
    private static final long serialVersionUID = -7960828650875426272L;
    private GlucoValues data = new GlucoValues();
    private ReadablePlotData plotData = null;
    private TimeSeriesCollection BGDataset = new TimeSeriesCollection();
    private TimeSeriesCollection readingsDataset = new TimeSeriesCollection();
    private TimeSeriesCollection sumDataset = new TimeSeriesCollection();
    private TimeSeriesCollection averageDataset = new TimeSeriesCollection();

    /**
     * Draws an empty graph.
     */
    public CourseGraphView()
    {
        this(new GlucoValues(), new PlotData());
    }

    /**
     * Initialize and draw an empty graph with the passed
     * <code>{@link PlotData data}</code>.<br>
     * <code>{@link #setGlucoValues(GlucoValues)}</code> should be used to add
     * data to the graph.
     * 
     * @param data
     *            The <code>{@link PlotData}</code> specifying what data to
     *            plot.
     */
    public CourseGraphView(PlotData data)
    {
        this(new GlucoValues(), data);
    }

    /**
     * Initialize and draw this graph with the passed
     * <code>{@link GlucoValues}</code>, plotting the data specified by the
     * passed <code>{@link PlotData}</code>.
     * 
     * @param gV
     *            The <code>{@link GlucoValues}</code> containing the plotted
     *            data.
     * @param data
     *            The <code>{@link PlotData}</code> specifying what data to
     *            plot.
     */
    public CourseGraphView(GlucoValues gV, PlotData data)
    {
        super();
        this.data = gV;
        this.plotData = data;
        setBackground(backgroundColor);

        m_chart = ChartFactory.createTimeSeriesChart(null, translator.getMessage("AXIS_TIME_LABEL"), String.format(
            translator.getMessage("AXIS_VALUE_LABEL"), unitLabel), BGDataset, true, true, false);
        chartPanel = new ChartPanel(m_chart, false, true, true, false, true);
        chartPanel.setDomainZoomable(true);
        chartPanel.setRangeZoomable(true);
        setLayout(new BorderLayout());
        add(chartPanel, BorderLayout.CENTER);
        redraw();
    }

    /**
     * Set the <code>{@link GlucoValues data}</code> to be used for drawing this
     * graph and trigger a redraw.
     * 
     * @param gV
     *            The <code>{@link GlucoValues}</code> to use for drawing this
     *            graph.
     */
    public void setGlucoValues(GlucoValues gV)
    {
        this.data = gV;
        redraw();
    }

    /**
     * @param data
     *            the data to set
     */
    public void setData(ReadablePlotData data)
    {
        this.plotData = data;
        redraw();
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * ggc.gui.graphs.JFAbstractGraphView#drawFramework(org.jfree.chart.JFreeChart
     * )
     */
    @Override
    protected void drawFramework(JFreeChart chart)
    {
        if (chart == null)
        {
            return;
        }

        XYPlot plot = chart.getXYPlot();
        XYLineAndShapeRenderer defaultRenderer = (XYLineAndShapeRenderer) plot.getRenderer();
        XYLineAndShapeRenderer averageRenderer = new XYLineAndShapeRenderer();
        XYLineAndShapeRenderer sumRenderer = new XYLineAndShapeRenderer();
        XYLineAndShapeRenderer readingsRenderer = new XYLineAndShapeRenderer();
        NumberAxis BGAxis = (NumberAxis) plot.getRangeAxis();
        NumberAxis averageAxis = new NumberAxis();
        NumberAxis sumAxis = new NumberAxis();
        NumberAxis readingsAxis = new NumberAxis();
        DateAxis dateAxis = (DateAxis) plot.getDomainAxis();

        if (plotData.isPlotCHDayAvg() || plotData.isPlotIns1DayAvg() || plotData.isPlotIns2DayAvg()
                || plotData.isPlotInsTotalDayAvg() || plotData.isPlotInsPerCH())
        {
            plot.setRangeAxis(1, averageAxis);
            plot.setRangeAxisLocation(1, AxisLocation.BOTTOM_OR_RIGHT);
            plot.setDataset(1, averageDataset);
            plot.mapDatasetToRangeAxis(1, 1);
            plot.setRenderer(1, averageRenderer);
        }

        if (plotData.isPlotCHSum() || plotData.isPlotIns1Sum() || plotData.isPlotIns2Sum()
                || plotData.isPlotInsTotalSum())
        {
            plot.setRangeAxis(2, sumAxis);
            plot.setRangeAxisLocation(2, AxisLocation.BOTTOM_OR_RIGHT);
            plot.setDataset(2, sumDataset);
            plot.mapDatasetToRangeAxis(2, 2);
            plot.setRenderer(2, sumRenderer);
        }

        if (plotData.isPlotMeals() || plotData.isPlotBGReadings())
        {
            plot.setRangeAxis(3, readingsAxis);
            plot.setRangeAxisLocation(3, AxisLocation.BOTTOM_OR_LEFT);
            plot.setDataset(3, readingsDataset);
            plot.mapDatasetToRangeAxis(3, 3);
            plot.setRenderer(3, readingsRenderer);
        }

        applyMarkers(plot);
        plot.setRangeGridlinesVisible(false);
        plot.setDomainGridlinesVisible(false);

        dateAxis.setDateFormatOverride(new SimpleDateFormat(translator.getMessage("FORMAT_DATE_DAYS")));

        defaultRenderer.setSeriesPaint(0, dataAccessInst.getColor(settings.getSelectedColorScheme().getColor_bg_avg()));

        averageRenderer.setSeriesPaint(0, dataAccessInst.getColor(settings.getSelectedColorScheme().getColor_ch())
                .darker());
        averageRenderer.setSeriesPaint(1, dataAccessInst.getColor(settings.getSelectedColorScheme().getColor_ins1())
                .darker());
        averageRenderer.setSeriesPaint(2, dataAccessInst.getColor(settings.getSelectedColorScheme().getColor_ins2())
                .darker());
        averageRenderer.setSeriesPaint(3, dataAccessInst.getColor(settings.getSelectedColorScheme().getColor_ins())
                .darker());
        averageRenderer.setSeriesPaint(4, dataAccessInst.getColor(settings.getSelectedColorScheme()
                .getColor_ins_perbu()));

        sumRenderer.setSeriesPaint(0, dataAccessInst.getColor(settings.getSelectedColorScheme().getColor_ch()));
        sumRenderer.setSeriesPaint(1, dataAccessInst.getColor(settings.getSelectedColorScheme().getColor_ins1()));
        sumRenderer.setSeriesPaint(2, dataAccessInst.getColor(settings.getSelectedColorScheme().getColor_ins2()));
        sumRenderer.setSeriesPaint(3, dataAccessInst.getColor(settings.getSelectedColorScheme().getColor_ins()));

        readingsRenderer.setSeriesPaint(0, dataAccessInst.getColor(settings.getSelectedColorScheme().getColor_bg()));
        readingsRenderer.setSeriesPaint(1, dataAccessInst.getColor(settings.getSelectedColorScheme().getColor_ch())
                .brighter());

        chart.setBackgroundPaint(backgroundColor);
        chart.setRenderingHints(renderingHints);
        chart.setBorderVisible(false);

        BGAxis.setAutoRangeIncludesZero(true);
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * ggc.gui.graphs.JFAbstractGraphView#drawValues(org.jfree.chart.JFreeChart)
     */
    @Override
    protected void drawValues(JFreeChart chart)
    {
        DailyValuesRow row;
        Day time;

        if (BGDataset == null)
        {
            return;
        }

        BGDataset.removeAllSeries();
        averageDataset.removeAllSeries();
        sumDataset.removeAllSeries();
        readingsDataset.removeAllSeries();

        if (data == null)
        {
            return;
        }

        TimeSeries BGAvgSeries = new TimeSeries(translator.getMessage("AVG_BG_PER_DAY"), Day.class);
        TimeSeries BGReadingsSeries = new TimeSeries(translator.getMessage("READINGS"), Day.class);
        TimeSeries CHAvgSeries = new TimeSeries(translator.getMessage("AVG_MEAL_SIZE"), Day.class);
        TimeSeries CHSumSeries = new TimeSeries(translator.getMessage("SUM_BU"), Day.class);
        TimeSeries ins1AvgSeries = new TimeSeries(translator.getMessage("AVG") + " " + settings.getIns1Name(),
                Day.class);
        TimeSeries ins1SumSeries = new TimeSeries(translator.getMessage("SUM") + " " + settings.getIns1Name(),
                Day.class);
        TimeSeries ins2AvgSeries = new TimeSeries(translator.getMessage("AVG") + " " + settings.getIns2Name(),
                Day.class);
        TimeSeries ins2SumSeries = new TimeSeries(translator.getMessage("SUM") + " " + settings.getIns2Name(),
                Day.class);
        TimeSeries insAvgSeries = new TimeSeries(translator.getMessage("AVG_INS"), Day.class);
        TimeSeries insSumSeries = new TimeSeries(translator.getMessage("SUM_INSULIN"), Day.class);
        TimeSeries insPerCHSeries = new TimeSeries(translator.getMessage("INS_SLASH_BU"), Day.class);
        TimeSeries mealsSeries = new TimeSeries(translator.getMessage("MEALS"), Day.class);

        int days = data.getDailyValuesItemsCount();
        for (int i = 0; i < days; i++)
        {
            for (int j = 0; j < data.getDailyValuesItem(i).getRowCount(); j++)
            {
                row = data.getDailyValuesItem(i).getRow(j);
                time = new Day(row.getDateTimeAsDate());

                if (row.getBG(BGUnit) > 0)
                {
                    if (BGAvgSeries.getDataItem(time) == null)
                    {
                        BGAvgSeries.add(time, row.getBG(BGUnit));
                    }
                    else
                    {
                        BGAvgSeries.addOrUpdate(time, MathUtils.getAverage(row.getBG(BGUnit), BGAvgSeries.getDataItem(
                            time).getValue()));
                    }
                }
                if (row.getBG(BGUnit) > 0)
                {
                    if (BGReadingsSeries.getDataItem(time) == null)
                    {
                        BGReadingsSeries.add(time, 1);
                    }
                    else
                    {
                        BGReadingsSeries.addOrUpdate(time, MathUtils.add(1, BGReadingsSeries.getDataItem(time)
                                .getValue()));
                    }
                }

                if (row.getCH() > 0)
                {
                    if (CHAvgSeries.getDataItem(time) == null)
                    {
                        CHAvgSeries.add(time, row.getCH());
                    }
                    else
                    {
                        CHAvgSeries.addOrUpdate(time, MathUtils.getAverage(row.getCH(), CHAvgSeries.getDataItem(time)
                                .getValue()));
                    }
                }
                if (row.getCH() > 0)
                {
                    if (CHSumSeries.getDataItem(time) == null)
                    {
                        CHSumSeries.add(time, row.getCH());
                    }
                    else
                    {
                        CHSumSeries.addOrUpdate(time, MathUtils.add(row.getCH(), CHSumSeries.getDataItem(time)
                                .getValue()));
                    }
                }

                if (row.getIns1() > 0)
                {
                    // ins 1 avg
                    if (ins1AvgSeries.getDataItem(time) == null)
                    {
                        ins1AvgSeries.add(time, row.getIns1());
                    }
                    else
                    {
                        ins1AvgSeries.addOrUpdate(time, MathUtils.getAverage(row.getIns1(), ins1AvgSeries.getDataItem(
                            time).getValue()));
                    }

                    // ins 1 sum
                    if (ins1SumSeries.getDataItem(time) == null)
                    {
                        ins1SumSeries.add(time, row.getIns1());
                    }
                    else
                    {
                        float sum = ((Number)ins1SumSeries.getDataItem(time).getValue()).floatValue();
                        sum += row.getIns1();
                            
                        ins1SumSeries.addOrUpdate(time, sum); 
                    }
                    
                    
                }
                

                if (row.getIns2() > 0)
                {
                    
                    // ins 2 avg
                    if (ins2AvgSeries.getDataItem(time) == null)
                    {
                        ins2AvgSeries.add(time, row.getIns2());
                    }
                    else
                    {
                        ins2AvgSeries.addOrUpdate(time, MathUtils.getAverage(row.getIns2(), ins2AvgSeries.getDataItem(
                            time).getValue()));
                    }
                    
                    // ins 2 sum
                    if (ins2SumSeries.getDataItem(time) == null)
                    {
                        ins2SumSeries.add(time, row.getIns2());
                    }
                    else
                    {
                        ins2SumSeries.addOrUpdate(time, MathUtils.add(row.getIns2(), ins2SumSeries.getDataItem(time)
                                .getValue()));
                    }
                    
                }

                if ((row.getIns1() > 0) || (row.getIns2() > 0))
                {
                    // ins avg
                    if (insAvgSeries.getDataItem(time) == null)
                    {
                        insAvgSeries.add(time, row.getIns1() + row.getIns2());
                    }
                    else
                    {
                        insAvgSeries.addOrUpdate(time, MathUtils.getAverage(row.getIns1() + row.getIns2(), insAvgSeries
                                .getDataItem(time).getValue()));
                    }

                    // ins sum
                    if (insSumSeries.getDataItem(time) == null)
                    {
                        insSumSeries.add(time, row.getIns1() + row.getIns2());
                    }
                    else
                    {
                        insSumSeries.addOrUpdate(time, MathUtils.add(row.getIns1() + row.getIns2(), insSumSeries
                                .getDataItem(time).getValue()));
                    }
                }

                // TODO check 
                if ((CHSumSeries.getDataItem(time) != null)
                        && (CHSumSeries.getDataItem(time).getValue().doubleValue() > 0))
                {

                    double a=0.0f,b=0.0f;
                    
                    if (insSumSeries.getDataItem(time)!=null)
                    {
                        a = insSumSeries.getDataItem(time).getValue().doubleValue();
                    }
                        
                    if (CHSumSeries.getDataItem(time)!=null)
                    {
                        b = CHSumSeries.getDataItem(time).getValue().doubleValue();
                    }
                    
                    if ((a!=0.0d) && (b!=0.0d))
                    {
                        double ins_ch = a/b;
                        insPerCHSeries.addOrUpdate(time, ins_ch);
                    }
                }
                if (row.getCH() > 0)
                {
                    if (mealsSeries.getDataItem(time) == null)
                    {
                        mealsSeries.add(time, 1);
                    }
                    else
                    {
                        mealsSeries.addOrUpdate(time, MathUtils.add(1, mealsSeries.getDataItem(time).getValue()));
                    }
                }
            }
        }

        if (!plotData.isPlotBGDayAvg())
            BGAvgSeries = new TimeSeries(translator.getMessage("AVG_BG_PER_DAY"), Day.class);
        
        if (!plotData.isPlotBGReadings())
            BGReadingsSeries = new TimeSeries(translator.getMessage("READINGS"), Day.class);
        if (!plotData.isPlotCHDayAvg())
            CHAvgSeries = new TimeSeries(translator.getMessage("AVG_MEAL_SIZE"), Day.class);
        if (!plotData.isPlotCHSum())
            CHSumSeries = new TimeSeries(translator.getMessage("SUM_BU"), Day.class);
        if (!plotData.isPlotIns1DayAvg())
            ins1AvgSeries = new TimeSeries(translator.getMessage("AVG") + " " + settings.getIns1Name(), Day.class);
        
        if (!plotData.isPlotIns1Sum())
        {
            System.out.println("ins1sum");
            ins1SumSeries = new TimeSeries(translator.getMessage("SUM") + " " + settings.getIns1Name(), Day.class);
        }
        
        if (!plotData.isPlotIns2DayAvg())
            ins2AvgSeries = new TimeSeries(translator.getMessage("AVG") + " " + settings.getIns2Name(), Day.class);
        if (!plotData.isPlotIns2Sum())
            ins2SumSeries = new TimeSeries(translator.getMessage("SUM") + " " + settings.getIns2Name(), Day.class);
        if (!plotData.isPlotInsTotalDayAvg())
            insAvgSeries = new TimeSeries(translator.getMessage("AVG_INS"), Day.class);
        if (!plotData.isPlotInsTotalSum())
            insSumSeries = new TimeSeries(translator.getMessage("SUM_INSULIN"), Day.class);
        if (!plotData.isPlotInsPerCH())
            insPerCHSeries = new TimeSeries(translator.getMessage("INS_SLASH_BU"), Day.class);
        if (!plotData.isPlotMeals())
            mealsSeries = new TimeSeries(translator.getMessage("MEALS"), Day.class);

        BGDataset.addSeries(BGAvgSeries);
        sumDataset.addSeries(CHSumSeries);
        sumDataset.addSeries(ins1SumSeries);
        sumDataset.addSeries(ins2SumSeries);
        sumDataset.addSeries(insSumSeries);
        averageDataset.addSeries(CHAvgSeries);
        averageDataset.addSeries(ins1AvgSeries);
        averageDataset.addSeries(ins2AvgSeries);
        averageDataset.addSeries(insAvgSeries);
        averageDataset.addSeries(insPerCHSeries);
        readingsDataset.addSeries(BGReadingsSeries);
        readingsDataset.addSeries(mealsSeries);
    }
}
