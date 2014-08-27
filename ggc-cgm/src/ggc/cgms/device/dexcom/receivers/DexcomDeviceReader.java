package ggc.cgms.device.dexcom.receivers;

import ggc.cgms.device.dexcom.receivers.data.ReceiverDownloadData;
import ggc.cgms.device.dexcom.receivers.data.ReceiverDownloadDataConfig;
import ggc.cgms.device.dexcom.receivers.data.output.ConsoleOutputParser;
import ggc.cgms.device.dexcom.receivers.data.output.DataOutputParserInterface;
import ggc.cgms.device.dexcom.receivers.data.output.DataOutputParserType;
import ggc.cgms.device.dexcom.receivers.g4receiver.DexcomG4Api;
import ggc.cgms.device.dexcom.receivers.g4receiver.data.EGVRecord;
import ggc.cgms.device.dexcom.receivers.g4receiver.data.UserEventDataRecord;
import ggc.cgms.device.dexcom.receivers.g4receiver.internal.PartitionInfo;
import ggc.cgms.device.dexcom.receivers.g4receiver.util.DexcomException;
import ggc.cgms.device.dexcom.receivers.g4receiver.util.DexcomExceptionType;
import ggc.cgms.device.dexcom.receivers.g4receiver.util.DexcomUtils;
import gnu.io.CommPort;
import gnu.io.CommPortIdentifier;
import gnu.io.NoSuchPortException;
import gnu.io.PortInUseException;

import java.util.Date;
import java.util.Enumeration;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.jdom.Element;

import com.atech.utils.data.ByteUtils;

//Still kludgy
//Newer, similar to Dex's architecture, classes are in progress, but this works reliably, if not the
//most efficient, elegant design around.

public class DexcomDeviceReader implements DexcomDeviceProgressReport {

    // private final String EPOCH = "01-01-2009";

    private Log log = LogFactory.getLog(DexcomDeviceReader.class);
    public String bGValue;
    public String displayTime;
    public String trend;
    public EGVRecord[] mRD;

    private String portName;
    CommPortIdentifier portIdentifier;
    private CommPort serialDevice;
    ByteUtils byteUtils = new ByteUtils();
    DexcomG4Api api = null;

    private DataOutputParserInterface dataOutputParser = new ConsoleOutputParser();


    public DexcomDeviceReader(String portName, DexcomDevice dexcomDevice) throws DexcomException {
        this.portName = portName;

        @SuppressWarnings("rawtypes")
        Enumeration e = CommPortIdentifier.getPortIdentifiers();
        StringBuilder sb = new StringBuilder();

        boolean deviceFound = false;

        while (e.hasMoreElements()) {
            CommPortIdentifier comp = (CommPortIdentifier)e.nextElement();

            if (comp.equals(this.portName)) {
                deviceFound = true;
            }

            sb.append(comp.getName());
        }

        log.debug("Serial Ports found: " + sb.toString() + ", configured port found: " + deviceFound);

        if (!deviceFound) {
            throw new DexcomException(DexcomExceptionType.DeviceNotFoundOnConfiguredPort,
                new Object[] { this.portName });
        }

        try {
            this.portIdentifier = CommPortIdentifier.getPortIdentifier(portName);
        } catch (NoSuchPortException e1) {
            throw new DexcomException(DexcomExceptionType.DeviceNotFoundOnConfiguredPort,
                new Object[] { this.portName });
        }
        try {
            this.serialDevice = this.portIdentifier.open("ggc", 4000);
        } catch (PortInUseException e1) {
            throw new DexcomException(DexcomExceptionType.DevicePortInUse, new Object[] { this.portName });
        }

        if (dexcomDevice.getApi() == ReceiverApiType.G4_Api) {
            this.api = new DexcomG4Api(this.serialDevice, portIdentifier, this);
            DexcomUtils.setDexcomG4Api(api);
        } else {
            throw new DexcomException(DexcomExceptionType.UnsupportedReceiver);
        }

        // this.mSerialDevice = new SerialPort(); // SerialPort(portName);

        /*
         * serialDevice.setSerialPortParams(9600, SerialPort.DATABITS_8,
         * SerialPort.STOPBITS_1, SerialPort.PARITY_NONE);
         */

        // this.mSerialDevice.openPort();
        // this.mSerialDevice.set.setParams(SerialPort.BAUDRATE_9600,
        // SerialPort.DATABITS_8, SerialPort.STOPBITS_1,
        // SerialPort.PARITY_NONE);
        // this.mSerialDevice.

    }


    public void downloadSettings() throws DexcomException {

        try {
            ReceiverDownloadData data = new ReceiverDownloadData();

            this.setStaticProgressPercentage(0);
            this.setMaxElementsForDynamicProgress(8);

            Element header = this.api.readFirmwareHeader();
            this.addToProgressAndCheckIfCanceled(ProgressType.Dynamic, 1);

            data.addConfigurationEntry("API_VERSION", header.getAttributeValue("ApiVersion"), false);
            data.addConfigurationEntry("PRODUCT_ID", header.getAttributeValue("ProductId"), false);
            data.addConfigurationEntry("PRODUCT_NAME", header.getAttributeValue("ProductName"), false);
            data.addConfigurationEntry("SOFTWARE_NUMBER", header.getAttributeValue("SoftwareNumber"), false);
            data.addConfigurationEntry("FIRMWARE_VERSION", header.getAttributeValue("FirmwareVersion"), false);
            data.addConfigurationEntry("PORT_VERSION", header.getAttributeValue("PortVersion"), false);
            data.addConfigurationEntry("RF_VERSION", header.getAttributeValue("RFVersion"), false);

            data.setSerialNumber(api.readReceiverSerialNumber());

            data.addConfigurationEntry("SYSTEM_TIME", DexcomUtils.getDateTimeString(api.readSystemTimeAsDate()), false);

            this.addToProgressAndCheckIfCanceled(ProgressType.Dynamic, 1);

            api.readDisplayTimeAsDate();

            // this.progressReport.addToProgressAndCheckIfCanceled(ProgressType.Dynamic, 1);

            this.addToProgressAndCheckIfCanceled(ProgressType.Dynamic, 1);

            // api.readLanguage()
            this.addToProgressAndCheckIfCanceled(ProgressType.Dynamic, 1);

            // api.readGlucoseUnit()
            this.addToProgressAndCheckIfCanceled(ProgressType.Dynamic, 1);

            // api.readClockMode()
            this.addToProgressAndCheckIfCanceled(ProgressType.Dynamic, 1);

            // parse
            this.dataOutputParser.parse(DataOutputParserType.Configuration, data);
            this.addToProgressAndCheckIfCanceled(ProgressType.Dynamic, 1);

        } catch (DexcomException ex) {

            if ((ex.getExceptionType() != null)
                && (ex.getExceptionType() == DexcomExceptionType.DownloadCanceledByUser)) {
                return;
            }

            throw ex;
        }
        // SERIAL_NUMBER=Serial Number
        /*
         * SYSTEM_TIME=System Time
         * DISPLAY_TIME=Display Time
         * LANGUAGE=Language
         * LANGUAGE_ENGLISH=English
         * GLUCOSE_UNIT=Glucose Unit
         * GLUCOSE_UNIT_MGDL=mg/dL
         * GLUCOSE_UNIT_MMOLL=mmol/L
         * CLOCK_MODE=Clock
         * Mode
         * CLOCK_MODE_12H=12h
         * CLOCK_MODE_24H=24h
         */

    }


    public void addToProgressAndCheckIfCanceled(ProgressType progressType, int i) throws DexcomException {

        // if this.

        // progress

        if (this.isDownloadCanceled()) {
            throw new DexcomException(DexcomExceptionType.DownloadCanceledByUser);
        }

    }


    public void downloadData() throws DexcomException {
        // Element info = (Element) api.readDatabasePartitionInfo();
        ReceiverDownloadData data = new ReceiverDownloadData();

        // FIXME
        // read page ranges to create dynamic counter

        // FIXME
        // serial number

        PartitionInfo info = this.api.readDatabasePartitionInfo();
        log.debug("Partition Info: PageLength: " + info.getPageDataLength());

        log.debug("Partitions: " + info.getPartitions().size());

        this.addToDynamicProgress(1);

        // FIXME
        // report
        this.addToDynamicProgress(1);

        // ranges cached
        data.setSerialNumber(api.readReceiverSerialNumber());
        // log.debug(api.readDatabasePageRange(ReceiverRecordType.InsertionTime));
        this.addToDynamicProgress(1);

        // pages extend

        data.addData(DataOutputParserType.G4_UserEventData, api.readAllRecordsForEvents());
        // parse
        this.addToDynamicProgress(1);

        data.addData(DataOutputParserType.G4_EGVData, api.readAllRecordsForEGVData());
        // parse
        this.addToDynamicProgress(1);

        data.addData(DataOutputParserType.G4_InsertionTime, api.readAllRecordsForInsertionTime());
        // parse
        this.addToDynamicProgress(1);

        data.addData(DataOutputParserType.G4_MeterData, api.readAllRecordsForMeterData());
        // parse
        this.addToDynamicProgress(1);

    }


    public ReceiverDownloadData doDownloadReceiverDataAsObject() throws Exception {

        // DexcomReader api = this;

        ReceiverDownloadData data = new ReceiverDownloadData();

        Date now = new Date();

        // Element header = this.api.readFirmwareHeader();
        //
        // //log.debug("Xml: " + header.toString());
        //
        // data.addConfigurationEntry("API_VERSION", header.getAttributeValue("ApiVersion"));
        // data.addConfigurationEntry("PRODUCT_ID", header.getAttributeValue("ProductId")); // header.getProductId());
        // data.addConfigurationEntry("PRODUCT_NAME", header.getAttributeValue("ProductName")); //
        // header.getProductName());
        // data.addConfigurationEntry("SOFTWARE_NUMBER", header.getAttributeValue("SoftwareNumber")); //
        // header.getSoftwareNumber());
        // data.addConfigurationEntry("FIRMWARE_VERSION", header.getAttributeValue("FirmwareVersion")); //
        // header.getFirmwareVersion());
        // data.addConfigurationEntry("PORT_VERSION", header.getAttributeValue("PortVersion")); //
        // header.getPortVersion());
        // data.addConfigurationEntry("RF_VERSION", header.getAttributeValue("RFVersion")); // header.getRFVersion());

        // FIXME
        // data.setSerialNumber(api.readReceiverSerialNumber());

        // obj2.Element.AppendChild(ownerDocument.ImportNode(obj3.Element,
        // true));
        // data.addConfigurationEntry("SYSTEM_TIME", api.readSystemTime()); //
        // obj3.getAttributeAsDateTime("SystemTime").ToString("YYYYMMDDHHmmSS"));
        // data.addConfigurationEntry("DISPLAY_TIME",
        // this.getDisplayTime().toString());
        // //obj3.getAttributeAsDateTime("DisplayTime").ToString("YYYYMMDDHHmmSS"));
        // data.addConfigurationEntry("DISPLAY_TIME_OFFSET",
        // api.readDisplayTimeOffset());
        // //obj3.getAttributeAsDateTimeOffset("DisplayTimeOffset").ToString("HHmmSS"));
        // data.addConfigurationEntry("LANGUAGE", api.readLanguage().name());
        // data.addConfigurationEntry("GLUCOSE_UNIT",
        // api.readGlucoseUnits().name());
        // data.addConfigurationEntry("CLOCK_MODE", api.readClockMode().name());

        this.log.debug("Configuration: ");
        for (ReceiverDownloadDataConfig cfg : data.getConfigs()) {
            log.debug(cfg.getKey() + " = " + cfg.getValue());
        }

        // ERRORS
        // if ((recordsFilter & ReceiverRecordTypeFlags.ProcessorErrors) ==
        // ReceiverRecordTypeFlags.ProcessorErrors)
        // {
        // //XObject obj4 = new XObject("ProcessorErrorPages", ownerDocument);
        // //obj2.AppendChild(obj4);
        // byte[] array = api.ReadFlashPage(0x4f8);
        // if (!Array.<byte>TrueForAll(/* [UNSUPPORTED] to translate lambda
        // expressions we need an explicit delegate
        // type, try adding a cast "(array, val) => {
        // return val == 0xff;
        // }" */))
        // {
        // }
        //
        // }

        // {
        //
        // XPartitionInfo info = api.readDatabasePartitionInfo();
        //

        // Element info = (Element)api.readDatabasePartitionInfo();

        // XmlParser parser =
        // (XmlParser)ParserUtils.getParser(ParserType.XmlParser);

        // log.debug("PartitionInfo: "+ info);
        // log.debug("PartitionInfo Xml: \n" +
        // ((XmlParser)ParserUtils.getParser(ParserType.XmlParser)).getXml(info));

        // <PartitionInfo SchemaVersion="1" PageHeaderVersion="1"
        // PageDataLength="500">
        // <Partition Name="ManufacturingData" Id="0" RecordRevision="1"
        // RecordLength="500" />
        // <Partition Name="FirmwareParameterData" Id="1" RecordRevision="1"
        // RecordLength="500" />
        // <Partition Name="PCSoftwareParameter" Id="2" RecordRevision="1"
        // RecordLength="500" />
        // <Partition Name="SensorData" Id="3" RecordRevision="1"
        // RecordLength="20" />
        // <Partition Name="EGVData" Id="4" RecordRevision="2" RecordLength="13"
        // />
        // <Partition Name="CalSet" Id="5" RecordRevision="2" RecordLength="148"
        // />
        // <Partition Name="Aberration" Id="6" RecordRevision="1"
        // RecordLength="15" />
        // <Partition Name="InsertionTime" Id="7" RecordRevision="1"
        // RecordLength="15" />
        // <Partition Name="ReceiverLogData" Id="8" RecordRevision="1"
        // RecordLength="20" />
        // <Partition Name="ReceiverErrorData" Id="9" RecordRevision="1"
        // RecordLength="500" />
        // <Partition Name="MeterData" Id="10" RecordRevision="1"
        // RecordLength="16" />
        // <Partition Name="UserEventData" Id="11" RecordRevision="1"
        // RecordLength="20" />
        // <Partition Name="UserSettingData" Id="12" RecordRevision="3"
        // RecordLength="48" />
        // </PartitionInfo>

        /*
         * for(Object ch : info.getChildren()) { Element ell = (Element)ch;
         * 
         * log.debug(ell.getAttribute("Name").toString());
         * 
         * }
         */

        PartitionInfo info = this.api.readDatabasePartitionInfo();
        log.debug("Partition Info: PageLength: " + info.getPageDataLength());

        log.debug("Partitions: " + info.getPartitions().size());

        // log.debug(api.readDatabasePageRange(ReceiverRecordType.InsertionTime));

        List<UserEventDataRecord> evs = api.readAllRecordsForEvents();
        log.debug("Events Records: " + evs.size());

        List<EGVRecord> recs = api.readAllRecordsForEGVData();

        log.debug("EGV Records: " + recs.size());

        // LanguageType lang = api.readLanguage();

        // log.debug("Language: " + lang.name());

        // GlucoseUnitType glu = api.readGlucoseUnit();

        // ClockModeType clk = api.readClockMode();

        // log.debug("Clock Mode: " + clk.name());

        // OK
        // api.readAllRecordsForEvents();
        // api.readAllRecordsForManufacturingData();
        // api.readAllRecordsForInsertionTime();
        // api.readAllRecordsForMeterData();
        // api.readSystemTimeAsDate();
        // api.readDisplayTimeAsDate();
        // api.readLanguage()
        // api.readGlucoseUnit()
        // api.readClockMode()

        // HashMap<String, String> map = api.readAllRecordsForManufacturingData();

        // DexcomUtils.debugXmlTree(el);

        if (true) {
            return data;
        }

        // int allPages = doCountDatabasePagesInReceiver(api);
        // log.debug("All pages count: " + allPages);
        // int num3 = 6;
        // allPages += num3 + 1;
        // //
        //
        // List<XPage> dataPages = new List<XPage>();
        // log.Debug("Partirions count: " + info.getPartitions().getCount());
        // for (Object __dummyForeachVar2 : info.getPartitions())
        // {
        // XPartition partition = (XPartition)__dummyForeachVar2;
        // int num7 = 0; //new int();
        // Func<KeyValuePair<ReceiverRecordType, XPageHeader>, boolean>
        // predicate = null;
        //
        // byte id = partition.getId();
        // ReceiverRecordType record_type = (ReceiverRecordType)id;
        //
        // log.Debug("Partition for ReceiverRecordType: " +
        // record_type.ToString());
        // DatabasePageRange range = api.readDatabasePageRange(record_type);
        //
        // if (range.LastPage == Integer.MAX_VALUE)
        // {
        // continue;
        // }
        //
        // int first_page = range.FirstPage;
        // int last_page = range.LastPage;
        // log.Debug("Range: first=" + range.FirstPage + ", last=" +
        // range.LastPage);
        // //if (true)
        // // continue;
        // num7 = first_page;
        // while (num7 <= last_page)
        // {
        // // if ((backgroundWorker != null) &&
        // backgroundWorker.CancellationPending)
        // // {
        // // break;
        // // }
        //
        // num3++;
        // int num8 = Convert.ToInt32((double)(((((double)num3) /
        // ((double)allPages)) * 100.0) * 10.0));
        //
        //
        // int num9 = (last_page - num7) + 1;
        // if (num9 > 4)
        // {
        // num9 = 4;
        // }
        //
        // for (Object __dummyForeachVar1 :
        // api.readDatabasePages(record_type,num7,(byte)num9))
        // {
        // DatabasePage page = (DatabasePage)__dummyForeachVar1;
        // XPage page2 = new XPage();
        // //obj8.AppendChild(page2);
        // page2.getPageHeader().setPageNumber(page.PageHeader.PageNumber);
        // page2.getPageHeader().setRecordType(page.PageHeader.RecordType.ToString());
        // page2.getPageHeader().setRecordTypeId(((Enum)page.PageHeader.RecordType).ordinal());
        // page2.getPageHeader().setRecordRevision(page.PageHeader.Revision);
        // page2.getPageHeader().setFirstRecordIndex(page.PageHeader.FirstRecordIndex);
        // page2.getPageHeader().setNumberOfRecords(page.PageHeader.NumberOfRecords);
        // page2.getPageHeader().setCrc(page.PageHeader.Crc);
        // page2.setPageData(page.PageData);
        // dataPages.Add(page2);
        // }
        // num3 += ((int)num9) - 1;
        // num7 += num9 - 1;
        // num7++;
        // }
        // }
        // ReceiverDatabaseRecordsParser records = new
        // ReceiverDatabaseRecordsParser();
        // records.ParsePages(dataPages);
        // data.setData(records);
        // }
        // records.Parse(receiverData);

        int intValue = 0;
        return data;
    }


    public static void main(String[] args) {
        try {
            DexcomDeviceReader dr = new DexcomDeviceReader("COM4", DexcomDevice.Dexcom_G4);
            // Date d = dr.getDisplayTime();
            // System.out.println("Display Time: " + d);
            // System.out.println("SysTime: " + dr.getSystemTime());

            dr.doDownloadReceiverDataAsObject();

            // Date d = new

            /*
             * System.out.println("SysTime: " + dr.getSystemTime());
             * 
             * //dr.getDisplayTimeOffset(); System.out.println("Difference: " +
             * dr.getDisplayTimeOffset()); System.out.println("SysTime: " +
             * dr.getDisplayTime());
             */

            // System.out.println("SysTime2: " + dr.getSystemTime2());
        } catch (Exception ex) {
            System.out.println("Error reading from " + ex);
            ex.printStackTrace();
        }

        Date d = new Date(2009, 1, 1);
        // d.setTime(177714110);
        // 177716034

        long ddd = d.getTime();
        ddd += 33555969;

        d.setTime(177716034 * 1000);

        System.out.println("SysTime: " + d);

        System.out.println("Ms: " + System.currentTimeMillis());

    }


    public void setStaticProgressPercentage(int percentage) {
        // TODO Auto-generated method stub

    }


    public void addToDynamicProgress(int progressAdd) {
        // TODO Auto-generated method stub

    }


    public void addToStaticProgress(int progressAdd) {
        // TODO Auto-generated method stub

    }


    public void setMaxElementsForStaticProgress(int maxElements) {
        // TODO Auto-generated method stub

    }


    public void setMaxElementsForDynamicProgress(int maxElements) {
        // TODO Auto-generated method stub

    }


    public void setDownloadCancel(boolean cancel) {
        // TODO Auto-generated method stub

    }


    public boolean isDownloadCanceled() {
        // TODO Auto-generated method stub
        return false;
    }


    public DataOutputParserInterface getDataOutputParser() {
        return dataOutputParser;
    }


    public void setDataOutputParser(DataOutputParserInterface dataOutputParser) {
        this.dataOutputParser = dataOutputParser;
    }

    // public ClockModeType ReadClockMode()
    // {
    //
    //
    // this.WriteGenericCommandPacket(DexcomCommands.ReadClockMode);
    // byte commandId = 0;
    // byte[] payload = this.ReadGenericCommandPacket(this.CommandTimeout, out
    // commandId);
    // this.VerifyResponseCommandByte(commandId, payload);
    // this.VerifyPayloadLength(payload.Length, 1);
    // return (ClockModeType) payload[0];
    // }
    //
    // public void readData(DexcomCommand dexCmd)
    // {
    // CommandPacket cmdPacket = new CommandPacket(dexCmd);
    //
    // this.writeCommandAndReadResult2(command)
    //
    // }

}
