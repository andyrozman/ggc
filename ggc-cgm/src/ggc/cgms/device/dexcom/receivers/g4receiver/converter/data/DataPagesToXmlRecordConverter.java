package ggc.cgms.device.dexcom.receivers.g4receiver.converter.data;

import ggc.cgms.device.dexcom.receivers.g4receiver.converter.BytesConverterAbstract;
import ggc.cgms.device.dexcom.receivers.g4receiver.data.XmlRecord;
import ggc.cgms.device.dexcom.receivers.g4receiver.internal.DatabasePage;
import ggc.cgms.device.dexcom.receivers.g4receiver.internal.DatabaseRecord;
import ggc.cgms.device.dexcom.receivers.g4receiver.util.DexcomException;

import java.util.ArrayList;
import java.util.List;

public class DataPagesToXmlRecordConverter extends BytesConverterAbstract
{

    public List<XmlRecord> convert(List<DatabasePage> pages, XmlRecord template) throws DexcomException
    {
        ArrayList<DatabaseRecord> rawRecords = this.getRawRecords(pages, template);

        List<XmlRecord> records = new ArrayList<XmlRecord>();

        for (DatabaseRecord rawRecord : rawRecords)
        {
            short[] data = rawRecord.getData();

            XmlRecord xr = template.createObject();
            xr.setSystemSeconds(this.getIntFromArray(data, 0));
            xr.setDisplaySeconds(this.getIntFromArray(data, 4));
            xr.setXmlData(shortUtils.getString(data, 8, 490));
            xr.setCrc(this.getIntShortFromArray(data, 498));

            this.checkCrc(data, xr.getCrc());

            records.add(xr);

            // log.debug(itr);
        }

        return records;
    }

}
