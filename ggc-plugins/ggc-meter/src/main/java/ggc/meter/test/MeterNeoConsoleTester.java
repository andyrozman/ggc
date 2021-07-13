package ggc.meter.test;

import com.atech.utils.TimerThread;
import com.atech.utils.data.TimeZoneUtil;

import ggc.meter.defs.device.MeterDeviceDefinition;
import ggc.meter.device.abbott.AbbottNeoMeterHandler;
import ggc.plugin.device.DeviceIdentification;
import ggc.plugin.output.ConsoleOutputWriter;

/**
 *  Application:   GGC - GNU Gluco Control
 *  Plug-in:       CGMS Tool (support for CGMS devices)
 *
 *  See AUTHORS for copyright information.
 *
 *  This program is free software; you can redistribute it and/or modify it under
 *  the terms of the GNU General Public License as published by the Free Software
 *  Foundation; either version 2 of the License, or (at your option) any later
 *  version.
 *
 *  This program is distributed in the hope that it will be useful, but WITHOUT
 *  ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 *  FOR A PARTICULAR PURPOSE. See the GNU General Public License for more
 *  details.
 *
 *  You should have received a copy of the GNU General Public License along with
 *  this program; if not, write to the Free Software Foundation, Inc., 59 Temple
 *  Place, Suite 330, Boston, MA 02111-1307 USA
 *
 *  Filename:     PluginDb
 *  Description:  This is master class for using Db instance within plug-in. In most cases, we
 *                would want data to be handled by outside authority (GGC), but in some cases
 *                we wouldn't want that.
 *
 *  Author: Andy {andy@atech-software.com}
 */

public class MeterNeoConsoleTester extends AbstractMeterTest
{

    TimerThread thread;
    String path_to_test_files = "../../test/";


    /**
     * Constructor
     *
     * @param portName
     */
    public MeterNeoConsoleTester(String portName)
    {

        TimeZoneUtil tzu = TimeZoneUtil.getInstance();

        tzu.setTimeZone("Europe/Prague");
        tzu.setWinterTimeChange(0);
        tzu.setSummerTimeChange(+1);

        // thread = new TimerThread();
        // thread.start();

        prepareContext();

        try
        {
            // startDexcom();

            // startFileSelector();

            // this.startMinimed("");

            this.startNeo();

            // startAscensia(portName);
            // this.startOneTouchUltra(portName);

            /*
             * //GGCFileOutputWriter gfo = new GGCFileOutputWriter();
             * ConsoleOutputWriter cow = new ConsoleOutputWriter();
             * // thread.addJob(gfo.getOutputUtil());
             * displaySerialPorts();
             * /*
             * m_meter = new AscensiaContour(portName, gfo);
             * m_meter.setPort(portName);
             * m_meter.loadInitialData();
             */

            // m_meter = new OneTouchUltra(portName, cow);
            // m_meter.loadInitialData();

        }
        catch (Exception ex)
        {
            System.out.println("Tester -> Exception on creation of meter. " + ex);
            ex.printStackTrace();
        }

    }





    public void startNeo() throws Exception
    {
        AbbottNeoMeterHandler handler = new AbbottNeoMeterHandler();

        // DeviceDefinition definition, Object connectionParameters, OutputWriter outputWriter

        ConsoleOutputWriter cop = new ConsoleOutputWriter();
        cop.setDeviceIdentification(new DeviceIdentification(dataAccessMeter.getI18nControlInstance()));

        boolean config = true;

        if (config)
        {
            handler.readConfiguration(MeterDeviceDefinition.AbbottFreeStyleOptiumNeo, "1a61:3850",
                    cop);
        }
        else
        {
            handler.readDeviceData(MeterDeviceDefinition.AbbottFreeStyleOptiumNeo, "1a61:3850", cop);
        }
    }





    /**
     * Start File Selector
     */
    public void startFileSelector()
    {
        // DataAccessCGMS dap = DataAccessCGMS.createInstance(new
        // LanguageManager(new GGCLanguageManagerRunner())); // .getInstance();
        // // dap.initAllObjects();
        // dap.loadSpecialParameters();

        // JDialog d = new JDialog();
        // MultipleFileSelectorPanel p = new MultipleFileSelectorPanel(dap, d);
        // ImportFileSelectorPanel p = new ImportFileSelectorPanel(dap, d, null,
        // new FRC_DexcomTxt_DM3(dap));

        // d.getContentPane().add(p);

        // d.setBounds(20, 20, p.getMinSize()[0], p.getMinSize()[1]);
        // d.setVisible(true);
    }


    /**
     * Main method
     * 
     * @param args
     */
    public static void main(String args[])
    {
        try
        {

            if (args.length == 0)
            {
                new MeterNeoConsoleTester("");
            }
            else
            {
                new MeterNeoConsoleTester(args[0]);
            }

            /*
             * AscensiaMeter am = new AscensiaMeter();
             * am.setPort("COM1");
             * am.open();
             * try
             * {
             * Thread.sleep(2000);
             * am.test();
             * }
             * catch(Exception ex)
             * {
             * }
             */

        }
        catch (Exception ex)
        {
            System.out.println("Error:" + ex);
            ex.printStackTrace();
        }
    }

}
