package ggc.meter.device.ascensia;



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
 *  Filename: AscensiaContourMeter.java
 *  Purpose:  This class is used for data retrival from Ascensia Contour Meter and
 *            implements SerialProtocol.
 *
 *  Author:   andyrozman {andyrozman@sourceforge.net}
 *
 */

import ggc.data.meter.MeterManager;
import ggc.meter.device.MeterException;
import ggc.meter.output.OutputWriter;
import gnu.io.SerialPortEvent;
import gnu.io.SerialPortEventListener;

import java.io.IOException;

import javax.swing.ImageIcon;


public class AscensiaContour extends AscensiaMeter implements SerialPortEventListener
{

//	OutputWriter writer;
	String end_string;
	
	String end_strings[] = null;
	
	String text_def[] = null;

	
	
    
	
	
    public AscensiaContour()
    {
    }


    public AscensiaContour(String portName, OutputWriter writer)
    {
    	super(MeterManager.METER_ASCENSIA_CONTOUR, portName, writer);
        
    	end_string = (new Character((char)13)).toString();
    	
    	//this.writer = new GGCFileOutputWriter();
		this.m_output_writer.writeHeader();

		this.setMeterType("Ascensia", "Contour");
	
		
		//this.serialPort.
		
		this.serialPort.notifyOnOutputEmpty(true);
		this.serialPort.notifyOnBreakInterrupt(true);
		
		
		this.end_strings = new String[2];
        end_strings[0] = (new Character((char)3)).toString(); // ETX - End of Text
        end_strings[1] = (new Character((char)4)).toString(); // EOT - End of Transmission
        //end_strings[2] = (new Character((char)23)).toString(); // ETB - End of Text
        
        this.text_def = new String[3];
        this.text_def[0] = (new Character((char)2)).toString(); // STX - Start of Text
        this.text_def[1] = (new Character((char)3)).toString(); // ETX - Start of Text
        this.text_def[2] = (new Character((char)13)).toString(); // EOL - Start of Text
        
		
		try
		{
		    this.serialPort.addEventListener(this);
		}
		catch(Exception ex)
		{
		    System.out.println(ex);
		}
		
		
    }

    public void loadInitialData_2()
    {
    	writePort(5);
    	writePort(5);
    	writePort(5);
    	writePort(5);
    	
    }
    
    public boolean device_running = false;

    public void loadInitialData()
    {
    	System.out.println("loadInitionalData");

    	waitTime(5000);
    	
    	try
    	{

//    		this.write("X".getBytes());
//    		System.out.println(this.readLine());
    		

    		this.device_running = true;
    		
    		
    		write(5);  // ENQ
    		waitTime(1000); 
   		    writePort(6);  // ACK
    		
    		String line;

    		System.out.println("loadInitialData()::");
    		
    		
    		
    		while (((line = this.readLine()) != null) && (!isDeviceStopped(line)))
    		{
    			//System.out.println(line);
    			//processData(line.trim());
    			sendToProcess(line);
    			write(6);
    		}
    	
    	}
    	catch(Exception ex)
    	{
    		System.out.println("Exception: " + ex);
    		ex.printStackTrace();
    		
    	}
    	/*
    	getData(6);
    	
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
			//System.out.println("Write test");
			//writePort("\rR|");
		    waitTime(500);

		    
		    //System.out.println("Data: " + this.receivedText);
		    processData(this.receivedText);
		}
*/
    }

    
    private void sendToProcess(String text)
    {
        boolean stx = false;
        int stx_idx = 0;
        boolean etx = false;
        int etx_idx = 0;
        boolean eol = false;
        int eol_idx = 0;
        
        
        if ((stx_idx = text.indexOf(this.text_def[0])) > -1)
        {
            //System.out.println("STX");
            stx = true;
        }
        
        if ((etx_idx = text.indexOf(this.text_def[1])) > -1)
        {
            //System.out.println("ETX");
            etx = true;
        }
    
        if ((eol_idx = text.indexOf(this.text_def[2])) > -1)
        {
            //System.out.println("EOL");
            eol = true;
        }
        
        if ((stx) && ((etx) || (eol)))
        {
            String t;
            stx_idx++;
            
            if (etx)
            {
                t = text.substring(stx_idx, etx_idx);
                
            }
            else
            {
                t = text.substring(stx_idx, eol_idx);
                
            }
            System.out.println(t);
            this.processData(t);
        }
        
    }
    

    private boolean isDeviceStopped(String vals)
    {
        if (!this.device_running)
            return true;
        
        //if (vals.contains(this.end_string))
        //    System.out.println("EOL found");
        
        if (vals.contains(this.end_strings[0]))
            System.out.println("ETX");
        
        if (vals.contains(this.end_strings[1]))
        {
            System.out.println("EOT");
            
            System.out.println("writter: " + this.m_output_writer);
            System.out.println("writter output util: " + this.m_output_writer.getOutputUtil());
            
            if (this.m_output_writer.getOutputUtil().hasTimerStarted())
            {
                this.m_output_writer.endOutput();
            }
            System.out.println("EOT");
        }
        
        
        
        /*
        if (vals.contains(this.end_string))
        {
            return true;
            
        }
        else
            return false;*/
        
        return false;
        
    }
    
    
    public void setDeviceStopped()
    {
        this.device_running = false;
        this.m_output_writer.endOutput();
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


    
    
    
    

    public String getInfo()
    {
        try
        {
            System.out.println("getInfo");

            String data = "R|D|8E\n";
            this.portOutputStream.write(data.getBytes());

            while(portInputStream.available()>0)
            {
                System.out.println(portInputStream.read());
            }
            return "data. not converted.";
        }
        catch(IOException ex)
        {
            System.out.println("getInfo. Ex: " + ex);
            return "no data";
        }
        //this.portInputStream
    }


/*
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
  //  }

    public void test()
    {
        writeToMeter(1, "", "");
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


    @Override
    public void test2()
    {
	writeToMeter(1, "d", null);
    }


    

    @Override
    public void writeToMeter(int type, String cmd1, String cmd2)
    {

	

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
		
	
	
/*
		// commands
	
		
		
	
		writePort(21);  // NAK
		waitTime(500);
	
		writePort(5);  // ENQ
		waitTime(1000);
	
		while(mode != MODE_ACK)
		    waitTime(100);
	
		writePort("R|");
	
		waitTime(500);
	
		writePort("D|");
	
		waitTime(500);
		waitTime(1000);
	
		System.out.println("Received Text: " + this.receivedText);
	
		//Enumeration en = new Enumerator(type);
		//en.hasMoreElements()
		//en.nextElement();
	 */
	 
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
	
 /*       
	    if (type==1)
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

    /*
    private String getModeString()
    {
	String[] modes = { "None", "ENQuiry", "Out", "ACKnowledge", "Negative AcKnowledge", "End Of Transmition"
	};

	return modes[mode];

    }
*/

    private void writePort(String input)
    {
        writePort(getBytes(input));
    }

    private void writePort(int input)
    {
    	System.out.println("WritePort: " + input);
    	byte[] b = new byte[1];
    	b[0] = (byte)input;
    	writePort(b);
    }


    public String receivedText = "";

    protected void writePort(byte[] input)
    {
    	System.out.println("WritePort: " + input);
	try
	{
		this.write(input);
	    //this.portOutputStream.write(input);
    	System.out.println("WritePort: " + input);
	}
	catch(Exception ex)
	{
	    System.out.println("Error writing to Serial: "+ ex);
	}
    }

    protected byte[] getBytes(String inp)
    {
        return inp.getBytes();
    }




    @Override
    public void serialEvent(SerialPortEvent event)
    {

    	//System.out.println("InEvent");

	boolean debug = false;

	//System.out.println();

	// Determine type of event.
	switch (event.getEventType()) 
	{
	/*
		case SerialPortEvent.DATA_AVAILABLE:
		{
			String newData; 
			try
			{
				while ((newData=this.readLine())!=null)
				{
					System.out.println(newData);
				}
			}
			catch(Exception ex)
			{
				System.out.println("Error reading: " + ex);
			}
			
		} break;
	*/
	    case SerialPortEvent.DATA_AVAILABLE:
		{
			/*
			try
			{
				int newData = 2;
				byte[] bt = new byte[1024];
			//portInputStream.read(bt);

				while ((newData=portInputStream.read(bt))>0)
				{
					System.out.println(bt);
				}
			}
			catch(Exception ex)
			{
				System.out.println("Exception trown: " + ex);
				
			}
			*/	
				
			
			//System.out.println(bt);
			
	    	
		    int newData = 0;
		    receivedText = "";
		    try
		    {

		    	//portInputStream.r
		    	//portInputStream.
			while ((newData=this.read())!=-1)
			{
			    switch (newData)
			    {
				case 6:
				    if (debug)
					System.out.println("<ACK>");
				    mode = AscensiaMeter.MODE_ACK;
				    break;

				case 13:
				    if (debug)
					System.out.println("<CR>");
				    break;

				case 5:
				    if (debug)
					System.out.println("<ENQ>");
				    mode = AscensiaMeter.MODE_ENQ;
				    //this.portOutputStream.write(6);
				    break;
				case 4:
				    if (debug)
					System.out.println("<EOT>");
				    mode = AscensiaMeter.MODE_EOT;
				    break;

				case 23:
				    if (debug)
					System.out.println("<ETB>");
				    	stx = false;
				    break;

				case 3:
				    if (debug)
				    	System.out.println("<ETX>");
				    stx = false;
				    break;

				case 10:
				    if (debug)
				    	System.out.println("<LF>");
				    break;

				case 21:
				    if (debug)
					System.out.println("<NAK>");
				    mode = AscensiaMeter.MODE_NAK;
				    break;

				case 2:
				{
				    if (debug)
				    	System.out.println("<STX>");
				    stx = true;
				    
				    this.readChucksOfData2();
				    
				    break;
				}

				default:
				    {
					if (stx)
					{
					    this.readChucksOfData2();
						
						//this.readChucksOfData();
						/*
					    if (debug)
					    	System.out.print((char)newData);

					    receivedText += (new Character((char)newData)).toString();
					    */
					}
					//System.out.print((char)newData);
					//receivedText += (new Character((char)newData)).toString();
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
	        setDeviceStopped();
		//this.device_running = false;
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
        System.out.println("Output Empty");

		break;
	    case SerialPortEvent.PE:
		System.out.println("recievied pe");
		break;
	    case SerialPortEvent.RI:
		System.out.println("recievied ri");
		break;
	}
    } 


    public void readChucksOfData()
    {
		try
		{
			int newData = 2;
			byte[] bt = new byte[51200];
		//portInputStream.read(bt);

			while ((newData=portInputStream.read(bt))>0)
			{
				System.out.println(bt);
				this.write(6);
			}
		}
		catch(Exception ex)
		{
			System.out.println("Exception trown: " + ex);
			
		}
    	
    }

    
    
    public void readChucksOfData2()
    {
		try
		{
			String nd;
			while ((nd=this.readLine())!=null)
			{
				System.out.println(nd);
			}
		}
		catch(Exception ex)
		{
			System.out.println("Exception trown: " + ex);
			
		}
    	
    }
    
    
    public int getTimeDifference()
    {
	writePort(21);  // NAK
	waitTime(500);
	writePort(5);  // ENQ
	waitTime(1000);
	writePort("R|");
	waitTime(500);
	writePort("D|");
	waitTime(500);

	System.out.println("Received Text: " + this.receivedText);

	writePort(21);  // NAK
	waitTime(500);
	writePort(5);  // ENQ
	waitTime(1000);
	writePort("R|");
	waitTime(500);
	writePort("T|");
	waitTime(500);

	System.out.println("Received Text: " + this.receivedText);



	return 0;
    }



    //************************************************
    //***        Available Functionality           ***
    //************************************************


    
    //public void getData(int x)
    
    
    //************************************************
    //***      Meter Identification Methods        ***
    //************************************************


    /**
     * getName - Get Name of meter. 
     * 
     * @return name of meter
     */
    public String getName()
    {
        return "name";
    }


    /**
     * getIcon - Get Icon of meter
     * Should be implemented by meter class.
     * 
     * @return ImageIcon
     */
    public ImageIcon getIcon()
    {
        return null;
    }

    
    /**
     * getIconName - Get Icon of meter
     * 
     * @return icon name
     */
    public String getIconName()
    {
        return null;
    }
    

    /**
     * getMeterId - Get Meter Id, within Meter Company class 
     * 
     * @return id of meter within company
     */
    public int getMeterId()
    {
        return 0;
    }

    
    /**
     * getGlobalMeterId - Get Global Meter Id, within Meter Company class 
     * 
     * @return global id of meter
     */
    public int getGlobalMeterId()
    {
        return 0;
    }

    
    /**
     * getCompanyId - Get Company Id 
     * 
     * @return id of company
     */
    public int getCompanyId()
    {
        return 0;
    }
    
    
    /**
     * getInstructions - get instructions for device
     * 
     * @return instructions for reading data 
     */
    public String getInstructions()
    {
        return null;
    }
    
    /**
     * getComment - Get Comment for device 
     * 
     * @return comment or null
     */
    public String getComment()
    {
        return null;
    }
    
    
    /**
     * getImplementationStatus - Get Company Id 
     * 
     * @return implementation status as number
     * @see ggc.meter.manager.MeterImplementationStatus
     */
    public int getImplementationStatus() 
    {
        return 0;
    }



}
