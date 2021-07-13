package ggc.pump.plugin;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.GregorianCalendar;

import javax.swing.*;

import com.atech.db.hibernate.transfer.BackupRestoreCollection;
import com.atech.i18n.I18nControlAbstract;
import com.atech.misc.statistics.StatisticsCollection;
import com.atech.plugin.BackupRestorePlugin;
import com.atech.utils.ATDataAccessLMAbstract;
import com.atech.utils.ATSwingUtils;

import ggc.core.util.DataAccess;
import ggc.plugin.DevicePlugInServer;
import ggc.plugin.cfg.DeviceConfigEntry;
import ggc.plugin.data.DeviceValuesRange;
import ggc.plugin.device.DownloadSupportType;
import ggc.plugin.graph.PlugInGraphDialog;
import ggc.plugin.util.DataAccessPlugInBase;
import ggc.pump.data.PumpValuesEntry;
import ggc.pump.data.dto.BasalStatistics;
import ggc.pump.db.PumpData;
import ggc.pump.db.PumpDataExtended;
import ggc.pump.db.PumpProfile;
import ggc.pump.defs.PumpPluginDefinition;
import ggc.pump.gui.manual.PumpDataDialog;
import ggc.pump.gui.profile.ProfileSelector;
import ggc.pump.util.DataAccessPump;

/**
 *  Application:   GGC - GNU Gluco Control
 *  Plug-in:       Pump Tool (support for Pump devices)
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
 *  Filename:      PumpPlugInServer
 *  Description:   Plugin Server Instance
 * 
 *  Author: Andy {andy@atech-software.com}
 */

public class PumpPlugInServer extends DevicePlugInServer
{

    /**
     * This is action that needs to be done, after config
     */
    public static final int RETURN_ACTION_STATISTICS = 100;

    public static final int RETURN_ACTION_BASAL_STATISTICS = 101;

    /**
     * Return Object: Selected Device with parameters
     */
    public static final int RETURN_OBJECT_DEVICE_WITH_PARAMS = 1;

    private DataAccessPump dataAccessPump = null;
    private JMenuItem[] menus = new JMenuItem[4];


    public PumpPlugInServer()
    {
        super();
    }


    /**
     * Constructor
     * 
     * @param cont
     * @param da
     */
    public PumpPlugInServer(Container cont, ATDataAccessLMAbstract da)
    {
        super(cont, da);

        dataAccessPump = DataAccessPump.createInstance(getPluginDefinition(da));
        dataAccessPump.addComponent(cont);

        this.dataAccessPlugInBase = dataAccessPump;
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public String getName()
    {
        return i18nControl.getMessage("PUMP_PLUGIN");
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public DataAccessPlugInBase getPlugInDataAccess()
    {
        return this.dataAccessPump;
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public String getVersion()
    {
        return dataAccessPump.getPlugInVersion();
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public void initPlugIn()
    {
        i18nControl = dataAccess.getI18nControlInstance();

        if (dataAccessPump == null)
        {
            dataAccessPump = DataAccessPump.createInstance(getPluginDefinition((ATDataAccessLMAbstract) dataAccess));
        }

        this.initPlugInServer((DataAccess) dataAccess, dataAccessPump);
    }


    /**
     * {@inheritDoc}
     */
    private PumpPluginDefinition getPluginDefinition(ATDataAccessLMAbstract da)
    {
        return new PumpPluginDefinition(da.getLanguageManager());
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public Object getReturnObject(int ret_obj_id)
    {
        if (ret_obj_id == PumpPlugInServer.RETURN_OBJECT_DEVICE_WITH_PARAMS)
        {
            DataAccessPump da = DataAccessPump.getInstance();

            DeviceConfigEntry de = da.getDeviceConfiguration().getSelectedDeviceInstance();

            if (de == null)
                return da.getI18nControlInstance().getMessage("NO_DEVICE_SELECTED");
            else
            {

                if (de.device_device.equals(da.getI18nControlInstance().getMessage("NO_DEVICE_SELECTED")))
                    return da.getI18nControlInstance().getMessage("NO_DEVICE_SELECTED");
                else
                {
                    if (dataAccess.isValueSet(de.communication_port)
                            && !de.communication_port.equals(da.getI18nControlInstance().getMessage("NOT_SET")))
                        return String.format(da.getI18nControlInstance().getMessage("DEVICE_FULL_NAME_WITH_PORT"),
                            de.device_device + " [" + de.device_company + "]", de.communication_port);
                    else
                        return String.format(da.getI18nControlInstance().getMessage("DEVICE_FULL_NAME_WITHOUT_PORT"),
                            de.device_device + " [" + de.device_company + "]");
                }
            }

        }
        else
            return null;
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public Object getReturnObject(int ret_obj_id, Object[] parameters)
    {

        if (ret_obj_id == PumpPlugInServer.RETURN_ACTION_STATISTICS)
        {
            GregorianCalendar gc_from = (GregorianCalendar) parameters[0];
            GregorianCalendar gc_to = (GregorianCalendar) parameters[1];

            DataAccessPump da = DataAccessPump.getInstance();
            DeviceValuesRange dvre = da.getDb().getRangePumpValues(gc_from, gc_to);

            StatisticsCollection sc = new StatisticsCollection(da, new PumpValuesEntry());
            sc.processFullCollection(dvre.getAllEntries());

            return sc;
        }
        else if (ret_obj_id == PumpPlugInServer.RETURN_ACTION_BASAL_STATISTICS)
        {
            GregorianCalendar gc_from = (GregorianCalendar) parameters[0];
            GregorianCalendar gc_to = (GregorianCalendar) parameters[1];

            BasalStatistics basalStatistics = DataAccessPump.getInstance().getPumpBasalManager()
                    .getBasalStatistics(gc_from, gc_to);

            return basalStatistics;
        }
        else
            return null;

    }


    /**
     * {@inheritDoc}
     */
    @Override
    public BackupRestoreCollection getBackupObjects()
    {
        if (backupRestoreCollection == null)
        {
            I18nControlAbstract ic_pump = DataAccessPump.getInstance().getI18nControlInstance();

            backupRestoreCollection = new BackupRestoreCollection("PUMP_TOOL", ic_pump);
            backupRestoreCollection.addNodeChild(new PumpData(ic_pump));
            backupRestoreCollection.addNodeChild(new PumpDataExtended(ic_pump));
            backupRestoreCollection.addNodeChild(new PumpProfile(ic_pump));
        }

        return backupRestoreCollection;
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public JMenu getPlugInMainMenu()
    {
        DataAccessPump dataAccessPump = DataAccessPump.getInstance();

        JMenu menu_pump = ATSwingUtils.createMenu("MN_PUMPS", null, i18nControlLocal);

        JMenuItem menu = ATSwingUtils.createMenuItem(menu_pump, "MN_PUMPS_READ_DATA", "MN_PUMPS_READ_DATA_DESC",
            "plugin_read_data", this, null, i18nControlLocal, dataAccessPump, parent);

        menus[0] = menu;
        menus[0].setEnabled(
            DownloadSupportType.isOptionSet(dataAccessPump.getDownloadStatus(), DownloadSupportType.DownloadData));

        menu = ATSwingUtils.createMenuItem(menu_pump, "MN_PUMPS_READ_CONFIG", "MN_PUMPS_READ_CONFIG_DESC",
            "plugin_read_config", this, null, i18nControlLocal, dataAccessPump, parent);

        menus[1] = menu;
        menus[1].setEnabled(
            DownloadSupportType.isOptionSet(dataAccessPump.getDownloadStatus(), DownloadSupportType.DownloadConfig));

        menu = ATSwingUtils.createMenuItem(menu_pump, "MN_PUMPS_READ_FILE", "MN_PUMPS_READ_FILE_DESC",
            "plugin_read_data_file", this, null, i18nControlLocal, dataAccessPump, parent);

        menus[2] = menu;
        menus[2].setEnabled(
            DownloadSupportType.isOptionSet(dataAccessPump.getDownloadStatus(), DownloadSupportType.DownloadDataFile));

        menu = ATSwingUtils.createMenuItem(menu_pump, "MN_PUMPS_READ_CONFIG_FILE", "MN_PUMPS_READ_CONFIG_FILE_DESC",
            "plugin_read_config_file", this, null, i18nControlLocal, dataAccessPump, parent);

        menus[3] = menu;
        menus[3].setEnabled(DownloadSupportType.isOptionSet(dataAccessPump.getDownloadStatus(),
            DownloadSupportType.DownloadConfigFile));

        menu_pump.addSeparator();

        // this.createMenuItem(menux, "MN_DAILY", "MN_DAILY_DESC", "view_daily",
        // "calendar.png");
        // ATSwingUtils.createMenuItem(menu_pump, "MN_PUMPS_MANUAL_ENTRY",
        // "MN_PUMPS_MANUAL_ENTRY_DESC",
        // "pumps_manual_entry", this, null, i18nControlLocal, dataAccessPump, parent);

        ATSwingUtils.createMenuItem(menu_pump, "MN_PUMP_DAILY", "MN_PUMP_DAILY_DESC", "pumps_manual_entry", this,
            "calendar.png", i18nControlLocal, dataAccessPump, parent);

        ATSwingUtils.createMenuItem(menu_pump, "MN_PUMPS_ADDITIONAL_DATA", "MN_PUMPS_ADDITIONAL_DATA_DESC",
            "pumps_additional_data", this, null, i18nControlLocal, dataAccessPump, parent);

        ATSwingUtils.createMenuItem(menu_pump, "MN_PUMP_PROFILES", "MN_PUMP_PROFILES_DESC", "pumps_profile", this, null,
            i18nControlLocal, DataAccessPump.getInstance(), parent);

        menu_pump.addSeparator();

        ATSwingUtils.createMenuItem(menu_pump, "MN_PUMPS_LIST", "MN_PUMPS_LIST_DESC", "plugin_list", this, null,
            i18nControlLocal, DataAccessPump.getInstance(), parent);

        menu_pump.addSeparator();

        // TODO
        if (this.dataAccessPump.isDeveloperMode())
        {
            JMenu m = ATSwingUtils.createMenu("MN_PUMPS_GRAPHS", "MN_PUMPS_GRAPHS_DESC", menu_pump, i18nControlLocal);

            ATSwingUtils.createMenuItem(m, "MN_PUMPS_GRAPH_CUSTOM", "MN_PUMPS_GRAPH_CUSTOM_DESC", "pumps_graph1_custom",
                this, null, i18nControlLocal, dataAccessPump, parent);

            menu_pump.addSeparator();
        }

        ATSwingUtils.createMenuItem(menu_pump, "MN_PUMPS_CONFIG", "MN_PUMPS_CONFIG_DESC", "plugin_config", this, null,
            i18nControlLocal, dataAccessPump, parent);

        menu_pump.addSeparator();

        ATSwingUtils.createMenuItem(menu_pump, "MN_PUMPS_ABOUT", "MN_PUMPS_ABOUT_DESC", "plugin_about", this, null,
            i18nControlLocal, dataAccessPump, parent);

        refreshMenusAfterConfig();

        return menu_pump;
    }


    /**
     * {@inheritDoc}
     */
    public void refreshMenusAfterConfig()
    {
        menus[0].setEnabled(
            DownloadSupportType.isOptionSet(dataAccessPump.getDownloadStatus(), DownloadSupportType.DownloadData));
        menus[1].setEnabled(
            DownloadSupportType.isOptionSet(dataAccessPump.getDownloadStatus(), DownloadSupportType.DownloadConfig));
        menus[2].setEnabled(
            DownloadSupportType.isOptionSet(dataAccessPump.getDownloadStatus(), DownloadSupportType.DownloadDataFile));
        menus[3].setEnabled(DownloadSupportType.isOptionSet(dataAccessPump.getDownloadStatus(),
            DownloadSupportType.DownloadConfigFile));
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public void actionPerformed(ActionEvent ae)
    {
        String command = ae.getActionCommand();

        if (!executeBasePluginAction(command, DataAccessPump.getInstance()))
        {
            if (command.equals("pumps_manual_entry") || command.equals("pumps_additional_data"))
            {
                new PumpDataDialog(DataAccessPump.getInstance(), (JFrame) this.parent);
                this.client.executeReturnAction(PumpPlugInServer.RETURN_ACTION_READ_DATA);
            }
            else if (command.equals("pumps_profile"))
            {
                new ProfileSelector(DataAccessPump.getInstance(), this.parent);
            }
            else if (command.equals("pumps_graph1_custom"))
            {
                new PlugInGraphDialog(DataAccessPump.getInstance(), null);
            }
            else if (command.startsWith("pump_graph_"))
            {
                dataAccessPump.getGraphsDefinition().startPlugInGraphMenuAction(command);
            }
            else
            {
                System.out.println("PumpPlugInServer::Unknown Command: " + command);
            }
        }

    }


    /**
     * Get Backup Restore Handler
     * 
     * @return
     */
    @Override
    public BackupRestorePlugin getBackupRestoreHandler()
    {
        return new BackupRestorePumpHandler();
    }

}
