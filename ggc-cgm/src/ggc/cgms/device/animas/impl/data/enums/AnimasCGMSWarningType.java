package ggc.cgms.device.animas.impl.data.enums;

import java.util.HashMap;

import com.atech.utils.data.CodeEnum;

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

    SensorFailure(203, "Alarm_SensorError"), //
    TransmiterOutOfRange(206, "Alarm_SensorLost"), //
    GlucoseBelowLimit(208, "Alarm_LowGlucose"), //
    SessionExpiresIn30Minutes(211, "Alarm_SensorEndOfLifeAproaching"), //
    SessionExpired(212, "Alarm_SessionExpired"), //
    GlucoseAboveLimit(213, "Alarm_HighGlucose"), //
    GlucoseBelow55(214, "Alarm_LowGlucose"), //
    CGMSFailure(215, "Alarm_CGMSFailure"), //
    SessionStopped(216, "Alarm_SessionStopped"), //
    GlucoseRiseRate(217, "Alarm_GlucoseRiseRateTooHigh"), //
    GlucoseFallRate(218, "Alarm_GlucoseFallRateTooHigh"), //
    UseBGForTreatment(221, "Alarm_CalibrationRequired"), //

    Unknown(-2, "Alarm_UnknownAlarm"), //
    None(0, null), //

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
    private String cgmsWriterKey;


    private AnimasCGMSWarningType(int code, String cgmsWriterKey)
    {
        this.code = code;
        this.cgmsWriterKey = cgmsWriterKey;
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


    public String getCgmsWriterKey()
    {
        return cgmsWriterKey;
    }


    public int getCode()
    {
        return this.code;
    }

}
