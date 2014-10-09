package ggc.core.plugins;

import ggc.core.util.DataAccess;
import ggc.core.util.RefreshInfo;

import java.awt.Component;
import java.awt.event.ActionEvent;

import javax.swing.JFrame;

import com.atech.graphics.components.StatusReporterInterface;
import com.atech.i18n.I18nControlAbstract;
import com.atech.plugin.PlugInClient;
import com.atech.plugin.PlugInServer;

/**
 * Application: GGC - GNU Gluco Control
 * See AUTHORS for copyright information.
 * This program is free software; you can redistribute it and/or modify it under
 * the terms of the GNU General Public License as published by the Free Software
 * Foundation; either version 2 of the License, or (at your option) any later
 * version.
 * This program is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU General Public License for more
 * details.
 * You should have received a copy of the GNU General Public License along with
 * this program; if not, write to the Free Software Foundation, Inc., 59 Temple
 * Place, Suite 330, Boston, MA 02111-1307 USA
 * Filename: CGMSPlugIn
 * Description: Class CGMS Plugin Client
 * Author: andyrozman {andy@atech-software.com}
 */

public class CGMSPlugIn extends PlugInClient
{

    /**
     * Command: Read CGMS Data
     */
    public static final int COMMAND_READ_CGMS_DATA = 0;

    /**
     * Command: CGMS List
     */
    public static final int COMMAND_CGMS_LIST = 1;

    /**
     * Command: CGMS Configuration
     */
    public static final int COMMAND_CGMS_CONFIGURATION = 2;

    /**
     * Command: CGMS About
     */
    public static final int COMMAND_CGMS_ABOUT = 3;

    /**
     * Return Object: Selected Device with parameters
     */
    public static final int RETURN_OBJECT_DEVICE_WITH_PARAMS = 1;

    /**
     * Constructor
     * 
     * @param parent
     * @param ic
     */
    public CGMSPlugIn(Component parent, I18nControlAbstract ic)
    {
        super((JFrame) parent, ic);
    }

    /**
     * Constructor
     */
    public CGMSPlugIn()
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
            Class<?> c = Class.forName("ggc.cgms.plugin.CGMSPlugInServer");

            this.m_server = (PlugInServer) c.newInstance();
            installed = true;

            this.m_server.init(this.parent, DataAccess.getInstance().getI18nControlInstance().getSelectedLanguage(),
                DataAccess.getInstance(), this, DataAccess.getInstance().getDb());
        }
        catch (Exception ex)
        {
            this.installed = false;
            // System.out.println("CGMSPlugInServer::Exception:" + ex);
            // ex.printStackTrace();
        }

    }

    /**
     * Get Name Base (untranslated)
     * 
     * @return name of plugin
     */
    @Override
    public String getNameBase()
    {
        return "CGMS_PLUGIN";
    }

    /**
     * Init Plugin
     */
    @Override
    public void initPlugin()
    {
        this.commands = new String[4];
        this.commands[0] = "MN_CGMS_READ_DESC";
        this.commands[1] = "MN_CGMS_LIST_DESC";
        this.commands[2] = "MN_CGMS_CONFIG_DESC";
        this.commands[3] = "MN_CGMS_ABOUT_DESC";

        this.commands_implemented = new boolean[4];
        this.commands_implemented[0] = false;
        this.commands_implemented[1] = false;
        this.commands_implemented[2] = false;
        this.commands_implemented[3] = false;

        this.commands_will_be_done = new String[4];
        this.commands_will_be_done[0] = "0.6";
        this.commands_will_be_done[1] = "0.6";
        this.commands_will_be_done[2] = "0.6";
        this.commands_will_be_done[3] = "0.6";

    }

    /**
     * Read CGMS Data
     */
    public void readCGMSData()
    {
        this.featureNotImplemented(commands[0]);
    }

    /**
     * actionPerformed
     */
    @Override
    public void actionPerformed(ActionEvent e)
    {
        String command = e.getActionCommand();

        if (command.equals("cgms_read"))
        {
            this.readCGMSData();
            refreshPanels(RefreshInfo.PANEL_GROUP_ALL_DATA);

        }
        else if (command.equals("cgms_list"))
        {
            this.executeCommand(CGMSPlugIn.COMMAND_CGMS_LIST);
        }
        else if (command.equals("cgms_config"))
        {
            this.executeCommand(CGMSPlugIn.COMMAND_CGMS_CONFIGURATION);
            refreshPanels(RefreshInfo.PANEL_GROUP_PLUGINS_DEVICES);
        }
        else if (command.equals("cgms_about"))
        {
            this.executeCommand(CGMSPlugIn.COMMAND_CGMS_ABOUT);
        }
        else
        {
            System.out.println("Wrong command for this plug-in [CGMS]: " + command);
        }

    }

    /**
     * Get When Will Be Implemented
     * 
     * @return
     */
    @Override
    public String getWhenWillBeImplemented()
    {
        return "0.6";
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

    private void refreshPanels(int mask)
    {
        DataAccess.getInstance().setChangeOnEventSource(DataAccess.OBSERVABLE_PANELS, mask);
    }

    /**
     * This is method which can be used by server side to do certain action.
     * Mainly this will be used
     * to run refreshes and such actions. This needs to be implemented by Client
     * side, if you wish to use
     * it.
     * 
     * @param action_type
     */
    @Override
    public void executeReturnAction(int action_type)
    {

        if (action_type == PumpsPlugIn.RETURN_ACTION_READ_DATA)
        {
            refreshPanels(RefreshInfo.PANEL_GROUP_ALL_DATA);
        }
        else if (action_type == PumpsPlugIn.RETURN_ACTION_CONFIG)
        {
            refreshPanels(RefreshInfo.PANEL_GROUP_PLUGINS_DEVICES);
        }

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

}
