package ggc.meter.plugin;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import javax.swing.*;

import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.atech.db.hibernate.transfer.BackupRestoreCollection;
import com.atech.utils.ATDataAccessLMAbstract;
import com.atech.utils.ATSwingUtils;

import ggc.core.data.defs.ReturnActionType;
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

@Slf4j
public class MeterPlugInServer extends DevicePlugInServer implements ActionListener {

    DataAccessMeter dataAccessMeter;

    /**
     * Return Object: Selected Device with parameters
     */
    public static final int RETURN_OBJECT_DEVICE_WITH_PARAMS = 1;

    private JMenuItem[] menus = new JMenuItem[2];


    /**
     * {@inheritDoc}
     */
    public MeterPlugInServer() {
        super();
    }


    /**
     * {@inheritDoc}
     */
    public MeterPlugInServer(Container cont, String selected_lang, ATDataAccessLMAbstract da) {
        super(cont, selected_lang, da);

        dataAccessMeter = DataAccessMeter.createInstance(getPluginDefinition(da));
        dataAccessMeter.addComponent(cont);
    }


    /**
     * {@inheritDoc}
     */
    private MeterPluginDefinition getPluginDefinition(ATDataAccessLMAbstract da) {
        return new MeterPluginDefinition(da.getLanguageManager());
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public String getName() {
        return i18nControl.getMessage("METERS_PLUGIN");
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public String getVersion() {
        return dataAccessMeter.getPlugInVersion();
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public DataAccessPlugInBase getPlugInDataAccess() {
        return this.dataAccessMeter;
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public void initPlugIn() {
        i18nControl = dataAccess.getI18nControlInstance();

        if (dataAccessMeter == null) {
            try {
                dataAccessMeter = DataAccessMeter
                        .createInstance(getPluginDefinition((ATDataAccessLMAbstract) dataAccess));
            } catch (Exception ex) {
                log.error("Error on Init Plugin Meter. Ex.: " + ex, ex);
            }
        }

        this.initPlugInServer((DataAccess) dataAccess, dataAccessMeter);

        this.backup_restore_enabled = false;
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public Object getReturnObject(int ret_obj_id) {
        if (ret_obj_id == MeterPlugInServer.RETURN_OBJECT_DEVICE_WITH_PARAMS) {
            DataAccessMeter da = DataAccessMeter.getInstance();

            DeviceConfigEntry de = da.getDeviceConfiguration().getSelectedDeviceInstance();

            if (de == null)
                return da.getI18nControlInstance().getMessage("NO_DEVICE_SELECTED");
            else {

                if (de.device_device.equals(da.getI18nControlInstance().getMessage("NO_DEVICE_SELECTED")))
                    return da.getI18nControlInstance().getMessage("NO_DEVICE_SELECTED");
                else {
                    if (dataAccess.isValueSet(de.communication_port))
                        return String.format(da.getI18nControlInstance().getMessage("DEVICE_FULL_NAME_WITH_PORT"),
                                de.device_device + " [" + de.device_company + "]", de.communication_port);
                    else
                        return String.format(da.getI18nControlInstance().getMessage("DEVICE_FULL_NAME_WITHOUT_PORT"),
                                de.device_device + " [" + de.device_company + "]");
                }
            }

        } else
            return null;
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public Object getReturnObject(int ret_obj_id, Object[] parameters) {
        return null;
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public BackupRestoreCollection getBackupObjects() {
        // this plugin has no backup objects
        return null;
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public JMenu getPlugInMainMenu() {

        JMenu menu_meter = ATSwingUtils.createMenu("MN_METERS", null, this.i18nControlLocal);

        JMenuItem mi = ATSwingUtils.createMenuItem(menu_meter, "MN_METERS_READ", "MN_METERS_READ_DESC", "meters_read",
                this, null, this.i18nControlLocal, DataAccessMeter.getInstance(), parent);

        // if
        // (DownloadSupportType.isOptionSet(dataAccessMeter.getDownloadStatus(),
        // DownloadSupportType.DownloadData))

        mi.setEnabled(
                DownloadSupportType.isOptionSet(dataAccessMeter.getDownloadStatus(), DownloadSupportType.DownloadData));

        menus[0] = mi;

        mi = ATSwingUtils.createMenuItem(menu_meter, "MN_METERS_READ_FILE", "MN_METERS_READ_FILE_DESC",
                "meters_read_file", this, null, this.i18nControlLocal, DataAccessMeter.getInstance(), parent);

        mi.setEnabled(
                DownloadSupportType.isOptionSet(dataAccessMeter.getDownloadStatus(), DownloadSupportType.DownloadDataFile));

        menus[1] = mi;

        menu_meter.addSeparator();

        ATSwingUtils.createMenuItem(menu_meter, "MN_METERS_LIST", "MN_METERS_LIST_DESC", "meters_list", this, null,
                this.i18nControlLocal, DataAccessMeter.getInstance(), parent);

        menu_meter.addSeparator();

        ATSwingUtils.createMenuItem(menu_meter, "MN_METERS_CONFIG", "MN_METERS_CONFIG_DESC", "meters_config", this,
                null, this.i18nControlLocal, DataAccessMeter.getInstance(), parent);

        menu_meter.addSeparator();

        ATSwingUtils.createMenuItem(menu_meter, "MN_METERS_ABOUT", "MN_METERS_ABOUT_DESC", "meters_about", this, null,
                this.i18nControlLocal, DataAccessMeter.getInstance(), parent);

        // System.out.println("MenuMeter Plugin");

        return menu_meter;
    }


    /**
     * {@inheritDoc}
     */
    public void refreshMenusAfterConfig() {
        menus[0].setEnabled(
                DownloadSupportType.isOptionSet(dataAccessMeter.getDownloadStatus(), DownloadSupportType.DownloadData));
        menus[1].setEnabled(
                DownloadSupportType.isOptionSet(dataAccessMeter.getDownloadStatus(), DownloadSupportType.DownloadDataFile));
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public JMenu[] getPlugInReportMenus() {
        // there are no print menus for this plugin
        return null;
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public JMenu[] getPlugInGraphMenus() {
        // no graph menus for this plugin
        return null;
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public void actionPerformed(ActionEvent ae) {
        String command = ae.getActionCommand();

        // FIXME-Andy
        // if (executeBasePluginAction(command, this.dataAccessMeter))

        if (command.equals("meters_read")) {
            new DeviceInstructionsDialog(this.parent, dataAccessMeter, this, DeviceDataHandler.TRANSFER_READ_DATA);
            // this.client.executeReturnAction(MeterPlugInServer.RETURN_ACTION_READ_DATA);
            this.client.executeReturnAction(ReturnActionType.ReadData);
        } else if (command.equals("meters_list")) {
            new BaseListDialog((JFrame) parent, dataAccessMeter);
        } else if (command.equals("meters_about")) {
            new AboutBaseDialog((JFrame) parent, dataAccessMeter);
        } else if (command.equals("meters_read_file")) {
            // System.out.println("Meters read file");
            new DeviceInstructionsDialog(this.parent,
                    DataAccessMeter.getInstance(), this,
                    DeviceDataHandler.TRANSFER_READ_DATA_FILE);
            // this.client.executeReturnAction(MeterPlugInServer.RETURN_ACTION_READ_DATA);
            this.client.executeReturnAction(ReturnActionType.ReadData);
        } else if (command.equals("meters_config")) {
            new DeviceConfigurationDialog((JFrame) parent, dataAccessMeter);
            refreshMenusAfterConfig();
            // this.client.executeReturnAction(MeterPlugInServer.RETURN_ACTION_CONFIG);
            this.client.executeReturnAction(ReturnActionType.ChangeConfig);
        } else {
            log.error("MeterPlugInServer::Unknown Command: " + command);
        }

    }

}
