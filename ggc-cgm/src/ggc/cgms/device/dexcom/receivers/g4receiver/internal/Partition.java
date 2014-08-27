package ggc.cgms.device.dexcom.receivers.g4receiver.internal;

import ggc.cgms.device.dexcom.receivers.g4receiver.enums.ReceiverRecordType;

public class Partition
{
    // Name="UserSettingData" Id="12" RecordRevision="3"
    // RecordLength="48"

    int id;
    int recordRevision;
    int recordLength;
    ReceiverRecordType recordType;

    public int getId()
    {
        return this.id;
    }

    public void setId(int id)
    {
        this.id = id;
    }

    public int getRecordRevision()
    {
        return this.recordRevision;
    }

    public void setRecordRevision(int recordRevision)
    {
        this.recordRevision = recordRevision;
    }

    public int getRecordLength()
    {
        return this.recordLength;
    }

    public void setRecordLength(int recordLength)
    {
        this.recordLength = recordLength;
    }

    public ReceiverRecordType getRecordType()
    {
        return this.recordType;
    }

    public void setRecordType(ReceiverRecordType recordType)
    {
        this.recordType = recordType;
    }

}
