package ggc.meter.plugin;

import ggc.meter.util.DataAccessMeter;
import ggc.meter.util.I18nControl;
import ggc.plugin.DevicePlugInServer;
import ggc.plugin.cfg.DeviceConfigurationDialog;
import ggc.plugin.gui.AboutBaseDialog;
import ggc.plugin.gui.DeviceInstructionsDialog;
import ggc.plugin.list.BaseListDialog;

import java.awt.Container;

import javax.swing.JFrame;

import com.atech.db.DbDataReaderAbstract;
import com.atech.utils.ATDataAccessAbstract;

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


public class MeterPlugInServer extends DevicePlugInServer
{
    
    /**
     *  Command: Read Meter Data  
     */
    public static final int COMMAND_READ_METER_DATA = 0;
    
    /**
     *  Command: Meters List  
     */
    public static final int COMMAND_METERS_LIST = 1;

    /**
     *  Command: Configuration
     */
    public static final int COMMAND_CONFIGURATION = 2;

    /**
     *  Command: About  
     */
    public static final int COMMAND_ABOUT = 3;
    
    /*
    private String commands[] = { 
            "MN_METERS_READ_DESC",
            "MN_METERS_LIST_DESC",  
            "MN_METERS_CONFIG_DESC",
            "MN_METERS_ABOUT_DESC"
            };
    */
    
    
    
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
    public MeterPlugInServer(Container cont, String selected_lang, ATDataAccessAbstract da)
    {
        super(cont, selected_lang, da);
        DataAccessMeter.getInstance().addComponent(cont);
    }
    

    
    /**
     * Execute Command on Server Side
     * 
     * @param command
     */
    @Override
    public void executeCommand(int command, Object obj_data)
    {
        switch(command)
        {
            case MeterPlugInServer.COMMAND_READ_METER_DATA:
                {
                    DbDataReaderAbstract reader = (DbDataReaderAbstract)obj_data; 
                    //new MeterInstructionsDialog(reader, this);
                    new DeviceInstructionsDialog(DataAccessMeter.getInstance(), reader, this);
                    return;
                }

            case MeterPlugInServer.COMMAND_METERS_LIST:
                {
                    new BaseListDialog((JFrame)parent, DataAccessMeter.getInstance());
                    return;
                }
            
            case MeterPlugInServer.COMMAND_ABOUT:
                {
                    new AboutBaseDialog((JFrame)parent, DataAccessMeter.getInstance());
                    return;
                }
            
            case MeterPlugInServer.COMMAND_CONFIGURATION:
                {
                    //m_da.listComponents();
                    //new SimpleConfigurationDialog(this.m_da);
                    new DeviceConfigurationDialog((JFrame)parent, DataAccessMeter.getInstance());
                    return;
                }
            
            default:
                {
                    System.out.println("Internal error with MeterPlugInServer.");
                }
            
        }
        
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
        return DataAccessMeter.PLUGIN_VERSION;
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
     * Init PlugIn which needs to be implemented 
     */
    @Override
    public void initPlugIn()
    {
        ic = m_da.getI18nControlInstance();
        I18nControl.getInstance().setLanguage(this.selected_lang);
        DataAccessMeter.getInstance().addComponent(this.parent);
        DataAccessMeter.getInstance().setHelpContext(m_da.getHelpContext());
    }
    
}


