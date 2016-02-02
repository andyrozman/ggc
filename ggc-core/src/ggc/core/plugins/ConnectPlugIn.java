package ggc.core.plugins;

import java.awt.*;

import javax.swing.*;

import com.atech.i18n.I18nControlAbstract;

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
 * Filename: PumpsPlugIn
 * Description: Class Pump Plugin Client
 * Author: andyrozman {andy@atech-software.com}
 */

public class ConnectPlugIn extends GGCPluginClient
{

    private static final String PLUGIN_SERVER_CLASS_NAME = "ggc.connect.plugin.ConnectPlugInServer";

    private static final String PLUGIN_SERVER_SHORT_NAME = "ConnectPlugIn";

    /**
     * Return Object: Selected Device with parameters
     */
    public static final int RETURN_OBJECT_DEVICE_WITH_PARAMS = 1;


    /**
     * Constructor
     *
     * @param parent
     * @param ic
     */
    public ConnectPlugIn(Component parent, I18nControlAbstract ic)
    {
        super((JFrame) parent, ic);
    }


    /**
     * Constructor
     */
    public ConnectPlugIn()
    {
        super();
    }


    /**
     * Check If Installed
     */
    // @Override
    // public void checkIfInstalled()
    // {
    // try
    // {
    // Class<?> c = Class.forName("ggc.nutri.plugin.NutriPlugInServer");
    //
    // this.m_server = (PlugInServer) c.newInstance();
    // installed = true;
    //
    // this.m_server.init(this.parent,
    // DataAccess.getInstance().getI18nControlInstance().getSelectedLanguage(),
    // DataAccess.getInstance(), this, DataAccess.getInstance().getDb());
    //
    // // System.out.println("Class done");
    // }
    // catch (Exception ex)
    // {
    // this.installed = false;
    // // System.out.println("Ex:" + ex);
    // }
    // }

    /**
     * Get Name Base (untranslated)
     * 
     * @return name of plugin
     */
    @Override
    public String getNameBase()
    {
        return "CONNECT_PLUGIN";
    }

    // /**
    // * Get Short Status
    // *
    // * @return
    // */
    // @Override
    // public String getShortStatus()
    // {
    // if (this.m_server != null)
    // return String.format(i18nControl.getMessage("STATUS_INSTALLED"),
    // this.m_server.getVersion());
    // else
    // return i18nControl.getMessage("STATUS_NOT_INSTALLED");
    // }


    // /**
    // * Set Return Data (for getting data from plugin - async)
    // *
    // * @param return_data
    // * @param stat_rep_int
    // */
    // @Override
    // public void setReturnData(Object return_data, StatusReporterInterface
    // stat_rep_int)
    // {
    // }

    public String getServerClassName()
    {
        return PLUGIN_SERVER_CLASS_NAME;
    }


    public String getServerShortName()
    {
        return PLUGIN_SERVER_SHORT_NAME;
    }

}
