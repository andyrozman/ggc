package ggc.cgm.test;

import ggc.cgm.device.minimed.MinimedCareLink;

import java.awt.TextArea;
import java.io.File;

import com.atech.utils.TimeZoneUtil;
import com.atech.utils.TimerThread;

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
 *  Filename:  ###---###  
 *  Description:
 * 
 *  Author: Andy {andy@atech-software.com}
 */


public class CGMConsoleTester //extends JFrame
{
	static final long serialVersionUID = 0;

	
//    private JButton openButton, sendButton, enterButton, clearButton;
//    private JTextField textField, textField2, textField3;

    public static TextArea messageArea;

    //private AscensiaContour m_meter;
    //private AbstractSerialMeter m_meter;
    //private SerialProtocol m_meter;

    TimerThread thread;
    
    public CGMConsoleTester(String portName)
    {
    	
    	TimeZoneUtil  tzu = TimeZoneUtil.getInstance();
    	
		tzu.setTimeZone("Europe/Prague");
		tzu.setWinterTimeChange(0);
		tzu.setSummerTimeChange(+1);
    	
        
		//thread = new TimerThread();
	    //thread.start();
	    
    	try
    	{
    	    this.startMiniMed("");
    	    //startAscensia(portName);
    	    //this.startOneTouchUltra(portName);
    	    
    	    
    	    
    	    /*
    		//GGCFileOutputWriter gfo = new GGCFileOutputWriter();
    	    ConsoleOutputWriter cow = new ConsoleOutputWriter();
    		
//    		thread.addJob(gfo.getOutputUtil());
    		
    		displaySerialPorts();
    		
/*    		
    		m_meter = new AscensiaContour(portName, gfo);
    	    m_meter.setPort(portName);
    	    m_meter.loadInitialData();
  */
    		
    //		m_meter = new OneTouchUltra(portName, cow);
    	//	m_meter.loadInitialData();
    		
    	}
    	catch(Exception ex)
    	{
    	    System.out.println("Tester -> Exception on creation of meter. " + ex);
    	    ex.printStackTrace();
    	} 

    }


    
    
    
    public void startMiniMed(String portName) throws Exception
    {
        
        MinimedCareLink mcl = new MinimedCareLink();
        
        mcl.parseExportFile(new File("./data/CareLink-Export-2008-05-18--06-01.csv"));
        
        
/*        
        ConsoleOutputWriter cow = new ConsoleOutputWriter();
        
//a        thread.addJob(cow.getOutputUtil());
        
        displaySerialPorts();
        
        OneTouchUltra otu = new OneTouchUltra(portName, cow);
        //m_meter = new OneTouchUltra(portName, cow);
        otu.loadInitialData();
*/
    }
    
    


    public static void main(String args[])
    {
	try
	{
	    
	    if (args.length == 0)
	        new CGMConsoleTester("");
	    else
	        new CGMConsoleTester(args[0]);

	    /*
	    AscensiaMeter am = new AscensiaMeter();
	    am.setPort("COM1");
	    am.open();

	    try
	    {
		Thread.sleep(2000);
		am.test();

	    }
	    catch(Exception ex)
	    {
	    }*/

	}
	catch(Exception ex)
	{
	    System.out.println("Error:" + ex);
	    ex.printStackTrace();
	}
    }



}
