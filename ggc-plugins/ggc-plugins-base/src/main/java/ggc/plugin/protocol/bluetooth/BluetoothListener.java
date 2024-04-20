package ggc.plugin.protocol.bluetooth;

//import com.google.gson.Gson;
//import com.google.gson.GsonBuilder;
//import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

//import javax.bluetooth.DeviceClass;
//import javax.bluetooth.DiscoveryListener;
//import javax.bluetooth.RemoteDevice;
//import javax.bluetooth.ServiceRecord;

/**
 * Created by andy on 06.04.2024.
 */
@Slf4j
public class BluetoothListener //implements DiscoveryListener
{
//
//    private static Object lock=new Object();
//    @Getter
//    private Gson gson = new GsonBuilder().setPrettyPrinting().create();
//
//    public BluetoothListener(Object lockInput) {
//        lock = lockInput;
//    }
//
//    @Override
//    public void deviceDiscovered(RemoteDevice btDevice, DeviceClass arg1) {
//        log.info("DeviceDiscovered: RemoteDevice: {}, DeviceClass: {}", gson.toJson(btDevice), gson.toJson(arg1));
//
//        String name;
//        try {
//            name = btDevice.getFriendlyName(false);
//        } catch (Exception e) {
//            name = btDevice.getBluetoothAddress();
//        }
//
//        System.out.println("device found: " + name);
//
//    }
//
//    @Override
//    public void inquiryCompleted(int inquiryReturnCode) {
//        String status;
//
//        switch (inquiryReturnCode) {
//            case INQUIRY_COMPLETED:
//                status = "Completed";
//                break;
//
//            case INQUIRY_ERROR:
//                status = "Error";
//                break;
//
//            case INQUIRY_TERMINATED:
//                status = "Terminated";
//                break;
//
//            default:
//                status = "Unknown (" + inquiryReturnCode + ")";
//        }
//
//        log.info("inquiryCompleted: code={}, description={}", inquiryReturnCode, status);
//
//        synchronized(lock){
//            lock.notify();
//        }
//    }
//
//    @Override
//    public void serviceSearchCompleted(int arg0, int arg1) {
//        log.info("serviceSearchCompleted: param1={}, param2={}", arg0, arg1);
//    }
//
//    @Override
//    public void servicesDiscovered(int arg0, ServiceRecord[] arg1) {
//        log.info("serviceSearchCompleted: param1={}, serviceRecords={}", arg0, gson.toJson(arg1));
//    }
//


}
