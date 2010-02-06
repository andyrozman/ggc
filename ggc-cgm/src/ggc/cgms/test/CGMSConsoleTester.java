package ggc.cgms.test;

import ggc.cgms.device.dexcom.FRC_DexcomXml_DM3;
import ggc.cgms.device.minimed.MinimedCareLink;
import ggc.cgms.util.DataAccessCGMS;
import ggc.core.util.GGCLanguageManagerRunner;
import ggc.plugin.gui.file.ImportFileSelectorPanel;

import java.io.File;

import javax.swing.JDialog;

import com.atech.i18n.mgr.LanguageManager;
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


public class CGMSConsoleTester //extends JFrame
{
	
    TimerThread thread;
    
    /**
     * Constructor
     * @param portName
     */
    public CGMSConsoleTester(String portName)
    {
    	
    	TimeZoneUtil  tzu = TimeZoneUtil.getInstance();
    	
		tzu.setTimeZone("Europe/Prague");
		tzu.setWinterTimeChange(0);
		tzu.setSummerTimeChange(+1);
    	
        
		//thread = new TimerThread();
	    //thread.start();
	    
    	try
    	{
//    	    startDexcom();
    		
    		startFileSelector();
    		
    	    
    	    //this.startMiniMed("");
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


    private void startDexcom()
    {
        DataAccessCGMS dap = DataAccessCGMS.createInstance(new LanguageManager(new GGCLanguageManagerRunner())); //.getInstance();
        //dap.initAllObjects();
        dap.loadSpecialParameters();
     
        //FRC_DexcomTxt_DM3 dt = new FRC_DexcomTxt_DM3(dap);
        //dt.readFile("../test/DexDM3SampleExport.txt");
        
        FRC_DexcomXml_DM3 dt1 = new FRC_DexcomXml_DM3(dap);
        dt1.readFile("../test/DexDM3SampleExport.xml");
        
    }
    
    
    @SuppressWarnings("unused")
    private void startMiniMed(String portName) throws Exception
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
    
    
    public void startFileSelector()
    {
        DataAccessCGMS dap = DataAccessCGMS.createInstance(new LanguageManager(new GGCLanguageManagerRunner())); //.getInstance();
        //dap.initAllObjects();
        dap.loadSpecialParameters();

    	
    	JDialog d = new JDialog();
		//MultipleFileSelectorPanel p = new MultipleFileSelectorPanel(dap, d);
		ImportFileSelectorPanel p = new ImportFileSelectorPanel(dap, d, null);
    	
		d.getContentPane().add(p);
		
		d.setBounds(20, 20, p.getMinSize()[0], p.getMinSize()[1]);
		d.setVisible(true);
    }
    


    /**
     * Main method
     * @param args
     */
    public static void main(String args[])
    {
	try
	{
	    
	    if (args.length == 0)
	        new CGMSConsoleTester("");
	    else
	        new CGMSConsoleTester(args[0]);

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
