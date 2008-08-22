package ggc.meter.device;

import ggc.plugin.device.PlugInBaseException;


public class MeterException extends PlugInBaseException
{

    private static final long serialVersionUID = 5981328555320689970L;

    /**
     * Constructor for MeterException.
     */
    public MeterException()
    {
        super();
    }

    /**
     * Constructor for MeterException.
     * @param message
     */
    public MeterException(String message)
    {
        super(message);
    }

    /**
     * Constructor for MeterException.
     * @param message
     * @param cause
     */
    public MeterException(String message, Throwable cause)
    {
        super(message, cause);
    }

    /**
     * Constructor for MeterException.
     * @param cause
     */
    public MeterException(Throwable cause)
    {
        super(cause);
    }

}
