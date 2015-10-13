package ggc.plugin.comm.ports;

/**
 * Created by andy on 08.10.15.
 */
public class DevicePortDto
{

    private String displayValue;
    private String setValue;


    public DevicePortDto(String displayAndSetValue)
    {
        this(displayAndSetValue, displayAndSetValue);
    }


    public DevicePortDto(String displayValue, String setValue)
    {
        this.displayValue = displayValue;
        this.setValue = setValue;
    }


    public String getDisplayValue()
    {
        return this.displayValue;
    }


    public String getSetValue()
    {
        return this.setValue;
    }


    @Override
    public String toString()
    {
        return this.displayValue;
    }

}
