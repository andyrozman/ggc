/*
 *  GGC - GNU Gluco Control
 *
 *  A pure java app to help you manage your diabetes.
 *
 *  See AUTHORS for copyright information.
 *
 *  This program is free software; you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation; either version 2 of the License, or
 *  (at your option) any later version.
 *
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with this program; if not, write to the Free Software
 *  Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA
 *
 *  Filename: DateTimeError.java
 *  Purpose:  signals there was an error to do with date/time
 *
 *  Author:   rumbi
 */

package ggc.errors;

/**
 * DateTimeError is used to signal there was an error with some date or time.
 * 
 * Created on 11.03.2006 by user reini
 * 
 * @author Reinhold Rumberger
 */
public class DateTimeError extends Error 
{

    /**
     * Eclipse says this class is serializable and therefore should have this
     * field.
     */
    static final long serialVersionUID = 1L;

    public DateTimeError(String msg) 
    {
        super(msg);
    }
}
