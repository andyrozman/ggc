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
 */

package ggc.view;


import ggc.util.GGCProperties;

import javax.swing.*;
import java.awt.*;


public abstract class AbstractGraphView extends JComponent
{
    Object oAA, oCR, oTAA, oR, oD, oFM, oI;

    GGCProperties props = GGCProperties.getInstance();

    int maxBG = 300;
    int minBG = 0;
    int counter = 10;
    int upperSpace = 10, lowerSpace = 30, leftSpace = 40, rightSpace = 30;
    float maxGoodBG = props.getTargetHighBG();
    float minGoodBG = props.getTargetLowBG();

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
    int BGDiff = maxBG - minBG;

    public AbstractGraphView()
    {
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
        switch (props.getAntiAliasing()) {
            case 1:
                oAA = RenderingHints.VALUE_ANTIALIAS_OFF;
                break;
            case 2:
                oAA = RenderingHints.VALUE_ANTIALIAS_ON;
                break;
            default:
                oAA = RenderingHints.VALUE_ANTIALIAS_DEFAULT;
        }
        //System.out.println("rendering " + oAA);

        switch (props.getColorRendering()) {
            case 1:
                oCR = RenderingHints.VALUE_COLOR_RENDER_QUALITY;
                break;
            case 2:
                oCR = RenderingHints.VALUE_COLOR_RENDER_SPEED;
                break;
            default:
                oCR = RenderingHints.VALUE_COLOR_RENDER_DEFAULT;
        }
        //System.out.println("colorrend " + oCR);

        switch (props.getDithering()) {
            case 1:
                oD = RenderingHints.VALUE_DITHER_DISABLE;
                break;
            case 2:
                oD = RenderingHints.VALUE_DITHER_ENABLE;
                break;
            default:
                oD = RenderingHints.VALUE_DITHER_DEFAULT;
        }
        //System.out.println("dithering " + oD);

        switch (props.getFractionalMetrics()) {
            case 1:
                oFM = RenderingHints.VALUE_FRACTIONALMETRICS_OFF;
                break;
            case 2:
                oFM = RenderingHints.VALUE_FRACTIONALMETRICS_ON;
                break;
            default:
                oFM = RenderingHints.VALUE_FRACTIONALMETRICS_DEFAULT;
        }
        //System.out.println("fractional " + oFM);

        switch (props.getInterpolation()) {
            case 1:
                oI = RenderingHints.VALUE_INTERPOLATION_BICUBIC;
                break;
            case 2:
                oI = RenderingHints.VALUE_INTERPOLATION_BILINEAR;
                break;
            default:
                oI = RenderingHints.VALUE_INTERPOLATION_NEAREST_NEIGHBOR;
        }
        //System.out.println("interpolation " + oI);

        switch (props.getRendering()) {
            case 1:
                oR = RenderingHints.VALUE_RENDER_QUALITY;
                break;
            case 2:
                oR = RenderingHints.VALUE_RENDER_SPEED;
                break;
            default:
                oR = RenderingHints.VALUE_RENDER_DEFAULT;
        }
        //System.out.println("rendering " + oR);

        switch (props.getTextAntiAliasing()) {
            case 1:
                oTAA = RenderingHints.VALUE_TEXT_ANTIALIAS_OFF;
                break;
            case 2:
                oTAA = RenderingHints.VALUE_TEXT_ANTIALIAS_ON;
                break;
            default:
                oTAA = RenderingHints.VALUE_TEXT_ANTIALIAS_DEFAULT;
        }
        //System.out.println("text AA " + oTAA);
    }
}
