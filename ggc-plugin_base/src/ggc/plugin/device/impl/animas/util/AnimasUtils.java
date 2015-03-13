package ggc.plugin.device.impl.animas.util;

import com.atech.utils.data.ATechDate;
import ggc.plugin.data.enums.PlugInExceptionType;
import ggc.plugin.device.PlugInBaseException;
import ggc.plugin.device.impl.animas.data.AnimasDeviceData;
import ggc.plugin.device.impl.animas.enums.AnimasDataType;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.List;

import ggc.plugin.util.DataAccessPlugInBase;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.atech.utils.data.ShortUtils;

/**
 *  Application:   GGC - GNU Gluco Control
 *  Plug-in:       Pump Tool (support for Pump devices)
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
 *  Filename:     AnimasIR2020
 *  Description:  Animas IR 2020 implementation
 *
 *  Author: Andy Rozman {andy@atech-software.com}
 */

public class AnimasUtils
{
    private static Log log = LogFactory.getLog(AnimasUtils.class);
    private static ShortUtils shortUtils = new ShortUtils();
    static public HashMap<AnimasDataType, Integer> dataTypeProcessed = new HashMap<AnimasDataType, Integer>();
    static DataAccessPlugInBase dataAccessPlugInBase;
    static AnimasDeviceData deviceData;


    public static void sleepInMs(long ms)
    {
        try
        {
            Thread.sleep(ms);
        }
        catch (InterruptedException e)
        {}
    }


    public static void setDataAccessInstance(DataAccessPlugInBase dataAccessPlugInBase)
    {
        AnimasUtils.dataAccessPlugInBase = dataAccessPlugInBase;
    }


    public static String short2hex(short c)
    {
        String ms = String.format("%2x", new Object[] { Short.valueOf(c) });
        if (ms.charAt(0) == ' ')
        {
            char[] data = { '0', ms.charAt(1) };

            ms = new String(data);
        }
        return ms;
    }


    public static String short2decimal(short c, int len)
    {
        String ms = String.format("%" + String.valueOf(len) + "d", new Object[] { Short.valueOf(c) });

        return ms;
    }


    public static void clearDataTypesProcessed()
    {
        dataTypeProcessed.clear();
    }


    public static void addDataTypeProcessed(AnimasDataType dataType)
    {
        if (!dataTypeProcessed.containsKey(dataType))
        {
            dataTypeProcessed.put(dataType, 1);
        }
    }


    public static boolean wasDataTypeProcessed(AnimasDataType dataType)
    {
        return dataTypeProcessed.containsKey(dataType);
    }


    public static short getUnsignedShort(short data)
    {
        if (data < 0)
        {
            data += 256;
        }

        return data;
    }


    public static short getUnsignedShort(int data)
    {
        if (data < 0)
        {
            data += 256;
        }

        return (short) data;
    }


    public static void debugHexData(boolean debugFlag, short[] data, int dataLength, String logFormater, Log log)
    {
        if (debugFlag)
        {
            StringBuffer sb = new StringBuffer();
            StringBuffer sbNumeric = new StringBuffer();
            for (int i = 0; i < dataLength; i++)
            {
                sb.append(AnimasUtils.short2hex(data[i]));
                sbNumeric.append(data[i]);
                sb.append(" ");
                sbNumeric.append(" ");
            }

            log.debug(String.format(logFormater, sb.toString(), sbNumeric.toString()));
        }
    }


    public static void debugHexData(boolean debugFlag, short[] data, int dataLength, Log log)
    {
        debugHexData(debugFlag, data, dataLength, "%s [%s]", log);
    }


    public static void debugHexData(boolean debugFlag, List<Short> data, int dataLength, String logFormater, Log log)
    {
        if (debugFlag)
        {
            StringBuffer sb = new StringBuffer();
            StringBuffer sbNumeric = new StringBuffer();
            for (int i = 0; i < dataLength; i++)
            {
                sb.append(AnimasUtils.short2hex(data.get(i)));
                sbNumeric.append(data.get(i));
                sb.append(" ");
                sbNumeric.append(" ");
            }

            log.debug(String.format(logFormater, sb.toString(), sbNumeric.toString()));
        }
    }


    public static void debugHexData(boolean debugFlag, List<Short> data, int dataLength, Log log)
    {
        debugHexData(debugFlag, data, dataLength, "%s [%s]", log);
    }


    public static void debugHexData(boolean debugFlag, char[] data, int dataLength, String logFormater, Log log)
    {
        if (debugFlag)
        {
            StringBuffer sb = new StringBuffer();
            StringBuffer sbNumeric = new StringBuffer();
            for (int i = 0; i < dataLength; i++)
            {
                sb.append(AnimasUtils.short2hex((short) data[i]));
                sbNumeric.append(data[i]);
                sb.append(" ");
                sbNumeric.append(" ");
            }

            log.debug(String.format(logFormater, sb.toString(), sbNumeric.toString()));
        }
    }


    public static boolean checkIfUserRelevantExceptionIsThrownNoRethrow(PlugInBaseException ex)
    {
        try
        {
            return checkIfUserRelevantExceptionIsThrown(ex, false);
        }
        catch (PlugInBaseException ex1)
        {
            return false;
        }
    }


    public static boolean checkIfUserRelevantExceptionIsThrown(PlugInBaseException ex, boolean rethrow) throws PlugInBaseException
    {
        if (ex.getExceptionType() != PlugInExceptionType.CommunicationPortClosed)
        {
            if (rethrow)
            {
                throw ex;
            }
            else
            {
                return true;
            }
        }

        return false;
    }


    public static short[] calculateFletcher16(char[] chars, int length)
    {
        int sum1 = 0;
        int sum2 = 0;
        for (int i = 0; i < length; i++)
        {
            sum1 += chars[i];
            sum1 %= 255;
            sum2 += sum1;
            sum2 %= 255;
        }

        short[] checks = new short[2];

        checks[0] = (short) (255 - ((sum1 + sum2) % 255));
        checks[1] = (short) (255 - ((sum1 + checks[0]) % 255));

        return checks;
    }


    public static long createLongValueThroughMoreBits(short... bits)
    {
        long value = 0L;

        for (int i = 0; i < bits.length; i++)
        {
            value += bits[i] * Math.pow(256, i);
        }

        return value;
    }


    public static int createIntValueThroughMoreBits(short... bits)
    {
        return (int) createLongValueThroughMoreBits(bits);
    }


    public static BigDecimal createBigDecimalValueThroughMoreBits(short... bits)
    {
        return new BigDecimal(createLongValueThroughMoreBits(bits));
    }


    public static String getDecimalValueString(float value, int places, int decimalPlaces)
    {
        return dataAccessPlugInBase.getDecimalHandler(). //
                getDecimalNumberFormatedWithDot(value, places, decimalPlaces);
    }


    public static ATechDate calculateTimeFromTimeSet(int timeSet)
    {
        int timeMin = timeSet * 30;

        int hour = timeMin / 60;

        long timeFull = hour * 100L + timeMin % 60;

        return new ATechDate(ATechDate.FORMAT_TIME_ONLY_MIN, timeFull);
    }


    public static void setAnimasData(AnimasDeviceData deviceData_)
    {
        deviceData = deviceData_;
    }


    public static AnimasDeviceData getAnimasData()
    {
        return deviceData;
    }

}
