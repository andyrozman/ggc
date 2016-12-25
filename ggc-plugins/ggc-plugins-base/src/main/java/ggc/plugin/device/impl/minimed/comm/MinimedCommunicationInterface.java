package ggc.plugin.device.impl.minimed.comm;

import ggc.plugin.device.PlugInBaseException;
import ggc.plugin.device.impl.minimed.data.MinimedCommandReply;
import ggc.plugin.device.impl.minimed.enums.MinimedCommandType;

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
 *  Filename:     MinimedCommunicationInterface
 *  Description:  Minimed Communication Interface
 * 
 *  Author: Andy {andy@atech-software.com}
 */

public interface MinimedCommunicationInterface
{

    /**
     * Initialize Interface - Open interface (physical connection and prepares for communication)
     * 
     * @return
     * @throws PlugInBaseException 
     */
    void initializeCommunicationInterface() throws PlugInBaseException;


    /**
     * Close Interface - Closes interface (serial/USB/...)
     * 
     * @return
     * @throws PlugInBaseException 
     */
    int closeCommunicationInterface() throws PlugInBaseException;


    /**
     * Init Device - it calls commands required so that we start working with device
     * 
     * @return
     * @throws PlugInBaseException 
     */
    int initDevice() throws PlugInBaseException;


    /**
     * Close Device - close communication with device (sends commands to turn RF off)
     * 
     * @return
     * @throws PlugInBaseException 
     */
    int closeDevice() throws PlugInBaseException;


    /**
     * Execute Command With Retry - Execute Command With Retry
     *
     * @param commandType
     * @return
     * @throws PlugInBaseException
     */
    MinimedCommandReply executeCommandWithRetry(MinimedCommandType commandType) throws PlugInBaseException;


    /**
     * This is main method for reading ALL data from device. It should contain call to all
     * Data commands and it should also convert all data on the spot.
     */
    void readDataFromInterface() throws PlugInBaseException;


    /**
     * This is main method for reading ALL configuration from device. It should contain call
     * to all Configuration commands and it should also convert all data on the spot.
     */
    void readConfigurationFromInterface() throws PlugInBaseException;

}
