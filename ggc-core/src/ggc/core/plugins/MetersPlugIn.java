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
 *  Application:   GGC - GNU Gluco Control
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
 *  Filename:     MetersPlugIn  
 *  Description:  Class Meter Plugin Client
 * 
 *  Author: andyrozman {andy@atech-software.com}  
 */


public class MetersPlugIn extends PlugInClient
{

    // private PlugInServer m_srv = null;

    /**
     * Command: Read Meter Data
     */
    public static final int COMMAND_READ_METER_DATA = 0;
    
    /**
     * Command: Meter List
     */
    public static final int COMMAND_METERS_LIST = 1;
    
    /**
     * Command: Meter Configuration
     */
    public static final int COMMAND_CONFIGURATION = 2;
    
    /**
     * Command: Meter About
     */
    public static final int COMMAND_ABOUT = 3;

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
    public MetersPlugIn(Component parent, I18nControlAbstract ic)
    {
        super((JFrame)parent, ic);
    }

    
    /**
     * Constructor
     */
    public MetersPlugIn()
    {
        super();
    }

    
    /**
     * Init Plugin
     */
    public void initPlugin()
    {
        this.commands = new String[4];
        this.commands[0] = "MN_METERS_READ_DESC";
        this.commands[1] = "MN_METERS_LIST_DESC";
        this.commands[2] = "MN_METERS_CONFIG_DESC";
        this.commands[3] = "MN_METERS_ABOUT_DESC";
        
        this.commands_implemented = new boolean[4];
        this.commands_implemented[0] = false;
        this.commands_implemented[1] = true;
        this.commands_implemented[2] = true;
        this.commands_implemented[3] = true;
    }

    
    /**
     * Check If Installed
     */
    public void checkIfInstalled()
    {
        try
        {
            Class<?> c = Class.forName("ggc.meter.plugin.MeterPlugInServer");

            this.m_server = (PlugInServer) c.newInstance();
            installed = true;
            
            this.m_server.init(this.parent, 
                DataAccess.getInstance().getI18nControlInstance().getSelectedLangauge(), 
                DataAccess.getInstance(), 
                this, 
                DataAccess.getInstance().getDb() );
            
            this.installed = true;
            
        }
        catch (Exception ex)
        {
            this.installed = false;
            ex.printStackTrace();
        }

        System.out.println("Installed [" + this.getNameBase() + ": " + this.installed);
        
    }

    
    /**
     * Get Name Base (untranslated)
     * 
     * @return name of plugin
     */
    public String getNameBase()
    {
        return "METERS_PLUGIN";
    }

    
    /**
     * Read Meter Data
     */
    public void readMeterData()
    {
        //this.featureNotImplemented(commands[MetersPlugIn.COMMAND_READ_METER_DATA]);
        int command = MetersPlugIn.COMMAND_READ_METER_DATA;
        
        if (m_server==null)
        {
            if (this.isCommandImplemented(command))
            {
                this.showMessage(String.format(ic.getMessage("PLUGIN_NOT_INSTALLED"), this.getName()));
            }
            else
            {
                this.featureNotImplemented(commands[command]);
            }
        }
        else
        {
            m_server.executeCommand(command);
            // TODO
/*            GGCDataReader greader = new GGCDataReader(DataAccess.getInstance().getDb(), GGCDataReader.DATA_METER);
            greader.start();
            
            m_server.executeCommand(command, greader); */
        }
        
    }


    /**
     * Action Performed
     */
    public void actionPerformed(ActionEvent e)
    {
        String command = e.getActionCommand();

        if (command.equals("meters_read"))
        {
            this.readMeterData();
            refreshPanels(RefreshInfo.PANEL_GROUP_ALL_DATA);
        }
        else if (command.equals("meters_list"))
        {
            this.executeCommand(MetersPlugIn.COMMAND_METERS_LIST);
        }
        else if (command.equals("meters_config"))
        {
            this.executeCommand(MetersPlugIn.COMMAND_CONFIGURATION);
            refreshPanels(RefreshInfo.PANEL_GROUP_PLUGINS_DEVICES);
        }
        else if (command.equals("meters_about"))
        {
            this.executeCommand(MetersPlugIn.COMMAND_ABOUT);
        }
        else
        {
            System.out.println("Wrong command for this plug-in [Meters]: "
                    + command);
        }

    }

    
    private void refreshPanels(int mask)
    {
        DataAccess.getInstance().setChangeOnEventSource(DataAccess.OBSERVABLE_PANELS, mask);
        //MainFrame mf = (MainFrame)parent;
        //mf.informationPanel.refreshGroup(mask);
        //
    }
    
    
    
    /**
     * Get When Will Be Implemented
     * 
     * @return
     */
    public String getWhenWillBeImplemented()
    {
        return "0.3";
    }

    
    /**
     * Get Short Status
     * 
     * @return
     */
    public String getShortStatus()
    {
        if (this.m_server != null)
            return String.format(ic.getMessage("STATUS_INSTALLED"), this.m_server.getVersion());
        else
            return ic.getMessage("STATUS_NOT_INSTALLED");
    }

    
    /**
     * Set Return Data (for getting data from plugin - async)
     * 
     * @param return_data
     * @param stat_rep_int
     */
    public void setReturnData(Object return_data, StatusReporterInterface stat_rep_int)
    {
        GGCDataWriter gdw = new GGCDataWriter(GGCDataWriter.DATA_METER, return_data, stat_rep_int);
        gdw.start();
    }

    
    /**
     * This is action that needs to be done, after read data.
     */
    public static final int RETURN_ACTION_READ_DATA = 1;
    
    
    /**
     * This is action that needs to be done, after config
     */
    public static final int RETURN_ACTION_CONFIG = 2;
   
    
    
    /**
     * This is method which can be used by server side to do certain action. Mainly this will be used
     * to run refreshes and such actions. This needs to be implemented by Client side, if you wish to use
     * it.
     * 
     * @param action_type
     */
    public void executeReturnAction(int action_type)
    {
        
        if (action_type == MetersPlugIn.RETURN_ACTION_READ_DATA)
        {
            refreshPanels(RefreshInfo.PANEL_GROUP_ALL_DATA);
        }
        else if (action_type == MetersPlugIn.RETURN_ACTION_CONFIG)
        {
            refreshPanels(RefreshInfo.PANEL_GROUP_PLUGINS_DEVICES);
        }
        
        
    }
    
    
    
}