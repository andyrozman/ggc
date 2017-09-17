package ggc.plugin.device.v2.handler;

import java.util.List;

import ggc.plugin.comm.cfg.USBDevice;
import ggc.plugin.device.v2.DeviceHandler;

/**
 * Created by andy on 29.08.17.
 */
public interface UsbHidDeviceHandler extends DeviceHandler
{

    List<USBDevice> getAllowedDevicesList();

}
