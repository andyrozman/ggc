package ggc.pump.device.dana;


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
 *  Filename:     DanaUtil  
 *  Description:  Dana Util 
 * 
 *  Author: Andy {andy@atech-software.com}
 */



// TODO: Auto-generated Javadoc
// OLD Implementation


// TODO: Auto-generated Javadoc
/**
 * The Class DanaUtil.
 */
public class DanaUtil
{
    private static int crc16(byte data, int crc)
    {
        int num = 0;
        num = ((crc >> 8) & 0xff) | (crc << 8);
        num ^= data & 0xff;
        num ^= ((num & 0xff) >> 4) & 0xfff;
        num ^= (num << 8) << 4;
        return (num ^ (((num & 0xff) << 5) | ((((num & 0xff) >> 3) & 0x1fff) << 8)));
    }

    
    /**
     * Creates the crc.
     * 
     * @param data the data
     * @param offset the offset
     * @param length the length
     * 
     * @return the int
     */
    public static int createCRC(byte[] data, int offset, int length)
    {
        int crc = 0;
        for (int i = 0; i < length; i++)
        {
            crc = crc16(data[offset + i], crc);
        }
        return crc;
    }
    
    
    
    
    
    /**
     * Convert byte to char.
     * 
     * @param data the data
     * 
     * @return the char[]
     */
    public static char[] convertByteToChar(byte[] data)
    {
        char[] chArray = new char[data.length];
        for (int i = 0; i < data.length; i++)
        {
            chArray[i] = (char) data[i];
        }
        return chArray;
    }

    /**
     * To hex char.
     * 
     * @param v the v
     * 
     * @return the char
     */
    public static char toHexChar(byte v)
    {
        switch ((v & 15))
        {
            case 0:
                return '0';

            case 1:
                return '1';

            case 2:
                return '2';

            case 3:
                return '3';

            case 4:
                return '4';

            case 5:
                return '5';

            case 6:
                return '6';

            case 7:
                return '7';

            case 8:
                return '8';

            case 9:
                return '9';

            case 10:
                return 'A';

            case 11:
                return 'B';

            case 12:
                return 'C';

            case 13:
                return 'D';

            case 14:
                return 'E';

            case 15:
                return 'F';
        }
        return '?';
    }

    /**
     * To hex string.
     * 
     * @param v the v
     * 
     * @return the string
     */
    public static String toHexString(byte v)
    {
        return ("" + toHexChar((byte) (v >> 4)) + toHexChar(v));
    }

    /**
     * To hex string.
     * 
     * @param v the v
     * 
     * @return the string
     */
    public static String toHexString(int v)
    {
        return "" + toHexChar((byte) ((v >> 12) & 15)) + toHexChar((byte) ((v >> 8) & 15)) +  toHexChar((byte) ((v >> 4) & 15)) + toHexChar((byte) (v & 15));
    }

    /**
     * To hex value.
     * 
     * @param data the data
     * 
     * @return the string
     */
    public static String toHexValue(byte[] data)
    {
        return toHexValue(data, 0, data.length);
    }

    /**
     * To hex value.
     * 
     * @param data the data
     * @param length the length
     * 
     * @return the string
     */
    public static String toHexValue(byte[] data, int length)
    {
        return toHexValue(data, 0, length);
    }

    /**
     * To hex value.
     * 
     * @param data the data
     * @param offset the offset
     * @param length the length
     * 
     * @return the string
     */
    public static String toHexValue(byte[] data, int offset, int length)
    {
        StringBuffer builder = new StringBuffer();
        for (int i = 0; i < length; i++)
        {
            if ((i % 4) == 0)
            {
                builder.append(" ");
            }
            if ((i % 0x10) == 0)
            {
                builder.append("\n");
            }
            builder.append(toHexString((byte) (data[offset + i] & 0xff)));
        }
        return builder.toString();
    }
    
    
    
    /**
     * Delay sync.
     */
    public static void delaySync()
    {
        delaySync(200);
    }

    /**
     * Delay sync.
     * 
     * @param i the i
     */
    public static void delaySync(int i)
    {
        try
        {
            Thread.sleep(i);
        }
        catch(Exception ex)
        {
            
        }
        
        
        /*
        try
        {
            long tickCount = System.currentTimeMillis();
            while ((Environment.TickCount - tickCount) < i)
            {
            }
        }
        catch (Exception exception)
        {
            throw exception;
        }*/
    }

    /**
     * Delay sync end.
     */
    public static void delaySyncEnd()
    {
        delaySync(0x3e8);
    }

    /**
     * Delay sync open.
     */
    public static void delaySyncOpen()
    {
        delaySync(0x7d0);
    }

    /**
     * Delay sync start.
     */
    public static void delaySyncStart()
    {
        delaySync(0x3e8);
    }
    
    
    
}
