package ggc.connect.software.web.nightscout;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.json.JSONArray;

import com.atech.utils.web.RestAPIUtil;
import com.atech.utils.web.RestAuthorization;
import com.google.gson.Gson;

import ggc.connect.software.web.nightscout.data.ImportType;
import ggc.connect.software.web.nightscout.data.NightScoutConverter;
import ggc.connect.software.web.nightscout.data.config.NightScoutConfig;
import ggc.connect.software.web.nightscout.data.config.NightScoutData;
import ggc.connect.software.web.nightscout.data.dto.Entry;
import ggc.connect.software.web.nightscout.data.dto.Treatment;
import ggc.connect.software.web.nightscout.data.dto.util.Count;
import ggc.plugin.data.enums.PlugInExceptionType;
import ggc.plugin.device.PlugInBaseException;
import ggc.plugin.output.ConsoleOutputWriter;

/**
 * Created by andy on 3/15/18.
 */
public class NightScoutImporter {

    int currentYear;
    int currentMonth;
    String serverUrl = "https://andyrozman.ns.10be.de:25508"; // andyslittleloopi547@

    String yearTemplate = "/api/v1/count/entries/where?find[dateString][$gte]=YEAR-01-01&find[dateString][$lte]=YEAR-12-31&find[type]=sgv";
    String monthTemplate = "/api/v1/count/entries/where?find[dateString][$gte]=YEAR-MONTH-01&find[dateString][$lte]=YEAR-MONTH-LAST_DAY&find[type]=sgv";
    String monthTemplate2 = null;

    // 1. See current status
    //      No status: Get First Month
    //      Status: Show all not downloaded (last active month is marked as invalid)
    // 2. Show summary
    // 3. Download

    RestAPIUtil apiUtil; // = new RestAPIUtil();
    Gson gson; // = apiUtil.getGson();

    NightScoutConfig config;
    NightScoutData data;

    GregorianCalendar gcYear;

    NightScoutConverter converter;

    public NightScoutImporter(NightScoutConfig config, NightScoutData data)
    {
        GregorianCalendar gcNow = new GregorianCalendar();
        currentYear = gcNow.get(Calendar.YEAR);
        currentMonth = gcNow.get(Calendar.MONTH);

        apiUtil = new RestAPIUtil();
        gson = apiUtil.getGson();

        this.config = config;
        this.data = data;

        converter = new NightScoutConverter(new ConsoleOutputWriter());
    }


    public Map<String,Object> getSummaryItems() throws PlugInBaseException
    {
        if (StringUtils.isBlank(data.getFirstEntry()))
        {
            determineFirstMonth();
        }
        else
        {
            String[] dateM = data.getFirstEntry().split(".");
            completeUndownloadedList(Integer.parseInt(dateM[0]), Integer.parseInt(dateM[1]));
        }


        return null;
    }

    private void completeUndownloadedList(int firstYear, int firstMonth) {

        for(int i=(firstYear-1); i<=currentYear; i++)
        {
            int maxMonth = 12;
            int minMonth = 1;

            if (i == currentYear)
            {
                maxMonth = currentMonth;
            }

            if (i == firstYear)
            {
                minMonth = firstMonth;
            }

            for(int j=minMonth-1; j<=maxMonth; j++)
            {
                String entry = i + "." + j;

                if (!data.isEntryPresent(entry)) {
                    data.addNotDownloadedEntry(entry);
                }
            }
        }
    }


    public void determineFirstMonth() throws PlugInBaseException{
        int firstYear = determineFirstYear();
        int month = 0;

        setYear(firstYear);

        if (firstYear == currentYear)
            month = getMonthHardWay(1, currentMonth);
        else if (firstYear==-1)
        {
            System.out.println("firstYear=-1, no entries found.");
            throw new PlugInBaseException(PlugInExceptionType.NoDataFoundForConversion);
        }
        else
            month = getMonthQuickSortWay(1, 12, false);

        System.out.println("Month: " + month + ", Year: " + firstYear);

        data.setFirstEntry(firstYear + "." + month);

        completeUndownloadedList(firstYear, month);

    }

    private void setYear(int year) {
        gcYear = new GregorianCalendar(year, 1, 1);
        monthTemplate2 = monthTemplate.replace("YEAR", "" + year);
    }


    private String setYear(int year, ImportType importType) {
        gcYear = new GregorianCalendar(year, 1, 1);
        return importType.getUrl().replace("YEAR", "" + year);
    }


    private int determineFirstYear()
    {
        int itemsfound = 0;

        int year = currentYear;

        int yearWithData = -1;

        do
        {
            //System.out.println("Check Year: " + year);

            itemsfound = getCountByYear(year);

            if (itemsfound>0) {
                yearWithData = year;
            }

            //System.out.println("      itemsFound: " + itemsfound);

            if ((year==currentYear) && (itemsfound==0))
                return -1;

            year--;

        } while(itemsfound>0);

        return yearWithData;

    }


    private int getCountByYear(int year)
    {
        return getCount(yearTemplate.replace("YEAR", "" + year));
    }


    private int getCountByMonth(int month)
    {
        gcYear.set(Calendar.MONTH, month-1);

        String m = "0" + month;

        if (month>9)
            m = "" + month;

        return getCount(monthTemplate2.replaceAll("MONTH", m).replaceAll("LAST_DAY", "" + gcYear.getActualMaximum(Calendar.DAY_OF_MONTH)));
    }


    private int getCount(int year, int month)
    {
        setYear(year);

        gcYear.set(Calendar.MONTH, month-1);

        String m = "0" + month;

        if (month>9)
            m = "" + month;

        return getCount(monthTemplate2.replaceAll("MONTH", m).replaceAll("LAST_DAY", "" + gcYear.getActualMaximum(Calendar.DAY_OF_MONTH)));
    }

    private int getCount(int year, int month, ImportType importType)
    {
        String url = setYear(year, importType);

        gcYear.set(Calendar.MONTH, month-1);

        String m = "0" + month;

        if (month>9)
            m = "" + month;

        return getCount(url.replaceAll("MONTH", m).replaceAll("LAST_DAY", "" + gcYear.getActualMaximum(Calendar.DAY_OF_MONTH)));
    }


    private String getData(int year, int month, int count, ImportType importType)
    {
        String url = setYear(year, importType);

        gcYear.set(Calendar.MONTH, month-1);

        String m = "0" + month;

        if (month>9)
            m = "" + month;

        url = url.replaceAll("MONTH", m) //
                .replaceAll("LAST_DAY", "" + gcYear.getActualMaximum(Calendar.DAY_OF_MONTH))
        .replaceAll("COUNT", "" + count);

        return apiUtil.doGetCallJsonString(
                serverUrl + url, RestAuthorization.None, null);

    }


    private <E extends Object> List<E> getData(int year, int month, int count, ImportType importType, Class<E[]> clazz)
    {
        String url = setYear(year, importType);

        gcYear.set(Calendar.MONTH, month-1);

        String m = "0" + month;

        if (month>9)
            m = "" + month;

        url = url.replaceAll("MONTH", m) //
                .replaceAll("LAST_DAY", "" + gcYear.getActualMaximum(Calendar.DAY_OF_MONTH))
                .replaceAll("COUNT", "" + count);

        System.out.println("Rest API Url: " + url);

        return apiUtil.doGetCallJsonList(
                serverUrl + url, RestAuthorization.None, null, clazz);

    }



    private int getCount(String apiUrl)
    {
        //System.out.println("URL: " + serverUrl + apiUrl);

        String resultJson = apiUtil.doGetCallJsonString(
                serverUrl + apiUrl, RestAuthorization.None, null);

        if (resultJson.length()==2)
        {
            return 0;
        }
        else
        {
            Count[] cc = gson.fromJson(resultJson, Count[].class);

            return cc[0].getCount();
        }
    }




    private int getMonthHardWay(int first, int last) {

        //System.out.println("getMonthHardWay [first=" + first + ",last="+ last);

        int monthWithData = -1;

        for(int i=(last+1); i>=first; i--)
        {
            int cnt = getCountByMonth(i);

            if (cnt>0)
            {
                monthWithData = i;
            }
        }

        return monthWithData;
    }


    private int getMonthQuickSortWay(int first, int last, boolean lastPart) {
        //System.out.println("getMonthQuickSortWay: first=" + first + ", last=" + last);

        if (first==last)
            return first;

        if (lastPart)
        {
            int x = (last - first);

            if (x%2==0)
            {
                int y = x/2;

                int z = last - y;

                int cnt = getCountByMonth(z);

                if (cnt==0)
                {
                    return getMonthQuickSortWay(z, last,  true);
                }
                else
                {
                    return getMonthQuickSortWay(first, z,  true);
                }
            }
            else
            {
                return getMonthHardWay(first, last);
            }
        }
        else
        {
            float l = last/2.0f;
            int month = 0;

            if (last%2==0)
            {
                month = (last/2);
            }
            else
            {
                return getMonthHardWay(first, last);
            }

            int cnt = getCountByMonth(month);

            if (cnt>0)
            {
                return getMonthQuickSortWay(first, month, false);
            }
            else
            {
                return getMonthQuickSortWay(month, last, true);
            }
        }

    }


    public void getTreatmentData(int year, int month)
    {
        int count = getCount(year, month, ImportType.TreatmentMonthDataCount);

        System.out.println("Treatment count: " + count);

        List<Treatment> data = getData(year, month, count, ImportType.TreatmentMonthData, Treatment[].class);

        System.out.println("Data: " + data.size());

        for (Treatment entry : data) {
            converter.convert(entry);
        }
    }

    public void getSgvData(int year, int month)
    {
        int count = getCount(year, month, ImportType.EntryMonthDataCount);

        System.out.println("Sgv count: " + count);

        List<Entry> data = getData(year, month, count, ImportType.EntryMonthData, Entry[].class);

        System.out.println("Data: " + data.size());

        for (Entry entry : data) {
            converter.convert(entry);
        }

    }


    public void writeReportInLog()
    {
        converter.showEndStatus();
    }


    public static void test()
    {
        try {

            File f = new File(".");

            System.out.println("f: " + f.getAbsolutePath());

            BufferedReader br = new BufferedReader(new FileReader("data.txt"));

            String line;
            StringBuilder sb = new StringBuilder();

            while((line = br.readLine()) != null)
            {
                sb.append(line);
            }

            JSONArray mArray = new JSONArray(sb.toString());

            System.out.println("Size: " + mArray.length());

            final Treatment[] jsonToObject = new Gson().fromJson(sb.toString(), Treatment[].class);

            System.out.println("Size: " + jsonToObject.length);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public static void main(String[] args)
    {
        NightScoutConfig config = new NightScoutConfig();
        config.setSiteUrl("https://andyrozman.ns.10be.de:25508");

        NightScoutData data = new NightScoutData();


        //NightScoutImporter.test();



        //if (true)
        //    return;


        NightScoutImporter importer = new NightScoutImporter(config, data);

        importer.getSgvData(2018,3);
        //importer.getTreatmentData(2018, 3);
        importer.writeReportInLog();

        //importer.determineFirstMonth();
    }


}