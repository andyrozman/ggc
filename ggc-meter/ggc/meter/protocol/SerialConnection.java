/**
 * SerialConnection.java 
 *
 * Copyright (c) 2003, Raben Systems, Inc.
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without 
 * modification, are permitted provided that the following conditions are met:
 *
 *    Redistributions of source code must retain the above copyright notice, 
 *    this list of conditions and the following disclaimer. 
 *    
 *    Redistributions in binary form must reproduce the above copyright notice, 
 *    this list of conditions and the following disclaimer in the documentation 
 *    and/or other materials provided with the distribution.
 * 
 *    Neither the name of Raben Systems, Inc. nor the names of its contributors 
 *    may be used to endorse or promote products derived from this software 
 *    without specific prior written permission.
 * 
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" 
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE 
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE 
 * ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE 
 * LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR 
 * CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF 
 * SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS 
 * INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN 
 * CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) 
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE 
 * POSSIBILITY OF SUCH DAMAGE.
 */
package ggc.meter.protocol;

import gnu.io.*;



import java.io.OutputStream;
import java.io.InputStream;
import java.io.IOException;
import java.util.TooManyListenersException;
import java.util.LinkedList;
import java.util.ArrayList;
import java.util.Enumeration;

/**
 * A class to handle communications with a serial connection
 * @author Vern Raben
 * @version $Revision: 1.1.1.1 $ $Date: 2003/06/13 04:51:09 $
*/
public class SerialConnection implements SerialPortEventListener, 
CommPortOwnershipListener 
{
    /** Output stream for communications */
    private OutputStream os;

    /** Input stream for communications */
    private InputStream is;

    /** Communications port id */
    private CommPortIdentifier portId;

    /** Serial port */
    private SerialPort serialPort;

    /** flag to indicate whehter comm port is open */
    private boolean open = false;

    /** Name of the communications port */
    private String portName = "COM1";

    /** Baud rate of connection */
    private int baudRate = 9600;

    /** Number of data bits */
    private int databits = SerialPort.DATABITS_8;

    /** Number of stop bits */
    private int stopbits = SerialPort.STOPBITS_1;

    /** Parity */
    private int parity = SerialPort.PARITY_NONE; 

    /** Flow control */
    private int flowControl = SerialPort.FLOWCONTROL_NONE 
                              | SerialPort.FLOWCONTROL_NONE;

    /** Queue for receiving characters from comm port */
    private MessageQueue messageQueue = new MessageQueue();

    /** Buffer for receiving chars from comm port */
    private StringBuffer inputBuffer = new StringBuffer();

    /** Debug flag */
    private boolean debug = false;

    /**
     * Create a SerialConnection using default settings of
     * 8 bits, 9600 baud, no parity, no flow control, and 1 stop bit on
     * port named COM1 .
     */
    public SerialConnection()
    {
    }


    /**
     * Open a serial connection and IO streams using the current parameters
     * @exception IOException may occur
     */
    public void openConnection() throws IOException {

        // Obtain a port identifier
        try
        {
            portId = CommPortIdentifier.getPortIdentifier(getPortName());
        }
        catch (NoSuchPortException e)
        {
            throw new IOException(e.getMessage());
        }

        // Open the port
        try
        {
            serialPort = (SerialPort) portId.open("GGC", 
                                                  30000);
        }
        catch (PortInUseException e)
        {
            throw new IOException(e.getMessage());
        }

        // Set serial port connection parameters
        try
        {
            setSerialPortConnectionParameters();
            os = serialPort.getOutputStream();
            is = serialPort.getInputStream(); 
            os.flush();
        }
        catch (IOException e)
        {
            serialPort.close();
            throw e;
        }

        // Add this object as an event listener
        try
        {
            serialPort.addEventListener(this);
        }
        catch (TooManyListenersException e)
        {
            serialPort.close();
            throw new IOException("Too many listeners:" + e.getMessage());
        }

        // Notify when data is available
        serialPort.notifyOnDataAvailable(true);

        // Enable receive timeout 
        try
        {
            serialPort.enableReceiveTimeout(30);
        }
        catch (UnsupportedCommOperationException e)
        {
            // Ignore
        }
        // Add ownership listener to close connection (to prevent lockout 
        // if program hangs)
        portId.addPortOwnershipListener(this);
        open = true;

    }

    /**
     * Set the serial port connection parameters
     * @throws IOException may occur
     */
    private void setSerialPortConnectionParameters() throws IOException {

        // Set serial connection parameters
        try
        {
            serialPort.setSerialPortParams(getBaudRate(), getDatabits(),
                                           getStopbits(), getParity());
        }
        catch (UnsupportedCommOperationException e)
        {
            throw new IOException("Unsupported serial operation:"
                                  + e.getMessage());
        }

        // Set flow control.
        try
        {
            serialPort.setFlowControlMode(getFlowControl());
        }
        catch (UnsupportedCommOperationException e)
        {
            throw new IOException("Unsupported flow control mode:" 
                                  + e.getMessage());
        }
    }

    /**
    Close the port and clean up associated elements.
    */
    public void closeConnection()
    {

        // If port is already closed just return.
        if (!open)
        {
            return;
        }


        // Close streams if serialPort not null
        if (serialPort != null)
        {
            try
            {
                os.close();
                is.close();
            }
            catch (IOException e)
            {
                System.err.println(e);
            }

            // Close the port.
            serialPort.close();

            // Remove the ownership listener.
            portId.removePortOwnershipListener(this);
        }

        open = false;
    }


    /**
     * Check if the serial port is open
     * @return true if port is open, false if port is closed.
     */
    public boolean isOpen()
    {
        return open;
    }

    /**
     * Handle Serial Port Events.
     * @param evt SerialPortEvent 
     */
    public void serialEvent(SerialPortEvent evt)
    {
        int newData = 0;

        // Determine type of event.
        switch (evt.getEventType())
        {

            // Read data until -1 is returned
            case SerialPortEvent.DATA_AVAILABLE:
                while (newData != -1)
                {
                    try
                    {
                        newData = is.read();
                        if (newData == -1)
                        {
                            break;
                        }

                        inputBuffer.append((char) newData);

                        System.out.print((char) newData);
                        /*
                        if (newData == '#')
                        {
                            char[] chars = new char[inputBuffer.length()];
                            inputBuffer.getChars(0, inputBuffer.length(), 
                                                 chars, 0);
                            messageQueue.addChars(chars);
                            inputBuffer.delete(0, inputBuffer.length());
                        }
                        */


                    }
                    catch (IOException e)
                    {
                        System.err.println(e);
                        return;
                    }
                }

                break;
        }

    }   

    /**
     * Handle ownership change event.
     * When PORT_OWNERSHIP_REQUESTED event is
     * received, close the connection.
     * @param type Type of owernership event
     */
    public void ownershipChange(int type)
    {
        if (type == CommPortOwnershipListener.PORT_OWNERSHIP_REQUESTED)
        {
            closeConnection();
        }
    }


    /**
     * Send an array of chars to the serial connection
     * @param chars Array of chars
     * @exception IOException may occur
     */
    public void sendChars(char[] chars) throws IOException {

        if ((chars != null) && (isOpen()))
        {
            byte[] bytes = new byte[chars.length];

            for (int i = 0; i < chars.length; i++)
            {
                bytes[i] = (byte) chars[i];
            }

            if (isDebug())
            {
                System.out.println("SerialConnection.sendChars:" 
                                   + displayCharArrayAsHexString(chars));
            }

            os.write(bytes);
            os.flush();
        }
    }

    /**
     * Send string to the serial connection
     * @param str The string to send
     * @throws IOException may occur
     */
    public void sendString(String str) throws IOException 
    {
        if ((str != null) && (str.length() > 0) && (isOpen()))
        {
            char[] chars = new char[80];
            str.getChars(0, str.length(), chars, 0);

            if (isDebug())
            {
                System.out.println("SerialConnection.sendString=" + str);
            }

            os.write(str.getBytes());
            os.flush();

        }
    }

    /**
     * Receive String from serial connection
     * @return String
     */
    public String receiveString()
    {

        if (isOpen())
        {
            int count = 0;

            while ((count < 10) && (messageQueue.isEmpty()))
            {
                if (count > 0)
                {
                    System.out.println(getPortName() + ": waiting:" + count);
                }

                count++;
                try
                {
                    Thread.sleep(1000L);
                }
                catch (InterruptedException e)
                {
                    // Ignore 
                }
            }
        }

        if (isDebug())
        {
            String response = messageQueue.getMessage();
            System.out.println("SerialConnection.receiveString=" + response);
            return response;
        }

        return messageQueue.getMessage();
    }

    /**
     * Receive character array from serial connection
     * @return char[]
     */
    public char[] receiveChars()
    {

        if (isOpen())
        {
            int count = 0;

            while ((count < 10) && (messageQueue.isEmpty()))
            {
                if (count > 0)
                {
                    System.out.println(getPortName() + ": waiting:" + count);
                }

                count++;
                try
                {
                    Thread.sleep(1000L);
                }
                catch (InterruptedException e)
                {
                    // Ignore

                }
            }
        }

        if (isDebug())
        {
            char[] response = messageQueue.getChars();
            System.out.println("SerialConnection.receiveChars:"
                               + displayCharArrayAsHexString(response));
            return response;
        }

        return messageQueue.getChars();
    }

    /**
     * Set name of port to open
     * @param portName (example for MS Windows COM1 or COM2)
     */
    public void setPortName(String portName)
    {
        this.portName = portName;
    }

    /**
     * Get the port name being used by this connection
     * @return Current name of the port
     */
    public String getPortName()
    {
        return this.portName;
    }


    /**
     * Set stop bits.
     * @param stopbits New stop bits setting.
     */
    public void setStopbits(int stopbits)
    {
        this.stopbits = stopbits;
    }

    /**
     * Get stop bits
     * @return Current stop bits.
     */
    public int getStopbits()
    {
        return this.stopbits;
    }

    /**     
     * Set parity
     * @param parity New parity setting.
     */
    public void setParity(int parity)
    {
        this.parity = parity;
    }

    /**
     * Get current parity setting
     * @return parity settting
     */
    public int getParity()
    {
        return this.parity;
    }

    /**
     * Get baud rate
     * @return Current baud rate.
     */
    public int getBaudRate()
    {
        return this.baudRate;
    }

    /** 
     * Set baud rate.
     * @param baudRate New baud rate (9600 is default)
     */
    public void setBaudRate(int baudRate)
    {
        this.baudRate = baudRate;
    }

    /**
     * Get data bits
     * @return Current data bits setting.
     */
    public int getDatabits()
    {
        return this.databits;
    }

    /**
     * Set new data bits.
     * @param databits New data bits setting.
     */
    public void setDatabits(int databits)
    {
        this.databits = databits;
    }

    /**
     * Set flow control 

     * Formed by "or-ing" both "in" and "out" flow control settings.
     * The efault is SerialPort.FLOWCONTROL_NONE | SerialPort.FLOWCONTROL_NONE.
     * @see javax.comm.SerialPort for flow control definitions
     * @param flowControl Flow control settings setting 
     */
    public void setFlowControl(int flowControl)
    {
        this.flowControl = flowControl;
    }

    /**
     * Get flow control
     * @return Current flow control setting.
     */ 
    public int getFlowControl()
    {
        return this.flowControl;
    }

    /**
     * Convert array of bytes to space separated hex string for debug display
     * @param chars Array of chars
     * @return String for display
     */
    public static String displayCharArrayAsHexString(char[] chars)
    {
        StringBuffer buf = new StringBuffer();

        if (chars != null)
        {
            for (int i = 0; i < chars.length; i++)
            {

                if (i != 0)
                {
                    buf.append(" ");
                }

                buf.append("char[");
                buf.append(i);
                buf.append("]=0x");
                buf.append(Integer.toString(chars[i], 16));
            }
        }
        else
        {
            buf.append("chars is null");
        }

        return buf.toString();
    }

    /**
     * Set debug flag
     * @param debug Set to true to list cmds sent, responses received 
     */
    public void setDebug(boolean debug)
    {
        this.debug = debug;
    } 

    /**
     * Check debug flag
     * return true if set
     * @return boolean
     */
    public boolean isDebug()
    {
        return this.debug;
    }

    /**
     * Get serial ports 
     * @return String array of ports on workstation (may return null)
     */
    public static String[] getAvailablePorts()
    {
        Enumeration en = CommPortIdentifier.getPortIdentifiers();
        String[] retStr = null;        
        ArrayList list = new ArrayList();

        while (en.hasMoreElements())
        {
            CommPortIdentifier comId = (CommPortIdentifier) en.nextElement();

            if (comId.getPortType() == CommPortIdentifier.PORT_SERIAL)
            {
                list.add(comId.getName());
            }
        }


        Object[] ports = list.toArray();

        if (ports != null)
        {
            retStr = new String[ports.length];

            for (int i = 0; i < ports.length; i++)
            {
                retStr[i] = (String) ports[i];
            }
        }

        return retStr;
    }    

    /**
     * Class to synchronize values received from serial connection
     */
    class MessageQueue
    {
        /** Queue containing responses */
        private LinkedList queue = new LinkedList();

        /**
         * Add message to queue
         * @param txt The message to add
         */
        public void addMessage(String txt)
        {

            synchronized (queue)
            {
                queue.addLast(txt);
            }

        }

        /**
         * Add characters to queue
         * @param chars Array of char to add
         */
        public void addChars(char[] chars)
        {
            synchronized (queue)
            {
                queue.addLast(chars);
            }
        }

        /**
         * Get message from queue as a char array
         * @return char[]
         */
        public char[] getChars()
        {
            boolean empty = true;
            char[] chars = null;

            synchronized (queue)
            {
                empty = queue.isEmpty();
            }

            if (!empty)
            {

                synchronized (queue)
                {
                    chars = (char[]) queue.removeFirst();
                }
            }

            return chars;
        }

        /**
         * Size of the queue
         * @return Number of messages in queue
         */
        public int size()
        {
            synchronized (queue)
            {
                return queue.size();
            }
        }

        /**
         * Check if queue is empty
         * @return True if empty, false otherwise
         */
        public boolean isEmpty()
        {
            synchronized (queue)
            {
                return queue.isEmpty();
            }
        }

        /**
         * Get String message from queue
         * @return The message as a string
         */
        public String getMessage()
        {
            boolean empty = true;
            String msg = "";


            synchronized (queue)
            {
                empty = queue.isEmpty();
            }

            if (!empty)
            {
                synchronized (queue)
                {
                    char[] chars = (char[]) queue.removeFirst();
                    if (chars != null)
                    {
                        msg = new String(chars);
                    }
                }
            }


            return msg;
        }

    }


}
