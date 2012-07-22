package ggc.plugin.device.impl.minimed.comm;

import ggc.plugin.device.PlugInBaseException;
import ggc.plugin.device.impl.minimed.cmd.MinimedCommand;

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
 *  Filename:     MinimedComm_Interface  
 *  Description:  Minimed Communication Interface
 * 
 *  Author: Andy {andy@atech-software.com}
 */


public interface MinimedComm_Interface
{


    
    /**
     * Initialize Interface - Initialize interface 
     * 
     * @return
     * @throws PlugInBaseException 
     */
    public int initializeCommunicationInterface() throws PlugInBaseException;
    
    
    /**
     * Close Interface - Close interface
     * 
     * @return
     * @throws PlugInBaseException 
     */
    public int closeCommunicationInterface() throws PlugInBaseException;

    
    /**
     * Init Device - Init device 
     * 
     * @return
     * @throws PlugInBaseException 
     */
    public int initDevice() throws PlugInBaseException;
    

    
    /**
     * Close Device - Close device 
     * 
     * @return
     * @throws PlugInBaseException 
     */
    public int closeDevice() throws PlugInBaseException;
    
    
    /**
     * Decode message
     * 
     * @param input
     * @return
     */
    public int[] decrypt(int[] input);

    
    /**
     * Encode message
     * 
     * @param input
     * @return
     */
    public int[] encrypt(int[] input);
    
    
    /**
     * Does interface support encoding/decoding
     * @return
     */
    public boolean hasEncryptionSupport();
    
    
    /**
     * Dump Interface Status - Dump status of interface
     * 
     */
    //public void dumpInterfaceStatus();

    
    
    /**
     * Execute Command
     * 
     * @param command_id
     * @throws PlugInBaseException
     */
    public boolean executeCommandRetry(int command_id) throws PlugInBaseException;
    
    
    /**
     * Execute Command
     * 
     * @param command
     * @throws PlugInBaseException
     */
    public boolean executeCommandRetry(MinimedCommand command) throws PlugInBaseException;
    
    
    
    public void sendCommandReadData(MinimedCommand command) throws PlugInBaseException;
    
    
    
}
