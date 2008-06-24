package ggc.meter.device.ascensia;

import ggc.meter.output.OutputWriter;


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
 *  Filename: AscensiaBreezeMeter.java
 *  Purpose:  This class is used for data retrival from Ascensia Breeze Meter and
 *            implements SerialProtocol.
 *
 *  Author:   andyrozman {andyrozman@sourceforge.net}
 *
 */

public class AscensiaBreeze2 extends AscensiaMeter //SerialProtocol
{

    public AscensiaBreeze2()
    {
    }
    
    
    public AscensiaBreeze2(String portName, OutputWriter writer)
    {
    	super(portName, writer);
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
        return "Breeze2";
    }


    /**
     * getIconName - Get Icon of meter
     * 
     * @return icon name
     */
    public String getIconName()
    {
        return "ascensia_breeze.png";
    }
    

    /**
     * getMeterId - Get Meter Id, within Meter Company class 
     * 
     * @return id of meter within company
     */
    public int getMeterId()
    {
        return AscensiaMeter.METER_ASCENSIA_BREEZE2;
    }

    

    
    /**
     * getCompanyId - Get Company Id 
     * 
     * @return id of company
     */
    public int getCompanyId()
    {
        return AscensiaMeter.ASCENSIA_COMPANY;
    }
    
    
    
    public String getDeviceClassName()
    {
        return "ggc.meter.device.ascensia.AscensiaBreeze2";
    }

    
    
    
    /**
     * getInstructions - get instructions for device
     * 
     * @return instructions for reading data 
     */
    public String getInstructions()
    {
        return "INSTRUCTIONS_ASCENSIA_BREEZE";
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
     * getImplementationStatus - Get Company Id 
     * 
     * @return implementation status as number
     * @see ggc.meter.manager.MeterImplementationStatus
     */
    public int getImplementationStatus() 
    {
        return 0;
    }
    
    public int getMaxMemoryRecords()
    {
        return 420;
    }
    
    
}
