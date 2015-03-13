package ggc.plugin.device.impl.minimed.comm;

import ggc.plugin.device.DownloadSupportType;
import ggc.plugin.device.PlugInBaseException;
import ggc.plugin.device.impl.minimed.MinimedDevice;
import ggc.plugin.device.impl.minimed.cmd.MinimedCommand;
import ggc.plugin.manager.DeviceImplementationStatus;
import ggc.plugin.protocol.SerialProtocol;
import gnu.io.SerialPortEvent;

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
 *  Filename:     MinimedComm_ComStation  
 *  Description:  Communication Protocol: COM Station (For 508 and 508c only)
 * 
 *  Author: Andy {andy@atech-software.com}
 */

public class MinimedComm_ComStation extends SerialProtocol implements MinimedComm_Interface // extends
                                                                                            // SerialProtocol
                                                                                            // implements
                                                                                            // MinimedComm_Interface
{

    /**
     * Constructor
     */
    public MinimedComm_ComStation(MinimedDevice mmd)
    {
        super(mmd.getDataAccess());
        // super(port, serial_number);
    }

    @Override
    public void serialEvent(SerialPortEvent event)
    {
    }

    // Here are old methods from minimed stored

    public int closeCommunicationInterface() throws PlugInBaseException
    {
        return 0;
    }

    public int closeDevice() throws PlugInBaseException
    {
        return 0;
    }

    public int initializeCommunicationInterface() throws PlugInBaseException
    {
        return 0;
    }

    public int[] decrypt(int[] input)
    {
        return input;
    }

    public int[] encrypt(int[] input)
    {
        return input;
    }

    public boolean hasEncryptionSupport()
    {
        return false;
    }

    public void dumpInterfaceStatus()
    {
        // TODO Auto-generated method stub

    }

    public int initDevice() throws PlugInBaseException
    {
        // TODO Auto-generated method stub
        return 0;
    }

    public boolean executeCommandRetry(int commandId) throws PlugInBaseException
    {
        return false;
    }

    public boolean executeCommandRetry(MinimedCommand command) throws PlugInBaseException
    {
        return false;
    }

    @Override
    public void dispose()
    {
    }

    public String getComment()
    {
        // TODO Auto-generated method stub
        return null;
    }

    public String getConnectionPort()
    {
        // TODO Auto-generated method stub
        return null;
    }

    public String getDeviceClassName()
    {
        // TODO Auto-generated method stub
        return null;
    }

    public int getDeviceId()
    {
        return 0;
    }

    @Override
    public String getDeviceSpecialComment()
    {
        // TODO Auto-generated method stub
        return null;
    }

    public DownloadSupportType getDownloadSupportType()
    {
        return DownloadSupportType.NoDownloadSupport;
    }



    public String getIconName()
    {
        // TODO Auto-generated method stub
        return null;
    }

    public DeviceImplementationStatus getImplementationStatus()
    {
        return DeviceImplementationStatus.NotAvailable;
    }

    public String getInstructions()
    {
        // TODO Auto-generated method stub
        return null;
    }

    public String getName()
    {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public boolean hasIndeterminateProgressStatus()
    {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public boolean hasSpecialProgressStatus()
    {
        // TODO Auto-generated method stub
        return false;
    }

    public boolean isDeviceCommunicating()
    {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public boolean isFileDownloadSupported()
    {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public boolean isReadableDevice()
    {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public void readConfiguration() throws PlugInBaseException
    {
        // TODO Auto-generated method stub

    }

    public void readDeviceDataFull() throws PlugInBaseException
    {
        // TODO Auto-generated method stub

    }

    @Override
    public void readDeviceDataPartitial() throws PlugInBaseException
    {
        // TODO Auto-generated method stub

    }

    @Override
    public void readInfo() throws PlugInBaseException
    {
        // TODO Auto-generated method stub

    }

    @Override
    public long getItemId()
    {
        // TODO Auto-generated method stub
        return 0;
    }

    public void sendCommandReadData(MinimedCommand command) throws PlugInBaseException
    {
        // TODO Auto-generated method stub

    }

}
