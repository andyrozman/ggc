package ggc.pump.data.dto;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.atech.utils.data.ATechDate;

import ggc.pump.data.PumpValuesEntry;
import ggc.pump.data.profile.ProfileSubPattern;
import ggc.pump.db.PumpProfile;
import ggc.pump.util.DataAccessPump;

/**
 * Created by andy on 24.08.15.
 */
public class BasalRatesDayDTO
{

    private static final Logger LOG = LoggerFactory.getLogger(BasalRatesDayDTO.class);

    ATechDate date;
    List<PumpProfile> profiles;
    List<PumpValuesEntry> pumpValuesEntriesAll;
    List<PumpValuesEntry> pumpBasalEntries;
    Float[] dailyData = new Float[24];


    public BasalRatesDayDTO(ATechDate date)
    {
        this.date = date;
    }


    public void addProfilesForDay(Collection<PumpProfile> profiles)
    {
        if (profiles.size() != 0)
        {
            this.profiles = new ArrayList<PumpProfile>();
            this.profiles.addAll(profiles);

            processData();
        }
    }


    public boolean hasBasalDailyEntries()
    {
        return (pumpBasalEntries != null) && (pumpBasalEntries.size() != 0);
    }


    public void addPumpValuesEntry(PumpValuesEntry pve)
    {
        if (pumpValuesEntriesAll == null)
        {
            pumpValuesEntriesAll = new ArrayList<PumpValuesEntry>();
            pumpBasalEntries = new ArrayList<PumpValuesEntry>();
        }

        pumpValuesEntriesAll.add(pve);

        if ((pve.getSubType() == 1) || // Basal Value
                (pve.getSubType() == 8)) // Basal Change
        {
            pumpBasalEntries.add(pve);
        }
    }


    private void initializeArray()
    {
        for (int i = 0; i < 24; i++)
        {
            dailyData[i] = 0.0f;
        }
    }


    private void clearArray()
    {
        for (int i = 0; i < 24; i++)
        {
            dailyData[i] = null;
        }
    }


    private boolean isDataComplete()
    {
        for (int i = 0; i < 24; i++)
        {
            if (dailyData[i] == null)
                return false;
        }

        return true;
    }


    public void processData()
    {
        clearArray();

        if (CollectionUtils.isNotEmpty(pumpBasalEntries))
        {
            processPumpData();
        }

        if (!isDataComplete())
        {
            if (!CollectionUtils.isEmpty(profiles))
                processProfiles();
            else
                initializeArray();
        }

    }


    private void processPumpData()
    {
        for (PumpValuesEntry pve : pumpBasalEntries) // Basal_Value
        {
            if (pve.getSubType() == 1)
            {
                dailyData[pve.getDateTimeObject().getHourOfDay()] = getFloat(pve.getValue());
            }
        }

        for (PumpValuesEntry pve : pumpBasalEntries) // Basal_Value_Change
        {
            if (pve.getSubType() == 8)
            {
                dailyData[pve.getDateTimeObject().getHourOfDay()] = getFloat(pve.getValue());
            }
        }

        Float prevValue = 0.0f;

        for (int i = 0; i < 24; i++)
        {
            if (dailyData[i] == null)
            {
                dailyData[i] = prevValue;
            }
            else
            {
                prevValue = dailyData[i];
            }
        }
    }


    private Float getFloat(String value)
    {
        if (StringUtils.isNotBlank(value))
        {
            return Float.parseFloat(value);
        }

        return null;
    }


    @Override
    public String toString()
    {
        return "BasalRatesDailyDTO [date=" + date + ", dailyData=" + getTimeData() + "]";
    }


    private String getTimeData()
    {
        StringBuilder sb = new StringBuilder();

        sb.append("00:00=" + dailyData[0]);

        for (int i = 1; i < 24; i++)
        {
            String time = DataAccessPump.getLeadingZero(i, 2) + ":00=";

            sb.append(", " + time);
            sb.append(dailyData[i]);
        }

        return sb.toString();
    }


    public List<PumpProfile> getProfiles()
    {
        return profiles;
    }


    public double getDailySum()
    {
        double sum = 0.0d;

        for (int i = 0; i < 24; i++)
        {
            sum += dailyData[i];
        }

        return sum;
    }


    private void processProfiles()
    {
        for (int i = 0; i < 24; i++)
        {
            PumpProfile pp = getCorrectProfile(i);

            if (pp != null)
            {

                ProfileSubPattern patternForHour = pp.getPatternForHour(i);

                if (patternForHour != null)
                {
                    dailyData[i] = patternForHour.getAmount();
                }
            }
            else
            {
                long dt = date.getDate() * 1000000 + i * 10000;
                LOG.warn("Profile Entry not found for: {}", dt);
            }
        }
    }


    public PumpProfile getCorrectProfile(int hour)
    {
        long dt = date.getDate() * 1000000L + hour * 10000L;

        for (PumpProfile pp : this.profiles)
        {
            if (pp.isInProfileRange(dt))
            {
                return pp;
            }
        }

        return null;
    }


    public float getBasalForHour(int hour)
    {
        Float f = this.dailyData[hour];

        if (f == null)
            return 0.0f;
        else
            return f;
    }

}
