/*
 * Created on 10.08.2002
 *
 * To change this generated comment edit the template variable "filecomment":
 * Window>Preferences>Java>Templates.
 */

package ggc.meter.device;


import ggc.meter.data.MeterValuesEntry;
import ggc.meter.manager.MeterDevice;
import ggc.meter.manager.company.AbstractMeterCompany;
import ggc.meter.util.DataAccessMeter;
import ggc.meter.util.I18nControl;
import ggc.plugin.device.DeviceIdentification;
import ggc.plugin.device.PlugInBaseException;
import ggc.plugin.output.OutputWriter;
import ggc.plugin.protocol.SerialProtocol;
import gnu.io.NoSuchPortException;
import gnu.io.SerialPortEvent;

import java.util.ArrayList;
import java.util.StringTokenizer;

import javax.swing.ImageIcon;

import com.atech.graphics.dialogs.selector.ColumnSorter;
import com.atech.graphics.dialogs.selector.SelectableInterface;


public abstract class AbstractSerialMeter extends SerialProtocol implements MeterInterface, SelectableInterface
{

    
    
    protected int m_status = 0;
    protected I18nControl ic = I18nControl.getInstance();

    protected String m_info = "";
    protected int m_time_difference = 0;
    protected ArrayList<MeterValuesEntry> data = null;
    protected String device_name = "Undefined";
    protected OutputWriter output_writer;
    
    AbstractMeterCompany meter_company = null;
    

    public AbstractSerialMeter()
    {
        super();
    }

    /*
    public AbstractSerialMeter(int i2, int i3, int i4, int i5)
    {
        super();
    }
    */

    
    
    public AbstractSerialMeter(DataAccessMeter da)
    {
        super(da);
    }
    
    
//	this.m_device_index = device_index;
    
    
    public void serialEvent(SerialPortEvent event)
    {
    	
    }

    
    
    boolean can_read_data = false; 
	boolean can_read_partitial_data = false;
	boolean can_read_device_info = false;
	boolean can_read_device_configuration = false;
    
    
	
	
	
    public void setDeviceAllowedActions(boolean can_read_data, 
    									boolean can_read_partitial_data,
    									boolean can_read_device_info,
    									boolean can_read_device_configuration)
    {
        this.can_read_data = can_read_data; 
        this.can_read_partitial_data = can_read_partitial_data;
        this.can_read_device_info = can_read_device_info;
        this.can_read_device_configuration = can_read_device_configuration;
    }
    

    public void setCommunicationSettings(int baudrate, int databits,
    									 int stopbits, int parity,
    									 int flow_control,
    									 int event_type)
    {
    	super.setCommunicationSettings(baudrate, databits, stopbits, parity, flow_control, event_type);
    }
    
    
    String meter_group = null;
    String meter_device = null;
    
    MeterDevice device_instance = null;
    
    
    public void setMeterType(String group, String device)
    {
        this.device_name = device;
        
        DeviceIdentification di = new DeviceIdentification(DataAccessMeter.getInstance().getI18nControlInstance());
        di.company = group;
        di.device_selected = device;
        
        this.output_writer.setDeviceIdentification(di);
        //this.output_writer.
    	//this.device_instance = MeterManager.getInstance().getMeterDevice(group, device);
    }
    
    /*
    public String getName()
    {
        return this.device_name;
    }*/
    
    
    String serial_port = null;
    
    public void setSerialPort(String port) throws PlugInBaseException
    {
    	this.serial_port = port;
    	
    	this.setPort(port);
    	
    }
    
    
    
    /**
     * getConnectionPort - connection port data
     * 
     * @return connection port as string
     */
    public String getConnectionPort()
    {
        return this.serial_port;
    }
    
    
    
    
    public String getSerialPort()
    {
    	return this.serial_port;
    }
    
    
    /**
     * hasSpecialProgressStatus - in most cases we read data directly from device, in this case we have 
     *    normal progress status, but with some special devices we calculate progress through other means.
     * @return true is progress status is special
     */
    public boolean hasSpecialProgressStatus()
    {
        return false;
    }
    
    
    
    /*
    public GenericMeter(int meter_type, String portName)
    {

	super(meter_type,
	      9600, 
	      SerialPort.DATABITS_8, 
	      SerialPort.STOPBITS_1, 
	      SerialPort.PARITY_NONE);

	data = new ArrayList<DailyValuesRow>();

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
*/
    /**
     * Used for opening connection with device.
     * @return boolean - if connection established
     */
    //@Override
    public boolean open() throws PlugInBaseException
    {
        return super.open();
        
            
    }


    /**
     * Will be called, when the import is ended and freeing resources.
     */
    //@Override
    public void close()
    {
        if (this.serialPort==null)
            return;
        
        this.serialPort.removeEventListener();
        this.serialPort.close();
    }



    /**
     * getTimeDifference - returns time difference between Meter and Computer
     */
    public int getTimeDifference()
    {
	//return this.m_time_difference;
        return 0;
    }


    /**
     * getInfo - returns Meter information
     */
    public String getInfo()
    {
        return "Generic Device, v0.1\nNo real device connected.";
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


    
    
    /**
     * getDeviceSpecialComment - special comment for device (this is needed in case that we need to display
     *    special comment about device (for example pix device, doesn't display anything till the end, which
     *    would be nice if user knew. 
     */
    public String getDeviceSpecialComment()
    {
        return "";
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
     * getDeviceInfo - get Device info (firmware and software revision)
     */
    public DeviceIdentification getDeviceInfo()
    {
        return this.output_writer.getDeviceIdentification();
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
        //return this.data;
    	return null;
    }


    /**
     * getData - get data for specified time
     */
    public ArrayList<MeterValuesEntry> getData(int from, int to)
    {
    	/*
	ArrayList<DailyValuesRow> out = new ArrayList<DailyValuesRow>();

	for (int i=0; i<this.data.size(); i++)
	{
	    DailyValuesRow dwr = this.data.get(i);

	    if ((dwr.getDateTime() > from) && (dwr.getDateTime() < to))
	    {
		out.add(dwr);
	    }
	}

	return out; */
    	return null;
    }




    protected void processData(String input)
    {
	input = input.replace("||", "|_|");
	input = input.replace("||", "|_|");
	input = input.replace("||", "|_|");

	if (input.contains("|^^^Glucose|"))
	{
	    readData(input);
	    //StringTokenizer strtok = new StringTokenizer(input, "|");
	    //System.out.println("Data (" + strtok.countTokens() + "): " + input);
	}
	else if (input.contains("|Bayer"))
	{
	    //System.out.println("Device Id: " + input);
	    readDeviceIdAndSettings(input);
	}
    }


    protected void readDeviceIdAndSettings(String input)
    {
	StringTokenizer strtok = new StringTokenizer(input, "|");

	// -10751|Bayer7150^2.04\20.0^7150A1155328|_|_|_|_|_|_|P|1|200612272011
	strtok.nextToken();

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

    }


    protected void readDeviceId(String input)
    {
    	StringTokenizer strtok = new StringTokenizer(input, "^");
    
    	String inf = "";
    
    	String id = strtok.nextToken();
    	String versions = strtok.nextToken();
    	String serial = strtok.nextToken();
    
    	inf += ic.getMessage("PRODUCT_CODE") + ": ";
    
    	if ((id.equals("Bayer6115")) || (id.equals("Bayer6116")))
    	{
    	    inf += "BREEZE� Meter Family (";
    	}
    	else if (id.equals("Bayer7150"))
    	{
    	    inf += "CONTOUR� Meter Family (";
    	}
    	else if (id.equals("Bayer3950"))
    	{
    	    inf += "DEX� Meter Family (";
    	}
    	else if (id.equals("Bayer3883"))
    	{
    	    inf += "ELITE� XL Meter Family (";
    	}
    	else
    	{
    	    inf += "Unknown Meter Family (";
    	}
    
    	inf += id;
    	inf += ")\n";
    
    	StringTokenizer strtok2 = new StringTokenizer(versions, "\\");
    
    	inf += ic.getMessage("SOFTWARE_VERSION") + ": " + strtok2.nextToken();
    	inf += ic.getMessage("\nEEPROM_VERSION") + ": " + strtok2.nextToken();
    
    	inf += ic.getMessage("\nSERIAL_NUMBER") + ": " + serial;
    
    	this.m_info = inf;

    }

    protected void readDateInformation(String dt)
    {

        /*
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
*/
    }


    protected void readData(String input)
    {
        /*
    	StringTokenizer strtok = new StringTokenizer(input, "|");
    	//System.out.println("Data (" + strtok.countTokens() + "): " + input);
    
    	// 1R|2|^^^Glucose|2.83|mmol/L^P|_|_|_|_|_|_|200612190719
    	strtok.nextToken();
    	strtok.nextToken();
    	strtok.nextToken();
    
    	String value = strtok.nextToken();
    	String unit = strtok.nextToken();
    
    	strtok.nextToken();
    	strtok.nextToken();
    	strtok.nextToken();
    	strtok.nextToken();
    	strtok.nextToken();
    	strtok.nextToken();
    
    	String time = strtok.nextToken();
    
    
    
    	//mg/dL or mmol/L
    
    	System.out.println("Dt: " + time + " " + value + " " + unit);
    
    	DailyValuesRow dv = new DailyValuesRow();
    	dv.setDateTime(Long.parseLong(time));
    
    	if (unit.startsWith("mg/dL"))
    	{
    	    dv.setBG(DailyValuesRow.BG_MGDL, value);
    	}
    	else
    	{
    	    dv.setBG(DailyValuesRow.BG_MMOLL, value);
    	}
    
    	this.data.add(dv);
*/
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


    public void test2()
    {
        writeToMeter(1, "d", null);
    }


    

    public void writeToMeter(int type, String cmd1, String cmd2)
    {

	/*

	// read everything
	while(mode != MODE_ENQ)
	    waitTime(100);

	writePort(5);  // ENQ

	waitTime(1000); 

	if (mode == MODE_EOT)
	{
	    writePort(5);  // ENQ

	    waitTime(1000); 
	}

	//writePort(6);  // ACK

	
	while (mode != MODE_EOT)
	{
	    writePort(6);  // ACK
	    waitTime(500);
	}
	*/
	

	// commands

	

	writePort(21);  // NAK
	waitTime(500);

	writePort(5);  // ENQ
	waitTime(1000);

	//while(mode != MODE_ACK)
	//    waitTime(100);

	writePort("R|");

	waitTime(500);

	writePort("D|");

	waitTime(500);

	System.out.println("Received Text: " + this.receivedText);

	//Enumeration en = new Enumerator(type);
	//en.hasMoreElements()
	//en.nextElement();
/*
	waitTime(1000); 

	if (mode == MODE_EOT)
	{
	    writePort(5);  // ENQ

	    waitTime(1000); 
	}
*/
	/*
	writePort("R|D|");
	writePort(13);
*/
	
 /*       if (type==1)
	{
	    writePort("r");
	    writePort(13);
	}
	else
	    writePort("w");

	waitTime(500);

	System.out.println("Return after read/write " + getModeString());

	System.out.println("Command 1: " + cmd1);

	//this.portOutputStream.write(
	writePort(cmd1 );

	System.out.println("Return after cmd1 " + getModeString());
   */ 
	
	/*
	//writePort(6);  // ACK


	while (mode != MODE_EOT)
	{
	    writePort(6);  // ACK
	    waitTime(500);
	}
*/


	/*
	while(mode == AscensiaMeter.MODE_OUT)
	{
	    waitTime(100);
	}
	*/
/*
	if (mode == AscensiaMeter.MODE_ENQ)
	{
	    writePort((byte)6); // ACK
	    waitTime(500);
	}
*/
	/*
	if (mode == AscensiaMeter.MODE_ENQ)
	{
	    writePort((byte)5); // ENQ
	    waitTime(500);
	}

	System.out.println("Return after ENQ: " + getModeString());

	System.out.println("Mode " );

	if (type==1)
	    writePort("R|");
	else
	    writePort("W|");

	waitTime(500);

	System.out.println("Return after read/write " + getModeString());

	System.out.println("Command 1: " + cmd1);

	//this.portOutputStream.write(
	writePort(cmd1 + "|");

	System.out.println("Return after cmd1 " + getModeString());


	if (cmd2!=null)
	{
	    waitTime(500);
	    System.out.println("Command 2: " + cmd1);

	    //this.portOutputStream.write(
	    writePort(cmd2 + "|");

	}
*/	


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

    private void writePort(byte[] input)
    {
        /*
    	try
    	{
    	    this.portOutputStream.write(input);
    	}
    	catch(Exception ex)
    	{
    	    System.out.println("Error writing to Serial: "+ ex);
    	}*/
    }

    private byte[] getBytes(String inp)
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


    /*
    {


	//System.out.println();

	// Determine type of event.
	switch (event.getEventType()) 
	{
	    case SerialPortEvent.DATA_AVAILABLE:
		{
		    int newData = 0;
		    receivedText = "";
		    try
		    {
			while ((newData=portInputStream.read())!=-1)
			{
			    switch (newData)
			    {
				case 6:
				    System.out.println("<ACK>");
				    mode = AscensiaMeter.MODE_ACK;
				    break;

				case 13:
				    System.out.println("<CR>");
				    break;

				case 5:
				    System.out.println("<ENQ>");
				    mode = AscensiaMeter.MODE_ENQ;
				    //this.portOutputStream.write(6);
				    break;
				case 4:
				    System.out.println("<EOT>");
				    mode = AscensiaMeter.MODE_EOT;
				    break;

				case 23:
				    System.out.println("<ETB>");
				    break;

				case 3:
				    System.out.println("<ETX>");
				    break;

				case 10:
				    System.out.println("<LF>");
				    break;

				case 21:
				    System.out.println("<NAK>");
				    mode = AscensiaMeter.MODE_NAK;
				    break;

				case 2:
				    System.out.println("<STX>");
				    break;

				default:
				    {
					System.out.print((char)newData);
					receivedText += (new Character((char)newData)).toString();
				    }
			    }
			    //inputBuffer.append((char)newData);
			    //System.out.print((char)newData);
			}
		    }
		    catch(Exception ex)
		    {
			System.out.println("Exception:" + ex);
		    }

		    //System.out.print(newData + " ");
/*
		    dataFromMeter = true;

		    System.out.println("Data");

		    timeOut += 5000;
*/
  /*              } break;


		// If break event append BREAK RECEIVED message.
	    case SerialPortEvent.BI:
		System.out.println("recievied break");
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
		break;
	    case SerialPortEvent.PE:
		System.out.println("recievied pe");
		break;
	    case SerialPortEvent.RI:
		System.out.println("recievied ri");
		break;
	}
    } */





    public void readDeviceData() throws MeterException
    {
    }




    //************************************************
    //***          Process Meter Data              ***
    //************************************************


    

    
    /**
     * canReadData - Can Meter Class read data from device
     * 
     * @return true if action is allowed
     */
    public boolean canReadData()
    {
    	return this.can_read_data;
    }

    /**
     * canReadPartitialData - Can Meter class read (partitial) data from device, just from certain data
     * 
     * @return true if action is allowed
     */
    public boolean canReadPartitialData()
    {
    	return this.can_read_partitial_data;
    }


    
    /**
     * canReadDeviceInfo - tells if we can read info about device
     * 
     * @return true if action is allowed
     */
    public boolean canReadDeviceInfo()
    {
    	return this.can_read_device_info;
    }
    
    
    /**
     * canReadConfiguration - tells if we can read configuration from device
     * 
     * @return true if action is allowed
     */
    public boolean canReadConfiguration()
    {
    	return this.can_read_device_configuration;
    }
    
    

    //************************************************
    //***      Meter Identification Methods        ***
    //************************************************

    /**
     * getName - Get Name of meter. 
     * Should be implemented by protocol class.
     */
    public String getName()
    {
    	if (this.device_instance==null)
    		return "Generic Serial Device";
    	else
    		return this.device_instance.name;
    }


    /**
     * getIcon - Get Icon of meter
     * Should be implemented by protocol class.
     */
    public ImageIcon getIcon()
    {
    	if (this.device_instance==null)
    		return null;
    	else
    		return m_da.getImageIcon(this.device_instance.picture); 
    	//this.device_instance.picture;
    }
    


    /**
     * getMeterIndex - Get Index of Meter 
     * Should be implemented by protocol class.
     */
    public int getMeterIndex()
    {
        return 0;
    }




    //************************************************
    //***                    Test                  ***
    //************************************************

    public void test()
    {
    }



    
    /* 
     * compareTo
     */
    public int compareTo(SelectableInterface o)
    {
        // TODO Auto-generated method stub
        return 0;
    }


    /* 
     * getColumnCount
     */
    public int getColumnCount()
    {
        return 3;
    }


    String device_columns[] = { ic.getMessage("METER_COMPANY"), ic.getMessage("METER_DEVICE"), ic.getMessage("DEVICE_CONNECTION") }; 
    
    /* 
     * getColumnName
     */
    public String getColumnName(int num)
    {
        return device_columns[num-1];
    }


    /* 
     * getColumnValue
     */
    public String getColumnValue(int num)
    {
        switch(num)
        {
         
            case 2:
                return this.getName();
                
            case 3:
                return this.getMeterCompany().getConnectionSamples();

            case 1:
            default:    
                return this.getMeterCompany().getName();
                
                
        }
    }


    /* 
     * getColumnValueObject
     */
    public Object getColumnValueObject(int num)
    {
        return this.getColumnValue(num);
    }


    /* 
     * getColumnWidth
     */
    public int getColumnWidth(int num, int width)
    {
        // TODO Auto-generated method stub
        return 0;
    }


    /* 
     * getItemId
     */
    public long getItemId()
    {
        return 0;
    }


    /* 
     * getShortDescription
     */
    public String getShortDescription()
    {
        return this.getName();
    }


    /* 
     * isFound
     */
    public boolean isFound(int from, int till, int state)
    {
        return true;
    }


    /* 
     * isFound
     */
    public boolean isFound(int value)
    {
        return true;
    }


    /* 
     * isFound
     */
    public boolean isFound(String text)
    {
        return true;
    }


    /* 
     * setColumnSorter
     */
    public void setColumnSorter(ColumnSorter cs)
    {
    }


    /* 
     * setSearchContext
     */
    public void setSearchContext()
    {
    }

    
    
    

    public void setMeterCompany(AbstractMeterCompany company)
    {
        this.meter_company = company;
    }
    
    
    public AbstractMeterCompany getMeterCompany()
    {
        return this.meter_company;
    }




}
