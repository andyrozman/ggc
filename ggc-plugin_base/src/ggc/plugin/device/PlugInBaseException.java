package ggc.plugin.device;

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
