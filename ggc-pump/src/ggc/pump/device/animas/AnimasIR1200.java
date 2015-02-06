package ggc.pump.device.animas;

import ggc.plugin.device.DownloadSupportType;
import ggc.plugin.device.PlugInBaseException;
import ggc.plugin.device.impl.animas.enums.AnimasDeviceType;
import ggc.plugin.manager.DeviceImplementationStatus;
import ggc.plugin.manager.company.AbstractDeviceCompany;
import ggc.plugin.output.OutputWriter;
import ggc.pump.data.defs.PumpDeviceDefinition;
import ggc.pump.device.animas.impl.AnimasPumpDeviceReader;
import ggc.plugin.device.impl.animas.util.AnimasException;
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
 *  Filename:     AnimasIR1200  
 *  Description:  Animas IR 1200 implementation
 *
 *  Author: Andy Rozman {andy@atech-software.com}
 */

public class AnimasIR1200 extends AnimasPump
{
    AnimasIR1200Handler handler = new AnimasIR1200Handler();

    /**
     * Constructor 
     */
    public AnimasIR1200()
    {
        super();
    }
    
    
    /**
     * Constructor 
     * 
     * @param communicationPort
     * @param writer 
     */
    public AnimasIR1200(String communicationPort, OutputWriter writer)
    {
        super(communicationPort, writer);
    }
    
    
    /**
     * Constructor
     * 
     * @param cmp
     */
    public AnimasIR1200(AbstractDeviceCompany cmp)
    {
        super(cmp);
    }
    

    /**
     * getName - Get Name of meter. 
     * 
     * @return name of meter
     */
    public String getName()
    {
        return this.getPumpDeviceDefinition().getDeviceName();
    }


    
    /**
     * getIconName - Get Icon of meter
     * 
     * @return icon name
     */
    public String getIconName()
    {
        return this.getPumpDeviceDefinition().getIconName();
    }
    

    /**
     * getDeviceId - Get Device Id, within MgrCompany class 
     * Should be implemented by device class.
     * 
     * @return id of device within company
     */
    public int getDeviceId()
    {
        return this.getPumpDeviceDefinition().getDeviceId();
    }


    public PumpDeviceDefinition getPumpDeviceDefinition()
    {
        return PumpDeviceDefinition.Animas_IR1200;
    }

    
    /**
     * getInstructions - get instructions for device
     * Should be implemented by meter class.
     * 
     * @return instructions for reading data 
     */
    public String getInstructions()
    {
        return this.getPumpDeviceDefinition().getInstructionsI18nKey();
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
    public DeviceImplementationStatus getImplementationStatus()
    {
        return this.getPumpDeviceDefinition().getDeviceImplementationStatus();
    }
    
    
    /**
     * getDeviceClassName - Get Class name of device implementation, used by Reflection at later time
     * 
     * @return class name as string
     */
    public String getDeviceClassName()
    {
        return this.getClass().getName();
    }


    /** 
     * Get Max Memory Records
     */
    public int getMaxMemoryRecords()
    {
        return 0;
    }

    public void loadPumpSpecificValues()
    {

    }

    @Override
    public AnimasDeviceType getAnimasDeviceType()
    {
        return (AnimasDeviceType)this.getPumpDeviceDefinition().getInternalDefintion();
    }


    /**
     * Get Download Support Type
     * 
     * @return
     */
    public int getDownloadSupportType()
    {
        return DownloadSupportType.DOWNLOAD_CONFIG_FROM_DEVICE | DownloadSupportType.DOWNLOAD_FROM_DEVICE;
    }
    
    
    /**
     * How Many Months Of Data Stored
     * 
     * @return
     */
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
    public String getTemporaryBasalTypeDefinition()
    {
        return "TYPE=Procent;STEP=10;MIN=-100;MAX=100";
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


    @Override
    public void readConfiguration() throws PlugInBaseException
    {
        handler.readConfiguration(this.getPumpDeviceDefinition(), this.connectionParameters, this.outputWriter);


        try
        {
            AnimasPumpDeviceReader reader = new AnimasPumpDeviceReader(this.communicationPort, this.getAnimasDeviceType(), this.outputWriter);
            reader.downloadPumpSettings();
        }
        catch(AnimasException ex)
        {
            throw new PlugInBaseException(ex);
        }

    }


    @Override
    public void readDeviceDataFull() throws PlugInBaseException
    {
        try
        {
            AnimasPumpDeviceReader reader = new AnimasPumpDeviceReader(this.communicationPort, this.getAnimasDeviceType(), this.outputWriter);
            reader.downloadPumpData();
        }
        catch(AnimasException ex)
        {
            throw new PlugInBaseException(ex);
        }
    }


}
