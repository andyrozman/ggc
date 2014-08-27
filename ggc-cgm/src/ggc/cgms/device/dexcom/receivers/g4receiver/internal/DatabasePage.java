package ggc.cgms.device.dexcom.receivers.g4receiver.internal;

public class DatabasePage
{

    public DatabasePageHeader pageHeader = new DatabasePageHeader(); // 28
    public short[] pageData = new short[0]; // 500

    public DatabasePage()
    {
    }

    public DatabasePageHeader getPageHeader()
    {
        return pageHeader;
    }

    public void setPageHeader(DatabasePageHeader pageHeader)
    {
        this.pageHeader = pageHeader;
    }

    public short[] getPageData()
    {
        return pageData;
    }

    public void setPageData(short[] pageData)
    {
        this.pageData = pageData;
    }

}
