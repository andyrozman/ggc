package ggc.cgms.device.dexcom.receivers.g4receiver.converter.data;

import ggc.cgms.device.dexcom.receivers.g4receiver.converter.BytesConverterAbstract;
import ggc.cgms.device.dexcom.receivers.g4receiver.data.EGVRecord;
import ggc.cgms.device.dexcom.receivers.g4receiver.internal.DatabasePage;
import ggc.cgms.device.dexcom.receivers.g4receiver.internal.DatabaseRecord;
import ggc.cgms.device.dexcom.receivers.g4receiver.util.DexcomException;
import ggc.plugin.device.PlugInBaseException;

import java.util.ArrayList;
import java.util.List;

public class DataPageToEGVDataConverter extends BytesConverterAbstract
{
    // private static final Log log =
    // LogFactory.getLog(DataPageToEGVDataConverter.class);

    public List<EGVRecord> convert(List<DatabasePage> pages) throws PlugInBaseException
    {
        ArrayList<DatabaseRecord> rawRecords = this.getRawRecords(pages, new EGVRecord());

        List<EGVRecord> records = new ArrayList<EGVRecord>();

        for (DatabaseRecord rawRecord : rawRecords)
        {
            short[] data = rawRecord.getData();
            EGVRecord egv = new EGVRecord();

            egv.setSystemSeconds(this.getIntFromArray(data, 0));
            egv.setDisplaySeconds(this.getIntFromArray(data, 4));
            egv.glucoseValueWithFlags = this.getShortFromArray(data, 8);
            egv.trendArrowAndNoise = (byte) data[10];
            egv.setCrc(this.getIntShortFromArray(data, 11));

            this.checkCrc(data, egv.getCrc());

            records.add(egv);

            // log.debug(egv);
        }

        return records;
    }

}
