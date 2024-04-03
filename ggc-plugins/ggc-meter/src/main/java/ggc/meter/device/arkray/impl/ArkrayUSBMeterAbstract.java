package ggc.meter.device.arkray.impl;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.atech.utils.data.ATechDate;
import com.atech.utils.data.ATechDateType;

import ggc.meter.device.UsbMeterAbstract;
import ggc.meter.device.arkray.ArkrayMeterDataReader;
import ggc.meter.device.arkray.ArkrayUtil;
import ggc.plugin.data.progress.ProgressType;
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
 *  Filename:     ArkrayUSBMeterAbstract
 *  Description:  Data/device abstract reader for Arkray USB Devices
 *
 *  Author: Andy {andy@atech-software.com}
 */

public abstract class ArkrayUSBMeterAbstract extends UsbMeterAbstract
{

    private static final Logger LOG = LoggerFactory.getLogger(ArkraySerialMeterAbstract.class);

    protected int entryCount = 0;

    ArkrayMeterDataReader dataReader;
    Map<String, SimpleDateFormat> mapDateFormat = new HashMap<String, SimpleDateFormat>();


    public ArkrayUSBMeterAbstract(ArkrayMeterDataReader dataReader)
    {
        super(dataReader.getDataAccess(), dataReader.getOutputWriter(), dataReader.getDeviceDefinition());
        this.dataReader = dataReader;
        serialNumber = "GETSN";
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


    protected String getDateTimeDefinition()
    {
        return "yyyyMMddHHmm";
    }


    public void addToProgress() throws PlugInBaseException
    {
        this.dataReader.addToProgress(ProgressType.Dynamic, 1);
    }


    protected void checkIfSerialNumberWasRead() throws PlugInBaseException
    {
        ArkrayUtil.checkIfSerialNumberWasRead(this.serialNumber);
    }


    protected boolean checkIsControlEntry(String flag)
    {
        return ArkrayUtil.checkIsControlEntry(flag, this.deviceDefinition);
    }


    protected boolean checkDeleted(String flag)
    {
        return ArkrayUtil.checkDeleted(flag, deviceDefinition);
    }

}
