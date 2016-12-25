package main.java.ggc.pump.test;

import java.io.File;

import ggc.plugin.device.PlugInBaseException;
import ggc.plugin.output.ConsoleOutputWriter;
import main.java.ggc.pump.device.insulet.InsuletReader;

/**
 * Created by andy on 25.05.15.
 */
public class TestOmnipod extends AbstractPumpTest
{

    public void testOmnipod()
    {
        prepareContext();

        File f = new File(".");
        System.out.println(f.getAbsolutePath());

        ConsoleOutputWriter writer = new ConsoleOutputWriter();

        String path = "../../test_data/";

        try
        {
            InsuletReader reader = new InsuletReader(path + "130113444-2015-05-22-18-59-58.ibf", writer);

            // InsuletReader reader = new InsuletReader(path +
            // "130113444-2015-05-22-18-59-58.ibf", writer);

            // '20006422-2009-09-17-23-09-07.ibf'

            // InsuletReader reader = new InsuletReader(path +
            // "20006422-2009-09-17-23-09-07.ibf", writer);

            reader.readData();

        }
        catch (PlugInBaseException e)
        {
            e.printStackTrace();
        }

    }


    public static void main(String[] args)
    {

        TestOmnipod to = new TestOmnipod();
        to.testOmnipod();

    }

}
