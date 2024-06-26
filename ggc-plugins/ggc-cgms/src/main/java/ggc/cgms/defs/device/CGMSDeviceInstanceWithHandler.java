package ggc.cgms.defs.device;

import ggc.cgms.device.CGMSInterfaceV2;
import ggc.cgms.util.DataAccessCGMS;
import ggc.plugin.device.v2.DeviceInstanceWithHandler;

/**
 *  Application: GGC - GNU Gluco Control
 *  Plug-in: CGMS Tool (support for CGMS devices)
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
 *  Filename: CGMSDeviceInstanceWithHandler
 *  Description: CGMS Device Instance With Handler (CGMS specific)
 *
 *  Author: Andy {andy@atech-software.com}
 */

public class CGMSDeviceInstanceWithHandler extends DeviceInstanceWithHandler implements CGMSInterfaceV2
{

    public CGMSDeviceInstanceWithHandler(CGMSDeviceDefinition deviceDefinition)
    {
        super(deviceDefinition, DataAccessCGMS.getInstance());
    }

}
