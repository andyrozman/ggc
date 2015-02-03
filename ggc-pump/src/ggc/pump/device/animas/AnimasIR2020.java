package ggc.pump.device.animas;

import ggc.plugin.device.DownloadSupportType;
import ggc.plugin.device.impl.animas.enums.AnimasDeviceType;
import ggc.plugin.manager.DeviceImplementationStatus;
import ggc.plugin.manager.company.AbstractDeviceCompany;
import ggc.plugin.output.OutputWriter;
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
 *  Filename:     AnimasIR2020  
 *  Description:  Animas IR 2020 implementation
 *
 *  Author: Andy Rozman {andy@atech-software.com}
 */

public class AnimasIR2020 extends AnimasIR1200
{

    /**
     * Constructor 
     */
    public AnimasIR2020()
    {
        super();
    }
    
    
    /**
     * Constructor 
     * 
     * @param communicationPort
     * @param writer 
     */
    public AnimasIR2020(String communicationPort, OutputWriter writer)
    {
        super(communicationPort, writer);
    }
    
    
    /**
     * Constructor
     * 
     * @param cmp
     */
    public AnimasIR2020(AbstractDeviceCompany cmp)
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
        return "IR 2020 (1275/1275i)";
    }



    /**
     * {@inheritDoc}
     */
    public String getIconName()
    {
        return "an_ir2020.jpg";
    }


    /**
     * {@inheritDoc}
     */
    public int getDeviceId()
    {
        return PumpDevicesIds.PUMP_ANIMAS_IR_2020;
    }


    /**
     * {@inheritDoc}
     */
    public float getBolusStep()
    {
        return 0.05f;
    }
    
    
    /**
     * Get Basal Step (precission)
     * 
     * @return
     */
    public float getBasalStep()
    {
        return 0.05f;
    }
    
    
    /**
     * Are Pump Settings Set (Bolus step, Basal step and TBR settings)
     * 
     * @return
     */
    public boolean arePumpSettingsSet()
    {
        return true;
    }

    @Override
    public AnimasDeviceType getAnimasDeviceType()
    {
        return AnimasDeviceType.Animas_IR1285;
    }
}

