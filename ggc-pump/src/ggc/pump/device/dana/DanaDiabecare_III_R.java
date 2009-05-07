package ggc.pump.device.dana;

import ggc.plugin.protocol.SerialProtocol;
import gnu.io.SerialPort;
import gnu.io.SerialPortEvent;

import java.util.GregorianCalendar;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

// TODO: Auto-generated Javadoc
/**
 * The Class DanaDiabecare_III_R.
 */
public class DanaDiabecare_III_R extends SerialProtocol
{
    private static Log logger = LogFactory.getLog(DanaDiabecare_III_R.class);

    @SuppressWarnings("unused")
    private String portName;
    // public SerialPort serialPort;
    /**
     * The Constant SYNC_CMD_BASAL.
     */
    public static final String SYNC_CMD_BASAL = "BASAL";
    
    /**
     * The Constant SYNC_CMD_BASAL_PROFILE.
     */
    public static final String SYNC_CMD_BASAL_PROFILE = "BASAL-PROFILE";
    
    /**
     * The Constant SYNC_CMD_BOLUS.
     */
    public static final String SYNC_CMD_BOLUS = "BOLUS";
    
    /**
     * The Constant SYNC_CMD_CARBO.
     */
    public static final String SYNC_CMD_CARBO = "CARBO";
    
    /**
     * The Constant SYNC_CMD_GENER.
     */
    public static final String SYNC_CMD_GENER = "GENER";
    
    /**
     * The Constant SYNC_CMD_GLUCOMODE.
     */
    public static final String SYNC_CMD_GLUCOMODE = "GLUCOMODE";
    
    /**
     * The Constant SYNC_CMD_MAX.
     */
    public static final String SYNC_CMD_MAX = "MAX";
    
    /**
     * The Constant SYNC_CMD_PWM.
     */
    public static final String SYNC_CMD_PWM = "PWM";
    
    /**
     * The Constant SYNC_CMD_SHIPPING.
     */
    public static final String SYNC_CMD_SHIPPING = "SHIPPING";

    /**
     * The Constant COMMAND_BASAL.
     */
    public static final int COMMAND_BASAL = 1;
    
    /**
     * The Constant COMMAND_BASAL_PROFILE.
     */
    public static final int COMMAND_BASAL_PROFILE = 2;
    
    /**
     * The Constant COMMAND_BOLUS.
     */
    public static final int COMMAND_BOLUS = 3;
    
    /**
     * The Constant COMMAND_CARBO.
     */
    public static final int COMMAND_CARBO = 4;
    
    /**
     * The Constant COMMAND_GENER.
     */
    public static final int COMMAND_GENER = 5;
    
    /**
     * The Constant COMMAND_GLUCOMODE.
     */
    public static final int COMMAND_GLUCOMODE = 6;
    
    /**
     * The Constant COMMAND_MAX.
     */
    public static final int COMMAND_MAX = 7;
    
    /**
     * The Constant COMMAND_PWM.
     */
    public static final int COMMAND_PWM = 8;
    
    /**
     * The Constant COMMAND_SHIPPING.
     */
    public static final int COMMAND_SHIPPING = 9;

    /**
     * The command_commnds.
     */
    public String[] command_commnds = { "", "BASAL", "BASAL-PROFILE", "BOLUS", "CARBO", "GENER", "GLUCOMODE", "MAX", "PWM", "SHIPPING" };

    /**
     * The SYN c_ connect.
     */
    public static byte[] SYNC_CONNECT = new byte[] { 0x30, 1 };
    
    /**
     * The SYN c_ disconnect.
     */
    public static byte[] SYNC_DISCONNECT = new byte[] { 0x30, 2 };
    
    /**
     * The Constant SYNC_OPTION_ALL.
     */
    public static final byte SYNC_OPTION_ALL = 1;
    
    /**
     * The Constant SYNC_OPTION_CONFIG.
     */
    public static final byte SYNC_OPTION_CONFIG = 2;
    
    /**
     * The Constant SYNC_OPTION_RECORD.
     */
    public static final byte SYNC_OPTION_RECORD = 0;
    
    /**
     * The SYN c_ quer y_ alar m_ record.
     */
    public static byte[] SYNC_QUERY_ALARM_RECORD = new byte[] { 0x31, 5 };
    
    /**
     * The SYN c_ quer y_ basal.
     */
    public static byte[] SYNC_QUERY_BASAL = new byte[] { 50, 2 };
    
    /**
     * The SYN c_ quer y_ basa l_ profile.
     */
    public static byte[] SYNC_QUERY_BASAL_PROFILE = new byte[] { 50, 6 };
    
    /**
     * The SYN c_ quer y_ bolus.
     */
    public static byte[] SYNC_QUERY_BOLUS = new byte[] { 50, 1 };
    
    /**
     * The SYN c_ quer y_ bolu s_ record.
     */
    public static byte[] SYNC_QUERY_BOLUS_RECORD = new byte[] { 0x31, 1 };
    
    /**
     * The SYN c_ quer y_ carbo.
     */
    public static byte[] SYNC_QUERY_CARBO = new byte[] { 50, 4 };
    
    /**
     * The SYN c_ quer y_ carb o_ record.
     */
    public static byte[] SYNC_QUERY_CARBO_RECORD = new byte[] { 0x31, 7 };
    
    /**
     * The SYN c_ quer y_ dail y_ record.
     */
    public static byte[] SYNC_QUERY_DAILY_RECORD = new byte[] { 0x31, 2 };
    
    /**
     * The SYN c_ quer y_ erro r_ record.
     */
    public static byte[] SYNC_QUERY_ERROR_RECORD = new byte[] { 0x31, 6 };
    
    /**
     * The SYN c_ quer y_ gener.
     */
    public static byte[] SYNC_QUERY_GENER = new byte[] { 50, 3 };
    
    /**
     * The SYN c_ quer y_ gluc o_ record.
     */
    public static byte[] SYNC_QUERY_GLUCO_RECORD = new byte[] { 0x31, 4 };
    
    /**
     * The SYN c_ quer y_ glucomode.
     */
    public static byte[] SYNC_QUERY_GLUCOMODE = new byte[] { 50, 9 };
    
    /**
     * The SYN c_ quer y_ max.
     */
    public static byte[] SYNC_QUERY_MAX = new byte[] { 50, 5 };
    
    /**
     * The SYN c_ quer y_ prim e_ record.
     */
    public static byte[] SYNC_QUERY_PRIME_RECORD = new byte[] { 0x31, 3 };
    
    /**
     * The SYN c_ quer y_ pwm.
     */
    public static byte[] SYNC_QUERY_PWM = new byte[] { 50, 8 };
    
    /**
     * The SYN c_ quer y_ shipping.
     */
    public static byte[] SYNC_QUERY_SHIPPING = new byte[] { 50, 7 };

    /**
     * The sett.
     */
    Settings sett = new Settings();

    /**
     * Instantiates a new dana diabecare_ ii i_ r.
     * 
     * @param portName the port name
     */
    public DanaDiabecare_III_R(String portName)
    {
        super();

        this.portName = portName;
        this.setCommunicationSettings(19200, SerialPort.DATABITS_8, SerialPort.STOPBITS_1, SerialPort.PARITY_NONE, 0, 0);

        /*
         * this.serialPort.ReadBufferSize = sett.serialReadBufferSize;
         * this.serialPort.ReadTimeout = sett.serialReadTimeout;
         * this.serialPort.WriteBufferSize = sett.serialWriteBufferSize;
         * this.serialPort.WriteTimeout = sett.serialWriteTimeout;
         */

        try
        {
            this.portName = portName;
            this.setPort(portName);
            // .serialPort = new SerialPort(portName);
            // this.logger = Logger.getInstance("SYNC[" + portName + "]");
            logger.debug("SyncManager()");
        }
        catch (Exception ex)
        {
            // throw new Exception("SyncManager(" + portName + ")" +
            // ex.getMessage());
        }
    }

    /*
     * private void close() throws Exception { try { this.serialPort.close(); }
     * catch (Exception exception) { throw exception; } }
     */

    private void connect() throws Exception
    {
        byte[] buffer = new byte[0x100];
        try
        {
            this.writeData(SYNC_CONNECT);
            DanaUtil.delaySyncStart();
            /*int num =*/ this.readData(buffer);
            /*PacketStreamReader reader =*/ new PacketStreamReader(buffer);
            DanaUtil.delaySync();
        }
        catch (Exception exception)
        {
            throw exception;
        }
    }

    @SuppressWarnings("unused")
    private void disconnect() throws Exception
    {
        byte[] buffer = new byte[0x100];
        try
        {
            this.writeData(SYNC_DISCONNECT);
            DanaUtil.delaySyncStart();
            /*int num =*/ this.readData(buffer);
            /*PacketStreamReader reader =*/ new PacketStreamReader(buffer);
            DanaUtil.delaySync();
        }
        catch (Exception exception)
        {
            throw exception;
        }
    }

    /**
     * Gets the device info.
     * 
     * @throws Exception the exception
     */
    public void getDeviceInfo() throws Exception
    {
        try
        {
            try
            {
                this.open();
                this.connect();
                this.getDeviceInfo(DanaDiabecare_III_R.COMMAND_BOLUS);
                this.getDeviceInfo(DanaDiabecare_III_R.COMMAND_BASAL_PROFILE);
                this.getDeviceInfo(DanaDiabecare_III_R.COMMAND_BASAL);
                this.getDeviceInfo(DanaDiabecare_III_R.COMMAND_GENER);
                this.getDeviceInfo(DanaDiabecare_III_R.COMMAND_CARBO);
                this.getDeviceInfo(DanaDiabecare_III_R.COMMAND_MAX);
                this.getDeviceInfo(DanaDiabecare_III_R.COMMAND_SHIPPING);
                this.getDeviceInfo(DanaDiabecare_III_R.COMMAND_GLUCOMODE);
            }
            catch (Exception exception)
            {
                throw exception;
            }
        }
        finally
        {
            this.close();
        }
    }

    private void getDeviceInfo(int param) throws Exception
    {
        try
        {
            byte[] buffer = new byte[0x100];
            //int num = 0;
            logger.debug("getDeviceInfo(" + param + "):Start");
            switch (param)
            {
            case COMMAND_BOLUS:
                this.writeData(SYNC_QUERY_BOLUS);
                break;

            case COMMAND_BASAL:
                this.writeData(SYNC_QUERY_BASAL);
                break;

            case COMMAND_GENER:
                this.writeData(SYNC_QUERY_GENER);
                break;

            case COMMAND_CARBO:
                this.writeData(SYNC_QUERY_CARBO);
                break;

            case COMMAND_MAX:
                this.writeData(SYNC_QUERY_MAX);
                break;

            case COMMAND_BASAL_PROFILE:
                this.writeData(SYNC_QUERY_BASAL_PROFILE);
                break;

            case COMMAND_SHIPPING:
                this.writeData(SYNC_QUERY_SHIPPING);
                break;

            case COMMAND_PWM:
                this.writeData(SYNC_QUERY_PWM);
                break;

            case COMMAND_GLUCOMODE:
                this.writeData(SYNC_QUERY_GLUCOMODE);
                break;
            }
            DanaUtil.delaySync();
            /*num =*/ this.readData(buffer);
            PacketStreamReader reader = new PacketStreamReader(buffer);

            switch (param)
            {
            case COMMAND_BOLUS:
                sett.bolus0 = reader.getInt();
                sett.bolus1 = reader.getInt();
                sett.bolus2 = reader.getInt();
                sett.bolus3 = reader.getInt();
                break;

            case COMMAND_BASAL:
                sett.basal0 = reader.getInt();
                sett.basal1 = reader.getInt();
                sett.basal2 = reader.getInt();
                sett.basal3 = reader.getInt();
                sett.basal4 = reader.getInt();
                sett.basal5 = reader.getInt();
                sett.basal6 = reader.getInt();
                sett.basal7 = reader.getInt();
                sett.basal8 = reader.getInt();
                sett.basal9 = reader.getInt();
                sett.basal10 = reader.getInt();
                sett.basal11 = reader.getInt();
                sett.basal12 = reader.getInt();
                sett.basal13 = reader.getInt();
                sett.basal14 = reader.getInt();
                sett.basal15 = reader.getInt();
                sett.basal16 = reader.getInt();
                sett.basal17 = reader.getInt();
                sett.basal18 = reader.getInt();
                sett.basal19 = reader.getInt();
                sett.basal20 = reader.getInt();
                sett.basal21 = reader.getInt();
                sett.basal22 = reader.getInt();
                sett.basal23 = reader.getInt();
                break;

            case COMMAND_GENER:
                sett.basalIncrement = reader.getByte();
                sett.bolusIncrement = reader.getByte();
                sett.bolusPreset = reader.getByte();
                sett.bolusAlarm = reader.getByte();
                sett.bolusBlock = reader.getByte();
                sett.basalUnit = reader.getByte();
                break;

            case COMMAND_CARBO:
                sett.carboCIR = reader.getInt();
                sett.carboCF = reader.getInt();
                sett.carboAI = reader.getInt();
                sett.carboTG = reader.getInt();
                sett.carboAIDR = reader.getByte();
                break;

            case COMMAND_MAX:
                sett.maxBolusRate = reader.getInt();
                sett.maxBasalRate = reader.getInt();
                sett.maxDailyRate = reader.getInt();
                break;

            case COMMAND_SHIPPING:
                sett.serialNo = reader.getString(10);
                try
                {
                    // sett.shippingDate = new DateTime(reader.getByte() +
                    // 0x7d0, reader.getByte(), reader.getByte());
                }
                catch (Exception exception)
                {
                    // sett.shippingDate = DateTime.MinValue;
                    logger.error(exception.getMessage());
                }
                sett.shippingCountry = reader.getAscii(3);
                break;

            case COMMAND_GLUCOMODE:
                sett.glucoseUnit = reader.getByte();
                sett.easyMode = reader.getByte();
                break;
            }
            DanaUtil.delaySync();
            logger.debug("getDeviceInfo(" + param + "):End");
        }
        catch (Exception ex2)
        {
            throw new Exception("sync(" + param + ")" + ex2.getMessage());
        }
    }

    /**
     * Gets the device record.
     * 
     * @throws Exception the exception
     */
    public void getDeviceRecord() throws Exception
    {
        try
        {
            try
            {
                this.open();
                this.connect();
                sett.recordList.clear();
                this.getDeviceRecord((byte) 1);
                this.getDeviceRecord((byte) 2);
                this.getDeviceRecord((byte) 3);
                this.getDeviceRecord((byte) 7);
                this.getDeviceRecord((byte) 4);
            }
            catch (Exception exception)
            {
                sett.recordList.clear();
                throw exception;
            }
        }
        finally
        {
            this.close();
        }
    }

    /**
     * Gets the device record.
     * 
     * @param param the param
     * 
     * @throws Exception the exception
     */
    public void getDeviceRecord(byte param) throws Exception
    {
        try
        {
            boolean flag;
            logger.debug("getDeviceRecord(" + param + "):Start");
            byte num = param;
            byte[] buffer = new byte[0x200];
            byte[] buffer2 = new byte[] { 0x31, num };
            int num2 = 0;
            this.writeData(buffer2);
            DanaUtil.delaySyncStart();

            flag = true;

            while (flag)
            {

                // goto Label_01AF;

                // Label_0061:;

                try
                {
                    num2 = (this.readData(buffer) - 2) / 10;
                }
                catch (Exception exception1)
                {
                    logger.debug("readData: " + exception1.getMessage(), exception1);
                    logger.debug("getDeviceRecord(" + param + "):End");
                    flag = false;
                }

                PacketStreamReader reader = new PacketStreamReader(buffer);
                for (int i = 0; i < num2; i++)
                {
                    DeviceRecord record = new DeviceRecord();
                    record.serialNo = sett.serialNo;
                    record.recordType = reader.getByte();
                    int num5 = reader.getByte();
                    int month = reader.getByte();
                    int day = reader.getByte();
                    int hour = reader.getByte();
                    int minute = reader.getByte();
                    int second = reader.getByte();
                    record.recordCode = reader.getByte();
                    record.recordValue = reader.getInt();
                    try
                    {
                        record.recordDate = new GregorianCalendar(num5 + 0x7d0, month, day, hour, minute, second);
                    }
                    catch (Exception exception2)
                    {
                        record.recordDate = new GregorianCalendar(2000, 01, 01, 00, 00, 00);
                        logger.error(exception2.getMessage());
                    }
                    sett.recordList.add(record);
                }

                DanaUtil.delaySync(num2 * 10);

                // Label_01AF:

                //flag = true;
            }
            // goto Label_0061;

            // Label_01B7:
            logger.debug("getDeviceRecord(" + param + "):End");
        }
        catch (Exception exception4)
        {
            Exception exception = exception4;
            throw new Exception("SyncManager.getDeviceInfo(" + param + ")" + exception.getMessage());
        }
    }

    /*
     * public static SyncManager getInstance(string portName) { SyncManager
     * manager; try { manager = new SyncManager(portName); } catch (Exception
     * exception) { throw new Exception("SyncManager.getInstance()" +
     * exception.Message); } return manager; }
     */

    /** 
     * open
     */
    public boolean open()
    {

        try
        {
            byte[] buffer = new byte[0x100];

            /*
             * this.serialPort.PortName = sett.serialPortName;
             * this.serialPort.BaudRate = sett.serialBaudRate;
             * this.serialPort.Parity = Parity.None; this.serialPort.StopBits =
             * StopBits.One; this.serialPort.DataBits = sett.serialDataBits;
             * this.serialPort.ReadBufferSize = sett.serialReadBufferSize;
             * this.serialPort.ReadTimeout = sett.serialReadTimeout;
             * this.serialPort.WriteBufferSize = sett.serialWriteBufferSize;
             * this.serialPort.WriteTimeout = sett.serialWriteTimeout;
             * this.serialPort.Open();
             */
            super.open();

            DanaUtil.delaySyncOpen();
            /*int num =*/ this.readData(buffer);
            DanaUtil.delaySync();
            /*int num2 =*/ this.readData(buffer);
            DanaUtil.delaySync();
            /*int num3 =*/ this.readData(buffer);
            DanaUtil.delaySync();
            PacketStreamReader reader = new PacketStreamReader(buffer);
            byte num4 = reader.getCommand();
            byte num5 = reader.getSubCommand();
            if (!(num4 == (byte) 3) && (num5 == ((byte) 3)))
            {
                throw new Exception("Port(" + sett.serialPortName + ") is not for DANA Diabecare R");
            }
            return true;
        }
        catch (Exception ex)
        {
            logger.error("Exception on open: " + ex, ex);
            // throw exception;
            return false;
        }
    }

    /**
     * Read data.
     * 
     * @param buffer the buffer
     * 
     * @return the int
     * 
     * @throws Exception the exception
     */
    public int readData(byte[] buffer) throws Exception
    {
        int num = 0;
        try
        {
            logger.debug("readData:Start");

            if (this.read(buffer, 0, 4) == 0)
            {
                throw new Exception("No more data");
            }

            num = buffer[2] - 1;
            logger.debug("Receieve length: " + num);
            this.read(buffer, 4, num + 4);
            int v = DanaUtil.createCRC(buffer, 3, num + 1);
            logger.debug("Created CRC: " + DanaUtil.toHexString(v));
            byte num4 = (byte) ((v >> 8) & 0xff);
            byte num5 = (byte) (v & 0xff);
            if ((buffer[4 + num] != num4) || (buffer[(4 + num) + 1] != num5))
            {
            }
            logger.debug("readData:End");
        }
        catch (Exception exception)
        {
            throw exception;
        }
        return num;
    }

    /*
     * public void setDeviceInfo() { try { try { this.open(); this.connect();
     * this.setDeviceInfo("BOLUS"); this.setDeviceInfo("BASAL");
     * this.setDeviceInfo("GENER"); this.setDeviceInfo("CARBO");
     * this.setDeviceInfo("MAX"); this.setDeviceInfo("GLUCOMODE"); } catch
     * (Exception exception) { throw exception; } } finally { this.close(); } }
     * 
     * 
     * public void setDeviceInfo(string param) { try { byte[] buffer; byte[]
     * buffer2 = new byte[0x100]; this.logger.debug("setDeviceInfo(" + param +
     * "):Start", new Object[0]); switch (param) { case "BOLUS": buffer = new
     * byte[10]; break;
     * 
     * case "BASAL": buffer = new byte[50]; break;
     * 
     * case "GENER": buffer = new byte[8]; break;
     * 
     * case "CARBO": buffer = new byte[11]; break;
     * 
     * case "MAX": buffer = new byte[8]; break;
     * 
     * case "BASAL-PROFILE": buffer = new byte[10]; break;
     * 
     * case "SHIPPING": buffer = new byte[10]; break;
     * 
     * case "PWM": buffer = new byte[10]; break;
     * 
     * case "GLUCOMODE": buffer = new byte[4]; break;
     * 
     * default: buffer = new byte[10]; break; } PacketStreamWriter writer = new
     * PacketStreamWriter(buffer); switch (param) { case "BOLUS":
     * writer.setByte(0x33); writer.setByte(1); writer.setInt(sett.bolus0);
     * writer.setInt(sett.bolus1); writer.setInt(sett.bolus2);
     * writer.setInt(sett.bolus3); break;
     * 
     * case "BASAL": writer.setByte(0x33); writer.setByte(2);
     * writer.setInt(sett.basal0); writer.setInt(sett.basal1);
     * writer.setInt(sett.basal2); writer.setInt(sett.basal3);
     * writer.setInt(sett.basal4); writer.setInt(sett.basal5);
     * writer.setInt(sett.basal6); writer.setInt(sett.basal7);
     * writer.setInt(sett.basal8); writer.setInt(sett.basal9);
     * writer.setInt(sett.basal10); writer.setInt(sett.basal11);
     * writer.setInt(sett.basal12); writer.setInt(sett.basal13);
     * writer.setInt(sett.basal14); writer.setInt(sett.basal15);
     * writer.setInt(sett.basal16); writer.setInt(sett.basal17);
     * writer.setInt(sett.basal18); writer.setInt(sett.basal19);
     * writer.setInt(sett.basal20); writer.setInt(sett.basal21);
     * writer.setInt(sett.basal22); writer.setInt(sett.basal23); break;
     * 
     * case "GENER": writer.setByte(0x33); writer.setByte(3);
     * writer.setByte(sett.basalIncrement); writer.setByte(sett.bolusIncrement);
     * writer.setByte(sett.bolusPreset); writer.setByte(sett.bolusAlarm);
     * writer.setByte(sett.bolusBlock); writer.setByte(sett.basalUnit); break;
     * 
     * case "CARBO": writer.setByte(0x33); writer.setByte(4);
     * writer.setInt(sett.carboCIR); writer.setInt(sett.carboCF);
     * writer.setInt(sett.carboAI); writer.setInt(sett.carboTG);
     * writer.setByte(sett.carboAIDR); break;
     * 
     * case "MAX": writer.setByte(0x33); writer.setByte(5);
     * writer.setInt(sett.maxBolusRate); writer.setInt(sett.maxBasalRate);
     * writer.setInt(sett.maxDailyRate); break;
     * 
     * case "GLUCOMODE": writer.setByte(0x33); writer.setByte(9);
     * writer.setByte(sett.glucoseUnit); writer.setByte(sett.easyMode); break; }
     * this.writeData(buffer); Delay.delaySyncStart(); int num =
     * this.readData(buffer2); PacketStreamReader reader = new
     * PacketStreamReader(buffer2); Delay.delaySync();
     * this.logger.debug("setDeviceInfo(" + param + "):End", new object[0]); }
     * catch (Exception exception) { this.logger.error(exception.Message, new
     * object[0]); throw new Exception("sync(" + param + ")" +
     * exception.Message); } }
     * 
     * 
     * public byte setNormalBolus(int bolusRate) { byte num3; byte[] buffer =
     * new byte[4]; byte[] buffer2 = new byte[0x100]; try { this.open();
     * PacketStreamWriter writer = new PacketStreamWriter(buffer);
     * writer.setByte((byte)1); writer.setByte((byte)2);
     * writer.setInt(bolusRate); this.writeData(buffer); Delay.delaySyncStart();
     * int num2 = this.readData(buffer2); num3 = new
     * PacketStreamReader(buffer2).getByte(); } catch (Exception exception) {
     * this.logger.error(exception.Message, new object[0]); throw exception; }
     * finally { this.close(); } return num3; }
     * 
     * public byte setTemporaryBasal(byte tbHour, byte tbRatio) { byte num3;
     * byte[] buffer = new byte[4]; byte[] buffer2 = new byte[0x100]; try {
     * this.open(); PacketStreamWriter writer = new PacketStreamWriter(buffer);
     * writer.setByte((byte)4); writer.setByte((byte)0x1);
     * writer.setByte(tbRatio); writer.setByte(tbHour); this.writeData(buffer);
     * Delay.delaySyncStart(); int num2 = this.readData(buffer2); num3 = new
     * PacketStreamReader(buffer2).getByte(); } catch (Exception ex) {
     * this.logger.error(ex.getMessage(), ex); throw ex; } finally {
     * this.close(); } return num3; }
     */

    private void writeData(byte[] buffer) throws Exception
    {
        this.writeData(buffer, 0, buffer.length);
    }

    private void writeData(byte[] buffer, int offset, int length) throws Exception
    {
        byte[] destinationArray = new byte[length + 8];
        try
        {
            logger.debug("writeData:Start");
            destinationArray[0] = 0x7e;
            destinationArray[1] = 0x7e;
            destinationArray[2] = (byte) ((length + 1) & 0xff);
            logger.debug("Send length: " + length);
            destinationArray[3] = (byte) 0xf1;
            System.arraycopy(buffer, offset, destinationArray, 4, length);
            int v = DanaUtil.createCRC(buffer, offset, length);
            destinationArray[4 + length] = (byte) ((v >> 8) & 0xff);
            destinationArray[5 + length] = (byte) (v & 0xff);
            logger.debug("CRC: " + DanaUtil.toHexString(v));
            destinationArray[6 + length] = 0x2e;
            destinationArray[7 + length] = 0x2e;
            this.write(destinationArray, 0, destinationArray.length);
            logger.debug("writeData:End");
        }
        catch (Exception exception)
        {
            throw exception;
        }
    }

    /** 
     * serialEvent
     */
    @Override
    public void serialEvent(SerialPortEvent event)
    {
        // TODO Auto-generated method stub
    }

}
