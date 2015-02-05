package ggc.cgms.device.dexcom.receivers.g4receiver.internal;

import ggc.cgms.device.dexcom.receivers.g4receiver.util.DexcomException;
import ggc.cgms.device.dexcom.receivers.g4receiver.util.DexcomUtils;
import ggc.plugin.device.PlugInBaseException;

import java.util.Date;

public abstract class GenericReceiverRecordAbstract implements IGenericReceiverRecord
{
    // private static final Log log =
    // LogFactory.getLog(GenericReceiverRecordAbstract.class);

    protected int systemSeconds;
    protected int displaySeconds;
    protected int crc;

    public abstract int getImplementedRecordVersion();

    public void checkRecordVersionAndSize()
    {
        // // FIXME
        // int currentVersion = 0; // read from partition info
        //
        // if (this.getImplementedRecordVersion() != getCurrentRecordVersion())
        // {
        // if (this.getImplementedRecordSize() != this.getCurrentRecordSize())
        // {
        // // FIXME
        // log.warn(String.format("Versions and sizes of object %s differ, possible data problem "
        // + "(versions/record size: implemented: %s/%s, device: %s/%s", //
        // this.getRecordType().name(), //
        // this.getImplementedRecordVersion(), this.getImplementedRecordSize(),
        // //
        // this.getCurrentRecordVersion(), this.getCurrentRecordSize()));
        // }
        // else
        // {
        // log.warn(String.format("Versions of object %s differ, possible data problem "
        // + "(versions: implemented: %s, device: %s", //
        // this.getRecordType().name(), //
        // this.getImplementedRecordVersion(), //
        // this.getCurrentRecordVersion()));
        // }
        //
        // }

    }

    public Date getSystemDate()
    {
        return DexcomUtils.getDateFromSeconds(this.systemSeconds);
    }

    public Date getDisplayDate()
    {
        return DexcomUtils.getDateFromSeconds(this.displaySeconds);
    }

    public int getCrc()
    {
        return this.crc;
    }

    public int getCurrentRecordSize() throws PlugInBaseException
    {
        return DexcomUtils.getPartition(this.getRecordType()).getRecordLength();
    }

    public int getCurrentRecordVersion() throws PlugInBaseException
    {
        return DexcomUtils.getPartition(this.getRecordType()).getRecordRevision();
    }

    public int getSystemSeconds()
    {
        return systemSeconds;
    }

    public void setSystemSeconds(int systemSeconds)
    {
        this.systemSeconds = systemSeconds;
    }

    public int getDisplaySeconds()
    {
        return displaySeconds;
    }

    public void setDisplaySeconds(int displaySeconds)
    {
        this.displaySeconds = displaySeconds;
    }

    public void setCrc(int crc)
    {
        this.crc = crc;
    }

}
