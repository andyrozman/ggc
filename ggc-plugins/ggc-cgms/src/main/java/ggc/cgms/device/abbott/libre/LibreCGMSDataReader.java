package ggc.cgms.device.abbott.libre;

import java.util.GregorianCalendar;

import ggc.cgms.device.abbott.libre.enums.BaseCommand;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.atech.utils.data.ATechDate;
import com.atech.utils.data.ATechDateType;
import com.atech.utils.data.BitUtils;

import ggc.cgms.defs.device.CGMSDeviceDefinition;
import ggc.cgms.device.abbott.libre.data.LibreDataConverter;
import ggc.cgms.device.abbott.libre.data.LibreHidReportDto;

import ggc.cgms.device.abbott.libre.enums.LibreTextCommand;
import ggc.cgms.device.abbott.libre.util.LibreUtil;
import ggc.cgms.util.DataAccessCGMS;
import ggc.plugin.data.enums.PlugInExceptionType;
import ggc.plugin.data.progress.ProgressType;
import ggc.plugin.device.PlugInBaseException;
import ggc.plugin.device.v2.DeviceDefinition;
import ggc.plugin.output.OutputWriter;
import ggc.plugin.protocol.reader.UsbHidDeviceReader;

/**
 *  Application:   GGC - GNU Gluco Control
 *  Plug-in:       Meter Tool (support for Meter devices)
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
 *  Filename:     LibraCGMSDataReader
 *  Description:  Data reader for Arkray family
 *
 *  Author: Andy {andy@atech-software.com}
 */

public class LibreCGMSDataReader extends UsbHidDeviceReader
{

    private static final Logger LOG = LoggerFactory.getLogger(LibreCGMSDataReader.class);

    private final LibreDataConverter converter;
    private CGMSDeviceDefinition deviceDefinition;
    private BitUtils bitUtils;

    String serialNumber;
    private GregorianCalendar currentDate;
    String softwareVersion;


    public LibreCGMSDataReader(LibreCGMSHandler libreCGMSHandler, DeviceDefinition definition, String portName,
            OutputWriter outputWriter) throws PlugInBaseException
    {
        super(libreCGMSHandler, portName, outputWriter);

        this.deviceDefinition = (CGMSDeviceDefinition) definition;
        // configureProgressReporter(ProgressType.Static, 100,
        // this.deviceDefinition.getMaxRecords(), 0);
        this.bitUtils = LibreUtil.getBitUtils();
        LibreUtil.setOutputWriter(outputWriter);

        converter = new LibreDataConverter(outputWriter);

        LibreUtil.resetDebug();
    }


    @Override
    public void readData() throws PlugInBaseException
    {
        LibreUtil.setCurrentDebug(false);

        configureProgressReporter(ProgressType.Static, 100, 100, 0);

        this.initDevice();

        this.readHistory();
        addToStaticProgress(40);

        this.readOtherHistory();
        addToStaticProgress(40);

        this.close();

        LibreUtil.resetDebug();
    }


    @Override
    public void readConfiguration() throws PlugInBaseException
    {
        LibreUtil.setCurrentDebug(false);

        this.initDevice();

        converter.convertData(LibreTextCommand.SerialNumber, serialNumber);
        converter.convertData(LibreTextCommand.SoftwareVersion, softwareVersion);

        this.readDateTime();

        readAndConvertData(LibreTextCommand.NtSound);
        addToStaticProgress(20);
        readAndConvertData(LibreTextCommand.BtSound);
        addToStaticProgress(20);

        // Glucose Unit
        // Bg Range

        this.close();

        LibreUtil.resetDebug();
    }


    public void initDevice() throws PlugInBaseException
    {
        this.connectAndInitDevice();
        this.communicationHandler.setThrowExceptionOnError(true);

        drainDevice();

        // init 1
        sendToDeviceAndCheckResponse(
                BaseCommand.INIT_REQUEST_1, new byte[] { 0xc });
        addToStaticProgress(5);

        // init 2 - read serialNumber
        writeReport(BaseCommand.INIT_REQUEST_2, "");
        serialNumber = readTextResponse(BaseCommand.INIT_REQUEST_2).trim();
        addToStaticProgress(5);

        // init 3 - read software version
        writeReport(BaseCommand.INIT_REQUEST_3, "");
        softwareVersion = readTextResponse(BaseCommand.INIT_REQUEST_3).trim();
        addToStaticProgress(5);

        // System.out.println("Software Version: " + softwareVersion);

        byte[] response = sendToDeviceAndReceiveResponse(BaseCommand.INIT_REQUEST_4);
        addToStaticProgress(5);

        // System.out.println("Response: " + bitUtils.getDebugByteArray(response));

    }


    private void addToStaticProgress(int progress)
    {
        addToProgress(ProgressType.Static, progress);
    }


    private void drainDevice()
    {
        byte[] data = new byte[1024];
        int readInfo = 0;

        do
        {
            try
            {
                readInfo = communicationHandler.read(data);
                if (readInfo > 0)
                    LOG.debug("Drained {} bytes.", readInfo);
            }
            catch (PlugInBaseException e)
            {

            }
        } while (readInfo > 0);

    }


    public boolean sendToDeviceAndCheckResponse(BaseCommand command, byte[] expectedResponse) throws PlugInBaseException
    {
        writeReport(command, "");
        byte[] response = readByteResponse(command);

        if (LibreUtil.getCurrentDebug())
            LOG.debug("Response: " + bitUtils.getByteArrayHex(response));

        if (compareByteArrayResult(expectedResponse, response))
        {
            throw new PlugInBaseException(PlugInExceptionType.DeviceInvalidResponseDescription,
                    new Object[] { response });
        }

        return true;
    }


    public byte[] sendToDeviceAndReceiveResponse(BaseCommand command) throws PlugInBaseException
    {
        writeReport(command, "");
        byte[] response = readByteResponse(command);

        if (LibreUtil.getCurrentDebug())
            LOG.debug("Response: " + bitUtils.getByteArrayHex(response));

        return response;
    }


    private boolean compareByteArrayResult(byte[] expected, byte[] actual)
    {
        if (expected.length != actual.length)
            return false;

        for (int i = 0; i < expected.length; i++)
        {
            if (expected[i] != actual[i])
            {
                return false;
            }
        }

        return true;
    }


    @Override
    public void customInitAndChecks() throws PlugInBaseException
    {
        // FIXME
    }


    @Override
    public void configureProgressReporter()
    {
        // FIXME
    }


    public DataAccessCGMS getDataAccess()
    {
        return DataAccessCGMS.getInstance();
    }


    public CGMSDeviceDefinition getDeviceDefinition()
    {
        return deviceDefinition;
    }


    @Override
    public void initHandler()
    {

    }


    private LibreHidReportDto readReport() throws PlugInBaseException
    {
        byte[] resultData = new byte[64];

        int result = -1;

        do
        {
            try
            {
                result = this.communicationHandler.read(resultData);

                if (result == 0)
                    DataAccessCGMS.sleep(100);
            }
            catch (Exception ex)
            {}
        } while (result == 0);

        return new LibreHidReportDto(resultData);
    }


    private void writeReport(BaseCommand command, String s) throws PlugInBaseException
    {
        LOG.debug("writeReport: command={}, value={}", command, s);

        // int length = 2;
        // boolean hasPayload = false;

        // if (s.length() != 0)
        // {
        // s += "\r\n";
        // length++;
        // length += s.length();
        // hasPayload = true;
        // }

        byte[] sendArray = new byte[64];

        int i = 0;

        // sendArray[0] = 0x00;
        sendArray[i] = (byte) command.getRequestCode();

        if (s.length() != 0)
        {
            i++;
            sendArray[i] = (byte) s.length();

            // int i = 1;

            for (Byte b : s.getBytes())
            {
                i++;
                sendArray[i] = b;
            }

            // i++;
        }

        int result = communicationHandler.writeWithReturn(sendArray);

        // System.out.println(
        // "Send code: result: " + result + ", data: " +
        // LibreUtil.getBitUtils().getDebugByteArray(sendArray));
    }


    private void writeReport(LibreTextCommand libreTextCommand) throws PlugInBaseException
    {
        writeReport(BaseCommand.TEXT_COMMAND, libreTextCommand.getCommandText());
    }


    private byte[] readByteResponse(BaseCommand command) throws PlugInBaseException
    {
        LibreHidReportDto report = readReport();

        checkIfCorrectResponse(command, report.getMessageType());

        return report.extractByteResponse();
    }


    private void checkIfCorrectResponse(int expectedCode, byte receivedCode) throws PlugInBaseException
    {
        if (expectedCode != receivedCode)
        {
            throw new PlugInBaseException(PlugInExceptionType.DeviceUnexpectedResponseCompared, //
                    new Object[] { expectedCode, receivedCode });
        }
    }


    private void checkIfCorrectResponse(BaseCommand expectedCommand, BaseCommand receivedCommandResponse)
            throws PlugInBaseException
    {
        if (expectedCommand != receivedCommandResponse)
        {
            throw new PlugInBaseException(PlugInExceptionType.DeviceUnexpectedResponseCompared, //
                    new Object[] { expectedCommand.getResponseCode(), receivedCommandResponse.getResponseCode() });
        }
    }


    private String readTextResponse(BaseCommand command) throws PlugInBaseException
    {
        LibreHidReportDto report = readReport();

        checkIfCorrectResponse(command, report.getMessageType());

        return report.extractText();
    }


    public String readTextResponse() throws PlugInBaseException
    {
        return readTextResponse(BaseCommand.TEXT_COMMAND);
    }


    private void readDateTime() throws PlugInBaseException
    {
        currentDate = new GregorianCalendar();
        writeReport(LibreTextCommand.Date);
        String date = readTextResponse();
        addToStaticProgress(20);
        writeReport(LibreTextCommand.Time);
        String time = readTextResponse();
        addToStaticProgress(20);

        converter.convertData(LibreTextCommand.Date, date + "," + time);

        converter.convertData(LibreTextCommand.ComputerDateTime,
            ATechDate.getATechDateFromGC(currentDate, ATechDateType.DateAndTimeSec).toString());
    }


    private void readAndConvertData(LibreTextCommand textCommand) throws PlugInBaseException
    {
        writeReport(textCommand);
        converter.convertData(textCommand, readTextResponse());
    }


    private void readHistory() throws PlugInBaseException
    {
        writeReport(LibreTextCommand.History);
        readMultiPacket(LibreTextCommand.History, false);
    }


    private void readOtherHistory() throws PlugInBaseException
    {
        writeReport(LibreTextCommand.OtherHistory);
        readMultiPacket(LibreTextCommand.OtherHistory, true);
    }


    private void close()
    {
        this.communicationHandler.disconnectDevice();
    }


    private void readMultiPacket(LibreTextCommand command, boolean ignoreCrcCheck) throws PlugInBaseException
    {
        String text;
        StringBuffer contentBuffer = new StringBuffer();

        do
        {
            LibreHidReportDto libreHidReportDto = readReport();

            text = libreHidReportDto.extractTextNoValidation();

            contentBuffer.append(text);

        } while (!text.contains("CMD"));

        String responseText = LibreUtil.validateTextResponse(contentBuffer.toString(), false, ignoreCrcCheck);

        // System.out.println("got response Text:" + responseText);

        String[] responseLines = responseText.split("\r\n");

        // System.out.println("got response Lines:" + responseLines.length);

        for (int i = 0; i < responseLines.length - 1; i++) // ignoring the last line
        {
            converter.convertData(command, responseLines[i]);
        }
    }

}
