package ggc.cgms.device.dexcom.receivers.g4receiver.data.parsers;

import ggc.cgms.device.dexcom.receivers.data.CommandPacket;
import ggc.cgms.device.dexcom.receivers.g4receiver.util.DexcomException;
import ggc.cgms.device.dexcom.receivers.g4receiver.util.DexcomUtils;

import org.jdom.Element;

import com.atech.utils.data.ShortUtils;

public class XmlParser implements DexcomCommandParserInterface
{
    private ShortUtils utils = DexcomUtils.getShortUtils();

    public Object parse(CommandPacket cmdPacket) throws DexcomException
    {
        short[] responseMessage = cmdPacket.getResponse();

        String stringValue = utils.getString(responseMessage, 0, responseMessage.length);
        stringValue = stringValue.replaceAll("'", "\"");

        return DexcomUtils.createXmlTree(stringValue);
    }

    public String getXml(Element element)
    {
        return DexcomUtils.getXmlStringFromElement(element);
    }

}
