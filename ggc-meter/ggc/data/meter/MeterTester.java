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


package ggc.data.meter;

import ggc.meter.device.ascensia.AscensiaContourMeter;
import ggc.meter.output.GGCFileOutputWriter;

import java.awt.TextArea;

import javax.swing.JFrame;

import com.atech.utils.TimeZoneUtil;
import com.atech.utils.TimerThread;


public class MeterTester extends JFrame
{
	static final long serialVersionUID = 0;

	
//    private JButton openButton, sendButton, enterButton, clearButton;
//    private JTextField textField, textField2, textField3;

    public static TextArea messageArea;

    private AscensiaContourMeter m_meter;
    //private SerialProtocol m_meter;

    public MeterTester()
    {
    	
    	TimeZoneUtil  tzu = TimeZoneUtil.getInstance();
    	
		tzu.setTimeZone("Europe/Prague");
		tzu.setWinterTimeChange(0);
		tzu.setSummerTimeChange(+1);
    	
        
		TimerThread thread = new TimerThread();
	    thread.start();
	    
    	try
    	{
    		GGCFileOutputWriter gfo = new GGCFileOutputWriter();
    		
    		thread.addJob(gfo.getOutputUtil());
    		
    		
    		m_meter = new AscensiaContourMeter("COM9", gfo);
    	    //new AscensiaContourMeter("COM9");
    	    m_meter.setPort("COM9");
    	    //m_meter.open();
    
    	    m_meter.loadInitialData();
    	    
//    	    m_meter.readDeviceData();
    
    //	    m_meter.readCommData();
    	    //m_meter.getTimeDifference();
    
    	    //m_meter.test(); 
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
	    new MeterTester();

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
