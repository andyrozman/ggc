package ggc.gui.graphs;

import ggc.core.data.DailyValues;
import ggc.core.data.GlucoValues;
import ggc.core.data.ReadablePlotData;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;

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
 *  Filename:     FrequencyGraphView.java
 *  Description:  Shows the frequency of some values. (will be DEPRECATED) !
 *
 *  Author:   schultd
 *  Author:   reini
 * 
 *  Author: andyrozman {andy@atech-software.com}  
 */

public class FrequencyGraphView extends AbstractGraphView
{
    private static final long serialVersionUID = 8358248079574399547L;

    GlucoValues gV = null;
    ReadablePlotData data = null;

    int[] values = null;
    int minvalue;
    int maxvalue;
    int maxcount;

    int avgBG = 0;

    /**
     * Constructor
     */
    public FrequencyGraphView()
    {
        super();
        dayCount = 0;
    }

    /**
     * Constructor
     * 
     * @param gV
     */
    public FrequencyGraphView(GlucoValues gV)
    {
        this();
        setGlucoValues(gV);
    }

    /**
     * Set Gluco Values
     * 
     * @param gV
     */
    public void setGlucoValues(GlucoValues gV)
    {
        this.gV = gV;
        dayCount = gV.getDailyValuesItemsCount();


        values = new int[500];
        minvalue = 500;
        maxvalue = 0;
        maxcount = 0;

        long sum = 0;
        int c = 0;

        for (int i = 0; i < dayCount; i++) 
	{
            DailyValues dV = gV.getDailyValuesItem(i);
            for (int j = 0; j < dV.getRowCount(); j++) 
	    {
                int tmp = (int)dV.getRow(j).getBG();
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

    /**
     * @param data
     *            the data to set
     */
    public void setData(ReadablePlotData data)
    {
        this.data = data;
    }

    /**
     * Painting method
     * 
     * @see javax.swing.JComponent#paint(java.awt.Graphics)
     */
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

//x        int lower = (int)m_da.getSettings().getBG_Low();
//        int upper = (int)m_da.getSettings().getBG_High();

        // XXX: these need something similar to BGtoCoord, but that's too much effort
        g2D.setPaint(m_da.getColor(m_da.getSettings().getSelectedColorScheme().getColor_bg_low()));
        g2D.fillRect(leftSpace + 1, upperSpace, (int) minGoodBG, (int)drawableHeight);

        g2D.setPaint(m_da.getColor(m_da.getSettings().getSelectedColorScheme().getColor_bg_target()));
        g2D.fillRect((int)(leftSpace + minGoodBG), upperSpace, (int)(maxGoodBG - minGoodBG), (int)drawableHeight);

        g2D.setPaint(m_da.getColor(m_da.getSettings().getSelectedColorScheme().getColor_bg_high()));
        g2D.fillRect((int)(leftSpace + maxGoodBG), upperSpace, (int)(drawableWidth - maxGoodBG), (int)drawableHeight);

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

            g2D.setPaint(m_da.getColor(m_da.getSettings().getSelectedColorScheme().getColor_bg_avg()));
            g2D.drawLine(leftSpace + 1 + avgBG, viewHeight - lowerSpace, leftSpace + 1 + avgBG, upperSpace);
        }
    }
}
