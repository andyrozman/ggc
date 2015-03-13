package ggc.plugin.protocol;

import ggc.plugin.device.DeviceAbstract;
import ggc.plugin.device.PlugInBaseException;
import ggc.plugin.manager.company.AbstractDeviceCompany;
import ggc.plugin.output.OutputWriter;
import ggc.plugin.util.DataAccessPlugInBase;

import java.io.IOException;
import java.util.Hashtable;
import java.util.Stack;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
//import org.beblue.jna.usb.usb_dev_handle;
//
//import com.atech.library.libusb.LibUsb4j;
//import com.atech.library.libusb.LibUsb4jUtil;
//import com.atech.library.libusb.data.UsbDevice;
//import com.atech.library.libusb.data.UsbMachine;

public abstract class USBProtocol extends DeviceAbstract
{
    public USBProtocol(DataAccessPlugInBase da)
    {
        super(da);
    }

    public USBProtocol(AbstractDeviceCompany adc, DataAccessPlugInBase da)
    {
        super(adc, da);
    }

    public USBProtocol(DataAccessPlugInBase da, OutputWriter output_writer_)
    {
        super(da, output_writer_);
    }

    public USBProtocol(String parameters, OutputWriter writer, DataAccessPlugInBase da)
    {
        super(parameters, writer, da);
    }

//    protected boolean isPortOpen = false;
//    protected UsbDevice selected_device = null;
//
//    // LibUSB usb_context = null;
//    public static UsbMachine usb_machine = null;
//
//    protected Hashtable<String, String> supported_devices = null;
//    private static Log log = LogFactory.getLog(USBProtocol.class);
//
//    private usb_dev_handle handle = null;
//    private LibUsb4j usb = null;
//
//    protected int USB_TIMEOUT = 30000;
//
//    /**
//     * Constructor
//     *
//     * @param da
//     */
//    public USBProtocol(DataAccessPlugInBase da)
//    {
//        super(da);
//    }
//
//    /**
//     * Constructor
//     *
//     * @param comm_parameters
//     * @param writer
//     * @param da
//     */
//    public USBProtocol(String comm_parameters, OutputWriter writer, DataAccessPlugInBase da)
//    {
//        super(comm_parameters, writer, da);
//        initUsb();
//        initializeUsbSettings();
//    }
//
//    public void initUsb()
//    {
//        if (usb_machine == null)
//        {
//            usb_machine = new UsbMachine();
//        }
//
//        usb = usb_machine.getUsbLib4jInstance();
//    }
//
//    public abstract void initializeUsbSettings();
//
//    /**
//     * Open Serial Port
//     *
//     * @throws PlugInBaseException
//     * @return true if port is opened
//     */
//    public boolean open() throws PlugInBaseException
//    {
//
//        // if (this.selected_device!=null)
//        // return true;
//
//        try
//        {
//
//            initUsb();
//
//            if (!USBProtocol.usb_machine.isContextAvailable())
//            {
//                log.error("Usb Context not available.");
//                return false;
//            }
//
//            if (this.supported_devices == null || this.supported_devices.size() == 0)
//            {
//                log.error("Supported USB devices not set. Problem in code.");
//                return false;
//            }
//
//            // FIXME
//
//            // Hashtable<String,String> devs_ids = getDesiredDevicesIds();
//
//            this.selected_device = USBProtocol.usb_machine.findUsbDevice(this.supported_devices);
//
//            if (this.selected_device == null)
//            {
//                log.warn("Required device not found");
//                isPortOpen = false;
//                return false;
//            }
//
//            // log.debug("Device not found");
//            log.debug("Device found : " + this.selected_device);
//            this.setUsbDebugLevel(255);
//
//            this.selected_device.openUsbDevice();
//
//            this.handle = this.selected_device.getDeviceHandle();
//
//            isPortOpen = true;
//            // throw new PlugInBaseException(ex);
//
//        }
//        catch (Exception ex)
//        {
//            isPortOpen = false;
//        }
//
//        return isPortOpen;
//    }
//
//    public UsbDevice getUsbDevice()
//    {
//        return this.selected_device;
//    }
//
//    public LibUsb4j getUsb4jInstance()
//    {
//        return usb;
//    }
//
//    // public abstract Hashtable<String,String> getDesiredDevicesIds();
//
//    public void setUsbDebugLevel(int level)
//    {
//        this.usb.usb_set_debug(level);
//    }
//
//    int used_interface = 0;
//
//    public void claimInterface(int num)
//    {
//        log.debug("Claiming interface: " + num);
//        this.used_interface = num;
//        int res = this.usb.usb_claim_interface(handle, num);
//
//        if (res < 0)
//        {
//            log.warn("Claiming failed: Error code: " + res + " (" + this.usb.usb_strerror() + ")");
//        }
//
//    }
//
//    public void releaseInterface()
//    {
//        log.debug("Release interface: " + this.used_interface);
//        int res = this.usb.usb_release_interface(handle, this.used_interface);
//
//        if (res < 0)
//        {
//            log.warn("Release failed: Error code: " + res + " (" + this.usb.usb_strerror() + ")");
//        }
//
//    }
//
//    public void setRequiredEndpoint(int end_point_in, int end_point_out)
//    {
//        this.endpoint_in = end_point_in;
//        this.endpoint_out = end_point_out;
//        // this.selected_endpoint = end_point;
//    }
//
//    public boolean usbResetDevice()
//    {
//        // this is one option, that is not available to us, via libusb
//        // return this.usb.usb_reset(this.handle)>0;
//        return false;
//    }
//
//    /**
//     * Read
//     *
//     * @param b
//     * @throws IOException
//     * @return
//     */
//    public int read(byte[] b) throws PlugInBaseException
//    {
//        if (this.selected_device == null)
//            throw new PlugInBaseException("Device is not available.");
//        else
//        {
//            int ret = this.usb.usb_bulk_read(this.selected_device.getDeviceHandle(), USB_EP, b, b.length, USB_TIMEOUT);
//
//            if (ret <= 0)
//            {
//                log.warn("Reading failed.");
//                return -1;
//            }
//            else
//                return ret;
//
//        }
//
//    }
//
//    public int USB_EP = 1;
//
//    /**
//     * Write (byte[])
//     *
//     * @param b
//     * @throws IOException
//     */
//    public int write(byte[] b) throws IOException
//    {
//        int res = this.usb.usb_bulk_write(this.handle, USB_EP, b, b.length, this.USB_TIMEOUT);
//
//        if (res <= 0)
//        {
//            log.warn("Writing failed.");
//        }
//
//        return res;
//    }
//
//    /**
//     * Write (int[])
//     *
//     * @param b
//     * @throws IOException
//     */
//    /*
//     * public void write(int[] b) throws IOException
//     * {
//     * for(int i=0; i<b.length; i++)
//     * portOutputStream.write(b[i]);
//     * }
//     */
//
//    /**
//     * Write (int)
//     * @param i
//     * @throws IOException
//     */
//    public int write(int i) throws IOException
//    {
//        Integer ii = i;
//        // ii.byteValue();
//        byte[] b = new byte[1];
//        b[0] = ii.byteValue();
//
//        return this.write(b);
//
//        // portOutputStream.write(i);
//    }
//
//    // Stack<Byte> stack_data = new Stack<Byte>();
//    /*
//     * public String readLine() throws IOException //, SerialIOHaltedException
//     * {
//     * boolean reading_stopped = false;
//     * // if stack doesn't contain new line, we read another 255 characters
//     * if (stack_data.search(0x13) <= 0)
//     * {
//     * boolean new_line_found = false;
//     * while (!new_line_found)
//     * {
//     * byte[] b = new byte[1024];
//     * try
//     * {
//     * this.read(b);
//     * }
//     * catch(Exception ex)
//     * {
//     * log.error("Error reading data. Ex: " + ex, ex);
//     * }
//     * // add to stack
//     * for(int i=0; i<b.length; i++)
//     * {
//     * if (b[i]=='\0')
//     * {
//     * reading_stopped = true;
//     * break;
//     * }
//     * else
//     * this.stack_data.add(b[i]);
//     * }
//     * if (reading_stopped)
//     * new_line_found = true;
//     * else
//     * {
//     * if (this.stack_data.search(0x13)>0)
//     * new_line_found = true;
//     * }
//     * }
//     * }
//     * byte el = 0;
//     * StringBuffer sb = new StringBuffer();
//     * while (!this.stack_data.empty())
//     * {
//     * el = this.stack_data.pop().byteValue();
//     * if (el == 13)
//     * break;
//     * else
//     * {
//     * sb.append((char)el);
//     * }
//     * }
//     * return sb.toString();
//     * /*
//     * char c = '\uFFFF';
//     * boolean flag = false;
//     * StringBuffer stringbuffer = new StringBuffer("");
//     * int j;
//     * do
//     * {
//     * int i = c;
//     * j = (byte)this.portInputStream.read();
//     * c = (char)j;
//     * if(j != -1)
//     * stringbuffer.append(c);
//     * if(i == 13 && c == '\n')
//     * flag = true;
//     * } while(j != -1 && !flag);
//     * return stringbuffer.toString();
//     */
//    // }
//
//    /**
//     * Wait for x ms
//     * @param time
//     */
//    public void waitTime(long time)
//    {
//        try
//        {
//            Thread.sleep(time);
//
//        }
//        catch (Exception ex)
//        {}
//        // this is one option, that is not available to us, via libusb
//        // return this.usb.usb_reset(this.handle)>0;
//        // return false;
//    }
//
//    /**
//     * Write (byte[],int,int)
//     *
//     * @param b byte array
//     * @param off offset
//     * @param len length
//     * @throws IOException
//     */
//    /*
//     * public void write(byte[] b, int off, int len) throws IOException
//     * {
//     * byte[] b2 = Arrays.copyOfRange(b, off, off+len);
//     * this.write(b2);
//     * }
//     */
//
//    /**
//     * Read
//     *
//     * @param b
//     * @throws IOException
//     * @return
//     */
//    /*
//     * public int read(byte[] b) throws PlugInBaseException
//     * {
//     * if (this.selected_device==null)
//     * {
//     * throw new PlugInBaseException("Device is not available.");
//     * }
//     * else
//     * {
//     * //int ret =
//     * this.usb.usb_bulk_read(this.selected_device.getDeviceHandle(),
//     * endpoint_in, b, b.length, USB_TIMEOUT);
//     * int ret =
//     * this.usb.usb_interrupt_read(this.selected_device.getDeviceHandle(),
//     * endpoint_in, b, b.length, USB_TIMEOUT);
//     * log.debug("read(byte[]): " + b);
//     * if (ret<=0)
//     * {
//     * log.warn("Reading failed. Code: " + ret + usb.usb_strerror());
//     * return ret;
//     * }
//     * else
//     * return ret;
//     * }
//     * }
//     */
//
//    public int endpoint_in = 0;
//    public int endpoint_out = 0;
//
//    /**
//     * Write (byte[])
//     *
//     * @param b
//     * @throws IOException
//     */
//    /*
//     * public int write(byte[] b) throws PlugInBaseException
//     * {
//     * log.debug("Usb:Write: ");
//     * //int res = this.usb.usb_bulk_write(this.handle, endpoint_out, b,
//     * b.length, this.USB_TIMEOUT);
//     * int res = this.usb.usb_interrupt_write(this.handle, endpoint_out, b,
//     * b.length, this.USB_TIMEOUT);
//     * if (res<=0)
//     * {
//     * log.warn("Writing failed.");
//     * //throw new PlugInBaseException("Writing failed !");
//     * }
//     * log.debug("Usb:Write: Status:" + res + " (");
//     * return res;
//     * }
//     */
//
//    /**
//     * Write (int[])
//     *
//     * @param b
//     * @throws IOException
//     */
//    /*
//     * public void write(int[] b) throws IOException
//     * {
//     * for(int i=0; i<b.length; i++)
//     * portOutputStream.write(b[i]);
//     * }
//     */
//
//    /**
//     * Write (int)
//     * @param i
//     * @throws IOException
//     */
//    /*
//     * public void write(int i) throws PlugInBaseException
//     * {
//     * Integer ii = i;
//     * //ii.byteValue();
//     * byte[] b = new byte[1];
//     * b[0] = ii.byteValue();
//     * this.write(b);
//     * //portOutputStream.write(i);
//     * }
//     */
//
//    Stack<Byte> stack_data = new Stack<Byte>();
//
//    public String readLine() throws IOException // , SerialIOHaltedException
//    {
//
//        boolean reading_stopped = false;
//
//        // if stack doesn't contain new line, we read another 255 characters
//        if (stack_data.search(0x13) <= 0)
//        {
//            boolean new_line_found = false;
//
//            while (!new_line_found)
//            {
//
//                byte[] b = new byte[1024];
//
//                try
//                {
//                    this.read(b);
//                }
//                catch (Exception ex)
//                {
//                    log.error("Error reading data. Ex: " + ex, ex);
//                }
//
//                // add to stack
//
//                for (byte element : b)
//                {
//                    if (element == '\0')
//                    {
//                        reading_stopped = true;
//                        break;
//                    }
//                    else
//                    {
//                        this.stack_data.add(element);
//                    }
//                }
//
//                if (reading_stopped)
//                {
//                    new_line_found = true;
//                }
//                else
//                {
//                    if (this.stack_data.search(0x13) > 0)
//                    {
//                        new_line_found = true;
//                    }
//                }
//
//            }
//        }
//
//        byte el = 0;
//        StringBuffer sb = new StringBuffer();
//
//        while (!this.stack_data.empty())
//        {
//            el = this.stack_data.pop().byteValue();
//
//            if (el == 13)
//            {
//                break;
//            }
//            else
//            {
//                sb.append((char) el);
//            }
//        }
//
//        return sb.toString();
//
//        /*
//         * char c = '\uFFFF';
//         * boolean flag = false;
//         * StringBuffer stringbuffer = new StringBuffer("");
//         * int j;
//         * do
//         * {
//         * int i = c;
//         * j = (byte)this.portInputStream.read();
//         * c = (char)j;
//         * if(j != -1)
//         * stringbuffer.append(c);
//         * if(i == 13 && c == '\n')
//         * flag = true;
//         * } while(j != -1 && !flag);
//         * return stringbuffer.toString();
//         */
//    }
//
//    /**
//     * Close
//     *
//     * @throws PlugInBaseException
//     */
//    public void close() throws PlugInBaseException
//    {
//        if (!isPortOpen)
//            return;
//
//        this.usb.usb_close(handle);
//
//        isPortOpen = false;
//    }
//
//    /**
//     * Write (byte[],int,int)
//     *
//     * @param b byte array
//     * @param off offset
//     * @param len length
//     * @throws IOException
//     */
//    public void write(byte[] b, int off, int len) throws PlugInBaseException, IOException
//    {
//        throw new PlugInBaseException("Not implemented");
//        // byte[] b2 = Arrays.copyOfRange(b, off, off+len);
//        // this.write(b2);
//    }
//
//    public byte[] usbSendControlMessage()
//    {
//        /*
//         * int usb_control_msg(usb_dev_handle dev, int requesttype, int request,
//         * int value,
//         * int index, ByteBuffer bytes, int size, int timeout);
//         */
//        return null;
//    }
//
//    public String usbGetStringSimple(int index_code)
//    {
//        return LibUsb4jUtil.getInstance().getStringFromDevice(handle, index_code);
//    }
//
//    public boolean usbBulkWrite(int ep, byte[] data)
//    {
//        return this.usb.usb_bulk_write(this.handle, ep, data, data.length, this.USB_TIMEOUT) > 0;
//    }
//
//    /*
//     * public byte[] usbBulkRead(int ep, int length)
//     * {
//     * byte[] b = new byte[length];
//     * this.usb.usb_bulk_read(this.handle, ep, b, length,
//     * (int)this.USB_TIMEOUT);
//     * return b;
//     * }
//     */

}
