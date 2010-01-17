package ggc.pump.plugin;

import ggc.core.util.DataAccess;
import ggc.plugin.DevicePlugInServer;
import ggc.plugin.cfg.DeviceConfigEntry;
import ggc.plugin.cfg.DeviceConfigurationDialog;
import ggc.plugin.gui.AboutBaseDialog;
import ggc.plugin.gui.DeviceInstructionsDialog;
import ggc.plugin.list.BaseListDialog;
import ggc.pump.db.PumpData;
import ggc.pump.db.PumpDataExtended;
import ggc.pump.db.PumpProfile;
import ggc.pump.gui.PumpPrintDialog;
import ggc.pump.gui.manual.PumpDataDialog;
import ggc.pump.gui.profile.ProfileSelector;
import ggc.pump.util.DataAccessPump;

import java.awt.Container;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JFrame;
import javax.swing.JMenu;

import com.atech.db.hibernate.transfer.BackupRestoreCollection;
import com.atech.i18n.I18nControlAbstract;
import com.atech.plugin.BackupRestorePlugin;
import com.atech.utils.ATDataAccessLMAbstract;
import com.atech.utils.ATSwingUtils;

/**
 *  Application:   GGC - GNU Gluco Control
 *  Plug-in:       Pump Tool (support for Pump devices)
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


public class PumpPlugInServer extends DevicePlugInServer implements ActionListener
{

    //String plugin_version = "0.1.7.1";
    
    I18nControlAbstract ic_local = null;
    
    
    /**
     *  Command: Read Pump Data  
     */
    public static final int COMMAND_READ_PUMP_DATA = 0;
    
    /**
     *  Command: Pumps List  
     */
    public static final int COMMAND_PUMPS_LIST = 1;
    
    /**
     *  Command: Configuration  
     */
    public static final int COMMAND_CONFIGURATION = 2;
    
    /**
     *  Command: Profiles  
     */
    public static final int COMMAND_PROFILES = 3;
    
    /**
     *  Command: Manual Entry 
     */
    public static final int COMMAND_MANUAL_ENTRY = 4;
    
    /**
     *  Command: Additional Data  
     */
    public static final int COMMAND_ADDITIONAL_DATA = 5;
    
    /**
     *  Command: About  
     */
    public static final int COMMAND_ABOUT = 6;

    
    /**
     * This is action that needs to be done, after read data.
     */
    public static final int RETURN_ACTION_READ_DATA = 1;
    
    
    /**
     * This is action that needs to be done, after config
     */
    public static final int RETURN_ACTION_CONFIG = 2;
    
    
    
    /**
     * Return Object: Selected Device with parameters
     */
    public static final int RETURN_OBJECT_DEVICE_WITH_PARAMS = 1;
    
    
    private String commands[] = { 
                                  "MN_PUMPS_READ_DESC", 
                                  "MN_PUMPS_LIST_DESC", 
                                  "MN_PUMPS_CONFIG_DESC",
                                                               
                                  "MN_PUMP_PROFILES_DESC", 
                                  "MN_PUMPS_MANUAL_ENTRY_DESC",
                                  "MN_PUMPS_ADDITIONAL_DATA_DESC", 
                                  
                                  "MN_PUMPS_ABOUT" };
    
    private DataAccessPump da_local = null;
    
    
    /**
     * Constructor
     */
    public PumpPlugInServer()
    {
        super();
        //da_local = DataAccessPump.getInstance();
    }
    
    
    /**
     * Constructor
     * 
     * @param cont
     * @param selected_lang
     * @param da
     */
    public PumpPlugInServer(Container cont, String selected_lang, ATDataAccessLMAbstract da)
    {
        super(cont, selected_lang, da);
        da_local = DataAccessPump.createInstance(da.getLanguageManager());
        da_local.addComponent(cont);
        //DataAccessPump.getInstance().setPlugInServerInstance(this);
        //DataAccessPump.getInstance().m
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
/*
            case PumpPlugInServer.COMMAND_CONFIGURATION:
            {
                new DeviceConfigurationDialog((JFrame)this.parent, DataAccessPump.getInstance());
                //new SimpleConfigurationDialog(this.m_da);
                return;
            }
            
            case PumpPlugInServer.COMMAND_PUMPS_LIST:
            {
                new BaseListDialog((JFrame)this.parent, DataAccessPump.getInstance());
                return;
            }

            case PumpPlugInServer.COMMAND_ABOUT:
            {
                new AboutBaseDialog((JFrame)this.parent, DataAccessPump.getInstance());
                return;
            }

            case PumpPlugInServer.COMMAND_PROFILES:
            {
                //System.out.println("parent: " + this.parent);
                new ProfileSelector(DataAccessPump.getInstance(), this.parent);
                return;
            }
            
            case PumpPlugInServer.COMMAND_READ_PUMP_DATA:
            {
                new DeviceInstructionsDialog(DataAccessPump.getInstance(), this);
                return;
            }
            
            
            case PumpPlugInServer.COMMAND_MANUAL_ENTRY:
            case PumpPlugInServer.COMMAND_ADDITIONAL_DATA:
            {
                new PumpDataDialog(DataAccessPump.getInstance(), this.parent);
                return;
            } 
  */          
            default:
            {
                this.featureNotImplemented(commands[command]);
                return;
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
        return ic.getMessage("PUMP_PLUGIN");
    }

    
    
    /**
     * Get Version of plugin
     * 
     * @return
     */
    @Override
    public String getVersion()
    {
        return DataAccessPump.PLUGIN_VERSION;
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
        //I18nControl.getInstance().setLanguage(this.selected_lang);
        
        //System.out.println("initPlugIn");
        da_local = DataAccessPump.createInstance(((ATDataAccessLMAbstract)m_da).getLanguageManager());
        //da_local = DataAccessPump.createInstance(da_parent..getL).getInstance();
        
        da_local.loadManager();
        
        ic_local = da_local.getI18nControlInstance();
        da_local.setParentI18nControlInstance(ic);
        
        da_local.addComponent(this.parent);
        da_local.setHelpContext(this.m_da.getHelpContext());
        da_local.setPlugInServerInstance(this);
        da_local.createDb(m_da.getHibernateDb());
        da_local.initAllObjects();
        da_local.loadSpecialParameters();
        da_local.setCurrentUserId(((DataAccess)m_da).current_user_id);
        da_local.setConfigurationManager(((DataAccess)m_da).getConfigurationManager());
        this.backup_restore_enabled = true;
        
        m_da.loadSpecialParameters();
        //System.out.println("PumpServer: " + m_da.getSpecialParameters().get("BG"));
        
        da_local.setBGMeasurmentType(m_da.getIntValueFromString(m_da.getSpecialParameters().get("BG")));
        da_local.setGraphConfigProperties(m_da.getGraphConfigProperties());
    }
    
    
    /**
     * Get Return Object
     * 
     * @param ret_obj_id
     * @return
     */
    @Override
    public Object getReturnObject(int ret_obj_id)
    {
        if (ret_obj_id == PumpPlugInServer.RETURN_OBJECT_DEVICE_WITH_PARAMS)
        {
            //System.out.println("m_da: " + DataAccessPump.getInstance());
            //System.out.println("m_da: getDeviceConfiguration: " + DataAccessPump.getInstance().getDeviceConfiguration());
            //System.out.println("m_da: getSelectedDevice: " + DataAccessPump.getInstance().getDeviceConfiguration().getSelectedDeviceInstance());
            
            
            DeviceConfigEntry de = DataAccessPump.getInstance().getDeviceConfiguration().getSelectedDeviceInstance();
            
            if (de==null)
                return DataAccessPump.getInstance().getI18nControlInstance().getMessage("NO_DEVICE_SELECTED");
            else
            {

                if (de.device_device.equals(DataAccessPump.getInstance().getI18nControlInstance().getMessage("NO_DEVICE_SELECTED")))
                {
                    return DataAccessPump.getInstance().getI18nControlInstance().getMessage("NO_DEVICE_SELECTED");
                }
                else
                {
                    if (m_da.isValueSet(de.communication_port))
                        return String.format(DataAccessPump.getInstance().getI18nControlInstance().getMessage("DEVICE_FULL_NAME_WITH_PORT"), de.device_device, de.communication_port);
                    else
                        return String.format(DataAccessPump.getInstance().getI18nControlInstance().getMessage("DEVICE_FULL_NAME_WITHOUT_PORT"), de.device_company + " " + de.device_device);
                }
            }   
        }
        else
            return null;
    }

    
    
    

    /**
     * Get Backup Objects (if available)
     * 
     * @return
     */
    @Override
    public BackupRestoreCollection getBackupObjects()
    {
        I18nControlAbstract ic_pump = DataAccessPump.getInstance().getI18nControlInstance();
        BackupRestoreCollection brc = new BackupRestoreCollection("PUMP_TOOL", ic_pump);
        brc.addNodeChild(new PumpData(ic_pump));
        brc.addNodeChild(new PumpDataExtended(ic_pump));
        brc.addNodeChild(new PumpProfile(ic_pump));

        return brc;
    }



    /**
     * Get PlugIn Main Menu 
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
        
        JMenu menu_pump = ATSwingUtils.createMenu("MN_PUMPS", null, ic_local);
        
        ATSwingUtils.createMenuItem(menu_pump, 
            "MN_PUMPS_READ_DATA", 
            "MN_PUMPS_READ_DATA_DESC", 
            "pumps_read", 
            this, null, 
            ic_local, DataAccessPump.getInstance(), parent);

        ATSwingUtils.createMenuItem(menu_pump, 
            "MN_PUMPS_READ_CONFIG", 
            "MN_PUMPS_READ_CONFIG_DESC", 
            "pumps_read_config", 
            this, null, 
            ic_local, DataAccessPump.getInstance(), parent);

        menu_pump.addSeparator();
        
        ATSwingUtils.createMenuItem(menu_pump, 
            "MN_PUMPS_MANUAL_ENTRY", 
            "MN_PUMPS_MANUAL_ENTRY_DESC", 
            "pumps_manual_entry", 
            this, null, 
            ic_local, DataAccessPump.getInstance(), parent);

        
        ATSwingUtils.createMenuItem(menu_pump, 
            "MN_PUMPS_ADDITIONAL_DATA", 
            "MN_PUMPS_ADDITIONAL_DATA_DESC", 
            "pumps_additional_data", 
            this, null, 
            ic_local, DataAccessPump.getInstance(), parent);
        

        ATSwingUtils.createMenuItem(menu_pump, 
            "MN_PUMP_PROFILES", 
            "MN_PUMP_PROFILES_DESC", 
            "pumps_profile", 
            this, null, 
            ic_local, DataAccessPump.getInstance(), parent);
        
        menu_pump.addSeparator();

        ATSwingUtils.createMenuItem(menu_pump, 
            "MN_PUMPS_LIST", 
            "MN_PUMPS_LIST_DESC", 
            "pumps_list", 
            this, null, 
            ic_local, DataAccessPump.getInstance(), parent);
        
        menu_pump.addSeparator();
        
        ATSwingUtils.createMenuItem(menu_pump, 
            "MN_PUMPS_CONFIG", 
            "MN_PUMPS_CONFIG_DESC", 
            "pumps_config", 
            this, null, 
            ic_local, DataAccessPump.getInstance(), parent);

        menu_pump.addSeparator();
        
        ATSwingUtils.createMenuItem(menu_pump, 
            "MN_PUMPS_ABOUT", 
            "MN_PUMPS_ABOUT_DESC", 
            "pumps_about", 
            this, null, 
            ic_local, DataAccessPump.getInstance(), parent);
        
        return menu_pump;
    }

    
    /**
     * Get PlugIn Print Menus 
     * 
     * Since printing is also PlugIn specific we need to add Printing jobs to application.
     *  
     * @return
     */
    @Override
    public JMenu[] getPlugInPrintMenus()
    {
        I18nControlAbstract icp = DataAccessPump.getInstance().getI18nControlInstance();

        JMenu menu_reports_pump = ATSwingUtils.createMenu("MN_PUMP", "MN_PUMP_PRINT_DESC", icp);
        
        ATSwingUtils.createMenuItem(menu_reports_pump, 
            "MN_PUMP_PRINT_SIMPLE", 
            "MN_PUMP_PRINT_SIMPLE_DESC", 
            "report_print_pump_simple",
            this, "print.png", 
            icp, DataAccessPump.getInstance(), parent);
        
        
        ATSwingUtils.createMenuItem(menu_reports_pump,
            "MN_PUMP_PRINT_EXT", 
            "MN_PUMP_PRINT_EXT_DESC", 
            "report_print_pump_ext", 
            this, "print.png", 
            icp, DataAccessPump.getInstance(), parent);
        
        
        
        JMenu[] mns = new JMenu[1];
        mns[0] = menu_reports_pump;
        
        return mns;
    }
    
    
    /** 
     * Action Performed
     */
    public void actionPerformed(ActionEvent ae)
    {
        String command = ae.getActionCommand();
        
        if (command.equals("report_print_pump_simple"))
        {
            new PumpPrintDialog((JFrame)parent, PumpPrintDialog.PUMP_REPORT_SIMPLE);
        }
        else if (command.equals("report_print_pump_ext"))
        {
            new PumpPrintDialog((JFrame)parent, PumpPrintDialog.PUMP_REPORT_EXTENDED);
        }
        else if (command.equals("pumps_read"))
        {
            new DeviceInstructionsDialog(this.parent, DataAccessPump.getInstance(), this, DeviceInstructionsDialog.CONTINUING_TYPE_READ_DATA);
            this.client.executeReturnAction(PumpPlugInServer.RETURN_ACTION_READ_DATA);
        }
        else if (command.equals("pumps_read_config"))
        {
            new DeviceInstructionsDialog(this.parent, DataAccessPump.getInstance(), this, DeviceInstructionsDialog.CONTINUING_TYPE_READ_CONFIGURATION);
        }
        else if ((command.equals("pumps_manual_entry")) ||
                 (command.equals("pumps_additional_data")))
        {
            new PumpDataDialog(DataAccessPump.getInstance(), (JFrame)this.parent);
            this.client.executeReturnAction(PumpPlugInServer.RETURN_ACTION_READ_DATA);
        }
        else if (command.equals("pumps_profile"))
        {
            new ProfileSelector(DataAccessPump.getInstance(), this.parent);
        }
        else if (command.equals("pumps_list"))
        {
            new BaseListDialog((JFrame)this.parent, DataAccessPump.getInstance());
        }
        else if (command.equals("pumps_config"))
        {
            new DeviceConfigurationDialog((JFrame)this.parent, DataAccessPump.getInstance());
            this.client.executeReturnAction(PumpPlugInServer.RETURN_ACTION_CONFIG);
        }
        else if (command.equals("pumps_about"))
        {
            new AboutBaseDialog((JFrame)this.parent, DataAccessPump.getInstance());
        }
        else
            System.out.println("PumpPlugInServer::Unknown Command: " + command);

    }
    
    
    /**
     * Get Backup Restore Handler
     * 
     * @return
     */
    public BackupRestorePlugin getBackupRestoreHandler()
    {
        return new BackupRestorePumpHandler();
    }
    
    
}