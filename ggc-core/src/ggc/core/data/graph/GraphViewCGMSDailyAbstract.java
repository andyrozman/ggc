package ggc.core.data.graph;

import java.awt.*;
import java.util.*;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.jfree.data.xy.XYSeries;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.atech.graphics.graphs.AbstractGraphViewAndProcessor;
import com.atech.plugin.PlugInClient;
import com.atech.utils.ATDataAccessAbstract;

import ggc.core.plugins.GGCPluginType;
import ggc.core.util.DataAccess;

/**
 * Created by andy on 12.10.15.
 */
public abstract class GraphViewCGMSDailyAbstract extends AbstractGraphViewAndProcessor
{

    private static final Logger LOG = LoggerFactory.getLogger(GraphViewCGMSDailyAbstract.class);

    protected GregorianCalendar currentCalendar;
    protected boolean useCGMSData = false;
    protected XYSeries CGMSSeries;
    protected DataAccess dataAccessCore = DataAccess.getInstance();
    protected GGCGraphUtil graphUtil = GGCGraphUtil.getInstance(dataAccessCore);


    /**
     * Constructor
     *
     * @param da
     */
    public GraphViewCGMSDailyAbstract(ATDataAccessAbstract da)
    {
        super(da);
    }


    public void loadCGMSData()
    {
        useCGMSData = dataAccessCore.getConfigurationManagerWrapper().getUseCGMSDataInPenDailyGraph();

        if (useCGMSData)
        {
            useCGMSData = false;

            PlugInClient client = dataAccessCore.getPlugIn(GGCPluginType.CGMSToolPlugin);

            if (client.isActive())
            {
                LOG.debug("Loading CGMS data...");

                List<Object> dataFromPlugin = client.getDataFromPlugin(getCGMSParameters());

                if (CollectionUtils.isEmpty(dataFromPlugin))
                {
                    LOG.warn("There was problem getting data from Plugin. Data returned was empty.");
                    return;
                }

                try
                {
                    this.CGMSSeries = (XYSeries) dataFromPlugin.get(0);
                }
                catch (Exception ex)
                {
                    LOG.error("Error getting correct data from data. Ex.: " + ex.getMessage());
                }

                if (this.CGMSSeries == null)
                {
                    LOG.warn("There was problem getting data from Plugin. Problem getting XYSeries from data.");
                }
                else
                {
                    if (this.CGMSSeries.getItemCount() == 0)
                    {
                        LOG.debug("We got XYSeries from Plugin, but it has no items.");
                    }
                    else
                    {
                        useCGMSData = true;
                    }
                }
            }
            else
            {
                LOG.warn("Loading of CGMS data enabled, but PlugIn not installed. No data will be read.");
            }
        }

    }


    private Map<String, Object> getCGMSParameters()
    {
        Map<String, Object> parameters = new HashMap<String, Object>();
        parameters.put("dataType", "CGMSReadingsDaily4Graph");
        parameters.put("calendarDate", getCalendarRange(true));

        return parameters;
    }


    protected Color getColor(int color)
    {
        return dataAccessCore.getColor(color);
    }


    /**
     * Get Title (used by GraphViewer)
     *
     * @return title as string
     */
    @Override
    public String getTitle()
    {
        return m_ic.getMessage("DAILYGRAPHFRAME") + " [" + currentCalendar.get(Calendar.DAY_OF_MONTH) + "."
                + (currentCalendar.get(Calendar.MONTH) + 1) + "." + currentCalendar.get(Calendar.YEAR) + "]";
    }


    protected GregorianCalendar getCalendarRange(boolean isStart)
    {
        GregorianCalendar gc1 = (GregorianCalendar) currentCalendar.clone();

        if (isStart)
        {
            gc1.set(Calendar.HOUR_OF_DAY, 0);
            gc1.set(Calendar.MINUTE, 0);
            gc1.set(Calendar.SECOND, 0);
            gc1.set(Calendar.MILLISECOND, 0);
        }
        else
        {
            gc1.set(Calendar.HOUR_OF_DAY, 23);
            gc1.set(Calendar.MINUTE, 59);
            gc1.set(Calendar.SECOND, 59);
            gc1.set(Calendar.MILLISECOND, 59);
        }

        return gc1;
    }

}
