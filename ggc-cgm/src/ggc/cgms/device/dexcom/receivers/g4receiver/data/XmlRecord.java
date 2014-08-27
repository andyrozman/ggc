package ggc.cgms.device.dexcom.receivers.g4receiver.data;

import ggc.cgms.device.dexcom.receivers.g4receiver.internal.GenericReceiverRecordAbstract;

public abstract class XmlRecord extends GenericReceiverRecordAbstract
{
    //  Version 1: Size = 500
    //  public uint SystemSeconds; (4)
    //  public uint DisplaySeconds; (4)
    //  [MarshalAs(UnmanagedType.ByValTStr, SizeConst=490)] (490)
    //  public string XmlData;
    //  public ushort m_crc; (2)

    private String xmlData;

    @Override
    public int getImplementedRecordVersion()
    {
        return 1;
    }

    public int getImplementedRecordSize()
    {
        return 500;
    }

    public String getXmlData()
    {
        return xmlData;
    }

    public void setXmlData(String xmlData)
    {
        this.xmlData = xmlData;
    }

    public abstract XmlRecord createObject();

}
