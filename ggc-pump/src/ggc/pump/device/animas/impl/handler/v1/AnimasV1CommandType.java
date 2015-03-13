package ggc.pump.device.animas.impl.handler.v1;

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
 *  Filename:     AnimasV1CommandType
 *  Description:  Command Type for Implementation V1
 *
 *  Author: Andy Rozman {andy@atech-software.com}
 */

public enum AnimasV1CommandType
{

    AlarmLog("[|0 01 00 FF FF]", 2, 19, 256, "H", "Alarms Log"),
    BolusLog("[|0 02 00 FF FF]",  3, 19, 256, "B", "Bolus Log"),
    PumpInfo("[>0 02 00 FF FF]", 4, 9, 224, "R", "Pump Settings"),
    DailyTotalsLog("[|0 00 00 FF FF]", 1, 19, 256, "D", "Daily Totals"),
    EndTestMode("t"),
    StartTestMode("[M0 00 00 00 00]"),
    ;




    private String commandString;
    private int cmdType;
    private int recordLength;
    private int recordCount;
    private String errCode;
    private String description;

    private AnimasV1CommandType(String commandString, int cmdType, int recordLength, int recordCount, String errcode, String description)
    {
        this.commandString = commandString;
        this.cmdType = cmdType;
        this.recordLength = recordLength;
        this.recordCount = recordCount;
        this.errCode = errcode;
        this.description = description;

    }


    private AnimasV1CommandType(String commandString)
    {
        this.commandString = commandString;
    }

    public String getCommandString()
    {
        return commandString;
    }

    public String getDescription()
    {
        return this.description;
    }

    public int getCommandCode()
    {
        return cmdType;
    }

    public int getRecordCount()
    {
        return recordCount;
    }

    public int getRecordLength()
    {
        return recordLength;
    }

    public String getErrorCode()
    {
        return this.errCode;
    }
}
