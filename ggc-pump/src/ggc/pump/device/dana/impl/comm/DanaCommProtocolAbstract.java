package ggc.pump.device.dana.impl.comm;

import com.atech.i18n.I18nControlAbstract;
import com.atech.utils.data.ATechDate;
import ggc.plugin.comm.IBMCommunicationHandler;
import ggc.plugin.output.AbstractOutputWriter;
import ggc.plugin.output.OutputWriter;
import ggc.plugin.protocol.SerialProtocol;
import ggc.plugin.protocol.reader.AbstractDeviceReader;
import ggc.pump.device.dana.impl.data.defs.DanaDataType;
import ggc.pump.util.DataAccessPump;
import gnu.io.SerialPort;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.util.GregorianCalendar;

/**
 * Created by andy on 11.03.15.
 */
public abstract class DanaCommProtocolAbstract
{
    private static final Log LOG = LogFactory.getLog(DanaCommProtocolAbstract.class);

    protected OutputWriter outputWriter;
    protected DataAccessPump dataAccess;
    protected I18nControlAbstract i18nControl;
    byte[] check_response = { 0x7e, 0x7e, (byte) 0xf2 };
    protected int errorCount;
    int currentYear;

    IBMCommunicationHandler commHandler;
    private String portName;

    public DanaCommProtocolAbstract(OutputWriter outputWriter, AbstractDeviceReader reader, String portName)
    {
        this.outputWriter = outputWriter;
        dataAccess = DataAccessPump.getInstance();
        i18nControl = dataAccess.getI18nControlInstance();

        init();
        loadPumpSpecificValues();
    }


    abstract void loadPumpSpecificValues();


    /**
     * Wait for x ms
     * @param time
     */
    protected void waitTime(long time)
    {
        try
        {
            Thread.sleep(time);

        }
        catch (Exception ex)
        {}
    }


    protected boolean checkIfValid(byte[] arr)
    {
        if ((arr[0] == check_response[0]) && (arr[1] == check_response[1]) && (arr[3] == check_response[2]))
        {
            return true;
        }
        else
        {
            return false;
        }
    }

    protected void incrementError()
    {
        this.errorCount++;
        this.outputWriter.setSubStatus(String.format(i18nControl.getMessage("ERROR_COUNT"), "" + this.errorCount));
    }




    public void init()
    {
        try
        {
        this.commHandler = new IBMCommunicationHandler(this.portName, null);


        // communcation settings for this meter(s)
        this.commHandler.setCommunicationSettings(19200, SerialPort.DATABITS_8, SerialPort.STOPBITS_1, SerialPort.PARITY_NONE,
                SerialPort.FLOWCONTROL_NONE, SerialProtocol.SERIAL_EVENT_BREAK_INTERRUPT
                        | SerialProtocol.SERIAL_EVENT_OUTPUT_EMPTY);



        this.errorCount = 0;


        // this.outputWriter.getOutputUtil().setMaxMemoryRecords(this.getMaxMemoryRecords());


        // settting serial port in com library

            //this.commHandler.setSerialPort(params);

            if (!open())
            {
                setDeviceStopped();
                return;
            }

            this.outputWriter.writeHeader();

        }
        catch (javax.comm.NoSuchPortException ex)
        {
            LOG.error("Port [" + this.portName + "] not found");
            setDeviceStopped();
        }
        catch (Exception ex)
        {
            LOG.error("Exception on create:" + ex, ex);
            this.setDeviceStopped();
        }

    }



    public boolean open() throws Exception
    {

        //try
        {
            byte[] buffer = new byte[0x100];

            if (!this.commHandler.connectAndInitDevice())
            {
                return false;
            }

            waitTime(2000); // wait for device to settle

            this.readData(buffer);
            waitTime(200);

            this.readData(buffer);
            waitTime(200);

            this.readData(buffer);
            waitTime(200);

            waitTime(1000);

            // PacketStreamReader reader = new PacketStreamReader(buffer);

            // byte num4 = reader.getCommand();
            // byte num5 = reader.getSubCommand();
            if (!(buffer[4] == (byte) 3) && (buffer[5] == (byte) 3))
            {
                throw new Exception("Port (" + this.portName + ") is not for DANA Diabecare R");
            }

            this.commHandler.readLine();
            waitTime(200);

            return true;
        }
        /*catch (Exception ex)
        {
            LOG.error("Exception on open: " + ex, ex);
            this.setDeviceStopped();
            return false;
        }*/
    }


    protected void connect() throws Exception
    {
        try
        {
            this.writeData(DanaDataType.Connect.getCommand());
            waitTime(1000);
            this.readData(new byte[0x100]);
            waitTime(200);
        }
        catch (Exception ex)
        {
            LOG.error("Error on connect. Ex: " + ex, ex);
            throw ex;
        }
    }

    protected void disconnect() throws Exception
    {
        try
        {
            this.writeData(DanaDataType.Disconnect.getCommand());
            waitTime(1000);
            this.readData(new byte[0x100]);
            waitTime(200);
        }
        catch (Exception ex)
        {
            LOG.error("Error on disconnect. Ex: " + ex, ex);
            throw ex;
        }
    }


    /**
     * Read data.
     *
     * @param buffer
     *            the buffer
     * @return the int
     * @throws Exception
     *             the exception
     */
    protected int readData(byte[] buffer) throws Exception
    {
        int num = 0;
        try
        {
            if (this.commHandler.read(buffer, 0, 4) == 0)
            {}

            num = buffer[2] - 1;
            this.commHandler.read(buffer, 4, num + 4);
            int v = this.createCRC(buffer, 3, num + 1);
            byte num4 = (byte) ((v >> 8) & 0xff);
            byte num5 = (byte) (v & 0xff);
            if ((buffer[4 + num] != num4) || (buffer[4 + num + 1] != num5))
            {}

        }
        catch (Exception ex)
        {
            LOG.error("readData(). Exception: " + ex, ex);
        }
        return num;
    }


    protected void writeData(byte[] buffer) throws Exception
    {
        this.writeData(buffer, 0, buffer.length);
    }

    protected void writeData(byte[] buffer, int offset, int length) throws Exception
    {
        byte[] destinationArray = new byte[length + 8];
        try
        {
            // logger.debug("writeData:Start");
            destinationArray[0] = 0x7e;
            destinationArray[1] = 0x7e;
            destinationArray[2] = (byte) ((length + 1) & 0xff);
            // logger.debug("Send length: " + length);
            destinationArray[3] = (byte) 0xf1;
            System.arraycopy(buffer, offset, destinationArray, 4, length);
            int v = this.createCRC(buffer, offset, length);
            destinationArray[4 + length] = (byte) ((v >> 8) & 0xff);
            destinationArray[5 + length] = (byte) (v & 0xff);
            // logger.debug("CRC: " + DanaUtil.toHexString(v));
            destinationArray[6 + length] = 0x2e;
            destinationArray[7 + length] = 0x2e;
            this.commHandler.write(destinationArray, 0, destinationArray.length);
            // logger.debug("writeData:End");
        }
        catch (Exception exception)
        {
            throw exception;
        }
    }



    protected int getYear(int year)
    {
        year += 2000;

        if (year > currentYear)
        {
            return currentYear;
        }
        else
        {
            return year;
        }
    }

    protected ATechDate getDateTime(byte year, byte month, byte day, byte hour, byte minute, byte second)
    {
        GregorianCalendar gc;
        try
        {
            gc = new GregorianCalendar(getYear(year), month - 1, day, hour, hour, second);
        }
        catch (Exception ex)
        {
            gc = new GregorianCalendar(2000, 0, 1, 0, 0, 0);
        }

        return new ATechDate(ATechDate.FORMAT_DATE_AND_TIME_S, gc);

    }


    protected void setDeviceStopped()
    {
        //this.device_communicating = false;
        // System.out.println("Device not communicating");
        this.outputWriter.setStatus(AbstractOutputWriter.STATUS_STOPPED_DEVICE);
    }


    private int crc16(byte data, int crc)
    {
        int num = 0;
        num = ((crc >> 8) & 0xff) | (crc << 8);
        num ^= data & 0xff;
        num ^= ((num & 0xff) >> 4) & 0xfff;
        num ^= num << 8 << 4;
        return num ^ (((num & 0xff) << 5) | ((((num & 0xff) >> 3) & 0x1fff) << 8));
    }

    /**
     * Creates the crc.
     *
     * @param data
     *            the data
     * @param offset
     *            the offset
     * @param length
     *            the length
     *
     * @return the int
     */
    protected int createCRC(byte[] data, int offset, int length)
    {
        int crc = 0;
        for (int i = 0; i < length; i++)
        {
            crc = crc16(data[offset + i], crc);
        }
        return crc;
    }

}
