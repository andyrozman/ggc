package main.java.ggc.pump.data.graph;

import java.awt.*;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.XYItemRenderer;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.data.general.AbstractDataset;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;

import com.atech.graphics.graphs.AbstractGraphViewAndProcessor;
import com.atech.utils.data.ATechDate;

import ggc.core.data.graph.v1.GGCGraphUtil;
import main.java.ggc.pump.data.profile.ProfileSubEntry;
import main.java.ggc.pump.gui.profile.ProfileEditor;
import main.java.ggc.pump.util.DataAccessPump;

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
 *  Filename:     GraphViewProfileEditor
 *  Description:  Graph for Profile Editor
 *
 *  Author: andyrozman {andy@atech-software.com}
 */

public class GraphViewProfileEditor extends AbstractGraphViewAndProcessor // implements
// GraphViewInterface,
// GraphViewDataProcessorInterface
{

    // GregorianCalendar currentCalendar;
    XYSeriesCollection dataset = new XYSeriesCollection();

    // NumberAxis BGAxis;
    // DateAxis dateAxis;

    DefaultCategoryDataset cat_ds = new DefaultCategoryDataset();

    DataAccessPump da_local = DataAccessPump.getInstance();
    GGCGraphUtil graph_util = GGCGraphUtil.getInstance(da_local);

    ArrayList<ProfileSubEntry> profiles_entries;
    ProfileEditor prof_editor = null;


    /**
     * Constructor
     * @param pe
     */
    public GraphViewProfileEditor(ProfileEditor pe)
    {
        super(DataAccessPump.getInstance());
        this.prof_editor = pe;
    }


    /**
     * Get Help Id
     *
     * @return
     */
    public String getHelpId()
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
        return new Rectangle(100, 100, 500, 400);
    }


    /**
     * Load Data
     */
    public void loadData()
    {
        if (this.profiles_entries == null)
        {
            this.profiles_entries = prof_editor.getProfileEntriesAL();
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
     * Preprocess Data
     */
    public void preprocessData()
    {

        if (this.profiles_entries == null)
            return;

        dataset.removeAllSeries();

        XYSeries xy_ser = new XYSeries("", true, true); // , Hour.class);

        for (int i = 0; i < this.profiles_entries.size(); i++)
        {
            ProfileSubEntry pse = this.profiles_entries.get(i);

            for (int k = pse.timeStart; k <= pse.timeEnd; k++)
            {

                int h = k / 100;
                int m = k - h * 100;

                int tm = h * 100;

                if (m == 60)
                {
                    k = (h + 1) * 100;
                    tm = k;
                }
                else
                {
                    tm += m;
                }

                long time = getTimeMs(tm);

                if (m > 4 && m < 26 || m > 34 && m < 56)
                {
                    xy_ser.add(time, pse.amount);
                }
            }

        }

        dataset.addSeries(xy_ser);

        int last = 0;

        for (int k = 0; k < 2400; k += 30)
        {

            int h = k / 100;
            int m = k - h * 100;

            int tm = h * 100;

            // System.out.println("hh:mm " + h + ":" + m);

            if (m == 60)
            {
                k = (h + 1) * 100;
                tm = k;
            }
            else
            {
                tm += m;
            }

            // System.out.println("Time: " + tm);

            for (int j1 = last; j1 < this.profiles_entries.size(); j1++)
            {
                ProfileSubEntry pse = this.profiles_entries.get(j1);

                if (tm >= pse.timeStart && tm <= pse.timeEnd)
                {
                    last = j1;

                    this.cat_ds.addValue(pse.amount, "1", "" + tm);
                    break;
                }

            }
        }

    }

    GregorianCalendar gcx = new GregorianCalendar();


    /**
     * Get Time in Ms
     *
     * @param time
     * @return
     */
    public long getTimeMs(int time)
    {
        ATechDate atd = new ATechDate(ATechDate.FORMAT_TIME_ONLY_MIN, time);

        if (atd.minute == 99)
        {
            atd.minute = 59;
        }

        gcx.set(Calendar.HOUR_OF_DAY, atd.hourOfDay);
        gcx.set(Calendar.MINUTE, atd.minute);

        return gcx.getTimeInMillis();
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
     * Set Plot
     *
     * @param chart JFreeChart instance
     */
    public void setPlot(JFreeChart chart)
    {

        XYPlot plot = chart.getXYPlot();

        XYItemRenderer renderer = plot.getRenderer();
        renderer.setSeriesPaint(0, Color.blue);

    }


    /**
     * Refresh Data
     */
    public void refreshData()
    {
        this.loadData();

        this.preprocessData();
        this.setPlot(chart);
    }


    /**
     * Create Chart
     */
    @Override
    public void createChart()
    {
        chart = ChartFactory.createXYBarChart(null, m_ic.getMessage("TIME_AXIS_BASAL"), true,
            m_ic.getMessage("VALUE_AXIS_BASAL"), dataset, PlotOrientation.VERTICAL, false, false, false);
        this.setPlot(chart);
    }


    /**
     * Create Chart Panel
     */
    @Override
    public void createChartPanel()
    {
        this.chart_panel = new ChartPanel(getChart(), true, true, true, true, false);
    }

}
