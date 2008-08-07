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


package ggc.meter.test;

import ggc.meter.device.accuchek.AccuChekAviva;
import ggc.meter.device.ascensia.AscensiaContour;
import ggc.meter.device.onetouch.OneTouchUltra;
import ggc.meter.output.ConsoleOutputWriter;
import ggc.meter.protocol.SerialProtocol;

import java.awt.TextArea;
import java.util.Vector;

import com.atech.utils.TimeZoneUtil;
import com.atech.utils.TimerThread;


public class MeterConsoleTester //extends JFrame
{
	static final long serialVersionUID = 0;

	
//    private JButton openButton, sendButton, enterButton, clearButton;
//    private JTextField textField, textField2, textField3;

    public static TextArea messageArea;

    //private AscensiaContour m_meter;
    //private AbstractSerialMeter m_meter;
    private OneTouchUltra m_meter;
    //private SerialProtocol m_meter;

    TimerThread thread;
    
    public MeterConsoleTester(String portName)
    {
    	
    	TimeZoneUtil  tzu = TimeZoneUtil.getInstance();
    	
		tzu.setTimeZone("Europe/Prague");
		tzu.setWinterTimeChange(0);
		tzu.setSummerTimeChange(+1);
    	
        
		//thread = new TimerThread();
	    //thread.start();
	    
    	try
    	{
    	    //startAscensia(portName);
    	    //this.startOneTouchUltra(portName);
    	    
    	    startAccuChekAviva();
    	    
    	    
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

/*
        //MeterImportManager mim = new MeterImportManager();

        if (mim.getErrorCode()!=0)
        {
            System.exit(1);
        }
*/


/*

        JFrame.setDefaultLookAndFeelDecorated(true);
        JDialog.setDefaultLookAndFeelDecorated(true);

        enableEvents(AWTEvent.WINDOW_EVENT_MASK);

        this.getContentPane().setLayout(null);
        this.setSize(640, 480);
        this.setTitle("Meter Tester - v0.1");
        this.setResizable(true);
        
        JPanel fullPanel = new JPanel();
        fullPanel.setLayout(null);
        fullPanel.setBounds(0,0,640,480);
        this.getContentPane().add(fullPanel);


        JLabel label = new JLabel("String to Send:");
        label.setBounds(30, 10, 100, 25);

        fullPanel.add(label);



        textField  = new JTextField();
        textField.setBounds(30, 35, 360, 25);
        fullPanel.add(textField);

        sendButton = new JButton("Send");
        sendButton.setBounds(405, 35, 65, 25);
        //sendButton.setEnabled(false);
        sendButton.addActionListener(new ActionListener()
            {
                public void actionPerformed(ActionEvent e) 
                {
                    readyToWrite(textField.getText()+"\n");
                    messageArea.append("--> "+textField.getText()+"\n");
                }
            });
        fullPanel.add(sendButton);


        enterButton = new JButton("Test");
        enterButton.setBounds(480, 35, 120, 25);
        //enterButton.setEnabled(false);
        enterButton.addActionListener(new ActionListener()
            {
                public void actionPerformed(ActionEvent e) 
                {
                    //readyToWrite("\n");
                    //messageArea.append("--> <Enter> sent.\n");
		    //m_meter.test2();
		    try
		    {
			m_meter.loadInitialData();

			System.out.println("Info: \n" + m_meter.getInfo());
			System.out.println("Time Difference: \n" + m_meter.getTimeDifference());

			//m_meter.getDataFull();
		    }
		    catch(Exception ex)
		    {
			System.out.println("Exception: " + ex);
		    }

		    m_meter.getInfo();
		    m_meter.getTimeDifference();
                }
            });
        fullPanel.add(enterButton);


        label = new JLabel("Communication window:");
        label.setBounds(30, 70, 200, 25);
        fullPanel.add(label);


        clearButton = new JButton("Clear");
        clearButton.setBounds(535, 73, 65, 20);
        clearButton.addActionListener(new ActionListener()
            {
                public void actionPerformed(ActionEvent e) 
                {
                    messageArea.setText("");
                }
            });
        fullPanel.add(clearButton);


        messageArea = new TextArea();
        messageArea.setBounds(30, 95, 570, 300);
        fullPanel.add(messageArea, null);

	this.setVisible(true);
	*/
    }


    public void startAscensia(String portName) throws Exception
    {
        //GGCFileOutputWriter ow = new GGCFileOutputWriter();
        ConsoleOutputWriter ow = new ConsoleOutputWriter();
        
        //thread.addJob(cow.getOutputUtil());
        //thread.addJob(ow.getOutputUtil());
        
        displaySerialPorts();
        
          
        AscensiaContour asc_meter = new AscensiaContour(portName, ow);
        asc_meter.setPort(portName);
        
       
        
        asc_meter.readDeviceDataFull(); 
        
        System.out.println("We are back in tester !!!!");
        
        System.exit(0);
        
    }

    
    public void startAccuChekAviva()
    {
        try
        {
            AccuChekAviva acv = new AccuChekAviva();
            acv.readDeviceData();
        }
        catch(Exception ex)
        {
            System.out.println("Exception: " + ex);
        }
    }
    
    
    
    public void startOneTouchUltra(String portName) throws Exception
    {
        
        ConsoleOutputWriter cow = new ConsoleOutputWriter();
        
//a        thread.addJob(cow.getOutputUtil());
        
        displaySerialPorts();
        
        OneTouchUltra otu = new OneTouchUltra(portName, cow);
        //m_meter = new OneTouchUltra(portName, cow);
        otu.loadInitialData();

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
    

    public void readyToWrite(String str)
    {
	try
	{
	    this.m_meter.portOutputStream.write(str.getBytes());
	}
	catch(Exception ex)
	{
	    System.out.println("Tester -> Error writing to meter: " + ex);
	}
    }


    public static void main(String args[])
    {
	try
	{
	    
	    if (args.length == 0)
	        new MeterConsoleTester("");
	    else
	        new MeterConsoleTester(args[0]);

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
