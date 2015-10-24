package ggc.pump.test;

import java.io.File;
import java.util.Vector;

import com.atech.i18n.I18nControlAbstract;
import com.atech.utils.TimerThread;
import com.atech.utils.data.TimeZoneUtil;

import ggc.plugin.device.impl.minimed.file.MinimedCareLink;
import ggc.plugin.output.ConsoleOutputWriter;
import ggc.plugin.protocol.SerialProtocol;
import ggc.pump.device.accuchek.AccuChekCombo;
import ggc.pump.device.animas.FRC_EZManager_v2;
import ggc.pump.device.dana.DanaDiabecare_III_R;
import ggc.pump.device.minimed.file.MinimedCareLinkPump;

// TODO: Auto-generated Javadoc
/**
 * Application: GGC - GNU Gluco Control Plug-in: Pump Tool (support for Pump
 * devices)
 *
 * See AUTHORS for copyright information.
 * 
 * This program is free software; you can redistribute it and/or modify it under
 * the terms of the GNU General Public License as published by the Free Software
 * Foundation; either version 2 of the License, or (at your option) any later
 * version.
 * 
 * This program is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU General Public License for more
 * details.
 * 
 * You should have received a copy of the GNU General Public License along with
 * this program; if not, write to the Free Software Foundation, Inc., 59 Temple
 * Place, Suite 330, Boston, MA 02111-1307 USA
 * 
 * Filename: ###---### Description:
 * 
 * Author: Andy {andy@atech-software.com}
 */

public class PumpConsoleTester extends AbstractPumpTest
{

    String path_to_test_files = "../../test/";

    /**
     * The thread.
     */
    TimerThread thread;


    /**
     * Constructor
     * 
     * @param portName
     */
    public PumpConsoleTester(String portName)
    {

        TimeZoneUtil tzu = TimeZoneUtil.getInstance();

        tzu.setTimeZone("Europe/Prague");
        tzu.setWinterTimeChange(0);
        tzu.setSummerTimeChange(0);

        prepareContext();

        try
        {
            startRoche(portName);
            // startAnimas();
            // startCosmo();
            // startDana(portName);
            // startMinimed("./dta/CareLink-Export-1213803114904.csv");
            // startMinimed("");
            // test();
        }
        catch (Exception ex)
        {
            System.out.println("Tester -> Exception on creation of Pump. " + ex);
            ex.printStackTrace();
        }

    }


    public PumpConsoleTester()
    {

    }


    /**
     * Test
     */
    // public void test()
    // {
    // int num = 56;
    //
    // if ((num & DownloadSupportType.DOWNLOAD_FROM_DEVICE) ==
    // DownloadSupportType.DOWNLOAD_FROM_DEVICE)
    // {
    // System.out.println("DOWNLOAD_DEVICE");
    // }
    //
    // }

    /**
     * Start Dana - For testing implementation of Dana Pump
     * 
     * @param portname
     * @throws Exception
     */
    public void startDana(String portname) throws Exception
    {
        DanaDiabecare_III_R dana = new DanaDiabecare_III_R("COM29", new ConsoleOutputWriter());
        dana.readDeviceDataFull();
    }


    /**
     * Start Roche
     * 
     * @param portName
     * @throws Exception
     */
    public void startRoche(String portName) throws Exception
    {
        // DataAccess daCore = DataAccess.getInstance();
        //
        // DataAccessPump dap =
        // DataAccessPump.createInstance(daCore.getLanguageManager());
        // dap.initAllObjects();

        // DataAccessPump dap = DataAccessPump.createInstance(new
        // LanguageManager(new GGCLanguageManagerRunner())); // .getInstance();
        // dap.setHelpContext(da.getHelpContext());
        // dap.setPlugInServerInstance(this);
        // dap.createDb(da.getHibernateDb());
        // dap.dap.initAllObjects();
        // dap.loadSpecialParameters();
        // this.backup_restore_enabled = true;

        // da.loadSpecialParameters();

        // AccuChekSpirit acs = new AccuChekSpirit("", new
        // ConsoleOutputWriter());
        // acs.processXml(new File("../test/I0014072.XML"));
        // acs.processXml(new File("../test/I0026117_2303_2010.XML"));

        // path_to_test_files = "d:\\";

        path_to_test_files = "/home/andy/Dropbox/GGC/bugs/";

        AccuChekCombo acc = new AccuChekCombo("", new ConsoleOutputWriter());
        acc.processXml(new File(path_to_test_files + "I1026739.XML"));
        // I0122425_1112_2010.XML, I0122425_1510_2010.XML ,
        // I0122425_2808_2010.XML

        // "../test/I0026117.XML"));
        // acs.test();

        // AccuChekSmartPixPump pp = (AccuChekSmartPixPump)acs;
        // pp.test();

    }


    /**
     * Start Minimed
     * 
     * @param file
     * @throws Exception
     */
    public void startMinimed(String file) throws Exception
    {
        // MinimedCareLink mcl = new MinimedCareLink();
        // mcl.parseExportFile(new File(file));
        // MinimedSMP msp = new MinimedSMP("f:\\Rozman_A_Plus_20090423.mmp");

        // DataAccess da = DataAccess.getInstance();
        //
        // GGCDb db = new GGCDb(da);
        // // db.initDb();
        //
        // da.setDb(db);
        //
        // DataAccessPump dap =
        // DataAccessPump.createInstance(da.getLanguageManager());
        // // dap.setHelpContext(da.getHelpContext());
        // // dap.setPlugInServerInstance(this);
        // // dap.createDb(da.getHibernateDb());
        // dap.initAllObjects();
        // dap.loadSpecialParameters();
        // // this.backup_restore_enabled = true;
        //
        // da.loadSpecialParameters();
        // // System.out.println("PumpServer: " +
        // // dataAccess.getSpecialParameters().get("BG"));
        //
        // dap.setGlucoseUnitType(da.getIntValueFromString(da.getSpecialParameters().get("BG")));

        System.out.println(new File(".").getAbsolutePath());

        MinimedCareLinkPump mcl = new MinimedCareLinkPump(dataAccessPump, new ConsoleOutputWriter(),
                MinimedCareLink.READ_DEVICE_DATA);
        mcl.parseExportFile(new File(path_to_test_files + "CareLink-Export-1213803114904.csv"));

        // MinimedSPMPump msp = new MinimedSPMPump("Nemec_B_001_20090425.mmp",
        // DataAccessPump.getInstance());
        // msp.readData();

    }


    public void checkTranslation()
    {

        // DataAccess da = DataAccess.getInstance();
        // // da.initSpecial();
        //
        // // GGCDb db = new GGCDb(da);
        // // db.initDb();
        //
        // // da.setDb(db);
        //
        // DataAccessPump dap =
        // DataAccessPump.createInstance(da.getLanguageManager());
        // // dap.setHelpContext(da.getHelpContext());
        // // dap.setPlugInServerInstance(this);
        // // dap.createDb(da.getHibernateDb());
        // dap.initAllObjects();
        // dap.loadSpecialParameters();
        // // this.backup_restore_enabled = true;
        //
        // da.loadSpecialParameters();
        // // System.out.println("PumpServer: " +
        // // dataAccess.getSpecialParameters().get("BG"));
        //
        // dap.setGlucoseUnitType(da.getIntValueFromString(da.getSpecialParameters().get("BG")));

        I18nControlAbstract ic = dataAccessPump.getI18nControlInstance();

        System.out.println("Profiles: " + ic.getMessage("PUMP_DATA_PROFILES"));

        System.out.println("January: " + ic.getMessage("JANUARY"));

        System.out.println("Report Type: " + ic.getMessage("REPORT_TYPE"));

    }


    /**
     * Start Animas
     */
    public void startAnimas()
    {
        // DataAccess da = DataAccess.getInstance();
        //
        // GGCDb db = new GGCDb(da);
        // db.initDb();
        //
        // da.setDb(db);
        //
        // DataAccessPump dap = DataAccessPump.createInstance(new
        // LanguageManager(new GGCLanguageManagerRunner())); // .getInstance();
        //
        // // DataAccessPump dap = DataAccessPump.getInstance();
        // // dap.setHelpContext(da.getHelpContext());
        // // dap.setPlugInServerInstance(this);
        // dap.createDb(da.getHibernateDb());
        // dap.initAllObjects();
        // dap.loadSpecialParameters();
        // // this.backup_restore_enabled = true;
        //
        // da.loadSpecialParameters();
        // // System.out.println("PumpServer: " +
        // // dataAccess.getSpecialParameters().get("BG"));
        //
        // dap.setGlucoseUnitType(da.getIntValueFromString(da.getSpecialParameters().get("BG")));

        FRC_EZManager_v2 ezm = new FRC_EZManager_v2(new ConsoleOutputWriter());
        ezm.readFile("/home/andy/workspace/EZMD.mdb");

    }


    /**
     * Start Cosmo
     */
    public void startCosmo()
    {
        // DataAccess da = DataAccess.getInstance();

        /*
         * GGCDb db = new GGCDb(da); db.initDb();
         * da.setDb(db);
         */

        // DataAccessPump dap = DataAccessPump.createInstance(new
        // LanguageManager(new GGCLanguageManagerRunner())); //.getInstance();

        // DataAccessPump dap = DataAccessPump.getInstance();
        // dap.setHelpContext(da.getHelpContext());
        // dap.setPlugInServerInstance(this);
        /*
         * dap.createDb(da.getHibernateDb()); dap.initAllObjects();
         * dap.loadSpecialParameters(); //this.backup_restore_enabled = true;
         * da.loadSpecialParameters(); //System.out.println("PumpServer: " +
         * dataAccess.getSpecialParameters().get("BG"));
         * dap.setGlucoseUnitType(da.getIntValueFromString(da.
         * getSpecialParameters
         * ().get("BG")));
         * //FRC_EZManager_v2 ezm = new FRC_EZManager_v2(new
         * ConsoleOutputWriter());
         * //ezm.readFile("/home/andy/workspace/EZMD.mdb");
         * //CoPilot cp = new CoPilot(dap);
         */

        // CoPilot cp = new CoPilot(dap);
        // cp.readFile("/home/andy/workspace/GGC
        // Desktop/test/CoPilot_Arch.XML");

    }


    /**
     * Display Serial Ports
     */
    public void displaySerialPorts()
    {
        try
        {
            Vector<String> vct = SerialProtocol.getAllAvailablePortsString();

            System.out.println(" --- List Serial Ports -----");

            for (int i = 0; i < vct.size(); i++)
            {
                System.out.println(vct.get(i));
            }
        }
        catch (Exception ex)
        {
            System.out.println("Exception getting serial ports. Ex: " + ex);
            ex.printStackTrace();
        }

    }


    /**
     * Something.
     */
    public static void something()
    {
        // String input = "0-400=0.2";
        // String[] ss = input.split("[-=]");

    }


    /**
     * Main startup method
     * 
     * @param args
     */
    public static void main(String args[])
    {
        try
        {
            // if (args.length == 0)
            // {
            // PumpConsoleTester pct = new PumpConsoleTester();
            // pct.checkTranslation();
            // }
            // else
            // {
            // new PumpConsoleTester(args[0]);
            // }

            new PumpConsoleTester(args[0]);

        }
        catch (Exception ex)
        {
            System.out.println("Error:" + ex);
            ex.printStackTrace();
        }
    }

}
