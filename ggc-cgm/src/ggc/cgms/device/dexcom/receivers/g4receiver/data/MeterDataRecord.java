//
// Translated by CS2J (http://www.cs2j.com): 15.8.2014 0:18:17
//

package ggc.cgms.device.dexcom.receivers.g4receiver.data;

import ggc.cgms.device.dexcom.receivers.g4receiver.enums.ReceiverRecordType;
import ggc.cgms.device.dexcom.receivers.g4receiver.internal.GenericReceiverRecordAbstract;
import ggc.cgms.device.dexcom.receivers.g4receiver.util.DexcomUtils;
import ggc.cgms.device.dexcom.receivers.g4receiver.util.DexcomUtils.DexcomDateParsing;

import java.util.Date;

public class MeterDataRecord extends GenericReceiverRecordAbstract
{

    // Version: 1 Length: 16
    // public int SystemSeconds; 4
    // public int DisplaySeconds; 4
    // public ushort MeterValue; 2
    // public int MeterTime; 4
    // public ushort m_crc; // 2

    public short meterValue;
    public int meterTime;

    public MeterDataRecord()
    {
    }

    public Date getMeterTime()
    {
        return DexcomUtils.getDateFromSeconds(this.meterTime, DexcomDateParsing.DateWithDifference);
    }

    public short getMeterValue()
    {
        return this.meterValue;
    }

    public ReceiverRecordType getRecordType()
    {
        return ReceiverRecordType.MeterData;
    }

    public void setMeterValue(short meterValue)
    {
        this.meterValue = meterValue;
    }

    public void setMeterTime(int meterTime)
    {
        this.meterTime = meterTime;
    }

    public int getImplementedRecordSize()
    {
        return 16;
    }

    @Override
    public int getImplementedRecordVersion()
    {
        return 1;
    }

    @Override
    public String toString()
    {
        StringBuffer sb = new StringBuffer();
        sb.append("MeterDataRecord [");
        sb.append("MeterTime=" + DexcomUtils.getDateTimeString(getMeterTime()));
        sb.append(", MeterValue=" + this.meterValue);
        sb.append("]");

        return sb.toString();
    }

}
