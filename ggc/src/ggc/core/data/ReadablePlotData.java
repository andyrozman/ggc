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
 *  Filename: ReadablePlotData.java
 *  Purpose:  Provide the means to pass a read-only instance of PlotData
 *
 *  Author:   rumbi
 *  
 */

package ggc.core.data;

/**
 * Provide a read-only instance of <code>{@link PlotData}</code>.
 * 
 * @author rumbi
 */
public interface ReadablePlotData
{
    /**
     * @return whether to plot the individual blood glucose levels
     */
    public boolean isPlotBG();

    /**
     * @return whether to plot the average blood glucose level for each day
     */
    public boolean isPlotBGDayAvg();

    /**
     * @return whether to plot the amount of blood glucose readings for each day
     */
    public boolean isPlotBGReadings();

    /**
     * @return whether to plot the individual amounts of carbohydrates eaten
     */
    public boolean isPlotCH();

    /**
     * @return whether to plot the average amounts of carbohydrates eaten for
     *         each day
     */
    public boolean isPlotCHDayAvg();

    /**
     * @return whether to plot the sum of all carbohydrates eaten on one day
     */
    public boolean isPlotCHSum();

    /**
     * @return whether to plot the individual amounts of ins1 injected
     */
    public boolean isPlotIns1();

    /**
     * @return whether to plot the average amounts of ins1 injected for each day
     */
    public boolean isPlotIns1DayAvg();

    /**
     * @return whether to plot the sum of all ins1 injected on one day
     */
    public boolean isPlotIns1Sum();

    /**
     * @return whether to plot the individual amounts of ins2 injected
     */
    public boolean isPlotIns2();

    /**
     * @return whether to plot the average amounts of ins2 injected for each day
     */
    public boolean isPlotIns2DayAvg();

    /**
     * @return whether to plot the sum of all ins2 injected on one day
     */
    public boolean isPlotIns2Sum();

    /**
     * @return whether to plot the individual amounts of insulin injected for
     *         each datapoint
     */
    public boolean isPlotInsTotal();

    /**
     * @return whether to plot the average amounts of insulin injected for each
     *         day
     */
    public boolean isPlotInsTotalDayAvg();

    /**
     * @return whether to plot the sum of all insulin injected on one day
     */
    public boolean isPlotInsTotalSum();

    /**
     * @return whether to plot the amount of insulin/CH on one day
     */
    public boolean isPlotInsPerCH();

    /**
     * @return whether to plot the amount of meals on one day
     */
    public boolean isPlotMeals();
}
