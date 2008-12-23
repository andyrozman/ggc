package ggc.gui.little.panels;

import ggc.core.util.I18nControl;
import ggc.gui.panels.info.AbstractInfoPanel;

import java.awt.GridLayout;

import javax.swing.JLabel;

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
 *  Filename:     PlugInMeterPanelL  
 *  Description:  Panel for Meter Plugin
 *
 *  Author: andyrozman {andy@atech-software.com}  
 */


public class PlugInMeterPanelL extends AbstractInfoPanel
{
    
    private static final long serialVersionUID = 8602621885397419968L;
    
    
    /**
     * Constructor
     */
    public PlugInMeterPanelL()
    {
        super(I18nControl.getInstance().getMessage("METERS_PLUGIN"));
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
    
    
    /**
     * Get Tab Name
     * 
     * @return name as string
     */
    public String getTabName()
    {
        return "PlugInMeterInfo";
    }

    
    /**
     * Do Refresh - This method can do Refresh
     */
    public void doRefresh()
    {
    }
    
    
}
