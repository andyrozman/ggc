package ggc.cgms.device.minimed;

import java.util.ArrayList;

import ggc.cgms.device.AbstractCGMS;
import ggc.cgms.device.minimed.file.FRC_MinimedCarelink;
import ggc.plugin.data.GGCPlugInFileReaderContext;
import ggc.plugin.device.DeviceIdentification;
import ggc.plugin.device.DownloadSupportType;
import ggc.plugin.device.PlugInBaseException;
import ggc.plugin.manager.DeviceImplementationStatus;
import ggc.plugin.manager.company.AbstractDeviceCompany;
import ggc.plugin.output.OutputWriter;
import ggc.plugin.protocol.DeviceConnectionProtocol;
import ggc.plugin.util.DataAccessPlugInBase;

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
 *  Filename:     AccuChekSpirit  
 *  Description:  Accu Chek Spirit Pump Implementation
 * 
 *  Author: Andy {andy@atech-software.com}
 */

// FIXME

public abstract class MiniMedCGMS extends AbstractCGMS
{

    /**
     * Constructor 
     */
    public MiniMedCGMS()
    {
        super();
    }


    /**
     * Constructor 
     * 
     * @param params 
     * @param writer 
     */
    public MiniMedCGMS(String params, OutputWriter writer)
    {
        super(params, writer);
    }


    /**
     * Constructor
     * 
     * @param params
     * @param writer
     * @param da
     */
    public MiniMedCGMS(String params, OutputWriter writer, DataAccessPlugInBase da)
    {
        super(params, writer, da);
    }


    /**
     * Constructor
     * 
     * @param cmp
     */
    public MiniMedCGMS(AbstractDeviceCompany cmp)
    {
        super(cmp);
    }


    // ************************************************
    // *** Meter Identification Methods ***
    // ************************************************

    /**
     * getComment - Get Comment for device 
     * 
     * @return comment or null
     */
    public String getComment()
    {
        return null;
    }


    /**
     * getImplementationStatus - Get Implementation Status 
     * 
     * @return implementation status as number
     * @see ggc.plugin.manager.DeviceImplementationStatus
     */
    public DeviceImplementationStatus getImplementationStatus()
    {
        return DeviceImplementationStatus.NotAvailable;
    }


    /**
     * Open
     * 
     * @return 
     * @throws PlugInBaseException 
     */
    public boolean open() throws PlugInBaseException
    {
        return true;
    }


    /**
     * Close
     * 
     * @throws PlugInBaseException 
     */
    public void close() throws PlugInBaseException
    {
    }


    /** 
     * This is method for reading configuration, in case that dump doesn't give this information.
     * 
     * @throws PlugInBaseException
     */
    @Override
    public void readConfiguration() throws PlugInBaseException
    {
    }


    /**
     * readDeviceDataFull - This is method for reading data from device. All reading from actual device should 
     * be done from here. Reading can be done directly here, or event can be used to read data. Usage of events 
     * is discouraged because reading takes 3-4x more time.
     * 
     * @throws PlugInBaseException 
     */
    public void readDeviceDataFull() throws PlugInBaseException
    {
    }


    /**
     * This is method for reading partial data from device. This can be used if your device can be read partialy 
     * (from some date to another)
     * 
     * @throws PlugInBaseException 
     */
    @Override
    public void readDeviceDataPartitial() throws PlugInBaseException
    {
    }


    /**
     * This is for reading device information. This should be used only if normal dump doesn't retrieve this
     * information (most dumps do).
     *  
     * @throws PlugInBaseException
     */
    @Override
    public void readInfo() throws PlugInBaseException
    {
    }


    /**
     * getDeviceInfo - get Device info (firmware and software revision)
     * @return 
     */
    @Override
    public DeviceIdentification getDeviceInfo()
    {
        return null;
    }


    /**
     * loadPumpSpecificValues - should be called from constructor of any AbstractPump classes and should
     *      create, AlarmMappings and EventMappings and any other pump constants.
     */
    public void loadPumpSpecificValues()
    {
    }


    /** 
     * Dispose
     */
    public void dispose()
    {
    }


    /**
     * getConnectionPort - connection port data
     * 
     * @return connection port as string
     */
    public String getConnectionPort()
    {
        return null;
    }


    /**
     * getConnectionProtocol - returns id of connection protocol
     * 
     * @return id of connection protocol
     */
    public DeviceConnectionProtocol getConnectionProtocol()
    {
        return DeviceConnectionProtocol.None;
    }


    /**
     * hasSpecialProgressStatus - in most cases we read data directly from device, in this case we have 
     *    normal progress status, but with some special devices we calculate progress through other means.
     *    
     * @return true is progress status is special
     */
    @Override
    public boolean hasSpecialProgressStatus()
    {
        return false;
    }


    /**
     * Is Device Communicating
     * 
     * @return
     */
    public boolean isDeviceCommunicating()
    {
        return false;
    }


    /**
     * Is Device Readable (there are some devices that are not actual devices, but are used to get some
     * sort of specific device data - in most cases we call them generics, and they don't have ability
     * to read data)
     * 
     * @return
     */
    @Override
    public boolean isReadableDevice()
    {
        return false;
    }


    /**
     * hasIndeterminateProgressStatus - if status can't be determined then JProgressBar will go from 
     *     left to right side, without displaying progress.
     * @return
     */
    @Override
    public boolean hasIndeterminateProgressStatus()
    {
        return true;
    }


    @Override
    public boolean hasDefaultParameter()
    {
        return false;
    }


    /**
     * Load File Contexts - Load file contexts that device supports
     */
    @Override
    public void loadFileContexts()
    {
        this.fileContexts = new ArrayList<GGCPlugInFileReaderContext>();
        this.fileContexts.add(new FRC_MinimedCarelink(dataAccess, this.outputWriter));
    }


    /**
     * Get Download Support Type
     * 
     * @return
     */
    @Override
    public DownloadSupportType getDownloadSupportType()
    {
        return DownloadSupportType.DownloadDataFile;
    }

}
