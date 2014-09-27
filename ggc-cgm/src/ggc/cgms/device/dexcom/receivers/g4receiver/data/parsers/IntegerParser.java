package ggc.cgms.device.dexcom.receivers.g4receiver.data.parsers;

import ggc.cgms.device.dexcom.receivers.data.CommandPacket;
import ggc.cgms.device.dexcom.receivers.g4receiver.util.DexcomException;
import ggc.cgms.device.dexcom.receivers.g4receiver.util.DexcomExceptionType;

public class IntegerParser implements DexcomCommandParserInterface
{

    public Object parse(CommandPacket cmdPacket) throws DexcomException
    {

        short[] data = cmdPacket.getResponse();

        if (data.length == 1)
            return (int) data[0];
        else if (data.length == 2)
            return data[0] & 0xFF | //
                    (data[1] & 0xFF) << 8;
        else if (data.length == 3)
            return data[0] & 0xFF | //
                    (data[1] & 0xFF) << 8 | //
                    (data[2] & 0xFF) << 16; //
        else if (data.length == 4)
            return data[0] & 0xFF | //
                    (data[1] & 0xFF) << 8 | //
                    (data[2] & 0xFF) << 16 | //
                    (data[3] & 0xFF) << 24;

        throw new DexcomException(DexcomExceptionType.ParsingErrorUnsupportedDataLenth, new Object[] { data.length,
                                                                                                      "1-4" });

    }
}
