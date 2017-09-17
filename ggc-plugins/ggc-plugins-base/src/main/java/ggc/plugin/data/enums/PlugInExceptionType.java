package ggc.plugin.data.enums;

/**
 *  Application:   GGC - GNU Gluco Control
 *  Plug-in:       GGC PlugIn Base (base class for all plugins)
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
 *  Filename:     PlugInExceptionType
 *  Description:  Plugin Exception Type (new way of creating exceptions)
 *
 *  Author: Andy {andy@atech-software.com}
 */

public enum PlugInExceptionType
{

    UnsupportedReceiver("XX"), //

    // Interface
    UnsupportedAndNotPlannedInterface("%s interface is not supported and is not planned to be supported."), //
    YetUnsupportedInterface("%s interface is not YET supported."), //
    NeedToSelectInterface("You need to select valid interface."), //
    InterfaceProblem("Problem with computer interface. %s"), //

    // Device
    InvalidPortOrPortClosed("Invalid port or port closed!"), //
    DeviceNotFoundOnConfiguredPort("DEVICE_NOT_FOUND_ON_CONFIGURED_PORT"), //
    DeviceNotFoundOnConfiguredPortOtherAvailable(
            "Device not found on configured port, but other ports are available (%s)."), //

    DevicePortInUse("DEVICE_PORT_IN_USE"), //
    DeviceNAKOrInvalidCRC("Device reported NAK or an invalid CRC error."), //
    DeviceNAKOrInvalidCRCDesc("Device reported NAK or an invalid CRC error. (%s)"), //

    DeviceNAK("Device reported NAK error [%s]."), //
    DeviceSerialNumberCouldNotBeRead("Serial number was not read from device."), //

    DeviceInvalidCommand("Device reported an invalid command error."), //
    DeviceIsInWrongState("Device is in wrong state [current=%s, expected=%s]"), //
    DeviceInvalidParameterDesc("Device reported an invalid parameter error for parameter %s."), //
    DeviceInvalidParameter("Device reported an invalid parameter error."), //
    DeviceInternalError("Device reported an internal error."), //
    DeviceInvalidResponseCommand("Unknown or invalid response for command: %s (expected: %s)."), //
    DeviceInvalidResponseLength("Device packet response length is %d (expected length is %d)"), //
    DeviceInvalidResponseDescription("Unknown or invalid response - (%s)."), //
    DeviceFailedToReadResponse("Failed to read contents of device packet"), //
    DeviceModelCouldNotBeIdentified("Device Model could not be identified."), //
    DeviceCouldNotBeContacted("Device could not be contacted."), //
    TimeoutReadingData("Timeout reading data from device."), //
    DeviceErrorWritingToDevice("EXC_RECEIVER_ERROR_WRITING"), //
    DeviceNotFound("Device not found."), //
    ErrorWithDeviceCommunication("Error when communicating with device. Exception: %s"), //
    ErrorWithDeviceCommunicationDescription("Error when communicating with device [%s]. Exception: %s"), //

    // Communication Port
    CommunicationPortClosed("Communication Port is closed."), //
    CommunicationError("Error Communicating with device. (%s)"), //
    CommunicationErrorWithCode("Device Communication Error: %s"), //

    // Download
    DownloadCanceledByUser("STATUS_STOPPED_USER"), //
    DownloadCanceledByDevice("Download Canceled by Device."), //
    DeviceUnreachableRetry("Device unreachangle, trying to retry."), //

    // Data - Commands
    UnknownDataReadWrongStartOfPacket("Unknown data read. Failed to read start of packet."), //
    WrongResponseStatus("Wrong response status or start of command. Expected status: %s, returned statuses are: %s."), //
    NoDataFoundForConversion("No data found for converting %s."), //
    NoResponseFromDeviceForIssuedCommand("No response from device for Issued Command (%s)."), //
    Parsing_BytesParsingError("EXC_BYTES_PARSING_ERROR"), //
    ParsingError("EXC_PARSING_ERROR"), //
    ParsingErrorUnsupportedDataLenth("EXC_PARSING_ERROR_UNSUPP_DATA_LENGTH"), //
    FailedCRCCheck("Device response failed CRC check. [expected=%s, got=%s]"), // "EXC_FAILED_CRC_CHECK"
    FailedCRCCheckInPacket("Failed CRC check in packet."), //
    FailedEncryptionDecryption("Failed encryption/decryption of data."), //
    FailedEncryptionDecryptionDesc("Failed encryption/decryption of data - (%s)."), //
    UnsupportedTypeOfParametersForCommand("EXC_UNSUPPORTED_TYPE_OF_PARAMS_FOR_CMD"), //

    // Handler
    WrongDeviceConfigurationSelected("Wrong device implementation selected: %s (supported %s)"), //
    OperationNotSupportedForThisHandler("Operation '%s' not supported for handler '%s'"), //
    UnsupportedDevice("Unsupported device for this reader: %s"), //
    InvalidInternalConfiguration("Problem with plugin configuration: %s"), //

    DeviceInvalidResponse("Device returned invalid response. Expected: %s, Received: %s"), //
    DeviceCommandInvalidResponse("Device was sent command [%s], but it responded with [%s], instead of [%s]"), //
    DeviceUnexpectedResponse("Device returned unexpected response. Received: %s"), //
    DeviceUnexpectedResponseCompared("Device returned unexpected response. Expected: %s, Received: %s"), //

    DeviceReturnedError("Device returned error [command=%s, errorCode=%s, errorDescription=%s, returnedData=%s]"), //

    ImportFileNotFound("Import file (name=%s) not found."), //
    ImportFileCouldNotBeRead("Import file (name=%s) could not be read (%s)"), //
    ErrorCommunicationWithProtocolHandler("Error communicating with specific library [%s]: (%s)"), //

    Hid4JavaAPIError("Error using Hid4Java API: %s\n%s"), //
    ;

    // DXC_PARSING_ERROR = Error parsing. Exception: %s
    // DXC_PARSING_ERROR_UNSUPP_DATA_LENGTH = Error parsing. Unsupported data
    // length: %s (allowed lengths: %s)
    // DXC_RECEIVER_ERROR_WRITING = Error writing to Receiver: %s
    // DXP_DATABASE_PAGE_PARSING_ERROR = Error parsing %s from bytes: %s
    // DXP_FAILED_CRC_CHECK = Failed CRC check in %s (expected: %s, calculated:
    // %s)
    // DXP_UNSUPPORTED_TYPE_OF_PARAMS_FOR_CMD = Unsupported type of parameter(s)
    // for command !
    // DXC_UNSUPPORTED_RECEIVER = Unsupported receiver (we currently support
    // only G4 Receiver) !

    String errorMessage;


    private PlugInExceptionType(String errorMessage)
    {
        this.errorMessage = errorMessage;
    }


    public String getErrorMessage()
    {
        return this.errorMessage;
    }

}
