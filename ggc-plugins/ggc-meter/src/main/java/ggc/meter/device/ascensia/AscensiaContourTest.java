package ggc.meter.device.ascensia;

import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.StringTokenizer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.atech.utils.ATDataAccessAbstract;
import com.atech.utils.data.ATechDate;
import com.atech.utils.data.TimeZoneUtil;

import ggc.core.data.defs.GlucoseUnitType;
import ggc.meter.data.MeterValuesEntry;
import ggc.meter.device.AbstractSerialMeter;
import ggc.meter.manager.MeterDevicesIds;
import ggc.meter.manager.company.AscensiaBayer;
import ggc.meter.util.DataAccessMeter;
import ggc.plugin.device.DeviceIdentification;
import ggc.plugin.device.PlugInBaseException;
import ggc.plugin.manager.DeviceImplementationStatus;
import ggc.plugin.manager.company.AbstractDeviceCompany;
import ggc.plugin.output.AbstractOutputWriter;
import ggc.plugin.output.OutputWriter;
import ggc.plugin.protocol.SerialProtocol;
import ggc.plugin.util.DataAccessPlugInBase;
import gnu.io.SerialPort;
import gnu.io.SerialPortEvent;
import gnu.io.SerialPortEventListener;

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
 *  Filename:     AscensiaContour
 *  Description:  Support for Ascensia/Bayer Contour Meter
 * 
 *  Author: Andy {andy@atech-software.com}
 */

public class AscensiaContourTest extends AbstractSerialMeter implements SerialPortEventListener
{

    // protected I18nControl i18nControlAbstract = I18nControl.getInstance();

    protected TimeZoneUtil tzu = TimeZoneUtil.getInstance();

    private static final Logger LOG = LoggerFactory.getLogger(AscensiaContourTest.class);

    boolean multiline = false;
    String multiline_body;

    // String end_string;
    String end_strings[] = null;
    String text_def[] = null;
    boolean device_running;


    /**
     * Constructor 
     */
    public AscensiaContourTest()
    {
    }


    /**
     * Constructor 
     * 
     * @param cmp
     */
    public AscensiaContourTest(AbstractDeviceCompany cmp)
    {
        super(cmp);
    }


    /**
     * Constructor
     * 
     * @param portName
     * @param writer
     */
    public AscensiaContourTest(String portName, OutputWriter writer)
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
    public AscensiaContourTest(String portName, OutputWriter writer, DataAccessPlugInBase da)
    {
        // communcation settings for this meter(s)
        this.setCommunicationSettings(9600, SerialPort.DATABITS_8, SerialPort.STOPBITS_1, SerialPort.PARITY_NONE,
            SerialPort.FLOWCONTROL_NONE,
            SerialProtocol.SERIAL_EVENT_BREAK_INTERRUPT | SerialProtocol.SERIAL_EVENT_OUTPUT_EMPTY);

        // output writer, this is how data is returned (for testing new devices,
        // we can use Consol
        this.outputWriter = writer;
        this.outputWriter.getOutputUtil().setMaxMemoryRecords(this.getMaxMemoryRecords());

        // set meter type (this will be deprecated in future, but it's needed
        // for now
        this.setMeterType("Ascensia/Bayer", this.getName());

        // set device company (needed for now, will also be deprecated)
        this.setDeviceCompany(new AscensiaBayer());

        // settting serial port in com library
        try
        {
            this.setSerialPort(portName);

            if (!this.open())
                // this.m_status = 1;
                return;

            this.outputWriter.writeHeader();

            // this.serialPort.notifyOnOutputEmpty(true); // notify on empty for
            // stopping
            // this.serialPort.notifyOnBreakInterrupt(true); // notify on break
            // interrupt for stopping

            // setting specific for this driver
            this.end_strings = new String[2];
            end_strings[0] = new Character((char) 3).toString(); // ETX - End of
                                                                 // Text
            end_strings[1] = new Character((char) 4).toString(); // EOT - End of
                                                                 // Transmission
            // end_strings[2] = (new Character((char)23)).toString(); // ETB -
            // End of Text

            this.text_def = new String[3];
            this.text_def[0] = new Character((char) 2).toString(); // STX -
                                                                   // Start of
                                                                   // Text
            this.text_def[1] = new Character((char) 3).toString(); // ETX -
                                                                   // Start of
                                                                   // Text
            this.text_def[2] = new Character((char) 13).toString(); // EOL -
                                                                    // Start of
                                                                    // Text

        }
        catch (Exception ex)
        {
            LOG.error("Exception on create:" + ex, ex);
        }

    }


    // ************************************************
    // *** Device Implemented methods ***
    // ************************************************

    /**
     * readDeviceDataFull - This is method for reading data from device. All reading from actual device should 
     * be done from here. Reading can be done directly here, or event can be used to read data. Usage of events 
     * is discouraged because reading takes 3-4x more time.
     */
    public void readDeviceDataFull() throws PlugInBaseException
    {
        waitTime(2000);

        try
        {

            this.device_running = true;

            write(6); // ENQ
            LOG.debug("->ENQ");

            String line;

            while ((line = this.readLine()) != null && !isDeviceStopped(line))
            {
                sendToProcess(line);
                write(6);
                LOG.debug("->ENQ");
            }

        }
        catch (Exception ex)
        {
            System.out.println("Exception: " + ex);
            ex.printStackTrace();

        }

    }


    private void sendToProcess(String text)
    {
        boolean stx = false;
        int stx_idx = 0;
        boolean etx = false;
        int etx_idx = 0;
        boolean eol = false;
        int eol_idx = 0;

        // System.out.println("Send: " + text);

        if ((stx_idx = text.indexOf(this.text_def[0])) > -1)
        {
            // System.out.println("STX");
            stx = true;
        }

        if ((etx_idx = text.indexOf(this.text_def[1])) > -1)
        {
            // System.out.println("ETX");
            etx = true;
        }

        if ((eol_idx = text.indexOf(this.text_def[2])) > -1)
        {
            // System.out.println("EOL");
            eol = true;
        }

        if (stx)
        {

            if (etx || eol)
            {
                String t;
                stx_idx++;

                if (etx)
                {
                    t = text.substring(stx_idx, etx_idx);

                }
                else
                {
                    t = text.substring(stx_idx, eol_idx);
                }
                // System.out.println(t);
                this.processData(t);
            }
            else
            {
                // only start, multiline
                this.multiline = true;
                this.multiline_body = text;
            }
        }
        else
        // if ((stx) && (!etx) || (!eol))
        {
            if (etx || eol)
            {
                if (this.multiline)
                {
                    this.multiline_body += text;

                    String txt = this.multiline_body;

                    stx_idx = txt.indexOf(this.text_def[0]);
                    etx_idx = text.indexOf(this.text_def[1]);
                    eol_idx = text.indexOf(this.text_def[2]);

                    int end = 0;

                    if (etx_idx != -1)
                    {
                        end = etx_idx;
                    }
                    else
                    {
                        end = eol_idx;
                    }

                    String t = txt.substring(stx_idx, end);
                    // System.out.println("Multi: " + t);
                    this.processData(t);

                    // reset
                    this.multiline = false;
                    this.multiline_body = "";

                }
            }
            else
            {
                if (this.multiline)
                {
                    this.multiline_body += text;
                }
            }
        }

    }


    private boolean isDeviceStopped(String vals)
    {
        if (!this.device_running)
            return true;

        if (this.outputWriter.isReadingStopped())
            return true;

        // if (vals.contains(this.end_strings[0]))
        // System.out.println("ETX");

        if (vals.contains(this.end_strings[1]))
        {
            // System.out.println("EOT");
            this.outputWriter.endOutput();
            // System.out.println("EOT");
            return true;
        }

        return false;

    }


    /**
     * Set Device Stopped
     */
    public void setDeviceStopped()
    {
        this.device_running = false;
        this.outputWriter.endOutput();
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
                setDeviceStopped();
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


    protected void processData(String input)
    {
        input = ATDataAccessAbstract.replaceExpression(input, "||", "|_|");

        if (input.contains("|^^^Glucose|"))
        {
            readData(input);
        }
        else if (input.contains("|Bayer"))
        {
            readDeviceIdAndSettings(input);
        }
    }


    protected void readDeviceIdAndSettings(String input)
    {
        input = input.substring(input.indexOf("Bayer"));
        LOG.debug("<- Id:" + input);

        StringTokenizer strtok = new StringTokenizer(input, "|");

        String devId = strtok.nextToken();
        strtok.nextToken();
        strtok.nextToken();
        strtok.nextToken();
        strtok.nextToken();
        strtok.nextToken();
        strtok.nextToken();
        strtok.nextToken();
        strtok.nextToken();

        String date = strtok.nextToken();

        // System.out.println("Device:\n" + devId + "\nDate: " + date);
        // System.out.println("Data (" + strtok.countTokens() + "): " + input);

        readDeviceId(devId);
        readDateInformation(date);

        this.outputWriter.writeDeviceIdentification();

    }


    protected void readDeviceId(String input)
    {

        LOG.debug("<- Id:" + input);

        DeviceIdentification di = this.outputWriter.getDeviceIdentification();

        // System.out.println("readDeviceId: " + input);
        StringTokenizer strtok = new StringTokenizer(input, "^");

        String inf = "";

        String id = strtok.nextToken();
        String versions = strtok.nextToken();
        String serial = strtok.nextToken();

        inf += i18nControlAbstract.getMessage("PRODUCT_CODE") + ": ";

        String tmp;

        if (id.equals("Bayer6115") || id.equals("Bayer6116"))
        {
            // inf += "BREEZE Meter Family (";
            tmp = "Breeze Family (";
        }
        else if (id.equals("Bayer7150"))
        {
            tmp = "CONTOUR Meter Family (";
        }
        else if (id.equals("Bayer3950"))
        {
            tmp = "DEX Meter Family (";
        }
        else if (id.equals("Bayer3883"))
        {
            tmp = "ELITE XL Meter Family (";
        }
        else
        {
            tmp = "Unknown Meter Family (";
        }

        tmp += id;
        tmp += ")";

        di.device_identified = tmp;

        inf += tmp;
        inf += "\n";

        StringTokenizer strtok2 = new StringTokenizer(versions, "\\");

        di.deviceSoftwareVersion = strtok2.nextToken();
        di.deviceHardwareVersion = strtok2.nextToken();
        di.deviceSerialNumber = serial;

        inf += i18nControlAbstract.getMessage("SOFTWARE_VERSION") + ": " + di.deviceSoftwareVersion;
        inf += i18nControlAbstract.getMessage("\nEEPROM_VERSION") + ": " + di.deviceHardwareVersion;

        inf += i18nControlAbstract.getMessage("\nSERIAL_NUMBER") + ": " + serial;

        // this.m_info = inf;
        System.out.println("Info: " + inf);

    }


    protected void readDateInformation(String dt)
    {

        GregorianCalendar gc_meter = new GregorianCalendar();
        gc_meter.setTimeInMillis(Long.parseLong(dt));

        GregorianCalendar gc_curr = new GregorianCalendar();
        gc_curr.setTimeInMillis(System.currentTimeMillis());

        GregorianCalendar gc_comp = new GregorianCalendar();
        gc_comp.set(Calendar.DAY_OF_MONTH, gc_curr.get(Calendar.DAY_OF_MONTH));
        gc_comp.set(Calendar.MONTH, gc_curr.get(Calendar.MONTH));
        gc_comp.set(Calendar.YEAR, gc_curr.get(Calendar.YEAR));
        gc_comp.set(Calendar.HOUR_OF_DAY, gc_curr.get(Calendar.HOUR_OF_DAY));
        gc_comp.set(Calendar.MINUTE, gc_curr.get(Calendar.MINUTE));

        // long diff = gc_comp.getTimeInMillis() - gc_meter.getTimeInMillis();
        // this.m_time_difference = (-1) * (int)diff;

        // System.out.println("Computer Time: " + gc_comp + "\nMeter Time: " +
        // gc_meter + " Diff: " + this.m_time_difference);

    }

    boolean header_set = false;


    protected void readData(String input)
    {
        try
        {
            LOG.debug("<- Data:" + input);

            StringTokenizer strtok = new StringTokenizer(input, "|");

            boolean found = false;

            // we search for entry containing Glucose... (in case that data was
            // not
            // received entirely)
            while (!found && strtok.hasMoreElements())
            {
                String s = strtok.nextToken();
                if (s.equals("^^^Glucose"))
                {
                    found = true;
                }
            }

            if (!found)
                return;

            // System.out.println(input);

            MeterValuesEntry mve = new MeterValuesEntry();

            String val = strtok.nextToken();

            // System.out.println("val:" + val);

            // mve.setBgValue(val); // bg_value
            String unit = strtok.nextToken(); // unit mmol/L^x, mg/dL^x

            mve.addParameter("REF_RANGES", strtok.nextToken()); // Reference
                                                                // ranges (Dex
                                                                // Only)
            mve.addParameter("RES_ABNORMAL_FLAGS", strtok.nextToken()); // Result
                                                                        // abnormal
                                                                        // flags
                                                                        // (7)
            mve.addParameter("USER_MARKS", strtok.nextToken()); // User Marks
                                                                // (8)
            mve.addParameter("RES_STATUS_MARKER", strtok.nextToken()); // Result
                                                                       // status
                                                                       // marker
            strtok.nextToken(); // N/A
            strtok.nextToken(); // OperatorId (N/A)

            String time = strtok.nextToken(); // datetime

            mve.setDateTimeObject(tzu.getCorrectedDateTime(new ATechDate(Long.parseLong(time))));

            if (unit.startsWith("mg/dL"))
            {
                // mve.setBgUnit(OutputUtil.BG_MGDL);
                mve.setBgValue(val, GlucoseUnitType.mg_dL);
                // this.m_output.writeBGData(atd, bg_value, OutputUtil.BG_MGDL);
                // dv.setBG(DailyValuesRow.BG_MGDL, value);
            }
            else
            {
                // mve.setBgUnit(OutputUtil.BG_MMOL);
                mve.setBgValue(getCorrectDecimal(val), GlucoseUnitType.mmol_L);
                // this.m_output.writeBGData(atd, bg_value, OutputUtil.BG_MMOL);
                // dv.setBG(DailyValuesRow.BG_MMOLL, value);
            }

            this.outputWriter.writeData(mve);

        }
        catch (Exception ex)
        {
            System.out.println("Exception: " + ex);
            System.out.println("Entry: " + input);
            ex.printStackTrace();
        }

        // this.data.add(dv);

    }


    protected String getCorrectDecimal(String input)
    {
        float f = Float.parseFloat(input);
        return DataAccessPlugInBase.Decimal1Format.format(f).replace(',', '.');
    }


    // ************************************************
    // *** Test ***
    // ************************************************

    /**
     * test
     */
    @Override
    public void test()
    {
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
    }


    /**
     * getCompanyId - Get Company Id 
     * 
     * @return id of company
     */
    public int getCompanyId()
    {
        return MeterDevicesIds.COMPANY_ASCENSIA;
    }


    // ************************************************
    // *** Meter Identification Methods ***
    // ************************************************

    /**
     * getName - Get Name of meter. 
     * 
     * @return name of meter
     */
    public String getName()
    {
        return "Contour Test";
    }


    /**
     * getIconName - Get Icon of meter
     * 
     * @return icon name
     */
    public String getIconName()
    {
        return "ascensia_contour.png";
    }


    /**
     * getDeviceId - Get Device Id, within MgrCompany class 
     * Should be implemented by device class.
     * 
     * @return id of device within company
     */
    public int getDeviceId()
    {
        return MeterDevicesIds.METER_ASCENSIA_CONTOUR;
    }


    /**
     * getInstructions - get instructions for device
     * 
     * @return instructions for reading data 
     */
    public String getInstructions()
    {
        return "INSTRUCTIONS_ASCENSIA_CONTOUR";
    }


    /**
     * getComment - Get Comment for device 
     * 
     * @return comment or null
     */
    public String getComment()
    {
        return null;
    }


    /**
     * getImplementationStatus - Get implementation status 
     * 
     * @return implementation status as number
     * @see ggc.plugin.manager.DeviceImplementationStatus
     */

    public DeviceImplementationStatus getImplementationStatus()
    {
        return DeviceImplementationStatus.NotAvailable;
    }


    /** 
     * Get Device ClassName
     */
    public String getDeviceClassName()
    {
        return "ggc.meter.device.ascensia.AscensiaContourTest";
    }


    /**
     * Maximum of records that device can store
     */
    public int getMaxMemoryRecords()
    {
        return 480;
    }

}
