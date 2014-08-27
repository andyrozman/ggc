package ggc.cgms.device.dexcom.receivers.g4receiver.converter;

import ggc.cgms.device.dexcom.receivers.g4receiver.internal.DatabasePage;
import ggc.cgms.device.dexcom.receivers.g4receiver.util.DexcomException;
import ggc.cgms.device.dexcom.receivers.g4receiver.util.DexcomExceptionType;

import java.util.ArrayList;
import java.util.List;

public class BytesToDatabasePagesConverter
{

    // <PartitionInfo SchemaVersion="1" PageHeaderVersion="1" PageDataLength="500">
    // <Partition Name="ManufacturingData" Id="0" RecordRevision="1" RecordLength="500" />
    // <Partition Name="FirmwareParameterData" Id="1" RecordRevision="1" RecordLength="500" />
    // <Partition Name="PCSoftwareParameter" Id="2" RecordRevision="1" RecordLength="500" />
    // <Partition Name="SensorData" Id="3" RecordRevision="1" RecordLength="20" />
    // <Partition Name="EGVData" Id="4" RecordRevision="2" RecordLength="13" />
    // <Partition Name="CalSet" Id="5" RecordRevision="2" RecordLength="148" />
    // <Partition Name="Aberration" Id="6" RecordRevision="1" RecordLength="15" />
    // <Partition Name="InsertionTime" Id="7" RecordRevision="1" RecordLength="15" />
    // <Partition Name="ReceiverLogData" Id="8" RecordRevision="1" RecordLength="20" />
    // <Partition Name="ReceiverErrorData" Id="9" RecordRevision="1" RecordLength="500" />
    // <Partition Name="MeterData" Id="10" RecordRevision="1" RecordLength="16" />
    // <Partition Name="UserEventData" Id="11" RecordRevision="1" RecordLength="20" />
    // <Partition Name="UserSettingData" Id="12" RecordRevision="3" RecordLength="48" />
    // </PartitionInfo>

    static BytesToDatabasePageHeaderConverter databaseHeaderConverter = new BytesToDatabasePageHeaderConverter();

    public List<DatabasePage> convert(short[] dataBytes) throws DexcomException
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

                short[] pageContent = new short[500];
                System.arraycopy(dataPage, 28, pageContent, 0, 500);

                dp.setPageData(pageContent);

                pages.add(dp);
            }

            return pages;
        }
        catch (DexcomException ex)
        {
            throw ex;
        }
        catch (Exception ex)
        {
            throw new DexcomException(DexcomExceptionType.Parsing_BytesParsingError,
                    new Object[] { "DatabasePage", ex.getLocalizedMessage() }, ex);
        }

    }
}
