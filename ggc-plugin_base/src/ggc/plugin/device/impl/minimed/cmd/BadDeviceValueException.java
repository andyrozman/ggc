package ggc.plugin.device.impl.minimed.cmd;

public class BadDeviceValueException extends Exception
{
    String error_desc = null;
    
    public BadDeviceValueException(String error)
    {
        this.error_desc = error;
    }

    
    public String toString()
    {
        return "BadDeviceValueException: [" + this.error_desc + "]";
    }
    
}
