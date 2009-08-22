package ggc.meter.device.abbott;

import ggc.meter.data.MeterValuesEntry;
import ggc.meter.device.AbstractSerialMeter;
import ggc.meter.manager.MeterDevicesIds;
import ggc.meter.manager.company.Abbott;
import ggc.meter.util.DataAccessMeter;
import ggc.plugin.device.DeviceIdentification;
import ggc.plugin.device.PlugInBaseException;
import ggc.plugin.manager.DeviceImplementationStatus;
import ggc.plugin.manager.company.AbstractDeviceCompany;
import ggc.plugin.output.AbstractOutputWriter;
import ggc.plugin.output.OutputUtil;
import ggc.plugin.output.OutputWriter;
import ggc.plugin.protocol.SerialProtocol;
import gnu.io.SerialPort;
import gnu.io.SerialPortEvent;

import java.util.StringTokenizer;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.atech.utils.ATechDate;
import com.atech.utils.TimeZoneUtil;

/**
 *  Application:   GGC - GNU Gluco Control
 *  Plug-in:       Meter Tool (support for Meter devices)
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
 *  Filename:     OneTouchMeter  
 *  Description:  Super class for OT meters with basic ASCII protocol
 * 
 *  Author: Andy {andy@atech-software.com}
 */

public class OptiumXceed extends AbstractSerialMeter
{
    
    private static Log log = LogFactory.getLog(OptiumXceed.class);
    protected boolean device_running = true;
    protected TimeZoneUtil tzu = TimeZoneUtil.getInstance();
    private int entries_max = 0;
    private int entries_current = 0;
    
    
    /**
     * Constructor
     */
    public OptiumXceed()
    {
    }
    
    /**
     * Constructor for device manager
     * 
     * @param cmp
     */
    public OptiumXceed(AbstractDeviceCompany cmp)
    {
        super(cmp);
    }
    
    
    
    /**
     * Constructor
     * 
     * @param portName
     * @param writer
     */
    public OptiumXceed(String portName, OutputWriter writer)
    {
        super(DataAccessMeter.getInstance());
        
        this.setCommunicationSettings( 
                  9600,
                  SerialPort.DATABITS_8, 
                  SerialPort.STOPBITS_1, 
                  SerialPort.PARITY_NONE,
                  SerialPort.FLOWCONTROL_NONE, 
                  SerialProtocol.SERIAL_EVENT_BREAK_INTERRUPT|SerialProtocol.SERIAL_EVENT_OUTPUT_EMPTY);
                
        // output writer, this is how data is returned (for testing new devices, we can use Consol
        this.output_writer = writer; 
        this.output_writer.getOutputUtil().setMaxMemoryRecords(this.getMaxMemoryRecords());
        
        // set meter type (this will be deprecated in future, but it's needed for now
        this.setMeterType("Abbott", this.getName());

        // set device company (needed for now, will also be deprecated)
        this.setDeviceCompany(new Abbott());
        

        // settting serial port in com library
        try
        {
            this.setSerialPort(portName);
    
            if (!this.open())
            {
                this.m_status = 1;
                this.deviceDisconnected();
                return;
            }

            this.output_writer.writeHeader();
            
        }
        catch(Exception ex)
        {
            log.error("OptiumXceed: Error connecting !\nException: " + ex, ex);
            System.out.println("OptiumXceed: Error connecting !\nException: " + ex);
        }
        
        
    }


   // public static final byte ENQ = 0x05;
    
    
    

   
    /** 
     * readDeviceDataFull
     */
    public void readDeviceDataFull()
    {
        //System.out.println("readDeviceDataFull()");
        try
        {
            
            readInfo();
            
            String data_back;            

            this.sendMessageToMeter("1GET_EVENTS\00348\r\n");
            
            for(data_back = readLine(); data_back.indexOf("END_OF_DATA") == -1;)
            {
                processDataLine(data_back);
                writeCommand(6);
            
                data_back = readLine(); 

                if(data_back == null)
                {
                    endReading();
                    break;
                }
            }

            writeCommand(6);
            readByteTimed();
            
            this.output_writer.setSpecialProgress(100);
            this.output_writer.setSubStatus(null);
        
        }
        catch(Exception ex)
        {
            System.out.println("Exception: " + ex);
            ex.printStackTrace();
            
        }
        
        if (this.isDeviceFinished())
        {
            this.output_writer.endOutput();
        }
        
        //System.out.println("Reading finished !");
        
    }

    
    private boolean startMessageToMeter() throws Exception
    {
        boolean done=false;
        int status;
        
        write(SerialProtocol.ASCII_ENQ);
        
        do
        {
            
            
            status = readByteTimed();
            
            if (status == SerialProtocol.ASCII_ENQ)
            {
                write(SerialProtocol.ASCII_ACK);
            }
            else if (status == SerialProtocol.ASCII_ACK)
            {
                return true;
            }
            else if (status == 2) 
            {
                this.readLine();
                commandAfterRead();
                return true;
            }
            else if ((status == 4))
            {
                commandAfterRead();
                return true;
            }
            else if ((status == SerialProtocol.ASCII_NAK) || (status == 0))
            {
                endReading();
                return false;
            }
            
        } while (done!=true);
        
        
        return false;
    }
    
    
    
    private void processDataLine(String line)
    {
        
        if ((line==null) || (line.trim().length()==0))
            return;
        
        try
        {
            String[] data = m_da.splitString(line, "\t");
            
            if (!data[1].equals("1"))
                return;
            
            
            String type_id = data[0].substring(1);
            
            boolean is_BG = false;
            
            if (type_id.equals("01"))
            {
                is_BG = true;
            }
            else if (type_id.equals("04"))
            {
                is_BG = false;
            }
            else
            {
                this.entries_current++;
                readingEntryStatus();
                
                return;
            }
                
            if (is_BG)
                addBGData(data[4], getDateTime(data[2], data[3]));
            else
                addUrineData(data[4], getDateTime(data[2], data[3]));
            
            StringTokenizer strtok = new StringTokenizer(line, "\t");
            
            while(strtok.hasMoreTokens())
            {
                strtok.nextToken();
            }
        }
        catch (Exception ex)
        {
            log.error("Exception on parse: " + ex + "\nData: " + line, ex);
        }
        
    }
    

    private String[] processId(String line)
    {
        
        //System.out.println("LL: " + line);
        
        StringTokenizer strtok = new StringTokenizer(line, "\t");
        
        strtok.nextToken();
        
        String o = strtok.nextToken();
        
        strtok = new StringTokenizer(o, " ");
        
        String[] ids = new String[2];
        ids[0] = strtok.nextToken();
        ids[1] = strtok.nextToken();
     
        return ids;
        
    }
    
    
    
    
    private void endReading()
    {
        this.output_writer.setSubStatus(null);
        this.output_writer.endOutput();
        this.output_writer.setStatus(AbstractOutputWriter.STATUS_STOPPED_DEVICE);
        System.out.println("Reading finished prematurely !");
    }
    
    
    private boolean isDeviceFinished()
    {
    	return (this.entries_current==this.entries_max);
    }
    
    
 
    /**
     * This is method for reading partitial data from device. All reading from actual device should be done from 
     * here. Reading can be done directly here, or event can be used to read data.
     */
    public void readDeviceDataPartitial() throws PlugInBaseException
    {
        
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
     * This is for reading device information. This should be used only if normal dump doesn't retrieve this
     * information (most dumps do). 
     * @throws PlugInBaseException
     */
    public void readInfo() throws PlugInBaseException
    {
        try
        {
            this.output_writer.setSubStatus(ic.getMessage("READING_SERIAL_NR_SETTINGS"));
            this.output_writer.setSpecialProgress(1);

            if (!this.startMessageToMeter())
                return;
            
            String data_back;
            
            this.sendMessageToMeter("1ID\003C1\r\n");
            data_back = readMessageFromMeter();
            //System.out.println("ID: " + data_back);
            
            String[] ids = this.processId(data_back);
            
            
            this.sendMessageToMeter("1GET_METER\003F0\r\n");
            data_back = readMessageFromMeter();
            //System.out.println("Get meter: " + data_back);
            
            
            if  ((data_back == null) || (data_back.charAt(0) == 0))
            {
                endReading();
            }
            
            // first we read device identification data
            DeviceIdentification di = this.output_writer.getDeviceIdentification();
            
            di.device_serial_number = ids[0]; //this.readLineDebug();
            this.output_writer.setSpecialProgress(2);
            di.device_hardware_version = ids[1]; //this.readLineDebug();
            this.output_writer.setSpecialProgress(3);
            //this.readLineDebug();
            this.output_writer.setSpecialProgress(4);
            this.entries_max = this.getMaxMemoryRecords(); //   Integer.parseInt(this.readLineDebug());
            
            
            this.output_writer.setDeviceIdentification(di);
            this.output_writer.writeDeviceIdentification();
      
           this.output_writer.setSpecialProgress(5);
           
           
           
        }
        catch(Exception ex)
        {
            throw new PlugInBaseException(ex);
        }
    
    }
    

    
    /**
     * Add BG Data
     * 
     * @param data 
     * @param adt 
     */
    public void addBGData(String data, ATechDate adt)
    {
        if ((data==null) || (data.length()==0))
            return;

        MeterValuesEntry mve = new MeterValuesEntry();
        mve.setBgUnit(OutputUtil.BG_MGDL);
        
        mve.setBgValue("" + m_da.getIntValueFromString(data));
        mve.setDateTimeObject(adt);
        
        this.output_writer.writeData(mve);
        this.entries_current++;
        readingEntryStatus();
    }
        
        
        
    /**
     * Add Urine Data
     * 
     * @param data 
     * @param adt 
     * 
     */
    public void addUrineData(String data, ATechDate adt)
    {
        if ((data==null) || (data.length()==0))
            return;

        MeterValuesEntry mve = new MeterValuesEntry();
        
        mve.setDateTimeObject(adt);
        mve.setSpecialEntry(MeterValuesEntry.SPECIAL_ENTRY_URINE_MMOLL, 
                            DataAccessMeter.Decimal1Format.format(m_da.getBGValueByType(DataAccessMeter.BG_MGDL, DataAccessMeter.BG_MMOL, data)));
        
//        mve.setBgValue("" + m_da.getIntValueFromString(data));
        
        this.output_writer.writeData(mve);
        this.entries_current++;
        readingEntryStatus();
    }
    
    
    
    
    
    protected void setDeviceStopped()
    {
        this.device_running = false;
        this.output_writer.endOutput();
    }
    
    
    protected ATechDate getDateTime(String date, String time)
    {
        long dt = m_da.getLongValueFromString(date) * 10000L;
        String tm = m_da.replaceExpression(time, ":", "");
        dt += m_da.getLongValueFromString(tm);
        
        return tzu.getCorrectedDateTime(new ATechDate(ATechDate.FORMAT_DATE_AND_TIME_MIN, dt));
    }

    
    //private void 
    
    private void readingEntryStatus()
    {
        float proc_read = ((this.entries_current*1.0f)  / this.entries_max);
        float proc_total = 5 + (95 * proc_read);
        this.output_writer.setSpecialProgress((int)proc_total); //.setSubStatus(sub_status)
    }
    
    
    /**
     * hasSpecialProgressStatus - in most cases we read data directly from device, in this case we have 
     *    normal progress status, but with some special devices we calculate progress through other means.
     * @return true is progress status is special
     */
    public boolean hasSpecialProgressStatus()
    {
        return true;
    }    
    
    
    
    /**
     * We don't use serial event for reading data, because process takes too long, we use serial event just 
     * to determine if device is stopped (interrupted) 
     */
    @Override
    public void serialEvent(SerialPortEvent event)
    {


        // Determine type of event.
        switch (event.getEventType()) 
        {
    
            // If break event append BREAK RECEIVED message.
            case SerialPortEvent.BI:
                System.out.println("recievied break");
                this.output_writer.setStatus(AbstractOutputWriter.STATUS_STOPPED_DEVICE);
                //setDeviceStopped();
                break;
            case SerialPortEvent.CD:
                System.out.println("recievied cd");
                break;
            case SerialPortEvent.CTS:
                System.out.println("recievied cts");
                break;
            case SerialPortEvent.DSR:
                System.out.println("recievied dsr");
                break;
            case SerialPortEvent.FE:
                System.out.println("recievied fe");
                break;
            case SerialPortEvent.OE:
                System.out.println("recievied oe");
                System.out.println("Output Empty");
                break;
            case SerialPortEvent.PE:
                System.out.println("recievied pe");
                break;
            case SerialPortEvent.RI:
                System.out.println("recievied ri");
                break;
        }
    } 
    
    
    /**
     * getCompanyId - Get Company Id 
     * 
     * @return id of company
     */
    public int getCompanyId()
    {
        return MeterDevicesIds.COMPANY_ABBOTT;
    }
    
    

    /**
     * Maximum of records that device can store
     */
    public int getMaxMemoryRecords()
    {
        return 450;
    }

    /** 
     * Get Device ClassName
     */
    public String getDeviceClassName()
    {
        return "ggc.meter.device.abbott.OptiumXceed";
    }

    /**
     * getDeviceId - Get Device Id 
     * 
     * @return id of device within company
     */
    public int getDeviceId()
    {
        return MeterDevicesIds.METER_ABBOTT_OPTIUM_XCEED;
    }

    
    /**
     * getIconName - Get Icon of meter
     * 
     * @return icon name
     */
    public String getIconName()
    {
        return "ab_optium_xceed.jpg";
    }

    /**
     * getName - Get Name of meter. 
     * 
     * @return name of meter
     */
    public String getName()
    {
        return "Optium Xceed";
    }

    
    
    /** 
     * getComment
     */
    public String getComment()
    {
        return null;
    }


    /** 
     * getImplementationStatus
     */
    public int getImplementationStatus()
    {
        return DeviceImplementationStatus.IMPLEMENTATION_TESTING;
    }

    
    /** 
     * getInstructions
     */
    public String getInstructions()
    {
        return "INSTRUCTIONS_ABBOTT_OPTIUMXCEED";
    }
    
    
}