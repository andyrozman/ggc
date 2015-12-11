package ggc.pump.device.minimed.data;

import com.atech.utils.data.CodeEnum;

import ggc.plugin.device.impl.minimed.data.MinimedHistoryEntry;
import ggc.pump.device.minimed.data.enums.PumpHistoryEntryType;

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
 *  Filename:     PumpHistoryEntry
 *  Description:  Pump History Entry.
 *
 *  Author: Andy {andy@atech-software.com}
 */

public class PumpHistoryEntry extends MinimedHistoryEntry
{

    private PumpHistoryEntryType entryType;
    private Integer opCode; // this is set only when we have unknown entry...


    public CodeEnum getEntryType()
    {
        return (CodeEnum) entryType;
    }


    public void setEntryType(PumpHistoryEntryType entryType)
    {
        this.entryType = entryType;

        this.sizes[0] = entryType.getHeadLength();
        this.sizes[1] = entryType.getDateLength();
        this.sizes[2] = entryType.getBodyLength();
    }


    @Override
    public int getOpCode()
    {
        if (opCode == null)
            return entryType.getOpCode();
        else
            return opCode;
    }


    @Override
    public String getToStringStart()
    {
        return "PumpHistoryRecord [type=" + entryType.name() + " [" + getOpCode() + ", 0x"
                + bitUtils.getCorrectHexValue(getOpCode()) + "]";
    }


    public void setOpCode(Integer opCode)
    {
        this.opCode = opCode;
    }
}
