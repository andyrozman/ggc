package ggc.cgms.device.dexcom.receivers.data;

import ggc.cgms.device.dexcom.receivers.DexcomCommand;
import ggc.cgms.device.dexcom.receivers.ReceiverApiType;
import ggc.cgms.device.dexcom.receivers.g4receiver.DexcomG4Commands;
import ggc.cgms.device.dexcom.receivers.g4receiver.data.parsers.ParserType;
import ggc.cgms.device.dexcom.receivers.g4receiver.util.DexcomException;
import ggc.cgms.device.dexcom.receivers.g4receiver.util.DexcomExceptionType;
import ggc.cgms.device.dexcom.receivers.g4receiver.util.DexcomUtils;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 *  Application:   GGC - GNU Gluco Control
 *  Plug-in:       CGMS Tool (support for Pump devices)
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
 *  Filename:     FRC_MinimedCarelink
 *  Description:  Minimed Carelink File Handler
 *
 *  Author: Andy {andy@atech-software.com}
 */

public class CommandPacket
{

    private DexcomCommand dexcomCommand;
    private DexcomG4Commands dexcomG4Command;
    ReceiverApiType recieverType;
    private ParserType parserType;
    // private ByteUtils byteUtils = new ByteUtils();

    int commandId;
    short[] command;

    short[] responseData;
    int responseCommandId;
    private int expectedResponseLength;

    // Long commandParameter = null;
    Object[] commandParameters = null;

    private DexcomException exception;
    private static Log log = LogFactory.getLog(CommandPacket.class);

    // refacotirng dexcom command, G4 R2... no store of G4 command

    public CommandPacket(DexcomG4Commands dexCommand)
    {
        this(dexCommand, null);
    }

    public CommandPacket(DexcomG4Commands dexCommand, Object[] parameters)
    {
        try
        {
            this.dexcomCommand = dexCommand;
            this.dexcomG4Command = dexCommand;
            this.recieverType = ReceiverApiType.G4_Api;
            // FIXME
            this.parserType = dexCommand.getParserType();

            expectedResponseLength = dexCommand.getExpectedResponseLength();
            this.commandParameters = parameters;

            prepareCommand();
        }
        catch (Exception ex)
        {
            log.error("Error creating command: " + ex, ex);
        }
    }

    private void prepareCommand() throws DexcomException
    {
        this.command = new short[1590];

        if (this.dexcomG4Command.getCommandAsBytes() != null)
        {
            short[] cmd = this.dexcomG4Command.getCommandAsBytes();

            for (int i = 0; i < cmd.length; i++)
            {
                this.command[i] = cmd[i];
            }

            // log.debug("Predefined command: " +
            // byteUtils.getDebugByteArray(byteUtils.getByteSubArray(this.command,
            // 0, cmd.length)));
        }
        else
        {
            createCommand();
        }

        // EXTENSION
        // support for R2 Command

    }

    private void createCommand() throws DexcomException
    {

        short[] cmd = new short[1590];
        int packetLength = 6 + this.dexcomCommand.getCommandParameter().getLengthInPacket();

        cmd[0] = 1; // Command start
        storeValueAsBytes((long) packetLength, cmd, 1, CommandParameter.Short); // Number
                                                                                // of
                                                                                // bytes
                                                                                // in
                                                                                // Command
        cmd[3] = (byte) this.dexcomCommand.getCommandId(); // Command Id

        if (this.dexcomCommand.getCommandParameter() != CommandParameter.None)
        {
            if (this.dexcomCommand.getCommandParameter() == CommandParameter.Byte || //
                    this.dexcomCommand.getCommandParameter() == CommandParameter.Int || //
                    this.dexcomCommand.getCommandParameter() == CommandParameter.Short || //
                    this.dexcomCommand.getCommandParameter() == CommandParameter.Long)
            {
                long l = (Long) this.commandParameters[0];
                storeValueAsBytes(l, cmd, 4, this.dexcomCommand.getCommandParameter()); // Parameter
            }
            else if (this.dexcomCommand.getCommandParameter() == CommandParameter.ByteIntByte)
            {
                long p = (Long) this.commandParameters[0];
                storeValueAsBytes(p, cmd, 4, CommandParameter.Byte);
                p = (Long) this.commandParameters[1];
                storeValueAsBytes(p, cmd, 5, CommandParameter.Int);
                p = (Long) this.commandParameters[2];
                storeValueAsBytes(p, cmd, 9, CommandParameter.Byte);
            }
            else
                throw new DexcomException(DexcomExceptionType.UnsupportedTypeOfParametersForCommand);
        } // for Command

        storeValueAsBytes((long) DexcomUtils.calculateCRC16(cmd, 0, packetLength - 2), cmd, packetLength - 2,
            CommandParameter.Short); // CRC

        // log.debug("Created Command: " +
        // byteUtils.getDebugByteArray(byteUtils.getByteSubArray(cmd, 0,
        // packetLength)));

        this.command = cmd;
    }

    private void storeValueAsBytes(Long parameter, short[] packet, int index, CommandParameter commandParameter)
            throws DexcomException
    {

        switch (commandParameter)
        {

            case None:
                break;

            case Byte:
                {
                    packet[index] = parameter.byteValue();
                }
                break;

            case Short:
                {
                    writeParameterToPacket(parameter.shortValue(), packet, index);
                }
                break;

            case Int:
                {
                    writeParameterToPacket(parameter.intValue(), packet, index);
                }
                break;

            case Long:
                {
                    writeParameterToPacket(parameter.longValue(), packet, index);
                }
                break;

            default:
                throw new DexcomException(DexcomExceptionType.UnsupportedTypeOfParametersForCommand);

        }

    }

    private void writeParameterToPacket(short parameter, short[] packet, int index)
    {
        // packet[index] = (byte)(parameter & 0xff);
        // packet[index + 1] = (byte)((parameter >> 8) & 0xff);

        packet[index] = (short) (parameter & 0xff);
        packet[index + 1] = (short) (parameter >> 8 & 0xff); // (byte)((parameter
                                                             // >> 8) & 0xff);

        // packet[index + 1] = (byte)(parameter & 0xff);
        // packet[index] = (byte)((parameter >> 8) & 0xff);

    }

    // private short getByteValuedd(long value)
    // {
    // // problem should 1 6 0 15 251 53
    // // is 1 6 0 15 -5 53
    //
    // int val = (int) value;
    //
    // byte b = (byte) val;
    //
    // //log.debug("Byte: " + value + ", byte=" + b);
    //
    // //log.debug(new Integer(value).)
    //
    // if (b < 0)
    // {
    // return (b + 256);
    // }
    // else
    // {
    // return b;
    // }
    //
    // }

    private void writeParameterToPacket(int parameter, short[] packet, int index)
    {
        packet[index] = (short) (parameter & 0xff);
        packet[index + 1] = (short) (parameter >> 8 & 0xff);
        packet[index + 2] = (short) (parameter >> 0x10 & 0xffL);
        packet[index + 3] = (short) (parameter >> 0x18 & 0xffL);
    }

    private void writeParameterToPacket(long parameter, short[] packet, int index)
    {
        packet[index] = (short) (parameter & 0xff);
        packet[index + 1] = (short) (parameter >> 8 & 0xff);
        packet[index + 2] = (short) (parameter >> 0x10 & 0xffL);
        packet[index + 3] = (short) (parameter >> 0x18 & 0xffL);
        packet[index + 4] = (short) (parameter >> 0x20 & 0xffL);
        packet[index + 5] = (short) (parameter >> 40 & 0xffL);
        packet[index + 6] = (short) (parameter >> 0x30 & 0xffL);
        packet[index + 7] = (short) (parameter >> 0x38 & 0xffL);
    }

    public int getCommandId()
    {
        return commandId;
    }

    public void setCommandId(int commandId)
    {
        this.commandId = commandId;
    }

    public short[] getCommand()
    {
        return command;
    }

    public short[] getResponse()
    {
        return responseData;
    }

    public void setResponse(short[] destinationArray)
    {
        this.responseData = destinationArray;
    }

    public int getResponseCommandId()
    {
        return responseCommandId;
    }

    public void setResponseCommandId(int commandIdReturned)
    {
        this.responseCommandId = commandIdReturned;
    }

    public DexcomException getException()
    {
        return exception;
    }

    public void setException(DexcomException exception)
    {
        this.exception = exception;
    }

    public DexcomG4Commands getDexcomG4Command()
    {
        return dexcomG4Command;
    }

    public void setDexcomG4Command(DexcomG4Commands dexcomG4Command)
    {
        this.dexcomG4Command = dexcomG4Command;
    }

    public int getExpectedResponseLength()
    {
        return expectedResponseLength;
    }

    public void setExpectedResponseLength(int expectedResponseLength)
    {
        this.expectedResponseLength = expectedResponseLength;
    }

    public ParserType getParserType()
    {
        return parserType;
    }

    public void setParserType(ParserType parserType)
    {
        this.parserType = parserType;
    }

}
