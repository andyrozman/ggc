package ggc.gui.main.panels;

import ggc.core.plugins.GGCPluginType;
import info.clearthought.layout.TableLayout;

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
 *  Filename:     PlugInsInfoPanel  
 *  Description:  Panel for displaying PlugIns info
 *
 *  Author: andyrozman {andy@atech-software.com}  
 */

public class PlugInsInfoPanel extends AbstractInfoPanel
{

    private static final long serialVersionUID = 6475014158593005970L;

    String notFoundString;


    /**
     * Constructor
     */
    public PlugInsInfoPanel()
    {
        super("PLUGINS");
        init();
        refreshInfo();
    }


    private void init()
    {
        double sizes[][] = { { 10, 0.45, 10, TableLayout.FILL, 10 },
                             { 0.02, 0.19, 0.19, 0.19, 0.19, 0.19, TableLayout.FILL }, }; // 0.19

        initWithTableLayoutAndDisplayPairs(sizes, //
            "NUTRITION_PLUGIN", //
            "METERS_PLUGIN", //
            "PUMPS_PLUGIN", //
            "CGMS_PLUGIN" // , //
        // "CONNECT_PLUGIN"
        );

        notFoundString = m_ic.getMessage("STATUS_NOT_INSTALLED");
    }


    /**
     * Do Refresh - This method can do Refresh
     */
    @Override
    public void doRefresh()
    {
        setPluginStatus(GGCPluginType.MeterToolPlugin, "METERS_PLUGIN");
        setPluginStatus(GGCPluginType.PumpToolPlugin, "PUMPS_PLUGIN");
        setPluginStatus(GGCPluginType.CGMSToolPlugin, "CGMS_PLUGIN");
        setPluginStatus(GGCPluginType.NutritionToolPlugin, "NUTRITION_PLUGIN");
        // setPluginStatus(GGCPluginType.ConnectToolPlugin, "CONNECT_PLUGIN");
    }


    private void setPluginStatus(GGCPluginType pluginType, String i18nKey)
    {
        if (m_da.isPluginAvailable(pluginType))
            this.setValueOnDisplayLabel(i18nKey, m_da.getPlugIn(pluginType).getShortStatus());
        else
            this.setValueOnDisplayLabel(i18nKey, notFoundString);
    }


    @Override
    public InfoPanelType getPanelType()
    {
        return InfoPanelType.Plugins;
    }

}
