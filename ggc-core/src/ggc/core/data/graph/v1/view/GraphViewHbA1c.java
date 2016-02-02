package ggc.core.data.graph.v1.view;

import java.awt.*;
import java.util.Calendar;
import java.util.GregorianCalendar;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PiePlot;
import org.jfree.data.general.AbstractDataset;
import org.jfree.data.general.DefaultPieDataset;
import org.jfree.util.Rotation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.atech.graphics.graphs.AbstractGraphViewAndProcessor;
import com.atech.graphics.graphs.GraphViewControlerInterface;

import ggc.core.data.HbA1cValues;
import ggc.core.data.graph.v1.GGCGraphType;
import ggc.core.data.graph.v1.db.GraphV1DbRetriever;
import ggc.core.util.DataAccess;

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
public class GraphViewHbA1c extends AbstractGraphViewAndProcessor
{

    private static final Logger LOG = LoggerFactory.getLogger(GraphViewHbA1c.class);

    private HbA1cValues hbValues;
    private DefaultPieDataset dataset = new DefaultPieDataset();
    private GregorianCalendar lastCalendar;
    private GregorianCalendar currentCalendar;
    protected GraphV1DbRetriever dbRetriever;


    /**
     * Constructor
     */
    public GraphViewHbA1c(GraphV1DbRetriever dbRetriever)
    {
        super(DataAccess.getInstance());

        this.dbRetriever = dbRetriever;
    }


    public void setCalendar(GregorianCalendar gc)
    {
        this.currentCalendar = gc;
    }


    /**
     * Get Data Object (HbA1cValues)
     * @return
     */
    public HbA1cValues getDataObject()
    {
        loadData();
        preprocessData();
        return hbValues;
    }


    /**
     * Get Controler Interface instance
     * 
     * @return GraphViewControlerInterface instance or null
     */
    @Override
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
        return "GGC_BG_HbA1c";
    }


    /**
     * Get Title (used by GraphViewer)
     * 
     * @return title as string 
     */
    @Override
    public String getTitle()
    {
        return null;
    }


    /**
     * Get Viewer Dialog Bounds (used by GraphViewer)
     * 
     * @return Rectangle object
     */
    @Override
    public Rectangle getViewerDialogBounds()
    {
        return null;
    }


    /**
     * Load Data
     */
    public void loadData()
    {
        if (dateHasChanged())
        {
            LOG.debug("Reload data [graphType={}, from={-3 monhs}, to={}]", GGCGraphType.HbA1c.name(),
                m_da.getGregorianCalendarAsString(currentCalendar));

            hbValues = this.dbRetriever.getHba1cValues(this.currentCalendar);
            this.lastCalendar = (GregorianCalendar) this.currentCalendar.clone();
        }
    }


    private boolean dateHasChanged()
    {
        if (lastCalendar == null)
        {
            return true;
        }
        else
        {
            return !((lastCalendar.get(Calendar.YEAR) == this.currentCalendar.get(Calendar.YEAR))
                    && (lastCalendar.get(Calendar.DAY_OF_YEAR) == this.currentCalendar.get(Calendar.DAY_OF_YEAR)));
        }
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
     * Preprocess Data, choose which data to show in the plot.
     */
    public void preprocessData()
    {
        dataset.clear();

        LOG.debug("Preprocess Data [{}]", this.hbValues);

        dataset.insertValue(0, m_ic.getMessage("DAYS_WITH_READINGS_0_1"), hbValues.getPercentOfDaysInClass(0));
        dataset.insertValue(1, m_ic.getMessage("DAYS_WITH_READINGS_2_3"), hbValues.getPercentOfDaysInClass(1));
        dataset.insertValue(2, m_ic.getMessage("DAYS_WITH_READINGS_4_5"), hbValues.getPercentOfDaysInClass(2));
        dataset.insertValue(3, m_ic.getMessage("DAYS_WITH_READINGS_6_7"), hbValues.getPercentOfDaysInClass(3));
        dataset.insertValue(4, m_ic.getMessage("DAYS_WITH_READINGS_MORE_7"), hbValues.getPercentOfDaysInClass(4));

        setPlot(chart);
    }


    /**
     * Set Plot Properties
     * 
     * @param chart JFreeChart instance
     */
    public void setPlot(JFreeChart chart)
    {
        if (chart == null)
            return;

        PiePlot plot = (PiePlot) chart.getPlot();

        plot.setBackgroundPaint(Color.WHITE); // backgroundColor);
        plot.setCircular(true);
        plot.setForegroundAlpha(0.7f);
        plot.setInteriorGap(0);
        plot.setStartAngle(45);
        Rotation direction = Rotation.ANTICLOCKWISE;
        plot.setDirection(direction);
        plot.setSectionPaint(m_ic.getMessage("DAYS_WITH_READINGS_0_1"), Color.RED);
        plot.setSectionPaint(m_ic.getMessage("DAYS_WITH_READINGS_2_3"), Color.BLUE);
        plot.setSectionPaint(m_ic.getMessage("DAYS_WITH_READINGS_4_5"), Color.YELLOW);
        plot.setSectionPaint(m_ic.getMessage("DAYS_WITH_READINGS_6_7"), Color.GREEN);
        plot.setSectionPaint(m_ic.getMessage("DAYS_WITH_READINGS_MORE_7"), Color.MAGENTA);

    }


    /**
     * Create Chart
     */
    @Override
    public void createChart()
    {
        chart = ChartFactory.createPieChart3D(null, dataset, true, true, false);
        setPlot(chart);

    }


    /**
     * Create Chart Panel
     */
    @Override
    public void createChartPanel()
    {
        chart_panel = new ChartPanel(this.chart, false, true, true, false, true);
    }

}
