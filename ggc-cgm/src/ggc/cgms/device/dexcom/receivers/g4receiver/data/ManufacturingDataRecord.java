//
// Translated by CS2J (http://www.cs2j.com): 15.8.2014 0:18:17
//

package ggc.cgms.device.dexcom.receivers.g4receiver.data;

import ggc.cgms.device.dexcom.receivers.g4receiver.enums.ReceiverRecordType;

public class ManufacturingDataRecord extends XmlRecord
{
    // Version: 1  Size: 500
    // public int SystemSeconds;
    // public int DisplaySeconds;
    // [MarshalAs(UnmanagedType.ByValTStr, SizeConst=490)]
    // public string XmlData;
    // public ushort m_crc;

    public ReceiverRecordType getRecordType()
    {
        return ReceiverRecordType.ManufacturingData;
    }

    @Override
    public XmlRecord createObject()
    {
        return new ManufacturingDataRecord();
    }

}
