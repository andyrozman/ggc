package ggc.pump.test;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.TimeZone;

import com.atech.utils.data.DataReaderWriter;
import com.atech.utils.data.HexUtils;

public class MinimedDecoderTester
{
    HexUtils hu = null;

    public MinimedDecoderTester(int[] data)
    {
        hu = new HexUtils();

        System.out.println(data.length);

        System.out.println(hu.getIntArrayShow(data));

        readFromDisk(data);

        /*
         * //(data.length-7
         * for(int i=0; i<1024-7 ; i++)
         * {
         * int[] d = hu.getIntSubArray(data, i, 7);
         * this.decodeCurrentTimeStamp(d, i);
         * }
         */

    }

    private void decodeCurrentTimeStamp(int ai[], int start)

    {
        int i = ai[0];
        int j = ai[1];
        int k = ai[2];
        // int l = MedicalDevice.Util.makeUnsignedShort(ai[3], ai[4]);

        int m = ai[5];
        int y = ai[6];

        GregorianCalendar gc = new GregorianCalendar(convertYear(y) - 1, m - 1, ai[4], ai[0], ai[1], ai[2]); // ,
                                                                                                             // ai[3]);
        gc.setTimeZone(TimeZone.getTimeZone("CET"));

        // m_timeStamp = createTimestamp(j1, i1, l, i, j, k);
        // log.info( (new
        // StringBuilder()).append("decodeCurrentTimeStamp: current time stamp for device is ").append(m_timeStamp).toString());

        // GregorianCalendar gc = createTimestamp(j1, i1, l, hour, min, year);

        if (y == 11)
        {

            System.out.println(hu.getIntArrayShow(ai));

            System.out.println("Start: " + start + "   " + gc.get(Calendar.DAY_OF_MONTH) + "." + gc.get(Calendar.MONTH)
                    + "." + gc.get(Calendar.YEAR) + " " + gc.get(Calendar.HOUR_OF_DAY) + ":" + gc.get(Calendar.MINUTE)
                    + ":" + gc.get(Calendar.SECOND));

            System.out.println("Start: " + start + "   x." + m + "." + convertYear(y));
        }

        // log.info( (new
        // StringBuilder()).append("decodeCurrentTimeStamp: current time stamp for device is ").append(m_timeStamp).toString());
    }

    /*
     * final Date createTimestamp(int i, int j, int k, int l, int i1, int j1)
     * throws BadDeviceValueException
     * {
     * verifyDeviceTimeStamp(i, j, k, l, i1, j1);
     * GregorianCalendar gregoriancalendar = new
     * GregorianCalendar(Util.convertYear(k), j - 1, i, l, i1, j1);
     * return gregoriancalendar.getTime();
     * }
     */

    private GregorianCalendar createTimestamp(int day, int month, int year, int l, int i1, int j1)
    {

        GregorianCalendar gregoriancalendar = new GregorianCalendar(convertYear(year), month - 1, day, l, i1, j1);
        return gregoriancalendar;

        // return gregoriancalendar.getTime();
    }

    private int convertYear(int i)
    {
        // System.out.println("Y: " + i);

        int k = 0;

        if (i <= 99 && i > 84)
        {
            k = 1900 + i;
        }
        else
        {
            k = 2000 + i;
        }

        // if(i == 97 || i == 98 || i == 99)
        // i += 1900;
        // int k = i <= 1000 ? i + 2000 : i;
        // System.out.println("Y: " + k);

        return k;

    }

    public void readFromDisk(int[] dta)
    {
        // HexUtils hu = new HexUtils();

        try
        {

            /*
             * File file = new File(data_file);
             * System.out.println("File exists: " + file.exists());
             */

            int[] b = dta;

            System.out.println(b.length);

            System.out.println(hu.getHexCompact(b));

            int[] bytepoint = new int[255 + 1];

            // int[] arr = new int[b.length]; //(int[])b;

            for (int i = 0; i < bytepoint.length; i++)
            {
                bytepoint[i] = 0;
            }

            int[] arr = dta;

            ArrayList<Integer> idx255 = new ArrayList<Integer>();

            for (int i = 0; i < arr.length - 1; i++)
            {
                // if (b[i]==11)
                // System.out.println("Index " + i);

                bytepoint[b[i]]++;

                if (b[i] == 244 && b[i + 1] == 128)
                {
                    idx255.add(i + 1);
                }

                arr[i] = b[i];
            }

            for (int i = 0; i < bytepoint.length; i++)
            {
                if (bytepoint[i] != 0)
                {
                    System.out.println("Nr: " + i + " " + bytepoint[i] + " times.");
                }
            }

            for (int i = 0; i < idx255.size() - 1; i++)
            {
                /*
                 * System.out.print("" + idx255.get(i) + "  ");
                 * //idx255.add(i);
                 * if (i%25==0)
                 * System.out.println();
                 */

                int[] b2 = hu.getIntSubArray(b, idx255.get(i) + 1, idx255.get(i + 1) - idx255.get(i) - 1);

                System.out.println(hu.getHexCompact(b2));
                System.out.println(hu.getIntArrayShow(b2)); // this.convertToIntArray(b2)));
            }

            // System.out.println(hu.getIntArrayShow(arr));

            /*
             * FileInputStream f_in = new FileInputStream(data_file);
             * // Read object using ObjectInputStream
             * ObjectInputStream obj_in = new ObjectInputStream(f_in);
             * //obj_in.
             * // Read an object
             * Object obj = obj_in.readObject();
             * if ((obj instanceof CommandHistoryData) && (!(obj instanceof
             * CommandReadSensorHistoryData)))
             * {
             * // Cast object to a Vector
             * CommandHistoryData chd = (CommandHistoryData) obj;
             * // Do something with vector....
             * System.out.println("Chd: " + chd);
             * }
             * else
             * {
             * System.out.println("Wrong type: " + obj.getClass());
             * }
             */

        }
        catch (Exception ex)
        {
            System.out.println("Error decodiong data: " + ex);
            ex.printStackTrace();
        }

    }

    public static void main(String[] args)
    {
        int nr = 0;
        DataReaderWriter drw = new DataReaderWriter(
                "/mnt/f/My_Projects/GNUGlucoCenter/Tools/PumpsTool/test_data/roman/mm_history_cmd_128_page_" + nr
                        + ".data");

        int[] dta = drw.readIntArray();

        new MinimedDecoderTester(dta);

    }

}
