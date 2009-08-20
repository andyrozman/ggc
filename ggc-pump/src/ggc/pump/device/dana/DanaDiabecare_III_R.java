package ggc.pump.device.dana;

import ggc.plugin.data.DeviceValuesWriter;
import ggc.plugin.device.DeviceIdentification;
import ggc.plugin.device.DownloadSupportType;
import ggc.plugin.device.PlugInBaseException;
import ggc.plugin.manager.DeviceImplementationStatus;
import ggc.plugin.manager.company.AbstractDeviceCompany;
import ggc.plugin.output.AbstractOutputWriter;
import ggc.plugin.output.OutputWriter;
import ggc.plugin.protocol.SerialProtocol;
import ggc.pump.data.PumpTempValues;
import ggc.pump.data.PumpValuesEntryProfile;
import ggc.pump.data.defs.PumpAdditionalDataType;
import ggc.pump.data.defs.PumpAlarms;
import ggc.pump.data.defs.PumpBaseType;
import ggc.pump.data.defs.PumpBolusType;
import ggc.pump.data.defs.PumpErrors;
import ggc.pump.data.defs.PumpEvents;
import ggc.pump.data.defs.PumpReport;
import ggc.pump.data.profile.ProfileSubEntry;
import ggc.pump.device.AbstractBlueToothPump;
import ggc.pump.manager.PumpDevicesIds;
import ggc.pump.manager.company.Sooil;
import ggc.pump.util.DataAccessPump;

import java.util.GregorianCalendar;
import java.util.Hashtable;

import javax.comm.SerialPort;
import javax.comm.SerialPortEvent;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.atech.utils.ATechDate;
import com.atech.utils.HexUtils;


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
 *  Filename:     DanaDiabecare_III  
 *  Description:  Dana Diabecare R/III device implementation 
 * 
 *  Author: Andy {andy@atech-software.com}
 */



// TODO: Auto-generated Javadoc
// OLD Implementation
public class DanaDiabecare_III_R extends AbstractBlueToothPump //SerialProtocol //extends DanaPump //implements SerialProtocol
{

    
    HexUtils hex_utils = new HexUtils();
    private static Log log = LogFactory.getLog(DanaDiabecare_III_R.class);
    private boolean device_communicating = true;
    int entries_current = 0;
    int entries_max = 100;    
    
    /**
     * Command: Connect
     */
    public static byte[] COMMAND_CONNECT = new byte[] { 0x30, 1 };
    
    /**
     * Command: Disconnect
     */
    public static byte[] COMMAND_DISCONNECT = new byte[] { 0x30, 2 };

    
    /**
     * Command: Shipping
     */
    public static byte[] COMMAND_SHIPPING = new byte[] { 50, 7 };


    
    /**
     * Command: Basal Pattern
     */
    public static byte[] COMMAND_BASAL_PATTERN = new byte[] { 50, 2 };
    

    
    
    
    public static byte[] SYNC_QUERY_BASAL_PROFILE = new byte[] { 50, 6 };
    
    public static byte[] SYNC_QUERY_BOLUS = new byte[] { 50, 1 };

    public static byte[] SYNC_QUERY_CARBO = new byte[] { 50, 4 };
    
    public static byte[] SYNC_QUERY_GENER = new byte[] { 50, 3 };
    
    public static byte[] SYNC_QUERY_GLUCOMODE = new byte[] { 50, 9 };
    
    public static byte[] SYNC_QUERY_MAX = new byte[] { 50, 5 };
    
    public static byte[] SYNC_QUERY_PWM = new byte[] { 50, 8 };
    
    public static byte[] SYNC_QUERY_SHIPPING = new byte[] { 50, 7 };
    
    
    
    
    
    
    String[] records_cmd = { "", "Bolus", "Daily", "Prime", "Glucose", "Alarm", "Error", "Carbo" };
    
    /**
     * Command for Records: Alarms
     */
    public static final byte[] COMMAND_RECORD_ALARM = new byte[] { 0x31, 5 };
    
    /**
     * Command for Records: Bolus
     */
    public static final byte[] COMMAND_RECORD_BOLUS = new byte[] { 0x31, 1 };
    
    /**
     * Command for Records: Carbohydrates
     */
    public static final byte[] COMMAND_RECORD_CARBO = new byte[] { 0x31, 7 };
    
    /**
     * Command for Records: Daily Insulin Usage
     */
    public static final byte[] COMMAND_RECORD_DAILY = new byte[] { 0x31, 2 };
    
    /**
     * Command for Records: Errors
     */
    public static final byte[] COMMAND_RECORD_ERROR = new byte[] { 0x31, 6 };
    
    /**
     * Command for Records: Glucose Entries
     */
    public static final byte[] COMMAND_RECORD_GLUCOSE = new byte[] { 0x31, 4 };
    
    /**
     * Command for Records: Primes
     */
    public static final byte[] COMMAND_RECORD_PRIME = new byte[] { 0x31, 3 };
    
    
    /**
     * Record Type: Alarm
     */
    public static final byte RECORD_TYPE_ALARM = 5;
    
    /**
     * Record Type: Bolus
     */
    public static final byte RECORD_TYPE_BOLUS = 1;
    
    /**
     * Record Type: Carbohydrates
     */
    public static final byte RECORD_TYPE_CARBO = 8;
    
    /**
     * Record Type: Daily Insulin Usage
     */
    public static final byte RECORD_TYPE_DAILY = 2;
    
    /**
     * Record Type: Error
     */
    public static final byte RECORD_TYPE_ERROR = 4;
    
    /**
     * Record Type: Glucose
     */
    public static final byte RECORD_TYPE_GLUCOSE = 6;
    
    /**
     * Record Type: Prime
     */
    public static final byte RECORD_TYPE_PRIME = 3;
    
    
    
    /**
     * Constructor 
     */
    public DanaDiabecare_III_R()
    {
        super();
        loadPumpSpecificValues();
    }
    

    /**
     * Constructor
     * 
     * @param cmp
     */
    public DanaDiabecare_III_R(AbstractDeviceCompany cmp)
    {
        super(cmp);
        loadPumpSpecificValues();
    }
    

    
    /**
     * Constructor
     * 
     * @param portName the port name
     * @param writer 
     */
    public DanaDiabecare_III_R(String portName, OutputWriter writer)
    {
        
        super(); //DataAccessPump.getInstance()); 

        // communcation settings for this meter(s)
        this.setCommunicationSettings( 
                  19200,
                  SerialPort.DATABITS_8, 
                  SerialPort.STOPBITS_1, 
                  SerialPort.PARITY_NONE,
                  SerialPort.FLOWCONTROL_NONE,
                  SerialProtocol.SERIAL_EVENT_BREAK_INTERRUPT|SerialProtocol.SERIAL_EVENT_OUTPUT_EMPTY);
        
        loadPumpSpecificValues();
        
        
        // output writer, this is how data is returned (for testing new devices, we can use Consol
        this.output_writer = writer; 
        //this.output_writer.getOutputUtil().setMaxMemoryRecords(this.getMaxMemoryRecords());

        // set meter type (this will be deprecated in future, but it's needed for now
        this.setPumpType("Sooil (Dana)", this.getName());

        // set device company (needed for now, will also be deprecated)
        this.setDeviceCompany(new Sooil());
        

        // settting serial port in com library
        try
        {
            this.setSerialPort(portName);
    
            if (!open())
            {
                setDeviceStopped();
                return;
            }
            
            this.output_writer.writeHeader();

        }
        catch(javax.comm.NoSuchPortException ex)
        {
            log.error("Port [" + portName + "] not found");
            setDeviceStopped();
        }
        catch(Exception ex)
        {
            log.error("Exception on create:" + ex, ex);
            this.setDeviceStopped();
        }
    }
    
    
    
    /**
     * getName - Get Name of device 
     * 
     * @return name of device
     */
    public String getName()
    {
        return "Diabcare II R (III)";
    }
    
    
    
    /**
     * getIconName - Get Icon of device
     * 
     * @return icon name
     */
    public String getIconName()
    {
        return "so_danaIII.jpg";
    }
    
    
    /**
     * getDeviceId - Get Device Id, within MgrCompany class 
     * Should be implemented by device class.
     * 
     * @return id of device within company
     */
    public int getDeviceId()
    {
        return PumpDevicesIds.PUMP_DANA_DIABECARE_III_R;
    }
    
    
    /**
     * getInstructions - get instructions for device
     * Should be implemented by meter class.
     * 
     * @return instructions for reading data 
     */
    public String getInstructions()
    {
        return "INSTRUCTIONS_DANA_III_R";
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
        return DeviceImplementationStatus.IMPLEMENTATION_TESTING;
    }
    
    
    /**
     * getDeviceClassName - Get Class name of device implementation, used by Reflection at later time
     * 
     * @return class name as string
     */
    public String getDeviceClassName()
    {
        return "ggc.pump.device.dana.DanaDiabecare_III_R";
    }
    
    
    
    
    /** 
     * Get Max Memory Records
     * 
     * @return 
     */
    public int getMaxMemoryRecords()
    {
        return -1;
    }



    private void connect() throws Exception
    {
        try
        {
            this.writeData(COMMAND_CONNECT);
            waitTime(1000);
            this.readData(new byte[0x100]);
            waitTime(200);
        }
        catch (Exception ex)
        {
            log.error("Error on connect. Ex: " + ex, ex);
            throw ex;
        }
    }

    private void disconnect() throws Exception
    {
        try
        {
            this.writeData(COMMAND_DISCONNECT);
            waitTime(1000);
            this.readData(new byte[0x100]);
            waitTime(200);
        }
        catch (Exception ex)
        {
            log.error("Error on disconnect. Ex: " + ex, ex);
            throw ex;
        }
    }

    
    
    
    
    
    
    
    
    Hashtable<String,String> dtypes = new Hashtable<String,String>();
    
    ATechDate atd_1 = new ATechDate(ATechDate.FORMAT_DATE_AND_TIME_S, 0);
    byte old_record_code = -1;
    int old_record_value = -1;
    
    
    /**
     * Gets the device record.
     * 
     * @param command 
     * @param write 
     * 
     
     * @throws Exception the exception
     */
    public void getDeviceRecord(byte[] command, boolean write) throws Exception
    {
        try
        {
            boolean flag;
            
            log.debug("getDeviceRecord (" + command[1] + "/" +  records_cmd[command[1]] + "):Start");
            
            byte[] buffer = new byte[0x200];
            int num2 = 0;
            
            
            this.writeData(command);
            waitTime(1000);
            
            
            flag = true;

            while (flag)
            {

                
                try
                {
                    num2 = (this.readData(buffer) - 2) / 10;
                }
                catch (Exception exception1)
                {
                    log.error("getDeviceRecord. Ex: " + exception1.getMessage(), exception1);
                    log.debug("getDeviceRecord(" + command[1] + "):End");
                    flag = false;
                    break;
                }
                
                if (num2==0)
                {
                    flag = false;
                    break;
                }
                
                hex_utils.readByteArray(buffer);
                
                ATechDate atd = getDateTime(hex_utils.getByteFromArray(7),  
                    hex_utils.getByteFromArray(8), // m
                    hex_utils.getByteFromArray(9), // d
                    hex_utils.getByteFromArray(10), // h
                    hex_utils.getByteFromArray(11), // m
                    hex_utils.getByteFromArray(12) // s
                );
                
                byte record_code = hex_utils.getByteFromArray(13);
                int record_value = hex_utils.getIntFromArray(14);
                
                if ((atd.equals(this.atd_1)) && (old_record_code == record_code) && (old_record_value == record_value))
                {
                    break;
                }
                else
                {
                    this.atd_1 = atd;
                    this.old_record_code = record_code;
                    this.old_record_value = record_value;
                }

                String key = hex_utils.getByteFromArray(6) + "_" + record_code;
                
                if (write)
                {
                    if (!writeData(key, atd, hex_utils.getByteFromArray(6), record_value))
                        System.out.println("date: " + atd.getDateTimeString() + ", code: " + record_code + ", value=" + record_value ); //+", value2=" + record_value2);
                }

                waitTime(num2 * 100);
            }
            
        }
        catch (Exception ex)
        {
            throw new Exception("getDeviceRecord (" + command[1] + ")" + ex.getMessage());
        }
        finally
        {
            log.debug("getDeviceRecord(" + command[1] + "):End");
        }
        
    }

    
    
    
    private boolean writeData(String key, ATechDate atd, int type, int value)
    {
        String v = "";
        
        if ((type==RECORD_TYPE_BOLUS)  || (type==RECORD_TYPE_DAILY) || (type==RECORD_TYPE_PRIME))
            v = DataAccessPump.Decimal1Format.format((value/100.0f));
        else if (type==RECORD_TYPE_ALARM) 
        {
            return this.getWriter().writeObject(key, 
                atd, 
                getCorrectCode(CODE_TYPE_ALARM, value), 
                null); 
        }
        else if (type==RECORD_TYPE_ERROR)
        {
            return this.getWriter().writeObject(key, 
                atd, 
                getCorrectCode(CODE_TYPE_ERROR, value),
                null); 
        }
        else
            v = "" + value; 
        
        return this.getWriter().writeObject(key, 
            atd, 
            v); 
        
    }
    
    

    int current_year;
    
    private int getYear(int year)
    {
        year += 2000;
        
        if (year > current_year)
            return current_year;
        else
            return year;
    }
    
    
    private ATechDate getDateTime(byte y, byte m, byte d, byte h, byte min, byte s)
    {
        GregorianCalendar gc;
        try
        {
            gc = new GregorianCalendar(getYear(y), m-1, d, h, min, s);
        }
        catch(Exception ex)
        {
            gc = new GregorianCalendar(2000, 0, 1, 0, 0, 0);
        }
        
        return new ATechDate(ATechDate.FORMAT_DATE_AND_TIME_S, gc);
        
    }
    
    
    
    
    
    /** 
     * open
     */
    public boolean open()
    {

        try
        {
            byte[] buffer = new byte[0x100];

            if (!super.open())
            {
                return false;
            }
                    
            waitTime(2000); // wait for device to settle
            
            this.readData(buffer);
            waitTime(200);
            
            this.readData(buffer);
            waitTime(200);
            
            this.readData(buffer);
            waitTime(200);
            
            waitTime(1000);
            
            //PacketStreamReader reader = new PacketStreamReader(buffer);

            //byte num4 = reader.getCommand();
            //byte num5 = reader.getSubCommand();
            if (!(buffer[4] == (byte) 3) && (buffer[5] == ((byte) 3)))
            {
                throw new Exception("Port (" + this.port_name + ") is not for DANA Diabecare R");
            }

            
            
            this.readLine();
            waitTime(200);
            
            
            return true;
        }
        catch (Exception ex)
        {
            log.error("Exception on open: " + ex, ex);
            this.setDeviceStopped();
            return false;
        }
    }

    /**
     * Read data.
     * 
     * @param buffer the buffer
     * @return the int
     * @throws Exception the exception
     */
    public int readData(byte[] buffer) throws Exception
    {
        int num = 0;
        try
        {
            //logger.debug("readData:Start");

            if (this.read(buffer, 0, 4) == 0)
            {
                //throw new Exception("No more data");
            }

            num = buffer[2] - 1;
            //logger.debug("Receieve length: " + num);
            this.read(buffer, 4, num + 4);
            int v = this.createCRC(buffer, 3, num + 1);
            //logger.debug("Created CRC: " + DanaUtil.toHexString(v));
            byte num4 = (byte) ((v >> 8) & 0xff);
            byte num5 = (byte) (v & 0xff);
            if ((buffer[4 + num] != num4) || (buffer[(4 + num) + 1] != num5))
            {
            }
            //ogger.debug("readData:End");
            //System.out.print(".");
            
        }
        catch (Exception exception)
        {
            throw exception;
        }
        return num;
    }


    
    
    DeviceValuesWriter dvw = null;
    
    private DeviceValuesWriter getWriter()
    {
        
        if (dvw==null)
        {
            createDeviceValuesWriter();
        }
        
        return dvw;
        
    }
    
    
    
    
    
    
    private void writeData(byte[] buffer) throws Exception
    {
        this.writeData(buffer, 0, buffer.length);
    }

    private void writeData(byte[] buffer, int offset, int length) throws Exception
    {
        byte[] destinationArray = new byte[length + 8];
        try
        {
            //logger.debug("writeData:Start");
            destinationArray[0] = 0x7e;
            destinationArray[1] = 0x7e;
            destinationArray[2] = (byte) ((length + 1) & 0xff);
            //logger.debug("Send length: " + length);
            destinationArray[3] = (byte) 0xf1;
            System.arraycopy(buffer, offset, destinationArray, 4, length);
            int v = this.createCRC(buffer, offset, length);
            destinationArray[4 + length] = (byte) ((v >> 8) & 0xff);
            destinationArray[5 + length] = (byte) (v & 0xff);
            //logger.debug("CRC: " + DanaUtil.toHexString(v));
            destinationArray[6 + length] = 0x2e;
            destinationArray[7 + length] = 0x2e;
            this.write(destinationArray, 0, destinationArray.length);
            //logger.debug("writeData:End");
        }
        catch (Exception exception)
        {
            throw exception;
        }
    }

    /** 
     * serialEvent
     */
    @Override
    public void serialEvent(SerialPortEvent event)
    {
        // not used
    }

    
    
    /**
     * Get Download Support Type
     * 
     * @return
     */
    public int getDownloadSupportType()
    {
        return DownloadSupportType.DOWNLOAD_YES;
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


    /**
     * Map pump specific alarms to PumpTool specific alarm codes
     * @return
     */
    public Hashtable<String, Integer> getAlarmMappings()
    {
        return alarm_map;
    }


    /**
     * Get Bolus Mappings - Map pump specific bolus to Pump Tool specific 
     *     event codes
     * @return
     */
    public Hashtable<String, Integer> getBolusMappings()
    {
        return null;
    }


    /**
     * Get Error Mappings - Map pump specific errors to Pump Tool specific 
     *     event codes
     * @return
     */
    public Hashtable<String, Integer> getErrorMappings()
    {
        return this.error_map; 
    }


    /**
     * Map pump specific events to PumpTool specific event codes
     * @return
     */
    public Hashtable<String, Integer> getEventMappings()
    {
        return null;
    }


    /**
     * Get Report Mappings - Map pump specific reports to Pump Tool specific 
     *     event codes
     * @return
     */
    public Hashtable<String, Integer> getReportMappings()
    {
        return null;
    }

    
    Hashtable<String, Integer> error_map;
    Hashtable<String, Integer> alarm_map;
    

    /**
     * loadPumpSpecificValues - should be called from constructor of any AbstractPump classes and should
     *      create, AlarmMappings and EventMappings and any other pump constants.
     */
    public void loadPumpSpecificValues()
    {
        // FIXME
        error_map = new Hashtable<String, Integer>();
        error_map.put("17030", PumpErrors.PUMP_ERROR_BATTERY_DEPLETED);
        error_map.put("26410", PumpErrors.PUMP_ERROR_CARTRIDGE_EMPTY);
        
        alarm_map = new Hashtable<String, Integer>();
        alarm_map.put("17030", PumpAlarms.PUMP_ALARM_BATTERY_LOW);
        alarm_map.put("26410", PumpAlarms.PUMP_ALARM_CARTRIDGE_LOW);
        
        
        
        
        
    }


    /**
     * getDeviceSpecialComment - special comment for device (this is needed in case that we need to display
     *    special comment about device (for example pix device, doesn't display anything till the end, which
     *    would be nice if user knew. 
     */
    public String getDeviceSpecialComment()
    {
        return null;
    }


    /**
     * hasSpecialProgressStatus - in most cases we read data directly from device, in this case we have 
     *    normal progress status, but with some special devices we calculate progress through other means.
     *    
     * @return true is progress status is special
     */
    public boolean hasSpecialProgressStatus()
    {
        return false;
    }


    /** 
     * This is method for reading configuration
     * 
     * @throws PlugInBaseException
     */
    public void readConfiguration() throws PlugInBaseException
    {
        try
        {

            this.open();
            
            if (!this.device_communicating)
                return;

            this.connect();
            
            //this.getDeviceRecord(COMMAND_RECORD_BOLUS, false);

//            waitTime(1000);
 //           this.readLine();

  // TODO
            
            // DOING
            getDeviceConfiguration(SYNC_QUERY_GENER);

            
            // DONE
            
            // NOT DONE (FALSE)
 //           getDeviceConfiguration(this.SYNC_QUERY_BASAL_PROFILE);
//            getDeviceConfiguration(this.SYNC_QUERY_BOLUS);
            
            
            
            /*
            switch (param)
            {


            case COMMAND_GENER:
                this.writeData(SYNC_QUERY_GENER);
                break;

            case COMMAND_CARBO:
                this.writeData(SYNC_QUERY_CARBO);
                break;

            case COMMAND_MAX:
                this.writeData(SYNC_QUERY_MAX);
                break;

            //case COMMAND_BASAL_PROFILE:
            //    this.writeData(SYNC_QUERY_BASAL_PROFILE);
            //    break;

            case COMMAND_SHIPPING:
                this.writeData(SYNC_QUERY_SHIPPING);
                break;

            case COMMAND_PWM:
                this.writeData(SYNC_QUERY_PWM);
                break;

            case COMMAND_GLUCOMODE:
                this.writeData(SYNC_QUERY_GLUCOMODE);
                break;
            }*/
            
            
            
        }
        catch(Exception ex)
        {
            System.out.println("Ex: " + ex);
        }
        finally 
        {
            try
            {
                if (this.device_communicating)
                {
                    disconnect();
                    this.close();
                }
            }
            catch(Exception exx)
            {
                log.error("readDeviceDataFull.disconnect(): " + exx, exx);
            }
            
        }
        
        
    }


    
    /** 
     * Read Device Data Full
     */
    public void readDeviceDataFull() throws PlugInBaseException
    {
        this.current_year = new GregorianCalendar().get(GregorianCalendar.YEAR); 

        if (!this.device_communicating)
            return;
        
        
        try
        {

            this.open();
            
            if (!this.device_communicating)
                return;

            this.connect();
            
            this.getDeviceRecord(COMMAND_RECORD_BOLUS, false);
            this.getDeviceRecord(COMMAND_RECORD_BOLUS, false);

            waitTime(1000);
            this.readLine();

            this.readInfo();
            
            if (this.device_communicating)
            {
                this.getDeviceRecord(COMMAND_RECORD_BOLUS, true);
                this.readingEntryStatus(20);
                this.getDeviceRecord(COMMAND_RECORD_DAILY, true);
                this.readingEntryStatus(30);
                this.getDeviceRecord(COMMAND_RECORD_PRIME, true);
                this.readingEntryStatus(40);
                this.getDeviceRecord(COMMAND_RECORD_GLUCOSE, true);
                this.readingEntryStatus(50);
                this.getDeviceRecord(COMMAND_RECORD_ALARM, true);
                this.readingEntryStatus(60);
                this.getDeviceRecord(COMMAND_RECORD_ERROR, true);
                this.readingEntryStatus(70);
                this.getDeviceRecord(COMMAND_RECORD_CARBO, true);
                this.readingEntryStatus(80);
                
                readProfiles();
                this.readingEntryStatus(100);
                
                this.output_writer.setStatus(AbstractOutputWriter.STATUS_DOWNLOAD_FINISHED);
            
            }
        }
        catch(Exception ex)
        {
            log.error("readDeviceDataFull: " + ex, ex);
            this.setDeviceStopped();
        }
        finally 
        {
            try
            {
                if (this.device_communicating)
                {
                    disconnect();
                    this.close();
                }
            }
            catch(Exception exx)
            {
                log.error("readDeviceDataFull.disconnect(): " + exx, exx);
            }
            
        }
        
        
    }


    /**
     * This is method for reading partitial data from device. All reading from actual device should be done from 
     * here. Reading can be done directly here, or event can be used to read data.
     */
    public void readDeviceDataPartitial() throws PlugInBaseException
    {
    }


    /**
     * This is for reading device information. This should be used only if normal dump doesn't retrieve this
     * information (most dumps do).
     *  
     * @throws PlugInBaseException
     */
    public void readInfo() throws PlugInBaseException
    {
        
        
        try
        {
            
            DeviceIdentification di = this.output_writer.getDeviceIdentification();

            
            byte[] buffer = new byte[0x200];
            log.debug("readInfo() - Start");

            this.readingEntryStatus(2);
            
            this.writeData(COMMAND_SHIPPING);  // 50, 7
            waitTime(1000);

            this.readData(buffer);
            //this.hex_utils.showByteArrayHex(buffer);
            this.hex_utils.readByteArray(buffer);
            
            if (!checkIfValid(buffer))
            {
                setDeviceStopped();
                return;
            }
            
            
            String sn = this.hex_utils.getStringFromArray(6, 10);
            
            //System.out.println("S/N: " + sn);

            di.device_serial_number = sn;
            
            /*
            ATechDate atd = getDateTime(this.hex_utils.getByteFromArray(16), 
                                        this.hex_utils.getByteFromArray(17), 
                                        this.hex_utils.getByteFromArray(18), 
                                        (byte)0, (byte)0, (byte)0);
            System.out.println("Date: " + atd.getDateString());
            
            String cnt = this.hex_utils.getAsciiFromArray(19, 3);
            System.out.println("Country: " + cnt);
            */
            
            this.readingEntryStatus(5);

            this.output_writer.writeDeviceIdentification();
            
            waitTime(200);
        }
        catch (Exception ex)
        {
            log.error("readInfo(). Ex: " + ex, ex);
        }
        finally
        {
            log.debug("readInfo() - End");
        }
        
    }
    
    byte[] val =  { 0x7e, 0x7e, (byte)0xf2 };
    
    
    private boolean checkIfValid(byte[] arr)
    {
        if ((arr[0]==val[0]) && (arr[1]==val[1]) && (arr[3]==val[2]))
            return true;
        else
            return false;
        
        
    }
    
    private void readProfiles()
    {
        
        try
        {
            byte[] buffer = new byte[0x100];
            //int num = 0;
            log.debug("getProfiles() :Start");

            this.writeData(COMMAND_BASAL_PATTERN);
            waitTime(200);
            
           
            this.readData(buffer);
            
            if (!checkIfValid(buffer))
            {
                this.setDeviceStopped();
                return;
            }
            
            this.hex_utils.readByteArray(buffer);
            
            //float[] all_data = new float[24];
            
            PumpValuesEntryProfile pvep = new PumpValuesEntryProfile();
            
            for(int i=0; i<24; i++)
            {
                ProfileSubEntry pse = new ProfileSubEntry();
                pse.time_start = i *100;
                pse.time_end = pse.time_start + 59;
                pse.amount = (this.hex_utils.getIntFromArray(6 + (i*2)) / 100.0f);
                
                //all_data[i] = (this.hex_utils.getIntFromArray(6 + (i*2)) / 100.0f);
                //System.out.println(i + ":00 = " + pse.amount);
                
                pvep.addProfileSubEntry(pse);
            }

            this.output_writer.writeData(pvep);
            
            waitTime(200);
        }
        catch (Exception ex2)
        {
            log.error("getProfiles(). Ex: " + ex2.getMessage());
            this.setDeviceStopped();
        }
        finally
        {
            log.debug("getProfiles() :End");
        }
        
        
        
    }
    
    
    
    private void readingEntryStatus(int cur_ent)
    {
        this.entries_current = cur_ent;
        float proc_read = ((this.entries_current*1.0f)  / this.entries_max);
        float proc_total = 6 + (94 * proc_read);
        this.output_writer.setSpecialProgress((int)proc_total); //.setSubStatus(sub_status)
    }
    
    
    
    private void setDeviceStopped()
    {
        this.device_communicating = false;
//        System.out.println("Device not communicating");
        this.output_writer.setStatus(AbstractOutputWriter.STATUS_STOPPED_DEVICE);
    }
    
    
    private void createDeviceValuesWriter()
    {
        this.dvw = new DeviceValuesWriter();
        this.dvw.setOutputWriter(this.output_writer);

        // bolus - standard 
        this.dvw.put("1_66", new PumpTempValues(PumpTempValues.OBJECT_BASE, PumpBaseType.PUMP_DATA_BOLUS, PumpBolusType.PUMP_BOLUS_STANDARD));  
        
        // bolus - wave (this is unhandled, data is not all available) 
        this.dvw.put("1_69", new PumpTempValues(PumpTempValues.OBJECT_BASE, PumpBaseType.PUMP_DATA_BOLUS, PumpBolusType.PUMP_BOLUS_MULTIWAVE));  
        
        
        // daily insulin record
        this.dvw.put("2_68", new PumpTempValues(PumpTempValues.OBJECT_BASE, PumpBaseType.PUMP_DATA_REPORT, PumpReport.PUMP_REPORT_INSULIN_TOTAL_DAY));  
        // CH (carbohydrates)
        this.dvw.put("8_82", new PumpTempValues(PumpTempValues.OBJECT_EXT, PumpAdditionalDataType.PUMP_ADD_DATA_CH, 0));  
        // prime
        this.dvw.put("3_80", new PumpTempValues(PumpTempValues.OBJECT_BASE, PumpBaseType.PUMP_DATA_EVENT, PumpEvents.PUMP_EVENT_PRIME_INFUSION_SET));  

        // BG
        this.dvw.put("6_71", new PumpTempValues(PumpTempValues.OBJECT_EXT, PumpAdditionalDataType.PUMP_ADD_DATA_BG, 0));  
        
        // alarm
        this.dvw.put("5_66", new PumpTempValues(PumpTempValues.OBJECT_BASE, PumpBaseType.PUMP_DATA_ALARM, 0));  

        // error
        this.dvw.put("4_2", new PumpTempValues(PumpTempValues.OBJECT_BASE, PumpBaseType.PUMP_DATA_ERROR, 0));  
        
    }


    
    
    /**
     * Code Type: Alarm
     */
    public static final int CODE_TYPE_ALARM = 1;

    /**
     * Code Type: Error
     */
    public static final int CODE_TYPE_ERROR = 2;
    
    private int getCorrectCode(int type, int code)
    {
        if (type==CODE_TYPE_ALARM)
        {
            //s("CodeTypeAlarm" + code);
            if (this.alarm_map.containsKey("" + code))
            {
                //s("CodeTypeAlarm: found. Code: " + code);
                return this.alarm_map.get("" + code);
            }
        }
        else
        {
            if (this.error_map.containsKey("" + code))
            {
                //s("CodeTypeAlarm: found. Code: " + code);
                return this.error_map.get("" + code);
            }
            
        }
        
        return 0;
        
        
    }
    
    

    
    private int crc16(byte data, int crc)
    {
        int num = 0;
        num = ((crc >> 8) & 0xff) | (crc << 8);
        num ^= data & 0xff;
        num ^= ((num & 0xff) >> 4) & 0xfff;
        num ^= (num << 8) << 4;
        return (num ^ (((num & 0xff) << 5) | ((((num & 0xff) >> 3) & 0x1fff) << 8)));
    }

    
    /**
     * Creates the crc.
     * 
     * @param data the data
     * @param offset the offset
     * @param length the length
     * 
     * @return the int
     */
    public int createCRC(byte[] data, int offset, int length)
    {
        int crc = 0;
        for (int i = 0; i < length; i++)
        {
            crc = crc16(data[offset + i], crc);
        }
        return crc;
    }
    
    
    private void getDeviceConfiguration(byte[] cmd) throws Exception
    {
        
        String param = "";
        
        try
        {
            byte[] buffer = new byte[0x100];
            //int num = 0;
            log.debug("getDeviceInfo(" + param + "):Start");
            
            
            
            /*
            switch (param)
            {
            case COMMAND_BOLUS:
                this.writeData(SYNC_QUERY_BOLUS);
                break;

            case COMMAND_BASAL_PROFILE:
                this.writeData(SYNC_QUERY_BASAL);
                break;

            case COMMAND_GENER:
                this.writeData(SYNC_QUERY_GENER);
                break;

            case COMMAND_CARBO:
                this.writeData(SYNC_QUERY_CARBO);
                break;

            case COMMAND_MAX:
                this.writeData(SYNC_QUERY_MAX);
                break;

            //case COMMAND_BASAL_PROFILE:
            //    this.writeData(SYNC_QUERY_BASAL_PROFILE);
            //    break;

            case COMMAND_SHIPPING:
                this.writeData(SYNC_QUERY_SHIPPING);
                break;

            case COMMAND_PWM:
                this.writeData(SYNC_QUERY_PWM);
                break;

            case COMMAND_GLUCOMODE:
                this.writeData(SYNC_QUERY_GLUCOMODE);
                break;
            }
            waitTime(200);
            
            
            //System.out.println(this.readLine());

            
            /*num =*/ 

            // TODO
            
            this.writeData(cmd);
            //waitTime(200);
            
            this.readData(buffer);
            
            
            hex_utils.showByteArray(buffer);
            System.out.println();
            
            hex_utils.showByteArrayHex(buffer);
            
            PacketStreamReader reader = new PacketStreamReader(buffer);
            hex_utils.readByteArray(buffer);
            
            
            if (cmd==this.SYNC_QUERY_BOLUS)
            {
                System.out.println("Bolus0: " + reader.getInt());
                System.out.println("Bolus1: " + reader.getInt());
                System.out.println("Bolus2: " + reader.getInt());
                System.out.println("Bolus3: " + reader.getInt());
                

                System.out.println(hex_utils.getIntFromArray(6));
                System.out.println(hex_utils.getIntFromArray(8));
                System.out.println(hex_utils.getIntFromArray(10));
                System.out.println(hex_utils.getIntFromArray(12));
                //System.out.println(hex_utils.getIntFromArray(14));
            }
            else if (cmd==this.SYNC_QUERY_GENER)
            {
                System.out.println("Basal Increment: " + reader.getByte());
                System.out.println("Bolus Increment: " +reader.getByte());
                System.out.println("Bolus Preset: " +reader.getByte());
                System.out.println("Bolus Alarm: " +reader.getByte());
                System.out.println("Bolus Block: " +reader.getByte());
                System.out.println("Basal Unit: " +reader.getByte());
                System.out.println("Basal Profile: " +reader.getByte());
                
                writeConfiguration("PCFG_BASAL_INCREMENT", "" + (hex_utils.getByteFromArray(6)/100.0f) );
                writeConfiguration("PCFG_BOLUS_INCREMENT", "" + (hex_utils.getByteFromArray(7)/100.0f) );
                writeConfiguration("PCFG_BOLUS_PRESET", "" + getTrueOrFalse(hex_utils.getByteFromArray(8)) );
                writeConfiguration("PCFG_BOLUS_ALARM", "" + (hex_utils.getByteFromArray(9)));
                writeConfiguration("PCFG_BOLUS_BLOCK", "" + hex_utils.getByteFromArray(10));
                
                if (hex_utils.getByteFromArray(11)==0)
                    writeConfiguration("PCFG_BASAL_UNIT", "UNIT_PER_HOUR");
                else
                    writeConfiguration("PCFG_BASAL_UNIT", "UNIT_PER_DAY");
                
                // PROFILE = 12
            }
            
            
//            PacketStreamReader reader = new PacketStreamReader(buffer);

            /*
            switch (param)
            {
            case COMMAND_BOLUS:
                sett.bolus0 = reader.getInt();
                sett.bolus1 = reader.getInt();
                sett.bolus2 = reader.getInt();
                sett.bolus3 = reader.getInt();
                
                System.out.println("Bolus0: " + sett.bolus0);
                System.out.println("Bolus1: " + sett.bolus1);
                System.out.println("Bolus2: " + sett.bolus2);
                System.out.println("Bolus3: " + sett.bolus3);
                
                
                break;

            case COMMAND_BASAL_PROFILE:
                sett.basal0 = reader.getInt();
                sett.basal1 = reader.getInt();
                sett.basal2 = reader.getInt();
                sett.basal3 = reader.getInt();
                sett.basal4 = reader.getInt();
                sett.basal5 = reader.getInt();
                sett.basal6 = reader.getInt();
                sett.basal7 = reader.getInt();
                sett.basal8 = reader.getInt();
                sett.basal9 = reader.getInt();
                sett.basal10 = reader.getInt();
                sett.basal11 = reader.getInt();
                sett.basal12 = reader.getInt();
                sett.basal13 = reader.getInt();
                sett.basal14 = reader.getInt();
                sett.basal15 = reader.getInt();
                sett.basal16 = reader.getInt();
                sett.basal17 = reader.getInt();
                sett.basal18 = reader.getInt();
                sett.basal19 = reader.getInt();
                sett.basal20 = reader.getInt();
                sett.basal21 = reader.getInt();
                sett.basal22 = reader.getInt();
                sett.basal23 = reader.getInt();

                System.out.println("Basal0: " + sett.basal0);
                System.out.println("Basal1: " + sett.basal1);
                System.out.println("Basal2: " + sett.basal2);
                System.out.println("Basal3: " + sett.basal3);
                System.out.println("Basal4: " + sett.basal4);
                System.out.println("Basal5: " + sett.basal5);
                System.out.println("Basal6: " + sett.basal6);
                System.out.println("Basal7: " + sett.basal7);
                System.out.println("Basal8: " + sett.basal8);
                System.out.println("Basal9: " + sett.basal9);
                System.out.println("Basal10: " + sett.basal10);
                System.out.println("Basal11: " + sett.basal11);
                System.out.println("Basal12: " + sett.basal12);
                System.out.println("Basal13: " + sett.basal13);
                System.out.println("Basal14: " + sett.basal14);
                System.out.println("Basal15: " + sett.basal15);
                
                
                break;

            case COMMAND_GENER:
                sett.basalIncrement = reader.getByte();
                sett.bolusIncrement = reader.getByte();
                sett.bolusPreset = reader.getByte();
                sett.bolusAlarm = reader.getByte();
                sett.bolusBlock = reader.getByte();
                sett.basalUnit = reader.getByte();
                break;

            case COMMAND_CARBO:
                sett.carboCIR = reader.getInt();
                sett.carboCF = reader.getInt();
                sett.carboAI = reader.getInt();
                sett.carboTG = reader.getInt();
                sett.carboAIDR = reader.getByte();
                break;

            case COMMAND_MAX:
                sett.maxBolusRate = reader.getInt();
                sett.maxBasalRate = reader.getInt();
                sett.maxDailyRate = reader.getInt();
                break;

            case COMMAND_SHIPPING:
                sett.serialNo = reader.getString(10);
                try
                {
                    // sett.shippingDate = new DateTime(reader.getByte() +
                    // 0x7d0, reader.getByte(), reader.getByte());
                }
                catch (Exception exception)
                {
                    // sett.shippingDate = DateTime.MinValue;
                    log.error(exception.getMessage());
                }
                sett.shippingCountry = reader.getAscii(3);
                break;

            case COMMAND_GLUCOMODE:
                sett.glucoseUnit = reader.getByte();
                sett.easyMode = reader.getByte();
                
                System.out.println("Glucose Unit: " + sett.glucoseUnit);
                System.out.println("easyMode: " + sett.easyMode);
                
                
                break;
            }
            */
            
            
            waitTime(200);
            log.debug("getDeviceInfo(" + param + "):End");
        }
        catch (Exception ex2)
        {
            throw new Exception("sync(" + param + ")" + ex2.getMessage());
        }
    }
    
    
    public void writeConfiguration(String key, String value)
    {
        System.out.println(key + " = " + value);
    }
    
    
    public String getTrueOrFalse(byte val)
    {
        if (val==0)
            return "FALSE";
        else
            return "TRUE";
    }
    
    
    
}

