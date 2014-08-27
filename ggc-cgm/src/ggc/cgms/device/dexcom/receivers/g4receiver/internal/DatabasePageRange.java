package ggc.cgms.device.dexcom.receivers.g4receiver.internal;

public class DatabasePageRange
{

    int firstPage;
    int lastPage;

    public int getFirstPage()
    {
        return firstPage;
    }

    public void setFirstPage(int firstPage)
    {
        this.firstPage = firstPage;
    }

    public int getLastPage()
    {
        return lastPage;
    }

    public void setLastPage(int lastPage)
    {
        this.lastPage = lastPage;
    }

    public int getPagesCount()
    {
        return (lastPage + 1) - firstPage;
    }

}
