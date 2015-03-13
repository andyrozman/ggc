package ggc.cgms.device.animas.impl.data.dto;

import com.atech.utils.data.ATechDate;
import ggc.cgms.data.defs.CGMSTransmiterEvents;


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
 *  Filename:     AnimasDexcomHistoryEntry
 *  Description:  Animas Dexcom History Entry
 *
 *  Author: Andy {andy@atech-software.com}
 */

public class AnimasDexcomHistoryEntry
{

    public static int IsDisplayOnlyEgvMask = 0x8000;
    public static int EgvValueMask = 0x3ff;
    public static int TrendArrowMask = 15;
    public static int NoiseMask = 0x70;

    public static int SpecialValueLimit = 12;

    public short glucoseValueWithFlags;
    //public byte trendArrowAndNoise;


    public short getGlucoseValueWithoutFlags()
    {
        return (short) (this.glucoseValueWithFlags & EgvValueMask);
    }

    public boolean hasGlucoseValue()
    {
        return (getGlucoseValueWithoutFlags() > SpecialValueLimit);
    }

    public Short getGlucoseValue()
    {
        if (hasGlucoseValue())
        {
            return getGlucoseValueWithoutFlags();
        }
        else
        {
            return null;
        }
    }



    public boolean getIsDisplayOnly()
    {
        return (this.glucoseValueWithFlags & IsDisplayOnlyEgvMask) != 0;
    }

//    public boolean getIsImmediateMatch()
//    {
//        return (this.trendArrowAndNoise & DexcomUtils.ImmediateMatchMask) != 0;
//    }
//
//    public NoiseMode getNoiseMode()
//    {
//        return NoiseMode.getByCode((this.trendArrowAndNoise & NoiseMask) >> 4);
//    }


    public boolean hasSpecialValue()
    {
        return (!hasGlucoseValue());
        //return (getSpecialValue()!=null);
    }


    public CGMSTransmiterEvents getSpecialValue()
    {
        CGMSTransmiterEvents sgv = CGMSTransmiterEvents.getByCode(this.getGlucoseValueWithoutFlags());

        if (sgv == null)
            return null;
        else
            return sgv;
    }

//    public TrendArrow getTrendArrow()
//    {
//        return TrendArrow.getByCode(this.trendArrowAndNoise & TrendArrowMask);
//    }


    //public int bg;
    public ATechDate dateTime;

    public String toString()
    {
        StringBuffer sb = new StringBuffer();
        sb.append("AnimasDexcomHistoryEntry [dateTime=" + dateTime.getDateTimeString() + ", bg=" + getGlucoseValueWithoutFlags());

        if (getIsDisplayOnly())
        {
            sb.append(", displayOnly=true");
        }

        CGMSTransmiterEvents spValue = getSpecialValue();

        if (spValue!=null)
        {
            sb.append(", specialValue=" + spValue.name());
        }

        sb.append("]");

        return sb.toString();
    }
}
