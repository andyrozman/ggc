package ggc.core.plugins;

import ggc.core.util.RefreshInfo;

import java.awt.Component;

import javax.swing.JFrame;

import com.atech.graphics.components.StatusReporterInterface;
import com.atech.i18n.I18nControlAbstract;

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
 *  Filename:     MetersPlugIn  
 *  Description:  Class Meter Plugin Client
 * 
 *  Author: andyrozman {andy@atech-software.com}  
 */

public class MetersPlugIn extends GGCPluginClient
{

    /**
     * This is action that needs to be done, after read data.
     */
    public static final int RETURN_ACTION_READ_DATA = 1;

    /**
     * This is action that needs to be done, after config
     */
    public static final int RETURN_ACTION_CONFIG = 2;


    private static final String PLUGIN_SERVER_CLASS_NAME = "ggc.meter.plugin.MeterPlugInServer";

    private static final String PLUGIN_SERVER_SHORT_NAME = "MetersPlugIn";


    /**
     * Constructor
     * 
     * @param parent
     * @param ic
     */
    public MetersPlugIn(Component parent, I18nControlAbstract ic)
    {
        super((JFrame) parent, ic);
    }


    /**
     * Constructor
     */
    public MetersPlugIn()
    {
        super();
    }


    /**
     * Get Name Base (untranslated)
     * 
     * @return name of plugin
     */
    @Override
    public String getNameBase()
    {
        return "METERS_PLUGIN";
    }


    /**
     * Set Return Data (for getting data from plugin - async)
     * 
     * @param return_data
     * @param stat_rep_int
     */
    @Override
    public void setReturnData(Object return_data, StatusReporterInterface stat_rep_int)
    {
        GGCDataWriter gdw = new GGCDataWriter(GGCDataWriter.DATA_METER, return_data, stat_rep_int);
        gdw.start();
    }


    /**
     * This is method which can be used by server side to do certain action. Mainly this will be used
     * to run refreshes and such actions. This needs to be implemented by Client side, if you wish to use
     * it.
     * 
     * @param action_type
     */
    @Override
    public void executeReturnAction(int action_type)
    {
        if (action_type == MetersPlugIn.RETURN_ACTION_READ_DATA)
        {
            refreshPanels(RefreshInfo.PANEL_GROUP_ALL_DATA);
        }
        else if (action_type == MetersPlugIn.RETURN_ACTION_CONFIG)
        {
            refreshPanels(RefreshInfo.PANEL_GROUP_PLUGINS_DEVICES);
        }
    }


    public String getServerClassName()
    {
        return PLUGIN_SERVER_CLASS_NAME;
    }


    public String getServerShortName()
    {
        return PLUGIN_SERVER_SHORT_NAME;
    }

}
