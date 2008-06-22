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
 *  Filename: AscensiaDEXMeter.java
 *  Purpose:  This class is used for data retrival from Ascensia DEX Meter and
 *            implements SerialProtocol.
 *
 *  Author:   andyrozman {andyrozman@sourceforge.net}
 *
 */

import ggc.meter.data.MeterValuesEntry;
import ggc.meter.device.MeterException;
import ggc.meter.output.AbstractOutputWriter;
import gnu.io.SerialPortEvent;

import java.util.ArrayList;

import javax.swing.ImageIcon;

public class AscensiaDEX extends AscensiaMeter
// extends SerialProtocol
{

    
    public AscensiaDEX()
    {
    }

    
    
	private int m_status = 0;

	// private boolean stx = false;
	// private ArrayList raw_data = new ArrayList();

	public AscensiaDEX(String portName, AbstractOutputWriter writer) 
	{
		super(AscensiaMeter.METER_ASCENSIA_DEX, portName, writer);

		/*
		 * super(MeterManager.METER_ASCENSIA_DEX, 9600, SerialPort.DATABITS_8,
		 * SerialPort.STOPBITS_1, SerialPort.PARITY_NONE);
		 * 
		 * //this.setPort(portName);
		 * 
		 * 
		 * try { this.setPort(portName); this.open();
		 * //System.out.println("AscensiaMeter -> Adding event listener"); //
		 * serialPort.addEventListener(this); } catch(Exception ex) {
		 * System.out.println("AscensiaMeter -> Error adding listener: " + ex);
		 * ex.printStackTrace(); }
		 */
	}

	/**
	 * Used for opening connection with device.
	 * 
	 * @return boolean - if connection established
	 */
	@Override
	public boolean open() throws MeterException {
		return super.open();
		// return false;
	}

	/**
	 * Will be called, when the import is ended and freeing resources.
	 */
	@Override
	public void close() {
		return;
	}

	/**
	 * getTimeDifference - returns time difference between Meter and Computer
	 */
	public int getTimeDifference() {
		/*
		 * System.out.println("getTimeDifference: ");
		 * 
		 * writePort(21); // NAK waitTime(500); writePort(5); // ENQ
		 * waitTime(1000); writePort("R|"); waitTime(500); writePort("D|");
		 * waitTime(500);
		 * 
		 * System.out.println("Received Text: " + this.receivedText);
		 * 
		 * writePort(21); // NAK waitTime(500); writePort(5); // ENQ
		 * waitTime(1000); writePort("R|"); waitTime(500); writePort("T|");
		 * waitTime(500);
		 * 
		 * System.out.println("Received Text: " + this.receivedText);
		 */

		writeToMeter(1, "d", null);
		// test();
		return 0;
	}

	public void readCommData() {
	}

	/**
	 * getInfo - returns Meter information
	 */
	public String getInfo() {
		return "No Data";
	}

	/**
	 * getStatus - get Status of meter
	 */
	public int getStatus() {
		return 0;
	}

	/**
	 * isStatusOK - has Meter OK status
	 */
	public boolean isStatusOK() {
		return (m_status == 0);
	}

	/**
	 * getDataFull - get all data from Meter
	 */
	public ArrayList<MeterValuesEntry> getDataFull() {
		return null;
	}

	/**
	 * getData - get data for specified time
	 */
	public ArrayList<MeterValuesEntry> getData(int from, int to) {
		return null;
	}

	public void test() {
		try {
			String data = "R|D|8E\n";
			this.portOutputStream.write(data.getBytes());
		} catch (Exception ex) {

		}
		/*
		 * byte[] data = { (byte)"R", (byte)"|", (byte)"D", (byte)"|",
		 * (byte)"8", (byte)"E", 13 };
		 */
	}

	/*
	 * public byte getByte(String input) {
	 * 
	 *  }
	 */

	public static final int MODE_ENQ = 1;
	public static final int MODE_OUT = 2;
	public static final int MODE_ACK = 3;
	public static final int MODE_NAK = 4;
	public static final int MODE_EOT = 5;

	public int mode = 0;

	public static final int METER_ENQ_READ = 1;
	public static final int METER_ENQ_WRITE = 2;

	public void test2() {
		writeToMeter(1, "d", null);
	}

	public void writeToMeter(int type, String cmd1, String cmd2) {

		// /*

		// read everything
		while (mode != MODE_ENQ)
			waitTime(100);

		while (mode != MODE_ENQ)
			waitTime(100);

		writePort(6); // ACK

		/*
		 * while(mode == MODE_EOT) { waitTime(500); }
		 */

		if (mode == MODE_EOT) {

		}

		/*
		 * writePort(5); // ENQ waitTime(1000);
		 * 
		 * if (mode == MODE_EOT) { writePort(5); // ENQ waitTime(1000); }
		 * 
		 * //writePort(6); // ACK
		 */

		while (mode != MODE_EOT) {
			writePort(6); // ACK
			waitTime(500);
		}
		// */

		// commands

		/*
		 * writePort(21); // NAK waitTime(500);
		 * 
		 * writePort(5); // ENQ waitTime(1000);
		 * 
		 * //while(mode != MODE_ACK) // waitTime(100);
		 * 
		 * writePort("R|"); waitTime(500);
		 * 
		 * writePort("D|"); waitTime(500);
		 * 
		 * System.out.println("Received Text: " + this.receivedText);
		 * 
		 * //Enumeration en = new Enumerator(type); //en.hasMoreElements()
		 * //en.nextElement();
		 * 
		 *  /* waitTime(1000);
		 * 
		 * if (mode == MODE_EOT) { writePort(5); // ENQ
		 * 
		 * waitTime(1000); }
		 */
		/*
		 * writePort("R|D|"); writePort(13);
		 */

		/*
		 * if (type==1) { writePort("r"); writePort(13); } else writePort("w");
		 * 
		 * waitTime(500);
		 * 
		 * System.out.println("Return after read/write " + getModeString());
		 * 
		 * System.out.println("Command 1: " + cmd1);
		 * 
		 * //this.portOutputStream.write( writePort(cmd1 );
		 * 
		 * System.out.println("Return after cmd1 " + getModeString());
		 */

		/*
		 * //writePort(6); // ACK
		 * 
		 * 
		 * while (mode != MODE_EOT) { writePort(6); // ACK waitTime(500); }
		 */

		/*
		 * while(mode == AscensiaMeter.MODE_OUT) { waitTime(100); }
		 */
		/*
		 * if (mode == AscensiaMeter.MODE_ENQ)
		 * 
		 * writePort((byte)6); // ACK waitTime(500); }
		 */
		/*
		 * if (mode == AscensiaMeter.MODE_ENQ) { writePort((byte)5); // ENQ
		 * waitTime(500); }
		 * 
		 * System.out.println("Return after ENQ: " + getModeString());
		 * 
		 * System.out.println("Mode " );
		 * 
		 * if (type==1) writePort("R|"); else writePort("W|");
		 * 
		 * waitTime(500);
		 * 
		 * System.out.println("Return after read/write " + getModeString());
		 * 
		 * System.out.println("Command 1: " + cmd1);
		 * 
		 * //this.portOutputStream.write( writePort(cmd1 + "|");
		 * 
		 * System.out.println("Return after cmd1 " + getModeString());
		 * 
		 * 
		 * if (cmd2!=null) { waitTime(500); System.out.println("Command 2: " +
		 * cmd1);
		 * 
		 * //this.portOutputStream.write( writePort(cmd2 + "|");
		 *  }
		 */

	}
/*
	private String getModeString() {
		String[] modes = { "None", "ENQuiry", "Out", "ACKnowledge",
				"Negative AcKnowledge", "End Of Transmition" };

		return modes[mode];

	}
	*/
/*
	private void writePort(String input) 
	{
		writePort(getBytes(input));
	}
*/
	private void writePort(int input) {
		byte[] b = new byte[1];
		b[0] = (byte) input;
		writePort(b);
	}

	public String receivedText = "";

	protected void writePort(byte[] input) {
		try {
			this.portOutputStream.write(input);
		} catch (Exception ex) {
			System.out.println("Error writing to Serial: " + ex);
		}
	}

	protected byte[] getBytes(String inp) {
		return inp.getBytes();
	}

	public void waitTime(long time) {
		try {
			Thread.sleep(time);
		} catch (Exception ex) {
		}
	}

	private int cr_lf = 0;

	@Override
	public void serialEvent(SerialPortEvent event) {

		// System.out.println();

		// Determine type of event.
		switch (event.getEventType()) {
		case SerialPortEvent.DATA_AVAILABLE: {
			int newData = 0;
			receivedText = "";
			try {
				while ((newData = portInputStream.read()) != -1) {
					switch (newData) {
					case 6:
						System.out.println("<ACK>");
						mode = AscensiaMeter.MODE_ACK;
						cr_lf = 0;
						break;

					case 13:
						// System.out.println("<CR>");
						cr_lf = 1;
						break;

					case 5:
						System.out.println("<ENQ>");
						mode = AscensiaMeter.MODE_ENQ;
						cr_lf = 0;
						// this.portOutputStream.write(6);
						break;
					case 4:
						System.out.println("<EOT>");
						mode = AscensiaMeter.MODE_EOT;
						cr_lf = 0;
						break;

					case 23: // etb
					case 3: // etx
						// System.out.println("<ETB>");
						stx = false;
						this.receivedText += "<ETB>";
						// System.out.println("recivedText: " +
						// this.receivedText);
						break;

					// case 3:
					// System.out.println("<ETX>");
					// break;

					case 10:
						if (cr_lf == 1) {
							System.out.println("recivedLine: "
									+ this.receivedText);
						}
						cr_lf = 0;
						// System.out.println("<LF>");
						// System.out.println("<EOT>");
						break;

					case 21:
						System.out.println("<NAK>");
						mode = AscensiaMeter.MODE_NAK;
						cr_lf = 0;
						break;

					case 2:
						// System.out.println("<STX>");
						cr_lf = 0;
						stx = true;
						break;

					default: {
						// System.out.print((char)newData);
						receivedText += (new Character((char) newData))
								.toString();
					}
					}
					// inputBuffer.append((char)newData);
					// System.out.print((char)newData);
				}
			} catch (Exception ex) {
				System.out.println("Exception:" + ex);
			}

			// System.out.print(newData + " ");
			/*
			 * dataFromMeter = true; System.out.println("Data"); timeOut +=
			 * 5000;
			 */
		}
			break;

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

	public void loadInitialData() {
		// TODO Auto-generated method stub

	}

	// ************************************************
	// *** Available Functionality ***
	// ************************************************

	/**
	 * canReadData - Can Meter Class read data from device
	 */
	public boolean canReadData() {
		return true;
	}

	/**
	 * canReadPartitialData - Can Meter class read (partitial) data from device,
	 * just from certain data
	 */
	public boolean canReadPartitialData() {
		return false;
	}

	/**
	 * canClearData - Can Meter class clear data from meter.
	 */
	public boolean canClearData() {
		return false;
	}

	
	
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
        return "DEX";
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
        return "ascensia_dex2.png";
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
        return "INSTRUCTIONS_ASCENSIA_DEX";
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
	
	
    public String getDeviceClassName()
    {
        return "ggc.meter.device.ascensia.AscensiaDEX";
    }

    
    
	
}
