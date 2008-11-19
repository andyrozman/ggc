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
 *  Author:   andyrozman
 */


package ggc.pump.test;

import ggc.plugin.output.ConsoleOutputWriter;
import ggc.pump.device.accuchek.AccuChekSpirit;
import ggc.pump.device.minimed.MinimedCareLink;
import ggc.pump.protocol.SerialProtocol;

import java.awt.TextArea;
import java.io.File;
import java.util.Vector;

import com.atech.utils.TimeZoneUtil;
import com.atech.utils.TimerThread;


public class PumpConsoleTester //extends JFrame
{
	static final long serialVersionUID = 0;

	
//    private JButton openButton, sendButton, enterButton, clearButton;
//    private JTextField textField, textField2, textField3;

    public static TextArea messageArea;

    //private AscensiaContour m_meter;
    //private AbstractSerialMeter m_meter;
    //private OneTouchUltra m_meter;
    //private SerialProtocol m_meter;

    TimerThread thread;
    
    public PumpConsoleTester(String portName)
    {
    	
    	TimeZoneUtil  tzu = TimeZoneUtil.getInstance();
    	
		tzu.setTimeZone("Europe/Prague");
		tzu.setWinterTimeChange(0);
		tzu.setSummerTimeChange(0);
    	
        
    	try
    	{
    	    //startRoche(portName);
    	    startMinimed("./dta/CareLink-Export-1213803114904.csv");
    	}
    	catch(Exception ex)
    	{
    	    System.out.println("Tester -> Exception on creation of meter. " + ex);
    	    ex.printStackTrace();
    	} 

    }



    public void startRoche(String portName) throws Exception
    {
        AccuChekSpirit acs = new AccuChekSpirit("", new ConsoleOutputWriter());
        acs.processXml(new File("I2034162.XML"));
    }
    
    
    public void startMinimed(String file) throws Exception
    {
        MinimedCareLink mcl = new MinimedCareLink();
        mcl.parseExportFile(new File(file));
        
    }
    
    
    public void displaySerialPorts()
    {
    	Vector<String> vct = SerialProtocol.getAvailableSerialPorts();
    	
		System.out.println(" --- List Serial Ports -----");
    	
    	for(int i=0; i<vct.size(); i++)
    	{
    		System.out.println(vct.get(i));
    	}
    }
    



    public static void main(String args[])
    {
	try
	{
	    new PumpConsoleTester(args[0]);


	}
	catch(Exception ex)
	{
	    System.out.println("Error:" + ex);
	    ex.printStackTrace();
	}
    }



}
