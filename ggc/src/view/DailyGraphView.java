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

package view;


import datamodels.DailyValues;

import java.awt.*;
import java.awt.geom.GeneralPath;
import java.awt.geom.Rectangle2D;


public class DailyGraphView extends AbstractGraphView
{
    DailyValues dayData;

    public DailyGraphView()
    {
        super();
        dayData = DailyValues.getInstance();
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

    public void setDailyValues(DailyValues dV)
    {
        dayData = dV;
    }

    protected void drawValues(Graphics2D g2D)
    {
        GeneralPath polyline = new GeneralPath();
        int tmpC = 0;
        for (int i = 0; i < dayData.getRowCount(); i++) {
            float tmpBG = dayData.getBGAt(i);

            if (tmpBG != 0) {
                int X = TimetoCoord(dayData.getDateTimeAt(i));
                int Y = BGtoCoord(tmpBG);
                if (tmpC == 0)
                    polyline.moveTo(X, Y);
                else
                    polyline.lineTo(X, Y);
                tmpC++;
            }
        }
        g2D.setPaint(props.getColorBG());
        g2D.draw(polyline);
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
        //Target Zone
        Rectangle2D.Float rect1 = new Rectangle2D.Float(leftSpace + 1, BGtoCoord(maxGoodBG), drawableWidth, BGtoCoord(minGoodBG) - BGtoCoord(maxGoodBG));
        g2D.setPaint(props.getColorTargetBG());
        g2D.fill(rect1);
        g2D.draw(rect1);

        //High Zone
        rect1 = new Rectangle2D.Float(leftSpace + 1, BGtoCoord(maxBG), drawableWidth, BGtoCoord(props.getHighBG()) - BGtoCoord(maxBG));
        g2D.setPaint(props.getColorHighBG());
        g2D.fill(rect1);
        g2D.draw(rect1);

        //Low Zone
        rect1 = new Rectangle2D.Float(leftSpace + 1, BGtoCoord(props.getLowBG()), drawableWidth, BGtoCoord(0) - BGtoCoord(props.getLowBG()) - 1);
        g2D.setPaint(props.getColorLowBG());
        g2D.fill(rect1);
        g2D.draw(rect1);
    }
}