package ggc.pump.device.animas;

import ggc.plugin.device.DownloadSupportType;
import ggc.plugin.device.impl.animas.enums.AnimasDeviceType;
import ggc.plugin.manager.DeviceImplementationStatus;
import ggc.plugin.manager.company.AbstractDeviceCompany;
import ggc.plugin.output.OutputWriter;
import ggc.pump.data.defs.PumpDeviceDefinition;
import ggc.pump.manager.PumpDevicesIds;

/**
 *  Application:   GGC - GNU Gluco Control
 *  Plug-in:       Pump Tool (support for Pump devices)
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
 *  Filename:     OneTouchPing
 *  Description:  One Touch Ping implementation
 * 
 *  Author: Andy Rozman {andy@atech-software.com}
 */

public class OneTouchPing extends AnimasIR2020
{

    /**
     * Constructor 
     */
    public OneTouchPing()
    {
        super();
    }
    
    
    /**
     * Constructor 
     * 
     * @param communicationPort
     * @param writer 
     */
    public OneTouchPing(String communicationPort, OutputWriter writer)
    {
        super(communicationPort, writer);
    }
    
    
    /**
     * Constructor
     * 
     * @param cmp
     */
    public OneTouchPing(AbstractDeviceCompany cmp)
    {
        super(cmp);
    }



    /**
     * {@inheritDoc}
     */
    @Override
    public PumpDeviceDefinition getPumpDeviceDefinition()
    {
        return PumpDeviceDefinition.OneTouchPing;
    }

}


