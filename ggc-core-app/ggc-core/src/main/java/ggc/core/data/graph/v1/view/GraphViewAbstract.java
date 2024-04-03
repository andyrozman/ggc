package ggc.core.data.graph.v1.view;

import java.awt.*;
import java.util.GregorianCalendar;

import org.jfree.chart.ChartPanel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.atech.graphics.graphs.AbstractGraphViewAndProcessor;
import com.atech.graphics.graphs.GraphUtil;
import com.atech.graphics.graphs.GraphViewControlerInterface;
import com.atech.i18n.I18nControlAbstract;
import com.atech.utils.ATDataAccessAbstract;

import ggc.core.data.GlucoValues;
import ggc.core.data.cfg.ConfigurationManagerWrapper;
import ggc.core.data.defs.GlucoseUnitType;
import ggc.core.data.graph.v1.GGCGraphType;
import ggc.core.data.graph.v1.GGCGraphUtil;
import ggc.core.data.graph.v1.data.PlotSelectorData;
import ggc.core.data.graph.v1.db.GraphV1DbRetriever;
import ggc.core.data.graph.v1.gui.GGCGraphViewControler;
import ggc.core.util.DataAccess;
import ggc.core.util.GGCProperties;

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
 *  Filename:     GraphViewAbstract
 *  Description:  GraphView Abstract for GGC graphs
 *
 *  Author: andyrozman {andy@atech-software.com}
 */

public abstract class GraphViewAbstract extends AbstractGraphViewAndProcessor
{

    private static final Logger LOG = LoggerFactory.getLogger(GraphViewAbstract.class);

    protected DataAccess dataAccesLocal = null;
    protected I18nControlAbstract i18nControl = null;

    protected GlucoseUnitType glucoseUnitType;
    protected ConfigurationManagerWrapper configurationManagerWrapper;
    protected GGCProperties properties;

    protected GGCGraphViewControler controler = null;
    protected PlotSelectorData plotData = null;
    protected GregorianCalendar calendarFrom, calendarTo;
    protected GlucoValues glucoValues;
    protected GraphUtil graphUtil;
    protected Dimension initialSize;
    protected GGCGraphType graphType;
    protected GraphV1DbRetriever dbRetriever;


    public GraphViewAbstract(DataAccess dataAccess, GGCGraphType graphType, GraphV1DbRetriever dbRetriever,
            Dimension initialSize)
    {
        super(dataAccess);

        dataAccesLocal = dataAccess;
        i18nControl = dataAccesLocal.getI18nControlInstance();
        this.configurationManagerWrapper = dataAccesLocal.getConfigurationManagerWrapper();
        this.glucoseUnitType = this.configurationManagerWrapper.getGlucoseUnit();
        this.properties = dataAccesLocal.getSettings();

        plotData = new PlotSelectorData();
        graphUtil = GGCGraphUtil.getInstance(dataAccesLocal);

        this.controler = new GGCGraphViewControler(this, graphType);
        this.initialSize = initialSize;
        this.graphType = graphType;
        this.dbRetriever = dbRetriever;
    }


    public void setInitialSize(Dimension initialSize)
    {
        this.initialSize = initialSize;
    }


    /**
     * Load Data
     */
    public void loadData()
    {

        boolean changed = false;

        if (calendarFrom == null || !m_da.compareGregorianCalendars(ATDataAccessAbstract.GC_COMPARE_DAY, calendarFrom,
            this.plotData.getDateRangeData().getRangeFrom()))
        {
            calendarFrom = this.plotData.getDateRangeData().getRangeFrom();
            changed = true;
        }

        if (calendarTo == null || !m_da.compareGregorianCalendars(ATDataAccessAbstract.GC_COMPARE_DAY, calendarTo,
            this.plotData.getDateRangeData().getRangeTo()))
        {
            calendarTo = this.plotData.getDateRangeData().getRangeTo();
            changed = true;
        }

        if (changed)
        {
            LOG.debug("Reload data [graphType={}, from={}, to={}]", graphType.name(),
                dataAccesLocal.getGregorianCalendarAsString(calendarFrom),
                dataAccesLocal.getGregorianCalendarAsString(calendarTo));

            this.glucoValues = dbRetriever.getGlucoValues(calendarFrom, calendarTo);
        }
    }


    /**
     * {@inheritDoc}
     */
    public String getHelpId()
    {
        return this.graphType.getHelpId();
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public String getTitle()
    {
        return i18nControl.getMessage(this.graphType.getTitle());
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public Rectangle getViewerDialogBounds()
    {
        return initialSize != null ? new Rectangle(0, 0, initialSize.width, initialSize.height)
                : new Rectangle(100, 100, 750, 550);
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public void setControllerData(Object data)
    {
        plotData = (PlotSelectorData) data;
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public GraphViewControlerInterface getControler()
    {
        return this.controler;
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public void createChartPanel()
    {
        this.chart_panel = new ChartPanel(this.getChart(), false, true, true, false, true);
        this.chart_panel.setDomainZoomable(true);
        this.chart_panel.setRangeZoomable(true);
    }

}
