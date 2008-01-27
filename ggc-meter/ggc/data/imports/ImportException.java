/*
 * Created on 10.08.2002
 *
 * To change this generated comment edit the template variable "filecomment":
 * Window>Preferences>Java>Templates.
 */

package ggc.data.imports;


/**
 * @author stephan
 *
 * To change this generated comment edit the template variable "typecomment":
 * Window>Preferences>Java>Templates.
 */
public class ImportException extends Exception
{
	static final long serialVersionUID = 0;

	
    /**
     * Constructor for ImportException.
     */
    public ImportException()
    {
        super();
    }

    /**
     * Constructor for ImportException.
     * @param message
     */
    public ImportException(String message)
    {
        super(message);
    }

    /**
     * Constructor for ImportException.
     * @param message
     * @param cause
     */
    public ImportException(String message, Throwable cause)
    {
        super(message, cause);
    }

    /**
     * Constructor for ImportException.
     * @param cause
     */
    public ImportException(Throwable cause)
    {
        super(cause);
    }

}
