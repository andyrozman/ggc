package ggc.plugin.data.enums;

public enum PlugInExceptionType
{



    UnsupportedReceiver("XX"), //



    // Device
    InvalidPortOrPortClosed("Invalid port or port closed!"), //
    DeviceNotFoundOnConfiguredPort("DEVICE_NOT_FOUND_ON_CONFIGURED_PORT"), //
    DevicePortInUse("DEVICE_PORT_IN_USE"), //
    DeviceNAKOrInvalidCRC("Device reported NAK or an invalid CRC error."), //
    DeviceInvalidCommand("Device reported an invalid command error."), //
    DeviceInvalidParameterDesc("Device reported an invalid parameter error for parameter %s."), //
    DeviceInvalidParameter("Device reported an invalid parameter error."), //
    DeviceInternalError("Device reported an internal error."), //
    DeviceInvalidResponseCommand("Unknown or invalid response command %s (expected %s)."), //
    DeviceInvalidResponseLength("Device packet response length is %d (expected length is %d)"), //
    DeviceFailedToReadResponse("Failed to read contents of device packet"), //
    DeviceModelCouldNotBeIdentified("Device Model could not be identified."), //
    DeviceCouldNotBeContacted("Device could not be contacted."), //
    TimeoutReadingData("Timeout reading data from device."), //
    DeviceErrorWritingToDevice("EXC_RECEIVER_ERROR_WRITING"), //

    // Communication Port
    CommunicationPortClosed("Communication Port is closed."), //
    CommunicationError("Error Communicating with device. (%s)"), //
    CommunicationErrorWithCode("Device Communication Error: %s"), //

    // Download
    DownloadCanceledByUser("STATUS_STOPPED_USER"), //
    DownloadCanceledByDevice("Download Canceled by Device."), //

    // Data -  Commands
    UnknownDataReadWrongStartOfPacket("Unknown data read. Failed to read start of packet."), //
    NoDataFoundForConversion("No data found for converting %s."), //
    NoResponseFromDeviceForIssuedCommand("No response from device for Issued Command (%s)."), //
    Parsing_BytesParsingError("EXC_BYTES_PARSING_ERROR"), //
    ParsingError("EXC_PARSING_ERROR"), //
    ParsingErrorUnsupportedDataLenth("EXC_PARSING_ERROR_UNSUPP_DATA_LENGTH"), //
    FailedCRCCheck("EXC_FAILED_CRC_CHECK"), //
    FailedCRCCheckInPacket("Failed CRC check in packet."), //
    UnsupportedTypeOfParametersForCommand("EXC_UNSUPPORTED_TYPE_OF_PARAMS_FOR_CMD"), //

    // Handler
    WrongDeviceConfigurationSelected("Wrong device implementation selected: %s (supported %s)"), //
    OperationNotSupportedForThisHandler("Operation '%s' not supported for handler '%s'"), //
    UnsupportedDevice("Unsupported device for this reader: %s"), //

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
