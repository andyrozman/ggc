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

package ggc.gui.graphs;

import ggc.core.data.HbA1cValues;

import java.awt.BorderLayout;
import java.awt.Color;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PiePlot;
import org.jfree.data.general.DefaultPieDataset;

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
 *  Filename:     HbA1cView  
 *  Description:  Class for HbA1c view, old framework
 * 
 *  Author: andyrozman {andy@atech-software.com}  
 */

public class HbA1cView extends JFAbstractGraphView
{
    private static final long serialVersionUID = -4029577798993120891L;

    private HbA1cValues data; // = new HbA1cValues();
    private DefaultPieDataset dataset = new DefaultPieDataset();

    
    
    /**
     * Constructor
     * 
     * @param hbValues
     */
    public HbA1cView(HbA1cValues hbValues)
    {
        data = hbValues;
        chart = ChartFactory.createPieChart3D(null, dataset, true, true, false);
        
        //title, dataset, legend, tooltips, urls).createPieChart(
        chartPanel = new ChartPanel(chart, false, true, true, false, true);
        redraw();
        setLayout(new BorderLayout());
        add(chartPanel, BorderLayout.CENTER);
    }
/*
    public void setHbA1cValues(HbA1cValues hbValues)
    {
        data = hbValues;
    }
*/
    @Override
    protected void drawFramework(JFreeChart chart)
    {
        if ((data == null) || (chart == null))
        {
            return;
        }

        PiePlot plot = (PiePlot) chart.getPlot();

        //chart.setRenderingHints(renderingHints);

        plot.setBackgroundPaint(backgroundColor);
        plot.setCircular(true);
        //plot.setBackgroundAlpha(0.5f);
        plot.setForegroundAlpha(0.5f);
        //plot.s
        plot.setSectionPaint(translator.getMessage("DAYS_WITH_READINGS_0_1"), Color.RED);
        plot.setSectionPaint(translator.getMessage("DAYS_WITH_READINGS_2_3"), Color.ORANGE);
        plot.setSectionPaint(translator.getMessage("DAYS_WITH_READINGS_4_5"), Color.YELLOW);
        plot.setSectionPaint(translator.getMessage("DAYS_WITH_READINGS_6_7"), Color.GREEN.brighter());
        plot.setSectionPaint(translator.getMessage("DAYS_WITH_READINGS_MORE_7"), Color.GREEN.darker());
    }

    @Override
    protected void drawValues(JFreeChart chart)
    {
        if ((data == null) || (dataset == null))
        {
            return;
        }

        dataset.clear();

        System.out.println("Read classes:\n" + data.getPercentOfDaysInClass(0) + "\n" + data.getPercentOfDaysInClass(1) + "\n"
                + data.getPercentOfDaysInClass(2) + "\n" + data.getPercentOfDaysInClass(3) + "\n"
                + data.getPercentOfDaysInClass(4));
        dataset.insertValue(0, translator.getMessage("DAYS_WITH_READINGS_0_1"), data.getPercentOfDaysInClass(0));
        dataset.insertValue(1, translator.getMessage("DAYS_WITH_READINGS_2_3"), data.getPercentOfDaysInClass(1));
        dataset.insertValue(2, translator.getMessage("DAYS_WITH_READINGS_4_5"), data.getPercentOfDaysInClass(2));
        dataset.insertValue(3, translator.getMessage("DAYS_WITH_READINGS_6_7"), data.getPercentOfDaysInClass(3));
        dataset.insertValue(4, translator.getMessage("DAYS_WITH_READINGS_MORE_7"), data.getPercentOfDaysInClass(4));
    }
}
