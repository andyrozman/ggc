package ggc.plugin.device.impl.animas.util;

/**
 *  Application:   GGC - GNU Gluco Control
 *  Plug-in:       Pump Tool (support for Pump devices)
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
 *  Filename:     AnimasIR2020
 *  Description:  Animas IR 2020 implementation
 *
 *  Author: Andy Rozman {andy@atech-software.com}
 */

public enum AnimasExceptionType
{

    CommunicationPortClosed(""), //
    PumpModelCouldNotBeIdentified(""), //
    WrongPumpConfigurationSelected(""), //
    OperationNotSupported(""), //
    OperationNotSupportedForThisHandler(""), //
    ErrorIRSNoAck(""), //
    DeviceNotFoundOnConfiguredPort("DEVICE_NOT_FOUND_ON_CONFIGURED_PORT"), //
    UnsupportedDevice(""), //
    DownloadCanceledByUser(""), //
    CommError_IRSNoAck(""), //
    CommErrorWithCode("Pump Communication Error: %s"), //
    PumpDownloadTimeout(""), //
    DownloaderNotIdentified(""), //
    DeviceDoesNotSupportThisOperation(""), //

    // DX
    // ParsingError("DXC_PARSING_ERROR"), //
    // ParsingErrorUnsupportedDataLenth("DXC_PARSING_ERROR_UNSUPP_DATA_LENGTH"),
    // //
    // Receiver_ErrorWritingToReceiver("DXC_RECEIVER_ERROR_WRITING"), //
    // Parsing_BytesParsingError("DXC_BYTES_PARSING_ERROR"), //
    // FailedCRCCheck("DXC_FAILED_CRC_CHECK"), //
    // UnsupportedTypeOfParametersForCommand("DXC_UNSUPPORTED_TYPE_OF_PARAMS_FOR_CMD"),
    // //
    // UnsupportedReceiver("DXC_UNSUPPORTED_RECEIVER"),
    // DownloadCanceledByUser("STATUS_STOPPED_USER"),
    // DeviceNotFoundOnConfiguredPort(
    // "DEVICE_NOT_FOUND_ON_CONFIGURED_PORT"),
    // DevicePortInUse("DEVICE_PORT_IN_USE"),

    DeviceCouldNotBeContacted(""), //
    PumpCommunicationTimeout(""), //
    NoResponseFromDeviceForIssuedCommand(""), ErrorCommunciationgWithDevice(""), DownloadCanceledByDevice("");

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

    private AnimasExceptionType(String errorMessage)
    {
        this.errorMessage = errorMessage;
    }

}
