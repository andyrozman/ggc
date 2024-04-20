package ggc.meter.device;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.hid4java.*;
import org.hid4java.event.HidServicesEvent;
import org.junit.Ignore;
import org.junit.Test;

/**
 * Created by andy on 09.04.2024.
 */
@Ignore
public class TestHidApi {

    Gson gson = new GsonBuilder().setPrettyPrinting().create();

    // 10C4:EA80 - CP2110 HID USB-to-UART Bridge  - Silicon Laboratories - 00CD6293   GlucoRx HCT

    @Test
    public void showHidDevices() {
        // Configure to use custom specification
        HidServicesSpecification hidServicesSpecification = new HidServicesSpecification();

// Use the v0.7.0 manual start feature to get immediate attach events
        hidServicesSpecification.setAutoStart(false);

// Get HID services using custom specification
        HidServices hidServices = HidManager.getHidServices(hidServicesSpecification);
        hidServices.addHidServicesListener(new HidServicesListener() {
            @Override
            public void hidDeviceAttached(HidServicesEvent hidServicesEvent) {
                System.out.println("hidDeviceAttached: " + gson.toJson(hidServicesEvent));
            }

            @Override
            public void hidDeviceDetached(HidServicesEvent hidServicesEvent) {
                System.out.println("hidDeviceDetached: " + gson.toJson(hidServicesEvent));

            }

            @Override
            public void hidFailure(HidServicesEvent hidServicesEvent) {
                System.out.println("hidFailure: " + gson.toJson(hidServicesEvent));
            }
        });

// Manually start the services to get attachment event
        hidServices.start();

// Provide a list of attached devices
        for (HidDevice hidDevice : hidServices.getAttachedHidDevices()) {
            System.out.println(hidDevice);
        }
    }


    @Test
    public void testHid() throws HidException {
        HidServicesSpecification hidServicesSpecification = new HidServicesSpecification();
        hidServicesSpecification.setAutoShutdown(true);
        hidServicesSpecification.setScanInterval(500);
        hidServicesSpecification.setPauseInterval(5000);
        hidServicesSpecification.setScanMode(ScanMode.SCAN_AT_FIXED_INTERVAL_WITH_PAUSE_AFTER_WRITE);
        HidServices hidServices = HidManager.getHidServices(hidServicesSpecification);
        hidServices.start();
        System.out.printf(" %-3s  %-3s - %-30s - %-20s - %-15s - %-10s\n", "VID", "PID", "Description", "Manf", "Serial Num", "Can open");
        for (HidDevice hidDevice : hidServices.getAttachedHidDevices()) {
            String manf = hidDevice.getManufacturer().trim();
            String prod = hidDevice.getProduct();
            int vendor = hidDevice.getVendorId();
            int product = hidDevice.getProductId();
            String serialNum = hidDevice.getSerialNumber();
            serialNum = serialNum != null ? serialNum : "";
            System.out.printf("%04X:%04X - %-30s - %-20s - %-15s", vendor, product, prod, manf, serialNum);
            if (hidDevice.isOpen()) {
                System.out.print(" - open");
            } else if (hidDevice.open() && hidDevice.isOpen()) {
                hidDevice.setNonBlocking(true);
                System.out.print(" - yes");
                hidDevice.close();
            } else {
                System.out.print(" - no");
            }
            System.out.println();
        }
        hidServices.shutdown();
    }

}
