/*
 * Created on 10.08.2002
 *
 * To change this generated comment edit the template variable "filecomment":
 * Window>Preferences>Java>Templates.
 */

package ggc.meter.protocol;


import ggc.data.imports.ImportException;
import ggc.meter.data.DailyValuesRow;
import ggc.meter.device.MeterException;
import ggc.meter.device.MeterInterface;
import ggc.meter.util.DataAccess;
import ggc.meter.util.I18nControl;
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
import java.util.TooManyListenersException;
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
public abstract class XmlProtocol implements MeterInterface //, SerialPortEventListener //, Runnable
{

    protected int m_device_index = 0;


    protected I18nControl m_ic = I18nControl.getInstance();
    protected DataAccess m_da = DataAccess.getInstance();



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
    private String useInfoMessage = null;
//x    private String name = null;

    int baudrate;
    int databits;
    int stopbits;
    int parity;
    int flow_control;
    //serialPort.getFlowControlMode();


    public XmlProtocol()
    {
    	super();
    }

    /**
     * Constructor for SerialMeterImport.
     */
    public XmlProtocol(int device_index, int baudrate, int databits, int stopbits, int parity )
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
     * @throws MeterException 
     * @see data.imports.DataImport#open()
     */
    public boolean open() throws MeterException
    {
        if (isPortOpen)
            return isPortOpen;

        try 
        {

            

        } 
        catch (Exception exc) 
        {
        	System.out.println("SerialProtocol: open():Exception - io");
            //throw new ImportException(exc);
        } 
        return isPortOpen;
    }


    
    
    
    
    
    
    /**
     * @see data.imports.DataImport#close()
     */
    public void close()
    {
        if (!isPortOpen)
            return;

    }

    /**
     * @see data.imports.DataImport#importData()
     */
    public void importData() throws ImportException
    {
        if (portIdentifier == null || serialPort == null || portOutputStream == null || portInputStream == null)
            throw new ImportException(m_ic.getMessage("COM_PORT_NOT_INIT_CORRECT"));

        startTime = System.currentTimeMillis();
        //Thread thread = new Thread(this);
        //thread.start();
    }

    
    
    




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




    public static String[] getAvailableMeters()
    {
        return DataAccess.getInstance().getMeterManager().getAvailableMeters();
    }


    public static String getMeterClassName(String meterName)
    {
        return DataAccess.getInstance().getMeterManager().getMeterClassName(meterName);
    }

    




    public void testReading(String filename)
    {
        
        
    }
    
    


}
