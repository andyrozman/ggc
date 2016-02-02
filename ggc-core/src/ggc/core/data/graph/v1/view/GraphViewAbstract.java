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
 * Created by andy on 25.01.16.
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
     * Get Help Id
     *
     * @return Help id
     */
    public String getHelpId()
    {
        return this.graphType.getHelpId();
    }


    /**
     * Get Title (used by GraphViewer)
     *
     * @return title as string
     */
    @Override
    public String getTitle()
    {
        return i18nControl.getMessage(this.graphType.getTitle());
    }


    /**
     * Get Viewer Dialog Bounds (used by GraphViewer)
     *
     * @return Rectangle object
     */
    @Override
    public Rectangle getViewerDialogBounds()
    {
        return initialSize != null ? new Rectangle(0, 0, initialSize.width, initialSize.height)
                : new Rectangle(100, 100, 750, 550);
    }


    /**
     * Set Controller Data (Processor)
     *
     * @see com.atech.graphics.graphs.AbstractGraphViewAndProcessor#setControllerData(java.lang.Object)
     */
    @Override
    public void setControllerData(Object data)
    {
        plotData = (PlotSelectorData) data;
    }


    /**
     * Get Controler Interface instance
     *
     * @return GraphViewControlerInterface instance or null
     */
    @Override
    public GraphViewControlerInterface getControler()
    {
        return this.controler;
    }


    /**
     * Create Chart Panel
     */
    @Override
    public void createChartPanel()
    {
        this.chart_panel = new ChartPanel(this.getChart(), false, true, true, false, true);
        this.chart_panel.setDomainZoomable(true);
        this.chart_panel.setRangeZoomable(true);
    }

}
