/*
 * Created on 10.08.2002
 *
 * To change this generated comment edit the template variable "filecomment":
 * Window>Preferences>Java>Templates.
 */

package ggc.meter.device.ascensia;


import ggc.meter.data.MeterValuesEntry;
import ggc.meter.device.AbstractSerialMeter;
import ggc.meter.device.DeviceIdentification;
import ggc.meter.device.MeterException;
import ggc.meter.output.OutputUtil;
import ggc.meter.output.OutputWriter;
import ggc.meter.util.I18nControl;
import gnu.io.SerialPort;
import gnu.io.SerialPortEvent;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.StringTokenizer;

import com.atech.utils.ATechDate;
import com.atech.utils.TimeZoneUtil;


public abstract class AscensiaMeter extends AbstractSerialMeter
//extends /*SerialIOProtocol*/  SerialProtocol implements MeterInterface
{

    
    public static final int METER_ASCENSIA_ELITE_XL   = 10001;
    public static final int METER_ASCENSIA_DEX        = 10002;
    public static final int METER_ASCENSIA_BREEZE     = 10003;
    public static final int METER_ASCENSIA_CONTOUR    = 10004;
    
    
    protected int m_status = 0;
    protected I18nControl ic = I18nControl.getInstance();

    protected String m_info = "";
    protected int m_time_difference = 0;
    protected ArrayList<MeterValuesEntry> data = null;
    //protected OutputWriter m_output_writer;
    public TimeZoneUtil tzu = TimeZoneUtil.getInstance();

    public AscensiaMeter()
    {
    }
    

    public AscensiaMeter(int meter_type, String portName, OutputWriter writer)
    {
    	
		super(meter_type, /*portName, */ 
		      9600,
			  //19200,
		      SerialPort.DATABITS_8, 
		      SerialPort.STOPBITS_1, 
		      SerialPort.PARITY_NONE);
	
		this.setCommunicationSettings( 
			      9600,
			      SerialPort.DATABITS_8, 
			      SerialPort.STOPBITS_1, 
			      SerialPort.PARITY_NONE,
			      SerialPort.FLOWCONTROL_NONE);
				
		this.setSerialPort(portName);
		
		data = new ArrayList<MeterValuesEntry>();
		
		this.output_writer = writer; 
	
		try
		{
		    this.setPort(portName);
	
		    if (!this.open())
		    {
		    	this.m_status = 1;
		    }
		}
		catch(Exception ex)
		{
		    System.out.println("AscensiaMeter -> Error adding listener: " + ex);
		    ex.printStackTrace();
		}
    }

    /**
     * Used for opening connection with device.
     * @return boolean - if connection established
     */
    public boolean open() throws MeterException
    {
    	return super.open();
    }


    /**
     * Will be called, when the import is ended and freeing resources.
     */
    @Override
    public void close()
    {
        return;
    }




    /**
     * getTimeDifference - returns time difference between Meter and Computer
     */
    public int getTimeDifference()
    {
	return this.m_time_difference;
    }


    /**
     * getInfo - returns Meter information
     */
    public String getInfo()
    {
        return m_info;
    }


    /**
     * getStatus - get Status of meter
     */
    public int getStatus()
    {
	return m_status;
    }


    /**
     * isStatusOK - has Meter OK status
     */
    public boolean isStatusOK()
    {
	return (m_status == 0);
    }


    
    //************************************************
    //***       Device Implemented methods         ***
    //************************************************
    

    /** 
     * clearDeviceData - Clear data from device 
     */
    public void clearDeviceData()
    {
    	
    }
    
    
    
    /**
     * getDeviceConfiguration - return device configuration
     * @return
     */
    public ArrayList<String> getDeviceConfiguration()
    {
    	return new ArrayList<String>();
    }
    
    
    

    /**
     * getDataFull - get all data from Meter
     */
    public ArrayList<MeterValuesEntry> getDataFull()
    {
	return this.data;
	
    }

    
    
    
    
    

    /**
     * getData - get data for specified time
     */
    public ArrayList<MeterValuesEntry> getData(int from, int to)
    {
    	/*
	ArrayList<MeterValuesEntry> out = new ArrayList<MeterValuesEntry>();

	for (int i=0; i<this.data.size(); i++)
	{
		MeterValuesEntry dwr = this.data.get(i);

	    if ((dwr.getDateTime() > from) && (dwr.getDateTime() < to))
	    {
	    	out.add(dwr);
	    }
	} 
	
	return out;
	*
	*
	*/

	return null;
    }




    protected void processData(String input)
    {
		input = m_da.replaceExpression(input, "||", "|_|"); 
	
		if (input.contains("|^^^Glucose|"))
		{
		    readData(input);
		}
		else if (input.contains("|Bayer"))
		{
		    readDeviceIdAndSettings(input);
		}
    }


    protected void readDeviceIdAndSettings(String input)
    {
        input = input.substring(input.indexOf("Bayer"));
    	
		StringTokenizer strtok = new StringTokenizer(input, "|");
	
		String devId = strtok.nextToken();
		strtok.nextToken();
		strtok.nextToken();
		strtok.nextToken();
		strtok.nextToken();
		strtok.nextToken();
		strtok.nextToken();
		strtok.nextToken();
		strtok.nextToken();
	
		String date = strtok.nextToken();
	
		//System.out.println("Device:\n" + devId + "\nDate: " + date);
		//System.out.println("Data (" + strtok.countTokens() + "): " + input);
	
		readDeviceId(devId);
		readDateInformation(date);

		this.output_writer.writeDeviceIdentification();
	
    }
    
    
    


    protected void readDeviceId(String input)
    {
        
        DeviceIdentification di = this.output_writer.getDeviceIdentification();
        
        System.out.println("readDeviceId: " + input);
    	StringTokenizer strtok = new StringTokenizer(input, "^");
    
    	String inf = "";
    
    	String id = strtok.nextToken();
    	String versions = strtok.nextToken();
    	String serial = strtok.nextToken();
    
    	inf += ic.getMessage("PRODUCT_CODE") + ": ";
    	
    	String tmp;
    
    	if ((id.equals("Bayer6115")) || (id.equals("Bayer6116")))
    	{
    	    //inf += "BREEZE Meter Family (";
    	    tmp = "Breeze Family (";
    	}
    	else if (id.equals("Bayer7150"))
    	{
    	    tmp = "CONTOUR Meter Family (";
    	}
    	else if (id.equals("Bayer3950"))
    	{
    	    tmp = "DEX Meter Family (";
    	}
    	else if (id.equals("Bayer3883"))
    	{
    	    tmp = "ELITE XL Meter Family (";
    	}
    	else
    	{
    	    tmp = "Unknown Meter Family (";
    	}
    
    	tmp+= id;
    	tmp+= ")";
    	
    	di.device_identified = tmp;
    	
    	
    	inf += tmp;
    	inf += "\n";
    
    	StringTokenizer strtok2 = new StringTokenizer(versions, "\\");
    
    	di.device_software_version = strtok2.nextToken();
    	di.device_hardware_version = strtok2.nextToken();
    	di.device_serial_number = serial; 
    	
    	
    	inf += ic.getMessage("SOFTWARE_VERSION") + ": " + di.device_software_version;
    	inf += ic.getMessage("\nEEPROM_VERSION") + ": " + di.device_hardware_version;
    
    	inf += ic.getMessage("\nSERIAL_NUMBER") + ": " + serial;
    
    	this.m_info = inf;

    }

    protected void readDateInformation(String dt)
    {

    	GregorianCalendar gc_meter = new GregorianCalendar();
    	gc_meter.setTime(m_da.getDateTimeAsDateObject(Long.parseLong(dt)));
    
    	GregorianCalendar gc_curr = new GregorianCalendar();
    	gc_curr.setTimeInMillis(System.currentTimeMillis());
    
    	GregorianCalendar gc_comp = new GregorianCalendar();
    	gc_comp.set(Calendar.DAY_OF_MONTH, gc_curr.get(Calendar.DAY_OF_MONTH));
    	gc_comp.set(Calendar.MONTH, gc_curr.get(Calendar.MONTH));
    	gc_comp.set(Calendar.YEAR, gc_curr.get(Calendar.YEAR));
    	gc_comp.set(Calendar.HOUR_OF_DAY, gc_curr.get(Calendar.HOUR_OF_DAY));
    	gc_comp.set(Calendar.MINUTE, gc_curr.get(Calendar.MINUTE));
    
    	long diff = gc_comp.getTimeInMillis() - gc_meter.getTimeInMillis();
    	this.m_time_difference = (-1) * (int)diff;
    
    	//System.out.println("Computer Time: " + gc_comp + "\nMeter Time: " + gc_meter + " Diff: " + this.m_time_difference);

    }


    boolean header_set = false;
    
    protected void readData(String input)
    {
    	try
    	{
	
	    	StringTokenizer strtok = new StringTokenizer(input, "|");
	    
	    	boolean found = false;
	    	
	    	// we search for entry containing Glucose... (in case that data was not 
	    	// received entirely)
	    	while ((!found) && (strtok.hasMoreElements()))
	    	{
	    		String s = strtok.nextToken();
	    		if (s.equals("^^^Glucose"))
	    		{
	    			found = true;
	    		}
	    	}
	    	
	    	if (!found)
	    		return;
	    	
	    	MeterValuesEntry mve = new MeterValuesEntry();
	    	
	    	mve.setBgValue(strtok.nextToken());  // bg_value
	    	String unit = strtok.nextToken();  // unit mmol/L^x, mg/dL^x
	    
	    	mve.addParameter("REF_RANGES", strtok.nextToken());  // Reference ranges (Dex Only) 
	    	mve.addParameter("RES_ABNORMAL_FLAGS", strtok.nextToken());  // Result abnormal flags (7)
            mve.addParameter("USER_MARKS", strtok.nextToken());  // User Marks (8)
	    	mve.addParameter("RES_STATUS_MARKER", strtok.nextToken());  // Result status marker
	    	strtok.nextToken();  // N/A
	    	strtok.nextToken();  // OperatorId (N/A)
	    
	    	String time = strtok.nextToken();  // datetime
	    
	    	mve.setDateTime(tzu.GetCorrectedDateTime(new ATechDate(Long.parseLong(time))));
	    	
	    	if (unit.startsWith("mg/dL"))
	    	{
	    		mve.setBgUnit(OutputUtil.BG_MGDL);
	    		
	    		//this.m_output.writeBGData(atd, bg_value, OutputUtil.BG_MGDL);
	    	    //dv.setBG(DailyValuesRow.BG_MGDL, value);
	    	}
	    	else
	    	{
	    		mve.setBgUnit(OutputUtil.BG_MMOL);
	    		//this.m_output.writeBGData(atd, bg_value, OutputUtil.BG_MMOL);
	    	    //dv.setBG(DailyValuesRow.BG_MMOLL, value);
	    	}
	    	
	    	this.output_writer.writeBGData(mve);
	    	
    	}
    	catch(Exception ex)
    	{
    		System.out.println("Exception: " + ex);
    		System.out.println("Entry: " + input);
    		ex.printStackTrace();
    	}
    
    	//this.data.add(dv);

    }



    protected boolean stx = false;




    public static final int MODE_ENQ = 1;
    public static final int MODE_OUT = 2;
    public static final int MODE_ACK = 3;
    public static final int MODE_NAK = 4;
    public static final int MODE_EOT = 5;

    public int mode = 0;


    public static final int METER_ENQ_READ = 1;
    public static final int METER_ENQ_WRITE = 2;



    



    protected String getModeString()
    {
    	String[] modes = { "None", "ENQuiry", "Out", "ACKnowledge", "Negative AcKnowledge", "End Of Transmition"
    	};

    	return modes[mode];
    }

    
    
    private void writePort(String input)
    {
    	writePort(getBytes(input));
    }

    private void writePort(int input)
    {
    	byte[] b = new byte[1];
    	b[0] = (byte)input;
    	writePort(b);
    }


    public String receivedText = "";

    protected void writePort(byte[] input)
    {
    	try
    	{
    	    this.portOutputStream.write(input);
    	}
    	catch(Exception ex)
    	{
    	    System.out.println("Error writing to Serial: "+ ex);
    	}
    }

    protected byte[] getBytes(String inp)
    {
        return inp.getBytes();
    }


    public void waitTime(long time)
    {
    	try
    	{
    	    Thread.sleep(time);
    
    	}
    	catch(Exception ex)
    	{
    	}
    }


    
    public abstract void serialEvent(SerialPortEvent event);




    public void readDeviceData() throws MeterException
    {
    }




    //************************************************
    //***          Process Meter Data              ***
    //************************************************


    /**
     * processMeterDataMain - this is main method for processing data. It should be called on all data received, and 
     * from here it should be sent to other process* methods. This methods are meant to be used, but don't have to 
     * be used if we have other ways to get data for methods needed (methods marked as used in Meter GUI)
     */
    public void processMeterData(String data)
    {
    }

    /**
     * processMeterIdentification - this should be used to process identification of meter and versions of firmware.
     */
    public void processMeterIdentification(String data)
    {
    }

    /**
     * processMeterTime - this should be used to process time and date of meter
     */
    public void processMeterTime(String data)
    {
    }

    /**
     * processMeterBGEntry - this should be used to process BG data from meter
     */
    public void processMeterBGEntry(String data)
    {
    }


    //************************************************
    //***        Available Functionality           ***
    //************************************************


    /**
     * canReadData - Can Meter Class read data from device
     */
    public boolean canReadData()
    {
        return true;
    }

    /**
     * canReadPartitialData - Can Meter class read (partitial) data from device, just from certain data
     */
    public boolean canReadPartitialData()
    {
        return false;
    }

    /**
     * canClearData - Can Meter class clear data from meter.
     */
    public boolean canClearData()
    {
        return false;
    }





    //************************************************
    //***                    Test                  ***
    //************************************************

    public void test()
    {
    }








}
