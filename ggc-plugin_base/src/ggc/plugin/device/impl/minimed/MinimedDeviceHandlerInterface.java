package ggc.plugin.device.impl.minimed;

import ggc.plugin.device.impl.minimed.enums.MinimedDeviceType;
import ggc.plugin.device.impl.minimed.enums.MinimedTargetType;
import ggc.plugin.device.v2.DeviceDefinition;
import ggc.plugin.util.DataAccessPlugInBase;

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
 *  Filename:     MinimedDeviceHandlerInterface
 *  Description:  This is interface for Handler, which contains several different methods we need to implements (so that
 *         we don't need special classes for both instances, but can just just one (eg. MinimedDeviceReader and
 *         MinimedDataHandler).
 *
 *  Author: Andy {andy@atech-software.com}
 */

public interface MinimedDeviceHandlerInterface
{

    /**
     * Get Minimed Device Type
     * 
     * @param definition deviceDefintion instance
     * @return MinimedDeviceType instance
     */
    MinimedDeviceType getMinimedDeviceType(DeviceDefinition definition);


    /**
     * Register converters
     */
    void registerConverters();


    /**
     * Get Device Target Type (either Pump or CGMS)
     * 
     * @return MinimedTargetType instance (returned values can be Pump or CGMS no other)
     */
    MinimedTargetType getDeviceTargetType();


    /**
     * Get DataAccess instance for correct plugin.
     *
     * @return DataAccessPlugInBase instance (either for Pump or CGMS)
     */
    DataAccessPlugInBase getDataAccessInstance();

}
