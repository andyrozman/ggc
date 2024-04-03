package ggc.cgms.device.dexcom.receivers.g4receiver.converter;

import ggc.cgms.device.dexcom.receivers.g4receiver.enums.ReceiverRecordType;
import ggc.cgms.device.dexcom.receivers.g4receiver.internal.DatabasePageHeader;
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
public class BytesToDatabasePageHeaderConverter extends BytesConverterAbstract
{

    public DatabasePageHeader convert(short[] headerBytes) throws PlugInBaseException
    {
        DatabasePageHeader dph = new DatabasePageHeader();

        dph.setFirstRecordIndex(this.getIntFromArray(headerBytes, 0));
        dph.setNumberOfRecords(this.getIntFromArray(headerBytes, 4));
        dph.setRecordType(ReceiverRecordType.getEnum(headerBytes[8]));
        dph.setRevision((byte) headerBytes[9]);
        dph.setPageNumber(this.getIntFromArray(headerBytes, 10));
        dph.setReserved2(this.getIntFromArray(headerBytes, 14));
        dph.setReserved3(this.getIntFromArray(headerBytes, 18));
        dph.setReserved4(this.getIntFromArray(headerBytes, 22));
        dph.setCrc(this.getIntShortFromArray(headerBytes, 26));

        checkCrc(headerBytes, dph.getCrc());

        return dph;
    }

}
