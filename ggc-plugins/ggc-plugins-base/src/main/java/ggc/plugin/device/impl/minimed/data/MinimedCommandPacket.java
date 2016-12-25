package ggc.plugin.device.impl.minimed.data;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ggc.plugin.device.impl.minimed.enums.MinimedCommandParameterType;
import ggc.plugin.device.impl.minimed.enums.MinimedCommandType;

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
 *  Filename:     MinimedCommandPacket
 *  Description:  Minimed Command Packet
 *
 *  Author: Andy {andy@atech-software.com}
 */

// Most of stuff here is ComLink specific. At later time it must be refactored,
// since most is not required for CarelinkUSB or ContourNextLink implementation.

public class MinimedCommandPacket
{

    private static final Logger LOG = LoggerFactory.getLogger(MinimedCommandPacket.class);

    public MinimedCommandType commandType;
    public MinimedCommandReply commandReply;
    private int dataCount;
    public Boolean hasSubCommands;
    public String description;

    int bytesPerRecord = 0;
    int maxRecords = 0;
    int commandTypeInternal = 0;

    public int commandParameterCount = 0;
    public byte[] commandParameters = null;
    public boolean multiXmitMode = false;


    public MinimedCommandPacket(MinimedCommandType commandType)
    {
        this.commandType = commandType;
        this.commandReply = new MinimedCommandReply(commandType);

        // parameters
        hasSubCommands = (commandType.parameterType != MinimedCommandParameterType.NoParameters);

        bytesPerRecord = commandType.getRecordLength();
        maxRecords = commandType.maxRecords;
        commandTypeInternal = commandType.command_type;
        commandParameterCount = commandType.commandParametersCount;
        commandParameters = commandType.commandParameters;
        description = commandType.commandDescription;

        // if (this.commandType == MinimedCommandType.RFPowerOn)
        // {
        // this.multiXmitMode = true;
        // }

        // System.out.println("Bytes/Records:" + bytesPerRecord + ", " +
        // maxRecords);

        if (this.commandType.parameterType == MinimedCommandParameterType.SubCommands)
        {
            commandParameterCount = 1;
            commandParameters = new byte[1];
        }
    }


    public MinimedCommandPacket createCommandPacket()
    {
        MinimedCommandPacket commandPacket = new MinimedCommandPacket(this.commandType);
        commandPacket.description = this.commandType.commandDescription + "-command packet";
        commandPacket.bytesPerRecord = 0;
        commandPacket.maxRecords = 0;
        commandPacket.commandTypeInternal = 0;
        commandPacket.commandParameterCount = 0;
        commandPacket.hasSubCommands = false;

        if (this.commandType == MinimedCommandType.RFPowerOn)
        {
            commandPacket.multiXmitMode = true;
        }

        return commandPacket;
    }


    public boolean doWeSendDataForReadyByte()
    {
        // System.out.println("xmit: " + multiXmitMode);

        return (commandParameterCount > 0);

        // if (multiXmitMode || commandParameterCount == 0)
        // {
        // return false;
        // }
        //
        // return true;
    }


    public boolean hasSubCommands()
    {
        return hasSubCommands;
    }


    public boolean canReturnData()
    {
        return ((bytesPerRecord * maxRecords) > 0);

        // LOG.debug("canReturnData: type={}, requestedPage={}",
        // this.commandType.parameterType.name(),
        // this.requestedPage);
        // if (this.commandType.parameterType !=
        // MinimedCommandParameterType.SubCommands)
        // {
        // boolean b = this.commandType.canReturnData();
        // LOG.debug("canReturnData (no sub commands): " +
        // this.commandType.canReturnData());
        // return this.commandType.canReturnData();
        // }
        // else
        // {
        // LOG.debug("canReturnData (sub commands): " + (this.requestedPage !=
        // null));
        // return (this.requestedPage != null);
        // }

        // if (!this.commandType.canReturnData())
        // return false;
        //
        // if ((this.commandType.parameterType ==
        // MinimedCommandParameterType.SubCommands) && (this.requestedPage !=
        // null))
        // return true;
        //
        // return false;

        //
        // if this.commandType.canReturnData() &&
        // (this.commandType.parameterType==MinimedCommandParameterType.SubCommands)
        //
        //
        // return this.commandType.canReturnData();
    }


    public String getFullCommandDescription()
    {
        return "Command [name=" + this.commandType.name() + ", id=" + this.commandType.commandCode + ",description="
                + this.commandType.commandDescription + (this.description == null ? "" : "-" + this.description) + "] ";
    }


    public int getResponseSize()
    {
        return bytesPerRecord * maxRecords;
    }


    public int getCommandResponseLength()
    {
        if (this.commandReply == null)
            return 0;
        else
            return this.commandReply.getRawDataLength();
    }


    public void setDataCount(int dataCount)
    {
        this.dataCount = dataCount;
    }


    public int getCommandCode()
    {
        return commandType.getCommandCode();
    }


    public int getDataCount()
    {
        return dataCount;
    }


    public boolean hasParameters()
    {
        return (commandParameterCount > 0);
    }


    @Override
    public String toString()
    {
        return new ToStringBuilder(this).append("commandType", commandType.name())
                .append("hasSubCommands", hasSubCommands).append("description", description)
                .append("bytesPerRecord", bytesPerRecord).append("maxRecords", maxRecords)
                .append("commandParameterCount", commandParameterCount).append("commandParameters", commandParameters)
                .append("multiXmitMode", multiXmitMode).append("commandTypeInternal", commandTypeInternal).toString();
    }


    public void clearReply()
    {
        this.commandReply.resetData();
    }
    // public boolean hasParameters()
    // {
    // if (commandParameters == null)
    // return false;
    // else
    // return (commandParameters.size() > 0);
    // }
    //
    //
    // public List<Integer> getCommandParameters()
    // {
    // return this.commandParameters;
    // }
}
