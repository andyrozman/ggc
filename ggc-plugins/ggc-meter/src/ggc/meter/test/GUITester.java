/**
 * 
 */
package ggc.meter.test;

import ggc.meter.util.DataAccessMeter;
import ggc.plugin.gui.DeviceDisplayDataDialog;

/**
 * @author aleksanderr
 *
 */
public class GUITester
{

    /**
     * @param args
     */
    public static void main(String[] args)
    {
        DataAccessMeter da = DataAccessMeter.getInstance();
        da.initSpecial();
        da.createConfigurationContext();
        da.createDeviceConfiguration();
        System.out.println("ddh: " + da.getDeviceDataHandler());
        System.out.println("ddh: " + da.getDeviceDataHandler().getConfiguredDevice());
        new DeviceDisplayDataDialog(da, da.getDeviceDataHandler(), true);
    }

}
