package ggc.cgms.device.dexcom.receivers.g4receiver.converter.data;

import java.util.ArrayList;
import java.util.List;

import ggc.cgms.device.dexcom.receivers.g4receiver.converter.BytesConverterAbstract;
import ggc.cgms.device.dexcom.receivers.g4receiver.data.XmlRecord;
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
public class DataPagesToXmlRecordConverter extends BytesConverterAbstract
{

    public List<XmlRecord> convert(List<DatabasePage> pages, XmlRecord template) throws PlugInBaseException
    {
        ArrayList<DatabaseRecord> rawRecords = this.getRawRecords(pages, template);

        List<XmlRecord> records = new ArrayList<XmlRecord>();

        for (DatabaseRecord rawRecord : rawRecords)
        {
            short[] data = rawRecord.getData();

            XmlRecord xr = template.createObject();
            xr.setSystemSeconds(this.getIntFromArray(data, 0));
            xr.setDisplaySeconds(this.getIntFromArray(data, 4));
            xr.setXmlData(shortUtils.getString(data, 8, 490));
            xr.setCrc(this.getIntShortFromArray(data, 498));

            this.checkCrc(data, xr.getCrc());

            records.add(xr);

            // log.debug(itr);
        }

        return records;
    }

}
