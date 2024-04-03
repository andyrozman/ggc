package ggc.plugin.comm.ports.discovery;

import java.util.*;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ggc.plugin.comm.Hid4JavaCommunicationHandler;
import ggc.plugin.comm.cfg.USBDevice;
import ggc.plugin.comm.ports.DevicePortDto;
import ggc.plugin.data.enums.PlugInExceptionType;
import ggc.plugin.device.PlugInBaseException;
import ggc.plugin.protocol.DeviceConnectionProtocol;

/**
 * Created by andy on 08.10.15.
 */
public class UsbHidDiscoveryAgent extends PortDiscoveryAgentAbstract
{

    private static final Logger LOG = LoggerFactory.getLogger(UsbHidDiscoveryAgent.class);


    public UsbHidDiscoveryAgent()
    {
        super(DeviceConnectionProtocol.USB_Hid);
    }


    public List<DevicePortDto> getAllPossiblePorts() throws PlugInBaseException
    {
        try
        {
            Set<USBDevice> availableDevices = Hid4JavaCommunicationHandler.getAllDevices();

            List<DevicePortDto> availablePorts = new ArrayList<DevicePortDto>();
            Map<String, String> devicesMap = new HashMap<String, String>();

            for (USBDevice device : availableDevices)
            {

                String usbId = getUsbAddressHexValue(device.getVendorId()) + ":"
                        + getUsbAddressHexValue(device.getProductId());

                if (!devicesMap.containsKey(usbId))
                {
                    availablePorts.add(new DevicePortDto(usbId + " - " + device.getDescription(), usbId));
                    devicesMap.put(usbId, usbId);
                }
            }

            logFoundPorts(LOG, availablePorts);

            return availablePorts;
        }
        catch (Exception ex)
        {
            throw new PlugInBaseException(PlugInExceptionType.ErrorCommunicationWithProtocolHandler,
                    new Object[] { "Hid4JavaCommunicationHandler", ex.getMessage() }, ex);
        }
    }


    private String getUsbAddressHexValue(int n)
    {
        String s = String.format("%4s", Integer.toHexString(n)).replace(' ', '0');

        if (s.length() > 4)
        {
            return s.substring(s.length() - 4);
        }
        else
            return s;
    }


    public String getSelectProtocolString()
    {
        return "SELECT_USB_DEVICE";
    }


    public String getPortDeviceName()
    {
        return "USB_DEVICE";
    }

}
