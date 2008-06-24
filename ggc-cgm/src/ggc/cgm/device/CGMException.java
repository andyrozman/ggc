/*
 * Created on 10.08.2002
 *
 * To change this generated comment edit the template variable "filecomment":
 * Window>Preferences>Java>Templates.
 */

package ggc.cgm.device;


/**
 * @author stephan
 *
 * To change this generated comment edit the template variable "typecomment":
 * Window>Preferences>Java>Templates.
 */
public class CGMException extends Exception
{
	static final long serialVersionUID = 0;

	
    /**
     * Constructor for ImportException.
     */
    public CGMException()
    {
        super();
    }

    /**
     * Constructor for ImportException.
     * @param message
     */
    public CGMException(String message)
    {
        super(message);
    }

    /**
     * Constructor for ImportException.
     * @param message
     * @param cause
     */
    public CGMException(String message, Throwable cause)
    {
        super(message, cause);
    }

    /**
     * Constructor for ImportException.
     * @param cause
     */
    public CGMException(Throwable cause)
    {
        super(cause);
    }

}
