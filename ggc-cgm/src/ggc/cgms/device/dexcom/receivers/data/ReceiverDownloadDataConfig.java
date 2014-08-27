package ggc.cgms.device.dexcom.receivers.data;

public class ReceiverDownloadDataConfig
{
    private String key;
    private String value;
    private boolean canBeTranslated;

    public ReceiverDownloadDataConfig(String key, String value, boolean canBeTranslated)
    {
        this.key = key;
        this.value = value;
        this.canBeTranslated = canBeTranslated;
    }

    public String getKey()
    {
        return key;
    }

    public void setKey(String key)
    {
        this.key = key;
    }

    public String getValue()
    {
        return value;
    }

    public void setValue(String value)
    {
        this.value = value;
    }

    @Override
    public String toString()
    {
        return this.key + " = " + this.value;
    }

    public boolean isCanBeTranslated()
    {
        return canBeTranslated;
    }

    public void setCanBeTranslated(boolean canBeTranslated)
    {
        this.canBeTranslated = canBeTranslated;
    }

}
