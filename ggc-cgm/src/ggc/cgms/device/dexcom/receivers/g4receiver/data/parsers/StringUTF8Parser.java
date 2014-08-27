package ggc.cgms.device.dexcom.receivers.g4receiver.data.parsers;

import ggc.cgms.device.dexcom.receivers.data.CommandPacket;
import ggc.cgms.device.dexcom.receivers.g4receiver.util.DexcomUtils;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.atech.utils.data.ShortUtils;

public class StringUTF8Parser implements DexcomCommandParserInterface
{
    Log log = LogFactory.getLog(StringUTF8Parser.class);

    ShortUtils utils = DexcomUtils.getShortUtils();

    public Object parse(CommandPacket cmdPacket)
    {
        short[] responseMessage = cmdPacket.getResponse();

        log.debug("Hex message: " + utils.getDebugShortArray(responseMessage));

        String stringValue = utils.getString(responseMessage, 0, responseMessage.length);
        return stringValue;
    }

}
