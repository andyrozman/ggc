package ggc.cgms.device.dexcom.receivers.g4receiver.data.parsers;

import ggc.cgms.device.dexcom.receivers.data.CommandPacket;

public class DateParser implements DexcomCommandParserInterface
{

    int dateAsInt;

    public Object parse(CommandPacket cmdPacket)
    {

        short[] responseMessage = cmdPacket.getResponse();

        int systemTime = responseMessage[4] & 0xFF | //
                (responseMessage[5] & 0xFF) << 8 | //
                (responseMessage[6] & 0xFF) << 16 | //
                (responseMessage[7] & 0xFF) << 24;

        this.dateAsInt = systemTime;

        return this.dateAsInt;
    }

    public Object getValue()
    {
        return dateAsInt;
    }

}
