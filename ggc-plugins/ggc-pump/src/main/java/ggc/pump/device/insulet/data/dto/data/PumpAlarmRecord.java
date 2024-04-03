package ggc.pump.device.insulet.data.dto.data;

// This is not real Abstract record (it's not directly processed by OmniPod), but just Dto for use for PumpAlarms (used by HistoryRecord and also by log record

import com.atech.utils.data.ATechDate;
import com.atech.utils.data.ATechDateType;

import ggc.pump.device.insulet.data.dto.AbstractRecord;
import ggc.pump.device.insulet.data.enums.AlarmType;
import ggc.pump.device.insulet.data.enums.OmnipodDataType;

public class PumpAlarmRecord extends AbstractRecord
{

    Byte day;
    Byte month;
    Short year;
    Byte seconds;
    Byte minutes;
    Byte hours;
    private ATechDate aTechDate;

    int alarm;
    private AlarmType alarmType;
    int errorCode;

    // History
    int lotNumber;
    int sequenceNumber;
    String processorVersion;
    String interlockVersion;

    // Log
    int fileNumber;
    int lineNumber;

    PumpAlarmRecordSource source;


    public PumpAlarmRecord(int[] rawdata, PumpAlarmRecordSource source)
    {
        super(true);
        this.source = source;

        process(rawdata);
    }


    @Override
    public int process(int[] data)
    {
        length = getShort(data, 0) + 2;
        rawData = data;
        this.customProcess(data);

        crc = getShort(data, this.length - 2);

        return this.length;
    }


    private void processLogRecord()
    {
        int offset = PumpAlarmRecordSource.LogRecord.getOffset();

        this.alarm = getShortInverted(offset + 8); // 39
        this.alarmType = AlarmType.getByCode(alarm);

        this.fileNumber = getShortInverted(offset + 10); // 41
        this.lineNumber = getShortInverted(offset + 12); // 43
        this.errorCode = getShortInverted(offset + 12);

    }


    private void processHistoryRecord()
    {
        int offset = PumpAlarmRecordSource.HistoryRecord.getOffset();

        this.alarm = getShortInverted(offset + 8);
        this.alarmType = AlarmType.getByCode(alarm);
        this.errorCode = getByte(offset + 11);
        this.lotNumber = getIntInverted(offset + 12);
        this.sequenceNumber = getInt(offset + 16);

        this.processorVersion = getByte(offset + 20) + "." + getByte(offset + 21) + "." + getByte(offset + 22);
        this.interlockVersion = getByte(offset + 23) + "." + getByte(offset + 24) + "." + getByte(offset + 25);
    }


    public boolean isValid()
    {
        return (this.alarmType != AlarmType.None);
    }


    @Override
    public String toString()
    {
        return new StringBuilder("PumpAlarm [") //
                .append("dateTime=").append(aTechDate.getDateTimeString()) //
                .append(", alarm=").append(alarm) //
                .append(", alarmType=").append(alarmType) //
                .append(", errorCode=").append(errorCode) //
                .append(", lotNumber=").append(lotNumber) //
                .append(", sequenceNumber=").append(sequenceNumber) //
                .append(", processorVersion=" + processorVersion) //
                .append(", interlockVersion=" + interlockVersion) //
                .append(']').toString();
    }


    @Override
    public void customProcess(int[] data)
    {
        int offset = source.getOffset();

        this.day = getByte(offset);
        this.month = getByte(offset + 1);
        this.year = getShortInverted(offset + 2);
        this.seconds = getByte(offset + 4);
        this.minutes = getByte(offset + 5);
        this.hours = getByte(offset + 6);

        aTechDate = new ATechDate(day, month, year, hours, minutes, seconds, ATechDateType.DateAndTimeSec);

        if (source == PumpAlarmRecordSource.LogRecord)
        {
            processLogRecord();
        }
        else
        {
            processHistoryRecord();
        }
    }


    public String getCombinedId()
    {
        return this.aTechDate.getATDateTimeAsLongString() + "_" + this.alarmType.name();
    }


    @Override
    public OmnipodDataType getOmnipodDataType()
    {
        return null;
    }


    public AlarmType getAlarmType()
    {
        return alarmType;
    }


    public void setAlarmType(AlarmType alarmType)
    {
        this.alarmType = alarmType;
    }


    public ATechDate getATechDate()
    {
        return aTechDate;
    }

    public static enum PumpAlarmRecordSource
    {
        LogRecord(31), //
        HistoryRecord(23); //

        int offset;


        public int getOffset()
        {
            return this.offset;
        }


        private PumpAlarmRecordSource(int offset)
        {
            this.offset = offset;
        }

    }

}
