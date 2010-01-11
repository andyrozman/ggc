package ggc.cgms.plugin;

import ggc.cgms.util.DataAccessCGMS;
import ggc.core.util.DataAccess;
import ggc.plugin.cfg.DeviceConfigurationDialog;
import ggc.plugin.gui.AboutBaseDialog;
import ggc.plugin.list.BaseListDialog;

import java.awt.Container;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JFrame;
import javax.swing.JMenu;

import com.atech.db.hibernate.transfer.BackupRestoreCollection;
import com.atech.i18n.I18nControlAbstract;
import com.atech.plugin.PlugInServer;
import com.atech.utils.ATDataAccessAbstract;
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


public class CGMSPlugInServer extends PlugInServer implements ActionListener
{

    private String cgm_tool_version = "0.1.1";
    
    I18nControlAbstract ic_local = null;
    
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
    
    
    private String commands[] = {
                                "MN_CGMS_READ_DESC",
                                "MN_CGMS_LIST_DESC",
                                "MN_CGMS_CONFIG_DESC",
                                "MN_CGMS_ABOUT_DESC"
    };
    
    
    
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
    public CGMSPlugInServer(Container cont, String selected_lang, ATDataAccessAbstract da)
    {
        super(cont, selected_lang, da);
        DataAccessCGMS.getInstance().addComponent(cont);
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
        return this.cgm_tool_version;
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
        DataAccessCGMS da_local = DataAccessCGMS.createInstance(((ATDataAccessLMAbstract)m_da).getLanguageManager());
        
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
        // TODO Auto-generated method stub
        return null;
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
        
        ATSwingUtils.createMenuItem(menu_cgms, 
            "MN_CGMS_READ", 
            "MN_CGMS_READ_DESC", 
            "cgms_read", 
            this, null, 
            ic_local, DataAccessCGMS.getInstance(), parent);
        
        menu_cgms.addSeparator();

        ATSwingUtils.createMenuItem(menu_cgms, 
            "MN_CGMS_LIST", 
            "MN_CGMS_LIST_DESC", 
            "cgms_list", 
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
        // TODO Auto-generated method stub
        String command = ae.getActionCommand();
        
        if (command.equals("cgms_list"))
        {
            new BaseListDialog((JFrame)this.parent, DataAccessCGMS.getInstance());
        }
        else if (command.equals("cgms_config"))
        {
            new DeviceConfigurationDialog((JFrame)this.parent, DataAccessCGMS.getInstance());
//            this.client.executeReturnAction(CGMSPlugInServer.RETURN_ACTION_CONFIG);
        }
        else if (command.equals("cgms_about"))
        {
            new AboutBaseDialog((JFrame)this.parent, DataAccessCGMS.getInstance());
        }
        else
            System.out.println("CGMSPluginServer::Unknown Command: " + command);
        
    }
    
    
}


