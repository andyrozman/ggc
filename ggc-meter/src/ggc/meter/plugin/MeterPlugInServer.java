package ggc.meter.plugin;

import ggc.meter.gui.MeterInstructionsDialog;
import ggc.meter.util.DataAccessMeter;
import ggc.meter.util.I18nControl;
import ggc.plugin.cfg.DeviceConfigurationDialog;
import ggc.plugin.gui.AboutBaseDialog;
import ggc.plugin.list.BaseListDialog;

import java.awt.Container;

import javax.swing.JFrame;

import com.atech.db.DbDataReaderAbstract;
import com.atech.plugin.PlugInServer;
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
 *  Filename:  ###---###  
 *  Description:
 * 
 *  Author: Andy {andy@atech-software.com}
 */


public class MeterPlugInServer extends PlugInServer
{
    /**
     * Version of Meter Tool
     */
    //private String meter_tool_version = "0.3.7";
    
    public static final int COMMAND_READ_METER_DATA = 0;
    public static final int COMMAND_METERS_LIST = 1;
    public static final int COMMAND_CONFIGURATION = 2;
    public static final int COMMAND_ABOUT = 3;
    
    /*
    private String commands[] = { 
            "MN_METERS_READ_DESC",
            "MN_METERS_LIST_DESC",  
            "MN_METERS_CONFIG_DESC",
            "MN_METERS_ABOUT_DESC"
            };
    */
    
    
    
    public MeterPlugInServer()
    {
        super();
    }
    
    
    public MeterPlugInServer(Container cont, String selected_lang, ATDataAccessAbstract da)
    {
        super(cont, selected_lang, da);
        DataAccessMeter.getInstance().addComponent(cont);
    }
    

    
    
    
    /* 
     * executeCommand
     */
    @Override
    public void executeCommand(int command, Object obj_data)
    {
        switch(command)
        {
            case MeterPlugInServer.COMMAND_READ_METER_DATA:
                {
                    DbDataReaderAbstract reader = (DbDataReaderAbstract)obj_data; 
                    new MeterInstructionsDialog(reader, this);
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

    /* 
     * getName
     */
    @Override
    public String getName()
    {
        return ic.getMessage("METERS_PLUGIN");
    }

    /* 
     * getVersion
     */
    @Override
    public String getVersion()
    {
        return DataAccessMeter.PLUGIN_VERSION;
        //return DataAccessMeter.getInstance().getPlugInVersion();
        //return this.meter_tool_version;
    }

    /* 
     * getWhenWillBeImplemented
     */
    @Override
    public String getWhenWillBeImplemented()
    {
        return "0.4";
    }

    /* 
     * initPlugIn
     */
    @Override
    public void initPlugIn()
    {
        ic = m_da.getI18nControlInstance();
        I18nControl.getInstance().setLanguage(this.selected_lang);
        DataAccessMeter.getInstance().addComponent(this.parent);
    }
    
    
    
    
}


