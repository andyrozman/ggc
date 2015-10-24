package ggc.core.plugins;

import java.awt.*;

import javax.swing.*;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.atech.i18n.I18nControlAbstract;
import com.atech.utils.ATDataAccessLMAbstract;
import ggc.core.util.RefreshInfo;

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
 *  Filename:     PumpsPlugIn
 *  Description:  Pump Plugin Client (communicates with Server part and sends commands
 *      to plugin)
 *
 *  Author: andyrozman {andy@atech-software.com}
 */

public class PumpsPlugIn extends GGCPluginClient
{

    private static final Logger LOG = LoggerFactory.getLogger(PumpsPlugIn.class);

    /**
     * Return Object: Selected Device with parameters
     */
    // public static final int RETURN_OBJECT_DEVICE_WITH_PARAMS = 1;

    /**
     * This is action that needs to be done, after read data.
     */
    public static final int RETURN_ACTION_READ_DATA = 1;

    /**
     * This is action that needs to be done, after config
     */
    public static final int RETURN_ACTION_CONFIG = 2;

    private static final String PLUGIN_SERVER_CLASS_NAME = "ggc.pump.plugin.PumpPlugInServer";
    private static final String PLUGIN_SERVER_SHORT_NAME = "PumpsPlugIn";


    /**
     * Constructor
     * 
     * @param parent
     * @param ic
     */
    public PumpsPlugIn(Component parent, I18nControlAbstract ic)
    {
        super((JFrame) parent, ic);
    }


    /**
     * Constructor
     * 
     * @param parent
     * @param da
     */
    public PumpsPlugIn(Component parent, ATDataAccessLMAbstract da)
    {
        super((JFrame) parent, da);
    }


    /**
     * Constructor
     */
    public PumpsPlugIn()
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
        return "PUMPS_PLUGIN";
    }


    /**
     * This is method which can be used by server side to do certain action. Mainly this will be used
     * to run refreshes and such actions. This needs to be implemented by Client side, if you wish to
     * use it.
     * 
     * @param action_type
     */
    @Override
    public void executeReturnAction(int action_type)
    {
        if (action_type == PumpsPlugIn.RETURN_ACTION_READ_DATA)
        {
            refreshPanels(RefreshInfo.PANEL_GROUP_ALL_DATA);
        }
        else if (action_type == PumpsPlugIn.RETURN_ACTION_CONFIG)
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
