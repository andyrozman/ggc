package ggc.cgms.device.dexcom.receivers.g4receiver.converter;

import ggc.cgms.device.dexcom.receivers.g4receiver.enums.ReceiverRecordType;
import ggc.cgms.device.dexcom.receivers.g4receiver.internal.DatabasePageHeader;
import ggc.cgms.device.dexcom.receivers.g4receiver.util.DexcomException;

public class BytesToDatabasePageHeaderConverter extends BytesConverterAbstract
{

    public DatabasePageHeader convert(short[] headerBytes) throws DexcomException
    {
        DatabasePageHeader dph = new DatabasePageHeader();

        dph.setFirstRecordIndex(this.getIntFromArray(headerBytes, 0));
        dph.setNumberOfRecords(this.getIntFromArray(headerBytes, 4));
        dph.setRecordType(ReceiverRecordType.getEnum(headerBytes[8]));
        dph.setRevision((byte) headerBytes[9]);
        dph.setPageNumber(this.getIntFromArray(headerBytes, 10));
        dph.setReserved2(this.getIntFromArray(headerBytes, 14));
        dph.setReserved3(this.getIntFromArray(headerBytes, 18));
        dph.setReserved4(this.getIntFromArray(headerBytes, 22));
        dph.setCrc(this.getIntShortFromArray(headerBytes, 26));

        checkCrc(headerBytes, dph.getCrc());

        return dph;
    }

}
