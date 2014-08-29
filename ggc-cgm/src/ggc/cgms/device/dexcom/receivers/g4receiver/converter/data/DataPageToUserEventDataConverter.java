package ggc.cgms.device.dexcom.receivers.g4receiver.converter.data;

import ggc.cgms.device.dexcom.receivers.g4receiver.converter.BytesConverterAbstract;
import ggc.cgms.device.dexcom.receivers.g4receiver.data.UserEventDataRecord;
import ggc.cgms.device.dexcom.receivers.g4receiver.enums.UserEvent;
import ggc.cgms.device.dexcom.receivers.g4receiver.internal.DatabasePage;
import ggc.cgms.device.dexcom.receivers.g4receiver.internal.DatabaseRecord;
import ggc.cgms.device.dexcom.receivers.g4receiver.util.DexcomException;

import java.util.ArrayList;
import java.util.List;

public class DataPageToUserEventDataConverter extends BytesConverterAbstract
{
    
    public List<UserEventDataRecord> convert(List<DatabasePage> pages) throws DexcomException
    {
        ArrayList<DatabaseRecord> rawRecords = this.getRawRecords(pages, new UserEventDataRecord());

        List<UserEventDataRecord> records = new ArrayList<UserEventDataRecord>();

        for (DatabaseRecord rawRecord : rawRecords)
        {
            short[] data = rawRecord.getData();
            UserEventDataRecord ued = new UserEventDataRecord();

            ued.setSystemSeconds(this.getIntFromArray(data, 0));
            ued.setDisplaySeconds(this.getIntFromArray(data, 4));
            ued.setEventType(UserEvent.getEnum(data[8]));
            ued.setEventSubType((byte) data[9]);
            ued.setEventTime(this.getIntFromArray(data, 10));
            ued.setEventValue(this.getIntFromArray(data, 14));
            ued.setCrc(this.getIntShortFromArray(data, 18));

            this.checkCrc(data, ued.getCrc());

            records.add(ued);

            //log.debug(ued);
        }

        return records;
    }

}
