package ggc.meter.device.ascensia;

import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.StringTokenizer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.atech.utils.ATDataAccessAbstract;
import com.atech.utils.data.TimeZoneUtil;

import ggc.meter.device.AbstractSerialMeter;
import ggc.meter.device.ascensia.impl.AscensiaDecoder;
import ggc.meter.manager.company.AscensiaBayer;
import ggc.meter.util.DataAccessMeter;
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
 * This class can be used as example on how to implement Serial meter driver
 * 
 * @author Andy
 *
 */

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
 *  Filename:     AscensiaMeter  
 *  Description:  Base Implementation for all Ascensia/Bayer meter devices
 * 
 *  Author: Andy {andy@atech-software.com}
 */

public abstract class AscensiaMeter extends AbstractSerialMeter
{

    protected TimeZoneUtil tzu = TimeZoneUtil.getInstance();
    private static final Logger LOG = LoggerFactory.getLogger(AscensiaMeter.class);

    boolean multiline = false;
    String multiline_body;

    // String end_string;
    String end_strings[] = null;
    String text_def[] = null;
    boolean device_running;
    private AscensiaDecoder decoder;


    /**
     * Constructor
     */
    public AscensiaMeter()
    {
    }


    /**
     * Constructor
     * 
     * @param cmp abstract device company instance
     */
    public AscensiaMeter(AbstractDeviceCompany cmp)
    {
        super(cmp);
    }


    /**
     * Constructor
     * 
     * @param portName
     * @param writer
     */
    public AscensiaMeter(String portName, OutputWriter writer)
    {
        this(portName, writer, DataAccessMeter.getInstance());
    }


    /**
     * Constructor
     * 
     * @param comm_parameters 
     * @param writer
     * @param da
     */
    public AscensiaMeter(String comm_parameters, OutputWriter writer, DataAccessPlugInBase da)
    {
        super(comm_parameters, writer, da);

        decoder = new AscensiaDecoder(writer);

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
            this.setSerialPort(comm_parameters);

            if (!this.open())
                // this.m_status = 1;
                return;

            this.outputWriter.writeHeader();

            // setting specific for this driver
            this.end_strings = new String[2];

            // ETX - End of Text
            end_strings[0] = Character.toString((char) 3);
            // EOT - End of Transmission
            end_strings[1] = Character.toString((char) 4);

            this.text_def = new String[3];
            // STX - Start of Text
            this.text_def[0] = Character.toString((char) 2);
            // ETX - Start of Text
            this.text_def[1] = Character.toString((char) 3);
            // EOL - Start of Text
            this.text_def[2] = Character.toString((char) 13);

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

            String line;

            while ((line = this.readLine()) != null && !isDeviceStopped(line))
            {
                sendToProcess(line);
                write(6);
            }

        }
        catch (Exception ex)
        {
            System.out.println("Exception: " + ex);
            ex.printStackTrace();

        }
        finally
        {
            super.close();
        }

    }


    private void sendToProcess(String text)
    {
        boolean stx = false;
        int stx_idx;
        boolean etx = false;
        int etx_idx;
        boolean eol = false;
        int eol_idx;

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
            decoder.decodeResultNonUsbMeters(input);
        }
        else if (input.contains("|Bayer"))
        {
            readDeviceIdAndSettings(input);
        }
    }


    protected void readDeviceIdAndSettings(String input)
    {
        input = input.substring(input.indexOf("Bayer"));

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

        decoder.decodeHeaderDeviceIdentification(devId);

        // readDeviceId(devId);
        readDateInformation(date);

        this.outputWriter.writeDeviceIdentification();

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


    public DeviceImplementationStatus getImplementationStatus()
    {
        return DeviceImplementationStatus.Done;
    }


    public DownloadSupportType getDownloadSupportType()
    {
        return DownloadSupportType.DownloadData;
    }

}
