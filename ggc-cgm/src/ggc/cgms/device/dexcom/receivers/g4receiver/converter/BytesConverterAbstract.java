package ggc.cgms.device.dexcom.receivers.g4receiver.converter;

import ggc.cgms.device.dexcom.receivers.g4receiver.internal.DatabasePage;
import ggc.cgms.device.dexcom.receivers.g4receiver.internal.DatabaseRecord;
import ggc.cgms.device.dexcom.receivers.g4receiver.internal.IGenericReceiverRecord;
import ggc.cgms.device.dexcom.receivers.g4receiver.util.DexcomException;
import ggc.cgms.device.dexcom.receivers.g4receiver.util.DexcomExceptionType;
import ggc.cgms.device.dexcom.receivers.g4receiver.util.DexcomUtils;
import ggc.cgms.device.dexcom.receivers.g4receiver.util.DexcomUtils.BitConversion;

import java.util.ArrayList;
import java.util.List;

import com.atech.utils.data.ShortUtils;

public class BytesConverterAbstract
{
    protected ShortUtils shortUtils = new ShortUtils();

    public int getIntFromArray(short[] array, int start)
    {
        return DexcomUtils.toInt(shortUtils.getShortSubArray(array, start, 4), BitConversion.LITTLE_ENDIAN);
    }

    public int getIntShortFromArray(short[] array, int start)
    {
        return DexcomUtils.toIntShort(shortUtils.getShortSubArray(array, start, 2), BitConversion.LITTLE_ENDIAN);
    }

    public short getShortFromArray(short[] array, int start)
    {
        return DexcomUtils.toShort(shortUtils.getShortSubArray(array, start, 2), BitConversion.LITTLE_ENDIAN);
    }

    public void checkCrc(byte[] data, short crc2) throws DexcomException
    {
        int newCrc = DexcomUtils.calculateCRC16(data, 0, data.length - 2);

        if (newCrc != crc2)
        {
            throw new DexcomException(DexcomExceptionType.FailedCRCCheck, new Object[] { "DatabasePage", crc2, newCrc });
        }
    }

    public void checkCrc(short[] data, int crc2) throws DexcomException
    {
        int newCrc = DexcomUtils.calculateCRC16(data, 0, data.length - 2);

        if (newCrc != crc2)
        {
            throw new DexcomException(DexcomExceptionType.FailedCRCCheck, new Object[] { "DatabasePage", crc2, newCrc });
        }
    }

    public ArrayList<DatabaseRecord> getRawRecords(List<DatabasePage> pages, IGenericReceiverRecord template)
            throws DexcomException
    {
        ArrayList<DatabaseRecord> rawRecords = new ArrayList<DatabaseRecord>();

        for (DatabasePage page : pages)
        {
            for (int i = 0; i < page.getPageHeader().getNumberOfRecords(); i++)
            {
                DatabaseRecord rec = new DatabaseRecord();
                short[] element = new short[template.getCurrentRecordSize()];

                System.arraycopy(page.pageData, i * template.getCurrentRecordSize(), element, 0,
                    template.getCurrentRecordSize());

                //                System.arraycopy(page.pageData,
                //                    (page.getPageHeader().getFirstRecordIndex() + i) * template.getCurrentRecordSize(), element, 0,
                //                    template.getCurrentRecordSize());

                rec.setData(element);

                rawRecords.add(rec);
            }
        }

        return rawRecords;
    }

}
