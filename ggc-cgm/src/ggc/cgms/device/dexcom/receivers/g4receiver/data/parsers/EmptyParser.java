package ggc.cgms.device.dexcom.receivers.g4receiver.data.parsers;

import ggc.cgms.device.dexcom.receivers.data.CommandPacket;
import ggc.plugin.device.PlugInBaseException;

public class EmptyParser implements DexcomCommandParserInterface
{

    public Object parse(CommandPacket cmdPacket) throws PlugInBaseException
    {

        return "No result received";
    }

}
