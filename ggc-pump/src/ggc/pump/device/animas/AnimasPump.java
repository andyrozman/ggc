package ggc.pump.device.animas;

import java.util.Hashtable;

import ggc.plugin.data.GGCPlugInFileReaderContext;
import ggc.plugin.device.PlugInBaseException;
import ggc.plugin.device.impl.animas.enums.AnimasDeviceType;
import ggc.plugin.manager.company.AbstractDeviceCompany;
import ggc.plugin.output.OutputWriter;
import ggc.pump.data.defs.*;
import ggc.pump.device.AbstractSerialPump;
import gnu.io.SerialPortEvent;

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
 *  Filename:     AnimasPump
 *  Description:  This is abstract class for Animas Pumps
 *
 *  Author: Andy Rozman {andy@atech-software.com}
 */

public abstract class AnimasPump extends AbstractSerialPump
{

    protected String communicationPort;
    //protected OutputWriter output_writer;
    //protected AbstractDeviceCompany device_company;
    
    
    public AnimasPump()
    {
        super();
    }

    
    /**
     * Constructor 
     * 
     * @param communicationPort
     * @param writer 
     */
    public AnimasPump(String communicationPort, OutputWriter writer)
    {
        super();
        this.communicationPort = communicationPort;
        this.outputWriter = writer;
    }
    
    
    /**
     * Constructor
     * 
     * @param cmp
     */
    public AnimasPump(AbstractDeviceCompany cmp)
    {
        super();
        this.deviceCompany = cmp;
    }
    
    
    
    
    
    
    
    
    
    
    @Override
    public void serialEvent(SerialPortEvent event)
    {
    }


    public void dispose()
    {
        // TODO Auto-generated method stub
    }


    
    
    
    
    
    
    
    public String getComment()
    {
        // TODO Auto-generated method stub
        return null;
    }


    public String getConnectionPort()
    {
        // TODO Auto-generated method stub
        return null;
    }




    public String getDeviceSpecialComment()
    {
        return "Test implementation";
    }


    public int getDownloadSupportType()
    {
        return 0;
    }


    public GGCPlugInFileReaderContext[] getFileDownloadTypes()
    {
        // TODO Auto-generated method stub
        return null;
    }




    public boolean hasIndeterminateProgressStatus()
    {
        // TODO Auto-generated method stub
        return false;
    }


    public boolean hasSpecialProgressStatus()
    {
        // TODO Auto-generated method stub
        return false;
    }


    public boolean isDeviceCommunicating()
    {
        // TODO Auto-generated method stub
        return false;
    }


    public boolean isFileDownloadSupported()
    {
        // TODO Auto-generated method stub
        return false;
    }


    public boolean isReadableDevice()
    {
        // TODO Auto-generated method stub
        return false;
    }


    public void readConfiguration() throws PlugInBaseException
    {

    }


    public void readDeviceDataFull() throws PlugInBaseException
    {
        // TODO Auto-generated method stub
        
    }


    public void readDeviceDataPartitial() throws PlugInBaseException
    {
        // TODO Auto-generated method stub
        
    }


    public void readInfo() throws PlugInBaseException
    {
        // TODO Auto-generated method stub
        
    }


    public long getItemId()
    {
        // TODO Auto-generated method stub
        return 0;
    }


    /**
     * Get Alarm Mappings - Map pump specific alarms to Pump Tool specific
     *     alarm codes
     * @return
     */
    public Hashtable<String, PumpAlarms> getAlarmMappings()
    {
        return null;
    }

    /**
     * Get Event Mappings - Map pump specific events to Pump Tool specific
     *     event codes
     * @return
     */
    public Hashtable<String, PumpEvents> getEventMappings()
    {
        return null;
    }


    /**
     * Get Error Mappings - Map pump specific errors to Pump Tool specific
     *     event codes
     * @return
     */
    public Hashtable<String, PumpErrors> getErrorMappings()
    {
        return null;
    }

    /**
     * Get Bolus Mappings - Map pump specific bolus to Pump Tool specific
     *     event codes
     * @return
     */
    public Hashtable<String, PumpBolusType> getBolusMappings()
    {
        return null;
    }

    /**
     * Get Report Mappings - Map pump specific reports to Pump Tool specific
     *     event codes
     * @return
     */
    public Hashtable<String, PumpReport> getReportMappings()
    {
        return null;
    }


    public void loadPumpSpecificValues()
    {


    }


    public abstract AnimasDeviceType getAnimasDeviceType();

}
