package ggc.plugin;

import ggc.core.util.DataAccess;
import ggc.plugin.util.DataAccessPlugInBase;

import java.awt.Container;

import com.atech.i18n.I18nControlAbstract;
import com.atech.plugin.PlugInServer;
import com.atech.utils.ATDataAccessAbstract;
import com.atech.utils.ATDataAccessLMAbstract;

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


public abstract class DevicePlugInServer extends PlugInServer
{
    
    protected I18nControlAbstract ic_local = null;

    /**
     * Constructor
     */
    public DevicePlugInServer()
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
    public DevicePlugInServer(Container cont, String selected_lang, ATDataAccessAbstract da)
    {
        super(cont, selected_lang, da);
    }
    
    /**
     * Constructor
     * 
     * @param cont
     * @param selected_lang
     * @param da
     */
    public DevicePlugInServer(Container cont, ATDataAccessLMAbstract da)
    {
        super(cont, da);
    }

    
    
    /**
     * Init PlugIn Server
     * 
     * @param da_ggc_core
     * @param da_plugin
     */
    public void initPlugInServer(DataAccess da_ggc_core, DataAccessPlugInBase da_plugin)
    {
        
        da_plugin.loadManager();
        
        ic_local = da_plugin.getI18nControlInstance();
        da_plugin.setParentI18nControlInstance(ic);
        
        
        //System.out.println(da_local.getI18nControlInstance().toString());
        
        
        da_plugin.addComponent(this.parent);
        da_plugin.setHelpContext(this.m_da.getHelpContext());
        da_plugin.setPlugInServerInstance(this);
        da_plugin.createDb(m_da.getHibernateDb());
        da_plugin.initAllObjects();
        da_plugin.loadSpecialParameters();
        da_plugin.setCurrentUserId(da_ggc_core.current_user_id);
        da_plugin.setConfigurationManager(((DataAccess)m_da).getConfigurationManager());
        this.backup_restore_enabled = true;
        
        da_ggc_core.loadSpecialParameters();
        //System.out.println("PumpServer: " + m_da.getSpecialParameters().get("BG"));
        
        da_plugin.setBGMeasurmentType(m_da.getIntValueFromString(da_ggc_core.getSpecialParameters().get("BG")));
        da_plugin.setGraphConfigProperties(da_ggc_core.getGraphConfigProperties());
        
        
        
    }
    
    
    
    
}


