package ggc.meter.gui.config;

import javax.swing.*;

import ggc.meter.util.DataAccessMeter;
import ggc.plugin.cfg.DeviceConfigurationDialog;

/**
 *  Application:   GGC - GNU Gluco Control
 *  Plug-in:       Meter Tool (support for Meter devices)
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
 *  Filename:     MeterDevicesIds
 *  Description:  This class contains all ids from this plugins. This means companies and meter devices,
 *                all classes should use Ids from here, so that Ids can change globally.
 *
 *  Author: Andy {andy@atech-software.com}
 */

public class SimpleConfiguration_v2_Dialog // extends JDialog implements
                                           // ActionListener, ChangeListener,
                                           // ItemListener //, HelpCapable
{

    /**
     * @param args
     */
    public static void main(String[] args)
    {
        DataAccessMeter da = DataAccessMeter.getInstance();

        JFrame d1 = new JFrame();
        da.addComponent(d1);

        new DeviceConfigurationDialog(d1, da);
        /* SimpleConfiguration_v2_Dialog d = */// new
                                               // SimpleConfiguration_v2_Dialog(d1,
                                               // da);

    }

}
