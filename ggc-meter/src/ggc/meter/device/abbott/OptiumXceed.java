package ggc.meter.device.abbott;

import java.util.StringTokenizer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.atech.utils.ATDataAccessAbstract;
import com.atech.utils.data.ATechDate;
import com.atech.utils.data.TimeZoneUtil;

import ggc.core.data.defs.DailyValuesExtendedType;
import ggc.core.data.defs.GlucoseUnitType;
import ggc.meter.data.MeterValuesEntry;
import ggc.meter.device.AbstractSerialMeter;
import ggc.meter.device.MeterInterface;
import ggc.meter.manager.MeterDevicesIds;
import ggc.meter.manager.company.Abbott;
import ggc.meter.util.DataAccessMeter;
import ggc.plugin.device.DeviceIdentification;
import ggc.plugin.device.DownloadSupportType;
import ggc.plugin.device.PlugInBaseException;
import ggc.plugin.manager.DeviceImplementationStatus;
import ggc.plugin.manager.company.AbstractDeviceCompany;
import ggc.plugin.output.AbstractOutputWriter;
import ggc.plugin.output.OutputWriter;
import ggc.plugin.protocol.SerialProtocol;
import ggc.plugin.util.DataAccessPlugInBase;
import gnu.io.SerialPort;
import gnu.io.SerialPortEvent;

/**
 *  Application:   GGC - GNU Gluco Control
 *  Plug-in:       Meter Tool (support for Meter devices)
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
 *  Filename:     OneTouchMeter  
 *  Description:  Super class for OT meters with basic ASCII protocol
 * 
 *  Author: Andy {andy@atech-software.com}
 */

public class OptiumXceed extends AbstractSerialMeter
{

    private static final Logger LOG = LoggerFactory.getLogger(OptiumXceed.class);
    protected boolean device_running = true;
    protected TimeZoneUtil tzu = TimeZoneUtil.getInstance();
    private int entries_max = 0;
    private int entries_current = 0;


    /**
     * Constructor
     */
    public OptiumXceed()
    {
    }


    /**
     * Constructor for device manager
     * 
     * @param cmp
     */
    public OptiumXceed(AbstractDeviceCompany cmp)
    {
        super(cmp);
    }


    /**
     * Constructor
     * 
     * @param portName
     * @param writer
     */
    public OptiumXceed(String portName, OutputWriter writer)
    {
        this(portName, writer, DataAccessMeter.getInstance());
        // super(DataAccessMeter.getInstance());

        this.setCommunicationSettings(9600, SerialPort.DATABITS_8, SerialPort.STOPBITS_1, SerialPort.PARITY_NONE,
            SerialPort.FLOWCONTROL_NONE,
            SerialProtocol.SERIAL_EVENT_BREAK_INTERRUPT | SerialProtocol.SERIAL_EVENT_OUTPUT_EMPTY);

        // output writer, this is how data is returned (for testing new devices,
        // we can use Consol
        this.outputWriter = writer;
        this.outputWriter.getOutputUtil().setMaxMemoryRecords(this.getMaxMemoryRecords());

        // set meter type (this will be deprecated in future, but it's needed
        // for now
        this.setMeterType("Abbott", this.getName());

        // set device company (needed for now, will also be deprecated)
        this.setDeviceCompany(new Abbott());

        // settting serial port in com library
        try
        {
            this.setSerialPort(portName);

            if (!this.open())
            {
                this.m_status = 1;
                this.deviceDisconnected();
                return;
            }

            this.outputWriter.writeHeader();

        }
        catch (Exception ex)
        {
            LOG.error("OptiumXceed: Error connecting !\nException: " + ex, ex);
            System.out.println("OptiumXceed: Error connecting !\nException: " + ex);
        }

    }


    /**
     * Constructor
     * 
     * @param comm_parameters 
     * @param writer
     * @param da
     */
    public OptiumXceed(String comm_parameters, OutputWriter writer, DataAccessPlugInBase da)
    {
        super(comm_parameters, writer, da);

        this.setCommunicationSettings(9600, SerialPort.DATABITS_8, SerialPort.STOPBITS_1, SerialPort.PARITY_NONE,
            SerialPort.FLOWCONTROL_NONE,
            SerialProtocol.SERIAL_EVENT_BREAK_INTERRUPT | SerialProtocol.SERIAL_EVENT_OUTPUT_EMPTY);

        // output writer, this is how data is returned (for testing new devices,
        // we can use Consol
        this.outputWriter = writer;
        this.outputWriter.getOutputUtil().setMaxMemoryRecords(this.getMaxMemoryRecords());

        // set meter type (this will be deprecated in future, but it's needed
        // for now
        this.setMeterType("Abbott", this.getName());

        // set device company (needed for now, will also be deprecated)
        this.setDeviceCompany(new Abbott());

        // settting serial port in com library
        try
        {
            this.setSerialPort(comm_parameters);

            if (!this.open())
            {
                this.m_status = 1;
                this.deviceDisconnected();
                return;
            }

            this.outputWriter.writeHeader();

        }
        catch (Exception ex)
        {
            LOG.error("OptiumXceed: Error connecting !\nException: " + ex, ex);
            System.out.println("OptiumXceed: Error connecting !\nException: " + ex);
        }

    }


    // public static final byte ENQ = 0x05;

    /** 
     * readDeviceDataFull
     */
    public void readDeviceDataFull()
    {
        System.out.println("readDeviceDataFull()");
        try
        {

            readInfo();

            String data_back;

            this.sendMessageToMeter("1GET_EVENTS\00348\r\n");

            this.outputWriter.setSubStatus(i18nControlAbstract.getMessage("PIX_READING"));

            for (data_back = readLine(); data_back.indexOf("END_OF_DATA") == -1;)
            {
                processDataLine(data_back);
                writeCommand(6);

                data_back = readLine();

                if (data_back == null)
                {
                    endReading();
                    break;
                }

                if (this.outputWriter.isReadingStopped())
                {
                    break;
                }

            }

            writeCommand(6);
            readByteTimed();

            this.outputWriter.setSpecialProgress(100);
            this.outputWriter.setSubStatus(null);

            if (this.isDeviceFinished())
            {
                this.outputWriter.endOutput();
            }

        }
        catch (Exception ex)
        {
            System.out.println("Exception: " + ex);
            ex.printStackTrace();

        }
        finally
        {
            this.close();
        }

        // System.out.println("Reading finished !");

    }


    private boolean startMessageToMeter() throws Exception
    {
        boolean done = false;
        int status;

        write(SerialProtocol.ASCII_ENQ);

        do
        {

            status = readByteTimed();

            if (status == SerialProtocol.ASCII_ENQ)
            {
                write(SerialProtocol.ASCII_ACK);
            }
            else if (status == SerialProtocol.ASCII_ACK)
                return true;
            else if (status == 2)
            {
                this.readLine();
                commandAfterRead();
                return true;
            }
            else if (status == 4)
            {
                commandAfterRead();
                return true;
            }
            else if (status == SerialProtocol.ASCII_NAK || status == 0)
            {
                endReading();
                return false;
            }

        } while (done != true);

        return false;
    }


    private void processDataLine(String line)
    {

        if (line == null || line.trim().length() == 0)
            return;

        try
        {
            String[] data = dataAccess.splitString(line, "\t");

            if (!data[1].equals("1"))
                return;

            String type_id = data[0].substring(1);

            boolean is_BG = false;

            if (type_id.equals("01"))
            {
                is_BG = true;
            }
            else if (type_id.equals("04"))
            {
                is_BG = false;
            }
            else
            {
                this.entries_current++;
                readingEntryStatus();

                return;
            }

            if (is_BG)
            {
                addBGData(data[4], getDateTime(data[2], data[3]));
            }
            else
            {
                addUrineData(data[4], getDateTime(data[2], data[3]));
            }

            StringTokenizer strtok = new StringTokenizer(line, "\t");

            while (strtok.hasMoreTokens())
            {
                strtok.nextToken();
            }
        }
        catch (Exception ex)
        {
            LOG.error("Exception on parse: " + ex + "\nData: " + line, ex);
        }

    }


    private String[] processId(String line)
    {

        // System.out.println("LL: " + line);

        StringTokenizer strtok = new StringTokenizer(line, "\t");

        strtok.nextToken();

        String o = strtok.nextToken();

        strtok = new StringTokenizer(o, " ");

        String[] ids = new String[2];
        ids[0] = strtok.nextToken();
        ids[1] = strtok.nextToken();

        return ids;

    }


    private void endReading()
    {
        this.outputWriter.setSubStatus(null);
        this.outputWriter.endOutput();
        this.outputWriter.setStatus(AbstractOutputWriter.STATUS_STOPPED_DEVICE);
        // System.out.println("Reading finished prematurely !");
    }


    private boolean isDeviceFinished()
    {
        return this.entries_current == this.entries_max;
    }


    /**
     * This is method for reading partitial data from device. All reading from actual device should be done from 
     * here. Reading can be done directly here, or event can be used to read data.
     */
    @Override
    public void readDeviceDataPartitial() throws PlugInBaseException
    {

    }


    /** 
     * This is method for reading configuration
     * 
     * @throws PlugInBaseException
     */
    @Override
    public void readConfiguration() throws PlugInBaseException
    {
    }


    /**
     * This is for reading device information. This should be used only if normal dump doesn't retrieve this
     * information (most dumps do). 
     * @throws PlugInBaseException
     */
    @Override
    public void readInfo() throws PlugInBaseException
    {
        try
        {
            this.outputWriter.setSubStatus(i18nControlAbstract.getMessage("READING_SERIAL_NR_SETTINGS"));
            this.outputWriter.setSpecialProgress(1);

            if (!this.startMessageToMeter())
                return;

            String data_back;

            this.sendMessageToMeter("1ID\003C1\r\n");
            data_back = readMessageFromMeter();
            // System.out.println("ID: " + data_back);

            String[] ids = this.processId(data_back);

            this.sendMessageToMeter("1GET_METER\003F0\r\n");
            data_back = readMessageFromMeter();
            // System.out.println("Get meter: " + data_back);

            if (data_back == null || data_back.charAt(0) == 0)
            {
                endReading();
            }

            // first we read device identification data
            DeviceIdentification di = this.outputWriter.getDeviceIdentification();

            di.device_serial_number = ids[0]; // this.readLineDebug();
            this.outputWriter.setSpecialProgress(2);
            di.device_hardware_version = ids[1]; // this.readLineDebug();
            this.outputWriter.setSpecialProgress(3);
            // this.readLineDebug();
            this.outputWriter.setSpecialProgress(4);
            this.entries_max = this.getMaxMemoryRecords(); // Integer.parseInt(this.readLineDebug());

            this.outputWriter.setDeviceIdentification(di);
            this.outputWriter.writeDeviceIdentification();

            this.outputWriter.setSpecialProgress(5);

        }
        catch (Exception ex)
        {
            LOG.error("Error reading info. Ex: " + ex, ex);
            throw new PlugInBaseException(ex);
        }

    }


    /**
     * Add BG Data
     * 
     * @param data data to add
     * @param adt atech date
     */
    public void addBGData(String data, ATechDate adt)
    {
        if (data == null || data.length() == 0)
            return;

        MeterValuesEntry mve = new MeterValuesEntry();
        // mve.setBgUnit(OutputUtil.BG_MGDL);

        mve.setBgValue(data, GlucoseUnitType.mg_dL);
        mve.setDateTimeObject(adt);

        this.outputWriter.writeData(mve);
        this.entries_current++;
        readingEntryStatus();
    }


    /**
     * Add Urine Data
     * 
     * @param data 
     * @param adt 
     * 
     */
    public void addUrineData(String data, ATechDate adt)
    {
        if (data == null || data.length() == 0)
            return;

        MeterValuesEntry mve = new MeterValuesEntry();

        mve.setDateTimeObject(adt);
        // mve.addSpecialEntry(MeterValuesEntrySpecial.SPECIAL_ENTRY_URINE_MMOLL,
        // DataAccessPlugInBase.Decimal1Format
        // .format(dataAccess.getBGValueByType(DataAccessPlugInBase.BG_MGDL,
        // DataAccessPlugInBase.BG_MMOL, data)));

        mve.setUrine(DailyValuesExtendedType.Urine_mmolL, dataAccess.getFormatedValue(
            dataAccess.getBGValueByType(GlucoseUnitType.mg_dL, GlucoseUnitType.mmol_L, Float.parseFloat(data)), 1));

        this.outputWriter.writeData(mve);
        this.entries_current++;
        readingEntryStatus();
    }


    protected void setDeviceStopped()
    {
        this.device_running = false;
        this.outputWriter.endOutput();
    }


    protected ATechDate getDateTime(String date, String time)
    {
        long dt = dataAccess.getLongValueFromString(date) * 10000L;
        String tm = ATDataAccessAbstract.replaceExpression(time, ":", "");
        dt += dataAccess.getLongValueFromString(tm);

        return tzu.getCorrectedDateTime(new ATechDate(ATechDate.FORMAT_DATE_AND_TIME_MIN, dt));
    }


    // private void

    private void readingEntryStatus()
    {
        float proc_read = this.entries_current * 1.0f / this.entries_max;
        float proc_total = 5 + 95 * proc_read;
        this.outputWriter.setSpecialProgress((int) proc_total); // .setSubStatus(sub_status)
    }


    /**
     * hasSpecialProgressStatus - in most cases we read data directly from device, in this case we have 
     *    normal progress status, but with some special devices we calculate progress through other means.
     * @return true is progress status is special
     */
    @Override
    public boolean hasSpecialProgressStatus()
    {
        return true;
    }


    /**
     * We don't use serial event for reading data, because process takes too long, we use serial event just 
     * to determine if device is stopped (interrupted) 
     */
    @Override
    public void serialEvent(SerialPortEvent event)
    {

        // Determine type of event.
        switch (event.getEventType())
        {

            // If break event append BREAK RECEIVED message.
            case SerialPortEvent.BI:
                System.out.println("recievied break");
                this.outputWriter.setStatus(AbstractOutputWriter.STATUS_STOPPED_DEVICE);
                // setDeviceStopped();
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
                System.out.println("Output Empty");
                break;
            case SerialPortEvent.PE:
                System.out.println("recievied pe");
                break;
            case SerialPortEvent.RI:
                System.out.println("recievied ri");
                break;
        }
    }


    /**
     * getCompanyId - Get Company Id 
     * 
     * @return id of company
     */
    public int getCompanyId()
    {
        return MeterDevicesIds.COMPANY_ABBOTT;
    }


    /**
     * Maximum of records that device can store
     */
    public int getMaxMemoryRecords()
    {
        return 450;
    }


    /** 
     * Get Device ClassName
     */
    public String getDeviceClassName()
    {
        return "ggc.meter.device.abbott.OptiumXceed";
    }


    /**
     * getDeviceId - Get Device Id 
     * 
     * @return id of device within company
     */
    public int getDeviceId()
    {
        return MeterDevicesIds.METER_ABBOTT_OPTIUM_XCEED;
    }


    /**
     * getIconName - Get Icon of meter
     * 
     * @return icon name
     */
    public String getIconName()
    {
        return "ab_optium_xceed.jpg";
    }


    /**
     * getName - Get Name of meter. 
     * 
     * @return name of meter
     */
    public String getName()
    {
        return "Optium Xceed";
    }


    /** 
     * getComment
     */
    public String getComment()
    {
        return null;
    }


    /** 
     * getImplementationStatus
     */
    public DeviceImplementationStatus getImplementationStatus()
    {
        return DeviceImplementationStatus.Done;
    }


    /** 
     * getInstructions
     */
    public String getInstructions()
    {
        return "INSTRUCTIONS_ABBOTT_OPTIUMXCEED";
    }


    /**
     * getInterfaceTypeForMeter - most meter devices, store just BG data, this use simple interface, but 
     *    there are some device which can store different kind of data (Ketones - Optium Xceed; Food, Insulin
     *    ... - OT Smart, etc), this devices require more extended data display. 
     * @return
     */
    @Override
    public int getInterfaceTypeForMeter()
    {
        return MeterInterface.METER_INTERFACE_EXTENDED;
    }


    public DownloadSupportType getDownloadSupportType()
    {
        return DownloadSupportType.DownloadData;
    }

}
