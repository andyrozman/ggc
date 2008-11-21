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
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.TooManyListenersException;
import java.util.Vector;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.atech.i18n.I18nControlAbstract;

/**
 *  Application:   GGC - GNU Gluco Control
 *  Plug-in:       GGC PlugIn Base (base class for all plugins)
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
 *  Filename:  ###---###  
 *  Description:
 * 
 *  Author: Andy {andy@atech-software.com}
 */


public abstract class SerialProtocol implements SerialPortEventListener //implements MeterInterface, SerialPortEventListener //, Runnable
{

    private static Log log = LogFactory.getLog("ProtocolLog");

    
    
    protected I18nControlAbstract m_ic = null; //I18nControl.getInstance();
    protected DataAccessPlugInBase m_da = null; //DataAccessMeter.getInstance();


    protected boolean isPortOpen = false;
    public boolean dataFromMeter = false;
    public SerialPort serialPort = null;
    protected CommPortIdentifier portIdentifier = null;
    public OutputStream portOutputStream = null;
    public InputStream portInputStream = null;
    public String port_name = null;

    public long startTime = System.currentTimeMillis();
    protected long timeOut = 50000;

//x    private EventListenerList listenerList = new EventListenerList();
//x    private ImageIcon image = null;
//z    private String useInfoMessage = null;
//x    private String name = null;

    public static final int SERIAL_EVENT_NONE = 0;
    public static final int SERIAL_EVENT_DATA_AVAILABLE = 1;
    public static final int SERIAL_EVENT_BREAK_INTERRUPT = 2;
    public static final int SERIAL_EVENT_OUTPUT_EMPTY = 4;
    
    public static final int SERIAL_EVENT_ALL = 7;
    
    
    
    int baudrate;
    int databits;
    int stopbits;
    int parity;
    int flow_control;
    protected int event_type;
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
    public void setPort(String port) throws PlugInBaseException
    {
        try
        {
            portIdentifier = CommPortIdentifier.getPortIdentifier(port);
            port_name = port;
        }
        catch(NoSuchPortException ex)
        {
            System.out.println("SerialProtocol::setPort:: No such port: " + ex);
            log.error("No such port exception: " + ex.getMessage(), ex);
            throw new PlugInBaseException(ex);
        }
    }

    /*
    public String getPort()
    {
        return port_name;
    }*/
    
    
    /**
     * @throws MeterException 
     * @see data.imports.DataImport#open()
     */
    
    // open was moved to abstract
    //public boolean open() throws PlugInBaseException
/*    {
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

            
            if (this.event_type!=SerialProtocol.SERIAL_EVENT_NONE)
                serialPort.addEventListener(this);
            
            
            isPortOpen = true;
            System.out.println("open port : " + portIdentifier.getName());
            //serialPort.addEventListener(this);

            serialPort.enableReceiveTimeout(250); //.setTimeoutRx(250);
            serialPort.setDTR(true);
            serialPort.setRTS(true);

            

        } 
        catch (UnsupportedCommOperationException ex)
        {
            
        }
        catch (PortInUseException exc) 
        {
        	System.out.println("SerialProtocol: open():Exception - in use");
            //throw new ImportException(exc);
        } 
        catch (TooManyListenersException exc) 
        {
            System.out.println("SerialProtocol: open():Exception - too many list");
            //throw new ImportException(exc);
        }  
        catch (IOException exc) 
        {
        	System.out.println("SerialProtocol: open():Exception - io");
            //throw new ImportException(exc);
        } 
        
        catch(NoSuchPortException ex)
        {
            
        }

        
        	
        	
        	
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
/*
        //if (isPortOpen)
        //    fireImportChanged(new ImportEvent(this, ImportEvent.PORT_OPENED, portIdentifier));

        return isPortOpen;
    }
*/
    
    
    
    public boolean open() throws PlugInBaseException
    {
        if (isPortOpen)
            return isPortOpen;
        
        //if (portIdentifier == null) 
        //    throw new ImportException(m_ic.getMessage("NO_COM_PORT_SPECIFIED"));
        
        try 
        {
            //this.output_writer.writeLog(LogEntryType.INFO, "AbstractSerialMeter::open()");
            System.out.println("SerialProtocol: open() - open");
            serialPort = (SerialPort)portIdentifier.open("ggc", (int)timeOut);
        
            
            //this.output_writer.writeLog(LogEntryType.INFO, "AbstractSerialMeter::open() - setting parameters");
            
            
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

            
            if ((this.event_type==SerialProtocol.SERIAL_EVENT_ALL) || 
                (this.event_type==SerialProtocol.SERIAL_EVENT_OUTPUT_EMPTY))
            {
                serialPort.notifyOnOutputEmpty(true);
            }
            else
                serialPort.notifyOnOutputEmpty(false);
            
            
            
            if (this.event_type!=SerialProtocol.SERIAL_EVENT_NONE)
                serialPort.addEventListener(this);
            
            
            isPortOpen = true;
            //System.out.println("open port : " + portIdentifier.getName());
            //serialPort.addEventListener(this);
        
            serialPort.enableReceiveTimeout(250); //.setTimeoutRx(250);
            serialPort.setDTR(true);
            serialPort.setRTS(true);
        
            
        
        } 
        catch (UnsupportedCommOperationException ex)
        {
            System.out.println("SerialProtocol::open(). Unsupported comm operation: " + ex);
            log.error("Unsupported comm operation: " + ex.getMessage(), ex);
            throw new PlugInBaseException(ex);
        }
        catch (PortInUseException ex) 
        {
            System.out.println("SerialProtocol::open(). Port in use: " + ex);
            log.error("Port in use: " + ex.getMessage(), ex);
            throw new PlugInBaseException(ex);
        } 
        catch (TooManyListenersException ex) 
        {
            System.out.println("SerialProtocol::open(). Too many listeners: " + ex);
            log.error("Too many listeners: " + ex.getMessage(), ex);
            throw new PlugInBaseException(ex);
        }  
        catch (IOException ex) 
        {
            System.out.println("SerialProtocol::open(). IO exception: " + ex);
            log.error("IO Exception: " + ex.getMessage(), ex);
            throw new PlugInBaseException(ex);
        } 
        catch(Exception ex)
        {
            if (ex instanceof NoSuchPortException)
            {
                System.out.println("SerialProtocol::open(). No such port: " + ex);
                log.error("No such port: " + ex.getMessage(), ex);

                printAllAvailableSerialPorts();

                throw new PlugInBaseException(ex);
            }
            else
            {
                System.out.println("SerialProtocol::open(). Exception: " + ex);
                log.error("Exception: " + ex.getMessage(), ex);
                throw new PlugInBaseException(ex);
            }
        }
        
        
            
            
            
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
        
        //if (isPortOpen)
        //    fireImportChanged(new ImportEvent(this, ImportEvent.PORT_OPENED, portIdentifier));
        
        return isPortOpen;
    }
    
    
    
    
    
    
    
    protected void setConnectionParameters()
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
    

    
    public byte[] readLineBytes() throws IOException //, SerialIOHaltedException
    {
        char c = '\uFFFF';
        boolean flag = false;
        StringBuffer stringbuffer = new StringBuffer("");

        ArrayList<Byte> lst = new ArrayList<Byte>();
        
        byte j;
        do
        {
            int i = c;
            j = (byte)this.portInputStream.read();
            
            //System.out.print(j + " ");
            
            c = (char)j;
            if(j != -1)
            {
                Byte b = new Byte(j);
                lst.add(b);
                //stringbuffer.append(c);
            }
            if(i == 13 && c == '\n')
                flag = true;
        } while(j != -1 && !flag);
        
        byte[] arr = new byte[lst.size()];
        
        for(int i=0; i<lst.size(); i++)
        {
            arr[i] = lst.get(i).byteValue();
        }
        
        return arr;
        //byte[] arr = lst.toArray(arr);
        
        //return lst.toArray(a);
        
        //return new String(stringbuffer);
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





    public void printAllAvailableSerialPorts()
    {
        Vector<CommPortIdentifier> lst = SerialProtocol.getAllAvailablePorts();
        
        System.out.println("Displaying all available ports");
        System.out.println("-------------------------------");
        for(int i=0;i<lst.size(); i++)
        {
            System.out.println(lst.get(i));
        }
        
    }





    
    @SuppressWarnings("unchecked")
    public static Vector<CommPortIdentifier> getAvailableSerialPorts()
    {
        //Vector<String> retVal = new Vector<String>();
        Vector<CommPortIdentifier> retVal = new Vector<CommPortIdentifier>();
        
        try
        {
            Enumeration enume = CommPortIdentifier.getPortIdentifiers();
            while (enume.hasMoreElements()) 
            {
                CommPortIdentifier portID = (CommPortIdentifier)enume.nextElement();
                if (portID.getPortType() == CommPortIdentifier.PORT_SERIAL)
                    retVal.add(portID);
            }
        }
        catch(Exception ex)
        {
            System.out.println("Exception: getAvailableSerialPorts: " + ex);
        }
        return retVal;

    }


    
    public static Vector<String> getAllAvailablePortsString()
    {
        Vector<String> retVal = new Vector<String>();
//        Vector<CommPortIdentifier> retVal = new Vector<CommPortIdentifier>();

        try
        {
            //Vector retVal = new Vector();
//            int counter = 0;
            
            //CommPortIdentifier.
            
            Enumeration<?> enume = CommPortIdentifier.getPortIdentifiers();
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
    
    
    @SuppressWarnings("unchecked")
    public static Vector<CommPortIdentifier> getAllAvailablePorts()
    {
//        Vector<String> retVal = new Vector<String>();
        Vector<CommPortIdentifier> retVal = new Vector<CommPortIdentifier>();

        try
        {
            //Vector retVal = new Vector();
//            int counter = 0;
            
            //CommPortIdentifier.
            
            Enumeration enume = CommPortIdentifier.getPortIdentifiers();
            while (enume.hasMoreElements()) 
            {
                CommPortIdentifier portID = (CommPortIdentifier)enume.nextElement();
                //if (portID.getPortType() == CommPortIdentifier.PORT_SERIAL)
                    retVal.add(portID);
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
