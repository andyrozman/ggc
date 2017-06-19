package ggc.core.plugins;

import java.awt.*;

import javax.swing.*;

import com.atech.i18n.I18nControlAbstract;
import com.atech.plugin.PluginActionType;

import ggc.core.data.defs.RefreshInfoType;
import ggc.core.data.defs.ReturnActionType;

/**
 * Application: GGC - GNU Gluco Control
 * See AUTHORS for copyright information.
 * This program is free software; you can redistribute it and/or modify it under
 * the terms of the GNU General Public License as published by the Free Software
 * Foundation; either version 2 of the License, or (at your option) any later
 * version.
 * This program is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU General Public License for more
 * details.
 * You should have received a copy of the GNU General Public License along with
 * this program; if not, write to the Free Software Foundation, Inc., 59 Temple
 * Place, Suite 330, Boston, MA 02111-1307 USA
 * Filename: CGMSPlugIn
 * Description: Class CGMS Plugin Client
 * Author: andyrozman {andy@atech-software.com}
 */

public class CGMSPlugIn extends GGCPluginClient
{

    /**
     * Constructor
     * 
     * @param parent
     * @param ic
     */
    public CGMSPlugIn(Component parent, I18nControlAbstract ic)
    {
        super((JFrame) parent, ic, //
                "CGMS_PLUGIN", //
                "ggc.cgms.plugin.CGMSPlugInServer", //
                "CGMSPlugIn");
    }


    @Override
    public void executeReturnAction(PluginActionType returnActionType)
    {
        ReturnActionType actionType = (ReturnActionType) returnActionType;

        if (actionType == ReturnActionType.ReadData)
        {
            refreshPanels(RefreshInfoType.DeviceDataCGMS);
        }
        else if (actionType == ReturnActionType.ChangeConfig)
        {
            refreshPanels(RefreshInfoType.DevicesConfiguration);
        }
    }

}
