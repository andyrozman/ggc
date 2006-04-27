/*
 * Created on 10.08.2002
 *
 * To change this generated comment edit the template variable "filecomment":
 * Window>Preferences>Java>Templates.
 */

package ggc.data.imports;


import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Enumeration;
import java.util.StringTokenizer;
import java.util.TooManyListenersException;
import java.util.Vector;

import javax.comm.*;
import javax.swing.*;
import javax.swing.event.EventListenerList;

import ggc.data.DailyValuesRow;
import ggc.event.ImportEvent;
import ggc.event.ImportEventListener;
import ggc.util.DataAccess;
import ggc.util.GGCProperties;
import ggc.util.I18nControl;


/**
 * @author stephan
 *
 * To change this generated comment edit the template variable "typecomment":
 * Window>Preferences>Java>Templates.
 */
public abstract class SerialMeterImport implements DataImport, SerialPortEventListener, Runnable
{

    protected I18nControl m_ic = I18nControl.getInstance();
    protected DataAccess m_da = DataAccess.getInstance();

    private boolean isPortOpen = false;

    private boolean dataFromMeter = false;

    protected SerialPort serialPort = null;

    protected CommPortIdentifier portIdentifier = null;

    protected OutputStream portOutputStream = null;

    protected InputStream portInputStream = null;

    private long startTime = System.currentTimeMillis();

    protected long timeOut = 50000;

    private EventListenerList listenerList = new EventListenerList();

    private ImageIcon image = null;

    private String useInfoMessage = null;

    private String name = null;

    /**
     * Constructor for SerialMeterImport.
     */
    public SerialMeterImport()
    {
        super();
    }

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
     * @see data.imports.DataImport#open()
     */
    public boolean open() throws ImportException
    {
        if (isPortOpen)
            return isPortOpen;

        if (portIdentifier == null) 
            throw new ImportException(m_ic.getMessage("NO_COM_PORT_SPECIFIED"));

        try 
        {
            serialPort = (SerialPort)portIdentifier.open("ggc", (int)timeOut);

            setConnectionParameters();

            portOutputStream = serialPort.getOutputStream();
            portInputStream = serialPort.getInputStream();
            serialPort.notifyOnDataAvailable(true);
            serialPort.notifyOnBreakInterrupt(true);

            try 
            {
                Thread.sleep(10000);
            } 
            catch (Exception exc) 
            {
            }

            isPortOpen = true;
            System.out.println("open port : " + portIdentifier.getName());
            serialPort.addEventListener(this);

        } 
        catch (PortInUseException exc) 
        {
            throw new ImportException(exc);
        } 
        catch (IOException exc) 
        {
            throw new ImportException(exc);
        } 
        catch (TooManyListenersException exc) 
        {
            throw new ImportException(exc);
        }

        try 
        {
            serialPort.enableReceiveTimeout(30);
        } 
        catch (UnsupportedCommOperationException e) 
        {
        }

        if (isPortOpen)
            fireImportChanged(new ImportEvent(this, ImportEvent.PORT_OPENED, portIdentifier));

        return isPortOpen;
    }

    private void setConnectionParameters()
    {

        if (serialPort == null)
            return;

        // Save state of parameters before trying a set.
        int oldBaudRate = serialPort.getBaudRate();
        int oldDatabits = serialPort.getDataBits();
        int oldStopbits = serialPort.getStopBits();
        int oldParity = serialPort.getParity();
        int oldFlowControl = serialPort.getFlowControlMode();

        // Set connection parameters, if set fails return parameters object
        // to original state.
        try 
        {
            serialPort.setSerialPortParams(9600, // BuadRate
                                           SerialPort.DATABITS_8, SerialPort.STOPBITS_1, SerialPort.PARITY_NONE);
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
            serialPort.setFlowControlMode(SerialPort.FLOWCONTROL_NONE);
        } 
        catch (UnsupportedCommOperationException e) 
        {
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
        fireImportChanged(new ImportEvent(this, ImportEvent.PORT_CLOSED, portIdentifier));
    }

    /**
     * @see data.imports.DataImport#importData()
     */
    public void importData() throws ImportException
    {
        if (portIdentifier == null || serialPort == null || portOutputStream == null || portInputStream == null)
            throw new ImportException(m_ic.getMessage("COM_PORT_NOT_INIT_CORRECT"));

        startTime = System.currentTimeMillis();
        Thread thread = new Thread(this);
        thread.start();
    }

    /**
     * @see data.imports.DataImport#getImportedData()
     */
    public DailyValuesRow[] getImportedData()
    {
        return new DailyValuesRow[0];
    }


    /**
     * @see javax.comm.SerialPortEventListener#serialEvent(SerialPortEvent)
     */
    public void serialEvent(SerialPortEvent event)
    {

        //System.out.println();

        // Determine type of event.
        switch (event.getEventType()) 
        {
            case SerialPortEvent.DATA_AVAILABLE:
                dataFromMeter = true;
                timeOut += 5000;
                break;


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
    public void run()
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
    }


    /**
     * Gets the image
     * @return Returns a ImageIcon
     */
    public ImageIcon getImage()
    {
        return image;
    }


    /**
     * Sets the image
     * @param image The image to set
     */
    public void setImage(ImageIcon image)
    {
        this.image = image;
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

    /**
     * Gets the name
     * @return Returns a String
     */
    public String getName()
    {
        return name;
    }

    /**
     * Sets the name
     * @param name The name to set
     */
    public void setName(String name)
    {
        this.name = name;
    }


    public static String[] getAvailableMeters()
    {
        return DataAccess.getInstance().getMeterManager().getAvailableMeters();
    }


    public static String getMeterClassName(String meterName)
    {
        return DataAccess.getInstance().getMeterManager().getMeterClassName(meterName);
    }

    public static Vector getAvailableSerialPorts()
    {
        Vector retVal = new Vector();
        int counter = 0;

        Enumeration enume = CommPortIdentifier.getPortIdentifiers();
        while (enume.hasMoreElements()) 
        {
            CommPortIdentifier portID = (CommPortIdentifier)enume.nextElement();
            if (portID.getPortType() == CommPortIdentifier.PORT_SERIAL)
                retVal.add(portID.getName());
        }
        return retVal;
    }

}
