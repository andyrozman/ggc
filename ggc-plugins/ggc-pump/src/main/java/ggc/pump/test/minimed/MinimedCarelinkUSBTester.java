package main.java.ggc.pump.test.minimed;

/**
 * Created by andy on 08.06.15.
 */
public class MinimedCarelinkUSBTester // implements HidServicesListener
{

    public MinimedCarelinkUSBTester()
    {
        // connectToHid();
    }

    // public void connectToHid()
    // {
    // try
    // {
    // System.out.println("Loading hidapi...");
    //
    // // Get HID services
    // HidServices hidServices = HidManager.getHidServices();
    // hidServices.addHidServicesListener(this);
    //
    // hidServices.scan();
    //
    // System.out.println("Enumerating attached devices...");
    //
    // // Provide a list of attached devices
    // for (HidDevice hidDevice : hidServices.getAttachedHidDevices())
    // {
    // System.out.println(hidDevice);
    // }
    //
    // // // Open the Trezor device by Vendor ID and Product ID with
    // // wildcard
    // // // serial number
    // // HidDevice trezor = hidServices.getHidDevice(0x534c, 0x01, null);
    // // if (trezor != null)
    // // {
    // // // Device is already attached so send message
    // // sendInitialise(trezor);
    // // }
    // // else
    // // {
    // // System.out.println("Waiting for Trezor attach...");
    // // }
    // // // Stop the main thread to demonstrate attach and detach events
    // // sleepUninterruptibly(5, TimeUnit.SECONDS);
    // //
    // // if (trezor != null && trezor.isOpen())
    // // {
    // // trezor.close();
    // // }
    // //
    // // System.exit(0);
    // }
    // catch (HidException ex)
    // {
    // System.out.println("Exception: " + ex);
    // ex.printStackTrace();
    //
    // // e.printStackTrace();
    // }
    //
    // }
    //
    //
    // public static void main(String[] args)
    // {
    // new MinimedCarelinkUSBTester();
    // }
    //
    //
    // public void hidDeviceAttached(HidServicesEvent event)
    // {
    // System.out.println("Device attached: " + event);
    //
    // if (event.getHidDevice().getVendorId() == 0x534c &&
    // event.getHidDevice().getProductId() == 0x01)
    // {
    //
    // // Open the Trezor device by Vendor ID and Product ID with wildcard
    // // serial number
    // // HidDevice trezor = hidServices.getHidDevice(0x534c, 0x01, null);
    // // if (trezor != null)
    // // {
    // // sendInitialise(trezor);
    // // }
    //
    // }
    // }
    //
    //
    // public void hidDeviceDetached(HidServicesEvent event)
    // {
    // System.err.println("Device detached: " + event);
    // }
    //
    //
    // public void hidFailure(HidServicesEvent event)
    // {
    // System.err.println("HID failure: " + event);
    // }
}
