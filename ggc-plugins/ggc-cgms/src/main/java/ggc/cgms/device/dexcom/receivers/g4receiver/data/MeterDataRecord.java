//
// Translated by CS2J (http://www.cs2j.com): 15.8.2014 0:18:17
//

package ggc.cgms.device.dexcom.receivers.g4receiver.data;

import java.util.Date;

import ggc.cgms.device.dexcom.receivers.g4receiver.enums.ReceiverRecordType;
import ggc.cgms.device.dexcom.receivers.g4receiver.internal.GenericReceiverRecordAbstract;
import ggc.cgms.device.dexcom.receivers.g4receiver.util.DexcomUtils;
import ggc.cgms.device.dexcom.receivers.g4receiver.util.DexcomUtils.DexcomDateParsing;

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
public class MeterDataRecord extends GenericReceiverRecordAbstract
{

    // Version: 1 Length: 16
    // public int SystemSeconds; 4
    // public int DisplaySeconds; 4
    // public ushort MeterValue; 2
    // public int MeterTime; 4
    // public ushort m_crc; // 2

    public short meterValue;
    public int meterTime;


    public MeterDataRecord()
    {
    }


    public Date getMeterTime()
    {
        return DexcomUtils.getDateFromSeconds(this.meterTime, DexcomDateParsing.DateWithDifference);
    }


    public short getMeterValue()
    {
        return this.meterValue;
    }


    public ReceiverRecordType getRecordType()
    {
        return ReceiverRecordType.MeterData;
    }


    public void setMeterValue(short meterValue)
    {
        this.meterValue = meterValue;
    }


    public void setMeterTime(int meterTime)
    {
        this.meterTime = meterTime;
    }


    public int getImplementedRecordSize()
    {
        return 16;
    }


    @Override
    public int getImplementedRecordVersion()
    {
        return 1;
    }


    @Override
    public String toString()
    {
        StringBuffer sb = new StringBuffer();
        sb.append("MeterDataRecord [");
        sb.append("MeterTime=" + DexcomUtils.getDateTimeString(getMeterTime()));
        sb.append(", MeterValue=" + this.meterValue);
        sb.append("]");

        return sb.toString();
    }

}
