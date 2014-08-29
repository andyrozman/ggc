package ggc.cgms.device.dexcom.receivers;

import ggc.cgms.device.dexcom.receivers.data.ReceiverDownloadData;
import ggc.cgms.device.dexcom.receivers.data.output.ConsoleOutputParser;
import ggc.cgms.device.dexcom.receivers.data.output.DataOutputParserInterface;
import ggc.cgms.device.dexcom.receivers.data.output.DataOutputParserType;
import ggc.cgms.device.dexcom.receivers.g4receiver.DexcomG4Api;
import ggc.cgms.device.dexcom.receivers.g4receiver.data.EGVRecord;
import ggc.cgms.device.dexcom.receivers.g4receiver.data.UserEventDataRecord;
import ggc.cgms.device.dexcom.receivers.g4receiver.enums.ReceiverRecordType;
import ggc.cgms.device.dexcom.receivers.g4receiver.internal.DatabasePageRange;
import ggc.cgms.device.dexcom.receivers.g4receiver.internal.PartitionInfo;
import ggc.cgms.device.dexcom.receivers.g4receiver.util.DexcomException;
import ggc.cgms.device.dexcom.receivers.g4receiver.util.DexcomExceptionType;
import ggc.cgms.device.dexcom.receivers.g4receiver.util.DexcomUtils;
import ggc.plugin.data.progress.ProgressData;
import ggc.plugin.data.progress.ProgressType;
import gnu.io.CommPortIdentifier;

import java.util.Enumeration;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.jdom.Element;



public class DexcomDeviceReader implements DexcomDeviceProgressReport
{
    private Log log = LogFactory.getLog(DexcomDeviceReader.class);

    private String portName;
    CommPortIdentifier portIdentifier;
    DexcomG4Api api = null;
    ProgressData progressData = new ProgressData();
    private DataOutputParserInterface dataOutputParser = new ConsoleOutputParser();
    boolean downloadCanceled = false;

    public DexcomDeviceReader(String portName, DexcomDevice dexcomDevice) throws DexcomException
    {
        this.portName = portName;

        @SuppressWarnings("rawtypes")
        Enumeration e = CommPortIdentifier.getPortIdentifiers();
        StringBuilder sb = new StringBuilder();

        boolean deviceFound = false;

        while (e.hasMoreElements())
        {
            CommPortIdentifier comp = (CommPortIdentifier) e.nextElement();

            if (comp.getName().equals(this.portName))
            {
                deviceFound = true;
            }

            sb.append(comp.getName() + " ");
        }

        log.debug(String.format("Serial Ports found: %s, configured port (%s) found: %s", sb.toString(), portName,
            deviceFound));

        if (!deviceFound)
        {
            throw new DexcomException(DexcomExceptionType.DeviceNotFoundOnConfiguredPort,
                    new Object[] { this.portName });
        }

        if (dexcomDevice.getApi() == ReceiverApiType.G4_Api)
        {
            this.api = new DexcomG4Api(portName, this);
            DexcomUtils.setDexcomG4Api(api);
        }
        else
        {
            throw new DexcomException(DexcomExceptionType.UnsupportedReceiver);
        }

    }

    public void downloadSettings() throws DexcomException
    {
        try
        {
        	int progressDynamic = 10; // header 1, serial 3, system 1, display 2, lang 1, glu 1 clock 1
        	progressDynamic += api.isDatabasePageRangeCached(ReceiverRecordType.ManufacturingData) ? 0 : 1;
        	
        	
            this.progressData.configureProgressReporter(ProgressType.Dynamic, 0, 0, progressDynamic);

            ReceiverDownloadData data = new ReceiverDownloadData();
            
            Element header = this.api.readFirmwareHeader();

            data.addConfigurationEntry("API_VERSION", header.getAttributeValue("ApiVersion"), false);
            data.addConfigurationEntry("PRODUCT_ID", header.getAttributeValue("ProductId"), false);
            data.addConfigurationEntry("PRODUCT_NAME", header.getAttributeValue("ProductName"), false);
            data.addConfigurationEntry("SOFTWARE_NUMBER", header.getAttributeValue("SoftwareNumber"), false);
            data.addConfigurationEntry("FIRMWARE_VERSION", header.getAttributeValue("FirmwareVersion"), false);
            data.addConfigurationEntry("PORT_VERSION", header.getAttributeValue("PortVersion"), false);
            data.addConfigurationEntry("RF_VERSION", header.getAttributeValue("RFVersion"), false);
           
            data.setSerialNumber(api.readReceiverSerialNumber());

            data.addConfigurationEntry("SYSTEM_TIME", DexcomUtils.getDateTimeString(api.readSystemTimeAsDate()), false);
            data.addConfigurationEntry("DISPLAY_TIME", DexcomUtils.getDateTimeString(api.readDisplayTimeAsDate()),
                false);
            data.addConfigurationEntry("LANGUAGE", api.readLanguage().getDescription(), true);
            data.addConfigurationEntry("GLUCOSE_UNIT", api.readGlucoseUnit().getDescription(), true);
            data.addConfigurationEntry("CLOCK_MODE", api.readClockMode().getDescription(), true);

            parseData(DataOutputParserType.Configuration, data);

        }
        catch (DexcomException ex)
        {

            if ((ex.getExceptionType() != null)
                    && (ex.getExceptionType() == DexcomExceptionType.DownloadCanceledByUser))
            {
                return;
            }

            throw ex;
        }

    }
    
    
    private void parseData(DataOutputParserType parserType, ReceiverDownloadData data) throws DexcomException
    {
    	this.dataOutputParser.parse(parserType, data);
        this.addToProgressAndCheckIfCanceled(ProgressType.Dynamic, 1);
    }

    public void addToProgressAndCheckIfCanceled(ProgressType progressType, int progressAdd) throws DexcomException
    {

        this.progressData.addToProgressAndCheckIfCanceled(progressType, progressAdd);

        log.debug("Progress: " + this.progressData.calculateProgress());

        if (this.isDownloadCanceled())
        {
            throw new DexcomException(DexcomExceptionType.DownloadCanceledByUser);
        }

    }

    public void downloadData() throws DexcomException
    {
        try {
			
			ReceiverDownloadData data = new ReceiverDownloadData();
			
			int staticProgressMax = 3; // serial (2), partition (1)
 			for (ReceiverRecordType rrt : ReceiverRecordType.getDownloadSupported().keySet())
			{
				staticProgressMax += api.isDatabasePageRangeCached(rrt) ? 0 : 1;
			}

			this.configureProgressReporter(ProgressType.Dynamic_Static, 10, staticProgressMax, 200);
			
			

			this.progressData.setCurrentProgressType(ProgressType.Static); 
			int countDynamicElements = ReceiverRecordType.getDownloadSupported().size(); // parsing
			
			for (ReceiverRecordType rrt : ReceiverRecordType.getDownloadSupported().keySet())
			{
				DatabasePageRange range = api.readDatabasePageRange(rrt); 
				countDynamicElements += range.getPagesCount();
			}

			//log.debug(" === Progress should be 4");
			
			this.progressData.setProgressDynamicMax(countDynamicElements);
			
			

			//log.debug(" === Progress should be 6");
			
			PartitionInfo info = this.api.readDatabasePartitionInfo();
			log.debug("Partition Info: PageLength: " + info.getPageDataLength());

			// FIXME
			// report

			log.debug("Partitions: " + info.getPartitions().size());

			data.setSerialNumber(api.readReceiverSerialNumber());
			
			log.debug("Progress: " + progressData.calculateProgress());

			//log.debug(" === Progress should be 7");
			
			
			
			// set dynamic
			this.progressData.setCurrentProgressType(ProgressType.Dynamic);

			data.addData(DataOutputParserType.G4_UserEventData, api.readAllRecordsForEvents());
            parseData(DataOutputParserType.Configuration, data);


			data.addData(DataOutputParserType.G4_EGVData, api.readAllRecordsForEGVData());
            parseData(DataOutputParserType.Configuration, data);


			data.addData(DataOutputParserType.G4_InsertionTime, api.readAllRecordsForInsertionTime());
            parseData(DataOutputParserType.Configuration, data);


			data.addData(DataOutputParserType.G4_MeterData, api.readAllRecordsForMeterData());
            parseData(DataOutputParserType.Configuration, data);
			
			
		} catch (DexcomException ex) {
			if ((ex.getExceptionType() != null)
                    && (ex.getExceptionType() == DexcomExceptionType.DownloadCanceledByUser))
            {
                return;
            }

            throw ex;
		}

    }

    public ReceiverDownloadData testDownload() throws Exception
    {

        // DexcomReader api = this;

        ReceiverDownloadData data = new ReceiverDownloadData();

        

        // Element header = this.api.readFirmwareHeader();
        //


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

 
        return data;
    }

    public void dispose()
    {
        this.api.disconnectDevice();
    }

    public static void main(String[] args)
    {
    	DexcomDeviceReader dr = null;
    	
        try
        {

            long now = System.currentTimeMillis();

            dr = new DexcomDeviceReader("COM9", DexcomDevice.Dexcom_G4);
            // Date d = dr.getDisplayTime();
            // System.out.println("Display Time: " + d);
            // System.out.println("SysTime: " + dr.getSystemTime());
            
            dr.downloadSettings();
            //dr.downloadData();

            //dr.doDownloadReceiverDataAsObject();

            // Date d = new

            /*
             * System.out.println("SysTime: " + dr.getSystemTime());
             * 
             * //dr.getDisplayTimeOffset(); System.out.println("Difference: " +
             * dr.getDisplayTimeOffset()); System.out.println("SysTime: " +
             * dr.getDisplayTime());
             */

            dr.dispose();

            double time_elapsed = System.currentTimeMillis() - now;

            time_elapsed /= 1000.0;

            System.out.println("Time needed for reading " + time_elapsed + " s.");
        }
        catch (Exception ex)
        {
            System.out.println("Error reading from " + ex);
            ex.printStackTrace();
        }
        finally
        {
        	if (dr!=null)
        		dr.dispose();
        }
    
    

    }

    public void setDownloadCancel(boolean cancel)
    {
        downloadCanceled = cancel;
    }

    public boolean isDownloadCanceled()
    {
        return downloadCanceled;
    }

    public DataOutputParserInterface getDataOutputParser()
    {
        return dataOutputParser;
    }

    public void setDataOutputParser(DataOutputParserInterface dataOutputParser)
    {
        this.dataOutputParser = dataOutputParser;
    }

    public void addToProgress(ProgressType arg0, int arg1)
    {
    }

    public void configureProgressReporter(ProgressType baseProgressType, int staticProgressPercentage,
            int staticMaxElements, int dynamicMaxElements)
    {
        this.progressData.configureProgressReporter(baseProgressType, staticProgressPercentage, staticMaxElements,
            dynamicMaxElements);
    }

}
