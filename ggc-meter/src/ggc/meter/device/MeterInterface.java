package ggc.meter.device;

import ggc.plugin.device.DeviceIdentification;
import ggc.plugin.device.DeviceInterface;
import ggc.plugin.device.PlugInBaseException;

import com.atech.graphics.dialogs.selector.SelectableInterface;


/*
 *  GGC - GNU Gluco Control
 *
 *  A pure java app to help you manage your diabetes.
 *
 *  See AUTHORS for copyright information.
 *
 *  This program is free software; you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation; either version 2 of the License, or
 *  (at your option) any later version.
 *
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with this program; if not, write to the Free Software
 *  Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA
 *
 *  Filename: MeterInterface
 *  Purpose:  This is interface class, used for meters. It should be primary implemented by protocol class, and 
 *       protocol class should be used as super class for meter definitions. Each meter family "should" 
 *       have it's own super class and one class for each meter.
 *
 *  Author:   andyrozman {andyrozman@sourceforge.net}
 */

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
 *  Filename:     MeterInterface
 *  Description:  This is interface class, used for meters. It should be primary implemented by abstract 
 *                protocol class. Each meter family "should" have it's own super class and one class for 
 *                each meter type instance.
 * 
 *  Author: Andy {andy@atech-software.com}
 */


public interface MeterInterface extends SelectableInterface, DeviceInterface
{


    //************************************************
    //***          Working with device             ***
    //************************************************


    /**
     * Will be called, on start of import to start communication with device
     * @return 
     * @throws PlugInBaseException 
     */
    boolean open() throws PlugInBaseException;


    /**
     * Will be called, when the import is ended and freeing resources.
     * @throws PlugInBaseException 
     */
    void close() throws PlugInBaseException;





    //************************************************
    //***      Meter Identification Methods        ***
    //************************************************

    
    /**
     * getCompanyId - Get Company Id 
     * Should be implemented by meter class.
     * 
     * @return id of company
     */
    int getCompanyId();
    
    
    
    /**
     * getMaxMemoryRecords - Get Maximum entries that can be stored in devices memory
     * 
     * @return number
     */
    public int getMaxMemoryRecords();

    
    


    //************************************************
    //***        Available Functionality for Meter          ***
    //************************************************

    
    /**
     * getDeviceInfo - get Device info (firmware and software revision)
     * @return 
     */
    public DeviceIdentification getDeviceInfo();
    
    
    
}
