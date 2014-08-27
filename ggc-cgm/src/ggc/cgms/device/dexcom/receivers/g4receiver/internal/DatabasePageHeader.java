package ggc.cgms.device.dexcom.receivers.g4receiver.internal;

import ggc.cgms.device.dexcom.receivers.g4receiver.enums.ReceiverRecordType;

public class DatabasePageHeader
{
    public int firstRecordIndex;
    public int numberOfRecords;
    public ReceiverRecordType recordType = ReceiverRecordType.Aberration;
    public byte revision;
    public int pageNumber;
    public int reserved2;
    public int reserved3;
    public int reserved4;
    public int crc;

    public int getFirstRecordIndex()
    {
        return firstRecordIndex;
    }

    public void setFirstRecordIndex(int firstRecordIndex)
    {
        this.firstRecordIndex = firstRecordIndex;
    }

    public int getNumberOfRecords()
    {
        return numberOfRecords;
    }

    public void setNumberOfRecords(int numberOfRecords)
    {
        this.numberOfRecords = numberOfRecords;
    }

    public ReceiverRecordType getRecordType()
    {
        return recordType;
    }

    public void setRecordType(ReceiverRecordType recordType)
    {
        this.recordType = recordType;
    }

    public byte getRevision()
    {
        return revision;
    }

    public void setRevision(byte revision)
    {
        this.revision = revision;
    }

    public int getPageNumber()
    {
        return pageNumber;
    }

    public void setPageNumber(int pageNumber)
    {
        this.pageNumber = pageNumber;
    }

    public int getReserved2()
    {
        return reserved2;
    }

    public void setReserved2(int reserved2)
    {
        this.reserved2 = reserved2;
    }

    public int getReserved3()
    {
        return reserved3;
    }

    public void setReserved3(int reserved3)
    {
        this.reserved3 = reserved3;
    }

    public int getReserved4()
    {
        return reserved4;
    }

    public void setReserved4(int reserved4)
    {
        this.reserved4 = reserved4;
    }

    public int getCrc()
    {
        return crc;
    }

    public void setCrc(int crc)
    {
        this.crc = crc;
    }
}
