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

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

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
 X    1 - Zazeni minimedovo knjiznico izven browserja, ter uspesno izvedi branje
 .    2 - Naredi testno okolje, ter GGC knjiÅ¾nico za Minimed, ter naredi preprosto povezavo na crpalko in neko komunikacijo s Ä�rpalko
 3 - Do konca naredi branje vseh potrebnih podatkov (za Ä�rpalko)
 4 - Dekodiraj prispele podatke
 5 - Prva faza integracije v GGC (branje pumpe in pisanje v bazo)
 6 - Druga faza integracije v GGC (uredi vse malenkosti - pump info, progress, itd).
 7 - Branje CGMS podatkov
 8 - Dekodiranje CGMS podatkov
 9 - ZakljuÄ�ek

 */

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
 *  Filename:     Minimed512  
 *  Description:  Minimed 512/712 implementation (just settings)
 * 
 *  Author: Andy {andy@atech-software.com}
 */

public abstract class MinimedDevice extends DeviceAbstract // implements
                                                           // PumpInterface,
                                                           // SelectableInterface
{

    private MinimedComm_Interface m_communication_protocol = null;
    protected int m_error_code = 0;
    protected MinimedDeviceUtil util = null; // MinimedDeviceUtil.getInstance();
    // protected DataAccessPump dataAccess = DataAccessPump.getInstance();
    protected DataAccessPlugInBase m_da = null;
    // AbstractDeviceCompany abstract_device_company = null;
    // AbstractDeviceCompany pump_company = null;
    // OutputWriter outputWriter = null;
    // boolean canReadData = false;
    // boolean canReadPartitialData = false;
    // boolean canReadDeviceInfo = false;
    // boolean canReadDeviceConfiguration = false;

    private static Log log = LogFactory.getLog(MinimedDevice.class);

    /**
     * Minimed Device Interface: Comstation
     */
    public static final int INTERFACE_COMSTATION = 1;

    /**
     * Minimed Device Interface: ComLink
     */
    public static final int INTERFACE_COMLINK = 2;

    /**
     * Minimed Device Interface: Paradigm Link Com
     */
    public static final int INTERFACE_PARADIGM_LINK_COM = 3;

    /**
     * Minimed Device Interface: Paradigm Link USB
     */
    public static final int INTERFACE_PARADIGM_LINK_USB = 4;

    /**
     * Minimed Device Interface: Carelink USB
     */
    public static final int INTERFACE_CARELINK_USB = 5;

    /**
     * Error: No Error.
     */
    public static final int ERROR_NO_ERROR = 0;

    /**
     * Error: Communication protocol not supported
     */
    public static final int ERROR_COMMUNICATION_PROTOCOL_NOT_SUPPORTED = 1;

    /**
     * Error: Action not supported
     */
    public static final int ERROR_ACTION_NOT_SUPPORTED = 2;

    /**
     * Error: Pump Active Error
     */
    public static final int ERROR_PUMP_ACTIVE_ERROR = 3;

    /**
     * Constructor
     * 
     * @param device_type
     * @param full_port is packed "portocol_id;port;serial_id"
     * @param writer
     */
    //
    public MinimedDevice(DataAccessPlugInBase da, int device_type, String full_port, OutputWriter writer)
    {
        super(da, writer); // full_port, writer, );
        this.m_da = da;

        this.util = MinimedDeviceUtil.createInstance(da, this);
        initDeviceSpecific();

        util.setDeviceType(device_type);
        util.setCombinedPort(full_port);

        System.out.println("Protocol ID: " + util.protocol_id);

        if (device_type == MinimedDevicesIds.PUMP_MINIMED_508 || device_type == MinimedDevicesIds.PUMP_MINIMED_508c)
        {
            m_communication_protocol = new MinimedComm_ComStation(this);
            this.util.device_stopped = true;
            this.util.device_stopped_exception = new PlugInBaseException(
                    "This communication protocol (Comstation for 508/508c) is not supported.");
        }
        else
        {
            if (util.protocol_id == INTERFACE_COMLINK || util.protocol_id == INTERFACE_PARADIGM_LINK_COM)
            {
                m_communication_protocol = new MinimedComm_ComLink(this);
            }
            else
            {
                m_communication_protocol = new MinimedComm_USBLink(this);
                this.util.device_stopped = true;
                this.util.device_stopped_exception = new PlugInBaseException(
                        "This communication protocol (Comlink USB) is not YET supported.");
            }
        }

        if (this.util.isCommunicationStopped())
            return;

        this.util.setCommunicationInterface(m_communication_protocol);
        createCommands();

    }

    /**
     * Constructor
     * 
     * @param cmp
     */
    public MinimedDevice(DataAccessPlugInBase da, AbstractDeviceCompany cmp)
    {
        super(da);
        this.setDeviceCompany(cmp);
        // abstract_device_company = cmp;
    }

    /**
     * initDeviceSpecific - Device Specific Initialization
     */
    public abstract void initDeviceSpecific();

    /**
     * Create Commands
     */
    public abstract void createCommands();

    public DataAccessPlugInBase getDataAccess()
    {
        return this.m_da;
    }

    // package received:
    // 1- 167
    // 2,3,4 - Serial1 Serial2 Serial3 (BCD packed)
    // 5 - CMD1
    // 6 - CMD2
    //

    // package sent:
    // Header
    // 1 - 10 (isUseMultiXmitMode), 5 (ParameterCount=0), 4 (ParameterCount<>0)
    // 2 - Element count (bytes)

    // Message
    // 1 - 167
    // 2-4 - Serial1 Serial2 Serial3 (BCD packed)
    // 5 - CMD1
    // 6 - CMD2
    // 7 - CRC8 of this package
    // encoded

    public boolean arePumpSettingsSet()
    {
        // TODO Auto-generated method stub
        return false;
    }



    public float getBasalStep()
    {
        // TODO Auto-generated method stub
        return 0;
    }



    public float getBolusStep()
    {
        // TODO Auto-generated method stub
        return 0;
    }



    public int getMaxMemoryRecords()
    {
        // TODO Auto-generated method stub
        return 0;
    }



    public String getTemporaryBasalTypeDefinition()
    {
        // TODO Auto-generated method stub
        return null;
    }

    public int howManyMonthsOfDataStored()
    {
        // TODO Auto-generated method stub
        return 0;
    }



    public String getComment()
    {
        // TODO Auto-generated method stub
        return null;
    }

    public int getDownloadSupportType()
    {
        return 0;
    }

    @Override
    public boolean hasSpecialProgressStatus()
    {
        return false;
    }

    @Override
    public void readConfiguration() throws PlugInBaseException
    {
        // TODO Auto-generated method stub

    }

    public void readDeviceDataFull() throws PlugInBaseException
    {
        log.debug("readDeviceDataFull");

        if (this.util.isCommunicationStopped())
        {
            log.debug("readDeviceDataFull:: Communication with pump was closed.");
            return;
        }

        try
        {

            // initialize communication interface
            this.m_communication_protocol.initializeCommunicationInterface();

            if (this.util.isCommunicationStopped())
            {
                log.debug("readDeviceDataFull:: Communication with pump was closed.");
                return;
            }

            // initalize device
            this.m_communication_protocol.initDevice();

            if (this.util.isCommunicationStopped())
            {
                log.debug("readDeviceDataFull:: Communication with pump was closed.");
                return;
            }

            // this.m_communication_protocol.executeCommand(MinimedCommand.COMMAND_HISTORY_DATA);

            // close device
            // this.m_communication_protocol.closeDevice();

            // this.m_communication_protocol.closeCommunicationInterface();

        }
        catch (PlugInBaseException ex)
        {
            throw ex;
        }
        finally
        {
            // close communication interface
            this.m_communication_protocol.closeCommunicationInterface();
        }

    }

    @Override
    public void readDeviceDataPartitial() throws PlugInBaseException
    {
        this.m_error_code = MinimedDevice.ERROR_ACTION_NOT_SUPPORTED;
    }

    @Override
    public void readInfo() throws PlugInBaseException
    {
        this.m_error_code = MinimedDevice.ERROR_ACTION_NOT_SUPPORTED;
    }

    public void close() throws PlugInBaseException
    {

        try
        {
            // close device
            this.m_communication_protocol.closeDevice();

            // close communication interface
            this.m_communication_protocol.closeCommunicationInterface();

        }
        catch (PlugInBaseException ex)
        {
            throw ex;
        }

    }

    /**
     * getDeviceInfo - get Device info (firmware and software revision)
     */
    @Override
    public DeviceIdentification getDeviceInfo()
    {
        return this.outputWriter.getDeviceIdentification();
    }

    public boolean open() throws PlugInBaseException
    {
        try
        {
            // initialize communication interface
            this.m_communication_protocol.initializeCommunicationInterface();

            // initalize device
            this.m_communication_protocol.initDevice();

        }
        catch (PlugInBaseException ex)
        {
            throw ex;
        }
        return false;
    }

    public void dispose()
    {
        // TODO Auto-generated method stub

    }

    public String getConnectionPort()
    {
        // TODO Auto-generated method stub
        return null;
    }

    public int getConnectionProtocol()
    {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public String getDeviceSpecialComment()
    {
        return "This is experimental download.";
    }

    @Override
    public boolean hasIndeterminateProgressStatus()
    {
        return false;
    }

    public boolean isDeviceCommunicating()
    {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public boolean isReadableDevice()
    {
        return true;
    }

    /** 
     * Get Item Id
     */
    @Override
    public long getItemId()
    {
        return 0;
    }

    public abstract Object convertDeviceReply(MinimedCommand mc);

    public abstract int getMinimedDeviceId();

}
