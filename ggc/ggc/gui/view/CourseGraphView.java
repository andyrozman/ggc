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
 */

package ggc.gui.view;


import ggc.datamodels.DailyValues;
import ggc.datamodels.GlucoValues;
import ggc.gui.dialogs.CourseGraphDialog;

import java.awt.*;
import java.awt.geom.GeneralPath;
import java.awt.geom.Rectangle2D;


public class CourseGraphView extends AbstractGraphView
{
    private GlucoValues gV = null;
    private CourseGraphDialog m_cGF = null;

    public CourseGraphView(CourseGraphDialog parent)
    {
        super();
        this.m_cGF = parent;
        dayCount = 0;
    }

    public CourseGraphView(GlucoValues gV, CourseGraphDialog parent)
    {
        this(parent);
        this.gV = gV;
        //this.m_cGF = parent;
        dayCount = gV.getDayCount();
    }

    public void setGlucoValues(GlucoValues gV)
    {
        this.gV = gV;
        dayCount = gV.getDayCount();
    }

    public void paint(Graphics g)
    {
        Graphics2D g2D = (Graphics2D)g;

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

    protected void drawFramework(Graphics2D g2D)
    {
        Dimension dim = getSize();
        int h = dim.height, w = dim.width;

        int markPos = 0;
        int diffBG = maxBG - minBG;
        int diffH = h - lowerSpace - upperSpace;
        int diffW = w - rightSpace - leftSpace;

        Rectangle2D.Float rect0 = new Rectangle2D.Float(0, 0, w, h);
        g2D.setPaint(Color.white);
        g2D.fill(rect0);
        g2D.draw(rect0);

        g2D.setPaint(Color.black);
        g2D.drawLine(leftSpace, upperSpace, leftSpace, h - lowerSpace);

        for (int i = 0; i <= counter; i++) 
	{
            markPos = upperSpace + i * (diffH) / counter;
            g2D.drawString((maxBG - (diffBG) / counter * i) + "", 5, markPos + 5);
            g2D.drawLine(leftSpace - 5, markPos, leftSpace, markPos);
        }
        g2D.drawLine(leftSpace, h - lowerSpace, w - rightSpace, h - lowerSpace);

        if (gV != null) 
	{
            float scale = (float)(dayCount - 1) / 9;
            for (int i = 0; i < 10; i++) 
	    {
                markPos = leftSpace + i * (diffW) / 9;
                g2D.drawLine(markPos, h - lowerSpace, markPos, h - lowerSpace + 5);
                g2D.drawString(gV.getDateForDayAt(Math.round(i * scale)), markPos - 10, h - lowerSpace + 20);
            }
        }

        //Target Zone
        Rectangle2D.Float rect1 = new Rectangle2D.Float(leftSpace + 1, BGtoCoord(maxGoodBG), drawableWidth, BGtoCoord(minGoodBG) - BGtoCoord(maxGoodBG));
        g2D.setPaint(m_da.getSettings().getColorTargetBG());
        g2D.fill(rect1);
        g2D.draw(rect1);

        //High Zone
        rect1 = new Rectangle2D.Float(leftSpace + 1, BGtoCoord(maxBG), drawableWidth, BGtoCoord(m_da.getSettings().getBG_High()) - BGtoCoord(maxBG));
        g2D.setPaint(m_da.getSettings().getColorHighBG());
        g2D.fill(rect1);
        g2D.draw(rect1);

        //Low Zone
        rect1 = new Rectangle2D.Float(leftSpace + 1, BGtoCoord(m_da.getSettings().getBG_Low()), drawableWidth, BGtoCoord(0) - BGtoCoord(m_da.getSettings().getBG_Low()) - 1);
        g2D.setPaint(m_da.getSettings().getColorLowBG());
        g2D.fill(rect1);
        g2D.draw(rect1);

    }

    protected void drawValues(Graphics2D g2D)
    {
        //CourseGraphFrame cGF = CourseGraphFrame.getInstance();

        GeneralPath plBG = new GeneralPath();
        //GeneralPath plAvgBGDay = new GeneralPath();
        GeneralPath plSumBU = new GeneralPath();
        GeneralPath plMeals = new GeneralPath();
        GeneralPath plSumIns1 = new GeneralPath();
        GeneralPath plSumIns2 = new GeneralPath();
        GeneralPath plSumIns = new GeneralPath();
        GeneralPath plInsPerBU = new GeneralPath();

        boolean firstBG = true;
        //boolean firstAvgBGDay = true;
        boolean firstMeals = true;
        boolean firstSumBU = true;
        boolean firstSumIns1 = true;
        boolean firstSumIns2 = true;
        boolean firstSumIns = true;
        boolean firstInsPerBU = true;

        DailyValues dV;

        g2D.setPaint(m_da.getSettings().getColorAvgBG());

        for (int i = 0; i < dayCount; i++) 
	{
            dV = gV.getDailyValuesForDay(i);
            float multiWidth = dayWidthC * i;
            float offset = multiWidth + leftSpace + dayWidthC / 2;

            //draw BG
            for (int j = 0; j < dV.getRowCount(); j++) {
                float tmpBG = dV.getBGAt(j);

                if (tmpBG != 0) {
                    int X = (int)(multiWidth + DateTimetoCoord(dV.getDateTimeAt(j)));
                    int Y = BGtoCoord(tmpBG);
                    if (firstBG) {
                        plBG.moveTo(X, Y);
                        firstBG = false;
                    } else
                        plBG.lineTo(X, Y);
                }
            }


            //draw avgBGDay
            //            if (firstAvgBGDay) {
            //                plAvgBGDay.moveTo(offset, BGtoCoord(dV.getAvgBG()));
            //                firstAvgBGDay = false;
            //            } else
            //                plAvgBGDay.lineTo(offset, BGtoCoord(dV.getAvgBG()));
            if (m_cGF.getDrawAvgBGDay()) 
	    {
                int tmp = BGtoCoord(dV.getAvgBG());
                g2D.drawLine((int)multiWidth + leftSpace, tmp, (int)(multiWidth + dayWidthC + leftSpace), tmp);
            }

            //draw sumBU
            if (firstSumBU) {
                plSumBU.moveTo(offset, BUtoCoord(dV.getSumCH()));
                firstSumBU = false;
            } else
                plSumBU.lineTo(offset, BUtoCoord(dV.getSumCH()));

            //draw sumMeals
            if (firstMeals) 
	    {
                plMeals.moveTo(offset, BUtoCoord(dV.getCHCount()));
                firstMeals = false;
            } 
	    else
                plMeals.lineTo(offset, BUtoCoord(dV.getCHCount()));

            //draw Ins1
            if (firstSumIns1) 
	    {
                plSumIns1.moveTo(offset, InstoCoord(dV.getSumIns1()));
                firstSumIns1 = false;
            } 
	    else
                plSumIns1.lineTo(offset, InstoCoord(dV.getSumIns1()));

            //draw Ins2
            if (firstSumIns2) {
                plSumIns2.moveTo(offset, InstoCoord(dV.getSumIns2()));
                firstSumIns2 = false;
            } else
                plSumIns2.lineTo(offset, InstoCoord(dV.getSumIns2()));

            //draw Ins
            if (firstSumIns) 
	    {
                plSumIns.moveTo(offset, InstoCoord(dV.getSumIns()));
                firstSumIns = false;
            } 
	    else
                plSumIns.lineTo(offset, InstoCoord(dV.getSumIns()));

            //draw Ins / BU
            if (firstInsPerBU) 
	    {
                plInsPerBU.moveTo(offset, InsPerBUtoCoord(dV.getIns2Count() / dV.getSumCH()));
                firstInsPerBU = false;
            } 
	    else
                plInsPerBU.lineTo(offset, InsPerBUtoCoord(dV.getIns2Count() / dV.getSumCH()));
	}

        if (m_cGF.getDrawBG()) 
	{
            g2D.setPaint(m_da.getSettings().getColorBG());
            g2D.draw(plBG);
        }

        //        if (cGF.getDrawAvgBGDay()) {
        //            g2D.setPaint(props.getColorAvgBG());
        //            g2D.draw(plAvgBGDay);
        //        }

	if (m_cGF.getDrawSumBU()) 
	{
            g2D.setPaint(m_da.getSettings().getColorBU());
            g2D.draw(plSumBU);
        }
        if (m_cGF.getDrawMeals()) 
	{
            g2D.setPaint(m_da.getSettings().getColorBU());
            g2D.draw(plMeals);
        }
        if (m_cGF.getDrawSumIns1()) 
	{
            g2D.setPaint(m_da.getSettings().getColorIns1());
            g2D.draw(plSumIns1);
        }

	if (m_cGF.getDrawSumIns2()) 
	{
            g2D.setPaint(m_da.getSettings().getColorIns2());
            g2D.draw(plSumIns2);
        }

	if (m_cGF.getDrawSumIns()) 
	{
            g2D.setPaint(m_da.getSettings().getColorIns());
            g2D.draw(plSumIns);
        }

        if (m_cGF.getDrawInsPerBU()) 
	{
            g2D.setPaint(m_da.getSettings().getColorInsPerBU());
            g2D.draw(plInsPerBU);
        }
    }
}
