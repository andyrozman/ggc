package ggc.cgms.device.dexcom.receivers.g4receiver.converter;

import java.util.ArrayList;
import java.util.List;

import ggc.cgms.device.dexcom.receivers.g4receiver.internal.DatabasePage;
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
public class BytesToDatabasePagesConverter
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

    static BytesToDatabasePageHeaderConverter databaseHeaderConverter = new BytesToDatabasePageHeaderConverter();


    public List<DatabasePage> convert(short[] dataBytes) throws PlugInBaseException
    {

        int pagesCount = dataBytes.length / 528;

        try
        {
            List<DatabasePage> pages = new ArrayList<DatabasePage>();

            for (int i = 0; i < pagesCount; i++)
            {

                DatabasePage dp = new DatabasePage();

                short[] dataPage = new short[528];
                System.arraycopy(dataBytes, 528 * i, dataPage, 0, 528);

                short[] header = new short[28];
                System.arraycopy(dataPage, 0, header, 0, 28);

                dp.setPageHeader(databaseHeaderConverter.convert(header));
                dp.pageHeaderRaw = header;

                short[] pageContent = new short[500];
                System.arraycopy(dataPage, 28, pageContent, 0, 500);

                dp.setPageData(pageContent);

                pages.add(dp);
            }

            return pages;
        }
        catch (PlugInBaseException ex)
        {
            throw ex;
        }
        catch (Exception ex)
        {
            throw new PlugInBaseException(PlugInExceptionType.Parsing_BytesParsingError,
                    new Object[] { "DatabasePage", ex.getLocalizedMessage() }, ex);
        }

    }
}
