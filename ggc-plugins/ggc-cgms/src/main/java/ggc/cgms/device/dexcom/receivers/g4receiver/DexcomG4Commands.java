package ggc.cgms.device.dexcom.receivers.g4receiver;

import java.util.HashMap;

import ggc.cgms.device.dexcom.receivers.DexcomCommand;
import ggc.cgms.device.dexcom.receivers.data.CommandParameter;
import ggc.cgms.device.dexcom.receivers.g4receiver.data.parsers.ParserType;

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

public enum DexcomG4Commands implements DexcomCommand
{

    // ReadSystemTime(0x22, new short[] { 0x01, 0x06, 0x00, 0x22, 0x34, (byte)
    // 0xc0 }, 4, ParserType.DateParser,
    // CommandParameter.None), //
    // new int[] { 0x01, 0x06, 0x00, 0x1d, (byte) 0x88, 0x07 }

    ShutDownReceiver(0x2e, new short[] { 0x01, 0x06, 0x00, 0x2e, (byte) 0xb8, 0x01 }, 0, ParserType.None,
            CommandParameter.None), //
    Ack(1), //
    Nak(2), //
    Null(0), //
    InvalidCommand(3), //
    InvalidParam(4), //
    ReceiverError(6), //

    // Commands
    ReadSystemTime(0x22, 4, ParserType.IntegerParser, CommandParameter.None), //
    // ReadSystemTimeOffset(0x23, 4, ParserType.IntegerParser,
    // CommandParameter.None), //
    ReadDisplayTimeOffset(0x1d, 4, ParserType.IntegerParser, CommandParameter.None), //
    ReadLanguage(0x1b, 2, ParserType.IntegerParser, CommandParameter.None), //
    ReadGlucoseUnit(0x25, 1, ParserType.IntegerParser, CommandParameter.None), //
    ReadClockMode(0x29, 1, ParserType.IntegerParser, CommandParameter.None), //
    ReadFirmwareHeader(11, -1, ParserType.XmlParser, CommandParameter.None), //
    ReadDatabaseParitionInfo(15, -1, ParserType.XmlParser, CommandParameter.None), //
    ReadDatabasePageRange(0x10, -1, ParserType.StringUTF8Parser, CommandParameter.Byte), //
    ReadDatabasePages(0x11, -1, ParserType.None, CommandParameter.ByteIntByte), //

    ;

    // IncompletePacketReceived = 5,
    // InvalidMode = 7,
    // MaxCommand = 0x3b,

    // Ping = 10,
    // ReadBatteryLevel = 0x21,
    // ReadBatteryState = 0x30,
    // ReadBlindedMode = 0x27,

    // ReadDeviceMode = 0x2b,
    // ReadDisplayTimeOffset = 0x1d,

    //

    // ReadFlashPage = 0x33,
    // ReadHardwareBoardId = 0x31,
    // ReadRTC = 0x1f,

    // ReadSystemTimeOffset = 0x23,
    // ReadTransmitterID = 0x19,
    // ResetReceiver = 0x20,

    // UNSUPPORTED

    // MaxPossibleCommand = 0xff,
    // ReadDatabasePageHeader = 0x12,

    // ReadEnableSetUpWizardFlag = 0x37,
    // ReadSetUpWizardState = 0x39,
    // EnterFirmwareUpgradeMode = 50,
    // EnterSambaAccessMode = 0x35,
    // EraseDatabase = 0x2d,
    // WriteBlindedMode = 40,
    // WriteClockMode = 0x2a,
    // WriteDisplayTimeOffset = 30,
    // WriteEnableSetUpWizardFlag = 0x38,
    // WriteFlashPage = 0x34,
    // WriteGlucoseUnit = 0x26,
    // WriteLanguage = 0x1c,
    // WritePCParameters = 0x2f,
    // WriteSetUpWizardState = 0x3a,
    // WriteSystemTime = 0x24,
    // WriteTransmitterID = 0x1a

    private Integer commandId;
    short[] commandAsBytes;

    private CommandParameter commandParameter = CommandParameter.None;

    private ParserType parserType = ParserType.None;
    private int expectedResponseLength = 0; // 0 = no result, 1-x = 1-x bytes,
                                            // -1 number specified only in
                                            // package

    private static HashMap<Integer, DexcomG4Commands> map = new HashMap<Integer, DexcomG4Commands>();

    static
    {
        for (DexcomG4Commands el : values())
        {
            map.put(el.getCommandId(), el);
        }
    }


    private DexcomG4Commands(int commandId)
    {
        this.commandId = commandId;
    }


    private DexcomG4Commands(int commandId, ParserType parserType)
    {
        this.commandId = commandId;
        this.parserType = parserType;
    }


    private DexcomG4Commands(int commandId, int expectedResponseLength)
    {
        this.commandId = commandId;
        this.expectedResponseLength = expectedResponseLength;
    }


    private DexcomG4Commands(int commandId, int expectedResultLength, ParserType parserType,
            CommandParameter commandParameter)
    {
        this(commandId, null, expectedResultLength, parserType, commandParameter);
    }


    private DexcomG4Commands(short command[], int expectedResultLength, ParserType parserType,
            CommandParameter commandParameter)
    {
        this(null, command, expectedResultLength, parserType, commandParameter);
    }


    private DexcomG4Commands(Integer commandId, short command[], int expectedResultLength, ParserType parserType,
            CommandParameter commandParameter)
    {
        this.commandId = commandId;
        commandAsBytes = command;
        this.expectedResponseLength = expectedResultLength;
        this.parserType = parserType;
        this.commandParameter = commandParameter;
    }


    public short[] getCommandAsBytes()
    {
        return commandAsBytes;
    }


    public static DexcomG4Commands getCommandById(int id)
    {
        return map.get(id);
    }


    public int getExpectedResponseLength()
    {
        return expectedResponseLength;
    }


    public ParserType getParserType()
    {
        return parserType;
    }


    public int getCommandId()
    {
        return commandId;
    }


    public CommandParameter getCommandParameter()
    {
        return this.commandParameter;
    }

}
