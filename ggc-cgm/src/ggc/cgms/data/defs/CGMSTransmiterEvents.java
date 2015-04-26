package ggc.cgms.data.defs;

import java.util.Collection;
import java.util.HashMap;

import com.atech.utils.data.CodeEnum;

/**
 *  Application: GGC - GNU Gluco Control
 *  Plug-in: CGMS Tool (support for CGMS devices)
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
 *  Filename: CGMDataType
 *  Description: CGMS Data types, as used in database (undefined at this time)
 *
 *  Author: Andy {andy@atech-software.com}
 */

public enum CGMSTransmiterEvents implements CodeEnum
{
    None(0), //
    SensorNotActive(1), //
    MinimallyEGVAberration(2), //
    NoAntenna(3), //
    Event_4(4), //
    SensorOutOfCal(5), //
    CountsAberration(6), //
    AbsoluteAberration(9), //
    PowerAberration(10), //
    RFBadStatus(12);

    private int value;
    private static HashMap<Integer, CGMSTransmiterEvents> map = new HashMap<Integer, CGMSTransmiterEvents>();

    static
    {
        for (CGMSTransmiterEvents el : values())
        {
            map.put(el.getCode(), el);
        }
    }


    CGMSTransmiterEvents(int value)
    {
        this.value = value;
    }


    public int getCode()
    {
        return value;
    }


    public void setValue(int value)
    {
        this.value = value;
    }


    public static CGMSTransmiterEvents getByCode(int value)
    {
        return map.get(value);
    }


    public static Collection<CGMSTransmiterEvents> getAllValues()
    {
        return map.values();
    }
}
