package ggc.pump.device;


public class PumpException extends Exception
{

	
    private static final long serialVersionUID = -2859706316077380716L;

    /**
     * Constructor for ImportException.
     */
    public PumpException()
    {
        super();
    }

    /**
     * Constructor for ImportException.
     * @param message
     */
    public PumpException(String message)
    {
        super(message);
    }

    /**
     * Constructor for ImportException.
     * @param message
     * @param cause
     */
    public PumpException(String message, Throwable cause)
    {
        super(message, cause);
    }

    /**
     * Constructor for ImportException.
     * @param cause
     */
    public PumpException(Throwable cause)
    {
        super(cause);
    }

}
