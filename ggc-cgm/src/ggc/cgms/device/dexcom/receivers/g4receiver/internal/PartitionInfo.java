package ggc.cgms.device.dexcom.receivers.g4receiver.internal;

import ggc.cgms.device.dexcom.receivers.g4receiver.enums.ReceiverRecordType;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class PartitionInfo
{

    int schemaVersion;
    int pageHeaderVersion;
    int pageDataLength;
    private List<Partition> partitions = new ArrayList<Partition>();
    private HashMap<ReceiverRecordType, Partition> partitionsMap = new HashMap<ReceiverRecordType, Partition>();

    public int getSchemaVersion()
    {
        return this.schemaVersion;
    }

    public void setSchemaVersion(int schemaVersion)
    {
        this.schemaVersion = schemaVersion;
    }

    public int getPageHeaderVersion()
    {
        return this.pageHeaderVersion;
    }

    public void setPageHeaderVersion(int pageHeaderVersion)
    {
        this.pageHeaderVersion = pageHeaderVersion;
    }

    public int getPageDataLength()
    {
        return this.pageDataLength;
    }

    public void setPageDataLength(int pageDataLength)
    {
        this.pageDataLength = pageDataLength;
    }

    public List<Partition> getPartitions()
    {
        return this.partitions;
    }

    public void addPartition(Partition p)
    {
        this.partitions.add(p);
        this.partitionsMap.put(p.getRecordType(), p);
    }

    public Partition getPartitionByRecordType(ReceiverRecordType recordType)
    {
        return this.partitionsMap.get(recordType);
    }

}
