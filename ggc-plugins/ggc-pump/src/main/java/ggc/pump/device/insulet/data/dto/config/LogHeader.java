package main.java.ggc.pump.device.insulet.data.dto.config;

import main.java.ggc.pump.device.insulet.data.dto.AbstractRecord;
import main.java.ggc.pump.device.insulet.data.enums.OmnipodDataType;

/**
 * Created by andy on 19.05.15.
 */

public class LogHeader extends AbstractRecord
{

    // log_hdr: { format: '7bS3b.S', fields: [
    Byte logsInfoRevision;
    Byte insulinHistoryRevision;
    Byte alarmHistoryRevision;
    Byte bloodGlucoseRevision;
    Byte insuletStatsRevision;
    Byte day;
    Byte month;
    Short year;
    Byte seconds;
    Byte minutes;
    Byte hours;
    Short numLogDescriptions;


    public LogHeader()
    {
        super(false);
    }


    @Override
    public int process(int[] data)
    {
        this.length = 15;

        customProcess(data);

        return this.length;
    }


    @Override
    public void customProcess(int[] data)
    {
        logsInfoRevision = getByte(data, 2);
        insulinHistoryRevision = getByte(data, 3);
        alarmHistoryRevision = getByte(data, 4);
        bloodGlucoseRevision = getByte(data, 5);
        insuletStatsRevision = getByte(data, 6);
        day = getByte(data, 7);
        month = getByte(data, 8);
        year = getShort(data, 9);
        seconds = getByte(data, 11);
        minutes = getByte(data, 12);
        hours = getByte(data, 13);

        numLogDescriptions = getShort(data, 15);
    }


    @Override
    public String toString()
    {
        return "LogHeader [" + "logsInfoRevision=" + logsInfoRevision + ", insulinHistoryRevision="
                + insulinHistoryRevision + ", alarmHistoryRevision=" + alarmHistoryRevision + ", bloodGlucoseRevision="
                + bloodGlucoseRevision + ", insuletStatsRevision=" + insuletStatsRevision + ", day=" + day + ", month="
                + month + ", year=" + year + ", seconds=" + seconds + ", minutes=" + minutes + ", hours=" + hours
                + ", numLogDescriptions=" + numLogDescriptions + ']';
    }


    @Override
    public OmnipodDataType getOmnipodDataType()
    {
        return OmnipodDataType.SubRecord;
    }

}
