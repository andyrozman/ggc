package ggc.cgms.device;

import ggc.cgms.util.DataAccessCGMS;
import ggc.plugin.device.PlugInBaseException;

import com.atech.i18n.I18nControlAbstract;

/**
 *  Application:   GGC - GNU Gluco Control
 *  Plug-in:       CGMS Tool (support for CGMS devices)
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
 *  Filename:     DummyCGM  
 *  Description:  Dummy CGM device, used for special stuff
 * 
 *  Author: Andy {andy@atech-software.com}
 */

public class DummyCGMS extends AbstractCGMS // GenericCGMS //implements
                                            // MeterInterface
{

    DataAccessCGMS m_da = DataAccessCGMS.getInstance();
    I18nControlAbstract m_ic = m_da.getI18nControlInstance();

    // public int m_meter_index = 0;

    /**
     * Used for opening connection with device.
     * @return boolean - if connection established
     */
    public boolean open()
    {
        return true;
    }

    /**
     * Will be called, when the import is ended and freeing resources.
     */
    public void close()
    {
        return;
    }

    /**
     * getName - Get Name of device. 
     * Should be implemented by device class.
     * 
     * @return 
     */
    @Override
    public String getName()
    {
        return "Dummy Meter";
    }

    /**
     * getDeviceId - Get Device Id, this are plugin specific and global (for example only one device 
     * of type meter, can have same id.  
     * Should be implemented by protocol class.
     */
    public int getDeviceId()
    {
        return 0;
    }

    public void dispose()
    {
        // TODO Auto-generated method stub

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

    public int getConnectionProtocol()
    {
        // TODO Auto-generated method stub
        return 0;
    }

    public String getDeviceClassName()
    {
        // TODO Auto-generated method stub
        return null;
    }

    public String getIconName()
    {
        // TODO Auto-generated method stub
        return null;
    }

    public int getImplementationStatus()
    {
        // TODO Auto-generated method stub
        return 0;
    }

    public String getInstructions()
    {
        // TODO Auto-generated method stub
        return null;
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

    /**
     * Load File Contexts - Load file contexts that device supports
     */
    @Override
    public void loadFileContexts()
    {
    }

}
