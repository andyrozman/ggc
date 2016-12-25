package ggc.cgms.device.dexcom.receivers.g4receiver.data;

import ggc.cgms.data.defs.CGMSTransmiterEvents;
import ggc.cgms.device.dexcom.receivers.g4receiver.enums.NoiseMode;
import ggc.cgms.device.dexcom.receivers.g4receiver.enums.ReceiverRecordType;
import ggc.cgms.device.dexcom.receivers.g4receiver.enums.TrendArrow;
import ggc.cgms.device.dexcom.receivers.g4receiver.internal.GenericReceiverRecordAbstract;
import ggc.cgms.device.dexcom.receivers.g4receiver.util.DexcomUtils;

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
 *  Filename:     Dexcom 7
 *  Description:  Dexcom 7 implementation (just settings)
 *
 *  Author: Andy {andy@atech-software.com}
 */
public class EGVRecord extends GenericReceiverRecordAbstract // implements
// IGenericRecord
{

    public static int IsDisplayOnlyEgvMask = 0x8000;
    public static int EgvValueMask = 0x3ff;
    public static int TrendArrowMask = 15;
    public static int NoiseMask = 0x70;

    public short glucoseValueWithFlags;
    public byte trendArrowAndNoise;


    public EGVRecord()
    {
    }


    public short getGlucoseValue()
    {
        return (short) (this.glucoseValueWithFlags & EgvValueMask);
    }


    public boolean getIsDisplayOnly()
    {
        return (this.glucoseValueWithFlags & IsDisplayOnlyEgvMask) != 0;
    }


    public boolean getIsImmediateMatch()
    {
        return (this.trendArrowAndNoise & DexcomUtils.ImmediateMatchMask) != 0;
    }


    public NoiseMode getNoiseMode()
    {
        return NoiseMode.getEnum((this.trendArrowAndNoise & NoiseMask) >> 4);
    }


    public String getSpecialValue()
    {
        CGMSTransmiterEvents sgv = CGMSTransmiterEvents.getByCode(this.getGlucoseValue());

        if (sgv == null)
            return "";
        else
            return sgv.name();
    }


    public TrendArrow getTrendArrow()
    {
        return TrendArrow.getEnum(this.trendArrowAndNoise & TrendArrowMask);
    }


    public int getImplementedRecordSize()
    {
        return 13;
    }


    public ReceiverRecordType getRecordType()
    {
        return ReceiverRecordType.EGVData;
    }


    @Override
    public int getImplementedRecordVersion()
    {
        return 2;
    }


    @Override
    public String toString()
    {
        StringBuffer sb = new StringBuffer();
        sb.append("EGV [");
        sb.append("DisplaySeconds=" + DexcomUtils.getDateTimeString(this.getDisplayDate()));
        sb.append(", GlucoseValue= " + this.getGlucoseValue());
        sb.append(", TrendArrow=" + this.getTrendArrow().name() + "]");
        // sb.append(", InsertionTimeinSeconds=" + this.insertionTimeinSeconds);
        // sb.append(", SensorSessionState=" + this.sensorSessionState.name() +
        // "]");

        return sb.toString();
    }

}
