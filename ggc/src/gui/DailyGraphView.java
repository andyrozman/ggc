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
 *  Filename: DailyGraphView.java
 *  Purpose:  Drawing all data for one day.
 *
 *  Author:   schultd
 */

package gui;


import datamodels.DailyValues;
import util.GGCProperties;

import javax.swing.*;
import java.awt.*;
import java.awt.geom.GeneralPath;
import java.awt.geom.Rectangle2D;
import java.sql.Time;
import java.util.Observable;
import java.util.Observer;


public class DailyGraphView extends JComponent implements Observer
{
    int maxBG = 300, minBG = 0;
    int counter = 10;
    int upperSpace = 10, lowerSpace = 30, leftSpace = 40, rightSpace = 30;
    float maxGoodBG, minGoodBG;

    Object oAA, oCR, oTAA, oR, oD, oFM, oI;

    DailyValues dayData;

    GGCProperties props;

    public DailyGraphView()
    {
        dayData = DailyValues.getInstance();
        props = GGCProperties.getInstance();
        maxGoodBG = props.getTargetHighBG();
        minGoodBG = props.getTargetLowBG();
        setRenderingQuality();
    }

    public void paint(Graphics g)
    {
        Graphics2D g2D = (Graphics2D)g;

        drawFramework(g2D);
        drawValues(g2D);
    }

    public void update(Observable o, Object rectangle)
    {
    }

    private void drawValues(Graphics2D g2D)
    {
        GeneralPath polyline = new GeneralPath(GeneralPath.WIND_EVEN_ODD, dayData.getBGCount());
        int tmpC = 0;
        for (int i = 0; i < dayData.getRowCount(); i++) {
            float tmpBG = dayData.getBGAt(i);

            if (tmpBG != 0) {
                int X = TimetoCoord(dayData.getTimeAt(i));
                int Y = BGtoCoord(tmpBG);
                if (tmpC == 0)
                    polyline.moveTo(X, Y);
                else
                    polyline.lineTo(X, Y);
                tmpC++;
            }
        }
        g2D.setPaint(Color.black);
        g2D.draw(polyline);
    }

    private void drawFramework(Graphics2D g2D)
    {
        Dimension dim = getSize();
        int h = dim.height, w = dim.width;

        int markPos = 0;
        int diffBG = maxBG - minBG;
        int diffH = h - lowerSpace - upperSpace;
        int diffW = w - rightSpace - leftSpace;
        /*
		upperSpace
		|
		leftSpace  |
		|
		|-------------------------- rightSpace
		lowerSpace
		*/

        Object o;

        g2D.setRenderingHint(RenderingHints.KEY_ANTIALIASING, oAA);
        g2D.setRenderingHint(RenderingHints.KEY_COLOR_RENDERING, oCR);
        g2D.setRenderingHint(RenderingHints.KEY_DITHERING, oD);
        g2D.setRenderingHint(RenderingHints.KEY_FRACTIONALMETRICS, oFM);
        g2D.setRenderingHint(RenderingHints.KEY_INTERPOLATION, oI);
        g2D.setRenderingHint(RenderingHints.KEY_RENDERING, oR);
        g2D.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, oTAA);


        g2D.setBackground(Color.white);
        g2D.setPaint(Color.black);
        g2D.drawLine(leftSpace, upperSpace, leftSpace, h - lowerSpace);
        for (int i = 0; i <= counter; i++) {
            markPos = upperSpace + i * (diffH) / counter;
            g2D.drawString((maxBG - (diffBG) / counter * i) + "", 5, markPos + 5);
            g2D.drawLine(leftSpace - 5, markPos, leftSpace, markPos);
        }
        g2D.drawLine(leftSpace, h - lowerSpace, w - rightSpace, h - lowerSpace);
        for (int i = 0; i <= 6; i++) {
            markPos = leftSpace + i * (diffW) / 6;
            g2D.drawLine(markPos, h - lowerSpace, markPos, h - lowerSpace + 5);
            g2D.drawString(4 * i + ":00", markPos - 10, h - lowerSpace + 20);
        }
        Rectangle2D.Float rect1 = new Rectangle2D.Float(leftSpace + 1, BGtoCoord(maxGoodBG), diffW, BGtoCoord(minGoodBG) - BGtoCoord(maxGoodBG));
        g2D.setPaint(Color.lightGray);
        g2D.fill(rect1);
        g2D.draw(rect1);
    }

    private int BGtoCoord(float BG)
    {
        Dimension dim = getSize();
        int h = dim.height;
        float diffBG = maxBG - minBG;
        float diffH = h - lowerSpace - upperSpace;

        return (int)(diffH + upperSpace - diffH / diffBG * BG);
    }

    private int TimetoCoord(Time time)
    {
        Dimension dim = getSize();
        int w = dim.width;
        float diffW = w - rightSpace - leftSpace;
        float hourW = diffW / 24;
        float minW = hourW / 60;
        int timeH = time.getHours();
        int timeM = time.getMinutes();

        return (int)(leftSpace + timeH * hourW + timeM * minW);
    }

    private void setRenderingQuality()
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

    public void setNewRenderingQuality()
    {
        setRenderingQuality();
    }
}