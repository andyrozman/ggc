package ggc.core.data.graph;

import ggc.core.data.HbA1cValues;
import ggc.core.util.DataAccess;

import java.awt.Color;
import java.awt.Rectangle;
import java.util.GregorianCalendar;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PiePlot;
import org.jfree.data.general.AbstractDataset;
import org.jfree.data.general.DefaultPieDataset;

import com.atech.graphics.graphs.AbstractGraphViewAndProcessor;
import com.atech.graphics.graphs.GraphViewControlerInterface;
import com.atech.i18n.I18nControlAbstract;

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
 *  Filename:     GraphViewHbA1c  
 *  Description:  GraphView implementation for HbA1c, used by new graph framework
 * 
 *  Author: andyrozman {andy@atech-software.com}  
 */


public class GraphViewHbA1c extends AbstractGraphViewAndProcessor //implements GraphViewInterface, GraphViewDataProcessorInterface 
{

    private HbA1cValues hbValues; 
    private DefaultPieDataset dataset = new DefaultPieDataset();
    I18nControlAbstract  m_ic = null;
    
    /**
     * Constructor
     */
    public GraphViewHbA1c()
    {
        super(DataAccess.getInstance());
    }
    

    /**
     * Get Data Object (HbA1cValues)
     * @return
     */
    public HbA1cValues getDataObject()
    {
        loadData();
        return hbValues;
    }
    
    
    /**
     * Get Controler Interface instance
     * 
     * @return GraphViewControlerInterface instance or null
     */
    public GraphViewControlerInterface getControler()
    {
        return null;
    }

    /**
     * Get Help Id
     * 
     * @return
     */
    public String getHelpId()
    {
        return "pages.GGC_BG_HbA1c";
    }

    /**
     * Get Title (used by GraphViewer)
     * 
     * @return title as string 
     */
    public String getTitle()
    {
        return null;
    }

    
    /**
     * Get Viewer Dialog Bounds (used by GraphViewer)
     * 
     * @return Rectangle object
     */
    public Rectangle getViewerDialogBounds()
    {
        return null;
    }

    
    /**
     * Load Data
     */
    public void loadData()
    {
        if (hbValues==null)
            hbValues = ((DataAccess)m_da).getDb().getHbA1c(new GregorianCalendar(), false);        
    }

    
    /**
     * Get Data Set
     * 
     * @return AbstractDataset instance
     */
    public AbstractDataset getDataSet()
    {
        return this.dataset;
    }

    /**
     * Preprocess Data
     */
    public void preprocessData()
    {
        dataset.clear();

        System.out.println("Read HbA1c data:\n" + hbValues.getPercentOfDaysInClass(0) + "\n" + hbValues.getPercentOfDaysInClass(1) + "\n"
                + hbValues.getPercentOfDaysInClass(2) + "\n" + hbValues.getPercentOfDaysInClass(3) + "\n"
                + hbValues.getPercentOfDaysInClass(4));
        dataset.insertValue(0, m_ic.getMessage("DAYS_WITH_READINGS_0_1"), hbValues.getPercentOfDaysInClass(0));
        dataset.insertValue(1, m_ic.getMessage("DAYS_WITH_READINGS_2_3"), hbValues.getPercentOfDaysInClass(1));
        dataset.insertValue(2, m_ic.getMessage("DAYS_WITH_READINGS_4_5"), hbValues.getPercentOfDaysInClass(2));
        dataset.insertValue(3, m_ic.getMessage("DAYS_WITH_READINGS_6_7"), hbValues.getPercentOfDaysInClass(3));
        dataset.insertValue(4, m_ic.getMessage("DAYS_WITH_READINGS_MORE_7"), hbValues.getPercentOfDaysInClass(4));
        
    }

    /**
     * Set Plot
     * 
     * @param chart JFreeChart instance
     */
    public void setPlot(JFreeChart chart)
    {
        PiePlot plot = (PiePlot) chart.getPlot();

        //chart.setRenderingHints(renderingHints);

        plot.setBackgroundPaint(Color.white); //backgroundColor);
        plot.setCircular(true);
        //plot.setBackgroundAlpha(0.5f);
        plot.setForegroundAlpha(0.5f);
        //plot.s
        plot.setSectionPaint(m_ic.getMessage("DAYS_WITH_READINGS_0_1"), Color.RED);
        plot.setSectionPaint(m_ic.getMessage("DAYS_WITH_READINGS_2_3"), Color.BLUE);
        plot.setSectionPaint(m_ic.getMessage("DAYS_WITH_READINGS_4_5"), Color.YELLOW);
        plot.setSectionPaint(m_ic.getMessage("DAYS_WITH_READINGS_6_7"), Color.GREEN);
        plot.setSectionPaint(m_ic.getMessage("DAYS_WITH_READINGS_MORE_7"), Color.MAGENTA);
        
    }

    /**
     * Create Chart
     */
    public void createChart()
    {
        chart = ChartFactory.createPieChart3D(null, (DefaultPieDataset)dataset, true, true, false);
    }


    /**
     * Create Chart Panel
     */
    public void createChartPanel()
    {
        chart_panel = new ChartPanel(this.chart, false, true, true, false, true);
    }
    
}
