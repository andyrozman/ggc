package ggc.pump.device.dana;

import ggc.plugin.device.DownloadSupportType;
import ggc.plugin.manager.DeviceImplementationStatus;
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
 *  Filename:     DanaDiabecare_IIS  
 *  Description:  Dana Diabecare II/S device implementation (just settings) 
 * 
 *  Author: Andy {andy@atech-software.com}
 */

public class DanaDiabecare_IIS extends DanaDiabecare_II
{

    /**
     * Constructor 
     */
    public DanaDiabecare_IIS()
    {
        super();
    }


    /**
     * Constructor 
     * 
     * @param params 
     * @param writer 
     */
    public DanaDiabecare_IIS(String params, OutputWriter writer)
    {
        super();
        // super(params, writer);
    }


    /**
     * Constructor
     * 
     * @param params
     * @param writer
     * @param da 
     */
    public DanaDiabecare_IIS(String params, OutputWriter writer, DataAccessPlugInBase da)
    {
        super(params, writer, da);
    }


    /**
     * Constructor
     * 
     * @param cmp
     */
    public DanaDiabecare_IIS(AbstractDeviceCompany cmp, DataAccessPlugInBase da)
    {
        super(cmp, da);
    }


    // ************************************************
    // *** Meter Identification Methods ***
    // ************************************************

    /**
     * getName - Get Name of meter. 
     * 
     * @return name of meter
     */
    @Override
    public String getName()
    {
        return "Diabcare II S/SG";
    }


    /**
     * getIconName - Get Icon of meter
     * 
     * @return icon name
     */
    @Override
    public String getIconName()
    {
        return "so_danaIISG.jpg";
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
        return PumpDevicesIds.PUMP_DANA_DIABECARE_IIS;
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
        return "INSTRUCTIONS_DANA_IIS";
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
     * getImplementationStatus - Get Implementation Status 
     * 
     * @return implementation status as number
     * @see ggc.plugin.manager.DeviceImplementationStatus
     */
    @Override
    public DeviceImplementationStatus getImplementationStatus()
    {
        return DeviceImplementationStatus.NotAvailable;
    }


    /**
     * getDeviceClassName - Get Class name of device implementation, used by Reflection at later time
     * 
     * @return class name as string
     */
    @Override
    public String getDeviceClassName()
    {
        return "ggc.pump.device.dana.DanaDiabcare_IIS";
    }


    /** 
     * Get Max Memory Records
     */
    @Override
    public int getMaxMemoryRecords()
    {
        // TODO Auto-generated method stub
        return 0;
    }


    /**
     * Get Download Support Type
     * 
     * @return
     */
    @Override
    public DownloadSupportType getDownloadSupportType()
    {
        return DownloadSupportType.NotSupportedByDevice;
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
