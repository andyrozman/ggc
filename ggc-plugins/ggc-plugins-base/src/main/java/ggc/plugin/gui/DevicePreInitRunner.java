package ggc.plugin.gui;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ggc.plugin.data.DeviceDataHandler;

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
 *  Filename:     DeviceReaderRunner
 *  Description:  This is separate thread class to get current data from database in order to 
 *                compare it later.
 * 
 *  Author: Andy {andy@atech-software.com}
 */

// Try to assess possibility of super-classing

public class DevicePreInitRunner extends Thread
{

    private static final Logger LOG = LoggerFactory.getLogger(DevicePreInitRunner.class);

    DeviceInstructionsDialog device_instruction_dialog;
    DeviceDataHandler deviceDataHandler;


    /**
     * Constructor
     * 
     * @param deviceDataHandler
     * @param did 
     */
    // FIXME
    public DevicePreInitRunner(DeviceDataHandler deviceDataHandler, DeviceInstructionsDialog did)
    {
        this.deviceDataHandler = deviceDataHandler;
        this.device_instruction_dialog = did;
    }


    /** 
     * Thread running method
     */
    @Override
    public void run()
    {
        try
        {
            LOG.debug("preInit Device - Start");

            if (deviceDataHandler.getDeviceInterfaceV2() != null)
            {
                deviceDataHandler.getDeviceInterfaceV2().preInitDevice();
            }
            else if (deviceDataHandler.getDeviceInterfaceV1() != null)
            {
                deviceDataHandler.getDeviceInterfaceV1().preInitDevice();
            }

            LOG.debug("preInit Device - End");
        }
        catch (Exception ex)
        {
            LOG.error("Error on PreInit. Ex: " + ex, ex);
        }
        finally
        {
            this.device_instruction_dialog.preInitDone(true);
        }
    }

}
