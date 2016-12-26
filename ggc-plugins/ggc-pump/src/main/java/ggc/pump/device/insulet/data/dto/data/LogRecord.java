package ggc.pump.device.insulet.data.dto.data;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.atech.utils.data.ATechDate;
import com.atech.utils.data.ATechDateType;

import ggc.pump.device.insulet.data.dto.AbstractRecord;
import ggc.pump.device.insulet.data.enums.HistoryRecordType;
import ggc.pump.device.insulet.data.enums.LogType;
import ggc.pump.device.insulet.data.enums.OmnipodDataType;

/**
 * Created by andy on 19.05.15.
 */
public class LogRecord extends AbstractRecord
{

    private static final Logger LOG = LoggerFactory.getLogger(LogRecord.class);

    // log_record: { format: 'bNSSbbsbbb.i', fields: [
    // 'logId', 'logIndex', 'recordSize', 'errorCode',
    // 'day', 'month', 'year', 'seconds', 'minutes', 'hours',
    // 'secs_since_powerup'
    // ]},

    // log_record: { format: 'bNSS bbsbbb.i', fields: [

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

    private Integer recordType;
    private HistoryRecordType historyRecordType;
    private Short flags;
    private ATechDate aTechDate;


    public LogRecord()
    {
        super(true);
    }


    @Override
    public void customProcess(int[] data)
    {
        // bNSSbbsbbb.i
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

        aTechDate = new ATechDate(day, month, year, hours, minutes, seconds, ATechDateType.DateAndTimeSec);

        if (logId == 3)
        {
            recordType = getShort2Inverted(data, 23);
            flags = getShortInverted(data, 25);

            // 29
            historyRecordType = HistoryRecordType.getByCode(recordType);
        }
        else
        {
            LOG.debug("Unknown type " + logId);
        }

    }


    @Override
    public OmnipodDataType getOmnipodDataType()
    {
        return OmnipodDataType.LogRecord;
    }


    @Override
    public String toString()
    {
        return "LogRecord [" + "logId=" + logId + ", logIndex=" + logIndex + ", recordSize=" + recordSize
                + ", errorCode=" + errorCode + ", dateTime=" + aTechDate.getDateTimeString() + ", secs_since_powerup="
                + secs_since_powerup + ", recType=" + recordType + ", historyRecordType=" + historyRecordType + "]";
    }


    public HistoryRecordType getHistoryRecordType()
    {
        return historyRecordType;
    }


    public void setHistoryRecordType(HistoryRecordType historyRecordType)
    {
        this.historyRecordType = historyRecordType;
    }


    public Byte getLogId()
    {
        return logId;
    }


    public LogType getLogType()
    {
        return logType;
    }


    public Integer getRecordType()
    {
        return recordType;
    }


    public Short getFlags()
    {
        return flags;
    }


    public void setFlags(Short flags)
    {
        this.flags = flags;
    }


    public ATechDate getATechDate()
    {
        return aTechDate;
    }

}
