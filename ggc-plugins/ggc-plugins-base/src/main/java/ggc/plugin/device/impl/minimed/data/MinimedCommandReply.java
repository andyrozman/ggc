package ggc.plugin.device.impl.minimed.data;

import com.atech.utils.data.BitUtils;

import ggc.plugin.device.impl.minimed.enums.MinimedCommandType;
import ggc.plugin.device.impl.minimed.util.MinimedUtil;

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
 *  Filename:     MinimedCommandReply
 *  Description:  Minimed Command Reply.
 *
 *  Author: Andy {andy@atech-software.com}
 */

public class MinimedCommandReply
{

    private byte[] rawData = null;

    private MinimedCommandType commandType;

    private static BitUtils bitUtils = MinimedUtil.getBitUtils();


    public MinimedCommandReply(MinimedCommandType commandType)
    {
        this.commandType = commandType;

        this.rawData = new byte[this.commandType.getRecordLength()];
    }


    public byte[] getRawData()
    {
        return this.rawData;
    }


    public int getRawDataLength()
    {
        return this.rawData.length;
    }


    public void setRawData(byte[] rawData)
    {
        this.rawData = rawData;
    }


    public MinimedCommandType getCommandType()
    {
        return commandType;
    }


    public int getRawDataBytesAsUnsignedShort(int byte1, int byte2)
    {
        return bitUtils.makeUnsignedShort(rawData[byte1], rawData[byte2]);
    }


    public int getRawDataAsInt(int byte1)
    {
        return rawData[byte1];
    }


    public int getRawDataBytesAsInt(int byte1, int byte2, int byte3)
    {
        return bitUtils.toInt(rawData[byte1], rawData[byte2], rawData[byte3]);
    }


    public int getRawDataBytesAsInt(int byte1, int byte2)
    {
        return bitUtils.toInt(rawData[byte1], rawData[byte2]);
    }


    public String getRawDataForDebug()
    {
        return bitUtils.getHex(this.rawData);

    }


    public void resetData()
    {
        this.rawData = new byte[this.commandType.getRecordLength()];
    }

}
