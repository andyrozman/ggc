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
 *  Filename: AbstractGraphView.java
 *  Purpose:  Common methods and variables for all views.
 *
 *  Author:   schultd
 *  
 *  MODIFICATIONS:
 *    - 2006-04-08: RR * changed maxBG to 450 for mg/dl and 25 for mmol
 *                     * changed minBG to 20 (mg/dl) and 1 (mmol), don't think
 *                       there are meters around that display less
 */

package ggc.gui.view;


import ggc.util.DataAccess;
import ggc.util.GGCProperties;

import javax.swing.*;
import java.awt.*;


public abstract class AbstractGraphView extends JComponent
{
    Object oAA, oCR, oTAA, oR, oD, oFM, oI;

    DataAccess m_da = DataAccess.getInstance();

    //GGCProperties props = GGCProperties.getInstance();

    int BGunit, maxBG, minBG, BGDiff;
    int counter = 10;
    int upperSpace = 20, lowerSpace = 30, leftSpace = 40, rightSpace = 30;
    float maxGoodBG = m_da.getSettings().getTargetHighBG();
    float minGoodBG = m_da.getSettings().getTargetLowBG();

    int viewWidth = 0;
    int viewHeight = 0;
    float drawableWidth = 0;
    float drawableHeight = 0;
    float dayWidthC = 0;
    float hourWidthC = 0;
    float minuteWidthC = 0;
    float dayWidth = 0;
    float hourWidth = 0;
    float minuteWidth = 0;
    int dayCount = 0;
    
    String unitLabel = "mg/dl";

    public AbstractGraphView()
    {

        BGunit = m_da.getSettings().getMeasurmentTypeBg();

	switch (BGunit) 
	{
        case DataAccess.BG_MMOL:
            maxBG = 44;
            minBG = 0;
            unitLabel = "mmol/l";
            break;
        case DataAccess.BG_MGDL:
        default:
            maxBG = 450;
            minBG = 0;
            unitLabel = "mg/dl";
            break;
        }
        BGDiff = maxBG - minBG;
        
        getRenderingQuality();
    }

    protected abstract void drawFramework(Graphics2D g2D);

    protected abstract void drawValues(Graphics2D g2D);

    protected void calculateSizes()
    {
        Dimension dim = getSize();
        viewWidth = dim.width;
        viewHeight = dim.height;
        drawableWidth = viewWidth - rightSpace - leftSpace;
        drawableHeight = viewHeight - lowerSpace - upperSpace;

        //continuous drawing
        dayWidthC = drawableWidth / (dayCount > 0 ? dayCount : 1);
        hourWidthC = dayWidthC / 24;
        minuteWidthC = hourWidthC / 60;

        //whole width is one day
        dayWidth = drawableWidth;
        hourWidth = dayWidth / 24;
        minuteWidth = hourWidth / 60;
    }

    protected int BGtoCoord(float BG)
    {
        return (int)(drawableHeight + upperSpace - drawableHeight / BGDiff * BG);
    }

    protected int BUtoCoord(float BU)
    {
        return (int)(drawableHeight + upperSpace - drawableHeight / 60 * BU);
    }

    protected int InstoCoord(float Ins)
    {
        return (int)(drawableHeight + upperSpace - drawableHeight / 60 * Ins);
    }

    protected int InsPerBUtoCoord(float factor)
    {
        return (int)(drawableHeight + upperSpace - drawableHeight / 4 * factor);
    }

    protected int DateTimetoCoord(java.util.Date time)
    {
        int timeH = time.getHours();
        int timeM = time.getMinutes();

        return (int)(leftSpace + timeH * hourWidthC + timeM * minuteWidthC);
    }

    protected int TimetoCoord(java.util.Date time)
    {
        int timeH = time.getHours();
        int timeM = time.getMinutes();

        return (int)(leftSpace + timeH * hourWidth + timeM * minuteWidth);
    }

    public void setNewRenderingQuality()
    {
        getRenderingQuality();
    }

    private void getRenderingQuality()
    {
	oAA = m_da.getSettings().getAntiAliasing();
	oCR = m_da.getSettings().getColorRendering();
        oD = m_da.getSettings().getDithering();
        oFM = m_da.getSettings().getFractionalMetrics();
        oI = m_da.getSettings().getInterpolation();
	oR = m_da.getSettings().getRendering();
	oTAA = m_da.getSettings().getTextAntiAliasing();
    }
}
