package ggc.plugin.device.impl.minimed;

import ggc.plugin.device.DeviceAbstract;
import ggc.plugin.device.DeviceIdentification;
import ggc.plugin.device.PlugInBaseException;
import ggc.plugin.device.impl.minimed.cmd.MinimedCommand;
import ggc.plugin.device.impl.minimed.comm.MinimedComm_ComLink;
import ggc.plugin.device.impl.minimed.comm.MinimedComm_ComStation;
import ggc.plugin.device.impl.minimed.comm.MinimedComm_Interface;
import ggc.plugin.device.impl.minimed.comm.MinimedComm_USBLink;
import ggc.plugin.manager.company.AbstractDeviceCompany;
import ggc.plugin.output.OutputWriter;
import ggc.plugin.util.DataAccessPlugInBase;

import java.util.Hashtable;


// TODO: Auto-generated Javadoc
/*
Phases (English):
  X    1 - Start minimed library outside browser and do reading
       2 - Create test environment, ter GGC library for Minimed, ter do simple communication with pump
       3 - Finialize all data reading
       4 - Decode Pump data
       5 - 1st phase integration to GGC (reading pump and writing to db)
       6 - 2nd phase integration into GGC (pump info, progress bar, etc.)
       7 - Reading CGMS data
       8 - Decoding CGMS data
       9 - Finalizing   
 
  
Faze (Slovene):
  X    1 - Zaženi minimedovo knjižnico izven browserja, ter uspešno izvedi branje
  .    2 - Naredi testno okolje, ter GGC knjižnico za Minimed, ter naredi preprosto povezavo na črpalko in neko komunikacijo s črpalko
       3 - Do konca naredi branje vseh potrebnih podatkov (za črpalko)
       4 - Dekodiraj prispele podatke
       5 - Prva faza integracije v GGC (branje pumpe in pisanje v bazo)
       6 - Druga faza integracije v GGC (uredi vse malenkosti - pump info, progress, itd).
       7 - Branje CGMS podatkov
       8 - Dekodiranje CGMS podatkov
       9 - Zaključek

*/


/**
 * Application:   GGC - GNU Gluco Control
 * Plug-in:       Pump Tool (support for Pump devices)
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
 * Filename:     Minimed512
 * Description:  Minimed 512/712 implementation (just settings)
 * 
 * Author: Andy {andy@atech-software.com}
 */

public abstract class MinimedDevice extends DeviceAbstract //implements PumpInterface, SelectableInterface
{

    /** The m_communication_protocol. */
    private MinimedComm_Interface m_communication_protocol = null;
    
    /** The m_error_code. */
    protected int m_error_code = 0;
    
    /** The util. */
    protected MinimedDeviceUtil util = MinimedDeviceUtil.getInstance();
    //protected DataAccessPump m_da = DataAccessPump.getInstance();
    //AbstractDeviceCompany abstract_device_company = null;
    //AbstractDeviceCompany pump_company = null;
    //OutputWriter output_writer = null;
    //boolean can_read_data = false; 
    //boolean can_read_partitial_data = false;
    //boolean can_read_device_info = false;
    //boolean can_read_device_configuration = false;
    
  
    
    
    /** Minimed Device Interface: Comstation. */
    public static final int INTERFACE_COMSTATION = 1;

    /** Minimed Device Interface: ComLink. */
    public static final int INTERFACE_COMLINK = 2;

    /** Minimed Device Interface: Paradigm Link Com. */
    public static final int INTERFACE_PARADIGM_LINK_COM = 3;

    /** Minimed Device Interface: Paradigm Link USB. */
    public static final int INTERFACE_PARADIGM_LINK_USB = 4;

    /** Minimed Device Interface: Carelink USB. */
    public static final int INTERFACE_CARELINK_USB = 5;
    
    
    /** Error: No Error. */
    public static final int ERROR_NO_ERROR = 0;
    
    /** Error: Communication protocol not supported. */
    public static final int ERROR_COMMUNICATION_PROTOCOL_NOT_SUPPORTED = 1;

    /** Error: Action not supported. */
    public static final int ERROR_ACTION_NOT_SUPPORTED = 2;

    /** Error: Pump Active Error. */
    public static final int ERROR_PUMP_ACTIVE_ERROR = 3;
    
    
    
    
    
    /**
     * Constructor.
     * 
     * @param device_type the device_type
     * @param full_port is packed "portocol_id;port;serial_id"
     * @param writer the writer
     * @param da the da
     */
    // 
    public MinimedDevice(DataAccessPlugInBase da, int device_type, String full_port, OutputWriter writer)
    {
        super(da, writer); //full_port, writer, );
        initDeviceSpecific();
        
        util.setDeviceType(device_type);
        util.setCombinedPort(full_port);
        
        System.out.println("Protocol ID: " + util.protocol_id );
        
        
        if ((device_type==MinimedDevicesIds.PUMP_MINIMED_508) ||
            (device_type==MinimedDevicesIds.PUMP_MINIMED_508c))
        {
            m_communication_protocol = new MinimedComm_ComStation(da);
        }
        else
        {
            if ((util.protocol_id==INTERFACE_COMLINK) || (util.protocol_id==INTERFACE_PARADIGM_LINK_COM))
            {
                m_communication_protocol = new MinimedComm_ComLink(da);
            }
            else
            {
                m_communication_protocol = new MinimedComm_USBLink(da);
            }
        }
        
    }


    
    /**
     * Constructor.
     * 
     * @param cmp the cmp
     * @param da the da
     */
    public MinimedDevice(DataAccessPlugInBase da, AbstractDeviceCompany cmp)
    {
        super(da);
        this.setDeviceCompany(cmp);
        //abstract_device_company = cmp;
    }

    
    /**
     * initDeviceSpecific - Device Specific Initialization.
     */
    public abstract void initDeviceSpecific();
    
    
    /**
     * Create Commands.
     */
    public abstract void createCommands();
    
    
    
    
    
    
    // package received:
    //   1- 167 
    //   2,3,4 - Serial1 Serial2 Serial3 (BCD packed)
    //   5 - CMD1
    //   6 - CMD2
    //   
    
    
    // package sent:
    //   Header
    //   1 - 10 (isUseMultiXmitMode), 5 (ParameterCount=0), 4 (ParameterCount<>0)
    //   2 - Element count (bytes)
    
    //   Message
    //   1 - 167 
    //   2-4 - Serial1 Serial2 Serial3 (BCD packed)
    //   5 - CMD1
    //   6 - CMD2
    //   7 - CRC8 of this package
    //   encoded
    
    

    /**
     * Are pump settings set.
     * 
     * @return true, if successful
     */
    public boolean arePumpSettingsSet()
    {
        // TODO Auto-generated method stub
        return false;
    }

    /**
     * Gets the alarm mappings.
     * 
     * @return the alarm mappings
     */
    public Hashtable<String, Integer> getAlarmMappings()
    {
        // TODO Auto-generated method stub
        return null;
    }

    /**
     * Gets the basal step.
     * 
     * @return the basal step
     */
    public float getBasalStep()
    {
        // TODO Auto-generated method stub
        return 0;
    }

    /**
     * Gets the bolus mappings.
     * 
     * @return the bolus mappings
     */
    public Hashtable<String, Integer> getBolusMappings()
    {
        // TODO Auto-generated method stub
        return null;
    }

    /**
     * Gets the bolus step.
     * 
     * @return the bolus step
     */
    public float getBolusStep()
    {
        // TODO Auto-generated method stub
        return 0;
    }

    /**
     * Gets the error mappings.
     * 
     * @return the error mappings
     */
    public Hashtable<String, Integer> getErrorMappings()
    {
        // TODO Auto-generated method stub
        return null;
    }

    /**
     * Gets the event mappings.
     * 
     * @return the event mappings
     */
    public Hashtable<String, Integer> getEventMappings()
    {
        // TODO Auto-generated method stub
        return null;
    }

    /**
     * Gets the max memory records.
     * 
     * @return the max memory records
     */
    public int getMaxMemoryRecords()
    {
        // TODO Auto-generated method stub
        return 0;
    }

    /**
     * Gets the report mappings.
     * 
     * @return the report mappings
     */
    public Hashtable<String, Integer> getReportMappings()
    {
        // TODO Auto-generated method stub
        return null;
    }

    /**
     * Gets the temporary basal type definition.
     * 
     * @return the temporary basal type definition
     */
    public String getTemporaryBasalTypeDefinition()
    {
        // TODO Auto-generated method stub
        return null;
    }

    /**
     * How many months of data stored.
     * 
     * @return the int
     */
    public int howManyMonthsOfDataStored()
    {
        // TODO Auto-generated method stub
        return 0;
    }

    /**
     * Load pump specific values.
     */
    public void loadPumpSpecificValues()
    {
        // TODO Auto-generated method stub
        
    }

    /* (non-Javadoc)
     * @see ggc.plugin.device.DeviceInterface#getComment()
     */
    public String getComment()
    {
        // TODO Auto-generated method stub
        return null;
    }



    /* (non-Javadoc)
     * @see ggc.plugin.device.DeviceInterface#getDownloadSupportType()
     */
    public int getDownloadSupportType()
    {
        return 0;
    }




    /* (non-Javadoc)
     * @see ggc.plugin.device.DeviceAbstract#hasSpecialProgressStatus()
     */
    public boolean hasSpecialProgressStatus()
    {
        return false;
    }

    
    /* (non-Javadoc)
     * @see ggc.plugin.device.DeviceAbstract#readConfiguration()
     */
    public void readConfiguration() throws PlugInBaseException
    {
        // TODO Auto-generated method stub
        
    }

    /* (non-Javadoc)
     * @see ggc.plugin.device.DeviceInterface#readDeviceDataFull()
     */
    public void readDeviceDataFull() throws PlugInBaseException
    {
        
        try
        {
            
            // initialize communication interface
            this.m_communication_protocol.initializeCommunicationInterface();
            
            // initalize device
            this.m_communication_protocol.initDevice();
            
    
            
            this.m_communication_protocol.executeCommand(MinimedCommand.COMMAND_HISTORY_DATA);
            
    /*        
            // close device
            this.m_communication_protocol.closeDevice();
            
            // close communication interface
            this.m_communication_protocol.closeCommunicationInterface();
      */      
        }
        catch(PlugInBaseException ex)
        {
            throw ex;
        }
        
        
    }

    /* (non-Javadoc)
     * @see ggc.plugin.device.DeviceAbstract#readDeviceDataPartitial()
     */
    public void readDeviceDataPartitial() throws PlugInBaseException
    {
        this.m_error_code = MinimedDevice.ERROR_ACTION_NOT_SUPPORTED;
    }

    /* (non-Javadoc)
     * @see ggc.plugin.device.DeviceAbstract#readInfo()
     */
    public void readInfo() throws PlugInBaseException
    {
        this.m_error_code = MinimedDevice.ERROR_ACTION_NOT_SUPPORTED;
    }
    
    
    /**
     * Close.
     * 
     * @throws PlugInBaseException the plug in base exception
     */
    public void close() throws PlugInBaseException
    {
        
        try
        {
            // close device
            this.m_communication_protocol.closeDevice();
            
            // close communication interface
            this.m_communication_protocol.closeCommunicationInterface();
            
        }
        catch(PlugInBaseException ex)
        {
            throw ex;
        }
        
        
    }
    
    
    
    /**
     * getDeviceInfo - get Device info (firmware and software revision).
     * 
     * @return the device info
     */
    public DeviceIdentification getDeviceInfo()
    {
        return this.output_writer.getDeviceIdentification();
    }


    /**
     * Open.
     * 
     * @return true, if successful
     * 
     * @throws PlugInBaseException the plug in base exception
     */
    public boolean open() throws PlugInBaseException
    {
        try
        {
            // initialize communication interface
            this.m_communication_protocol.initializeCommunicationInterface();
            
            // initalize device
            this.m_communication_protocol.initDevice();

        }
        catch(PlugInBaseException ex)
        {
            throw ex;
        }
        return false;
    }



    /* (non-Javadoc)
     * @see ggc.plugin.device.DeviceInterface#dispose()
     */
    public void dispose()
    {
        // TODO Auto-generated method stub
        
    }


    /* (non-Javadoc)
     * @see ggc.plugin.device.DeviceInterface#getConnectionPort()
     */
    public String getConnectionPort()
    {
        // TODO Auto-generated method stub
        return null;
    }


    /* (non-Javadoc)
     * @see ggc.plugin.device.DeviceInterface#getConnectionProtocol()
     */
    public int getConnectionProtocol()
    {
        // TODO Auto-generated method stub
        return 0;
    }




    /* (non-Javadoc)
     * @see ggc.plugin.device.DeviceAbstract#getDeviceSpecialComment()
     */
    public String getDeviceSpecialComment()
    {
        return "This is experimental download.";
    }




    /* (non-Javadoc)
     * @see ggc.plugin.device.DeviceAbstract#hasIndeterminateProgressStatus()
     */
    public boolean hasIndeterminateProgressStatus()
    {
        return false;
    }


    /* (non-Javadoc)
     * @see ggc.plugin.device.DeviceInterface#isDeviceCommunicating()
     */
    public boolean isDeviceCommunicating()
    {
        // TODO Auto-generated method stub
        return false;
    }




    /* (non-Javadoc)
     * @see ggc.plugin.device.DeviceAbstract#isReadableDevice()
     */
    public boolean isReadableDevice()
    {
        return true;
    }




    /**
     * Get Item Id.
     * 
     * @return the item id
     */
    public long getItemId()
    {
        return 0;
    }
    
    
}
