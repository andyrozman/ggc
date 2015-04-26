package ggc.cgms.device.dexcom.receivers.g4receiver.data.parsers;

import java.util.HashMap;

import ggc.cgms.device.dexcom.receivers.data.CommandPacket;
import ggc.plugin.device.PlugInBaseException;

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


    public static Object parsePacketResponse(CommandPacket cmdPacket) throws PlugInBaseException
    {
        return getParser(cmdPacket).parse(cmdPacket);
    }

}
