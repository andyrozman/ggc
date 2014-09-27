package ggc.cgms.device.dexcom.receivers.g4receiver.data;

import ggc.cgms.device.dexcom.receivers.g4receiver.enums.ReceiverRecordType;

public class PCParameterRecord extends XmlRecord
{
    // Version 1 Size = 500
    // public int SystemSeconds; (4)
    // public int DisplaySeconds; (4)
    // [MarshalAs(UnmanagedType.ByValTStr, SizeConst=490)] (490)
    // public string XmlData;
    // public ushort m_crc; (2)

    public PCParameterRecord()
    {
    }

    public ReceiverRecordType getRecordType()
    {
        return ReceiverRecordType.PCSoftwareParameter;
    }

    @Override
    public XmlRecord createObject()
    {
        return new PCParameterRecord();
    }

}
