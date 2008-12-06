/*
 *  GGC - GNU Gluco Control
 *
 *  A pure java app to help you manage your diabetes.
 *
 *  See AUTHORS for copyright information.
 *
 *  This program is free software; you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation; either version 2 of the License, or
 *  (at your option) any later version.
 *
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with this program; if not, write to the Free Software
 *  Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA
 *
 *  Filename: MeterManager.java
 *  Purpose:  This class contains all definitions for Meters. This includes:
 *        meter names, classes that handle meter and all other relevant data.
 *
 *  Author:   Alex {abalaban1@yahoo.ca}
 *  
 */

package ggc.meter.test;

import ggc.meter.device.onetouch.OneTouchUltraSmart;
import ggc.plugin.output.ConsoleOutputWriter;
import ggc.plugin.protocol.SerialProtocol;

import java.util.Vector;

import com.atech.utils.TimeZoneUtil;
import com.atech.utils.TimerThread;

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
 *  Filename:     MeterConsoleTesterAlex  
 *  Description:  Console tester for testing meter implementation of OT Ultra Smart
 * 
 *  Author: Alex {abalaban1@yahoo.ca}
 */


public class MeterConsoleTesterAlex // extends JFrame
{

    TimerThread thread;

    /**
     * Constructor
     * 
     * @param portName
     */
    public MeterConsoleTesterAlex(String portName)
    {

        TimeZoneUtil tzu = TimeZoneUtil.getInstance();

        tzu.setTimeZone("Europe/Prague");
        tzu.setWinterTimeChange(0);
        tzu.setSummerTimeChange(0);

        try
        {
            this.startOneTouchUltraSmart(portName);
        }
        catch (Exception ex)
        {
            System.out.println("Tester -> Exception on creation of meter. " + ex);
            ex.printStackTrace();
        }

    }

    /**
     * @param portName
     * @throws Exception
     */
    public void startOneTouchUltraSmart(String portName) throws Exception
    {

        ConsoleOutputWriter cow = new ConsoleOutputWriter();

        // a thread.addJob(cow.getOutputUtil());

        displaySerialPorts();

        OneTouchUltraSmart otUS = new OneTouchUltraSmart(portName, cow);

        // m_meter = new OneTouchUltra(portName, cow);
        otUS.readDeviceDataFull();

    }

    /**
     * Display Serial Ports
     */
    public void displaySerialPorts()
    {
        Vector<String> vct = SerialProtocol.getAllAvailablePortsString();

        System.out.println(" --- List Serial Ports -----");

        for (int i = 0; i < vct.size(); i++)
        {
            System.out.println(vct.get(i));
        }
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
                new MeterConsoleTesterAlex("COM5");
            else
                new MeterConsoleTesterAlex(args[0]);

        }
        catch (Exception ex)
        {
            System.out.println("Error:" + ex);
            ex.printStackTrace();
        }
    }

}
