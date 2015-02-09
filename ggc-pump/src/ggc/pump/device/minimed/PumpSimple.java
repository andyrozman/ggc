package ggc.pump.device.minimed;

import ggc.plugin.device.PlugInBaseException;
import ggc.plugin.protocol.SerialProtocol;
import gnu.io.CommPortIdentifier;
import gnu.io.NoSuchPortException;
import gnu.io.PortInUseException;
import gnu.io.SerialPort;
import gnu.io.UnsupportedCommOperationException;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Vector;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.atech.utils.data.CRCUtils;
import com.atech.utils.data.HexUtils;

public class PumpSimple
{

    String port_name = "COM1";
    String serial_no = "";
    SerialPort m_serialPort = null;
    long device_timeout = 500;
    // protected MinimedDeviceUtil util = null;
    // //MinimedDeviceUtil.getInstance();
    CRCUtils hex_utils = new CRCUtils();

    private static Log log = LogFactory.getLog(PumpSimple.class);

    public PumpSimple(String port, String serial_no)
    {
        this.serial_no = serial_no;
        this.port_name = port;

        // this.util = MinimedDeviceUtil.createInstance(da);

        try
        {

            initSerialCommunication();

            // this should really be a class, having 4 different protocols:
            // - CareLink USB (not supported for now)
            // - Paradigm Link (not supported ever)
            // - ComStation (not supported ever)
            // - ComLink (THIS IS OURS)
            // initCommunicationAdapter();

            initializeCommunicationInterface();

            //
            initPumpForCommunication();

            closeSerialCommunication();

        }
        catch (Exception ex)
        {
            log.error("Exception on execute: Ex.: " + ex, ex);
        }

        //
        // closePumpCommunication();

        // closeCommunicationAdapter();

    }

    public void closeSerialCommunication() throws Exception
    {

        this.serialPort.close();

    }

    protected boolean isPortOpen = false;
    protected SerialPort serialPort = null;
    protected CommPortIdentifier portIdentifier = null;
    protected OutputStream portOutputStream = null;
    protected InputStream portInputStream = null;
    protected long timeOut = 50000;

    public void initSerialCommunication() throws Exception
    {
        log.debug("Init Serial Communication - Start");

        try
        {
            System.out.println("port: " + this.port_name);
            portIdentifier = CommPortIdentifier.getPortIdentifier(this.port_name);
            // port_name = port;
        }
        catch (NoSuchPortException ex)
        {
            // System.out.println("SerialProtocol::setPort:: No such port: " +
            // ex);
            log.error("No such port exception: " + ex.getMessage(), ex);
            throw new PlugInBaseException(ex);
        }

        try
        {
            // this.outputWriter.writeLog(LogEntryType.INFO,
            // "AbstractSerialMeter::open()");
            // System.out.println("SerialProtocol: open() - open");
            serialPort = (SerialPort)portIdentifier.open("ggc", (int) timeOut);

            // this.outputWriter.writeLog(LogEntryType.INFO,
            // "AbstractSerialMeter::open() - setting parameters");

            log.debug("SerialProtocol:open()");
            // System.out.println("SerialProtocol: open() - parameters");

            try
            {
                serialPort.setSerialPortParams(57600, SerialPort.DATABITS_8, SerialPort.STOPBITS_1,
                    SerialPort.PARITY_NONE);

                serialPort.setFlowControlMode(SerialPort.FLOWCONTROL_NONE);
            }
            catch (UnsupportedCommOperationException ex)
            {
                log.error("Unuprted Comm Operation: " + ex, ex);
            }

            portOutputStream = serialPort.getOutputStream();
            portInputStream = serialPort.getInputStream();
            /*
             * // break interrupt event
             * if ((this.event_type==SerialProtocol.SERIAL_EVENT_ALL) ||
             * (this.event_type==SerialProtocol.SERIAL_EVENT_BREAK_INTERRUPT))
             * {
             * serialPort.notifyOnBreakInterrupt(true);
             * }
             * else
             * serialPort.notifyOnBreakInterrupt(false);
             * // data available
             * if ((this.event_type==SerialProtocol.SERIAL_EVENT_ALL) ||
             * (this.event_type==SerialProtocol.SERIAL_EVENT_DATA_AVAILABLE))
             * {
             * serialPort.notifyOnDataAvailable(true);
             * }
             * else
             * serialPort.notifyOnDataAvailable(false);
             * if ((this.event_type==SerialProtocol.SERIAL_EVENT_ALL) ||
             * (this.event_type==SerialProtocol.SERIAL_EVENT_OUTPUT_EMPTY))
             * {
             * serialPort.notifyOnOutputEmpty(true);
             * }
             * else
             * serialPort.notifyOnOutputEmpty(false);
             * if (this.event_type!=SerialProtocol.SERIAL_EVENT_NONE)
             * serialPort.addEventListener(this);
             */

            isPortOpen = true;
            // System.out.println("open port : " + portIdentifier.getName());
            // serialPort.addEventListener(this);

            serialPort.enableReceiveTimeout(20000); // .setTimeoutRx(250);
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
        /*
         * catch (TooManyListenersException ex)
         * {
         * System.out.println("SerialProtocol::open(). Too many listeners: " +
         * ex);
         * log.error("Too many listeners: " + ex.getMessage(), ex);
         * throw new PlugInBaseException(ex);
         * }
         */
        catch (IOException ex)
        {
            System.out.println("SerialProtocol::open(). IO exception: " + ex);
            log.error("IO Exception: " + ex.getMessage(), ex);
            throw new PlugInBaseException(ex);
        }
        catch (Exception ex)
        {
            if (ex instanceof NoSuchPortException)
            {
                // System.out.println("SerialProtocol::open(). No such port: " +
                // ex);
                log.error("No such port: " + ex.getMessage(), ex);

                printAllAvailableSerialPorts();

                throw new PlugInBaseException(ex);
            }
            else
            {
                // System.out.println("SerialProtocol::open(). Exception: " +
                // ex);
                log.error("Exception: " + ex.getMessage(), ex);
                throw new PlugInBaseException(ex);
            }
        }

        log.debug("Init Serial Communication - End");
        // log.debug("N/A");

    }

    public void initCommunicationAdapter() throws Exception
    {
        log.debug("Init Communication Adapter - Start");

        // readUntilEmpty();
        boolean done = this.sendCommandCheckReply((byte) 6, (byte) 51);

        if (done)
        {
            log.error("Communication Adapter READY.");
        }
        else
        {
            log.error("Communication Adapter failed.");
        }

        log.debug("Init Communication Adapter - End");

        /*
         * this.portOutputStream.write(6);
         * sleepMs(device_timeout);
         * log.trace("Read: " + this.portInputStream.read());
         */
        // log.debug("N/A");

    }

    private int readUntilEmpty()
    {
        // this.portInputStream.

        int i = 0;
        // Contract.pre(m_serialPort != null, "m_serialPortLocal is null.");
        int j;
        try
        {
            while ((j = portInputStream.available() /*
                                                     * m_serialPortLocal.
                                                     * rxReadyCount()
                                                     */) > 0)
            {
                // System.out.println("")
                byte abyte0[] = new byte[j];
                // util.sleepIO();
                i += portInputStream.read(abyte0) /*
                                                   * m_serialPortLocal.getData(
                                                   * abyte0)
                                                   */;
            }
        }
        catch (IOException ioexception)
        {}
        if (i > 0)
        {
            log.info("readUntilEmpty: dumped " + i + (i <= 1 ? " byte." : " bytes."));
        }
        return i;
    }

    private boolean sendCommandCheckReply(byte cmd_byte, byte expected_return) throws IOException, PlugInBaseException
    {
        // Contract.pre(getRS232Port() != null, "serial port is null.");
        // Contract.pre(getRS232Port().isOpen(), "serial port is not open.");
        int i = 0;
        // boolean flag = false;
        for (int j = 0; j <= 1 && i != expected_return; j++)
        {
            i = sendCommandGetReply(cmd_byte);
            // flag = i == byte1;
        }

        if (i != expected_return)
        {

            // dumpInterfaceStatus();
            // throw new IOException("SendCommand: command " +
            // hex_utils.getHex(cmd_byte) + " reply " + hex_utils.getHex(i) +
            // " does not match expected command " +
            // hex_utils.getHex(expected_return));
            log.debug("SendCommand: command " + hex_utils.getHex(cmd_byte) + " reply " + hex_utils.getHex(i)
                    + " does not match expected command " + hex_utils.getHex(expected_return));
            return false;
        }
        else
            return true;
    }

    public void initPumpForCommunication()
    {
        log.debug("Init Pump For Communication");

        log.debug("N/A");

    }

    private int sendCommandGetReply(byte cmd_byte) throws IOException
    {
        this.portOutputStream.write(cmd_byte);

        sleepMs(this.device_timeout);

        // this.write("\r".getBytes());

        // this.w
        // this.write((int)cmd_byte);

        // this.util.sleepIO();
        return this.portInputStream.read();
    }

    public static void main(String[] args)
    {

        // DataAccess da = DataAccess.createInstance(new JFrame());

        /*
         * GGCDb db = new GGCDb(da);
         * db.initDb();
         * da.setDb(db);
         */

        /*
         * DataAccessPump dap = DataAccessPump.getInstance();
         * //dap.setHelpContext(da.getHelpContext());
         * //dap.setPlugInServerInstance(this);
         * dap.createDb(da.getHibernateDb());
         * dap.initAllObjects();
         * dap.loadSpecialParameters();
         * //this.backup_restore_enabled = true;
         * da.loadSpecialParameters();
         * //System.out.println("PumpServer: " +
         * dataAccess.getSpecialParameters().get("BG"));
         * dap.setBGMeasurmentType(da.getIntValueFromString(da.getSpecialParameters
         * ().get("BG")));
         * MinimedSPMPump msp = new MinimedSPMPump("Nemec_B_001_20090425.mmp",
         * DataAccessPump.getInstance());
         * msp.readData();
         */

        HexUtils hex = new HexUtils();

        // 9E 8F 00
        // int j = MedicalDevice.Util.makeUnsignedShort(ai[2], ai[3])

        int i = hex.makeInt(Integer.valueOf("01", 16).intValue(), Integer.valueOf("01", 16).intValue()); // ,
                                                                                                         // Integer.valueOf("00",
                                                                                                         // 16).intValue());
        System.out.println("Value:" + i);
        // System.out.println(toBasalInsulin(i));

        // new PumpSimple("/dev/ttyUSB1", "904717");
    }

    public static double toBasalInsulin(int i)
    {
        return (double) i / (double) 40;
    }

    public void sleepMs(long ms)
    {
        try
        {
            Thread.sleep(ms);
        }
        catch (InterruptedException e)
        {
            // TODO Auto-generated catch block
            // e.printStackTrace();
        }
    }

    /**
     * Print All Available Serial Ports as vector of CommPortIdentifier
     */
    public static void printAllAvailableSerialPorts()
    {
        Vector<CommPortIdentifier> lst = SerialProtocol.getAllAvailablePorts();

        System.out.println("Displaying all available ports");
        System.out.println("-------------------------------");
        for (int i = 0; i < lst.size(); i++)
        {
            System.out.println(lst.get(i));
        }

    }

    public int initializeCommunicationInterface() throws PlugInBaseException
    {
        log.debug("Here");

        // setState(2);
        boolean flag = false;
        for (int i = 0; i <= 4 && !flag;)
        {
            try
            {
                initCommunicationsIO();
                flag = true;
                break;
            }
            catch (IOException ex)
            {
                log.warn("Got error on init. Ex.: " + ex);

                if (i == 4)
                    // FIXME
                    throw new PlugInBaseException(ex);

                log.warn("initCommunications: retrying...");
                i++;
            }
        }
        // setState(1);

        // TODO Auto-generated method stub
        return 0;
    }

    private void initCommunicationsIO() throws PlugInBaseException, IOException // ,
                                                                                // SerialIOHaltedException
    {
        log.debug("Here");
        log.info("initCommunicationsIO: waking up ComLink1.");
        // createSerialPort();
        log.debug("initCommunicationsIO: before read until empty");
        // readUntilEmpty();
        log.debug("initCommunicationsIO: before send command check reply [send=" + 6 + ", waiting for=" + 51);

        System.out.println("Receive timeout: " + this.serialPort.getReceiveTimeout());

        sendCommandCheckReply((byte) 6, (byte) 51);
        try
        {
            log.debug("initCommunicationsIO: before status");
            int i = readStatus();

            System.out.println("Status: " + i);
            if (i > 0)
            {
                log.warn("initCommunicationsIO: dumping " + i + " bytes.");
                this.portOutputStream.write((byte) 8);
                // sendTransferDataCommand();
                byte ai[] = new byte[i];
                // read(ai);

                this.portInputStream.read(ai);

                System.out.println("ai: " + ai);

                readAckByte();
            }
        }
        // catch(BadDeviceCommException baddevicecommexception) { }
        catch (IOException ex)
        {
            // FIXME
            throw new PlugInBaseException(ex);
        }
    }

    private int readStatus() throws PlugInBaseException, IOException
    {
        log.debug("readStatus");

        int status = sendCommandGetReply((byte) 2); // ASCII_STX);
        int m_numDataBytes = this.portInputStream.available();
        // read();
        readAckByte();
        log.debug("readStatus: " + status + ", CS status follows: NumberReceivedDataBytes=" + m_numDataBytes
                + ", ReceivedData=" + isStatusReceivedData(status) + ", RS232Mode=" + isStatusRS232Mode(status)
                + ", FilterRepeat=" + isStatusFilterRepeat(status) + ", AutoSleep=" + isStatusAutoSleep(status)
                + ", StatusError=" + isStatusError(status) + ", SelfTestError=" + isStatusSelfTestError(status));

        if (isStatusError(status))
            throw new PlugInBaseException("readStatus: ComLink1 has STATUS ERROR");
        if (isStatusSelfTestError(status))
            throw new PlugInBaseException("readStatus: ComLink1 has SELFTEST ERROR");
        else
            return isStatusReceivedData(status) ? m_numDataBytes : 0;

    }

    private boolean isStatusError(int status)
    {
        return (status & 0x10) != 0;
    }

    private boolean isStatusSelfTestError(int status)
    {
        return (status & 8) != 0;
    }

    private boolean isStatusReceivedData(int status)
    {
        return (status & 1) != 0;
    }

    private boolean isStatusFilterRepeat(int status)
    {
        return (status & 0x40) != 0;
    }

    private boolean isStatusAutoSleep(int status)
    {
        return (status & 0x20) != 0;
    }

    private boolean isStatusRS232Mode(int status)
    {
        return (status & 4) != 0;
    }

    private boolean readAckByte() throws PlugInBaseException, IOException
    {
        log.debug("readAckByte");

        // this.portInputStream.

        /*
         * while(this.portInputStream.available()>0)
         * {
         * System.out.println("Wait for data: ");
         * }
         */

        int[] rdata = new int[2];
        rdata[0] = this.portInputStream.read();

        // if (rdata[0] == 0)
        // rdata[0] = this.portInputStream.read();

        rdata[1] = this.portInputStream.read();

        /*
         * byte[] rdata = new byte[2];
         * rdata[0] = this.portInputStream.read();
         * rdata[1] = read();
         */

        System.out.println("Read Ack Byte: RData " + rdata[0] + ", " + rdata[1]);

        if (rdata[0] != 102)
        {
            log.debug("No return reply. We received: <" + hex_utils.getHex(rdata) + ">");
            // throw Exception;
            return false;
        }

        if (rdata[1] != 85)
        {
            log.debug("Incorrect ACK package. We expected " + hex_utils.getHex((byte) 85) + " (we got "
                    + hex_utils.getHex(rdata[1]) + ")");
            // throw Exception
            return false;
        }

        return true;
    }

}
