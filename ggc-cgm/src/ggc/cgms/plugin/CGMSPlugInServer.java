package ggc.cgms.plugin;

import ggc.cgms.data.db.CGMSData;
import ggc.cgms.data.db.CGMSDataExtended;
import ggc.cgms.gui.viewer.CGMSDataDialog;
import ggc.cgms.util.DataAccessCGMS;
import ggc.core.util.DataAccess;
import ggc.plugin.DevicePlugInServer;
import ggc.plugin.cfg.DeviceConfigEntry;
import ggc.plugin.cfg.DeviceConfigurationDialog;
import ggc.plugin.data.DeviceDataHandler;
import ggc.plugin.device.DownloadSupportType;
import ggc.plugin.gui.AboutBaseDialog;
import ggc.plugin.gui.DeviceInstructionsDialog;
import ggc.plugin.list.BaseListDialog;

import java.awt.Container;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuItem;

import com.atech.db.hibernate.transfer.BackupRestoreCollection;
import com.atech.i18n.I18nControlAbstract;
import com.atech.plugin.BackupRestorePlugin;
import com.atech.utils.ATDataAccessLMAbstract;
import com.atech.utils.ATSwingUtils;

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

    //private String cgm_tool_version = "0.1.1";
    
    //I18nControlAbstract ic_local = null;
    
    /**
     *  Command: Read CGMS data 
     */
    public static final int COMMAND_READ_CGMS_DATA = 0;
    
    /**
     *  Command: Get List of devices
     */
    public static final int COMMAND_CGMS_LIST = 1;
    
    /**
     *  Command: Configuration 
     */
    public static final int COMMAND_CGMS_CONFIGURATION = 2;
    
    /**
     *  Command: About  
     */
    public static final int COMMAND_CGMS_ABOUT = 3;
    
    
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
    
    
    
    private String commands[] = {
                                "MN_CGMS_READ_DESC",
                                "MN_CGMS_LIST_DESC",
                                "MN_CGMS_CONFIG_DESC",
                                "MN_CGMS_ABOUT_DESC"
    };
    
    DataAccessCGMS da_local;
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
     * @param selected_lang
     * @param da
     */
    public CGMSPlugInServer(Container cont, ATDataAccessLMAbstract da)
    {
        super(cont, da);
        
        da_local = DataAccessCGMS.createInstance(da.getLanguageManager());
        da_local.addComponent(cont);
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
            case CGMSPlugInServer.COMMAND_READ_CGMS_DATA:
            {
                this.featureNotImplemented(commands[CGMSPlugInServer.COMMAND_READ_CGMS_DATA]);
//                DbDataReaderAbstract reader = (DbDataReaderAbstract)obj_data; 
                //new MeterInstructionsDialog(reader, this);
                return;
            }

            case CGMSPlugInServer.COMMAND_CGMS_LIST:
            {
                this.featureNotImplemented(commands[CGMSPlugInServer.COMMAND_CGMS_LIST]);
                return;
            }

            case CGMSPlugInServer.COMMAND_CGMS_ABOUT:
            {
                this.featureNotImplemented(commands[CGMSPlugInServer.COMMAND_CGMS_ABOUT]);
                return;
            }
            
            
            
            default:
            case CGMSPlugInServer.COMMAND_CGMS_CONFIGURATION:
            {
                this.featureNotImplemented(commands[CGMSPlugInServer.COMMAND_CGMS_CONFIGURATION]);
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
        return ic.getMessage("CGMS_PLUGIN");
    }

    /**
     * Get Version of plugin
     * 
     * @return
     */
    @Override
    public String getVersion()
    {
        return DataAccessCGMS.PLUGIN_VERSION;
    }

    /**
     * Get Information When will it be implemented
     * 
     * @return
     */
    @Override
    public String getWhenWillBeImplemented()
    {
        return "0.6";
    }

    /**
     * Init PlugIn which needs to be implemented 
     */
    @Override
    public void initPlugIn()
    {
        ic = m_da.getI18nControlInstance();
        //I18nControl.getInstance().setLanguage(this.selected_lang);
        
        if (da_local==null)
            da_local = DataAccessCGMS.createInstance(((ATDataAccessLMAbstract)m_da).getLanguageManager());
       
        
        this.initPlugInServer((DataAccess)m_da, da_local);
        
        /*
        da_local.loadManager();
        
        ic_local = da_local.getI18nControlInstance();
        
        da_local.addComponent(this.parent);
        da_local.setParentI18nControlInstance(ic);
        
        da_local.setHelpContext(this.m_da.getHelpContext());
        da_local.setPlugInServerInstance(this);
        da_local.createDb(m_da.getHibernateDb());
        //da_local.initAllObjects();
        da_local.loadSpecialParameters();
        da_local.setCurrentUserId(((DataAccess)m_da).current_user_id);
        da_local.setConfigurationManager(((DataAccess)m_da).getConfigurationManager());
        //this.backup_restore_enabled = true;
        this.backup_restore_enabled = false;
        
        m_da.loadSpecialParameters();
        //System.out.println("PumpServer: " + m_da.getSpecialParameters().get("BG"));
        
        da_local.setBGMeasurmentType(m_da.getIntValueFromString(m_da.getSpecialParameters().get("BG")));
        da_local.setGraphConfigProperties(m_da.getGraphConfigProperties());
        */
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
        
        if (ret_obj_id == CGMSPlugInServer.RETURN_OBJECT_DEVICE_WITH_PARAMS)
        {
            DataAccessCGMS da = DataAccessCGMS.getInstance();
            DeviceConfigEntry de = da.getDeviceConfiguration().getSelectedDeviceInstance();
            
            if (de==null)
                return da.getI18nControlInstance().getMessage("NO_DEVICE_SELECTED");
            else
            {

                if (de.device_device.equals(da.getI18nControlInstance().getMessage("NO_DEVICE_SELECTED")))
                {
                    return da.getI18nControlInstance().getMessage("NO_DEVICE_SELECTED");
                }
                else
                {
                    if (m_da.isValueSet(de.communication_port))
                        return String.format(da.getI18nControlInstance().getMessage("DEVICE_FULL_NAME_WITH_PORT"), de.device_device + " [" + de.device_company + "]", de.communication_port);
                    else
                        return String.format(da.getI18nControlInstance().getMessage("DEVICE_FULL_NAME_WITHOUT_PORT"), de.device_device + " [" + de.device_company + "]");
                }
            }   
        }
        else
            return null;
        
    }

    
    /**
     * Get Return Object
     * 
     * @param ret_obj_id
     * @param parameters
     * @return
     */
    @Override
    public Object getReturnObject(int ret_obj_id, Object[] parameters)
    {
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
        I18nControlAbstract ic_pump = DataAccessCGMS.getInstance().getI18nControlInstance();
        BackupRestoreCollection brc = new BackupRestoreCollection("CGMS_TOOL", ic_pump);
        brc.addNodeChild(new CGMSData(ic_pump));
        brc.addNodeChild(new CGMSDataExtended(ic_pump));

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
        
        JMenu menu_cgms = ATSwingUtils.createMenu("MN_CGMS", null, ic_local);

        
        JMenuItem menu = ATSwingUtils.createMenuItem(menu_cgms, 
            "MN_CGMS_READ", 
            "MN_CGMS_READ_DESC", 
            "cgms_read", 
            this, null, 
            ic_local, DataAccessCGMS.getInstance(), parent);

        
        if ((da_local.getDownloadStatus() & DownloadSupportType.DOWNLOAD_FROM_DEVICE) == DownloadSupportType.DOWNLOAD_FROM_DEVICE)
            menu.setEnabled(true);
        else
            menu.setEnabled(false);
        
        menus[0] = menu;
        
        menu = ATSwingUtils.createMenuItem(menu_cgms, 
            "MN_CGMS_READ_FILE", 
            "MN_CGMS_READ_FILE_DESC", 
            "cgms_read_file", 
            this, null, 
            ic_local, DataAccessCGMS.getInstance(), parent);

        if ((da_local.getDownloadStatus() & DownloadSupportType.DOWNLOAD_FROM_DEVICE_FILE) == DownloadSupportType.DOWNLOAD_FROM_DEVICE_FILE)
            menu.setEnabled(true);
        else
            menu.setEnabled(false);
        menus[1] = menu;
        
        
        menu_cgms.addSeparator();

        ATSwingUtils.createMenuItem(menu_cgms, 
            "MN_CGMS_LIST", 
            "MN_CGMS_LIST_DESC", 
            "cgms_list", 
            this, null, 
            ic_local, DataAccessCGMS.getInstance(), parent);

        menu_cgms.addSeparator();

        ATSwingUtils.createMenuItem(menu_cgms, 
            "MN_CGMS_VIEW_DATA", 
            "MN_CGMS_VIEW_DATA_DESC", 
            "cgms_view_data", 
            this, null, 
            ic_local, DataAccessCGMS.getInstance(), parent);
        
        menu_cgms.addSeparator();
        
        ATSwingUtils.createMenuItem(menu_cgms, 
            "MN_CGMS_CONFIG", 
            "MN_CGMS_CONFIG_DESC", 
            "cgms_config", 
            this, null, 
            ic_local, DataAccessCGMS.getInstance(), parent);
        
        
        menu_cgms.addSeparator();
        
        ATSwingUtils.createMenuItem(menu_cgms, 
            "MN_CGMS_ABOUT", 
            "MN_CGMS_ABOUT_DESC", 
            "cgms_about", 
            this, null, 
            ic_local, DataAccessCGMS.getInstance(), parent);
        
        
        return menu_cgms;
    }

    
    private void refreshMenusAfterConfig()
    {
        if ((da_local.getDownloadStatus() & DownloadSupportType.DOWNLOAD_FROM_DEVICE) == DownloadSupportType.DOWNLOAD_FROM_DEVICE)
            menus[0].setEnabled(true);
        else
            menus[0].setEnabled(false);
        
        
        if ((da_local.getDownloadStatus() & DownloadSupportType.DOWNLOAD_FROM_DEVICE_FILE) == DownloadSupportType.DOWNLOAD_FROM_DEVICE_FILE)
            menus[1].setEnabled(true);
        else
            menus[1].setEnabled(false);
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
        // TODO Auto-generated method stub
        return null;
    }


    /** 
     * actionPerformed
     */
    @Override
    public void actionPerformed(ActionEvent ae)
    {
        String command = ae.getActionCommand();
        
        if (command.equals("cgms_read"))
        {
            new DeviceInstructionsDialog(this.parent, DataAccessCGMS.getInstance(), this, DeviceDataHandler.TRANSFER_READ_DATA);
            this.client.executeReturnAction(CGMSPlugInServer.RETURN_ACTION_READ_DATA);
        }
        else if (command.equals("cgms_read_file"))
        {
            new DeviceInstructionsDialog(this.parent, DataAccessCGMS.getInstance(), this, DeviceDataHandler.TRANSFER_READ_FILE);
            this.client.executeReturnAction(CGMSPlugInServer.RETURN_ACTION_READ_DATA);
        }
        else if (command.equals("cgms_list"))
        {
            new BaseListDialog((JFrame)this.parent, DataAccessCGMS.getInstance());
        }
        else if (command.equals("cgms_config"))
        {
            new DeviceConfigurationDialog((JFrame)this.parent, DataAccessCGMS.getInstance());
            refreshMenusAfterConfig();
            this.client.executeReturnAction(CGMSPlugInServer.RETURN_ACTION_CONFIG);
        }
        else if (command.equals("cgms_about"))
        {
            new AboutBaseDialog((JFrame)this.parent, DataAccessCGMS.getInstance());
        }
        else if (command.equals("cgms_view_data"))  
        {
            new CGMSDataDialog(DataAccessCGMS.getInstance(), (JFrame)this.parent);
        }
        else
            System.out.println("CGMSPluginServer::Unknown Command: " + command);
        
    }
    
    
    
    /**
     * Get Backup Restore Handler
     * 
     * @return
     */
    public BackupRestorePlugin getBackupRestoreHandler()
    {
        return new BackupRestoreCGMSHandler();
    }
    
}


