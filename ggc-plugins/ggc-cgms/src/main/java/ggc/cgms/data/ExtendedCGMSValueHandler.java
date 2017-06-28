package ggc.cgms.data;

import com.atech.db.ext.ExtendedEnumHandler;

/**
 *  Application:   GGC - GNU Gluco Control
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
 *  Filename:     ExtendedDailyValueHandler
 *  Description:  Extended DailyValue Handler
 * 
 *  @author Andy {andy@atech-software.com}
 */

public class ExtendedCGMSValueHandler extends ExtendedEnumHandler<ExtendedCGMSValueType>
{

    // private String extended;

    // private String[] extended_arr;

    // private boolean changed = false;
    // boolean debug = false;

    public ExtendedCGMSValueHandler()
    {
        super(ExtendedCGMSValueType.None, "CGMSValuesExtendedEntry");
    }

    // @Override
    // public String getExtendedObject()
    // {
    // return "DailyValuesRow";
    // }

}