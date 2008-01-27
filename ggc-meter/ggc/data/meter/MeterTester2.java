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

import ggc.meter.protocol.SerialConnection;

import java.awt.TextArea;

import javax.swing.JFrame;

public class MeterTester2 extends JFrame
{

	static final long serialVersionUID = 0;
	
    //private JButton openButton, sendButton, enterButton, clearButton;
    //private JTextField textField, textField2, textField3;

    public static TextArea messageArea;


    public MeterTester2()
    {
        try
        {
            System.out.println("Create SerialConnection");
            //AscensiaContourMeter acm = new AscensiaContourMeter("");
            SerialConnection sc = new SerialConnection();

            String[] av = SerialConnection.getAvailablePorts();

            for(int i=0; i<av.length; i++)
            {
                System.out.println(av[i]);
            }

            System.out.println("Set Comm Port !");
            sc.setPortName("COM9");

            System.out.println("Open Connection");
            sc.openConnection();

            System.out.println("Send string");
            sc.sendString("R|C|\r");
            

            String s1 = sc.receiveString();

            System.out.println("Receive string");
            System.out.println(s1);

        }
        catch(Exception ex)
        {
            System.out.println(ex);
        }


    }





    public static void main(String args[])
    {
	try
	{
	    new MeterTester2();

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
