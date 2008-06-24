/*
 * Created on 10.08.2002
 *
 * To change this generated comment edit the template variable "filecomment":
 * Window>Preferences>Java>Templates.
 */

package ggc.pump.protocol;


import ggc.core.data.DailyValuesRow;
import ggc.pump.device.PumpException;
import ggc.pump.device.PumpInterface;
import ggc.pump.util.DataAccess;
import ggc.pump.util.I18nControl;
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

import javax.swing.ImageIcon;

//import minimed.ddms.deviceportreader.MedicalDevice;
//import minimed.ddms.deviceportreader.SerialIOHaltedException;
//import minimed.util.Contract;


/**
 * @author stephan
 *
 * To change this generated comment edit the template variable "typecomment":
 * Window>Preferences>Java>Templates.
 */
public abstract class SerialProtocol implements PumpInterface, SerialPortEventListener //, Runnable
{

    protected int m_device_index = 0;


    protected I18nControl m_ic = I18nControl.getInstance();
    protected DataAccess m_da = DataAccess.getInstance();



    private boolean isPortOpen = false;
    public boolean dataFromPump = false;
    public SerialPort serialPort = null;
    protected CommPortIdentifier portIdentifier = null;
    public OutputStream portOutputStream = null;
    public InputStream portInputStream = null;

    public long startTime = System.currentTimeMillis();
    protected long timeOut = 50000;

//x    private EventListenerList listenerList = new EventListenerList();
//x    private ImageIcon image = null;
    private String useInfoMessage = null;
//x    private String name = null;

    int baudrate;
    int databits;
    int stopbits;
    int parity;
    int flow_control;
    //serialPort.getFlowControlMode();


    public SerialProtocol()
    {
    	super();
    }

    /**
     * Constructor for SerialPumpImport.
     */
    public SerialProtocol(int device_index, int baudrate, int databits, int stopbits, int parity )
    {
        super();

	this.m_device_index = device_index;
	this.baudrate = baudrate;
	this.databits = databits;
	this.stopbits = stopbits;
	this.parity = parity;
	
	
//		open();
    }

    
    public void setCommunicationSettings(int baudrate, int databits,
    									 int stopbits, int parity,
    									 int flow_control)
    {
    	this.baudrate = baudrate;
    	this.databits = databits;
    	this.stopbits = stopbits;
    	this.parity = parity;
    	this.flow_control = flow_control;
    }

    
    
/*
    public void addImportEventListener(ImportEventListener listener)
    {
        listenerList.add(ImportEventListener.class, listener);
    }

    public void removeImportEventListener(ImportEventListener listener)
    {
        listenerList.remove(ImportEventListener.class, listener);
    }

    protected void fireImportChanged(ImportEvent event)
    {
        // Guaranteed to return a non-null array
        Object[] listeners = listenerList.getListenerList();

        // Process the listeners last to first, notifying
        // those that are interested in this event
        for (int i = listeners.length - 2; i >= 0; i -= 2) 
        {
            if (listeners[i] instanceof ImportEventListener) 
            {
                // Lazily create the event:
                if (event == null)
                    event = new ImportEvent(this);
                ((ImportEventListener)listeners[i + 1]).importChanged(event);
            }
        }
    }


    protected void fireImportFinished()
    {
        fireImportChanged(new ImportEvent(this, ImportEvent.IMPORT_FINISHED));
    }


    protected void fireImportTimeout()
    {
        fireImportChanged(new ImportEvent(this, ImportEvent.TIMEOUT));
    }
*/

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
     * @throws PumpException 
     * @see data.imports.DataImport#open()
     */
    public boolean open() throws PumpException
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
            //serialPort.notifyOnDataAvailable(true);
            serialPort.notifyOnBreakInterrupt(true);

            try 
            {
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
        dataFromPump = false;
        System.out.println("close port : " + portIdentifier.getName());
//        fireImportChanged(new ImportEvent(this, ImportEvent.PORT_CLOSED, portIdentifier));
    }

    /**
     * @see data.imports.DataImport#importData()
     */
 /*   public void importData() throws ImportException
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
    public DailyValuesRow[] getImportedData()
    {
        return new DailyValuesRow[0];
    }


    /**
     * @see gnu.io.SerialPortEventListener#serialEvent(SerialPortEvent)
     */
    public abstract void serialEvent(SerialPortEvent event);

    /*
    {


        //System.out.println();

        // Determine type of event.
        switch (event.getEventType()) 
        {
	    case SerialPortEvent.DATA_AVAILABLE:
		{
		    int newData = 0;

		    try
		    {
			while ((newData=portInputStream.read())!=-1)
			{
			    switch (newData)
			    {
				case 6:
				    System.out.println("<ACK>");
				    break;

				case 13:
				    System.out.println("<CR>");
				    break;

				case 5:
				    System.out.println("<ENQ>");
				    break;
				case 4:
				    System.out.println("<EOT>");
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
				    break;

				case 2:
				    System.out.println("<STX>");
				    break;

				default:
				    System.out.print((char)newData);
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
		    dataFromPump = true;

		    System.out.println("Data");

		    timeOut += 5000;
*/
/*                } break;


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
    }
*/

    public long getTimeOut()
    {
        return timeOut;
    }


    public void stopImport()
    {
        timeOut = 1;
    }


    /**
     * @see java.lang.Runnable#run()
     */
/*    public void run()
    {
        while (System.currentTimeMillis() - startTime < getTimeOut()) 
        {
            try 
            {
                Thread.sleep(1000);
            } 
            catch (InterruptedException exc) 
            {
            }
        }

        if (getImportedData().length > 0)
            fireImportChanged(new ImportEvent(this, ImportEvent.IMPORT_FINISHED));
        else
            fireImportChanged(new ImportEvent(this, ImportEvent.TIMEOUT));
    }*/








    /**
     * Gets the useInfoMessage
     * @return Returns a String
     */
    public String getUseInfoMessage()
    {
        return useInfoMessage;
    }

    /**
     * Sets the useInfoMessage
     * @param useInfoMessage The useInfoMessage to set
     */
    public void setUseInfoMessage(String useInfoMessage)
    {
        this.useInfoMessage = useInfoMessage;
    }




    public static String[] getAvailablePumps()
    {
        return DataAccess.getInstance().getPumpManager().getAvailablePumps();
    }


    public static String getPumpClassName(String meterName)
    {
        //return DataAccess.getInstance().getPumpManager().getPumpClassName(meterName);
        return null;
    }

    
    @SuppressWarnings("unchecked")
    public static Vector<String> getAvailableSerialPorts()
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



    /**
     * getName - Get Name of meter
     */
    public String getName()
    {
	return m_da.getPumpManager().meter_names[this.m_device_index];
    }


    /**
     * getIcon - Get Icon of meter
     */
    public ImageIcon getIcon()
    {
        //return m_da.getPumpManager().meter_pictures[this.m_device_index];
        return null;
    }


    /**
     * getPumpIndex - Get Index of Pump 
     */
    public int getPumpIndex()
    {
	return this.m_device_index;
    }




}
