package ggc.core.data.graph.v1.view;

import java.awt.*;
import java.text.SimpleDateFormat;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.AxisLocation;
import org.jfree.chart.axis.DateAxis;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.XYLineAndShapeRenderer;
import org.jfree.data.general.AbstractDataset;
import org.jfree.data.time.Day;
import org.jfree.data.time.TimeSeries;
import org.jfree.data.time.TimeSeriesCollection;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ggc.core.data.DailyValuesRow;
import ggc.core.data.graph.v1.GGCGraphType;
import ggc.core.data.graph.v1.db.GraphV1DbRetriever;
import ggc.core.db.hibernate.settings.ColorSchemeH;
import ggc.core.util.DataAccess;
import ggc.core.util.MathUtils;

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
 *  Filename:     GraphViewCourse  
 *  Description:  GraphView implementation for Course, uses ATech Graph Framework
 * 
 *  Author: andyrozman {andy@atech-software.com}  
 */

public class GraphViewCourse extends GraphViewAbstract
{

    private static final Logger LOG = LoggerFactory.getLogger(GraphViewCourse.class);

    private TimeSeriesCollection BGDataset = new TimeSeriesCollection();
    private TimeSeriesCollection readingsDataset = new TimeSeriesCollection();
    private TimeSeriesCollection sumDataset = new TimeSeriesCollection();
    private TimeSeriesCollection averageDataset = new TimeSeriesCollection();


    /**
     * Constructor
     */
    public GraphViewCourse(Dimension startSize, GraphV1DbRetriever dbRetriever)
    {
        super(DataAccess.getInstance(), GGCGraphType.Course, dbRetriever, startSize);
    }


    /**
     * {@inheritDoc}
     */
    public AbstractDataset getDataSet()
    {
        return this.BGDataset;
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
        Day time;

        BGDataset.removeAllSeries();
        averageDataset.removeAllSeries();
        sumDataset.removeAllSeries();
        readingsDataset.removeAllSeries();

        TimeSeries BGAvgSeries = new TimeSeries(this.i18nControl.getMessage("AVG_BG_PER_DAY"), Day.class);
        TimeSeries BGReadingsSeries = new TimeSeries(this.i18nControl.getMessage("READINGS"), Day.class);
        TimeSeries CHAvgSeries = new TimeSeries(this.i18nControl.getMessage("AVG_MEAL_SIZE"), Day.class);
        TimeSeries CHSumSeries = new TimeSeries(this.i18nControl.getMessage("SUM_BU"), Day.class);
        TimeSeries ins1AvgSeries = new TimeSeries(this.i18nControl.getMessage("AVG") + " Ins1", Day.class);
        TimeSeries ins1SumSeries = new TimeSeries(this.i18nControl.getMessage("SUM") + " Ins1", Day.class);
        TimeSeries ins2AvgSeries = new TimeSeries(this.i18nControl.getMessage("AVG") + " Ins2", Day.class);
        TimeSeries ins2SumSeries = new TimeSeries(this.i18nControl.getMessage("SUM") + " Ins2", Day.class);
        TimeSeries insAvgSeries = new TimeSeries(this.i18nControl.getMessage("AVG_INS"), Day.class);
        TimeSeries insSumSeries = new TimeSeries(this.i18nControl.getMessage("SUM_INSULIN"), Day.class);
        TimeSeries insPerCHSeries = new TimeSeries(this.i18nControl.getMessage("INS_SLASH_BU"), Day.class);
        TimeSeries mealsSeries = new TimeSeries(this.i18nControl.getMessage("MEALS"), Day.class);

        // int days = data.getDailyValuesItemsCount();

        for (int i = 0; i < this.glucoValues.getRowCount(); i++)
        {
            row = this.glucoValues.getDailyValueRow(i);
            time = new Day(row.getDateTimeAsDate());

            if (row.getBG(glucoseUnitType) > 0)
            {
                if (BGAvgSeries.getDataItem(time) == null)
                {
                    BGAvgSeries.add(time, row.getBG(glucoseUnitType));
                }
                else
                {
                    BGAvgSeries.addOrUpdate(time,
                        MathUtils.getAverage(row.getBG(glucoseUnitType), BGAvgSeries.getDataItem(time).getValue()));
                }

                if (BGReadingsSeries.getDataItem(time) == null)
                {
                    BGReadingsSeries.add(time, 1);
                }
                else
                {
                    BGReadingsSeries.addOrUpdate(time, MathUtils.add(1, BGReadingsSeries.getDataItem(time).getValue()));
                }
            }

            if (row.getCH() > 0)
            {
                // System.out.println(".");
                if (CHAvgSeries.getDataItem(time) == null)
                {
                    CHAvgSeries.add(time, row.getCH());
                }
                else
                {
                    CHAvgSeries.addOrUpdate(time,
                        MathUtils.getAverage(row.getCH(), CHAvgSeries.getDataItem(time).getValue()));
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
                    CHSumSeries.addOrUpdate(time, MathUtils.add(row.getCH(), CHSumSeries.getDataItem(time).getValue()));
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
                    ins1AvgSeries.addOrUpdate(time,
                        MathUtils.getAverage(row.getIns1(), ins1AvgSeries.getDataItem(time).getValue()));
                }

                // ins 1 sum
                if (ins1SumSeries.getDataItem(time) == null)
                {
                    ins1SumSeries.add(time, row.getIns1());
                }
                else
                {
                    float sum = ins1SumSeries.getDataItem(time).getValue().floatValue();
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
                    ins2AvgSeries.addOrUpdate(time,
                        MathUtils.getAverage(row.getIns2(), ins2AvgSeries.getDataItem(time).getValue()));
                }

                // ins 2 sum
                if (ins2SumSeries.getDataItem(time) == null)
                {
                    ins2SumSeries.add(time, row.getIns2());
                }
                else
                {
                    ins2SumSeries.addOrUpdate(time,
                        MathUtils.add(row.getIns2(), ins2SumSeries.getDataItem(time).getValue()));
                }

            }

            if (row.getIns1() > 0 || row.getIns2() > 0)
            {
                // ins avg
                if (insAvgSeries.getDataItem(time) == null)
                {
                    insAvgSeries.add(time, row.getIns1() + row.getIns2());
                }
                else
                {
                    insAvgSeries.addOrUpdate(time,
                        MathUtils.getAverage(row.getIns1() + row.getIns2(), insAvgSeries.getDataItem(time).getValue()));
                }

                // ins sum
                if (insSumSeries.getDataItem(time) == null)
                {
                    insSumSeries.add(time, row.getIns1() + row.getIns2());
                }
                else
                {
                    insSumSeries.addOrUpdate(time,
                        MathUtils.add(row.getIns1() + row.getIns2(), insSumSeries.getDataItem(time).getValue()));
                }
            }

            // TODO check
            if (CHSumSeries.getDataItem(time) != null && CHSumSeries.getDataItem(time).getValue().doubleValue() > 0)
            {

                double a = 0.0f, b = 0.0f;

                if (insSumSeries.getDataItem(time) != null)
                {
                    a = insSumSeries.getDataItem(time).getValue().doubleValue();
                }

                if (CHSumSeries.getDataItem(time) != null)
                {
                    b = CHSumSeries.getDataItem(time).getValue().doubleValue();
                }

                if (a != 0.0d && b != 0.0d)
                {
                    double ins_ch = a / b;
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

        if (this.plotData.isPlotBGDayAvg())
        {
            BGDataset.addSeries(BGAvgSeries);
        }

        if (this.plotData.isPlotBGReadings())
        {
            readingsDataset.addSeries(BGReadingsSeries);
        }

        if (this.plotData.isPlotCHDayAvg())
        {
            averageDataset.addSeries(CHAvgSeries);
        }

        if (this.plotData.isPlotCHSum())
        {
            sumDataset.addSeries(CHSumSeries);
        }

        if (this.plotData.isPlotIns1DayAvg())
        {
            averageDataset.addSeries(ins1AvgSeries);
        }

        if (this.plotData.isPlotIns1Sum())
        {
            sumDataset.addSeries(ins1SumSeries);
        }

        if (this.plotData.isPlotIns2DayAvg())
        {
            averageDataset.addSeries(ins2AvgSeries);
        }

        if (this.plotData.isPlotIns2Sum())
        {
            sumDataset.addSeries(ins2SumSeries);
        }

        if (this.plotData.isPlotInsTotalDayAvg())
        {
            averageDataset.addSeries(insAvgSeries);
        }

        if (this.plotData.isPlotInsTotalSum())
        {
            sumDataset.addSeries(insSumSeries);
        }

        if (this.plotData.isPlotInsPerCH())
        {
            averageDataset.addSeries(insPerCHSeries);
        }

        if (this.plotData.isPlotMeals())
        {
            readingsDataset.addSeries(mealsSeries);
        }

        setPlot(chart);

    }


    /**
     * {@inheritDoc}
     */
    public void setPlot(JFreeChart chart)
    {
        if (chart == null)
            return;

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

        // graphUtil.applyMarkers(plot);
        // plot.setRangeGridlinesVisible(false);
        // plot.setDomainGridlinesVisible(false);

        plot.setRangeGridlinesVisible(true);
        plot.setDomainGridlinesVisible(true);

        dateAxis.setDateFormatOverride(new SimpleDateFormat(i18nControl.getMessage("FORMAT_DATE_DAYS")));

        ColorSchemeH colorScheme = properties.getSelectedColorScheme();

        defaultRenderer.setSeriesPaint(0, this.dataAccesLocal.getColor(colorScheme.getColor_bg_avg()));

        averageRenderer.setSeriesPaint(0, this.dataAccesLocal.getColor(colorScheme.getColor_ch()).darker());
        averageRenderer.setSeriesPaint(1, this.dataAccesLocal.getColor(colorScheme.getColor_ins1()).darker());
        averageRenderer.setSeriesPaint(2, this.dataAccesLocal.getColor(colorScheme.getColor_ins2()).darker());
        averageRenderer.setSeriesPaint(3, this.dataAccesLocal.getColor(colorScheme.getColor_ins()).darker());
        averageRenderer.setSeriesPaint(4, this.dataAccesLocal.getColor(colorScheme.getColor_ins_perbu()));

        sumRenderer.setSeriesPaint(0, this.dataAccesLocal.getColor(colorScheme.getColor_ch()));
        sumRenderer.setSeriesPaint(1, this.dataAccesLocal.getColor(colorScheme.getColor_ins1()));
        sumRenderer.setSeriesPaint(2, this.dataAccesLocal.getColor(colorScheme.getColor_ins2()));
        sumRenderer.setSeriesPaint(3, this.dataAccesLocal.getColor(colorScheme.getColor_ins()));

        readingsRenderer.setSeriesPaint(0, this.dataAccesLocal.getColor(colorScheme.getColor_bg()));
        readingsRenderer.setSeriesPaint(1, this.dataAccesLocal.getColor(colorScheme.getColor_ch()).brighter());

        // chart.setBackgroundPaint(backgroundColor);
        // chart.setRenderingHints(renderingHints);
        chart.setBorderVisible(false);

        BGAxis.setAutoRangeIncludesZero(true);

        plot.setNoDataMessage(this.i18nControl.getMessage("GRAPH_NO_DATA_AVAILABLE_CHANGE"));

    }


    /**
     * {@inheritDoc}
     */
    @Override
    public void createChart()
    {
        chart = ChartFactory.createTimeSeriesChart(null, i18nControl.getMessage("AXIS_TIME_LABEL"),
            String.format(i18nControl.getMessage("AXIS_VALUE_LABEL"), this.glucoseUnitType.getTranslation()), BGDataset,
            true, true, false);
    }

}
