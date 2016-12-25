package ggc.meter.device.abbott;

import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.atech.utils.data.ATechDate;
import com.atech.utils.data.TimeZoneUtil;

import ggc.core.data.defs.GlucoseUnitType;
import ggc.meter.data.MeterValuesEntry;
import ggc.meter.device.AbstractSerialMeter;
import ggc.meter.manager.MeterDevicesIds;
import ggc.meter.manager.company.Abbott;
import ggc.meter.util.DataAccessMeter;
import ggc.plugin.device.DeviceIdentification;
import ggc.plugin.device.DownloadSupportType;
import ggc.plugin.device.PlugInBaseException;
import ggc.plugin.manager.DeviceImplementationStatus;
import ggc.plugin.manager.company.AbstractDeviceCompany;
import ggc.plugin.output.AbstractOutputWriter;
import ggc.plugin.output.ConsoleOutputWriter;
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
 *  Filename:     FreeStyleMeter.java
 *  Description:  Class for FreeStyle Meter.
 *
 *  Author: Andy {andy@atech-software.com} - Base file, with approximate device reading
 *  Author: Ophir Setter {ophir.setter@gmail.com} - Testing and final changes for device reading
 */

// while basic OT ascii protocol is implemented this file is still unclean and
// we are waiting to get
// more of old protocols, before we do finishing touches...
// so far we are also missing few pictures and ALL instructions for meters

public abstract class FreestyleMeter extends AbstractSerialMeter
{

    private static final Logger LOG = LoggerFactory.getLogger(FreestyleMeter.class);

    protected boolean device_running = true;

    protected TimeZoneUtil tzu = TimeZoneUtil.getInstance();
    // public int meter_type = 20000;
    private int entries_max = 0;
    private int entries_current = 0;
    private int reading_status = 0;


    // private int info_tokens;
    // private String date_order;

    /**
     * Constructor
     */
    public FreestyleMeter()
    {
    }


    /**
     * Constructor for device manager
     *
     * @param cmp
     */
    public FreestyleMeter(AbstractDeviceCompany cmp)
    {
        super(cmp);
    }


    /**
     * Constructor
     *
     * @param portName
     * @param writer
     */
    public FreestyleMeter(String portName, OutputWriter writer)
    {
        this(portName, writer, DataAccessMeter.getInstance());
    }


    /**
     * Constructor
     *
     * @param portName
     * @param writer
     * @param da
     */
    public FreestyleMeter(String portName, OutputWriter writer, DataAccessPlugInBase da)
    {
        super(portName, writer, da);

        this.setCommunicationSettings(19200, SerialPort.DATABITS_8, SerialPort.STOPBITS_2, SerialPort.PARITY_NONE,
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
            LOG.error("Exception on create:" + ex, ex);
            // System.out.println("OneTouchMeter: Error connecting !\nException:
            // "
            // + ex);
            // ex.printStackTrace();
        }

        /*
         * if (this.getDeviceId()==OneTouchMeter.METER_LIFESCAN_ONE_TOUCH_ULTRA)
         * {
         * this.info_tokens = 3;
         * this.date_order = "MDY";
         * }
         * else
         * {
         * this.info_tokens = 8;
         * }
         */

    }


    /**
     * getComment
     */
    public String getComment()
    {
        return null;
    }


    // DO
    /**
     * getImplementationStatus
     */
    public DeviceImplementationStatus getImplementationStatus()
    {
        return DeviceImplementationStatus.Testing;
    }


    /**
     * getInstructions
     */
    public String getInstructions()
    {
        return null;
    }


    /**
     * readDeviceDataFull
     */
    public void readDeviceDataFull() throws PlugInBaseException
    {

        try
        {
            write("mem".getBytes());
            String line;

            readInfo();

            /*
             * while((line=this.readLine())==null)
             * {
             * System.out.println("Serial Number1: " + line);
             * }
             */

            // System.out.println("Serial Number2: " + line);
            // System.out.println("Serial Number: " + this.readLine());
            // System.out.println("Serial Number: " + this.readLine());

            while ((line = this.readLine()) != null && !isDeviceStopped(line))
            {
                line = line.trim();

                processBGData(line);

                // if (line==null)
                // break;
            }

            this.outputWriter.setSpecialProgress(100);
            this.outputWriter.setSubStatus(null);

        }
        catch (Exception ex)
        {
            System.out.println("Exception: " + ex);
            ex.printStackTrace();

        }

        if (this.isDeviceFinished())
        {
            this.outputWriter.endOutput();
        }

        // this.outputWriter.setStatus(100);
        System.out.println("Reading finished");
        super.close();

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

            // first we read device identification data
            DeviceIdentification di = this.outputWriter.getDeviceIdentification();

            this.readLineDebug();
            di.device_serial_number = this.readLineDebug();
            this.outputWriter.setSpecialProgress(2);
            di.device_hardware_version = this.readLineDebug();
            this.outputWriter.setSpecialProgress(3);
            this.readLineDebug();
            this.outputWriter.setSpecialProgress(4);
            String entries_max_string = this.readLineDebug().trim();
            this.entries_max = Integer.parseInt(entries_max_string);

            this.outputWriter.setDeviceIdentification(di);
            this.outputWriter.writeDeviceIdentification();
            this.outputWriter.setSpecialProgress(5);
        }
        catch (IOException ex)
        {
            throw new PlugInBaseException(ex);
        }

    }


    protected String readLineDebug() throws IOException
    {
        String rdl = this.readLine();
        LOG.debug(rdl);

        return rdl;
    }


    private boolean isDeviceStopped(String vals)
    {
        if (vals == null || this.reading_status == 1 && vals.length() == 0 || !this.device_running
                || this.outputWriter.isReadingStopped())
            return true;

        return false;
    }


    /**
     * Process BG Data
     *
     * @param entry
     */
    public void processBGData(String entry)
    {
        if (entry == null || entry.length() == 0)
            return;

        if (entry.contains("END"))
        {
            this.device_running = false;
            this.outputWriter.setReadingStop();
            return;
        }

        MeterValuesEntry mve = new MeterValuesEntry();
        // mve.setBgUnit(OutputUtil.BG_MGDL);

        // 227 Oct 11 2006 01:38 17 0x00
        String BGString = entry.substring(0, 5);

        if (BGString.contains("HI"))
        {
            mve.setBgValue("500", GlucoseUnitType.mg_dL);
            mve.addParameter("RESULT", "High");
        }
        else
        {
            mve.setBgValue(BGString.trim(), GlucoseUnitType.mg_dL);
        }

        String timeString = entry.substring(5, 23);
        mve.setDateTimeObject(getDateTime(timeString));

        this.outputWriter.writeData(mve);
        this.entries_current++;
        readingEntryStatus();
    }


    protected void setDeviceStopped()
    {
        this.device_running = false;
        this.outputWriter.endOutput();
    }


    protected String getParameterValue(String val)
    {
        String d = val.substring(1, val.length() - 1);
        return d.trim();
    }

    private static String months_en[] = { "", "Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Oct",
                                          "Nov", "Dec" };


    protected ATechDate getDateTime(String datetime)
    {
        // "mm/dd/yy","hh:mm:30 "
        // Oct 11 2006 01:38
        ATechDate dt = new ATechDate(ATechDate.FORMAT_DATE_AND_TIME_MIN);

        // dt.day_of_month = Integer.parseInt(datetime.substring(6, 8));
        String mnth = datetime.substring(0, 3);

        // dt.month =
        dt.dayOfMonth = Integer.parseInt(datetime.substring(5, 7));
        dt.year = Integer.parseInt(datetime.substring(8, 12));
        dt.hourOfDay = Integer.parseInt(datetime.substring(13, 15));
        dt.minute = Integer.parseInt(datetime.substring(16, 18));

        for (int i = 0; i < FreestyleMeter.months_en.length; i++)
        {
            if (mnth.equals(FreestyleMeter.months_en[i]))
            {
                dt.month = i;
                break;
            }

        }

        return dt;

        /*
         * System.out.println("Month: '" + datetime.substring(0, 3) + "'");
         * System.out.println("Day: '" + datetime.substring(5, 7)+ "'");
         * System.out.println("Year: '" + datetime.substring(8, 12)+ "'");
         * System.out.println("Hour: '" + datetime.substring(13, 15)+ "'");
         * System.out.println("Year: '" + datetime.substring(16, 18)+ "'");
         */

    }


    // private void

    private void readingEntryStatus()
    {
        float proc_read = this.entries_current * 1.0f / this.entries_max;

        float proc_total = 5 + 95 * proc_read;

        // System.out.println("proc_read: " + proc_read + ", proc_total: " +
        // proc_total);

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
     * Returns short name for meter (for example OT Ultra, would return "Ultra")
     *
     * @return short name of meter
     */
    // public abstract String getShortName();

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
     * @param args
     */
    public static void main(String args[])
    {
        /*
         * //Oct 11 2006 01:38
         * Freestyle fm = new Freestyle();
         * ATechDate atd = fm.getDateTime("Oct  11 2006 01:38");
         * System.out.println(atd.getDateString() + " " + atd.getTimeString());
         */

        Freestyle fm = new Freestyle();
        fm.outputWriter = new ConsoleOutputWriter();

        String data[] = { "093  May  30 2005 00:46 16 0x01", "105  May  30 2005 00:42 16 0x00",
                          "085  May  29 2005 23:52 16 0x00", "073  May  29 2005 21:13 16 0x00",
                          "091  May  29 2005 21:11 16 0x01" };

        for (String element : data)
        {
            fm.processBGData(element);
        }

    }


    public DownloadSupportType getDownloadSupportType()
    {
        return DownloadSupportType.DownloadData;
    }

}
