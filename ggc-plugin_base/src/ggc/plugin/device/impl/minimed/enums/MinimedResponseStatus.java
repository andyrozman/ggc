package ggc.plugin.device.impl.minimed.enums;

import java.util.HashMap;
import java.util.Map;

import ggc.plugin.util.DataAccessPlugInBase;

/**
 *  Application:   GGC - GNU Gluco Control
 *  Plug-in:       GGC PlugIn Base (base class for all plugins)
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
 *  Filename:     MinimedResponseStatus
 *  Description:  Minimed Response Status.
 *
 *  Author: Andy {andy@atech-software.com}
 */
public enum MinimedResponseStatus
{

    ReceivedData(1), //
    ReceivingData(2), //
    RS232Mode(4), //
    SelfTestError(8), //

    Error(0x10), //
    AutoSleep(0x20), //
    FilterRepeat(0x40), //

    Acknowledge(85, true), //
    NotAcknowledge(102, true), //

    ;

    int statusCode;
    boolean isAckStatus;


    MinimedResponseStatus(int statusCode)
    {
        this(statusCode, false);
    }


    MinimedResponseStatus(int statusCode, boolean isAckStatus)
    {
        this.statusCode = statusCode;
        this.isAckStatus = isAckStatus;
    }


    public static Map<MinimedResponseStatus, String> getAllRelevantStatuses(int currentStatus)
    {
        Map<MinimedResponseStatus, String> statsList = new HashMap<MinimedResponseStatus, String>();

        for (MinimedResponseStatus stat : values())
        {
            if ((!stat.isAckStatus) && ((currentStatus & stat.getStatusCode()) != 0))
                statsList.put(stat, null);
        }

        return statsList;
    }


    public static boolean hasStatus(MinimedResponseStatus requestedStatus, int currentStatus)
    {
        Map<MinimedResponseStatus, String> allRelevantStatuses = getAllRelevantStatuses(currentStatus);

        return allRelevantStatuses.containsKey(requestedStatus);
    }


    public static boolean hasStatus(MinimedResponseStatus requestedStatus,
            Map<MinimedResponseStatus, String> allRelevantStatuses)
    {
        return allRelevantStatuses.containsKey(requestedStatus);
    }


    public static String getAllStatusesAsString(int currentStatus)
    {
        Map<MinimedResponseStatus, String> allRelevantStatuses = getAllRelevantStatuses(currentStatus);

        StringBuilder stringBuffer = new StringBuilder();

        for (MinimedResponseStatus stat : allRelevantStatuses.keySet())
        {
            DataAccessPlugInBase.appendToStringBuilder(stringBuffer, stat.name(), ", ");
        }

        return stringBuffer.toString();
    }


    public int getStatusCode()
    {
        return statusCode;
    }
}
