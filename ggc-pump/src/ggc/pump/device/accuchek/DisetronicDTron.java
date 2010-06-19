package ggc.pump.device.accuchek;

import ggc.plugin.device.PlugInBaseException;
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
 *  Filename:     DisetronicDTron  
 *  Description:  Disetronic D-Tron Pump Implementation
 * 
 *  Author: Andy {andy@atech-software.com}
 */


public class DisetronicDTron extends AccuChekSmartPixPump
{
    
    
    /**
     * Constructor
     * 
     * @param drive_letter 
     * @param writer 
     */
    public DisetronicDTron(String drive_letter, OutputWriter writer)
    {
        super(drive_letter, writer);
    }
    
    
    public DisetronicDTron(String params, OutputWriter writer, DataAccessPlugInBase da)
    {
        super(params, writer, da);
    }
    
    
    /**
     * Constructor
     * 
     * @param cmp
     */
    public DisetronicDTron(AbstractDeviceCompany cmp)
    {
        super(cmp);
    }
    
    
    //************************************************
    //***      Meter Identification Methods        ***
    //************************************************


    /**
     * getName - Get Name of meter. 
     * 
     * @return name of meter
     */
    public String getName()
    {
        return "D-Tron (Disetronic)";
    }


    
    /**
     * getIconName - Get Icon of meter
     * 
     * @return icon name
     */
    public String getIconName()
    {
        return "ac_dtron.jpg";
    }
    

    /**
     * getDeviceId - Get Device Id, within MgrCompany class 
     * Should be implemented by device class.
     * 
     * @return id of device within company
     */
    public int getDeviceId()
    {
        return PumpDevicesIds.PUMP_DISETRONIC_D_TRON;
    }

    
    
    /**
     * getInstructions - get instructions for device
     * Should be implemented by meter class.
     * 
     * @return instructions for reading data 
     */
    public String getInstructions()
    {
        return "INSTRUCTIONS_ACCUCHEK_SPIRIT";
    }
    
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
    public int getImplementationStatus() 
    {
        return DeviceImplementationStatus.IMPLEMENTATION_IN_PROGRESS;
    }
    
    
    /**
     * getMaxMemoryRecords - Get Maximum entries that can be stored in devices memory (not verified for this pump)
     * 
     * @return number
     */
    public int getMaxMemoryRecords()
    {
        return 2000;
    }
    
    
    /**
     * getNrOfElementsFor1s - How many elements are read in 1s (which is our refresh time) (not verified for this pump)
     * @return number of elements
     */
    public int getNrOfElementsFor1s()
    {
        return 85;
    }
    
    
    /**
     * getDeviceClassName - Get Class name of device implementation, used by Reflection at later time
     * 
     * @return class name as string
     */
    public String getDeviceClassName()
    {
        return "ggc.meter.device.accuchek.DisetronicDTron";
    }


    /**
     * Open
     */
    public boolean open() throws PlugInBaseException
    {
        return true;
    }
    

    /**
     * Close
     */
    public void close() throws PlugInBaseException
    {
    }
   
    
    /**
     * Get Temporary Basal Type Definition
     * "TYPE=Unit;STEP=0.1"
     * "TYPE=Procent;STEP=10;MIN=0;MAX=200"
     * "TYPE=Both;STEP_UNIT=0.1;STEP=10;MIN=0;MAX=200"
     * 
     * @return
     */
    public String getTemporaryBasalTypeDefinition()
    {
        //return "TYPE=Unit;STEP=0.1";
        return null;
    }
    
    
    /**
     * Get Bolus Step (precission)
     * 
     * @return
     */
    public float getBolusStep()
    {
        return 0.1f;
    }
    
    
    /**
     * Get Basal Step (precission)
     * 
     * @return
     */
    public float getBasalStep()
    {
        return 0.1f;
    }
    
    
    /**
     * Are Pump Settings Set (Bolus step, Basal step and TBR settings)
     * 
     * @return
     */
    public boolean arePumpSettingsSet()
    {
        return false;
    }
    
    
}