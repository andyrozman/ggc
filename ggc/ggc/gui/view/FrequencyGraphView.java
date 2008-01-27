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
 *  Filename: FrequencyGraphView.java
 *  Purpose:  Shows the frequency of some values.
 *
 *  Author:   schultd
 */

package ggc.gui.view;


import ggc.data.DailyValues;
import ggc.data.GlucoValues;

import java.awt.*;


public class FrequencyGraphView extends AbstractGraphView
{
    GlucoValues gV = null;

    int[] values = null;
    int minvalue;
    int maxvalue;
    int maxcount;

    int avgBG = 0;

    public FrequencyGraphView()
    {
        super();
        dayCount = 0;
    }

    public FrequencyGraphView(GlucoValues gV)
    {
        this();
        setGlucoValues(gV);
    }

    public void setGlucoValues(GlucoValues gV)
    {
        this.gV = gV;
        dayCount = gV.getDayCount();


        values = new int[500];
        minvalue = 500;
        maxvalue = 0;
        maxcount = 0;

        long sum = 0;
        int c = 0;

        for (int i = 0; i < dayCount; i++) 
	{
            DailyValues dV = gV.getDailyValuesForDay(i);
            for (int j = 0; j < dV.getRowCount(); j++) 
	    {
                int tmp = (int)dV.getBGAt(j);
                if (tmp > 0) 
		{
                    if (tmp < minvalue)
                        minvalue = tmp;
                    if (tmp > maxvalue)
                        maxvalue = tmp;
                    if (++values[tmp] > maxcount)
                        maxcount = values[tmp];
                    sum += tmp;
                    c++;
                }
            }
        }
        
        if (c > 0)
        {
            avgBG = (int)(sum / c);
        }
        else
        {
            avgBG = 0;
        }
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
        g2D.setPaint(Color.white);
        g2D.fillRect(0, 0, viewWidth, viewHeight);

        g2D.setPaint(Color.black);
        g2D.drawLine(leftSpace, upperSpace, leftSpace, viewHeight - lowerSpace);

        int markPos = 0;
//x        int diffBG = maxBG - minBG;
        int diffH = (int)drawableHeight;
//x        int diffW = (int)drawableWidth;

        if (maxcount == 0)
            maxcount = 1;
        int z = (int)Math.floor(maxcount / 10);
        if (z == 0)
            z = 1;
//x        int steps = (maxcount / z);

        for (int i = 0; i <= maxcount; i += z) 
	{
            markPos = upperSpace + i * (diffH) / maxcount;
            g2D.drawString(maxcount - i + "", 5, markPos + 5);
            g2D.drawLine(leftSpace - 5, markPos, leftSpace, markPos);
        }

	g2D.drawLine(leftSpace, viewHeight - lowerSpace, viewWidth - rightSpace, viewHeight - lowerSpace);

        for (int i = 0; i <= maxvalue; i += 30) 
	{
            markPos = leftSpace + i;
            g2D.drawLine(markPos, viewHeight - lowerSpace, markPos, viewHeight - lowerSpace + 5);
            g2D.drawString(i + "", markPos - 10, viewHeight - lowerSpace + 20);
        }

        int lower = (int)m_da.getSettings().getBG_Low();
//        int upper = (int)m_da.getSettings().getBG_High();

        g2D.setPaint(m_da.getSettings().getColorLowBG());
        g2D.fillRect(leftSpace + 1, upperSpace, lower, (int)drawableHeight);

        g2D.setPaint(m_da.getSettings().getColorTargetBG());
        g2D.fillRect((int)(leftSpace + m_da.getSettings().getBG_TargetLow()), upperSpace, (int)(m_da.getSettings().getBG_TargetHigh() - m_da.getSettings().getBG_TargetLow()), (int)drawableHeight);

        g2D.setPaint(m_da.getSettings().getColorHighBG());
        g2D.fillRect((int)(leftSpace + m_da.getSettings().getBG_High()), upperSpace, (int)(drawableWidth - m_da.getSettings().getBG_High()), (int)drawableHeight);

    }

    @Override
    protected void drawValues(Graphics2D g2D)
    {
        if (values != null) 
	{
            float unitHeight = drawableHeight / maxcount;

            g2D.setPaint(Color.black);
            for (int i = minvalue; i <= maxvalue; i++) 
	    {
                g2D.drawLine(leftSpace + i + 1, viewHeight - lowerSpace, leftSpace + i + 1, (int)(viewHeight - lowerSpace - values[i] * unitHeight));
            }

            g2D.setPaint(m_da.getSettings().getColorAvgBG());
            g2D.drawLine(leftSpace + 1 + avgBG, viewHeight - lowerSpace, leftSpace + 1 + avgBG, upperSpace);
        }
    }
}
