package ggc.plugin.comm;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.apache.commons.lang.StringUtils;
import org.hid4java.HidDevice;
import org.hid4java.HidManager;
import org.hid4java.HidServices;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ggc.plugin.comm.cfg.USBDevice;
import ggc.plugin.data.enums.PlugInExceptionType;
import ggc.plugin.device.PlugInBaseException;

/**
 * Created by andy on 08.06.15.
 */
public class Hid4JavaCommunicationHandler extends SerialCommunicationAbstract
{

    private static final Logger LOG = LoggerFactory.getLogger(Hid4JavaCommunicationHandler.class);

    List<USBDevice> allowedDevices;
    HidDevice hidDevice = null;
    USBDevice selectedDevice;
    int timeoutMs = 100;
    private USBDevice targetDevice;


    public boolean connectAndInitDevice() throws PlugInBaseException
    {
        LOG.debug("connectAndInitDevice - USB/Hid");

        HidServices hidServices;

        try
        {
            hidServices = HidManager.getHidServices();
        }
        catch (Exception ex)
        {
            LOG.error("Error getting HidServices. Ex.: " + ex, ex);
            return false;
        }

        hidDevice = findHidDevice(hidServices);

        if (hidDevice == null)
        {
            throw new PlugInBaseException(PlugInExceptionType.DeviceNotFound);
        }

        LOG.debug("Found correct device: " + hidDevice);

        return hidDevice != null;
    }


    private HidDevice findHidDevice(HidServices hidServices) throws PlugInBaseException
    {
        // // 0.3.1
        //
        // HidDeviceInfo selHidDeviceInfo = null;
        //
        // // Provide a list of attached devices
        // for (HidDeviceInfo hidDeviceInfo :
        // hidServices.getAttachedHidDevices())
        // {
        // if (isCorrectDevice(hidDeviceInfo.getVendorId(),
        // hidDeviceInfo.getProductId()))
        // {
        // selHidDeviceInfo = hidDeviceInfo;
        // }
        // }
        //
        // if (selHidDeviceInfo == null)
        // {
        // throw new PlugInBaseException(PlugInExceptionType.DeviceNotFound);
        // }
        //
        // hidDevice = hidServices.getHidDevice(selectedDevice.getVendorId(),
        // selectedDevice.getProductId(), null);
        //
        // if (hidDevice == null)
        // {
        // LOG.error("Device could not be opened.");
        // throw new
        // PlugInBaseException(PlugInExceptionType.DeviceCouldNotBeContacted);
        // }

        // 0.4.x
        HidDevice selHidDeviceInfo = null;

        // Provide a list of attached devices
        for (HidDevice hidDeviceInfo : hidServices.getAttachedHidDevices())
        {
            System.out.println(hidDeviceInfo);
            if (isInAllowedDevicesList(hidDeviceInfo.getVendorId(), hidDeviceInfo.getProductId()))
            {
                selHidDeviceInfo = hidDeviceInfo;
            }
        }

        if (selHidDeviceInfo == null)
        {
            throw new PlugInBaseException(PlugInExceptionType.DeviceNotFound);
        }

        hidDevice = selHidDeviceInfo;

        if (!hidDevice.isOpen())
        {
            LOG.debug("Device was not opened, so we opened it.");
            hidDevice.open();
        }
        else
        {
            LOG.debug("Device was open.");
        }

        return hidDevice;

    }


    private boolean isCorrectDevice(int vendorId, int productId)
    {
        if (targetDevice != null)
        {
            if ((targetDevice.getVendorId() == vendorId) && (targetDevice.getProductId() == productId))
            {
                selectedDevice = targetDevice;
                return true;
            }
            else
                return false;
        }
        else
            return isInAllowedDevicesList(vendorId, productId);
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


    public String getLastErrorMessage()
    {
        return this.hidDevice.getLastErrorMessage();
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


    public static Set<USBDevice> getAllDevices() throws Exception
    {
        HidServices hidServices;

        try
        {
            hidServices = HidManager.getHidServices();
        }
        catch (Exception ex)
        {
            LOG.error("Error getting HidServices. Ex.: " + ex, ex);
            throw ex;
        }

        Set<USBDevice> list = new HashSet<USBDevice>();

        // FIXME
        // Provide a list of attached devices
        // 0.3.1 remove
        // for (HidDeviceInfo hidDeviceInfo :
        // hidServices.getAttachedHidDevices())
        // {
        // USBDevice device = new
        // USBDevice(hidDeviceInfo.getProductString().toString(),
        // hidDeviceInfo.getVendorId(),
        // hidDeviceInfo.getProductId());
        //
        // list.add(device);
        // }

        // 0.4.x
        for (HidDevice hidDeviceInfo : hidServices.getAttachedHidDevices())
        {
            USBDevice device = new USBDevice(hidDeviceInfo.getProduct(), hidDeviceInfo.getVendorId(),
                    hidDeviceInfo.getProductId());

            list.add(device);
        }

        return list;
    }


    public void setTargetDevice(String targetDevice)
    {
        if (StringUtils.isNotBlank(targetDevice))
        {
            String[] address = targetDevice.split(":");

            USBDevice device = new USBDevice("", Integer.parseInt(address[0], 16), Integer.parseInt(address[1], 16));
            this.targetDevice = device;
            LOG.debug("Target device is: " + targetDevice);
        }
        else
        {
            LOG.debug("Target device address is invalid: " + targetDevice);
        }

    }
}
