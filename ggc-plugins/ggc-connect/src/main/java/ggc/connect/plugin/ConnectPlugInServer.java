package ggc.connect.plugin;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;
import java.util.Map;

import javax.swing.*;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.atech.db.hibernate.transfer.BackupRestoreCollection;
import com.atech.plugin.BackupRestorePlugin;
import com.atech.utils.ATDataAccessLMAbstract;
import com.atech.utils.ATSwingUtils;

import ggc.connect.defs.ConnectPluginDefinition;
import ggc.connect.util.DataAccessConnect;
import ggc.core.util.DataAccess;
import ggc.plugin.DevicePlugInServer;
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

public class ConnectPlugInServer extends DevicePlugInServer implements ActionListener
{

    private static final Logger LOG = LoggerFactory.getLogger(ConnectPlugInServer.class);

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

    DataAccessConnect dataAccessConnect;
    private JMenuItem[] menus = new JMenuItem[3];


    /**
    * Constructor
    */
    public ConnectPlugInServer()
    {
        super();
    }


    /**
     * Constructor
     *
     * @param cont
     * @param da
     */
    public ConnectPlugInServer(Container cont, ATDataAccessLMAbstract da)
    {
        super(cont, da);

        dataAccessConnect = DataAccessConnect.createInstance(getPluginDefinition(da));
        dataAccessConnect.addComponent(cont);
    }


    /**
     * {@inheritDoc}
     */
    private ConnectPluginDefinition getPluginDefinition(ATDataAccessLMAbstract da)
    {
        return new ConnectPluginDefinition(da.getLanguageManager());
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public String getName()
    {
        return ic.getMessage("CONNECT_PLUGIN");
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public String getVersion()
    {
        return dataAccessConnect.getPlugInVersion();
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public void initPlugIn()
    {
        ic = dataAccess.getI18nControlInstance();

        if (dataAccessConnect == null)
        {
            dataAccessConnect = DataAccessConnect
                    .createInstance(getPluginDefinition((ATDataAccessLMAbstract) dataAccess));
        }

        this.initPlugInServer((DataAccess) dataAccess, dataAccessConnect);

        // this.backup_restore_enabled = (this.backupRestoreCollection != null);

        this.installed = true;
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public Object getReturnObject(int ret_obj_id)
    {
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
        return null;
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public JMenu getPlugInMainMenu()
    {

        JMenu menuConnect = ATSwingUtils.createMenu("MN_CONNECT", null, ic_local);

        // JMenu menu_cgms = ATSwingUtils.createMenu("MN_CGMS", null, ic_local);
        //
        // JMenuItem menu = ATSwingUtils.createMenuItem(menu_cgms,
        // "MN_CGMS_READ", //
        // "MN_CGMS_READ_DESC", "plugin_read_data", //
        // this, null, ic_local, DataAccessCGMS.getInstance(), parent);
        //
        // menus[0] = menu;
        // menus[0].setEnabled(
        // DownloadSupportType.isOptionSet(dataAccessConnect.getDownloadStatus(),
        // DownloadSupportType.DownloadData));
        //
        // menu = ATSwingUtils.createMenuItem(menu_cgms, "MN_CGMS_READ_CONFIG",
        // //
        // "MN_CGMS_READ_CONFIG_DESC", "plugin_read_config", //
        // this, null, ic_local, DataAccessCGMS.getInstance(), parent);
        //
        // menus[1] = menu;
        // menus[1].setEnabled(DownloadSupportType.isOptionSet(dataAccessConnect.getDownloadStatus(),
        // DownloadSupportType.Download_Data_Config));
        //
        // menu = ATSwingUtils.createMenuItem(menu_cgms, "MN_CGMS_READ_FILE", //
        // "MN_CGMS_READ_FILE_DESC", "plugin_read_data_file", //
        // this, null, ic_local, DataAccessCGMS.getInstance(), parent);
        //
        // menus[2] = menu;
        // menus[2].setEnabled(DownloadSupportType.isOptionSet(dataAccessConnect.getDownloadStatus(),
        // DownloadSupportType.DownloadDataFile));
        //
        // menu_cgms.addSeparator();
        //
        // ATSwingUtils.createMenuItem(menu_cgms, "MN_CGMS_VIEW_DATA", //
        // "MN_CGMS_VIEW_DATA_DESC", "cgms_view_data", //
        // this, null, ic_local, DataAccessCGMS.getInstance(), parent);
        //
        // menu_cgms.addSeparator();
        //
        // ATSwingUtils.createMenuItem(menu_cgms, "MN_CGMS_LIST", //
        // "MN_CGMS_LIST_DESC", "plugin_list", //
        // this, null, ic_local, DataAccessCGMS.getInstance(), parent);
        //
        // menu_cgms.addSeparator();
        //
        // ATSwingUtils.createMenuItem(menu_cgms, "MN_CGMS_CONFIG", //
        // "MN_CGMS_CONFIG_DESC", "plugin_config", //
        // this, null, ic_local, DataAccessCGMS.getInstance(), parent);
        //
        // menu_cgms.addSeparator();
        //
        // ATSwingUtils.createMenuItem(menu_cgms, "MN_CGMS_ABOUT", //
        // "MN_CGMS_ABOUT_DESC", "plugin_about", this, null, ic_local,
        // DataAccessCGMS.getInstance(), parent);
        //
        // return menu_cgms;

        return menuConnect;
    }


    /**
     * {@inheritDoc}
     */
    public void refreshMenusAfterConfig()
    {
        // menus[0].setEnabled(
        // DownloadSupportType.isOptionSet(dataAccessConnect.getDownloadStatus(),
        // DownloadSupportType.DownloadData));
        // menus[1].setEnabled(
        // DownloadSupportType.isOptionSet(dataAccessConnect.getDownloadStatus(),
        // DownloadSupportType.DownloadConfig));
        // menus[2].setEnabled(DownloadSupportType.isOptionSet(dataAccessConnect.getDownloadStatus(),
        // DownloadSupportType.DownloadDataFile));
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public DataAccessPlugInBase getPlugInDataAccess()
    {
        return dataAccessConnect;
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public void actionPerformed(ActionEvent ae)
    {
        String command = ae.getActionCommand();

        if (!executeBasePluginAction(command, this.dataAccessConnect))
        {
            // if (command.equals("cgms_view_data"))
            // {
            // //new CGMSDataDialog(dataAccessConnect, (JFrame) this.parent);
            // }
            // else
            {
                System.out.println("ConnectPluginServer::Unknown Command: " + command);
            }
        }

    }


    /**
     * {@inheritDoc}
     */
    @Override
    public BackupRestorePlugin getBackupRestoreHandler()
    {
        // return new BackupRestoreCGMSHandler();
        return null;
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public List<Object> getDataFromPlugin(Map<String, Object> parameters)
    {
        // if (MapUtils.isEmpty(parameters))
        // {
        // LOG.warn("We cannot retrieve data for empty request (no parameters):
        // " + parameters);
        // return null;
        // }
        //
        // if (!parameters.containsKey("dataType"))
        // {
        // LOG.warn("DataType of return data must be specified, along with other
        // required parameters.");
        // return null;
        // }
        //
        // if (parameters.get("dataType").equals("CGMSReadingsDaily4Graph"))
        // {
        // CGMSGraphDataHandler graphData = new CGMSGraphDataHandler();
        // XYSeries series = graphData.getCGMSDailyReadings(dataAccessConnect,
        // (GregorianCalendar) parameters.get("calendarDate"));
        //
        // List<Object> data = new ArrayList<Object>();
        // data.add(series);
        //
        // return data;
        // }
        // else
        // {
        // LOG.warn("Unknown dataType requested: " +
        // parameters.get("dataType"));
        // }

        return null;
    }

}
