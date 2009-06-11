package ggc.meter.device.freestyle;

import ggc.meter.data.MeterValuesEntry;
import ggc.meter.device.AbstractSerialMeter;
import ggc.meter.manager.MeterManager;
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
import gnu.io.PortInUseException;
import gnu.io.SerialPort;
import gnu.io.SerialPortEvent;
import gnu.io.UnsupportedCommOperationException;

import java.io.IOException;

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


// while basic OT ascii protocol is implemented this file is still unclean and we are waiting to get 
// more of old protocols, before we do finishing touches...
// so far we are also missing few pictures and ALL instructions for meters

public class OptiumXceed extends AbstractSerialMeter
{
    int characterPause = 1;
    int commandPause = 1;
    
    
    /**
     * 
     */
    public static final int METER_FREESTYLE                 = 40001;
    
    /**
     * 
     */
    public static final int METER_FREESTYLE_LITE            = 40002;
    
    /**
     * 
     */
    public static final int METER_FREESTYLE_FREEDOM         = 40003;
    
    /**
     * 
     */
    public static final int METER_FREESTYLE_FREEDOM_LITE    = 40004;
    
    /**
     * 
     */
    public static final int METER_FREESTYLE_FLASH           = 40005;
    
    /**
     * 
     */
    public static final int METER_PRECISION_XTRA            = 40006;
    
    
    private static Log log = LogFactory.getLog(FreestyleMeter.class);

    
    
    protected boolean device_running = true;
    //protected ArrayList<MeterValuesEntry> data = null;
//    protected OutputWriter m_output_writer;
    protected TimeZoneUtil tzu = TimeZoneUtil.getInstance();
    //public int meter_type = 20000;
    private int entries_max = 0;
    private int entries_current = 0;
    private int reading_status = 0;
    
    //private int info_tokens;
    //private String date_order;
    
    
    
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
            //log.error("")
            //System.out.println("OneTouchMeter: Error connecting !\nException: " + ex);
            //ex.printStackTrace();
        }
        
        /*
        if (this.getDeviceId()==OneTouchMeter.METER_LIFESCAN_ONE_TOUCH_ULTRA)
        {
            this.info_tokens = 3;
            this.date_order = "MDY";
        }
        else
        {
            this.info_tokens = 8;
        }*/
        
    }


    
    
    /** 
     * getComment
     */
    public String getComment()
    {
        return null;
    }


    // DO
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
        return null;
    }

   
    /** 
     * readDeviceDataFull
     */
    public void readDeviceDataFull()
    {
        System.out.println("readDeviceDataFull()");
        try
        {
            
            write("READ STATUS\r".getBytes());
            waitTime(1);
            //tr.pause(1);
            write("c\rREAD STATUS\r".getBytes());
            //tr.pause(1);
            waitTime(1);
            write("c\r".getBytes());
            waitTime(1);
            
            write(5);
            //readByte();
            //readByte();
            int bt = readByte();
            
            System.out.println("Status: " + bt);
            write(2);
            write("1ID\003C1\r\n".getBytes());
            String bt_line;
            
            afterWrite();
            bt_line = readLine();
            System.out.println("ID: " + bt_line);
//            readStringUntil("\n");
            afterRead();
            writeCmd(2);
            writeCmd("1GET_METER\003F0\r\n");
            afterWrite();
            //String inline = readStringUntil(newline);
            String inline = readLine();
            System.out.println("Get meter: " + inline);
            if(inline == null || inline.charAt(0) == 0)
                connectAndExit();
            afterRead();
//x            checkDate(parseDate(inline.substring(2)));
            writeCmd(2);
            writeCmd("1GET_EVENTS");
            writeCmd(3);
            writeCmd("48\r\n");
            afterWrite();
//x            setReadPause();
//x            super.display.initUpTo();
            for(inline = readLine() /*readStringUntil(newline)*/; inline.indexOf("END_OF_DATA") == -1;)
            {
//x                if(prepare(inline))
// x                   super.display.incrementCount();
// x               Trace _tmp4 = tr;
// x               tr.trace(Trace.DETAIL1, "Record = " + inline);
                System.out.println("L1: " + inline);
                writeCmd(6);
                inline = readLine(); //readStringUntil(newline);
                //System.out.println("L2: " +inline);
                if(inline == null)
                {
                    
                    break;
                }
//                    super.display.lostAndExit();
            }

            writeCmd(6);
            readByte();
            
            
            
            //write("MEM".getBytes());
            //waitTime(100);
            
//x            String line;
            
            
            //readInfo();

            
            
            /*
            while((line=this.readLine())==null)
            {
                System.out.println("Serial Number1: " + line);
            }*/
            
            //System.out.println("Serial Number2: " + line);
            //System.out.println("Serial Number: " + this.readLine());
            //System.out.println("Serial Number: " + this.readLine());
            
            
            
         /*
            while (((line = this.readLine()) != null) && (!isDeviceStopped(line)))
            {
                //xa processBGData(line);
                System.out.println(line);
                if (line==null)
                    break;
                
            }
           */ 
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
        
        //this.output_writer.setStatus(100);
        System.out.println("Reading finsihed");
        
    }

    
    protected int readByte()
    throws IOException, InterruptedException
{
    for(int r = 0; r++ < 100;)
    {
        waitTime(1);
        int iBuf = read();
        if(iBuf == -1)
        {
            waitTime(1);
        } else
        {
            //addDebug(iBuf, D_RD);
            return iBuf;
        }
    }

    //addDebug(0, D_TO);
    return -1;
}
    
    
    protected void writeCmd(int c)
    throws IOException, InterruptedException
{
    //addDebug(c, D_WR);
    write(c);
    waitTime(characterPause);
}
    
    
    protected void writeCmd(String line)
    throws IOException, InterruptedException
{
    for(int c = 0; c < line.length(); c++)
    {
        //addDebug(line.charAt(c), D_WR);
        write(line.charAt(c));
        waitTime(characterPause);
    }

    waitTime(commandPause);
}
    
    
    
    private void afterRead()
    throws PortInUseException, UnsupportedCommOperationException, IOException, InterruptedException
{
    writeCmd(6);
    readByte();
    writeCmd(5);
    readByte();
}

private void afterWrite()
    throws PortInUseException, UnsupportedCommOperationException, IOException, InterruptedException
{
    readByte();
    writeCmd(4);
    readByte();
    writeCmd(6);
}
    
    
    private void connectAndExit()
    {
        System.out.println("Error reading data from device !!!");
    }
    
    
    


    /** 
     * readDeviceDataFull
     */
    public void readDeviceDataFullxxxx()
    {
        
        try
        {
            
            write("READ STATUS\r".getBytes());
            waitTime(1000);
            //tr.pause(1);
            write("c\rREAD STATUS\r".getBytes());
            //tr.pause(1);
            waitTime(1000);
            write("c\r".getBytes());
            waitTime(1000);
            
            write(5);
            //readByte();
            //readByte();
            read();
            write(2);
            write("1ID\003C1\r\n".getBytes());
            
            
            //write("MEM".getBytes());
            //waitTime(100);
            
            String line;
            
            
            //readInfo();

            
            
            /*
            while((line=this.readLine())==null)
            {
                System.out.println("Serial Number1: " + line);
            }*/
            
            //System.out.println("Serial Number2: " + line);
            //System.out.println("Serial Number: " + this.readLine());
            //System.out.println("Serial Number: " + this.readLine());
            
            
            
         
            while (((line = this.readLine()) != null) && (!isDeviceStopped(line)))
            {
                //xa processBGData(line);
                System.out.println(line);
                if (line==null)
                    break;
                
            }
            
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
        
        //this.output_writer.setStatus(100);
        System.out.println("Reading finsihed");
        
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
    
            // first we read device identification data
            DeviceIdentification di = this.output_writer.getDeviceIdentification();
            
            di.device_serial_number = this.readLineDebug();
            this.output_writer.setSpecialProgress(2);
            di.device_hardware_version = this.readLineDebug();
            this.output_writer.setSpecialProgress(3);
            this.readLineDebug();
            this.output_writer.setSpecialProgress(4);
            this.entries_max = Integer.parseInt(this.readLineDebug());
            
            
            this.output_writer.setDeviceIdentification(di);
            this.output_writer.writeDeviceIdentification();
            this.output_writer.setSpecialProgress(5);
        }
        catch(IOException ex)
        {
            throw new PlugInBaseException(ex);
        }
    
    }
    
    
    protected String readLineDebug() throws IOException
    {
        String rdl = this.readLine();
        
        log.debug(rdl);
        
        return rdl;
    }
    
    
    
    private boolean isDeviceStopped(String vals)
    {
    	if ((vals == null) ||
    	    ((this.reading_status==1) && (vals.length()==0)) ||
            (!this.device_running) ||
            (this.output_writer.isReadingStopped()))
    		return true;
    	
        return false;
    }
    
    
    
    
    /**
     * Process BG Data
     * 
     * @param entry
     */
    public void processBGData(String entry)
    {
        if ((entry==null) || (entry.length()==0))
            return;
        
        if (entry.contains("END"))
        {
            this.device_running = false;
            this.output_writer.setReadingStop();
            return;
        }
        
        MeterValuesEntry mve = new MeterValuesEntry();
        mve.setBgUnit(OutputUtil.BG_MGDL);
        
        //227  Oct  11 2006 01:38 17 0x00
        String BGString = entry.substring(0,5);
        
        if (BGString.contains("HI"))
        {
            mve.setBgValue("500");
            mve.addParameter("RESULT", "High");
        }
        else
        {
            mve.setBgValue("" + BGString.trim());
        }
        
        String timeString = entry.substring(5,23);
        mve.setDateTimeObject(getDateTime(timeString));
        
        this.output_writer.writeData(mve);
        this.entries_current++;
        readingEntryStatus();
    }
        
        
        
    
    
    
    
    
    protected void setDeviceStopped()
    {
        this.device_running = false;
        this.output_writer.endOutput();
    }
    
    
    
    
    
    protected String getParameterValue(String val)
    {
        String d = val.substring(1, val.length()-1);
        return d.trim();
    }
    

    @SuppressWarnings("unused")
    private static String months_en[] = { "", "Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Oct", "Nov", "Dec"  };
    
    protected ATechDate getDateTime(String datetime)
    {
        // "mm/dd/yy","hh:mm:30 "
        // Oct  11 2006 01:38
        ATechDate dt = new ATechDate(ATechDate.FORMAT_DATE_AND_TIME_MIN);
        
        //dt.day_of_month = Integer.parseInt(datetime.substring(6, 8));
//x        String mnth = datetime.substring(0, 3);
        
        //dt.month = 
        dt.day_of_month = Integer.parseInt(datetime.substring(5, 7));
        dt.year = Integer.parseInt(datetime.substring(8, 12));
        dt.hour_of_day = Integer.parseInt(datetime.substring(13, 15));
        dt.minute = Integer.parseInt(datetime.substring(16, 18));
        /*
        for(int i=0; i<FreestyleMeter.months_en.length; i++)
        {
            if (mnth.equals(FreestyleMeter.months_en[i]))
            {
                dt.month = i;
                break;
            }
            
        }*/
        
        return dt;
        
        /*
        System.out.println("Month: '" + datetime.substring(0, 3) + "'");
        System.out.println("Day: '" + datetime.substring(5, 7)+ "'");
        System.out.println("Year: '" + datetime.substring(8, 12)+ "'");
                        
        System.out.println("Hour: '" + datetime.substring(13, 15)+ "'");
        System.out.println("Year: '" + datetime.substring(16, 18)+ "'");
        
        */
        
        
    }

    
    //private void 
    
    private void readingEntryStatus()
    {
        float proc_read = ((this.entries_current*1.0f)  / this.entries_max);
        
        float proc_total = 5 + (95 * proc_read);
        
        //System.out.println("proc_read: " + proc_read + ", proc_total: " + proc_total);
        
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
     * Returns short name for meter (for example OT Ultra, would return "Ultra")
     * 
     * @return short name of meter
     */
    //public abstract String getShortName();
    
    
    
    
    
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
        return MeterManager.METER_COMPANY_ABBOTT;
    }
    
    
    /**
     * @param args
     */
    public static void main(String args[])
    {
        /*
        //Oct  11 2006 01:38
        
        Freestyle fm = new Freestyle();
        
        ATechDate atd = fm.getDateTime("Oct  11 2006 01:38");
        
        System.out.println(atd.getDateString() + " " + atd.getTimeString());
        */
/*
        Freestyle fm = new Freestyle();
        fm.output_writer = new ConsoleOutputWriter();
        
        String data[] = { "093  May  30 2005 00:46 16 0x01",
                          "105  May  30 2005 00:42 16 0x00",
                          "085  May  29 2005 23:52 16 0x00",
                          "073  May  29 2005 21:13 16 0x00",
                          "091  May  29 2005 21:11 16 0x01"  };
        
        for(int i=0; i<data.length; i++)
        {
            fm.processBGData(data[i]);
        }
  */      
    }

    /**
     * Maximum of records that device can store
     */
    public int getMaxMemoryRecords()
    {
        // TODO Auto-generated method stub
        return 450;
    }

    /** 
     * Get Device ClassName
     */
    public String getDeviceClassName()
    {
        return "ggc.meter.device.freestyle.OptiumXceed";
    }

    /**
     * getDeviceId - Get Device Id 
     * 
     * @return id of device within company
     */
    public int getDeviceId()
    {
        return 0;
    }

    /**
     * getIconName - Get Icon of meter
     * 
     * @return icon name
     */
    public String getIconName()
    {
        return null;
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
    
    
    
}