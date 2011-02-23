package ggc.plugin.protocol;

import ggc.plugin.device.DeviceAbstract;
import ggc.plugin.device.PlugInBaseException;
import ggc.plugin.output.OutputWriter;
import ggc.plugin.util.DataAccessPlugInBase;

import java.nio.ByteBuffer;
import java.util.Hashtable;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.beblue.jna.usb.usb_dev_handle;

import com.atech.library.libusb.LibUsb4j;
import com.atech.library.libusb.LibUsb4jUtil;
import com.atech.library.libusb.data.UsbDevice;
import com.atech.library.libusb.data.UsbMachine;
import com.sun.jna.Pointer;

public abstract class USBProtocol extends DeviceAbstract
{

    protected boolean isPortOpen = false;
    protected UsbDevice selected_device = null;
    
    //LibUSB usb_context = null;
    public static UsbMachine usb_machine = null;
    
    protected  Hashtable<String, String> supported_devices = null;
    private static Log log = LogFactory.getLog(USBProtocol.class);

    private usb_dev_handle handle = null;
    private LibUsb4j usb = null;
    
    protected long USB_TIMEOUT = 30000;
    
    
    /**
     * Constructor
     * 
     * @param da 
     */
    public USBProtocol(DataAccessPlugInBase da)
    {
        super(da);
    }

    
    /**
     * Constructor
     * 
     * @param comm_parameters
     * @param writer
     * @param da 
     */
    public USBProtocol(String comm_parameters, OutputWriter writer, DataAccessPlugInBase da)
    {
        super(comm_parameters, writer, da);
        initUsb();
        initializeUsbSettings();
    }

    public void initUsb()
    {
        if (usb_machine==null)
            usb_machine = new UsbMachine();
        
        usb = usb_machine.getUsbLib4jInstance();
    }
    
  
    public abstract void initializeUsbSettings();

    
    /**
     * Open Serial Port
     * 
     * @throws PlugInBaseException 
     * @return true if port is opened 
     */
    public boolean open() throws PlugInBaseException
    {
        
        //if (this.selected_device!=null)
        //    return true;
        
        
        try 
        {
            
            initUsb();
            
            if (!this.usb_machine.isContextAvailable())
            {
                log.error("Usb Context not available.");
                return false;
            }

            
            if ((this.supported_devices==null) || (this.supported_devices.size()==0))
            {
                log.error("Supported USB devices not set. Problem in code.");
                return false;
            }
            
            
            // FIXME
            
            //Hashtable<String,String> devs_ids = getDesiredDevicesIds();            
            
            this.selected_device = this.usb_machine.findUsbDevice(this.supported_devices);
            
            if (this.selected_device == null)
            {
                log.warn("Required device not found");
                return false;
            }

            
            
            //log.debug("Device not found");
            log.debug("Device found : " + this.selected_device);
            

            this.selected_device.openUsbDevice();
            
            this.handle = this.selected_device.getDeviceHandle();    

            
            
            isPortOpen = true;
//            throw new PlugInBaseException(ex);
        
        } 
        catch (Exception ex)
        {
            isPortOpen = false;
        }
        
        return isPortOpen;
    }
    
    
    public UsbDevice getUsbDevice()
    {
        return this.selected_device;
    }
    
    
    public LibUsb4j getUsb4jInstance()
    {
        return this.usb_machine.getUsbLib4jInstance();
    }
    
    //public abstract Hashtable<String,String> getDesiredDevicesIds();
    

    
    public boolean usbResetDevice()
    {
        return this.usb.usb_reset(this.handle)>0;
    }
    

    public byte[] usbSendControlMessage()
    {
        /*
        
        int usb_control_msg(usb_dev_handle dev, int requesttype, int request, int value, 
                int index, ByteBuffer bytes, int size, int timeout);
        */
        return null;
    }
    
    
    public String usbGetStringSimple(int index_code)
    {
        return LibUsb4jUtil.getInstance().getStringFromDevice(handle, index_code);
    }
    
    

    public boolean usbBulkWrite(int ep, byte[] data)
    {
        return this.usb.usb_bulk_write(this.handle, ep, data, data.length, (int)this.USB_TIMEOUT) > 0;
    }
    

    public byte[] usbBulkRead(int ep, int length)
    {
        byte[] b = new byte[length];
        this.usb.usb_bulk_read(this.handle, ep, b, length, (int)this.USB_TIMEOUT);
        return b;
    }
    

}
