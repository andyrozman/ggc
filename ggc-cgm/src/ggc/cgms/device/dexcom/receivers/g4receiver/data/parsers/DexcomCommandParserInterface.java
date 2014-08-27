package ggc.cgms.device.dexcom.receivers.g4receiver.data.parsers;

import ggc.cgms.device.dexcom.receivers.data.CommandPacket;
import ggc.cgms.device.dexcom.receivers.g4receiver.util.DexcomException;

public interface DexcomCommandParserInterface {

    // Object parse(byte[] responseMessage);

    Object parse(CommandPacket cmdPacket) throws DexcomException;

    // Object getValue();

    // Date getValueAsDate();
    //
    // Integer getValueAsInt();
    //
    // Long getValueAsLong();
    //
    // Double getValueAsDouble();

}
