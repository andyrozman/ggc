package ggc.cgms.device.dexcom.receivers.g4receiver.converter;

import org.jdom.Element;

import ggc.cgms.device.dexcom.receivers.g4receiver.enums.ReceiverRecordType;
import ggc.cgms.device.dexcom.receivers.g4receiver.internal.Partition;
import ggc.cgms.device.dexcom.receivers.g4receiver.internal.PartitionInfo;
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
public class ElementToPartitionInfoConverter
{

    // <PartitionInfo SchemaVersion="1" PageHeaderVersion="1"
    // PageDataLength="500">
    // <Partition Name="ManufacturingData" Id="0" RecordRevision="1"
    // RecordLength="500" />
    // <Partition Name="FirmwareParameterData" Id="1" RecordRevision="1"
    // RecordLength="500" />
    // <Partition Name="PCSoftwareParameter" Id="2" RecordRevision="1"
    // RecordLength="500" />
    // <Partition Name="SensorData" Id="3" RecordRevision="1" RecordLength="20"
    // />
    // <Partition Name="EGVData" Id="4" RecordRevision="2" RecordLength="13" />
    // <Partition Name="CalSet" Id="5" RecordRevision="2" RecordLength="148" />
    // <Partition Name="Aberration" Id="6" RecordRevision="1" RecordLength="15"
    // />
    // <Partition Name="InsertionTime" Id="7" RecordRevision="1"
    // RecordLength="15" />
    // <Partition Name="ReceiverLogData" Id="8" RecordRevision="1"
    // RecordLength="20" />
    // <Partition Name="ReceiverErrorData" Id="9" RecordRevision="1"
    // RecordLength="500" />
    // <Partition Name="MeterData" Id="10" RecordRevision="1" RecordLength="16"
    // />
    // <Partition Name="UserEventData" Id="11" RecordRevision="1"
    // RecordLength="20" />
    // <Partition Name="UserSettingData" Id="12" RecordRevision="3"
    // RecordLength="48" />
    // </PartitionInfo>

    public PartitionInfo convert(Element xmlElement) throws PlugInBaseException
    {

        if (xmlElement == null)
            throw new PlugInBaseException(PlugInExceptionType.NoDataFoundForConversion,
                    new Object[] { "PartitionInfo" });

        try
        {
            PartitionInfo pi = new PartitionInfo();
            pi.setSchemaVersion(Integer.parseInt(xmlElement.getAttributeValue("SchemaVersion")));
            pi.setPageHeaderVersion(Integer.parseInt(xmlElement.getAttributeValue("PageHeaderVersion")));
            pi.setPageDataLength(Integer.parseInt(xmlElement.getAttributeValue("PageDataLength")));

            for (Object child : xmlElement.getChildren())
            {
                Element el = (Element) child;

                Partition p = new Partition();

                p.setId(Integer.parseInt(el.getAttributeValue("Id")));
                p.setRecordLength(Integer.parseInt(el.getAttributeValue("RecordLength")));
                p.setRecordRevision(Integer.parseInt(el.getAttributeValue("RecordRevision")));
                p.setRecordType(ReceiverRecordType.getEnum(p.getId()));

                pi.addPartition(p);
            }

            return pi;
        }
        catch (NumberFormatException ex)
        {
            throw new PlugInBaseException(PlugInExceptionType.Parsing_BytesParsingError,
                    new Object[] { "PartitionInfo", ex.getLocalizedMessage() }, ex);

        }

    }
}
