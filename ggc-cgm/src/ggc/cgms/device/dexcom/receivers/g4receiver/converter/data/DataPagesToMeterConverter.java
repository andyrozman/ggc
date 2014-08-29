package ggc.cgms.device.dexcom.receivers.g4receiver.converter.data;

import ggc.cgms.device.dexcom.receivers.g4receiver.converter.BytesConverterAbstract;
import ggc.cgms.device.dexcom.receivers.g4receiver.data.MeterDataRecord;
import ggc.cgms.device.dexcom.receivers.g4receiver.internal.DatabasePage;
import ggc.cgms.device.dexcom.receivers.g4receiver.internal.DatabaseRecord;
import ggc.cgms.device.dexcom.receivers.g4receiver.util.DexcomException;

import java.util.ArrayList;
import java.util.List;

public class DataPagesToMeterConverter extends BytesConverterAbstract
{
    //private static final Log log = LogFactory.getLog(DataPagesToMeterConverter.class);

    public List<MeterDataRecord> convert(List<DatabasePage> pages) throws DexcomException
    {
        ArrayList<DatabaseRecord> rawRecords = this.getRawRecords(pages, new MeterDataRecord());

        List<MeterDataRecord> records = new ArrayList<MeterDataRecord>();

        for (DatabaseRecord rawRecord : rawRecords)
        {
            short[] data = rawRecord.getData();
            MeterDataRecord mr = new MeterDataRecord();

            mr.setSystemSeconds(this.getIntFromArray(data, 0));
            mr.setDisplaySeconds(this.getIntFromArray(data, 4));
            mr.setMeterValue(this.getShortFromArray(data, 8));
            mr.setMeterTime(this.getIntFromArray(data, 10));
            mr.setCrc(this.getIntShortFromArray(data, 14));

            this.checkCrc(data, mr.getCrc());

            records.add(mr);

            //log.debug(mr);
        }

        return records;
    }

}
