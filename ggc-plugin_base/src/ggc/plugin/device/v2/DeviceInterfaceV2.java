package ggc.plugin.device.v2;

import java.util.List;

import com.atech.graphics.dialogs.selector.SelectableInterfaceV2;

import ggc.plugin.data.GGCPlugInFileReaderContext;
import ggc.plugin.data.enums.DeviceCompanyDefinition;
import ggc.plugin.data.enums.DevicePortParameterType;
import ggc.plugin.data.enums.DeviceProgressStatus;
import ggc.plugin.device.DownloadSupportType;
import ggc.plugin.device.PlugInBaseException;
import ggc.plugin.gui.DeviceSpecialConfigPanelInterface;
import ggc.plugin.manager.DeviceImplementationStatus;
import ggc.plugin.output.OutputWriter;
import ggc.plugin.protocol.DeviceConnectionProtocol;

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
 *  Filename: DeviceInterfaceV2
 *
 *  Description: This is interface class used for Devices (V2). Device Definitions are now stored as Enum
 *       entries (ex.: PumpDeviceDefintions, which implements DeviceDefintion), which means that not every
 *       instance of device, needs its own implemented class (in reality we have one implementation for each
 *       plugin (ex.: PumpDeviceInstanceWithHandler, implements DeviceInstanceWithHandler), which then
 *       dinamicaly loads Handler (thorugh DeviceHandlerManager). In special Handler class we now have only
 *       Handler methods (download Data, download Config, download Files, etc), so we have just limited number
 *       of classes for more devices (depending of implementation). If we look at Ascensia, we have two ways
 *       to get data, one is through Serial/Usb Bridge and the second one is through USB Hid interface.
 *       So we have 2 handlers, which can cover about 10 different devices.
 *
 *
 *  Example:  for Ascensia meters
 *         MeterDeviceInstanceWithHandler (implements MeterDeviceInterfaceV2, which implements DeviceInterfaceV2)
 *              |--> AscensiaSerialHandler (implements SerialProtocolHandler)
 *                       |---> used by: MeterDeviceDefintion.AscensiaContour, ...
 *              |--> AscensiaUSBHidHandler (implements USBHidProtocolHandler)
 *                       |---> used by: MeterDeviceDefintion.AscensiaNextUsb, ...
 *
 *  NOTE: This is just example how implementation for V2 will be, but most of devices are still in V1 (one class
 *      for one device), new devices are already implemented in V2 style, but old ones will just slowly be migrated
 *      to new mode.
 *
 *  Author: Andy {andy@atech-software.com}
 */

public interface DeviceInterfaceV2 extends SelectableInterfaceV2
{

    // ************************************************
    // *** Device Identification Methods ***
    // ************************************************

    /**
     * getName - Get Name of device.
     * Should be implemented by device class.
     *
     * @return name of device
     */
    String getName();


    /**
     * getIcon - Get Icon of device
     * Should be implemented by device class.
     *
     * @return icon Name
     */
    String getIconName();


    /**
     * getDeviceId - Get Device Id, this are plugin specific and global (for example only one device
     * of type meter, can have same id.
     * Should be implemented by protocol class.
     *
     * @return id of device
     */
    int getDeviceId();


    /**
     * getInstructions - get instructions for device
     * Should be implemented by meter class.
     *
     * @return instructions for reading data
     */
    String getInstructions();


    /**
     * getImplementationStatus - Get Implementation Status
     *
     * @return implementation status as DeviceImplementationStatus instance
     * @see ggc.plugin.manager.DeviceImplementationStatus
     */
    DeviceImplementationStatus getImplementationStatus();


    // ************************************************
    // *** Device Implemented methods ***
    // ************************************************

    /**
     * This method calls readDeviceData from Handler.
     *
     * @param connectionParameters
     * @param outputWriter
     *
     * @throws PlugInBaseException
     */
    void readDeviceData(Object connectionParameters, //
            OutputWriter outputWriter) throws PlugInBaseException;


    /**
     * This is method for reading configuration of device.
     *
     * @throws PlugInBaseException
     */
    void readConfiguration(Object connectionParameters, //
            OutputWriter outputWriter) throws PlugInBaseException;


    /**
     * getDeviceSpecialComment - special comment for device (this is needed in case that we need to display
     *    special comment about device (for example pix device, doesn't display anything till the end, which
     *    would be nice if user knew. 
     * @return 
     */
    String getDeviceSpecialComment();


    DeviceProgressStatus getDeviceProgressStatus();


    // ************************************************
    // *** Connection type/parameters ***
    // ************************************************

    /**
     * getConnectionProtocol - returns connection protocol enum
     * 
     * @return id of connection protocol
     */
    public DeviceConnectionProtocol getConnectionProtocol();


    /**
     * validateConnectionParameters
     *
     * Validates connection parameters. Since downloads are done directly with connection parameters, this
     * should be called before calling any download functions. In most cases non-null validation will sufice
     * with exception when connection parameters are defined as complex value (more than one value packed
     * together). Validation will be used together with getDevicePortParameterType(). We need to override this
     * only in case that we have PackedParameters.
     * 
     * @param param 
     * @return
     */
    boolean validateConnectionParameters(String param);


    /**
     * Get Device Port Parameter Type. This defines how Connection Parameter is defined.
     *
     * @return
     */
    DevicePortParameterType getDevicePortParameterType();


    // ************************************************
    // *** Company Specific Settings ***
    // ************************************************

    DeviceCompanyDefinition getCompany();


    // ************************************************
    // *** Download Support ***
    // ************************************************

    /**
     * Get Download Support Type
     * 
     * @return
     */
    DownloadSupportType getDownloadSupportType();


    /**
     * Get Device Source Name (this is device source in form of Company/Device, which is then
     * written to database).
     * 
     * @return
     */
    String getDeviceSourceName();


    /**
     * Get File Download Types as FileReaderContext. 
     * 
     * @return
     */
    List<GGCPlugInFileReaderContext> getFileDownloadContexts(DownloadSupportType downloadSupportType);


    // ************************************************
    // *** Special Config ***
    // ************************************************

    /**
     * Has Special Config
     * 
     * @return
     */
    boolean hasSpecialConfig();


    /**
     * Get Special Config Panel
     * 
     * @return
     */
    DeviceSpecialConfigPanelInterface getSpecialConfigPanel();


    /**
     * Initialize Special Config
     */
    void initSpecialConfig();

    /**
     * Has Default Parameter (if device has default parameter)
     * 
     * @return
     */
    // boolean hasDefaultParameter();


    // ************************************************
    // *** Pre-init ***
    // ************************************************

    /**
     * Has Pre Init - Some devices when started are in unusal state, this methods
     *    help us to put them in state we need. Example is AccuChek SmartPix, which is
     *    in autodetect state when we attach it, now if we put a meter/pump before it,
     *    it starts to automatically read, but GGC needs some time to do preliminary 
     *    stuff, so we need to have SmartPix in NO AutoScan mode). 
     * @return
     */
    boolean hasPreInit();


    /**
     * Pre Init Device - Does preinit
     */
    void preInitDevice();

}
