package ggc.cgms.device.dexcom.receivers.g4receiver.enums;

import java.util.HashMap;

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
 *  Filename:     Dexcom 7
 *  Description:  Dexcom 7 implementation (just settings)
 *
 *  Author: Andy {andy@atech-software.com}
 */
public enum ReceiverRecordType
{
    ManufacturingData(0), //
    FirmwareParameterData(1), //
    PCSoftwareParameter(2), //
    SensorData(3), //
    EGVData(4), //
    CalSet(5), //
    Aberration(6), //

    InsertionTime(7), //
    ReceiverLogData(8), //
    ReceiverErrorData(9), //
    MeterData(10), //
    UserEventData(11), //
    UserSettingData(12), //
    MaxValue(13), //

    None(-1);

    private int value;
    private static HashMap<Integer, ReceiverRecordType> map = new HashMap<Integer, ReceiverRecordType>();
    private static HashMap<ReceiverRecordType, ReceiverRecordType> downloadSupported = new HashMap<ReceiverRecordType, ReceiverRecordType>();

    static
    {
        for (ReceiverRecordType el : values())
        {
            map.put(el.getValue(), el);
        }

        downloadSupported.put(MeterData, MeterData);
        downloadSupported.put(UserEventData, UserEventData);
        downloadSupported.put(EGVData, EGVData);
        downloadSupported.put(InsertionTime, InsertionTime);
    }


    ReceiverRecordType(int value)
    {
        this.value = value;
    }


    public int getValue()
    {
        return value;
    }


    public void setValue(int value)
    {
        this.value = value;
    }


    public static ReceiverRecordType getEnum(int value)
    {
        return map.get(value);
    }


    public static HashMap<ReceiverRecordType, ReceiverRecordType> getDownloadSupported()
    {
        return downloadSupported;
    }

}
