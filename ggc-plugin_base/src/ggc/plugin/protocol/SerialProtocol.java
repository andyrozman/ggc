
package ggc.plugin.protocol;


import ggc.plugin.device.PlugInBaseException;
import ggc.plugin.util.DataAccessPlugInBase;
import gnu.io.CommPortIdentifier;
import gnu.io.NoSuchPortException;
import gnu.io.PortInUseException;
import gnu.io.SerialPort;
import gnu.io.SerialPortEvent;
import gnu.io.SerialPortEventListener;
import gnu.io.UnsupportedCommOperationException;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Enumeration;
import java.util.Vector;

import com.atech.i18n.I18nControlAbstract;



public abstract class SerialProtocol implements SerialPortEventListener //implements MeterInterface, SerialPortEventListener //, Runnable
{

    protected I18nControlAbstract m_ic = null; //I18nControl.getInstance();
    protected DataAccessPlugInBase m_da = null; //DataAccessMeter.getInstance();


    private boolean isPortOpen = false;
    public boolean dataFromMeter = false;
    public SerialPort serialPort = null;
    protected CommPortIdentifier portIdentifier = null;
    public OutputStream portOutputStream = null;
    public InputStream portInputStream = null;

    public long startTime = System.currentTimeMillis();
    protected long timeOut = 50000;

//x    private EventListenerList listenerList = new EventListenerList();
//x    private ImageIcon image = null;
//z    private String useInfoMessage = null;
//x    private String name = null;

    public static final int SERIAL_EVENT_NONE = 0;
    public static final int SERIAL_EVENT_DATA_AVAILABLE = 1;
    public static final int SERIAL_EVENT_BREAK_INTERRUPT = 2;
    public static final int SERIAL_EVENT_ALL = 3;
    
    
    
    int baudrate;
    int databits;
    int stopbits;
    int parity;
    int flow_control;
    int event_type;
    //serialPort.getFlowControlMode();


    public SerialProtocol()
    {
    	//super();
    }

    
    
    
    /**
     * Constructor for SerialMeterImport.
     */
    public SerialProtocol(DataAccessPlugInBase da)
    {
        this.m_da = da;
        this.m_ic = da.getI18nControlInstance();
    }

    
    public void setCommunicationSettings(int baudrate, 
                                         int databits,
    									 int stopbits, 
    									 int parity,
    									 int flow_control,
    									 int event_type)
    {
    	this.baudrate = baudrate;
    	this.databits = databits;
    	this.stopbits = stopbits;
    	this.parity = parity;
    	this.flow_control = flow_control;
    	this.event_type = event_type;
    }
    
    
    /**
     * Return the COM-Port from which will be read.
     * @return String
     */
    public String getPort()
    {
        return portIdentifier.getName();
    }

    /**
     * Set the COM-Port from wich will be read.
     * @param String port
     */
    public void setPort(String port) throws NoSuchPortException
    {
        portIdentifier = CommPortIdentifier.getPortIdentifier(port);
    }

    /**
     * @throws MeterException 
     * @see data.imports.DataImport#open()
     */
    public boolean open() throws PlugInBaseException
    {
        if (isPortOpen)
            return isPortOpen;

        //if (portIdentifier == null) 
        //    throw new ImportException(m_ic.getMessage("NO_COM_PORT_SPECIFIED"));

        try 
        {
        	System.out.println("SerialProtocol: open() - Start");
        	System.out.println("SerialProtocol: open() - open");
            serialPort = (SerialPort)portIdentifier.open("ggc", (int)timeOut);

            
            
            
            System.out.println("SerialProtocol: open() - parameters");
            setConnectionParameters();

            portOutputStream = serialPort.getOutputStream();
            portInputStream = serialPort.getInputStream();
            
            // break interrupt event
            if ((this.event_type==SerialProtocol.SERIAL_EVENT_ALL) || 
                (this.event_type==SerialProtocol.SERIAL_EVENT_BREAK_INTERRUPT))
            {
                serialPort.notifyOnBreakInterrupt(true);
            }
            else
                serialPort.notifyOnBreakInterrupt(false);
                
            // data available
            if ((this.event_type==SerialProtocol.SERIAL_EVENT_ALL) || 
                (this.event_type==SerialProtocol.SERIAL_EVENT_DATA_AVAILABLE))
            {
                serialPort.notifyOnDataAvailable(true);
            }
            else
                serialPort.notifyOnDataAvailable(false);

            try 
            {
                serialPort.addEventListener(this);
                //Thread.sleep(10000);
            } 
            catch (Exception exc) 
            {
            }

            isPortOpen = true;
            System.out.println("open port : " + portIdentifier.getName());
            //serialPort.addEventListener(this);


            

        } 
        catch (PortInUseException exc) 
        {
        	System.out.println("SerialProtocol: open():Exception - in use");
            //throw new ImportException(exc);
        } 
        catch (IOException exc) 
        {
        	System.out.println("SerialProtocol: open():Exception - io");
            //throw new ImportException(exc);
        } 
/*        catch (TooManyListenersException exc) 
        {
        	System.out.println("SerialProtocol: open():Exception - too many list");
            //throw new ImportException(exc);
        } */ 

        
        try 
        {
        	
        	
        	serialPort.enableReceiveTimeout(250); //.setTimeoutRx(250);
        	//serialPort.
            //m_serialPortLocal.setTimeoutTx(250);
        	serialPort.setDTR(true);
        	serialPort.setRTS(true);
        	
        	
        	//serialPort.getBaudBase(9600);
        	//serialPort.getBaudRate(9600);
            //serialPort.enableReceiveTimeout(30);
            //serialPort.enableReceiveTimeout(10000);
            //serialPort.
            //10000
            //serialPort.setInputBufferSize(255);
            //serialPort.setOutputBufferSize(255);
            
            
            //int ss = serialPort.getInputBufferSize();
            //System.out.println("input buffer:" + ss);
/*
        	int ss = serialPort.getReceiveThreshold();
            System.out.println("receive treshold:" + ss);
            
            serialPort.setOutputBufferSize(1000000);
             serialPort.setInputBufferSize(1000000);
             serialPort.enableReceiveThreshold(10000);

             ss = serialPort.getReceiveThreshold();
            System.out.println("receive treshold:" + ss);
  */          
        } 
        catch (Exception e) 
        {
        	//UnsupportedCommOperationException 
	    System.out.println("SerialProtocol: open():Exception - unsported");

        }

        //if (isPortOpen)
        //    fireImportChanged(new ImportEvent(this, ImportEvent.PORT_OPENED, portIdentifier));

        return isPortOpen;
    }

    
    
    
    
    
    
    
    
    private void setConnectionParameters()
    {

        if (serialPort == null)
            return;

/*        
        // Save state of parameters before trying a set.
        int oldBaudRate = serialPort.getBaudRate();
        int oldDatabits = serialPort.getDataBits();
        int oldStopbits = serialPort.getStopBits();
        int oldParity = serialPort.getParity();
        int oldFlowControl = serialPort.getFlowControlMode();
*/
        // Set connection parameters, if set fails return parameters object
        // to original state.
        try 
        {
            serialPort.setSerialPortParams(this.baudrate, 
					   this.databits, 
					   this.stopbits, 
					   this.parity);

        } 
        catch (UnsupportedCommOperationException e) 
        {
        }

        // Set flow control.
        try 
        {
            //			serialPort.setFlowControlMode(
            //					parameters.getFlowControlIn() |
            //					parameters.getFlowControlOut()
            //			);
            //serialPort.setFlowControlMode(SerialPort.FLOWCONTROL_NONE);
        	//serialPort.setFlowControlMode(SerialPort.FLOWCONTROL_XONXOFF_IN | SerialPort.FLOWCONTROL_XONXOFF_OUT);
            
            //SerialPort.FLOWCONTROL_RTSCTS_IN | SerialPort.FLOWCONTROL_RTSCTS_OUT
        } 
        catch (Exception e) 
        {
        	// UnsupportedCommOperationException 
            //throw new SerialConnectionException("Unsupported flow control");
        }
    }

    
    
    
    
    
    
    /**
     * @see data.imports.DataImport#close()
     */
    public void close()
    {
        if (!isPortOpen)
            return;

        serialPort.removeEventListener();
        serialPort.close();
        isPortOpen = false;
        dataFromMeter = false;
        System.out.println("close port : " + portIdentifier.getName());
//        fireImportChanged(new ImportEvent(this, ImportEvent.PORT_CLOSED, portIdentifier));
    }

    /**
     * @see data.imports.DataImport#importData()
     */
/*    public void importData() throws ImportException
    {
        if (portIdentifier == null || serialPort == null || portOutputStream == null || portInputStream == null)
            throw new ImportException(m_ic.getMessage("COM_PORT_NOT_INIT_CORRECT"));

        startTime = System.currentTimeMillis();
        //Thread thread = new Thread(this);
        //thread.start();
    }
*/
    
    
    public int read() throws IOException
    {
    	return portInputStream.read();
    }
    
    public int read(byte[] b) throws IOException
    {
    	return portInputStream.read(b);
    }
    
    
    public int read(byte[] b, int off, int len) throws IOException
    {
    	return portInputStream.read(b, off, len);
    }
    
    
    public String readLine() throws IOException //, SerialIOHaltedException
    {
	    char c = '\uFFFF';
	    boolean flag = false;
	    StringBuffer stringbuffer = new StringBuffer("");
	    //Contract.pre(m_serialPortLocal != null, "m_serialPortLocal is null.");
	    //Contract.pre(m_serialPortLocal.isOpen(), "m_serialPortLocal is not open.");
	    //checkForSerialIOHalted();
	    //MedicalDevice.Util.sleepMS(m_ioDelay);
	    int j;
	    do
	    {
	        int i = c;
	        j = (byte)this.portInputStream.read();
	        c = (char)j;
	        if(j != -1)
	            stringbuffer.append(c);
	        if(i == 13 && c == '\n')
	            flag = true;
	    } while(j != -1 && !flag);
	    //long l = System.currentTimeMillis() - m_startTimeMS;
	    //MedicalDevice.logInfoHigh(this, "readLine(" + l + "MS): read <" + stringbuffer + ">");
	    //m_startTimeMS = System.currentTimeMillis();
	    return new String(stringbuffer);
    }
    
  
    
    public void write(byte[] b) throws IOException
    {
    	portOutputStream.write(b);
    }

    
    public void write(int i) throws IOException
    {
    	portOutputStream.write(i);
    }
    
    
    public void write(byte[] b, int off, int len) throws IOException
    {
    	portOutputStream.write(b, off, len);
    }
    
    
    public void test()
    {
    	//portOutputStream.write(b);
    	//portOutputStream.
    }
    
    
    
//    portInputStream.read()
    
    
    
    
    
    
    
    /**
     * @see data.imports.DataImport#getImportedData()
     */
/*    public DailyValuesRow[] getImportedData()
    {
        return new DailyValuesRow[0];
    }
*/

    
    
    
    
    
    /**
     * @see gnu.io.SerialPortEventListener#serialEvent(SerialPortEvent)
     */
    public abstract void serialEvent(SerialPortEvent event);


    public long getTimeOut()
    {
        return timeOut;
    }


    public void stopImport()
    {
        timeOut = 1;
    }











    
    @SuppressWarnings("unchecked")
    public static Vector<String> getAvailableSerialPorts()
    {
        Vector<String> retVal = new Vector<String>();

        try
        {
            Enumeration enume = CommPortIdentifier.getPortIdentifiers();
            while (enume.hasMoreElements()) 
            {
                CommPortIdentifier portID = (CommPortIdentifier)enume.nextElement();
                if (portID.getPortType() == CommPortIdentifier.PORT_SERIAL)
                    retVal.add(portID.getName());
            }
        }
        catch(Exception ex)
        {
            System.out.println("Exception: getAvailableSerialPorts: " + ex);
        }
        return retVal;

    }


    @SuppressWarnings("unchecked")
    public static Vector<String> getAllAvailablePorts()
    {
        Vector<String> retVal = new Vector<String>();

        try
        {
            //Vector retVal = new Vector();
//            int counter = 0;
            
            Enumeration enume = CommPortIdentifier.getPortIdentifiers();
            while (enume.hasMoreElements()) 
            {
                CommPortIdentifier portID = (CommPortIdentifier)enume.nextElement();
                //if (portID.getPortType() == CommPortIdentifier.PORT_SERIAL)
                    retVal.add(portID.getName());
            }
        }
        catch(Exception ex)
        {
            System.out.println("Exception: getAvailableSerialPorts: " + ex);

        }
        return retVal;

    }
    
    


    public int getConnectionProtocol()
    {
        return ConnectionProtocols.PROTOCOL_SERIAL_USBBRIDGE;
    }
    


}
