package main.java.ggc.pump.test;

import java.util.GregorianCalendar;

import com.atech.utils.data.ATechDate;

import ggc.plugin.device.impl.animas.enums.AnimasDeviceType;
import ggc.plugin.output.ConsoleOutputWriter;
import main.java.ggc.pump.device.animas.impl.AnimasPumpDeviceReader;
import main.java.ggc.pump.device.animas.impl.handler.AnimasPumpDataWriter;

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
 *  Filename:     TestAnimas
 *  Description:  Test Animas
 *
 *  Author: Andy Rozman {andy@atech-software.com}
 */

public class TestAnimas extends AbstractPumpTest
{

    public void testAnimas()
    {
        prepareContext();

        try
        {

            AnimasPumpDataWriter d = new AnimasPumpDataWriter(new ConsoleOutputWriter());

            ATechDate atd = new ATechDate(ATechDate.FORMAT_DATE_AND_TIME_MS, new GregorianCalendar());

            d.getDeviceValuesWriter().writeObject("TDD_All_Insulin", atd, (String) null);

            if (true)
                return;

            // DbToolApplicationGGC m_configFile = new DbToolApplicationGGC();
            // m_configFile.loadConfig();

            // DataAccess daCore = DataAccess.getInstance();

            // GGCDbLoader loader = new GGCDbLoader(daCore);
            // loader.run();

            // GGCDbConfig db = new GGCDbConfig(false);

            // GGCDb db = new GGCDb(daCore);
            // db.initDb();

            // DataAccessPump da =
            // DataAccessPump.createInstance(daCore.getLanguageManager());
            // da.initAllObjects();

            // da.createPlugInDataRetrievalContext();
            //
            // da.createDb(daCore.getHibernateDb());

            String portName = "/dev/ttyUSB0"; // linux

            if (System.getProperty("os.name").toLowerCase().contains("win"))
            {
                portName = "COM9";
            }

            AnimasPumpDeviceReader adr = new AnimasPumpDeviceReader(portName, AnimasDeviceType.Animas_Vibe,
                    new ConsoleOutputWriter());
            adr.readData();

        }
        catch (Exception ex)
        {

            System.out.println("Error running AnimasDeviceReader: " + ex.getMessage());
            ex.printStackTrace();
        }

    }


    public static void main(String[] args)
    {
        TestAnimas ta = new TestAnimas();
        ta.testAnimas();
    }

}
