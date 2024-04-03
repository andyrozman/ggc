package ggc.cgms.device.dexcom.receivers.g4receiver.data.parsers;

import ggc.cgms.device.dexcom.receivers.data.CommandPacket;
import ggc.plugin.data.enums.PlugInExceptionType;
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
public class IntegerParser implements DexcomCommandParserInterface
{

    public Object parse(CommandPacket cmdPacket) throws PlugInBaseException
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

        throw new PlugInBaseException(PlugInExceptionType.ParsingErrorUnsupportedDataLenth, new Object[] { data.length,
                                                                                                          "1-4" });

    }
}
