package ggc.plugin;

import java.awt.*;
import java.awt.event.ActionListener;

import javax.swing.*;

import com.atech.db.hibernate.transfer.BackupRestoreCollection;
import com.atech.i18n.I18nControlAbstract;
import com.atech.plugin.PlugInServer;
import com.atech.utils.ATDataAccessAbstract;
import com.atech.utils.ATDataAccessLMAbstract;

import ggc.core.data.defs.ReturnActionType;
import ggc.core.util.DataAccess;
import ggc.plugin.cfg.DeviceConfigurationDialog;
import ggc.plugin.data.DeviceDataHandler;
import ggc.plugin.graph.PluginGraphDefinition;
import ggc.plugin.gui.AboutBaseDialog;
import ggc.plugin.gui.DeviceInstructionsDialog;
import ggc.plugin.list.BaseListDialog;
import ggc.plugin.report.PluginReportDefinition;
import ggc.plugin.util.DataAccessPlugInBase;

/**
 *  Application:   GGC - GNU Gluco Control
 *  Plug-in:       Meter Tool (support for Meter devices)
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
 *  Filename:     MeterPlugInServer
 *  Description:  This is server side of plugin architecture
 * 
 *  Author: Andy {andy@atech-software.com}
 */

public abstract class DevicePlugInServer extends PlugInServer implements ActionListener
{

    protected I18nControlAbstract i18nControlLocal = null;

    /**
     * This is action that needs to be done, after read data.
     */
    public static final int RETURN_ACTION_READ_DATA = 1;

    /**
     * This is action that needs to be done, after config
     */
    public static final int RETURN_ACTION_CONFIG = 2;

    protected BackupRestoreCollection backupRestoreCollection;
    protected DataAccessPlugInBase dataAccessPlugInBase;
    protected String pluginPrefix;


    /**
     * {@inheritDoc}
     */
    public DevicePlugInServer()
    {
        super();
    }


    /**
     * {@inheritDoc}
     */
    public DevicePlugInServer(Container cont, String selected_lang, ATDataAccessAbstract da)
    {
        super(cont, selected_lang, da);
    }


    /**
     * {@inheritDoc}
     */
    public DevicePlugInServer(Container cont, ATDataAccessLMAbstract da)
    {
        super(cont, da);
    }


    /**
     * {@inheritDoc}
     */
    public void initPlugInServer(DataAccess da_ggc_core, DataAccessPlugInBase da_plugin)
    {
        da_plugin.loadManager();

        i18nControlLocal = da_plugin.getI18nControlInstance();
        da_plugin.setParentI18nControlInstance(i18nControl);

        da_plugin.loadManager();

        // System.out.println(da_local.getI18nControlInstance().toString());
        da_plugin.setMainParent(da_ggc_core.getMainParent());

        da_plugin.addComponent(this.parent);
        da_plugin.setHelpContext(this.dataAccess.getHelpContext());
        da_plugin.setPlugInServerInstance(this);
        da_plugin.createDb(dataAccess.getHibernateDb());
        da_plugin.initAllObjects();
        da_plugin.loadSpecialParameters();
        da_plugin.setCurrentUserId(da_ggc_core.current_user_id);
        da_plugin.setConfigurationManager(((DataAccess) dataAccess).getConfigurationManager());

        this.backup_restore_enabled = (getBackupObjects() != null);

        da_ggc_core.loadSpecialParameters();
        // System.out.println("PumpServer: " +
        // dataAccess.getSpecialParameters().get("BG"));

        da_plugin.setGlucoseUnitType(((DataAccess) dataAccess).getGlucoseUnitType());
        da_plugin.setGraphConfigProperties(da_ggc_core.getGraphConfigProperties());
        da_plugin.setDeveloperMode(da_ggc_core.getDeveloperMode());

        this.pluginPrefix = da_plugin.getPluginActionsPrefix();
    }


    // plugin_read_data, plugin_read_config, plugin_list, plugin_config,
    // plugin_read_data_file, plugin_read_config_file
    // plugin_about

    public boolean executeBasePluginAction(String command, DataAccessPlugInBase dataAccessPlugIn)
    {
        if (command.equals("plugin_read_data"))
        {
            new DeviceInstructionsDialog(this.parent, dataAccessPlugIn, this, DeviceDataHandler.TRANSFER_READ_DATA);
            this.client.executeReturnAction(ReturnActionType.ReadData);
        }
        else if (command.equals("plugin_read_config"))
        {
            new DeviceInstructionsDialog(this.parent, dataAccessPlugIn, this,
                    DeviceDataHandler.TRANSFER_READ_CONFIGURATION);
        }
        else if (command.equals("plugin_list"))
        {
            new BaseListDialog((JFrame) this.parent, dataAccessPlugIn);
        }
        else if (command.equals("plugin_config"))
        {
            new DeviceConfigurationDialog((JFrame) this.parent, dataAccessPlugIn);
            refreshMenusAfterConfig();
            this.client.executeReturnAction(ReturnActionType.ChangeConfig);
        }
        else if (command.equals("plugin_read_data_file"))
        {
            new DeviceInstructionsDialog(this.parent, dataAccessPlugIn, this,
                    DeviceDataHandler.TRANSFER_READ_DATA_FILE);
            this.client.executeReturnAction(ReturnActionType.ReadData);
        }
        else if (command.equals("plugin_read_config_file"))
        {
            new DeviceInstructionsDialog(this.parent, dataAccessPlugIn, this,
                    DeviceDataHandler.TRANSFER_READ_CONFIGURATION_FILE);
            this.client.executeReturnAction(ReturnActionType.ReadData);
        }
        else if (command.equals("plugin_about"))
        {
            new AboutBaseDialog((JFrame) this.parent, dataAccessPlugIn);
        }
        else if (command.startsWith(this.pluginPrefix + "report_"))
        {
            this.getPlugInDataAccess().getReportsDefinition().startPlugInReportMenuAction(command);
        }
        else if (command.startsWith(this.pluginPrefix + "graph_"))
        {
            this.getPlugInDataAccess().getGraphsDefinition().startPlugInGraphMenuAction(command);
        }
        else
        {
            return false;
        }

        return true;
    }


    public abstract void refreshMenusAfterConfig();


    /**
     * {@inheritDoc}
     */
    @Override
    public void executeCommand(int commandId, Object data)
    {
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public String getWhenWillBeImplemented()
    {
        return null;
    }


    /**
     * Get PlugIn DataAccess instance
     */
    public abstract DataAccessPlugInBase getPlugInDataAccess();


    /**
     * {@inheritDoc}
     */
    @Override
    public JMenu[] getPlugInGraphMenus()
    {
        PluginGraphDefinition pgd = this.getPlugInDataAccess().getPluginDefinition().getGraphsDefinition();

        if (pgd != null)
        {
            return pgd.getPlugInGraphMenus(this);
        }

        return null;
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public JMenu[] getPlugInReportMenus()
    {
        PluginReportDefinition prd = this.getPlugInDataAccess().getPluginDefinition().getReportsDefinition();

        if (prd != null)
        {
            return prd.getPlugInReportMenus(this);
        }

        return null;
    }

}
