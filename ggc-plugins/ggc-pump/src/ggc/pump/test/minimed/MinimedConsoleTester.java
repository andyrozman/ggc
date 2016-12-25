package ggc.pump.test.minimed;

import java.util.Iterator;
import java.util.List;
import java.util.Set;

import com.atech.utils.TimerThread;

import ggc.plugin.comm.NRSerialCommunicationHandler;
import ggc.plugin.device.impl.minimed.enums.MinimedCommInterfaceType;
import ggc.plugin.output.ConsoleOutputWriter;
import ggc.plugin.protocol.SerialProtocol;
import ggc.pump.defs.device.PumpDeviceDefinition;
import ggc.pump.device.minimed.MinimedPumpDeviceHandler;
import ggc.pump.device.minimed.old.MinimedSPMPump;
import ggc.pump.test.AbstractPumpTest;
import ggc.pump.util.DataAccessPump;

// TODO: Auto-generated Javadoc

/**
 *  Application:   GGC - GNU Gluco Control
 *  Plug-in:       Pump Tool (support for Pump devices)
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
 *  Filename:  ###---###  
 *  Description:
 * 
 *  Author: Andy {andy@atech-software.com}
 */

public class MinimedConsoleTester extends AbstractPumpTest
{

    /**
     * The thread.
     */
    TimerThread thread;


    /**
     * Constructor
     * 
     * @param portName
     */
    public MinimedConsoleTester(String portName)
    {

        // TimeZoneUtil tzu = TimeZoneUtil.getInstance();
        //
        // tzu.setTimeZone("Europe/Prague");
        // tzu.setWinterTimeChange(0);
        // tzu.setSummerTimeChange(0);
        this.initDb = false;

        prepareContext();

        try
        {
            // startRoche(portName);
            // startAnimas();
            // startCosmo();
            // startDana(portName);
            // startMinimed("./dta/CareLink-Export-1213803114904.csv");
            // startMinimed("");

            boolean old = false;

            if (portName != null)
            {
                old = true;
            }

            startMinimed_Device_512(old);
        }
        catch (Exception ex)
        {
            System.out.println("Tester -> Exception on creation of Pump. " + ex);
            ex.printStackTrace();
        }

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
        // db.initDb();
        //
        // da.setDb(db);
        //
        // DataAccessPump dap = DataAccessPump.getInstance();
        // dap.setHelpContext(da.getHelpContext());
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
        // dap.setGlucoseUnitType(da.getGlucoseUnitType());

        MinimedSPMPump msp = new MinimedSPMPump("Nemec_B_001_20090425.mmp", DataAccessPump.getInstance());
        msp.readData();

    }


    /**
     * Start Minimed
     * 
     * 
     * @throws Exception
     */
    // public void startMinimed_Device() throws Exception
    // {
    // // MinimedCareLink mcl = new MinimedCareLink();
    // // mcl.parseExportFile(new File(file));
    // // MinimedSMP msp = new MinimedSMP("f:\\Rozman_A_Plus_20090423.mmp");
    //
    // // DataAccess da = DataAccess.createInstance(null, new
    // // LanguageManager(new GGCLanguageManagerRunner()));
    // // da.loadSpecialParameters();
    // /*
    // * GGCDb db = new GGCDb(da);
    // * db.initDb();
    // * da.setDb(db);
    // */
    // // DataAccessPump dap = DataAccessPump.createInstance(new
    // // LanguageManager(new GGCLanguageManagerRunner()));
    // // // dap.setHelpContext(da.getHelpContext());
    // // // dap.setPlugInServerInstance(this);
    // // // dap.createDb(da.getHibernateDb());
    // // dap.initAllObjects();
    // // dap.loadSpecialParameters();
    // // // this.backup_restore_enabled = true;
    // //
    // // // System.out.println("PumpServer: " +
    // // // dataAccess.getSpecialParameters().get("BG"));
    //
    // //
    // dap.setGlucoseUnitType(da.getIntValueFromString(da.getSpecialParameters().get("BG")));
    //
    // // MinimedSPMPump msp = new MinimedSPMPump("Nemec_B_001_20090425.mmp",
    // // DataAccessPump.getInstance());
    // // msp.readData();
    //
    // System.out.println("String Buffer - Start ");
    //
    // StringBuffer sb = new StringBuffer();
    // // sb.append(MinimedDevice.INTERFACE_COMLINK);
    // sb.append(MinimedCommInterfaceType.CareLinkUSB.name());
    // sb.append(";");
    // // sb.append("COM3"); // port
    //
    // Set<String> ports = NRSerialCommunicationHandler.getAvailablePorts();
    //
    // System.out.println("Ports: " + ports);
    //
    // if (ports.size() == 1)
    // {
    // Iterator<String> it = ports.iterator();
    // sb.append(it.next());
    // }
    // else
    // {
    // sb.append("/dev/ttyUSB4");
    // }
    //
    // sb.append(";");
    // sb.append("316551"); // serial number
    // // sb.append("499012"); // serial number
    //
    // System.out.println("Minimed511 - Start ");
    // /*
    // * //Minimed554_Veo mm = new Minimed554_Veo(sb.toString(), new
    // * ConsoleOutputWriter());
    // * Minimed512 mm = new Minimed512(dap, sb.toString(), new
    // * ConsoleOutputWriter());
    // * System.out.println("mm" + mm);
    // * mm.readDeviceDataFull();
    // */
    // // portocol_id;port;serial_id
    //
    // // MMX54 mmx = new MMX54(14, 0, 0);
    //
    // // mmx.getModelNumber();
    //
    // // mmx.
    //
    // // Minimed554_Veo mm = new Minimed554_Veo(dap, sb.toString(), new
    // // ConsoleOutputWriter());
    // // mm.readDeviceDataFull();
    //
    // Minimed522 mm = new Minimed522(dataAccessPump, sb.toString(), new
    // ConsoleOutputWriter());
    // mm.readDeviceDataFull();
    //
    // }

    /**
     * Start Minimed
     *
     *
     * @throws Exception
     * @param old
     */
    // FIXME
    public void startMinimed_Device_512(boolean old) throws Exception
    {
        // MinimedCareLink mcl = new MinimedCareLink();
        // mcl.parseExportFile(new File(file));
        // MinimedSMP msp = new MinimedSMP("f:\\Rozman_A_Plus_20090423.mmp");

        // DataAccess da = DataAccess.createInstance(null, new
        // LanguageManager(new GGCLanguageManagerRunner()));
        // da.loadSpecialParameters();
        /*
         * GGCDb db = new GGCDb(da);
         * db.initDb();
         * da.setDb(db);
         */

        // boolean old = false;

        // DataAccess daCore = DataAccess.getInstance();
        //
        // DataAccessPump dap =
        // DataAccessPump.createInstance(daCore.getLanguageManager());
        // dap.initAllObjects();

        // DataAccessPump dap = DataAccessPump.createInstance(new
        // LanguageManager(new GGCLanguageManagerRunner()));
        // // dap.setHelpContext(da.getHelpContext());
        // // dap.setPlugInServerInstance(this);
        // // dap.createDb(da.getHibernateDb());
        // dap.initAllObjects();
        // dap.loadSpecialParameters();
        // this.backup_restore_enabled = true;

        // System.out.println("PumpServer: " +
        // dataAccess.getSpecialParameters().get("BG"));

        // dap.setGlucoseUnitType(da.getIntValueFromString(da.getSpecialParameters().get("BG")));

        // MinimedSPMPump msp = new MinimedSPMPump("Nemec_B_001_20090425.mmp",
        // DataAccessPump.getInstance());
        // msp.readData();

        System.out.println("String Buffer - Start ");

        StringBuffer sb = new StringBuffer();
        if (old)
        {
            // sb.append(MinimedDevice.INTERFACE_COMLINK);
        }
        else
        {
            // sb.append(MinimedCommInterfaceType.CareLinkUSB.name());
            sb.append(MinimedCommInterfaceType.CareLinkUSB.name());
        }
        sb.append(";");
        // sb.append("COM3"); // port
        // sb.append("/dev/ttyUSB0" + "");

        Set<String> ports = NRSerialCommunicationHandler.getAvailablePorts();

        System.out.println("Ports: " + ports);

        if (ports.size() == 1)
        {
            Iterator<String> it = ports.iterator();
            sb.append(it.next());
        }
        else
        {
            sb.append("/dev/ttyUSB8");
        }

        sb.append(";");
        sb.append("316551"); // serial number
        // sb.append("101161");
        // sb.append("499012"); // serial number

        System.out.println("Minimed512 - Start ");
        /*
         * //Minimed554_Veo mm = new Minimed554_Veo(sb.toString(), new
         * ConsoleOutputWriter());
         * Minimed512 mm = new Minimed512(dap, sb.toString(), new
         * ConsoleOutputWriter());
         * System.out.println("mm" + mm);
         * mm.readDeviceDataFull();
         */
        // portocol_id;port;serial_id

        // MMX54 mmx = new MMX54(14, 0, 0);

        // mmx.getModelNumber();

        // mmx.

        // Minimed554_Veo mm = new Minimed554_Veo(dap, sb.toString(), new
        // ConsoleOutputWriter());
        // mm.readDeviceDataFull();
        ConsoleOutputWriter cop = new ConsoleOutputWriter();

        // Minimed512 mm = new Minimed512(dap,
        // PumpDeviceDefinition.Minimed_512_712.getDeviceId(), sb.toString(),
        // cop);
        // mm.readDeviceDataFull();

        if (old)
        {
            // Minimed512 mm = new Minimed512(dataAccessPump,
            // PumpDeviceDefinition.Minimed_512_712.getDeviceId(),
            // sb.toString(), cop);
            // mm.readDeviceDataFull();
        }
        else
        {
            MinimedPumpDeviceHandler handler = new MinimedPumpDeviceHandler(dataAccessPump);
            // handler.readDeviceData(PumpDeviceDefinition.Minimed_512_712,
            // sb.toString(), cop);

            handler.readConfiguration(PumpDeviceDefinition.Minimed_512_712, sb.toString(), cop);

        }
        // Minimed522 mm = new Minimed522(dap, sb.toString(), new
        // ConsoleOutputWriter());
        // mm.readDeviceDataFull();

    }


    /**
     * Display Serial Ports
     */
    public void displaySerialPorts()
    {
        try
        {
            List<String> vct = SerialProtocol.getAllAvailablePortsString();

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
            if (args.length == 0)
                new ggc.pump.test.MinimedConsoleTester(null);
            else
                new ggc.pump.test.MinimedConsoleTester(args[0]);

        }
        catch (Exception ex)
        {
            System.out.println("Error:" + ex);
            ex.printStackTrace();
        }
    }

}
