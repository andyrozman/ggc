package ggc.cgms.device.dexcom.receivers.g4receiver.converter;

import java.util.ArrayList;
import java.util.List;

import com.atech.utils.data.ShortUtils;

import ggc.cgms.device.dexcom.receivers.g4receiver.internal.DatabasePage;
import ggc.cgms.device.dexcom.receivers.g4receiver.internal.DatabaseRecord;
import ggc.cgms.device.dexcom.receivers.g4receiver.internal.IGenericReceiverRecord;
import ggc.cgms.device.dexcom.receivers.g4receiver.util.DexcomUtils;
import ggc.cgms.device.dexcom.receivers.g4receiver.util.DexcomUtils.BitConversion;
import ggc.plugin.data.enums.PlugInExceptionType;
import ggc.plugin.device.PlugInBaseException;

/**
 *  Application:   GGC - GNU Gluco Control
 *  Plug-in:       CGMS Tool (support for CGMS devices)
 *
 *  See AUTHORS for copyright information.
 *
 *  This program is free software; you can redistribute it and/or modify it under
 *  the terms of the GNU General Public License as published by the Free Software
 *  Foundation; either version 2 of the License, or (at your option) any later
 *  version.
 *
 *  This program is distributed in the hope that it will be useful, but WITHOUT
 *  ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 *  FOR A PARTICULAR PURPOSE. See the GNU General Public License for more
 *  details.
 *
 *  You should have received a copy of the GNU General Public License along with
 *  this program; if not, write to the Free Software Foundation, Inc., 59 Temple
 *  Place, Suite 330, Boston, MA 02111-1307 USA
 *
 *  Filename:     Dexcom 7
 *  Description:  Dexcom 7 implementation (just settings)
 *
 *  Author: Andy {andy@atech-software.com}
 */
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


    public void checkCrc(byte[] data, short crc2) throws PlugInBaseException
    {
        int newCrc = DexcomUtils.calculateCRC16(data, 0, data.length - 2);

        if (newCrc != crc2)
            throw new PlugInBaseException(PlugInExceptionType.FailedCRCCheck, new Object[] { "DatabasePage", crc2,
                                                                                            newCrc });
    }


    public void checkCrc(short[] data, int crc2) throws PlugInBaseException
    {
        int newCrc = DexcomUtils.calculateCRC16(data, 0, data.length - 2);

        if (newCrc != crc2)
            throw new PlugInBaseException(PlugInExceptionType.FailedCRCCheck, new Object[] { "DatabasePage", crc2,
                                                                                            newCrc });
    }


    public ArrayList<DatabaseRecord> getRawRecords(List<DatabasePage> pages, IGenericReceiverRecord template)
            throws PlugInBaseException
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

                // System.arraycopy(page.pageData,
                // (page.getPageHeader().getFirstRecordIndex() + i) *
                // template.getCurrentRecordSize(), element, 0,
                // template.getCurrentRecordSize());

                rec.setData(element);

                rawRecords.add(rec);
            }
        }

        return rawRecords;
    }

}
