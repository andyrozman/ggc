package ggc.meter.device.arkray;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.atech.utils.data.ATechDate;
import com.atech.utils.data.ATechDateType;

import ggc.meter.defs.device.MeterDeviceDefinition;
import ggc.plugin.data.enums.PlugInExceptionType;
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
 *  Filename:     ArkrayUtil
 *  Description:  Arkray Util
 *
 *  Author: Andy {andy@atech-software.com}
 */

public class ArkrayUtil
{

    private static final Logger LOG = LoggerFactory.getLogger(ArkrayUtil.class);

    private static Map<String, SimpleDateFormat> mapDateFormat = new HashMap<String, SimpleDateFormat>();


    public static void checkIfSerialNumberWasRead(String serialNumber) throws PlugInBaseException
    {
        if (serialNumber.equals("GETSN"))
        {
            throw new PlugInBaseException(PlugInExceptionType.DeviceSerialNumberCouldNotBeRead);
        }
    }


    public static float getParsedResult(String result)
    {
        return Float.parseFloat(result.replace(',', '.'));
    }


    public static boolean checkDeleted(String flag, MeterDeviceDefinition deviceDefinition)
    {
        if (deviceDefinition.equals(MeterDeviceDefinition.ArkrayGlucoCardSM))
        {
            String binaryFlag = Integer.toBinaryString(Integer.parseInt(flag, 16));
            return checkFlagWithBinaryString(binaryFlag, 1) || checkFlagWithBinaryString(binaryFlag, 2);
        }
        else
            return checkFlag(flag, 3);
    }


    public static boolean checkIsControlEntry(String flag, MeterDeviceDefinition deviceDefinition)
    {
        if (deviceDefinition.equals(MeterDeviceDefinition.ArkrayGlucoCardSM))
            return checkFlag(flag, 0);
        else
            return checkFlag(flag, 2);
    }


    public static boolean checkFlag(String flag, int place)
    {
        String b_flag = Integer.toBinaryString(Integer.parseInt(flag, 16));

        return checkFlagWithBinaryString(b_flag, place);
    }


    public static boolean checkFlagWithBinaryString(String binaryFlag, int place)
    {
        if (place == 0)
        {
            return ((binaryFlag.length() > 0) && (binaryFlag.substring(binaryFlag.length() - 1).equals("1")));
        }
        else
        {
            return ((binaryFlag.length() > place) && (binaryFlag
                    .substring(binaryFlag.length() - (place + 1), binaryFlag.length() - place).equals("1")));
        }

    }


    public static SimpleDateFormat getSimpleDateFormat(String datePattern)
    {
        if (mapDateFormat.containsKey(datePattern))
        {
            return mapDateFormat.get(datePattern);
        }
        else
        {
            SimpleDateFormat sdf = new SimpleDateFormat(datePattern);
            mapDateFormat.put(datePattern, sdf);

            return sdf;
        }
    }


    public static Date parseDate(String data, String datePattern)
    {
        Date dt = null;
        try
        {
            dt = new Date(getSimpleDateFormat(datePattern).parse(data).getTime());
        }
        catch (Exception ex)
        {
            LOG.error("Error parsing date: " + ex.getMessage(), ex);
        }
        return dt;
    }


    public static ATechDate parseATechDate(String date, String datePattern)
    {
        try
        {
            Date dt = new Date(getSimpleDateFormat(datePattern).parse(date).getTime());

            return new ATechDate(dt.getDay(), dt.getMonth(), dt.getYear(), dt.getHours(), dt.getMinutes(),
                    ATechDateType.DateAndTimeMin);
        }
        catch (Exception ex)
        {
            LOG.error("Error parsing date: " + ex.getMessage(), ex);
        }

        return null;
    }

}
