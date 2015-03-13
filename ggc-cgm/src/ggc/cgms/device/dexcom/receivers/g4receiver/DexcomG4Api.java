package ggc.cgms.device.dexcom.receivers.g4receiver;

import ggc.cgms.device.dexcom.receivers.data.CommandPacket;
import ggc.cgms.device.dexcom.receivers.g4receiver.converter.BytesToDatabasePagesConverter;
import ggc.cgms.device.dexcom.receivers.g4receiver.converter.ConverterType;
import ggc.cgms.device.dexcom.receivers.g4receiver.converter.ElementToPartitionInfoConverter;
import ggc.cgms.device.dexcom.receivers.g4receiver.converter.data.DataPageToEGVDataConverter;
import ggc.cgms.device.dexcom.receivers.g4receiver.converter.data.DataPageToFileConverter;
import ggc.cgms.device.dexcom.receivers.g4receiver.converter.data.DataPageToUserEventDataConverter;
import ggc.cgms.device.dexcom.receivers.g4receiver.converter.data.DataPagesToInsertionTimeConverter;
import ggc.cgms.device.dexcom.receivers.g4receiver.converter.data.DataPagesToMeterConverter;
import ggc.cgms.device.dexcom.receivers.g4receiver.converter.data.DataPagesToXmlRecordConverter;
import ggc.cgms.device.dexcom.receivers.g4receiver.data.EGVRecord;
import ggc.cgms.device.dexcom.receivers.g4receiver.data.InsertionTimeRecord;
import ggc.cgms.device.dexcom.receivers.g4receiver.data.ManufacturingDataRecord;
import ggc.cgms.device.dexcom.receivers.g4receiver.data.MeterDataRecord;
import ggc.cgms.device.dexcom.receivers.g4receiver.data.UserEventDataRecord;
import ggc.cgms.device.dexcom.receivers.g4receiver.data.XmlRecord;
import ggc.cgms.device.dexcom.receivers.g4receiver.data.parsers.ParserUtils;
import ggc.cgms.device.dexcom.receivers.g4receiver.enums.ClockModeType;
import ggc.cgms.device.dexcom.receivers.g4receiver.enums.GlucoseUnitType;
import ggc.cgms.device.dexcom.receivers.g4receiver.enums.LanguageType;
import ggc.cgms.device.dexcom.receivers.g4receiver.enums.ReceiverRecordType;
import ggc.cgms.device.dexcom.receivers.g4receiver.internal.DatabasePage;
import ggc.cgms.device.dexcom.receivers.g4receiver.internal.DatabasePageRange;
import ggc.cgms.device.dexcom.receivers.g4receiver.internal.Partition;
import ggc.cgms.device.dexcom.receivers.g4receiver.internal.PartitionInfo;
import ggc.cgms.device.dexcom.receivers.g4receiver.util.DexcomUtils;
import ggc.cgms.device.dexcom.receivers.g4receiver.util.DexcomUtils.BitConversion;
import ggc.cgms.device.dexcom.receivers.g4receiver.util.DexcomUtils.DexcomDateParsing;
import ggc.plugin.data.enums.PlugInExceptionType;
import ggc.plugin.data.progress.ProgressReportInterface;
import ggc.plugin.data.progress.ProgressType;
import ggc.plugin.device.PlugInBaseException;
import gnu.io.CommPortIdentifier;
import gnu.io.NRSerialPort;
import gnu.io.PortInUseException;
import gnu.io.SerialPort;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.StringTokenizer;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.jdom.Element;

import com.atech.utils.data.ShortUtils;

public class DexcomG4Api
{

    private static final Log log = LogFactory.getLog(DexcomG4Api.class);
    // private CommPort serialDevice;
    private ShortUtils shortUtils = DexcomUtils.getShortUtils();
    private PartitionInfo partitionInfo;
    private CommPortIdentifier commPortIdentifier;
    private ProgressReportInterface progressReport;
    NRSerialPort serialDevice;
    InputStream inputStream;
    HashMap<ReceiverRecordType, DatabasePageRange> databasePagesRanges = new HashMap<ReceiverRecordType, DatabasePageRange>();

    public DexcomG4Api(String portName, ProgressReportInterface progressReport)
    {
        serialDevice = new NRSerialPort(portName, 115200);

        // this.serialDevice = serialPort;
        // this.commPortIdentifier = commPortIdentifier;
        this.progressReport = progressReport;

        serialDevice.connect();

        inputStream = this.serialDevice.getInputStream();
    }

    public void disconnectDevice()
    {
        if (this.serialDevice.isConnected())
        {
            this.serialDevice.disconnect();
        }
    }

    public Element readFirmwareHeader() throws PlugInBaseException
    {
        Element result = (Element) this.writeCommandAndReadParsedResponse(DexcomG4Commands.ReadFirmwareHeader);
        this.addToProgressAndCheckIfCanceled(1);

        return result;
    }

    public String readReceiverSerialNumber() throws PlugInBaseException
    {
        HashMap<String, String> map = readAllRecordsForManufacturingData();
        return map.get("SerialNumber");
    }

    public void shutDownReceiver() throws PlugInBaseException
    {

        try
        {
            SerialPort serialPort = this.commPortIdentifier.open("ggc", 4000);

            if (serialPort != null)
            {
                byte[] resetPacket = new byte[6];
                resetPacket[0] = 0x01;
                resetPacket[1] = 0x06;
                resetPacket[3] = 0x2e;
                resetPacket[4] = (byte) 0xb8;
                resetPacket[5] = (byte) 0x01;

                serialPort.getOutputStream().write(resetPacket); // , 200);
            }
        }
        catch (IOException ex)
        {
            log.error("Unable to write to serial device to shutdown.", ex);
            throw new PlugInBaseException(PlugInExceptionType.DeviceErrorWritingToDevice, ex);
        }
        catch (PortInUseException ex)
        {
            throw new PlugInBaseException(PlugInExceptionType.DeviceErrorWritingToDevice, ex);
        }

    }

    public DatabasePageRange readDatabasePageRange(ReceiverRecordType recordType) throws PlugInBaseException
    {
        if (!databasePagesRanges.containsKey(recordType))
        {
            databasePagesRanges.put(recordType, this.readDatabasePageRangeReal(recordType));
        }

        return databasePagesRanges.get(recordType);
    }

    public boolean isDatabasePageRangeCached(ReceiverRecordType recordType)
    {
        return databasePagesRanges.containsKey(recordType);
    }

    public DatabasePageRange readDatabasePageRangeReal(ReceiverRecordType recordType) throws PlugInBaseException
    {
        short[] data = this.writeCommandAndReadRawResponse(DexcomG4Commands.ReadDatabasePageRange, null,
            new Object[] { (long) recordType.getValue() });

        DatabasePageRange dpr = new DatabasePageRange();

        dpr.setFirstPage(DexcomUtils.toInt(this.shortUtils.getShortSubArray(data, 0, 4), BitConversion.LITTLE_ENDIAN));
        dpr.setLastPage(DexcomUtils.toInt(this.shortUtils.getShortSubArray(data, 4, 4), BitConversion.LITTLE_ENDIAN));

        log.debug(String.format("DatabasePageRange [RecordType=%s, FirstPage=%s, LastPage=%s, PageCount=%s]",
            recordType.name(), dpr.getFirstPage(), dpr.getLastPage(), dpr.getPagesCount()));

        this.addToProgressAndCheckIfCanceled(1);

        return dpr;
    }

    public List<InsertionTimeRecord> readAllRecordsForInsertionTime() throws PlugInBaseException
    {
        List<DatabasePage> pages = readDatabasePagesAll(ReceiverRecordType.InsertionTime);

        // log.debug("InsertionTime pages: " + pages.size());

        DataPagesToInsertionTimeConverter cnv = (DataPagesToInsertionTimeConverter) DexcomUtils
                .getConverter(ConverterType.DataPagesToInsertionTimeConverter);

        List<InsertionTimeRecord> records = cnv.convert(pages);

        // log.debug("InsertionTime Records: " + records.size());
        log.debug(String.format("InsertionTime Data [records=%s,pages=%s]", records.size(), pages.size()));

        return records;
    }

    public List<MeterDataRecord> readAllRecordsForMeterData() throws PlugInBaseException
    {
        List<DatabasePage> pages = readDatabasePagesAll(ReceiverRecordType.MeterData);

        // log.debug("Meter entries pages: " + pages.size());

        DataPagesToMeterConverter cnv = (DataPagesToMeterConverter) DexcomUtils
                .getConverter(ConverterType.DataPagesToMeterConverter);

        List<MeterDataRecord> records = cnv.convert(pages);

        // log.debug("MeterDataRecord Records: " + records.size());
        log.debug(String.format("Meter Data [records=%s,pages=%s]", records.size(), pages.size()));

        return records;
    }

    public List<UserEventDataRecord> readAllRecordsForEvents() throws PlugInBaseException
    {
        List<DatabasePage> pages = readDatabasePagesAll(ReceiverRecordType.UserEventData);

        // log.debug("EventData pages: " + pages.size());

        DataPageToUserEventDataConverter cnv = (DataPageToUserEventDataConverter) DexcomUtils
                .getConverter(ConverterType.DataPageToUserEventDataConverter);

        List<UserEventDataRecord> records = cnv.convert(pages);

        // log.debug("EventData Records: " + records.size());
        log.debug(String.format("Event Data [records=%s,pages=%s]", records.size(), pages.size()));

        return records;
    }

    public List<EGVRecord> readAllRecordsForEGVData() throws PlugInBaseException
    {

        List<DatabasePage> pages = readDatabasePagesAll(ReceiverRecordType.EGVData);

        // log.debug("EGV Data pages: " + pages.size());

        DataPageToEGVDataConverter cnv = (DataPageToEGVDataConverter) DexcomUtils
                .getConverter(ConverterType.DataPageToEGVDataConverter);

        List<EGVRecord> records = cnv.convert(pages);

        log.debug(String.format("EGV Data [records=%s,pages=%s]", records.size(), pages.size()));

        return records;
    }

    public void saveDatabasePages(ReceiverRecordType recordType) throws PlugInBaseException
    {
        List<DatabasePage> pages = readDatabasePagesAll(recordType);

        DataPageToFileConverter cnv = (DataPageToFileConverter) DexcomUtils
                .getConverter(ConverterType.DataPageToFileConverter);

        int i = 0;

        for (DatabasePage page : pages)
        {
            cnv.convert(page, i, recordType);
            i++;
        }

    }

    public HashMap<String, String> readAllRecordsForManufacturingData() throws PlugInBaseException
    {
        List<DatabasePage> pages = readDatabasePagesAll(ReceiverRecordType.ManufacturingData);

        log.debug("Manufacturing pages: " + pages.size());

        DataPagesToXmlRecordConverter cnv = (DataPagesToXmlRecordConverter) DexcomUtils
                .getConverter(ConverterType.DataPagesToXmlRecordConverter);

        List<XmlRecord> records = cnv.convert(pages, new ManufacturingDataRecord());

        // there is only one record here and it's filled with Xml Data
        ManufacturingDataRecord xmlRecord = (ManufacturingDataRecord) records.get(0);

        StringTokenizer strtok = new StringTokenizer(xmlRecord.getXmlData(), " ");
        strtok.nextToken();

        HashMap<String, String> paramMap = new HashMap<String, String>();

        while (strtok.hasMoreTokens())
        {
            String el = strtok.nextToken();

            if (el.contains("="))
            {
                String k = el.substring(0, el.indexOf("="));
                String v = el.substring(el.indexOf("=") + 2);
                v = v.substring(0, v.length() - 1);

                paramMap.put(k, v);
            }
        }

        // log.debug(paramMap);
        log.debug(String.format("Manufacturing Data [parameters=%s, pages=%s]", paramMap.size(), pages.size()));
        // this.addToProgressAndCheckIfCanceled(1);

        return paramMap;
    }

    public List<DatabasePage> readDatabasePagesAll(ReceiverRecordType recordType) throws PlugInBaseException
    {
        DatabasePageRange dpr = readDatabasePageRange(recordType);

        if (dpr.getFirstPage() == -1)
            return new ArrayList<DatabasePage>();

        int i = dpr.getFirstPage();
        int j = 4;
        List<DatabasePage> pages = new ArrayList<DatabasePage>();

        while (i <= dpr.getLastPage())
        {
            // if i>
            // j = i + 5;

            if (i + j > dpr.getLastPage())
            {
                // adjust j
                j = dpr.getPagesCount() - i + dpr.getFirstPage();
                // log.debug("J: " + j);
            }

            pages.addAll(readDatabasePages(recordType, i, j));

            i += j;
        }

        return pages;
    }

    public List<DatabasePage> readDatabasePages(ReceiverRecordType recordType, int pageNumber, int numberOfPages)
            throws PlugInBaseException
    {

        if (numberOfPages == 1 && pageNumber == 0)
        {
            log.debug(String.format("Reading pages %s of %s", pageNumber, recordType.name()));
        }
        else
        {
            log.debug(String.format("Reading pages %s - %s of %s", pageNumber, pageNumber + numberOfPages - 1,
                recordType.name()));
        }

        short[] data = this.writeCommandAndReadRawResponse(DexcomG4Commands.ReadDatabasePages, null,
            new Object[] { (long) recordType.getValue(), (long) pageNumber, (long) numberOfPages });

        BytesToDatabasePagesConverter cnv = (BytesToDatabasePagesConverter) DexcomUtils
                .getConverter(ConverterType.BytesToDatabasePagesConverter);

        List<DatabasePage> pages = cnv.convert(data);

        this.addToProgressAndCheckIfCanceled(numberOfPages);

        return pages;
    }

    public int readSystemTime() throws PlugInBaseException
    {
        Integer result = (Integer) this.writeCommandAndReadParsedResponse(DexcomG4Commands.ReadSystemTime, null);
        this.addToProgressAndCheckIfCanceled(1);

        return result;
    }

    public int readDisplayTime() throws PlugInBaseException
    {
        return readSystemTime() + DexcomUtils.readDisplayTimeOffset();
    }

    public Date readSystemTimeAsDate() throws PlugInBaseException
    {
        int systemTime = readSystemTime();
        return DexcomUtils.getDateFromSeconds(systemTime);
    }

    public Date readDisplayTimeAsDate() throws PlugInBaseException
    {
        return DexcomUtils.getDateFromSeconds(readSystemTime(), DexcomDateParsing.DateWithDifferenceWithTimeZoneFix);
    }

    public PartitionInfo readDatabasePartitionInfo() throws PlugInBaseException
    {
        if (this.partitionInfo == null)
        {
            ElementToPartitionInfoConverter cnv = (ElementToPartitionInfoConverter) DexcomUtils
                    .getConverter(ConverterType.ElementToPartitionInfoConverter);

            this.partitionInfo = cnv.convert((Element) this
                    .writeCommandAndReadParsedResponse(DexcomG4Commands.ReadDatabaseParitionInfo));
        }
        this.addToProgressAndCheckIfCanceled(1);
        return this.partitionInfo;
    }

    public Partition getPartition(ReceiverRecordType recordType) throws PlugInBaseException
    {
        if (this.partitionInfo == null)
        {
            this.readDatabasePartitionInfo();
        }

        return this.partitionInfo.getPartitionByRecordType(recordType);
    }

    public int readDisplayTimeOffset() throws PlugInBaseException
    {
        Integer value = (Integer) this.writeCommandAndReadParsedResponse(DexcomG4Commands.ReadDisplayTimeOffset);

        this.addToProgressAndCheckIfCanceled(1);

        return value;
    }

    public LanguageType readLanguage() throws PlugInBaseException
    {
        Integer language = (Integer) this.writeCommandAndReadParsedResponse(DexcomG4Commands.ReadLanguage);

        LanguageType lang = LanguageType.getEnum(language);

        if (lang == null)
        {
            log.warn("Unknown language code: " + language);
            return LanguageType.None;
        }
        this.addToProgressAndCheckIfCanceled(1);
        return lang;
    }

    public GlucoseUnitType readGlucoseUnit() throws PlugInBaseException
    {
        Integer glu = (Integer) this.writeCommandAndReadParsedResponse(DexcomG4Commands.ReadGlucoseUnit);

        GlucoseUnitType gluType = GlucoseUnitType.getEnum(glu);

        if (gluType == null)
        {
            log.warn("Unknown Glucose Unit Type code: " + glu);
            return GlucoseUnitType.None;
        }
        this.addToProgressAndCheckIfCanceled(1);
        return gluType;
    }

    public ClockModeType readClockMode() throws PlugInBaseException
    {
        Integer clock = (Integer) this.writeCommandAndReadParsedResponse(DexcomG4Commands.ReadClockMode);

        ClockModeType clockType = ClockModeType.getEnum(clock);

        if (clockType == null)
        {
            log.warn("Unknown Clock Mode Type code: " + clock);
            return ClockModeType.ClockMode12Hour;
        }
        this.addToProgressAndCheckIfCanceled(1);
        return clockType;
    }

    private Object writeCommandAndReadParsedResponse(DexcomG4Commands command) throws PlugInBaseException
    {
        CommandPacket cmdPacket = this.createCommandPacket(command, null);
        this.writeCommandAndReadRawResponse(null, cmdPacket, null);

        // log.debug(cmdPacket.getResponse());

        return ParserUtils.parsePacketResponse(cmdPacket);
    }

    private Object writeCommandAndReadParsedResponse(DexcomG4Commands command, Object[] parameters)
            throws PlugInBaseException
    {
        CommandPacket cmdPacket = this.createCommandPacket(command, parameters);
        this.writeCommandAndReadRawResponse(null, cmdPacket, parameters);

        // System.out.println(cmdPacket.getResponse());
        // log.debug(cmdPacket.getResponse());

        return ParserUtils.parsePacketResponse(cmdPacket);
    }

    private CommandPacket createCommandPacket(DexcomG4Commands command, Object[] parameters)
    {
        CommandPacket cmdPacket;

        if (parameters == null)
        {
            cmdPacket = new CommandPacket(command);
        }
        else
        {
            cmdPacket = new CommandPacket(command, parameters);
        }
        return cmdPacket;
    }

    private short[] writeCommandAndReadRawResponse(DexcomG4Commands command, CommandPacket cmdPacket,
            Object[] parameters) throws PlugInBaseException
    {
        if (cmdPacket == null)
        {
            cmdPacket = this.createCommandPacket(command, parameters);
        }

        // log.debug(byteUtils.getDebugByteArray(byteUtils.getByteSubArray(cmdPacket.getCommand(),
        // 0, 12)));

        int retries = 0;
        PlugInBaseException exception;

        do
        {

            try
            {
                try
                {
                    short[] cmd = cmdPacket.getCommand();

                    for (int element : cmd)
                    {
                        this.serialDevice.getOutputStream().write(element);
                    }
                }
                catch (IOException ex)
                {
                    throw new PlugInBaseException(PlugInExceptionType.DeviceErrorWritingToDevice,
                            new Object[] { ex.getLocalizedMessage() }, ex);
                }

                if (cmdPacket.getExpectedResponseLength() != 0)
                {
                    this.readGenericCommandPacket(5000, cmdPacket);
                    this.verifyResponseCommandByte(cmdPacket);

                    if (cmdPacket.getExpectedResponseLength() > 0)
                    {
                        this.verifyPayloadLength(cmdPacket);
                    }

                    return cmdPacket.getResponse();
                }
                else
                    return new short[0];
            }
            catch (PlugInBaseException ex)
            {
                exception = ex;
                // e.printStackTrace();
            }
        } while (retries < 4);

        if (exception != null)
            throw exception;

        return new short[0];

    }

    // FIXME
    public short[] readGenericCommandPacket(int maxWaitMs, CommandPacket packet) throws PlugInBaseException
    {
        if (this.serialDevice == null)
            throw new PlugInBaseException(PlugInExceptionType.InvalidPortOrPortClosed);

        short[] destinationArray = null;
        short[] buffer2 = new short[0x10005];
        int destinationIndex = 0;
        // long now = System.currentTimeMillis();

        PlugInBaseException exception = null;
        // maxWaitMs
        // while (System.currentTimeMillis() < (now + 20000))

        int retries = 0;

        // do
        // {
        try
        {
            short[] sourceArray = this.readSpecifiedBytes(4);

            if (sourceArray[0] == 1)
            {
                System.arraycopy(sourceArray, 0, buffer2, 0, 4);
                destinationIndex = 4;
                packet.setResponseCommandId(sourceArray[3]);
                int length = this.shortUtils.convertIntsToUnsignedShort(sourceArray[2], sourceArray[1]);
                // BitConverter.ToUInt16(sourceArray, 1);

                if (length > 6)
                {
                    length = length - 6;

                    destinationArray = new short[length];

                    // log.debug("Expected length: " + length);

                    short[] buffer4 = this.readSpecifiedBytes(length);
                    System.arraycopy(buffer4, 0, buffer2, destinationIndex, length);
                    System.arraycopy(buffer4, 0, destinationArray, 0, length);
                    destinationIndex += length;
                }
                else
                {
                    destinationArray = new short[0];
                }

                // CRC
                short[] buffer5 = this.readSpecifiedBytes(2);
                int num3 = this.shortUtils.convertIntsToUnsignedShort(buffer5[1], buffer5[0]);
                // BitConverter.ToUInt16(buffer5, 0);
                int num4 = DexcomUtils.calculateCRC16(buffer2, 0, destinationIndex);
                System.arraycopy(buffer5, 0, buffer2, destinationIndex, 2);
                destinationIndex += 2;
                if (num3 != num4)
                {
                    exception = new PlugInBaseException(PlugInExceptionType.FailedCRCCheckInPacket);
                    throw exception;
                }
                else
                {
                    exception = null;
                }

            }
            else
            {
                exception = new PlugInBaseException(PlugInExceptionType.UnknownDataReadWrongStartOfPacket);
                throw exception;
            }

            // break;

        }
        catch (PlugInBaseException ex)
        {
            exception = ex;
            // throw ex;
        }
        catch (Exception ex)
        {
            exception = new PlugInBaseException(PlugInExceptionType.DeviceFailedToReadResponse, ex);
        }

        retries++;

        // } while (retries < 4);

        if (exception != null)
        {
            packet.setException(exception);
            throw exception;
        }

        packet.setResponse(destinationArray);
        return destinationArray;
    }

    // FIXME DexcomException

    public short[] readSpecifiedBytes(int nrBytes) throws Exception
    {
        int count = 0;
        short[] retData = new short[nrBytes];

        long till = System.currentTimeMillis() + 5000;

        while (inputStream.available() < nrBytes)
        {

            if (System.currentTimeMillis() > till)
                throw new PlugInBaseException(PlugInExceptionType.TimeoutReadingData);

            // log.warn("Waiting for data: " + is.available() + ", Req: " +
            // nrBytes);

            Thread.sleep(44);
        }

        while (count < nrBytes)
        {
            try
            {
                retData[count] = (short) inputStream.read();

            }
            catch (IOException ex)
            {
                log.error("Error reading from Serial Port: " + ex, ex);
            }
            count++;
        }

        return retData;
    }

    private void verifyPayloadLength(CommandPacket cmdPacket) throws PlugInBaseException
    {
        if (cmdPacket.getResponse().length != cmdPacket.getExpectedResponseLength())
            throw new PlugInBaseException(PlugInExceptionType.DeviceInvalidResponseLength,
                    new Object[] { cmdPacket.getResponse().length, cmdPacket.getExpectedResponseLength()});
    }

    public void verifyResponseCommandByte(CommandPacket packet) throws PlugInBaseException
    {
        DexcomG4Commands receiverCommandFromByte = DexcomG4Commands.getCommandById(packet.getResponseCommandId());
        // ReceiverCommands receiverCommandFromByte =
        // ReceiverTools.GetReceiverCommandFromByte(commandByte);
        switch (receiverCommandFromByte)
        {
            case Ack:
                return;

            case Nak:
                throw new PlugInBaseException(PlugInExceptionType.DeviceNAKOrInvalidCRC);

            case InvalidCommand:
                throw new PlugInBaseException(PlugInExceptionType.DeviceInvalidCommand);

            case InvalidParam:
                if (packet.getResponse() != null && packet.getResponse().length >= 1) {
                    throw new PlugInBaseException(PlugInExceptionType.DeviceInvalidParameterDesc, new Object[]{packet.getResponse()[0]});
                }
                else {
                    throw new PlugInBaseException(PlugInExceptionType.DeviceInvalidParameter);
                }

            case ReceiverError:
                throw new PlugInBaseException(PlugInExceptionType.DeviceInternalError);

            default:
                break;
        }
        throw new PlugInBaseException(PlugInExceptionType.DeviceInvalidResponseCommand,
            new Object[] { receiverCommandFromByte, packet.getResponseCommandId() });
    }

    public void addToProgressAndCheckIfCanceled(int numberOfPages) throws PlugInBaseException
    {
        if (this.progressReport != null)
        {
            this.progressReport.addToProgressAndCheckIfCanceled(ProgressType.Dynamic, numberOfPages);
        }
    }

}
