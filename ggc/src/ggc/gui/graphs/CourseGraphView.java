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

package ggc.gui.graphs;

import ggc.core.data.DailyValues;
import ggc.core.data.GlucoValues;
import ggc.core.data.PlotData;
import ggc.core.data.ReadablePlotData;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.geom.GeneralPath;
import java.awt.geom.Rectangle2D;

public class CourseGraphView extends AbstractGraphView
{
    private static final long serialVersionUID = -7960828650875426272L;
    private GlucoValues gV = null;
    private ReadablePlotData data = null;

    public CourseGraphView()
    {
        this(null, new PlotData());
    }

    public CourseGraphView(PlotData data)
    {
        this(null, data);
    }

    public CourseGraphView(GlucoValues gV, PlotData data)
    {
        super();
        this.gV = gV;
        this.data = data;
        if (gV != null)
        {
            dayCount = gV.getDayCount();
        }
    }

    public void setGlucoValues(GlucoValues gV)
    {
        this.gV = gV;
        dayCount = gV.getDayCount();
    }

    /**
     * @param data
     *            the data to set
     */
    public void setData(ReadablePlotData data)
    {
        this.data = data;
    }

    @Override
    public void paint(Graphics g)
    {
        Graphics2D g2D = (Graphics2D) g;

        g2D.setRenderingHint(RenderingHints.KEY_ANTIALIASING, oAA);
        g2D.setRenderingHint(RenderingHints.KEY_COLOR_RENDERING, oCR);
        g2D.setRenderingHint(RenderingHints.KEY_DITHERING, oD);
        g2D.setRenderingHint(RenderingHints.KEY_FRACTIONALMETRICS, oFM);
        g2D.setRenderingHint(RenderingHints.KEY_INTERPOLATION, oI);
        g2D.setRenderingHint(RenderingHints.KEY_RENDERING, oR);
        g2D.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, oTAA);

        calculateSizes();

        drawFramework(g2D);
        drawValues(g2D);
    }

    @Override
    protected void drawFramework(Graphics2D g2D)
    {
        Dimension dim = getSize();
        int h = dim.height, w = dim.width;

        int markPos = 0;
        int diffH = h - lowerSpace - upperSpace;
        int diffW = w - rightSpace - leftSpace;
        // distance between lables on the vertical scale
        float labelDeltaV = ((float) BGDiff) / counter;

        Rectangle2D.Float rect0 = new Rectangle2D.Float(0, 0, w, h);
        g2D.setPaint(Color.white);
        g2D.fill(rect0);
        g2D.draw(rect0);

        g2D.setPaint(Color.black);
        g2D.drawLine(leftSpace, upperSpace, leftSpace, h - lowerSpace);

        for (int i = 0; i <= counter; i++)
        {
            markPos = upperSpace + i * diffH / counter;
            g2D.drawString(Math.round(maxBG - labelDeltaV * i) + "", 5, markPos + 5);
            g2D.drawLine(leftSpace - 5, markPos, leftSpace, markPos);
        }
        g2D.drawLine(leftSpace, h - lowerSpace, w - rightSpace, h - lowerSpace);

        if (gV != null)
        {
            int days = 10;
            if (dayCount < days)
            {
                days = (int) dayCount;
            }

            float scale = (float) (dayCount - 1) / (days - 1);
            String dayDate;
            for (int i = 0; i < days; i++)
            {
                dayDate = gV.getDateForDayAt(Math.round(i * scale));

                // handle small data sets
                if (m_da.isEmptyOrUnset(dayDate))
                {
                    break;
                }

                markPos = leftSpace + i * (diffW) / (days - 1);
                g2D.drawLine(markPos, h - lowerSpace, markPos, h - lowerSpace + 5);
                g2D.drawString(dayDate, markPos - 10, h - lowerSpace + 20);
            }
        }

        // Target Zone
        Rectangle2D.Float rect1 = new Rectangle2D.Float(leftSpace + 1, BGtoCoord(maxGoodBG), drawableWidth,
                BGtoCoord(minGoodBG) - BGtoCoord(maxGoodBG));
        g2D.setPaint(m_da.getColor(m_da.getSettings().getSelectedColorScheme().getColor_bg_target()));
        g2D.fill(rect1);
        g2D.draw(rect1);

        // High Zone
        rect1 = new Rectangle2D.Float(leftSpace + 1, BGtoCoord(maxBG), drawableWidth, BGtoCoord(maxGoodBG)
                - BGtoCoord(maxBG) - 1);
        g2D.setPaint(m_da.getColor(m_da.getSettings().getSelectedColorScheme().getColor_bg_high()));
        g2D.fill(rect1);
        g2D.draw(rect1);

        // Low Zone
        rect1 = new Rectangle2D.Float(leftSpace + 1, BGtoCoord(minGoodBG), drawableWidth, BGtoCoord(0)
                - BGtoCoord(minGoodBG) - 1);
        g2D.setPaint(m_da.getColor(m_da.getSettings().getSelectedColorScheme().getColor_bg_low()));
        g2D.fill(rect1);
        g2D.draw(rect1);

    }

    @Override
    protected void drawValues(Graphics2D g2D)
    {
        // CourseGraphFrame cGF = CourseGraphFrame.getInstance();

        GeneralPath plBG = new GeneralPath();
        // GeneralPath plAvgBGDay = new GeneralPath();
        GeneralPath plSumBU = new GeneralPath();
        GeneralPath plMeals = new GeneralPath();
        GeneralPath plSumIns1 = new GeneralPath();
        GeneralPath plSumIns2 = new GeneralPath();
        GeneralPath plSumIns = new GeneralPath();
        GeneralPath plInsPerBU = new GeneralPath();

        boolean firstBG = true;
        // boolean firstAvgBGDay = true;
        boolean firstMeals = true;
        boolean firstSumBU = true;
        boolean firstSumIns1 = true;
        boolean firstSumIns2 = true;
        boolean firstSumIns = true;
        boolean firstInsPerBU = true;

        DailyValues dV;

        g2D.setPaint(m_da.getColor(m_da.getSettings().getSelectedColorScheme().getColor_bg_avg()));

        for (int i = 0; i < dayCount; i++)
        {
            dV = gV.getDailyValuesForDay(i);
            float multiWidth = dayWidthC * i;
            float offset = multiWidth + leftSpace + dayWidthC / 2;

            // draw BG
            for (int j = 0; j < dV.getRowCount(); j++)
            {
                float tmpBG = dV.getBGAt(j);

                if (tmpBG != 0)
                {
                    int X = (int) (multiWidth + DateTimetoCoord(dV.getDateTimeAt(j)));
                    int Y = BGtoCoord(tmpBG);
                    if (firstBG)
                    {
                        plBG.moveTo(X, Y);
                        firstBG = false;
                    }
                    else
                        plBG.lineTo(X, Y);
                }
            }

            // draw avgBGDay
            // if (firstAvgBGDay) {
            // plAvgBGDay.moveTo(offset, BGtoCoord(dV.getAvgBG()));
            // firstAvgBGDay = false;
            // } else
            // plAvgBGDay.lineTo(offset, BGtoCoord(dV.getAvgBG()));
            if (data.isPlotBGDayAvg())
            {
                int tmp = BGtoCoord(dV.getAvgBG());
                g2D.drawLine((int) multiWidth + leftSpace, tmp, (int) (multiWidth + dayWidthC + leftSpace), tmp);
            }

            // draw sumBU
            if (firstSumBU)
            {
                plSumBU.moveTo(offset, BUtoCoord(dV.getSumCH()));
                firstSumBU = false;
            }
            else
                plSumBU.lineTo(offset, BUtoCoord(dV.getSumCH()));

            // draw sumMeals
            if (firstMeals)
            {
                plMeals.moveTo(offset, BUtoCoord(dV.getCHCount()));
                firstMeals = false;
            }
            else
                plMeals.lineTo(offset, BUtoCoord(dV.getCHCount()));

            // draw Ins1
            if (firstSumIns1)
            {
                plSumIns1.moveTo(offset, InstoCoord(dV.getSumIns1()));
                firstSumIns1 = false;
            }
            else
                plSumIns1.lineTo(offset, InstoCoord(dV.getSumIns1()));

            // draw Ins2
            if (firstSumIns2)
            {
                plSumIns2.moveTo(offset, InstoCoord(dV.getSumIns2()));
                firstSumIns2 = false;
            }
            else
                plSumIns2.lineTo(offset, InstoCoord(dV.getSumIns2()));

            // draw Ins
            if (firstSumIns)
            {
                plSumIns.moveTo(offset, InstoCoord(dV.getSumIns()));
                firstSumIns = false;
            }
            else
                plSumIns.lineTo(offset, InstoCoord(dV.getSumIns()));

            // draw Ins / BU
            if (firstInsPerBU)
            {
                plInsPerBU.moveTo(offset, InsPerBUtoCoord(dV.getIns2Count() / dV.getSumCH()));
                firstInsPerBU = false;
            }
            else
                plInsPerBU.lineTo(offset, InsPerBUtoCoord(dV.getIns2Count() / dV.getSumCH()));
        }

        if (data.isPlotBG())
        {
            g2D.setPaint(m_da.getColor(m_da.getSettings().getSelectedColorScheme().getColor_bg()));
            g2D.draw(plBG);
        }

        // if (cGF.getDrawAvgBGDay()) {
        // g2D.setPaint(props.getColorAvgBG());
        // g2D.draw(plAvgBGDay);
        // }

        if (data.isPlotCHSum())
        {
            g2D.setPaint(m_da.getColor(m_da.getSettings().getSelectedColorScheme().getColor_ch()));
            g2D.draw(plSumBU);
        }
        if (data.isPlotMeals())
        {
            g2D.setPaint(m_da.getColor(m_da.getSettings().getSelectedColorScheme().getColor_ch()));
            g2D.draw(plMeals);
        }
        if (data.isPlotIns1Sum())
        {
            g2D.setPaint(m_da.getColor(m_da.getSettings().getSelectedColorScheme().getColor_ins1()));
            g2D.draw(plSumIns1);
        }

        if (data.isPlotIns2Sum())
        {
            g2D.setPaint(m_da.getColor(m_da.getSettings().getSelectedColorScheme().getColor_ins2()));
            g2D.draw(plSumIns2);
        }

        if (data.isPlotInsTotal())
        {
            g2D.setPaint(m_da.getColor(m_da.getSettings().getSelectedColorScheme().getColor_ins()));
            g2D.draw(plSumIns);
        }

        if (data.isPlotInsPerCH())
        {
            g2D.setPaint(m_da.getColor(m_da.getSettings().getSelectedColorScheme().getColor_ins_perbu()));
            g2D.draw(plInsPerBU);
        }
    }
}
