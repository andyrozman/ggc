package ggc.data.meter.device;

import ggc.data.meter.MeterManager;
import gnu.io.SerialPortEvent;

import java.util.ArrayList;


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
 *  Filename: AscensiaBreezeMeter.java
 *  Purpose:  This class is used for data retrival from Ascensia Breeze Meter and
 *            implements SerialProtocol.
 *
 *  Author:   andyrozman {andyrozman@sourceforge.net}
 *
 */

public class AscensiaBreezeMeter extends AscensiaMeter //SerialProtocol
{

    private int m_status = 0;

    public AscensiaBreezeMeter(String portName)
    {
	super(MeterManager.METER_ASCENSIA_BREEZE, portName);

	/*
	super(MeterManager.METER_ASCENSIA_BREEZE,
	      9600, 
	      SerialPort.DATABITS_8, 
	      SerialPort.STOPBITS_1, 
	      SerialPort.PARITY_NONE);
	*/
	/*
	try
	{
	    System.out.println("AscensiaMeter -> Adding event listener");
	    serialPort.addEventListener(this);
	}
	catch(Exception ex)
	{
	    System.out.println("AscensiaMeter -> Error adding listener: " + ex);
	}
	*/
    }

    /**
     * Used for opening connection with device.
     * @return boolean - if connection established
     */
    @Override
    public boolean open() throws MeterException
    {
	return super.open();
	//return false;
    }


    /**
     * Will be called, when the import is ended and freeing resources.
     */
    @Override
    public void close()
    {
	return;
    }




    /**
     * getTimeDifference - returns time difference between Meter and Computer
     */
    public int getTimeDifference()
    {



	return 0;
    }


    /**
     * getInfo - returns Meter information
     */
    public String getInfo()
    {
	return "No Data";
    }


    /**
     * getStatus - get Status of meter
     */
    public int getStatus()
    {
	return 0;
    }


    /**
     * isStatusOK - has Meter OK status
     */
    public boolean isStatusOK()
    {
	return (m_status == 0);
    }


    /**
     * getDataFull - get all data from Meter
     */
    public ArrayList getDataFull()
    {
	return null;
    }


    /**
     * getData - get data for specified time
     */
    public ArrayList getData(int from, int to)
    {
	return null;
    }

    


    public void test()
    {
	try
	{
	    String data = "R|D|8E\n";
	    this.portOutputStream.write(data.getBytes());
	}
	catch(Exception ex)
	{

	}
	/*
	byte[] data = { (byte)"R",  
			(byte)"|",
			(byte)"D",
			(byte)"|",
			(byte)"8",
			(byte)"E",
			13
	};
	*/
    }

    /*
    public byte getByte(String input)
    {

	
    }*/

    public static final int MODE_ENQ = 1;
    public static final int MODE_OUT = 2;
    public static final int MODE_ACK = 3;
    public static final int MODE_NAK = 4;
    public static final int MODE_EOT = 5;

    public int mode = 0;


    public static final int METER_ENQ_READ = 1;
    public static final int METER_ENQ_WRITE = 2;


    public void test2()
    {
	writeToMeter(1, "d", null);
    }


    

    public void writeToMeter(int type, String cmd1, String cmd2)
    {

	/*

	// read everything
	while(mode != MODE_ENQ)
	    waitTime(100);

	writePort(5);  // ENQ

	waitTime(1000); 

	if (mode == MODE_EOT)
	{
	    writePort(5);  // ENQ

	    waitTime(1000); 
	}

	//writePort(6);  // ACK

	
	while (mode != MODE_EOT)
	{
	    writePort(6);  // ACK
	    waitTime(500);
	}
	*/
	

	// commands

	

	writePort(21);  // NAK
	waitTime(500);

	writePort(5);  // ENQ
	waitTime(1000);

	//while(mode != MODE_ACK)
	//    waitTime(100);

	writePort("R|");

	waitTime(500);

	writePort("D|");

	waitTime(500);

	System.out.println("Received Text: " + this.receivedText);

	//Enumeration en = new Enumerator(type);
	//en.hasMoreElements()
	//en.nextElement();
/*
	waitTime(1000); 

	if (mode == MODE_EOT)
	{
	    writePort(5);  // ENQ

	    waitTime(1000); 
	}
*/
	/*
	writePort("R|D|");
	writePort(13);
*/
	
 /*       if (type==1)
	{
	    writePort("r");
	    writePort(13);
	}
	else
	    writePort("w");

	waitTime(500);

	System.out.println("Return after read/write " + getModeString());

	System.out.println("Command 1: " + cmd1);

	//this.portOutputStream.write(
	writePort(cmd1 );

	System.out.println("Return after cmd1 " + getModeString());
   */ 
	
	/*
	//writePort(6);  // ACK


	while (mode != MODE_EOT)
	{
	    writePort(6);  // ACK
	    waitTime(500);
	}
*/


	/*
	while(mode == AscensiaMeter.MODE_OUT)
	{
	    waitTime(100);
	}
	*/
/*
	if (mode == AscensiaMeter.MODE_ENQ)
	{
	    writePort((byte)6); // ACK
	    waitTime(500);
	}
*/
	/*
	if (mode == AscensiaMeter.MODE_ENQ)
	{
	    writePort((byte)5); // ENQ
	    waitTime(500);
	}

	System.out.println("Return after ENQ: " + getModeString());

	System.out.println("Mode " );

	if (type==1)
	    writePort("R|");
	else
	    writePort("W|");

	waitTime(500);

	System.out.println("Return after read/write " + getModeString());

	System.out.println("Command 1: " + cmd1);

	//this.portOutputStream.write(
	writePort(cmd1 + "|");

	System.out.println("Return after cmd1 " + getModeString());


	if (cmd2!=null)
	{
	    waitTime(500);
	    System.out.println("Command 2: " + cmd1);

	    //this.portOutputStream.write(
	    writePort(cmd2 + "|");

	}
*/	


    }

    private String getModeString()
    {
	String[] modes = { "None", "ENQuiry", "Out", "ACKnowledge", "Negative AcKnowledge", "End Of Transmition"
	};

	return modes[mode];

    }


    private void writePort(String input)
    {
	writePort(getBytes(input));
    }

    private void writePort(int input)
    {
	byte[] b = new byte[1];
	b[0] = (byte)input;
	writePort(b);
    }


    public String receivedText = "";

    private void writePort(byte[] input)
    {
	try
	{
	    this.portOutputStream.write(input);
	}
	catch(Exception ex)
	{
	    System.out.println("Error writing to Serial: "+ ex);
	}
    }

    private byte[] getBytes(String inp)
    {
	
	return inp.getBytes();
    }


    public void waitTime(long time)
    {
	try
	{
	    Thread.sleep(time);

	}
	catch(Exception ex)
	{
	}
    }


    @Override
    public void serialEvent(SerialPortEvent event)
    {


	//System.out.println();

	// Determine type of event.
	switch (event.getEventType()) 
	{
	    case SerialPortEvent.DATA_AVAILABLE:
		{
		    int newData = 0;
		    receivedText = "";
		    try
		    {
			while ((newData=portInputStream.read())!=-1)
			{
			    switch (newData)
			    {
				case 6:
				    System.out.println("<ACK>");
				    mode = AscensiaMeter.MODE_ACK;
				    break;

				case 13:
				    System.out.println("<CR>");
				    break;

				case 5:
				    System.out.println("<ENQ>");
				    mode = AscensiaMeter.MODE_ENQ;
				    //this.portOutputStream.write(6);
				    break;
				case 4:
				    System.out.println("<EOT>");
				    mode = AscensiaMeter.MODE_EOT;
				    break;

				case 23:
				    System.out.println("<ETB>");
				    break;

				case 3:
				    System.out.println("<ETX>");
				    break;

				case 10:
				    System.out.println("<LF>");
				    break;

				case 21:
				    System.out.println("<NAK>");
				    mode = AscensiaMeter.MODE_NAK;
				    break;

				case 2:
				    System.out.println("<STX>");
				    break;

				default:
				    {
					System.out.print((char)newData);
					receivedText += (new Character((char)newData)).toString();
				    }
			    }
			    //inputBuffer.append((char)newData);
			    //System.out.print((char)newData);
			}
		    }
		    catch(Exception ex)
		    {
			System.out.println("Exception:" + ex);
		    }

		    //System.out.print(newData + " ");
/*
		    dataFromMeter = true;

		    System.out.println("Data");

		    timeOut += 5000;
*/
                } break;


		// If break event append BREAK RECEIVED message.
	    case SerialPortEvent.BI:
		System.out.println("recievied break");
		break;
	    case SerialPortEvent.CD:
		System.out.println("recievied cd");
		break;
	    case SerialPortEvent.CTS:
		System.out.println("recievied cts");
		break;
	    case SerialPortEvent.DSR:
		System.out.println("recievied dsr");
		break;
	    case SerialPortEvent.FE:
		System.out.println("recievied fe");
		break;
	    case SerialPortEvent.OE:
		System.out.println("recievied oe");
		break;
	    case SerialPortEvent.PE:
		System.out.println("recievied pe");
		break;
	    case SerialPortEvent.RI:
		System.out.println("recievied ri");
		break;
	}
    }

    public void loadInitialData()
    {
        // TODO Auto-generated method stub
        
    } 


}
