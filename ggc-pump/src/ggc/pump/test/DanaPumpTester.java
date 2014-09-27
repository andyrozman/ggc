package ggc.pump.test;

import ggc.core.db.GGCDb;
import ggc.core.util.DataAccess;
import ggc.plugin.output.ConsoleOutputWriter;
import ggc.plugin.protocol.SerialProtocol;
import ggc.pump.device.dana.DanaDiabecare_III_R;
import ggc.pump.util.DataAccessPump;

import java.util.Vector;

import com.atech.utils.TimerThread;

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

public class DanaPumpTester // extends JFrame
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
    public DanaPumpTester(String portName)
    {

        try
        {
            startDana(portName);
        }
        catch (Exception ex)
        {
            System.out.println("Tester -> Exception on creation of Pump. " + ex);
            ex.printStackTrace();
        }

    }

    /**
     * Start Dana - For testing implementation of Dana Pump
     * @param portname
     * @throws Exception
     */
    public void startDana(String portname) throws Exception
    {
        DataAccessPump dap = DataAccessPump.getInstance();
        dap.initAllObjects();
        dap.loadSpecialParameters();

        DanaDiabecare_III_R dana = new DanaDiabecare_III_R("COM14", new ConsoleOutputWriter());
        dana.readConfiguration();
    }

    /**
     * Start Roche
     * 
     * @param portName
     * @throws Exception
     */
    public void startRoche(String portName) throws Exception
    {
        DataAccessPump dap = DataAccessPump.getInstance();
        // dap.setHelpContext(da.getHelpContext());
        // dap.setPlugInServerInstance(this);
        // dap.createDb(da.getHibernateDb());
        dap.initAllObjects();
        dap.loadSpecialParameters();
        // this.backup_restore_enabled = true;

        // da.loadSpecialParameters();

        // AccuChekSpirit acs = new AccuChekSpirit("", new
        // ConsoleOutputWriter());
        // acs.processXml(new File("../test/I0014072.XML"));
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

        DataAccess da = DataAccess.getInstance();

        GGCDb db = new GGCDb(da);
        db.initDb();

        da.setDb(db);

        DataAccessPump dap = DataAccessPump.getInstance();
        dap.setHelpContext(da.getHelpContext());
        // dap.setPlugInServerInstance(this);
        dap.createDb(da.getHibernateDb());
        dap.initAllObjects();
        dap.loadSpecialParameters();
        // this.backup_restore_enabled = true;

        // da.loadSpecialParameters();
        // System.out.println("PumpServer: " +
        // m_da.getSpecialParameters().get("BG"));

        dap.setBGMeasurmentType(da.getIntValueFromString(da.getSpecialParameters().get("BG")));

        // MinimedSPMPump msp = new MinimedSPMPump("Nemec_B_001_20090425.mmp",
        // DataAccessPump.getInstance());
        // msp.readData();

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
            new DanaPumpTester(args[0]);

        }
        catch (Exception ex)
        {
            System.out.println("Error:" + ex);
            ex.printStackTrace();
        }
    }

}
