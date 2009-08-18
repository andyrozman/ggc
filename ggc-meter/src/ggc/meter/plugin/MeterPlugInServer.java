package ggc.meter.plugin;

import ggc.core.util.DataAccess;
import ggc.meter.util.DataAccessMeter;
import ggc.meter.util.I18nControl;
import ggc.plugin.DevicePlugInServer;
import ggc.plugin.cfg.DeviceConfigEntry;
import ggc.plugin.cfg.DeviceConfigurationDialog;
import ggc.plugin.gui.AboutBaseDialog;
import ggc.plugin.gui.DeviceInstructionsDialog;
import ggc.plugin.list.BaseListDialog;

import java.awt.Container;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JFrame;
import javax.swing.JMenu;

import com.atech.db.hibernate.transfer.BackupRestoreCollection;
import com.atech.utils.ATDataAccessAbstract;
import com.atech.utils.ATSwingUtils;

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
    
    DataAccessMeter m_da_local = DataAccessMeter.getInstance();
    //I18nControlAbstract ic_main = m_da.getI18nControlInstance();
    
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
    
    
    
    /**
     * Return Object: Selected Device with parameters
     */
    public static final int RETURN_OBJECT_DEVICE_WITH_PARAMS = 1;
    
    
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
        //m_da_local = DataAccessMeter.getInstance();
        //ic_main = da.getI18nControlInstance();
        m_da_local.addComponent(cont);
    }
    

    
    /**
     * Execute Command on Server Side [PlugIn Framework v1]
     * 
     * @deprecated this framework v1 functionality and should be removed, and instead menus should be created from
     *             within plugin
     * @param command
     */
    @Override
    public void executeCommand(int command, Object obj_data)
    {
        switch(command)
        {
            case MeterPlugInServer.COMMAND_READ_METER_DATA:
                {
                    // TODO: Remove this 
                    //DbDataReaderAbstract reader = (DbDataReaderAbstract)obj_data; 
                    //DeviceDataHandler ddh = m_da_local.getDeviceDataHandler();
                    //ddh.setDbDataReader(reader);
                    
                    //new MeterInstructionsDialog(reader, this);
                    new DeviceInstructionsDialog(this.parent, m_da_local, /*reader,*/ this, DeviceInstructionsDialog.CONTINUING_TYPE_READ_DATA);
                    return;
                }

            case MeterPlugInServer.COMMAND_METERS_LIST:
                {
                    new BaseListDialog((JFrame)parent, m_da_local);
                    return;
                }
            
            case MeterPlugInServer.COMMAND_ABOUT:
                {
                    new AboutBaseDialog((JFrame)parent, m_da_local);
                    return;
                }
            
            case MeterPlugInServer.COMMAND_CONFIGURATION:
                {
                    //m_da.listComponents();
                    //new SimpleConfigurationDialog(this.m_da);
                    new DeviceConfigurationDialog((JFrame)parent, m_da_local);
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
     * Init PlugIn which needs to be implemented [PlugIn Framework v1/v2] 
     */
    @Override
    public void initPlugIn()
    {
        ic = m_da.getI18nControlInstance();
        I18nControl.getInstance().setLanguage(this.selected_lang);
        
        DataAccessMeter da = DataAccessMeter.getInstance();
//        ic = da.getI18nControlInstance();
        
        da.addComponent(this.parent);
        da.setHelpContext(m_da.getHelpContext());
        da.setCurrentUserId(((DataAccess)m_da).current_user_id);
        da.createDb(m_da.getHibernateDb());
        
        //DataAccessMeter.getInstance().setBGMeasurmentType(m_da.get)
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
            DeviceConfigEntry de = m_da_local.getDeviceConfiguration().getSelectedDeviceInstance();
            
            if (de==null)
                return m_da_local.getI18nControlInstance().getMessage("NO_DEVICE_SELECTED");
            else
            {
                if (de.device_device.equals(m_da_local.getI18nControlInstance().getMessage("NO_DEVICE_SELECTED")))
                {
                    return m_da_local.getI18nControlInstance().getMessage("NO_DEVICE_SELECTED");
                }
                else
                    return String.format(m_da_local.getI18nControlInstance().getMessage("DEVICE_FULL_NAME_WITH_PORT"), de.device_device, de.communication_port);
            }
        }
        else
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

        JMenu menu_meter = ATSwingUtils.createMenu("MN_METERS", null, ic);
        
        ATSwingUtils.createMenuItem(menu_meter, 
            "MN_METERS_READ", 
            "MN_METERS_READ_DESC", 
            "meters_read", 
            this, null, 
            ic, DataAccessMeter.getInstance(), parent);
        
        menu_meter.addSeparator();
        
        ATSwingUtils.createMenuItem(menu_meter, 
            "MN_METERS_LIST", 
            "MN_METERS_LIST_DESC", 
            "meters_list", 
            this, null, 
            ic, DataAccessMeter.getInstance(), parent);
        
        menu_meter.addSeparator();
        
        ATSwingUtils.createMenuItem(menu_meter, 
            "MN_METERS_CONFIG", 
            "MN_METERS_CONFIG_DESC", 
            "meters_config", 
            this, null, 
            ic, DataAccessMeter.getInstance(), parent);
        
        menu_meter.addSeparator();
        
        ATSwingUtils.createMenuItem(menu_meter, 
            "MN_METERS_ABOUT", 
            "MN_METERS_ABOUT_DESC", 
            "meters_about", 
            this, null, 
            ic, DataAccessMeter.getInstance(), parent);
        
        //System.out.println("MenuMeter Plugin");
        
        
        return menu_meter;
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
    public void actionPerformed(ActionEvent ae)
    {
        String command = ae.getActionCommand();
        
        if (command.equals("meters_read"))
        {
            new DeviceInstructionsDialog(this.parent, m_da_local, this, DeviceInstructionsDialog.CONTINUING_TYPE_READ_DATA);
        }
        else if (command.equals("meters_list"))
        {
            new BaseListDialog((JFrame)parent, m_da_local);
        }
        else if (command.equals("meters_about"))
        {
            new AboutBaseDialog((JFrame)parent, m_da_local);
        }
        else if (command.equals("meters_config"))
        {
            new DeviceConfigurationDialog((JFrame)parent, m_da_local);
        }
        else
            System.out.println("MeterPlugInServer::Unknown Command: " + command);
            
    }
    
    
    
}


