package ggc.cgms.device.dexcom.receivers;

import java.util.Enumeration;
import java.util.List;

import org.jdom.Element;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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
import ggc.cgms.device.dexcom.receivers.g4receiver.util.DexcomUtils;
import ggc.plugin.data.enums.PlugInExceptionType;
import ggc.plugin.data.progress.ProgressData;
import ggc.plugin.data.progress.ProgressReportInterface;
import ggc.plugin.data.progress.ProgressType;
import ggc.plugin.device.PlugInBaseException;
import ggc.plugin.output.OutputWriter;
import gnu.io.CommPortIdentifier;

/**
 *  Application:   GGC - GNU Gluco Control
 *  Plug-in:       CGMS Tool (support for Pump devices)
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
 *  Filename:     FRC_MinimedCarelink
 *  Description:  Minimed Carelink File Handler
 *
 *  Author: Andy {andy@atech-software.com}
 */

public class DexcomDeviceReader implements ProgressReportInterface
{

    private static final Logger LOG = LoggerFactory.getLogger(DexcomDeviceReader.class);

    private String portName;
    CommPortIdentifier portIdentifier;
    DexcomG4Api api = null;
    ProgressData progressData = new ProgressData();
    private DataOutputParserInterface dataOutputParser = new ConsoleOutputParser();
    boolean downloadCanceled = false;
    OutputWriter outputWriter;


    public DexcomDeviceReader(String portName, DexcomDevice dexcomDevice) throws PlugInBaseException
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

        LOG.debug(String.format("Serial Ports found: %s, configured port (%s) found: %s", sb.toString(), portName,
            deviceFound));

        if (!deviceFound)
            throw new PlugInBaseException(PlugInExceptionType.DeviceNotFoundOnConfiguredPort,
                    new Object[] { this.portName });

        if (dexcomDevice.getApi() == ReceiverApiType.G4_Api)
        {
            this.api = new DexcomG4Api(portName, this);
            DexcomUtils.setDexcomG4Api(api);
        }
        else
            throw new PlugInBaseException(PlugInExceptionType.UnsupportedReceiver);

    }


    public void setOutputWriter(OutputWriter outputWriter)
    {
        this.outputWriter = outputWriter;
    }


    public void downloadSettings() throws PlugInBaseException
    {
        try
        {
            int progressDynamic = 10; // header 1, serial 3, system 1, display
                                      // 2, lang 1, glu 1 clock 1
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
        catch (PlugInBaseException ex)
        {

            if (ex.getExceptionType() != null && ex.getExceptionType() == PlugInExceptionType.DownloadCanceledByUser)
                return;

            throw ex;
        }

    }


    private void parseData(DataOutputParserType parserType, ReceiverDownloadData data) throws PlugInBaseException
    {
        this.dataOutputParser.parse(parserType, data);
        this.addToProgressAndCheckIfCanceled(ProgressType.Dynamic, 1);
    }


    public void addToProgressAndCheckIfCanceled(ProgressType progressType, int progressAdd) throws PlugInBaseException
    {

        this.progressData.addToProgressAndCheckIfCanceled(progressType, progressAdd);

        // LOG.debug("Progress: " + this.progressData.calculateProgress());

        if (this.outputWriter != null)
        {
            this.outputWriter.setSpecialProgress(this.progressData.getCurrentProgress());
        }

        if (this.isDownloadCanceled())
            throw new PlugInBaseException(PlugInExceptionType.DownloadCanceledByUser);

    }


    public void downloadData() throws PlugInBaseException
    {
        try
        {

            ReceiverDownloadData data = new ReceiverDownloadData();

            int staticProgressMax = 4; // serial (2), partition (1), fw header
                                       // (1)
            for (ReceiverRecordType rrt : ReceiverRecordType.getDownloadSupported().keySet())
            {
                staticProgressMax += api.isDatabasePageRangeCached(rrt) ? 0 : 1;
            }

            this.configureProgressReporter(ProgressType.Dynamic_Static, 10, staticProgressMax, 200);

            // this.progressData.setCurrentProgressType(ProgressType.Static);
            int countDynamicElements = ReceiverRecordType.getDownloadSupported().size(); // parsing
            countDynamicElements++; // EGV has two parsing passes

            for (ReceiverRecordType rrt : ReceiverRecordType.getDownloadSupported().keySet())
            {
                DatabasePageRange range = api.readDatabasePageRange(rrt);
                countDynamicElements += range.getPagesCount();
            }

            this.progressData.setProgressDynamicMax(countDynamicElements);

            PartitionInfo info = this.api.readDatabasePartitionInfo();
            LOG.debug("Partition Info: PageLength: " + info.getPageDataLength());

            // FIXME
            // report

            LOG.debug("Partitions: " + info.getPartitions().size());

            data.setSerialNumber(api.readReceiverSerialNumber());

            // LOG.debug("Progress: " + progressData.calculateProgress());

            Element header = this.api.readFirmwareHeader();

            data.addConfigurationEntry("SOFTWARE_NUMBER", header.getAttributeValue("SoftwareNumber"), false);
            data.addConfigurationEntry("FIRMWARE_VERSION", header.getAttributeValue("FirmwareVersion"), false);

            // set dynamic
            // this.progressData.setCurrentProgressType(ProgressType.Dynamic);

            data.addData(DataOutputParserType.G4_UserEventData, api.readAllRecordsForEvents());
            parseData(DataOutputParserType.G4_UserEventData, data);

            data.addData(DataOutputParserType.G4_EGVData, api.readAllRecordsForEGVData());
            parseData(DataOutputParserType.G4_EGVData_SensorReading, data);
            parseData(DataOutputParserType.G4_EGVData_SensorTrend, data);

            data.addData(DataOutputParserType.G4_InsertionTime, api.readAllRecordsForInsertionTime());
            parseData(DataOutputParserType.G4_InsertionTime, data);

            data.addData(DataOutputParserType.G4_MeterData, api.readAllRecordsForMeterData());
            parseData(DataOutputParserType.G4_MeterData, data);

        }
        catch (PlugInBaseException ex)
        {
            if (ex.getExceptionType() != null && ex.getExceptionType() == PlugInExceptionType.DownloadCanceledByUser)
                return;

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
        LOG.debug("Partition Info: PageLength: " + info.getPageDataLength());

        LOG.debug("Partitions: " + info.getPartitions().size());

        // LOG.debug(api.readDatabasePageRange(ReceiverRecordType.InsertionTime));

        List<UserEventDataRecord> evs = api.readAllRecordsForEvents();
        LOG.debug("Events Records: " + evs.size());

        List<EGVRecord> recs = api.readAllRecordsForEGVData();

        LOG.debug("EGV Records: " + recs.size());

        // LanguageType lang = api.readLanguage();

        // LOG.debug("Language: " + lang.name());

        // GlucoseUnitType glu = api.readGlucoseUnit();

        // ClockModeType clk = api.readClockMode();

        // LOG.debug("Clock Mode: " + clk.name());

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

        // HashMap<String, String> map =
        // api.readAllRecordsForManufacturingData();

        // DexcomUtils.debugXmlTree(el);

        return data;
    }


    public void saveAllPages() throws PlugInBaseException
    {
        for (ReceiverRecordType recordType : ReceiverRecordType.values())
        {
            if (recordType == ReceiverRecordType.MaxValue || recordType == ReceiverRecordType.None)
            {
                continue;
            }
            LOG.debug("Droping pages for " + recordType.name());
            this.api.saveDatabasePages(recordType);
        }
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

            dr = new DexcomDeviceReader("/dev/ttyACM0", DexcomDevice.Dexcom_G4);
            // Date d = dr.getDisplayTime();
            // System.out.println("Display Time: " + d);
            // System.out.println("SysTime: " + dr.getSystemTime());

            // dr.downloadSettings();
            // dr.downloadData();
            dr.saveAllPages();

            // dr.doDownloadReceiverDataAsObject();

            // Date d = new

            /*
             * System.out.println("SysTime: " + dr.getSystemTime());
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
            if (dr != null)
            {
                dr.dispose();
            }
        }

    }


    public void setDownloadCancel(boolean cancel)
    {
        downloadCanceled = cancel;
    }


    public boolean isDownloadCanceled()
    {
        if (this.outputWriter != null)
            return this.outputWriter.isReadingStopped();
        else
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
