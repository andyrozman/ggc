package ggc.meter.device.arkray.impl;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.atech.utils.data.ATechDate;

import ggc.core.data.defs.GlucoseUnitType;
import ggc.meter.data.GlucoseMeterMarkerDto;
import ggc.meter.defs.GlucoseMeterMarker;
import ggc.meter.defs.device.MeterDeviceDefinition;
import ggc.meter.device.SerialMeterAbstract;
import ggc.meter.device.arkray.ArkrayMeterDataReader;
import ggc.meter.device.arkray.ArkrayUtil;
import ggc.plugin.comm.cfg.SerialSettings;
import ggc.plugin.comm.cfg.SerialSettingsType;
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
 *  Filename:     ArkraySerialMeterAbstract
 *  Description:  Data/device abstract reader for Arkray Serial Devices
 *
 *  Author: Andy {andy@atech-software.com}
 */

public abstract class ArkraySerialMeterAbstract extends SerialMeterAbstract
{

    private static final Logger LOG = LoggerFactory.getLogger(ArkraySerialMeterAbstract.class);

    // protected int entryCount = 0;

    ArkrayMeterDataReader dataReader;


    public ArkraySerialMeterAbstract(ArkrayMeterDataReader dataReader)
    {
        super(dataReader.getDataAccess(), dataReader.getOutputWriter(), dataReader.getPortName(),
                dataReader.getDeviceDefinition());
        this.dataReader = dataReader;
        serialNumber = "GETSN";
    }


    protected abstract void preInitDevice() throws PlugInBaseException;


    public SerialSettings getSerialSettings()
    {
        return new SerialSettings(9600, SerialSettingsType.DataBits8, SerialSettingsType.StopBits2,
                SerialSettingsType.ParityNone);
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


    protected GlucoseUnitType getGlucoseUnitType() throws Exception
    {
        StringBuffer config = new StringBuffer();

        writeMessage("R|");
        char c;

        readUntilACKReceived();

        writeMessage("C|");

        while ((c = readChar()) != LF)
        {
            config.append(c);
        }

        return checkUnit(config.substring(2, 4));
    }


    private GlucoseUnitType checkUnit(String flag)
    {
        return ArkrayUtil.checkFlag(flag, 2) ? GlucoseUnitType.mmol_L : GlucoseUnitType.mg_dL;
    }

    protected boolean controlEntry = false;


    protected void decode(String line) throws PlugInBaseException
    {
        // System.out.println("line->" + line);

        String[] data = line.split("\\|");

        if (data[0].equals("H"))
        {
            processSerialNumberTypeH(data[4]);
        }
        else if (data[0].equals("D"))
        {
            processDataD(data[1]);
        }
        else if (data[0].equals("O"))
        {
            if (data.length > 2 && data[11].equals("Q"))
            {
                this.controlEntry = true;
            }
            else
            {
                this.controlEntry = false;
            }

        }
        else if (data[0].equals("R"))
        {
            processDataR(data);
        }
    }


    protected void processDataR(String[] data) throws PlugInBaseException
    {
        String[] subData = data[2].split("\\^");

        // e.id_str_ana = subData[3];
        float result = ArkrayUtil.getParsedResult(data[3]);

        GlucoseUnitType glucoseUnitType = data[4].toLowerCase().contains("mg") ? GlucoseUnitType.mg_dL
                : GlucoseUnitType.mmol_L;

        List<GlucoseMeterMarkerDto> glucoseMarkers = new ArrayList<GlucoseMeterMarkerDto>();

        if ((data[8] != null) && (data[8].length() > 0) && (data[8].endsWith("A")))
        {
            glucoseMarkers.add(GlucoseMeterMarkerDto.createMarker(GlucoseMeterMarker.PostMeal));
        }

        ATechDate date = null;

        if (data.length > 5)
        {
            // e.fl_out_range = data[5];
            date = parseATechDate(data[11]);
        }

        if (!this.controlEntry)
        {
            writeBGData(result, glucoseUnitType, date, glucoseMarkers);
        }

        addToProgress();

    }


    private ATechDate parseATechDate(String date)
    {
        return ArkrayUtil.parseATechDate(date, getDateTimeDefinition());
    }


    protected String getDateTimeDefinition()
    {
        return "yyyyMMddHHmm";
    }


    protected void checkIfSerialNumberWasRead() throws PlugInBaseException
    {
        ArkrayUtil.checkIfSerialNumberWasRead(this.serialNumber);
    }


    protected void processDataD(String data) throws PlugInBaseException
    {
        if ((deviceDefinition == MeterDeviceDefinition.ArkrayGT_1810) || //
                (deviceDefinition == MeterDeviceDefinition.ArkrayGT_1820) || //
                (deviceDefinition == MeterDeviceDefinition.ArkrayGlucoXMeter))
        {

            String meas_l = data.substring(0, 2);
            String meas_u = data.substring(2, 4);
            String minute = data.substring(4, 6);
            String hour = data.substring(6, 8);
            String day = data.substring(8, 10);
            String month = data.substring(10, 12);
            String year = data.substring(12, 14);
            String flag = data.substring(14, 16);

            if ((checkIsControlEntry(flag)) || (checkDeleted(flag)))
            {
                addToProgress();
                return;
            }

            ATechDate aTechDate = parseATechDate("20" + year + month + day + hour + minute);
            int result = Integer.parseInt(meas_u + meas_l, 16);

            List<GlucoseMeterMarkerDto> markers = null;

            if (checkPostMeal(flag))
            {
                markers = new ArrayList<GlucoseMeterMarkerDto>();
                markers.add(GlucoseMeterMarkerDto.createMarker(GlucoseMeterMarker.PostMeal));
            }

            if (!meas_u.equals("FF"))
            {
                writeBGData(result, glucoseUnitType, aTechDate, markers);
            }

            addToProgress();
        }
        else if (deviceDefinition == MeterDeviceDefinition.ArkrayGlucoCardMX)
        {
            String meas_l = data.substring(0, 2);
            String meas_u = data.substring(2, 4);
            String minute = data.substring(4, 6);
            String hour = data.substring(6, 8);
            String day = data.substring(8, 10);
            String month = data.substring(10, 12);
            String year = data.substring(12, 14);
            String flag1 = data.substring(14, 16);
            String flag2 = data.substring(16, 18);
            String elapsed = data.substring(18, 20);

            if ((checkIsControlEntry(flag1)) || (checkDeleted(flag1)))
            {
                addToProgress();
                return;
            }

            ATechDate aTechDate = parseATechDate("20" + year + month + day + hour + minute);

            List<GlucoseMeterMarkerDto> markers = checkAllFlags(flag2, Short.parseShort(elapsed, 16));
            int result = Integer.parseInt(meas_u + meas_l, 16);

            if (!meas_u.equals("FF"))
            {
                writeBGData(result, glucoseUnitType, aTechDate, markers);
            }

            addToProgress();
        }
        else
            throw new PlugInBaseException(PlugInExceptionType.DeviceUnexpectedResponse,
                    new Object[] { "entry type D" });

    }


    protected boolean checkDeleted(String flag)
    {
        return ArkrayUtil.checkDeleted(flag, deviceDefinition);
    }


    protected void processSerialNumberTypeH(String inputData)
    {
        String[] subData = inputData.split("\\^");

        serialNumber = subData[2].substring(subData[2].indexOf("-") + 1);

        if (serialNumber.startsWith("SN "))
        {
            serialNumber = serialNumber.substring(3);
        }

        setDeviceInformation(serialNumber);
    }


    protected boolean checkPostMeal(String flag)
    {
        String b_flag = Integer.toBinaryString(Integer.parseInt(flag, 16));
        return ((b_flag.length() > 4) && (b_flag.substring(b_flag.length() - 5, b_flag.length() - 4).equals("1")));
    }


    protected List<GlucoseMeterMarkerDto> checkAllFlags(String flag, Short elapsedAfterMeal)
    {

        List<GlucoseMeterMarkerDto> markers = new ArrayList<GlucoseMeterMarkerDto>();

        String binaryFlag = Integer.toBinaryString(Integer.parseInt(flag, 16));

        if (ArkrayUtil.checkFlagWithBinaryString(binaryFlag, 0))
        {
            markers.add(GlucoseMeterMarkerDto.createMarker(GlucoseMeterMarker.PreMeal));
        }

        if (ArkrayUtil.checkFlagWithBinaryString(binaryFlag, 1))
        {
            if ((elapsedAfterMeal == null) || (elapsedAfterMeal <= 0))
            {
                markers.add(GlucoseMeterMarkerDto.createMarker(GlucoseMeterMarker.PostMeal));
            }
            else
            {
                markers.add(GlucoseMeterMarkerDto.createMarker(GlucoseMeterMarker.AfterFoodWithTime,
                    elapsedAfterMeal.toString()));
            }
        }

        if (ArkrayUtil.checkFlagWithBinaryString(binaryFlag, 2))
        {
            markers.add(GlucoseMeterMarkerDto.createMarker(GlucoseMeterMarker.MealLevel1));
        }

        if (ArkrayUtil.checkFlagWithBinaryString(binaryFlag, 3))
        {
            markers.add(GlucoseMeterMarkerDto.createMarker(GlucoseMeterMarker.MealLevel2));
        }

        if (ArkrayUtil.checkFlagWithBinaryString(binaryFlag, 4))
        {
            markers.add(GlucoseMeterMarkerDto.createMarker(GlucoseMeterMarker.MealLevel3));
        }

        if (ArkrayUtil.checkFlagWithBinaryString(binaryFlag, 5))
        {
            markers.add(GlucoseMeterMarkerDto.createMarker(GlucoseMeterMarker.Activity));
        }

        if (ArkrayUtil.checkFlagWithBinaryString(binaryFlag, 6))
        {
            markers.add(GlucoseMeterMarkerDto.createMarker(GlucoseMeterMarker.LowGlucose));
        }

        return markers.size() == 0 ? null : markers;
    }


    protected boolean checkIsControlEntry(String flag)
    {
        return ArkrayUtil.checkIsControlEntry(flag, this.deviceDefinition);
    }


    // public void setDownloadCancel(boolean cancel)
    // {
    // this.downloadCanceled = cancel;
    // }
    //
    //
    // public boolean isDownloadCanceled()
    // {
    // return this.downloadCanceled;
    // }

    public void addToProgress() throws PlugInBaseException
    {
        this.dataReader.addToProgress(ProgressType.Dynamic, 1);
    }

}
