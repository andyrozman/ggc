package ggc.plugin.device.impl.minimed.comm.usb.contournext.data.message;

import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

/**
 * This class was taken from 600SeriesAndroidUploader project, which
 * is loacted at https://github.com/pazaan/600SeriesAndroidUploader.
 *
 * Original author: lgoedhart on 26/03/2016.
 *
 * Comment: this file has just minor changes
 */

public class MessageUtils
{

    public static byte oneByteSum(byte[] bytes)
    {
        byte sum = 0;

        for (byte b : bytes)
        {
            sum += (short) b;
        }

        return sum;
    }


    public static int CRC16CCITT(byte[] data, int initialValue, int polynomial, int bytesToCheck)
    {
        // From http://introcs.cs.princeton.edu/java/61data/CRC16CCITT.java
        int crc = initialValue;
        for (int c = 0; c < bytesToCheck; c++)
        {
            byte b = data[c];
            for (int i = 0; i < 8; i++)
            {
                boolean bit = ((b >> (7 - i) & 1) == 1);
                boolean c15 = ((crc >> 15 & 1) == 1);
                crc <<= 1;
                if (c15 ^ bit)
                    crc ^= polynomial;
            }
        }

        crc &= 0xffff;
        return crc;
    }


    public static byte[] hexStringToByteArray(String s)
    {
        int len = s.length();
        byte[] data = new byte[len / 2];
        for (int i = 0; i < len; i += 2)
        {
            data[i / 2] = (byte) ((Character.digit(s.charAt(i), 16) << 4) + Character.digit(s.charAt(i + 1), 16));
        }
        return data;
    }


    public static String byteArrayToHexString(byte[] in)
    {
        final StringBuilder builder = new StringBuilder();
        for (byte b : in)
        {
            builder.append(String.format("%02x", b));
        }
        return builder.toString();
    }


    public static Date decodeDateTime(long rtc, long offset)
    {
        TimeZone currentTz = Calendar.getInstance().getTimeZone();

        // Base time is midnight 1st Jan 2000 (GMT)
        long baseTime = 946684800;

        // The time from the pump represents epochTime in GMT, but we treat it
        // as if it were in our own timezone
        // We do this, because the pump does not have a concept of timezone
        // For example, if baseTime + rtc + offset was 1463137668, this would be
        // Fri, 13 May 2016 21:07:48 GMT.
        // However, the time the pump *means* is Fri, 13 May 2016 21:07:48 in
        // our own timezone
        long offsetFromUTC = currentTz.getOffset(Calendar.getInstance().getTimeInMillis());

        Date pumpDate = new Date(((baseTime + rtc + offset) * 1000) - offsetFromUTC);
        return pumpDate;
    }
}
