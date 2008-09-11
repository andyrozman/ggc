
package ggc.plugin.device;


public class PlugInBaseException extends Exception
{
    
	
    private static final long serialVersionUID = -3252275317251539876L;

    /**
     * Constructor for ImportException.
     */
    public PlugInBaseException()
    {
        super();
    }

    /**
     * Constructor for PlugInBaseException.
     * @param message
     */
    public PlugInBaseException(String message)
    {
        super(message);
    }

    /**
     * Constructor for PlugInBaseException.
     * @param message
     * @param cause
     */
    public PlugInBaseException(String message, Throwable cause)
    {
        super(message, cause);
    }

    /**
     * Constructor for PlugInBaseException.
     * @param cause
     */
    public PlugInBaseException(Throwable cause)
    {
        super(cause);
    }

}
