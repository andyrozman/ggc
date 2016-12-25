package ggc.gui.little.panels;

import java.awt.*;

import javax.swing.*;

import ggc.gui.main.panels.AbstractInfoPanel;
import ggc.gui.main.panels.InfoPanelType;

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
 *  Filename:     PlugInPumpPanelL  
 *  Description:  Panel for Pump Plugin
 *
 *  Author: andyrozman {andy@atech-software.com}  
 */

public class PlugInPumpPanelL extends AbstractInfoPanel
{

    private static final long serialVersionUID = 2496714983251707250L;


    // private I18nControl i18nControlAbstract = I18nControl.getInstance();

    /**
     * Constructor
     */
    public PlugInPumpPanelL()
    {
        super("PUMPS_PLUGIN");
        setLayout(new GridLayout(0, 1));
        init();
        refreshInfo();
    }


    private void init()
    {
        String text = "<html><body>";
        text += String.format(m_ic.getMessage("PLUGIN_IMPLEMENTED_VERSION"), "0.5");
        text += "</body></html>";

        add(new JLabel(text));
    }


    /**
     * Refresh Information 
     */
    @Override
    public void refreshInfo()
    {
    }


    @Override
    public InfoPanelType getPanelType()
    {
        return InfoPanelType.PluginPump;
    }


    /**
     * Do Refresh - This method can do Refresh
     */
    @Override
    public void doRefresh()
    {
    }

}
