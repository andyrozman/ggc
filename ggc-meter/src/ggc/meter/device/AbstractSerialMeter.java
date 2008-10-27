package ggc.meter.device;

import ggc.meter.data.MeterValuesEntry;
import ggc.meter.util.DataAccessMeter;
import ggc.meter.util.I18nControl;
import ggc.plugin.device.DeviceIdentification;
import ggc.plugin.device.PlugInBaseException;
import ggc.plugin.manager.company.AbstractDeviceCompany;
import ggc.plugin.output.OutputWriter;
import ggc.plugin.protocol.SerialProtocol;
import gnu.io.SerialPortEvent;

import java.util.ArrayList;

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

    AbstractDeviceCompany device_company = null;

    public AbstractSerialMeter()
    {
        super();
    }

    
    public AbstractSerialMeter(AbstractDeviceCompany cmp)
    {
        this.setDeviceCompany(cmp);
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

    // this.m_device_index = device_index;

    public void serialEvent(SerialPortEvent event)
    {

    }

    boolean can_read_data = false;
    boolean can_read_partitial_data = false;
    boolean can_read_device_info = false;
    boolean can_read_device_configuration = false;

    public void setDeviceAllowedActions(boolean can_read_data, boolean can_read_partitial_data, boolean can_read_device_info, boolean can_read_device_configuration)
    {
        this.can_read_data = can_read_data;
        this.can_read_partitial_data = can_read_partitial_data;
        this.can_read_device_info = can_read_device_info;
        this.can_read_device_configuration = can_read_device_configuration;
    }

    public void setCommunicationSettings(int baudrate, int databits, int stopbits, int parity, int flow_control, int event_type)
    {
        super.setCommunicationSettings(baudrate, databits, stopbits, parity, flow_control, event_type);
    }

    String meter_group = null;
    String meter_device = null;

    // MeterDevice device_instance = null;

    public void setMeterType(String group, String device)
    {
        this.device_name = device;

        DeviceIdentification di = new DeviceIdentification(DataAccessMeter.getInstance().getI18nControlInstance());
        di.company = group;
        di.device_selected = device;

        this.output_writer.setDeviceIdentification(di);
        // this.output_writer.
        // this.device_instance =
        // MeterManager.getInstance().getMeterDevice(group, device);
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
    // @Override
    public boolean open() throws PlugInBaseException
    {
        return super.open();

    }

    /**
     * Will be called, when the import is ended and freeing resources.
     */
    // @Override
    public void close()
    {
        if (this.serialPort == null)
            return;

        this.serialPort.removeEventListener();
        this.serialPort.close();
    }

    /**
     * getTimeDifference - returns time difference between Meter and Computer
     */
    public int getTimeDifference()
    {
        // return this.m_time_difference;
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

    // ************************************************
    // *** Device Implemented methods ***
    // ************************************************

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

    /*
    private void writePort(String input)
    {
        writePort(getBytes(input));
    }

    private void writePort(int input)
    {
        byte[] b = new byte[1];
        b[0] = (byte) input;
        writePort(b);
    }
*/
    public String receivedText = "";

    /*
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
    //}
/*
    private byte[] getBytes(String inp)
    {

        return inp.getBytes();
    }
*/
    public void waitTime(long time)
    {
        try
        {
            Thread.sleep(time);

        }
        catch (Exception ex)
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

    public void readDeviceData() throws PlugInBaseException
    {
    }

    // ************************************************
    // *** Process Meter Data ***
    // ************************************************

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


    // ************************************************
    // *** Test ***
    // ************************************************

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
        return device_columns[num - 1];
    }

    /* 
     * getColumnValue
     */
    public String getColumnValue(int num)
    {
        switch (num)
        {

        case 2:
            return this.getName();

        case 3:
            return this.getDeviceCompany().getConnectionSamples();

        case 1:
        default:
            {
     //           System.out.println("name: " + this.getName());
                return this.getDeviceCompany().getName();
            }

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

    /**
     * setDeviceCompany - set Company for device
     * 
     * @param company
     */
    public void setDeviceCompany(AbstractDeviceCompany company)
    {
        this.device_company = company;
    }

    /**
     * getDeviceCompany - get Company for device
     * 
     * @param company
     */
    public AbstractDeviceCompany getDeviceCompany()
    {
        return this.device_company;
    }

}
