package ggc.cgms.device.dexcom.receivers.g4receiver.data.parsers;

import ggc.cgms.device.dexcom.receivers.data.CommandPacket;
import ggc.cgms.device.dexcom.receivers.g4receiver.util.DexcomException;

public class EmptyParser implements DexcomCommandParserInterface
{

    public Object parse(CommandPacket cmdPacket) throws DexcomException
    {

        return "No result received";
    }

}
