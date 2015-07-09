package ggc.pump.device.insulet.data.dto.data;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.atech.utils.data.ATechDate;

import ggc.pump.device.insulet.data.dto.AbstractRecord;
import ggc.pump.device.insulet.data.enums.LogType;
import ggc.pump.device.insulet.data.enums.OmnipodDataType;

/**
 * Created by andy on 19.05.15.
 */

public class HistoryRecord extends AbstractRecord
{

    private static final Log LOG = LogFactory.getLog(HistoryRecord.class);

    // history_record: { format: 'bNSSbbsbbb.ins..', fields: [
    // 'log_id', 'log_index', 'record_size', 'error_code',
    // 'day', 'month', 'year', 'seconds', 'minutes', 'hours',
    // 'secs_since_powerup', 'rectype', 'flags'
    // this record is not the same, last two field are not existant... last one
    // is sec_since_powerup

    private Byte logId;
    private LogType logType = LogType.Unknown;
    Integer logIndex;
    Short recordSize;
    Short errorCode;
    Byte day;
    Byte month;
    Short year;
    Byte seconds;
    Byte minutes;
    Byte hours;
    Integer secs_since_powerup;
    private ATechDate aTechDate;

    private PumpAlarmRecord pumpAlarmData;


    public HistoryRecord()
    {
        super(false);
    }


    @Override
    public void customProcess(int[] data)
    {
        logId = getByte(data, 2);

        this.logType = LogType.getByCode(logId);

        logIndex = getInt(data, 3);
        recordSize = getShort(data, 7);
        errorCode = getShort(data, 9); // ?
        day = getByte(data, 11);
        month = getByte(data, 12);
        year = getShortInverted(data, 13);
        seconds = getByte(data, 15);
        minutes = getByte(data, 16);
        hours = getByte(data, 17);
        secs_since_powerup = getIntInverted(data, 19);

        aTechDate = new ATechDate(day, month, year, hours, minutes, seconds, ATechDate.ATechDateType.DateAndTimeSec);

        if (logId == 5)
        {
            this.pumpAlarmData = new PumpAlarmRecord(this.rawData, PumpAlarmRecord.PumpAlarmRecordSource.HistoryRecord);
        }
        else
        {
            LOG.debug("Unknown type " + logId);
        }

        // System.out.println(toString());

    }


    @Override
    public String toString()
    {
        StringBuilder sb = new StringBuilder("HistoryRecord [");
        sb.append("logId=").append(logId);
        sb.append(", logType=").append(logType);
        sb.append(", logIndex=").append(logIndex);
        sb.append(", recordSize=").append(recordSize);
        sb.append(", errorCode=").append(errorCode);
        sb.append(", aTechDate=").append(aTechDate);

        if (logId == 5)
        {
            sb.append(", subrecord=");
            sb.append(pumpAlarmData);
        }
        else
        {
            sb.append(", recordType=Unsupported [" + logId + "]");
        }

        sb.append("]");
        return sb.toString();
    }


    @Override
    public OmnipodDataType getOmnipodDataType()
    {
        return OmnipodDataType.HistoryRecord;
    }


    public LogType getLogType()
    {
        return logType;
    }


    public Byte getLogId()
    {
        return logId;
    }


    public PumpAlarmRecord getPumpAlarmData()
    {
        return pumpAlarmData;
    }


    public void setPumpAlarmData(PumpAlarmRecord pumpAlarmData)
    {
        this.pumpAlarmData = pumpAlarmData;
    }

    // class PumpAlarm
    // {
    //
    // Byte day;
    // Byte month;
    // Short year;
    // Byte seconds;
    // Byte minutes;
    // Byte hours;
    // private ATechDate aTechDate;
    // int alarm;
    // AlarmType alarmType;
    // int errorCode;
    // int lotNumber;
    // int sequenceNumber;
    // String processorVersion;
    // String interlockVersion;
    //
    //
    // public PumpAlarm(int[] rawdata)
    // {
    // int off = 23;
    //
    // this.day = getByte(off);
    // this.month = getByte(off + 1);
    // this.year = getShortInverted(off + 2);
    // this.seconds = getByte(off + 4);
    // this.minutes = getByte(off + 5);
    // this.hours = getByte(off + 6);
    //
    // aTechDate = new ATechDate(day, month, year, hours, minutes, seconds,
    // ATechDate.ATechDateType.DateAndTimeSec);
    //
    // this.alarm = getShortInverted(off + 8);
    // this.alarmType = AlarmType.getByCode(alarm);
    // this.errorCode = getByte(off + 11);
    // this.lotNumber = getIntInverted(off + 12);
    // this.sequenceNumber = getInt(off + 16);
    //
    // this.processorVersion = getByte(off + 20) + "." + getByte(off + 21) + "."
    // + getByte(off + 22);
    // this.interlockVersion = getByte(off + 23) + "." + getByte(off + 24) + "."
    // + getByte(off + 25);
    // }
    //
    //
    // @Override
    // public String toString()
    // {
    // return new StringBuilder("PumpAlarm [") //
    // .append("dateTime=").append(aTechDate.getDateTimeString()) //
    // .append(", alarm=").append(alarm) //
    // .append(", alarmType=").append(alarmType) //
    // .append(", errorCode=").append(errorCode) //
    // .append(", lotNumber=").append(lotNumber) //
    // .append(", sequenceNumber=").append(sequenceNumber) //
    // .append(", processorVersion=" + processorVersion) //
    // .append(", interlockVersion=" + interlockVersion) //
    // .append(']').toString();
    // }
    // }

}
