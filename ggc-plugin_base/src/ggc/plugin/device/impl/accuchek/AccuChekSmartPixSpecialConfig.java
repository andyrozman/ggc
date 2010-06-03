package ggc.plugin.device.impl.accuchek;

import ggc.plugin.gui.DeviceSpecialConfigPanelAbstract;

/**
 *  Application:   GGC - GNU Gluco Control
 *  Plug-in:       GGC PlugIn Base (base class for all plugins)
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
 *  Filename:     ----  
 *  Description:  ----
 * 
 *  Author: Andy {andy@atech-software.com}
 */


public class AccuChekSmartPixSpecialConfig extends DeviceSpecialConfigPanelAbstract 
{

    /**
     * SmartPix Version 2.x
     */
    public static final String SMARTPIX_V2 = "2.x";    

    /**
     * SmartPix Version 3.x
     */
    public static final String SMARTPIX_V3 = "3.x";    
    
    
    
    public void initPanel()
    {
    }

    

    public int getHeight()
    {
        return 0;
    }

    
    public void readParametersFromGUI()
    {
    }

    
    public void initParameters()
    {
        if (this.parameters.containsKey("SMARTPIX_VERSION"))
        {
            this.parameters.remove("SMARTPIX_VERSION");
            this.parameters.put("SMARTPIX_VERSION", "3.x");
        }
        else
            this.parameters.put("SMARTPIX_VERSION", "3.x");
    }

    
    public void loadParametersToGUI()
    {
    }

    
    
}
