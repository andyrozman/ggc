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
 *  Filename: MeterManager.java
 *  Purpose:  This class contains all definitions for Meters. This includes:
 *        meter names, classes that handle meter and all other relevant data.
 *
 *  Author:   andyrozman
 */


package ggc.meter.manager.company; 

import ggc.meter.device.accuchek.AccuChekActive;
import ggc.meter.device.accuchek.AccuChekAdvantage;
import ggc.meter.device.accuchek.AccuChekAviva;
import ggc.meter.device.accuchek.AccuChekComfort;
import ggc.meter.device.accuchek.AccuChekCompact;
import ggc.meter.device.accuchek.AccuChekCompactPlus;
import ggc.meter.device.accuchek.AccuChekGo;
import ggc.meter.device.accuchek.AccuChekIntegra;
import ggc.meter.device.accuchek.AccuChekPerforma;
import ggc.meter.device.accuchek.AccuChekSensor;
import ggc.meter.device.accuchek.AccuChekSmartPix;
import ggc.meter.manager.MeterImplementationStatus;

public class Roche extends AbstractMeterCompany
{

    
    public Roche()
    {
        //this.addDevice(new AccuChekSmartPix());
        this.addDevice(new AccuChekActive());
        this.addDevice(new AccuChekAdvantage());
        this.addDevice(new AccuChekAviva());
        this.addDevice(new AccuChekComfort());
        this.addDevice(new AccuChekCompact());
        this.addDevice(new AccuChekCompactPlus());
        this.addDevice(new AccuChekGo());
        this.addDevice(new AccuChekIntegra());
        this.addDevice(new AccuChekPerforma());
        this.addDevice(new AccuChekSensor());
    }



    
    
    
    //********************************************************
    //***      Meter Company Identification Methods        ***
    //********************************************************


    /**
     * getName - Get Name of meter. 
     * 
     * @return name of meter
     */
    public String getName()
    {
        return "Roche";
    }

    
    /**
     * getCompanyId - Get Company Id 
     * 
     * @return id of company
     */
    public int getCompanyId()
    {
        return 2;
    }
    
    
    /**
     * getInstructions - get instructions for device
     * 
     * @return instructions for reading data 
     */
    public String getDescription()
    {
       return "ROCHE_DESC"; 
    }
    
    
    
    /**
     * getImplementationStatus - Get Implementation status 
     * 
     * @return implementation status as number
     * @see ggc.meter.manager.MeterImplementationStatus
     */
    public int getImplementationStatus()
    {
        return MeterImplementationStatus.IMPLEMENTATION_IN_PROGRESS;
    }
    
  
    public String getConnectionSample()
    {
        return "G:";
    }
    
    
    public String getConnectionSamples()
    {
        return m_ic.getMessage("MASS_STORAGE");  //"Serial Ports: COM2,...";
    }
    
    
    
}
