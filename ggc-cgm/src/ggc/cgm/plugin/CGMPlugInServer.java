package ggc.cgm.plugin;

import ggc.cgm.util.DataAccessCGM;
import ggc.cgm.util.I18nControl;

import java.awt.Container;

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
 *  Filename:  ###---###  
 *  Description:
 * 
 *  Author: Andy {andy@atech-software.com}
 */


public class CGMPlugInServer extends PlugInServer
{
    /**
     * Version of Meter Tool
     */
    private String cgm_tool_version = "0.0.1";
    
    public static final int COMMAND_READ_CGMS_DATA = 0;
    public static final int COMMAND_CGMS_LIST = 1;
    public static final int COMMAND_CGMS_CONFIGURATION = 2;
    public static final int COMMAND_CGMS_ABOUT = 3;
    
    
    public String commands[] = {
                                "MN_CGMS_READ_DESC",
                                "MN_CGMS_LIST_DESC",
                                "MN_CGMS_CONFIG_DESC",
                                "MN_CGMS_ABOUT_DESC"
    };
    
    
    
    public CGMPlugInServer()
    {
        super();
    }
    
    
    public CGMPlugInServer(Container cont, String selected_lang, ATDataAccessAbstract da)
    {
        super(cont, selected_lang, da);
        DataAccessCGM.getInstance().addComponent(cont);
    }
    

    
    
    
    /* 
     * executeCommand
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

    /* 
     * getName
     */
    @Override
    public String getName()
    {
        return ic.getMessage("CGMS_PLUGIN");
    }

    /* 
     * getVersion
     */
    @Override
    public String getVersion()
    {
        return this.cgm_tool_version;
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
        DataAccessCGM.getInstance().addComponent(this.parent);
    }
    
    
    
    
}


