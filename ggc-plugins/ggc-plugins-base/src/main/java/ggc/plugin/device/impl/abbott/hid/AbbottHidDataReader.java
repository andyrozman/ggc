package ggc.plugin.device.impl.abbott.hid;

import java.util.GregorianCalendar;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.atech.utils.data.BitUtils;

import ggc.plugin.data.enums.PlugInExceptionType;
import ggc.plugin.data.progress.ProgressType;
import ggc.plugin.device.DeviceIdentification;
import ggc.plugin.device.PlugInBaseException;
import ggc.plugin.device.v2.DeviceDefinition;
import ggc.plugin.device.v2.handler.UsbHidDeviceHandler;
import ggc.plugin.output.OutputWriter;
import ggc.plugin.protocol.reader.UsbHidDeviceReader;
import ggc.plugin.util.LogEntryType;

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

public abstract class AbbottHidDataReader extends UsbHidDeviceReader
{

    private static final Logger LOG = LoggerFactory.getLogger(AbbottHidDataReader.class);

    protected AbbottHidDataConverter converter;
    protected DeviceDefinition deviceDefinitionBase;
    protected BitUtils bitUtils;

    protected String serialNumber;
    protected GregorianCalendar currentDate;
    protected String softwareVersion;

    protected int configEntryProgress;
    protected int dataEntryProgress;


    public AbbottHidDataReader(UsbHidDeviceHandler libreCGMSHandler, DeviceDefinition definition, String portName, OutputWriter outputWriter) throws PlugInBaseException
    {
        super(libreCGMSHandler, portName, outputWriter);

        this.deviceDefinitionBase = definition;

        this.bitUtils = AbbottUtil.getBitUtils();
        AbbottUtil.setOutputWriter(outputWriter);
        this.outputWriter = outputWriter;

        initReader();

        AbbottUtil.resetDebug();
    }

    /**
     * Init reader - here we need to set typed Device Definition and correct converter.
     */
    protected abstract void initReader();



    @Override
    public void readData() throws PlugInBaseException
    {
        AbbottUtil.setCurrentDebug(false);

        configureProgressReporter(ProgressType.Static, 100, 100, 0);

        this.initDevice();
        this.writeDeviceIdentification();

        readDataDeviceSpecific();

        this.close();

        AbbottUtil.resetDebug();
    }

    /**
     * This is device/reader specific implementation for reading Data.
     */
    protected abstract void readDataDeviceSpecific() throws PlugInBaseException;

    /**
     * This is device/reader specific implementation for reading Configuration.
     */
    protected abstract void readConfigurationDeviceSpecific() throws PlugInBaseException;


    @Override
    public void readConfiguration() throws PlugInBaseException
    {
        AbbottUtil.setCurrentDebug(false);

        this.initDevice();

        readConfigurationDeviceSpecific();

        this.close();

        AbbottUtil.resetDebug();
    }



    public void initDevice() throws PlugInBaseException
    {
        this.connectAndInitDevice();
        this.communicationHandler.setThrowExceptionOnError(true);

        drainDevice();

        // init 1
        sendToDeviceAndCheckResponse(AbbottHidBaseCommand.INIT_REQUEST_1, new byte[] { 0xc });
        addToStaticProgress(5);

        // init 2 - read serialNumber
        writeReport(AbbottHidBaseCommand.INIT_REQUEST_2, "", "Serial Number");
        serialNumber = readTextResponse(AbbottHidBaseCommand.INIT_REQUEST_2).trim();
        System.out.println("serialN:" + serialNumber);
        addToStaticProgress(5);

        // init 3 - read software version
        writeReport(AbbottHidBaseCommand.INIT_REQUEST_3, "", "Software Version");
        softwareVersion = readTextResponse(AbbottHidBaseCommand.INIT_REQUEST_3).trim();
        System.out.println("softwareVersion:" + softwareVersion);
        addToStaticProgress(5);

        byte[] response = sendToDeviceAndReceiveResponse(AbbottHidBaseCommand.INIT_REQUEST_4);
        addToStaticProgress(5);

        System.out.println("Response: " + bitUtils.getDebugByteArray(response));

    }


    protected void addToStaticProgress(int progress)
    {
        addToProgress(ProgressType.Static, progress);
    }


    protected void drainDevice()
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
            catch (PlugInBaseException ex)
            {
                LOG.error("Draining the device. Exception: " + ex.getMessage(), ex);
            }
        } while (readInfo > 0);

    }


    public boolean sendToDeviceAndCheckResponse(AbbottHidBaseCommand command, byte[] expectedResponse)
            throws PlugInBaseException
    {
        writeReport(command, "", null);
        byte[] response = readByteResponse(command);

        if (AbbottUtil.getCurrentDebug())
            LOG.debug("Response: " + bitUtils.getByteArrayHex(response));

        if (compareByteArrayResult(expectedResponse, response))
        {
            throw new PlugInBaseException(PlugInExceptionType.DeviceInvalidResponseDescription,
                    new Object[] { response });
        }

        return true;
    }


    public byte[] sendToDeviceAndReceiveResponse(AbbottHidBaseCommand command) throws PlugInBaseException
    {
        writeReport(command, "", null);
        byte[] response = readByteResponse(command);

        if (AbbottUtil.getCurrentDebug())
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



    @Override
    public void initHandler()
    {

    }


    protected AbbottHidReportDto readReport() throws PlugInBaseException
    {
        byte[] resultData = new byte[64];

        int result = -1;

        do
        {
            try
            {
                result = this.communicationHandler.read(resultData);

                if (result == 0)
                    AbbottUtil.sleep(100);
            }
            catch (Exception ex)
            {}
        } while (result == 0);

        return new AbbottHidReportDto(resultData);
    }


    protected void writeReport(AbbottHidBaseCommand command, String textCommand, String description)
            throws PlugInBaseException
    {
        // LOG.debug("writeReport: command={}, value={}", command, textCommand);

        if (description != null)
        {
            outputWriter.writeLog(LogEntryType.INFO, "Reading " + description);
        }

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

        if (textCommand.length() != 0)
        {
            i++;
            sendArray[i] = (byte) textCommand.length();

            // int i = 1;

            for (Byte b : textCommand.getBytes())
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


    protected void writeReport(AbbottHidTextCommand libreTextCommand) throws PlugInBaseException
    {
        writeReport(AbbottHidBaseCommand.TEXT_COMMAND, libreTextCommand.getCommandText(),
            libreTextCommand.getDescription());
    }


    protected byte[] readByteResponse(AbbottHidBaseCommand command) throws PlugInBaseException
    {
        AbbottHidReportDto report = readReport();

        checkIfCorrectResponse(command, report.getMessageType());

        return report.extractByteResponse();
    }


    protected void checkIfCorrectResponse(int expectedCode, byte receivedCode) throws PlugInBaseException
    {
        if (expectedCode != receivedCode)
        {
            throw new PlugInBaseException(PlugInExceptionType.DeviceUnexpectedResponseCompared, //
                    new Object[] { expectedCode, receivedCode });
        }
    }


    protected void checkIfCorrectResponse(AbbottHidBaseCommand expectedCommand,
            AbbottHidBaseCommand receivedCommandResponse) throws PlugInBaseException
    {
        if (expectedCommand != receivedCommandResponse)
        {
            throw new PlugInBaseException(PlugInExceptionType.DeviceUnexpectedResponseCompared, //
                    new Object[] { expectedCommand.getResponseCode(), receivedCommandResponse.getResponseCode() });
        }
    }


    protected String readTextResponse(AbbottHidBaseCommand command) throws PlugInBaseException
    {
        AbbottHidReportDto report = readReport();

        checkIfCorrectResponse(command, report.getMessageType());

        return report.extractText();
    }


    protected String readTextResponse() throws PlugInBaseException
    {
        return readTextResponse(AbbottHidBaseCommand.TEXT_COMMAND);
    }





    protected void readAndConvertData(AbbottHidTextCommand textCommand) throws PlugInBaseException
    {
        writeReport(textCommand);
        converter.convertData(textCommand, readTextResponse());
    }




    protected void close()
    {
        this.communicationHandler.disconnectDevice();
    }


    protected void readMultiPacket(AbbottHidTextCommand command, boolean ignoreCrcCheck) throws PlugInBaseException
    {
        String text;
        StringBuffer contentBuffer = new StringBuffer();

        do
        {
            AbbottHidReportDto libreHidReportDto = readReport();

            text = libreHidReportDto.extractTextNoValidation();

            contentBuffer.append(text);

        } while (!text.contains("CMD"));

        String responseText = AbbottUtil.validateTextResponse(contentBuffer.toString(), false, ignoreCrcCheck);

        // System.out.println("got response Text:" + responseText);

        String[] responseLines = responseText.split("\r\n");

        // System.out.println("got response Lines:" + responseLines.length);

        for (int i = 0; i < responseLines.length - 1; i++) // ignoring the last line
        {
            converter.convertData(command, responseLines[i]);
        }
    }


    protected void writeDeviceIdentification()
    {
        DeviceIdentification deviceIdentification = this.outputWriter.getDeviceIdentification();

        deviceIdentification.deviceSerialNumber = serialNumber;
        deviceIdentification.deviceSoftwareVersion = softwareVersion;

        this.outputWriter.setDeviceIdentification(deviceIdentification);
        this.outputWriter.writeDeviceIdentification();
    }

}
