package ggc.meter.test;

import java.io.File;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.atech.utils.ATDataAccessLMAbstract;
import com.atech.utils.data.TimeZoneUtil;

import ggc.core.db.GGCDb;
import ggc.core.util.DataAccess;
import ggc.meter.defs.MeterPluginDefinition;
import ggc.meter.defs.device.MeterDeviceDefinition;
import ggc.meter.device.abbott.OptiumXceed;
import ggc.meter.device.accuchek.AccuChekAvivaCombo;
import ggc.meter.device.ascensia.AscensiaContour;
import ggc.meter.device.ascensia.AscensiaUsbMeterHandler;
import ggc.meter.device.menarini.GlucofixMio;
import ggc.meter.device.onetouch.OneTouchUltra;
import ggc.meter.device.onetouch.OneTouchUltraEasy;
import ggc.meter.util.DataAccessMeter;
import ggc.plugin.output.ConsoleOutputWriter;
import ggc.plugin.protocol.SerialProtocol;

/**
 *  Application:   GGC - GNU Gluco Control
 *  Plug-in:       Meter Tool (support for Meter devices)
 *
 *  See AUTHORS for copyright information.
 *  <pre>
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
 *  </pre>
 *  Filename:     MeterConsoleTester  
 *  Description:  Console tester for testing meter implementations.
 * 
 *  Author: Andy {andy@atech-software.com}
 */

public class MeterConsoleTester
{

    String path_to_test_files = "../../test/";

    private static Logger logDevice = LoggerFactory.getLogger("deviceLogger");
    private static Logger logDeviceCat = LoggerFactory.getLogger("deviceLogger");


    /**
     * Constructor
     * 
     * @param portName
     */
    public MeterConsoleTester(String portName)
    {

        TimeZoneUtil tzu = TimeZoneUtil.getInstance();

        tzu.setTimeZone("Europe/Prague");
        tzu.setWinterTimeChange(0);
        // tzu.setSummerTimeChange(+1);
        tzu.setSummerTimeChange(0);

        preparePlugInContext();

        // thread = new TimerThread();
        // thread.start();

        try
        {
            //startAscensia(portName);
            this.startOneTouchUltra(portName);

            // this.startOneTouchEasy(portName);
            // startOptiumXceed(portName);
            // this.startMenarini(portName);

            // testLogger();

            // startAccuChekAviva();
            //startAscensiaUsb();

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


    /**
     * Ascensia Testing
     * 
     * @param portName
     * @throws Exception
     */
    public void startAscensia(String portName) throws Exception
    {
        // GGCFileOutputWriter ow = new GGCFileOutputWriter();
        ConsoleOutputWriter ow = new ConsoleOutputWriter();

        // thread.addJob(cow.getOutputUtil());
        // thread.addJob(ow.getOutputUtil());

        displaySerialPorts();

        AscensiaContour asc_meter = new AscensiaContour(portName, ow);
        asc_meter.setPort(portName);

        asc_meter.readDeviceDataFull();

        System.out.println("We are back in tester !!!!");

        System.exit(0);

    }


    /**
     * Ascensia Testing
     * 
     *
     * @throws Exception
     */
    public void startAscensiaUsb() throws Exception
    {
        // GGCFileOutputWriter ow = new GGCFileOutputWriter();
        ConsoleOutputWriter ow = new ConsoleOutputWriter();

        // thread.addJob(cow.getOutputUtil());
        // thread.addJob(ow.getOutputUtil());

        // displaySerialPorts();

        DataAccessMeter dap = preparePlugInContext();

        // DataAccessMeter dap = DataAccessMeter.createInstance(new
        // LanguageManager(new GGCLanguageManagerRunner())); // .getInstance();
        // // dap.setHelpContext(da.getHelpContext());
        // // dap.setPlugInServerInstance(this);
        // // dap.createDb(da.getHibernateDb());
        // dap.initAllObjects();
        // dap.loadSpecialParameters();
        //
        // //AscensiaContourUSB asc_meter = new AscensiaContourUSB("", ow);
        // // asc_meter.setPort(portName);

        AscensiaUsbMeterHandler handler = new AscensiaUsbMeterHandler();
        handler.readDeviceData(MeterDeviceDefinition.AscensiaContourUsb, null, new ConsoleOutputWriter());

        // AscensiaContourUsbReader reader = new AscensiaContourUsbReader()
        // asc_meter.readDeviceDataFull();

        System.out.println("We are back in tester !!!!");

        System.exit(0);

    }


    /**
     * Roche Testing
     */
    public void startAccuChekAviva()
    {
        try
        {
            DataAccess da = DataAccess.getInstance();

            DataAccessMeter dap = DataAccessMeter.createInstance(getPluginDefinition(da)); // .getInstance();
            // dap.setHelpContext(da.getHelpContext());
            // dap.setPlugInServerInstance(this);
            // dap.createDb(da.getHibernateDb());
            dap.initAllObjects();
            dap.loadSpecialParameters();
            // this.backup_restore_enabled = true;

            // da.loadSpecialParameters();

            // ConsoleOutputWriter ow = new ConsoleOutputWriter();

            // AccuChekAviva acv = new AccuChekAviva("g:", ow);
            // acv.readDeviceDataFull();

            AccuChekAvivaCombo acv = new AccuChekAvivaCombo("", new ConsoleOutputWriter());
            acv.processXml(new File(path_to_test_files + "G0079225_1112_2010.XML"));

            // G0079225_1112_2010.XML

        }
        catch (Exception ex)
        {
            System.out.println("Exception: " + ex);
            ex.printStackTrace();
        }
    }


    private DataAccessMeter preparePlugInContext()
    {
        DataAccess da = DataAccess.getInstance();

        GGCDb db = new GGCDb(da);
        // db.initDb();

        da.setDb(db);

        DataAccessMeter dap = DataAccessMeter.createInstance(getPluginDefinition(da));
        // dap.setHelpContext(da.getHelpContext());
        // dap.setPlugInServerInstance(this);
        // dap.createDb(da.getHibernateDb());
        dap.initAllObjects();
        dap.loadSpecialParameters();
        // this.backup_restore_enabled = true;

        da.loadSpecialParameters();
        // System.out.println("PumpServer: " +
        // dataAccess.getSpecialParameters().get("BG"));

        dap.setGlucoseUnitType(da.getGlucoseUnitType());

        return dap;
    }


    private MeterPluginDefinition getPluginDefinition(ATDataAccessLMAbstract da)
    {
        return new MeterPluginDefinition(da.getLanguageManager());
    }


    /**
     * OT Ultra testing
     * 
     * @param portName
     * @throws Exception
     */
    public void startOneTouchUltra(String portName) throws Exception
    {

        ConsoleOutputWriter cow = new ConsoleOutputWriter();

        // a thread.addJob(cow.getOutputUtil());

        displaySerialPorts();

        OneTouchUltra otu = new OneTouchUltra(portName, cow);

        // m_meter = new OneTouchUltra(portName, cow);
        otu.readDeviceDataFull();

    }


    /**
     * OT Easy Testing
     * @param portName
     * @throws Exception
     */
    public void startOneTouchEasy(String portName) throws Exception
    {

        ConsoleOutputWriter cow = new ConsoleOutputWriter();

        displaySerialPorts();

        OneTouchUltraEasy otu = new OneTouchUltraEasy(portName, cow);
        otu.readDeviceDataFull();
    }


    /**
     * OT Easy Testing
     * @param portName
     * @throws Exception
     */
    public void startOptiumXceed(String portName) throws Exception
    {

        ConsoleOutputWriter cow = new ConsoleOutputWriter();

        displaySerialPorts();

        OptiumXceed otu = new OptiumXceed(portName, cow);
        otu.readDeviceDataFull();
    }


    /**
     * OT Ultra testing
     * 
     * @param portName
     * @throws Exception
     */
    public void startMenarini(String portName) throws Exception
    {

        ConsoleOutputWriter cow = new ConsoleOutputWriter();

        // a thread.addJob(cow.getOutputUtil());

        displaySerialPorts();

        GlucofixMio otu = new GlucofixMio("COM16", cow);

        otu.readDeviceDataFull();

        // m_meter = new OneTouchUltra(portName, cow);
        // otu.readDeviceDataFull();

    }


    /**
     * Display Serial Ports
     */
    public static String displaySerialPorts()
    {
        try
        {
            List<String> vct = SerialProtocol.getAllAvailablePortsString();

            System.out.println(" --- List Serial Ports -----");

            for (int i = 0; i < vct.size(); i++)
            {
                System.out.println(vct.get(i));
            }

            if (vct.size()==1)
            {
                return vct.get(0);
            }



        }
        catch (Exception ex)
        {
            System.out.println("Exception getting serial ports. Ex: " + ex);
            ex.printStackTrace();
        }

        return null;
    }


    /**
     * Main method
     * 
     * @param args
     */
    public static void main(String args[])
    {

        String onlyPort = MeterConsoleTester.displaySerialPorts();



        try
        {

            if (args.length == 0)
            {
                if (onlyPort!=null)
                {
                    System.out.println("Found only port: " + onlyPort);
                    new MeterConsoleTester(onlyPort); // "/dev/ttyUSB0"); //COM5");
                }
                else
                    new MeterConsoleTester("COM9"); // "/dev/ttyUSB0"); //COM5");
            }
            else
            {
                new MeterConsoleTester(args[0]);
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

            System.exit(0);

        }
        catch (Exception ex)
        {
            System.out.println("Error:" + ex);
            ex.printStackTrace();
        }
    }


    /**
     * test Logger
     */
    public void testLogger()
    {
        logDevice.debug("debug message");
        logDevice.error("error message");
        logDevice.info("info message");
        logDevice.warn("warn message");
        logDevice.trace("trace message");

        logDeviceCat.debug("debug message");
        logDeviceCat.error("error message");

    }

}
