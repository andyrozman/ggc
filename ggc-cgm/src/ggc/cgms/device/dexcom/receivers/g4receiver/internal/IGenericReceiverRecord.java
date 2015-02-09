package ggc.cgms.device.dexcom.receivers.g4receiver.internal;

import ggc.cgms.device.dexcom.receivers.g4receiver.enums.ReceiverRecordType;
import ggc.plugin.device.PlugInBaseException;

import java.util.Date;

public interface IGenericReceiverRecord
{

    Date getSystemDate();

    Date getDisplayDate();

    int getCrc();

    int getCurrentRecordSize() throws PlugInBaseException;

    int getCurrentRecordVersion() throws PlugInBaseException;

    int getImplementedRecordSize();

    ReceiverRecordType getRecordType();

}
