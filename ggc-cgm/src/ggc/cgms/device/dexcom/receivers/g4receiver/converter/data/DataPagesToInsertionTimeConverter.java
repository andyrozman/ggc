package ggc.cgms.device.dexcom.receivers.g4receiver.converter.data;

import ggc.cgms.device.dexcom.receivers.g4receiver.converter.BytesConverterAbstract;
import ggc.cgms.device.dexcom.receivers.g4receiver.data.InsertionTimeRecord;
import ggc.cgms.device.dexcom.receivers.g4receiver.enums.SensorSessionState;
import ggc.cgms.device.dexcom.receivers.g4receiver.internal.DatabasePage;
import ggc.cgms.device.dexcom.receivers.g4receiver.internal.DatabaseRecord;
import ggc.cgms.device.dexcom.receivers.g4receiver.util.DexcomException;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class DataPagesToInsertionTimeConverter extends BytesConverterAbstract
{
    private static final Log log = LogFactory.getLog(DataPagesToInsertionTimeConverter.class);

    public List<InsertionTimeRecord> convert(List<DatabasePage> pages) throws DexcomException
    {
        ArrayList<DatabaseRecord> rawRecords = this.getRawRecords(pages, new InsertionTimeRecord());

        List<InsertionTimeRecord> records = new ArrayList<InsertionTimeRecord>();

        for (DatabaseRecord rawRecord : rawRecords)
        {
            short[] data = rawRecord.getData();
            InsertionTimeRecord itr = new InsertionTimeRecord();

            itr.setSystemSeconds(this.getIntFromArray(data, 0));
            itr.setDisplaySeconds(this.getIntFromArray(data, 4));
            itr.setInsertionTimeinSeconds(this.getIntFromArray(data, 8));
            itr.setSensorSessionState(SensorSessionState.getEnum(data[12]));
            itr.setCrc(this.getIntShortFromArray(data, 13));

            this.checkCrc(data, itr.getCrc());

            records.add(itr);

            log.debug(itr);
        }

        return records;
    }

}
