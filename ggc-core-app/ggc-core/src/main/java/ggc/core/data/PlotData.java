package ggc.core.data;

import java.util.Calendar;
import java.util.GregorianCalendar;

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
 *  Filename:     PlotData  
 *  Description:  The main purpose of this class is to store information on which 
 *      kinds of data should be plotted in a graph. By having a static instance of 
 *      this class assigned to each graph that needs it, the user's selection can 
 *      be remembered across different incarnations of each graph dialog.
 * 
 *  This file will be used with new framework, and is therefore little extended, with
 *  time data. (at later time new data file will be created) - Andy
 * 
 *  @author rumbi
 *  @author Andy {andy@atech-software.com}  
 */

public class PlotData implements Cloneable, ReadablePlotData
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

    /**
     * Time Range: 1 Week
     */
    public static final int TIME_RANGE_1_WEEK = 1;
    /**
     * Time Range: 1 Month
     */
    public static final int TIME_RANGE_1_MONTH = 2;
    /**
     * Time Range: 3 Months
     */
    public static final int TIME_RANGE_3_MONTHS = 3;
    /**
     * Time Range: Custom
     */
    public static final int TIME_RANGE_CUSTOM = 4;

    int time_range = TIME_RANGE_1_MONTH;

    GregorianCalendar time_range_from = null;
    GregorianCalendar time_range_to = null;

    /**
     * Initializes all data as <code>false</code>.
     */
    public PlotData()
    {
        this.time_range_from = new GregorianCalendar();
        this.time_range_from.add(Calendar.MONTH, -4);
        this.time_range_to = new GregorianCalendar();
        this.time_range_to.add(Calendar.MONTH, -5);
    }

    /**
     * Initializes the data from the passed <code>{@link PlotData}</code>. Has a
     * similar effect to calling the <code>{@link #clone()}</code> method.
     * 
     * @param data
     *            The <code>{@link PlotData}</code> to be copied.
     */
    public PlotData(PlotData data)
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

        time_range = data.time_range;

        time_range_from = data.time_range_from;
        time_range_to = data.time_range_to;

    }

    /**
     * Get Time Range Type
     * 
     * @return
     */
    public int getTimeRangeType()
    {
        return this.time_range;
    }

    /**
     * Set Time Range Type
     * 
     * @param type 
     */
    public void setTimeRangeType(int type)
    {
        this.time_range = type;
    }

    /**
     * Get Time Range From
     * 
     * @return
     */
    public GregorianCalendar getTimeRangeFrom()
    {
        return this.time_range_from;
    }

    /**
     * Set Time Range From
     * 
     * @param gc 
     */
    public void setTimeRangeFrom(GregorianCalendar gc)
    {
        this.time_range_from = gc;
    }

    /**
     * Get Time Range To
     * 
     * @return
     */
    public GregorianCalendar getTimeRangeTo()
    {
        return this.time_range_to;
    }

    /**
     * Set Time Range To
     * 
     * @param gc 
     */
    public void setTimeRangeTo(GregorianCalendar gc)
    {
        this.time_range_to = gc;
    }

    /**
     * (non-Javadoc)
     * 
     * @see ggc.core.data.ReadablePlotData#isPlotBG()
     */
    public boolean isPlotBG()
    {
        return plotBG;
    }

    /**
     * (non-Javadoc)
     * 
     * @see ggc.core.data.ReadablePlotData#isPlotBGDayAvg()
     */
    public boolean isPlotBGDayAvg()
    {
        return plotBGDayAvg;
    }

    /**
     * (non-Javadoc)
     * 
     * @see ggc.core.data.ReadablePlotData#isPlotBGReadings()
     */
    public boolean isPlotBGReadings()
    {
        return plotBGReadings;
    }

    /**
     * (non-Javadoc)
     * 
     * @see ggc.core.data.ReadablePlotData#isPlotCH()
     */
    public boolean isPlotCH()
    {
        return plotCH;
    }

    /**
     * (non-Javadoc)
     * 
     * @see ggc.core.data.ReadablePlotData#isPlotCHDayAvg()
     */
    public boolean isPlotCHDayAvg()
    {
        return plotCHDayAvg;
    }

    /**
     * (non-Javadoc)
     * 
     * @see ggc.core.data.ReadablePlotData#isPlotCHSum()
     */
    public boolean isPlotCHSum()
    {
        return plotCHSum;
    }

    /**
     * (non-Javadoc)
     * 
     * @see ggc.core.data.ReadablePlotData#isPlotIns1()
     */
    public boolean isPlotIns1()
    {
        return plotIns1;
    }

    /**
     * (non-Javadoc)
     * 
     * @see ggc.core.data.ReadablePlotData#isPlotIns1DayAvg()
     */
    public boolean isPlotIns1DayAvg()
    {
        return plotIns1DayAvg;
    }

    /**
     * (non-Javadoc)
     * 
     * @see ggc.core.data.ReadablePlotData#isPlotIns1Sum()
     */
    public boolean isPlotIns1Sum()
    {
        return plotIns1Sum;
    }

    /**
     * (non-Javadoc)
     * 
     * @see ggc.core.data.ReadablePlotData#isPlotIns2()
     */
    public boolean isPlotIns2()
    {
        return plotIns2;
    }

    /**
     * (non-Javadoc)
     * 
     * @see ggc.core.data.ReadablePlotData#isPlotIns2DayAvg()
     */
    public boolean isPlotIns2DayAvg()
    {
        return plotIns2DayAvg;
    }

    /**
     * (non-Javadoc)
     * 
     * @see ggc.core.data.ReadablePlotData#isPlotIns2Sum()
     */
    public boolean isPlotIns2Sum()
    {
        return plotIns2Sum;
    }

    /**
     * (non-Javadoc)
     * 
     * @see ggc.core.data.ReadablePlotData#isPlotInsTotal()
     */
    public boolean isPlotInsTotal()
    {
        return plotInsTotal;
    }

    /**
     * (non-Javadoc)
     * 
     * @see ggc.core.data.ReadablePlotData#isPlotInsTotalDayAvg()
     */
    public boolean isPlotInsTotalDayAvg()
    {
        return plotInsTotalDayAvg;
    }

    /**
     * (non-Javadoc)
     * 
     * @see ggc.core.data.ReadablePlotData#isPlotInsTotalSum()
     */
    public boolean isPlotInsTotalSum()
    {
        return plotInsTotalSum;
    }

    /**
     * (non-Javadoc)
     * 
     * @see ggc.core.data.ReadablePlotData#isPlotInsPerCH()
     */
    public boolean isPlotInsPerCH()
    {
        return plotInsPerCH;
    }

    /**
     * (non-Javadoc)
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
}
