package ggc.plugin.protocol;

import ggc.plugin.device.PlugInBaseException;
import ggc.plugin.util.DataAccessPlugInBase;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.TooManyListenersException;
import java.util.Vector;

import javax.comm.CommPortIdentifier;
import javax.comm.NoSuchPortException;
import javax.comm.PortInUseException;
import javax.comm.SerialPort;
import javax.comm.SerialPortEvent;
import javax.comm.SerialPortEventListener;
import javax.comm.UnsupportedCommOperationException;

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
 *  Filename:     SerialProtocol  
 *  Description:  This is implementation for Serial protocol. It contains methods for reading
 *                and writing to/from serial port.
 * 
 *  Author: Andy {andy@atech-software.com}
 */


public abstract class BlueToothProtocol implements SerialPortEventListener //implements MeterInterface, SerialPortEventListener //, Runnable
{
    /**
     * How many ms do we pause after each character is sent
     */
    protected int character_pause = 1;
    
    /**
     * How many ms do we pause after each command is sent
     */
    protected int command_pause = 1;
    
    
    
    /**
     * Ascii Code: Enquiry (0x05)
     */
    public static final byte ASCII_ENQ = 0x05;
    
    /**
     * Ascii Code: Acknowledge (0x06)
     */
    public static final byte ASCII_ACK = 0x06;
    
    
    /**
     * Ascii Code: Bot Acknowledged (0x15)
     */
    public static final byte ASCII_NAK = 0x15;
    
    /**
     * Ascii Code: End of Text (0x04)
     */
    public static final byte ASCII_EOT = 0x04;
    
    /**
     * Ascii Code: Start of Text (0x02)
     */
    public static final byte ASCII_STX = 0x02;
    
    
    private static Log log = LogFactory.getLog("ProtocolLog");
    
    protected I18nControlAbstract m_ic = null; //I18nControl.getInstance();
    protected DataAccessPlugInBase m_da = null; //DataAccessMeter.getInstance();


    protected boolean isPortOpen = false;
    protected SerialPort serialPort = null;
    protected CommPortIdentifier portIdentifier = null;
    protected OutputStream portOutputStream = null;
    protected InputStream portInputStream = null;
    protected String port_name = null;

    protected long startTime = System.currentTimeMillis();
    protected long timeOut = 500000000;

    int baudrate;
    int databits;
    int stopbits;
    int parity;
    int flow_control;
    protected int event_type;
    
    
    /**
     * Serial Event: None 
     */
    public static final int SERIAL_EVENT_NONE = 0;
    
    /**
     * Serial Event: Data Available 
     */
    public static final int SERIAL_EVENT_DATA_AVAILABLE = 1;
    
    /**
     * Serial Event: Break Interrupt 
     */
    public static final int SERIAL_EVENT_BREAK_INTERRUPT = 2;
    
    /**
     * Serial Event: Output Empty 
     */
    public static final int SERIAL_EVENT_OUTPUT_EMPTY = 4;
    
    /**
     * Serial Event: All 
     */
    public static final int SERIAL_EVENT_ALL = 7;
    
    

    /**
     * Constructor 
     */
    public BlueToothProtocol()
    {
    	//super();
    }

    
    
    
    /**
     * Constructor
     * 
     * @param da 
     */
    public BlueToothProtocol(DataAccessPlugInBase da)
    {
        this.m_da = da;
        this.m_ic = da.getI18nControlInstance();
    }

    
    /**
     * Set Communication Settings
     * 
     * @param baudrate
     * @param databits
     * @param stopbits
     * @param parity
     * @param flow_control
     * @param event_type
     */
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
     * Set the COM-Port from which will be read.
     * 
     * @param port 
     * @throws Exception 
     */
    public void setPort(String port) throws Exception
    {
     //   try
        {
            //System.out.println("port: " + port);
            portIdentifier = CommPortIdentifier.getPortIdentifier(port);
            port_name = port;
        }
       /* catch (NoSuchPortException ex)
        {
            //System.out.println("SerialProtocol::setPort:: No such port: " + ex);
            //log.error("No such port exception: " + ex.getMessage(), ex);
            throw new PlugInBaseException(ex);
        }*/
    }

    /*
    public String getPort()
    {
        return port_name;
    }*/
    
    
    
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
    
    
    /**
     * Open Serial Port
     * 
     * @throws PlugInBaseException 
     * @return true if port is opened 
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
            //System.out.println("SerialProtocol: open() - open");
            serialPort = (SerialPort)portIdentifier.open("ggc", (int)timeOut);
        
            
            //this.output_writer.writeLog(LogEntryType.INFO, "AbstractSerialMeter::open() - setting parameters");
            
            log.debug("SerialProtocol:open()");
            //System.out.println("SerialProtocol: open() - parameters");
            setConnectionParameters();
        
            portOutputStream = serialPort.getOutputStream();
            portInputStream = serialPort.getInputStream();
            
            // break interrupt event
            if ((this.event_type==BlueToothProtocol.SERIAL_EVENT_ALL) || 
                (this.event_type==BlueToothProtocol.SERIAL_EVENT_BREAK_INTERRUPT))
            {
                serialPort.notifyOnBreakInterrupt(true);
            }
            else
                serialPort.notifyOnBreakInterrupt(false);
                
            // data available
            if ((this.event_type==BlueToothProtocol.SERIAL_EVENT_ALL) || 
                (this.event_type==BlueToothProtocol.SERIAL_EVENT_DATA_AVAILABLE))
            {
                serialPort.notifyOnDataAvailable(true);
            }
            else
                serialPort.notifyOnDataAvailable(false);

            
            if ((this.event_type==BlueToothProtocol.SERIAL_EVENT_ALL) || 
                (this.event_type==BlueToothProtocol.SERIAL_EVENT_OUTPUT_EMPTY))
            {
                serialPort.notifyOnOutputEmpty(true);
            }
            else
                serialPort.notifyOnOutputEmpty(false);
            
            
            
            if (this.event_type!=BlueToothProtocol.SERIAL_EVENT_NONE)
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
            //System.out.println("SerialProtocol::open(). Unsupported comm operation: " + ex);
            log.error("Unsupported comm operation: " + ex.getMessage()); //, ex);
            throw new PlugInBaseException(ex);
        }
        catch (PortInUseException ex) 
        {
            //System.out.println("SerialProtocol::open(). Port in use: " + ex);
            log.error("Port in use: " + ex.getMessage()); //, ex);
            throw new PlugInBaseException(ex);
        } 
        catch (TooManyListenersException ex) 
        {
            //System.out.println("SerialProtocol::open(). Too many listeners: " + ex);
            log.error("Too many listeners: " + ex.getMessage()); //, ex);
            throw new PlugInBaseException(ex);
        }  
        catch (IOException ex) 
        {
            //System.out.println("SerialProtocol::open(). IO exception: " + ex);
            log.error("IO Exception: " + ex.getMessage()); //, ex);
            throw new PlugInBaseException(ex);
        } 
        catch(Exception ex)
        {
            if (ex instanceof NoSuchPortException)
            {
                //System.out.println("SerialProtocol::open(). No such port: " + ex);
                log.error("No such port: " + ex.getMessage(), ex);

                printAllAvailableSerialPorts();

                throw new PlugInBaseException(ex);
            }
            else
            {
                //System.out.println("SerialProtocol::open(). Exception: " + ex);
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
     */
    public void close()
    {
        if (!isPortOpen)
            return;

        serialPort.removeEventListener();
        serialPort.close();
        isPortOpen = false;
        //dataFromMeter = false;
        System.out.println("close port : " + portIdentifier.getName());
//        fireImportChanged(new ImportEvent(this, ImportEvent.PORT_CLOSED, portIdentifier));
    }

    
    
    /**
     * Read
     * 
     * @throws IOException
     * @return
     */
    public int read() throws IOException
    {
    	return portInputStream.read();
    }
    
    /**
     * Read
     * 
     * @param b 
     * @throws IOException
     * @return
     */
    public int read(byte[] b) throws IOException
    {
    	return portInputStream.read(b);
    }
    
    
    /**
     * Read
     * 
     * @param b 
     * @param off 
     * @param len 
     * @throws IOException
     * @return
     */
    public int read(byte[] b, int off, int len) throws IOException
    {
    	return portInputStream.read(b, off, len);
    }
    
    
    /**
     * Read Line
     * 
     * @return
     * @throws IOException
     */
    public String readLine() throws IOException //, SerialIOHaltedException
    {
	    char c = '\uFFFF';
	    boolean flag = false;
	    StringBuffer stringbuffer = new StringBuffer("");

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
	    
	    return stringbuffer.toString();
    }
    

    
    /**
     * Read Line as array of bytes
     * @return
     * @throws IOException
     */
    public byte[] readLineBytes() throws IOException //, SerialIOHaltedException
    {
        char c = '\uFFFF';
        boolean flag = false;
        //StringBuffer stringbuffer = new StringBuffer("");

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
    
    
    
    
    /**
     * Write (byte[]) 
     * 
     * @param b
     * @throws IOException
     */
    public void write(byte[] b) throws IOException
    {
    	portOutputStream.write(b);
    }

    
    /**
     * Write (int)
     * @param i
     * @throws IOException
     */
    public void write(int i) throws IOException
    {
    	portOutputStream.write(i);
    }
    
    
    /**
     * Write (byte[],int,int)
     * 
     * @param b byte array
     * @param off offset 
     * @param len length
     * @throws IOException
     */
    public void write(byte[] b, int off, int len) throws IOException
    {
    	portOutputStream.write(b, off, len);
    }
    
    
    
    // FIXME
    
    
    protected void sendMessageToMeter(String msg) throws Exception
    {
        writeCommand(BlueToothProtocol.ASCII_STX); // 0x02
        writeCommand(msg);
        readByteTimed();
        writeCommand(BlueToothProtocol.ASCII_EOT); // 0x04
        readByteTimed();
        writeCommand(BlueToothProtocol.ASCII_ACK); // 0x06
    }
    
    
    
    protected String readMessageFromMeter() throws Exception
    {
        String bt_line = readLine();
        commandAfterRead();
        
        return bt_line;
    }

    
    protected int readByteTimed() throws Exception
    {
        for(int r = 0; r++ < 100;)
        {
            waitTime(1);

            int iBuf = read();
            
            if(iBuf == -1)
            {
                waitTime(1);
            } 
            else
            {
                //addDebug(iBuf, D_RD);
                return iBuf;
            }
        }
    
        //addDebug(0, D_TO);
        return -1;
    }
    
    
    
    /**
     * Wait for x ms
     * @param time
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
    
    
    
    protected void writeCommand(int c) throws Exception
    {
        write(c);
        waitTime(character_pause);
    }
    

    
    protected void writeCommand(String line) throws IOException, InterruptedException
    {
        for(int c = 0; c < line.length(); c++)
        {
            write(line.charAt(c));
            waitTime(character_pause);
        }
    
        waitTime(command_pause);
    }
    
/*    
    public static final byte ASCII_ENQ = 0x05;
    
    public static final byte ASCII_ACK = 0x06;
    
    
    public static final byte ASCII_NAK = 0x15;
    
    public static final byte ASCII_EOT = 0x04;
    
    public static final byte ASCII_STX = 0x02;
  */  
    protected void commandAfterRead() throws Exception
    {
        writeCommand(BlueToothProtocol.ASCII_ACK); // 0x06
        readByteTimed();
        writeCommand(BlueToothProtocol.ASCII_ENQ); // 0x05
        readByteTimed();
    }

    
    protected void commandAfterWrite() throws Exception
    {
        readByteTimed();
        writeCommand(BlueToothProtocol.ASCII_EOT); // 0x04
        readByteTimed();
        writeCommand(BlueToothProtocol.ASCII_ACK); // 0x06
    }
    
    
    
    
    /**
     * Test
     */
    public void test()
    {
    }
    
    
    
    
    
    /**
     * Serial Event
     * 
     * @see javax.comm.SerialPortEventListener#serialEvent(SerialPortEvent)
     */
    public abstract void serialEvent(SerialPortEvent event);


    /**
     * Get Timeout
     * 
     * @return
     */
    public long getTimeOut()
    {
        return timeOut;
    }

/*
    public void stopImport()
    {
        timeOut = 1;
    }
*/



    /**
     * Print All Available Serial Ports as vector of CommPortIdentifier
     */
    public void printAllAvailableSerialPorts()
    {
        Vector<CommPortIdentifier> lst = BlueToothProtocol.getAllAvailablePorts();
        
        System.out.println("Displaying all available ports");
        System.out.println("-------------------------------");
        for(int i=0;i<lst.size(); i++)
        {
            System.out.println(lst.get(i));
        }
        
    }





    /**
     * Get All Available Serial Ports as vector of CommPortIdentifier
     *  
     * @return
     */
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


    
    /**
     * Get All Available Ports as Vector of Strings
     * 
     * @return
     */
    public static Vector<String> getAllAvailablePortsString()
    {
        Vector<String> retVal = new Vector<String>();
//        Vector<CommPortIdentifier> retVal = new Vector<CommPortIdentifier>();

        try
        {
            //Vector retVal = new Vector();
//            int counter = 0;
            
            //CommPortIdentifier.
            
//            CommPortIdentifier.getPortIdentifier("xx");
            
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
            log.error("There was problem obtaining list of serial ports. Ex: " + ex, ex);
        } 
        return retVal;

    }

    
    /**
     * Get All Available Ports as String (Internal)
     * @return
     * @throws Exception
     */
    public Vector<String> getAllAvailablePortsStringInternal() throws Exception
    {
        Vector<String> retVal = new Vector<String>();
//        Vector<CommPortIdentifier> retVal = new Vector<CommPortIdentifier>();

  //      try
        {
            //Vector retVal = new Vector();
//            int counter = 0;
            
            //CommPortIdentifier.
            
//            CommPortIdentifier.getPortIdentifier("xx");
            
            try
            {
                this.setPort("COM1");
                //System.loadLibrary( "rxtxSerial" );            
            }
            catch(Exception ex)
            {
                System.out.println(ex);
                if (m_da.checkUnsatisfiedLink(ex))
                {
                    System.out.println("UNSATISFIED");
                }
                
                
            }
            
            
            Enumeration<?> enume = CommPortIdentifier.getPortIdentifiers();
            while (enume.hasMoreElements()) 
            {
                CommPortIdentifier portID = (CommPortIdentifier)enume.nextElement();
                //if (portID.getPortType() == CommPortIdentifier.PORT_SERIAL)
                    retVal.add(portID.getName());
            }
        }
    /*    catch(Exception ex)
        {
            log.error("There was problem obtaining list of serial ports. Ex: " + ex, ex);
            throw ex;
            //System.out.println("Exception: getAvailableSerialPorts: " + ex);

        } */
        return retVal;

    }
    
    
    
    /**
     * Get All Available Ports as vector of CommPortIdentifier
     *  
     * @return
     */
    @SuppressWarnings("unchecked")
    public static Vector<CommPortIdentifier> getAllAvailablePorts()
    {
        Vector<CommPortIdentifier> retVal = new Vector<CommPortIdentifier>();

        try
        {
            Enumeration enume = CommPortIdentifier.getPortIdentifiers();
            while (enume.hasMoreElements()) 
            {
                CommPortIdentifier portID = (CommPortIdentifier)enume.nextElement();
                retVal.add(portID);
            }
        }
        catch(Exception ex)
        {
            System.out.println("Exception: getAvailableSerialPorts: " + ex);
        }

        return retVal;

    }
    

    /**
     * Get Connection Protocol
     * 
     * @return id of connection protocol (see ConnectionProtocols)
     */
    public int getConnectionProtocol()
    {
        return ConnectionProtocols.PROTOCOL_SERIAL_USBBRIDGE;
    }
    


}
