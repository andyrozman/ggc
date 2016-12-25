package main.java.ggc.pump.device.insulet.data.converter;

import java.util.*;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.atech.utils.data.ATechDate;
import com.atech.utils.data.BitUtils;

import ggc.core.util.DataAccess;
import ggc.plugin.output.OutputWriter;
import main.java.ggc.pump.data.defs.PumpBolusType;
import main.java.ggc.pump.data.dto.BasalDTO;
import main.java.ggc.pump.data.dto.BolusDTO;
import main.java.ggc.pump.data.dto.BolusWizardDTO;
import main.java.ggc.pump.data.writer.PumpValuesWriter;
import main.java.ggc.pump.device.insulet.data.dto.AbstractRecord;
import main.java.ggc.pump.device.insulet.data.dto.data.HistoryRecord;
import main.java.ggc.pump.device.insulet.data.dto.data.LogRecord;
import main.java.ggc.pump.device.insulet.data.dto.data.PumpAlarmRecord;
import main.java.ggc.pump.device.insulet.data.enums.AlarmType;
import main.java.ggc.pump.device.insulet.data.enums.BGFlags;
import main.java.ggc.pump.device.insulet.data.enums.LogType;
import main.java.ggc.pump.device.insulet.util.InsuletUtil;
import main.java.ggc.pump.util.DataAccessPump;

/**
 * Created by andy on 21.05.15.
 */
public class OmnipodDataConverter implements OmnipodConverter
{

    private static final Logger LOG = LoggerFactory.getLogger(OmnipodDataConverter.class);

    OutputWriter outputWriter;
    BitUtils bitUtils;
    PumpValuesWriter pumpValuesWriter;
    DataAccessPump dataAccess;

    boolean displayData = false;

    private Map<String, PumpAlarmRecord> pumpAlarmRecords = new HashMap<String, PumpAlarmRecord>();


    public OmnipodDataConverter(OutputWriter writer)
    {
        this.dataAccess = DataAccessPump.getInstance();
        this.outputWriter = writer;
        this.bitUtils = InsuletUtil.getBitUtils();
        this.pumpValuesWriter = PumpValuesWriter.getInstance(this.outputWriter);
    }


    public void convert(AbstractRecord record)
    {
        switch (record.getOmnipodDataType())
        {

            case LogRecord:
                convertLogRecord(record);
                break;

            case HistoryRecord:
                convertHistoryRecord(record);
                break;

            case BasalProgramNames:
            case EEpromSettings:
            case IbfVersion:
            case ManufacturingData:
            case PdmVersion:
            case Profile:
                LOG.debug(String.format("This type %s is not supported by this converter.",
                    record.getOmnipodDataType().name()));
                break;

            // ignored
            case LogDescriptions:
                break;

            default:
            case None:
            case UnknownRecord:
            case SubRecord:
                LOG.debug(String.format("This type %s can not be converted [class=%s].", record.getOmnipodDataType(),
                    record.getClass().getSimpleName()));

                break;
        }
    }


    public void postProcessData()
    {
        // postprocess bolus
        if (bolusTemp != null)
        {
            writeBolus(bolusTemp);
            bolusTemp = null;
        }

        postProcessBasalData();
    }


    private void convertLogRecord(AbstractRecord record)
    {
        LogRecord lr = (LogRecord) record;

        if (lr.getLogType() == LogType.Unknown)
        {
            LOG.warn(String.format("LogType [id=%d] is currently not supported.", lr.getLogId()));

        }
        else if (lr.getLogType() == LogType.History)
        {
            switch (lr.getHistoryRecordType())
            {

                case BasalRate:
                    addBasalRate(lr);
                    break;

                case Suspend:
                    writeData("Event_BasalStop", lr.getATechDate(), null);
                    break;

                case Resume:
                    writeData("Event_BasalRun", lr.getATechDate(), null);
                    break;

                case Activate:
                    {
                        writeData("Event_Activate", getDate1sBefore(lr.getATechDate()), null);
                        writeData("Event_BasalRun", lr.getATechDate(), null);
                    }
                    break;

                case Deactivate:
                    {
                        writeData("Event_BasalStop", getDate1sBefore(lr.getATechDate()), null);
                        writeData("Event_Deactivate", lr.getATechDate(), null);
                    }
                    break;

                case Occlusion:
                    writeData("Error_NoDeliveryOcclusion", lr.getATechDate(), null);
                    break;

                case Download:
                    writeData("Event_Download", lr.getATechDate(), null);
                    break;

                case Carb:
                    decodeCarb(lr);
                    break;

                case Bolus:
                    decodeBolus(lr);
                    break;

                case BloodGlucose:
                    decodeBG(lr);
                    break;

                case SuggestedCalc:
                    decodeSuggestedCalc(lr);
                    break;

                case TimeChange:
                    decodeTimeChange(lr);
                    break;

                case DateChange:
                    decodeDateChange(lr);
                    break;

                case RemoteHazardAlarm:
                case Alarm:
                    decodeLogAlarm(lr);
                    break;

                // ignored
                case EndMarker:
                case TerminateBolus:
                case TerminateBasal:
                    break;

                default:
                case Unknown:
                    LOG.warn(String.format("This HistoryType cannot be processed (%s, %s)",
                        lr.getHistoryRecordType().name(), lr.getRecordType()));
                    break;

            }
        }
        else
        // if (lr.getLogType() == LogType.PumpAlarm)
        {
            LOG.warn(String.format("LogType [%s] is currently not supported.", lr.getLogType().name()));
        }

    }


    private void convertHistoryRecord(AbstractRecord record)
    {
        HistoryRecord lr = (HistoryRecord) record;

        if (lr.getLogType() == LogType.PumpAlarm)
        {
            processPumpAlarmRecord(lr.getPumpAlarmData());
        }
        else
        {
            LOG.warn(String.format("HistoryType [%s] is currently not supported.", lr.getLogType().name()));
        }
    }


    private void processPumpAlarmRecord(PumpAlarmRecord pumpAlarmRecord)
    {
        if (!this.pumpAlarmRecords.containsKey(pumpAlarmRecord.getCombinedId()))
        {
            AlarmType alarmType = pumpAlarmRecord.getAlarmType();

            if (alarmType.getGGCEventKey() != null)
            {
                writeData(alarmType.getGGCEventKey(), pumpAlarmRecord.getATechDate(), alarmType.getGGCEventData());

                this.pumpAlarmRecords.put(pumpAlarmRecord.getCombinedId(), pumpAlarmRecord);
            }
        }
    }


    private void decodeLogAlarm(LogRecord lr)
    {
        PumpAlarmRecord pumpAlarmRecord = new PumpAlarmRecord(lr.rawData,
                PumpAlarmRecord.PumpAlarmRecordSource.LogRecord);

        processPumpAlarmRecord(pumpAlarmRecord);
    }


    private void decodeDateChange(LogRecord lr)
    {
        int day = lr.getByte(31);
        int month = lr.getByte(32);
        int year = lr.getIntInverted(33);

        String date = DataAccess.getLeadingZero(day, 2) + "." + DataAccess.getLeadingZero(month, 2) + "." + year;

        writeData("Event_DateSet", lr.getATechDate(), date);
    }


    private void decodeTimeChange(LogRecord lr)
    {
        int second = lr.getByte(31);
        int minutes = lr.getByte(32);
        int hour = lr.getByte(33);

        String time = DataAccess.getLeadingZero(hour, 2) + ":" + DataAccess.getLeadingZero(minutes, 2) + ":"
                + DataAccess.getLeadingZero(second, 2);

        writeData("Event_TimeSet", lr.getATechDate(), time);
    }


    private ATechDate getDate1sBefore(ATechDate dateTime)
    {
        ATechDate d = dateTime.clone();
        d.add(Calendar.SECOND, -1);

        return d;
    }


    private void decodeSuggestedCalc(LogRecord lr)
    {
        int offset = 31;

        Float correction_delivered = toDecimal(lr.getIntInverted(offset));
        Float carb_bolus_delivered = toDecimal(lr.getIntInverted(offset + 4));
        // Double correction_programmed = toDecimal(lr.getIntInverted(offset +
        // 8));
        // Double carb_bolus_programmed = toDecimal(lr.getIntInverted(offset +
        // 12));
        // Double correction_suggested = toDecimal(lr.getIntInverted(offset +
        // 16));
        // Double carb_bolus_suggested = toDecimal(lr.getIntInverted(offset +
        // 20));
        Float correction_iob = toDecimal(lr.getIntInverted(offset + 24));
        Float meal_iob = toDecimal(lr.getIntInverted(offset + 28));
        Short correction_factor_used = lr.getShortInverted(offset + 32);
        Short current_bg = lr.getShortInverted(offset + 34);
        Short target_bg = lr.getShortInverted(offset + 36);
        // Short bg_correction_threshold = lr.getShortInverted(offset + 38);
        Short carb_grams = lr.getShortInverted(offset + 40);
        Short ic_ratio_used = lr.getShortInverted(offset + 42);

        BolusWizardDTO dto = new BolusWizardDTO();

        dto.atechDate = lr.getATechDate();
        dto.chUnit = "g";
        dto.bloodGlucose = current_bg > 0 ? current_bg.intValue() : 0;
        dto.carbs = carb_grams > 0 ? carb_grams.intValue() : 0;
        dto.bgTargetHigh = target_bg > 0 ? target_bg.intValue() : 0;
        dto.bolusTotal = correction_delivered + carb_bolus_delivered;
        dto.foodEstimate = carb_bolus_delivered;
        dto.correctionEstimate = correction_delivered;
        dto.carbRatio = correction_factor_used > 0 ? correction_factor_used.floatValue() : 0.0f;
        dto.insulinSensitivity = ic_ratio_used > 0 ? ic_ratio_used.floatValue() : 0.0f;
        dto.unabsorbedInsulin = meal_iob + correction_iob;

        writeData("Event_BolusWizard", lr.getATechDate(), dto.getValue());
    }


    private void decodeBG(LogRecord lr)
    {
        Integer errorCode = lr.getIntInverted(31);
        Short bg = lr.getShortInverted(35);
        // String userTag1 = lr.getString(37, 24);
        // String userTag2 = lr.getString(61, 24);
        Byte flags = lr.getByte(85);

        // we ignore errored entries
        if ((errorCode != null && errorCode > 0) || //
                (flags == BGFlags.TEMPERATURE_FLAG.getCode() || //
                        flags == BGFlags.OTHER_ERROR_FLAG.getCode()))
        {
            return;
        }

        writeData("AdditionalData_BG", lr.getATechDate(), "" + bg);
    }

    BolusDTO bolusTemp = null;


    private void decodeBolus(LogRecord lr)
    {
        int bolusInt = lr.getIntInverted(31);

        if (bolusInt == 0)
            return;

        int duration = lr.getShortInverted(35);
        // int calculationRecordOffset = lr.getShortInverted(36);
        // int immediateDurationSec = lr.getShortInverted(37);

        float bolus = toDecimal(bolusInt);

        BolusDTO bolusEntry = new BolusDTO();
        bolusEntry.setDeliveredAmount(bolus);
        bolusEntry.setDuration(duration);
        bolusEntry.setATechDate(lr.getATechDate());

        if (duration == 0)
        {
            bolusEntry.setBolusType(PumpBolusType.Normal);

            if (bolusTemp == null)
            {
                writeBolus(bolusEntry);
            }
            else
            {
                ATechDate date = bolusTemp.getATechDate();
                date.add(Calendar.SECOND, -5);

                if (bolusEntry.getATechDate().after(date) || bolusEntry.getATechDate().same(date))
                {
                    bolusTemp.setATechDate(bolusEntry.getATechDate());
                    bolusTemp.setImmediateAmount(bolusEntry.getDeliveredAmount());
                    bolusTemp.setBolusType(PumpBolusType.Multiwave);

                    writeBolus(bolusTemp);
                    bolusTemp = null;
                }
                else
                {
                    writeBolus(bolusTemp);
                    bolusTemp = null;
                    writeBolus(bolusEntry);
                }
            }
        }
        else
        {
            bolusEntry.setBolusType(PumpBolusType.Extended);

            if (this.bolusTemp != null)
            {
                writeBolus(bolusTemp);
                this.bolusTemp = null;
            }

            this.bolusTemp = bolusEntry;
        }
    }


    private void writeBolus(BolusDTO bolus)
    {
        writeData(bolus.getBolusKey(), bolus.getATechDate(), bolus.getValue());
    }


    private void decodeCarb(LogRecord lr)
    {
        int carbs = lr.getShortInverted(31);
        // byte wasPreset = lr.getByte(33);
        // byte presetType = lr.getByte(34);

        writeData("AdditionalData_CH", lr.getATechDate(), "" + carbs);
    }

    List<BasalDTO> basals = new ArrayList<BasalDTO>();


    private void addBasalRate(LogRecord lr)
    {
        BasalDTO basalEntry = new BasalDTO();

        basalEntry.basalRate = toDecimal(lr.getIntInverted(31));

        basalEntry.duration = lr.getShortInverted(35);
        basalEntry.temporaryBasal = lr.getShortInverted(37);
        basalEntry.dateTime = lr.getATechDate();

        basals.add(basalEntry);
    }

    BasalDTO tbrRunning = null;


    private void postProcessBasalData()
    {
        // we need to process them for oldest to newest
        for (int i = basals.size() - 1; i > -1; i--)
        {
            convertBasalRate(basals.get(i));
        }
    }


    private void convertBasalRate(BasalDTO basalEntry)
    {
        if (basalEntry.duration == 0)
        {
            if (tbrRunning != null)
            {
                ATechDate dtEnd = tbrRunning.getPlannedTBREnd();
                ATechDate dtFinish = basalEntry.dateTime.clone();

                if (dtEnd.getTimeStringAsOnlyMinutes().equals(dtFinish.getTimeStringAsOnlyMinutes()))
                {
                    writeData("Basal_TemporaryBasalRateEnded", dtFinish, null);
                }
                else
                {
                    writeData("Basal_TemporaryBasalRateCanceled", dtFinish, null);
                }

                // tbr finished
                tbrRunning = null;
            }

            // standard Basal
            writeData("Basal_ValueChange", basalEntry.dateTime, dataAccess.getFormatedValue(basalEntry.basalRate, 2));
        }
        else
        {
            if (tbrRunning == null)
            {
                // first tbr
                tbrRunning = basalEntry;

                writeData("Basal_TemporaryBasalRate", basalEntry.dateTime,
                    String.format("TBR_VALUE=%s;TBR_UNIT=%s;DURATION=%s", basalEntry.temporaryBasal, "%",
                        DataAccessPump.getTimeFromMinutes(basalEntry.duration)));
            }
            else
            {
                // we ignore this entry now... it tells us how long TBR really
                // was, but since
                // we already displayed original value, we just wait for cancel
                // of TBR
                if (tbrRunning.duration != basalEntry.duration)
                {}

                // check changed time yes

                // no
                // ignore

            }
        }
    }


    private float toDecimal(int num)
    {
        return (num / 100.0f);
    }


    public void writeData(String key, ATechDate dateTime, String value)
    {
        this.pumpValuesWriter.writeObject(key, dateTime, value);
    }

}
