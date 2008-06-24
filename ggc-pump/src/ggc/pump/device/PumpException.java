/*
 * Created on 10.08.2002
 *
 * To change this generated comment edit the template variable "filecomment":
 * Window>Preferences>Java>Templates.
 */

package ggc.pump.device;


/**
 * @author stephan
 *
 * To change this generated comment edit the template variable "typecomment":
 * Window>Preferences>Java>Templates.
 */
public class PumpException extends Exception
{
	static final long serialVersionUID = 0;

	
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
