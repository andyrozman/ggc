package ggc.plugin.device.impl.animas.util;

import ggc.plugin.device.PlugInBaseException;

/**
 *  Application:   GGC - GNU Gluco Control
 *  Plug-in:       Pump Tool (support for Pump devices)
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
 *  Filename:     AnimasIR2020
 *  Description:  Animas IR 2020 implementation
 *
 *  Author: Andy Rozman {andy@atech-software.com}
 */

public class AnimasException extends PlugInBaseException
{

    private static final long serialVersionUID = -544500956349213288L;
    private AnimasExceptionType exceptionType;

    @Deprecated
    public AnimasException(String errorMessage, Exception e)
    {
        super(errorMessage, e);
    }

    @Deprecated
    public AnimasException(String errorMessage)
    {
        super(errorMessage);
        // TODO Auto-generated constructor stub
    }

    public AnimasException(AnimasExceptionType exceptionType)
    {
        super(AnimasException.createMessage(exceptionType, null));
        this.setExceptionType(exceptionType);
    }

    public AnimasException(AnimasExceptionType exceptionType, Exception ex)
    {
        super(AnimasException.createMessage(exceptionType, null), ex);
        this.setExceptionType(exceptionType);
    }

    public AnimasException(AnimasExceptionType exceptionType, Object[] parameters)
    {
        super(AnimasException.createMessage(exceptionType, parameters));
        this.setExceptionType(exceptionType);
    }

    public AnimasException(AnimasExceptionType exceptionType, Object[] parameters, Exception ex)
    {
        super(AnimasException.createMessage(exceptionType, parameters));
        this.setExceptionType(exceptionType);
    }

    public static String createMessage(AnimasExceptionType exceptionType, Object[] parameters)
    {

        if (parameters != null)
            return String.format(exceptionType.errorMessage, parameters);
        else
            return exceptionType.errorMessage;
    }

    public AnimasExceptionType getExceptionType()
    {
        return exceptionType;
    }

    public void setExceptionType(AnimasExceptionType exceptionType)
    {
        this.exceptionType = exceptionType;
    }

}
