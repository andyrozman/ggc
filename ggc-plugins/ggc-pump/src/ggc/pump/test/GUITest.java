package ggc.pump.test;

import javax.swing.*;

import ggc.plugin.device.v2.DeviceDefinition;
import ggc.pump.gui.profile.ProfileSelectorPump;

/**
 * Created by andy on 17.10.15.
 */
public class GUITest extends AbstractPumpTest
{

    public GUITest()
    {
        prepareContext();
    }


    public void testProfileSelectorPump()
    {
        JDialog jd = new JDialog();
        jd.setBounds(200, 200, 100, 100);

        ProfileSelectorPump psp = new ProfileSelectorPump(dataAccessPump, jd);
    }


    public void testConfiguration()
    {
        DeviceDefinition di = dataAccessPump.getSelectedDeviceDefinition();

        System.out.println("Sd: " + di);

        System.out.println("Sd: " + di.name());
    }


    public static void main(String[] args)
    {
        GUITest gt = new GUITest();
        // gt.testProfileSelectorPump();
        gt.testConfiguration();
    }

}
