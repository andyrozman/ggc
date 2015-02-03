package ggc.pump.test;

import ggc.core.util.DataAccess;
import ggc.plugin.device.impl.animas.enums.AnimasDeviceType;
import ggc.plugin.output.ConsoleOutputWriter;
import ggc.pump.device.animas.impl.AnimasPumpDeviceReader;
import ggc.pump.util.DataAccessPump;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

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

public class TestAnimas
{

    private static final Log LOG = LogFactory.getLog(TestAnimas.class);

    public static void main(String[] args)
    {
        try
        {
            // DbToolApplicationGGC m_configFile = new DbToolApplicationGGC();
            // m_configFile.loadConfig();

            DataAccess daCore = DataAccess.getInstance();

            // GGCDbLoader loader = new GGCDbLoader(daCore);
            // loader.run();

            // GGCDbConfig db = new GGCDbConfig(false);

            // GGCDb db = new GGCDb(daCore);
            // db.initDb();

            DataAccessPump da = DataAccessPump.createInstance(daCore.getLanguageManager());
            da.initAllObjects();

            // da.createPlugInDataRetrievalContext();
            //
            // da.createDb(daCore.getHibernateDb());



            String portName = "/dev/ttyUSB0"; // linux

            if (System.getProperty("os.name").toLowerCase().contains("win"))
            {
                portName = "COM9";
            }


            AnimasPumpDeviceReader adr = new AnimasPumpDeviceReader(portName, AnimasDeviceType.Animas_Vibe, new ConsoleOutputWriter());
            adr.downloadPumpData();


        }
        catch (Exception ex)
        {

            System.out.println("Error running AnimasDeviceReader: " + ex.getMessage());
            ex.printStackTrace();
        }
    }

}
