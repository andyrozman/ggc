package ggc.cgms.device.dexcom;

import ggc.cgms.device.AbstractCGMS;
import ggc.cgms.device.dexcom.file.FRC_DexcomTxt_DM3;
import ggc.cgms.device.dexcom.file.FRC_DexcomXml_DM3;
import ggc.plugin.data.GGCPlugInFileReaderContext;
import ggc.plugin.device.DeviceIdentification;
import ggc.plugin.device.PlugInBaseException;
import ggc.plugin.manager.DeviceImplementationStatus;
import ggc.plugin.manager.company.AbstractDeviceCompany;
import ggc.plugin.output.OutputWriter;
import ggc.plugin.protocol.DeviceConnectionProtocol;
import ggc.plugin.util.DataAccessPlugInBase;

/**
 * Application: GGC - GNU Gluco Control
 * Plug-in: CGMS Tool (support for CGMS devices)
 *
 * See AUTHORS for copyright information.
 *
 * This program is free software; you can redistribute it and/or modify it under
 * the terms of the GNU General Public License as published by the Free Software
 * Foundation; either version 2 of the License, or (at your option) any later
 * version.
 *
 * This program is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU General Public License for more
 * details.
 *
 * You should have received a copy of the GNU General Public License along with
 * this program; if not, write to the Free Software Foundation, Inc., 59 Temple
 * Place, Suite 330, Boston, MA 02111-1307 USA
 *
 * Filename: DexcomCGMS
 * Description: Dexcom Abstract Implementation
 *
 * Author: Andy {andy@atech-software.com}
 */

// FIXME

public abstract class DexcomCGMS extends AbstractCGMS
{

    /**
     * Constructor
     */
    public DexcomCGMS()
    {
        super();
    }

    /**
     * Constructor
     *
     * @param params
     * @param writer
     */
    public DexcomCGMS(String params, OutputWriter writer)
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
    public DexcomCGMS(String params, OutputWriter writer, DataAccessPlugInBase da)
    {
        super(params, writer, da);
    }

    /**
     * Constructor
     *
     * @param cmp
     */
    public DexcomCGMS(AbstractDeviceCompany cmp)
    {
        super(cmp);
    }

    /**
     * getComment - Get Comment for device
     *
     * @return comment or null
     */
    public String getComment()
    {
        return "";
    }

    /**
     * getImplementationStatus - Get Implementation Status
     *
     * @return implementation status as number
     * @see ggc.plugin.manager.DeviceImplementationStatus
     */
    public DeviceImplementationStatus getImplementationStatus()
    {
        return DeviceImplementationStatus.Partitial;
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
     *
     * @return
     */
    @Override
    public DeviceIdentification getDeviceInfo()
    {
        return this.outputWriter.getDeviceIdentification();
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
     * normal progress status, but with some special devices we calculate progress through other means.
     *
     * @return true is progress status is special
     */
    @Override
    public boolean hasSpecialProgressStatus()
    {
        return false;
    }

    /**
     * getInstructions - get instructions for device
     * Should be implemented by meter class.
     *
     * @return instructions for reading data
     */
    public String getInstructions()
    {
        return "INSTRUCTIONS_DEXCOM";
    }

    /**
     * Is Device Communicating
     *
     * @return
     */
    public boolean isDeviceCommunicating()
    {
        return true;
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
     * Get DateTime From String
     *
     * @param val
     * @return
     */
    public static long getDateTimeFromString(String val)
    {
        // 2007-03-23 13:13:29.010

        val = val.substring(0, val.indexOf("."));
        val = val.replace(" ", "");
        val = val.replace("-", "");
        val = val.replace(":", "");

        // System.out.println("" + val);

        return Long.parseLong(val);
    }

    /**
     * Get Time From String
     *
     * @param val
     * @return
     */
    public static int getTimeFromString(String val)
    {
        val = val.substring(val.indexOf(" ") + 1, val.indexOf("."));
        // val = val.replace(" ", "");
        // val = val.replace("-", "");
        val = val.replace(":", "");

        return Integer.parseInt(val);
    }

    /**
     * Load File Contexts - Load file contexts that device supports
     */
    @Override
    public void loadFileContexts()
    {
        // System.out.println("loadFileContexts");
        this.fileContexts = new GGCPlugInFileReaderContext[2];
        this.fileContexts[0] = new FRC_DexcomXml_DM3(dataAccess, this.outputWriter);
        this.fileContexts[1] = new FRC_DexcomTxt_DM3(dataAccess, this.outputWriter);
    }

}
