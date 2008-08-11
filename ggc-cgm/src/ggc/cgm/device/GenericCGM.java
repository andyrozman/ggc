
package ggc.cgm.device;


import ggc.cgm.data.CGMValuesEntry;
import ggc.cgm.manager.company.AbstractCGMCompany;
import ggc.cgm.util.I18nControl;

import java.util.ArrayList;


public class GenericCGM implements CGMInterface 
{

    protected int m_status = 0;
    protected I18nControl ic = I18nControl.getInstance();

    protected String m_info = "";
    protected int m_time_difference = 0;
    protected ArrayList<CGMValuesEntry> data = null;


    public GenericCGM()
    {
        super();
    }

    
    
    

    boolean can_read_data = false; 
	boolean can_read_partitial_data = false;
	boolean can_clear_data = false;
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
    
    
    
    
    
    public boolean open()
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
/*    public boolean open() throws MeterException
    {
        return super.open();
	//return false;
        //return true;
    }
*/

    /**
     * Will be called, when the import is ended and freeing resources.
     */
    //@Override
    public void close()
    {
        return;
    }


    public int getCGMIndex()
    {
        return 0;
    }

    public String getIconName()
    {
        return null;
    }

    public String getName()
    {
        return "Generic device";
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

    public String getPort()
    {
    	return "None";
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
    public ArrayList<String> getDeviceInfo()
    {
    	return new ArrayList<String>();
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
    public void readDataFull()
    {
        //return this.data;
    	return;
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





    public void readDeviceData() throws CGMException
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
     * canClearData - Can Meter class clear data from meter.
     * 
     * @return true if action is allowed
     */
    public boolean canClearData()
    {
    	return this.can_clear_data;
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
    //***                    Test                  ***
    //************************************************

    public void test()
    {
    }


    AbstractCGMCompany company;

    public void setCGMCompany(AbstractCGMCompany company)
    {
        this.company = company;
    }
    
    
    public AbstractCGMCompany getCGMCompany()
    {
        return this.company;
        
    }


    /**
     * getInstructions - get instructions for device
     * Should be implemented by meter class.
     * 
     * @return instructions for reading data 
     */
    public String getInstructions()
    {
        return null;
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
     * @return true is progress status is special
     */
    public boolean hasSpecialProgressStatus()
    {
        return false;
    }
    
    
    
    /**
     * This is method for reading data from device. All reading from actual device should be done from here.
     * Reading can be done directly here, or event can be used to read data.
     */
    public void readDeviceDataFull() throws CGMException
    {
        return;
    }
    
    
    /**
     * This is method for reading partitial data from device. All reading from actual device should be done from 
     * here. Reading can be done directly here, or event can be used to read data.
     */
    public void readDeviceDataPartitial() throws CGMException
    {
        return;
    }


    /** 
     * This is method for reading configuration
     * 
     * @throws MeterExceptions
     */
    public void readConfiguration() throws CGMException
    {
        return;
    }
    

    /**
     * This is for reading device information. This should be used only if normal dump doesn't retrieve this
     * information (most dumps do). 
     * @throws MeterExceptions
     */
    public void readInfo() throws CGMException
    {
        return;
    }
    
    public int getConnectionProtocol()
    {
        return 0;
    }

    
    

}
