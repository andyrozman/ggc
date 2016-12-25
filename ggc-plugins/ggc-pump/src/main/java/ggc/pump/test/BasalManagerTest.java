package main.java.ggc.pump.test;

import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Map;

import com.atech.utils.data.ATechDate;

import ggc.core.util.DataAccess;
import main.java.ggc.pump.data.dto.BasalRatesDayDTO;
import main.java.ggc.pump.data.util.PumpBasalManager;

/**
 * Created by andy on 24.09.15.
 */
public class BasalManagerTest extends AbstractPumpTest
{

    PumpBasalManager manager;


    public BasalManagerTest()
    {
        prepareContext();
        manager = new PumpBasalManager(dataAccessPump);
    }


    // 20150410140200L = change value
    // 20140901140200L
    // 20140927000000

    public void checkDataRetrieveal()
    {
        GregorianCalendar from = ATechDate.getGregorianCalendar(ATechDate.FORMAT_DATE_AND_TIME_S, 20140927000000L);
        GregorianCalendar to = (GregorianCalendar) from.clone();
        to.add(Calendar.WEEK_OF_YEAR, 1);

        Map<String, BasalRatesDayDTO> data = manager.getBasalRatesForRange(from, to);

        System.out.println("Data: " + data.size());

        for (BasalRatesDayDTO basal : data.values())
        {
            System.out.println(basal.toString());
        }

    }


    public void checkProfiles()
    {
        DataAccess da = DataAccess.getInstance();
        int days = da.getDaysInInterval(new GregorianCalendar(), new GregorianCalendar());

        System.out.println("Diff in days: " + days);

        GregorianCalendar from = ATechDate.getGregorianCalendar(ATechDate.FORMAT_DATE_AND_TIME_S, 20141201140200L);
        GregorianCalendar to = (GregorianCalendar) from.clone();
        to.add(Calendar.WEEK_OF_YEAR, 1);

        days = da.getDaysInInterval(from, to);

        System.out.println("Diff in days: " + days + " from: " + getDateString(from) + " to: " + getDateString(to));

    }


    private String getDateString(GregorianCalendar gc)
    {
        return ATechDate.getDateString(ATechDate.FORMAT_DATE_AND_TIME_S, gc);
    }


    public static void main(String args[])
    {
        BasalManagerTest basalManagerTest = new BasalManagerTest();
        basalManagerTest.checkDataRetrieveal();

    }

}
