package ggc.pump.device.minimed;

import ggc.plugin.device.DownloadSupportType;
import ggc.plugin.manager.company.AbstractDeviceCompany;
import ggc.plugin.output.OutputWriter;
import ggc.plugin.util.DataAccessPlugInBase;
import ggc.pump.manager.PumpDevicesIds;

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
 *  Filename:     Minimed522
 *  Description:  Minimed 522/722 implementation (just settings)
 *
 *  Author: Andy {andy@atech-software.com}
 */

public class Minimed523 extends Minimed522
{

    /**
     * Constructor
     *
     * @param da - data access instance
     * @param device_type - device type
     * @param full_port - full port identification
     * @param writer - output writer instance
     */
    public Minimed523(DataAccessPlugInBase da, int device_type, String full_port, OutputWriter writer)
    {
        super(da, device_type, full_port, writer);
    }


    /**
     * Constructor
     *
     * @param da - data access instance
     * @param full_port - full port identification
     * @param writer - output writer instance
     */
    public Minimed523(DataAccessPlugInBase da, String full_port, OutputWriter writer)
    {
        this(da, PumpDevicesIds.PUMP_MINIMED_523, full_port, writer);
    }


    /**
     * Constructor
     *
     * @param cmp
     */
    public Minimed523(AbstractDeviceCompany cmp)
    {
        super(cmp);
    }


    /**
     * getName - Get Name of meter.
     *
     * @return name of meter
     */
    @Override
    public String getName()
    {
        return "Minimed 522/722";
    }


    /**
     * getIconName - Get Icon of meter
     *
     * @return icon name
     */
    @Override
    public String getIconName()
    {
        return "mm_522_722.jpg";
    }


    /**
     * getDeviceId - Get Device Id, within MgrCompany class
     * Should be implemented by device class.
     *
     * @return id of device within company
     */
    @Override
    public int getDeviceId()
    {
        return PumpDevicesIds.PUMP_MINIMED_523;
    }


    /**
     * getInstructions - get instructions for device
     * Should be implemented by meter class.
     *
     * @return instructions for reading data
     */
    @Override
    public String getInstructions()
    {
        return "INSTRUCTIONS_MINIMED_508";
    }


    /**
     * getComment - Get Comment for device
     *
     * @return comment or null
     */
    @Override
    public String getComment()
    {
        return null;
    }


    /**
     * getDeviceClassName - Get Class name of device implementation, used by Reflection at later time
     *
     * @return class name as string
     */
    @Override
    public String getDeviceClassName()
    {
        return "ggc.pump.device.minimed.Minimed522";
    }


    /**
     * Get Max Memory Records
     */
    @Override
    public int getMaxMemoryRecords()
    {
        return 0;
    }


    /**
     * @{InheritDocs}
     */
    public DownloadSupportType getDownloadSupportType()
    {
        return DownloadSupportType.NotSupportedByGGC; // .DOWNLOAD_FROM_DEVICE_FILE;
    }


    /**
     * How Many Months Of Data Stored
     *
     * @return
     */
    @Override
    public int howManyMonthsOfDataStored()
    {
        return -1;
    }


    /**
     * Get Temporary Basal Type Definition
     * "TYPE=Unit;STEP=0.1"
     * "TYPE=Procent;STEP=10;MIN=0;MAX=200"
     * "TYPE=Both;STEP_UNIT=0.1;STEP=10;MIN=0;MAX=200"
     *
     * @return
     */
    @Override
    public String getTemporaryBasalTypeDefinition()
    {
        // return "TYPE=Unit;STEP=0.1";
        return null;
    }


    /**
     * Get Bolus Step (precission)
     *
     * @return
     */
    @Override
    public float getBolusStep()
    {
        return 0.1f;
    }


    /**
     * Get Basal Step (precission)
     *
     * @return
     */
    @Override
    public float getBasalStep()
    {
        return 0.1f;
    }


    /**
     * Are Pump Settings Set (Bolus step, Basal step and TBR settings)
     *
     * @return
     */
    @Override
    public boolean arePumpSettingsSet()
    {
        return false;
    }

}
