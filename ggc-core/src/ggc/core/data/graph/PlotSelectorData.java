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
 *  Filename: PlotData.java
 *  Purpose:  Contains information about which kinds of data should be plotted.
 *
 *  Author:   rumbi
 *  
 */

package ggc.core.data.graph;

import ggc.core.data.PlotData;

import com.atech.graphics.calendar.DateRangeData;

/**
 * The main purpose of this class is to store information on which kinds of data
 * should be plotted in a graph. By having a static instance of this class
 * assigned to each graph that needs it, the user's selection can be remembered
 * across different incarnations of each graph dialog.
 * 
 * This file was originally copied from Rumbi's implementation and then adapted 
 * for usage with new framework (time data and some other stuff)
 * 
 * @author rumbi
 * @author andy
 */
public class PlotSelectorData implements Cloneable // , ReadablePlotData
{
    boolean plotBG = true;
    boolean plotBGDayAvg = false;
    boolean plotBGReadings = false;

    boolean plotCH = false;
    boolean plotCHDayAvg = false;
    boolean plotCHSum = false;

    boolean plotIns1 = false;
    boolean plotIns1DayAvg = false;
    boolean plotIns1Sum = false;

    boolean plotIns2 = false;
    boolean plotIns2DayAvg = false;
    boolean plotIns2Sum = false;

    boolean plotInsTotal = false;
    boolean plotInsTotalDayAvg = false;
    boolean plotInsTotalSum = false;

    boolean plotInsPerCH = false;
    boolean plotMeals = false;

    DateRangeData range_data;

    /**
     * Time Range: 1 Week
     */
    // public static final int TIME_RANGE_1_WEEK = 1;
    /**
     * Time Range: 1 Month
     */
    // public static final int TIME_RANGE_1_MONTH = 2;
    /**
     * Time Range: 3 Months
     */
    // public static final int TIME_RANGE_3_MONTHS = 3;
    /**
     * Time Range: Custom
     */
    // public static final int TIME_RANGE_CUSTOM = 4;

    /*
     * int time_range = TIME_RANGE_1_MONTH;
     * GregorianCalendar time_range_from = null;
     * GregorianCalendar time_range_to = null;
     */

    /**
     * Initializes all data as <code>false</code>.
     */
    public PlotSelectorData()
    {
        this.range_data = new DateRangeData();
    }

    /**
     * Initializes the data from the passed <code>{@link PlotData}</code>. Has a
     * similar effect to calling the <code>{@link #clone()}</code> method.
     * 
     * @param data
     *            The <code>{@link PlotData}</code> to be copied.
     */
    public PlotSelectorData(PlotSelectorData data)
    {
        plotBG = data.plotBG;
        plotBGDayAvg = data.plotBGDayAvg;
        plotBGReadings = data.plotBGReadings;

        plotCH = data.plotCH;
        plotCHDayAvg = data.plotCHDayAvg;
        plotCHSum = data.plotCHSum;

        plotIns1 = data.plotIns1;
        plotIns1DayAvg = data.plotIns1DayAvg;
        plotIns1Sum = data.plotIns1Sum;

        plotIns2 = data.plotIns2;
        plotIns2DayAvg = data.plotIns2DayAvg;
        plotIns2Sum = data.plotIns2Sum;

        plotInsTotal = data.plotInsTotal;
        plotInsTotalDayAvg = data.plotInsTotalDayAvg;
        plotInsTotalSum = data.plotInsTotalSum;

        plotInsPerCH = data.plotInsPerCH;
        plotMeals = data.plotMeals;

        this.range_data = data.range_data;
    }

    /**
     * Get Date Range Data 
     * 
     * @return
     */
    public DateRangeData getDateRangeData()
    {
        return this.range_data;
    }

    /**
     * Set Date Range Data
     *  
     * @param drd 
     */
    public void setDateRangeData(DateRangeData drd)
    {
        this.range_data = drd;
    }

    /**
     * (non-Javadoc)
     * 
     * @return 
     * @see ggc.core.data.ReadablePlotData#isPlotBG()
     */
    public boolean isPlotBG()
    {
        return plotBG;
    }

    /**
     * (non-Javadoc)
     * @return 
     * 
     * @see ggc.core.data.ReadablePlotData#isPlotBGDayAvg()
     */
    public boolean isPlotBGDayAvg()
    {
        return plotBGDayAvg;
    }

    /**
     * (non-Javadoc)
     * @return 
     * 
     * @see ggc.core.data.ReadablePlotData#isPlotBGReadings()
     */
    public boolean isPlotBGReadings()
    {
        return plotBGReadings;
    }

    /**
     * (non-Javadoc)
     * @return 
     * 
     * @see ggc.core.data.ReadablePlotData#isPlotCH()
     */
    public boolean isPlotCH()
    {
        return plotCH;
    }

    /**
     * (non-Javadoc)
     * @return 
     * 
     * @see ggc.core.data.ReadablePlotData#isPlotCHDayAvg()
     */
    public boolean isPlotCHDayAvg()
    {
        return plotCHDayAvg;
    }

    /**
     * (non-Javadoc)
     * @return 
     * 
     * @see ggc.core.data.ReadablePlotData#isPlotCHSum()
     */
    public boolean isPlotCHSum()
    {
        return plotCHSum;
    }

    /**
     * (non-Javadoc)
     * @return 
     * 
     * @see ggc.core.data.ReadablePlotData#isPlotIns1()
     */
    public boolean isPlotIns1()
    {
        return plotIns1;
    }

    /**
     * (non-Javadoc)
     * @return 
     * 
     * @see ggc.core.data.ReadablePlotData#isPlotIns1DayAvg()
     */
    public boolean isPlotIns1DayAvg()
    {
        return plotIns1DayAvg;
    }

    /**
     * (non-Javadoc)
     * @return 
     * 
     * @see ggc.core.data.ReadablePlotData#isPlotIns1Sum()
     */
    public boolean isPlotIns1Sum()
    {
        return plotIns1Sum;
    }

    /**
     * (non-Javadoc)
     * @return 
     * 
     * @see ggc.core.data.ReadablePlotData#isPlotIns2()
     */
    public boolean isPlotIns2()
    {
        return plotIns2;
    }

    /**
     * (non-Javadoc)
     * @return 
     * 
     * @see ggc.core.data.ReadablePlotData#isPlotIns2DayAvg()
     */
    public boolean isPlotIns2DayAvg()
    {
        return plotIns2DayAvg;
    }

    /**
     * (non-Javadoc)
     * @return 
     * 
     * @see ggc.core.data.ReadablePlotData#isPlotIns2Sum()
     */
    public boolean isPlotIns2Sum()
    {
        return plotIns2Sum;
    }

    /**
     * (non-Javadoc)
     * @return 
     * 
     * @see ggc.core.data.ReadablePlotData#isPlotInsTotal()
     */
    public boolean isPlotInsTotal()
    {
        return plotInsTotal;
    }

    /**
     * (non-Javadoc)
     * @return 
     * 
     * @see ggc.core.data.ReadablePlotData#isPlotInsTotalDayAvg()
     */
    public boolean isPlotInsTotalDayAvg()
    {
        return plotInsTotalDayAvg;
    }

    /**
     * (non-Javadoc)
     * @return 
     * 
     * @see ggc.core.data.ReadablePlotData#isPlotInsTotalSum()
     */
    public boolean isPlotInsTotalSum()
    {
        return plotInsTotalSum;
    }

    /**
     * (non-Javadoc)
     * @return 
     * 
     * @see ggc.core.data.ReadablePlotData#isPlotInsPerCH()
     */
    public boolean isPlotInsPerCH()
    {
        return plotInsPerCH;
    }

    /**
     * (non-Javadoc)
     * @return 
     * 
     * @see ggc.core.data.ReadablePlotData#isPlotMeals()
     */
    public boolean isPlotMeals()
    {
        return plotMeals;
    }

    /**
     * @param plotBG
     *            Set whether to plot the BG measured.
     */
    public void setPlotBG(boolean plotBG)
    {
        this.plotBG = plotBG;
    }

    /**
     * @param plotBGDayAvg
     *            Set whether to plot the average BG on a given day.
     */
    public void setPlotBGDayAvg(boolean plotBGDayAvg)
    {
        this.plotBGDayAvg = plotBGDayAvg;
    }

    /**
     * @param plotBGReadings
     *            Set whether to plot the amount of BG readings taken on a given
     *            day.
     */
    public void setPlotBGReadings(boolean plotBGReadings)
    {
        this.plotBGReadings = plotBGReadings;
    }

    /**
     * @param plotCH
     *            Set whether to plot the carbohydrate units eaten.
     */
    public void setPlotCH(boolean plotCH)
    {
        this.plotCH = plotCH;
    }

    /**
     * @param plotCHDayAvg
     *            Set whether to plot the average amount of CH eaten on a given
     *            day.
     */
    public void setPlotCHDayAvg(boolean plotCHDayAvg)
    {
        this.plotCHDayAvg = plotCHDayAvg;
    }

    /**
     * @param plotCHSum
     *            Set whether to plot the sum of all CH eaten on a given day.
     */
    public void setPlotCHSum(boolean plotCHSum)
    {
        this.plotCHSum = plotCHSum;
    }

    /**
     * @param plotIns1
     *            Set whether to plot the amount of Ins1 injected.
     */
    public void setPlotIns1(boolean plotIns1)
    {
        this.plotIns1 = plotIns1;
    }

    /**
     * @param plotIns1DayAvg
     *            Set whether to plot the average amount of Ins1 injected on a
     *            given day.
     */
    public void setPlotIns1DayAvg(boolean plotIns1DayAvg)
    {
        this.plotIns1DayAvg = plotIns1DayAvg;
    }

    /**
     * @param plotIns1Sum
     *            Set whether to plot the sum of all Ins1 injected on a given
     *            day.
     */
    public void setPlotIns1Sum(boolean plotIns1Sum)
    {
        this.plotIns1Sum = plotIns1Sum;
    }

    /**
     * @param plotIns2
     *            Set whether to plot the amount of Ins2 injected.
     */
    public void setPlotIns2(boolean plotIns2)
    {
        this.plotIns2 = plotIns2;
    }

    /**
     * @param plotIns2DayAvg
     *            Set whether to plot the average amount of Ins2 injected on a
     *            given day.
     */
    public void setPlotIns2DayAvg(boolean plotIns2DayAvg)
    {
        this.plotIns2DayAvg = plotIns2DayAvg;
    }

    /**
     * @param plotIns2Sum
     *            Set whether to plot the sum of all Ins2 injected on a given
     *            day.
     */
    public void setPlotIns2Sum(boolean plotIns2Sum)
    {
        this.plotIns2Sum = plotIns2Sum;
    }

    /**
     * @param plotInsTotal
     *            Set whether to plot the total amount of insulin injected.
     */
    public void setPlotInsTotal(boolean plotInsTotal)
    {
        this.plotInsTotal = plotInsTotal;
    }

    /**
     * @param plotInsTotalDayAvg
     *            Set whether to plot the average amount of insulin injected on
     *            a given day.
     */
    public void setPlotInsTotalDayAvg(boolean plotInsTotalDayAvg)
    {
        this.plotInsTotalDayAvg = plotInsTotalDayAvg;
    }

    /**
     * @param plotInsTotalSum
     *            Set whether to plot the sum of all insulin injected on a given
     *            day.
     */
    public void setPlotInsTotalSum(boolean plotInsTotalSum)
    {
        this.plotInsTotalSum = plotInsTotalSum;
    }

    /**
     * @param plotInsPerCH
     *            Set whether to plot the total amount of insulin per CH on a
     *            given day.
     */
    public void setPlotInsPerCH(boolean plotInsPerCH)
    {
        this.plotInsPerCH = plotInsPerCH;
    }

    /**
     * @param plotMeals
     *            Set whether to plot the amount of meals on a given day.
     */
    public void setPlotMeals(boolean plotMeals)
    {
        this.plotMeals = plotMeals;
    }

    /**
     * (non-Javadoc)
     * 
     * @see java.lang.Object#clone()
     */
    @Override
    protected Object clone() throws CloneNotSupportedException
    {
        return super.clone();
    }

    @Override
    public String toString()
    {

        return "PlotSelectorData [" + "bg=" + plotBG + ", bg_day_avg=" + plotBGDayAvg + ", bg_reads=" + plotBGReadings +

        ", chs=" + plotCH + ", ch_day_avg=" + plotCHDayAvg + ", ch_sum=" + plotCHSum +

        ", bg_reads=" + plotIns1 + ", ins1_day_avg=" + plotIns1DayAvg + ", ins1_sum=" + plotIns1Sum +

        ", ins2=" + plotIns2 + ", ins2_day_avg=" + plotIns2DayAvg + ", ins2_sum=" + plotIns2Sum +

        ", ins_all=" + plotInsTotal + ", ins_all_day_avg=" + plotInsTotalDayAvg + ", ins_all_sum=" + plotInsTotalSum +

        ", ins_per_ch=" + plotInsPerCH + ", meals=" + plotMeals + "]";

    }

}
