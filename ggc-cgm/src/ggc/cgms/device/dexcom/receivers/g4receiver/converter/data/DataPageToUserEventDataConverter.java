package ggc.cgms.device.dexcom.receivers.g4receiver.converter.data;

import java.util.ArrayList;
import java.util.List;

import ggc.cgms.device.dexcom.receivers.g4receiver.converter.BytesConverterAbstract;
import ggc.cgms.device.dexcom.receivers.g4receiver.data.UserEventDataRecord;
import ggc.cgms.device.dexcom.receivers.g4receiver.enums.UserEvent;
import ggc.cgms.device.dexcom.receivers.g4receiver.internal.DatabasePage;
import ggc.cgms.device.dexcom.receivers.g4receiver.internal.DatabaseRecord;
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
public class DataPageToUserEventDataConverter extends BytesConverterAbstract
{

    public List<UserEventDataRecord> convert(List<DatabasePage> pages) throws PlugInBaseException
    {
        ArrayList<DatabaseRecord> rawRecords = this.getRawRecords(pages, new UserEventDataRecord());

        List<UserEventDataRecord> records = new ArrayList<UserEventDataRecord>();

        for (DatabaseRecord rawRecord : rawRecords)
        {
            short[] data = rawRecord.getData();
            UserEventDataRecord ued = new UserEventDataRecord();

            ued.setSystemSeconds(this.getIntFromArray(data, 0));
            ued.setDisplaySeconds(this.getIntFromArray(data, 4));
            ued.setEventType(UserEvent.getEnum(data[8]));
            ued.setEventSubType((byte) data[9]);
            ued.setEventTime(this.getIntFromArray(data, 10));
            ued.setEventValue(this.getIntFromArray(data, 14));
            ued.setCrc(this.getIntShortFromArray(data, 18));

            this.checkCrc(data, ued.getCrc());

            records.add(ued);

            // log.debug(ued);
        }

        return records;
    }

}
