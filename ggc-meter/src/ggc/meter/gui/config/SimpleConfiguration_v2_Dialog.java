/*
 *  GGC - GNU Gluco Control
 *
 *  A pure java app to help you manage your diabetes.
 *
 *  See AUTHORS for copyright information.
 *
 *  This program is free software; you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation; either version 2 of the License, or
 *  (at your option) any later version.
 *
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with this program; if not, write to the Free Software
 *  Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA
 *
 *  Filename: PropertiesDialog
 *
 *  Purpose:  Dialog for setting properties for application.
 *
 *  Author:   andyrozman {andy@atech-software.com}
 *
 */
package ggc.meter.gui.config;


import ggc.meter.util.DataAccessMeter;
import ggc.plugin.cfg.DeviceConfigurationDialog;

import javax.swing.JDialog;


public class SimpleConfiguration_v2_Dialog //extends JDialog implements ActionListener, ChangeListener, ItemListener //, HelpCapable
{

    
    
    public static void main(String[] args)
    {
        DataAccessMeter da = DataAccessMeter.getInstance();
        
        JDialog d1 = new JDialog();
        da.addComponent(d1);
        
        new DeviceConfigurationDialog(d1, da);
        /*SimpleConfiguration_v2_Dialog d =*/ //new SimpleConfiguration_v2_Dialog(d1, da);
        
    }

    
}
