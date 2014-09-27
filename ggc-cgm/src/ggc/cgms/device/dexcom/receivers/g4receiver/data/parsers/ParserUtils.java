package ggc.cgms.device.dexcom.receivers.g4receiver.data.parsers;

import ggc.cgms.device.dexcom.receivers.data.CommandPacket;
import ggc.cgms.device.dexcom.receivers.g4receiver.util.DexcomException;

import java.util.HashMap;

public class ParserUtils
{

    // private static ParserUtils sParserUtils;

    private static HashMap<ParserType, DexcomCommandParserInterface> parsers = new HashMap<ParserType, DexcomCommandParserInterface>();

    static
    {
        parsers.put(ParserType.None, new EmptyParser());
        parsers.put(ParserType.DateParser, new DateParser());
        parsers.put(ParserType.IntegerParser, new IntegerParser());
        parsers.put(ParserType.StringUTF8Parser, new StringUTF8Parser());
        parsers.put(ParserType.XmlParser, new XmlParser());
    }

    private ParserUtils()
    {
    }

    // public static ParserUtils getInstance() {
    // if (ParserUtils.sParserUtils == null) {
    // ParserUtils.sParserUtils = new ParserUtils();
    // }
    //
    // return ParserUtils.sParserUtils;
    // }

    public static DexcomCommandParserInterface getParser(ParserType parserType)
    {
        return parsers.get(parserType);
    }

    public static DexcomCommandParserInterface getParser(CommandPacket cmdPacket)
    {
        return parsers.get(cmdPacket.getParserType());
    }

    public static Object parsePacketResponse(CommandPacket cmdPacket) throws DexcomException
    {
        return getParser(cmdPacket).parse(cmdPacket);
    }

}
