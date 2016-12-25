package ggc.cgms.device.dexcom.receivers.g4receiver.data;

import java.util.Date;

import ggc.cgms.device.dexcom.receivers.g4receiver.enums.ReceiverRecordType;
import ggc.cgms.device.dexcom.receivers.g4receiver.enums.SensorSessionState;
import ggc.cgms.device.dexcom.receivers.g4receiver.internal.GenericReceiverRecordAbstract;
import ggc.cgms.device.dexcom.receivers.g4receiver.util.DexcomUtils;
import ggc.cgms.device.dexcom.receivers.g4receiver.util.DexcomUtils.DexcomDateParsing;
import ggc.plugin.device.PlugInBaseException;

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
public class InsertionTimeRecord extends GenericReceiverRecordAbstract
{

    // RecordRevision="1" RecordLength="15"
    // public int SystemSeconds; (4)
    // public int DisplaySeconds; (4)
    // public int InsertionTimeinSeconds; (4)
    // public SensorSessionState SessionState; (1=byte)
    // public ushort m_crc; (2)

    public static Date EmptySensorInsertionTime = DexcomUtils.toDate(Integer.MAX_VALUE);

    private int insertionTimeinSeconds;
    private SensorSessionState sensorSessionState = SensorSessionState.BadTransmitter;


    public InsertionTimeRecord() throws PlugInBaseException
    {
        // FIXME
        // checkRecordVersionAndSize();
    }


    public Date getInsertionTime()
    {
        return DexcomUtils.getDateFromSeconds(this.insertionTimeinSeconds, DexcomDateParsing.DateWithDifference);
    }


    public void setInsertionTimeinSeconds(int insertionTimeinSeconds)
    {
        this.insertionTimeinSeconds = insertionTimeinSeconds;
    }


    public void setSensorSessionState(SensorSessionState sessionState)
    {
        this.sensorSessionState = sessionState;
    }


    public boolean getIsInserted()
    {
        return !this.getInsertionTime().equals(EmptySensorInsertionTime);
    }


    public SensorSessionState getSensorSessionState()
    {
        return this.sensorSessionState;
    }


    public ReceiverRecordType getRecordType()
    {
        return ReceiverRecordType.InsertionTime;
    }


    public int getImplementedRecordSize()
    {
        return 15;
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
        sb.append("InsertionTimeRecord [");
        sb.append(", DisplaySeconds=" + this.displaySeconds + " [" + this.getDisplayDate() + "]");
        sb.append(", InsertionTimeinSeconds=" + this.insertionTimeinSeconds + " [" + getInsertionTime() + "]");
        sb.append(", SensorSessionState=" + this.sensorSessionState.name() + "]");

        return sb.toString();
    }
}
