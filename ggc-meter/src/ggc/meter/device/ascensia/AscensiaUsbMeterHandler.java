package ggc.meter.device.ascensia;

import java.util.ArrayList;
import java.util.List;

import ggc.meter.device.ascensia.impl.AscensiaContourUsbReader;
import ggc.plugin.comm.cfg.USBDevice;
import ggc.plugin.data.GGCPlugInFileReaderContext;
import ggc.plugin.data.enums.DeviceHandlerType;
import ggc.plugin.device.DownloadSupportType;
import ggc.plugin.device.PlugInBaseException;
import ggc.plugin.device.v2.DeviceDefinition;
import ggc.plugin.device.v2.DeviceHandler;
import ggc.plugin.output.OutputWriter;

public class AscensiaUsbMeterHandler implements DeviceHandler
{

    public DeviceHandlerType getDeviceHandlerKey()
    {
        return DeviceHandlerType.AscensiaUsbHandler;
    }


    public void readDeviceData(DeviceDefinition definition, Object connectionParameters, OutputWriter outputWriter)
            throws PlugInBaseException
    {
        AscensiaContourUsbReader reader = new AscensiaContourUsbReader(this, (String) connectionParameters,
                outputWriter);
        reader.readFromDevice();
        reader.closeDevice();
    }


    public void readConfiguration(DeviceDefinition definition, Object connectionParameters, OutputWriter outputWriter)
            throws PlugInBaseException
    {
    }


    public List<USBDevice> getAllowedDevicesList()
    {
        List<USBDevice> usbDeviceList = new ArrayList<USBDevice>();
        usbDeviceList.add(new USBDevice("Contour USB", 0x1a79, 0x6002));
        usbDeviceList.add(new USBDevice("Contour USB", 0x1a79, 0x7390)); // ?
        usbDeviceList.add(new USBDevice("Contour Next", 0x1a79, 0x7350));
        usbDeviceList.add(new USBDevice("Contour Next USB", 0x1a79, 0x7410));
        usbDeviceList.add(new USBDevice("Contour Next Link", 0x1a79, 0x6300));
        usbDeviceList.add(new USBDevice("Contour Next Link", 0x1a79, 0x6200)); // ?

        return usbDeviceList;
    }


    public List<GGCPlugInFileReaderContext> getFileDownloadContexts(DownloadSupportType downloadSupportType)
    {
        return null;
    }

}
