package ggc.cgms.device.dexcom.receivers.g4receiver.data.parsers;

import ggc.cgms.device.dexcom.receivers.data.CommandPacket;
import ggc.plugin.device.PlugInBaseException;


public interface DexcomCommandParserInterface
{

    // Object parse(byte[] responseMessage);

    Object parse(CommandPacket cmdPacket) throws PlugInBaseException;

    // Object getValue();

    // Date getValueAsDate();
    //
    // Integer getValueAsInt();
    //
    // Long getValueAsLong();
    //
    // Double getValueAsDouble();

}
