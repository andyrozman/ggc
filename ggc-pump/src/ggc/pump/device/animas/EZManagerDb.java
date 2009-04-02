package ggc.pump.device.animas;

import ggc.plugin.device.DeviceIdentification;
import ggc.plugin.device.PlugInBaseException;
import ggc.plugin.manager.DeviceImplementationStatus;
import ggc.plugin.output.OutputWriter;
import ggc.plugin.protocol.ConnectionProtocols;
import ggc.pump.device.AbstractPump;

import java.util.Hashtable;

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
 *  Filename:  ###---###  
 *  Description:
 * 
 *  Author: Andy {andy@atech-software.com}
 */




/**
 * @author innominate
 *
 */
public abstract class EZManagerDb extends AbstractPump
{
    
    //private OutputWriter m_writer;
    
    @SuppressWarnings("unused")
    private String m_fileName;
    
    
    /**
     * 
     */
    public static final int PUMP_ANIMAS_X1               = 40001;
    /**
     * 
     */
    public static final int PUMP_ANIMAS_TEST             = 40099;
    
    
    
    
    /**
     * Constructor
     */
    public EZManagerDb()
    {
        super();
    }
    
    
    /**
     * Constructor
     * 
     * @param db_path 
     * @param writer 
     */
    public EZManagerDb(String db_path, OutputWriter writer)
    {        
        super(writer);
        this.m_fileName = db_path;
    }

    /**
     * Will be called, when the import is ended and freeing resources.
     */
    public void close() throws PlugInBaseException
    {
        //dont need to do anything here
        return;
    }

    /**
     * getComment - Get Comment for device 
     * 
     * @return comment or null
     */
    public String getComment()
    {
        return "This is a pseudo meter that reads BG values from an Animas Ez" +
        "Manager Database.";
    }

    /**
     * getCompanyId - Get Company Id 
     * 
     * @return id of company
     */
    public int getCompanyId()
    {
        // TODO: im not to sure what this is supposed to be -Nate
        return 0;
    }

    /**
     *
     */
    public String getConnectionPort()
    {
        return "No port";
    }

    /**
     * 
     */
    public int getConnectionProtocol()
    {
        return ConnectionProtocols.PROTOCOL_NONE;
    }

    /**
     * 
     */
    public String getDeviceClassName()
    {
        return "ggc.pump.device.animas.EZManagerDb";
    }

    /**
     * getDeviceInfo - get Device info (firmware and software revision)
     */
    public DeviceIdentification getDeviceInfo()
    {
        DeviceIdentification toRet = new DeviceIdentification(ic);  
        toRet.company = "Animas";
        toRet.device_family = "n/a";
        toRet.device_hardware_version = "n/a";
        toRet.device_identified = "? Identified ?";
        toRet.device_selected = "? Selected ?";
        toRet.device_serial_number = "n/a";
        toRet.device_software_version = "v 5.2.1 (Build 32)";        
        return toRet;
    }

    /**
     * 
     */
    public String getIconName()
    {
        return "no_meter.gif";
    }

    /**
     * getImplementationStatus - Get Company Id 
     * 
     * @return implementation status as number
     * @see ggc.plugin.manager.DeviceImplementationStatus
     */
    public int getImplementationStatus()
    {
        return DeviceImplementationStatus.IMPLEMENTATION_IN_PROGRESS;
    }

    /**
     * getInstructions - get instructions for device
     * 
     * @return instructions for reading data 
     */
    public String getInstructions()
    {
        return "Point the file browser \n to your EZManager Database file\n" +
        "It should be named \"ezmd.mdb\"";
    }

    /**
     * 
     */
    public int getMaxMemoryRecords()
    {
        return Integer.MAX_VALUE;
    }


    /**
     * Used for opening connection with device.
     * @return boolean - if connection established
     */
    public boolean open() throws PlugInBaseException
    {
        return true;
        /*
        JFileChooser chooser = new JFileChooser();
        FileNameExtensionFilter filter = new FileNameExtensionFilter(
            "MDB Files", "mdb");
        chooser.setFileFilter(filter);
        int returnVal = chooser.showOpenDialog(null);
        if(returnVal == JFileChooser.APPROVE_OPTION) 
        {
            m_fileName = chooser.getSelectedFile().getAbsolutePath();
           return true;
        }
        else
        {
            return false;        
        }*/
    }

    /** 
     * This is method for reading configuration
     * 
     * @throws PlugInBaseException
     */
    public void readConfiguration() throws PlugInBaseException
    {
    }

    
    /**
     * This is method for reading data from device. All reading from actual device should be done from here.
     * Reading can be done directly here, or event can be used to read data.
     */
    public void readDeviceDataFull() throws PlugInBaseException
    {
        /*
        ArrayList<PumpValuesEntry> data = getDataFull();
        
        if (data == null)
        {
            
        }
        else
        {
        
            
            for (MeterValuesEntry entry : data)
            {
                m_writer.writeBGData(entry);
            }
        }
        */
        
        // Nate: Writing should be done as soon as it happens, not 
    }

    /**
     * This is method for reading partitial data from device. All reading from actual device should be done from 
     * here. Reading can be done directly here, or event can be used to read data.
     */
    public void readDeviceDataPartitial() throws PlugInBaseException
    {
        // TODO Auto-generated method stub

    }

    /**
     * This is for reading device information. This should be used only if normal dump doesn't retrieve this
     * information (most dumps do). 
     * @throws PlugInBaseException
     */
    public void readInfo() throws PlugInBaseException
    {
        // TODO Auto-generated method stub

    }
    
    
    


    /**
     * Get Alarm Mappings - Map pump specific alarms to PumpTool specific 
     *     alarm codes
     * @return
     */
    public Hashtable<String, Integer> getAlarmMappings()
    {
        // TODO Auto-generated method stub
        return null;
    }


    /**
     * Get Event Mappings - Map pump specific events to PumpTool specific 
     *     event codes
     * @return
     */
    public Hashtable<String, Integer> getEventMappings()
    {
        // TODO Auto-generated method stub
        return null;
    }


    /**
     * loadPumpSpecificValues
     */
    public void loadPumpSpecificValues()
    {
        // TODO Auto-generated method stub
        
    }



    /**
     * Is Device Readable (there are some devices that are not actual devices, but are used to get some
     * sort of specific device data - in most cases we call them generics, and they don't have ability
     * to read data)
     * @return
     */
    public boolean isReadableDevice()
    {
        return true;
    }


    

}
