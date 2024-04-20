package ggc.cgms.plugin;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.*;
import java.util.List;
import java.util.stream.Collectors;

import javax.swing.*;

import org.apache.commons.collections4.MapUtils;
import org.jfree.data.xy.XYSeries;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.atech.db.hibernate.transfer.BackupRestoreCollection;
import com.atech.i18n.I18nControlAbstract;
import com.atech.plugin.BackupRestorePlugin;
import com.atech.utils.ATDataAccessLMAbstract;
import com.atech.utils.ATSwingUtils;

import ggc.cgms.data.db.CGMSData;
import ggc.cgms.data.db.CGMSDataExtended;
import ggc.cgms.defs.CGMSPluginDefinition;
import ggc.cgms.gui.viewer.CGMSDataDialog;
import ggc.cgms.util.DataAccessCGMS;
import ggc.core.util.DataAccess;
import ggc.plugin.DevicePlugInServer;
import ggc.plugin.cfg.DeviceConfigEntry;
import ggc.plugin.device.DownloadSupportType;
import ggc.plugin.graph.data.CGMSGraphDataHandler;
import ggc.plugin.util.DataAccessPlugInBase;

/**
 *  Application:   GGC - GNU Gluco Control
 *  Plug-in:       CGMS Tool (support for CGMS devices)
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
 *  Filename:     CGMPlugInServer
 *  Description:  This is server side of plugin architecture
 * 
 *  Author: Andy {andy@atech-software.com}
 */

public class CGMSPlugInServer extends DevicePlugInServer implements ActionListener
{

    private static final Logger LOG = LoggerFactory.getLogger(CGMSPlugInServer.class);

    /**
     * Return Object: Selected Device with parameters
     */
    public static final int RETURN_OBJECT_DEVICE_WITH_PARAMS = 1;

    /**
     * This is action that needs to be done, after read data.
     */
    public static final int RETURN_ACTION_READ_DATA = 1;

    /**
     * This is action that needs to be done, after config
     */
    public static final int RETURN_ACTION_CONFIG = 2;

    // private String commands[] = { "MN_CGMS_READ_DESC", "MN_CGMS_LIST_DESC",
    // "MN_CGMS_CONFIG_DESC", "MN_CGMS_ABOUT_DESC" };

    DataAccessCGMS dataAccessCGMS;
    private JMenuItem[] menus = new JMenuItem[3];


    /**
    * Constructor
    */
    public CGMSPlugInServer()
    {
        super();
    }


    /**
     * Constructor
     * 
     * @param cont
     * @param da
     */
    public CGMSPlugInServer(Container cont, ATDataAccessLMAbstract da)
    {
        super(cont, da);

        dataAccessCGMS = DataAccessCGMS.createInstance(getPluginDefinition(da));
        dataAccessCGMS.addComponent(cont);
    }


    /**
     * {@inheritDoc}
     */
    private CGMSPluginDefinition getPluginDefinition(ATDataAccessLMAbstract da)
    {
        return new CGMSPluginDefinition(da.getLanguageManager());
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public String getName()
    {
        return i18nControl.getMessage("CGMS_PLUGIN");
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public String getVersion()
    {
        return dataAccessCGMS.getPlugInVersion();
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public void initPlugIn()
    {
        i18nControl = dataAccess.getI18nControlInstance();

        if (dataAccessCGMS == null)
        {
            dataAccessCGMS = DataAccessCGMS.createInstance(getPluginDefinition((ATDataAccessLMAbstract) dataAccess));
        }

        this.initPlugInServer((DataAccess) dataAccess, dataAccessCGMS);

        this.installed = true;
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public Object getReturnObject(int ret_obj_id)
    {

        if (ret_obj_id == CGMSPlugInServer.RETURN_OBJECT_DEVICE_WITH_PARAMS)
        {
            DataAccessCGMS da = DataAccessCGMS.getInstance();
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
        return null;
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public BackupRestoreCollection getBackupObjects()
    {
        I18nControlAbstract ic_pump = DataAccessCGMS.getInstance().getI18nControlInstance();
        BackupRestoreCollection brc = new BackupRestoreCollection("CGMS_TOOL", ic_pump);
        brc.addNodeChild(new CGMSData(ic_pump));
        brc.addNodeChild(new CGMSDataExtended(ic_pump));

        return brc;
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public JMenu getPlugInMainMenu()
    {
        JMenu menu_cgms = ATSwingUtils.createMenu("MN_CGMS", null, i18nControlLocal);

        JMenuItem menu = ATSwingUtils.createMenuItem(menu_cgms, "MN_CGMS_READ", //
            "MN_CGMS_READ_DESC", "plugin_read_data", //
            this, null, i18nControlLocal, DataAccessCGMS.getInstance(), parent);

        menus[0] = menu;
        menus[0].setEnabled(
            DownloadSupportType.isOptionSet(dataAccessCGMS.getDownloadStatus(), DownloadSupportType.DownloadData));

        menu = ATSwingUtils.createMenuItem(menu_cgms, "MN_CGMS_READ_CONFIG", //
            "MN_CGMS_READ_CONFIG_DESC", "plugin_read_config", //
            this, null, i18nControlLocal, DataAccessCGMS.getInstance(), parent);

        menus[1] = menu;
        menus[1].setEnabled(DownloadSupportType.isOptionSet(dataAccessCGMS.getDownloadStatus(),
            DownloadSupportType.Download_Data_Config));

        menu = ATSwingUtils.createMenuItem(menu_cgms, "MN_CGMS_READ_FILE", //
            "MN_CGMS_READ_FILE_DESC", "plugin_read_data_file", //
            this, null, i18nControlLocal, DataAccessCGMS.getInstance(), parent);

        menus[2] = menu;
        menus[2].setEnabled(
            DownloadSupportType.isOptionSet(dataAccessCGMS.getDownloadStatus(), DownloadSupportType.DownloadDataFile));

        menu_cgms.addSeparator();

        ATSwingUtils.createMenuItem(menu_cgms, "MN_CGMS_VIEW_DATA", //
            "MN_CGMS_VIEW_DATA_DESC", "cgms_view_data", //
            this, null, i18nControlLocal, DataAccessCGMS.getInstance(), parent);

        menu_cgms.addSeparator();

        ATSwingUtils.createMenuItem(menu_cgms, "MN_CGMS_LIST", //
            "MN_CGMS_LIST_DESC", "plugin_list", //
            this, null, i18nControlLocal, DataAccessCGMS.getInstance(), parent);

        menu_cgms.addSeparator();

        ATSwingUtils.createMenuItem(menu_cgms, "MN_CGMS_CONFIG", //
            "MN_CGMS_CONFIG_DESC", "plugin_config", //
            this, null, i18nControlLocal, DataAccessCGMS.getInstance(), parent);

        menu_cgms.addSeparator();

        ATSwingUtils.createMenuItem(menu_cgms, "MN_CGMS_ABOUT", //
            "MN_CGMS_ABOUT_DESC", "plugin_about", this, null, i18nControlLocal, DataAccessCGMS.getInstance(), parent);

        return menu_cgms;
    }


    /**
     * {@inheritDoc}
     */
    public void refreshMenusAfterConfig()
    {
        menus[0].setEnabled(
            DownloadSupportType.isOptionSet(dataAccessCGMS.getDownloadStatus(), DownloadSupportType.DownloadData));
        menus[1].setEnabled(
            DownloadSupportType.isOptionSet(dataAccessCGMS.getDownloadStatus(), DownloadSupportType.DownloadConfig));
        menus[2].setEnabled(
            DownloadSupportType.isOptionSet(dataAccessCGMS.getDownloadStatus(), DownloadSupportType.DownloadDataFile));
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public DataAccessPlugInBase getPlugInDataAccess()
    {
        return dataAccessCGMS;
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public void actionPerformed(ActionEvent ae)
    {
        String command = ae.getActionCommand();

        if (!executeBasePluginAction(command, this.dataAccessCGMS))
        {
            if (command.equals("cgms_view_data"))
            {
                new CGMSDataDialog(dataAccessCGMS, (JFrame) this.parent);
            }
            else
            {
                System.out.println("CGMSPluginServer::Unknown Command: " + command);
            }
        }

    }


    /**
     * {@inheritDoc}
     */
    @Override
    public BackupRestorePlugin getBackupRestoreHandler()
    {
        return new BackupRestoreCGMSHandler();
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public List<Object> getDataFromPlugin(Map<String, Object> parameters)
    {
        if (MapUtils.isEmpty(parameters))
        {
            LOG.warn("We cannot retrieve data for empty request (no parameters): " + parameters);
            return null;
        }

        // just getting the libraries
        if (parameters.containsKey("pluginLibraries")) {
            return getPlugInDataAccess().getPlugInLibraries()
                    .stream()
                    .map(a -> (Object) a)
                    .collect(Collectors.toList());
        } else if (parameters.containsKey("pluginBaseModule")) {
            return Arrays.asList(getPlugInDataAccess().getBaseModule());
        } else if (parameters.containsKey("pluginModule")) {
            return Arrays.asList(getPlugInDataAccess().getPluginModule());
        }

        if (!parameters.containsKey("dataType"))
        {
            LOG.warn("DataType of return data must be specified, along with other required parameters.");
            return null;
        }

        if (parameters.get("dataType").equals("CGMSReadingsDaily4Graph"))
        {
            CGMSGraphDataHandler graphData = new CGMSGraphDataHandler();
            XYSeries series = graphData.getCGMSDailyReadings(dataAccessCGMS,
                (GregorianCalendar) parameters.get("calendarDate"));

            List<Object> data = new ArrayList<Object>();
            data.add(series);

            return data;
        }
        else
        {
            LOG.warn("Unknown dataType requested: " + parameters.get("dataType"));
        }

        return null;
    }

}
