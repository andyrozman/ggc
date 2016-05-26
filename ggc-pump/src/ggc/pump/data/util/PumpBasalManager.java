package ggc.pump.data.util;

import java.util.*;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.atech.utils.data.ATechDate;
import com.atech.utils.data.ATechDateType;

import ggc.plugin.data.DeviceValuesDay;
import ggc.plugin.data.DeviceValuesEntry;
import ggc.plugin.data.DeviceValuesRange;
import ggc.pump.data.PumpValuesEntry;
import ggc.pump.data.defs.PumpBasalType;
import ggc.pump.data.defs.PumpBaseType;
import ggc.pump.data.dto.BasalRatesDayDTO;
import ggc.pump.data.dto.BasalStatistics;
import ggc.pump.data.profile.ProfileSubPattern;
import ggc.pump.db.PumpProfile;
import ggc.pump.util.DataAccessPump;

/**
 * Created by andy on 24.08.15.
 */
public class PumpBasalManager
{

    Logger LOG = LoggerFactory.getLogger(PumpBasalManager.class);

    DataAccessPump dataAccess;


    public PumpBasalManager(DataAccessPump dataAccess)
    {
        this.dataAccess = dataAccess;
    }


    public BasalStatistics getBasalStatistics(GregorianCalendar dayGc)
    {
        return new BasalStatistics(getBasalRatesForRange(dayGc, dayGc).values());
    }


    public BasalStatistics getBasalStatistics(GregorianCalendar dayGc, DeviceValuesDay dataIn)
    {
        List<PumpValuesEntry> pumpValuesEntryList = getPumpValuesEntries(dataIn);

        Map<String, BasalRatesDayDTO> basalRates = getBasalRatesFromData(pumpValuesEntryList);

        loadProfilesIfRequired(basalRates, dayGc, dayGc);

        return new BasalStatistics(basalRates.values());
    }


    private List<PumpValuesEntry> getPumpValuesEntries(DeviceValuesDay dataIn)
    {
        List<DeviceValuesEntry> list = dataIn.getList();
        List<PumpValuesEntry> pumpValuesEntryList = new ArrayList<PumpValuesEntry>();

        for (DeviceValuesEntry dve : list)
        {
            PumpValuesEntry pve = (PumpValuesEntry) dve;
            pumpValuesEntryList.add(pve);
        }
        return pumpValuesEntryList;
    }


    public BasalStatistics getBasalStatistics(GregorianCalendar dayGc, List<PumpValuesEntry> data)
    {
        Map<String, BasalRatesDayDTO> basalRates = getBasalRatesFromData(data);

        loadProfilesIfRequired(basalRates, dayGc, dayGc);

        return new BasalStatistics(basalRates.values());
    }


    public BasalStatistics getBasalStatistics(GregorianCalendar from, GregorianCalendar to)
    {
        return new BasalStatistics(getBasalRatesForRange(from, to).values());
    }


    public BasalRatesDayDTO getBasalRatesForDate(GregorianCalendar gc)
    {
        Map<String, BasalRatesDayDTO> basalRates = getBasalRatesForRange(gc, gc);

        return getSingleBasalRatesDayDTO(basalRates);
    }


    private BasalRatesDayDTO getSingleBasalRatesDayDTO(Map<String, BasalRatesDayDTO> basalRatesForRange)
    {
        for (BasalRatesDayDTO basalRates : basalRatesForRange.values())
        {
            return basalRates;
        }

        return null;
    }


    public BasalRatesDayDTO getBasalRatesForDate(GregorianCalendar gc, DeviceValuesDay deviceValuesDayData)
    {
        List<PumpValuesEntry> pumpValuesEntryList = getPumpValuesEntries(deviceValuesDayData);

        return getSingleBasalRatesDayDTO(prepareBasalRates(pumpValuesEntryList, gc, gc));
    }


    public Map<String, BasalRatesDayDTO> getBasalRatesForRange(GregorianCalendar from, GregorianCalendar to)
    {
        LOG.debug("Get BasalRates For Range: {} - {}", DataAccessPump.getGregorianCalendarAsDateString(from),
            DataAccessPump.getGregorianCalendarAsDateString(to));
        List<PumpValuesEntry> data = this.dataAccess.getDb().getRangePumpBasalValues(from, to);

        return prepareBasalRates(data, from, to);
    }


    private Map<String, BasalRatesDayDTO> prepareBasalRates(List<PumpValuesEntry> data, GregorianCalendar from,
            GregorianCalendar to)
    {
        Map<String, BasalRatesDayDTO> basalRates = getBasalRatesFromData(data);

        loadProfilesIfRequired(basalRates, from, to);

        return basalRates;
    }


    public void loadProfilesIfRequired(Map<String, BasalRatesDayDTO> basalRates, GregorianCalendar from,
            GregorianCalendar to)
    {
        int days = this.dataAccess.getDaysInInterval(from, to);

        if (basalRates.size() == days)
            return;

        LOG.debug("  Loading profiles...");

        List<PumpProfile> profilesForRange = this.dataAccess.getDb().getProfilesForRange(from, to);

        if (profilesForRange.size() == 0)
            return;

        // go through days to set profiles
        for (int i = 0; i < days; i++)
        {
            GregorianCalendar gcRunning = (GregorianCalendar) from.clone();
            gcRunning.add(Calendar.DAY_OF_YEAR, i);

            ATechDate date = new ATechDate(ATechDateType.DateAndTimeSec, gcRunning);

            if (basalRates.containsKey(date.getDateString()))
            {
                basalRates.get(date.getDateString()).addProfilesForDay(getProfilesForDay(profilesForRange, date));
            }
            else
            {
                BasalRatesDayDTO dto = new BasalRatesDayDTO(date);
                dto.addProfilesForDay(getProfilesForDay(profilesForRange, date));
                basalRates.put(date.getDateString(), dto);
            }

        }
    }


    private Collection<PumpProfile> getProfilesForDay(Collection<PumpProfile> profilesForRange, ATechDate aTechDate)
    {
        long date = aTechDate.getDate();

        Map<Long, PumpProfile> profiles = new HashMap<Long, PumpProfile>();

        for (int i = 0; i < 24; i++)
        {
            long dt = date * 1000000 + i * 10000;

            for (PumpProfile pp : profilesForRange)
            {
                if (pp.isInProfileRange(dt))
                {
                    if (!profiles.containsKey(pp.getId()))
                    {
                        profiles.put(pp.getId(), pp);
                    }
                    break;
                }
            }
        }

        return profiles.values();
    }


    public Map<String, BasalRatesDayDTO> getBasalRatesFromData(DeviceValuesRange rangePumpValues)
    {
        List<PumpValuesEntry> pumpValuesEntryList = new ArrayList<PumpValuesEntry>();

        for (DeviceValuesDay day : rangePumpValues.getDayEntries())
        {
            pumpValuesEntryList.addAll(getPumpValuesEntries(day));
        }

        return getBasalRatesFromData(pumpValuesEntryList);
    }


    public Map<String, BasalRatesDayDTO> getBasalRatesFromData(List<PumpValuesEntry> data)
    {
        Map<String, BasalRatesDayDTO> basalRatesMap = new HashMap<String, BasalRatesDayDTO>();

        // sort PumpValuesEntries into map
        for (PumpValuesEntry pve : data)
        {
            if (pve.getBaseType() != PumpBaseType.Basal)
            {
                continue;
            }

            if (pve.getSubType() != PumpBasalType.Value.getCode() && //
                    pve.getSubType() != PumpBasalType.ValueChange.getCode())
            {
                continue;
            }

            String date = pve.getDateTimeObject().getDateString();

            if (basalRatesMap.containsKey(date))
            {
                basalRatesMap.get(date).addPumpValuesEntry(pve);
            }
            else
            {
                BasalRatesDayDTO dto = new BasalRatesDayDTO(pve.getDateTimeObject());
                dto.addPumpValuesEntry(pve);

                basalRatesMap.put(date, dto);
            }
        }

        // TBRs are ignored for now (in some pumps they are calculated out of
        // Value_Change)

        // refresh data
        for (BasalRatesDayDTO basal : basalRatesMap.values())
        {
            basal.processData();
        }

        return basalRatesMap;
    }


    // // TODO
    // public BasalRatesDayDTO getBasalRatesForDay(DeviceValuesDay
    // deviceValuesDay)
    // {
    // // for(Da)
    //
    // return null;
    // }

    public float getBasalValueForHour_(int hour, PumpProfile profile, BasalRatesDayDTO basalEntries)
    {
        // FIXME
        float value = -1.0f;

        if (basalEntries != null)
        {

        }
        else
        {
            if (profile != null)
            {
                ProfileSubPattern patternForHour = profile.getPatternForHour(hour);

                if (patternForHour != null)
                {
                    value = patternForHour.getAmount();
                }
            }
        }

        return value;
    }

}
