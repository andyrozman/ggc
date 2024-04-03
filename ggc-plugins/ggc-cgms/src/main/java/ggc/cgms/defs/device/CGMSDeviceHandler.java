package ggc.cgms.defs.device;

import java.util.List;

import ggc.cgms.util.DataAccessCGMS;
import ggc.core.plugins.GGCPluginType;
import ggc.plugin.data.GGCPlugInFileReaderContext;
import ggc.plugin.device.DownloadSupportType;
import ggc.plugin.device.v2.DeviceDefinition;
import ggc.plugin.device.v2.DeviceHandlerAbstract;

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
 *  Filename: CGMSDeviceHandler
 *  Description: CGMS Device Handler
 *
 *  Author: Andy {andy@atech-software.com}
 */

public abstract class CGMSDeviceHandler extends DeviceHandlerAbstract
{

    protected DataAccessCGMS dataAccessCGMS;


    public CGMSDeviceHandler()
    {
        dataAccessCGMS = DataAccessCGMS.getInstance();
    }


    /**
     * Check if DataAccess Set, if not set it.
     */
    protected void checkIfDataAccessSet()
    {
        if (dataAccessCGMS == null)
            dataAccessCGMS = DataAccessCGMS.getInstance();
    }


    public List<GGCPlugInFileReaderContext> getFileDownloadContexts(DownloadSupportType downloadSupportType)
    {
        return null;
    }


    protected String getCommunicationPort(Object connectionParameters)
    {
        return (String) connectionParameters;
    }


    /**
     * {@inheritDoc}
     */
    protected CGMSDeviceDefinition getDeviceDefinition(DeviceDefinition definition)
    {
        return (CGMSDeviceDefinition) definition;
    }


    /**
     * {@inheritDoc}
     */
    public GGCPluginType getGGCPluginType()
    {
        return GGCPluginType.CGMSToolPlugin;
    }

}
