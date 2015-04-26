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
public enum SensorSessionState
{
    BadTransmitter(8), CountsAberration(4), EnteringManufacturingMode(9), None(0), ResidualAberration(3), SensorRemoved(
            1), SensorShutOffDueToTimeLoss(6), SessionExpired(2), SessionStarted(7), TryingToStartSecondSession(5);

    private int value;
    private static HashMap<Integer, SensorSessionState> map = new HashMap<Integer, SensorSessionState>();

    static
    {
        for (SensorSessionState el : values())
        {
            map.put(el.getValue(), el);
        }
    }


    SensorSessionState(int value)
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


    public static SensorSessionState getEnum(int value)
    {
        return map.get(value);
    }

}
