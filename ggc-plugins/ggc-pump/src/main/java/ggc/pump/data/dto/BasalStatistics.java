package ggc.pump.data.dto;

import java.util.Collection;

import com.atech.misc.statistics.StatisticsItem;

/**
 * Created by andy on 24.09.15.
 */
public class BasalStatistics implements StatisticsItem
{

    public static final int SUM_BASAL = 1;
    public static final int AVG_BASAL_PER_DAY = 2;
    public static final int AVG_BASAL_IN_DAY = 3;

    Collection<BasalRatesDayDTO> listDailyRates = null;
    double sumOfBasalRatesForAllDays = 0.0d;


    public BasalStatistics(Collection<BasalRatesDayDTO> list)
    {
        this.listDailyRates = list;

        discoverFullSum();
    }


    private void discoverFullSum()
    {
        for (BasalRatesDayDTO basal : listDailyRates)
        {
            sumOfBasalRatesForAllDays += basal.getDailySum();
        }
    }


    public float getValueForItem(int index)
    {
        switch (index)
        {
            case AVG_BASAL_IN_DAY:
                if (this.listDailyRates.isEmpty())
                    return 0.0f;
                else
                    return (float) (sumOfBasalRatesForAllDays / (this.listDailyRates.size() * 24));

            case AVG_BASAL_PER_DAY:
                if (this.listDailyRates.isEmpty())
                    return 0.0f;
                else
                    return (float) (sumOfBasalRatesForAllDays / this.listDailyRates.size());

            case SUM_BASAL:
                return (float) sumOfBasalRatesForAllDays;

            default:
                return 0.0f;
        }
    }


    public int getStatisticsAction(int index)
    {
        return 0;
    }


    public boolean isSpecialAction(int index)
    {
        return false;
    }


    public int getMaxStatisticsObject()
    {
        return 0;
    }


    public boolean weHaveSpecialActions()
    {
        return false;
    }
}
