package main.java.ggc.pump.data.util;

/**
 * Created by andy on 15.10.15.
 */

import java.util.*;

import org.apache.commons.collections.CollectionUtils;

import com.atech.data.SimpleTimeValueDataDto;
import com.atech.i18n.I18nControlAbstract;

import main.java.ggc.pump.data.PumpValuesEntry;
import main.java.ggc.pump.data.defs.PumpBasalType;
import main.java.ggc.pump.data.defs.PumpBolusType;
import main.java.ggc.pump.data.defs.PumpEventType;
import main.java.ggc.pump.util.DataAccessPump;

/**
 * Class is intended to retrieve extra pump data. This can be more info about PumpBolus (in case of datasheet),
 * PumpBasal TBRs, display of Pump events and more.
 */
public class PumpAdditionalDataHandler
{

    DataAccessPump dataAccess = DataAccessPump.getInstance();
    I18nControlAbstract i18nControl = dataAccess.getI18nControlInstance();

    private static Map<PumpEventType, PumpEventType> pairedEventsDefinition;


    public SimpleTimeValueDataDto processBolusEntry(PumpValuesEntry pumpValuesEntry)
    {
        PumpBolusType bolusType = PumpBolusType.getByCode(pumpValuesEntry.getSubType());

        if ((bolusType == PumpBolusType.Extended) || (bolusType == PumpBolusType.Multiwave))
        {
            Map<String, String> mapValues = dataAccess.createMapFromKeyValueString(pumpValuesEntry.getValue(), "=",
                ";");

            String value;
            String time = getTime(pumpValuesEntry);

            if (bolusType == PumpBolusType.Extended)
            {
                value = String.format(i18nControl.getMessage("RP_DAILYTIMESHEET_BOLUS_EXT"), mapValues.get("DURATION"),
                    mapValues.get("AMOUNT_SQUARE"), time);
            }
            else
            {
                value = String.format(i18nControl.getMessage("RP_DAILYTIMESHEET_BOLUS_MW"), mapValues.get("AMOUNT"),
                    mapValues.get("DURATION"), mapValues.get("AMOUNT_SQUARE"), time);
            }

            return new SimpleTimeValueDataDto(time, value);
        }

        return null;
    }


    public boolean isTBREntry(PumpValuesEntry pumpValuesEntry)
    {
        PumpBasalType pbt = PumpBasalType.getByCode(pumpValuesEntry.getSubType());

        return ((pbt == PumpBasalType.TemporaryBasalRate) || //
                (pbt == PumpBasalType.TemporaryBasalRateCanceled) || //
                (pbt == PumpBasalType.TemporaryBasalRateEnded));
    }


    public SimpleTimeValueDataDto processEventEntryWithoutPaired(PumpValuesEntry pumpValueEntry)
    {
        PumpEventType pumpEvent = PumpEventType.getByCode(pumpValueEntry.getSubType());

        if (isAllowedEvent(pumpEvent) && (!isPairedEvent(pumpEvent)))
        {
            String time = getTime(pumpValueEntry);
            return new SimpleTimeValueDataDto(time, pumpEvent.getTranslation() + " (" + time + ")");
        }

        return null;
    }


    public boolean isPairedEvent(PumpValuesEntry pumpValueEntry)
    {
        PumpEventType pumpEvent = PumpEventType.getByCode(pumpValueEntry.getSubType());

        return isPairedEvent(pumpEvent);
    }


    public List<SimpleTimeValueDataDto> processPairedEvents(List<PumpValuesEntry> pumpValueEntryList)
    {
        createPairedEventsDefinition();

        List<SimpleTimeValueDataDto> outList = new ArrayList<SimpleTimeValueDataDto>();

        List<PumpValuesEntry> deleteEntries = new ArrayList<PumpValuesEntry>();

        for (Map.Entry<PumpEventType, PumpEventType> entry : pairedEventsDefinition.entrySet())
        {

            PumpValuesEntry start = null;

            if (CollectionUtils.isNotEmpty(deleteEntries))
            {
                for (PumpValuesEntry pve : deleteEntries)
                {
                    pumpValueEntryList.remove(pve);
                }

                deleteEntries.clear();
            }

            if (CollectionUtils.isNotEmpty(pumpValueEntryList))
            {

                for (PumpValuesEntry pve : pumpValueEntryList)
                {
                    if (pve.getSubType() == entry.getKey().getCode())
                    {
                        start = pve;

                    }
                    else if (pve.getSubType() == entry.getValue().getCode())
                    {
                        String time = getTime(pve);

                        if (start != null)
                        {
                            String timeStart = getTime(start);

                            outList.add(new SimpleTimeValueDataDto(timeStart,
                                    entry.getKey().getTranslation() + " [" + timeStart + " - " + time + "]"));

                            deleteEntries.add(pve);
                            deleteEntries.add(start);
                            start = null;
                        }
                        else
                        {
                            outList.add(new SimpleTimeValueDataDto(time,
                                    entry.getValue().getTranslation() + " (" + time + ")"));

                            deleteEntries.add(pve);
                        }
                    }

                }
            }
            else
            {
                break;
            }

        }

        return outList;
    }


    public SimpleTimeValueDataDto processEventEntry(PumpValuesEntry pumpValueEntry)
    {
        PumpEventType pumpEvent = PumpEventType.getByCode(pumpValueEntry.getSubType());

        if (isAllowedEvent(pumpEvent))
        {
            String time = getTime(pumpValueEntry);
            return new SimpleTimeValueDataDto(time, pumpEvent.getTranslation() + " (" + time + ")");
        }

        return null;
    }


    public String getTime(PumpValuesEntry pumpValuesEntry)
    {
        String time = pumpValuesEntry.getDateTimeObject().getTimeString();

        return time.substring(0, time.lastIndexOf(":"));
    }


    public List<SimpleTimeValueDataDto> processTBRs(List<PumpValuesEntry> tbrs)
    {
        List<SimpleTimeValueDataDto> outList = new ArrayList<SimpleTimeValueDataDto>();

        PumpValuesEntry start = null;
        PumpValuesEntry end = null;

        for (PumpValuesEntry pumpValueEntry : tbrs)
        {
            if (pumpValueEntry.getSubType() == PumpBasalType.TemporaryBasalRate.getCode()) // Start
            {
                start = pumpValueEntry;
            }
            else
            {
                end = pumpValueEntry;
            }

            if (start != null && end != null)
            {
                String value;
                String timeString = getTime(start);

                Map<String, String> mapValues = dataAccess.createMapFromKeyValueString(start.getValue(), "=", ";");

                if (end.getSubType() == PumpBasalType.TemporaryBasalRateCanceled.getCode())
                {
                    long minutes = start.getDateTimeObject().getDifference(pumpValueEntry.getDateTimeObject(),
                        Calendar.MINUTE);

                    value = String.format(i18nControl.getMessage("RP_DAILYTIMESHEET_TBR"),
                        "" + DataAccessPump.getTimeFromMinutes((int) minutes), mapValues.get("VALUE"), timeString);
                }
                else
                {
                    value = String.format(i18nControl.getMessage("RP_DAILYTIMESHEET_TBR"), mapValues.get("DURATION"),
                        mapValues.get("VALUE"), timeString);
                }

                outList.add(new SimpleTimeValueDataDto(timeString, value));

                start = null;
                end = null;
            }

        }

        return outList;
    }


    public boolean isAllowedEvent(PumpEventType eventType)
    {
        switch (eventType)
        {
            case PrimeInfusionSet:
            case CartridgeChange:
            case CartridgeRewind:
            case BasalRun:
            case BasalStop:
            case PowerDown:
            case PowerUp:
            case Activate:
            case Deactivate:
                {
                    return true;
                }

            default:
                {
                    // DO-FUTURE add configured filtering
                    return false;
                }
        }
    }


    public boolean isPairedEvent(PumpEventType eventType)
    {
        switch (eventType)
        {
            case BasalRun:
            case BasalStop:
            case PowerDown:
            case PowerUp:
            case Activate:
            case Deactivate:
                {
                    return true;
                }

            default:
                return false;
        }
    }


    public void createPairedEventsDefinition()
    {
        if (pairedEventsDefinition != null)
            return;

        pairedEventsDefinition = new HashMap<PumpEventType, PumpEventType>();

        pairedEventsDefinition.put(PumpEventType.BasalStop, PumpEventType.BasalRun);
        pairedEventsDefinition.put(PumpEventType.PowerDown, PumpEventType.PowerUp);
        pairedEventsDefinition.put(PumpEventType.Deactivate, PumpEventType.Activate);
    }


    public void isAllowedEntry(PumpValuesEntry pumpValuesEntry)
    {
        // DO-FUTURE add global filtering of entries by configuration

    }

}
