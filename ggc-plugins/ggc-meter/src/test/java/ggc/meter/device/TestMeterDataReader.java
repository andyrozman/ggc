package ggc.meter.device;

import ggc.core.util.DataAccess;
import ggc.meter.defs.MeterPluginDefinition;
import ggc.meter.device.menarini.MeanriniMeterDataReader;
import ggc.meter.device.onetouch.OneTouchUsbMeterHandler;
import ggc.meter.util.DataAccessMeter;
import ggc.plugin.device.v2.DeviceDefinition;
import ggc.plugin.output.ConsoleOutputWriter;
import ggc.plugin.util.DataAccessPlugInBase;

import javax.swing.*;

/**
 * Created by andy on 05.04.2024.
 */
public class TestMeterDataReader {



    public static void main(String[] args) {

        JFrame f = new JFrame();

        //DataAccess.dontLoadIcons = true;

        DataAccess da = DataAccess.createInstance(f);

        DataAccessPlugInBase dam = DataAccessMeter.createInstance(new MeterPluginDefinition(da.getLanguageManager()));
        dam.setMainParent(f);


        DeviceDefinition deviceDefinition = null;
        String connectionParameters = null;

        //ConsoleOutputWriter consoleOutputWriter = new ConsoleOutputWriter();

        try {

            OneTouchUsbMeterHandler handler = new OneTouchUsbMeterHandler();
            handler.readDeviceData(deviceDefinition,
                    "2766:0004",
                    null);

        } catch (Exception ex) {
            System.out.println("Exception. Ex: " + ex.getMessage());
        }

        // Bus 001 Device 025: ID 2766:0004 LifeScan Verio Flex

//        MeanriniMeterDataReader dataReader = new MeanriniMeterDataReader(deviceDefinition, (String) connectionParameters,
//                consoleOutputWriter);
//        dataReader.readData();



    }


}
