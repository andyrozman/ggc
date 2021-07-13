package ggc.plugin.device;

import ggc.plugin.data.enums.PlugInExceptionType;

/**
 *  Application:   GGC - GNU Gluco Control
 *  Plug-in:       GGC PlugIn Base (base class for all plugins)
 *
 *  See AUTHORS for copyright information.
 * 
 *  This program is free software; you can redistribute it and/or modify it under
 *  the terms of the GNU General Public License as published by the Free Software
 *  Foundation; either version 2 of the License, or (at your option) any later
 *  version.
 * 
 *  This program is distributed in the hope that it will be useful, but WITHOUT
 *  ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 *  FOR A PARTICULAR PURPOSE. See the GNU General Public License for more
 *  details.
 * 
 *  You should have received a copy of the GNU General Public License along with
 *  this program; if not, write to the Free Software Foundation, Inc., 59 Temple
 *  Place, Suite 330, Boston, MA 02111-1307 USA
 * 
 *  Filename:  ###---###  
 *  Description:
 * 
 *  Author: Andy {andy@atech-software.com}
 */

public class PlugInBaseException extends Exception
{

    private static final long serialVersionUID = -3252275317251539876L;

    /**
     * Error Code
     */
    public int errorCode = 0;

    /**
     * Error Description
     */
    public String errorDescription = "";

    public PlugInExceptionType exceptionType;


    /**
     * Constructor for Exception.
     * @deprecated
     */
    public PlugInBaseException()
    {
        super();
    }


    /**
     * Constructor for PlugInBaseException.
     * @param message
     * @deprecated
     */
    public PlugInBaseException(String message)
    {
        super(message);
    }


    /**
     * Constructor for PlugInBaseException.
     * @param message
     * @param cause
     * @deprecated
     */
    public PlugInBaseException(String message, Throwable cause)
    {
        super(message, cause);
    }


    /**
     * Constructor for PlugInBaseException.
     * @param message
     * @param cause
     * @param error_code_
     * @deprecated
     */
    public PlugInBaseException(Throwable cause, int error_code_, String message)
    {
        super(message, cause);
        this.errorCode = error_code_;
    }


    /**
     * Constructor for PlugInBaseException.
     * @param cause
     * @deprecated
     */
    public PlugInBaseException(Throwable cause)
    {
        super(cause);
    }


    /**
     * Constructor for PlugInBaseException.
     * 
     * @param errorCode
     * @deprecated
     */
    public PlugInBaseException(int errorCode)
    {
        super();
        this.errorCode = errorCode;
    }


    /**
     * Constructor for PlugInBaseException.
     *
     * @param error_msg 
     * @param error_code_
     * @deprecated
     */
    public PlugInBaseException(String error_msg, int error_code_)
    {
        super(error_msg);
        this.errorCode = error_code_;
    }


    public PlugInBaseException(PlugInExceptionType exceptionType)
    {
        super(PlugInBaseException.createMessage(exceptionType, null));
        this.setExceptionType(exceptionType);
    }


    public PlugInBaseException(PlugInExceptionType exceptionType, Throwable ex)
    {
        super(PlugInBaseException.createMessage(exceptionType, null), ex);
        this.setExceptionType(exceptionType);
    }


    public PlugInBaseException(PlugInExceptionType exceptionType, Exception ex)
    {
        super(PlugInBaseException.createMessage(exceptionType, null), ex);
        this.setExceptionType(exceptionType);
    }


    public PlugInBaseException(PlugInExceptionType exceptionType, Object[] parameters)
    {
        super(PlugInBaseException.createMessage(exceptionType, parameters));
        this.setExceptionType(exceptionType);
    }


    // public PlugInBaseException(PlugInExceptionType exceptionType,
    // Object...parameters)
    // {
    // super(PlugInBaseException.createMessage(exceptionType, parameters));
    // this.setExceptionType(exceptionType);
    // }

    public PlugInBaseException(PlugInExceptionType exceptionType, Object[] parameters, Exception ex)
    {
        super(PlugInBaseException.createMessage(exceptionType, parameters), ex);
        this.setExceptionType(exceptionType);
    }


    // public PlugInBaseException(PlugInExceptionType exceptionType, Exception ex, Object...
    // parameters)
    // {
    // super(PlugInBaseException.createMessage(exceptionType, parameters), ex);
    // this.setExceptionType(exceptionType);
    // }

    public PlugInBaseException(PlugInExceptionType exceptionType, String errorDescription, Exception ex)
    {
        // FIXME
        super(PlugInBaseException.createMessage(exceptionType, null), ex);

        this.setExceptionType(exceptionType);
    }


    public static String createMessage(PlugInExceptionType exceptionType, Object[] parameters)
    {
        if (parameters != null)
            return String.format(exceptionType.getErrorMessage(), parameters);
        else
            return exceptionType.getErrorMessage();
    }


    public PlugInExceptionType getExceptionType()
    {
        return exceptionType;
    }


    public void setExceptionType(PlugInExceptionType exceptionType)
    {
        this.exceptionType = exceptionType;
    }

}
