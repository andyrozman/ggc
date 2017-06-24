package ggc.meter.device.menarini.impl;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.atech.utils.data.ATechDate;
import com.atech.utils.data.ATechDateType;

import ggc.meter.device.SerialMeterAbstract;
import ggc.meter.device.menarini.MeanriniMeterDataReader;
import ggc.plugin.comm.cfg.SerialSettings;
import ggc.plugin.data.enums.PlugInExceptionType;
import ggc.plugin.data.progress.ProgressType;
import ggc.plugin.device.DeviceIdentification;
import ggc.plugin.device.PlugInBaseException;

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
 *  Filename:     AscensiaBreeze2
 *  Description:  Data/device reader for Arkray GlucoCard
 *
 *  Author: Andy {andy@atech-software.com}
 */

public abstract class MenariniSerialMeterAbstract extends SerialMeterAbstract
{

    private static final Logger LOG = LoggerFactory.getLogger(MenariniSerialMeterAbstract.class);

    static final char ACK = '\006';

    protected String serialNumber;
    protected int entryCount = 0;
    protected String dataLine;
    MeanriniMeterDataReader dataReader;
    Map<String, SimpleDateFormat> mapDateFormat = new HashMap<String, SimpleDateFormat>();


    public MenariniSerialMeterAbstract(MeanriniMeterDataReader dataReader)
    {
        super(dataReader.getDataAccess(), dataReader.getOutputWriter(), dataReader.getPortName(),
                dataReader.getDeviceDefinition());
        this.dataReader = dataReader;
    }


    protected abstract void preInitDevice() throws PlugInBaseException;


    public abstract void readDeviceData() throws PlugInBaseException;


    public SerialSettings getSerialSettings()
    {
        return new SerialSettings();
    }


    public void write(int entry) throws PlugInBaseException
    {
        this.communicationHandler.write(entry);
        this.communicationHandler.flush();
    }


    protected void writeMessage(String message) throws PlugInBaseException
    {
        char[] messageChar = message.toCharArray();
        for (int i = 0; i < messageChar.length; i++)
        {
            this.communicationHandler.write(messageChar[i]);
        }
        this.communicationHandler.flush();
    }


    public void setDeviceInformation(String serialNumber)
    {
        DeviceIdentification deviceIdentification = this.outputWriter.getDeviceIdentification();

        deviceIdentification.device_serial_number = serialNumber;
        deviceIdentification.device_selected = this.deviceDefinition.getDeviceName();
        deviceIdentification.company = this.deviceDefinition.getDeviceCompany().getName();

        this.outputWriter.setDeviceIdentification(deviceIdentification);
        this.outputWriter.writeDeviceIdentification();
    }


    protected byte readByte() throws PlugInBaseException
    {
        int rv = -1;
        try
        {
            rv = this.communicationHandler.read();
            LOG.debug("Byte read: " + rv);
        }
        catch (Exception ex)
        {
            LOG.error("Error on readByte: " + ex, ex);
        }

        if (rv == -1)
        {
            if (this.active)
            {
                throw new PlugInBaseException("Serial port timeout on " + this.deviceDefinition.getDeviceName());
            }

            throw new PlugInBaseException(PlugInExceptionType.TimeoutReadingData);
        }
        return (byte) rv;
    }


    protected SimpleDateFormat getSimpleDateFormat()
    {
        if (mapDateFormat.containsKey(getDateTimeDefinition()))
        {
            return mapDateFormat.get(getDateTimeDefinition());
        }
        else
        {
            SimpleDateFormat sdf = new SimpleDateFormat(getDateTimeDefinition());
            mapDateFormat.put(getDateTimeDefinition(), sdf);

            return sdf;
        }
    }


    protected abstract String getDateTimeDefinition();


    protected Date parseDate(String data)
    {

        Date dt = null;
        try
        {
            dt = new Date(this.getSimpleDateFormat().parse(data).getTime());
        }
        catch (Exception ex)
        {
            LOG.error("Error parsing date: " + ex.getMessage(), ex);
        }
        return dt;
    }


    protected ATechDate parseATechDate(String data)
    {
        try
        {
            Date dt = new Date(this.getSimpleDateFormat().parse(data).getTime());

            return new ATechDate(dt.getDay(), dt.getMonth(), dt.getYear(), dt.getHours(), dt.getMinutes(),
                    ATechDateType.DateAndTimeMin);
        }
        catch (Exception ex)
        {
            LOG.error("Error parsing date: " + ex.getMessage(), ex);
        }

        return null;
    }


    public void addToProgress() throws PlugInBaseException
    {
        this.dataReader.addToProgress(ProgressType.Dynamic, 1);
    }

}
