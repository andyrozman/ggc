package ggc.cgms.device.dexcom.receivers.g4receiver.internal;

import ggc.cgms.device.dexcom.receivers.g4receiver.enums.ReceiverRecordType;
import ggc.cgms.device.dexcom.receivers.g4receiver.util.DexcomException;

import java.util.Date;

public interface IGenericReceiverRecord
{

    Date getSystemDate();

    Date getDisplayDate();

    int getCrc();

    int getCurrentRecordSize() throws DexcomException;

    int getCurrentRecordVersion() throws DexcomException;

    int getImplementedRecordSize();

    ReceiverRecordType getRecordType();

}
