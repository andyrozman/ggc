package ggc.pump.device.minimed.data;

import ggc.core.util.GGCLanguageManagerRunner;
import ggc.plugin.device.impl.minimed.MinimedDeviceUtil;
import ggc.plugin.device.impl.minimed.comm.MinimedComm_ComLink;
import ggc.pump.device.minimed.comm.MinimedReply;
import ggc.pump.util.DataAccessPump;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.StringTokenizer;

import com.atech.i18n.mgr.LanguageManager;
import com.atech.utils.data.HexUtils;

public class TestReturnData
{
    MinimedDeviceUtil mdu;
    MinimedComm_ComLink mcl;

    public TestReturnData()
    {
        mdu = MinimedDeviceUtil.createInstance(
            DataAccessPump.createInstance(new LanguageManager(new GGCLanguageManagerRunner())), null);
        mcl = new MinimedComm_ComLink(null);
        mdu.setCombinedPort("2;COM10;316551");
    }

    public void decodeTextLine(String values)
    {
        int arr[] = makeIntArray(values);

        // FIXME: Problem Checkin
        int arr_dec[] = null; // mcl.decode(arr);

        hu.showIntArrayHex(arr_dec);

        MinimedReply mr = new MinimedReply(arr_dec);

        System.out.println(mr);

        for (int i = 0; i < 55; i++)
        {
            decodeCurrentTimeStamp(mr.msg_body, i);
        }

        /*
         * a7 31 65 51 80 01
         * ea
         * A7 31 65 51 80 02
         * ea
         */
    }

    HexUtils hu = new HexUtils();

    public int[] makeIntArray(String line)
    {
        StringTokenizer strtok = new StringTokenizer(line.trim(), " ");

        ArrayList<Integer> list = new ArrayList<Integer>();
        int arr[] = new int[strtok.countTokens()];
        int i = 0;

        while (strtok.hasMoreTokens())
        {
            arr[i] = getIntFromHexString(strtok.nextToken());
            i++;
            // list.add(getIntFromHexString(strtok.nextToken()));
        }

        return arr;
    }

    public byte[] makeByteArray(String line)
    {
        StringTokenizer strtok = new StringTokenizer(line.trim(), " ");

        // ArrayList<Integer> list = new ArrayList<Integer>();
        byte arr[] = new byte[strtok.countTokens()];
        int i = 0;

        while (strtok.hasMoreTokens())
        {
            arr[i] = getByteFromHexString(strtok.nextToken());
            i++;
            // list.add(getIntFromHexString(strtok.nextToken()));
        }

        return arr;
    }

    public short[] makeShortArray(String line)
    {
        StringTokenizer strtok = new StringTokenizer(line.trim(), " ");

        // ArrayList<Integer> list = new ArrayList<Integer>();
        short arr[] = new short[strtok.countTokens()];
        int i = 0;

        while (strtok.hasMoreTokens())
        {
            arr[i] = getShortFromHexString(strtok.nextToken());
            i++;
            // list.add(getIntFromHexString(strtok.nextToken()));
        }

        return arr;
    }

    public int getIntFromHexString(String hex)
    {
        if (hex.startsWith("0x"))
        {
            hex = hex.substring(2);
        }

        return Integer.parseInt(hex, 16);
    }

    public byte getByteFromHexString(String hex)
    {
        if (hex.startsWith("0x"))
        {
            hex = hex.substring(2);
        }

        return Byte.parseByte(hex, 16);
        // return Integer.parseInt(hex, 16);
    }

    public short getShortFromHexString(String hex)
    {
        if (hex.startsWith("0x"))
        {
            hex = hex.substring(2);
        }

        return Short.parseShort(hex, 16);
        // return Integer.parseInt(hex, 16);
    }

    public void decodeLogFile(String file)
    {

        try
        {
            BufferedReader br = new BufferedReader(new FileReader(file));
            BufferedWriter bw = new BufferedWriter(new FileWriter("../test/proc.txt"));

            String line;

            while ((line = br.readLine()) != null)
            {

                if (line.contains("$RS232Command-readDeviceDataPage: just read packet 0"))
                {
                    bw.newLine();
                    bw.write(line.substring(0, 12) + "    "
                            + line.substring(line.indexOf("$RS232Command-readDeviceDataPage: just")));
                    bw.newLine();
                    bw.flush();
                }

                if (line.contains("$RS232Command-decodeDC: decoded bytes = <A7 31 65 51 80"))
                {
                    bw.write(line.substring(line.indexOf("$RS232Command-decodeDC: decoded bytes = <A7 31 65 51 80") + 41));
                    bw.newLine();
                    bw.flush();
                }

                System.out.print(".");
            }

            bw.close();
            br.close();

        }
        catch (Exception ex)
        {
            System.out.println("Error on read/write:" + ex);
        }
        finally
        {}

    }

    private void decodeCurrentTimeStamp(int ai[], int start)
    {

        HexUtils hu = new HexUtils();
        // Contract.pre(ai != null, "rawData is null.");
        // Contract.pre(ai.length == 64, "rawData.length is " + ai.length +
        // "; should be " + 64);
        int i = ai[start + 0];
        int j = ai[start + 1];
        int k = ai[start + 2];
        int l = hu.makeUnsignedShort(ai[start + 3], ai[start + 4]);
        int i1 = ai[start + 5];
        int j1 = ai[start + 6];
        Date m_timeStamp = createTimestamp(j1, i1, l, i, j, k);

        if (m_timeStamp.getYear() == 10)
        {

            System.out.println("decodeCurrentTimeStamp: current time stamp for device is (" + start + "): "
                    + m_timeStamp);
        }
    }

    public Date createTimestamp(int i, int j, int k, int l, int i1, int j1)
    {
        // verifyDeviceTimeStamp(i, j, k, l, i1, j1);
        GregorianCalendar gregoriancalendar = new GregorianCalendar(convertYear(k), j - 1, i, l, i1, j1);
        return gregoriancalendar.getTime();
    }

    public int convertYear(int i)
    {
        // verifyDeviceValue(i, 0, 9999, "year");
        if (i == 97 || i == 98 || i == 99)
        {
            i += 1900;
        }
        return i <= 1000 ? i + 2000 : i;
    }

    public static void main(String args[])
    {

        TestReturnData trd = new TestReturnData();

        trd.decodeTextLine("0xA9 0x68 0xF1 0x9A 0x59 0x71 0x69 0x55 0x71 0x57 0x15 0x63 0x56 0x35 0x55 0x66 0x65 0x63 0xD0 0xBC 0x63 0x56 0xA8 0xD9 0x54 0xD6 0x4E 0xC6 0xA6 0x8B 0x5A 0x35 0x6A 0x69 0x99 0x59 0xD2 0x38 0xD9 0x55 0xA2 0xE6 0xC5 0xC5 0x4D 0x5A 0x35 0x6A 0x69 0x99 0x59 0xD2 0x3C 0x9C 0x55 0x5A 0xB2 0x8F 0x15 0x4E 0xC6 0x35 0x6A 0x8F 0x26 0x55 0x55 0xCC 0x9A 0x8F 0x25 0x55 0xCB 0x15 0x55 0x55 0x55 0x55 0x55 0x5C 0xB1 0x8D 0x5C 0x74 0x56 0xC3 0x96 0x55 0x58 0xEC 0xC5 0x9C 0x55 0x8D 0xA8 0xD6 0xC5 0x5B 0x2A 0x2D 0x9C 0x55 0xCA 0x6B 0x23 0x3A 0xA5");
        // trd.decodeLogFile("../test/MM_test1.log");

        /*
         * int[] arr = trd.makeIntArray(
         * "14 0A 00 90 0E 2A 32 0A 00 00 00 00 00 0A 01 03 03 00 6A C9 48 14 0A 1E 00 5E DE 08 14 0A 1F 00 7A C5 09 1B 14 0A 01 0A 0A 00 5D C6 49 14 0A 39 11 56 C7 89 74 0A 89 59 43 2F 4E 7B CE 09 14 0A 55 90 0E 28 32 07 3C 00 00 0D 00 3C 30 08 28 10 00 0C 42 00 01 3C 3C 00 7B CE 49 14 0A 39 16 52 D2 6B 74 0A 89 59 43 2F 63 44 D3 0B 14 0A 00 90 0F 28 32 0C 00 00 00 2E 00 00 30 0E 3C 79 00 B4 83 00 28 8D 00 0C BF 00 01 03 03 00 44 D3 4B 14 0A 1E 00 76 E4 0B 14 0A 1F 00 64 C4 0D 14 0A 2F 00 77 C4 0D 14 0A 0C 90 0F 28 32 00 08 00 00 00 00 08 30 11 0C 6A 00 3C E2 00 B4 EC 00 28 F6 00 0C 28 10 01 0F 0F 00 78 C4 4D 14 0A 2F 00 6F D7 0D 14 0A 0C 90 0F 28 32 00 08 00 00 00 00 08 30 14 3C 19 00 0C 7D 00 3C F5 00 B4 FF 00 28 09 10 0C 3B 10 01 08 08 00 6F D7 4D 14 0A 39 0E 56 F5 4D 74 0A 89 59 43 2F 00 60 DE 0E 14 0A 3C 90 0F 28 32 00 28 00 00 00 00 28 30 17 20 48 00 3C 5C 00 0C C0 00"
         * );
         * for(int i = 0; i<arr.length-1; i++)
         * {
         * //System.out.print(arr[i] + " ");
         * System.out.print(MedicalDevice.Util.makeUnsignedShort(arr[i],
         * arr[i+1]) + " ");
         * if (i%30==0)
         * System.out.println();
         * }
         */

        int[] arri = trd
                .makeIntArray("14 0A 00 90 0E 2A 32 0A 00 00 00 00 00 0A 01 03 03 00 6A C9 48 14 0A 1E 00 5E DE 08 14 0A 1F 00 7A C5 09 1B 14 0A 01 0A 0A 00 5D C6 49 14 0A 39 11 56 C7 89 74 0A 89 59 43 2F 4E 7B CE 09 14 0A 55 90 0E 28 32 07 3C 00 00 0D 00 3C 30 08 28 10 00 0C 42 00 01 3C 3C 00 7B CE 49 14 0A 39 16 52 D2 6B 74 0A 89 59 43 2F 63 44 D3 0B 14 0A 00 90 0F 28 32 0C 00 00 00 2E 00 00 30 0E 3C 79 00 B4 83 00 28 8D 00 0C BF 00 01 03 03 00 44 D3 4B 14 0A 1E 00 76 E4 0B 14 0A 1F 00 64 C4 0D 14 0A 2F 00 77 C4 0D 14 0A 0C 90 0F 28 32 00 08 00 00 00 00 08 30 11 0C 6A 00 3C E2 00 B4 EC 00 28 F6 00 0C 28 10 01 0F 0F 00 78 C4 4D 14 0A 2F 00 6F D7 0D 14 0A 0C 90 0F 28 32 00 08 00 00 00 00 08 30 14 3C 19 00 0C 7D 00 3C F5 00 B4 FF 00 28 09 10 0C 3B 10 01 08 08 00 6F D7 4D 14 0A 39 0E 56 F5 4D 74 0A 89 59 43 2F 00 60 DE 0E 14 0A 3C 90 0F 28 32 00 28 00 00 00 00 28 30 17 20 48 00 3C 5C 00 0C C0 00");

        // FIXME: Problem Checkin
        byte[] arr = null; // MedicalDevice.Util.makeByteArray(arri);

        for (int i = 0; i < arr.length - 1; i++)
        {
            System.out.print(arr[i] + " ");

            // System.out.print(MedicalDevice.Util.makeUnsignedShort(arr[i],
            // arr[i+1]) + " ");

            if (i % 30 == 0)
            {
                System.out.println();
            }
        }

        /*
         * short[] arr = trd.makeShortArray(
         * "14 0A 00 90 0E 2A 32 0A 00 00 00 00 00 0A 01 03 03 00 6A C9 48 14 0A 1E 00 5E DE 08 14 0A 1F 00 7A C5 09 1B 14 0A 01 0A 0A 00 5D C6 49 14 0A 39 11 56 C7 89 74 0A 89 59 43 2F 4E 7B CE 09 14 0A 55 90 0E 28 32 07 3C 00 00 0D 00 3C 30 08 28 10 00 0C 42 00 01 3C 3C 00 7B CE 49 14 0A 39 16 52 D2 6B 74 0A 89 59 43 2F 63 44 D3 0B 14 0A 00 90 0F 28 32 0C 00 00 00 2E 00 00 30 0E 3C 79 00 B4 83 00 28 8D 00 0C BF 00 01 03 03 00 44 D3 4B 14 0A 1E 00 76 E4 0B 14 0A 1F 00 64 C4 0D 14 0A 2F 00 77 C4 0D 14 0A 0C 90 0F 28 32 00 08 00 00 00 00 08 30 11 0C 6A 00 3C E2 00 B4 EC 00 28 F6 00 0C 28 10 01 0F 0F 00 78 C4 4D 14 0A 2F 00 6F D7 0D 14 0A 0C 90 0F 28 32 00 08 00 00 00 00 08 30 14 3C 19 00 0C 7D 00 3C F5 00 B4 FF 00 28 09 10 0C 3B 10 01 08 08 00 6F D7 4D 14 0A 39 0E 56 F5 4D 74 0A 89 59 43 2F 00 60 DE 0E 14 0A 3C 90 0F 28 32 00 28 00 00 00 00 28 30 17 20 48 00 3C 5C 00 0C C0 00"
         * );
         * for(int i = 0; i<arr.length; i++)
         * {
         * //System.out.print((char)arr[i]);
         * System.out.print(arr[i] + " ");
         * if (i%30==0)
         * System.out.println();
         * }
         */
        for (int i = 0; i < arr.length - 6; i++)
        {
            int year = trd.convertYear(arr[i]);

            if (year == 2010)
            {
                System.out.print("Year: 2010, index=" + i);
                // trd.decodeCurrentTimeStamp(arr, i);
            }
        }

    }
    /*
     * 144 14 42 50 10 0 0 0 0 0 10 1 3 3 0 106 201 72 20 10 30 0 94 222 8 20 10
     * 31
     * 0 122 197 9 27 20 10 1 10 10 0 93 198 73 20 10 57 17 86 199 137 116 10
     * 137 89 67 47 78 123 206
     * 9 20 10 85 144 14 40 50 7 60 0 0 13 0 60 48 8 40 16 0 12 66 0 1 60 60 0
     * 123 206 73
     * 20 10 57 22 82 210 107 116 10 137 89 67 47 99 68 211 11 20 10 0 144 15 40
     * 50 12 0 0 0 46 0
     * 0 48 14 60 121 0 180 131 0 40 141 0 12 191 0 1 3 3 0 68 211 75 20 10 30 0
     * 118 228 11 20
     * 10 31 0 100 196 13 20 10 47 0 119 196 13 20 10 12 144 15 40 50 0 8 0 0 0
     * 0 8 48 17 12
     * 106 0 60 226 0 180 236 0 40 246 0 12 40 16 1 15 15 0 120 196 77 20 10 47
     * 0 111 215 13 20 10
     * 12 144 15 40 50 0 8 0 0 0 0 8 48 20 60 25 0 12 125 0 60 245 0 180 255 0
     * 40 9 16 12
     * 59 16 1 8 8 0 111 215 77 20 10 57 14 86 245 77 116 10 137 89 67 47 0 96
     * 222 14 20 10 60 144
     */
}
