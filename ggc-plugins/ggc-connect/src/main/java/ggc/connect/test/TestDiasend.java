package ggc.connect.test;

import java.util.HashSet;
import java.util.Set;

import ggc.connect.data.ConnectHandlerParameters;
import ggc.connect.enums.ConnectOperationType;
import ggc.connect.software.local.diasend.DiasendReader;
import ggc.connect.software.local.diasend.util.DiaSendUtil;
import ggc.plugin.data.enums.DeviceHandlerType;
import ggc.plugin.output.ConsoleOutputWriter;

public class TestDiasend extends AbstractConnectTest
{

    public void testDiasend()
    {
        this.initDb = false;

        prepareContext();

        try
        {

            DiaSendUtil.setOutputWriter(new ConsoleOutputWriter());

            ConnectHandlerParameters parameters = new ConnectHandlerParameters();
            parameters.setFileName(
                "/home/andy/Dropbox/workspaces/ggc/ggc-desktop-app/ggc-desktop/src/andy.rozman@gmail.com.xls");

            parameters.setActionType(ConnectOperationType.ViewConfiguration);
            parameters.setDeviceHandlerType(DeviceHandlerType.DiaSendHandler);

            Set<String> selectedItems = new HashSet<String>();
            selectedItems.add("Insulin pump settings");

            parameters.setSelectedItems(selectedItems);

            DiasendReader dr = new DiasendReader();
            dr.readData(parameters, new ConsoleOutputWriter());

            if (true)
                return;

        }
        catch (Exception ex)
        {

            System.out.println("Error running testDiasend: " + ex.getMessage());
            ex.printStackTrace();
        }

    }


    public static void main(String[] args)
    {
        TestDiasend ta = new TestDiasend();
        ta.testDiasend();
    }

}
