package ggc.core.plugins;

import ggc.core.util.DataAccess;

import java.awt.Container;
import java.awt.event.ActionEvent;

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
 *  Filename:     PumpsPlugIn  
 *  Description:  Class Pump Plugin Client
 * 
 *  Author: andyrozman {andy@atech-software.com}  
 */


public class PumpsPlugIn extends PlugInClient
{

    
    /**
     * Command: Read Pump Data
     */
    public static final int COMMAND_READ_PUMP_DATA = 0;
    
    /**
     * Command: Pump List
     */
    public static final int COMMAND_PUMPS_LIST = 1;
    
    /**
     * Command: Pump Configuration
     */
    public static final int COMMAND_CONFIGURATION = 2;
    
    /**
     * Command: Pump Profiles
     */
    public static final int COMMAND_PROFILES = 3;
    
    /**
     * Command: Pump Manual Entry
     */
    public static final int COMMAND_MANUAL_ENTRY = 4;
    
    /**
     * Command: Pump Additional Data
     */
    public static final int COMMAND_ADDITIONAL_DATA = 5;
    
    /**
     * Command: Pump About
     */
    public static final int COMMAND_ABOUT = 6;
    
    
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
    public PumpsPlugIn(Container parent, I18nControlAbstract ic)
    {
        super(parent, ic);
    }

    /**
     * Constructor
     */
    public PumpsPlugIn()
    {
        super();
    }

    /**
     * Check If Installed
     */
    public void checkIfInstalled()
    {
        try
        {
            Class<?> c = Class.forName("ggc.pump.plugin.PumpPlugInServer");

            this.m_server = (PlugInServer) c.newInstance();
            installed = true;
            
            this.m_server.init(this.parent, 
                DataAccess.getInstance().getI18nControlInstance().getSelectedLangauge(), 
                DataAccess.getInstance(), 
                this, 
                DataAccess.getInstance().getDb() );
        }
        catch (Exception ex)
        {
            System.out.println("Ex:" + ex);
        }
    }

    
    /**
     * Get Name 
     * 
     * @return name of plugin
     */
    public String getName()
    {
        return ic.getMessage("PUMPS_PLUGIN");
    }

    
    /**
     * Read Pumps Data
     */
    public void readPumpsData()
    {
        this.featureNotImplemented(commands[0]);
    }

    
    /**
     * Init Plugin
     */
    public void initPlugin()
    {
        this.commands = new String[7];
        this.commands[0] = "MN_PUMPS_READ_DESC";
        this.commands[1] = "MN_PUMPS_LIST_DESC";
        this.commands[2] = "MN_PUMPS_CONFIG_DESC";
        this.commands[3] = "MN_PUMP_PROFILES_DESC";
        this.commands[4] = "MN_PUMPS_MANUAL_ENTRY_DESC";
        this.commands[5] = "MN_PUMPS_ADDITIONAL_DATA_DESC";
        this.commands[6] = "MN_PUMPS_ABOUT_DESC";

        this.commands_implemented = new boolean[7];
        this.commands_implemented[0] = false;
        this.commands_implemented[1] = false;
        this.commands_implemented[2] = true;
        this.commands_implemented[3] = false;
        this.commands_implemented[4] = true;
        this.commands_implemented[5] = true;
        this.commands_implemented[6] = true;
        
        this.commands_will_be_done = new String[7];
        this.commands_will_be_done[0] = "0.6";
        this.commands_will_be_done[1] = "0.5";
        this.commands_will_be_done[2] = null;
        this.commands_will_be_done[3] = "0.6";
        this.commands_will_be_done[4] = null;
        this.commands_will_be_done[5] = null;
        this.commands_will_be_done[6] = null;
        
    }


    /**
     * actionPerformed
     */
    public void actionPerformed(ActionEvent e)
    {
        String command = e.getActionCommand();

        if (command.equals("pumps_read"))
        {
            this.readPumpsData();
        }
        else if (command.equals("pumps_list"))
        {
            this.executeCommand(PumpsPlugIn.COMMAND_PUMPS_LIST);
        }
        else if (command.equals("pumps_profile"))
        {
            this.executeCommand(PumpsPlugIn.COMMAND_PROFILES);
        }
        else if (command.equals("pumps_manual_entry"))
        {
            this.executeCommand(PumpsPlugIn.COMMAND_MANUAL_ENTRY);
        }
        else if (command.equals("pumps_additional_data"))
        {
            this.executeCommand(PumpsPlugIn.COMMAND_ADDITIONAL_DATA);
        }
        else if (command.equals("pumps_config"))
        {
            this.executeCommand(PumpsPlugIn.COMMAND_CONFIGURATION);
        }
        else if (command.equals("pumps_about"))
        {
            this.executeCommand(PumpsPlugIn.COMMAND_ABOUT);
        }
        else
        {
            System.out.println("Wrong command for this plug-in [Pumps]: " + command);
        }

    }

    
    /**
     * Get When Will Be Implemented
     * 
     * @return
     */
    public String getWhenWillBeImplemented()
    {
        return "0.5";
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
    }

}