package ggc.cgms.device.dexcom.receivers.g4receiver.util;

public enum DexcomExceptionType {

    ParsingError("DXC_PARSING_ERROR"), //
    ParsingErrorUnsupportedDataLenth("DXC_PARSING_ERROR_UNSUPP_DATA_LENGTH"), //
    Receiver_ErrorWritingToReceiver("DXC_RECEIVER_ERROR_WRITING"), //
    Parsing_BytesParsingError("DXC_BYTES_PARSING_ERROR"), //
    FailedCRCCheck("DXC_FAILED_CRC_CHECK"), //
    UnsupportedTypeOfParametersForCommand("DXC_UNSUPPORTED_TYPE_OF_PARAMS_FOR_CMD"), //
    UnsupportedReceiver("DXC_UNSUPPORTED_RECEIVER"),
    DownloadCanceledByUser("STATUS_STOPPED_USER"),
    DeviceNotFoundOnConfiguredPort("DEVICE_NOT_FOUND_ON_CONFIGURED_PORT"),
    DevicePortInUse("DEVICE_PORT_IN_USE"),

    ;

    // DXC_PARSING_ERROR = Error parsing. Exception: %s
    // DXC_PARSING_ERROR_UNSUPP_DATA_LENGTH = Error parsing. Unsupported data length: %s (allowed lengths: %s)
    // DXC_RECEIVER_ERROR_WRITING = Error writing to Receiver: %s
    // DXP_DATABASE_PAGE_PARSING_ERROR = Error parsing %s from bytes: %s
    // DXP_FAILED_CRC_CHECK = Failed CRC check in %s (expected: %s, calculated: %s)
    // DXP_UNSUPPORTED_TYPE_OF_PARAMS_FOR_CMD = Unsupported type of parameter(s) for command !
    // DXC_UNSUPPORTED_RECEIVER = Unsupported receiver (we currently support only G4 Receiver) !

    String errorMessage;


    private DexcomExceptionType(String errorMessage) {
        this.errorMessage = errorMessage;
    }

}
