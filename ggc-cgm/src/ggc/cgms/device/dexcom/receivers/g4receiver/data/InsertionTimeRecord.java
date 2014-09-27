package ggc.cgms.device.dexcom.receivers.g4receiver.data;

import ggc.cgms.device.dexcom.receivers.g4receiver.enums.ReceiverRecordType;
import ggc.cgms.device.dexcom.receivers.g4receiver.enums.SensorSessionState;
import ggc.cgms.device.dexcom.receivers.g4receiver.internal.GenericReceiverRecordAbstract;
import ggc.cgms.device.dexcom.receivers.g4receiver.util.DexcomException;
import ggc.cgms.device.dexcom.receivers.g4receiver.util.DexcomUtils;
import ggc.cgms.device.dexcom.receivers.g4receiver.util.DexcomUtils.DexcomDateParsing;

import java.util.Date;

public class InsertionTimeRecord extends GenericReceiverRecordAbstract
{

    // RecordRevision="1" RecordLength="15"
    // public int SystemSeconds; (4)
    // public int DisplaySeconds; (4)
    // public int InsertionTimeinSeconds; (4)
    // public SensorSessionState SessionState; (1=byte)
    // public ushort m_crc; (2)

    public static Date EmptySensorInsertionTime = DexcomUtils.toDate(Integer.MAX_VALUE);

    private int insertionTimeinSeconds;
    private SensorSessionState sensorSessionState = SensorSessionState.BadTransmitter;

    public InsertionTimeRecord() throws DexcomException
    {
        // FIXME
        // checkRecordVersionAndSize();
    }

    public Date getInsertionTime()
    {
        return DexcomUtils.getDateFromSeconds(this.insertionTimeinSeconds, DexcomDateParsing.DateWithDifference);
    }

    public void setInsertionTimeinSeconds(int insertionTimeinSeconds)
    {
        this.insertionTimeinSeconds = insertionTimeinSeconds;
    }

    public void setSensorSessionState(SensorSessionState sessionState)
    {
        this.sensorSessionState = sessionState;
    }

    public boolean getIsInserted()
    {
        return !this.getInsertionTime().equals(EmptySensorInsertionTime);
    }

    public SensorSessionState getSensorSessionState()
    {
        return this.sensorSessionState;
    }

    public ReceiverRecordType getRecordType()
    {
        return ReceiverRecordType.InsertionTime;
    }

    public int getImplementedRecordSize()
    {
        return 15;
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
        sb.append("InsertionTimeRecord [");
        sb.append(", DisplaySeconds=" + this.displaySeconds + " [" + this.getDisplayDate() + "]");
        sb.append(", InsertionTimeinSeconds=" + this.insertionTimeinSeconds + " [" + getInsertionTime() + "]");
        sb.append(", SensorSessionState=" + this.sensorSessionState.name() + "]");

        return sb.toString();
    }
}
