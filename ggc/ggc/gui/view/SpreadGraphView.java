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
 *  Filename: SpreadGraphView.java
 *  Purpose:  Draws the spread of values over the day.
 *
 *  Author:   schultd
 */

package ggc.gui.view;


import ggc.data.DailyValues;
import ggc.data.GlucoValues;
import ggc.gui.dialogs.SpreadGraphDialog;

import java.awt.*;
import java.awt.geom.GeneralPath;
import java.awt.geom.Rectangle2D;

import ggc.gui.dialogs.*;


public class SpreadGraphView extends AbstractGraphView
{

    GlucoValues gV = null;
    SpreadGraphDialog m_sGF = null;


    public SpreadGraphView(SpreadGraphDialog spread)
    {
        super();
	m_sGF = spread;
        dayCount = 0;
    }


    public SpreadGraphView(GlucoValues gV, SpreadGraphDialog spread)
    {
        this(spread);
        this.gV = gV;
        dayCount = gV.getDayCount();
    }


    public void setGlucoValues(GlucoValues gV)
    {
        this.gV = gV;
        dayCount = gV.getDayCount();
    }


    @Override
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
            markPos = upperSpace + i * (diffH) / counter;
            g2D.drawString(Math.round(maxBG - labelDeltaV * i) + "", 5, markPos + 5);
            g2D.drawLine(leftSpace - 5, markPos, leftSpace, markPos);
        }
        g2D.drawLine(leftSpace, h - lowerSpace, w - rightSpace, h - lowerSpace);

        for (int i = 0; i <= 6; i++) 
	{
            markPos = leftSpace + i * (diffW) / 6;
            g2D.drawLine(markPos, h - lowerSpace, markPos, h - lowerSpace + 5);
            g2D.drawString(4 * i + ":00", markPos - 10, h - lowerSpace + 20);
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

    @Override
    protected void drawValues(Graphics2D g2D)
    {
        //SpreadGraphFrame sGF = SpreadGraphFrame.getInstance();

        GeneralPath plBG = new GeneralPath();
        GeneralPath plBU = new GeneralPath();
        GeneralPath plIns1 = new GeneralPath();
        GeneralPath plIns2 = new GeneralPath();

        boolean firstBG = true;
        boolean firstBU = true;
        boolean firstIns1 = true;
        boolean firstIns2 = true;

        DailyValues dV;

        Color colorBG = m_da.getSettings().getColorBG();
        Color colorBU = m_da.getSettings().getColorBU();
        Color colorIns1 = m_da.getSettings().getColorIns1();
        Color colorIns2 = m_da.getSettings().getColorIns2();

        boolean drawBG = m_sGF.getDrawBG();
        boolean drawBU = m_sGF.getDrawBU();
        boolean drawIns1 = m_sGF.getDrawIns1();
        boolean drawIns2 = m_sGF.getDrawIns2();

        boolean connect = m_sGF.getConnectDays();

        for (int i = 0; i < dayCount; i++) 
	{
            dV = gV.getDailyValuesForDay(i);

            for (int j = 0; j < dV.getRowCount(); j++) 
	    {
                int offset = TimetoCoord(dV.getDateTimeAt(j));

                //draw BG
                float tmpBG = dV.getBGAt(j);
                if (drawBG && tmpBG != 0) 
		{
                    g2D.setPaint(colorBG);
                    g2D.fillRect(offset - 1, BGtoCoord(tmpBG) - 1, 3, 3);

                    if (connect) 
		    {
                        if (firstBG) 
			{
                            plBG.moveTo(offset, BGtoCoord(tmpBG));
                            firstBG = false;
                        } 
			else
                            plBG.lineTo(offset, BGtoCoord(tmpBG));
                    }
                }

                //draw BU
                float tmpBU = dV.getCHAt(j);
                if (drawBU && tmpBU != 0) 
		{
                    g2D.setPaint(colorBU);
                    g2D.fillRect(offset - 1, BUtoCoord(tmpBU) - 1, 3, 3);

                    if (connect) 
		    {
                        if (firstBU) 
			{
                            plBU.moveTo(offset, BUtoCoord(tmpBU));
                            firstBU = false;
                        } 
			else
                            plBU.lineTo(offset, BUtoCoord(tmpBU));
                    }
                }

                //draw Ins1
                float tmpIns1 = dV.getIns1At(j);
                if (drawIns1 && tmpIns1 != 0) 
		{
                    g2D.setPaint(colorIns1);
                    g2D.fillRect(offset - 1, InstoCoord(tmpIns1) - 1, 3, 3);

                    if (connect) 
		    {
                        if (firstIns1) 
			{
                            plIns1.moveTo(offset, InstoCoord(tmpIns1));
                            firstIns1 = false;
                        } 
			else
                            plIns1.lineTo(offset, InstoCoord(tmpIns1));
                    }
                }

                //draw Ins2
                float tmpIns2 = dV.getIns2At(j);
                if (drawIns2 && tmpIns2 != 0) 
		{
                    g2D.setPaint(colorIns2);
                    g2D.fillRect(offset - 1, InstoCoord(tmpIns2) - 1, 3, 3);

                    if (connect) 
		    {
                        if (firstIns2) 
			{
                            plIns2.moveTo(offset, InstoCoord(tmpIns2));
                            firstIns2 = false;
                        } 
			else
                            plIns2.lineTo(offset, InstoCoord(tmpIns2));
                    }
                }
            }

            if (drawBG) 
	    {
                g2D.setPaint(m_da.getSettings().getColorBG());
                g2D.draw(plBG);
            }

	    if (drawBU) 
	    {
                g2D.setPaint(m_da.getSettings().getColorBU());
                g2D.draw(plBU);
            }

	    if (drawIns1) 
	    {
                g2D.setPaint(m_da.getSettings().getColorIns1());
                g2D.draw(plIns1);
            }

	    if (drawIns2) 
	    {
                g2D.setPaint(m_da.getSettings().getColorIns2());
                g2D.draw(plIns2);
            }

            firstBG = true;
            firstBU = true;
            firstIns1 = true;
            firstIns2 = true;
        }
    }
}
