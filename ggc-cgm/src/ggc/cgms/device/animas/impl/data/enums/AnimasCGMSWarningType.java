package ggc.cgms.device.animas.impl.data.enums;

import com.atech.utils.data.CodeEnum;

import java.util.HashMap;

/**
 *  Application:   GGC - GNU Gluco Control
 *  Plug-in:       CGMS Tool (support for CGMS devices)
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
 *  Filename:     AnimasCGMSWarningType
 *  Description:  Animas CGMS Warning Type
 *
 *  Author: Andy {andy@atech-software.com}
 */

public enum AnimasCGMSWarningType implements CodeEnum
{

    SensorFailure(203),


    TransmiterOutOfRange(206),


    GlucoseBelowLimit(208),

    SessionExpiresIn30Minutes(211),
    SessionExpired(212),
    GlucoseAboveLimit(213),
    GlucoseBelow55(214),
    CGMFailure(215),
    SessionStopped(216),
    GlucoseRiseRate(217),
    GlucoseFallRate(218),


    UseBGForTreatment(221),

    Unknown(-2),
    None(0),

    ;


    static HashMap<Integer, AnimasCGMSWarningType> mappingWithId = new HashMap<Integer, AnimasCGMSWarningType>();

    static
    {
        for (AnimasCGMSWarningType adt : values())
        {
            mappingWithId.put(adt.getCode(), adt);
        }
    }


    int code;

    public int getCode()
    {
        return this.code;
    }


    private AnimasCGMSWarningType(int code)
    {
        this.code = code;
    }


    public static AnimasCGMSWarningType getWarningType(int code)
    {
        if (!mappingWithId.containsKey(code))
        {
            return Unknown;
        }
        else
        {
            return mappingWithId.get(code);
        }
    }



}
