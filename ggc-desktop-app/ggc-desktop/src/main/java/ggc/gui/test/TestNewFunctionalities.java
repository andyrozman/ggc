package ggc.gui.test;

import com.google.gson.Gson;
import ggc.meter.defs.device.MeterDeviceDefinition;
import ggc.meter.device.onetouch.OneTouchUsbMeterHandler;
import ggc.plugin.output.ConsoleOutputWriter;
import ggc.plugin.protocol.bluetooth.BluetoothListener;

import javax.bluetooth.DiscoveryAgent;
import javax.bluetooth.LocalDevice;

/**
 * Created by andy on 06.04.2024.
 */
public class TestNewFunctionalities {

    private static Object lock=new Object();

    String activeTestCode = "";

    public void executeTestCode() {
        this.activeTestCode = "";

        //testOneTouchVerio();
    }


    private void testCode() {




        // new DailyRowMealsDialog(null, new JDialog());

        // spread graph
        // new GraphViewer(new GraphViewSpread(), dataAccess);

        /*
         * // graph course
         * new GraphViewer(new GraphViewCourse(), dataAccess);
         */

        // ratio calculator
        // @SuppressWarnings("unused")
        // RatioCalculatorDialog rcd = new
        // RatioCalculatorDialog(MainFrame.this);

        // BasalRateEstimator bre = new BasalRateEstimator();

        /*
         * // daily view
         * GregorianCalendar gc = new GregorianCalendar();
         * gc.set(GregorianCalendar.DAY_OF_MONTH, 18);
         * gc.set(GregorianCalendar.MONTH, 10);
         * gc.set(GregorianCalendar.YEAR, 2008);
         * new GraphViewer(new GraphViewDaily(gc), dataAccess);
         */

        // new HbA1cDialog(dataAccess);
        // ImportDacioDb idb = new
        // ImportDacioDb("../data/temp/zivila.csv", true); //args[
        // idb.convertFoods();
        /*
         * DayValuesData dvd =
         * dataAccess.getDb().getDayValuesData(20081001,
         * 20091007); // .getMonthlyValues(yr,
         * // mnth);
         * PrintFoodMenuExt2 psm = new PrintFoodMenuExt2(dvd);
         * PrintingDialog.displayPDFExternal(psm.getName());
         */
        // BolusHelper bh = new BolusHelper(MainFrame.this);
        // featureNotImplemented(command, "0.6");

    }

    private void testBlueCoveReadingOfOneTouchVerioasBT() {
        BluetoothListener bluetoothListener = new BluetoothListener(lock);
        Gson gson = bluetoothListener.getGson();

        try{
            // 1
            LocalDevice localDevice = LocalDevice.getLocalDevice();
            //log.info("LocalDevice: {}", gson.toJson(localDevice));


            // 2
            DiscoveryAgent agent = localDevice.getDiscoveryAgent();
            //log.info("DiscoveryAgent: {}", gson.toJson(agent));
            //agent.

            // 3
            agent.startInquiry(DiscoveryAgent.GIAC, bluetoothListener);

            try {
                synchronized(lock){
                    lock.wait();
                }
            }
            catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println("Device Inquiry Completed. ");

        }
        catch (Exception e) {
            e.printStackTrace();
        }

    }



    private void testOneTouchVerio() {

        // problem connecting for now

        ConsoleOutputWriter consoleOutputWriter = new ConsoleOutputWriter();

        try {

            OneTouchUsbMeterHandler handler = new OneTouchUsbMeterHandler();
            handler.readDeviceData(MeterDeviceDefinition.ArkrayGlucoCardSM,
                    "2766:0004",
                    consoleOutputWriter);

        } catch (Exception ex) {
            System.out.println("Exception. Ex: " + ex.getMessage());
        }

    }

    private void testVeryOldCode() {

    }


}
