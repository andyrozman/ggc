package ggc.plugin.gui;

import javax.swing.*;

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
 *  Filename:     DeviceSpecialConfigPanelInterface
 *  Description:  
 * 
 *  Author: Andy {andy@atech-software.com}
 */

public interface DeviceSpecialConfigPanelInterface
{

    /**
     * Init Panel
     */
    void initPanel();


    /**
     * Get Panel
     * 
     * @return
     */
    JPanel getPanel();


    /**
     * Get Height
     * 
     * @return
     */
    int getHeight();


    /**
     * Init Parameters
     */
    void initParameters();


    /**
     * Has Default Parameter
     * 
     * @return
     */
    boolean hasDefaultParameter();


    /**
     * Are Connection Parameters Valid
     * 
     * @return
     */
    boolean areConnectionParametersValid();


    /**
     * Load Connection Parameters
     * 
     * @param param
     */
    void loadConnectionParameters(String param);


    /**
     * Save Connection Parameters
     * 
     * @return
     */
    String saveConnectionParameters();


    /**
     * Load Parameters To GUI
     */
    void loadParametersToGUI();


    /**
     * Read Parameters From GUI
     */
    void readParametersFromGUI();


    /**
     * Get Default Parameter
     * 
     * @return
     */
    String getDefaultParameter();


    /**
     * Set Default Parameter
     * 
     * @param par
     */
    void setDefaultParameter(String par);


    /**
     * Get Parameter
     * 
     * @param key
     * @return
     */
    String getParameter(String key);


    /**
     * get Custom Error Message
     * @return
     */
    String getCustomErrorMessage();

}
