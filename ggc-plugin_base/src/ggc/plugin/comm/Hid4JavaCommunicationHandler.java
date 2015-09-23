package ggc.plugin.comm;

import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hid4java.HidDevice;
import org.hid4java.HidDeviceInfo;
import org.hid4java.HidManager;
import org.hid4java.HidServices;

import ggc.plugin.comm.cfg.USBDevice;
import ggc.plugin.data.enums.PlugInExceptionType;
import ggc.plugin.device.PlugInBaseException;

/**
 * Created by andy on 08.06.15.
 */
public class Hid4JavaCommunicationHandler implements CommunicationInterface
{

    Log LOG = LogFactory.getLog(Hid4JavaCommunicationHandler.class);

    List<USBDevice> allowedDevices;
    private HidServices hidServices;
    HidDevice hidDevice = null;
    USBDevice selectedDevice;
    int timeoutMs = 100;


    public boolean connectAndInitDevice() throws PlugInBaseException
    {
        LOG.debug("connectAndInitDevice - USB/Hid");

        try
        {
            hidServices = HidManager.getHidServices();
        }
        catch (Exception ex)
        {
            LOG.error("Error getting HidServices. Ex.: " + ex, ex);
            return false;
        }

        // 0.3.1
        HidDeviceInfo selHidDeviceInfo = null;

        // Provide a list of attached devices
        for (HidDeviceInfo hidDeviceInfo : hidServices.getAttachedHidDevices())
        {
            if (isInAllowedDevicesList(hidDeviceInfo.getVendorId(), hidDeviceInfo.getProductId()))
            {
                selHidDeviceInfo = hidDeviceInfo;
            }
        }

        // 0.4.0
        // HidDevice selHidDeviceInfo = null;
        //
        // // Provide a list of attached devices
        // for (HidDevice hidDeviceInfo : hidServices.getAttachedHidDevices())
        // {
        // System.out.println(hidDeviceInfo);
        // if (isInAllowedDevicesList(hidDeviceInfo.getVendorId(),
        // hidDeviceInfo.getProductId()))
        // {
        // selHidDeviceInfo = hidDeviceInfo;
        // }
        // }

        if (selHidDeviceInfo == null)
        {
            throw new PlugInBaseException(PlugInExceptionType.DeviceNotFound);
        }

        // 0.3.1
        hidDevice = hidServices.getHidDevice(selectedDevice.getVendorId(), selectedDevice.getProductId(), null);

        // 0.4.0
        // hidDevice = selHidDeviceInfo;

        LOG.debug("Found correct device: " + hidDevice);

        return hidDevice != null;
    }


    private boolean isInAllowedDevicesList(int vendorId, int productId)
    {
        if (this.allowedDevices == null)
            return false;

        for (USBDevice device : allowedDevices)
        {
            if ((device.getVendorId() == vendorId) && (device.getProductId() == productId))
            {
                selectedDevice = device;
                return true;
            }
        }

        return false;
    }


    public void setAllowedDevices(List<USBDevice> allowedDevices)
    {
        this.allowedDevices = allowedDevices;
    }


    public void disconnectDevice()
    {
        this.hidDevice.close();
        this.hidDevice = null;
    }


    public boolean isDataAvailable()
    {
        return false;
    }


    public int available() throws PlugInBaseException
    {
        return 0;
    }


    public int read() throws PlugInBaseException
    {
        return 0;
    }


    public int read(byte[] buffer) throws PlugInBaseException
    {
        return this.hidDevice.read(buffer, timeoutMs);
    }


    public int read(byte[] b, int off, int len) throws PlugInBaseException
    {
        return 0;
    }


    public void write(int toWrite) throws PlugInBaseException
    {
    }


    public void write(byte[] buffer) throws PlugInBaseException
    {
    }


    public int writeWithReturn(byte[] buffer) throws PlugInBaseException
    {
        return this.hidDevice.write(buffer, buffer.length, this.selectedDevice.getReportId());
    }


    public void write(byte[] b, int off, int len) throws PlugInBaseException
    {

    }


    public byte[] readLineBytes() throws PlugInBaseException
    {
        return new byte[0];
    }


    public String readLine() throws PlugInBaseException
    {
        return null;
    }


    public int readByteTimed() throws PlugInBaseException
    {
        return 0;
    }


    public void setDelayForTimedReading(int ms)
    {
        this.timeoutMs = ms;
    }


    public void write(int[] cmd) throws PlugInBaseException
    {

    }


    public int getReceiveTimeout()
    {
        return 0;
    }


    public void setReceiveTimeout(int timeout) throws PlugInBaseException
    {
    }
}
