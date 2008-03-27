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
 *  Filename: HbA1cView.java
 *  Purpose:  Visualize the HbA1c quality.
 *
 *  Author:   schultd
 *  Author:   reini
 *  
 */

package ggc.gui.view;


import ggc.data.HbA1cValues;
import ggc.util.I18nControl;

import java.awt.*;
import java.text.DecimalFormat;


public class HbA1cView extends AbstractGraphView
{
    private HbA1cValues hbValues = null;

    private I18nControl m_ic = I18nControl.getInstance();

    public HbA1cView()
    {
        super();
    }

    public HbA1cView(HbA1cValues hbValues)
    {
        this();
        setHbA1cValues(hbValues);
    }

    public void setHbA1cValues(HbA1cValues hbValues)
    {
        this.hbValues = hbValues;
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

        drawValues(g2D);
    }

    @Override
    protected void drawFramework(Graphics2D g2D)
    {
    }

    @Override
    protected void drawValues(Graphics2D g2D)
    {
        g2D.setPaint(Color.white);
        g2D.fillRect(0, 0, viewWidth, viewHeight);

        int offset = 25;
        int width = viewWidth - 50;
        int CenterX = offset;
        int CenterY = offset;
        float arc = 0;
        float sumarc = 0;
        int h = viewHeight - 110; // -70

        g2D.setPaint(Color.red);
        arc = hbValues.getPercentOfDaysInClass(0) * 360;
        g2D.fillArc(CenterX, CenterY, width, width, (int)sumarc, (int)arc);
        sumarc += arc;
        g2D.fillRect(offset, h - 10, 12, 12);

        g2D.setPaint(Color.orange);
        arc = hbValues.getPercentOfDaysInClass(1) * 360;
        g2D.fillArc(CenterX, CenterY, width, width, (int)sumarc, (int)arc);
        sumarc += arc;
        g2D.fillRect(offset, h + 5, 12, 12);

        g2D.setPaint(Color.yellow);
        arc = hbValues.getPercentOfDaysInClass(2) * 360;
        g2D.fillArc(CenterX, CenterY, width, width, (int)sumarc, (int)arc);
        sumarc += arc;
        g2D.fillRect(offset, h + 20, 12, 12);

        g2D.setPaint(Color.green.brighter());
        arc = hbValues.getPercentOfDaysInClass(3) * 360;
        g2D.fillArc(CenterX, CenterY, width, width, (int)sumarc, (int)arc);
        sumarc += arc;
        g2D.fillRect(offset, h + 35, 12, 12);

        g2D.setPaint(Color.green.darker());
        arc = hbValues.getPercentOfDaysInClass(4) * 360;
        g2D.fillArc(CenterX, CenterY, width, width, (int)sumarc, (int)arc);
        g2D.fillRect(offset, h + 50, 12, 12);

      
        //Legend
        g2D.setPaint(Color.black);
        DecimalFormat df = new DecimalFormat("00.00");
        g2D.drawString(m_ic.getMessage("DAYS_WITH_READINGS_0_1") + " [" + df.format(hbValues.getPercentOfDaysInClass(0) * 100) + " %]", offset + 20, h);
        g2D.drawString(m_ic.getMessage("DAYS_WITH_READINGS_2_3") + " [" + df.format(hbValues.getPercentOfDaysInClass(1) * 100) + " %]", offset + 20, h + 15);
        g2D.drawString(m_ic.getMessage("DAYS_WITH_READINGS_4_5") + " [" + df.format(hbValues.getPercentOfDaysInClass(2) * 100) + " %]", offset + 20, h + 30);
        g2D.drawString(m_ic.getMessage("DAYS_WITH_READINGS_6_7") + " [" + df.format(hbValues.getPercentOfDaysInClass(3) * 100) + " %]", offset + 20, h + 45);
        g2D.drawString(m_ic.getMessage("DAYS_WITH_READINGS_MORE_7")+"    [" + df.format(hbValues.getPercentOfDaysInClass(4) * 100) + " %]", offset + 20, h + 60);

    }
}
