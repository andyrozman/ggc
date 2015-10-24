package ggc.meter.plugin;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.*;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.atech.db.hibernate.transfer.BackupRestoreCollection;
import com.atech.utils.ATDataAccessLMAbstract;
import com.atech.utils.ATSwingUtils;

import ggc.core.util.DataAccess;
import ggc.meter.defs.MeterPluginDefinition;
import ggc.meter.util.DataAccessMeter;
import ggc.plugin.DevicePlugInServer;
import ggc.plugin.cfg.DeviceConfigEntry;
import ggc.plugin.cfg.DeviceConfigurationDialog;
import ggc.plugin.data.DeviceDataHandler;
import ggc.plugin.device.DownloadSupportType;
import ggc.plugin.gui.AboutBaseDialog;
import ggc.plugin.gui.DeviceInstructionsDialog;
import ggc.plugin.list.BaseListDialog;

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

public class MeterPlugInServer extends DevicePlugInServer implements ActionListener
{

    DataAccessMeter da_local; // = DataAccessMeter.getInstance();
    private static final Logger LOG = LoggerFactory.getLogger(MeterPlugInServer.class);

    /**
     * Return Object: Selected Device with parameters
     */
    public static final int RETURN_OBJECT_DEVICE_WITH_PARAMS = 1;

    private JMenuItem[] menus = new JMenuItem[2];


    /**
    * Constructor
    */
    public MeterPlugInServer()
    {
        super();
    }


    /**
     * Constructor
     * 
     * @param cont
     * @param selected_lang
     * @param da
     */
    public MeterPlugInServer(Container cont, String selected_lang, ATDataAccessLMAbstract da)
    {
        super(cont, selected_lang, da);

        da_local = DataAccessMeter.createInstance(getPluginDefinition(da));
        da_local.addComponent(cont);
    }


    private MeterPluginDefinition getPluginDefinition(ATDataAccessLMAbstract da)
    {
        return new MeterPluginDefinition(da.getLanguageManager());
    }


    /**
     * Get Name of plugin
     * 
     * @return
     */
    @Override
    public String getName()
    {
        return ic.getMessage("METERS_PLUGIN");
    }


    /**
     * Get Version of plugin
     * 
     * @return
     */
    @Override
    public String getVersion()
    {
        return da_local.getPlugInVersion();
    }


    /**
     * Get Information When will it be implemented
     * 
     * @return
     */
    @Override
    public String getWhenWillBeImplemented()
    {
        return "0.4";
    }


    /**
     * Init PlugIn which needs to be implemented [PlugIn Framework v1/v2] 
     */
    @Override
    public void initPlugIn()
    {
        ic = dataAccess.getI18nControlInstance();

        if (da_local == null)
        {
            try
            {
                da_local = DataAccessMeter.createInstance(getPluginDefinition((ATDataAccessLMAbstract) dataAccess));
            }
            catch (Exception ex)
            {
                LOG.error("InitPlugin DaLocal Ex.: " + ex, ex);
            }
        }

        this.initPlugInServer((DataAccess) dataAccess, da_local);

        this.backup_restore_enabled = false;
    }


    /**
     * Get Return Object [PlugIn Framework v1/v2]
     * 
     * @param ret_obj_id
     * @return
     */
    @Override
    public Object getReturnObject(int ret_obj_id)
    {
        if (ret_obj_id == MeterPlugInServer.RETURN_OBJECT_DEVICE_WITH_PARAMS)
        {
            DataAccessMeter da = DataAccessMeter.getInstance();

            DeviceConfigEntry de = da.getDeviceConfiguration().getSelectedDeviceInstance();

            if (de == null)
                return da.getI18nControlInstance().getMessage("NO_DEVICE_SELECTED");
            else
            {

                if (de.device_device.equals(da.getI18nControlInstance().getMessage("NO_DEVICE_SELECTED")))
                    return da.getI18nControlInstance().getMessage("NO_DEVICE_SELECTED");
                else
                {
                    if (dataAccess.isValueSet(de.communication_port))
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
     * Get Return Object
     * 
     * @param ret_obj_id Return Object Id
     * @param parameters Parameters
     * @return
     */
    @Override
    public Object getReturnObject(int ret_obj_id, Object[] parameters)
    {
        return null;
    }


    /**
     * Get Backup Objects (if available) [PlugIn Framework v2]
     * 
     * @return
     */
    @Override
    public BackupRestoreCollection getBackupObjects()
    {
        // this plugin has no backup objects
        return null;
    }


    /**
     * Get PlugIn Main Menu [PlugIn Framework v2]
     * 
     * This is new way to handle everything, previously we used to pass ActionListener items through
     * plugin framework, but in new way, we will use this one. We just give main application menu,
     * which contains all items accessible through menus.
     *  
     * @return
     */
    @Override
    public JMenu getPlugInMainMenu()
    {

        JMenu menu_meter = ATSwingUtils.createMenu("MN_METERS", null, this.ic_local);

        JMenuItem mi = ATSwingUtils.createMenuItem(menu_meter, "MN_METERS_READ", "MN_METERS_READ_DESC", "meters_read",
            this, null, this.ic_local, DataAccessMeter.getInstance(), parent);

        // if (DownloadSupportType.isOptionSet(da_local.getDownloadStatus(),
        // DownloadSupportType.DownloadData))

        mi.setEnabled(DownloadSupportType.isOptionSet(da_local.getDownloadStatus(), DownloadSupportType.DownloadData));

        menus[0] = mi;

        mi = ATSwingUtils.createMenuItem(menu_meter, "MN_METERS_READ_FILE", "MN_METERS_READ_FILE_DESC",
            "meters_read_file", this, null, this.ic_local, DataAccessMeter.getInstance(), parent);

        mi.setEnabled(
            DownloadSupportType.isOptionSet(da_local.getDownloadStatus(), DownloadSupportType.DownloadDataFile));

        menus[1] = mi;

        menu_meter.addSeparator();

        ATSwingUtils.createMenuItem(menu_meter, "MN_METERS_LIST", "MN_METERS_LIST_DESC", "meters_list", this, null,
            this.ic_local, DataAccessMeter.getInstance(), parent);

        menu_meter.addSeparator();

        ATSwingUtils.createMenuItem(menu_meter, "MN_METERS_CONFIG", "MN_METERS_CONFIG_DESC", "meters_config", this,
            null, this.ic_local, DataAccessMeter.getInstance(), parent);

        menu_meter.addSeparator();

        ATSwingUtils.createMenuItem(menu_meter, "MN_METERS_ABOUT", "MN_METERS_ABOUT_DESC", "meters_about", this, null,
            this.ic_local, DataAccessMeter.getInstance(), parent);

        // System.out.println("MenuMeter Plugin");

        return menu_meter;
    }


    public void refreshMenusAfterConfig()
    {
        menus[0].setEnabled(
            DownloadSupportType.isOptionSet(da_local.getDownloadStatus(), DownloadSupportType.DownloadData));
        menus[1].setEnabled(
            DownloadSupportType.isOptionSet(da_local.getDownloadStatus(), DownloadSupportType.DownloadDataFile));
    }


    /**
     * Get PlugIn Print Menus [PlugIn Framework v2] 
     * 
     * Since printing is also PlugIn specific we need to add Printing jobs to application.
     *  
     * @return
     */
    @Override
    public JMenu[] getPlugInPrintMenus()
    {
        // there are no print menus for this plugin
        return null;
    }


    /**
     * Action Performed 
     * 
     * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
     */
    @Override
    public void actionPerformed(ActionEvent ae)
    {
        String command = ae.getActionCommand();

        if (command.equals("meters_read"))
        {
            new DeviceInstructionsDialog(this.parent, da_local, this, DeviceDataHandler.TRANSFER_READ_DATA);
            this.client.executeReturnAction(MeterPlugInServer.RETURN_ACTION_READ_DATA);
        }
        else if (command.equals("meters_list"))
        {
            new BaseListDialog((JFrame) parent, da_local);
        }
        else if (command.equals("meters_about"))
        {
            new AboutBaseDialog((JFrame) parent, da_local);
        }
        else if (command.equals("meters_read_file"))
        {
            // System.out.println("Meters read file");
            new DeviceInstructionsDialog(this.parent, DataAccessMeter.getInstance(), this,
                    DeviceDataHandler.TRANSFER_READ_DATA_FILE);
            this.client.executeReturnAction(MeterPlugInServer.RETURN_ACTION_READ_DATA);
        }
        else if (command.equals("meters_config"))
        {
            new DeviceConfigurationDialog((JFrame) parent, da_local);
            refreshMenusAfterConfig();
            this.client.executeReturnAction(MeterPlugInServer.RETURN_ACTION_CONFIG);
        }
        else
        {
            System.out.println("MeterPlugInServer::Unknown Command: " + command);
        }

    }

}
