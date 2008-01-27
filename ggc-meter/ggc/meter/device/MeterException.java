/*
 * Created on 10.08.2002
 *
 * To change this generated comment edit the template variable "filecomment":
 * Window>Preferences>Java>Templates.
 */

package ggc.meter.device;


/**
 * @author stephan
 *
 * To change this generated comment edit the template variable "typecomment":
 * Window>Preferences>Java>Templates.
 */
public class MeterException extends Exception
{
	static final long serialVersionUID = 0;

	
    /**
     * Constructor for ImportException.
     */
    public MeterException()
    {
        super();
    }

    /**
     * Constructor for ImportException.
     * @param message
     */
    public MeterException(String message)
    {
        super(message);
    }

    /**
     * Constructor for ImportException.
     * @param message
     * @param cause
     */
    public MeterException(String message, Throwable cause)
    {
        super(message, cause);
    }

    /**
     * Constructor for ImportException.
     * @param cause
     */
    public MeterException(Throwable cause)
    {
        super(cause);
    }

}
