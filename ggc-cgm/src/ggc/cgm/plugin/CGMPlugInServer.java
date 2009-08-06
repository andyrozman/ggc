package ggc.cgm.plugin;

import ggc.cgm.util.DataAccessCGM;
import ggc.cgm.util.I18nControl;

import java.awt.Container;
import java.awt.event.ActionEvent;

import javax.swing.JMenu;

import com.atech.db.hibernate.transfer.BackupRestoreCollection;
import com.atech.plugin.PlugInServer;
import com.atech.utils.ATDataAccessAbstract;

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


public class CGMPlugInServer extends PlugInServer
{
    private String cgm_tool_version = "0.0.1";
    
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
    public CGMPlugInServer()
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
    public CGMPlugInServer(Container cont, String selected_lang, ATDataAccessAbstract da)
    {
        super(cont, selected_lang, da);
        DataAccessCGM.getInstance().addComponent(cont);
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
            case CGMPlugInServer.COMMAND_READ_CGMS_DATA:
            {
                this.featureNotImplemented(commands[CGMPlugInServer.COMMAND_READ_CGMS_DATA]);
//                DbDataReaderAbstract reader = (DbDataReaderAbstract)obj_data; 
                //new MeterInstructionsDialog(reader, this);
                return;
            }

            case CGMPlugInServer.COMMAND_CGMS_LIST:
            {
                this.featureNotImplemented(commands[CGMPlugInServer.COMMAND_CGMS_LIST]);
                return;
            }

            case CGMPlugInServer.COMMAND_CGMS_ABOUT:
            {
                this.featureNotImplemented(commands[CGMPlugInServer.COMMAND_CGMS_ABOUT]);
                return;
            }
            
            
            
            default:
            case CGMPlugInServer.COMMAND_CGMS_CONFIGURATION:
            {
                this.featureNotImplemented(commands[CGMPlugInServer.COMMAND_CGMS_CONFIGURATION]);
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
        DataAccessCGM.getInstance().addComponent(this.parent);
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
        // TODO Auto-generated method stub
        return null;
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
        
    }
    
    
}


