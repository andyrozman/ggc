package ggc.core.plugins;

import java.awt.*;
import java.awt.event.ActionEvent;

import javax.swing.*;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.atech.graphics.components.StatusReporterInterface;
import com.atech.i18n.I18nControlAbstract;
import com.atech.plugin.PlugInClient;
import com.atech.plugin.PlugInServer;
import com.atech.utils.ATDataAccessLMAbstract;
import ggc.core.util.DataAccess;

/**
 * Created by andy on 22.02.15.
 */
public abstract class GGCPluginClient extends PlugInClient
{

    private static final Logger LOG = LoggerFactory.getLogger(GGCPluginClient.class);


    /**
     * Constructor
     *
     * @param parent
     * @param ic
     */
    public GGCPluginClient(Component parent, I18nControlAbstract ic)
    {
        super((JFrame) parent, ic);
    }


    /**
     * Constructor
     *
     * @param parent
     * @param da
     */
    public GGCPluginClient(Component parent, ATDataAccessLMAbstract da)
    {
        super((JFrame) parent, da);
    }


    /**
     * Constructor
     */
    public GGCPluginClient()
    {
        super();
    }


    /**
     * Check If Installed
     */
    @Override
    public void checkIfInstalled()
    {
        try
        {
            Class<?> c = Class.forName(getServerClassName());

            this.m_server = (PlugInServer) c.newInstance();

            this.m_server.init(this.parent, DataAccess.getInstance().getI18nControlInstance().getSelectedLanguage(),
                DataAccess.getInstance(), this, DataAccess.getInstance().getDb());

            installed = true;
        }
        catch (ClassNotFoundException ex)
        {
            LOG.debug(getServerShortName() + ":: Plugin Class could not be found.");
            this.installed = false;
        }
        catch (InstantiationException ex)
        {
            LOG.debug(getServerShortName() + ":: Plugin could not be found and/or loaded.");
            LOG.error(ex.getMessage(), ex);
            this.installed = false;
        }
        catch (IllegalAccessException ex)
        {
            LOG.debug(getServerShortName() + ":: Plugin could not be found and/or loaded.");
            LOG.error(ex.getMessage(), ex);
            this.installed = false;
        }
        catch (Exception ex)
        {
            this.installed = false;
            LOG.error(getServerShortName() + ":: Exception on loading. Ex.: " + ex, ex);
        }
    }


    /**
     * Init Plugin
     */
    @Override
    public void initPlugin()
    {
    }


    /**
     * actionPerformed
     */
    @Override
    public void actionPerformed(ActionEvent e)
    {
        this.m_server.actionPerformed(e);
    }


    /**
     * Get Short Status
     *
     * @return
     */
    @Override
    public String getShortStatus()
    {
        if (this.m_server != null)
            return String.format(ic.getMessage("STATUS_INSTALLED"), this.m_server.getVersion());
        else
            return ic.getMessage("STATUS_NOT_INSTALLED");
    }


    protected void refreshPanels(int mask)
    {
        DataAccess.getInstance().setChangeOnEventSource(DataAccess.OBSERVABLE_PANELS, mask);
    }


    /**
     * Get When Will Be Implemented
     *
     * @return
     */
    @Override
    public String getWhenWillBeImplemented()
    {
        return null;
    }


    /**
     * Set Return Data (for getting data from plugin - async)
     *
     * @param return_data
     * @param stat_rep_int
     */
    @Override
    public void setReturnData(Object return_data, StatusReporterInterface stat_rep_int)
    {
    }


    public abstract String getServerClassName();


    public abstract String getServerShortName();

}
