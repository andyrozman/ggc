package ggc.meter.device.onetouch;

import ggc.meter.data.MeterValuesEntry;
import ggc.meter.device.AbstractSerialMeter;
import ggc.meter.manager.company.LifeScan;
import ggc.meter.util.DataAccessMeter;
import ggc.plugin.device.PlugInBaseException;
import ggc.plugin.manager.DeviceImplementationStatus;
import ggc.plugin.manager.company.AbstractDeviceCompany;
import ggc.plugin.output.OutputUtil;
import ggc.plugin.output.OutputWriter;
import ggc.plugin.protocol.SerialProtocol;
import gnu.io.SerialPort;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import com.atech.utils.ATechDate;
import com.atech.utils.TimeZoneUtil;

/**
 *  Application:   GGC - GNU Gluco Control
 *  Plug-in:       Meter Tool (support for Meter devices)
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
 *  Filename:     OneTouchUltraSmart
 *  Description:  Support for LifeScan OneTouch Ultra Smart Meter
 * 
 *  Author: Alex {abalaban1@yahoo.ca}
 */

// in works
public class OneTouchUltraSmart extends AbstractSerialMeter
{
    /**
     * This is CRC-7 polynomial lookup table for computing checksum byte for OneTouch UltraSmart meter.
     */
    static byte[] lookupArr = new byte[] { 0x00, 0x1a, 0x34, 0x2e, 0x68, 0x72, 0x5c, 0x46, 0x5d, 0x47, 0x69, 0x73, 0x35,
                                    0x2f, 0x01, 0x1b, 0x37, 0x2d, 0x03, 0x19, 0x5f, 0x45, 0x6b, 0x71, 0x6a, 0x70,
                                    0x5e, 0x44, 0x02, 0x18, 0x36, 0x2c, 0x6e, 0x74, 0x5a, 0x40, 0x06, 0x1c, 0x32,
                                    0x28, 0x33, 0x29, 0x07, 0x1d, 0x5b, 0x41, 0x6f, 0x75, 0x59, 0x43, 0x6d, 0x77,
                                    0x31, 0x2b, 0x05, 0x1f, 0x04, 0x1e, 0x30, 0x2a, 0x6c, 0x76, 0x58, 0x42, 0x51,
                                    0x4b, 0x65, 0x7f, 0x39, 0x23, 0x0d, 0x17, 0x0c, 0x16, 0x38, 0x22, 0x64, 0x7e,
                                    0x50, 0x4a, 0x66, 0x7c, 0x52, 0x48, 0x0e, 0x14, 0x3a, 0x20, 0x3b, 0x21, 0x0f,
                                    0x15, 0x53, 0x49, 0x67, 0x7d, 0x3f, 0x25, 0x0b, 0x11, 0x57, 0x4d, 0x63, 0x79,
                                    0x62, 0x78, 0x56, 0x4c, 0x0a, 0x10, 0x3e, 0x24, 0x08, 0x12, 0x3c, 0x26, 0x60,
                                    0x7a, 0x54, 0x4e, 0x55, 0x4f, 0x61, 0x7b, 0x3d, 0x27, 0x09, 0x13, 0x2f, 0x35,
                                    0x1b, 0x01, 0x47, 0x5d, 0x73, 0x69, 0x72, 0x68, 0x46, 0x5c, 0x1a, 0x00, 0x2e,
                                    0x34, 0x18, 0x02, 0x2c, 0x36, 0x70, 0x6a, 0x44, 0x5e, 0x45, 0x5f, 0x71, 0x6b,
                                    0x2d, 0x37, 0x19, 0x03, 0x41, 0x5b, 0x75, 0x6f, 0x29, 0x33, 0x1d, 0x07, 0x1c,
                                    0x06, 0x28, 0x32, 0x74, 0x6e, 0x40, 0x5a, 0x76, 0x6c, 0x42, 0x58, 0x1e, 0x04,
                                    0x2a, 0x30, 0x2b, 0x31, 0x1f, 0x05, 0x43, 0x59, 0x77, 0x6d, 0x7e, 0x64, 0x4a,
                                    0x50, 0x16, 0x0c, 0x22, 0x38, 0x23, 0x39, 0x17, 0x0d, 0x4b, 0x51, 0x7f, 0x65,
                                    0x49, 0x53, 0x7d, 0x67, 0x21, 0x3b, 0x15, 0x0f, 0x14, 0x0e, 0x20, 0x3a, 0x7c,
                                    0x66, 0x48, 0x52, 0x10, 0x0a, 0x24, 0x3e, 0x78, 0x62, 0x4c, 0x56, 0x4d, 0x57,
                                    0x79, 0x63, 0x25, 0x3f, 0x11, 0x0b, 0x27, 0x3d, 0x13, 0x09, 0x4f, 0x55, 0x7b,
                                    0x61, 0x7a, 0x60, 0x4e, 0x54, 0x12, 0x08, 0x26, 0x3C };

    protected TimeZoneUtil tzu = TimeZoneUtil.getInstance();
    
    
    /**
     * Constructor used by most classes
     * 
     * @param portName
     * @param writer
     */
    public OneTouchUltraSmart(String portName, OutputWriter writer)
    {
        //super(portName, writer);
        
        setCommunicationSettings( 
            38400,
            SerialPort.DATABITS_8, 
            SerialPort.STOPBITS_1, 
            SerialPort.PARITY_NONE,
            SerialPort.FLOWCONTROL_NONE, 
            SerialProtocol.SERIAL_EVENT_ALL);
        
     // output writer, this is how data is returned (for testing new devices, we can use Consol
        this.output_writer = writer; 
        this.output_writer.getOutputUtil().setMaxMemoryRecords(this.getMaxMemoryRecords());
        
        // set meter type (this will be deprecated in future, but it's needed for now
        this.setMeterType("LifeScan", this.getName());

        // set device company (needed for now, will also be deprecated)
        this.setDeviceCompany(new LifeScan());
        
        // settting serial port in com library
        try
        {
            this.setSerialPort(portName);
    
            if (!this.open())
            {
                this.m_status = 1;
                this.deviceDisconnected();
                return;
            }

            this.output_writer.writeHeader();
            
        }
        catch(Exception ex)
        {
            System.out.println("OneTouchMeter -> Error adding listener: " + ex);
            ex.printStackTrace();
        }
    }

    /**
     * Constructor
     */
    public OneTouchUltraSmart()
    {
        super();
    }

    /**
     * Constructor for device manager
     * 
     * @param cmp
     */
    public OneTouchUltraSmart(AbstractDeviceCompany cmp)
    {
        super(cmp);
    }
    
    /** 
     * Set Communication Settings
     */
    public void setCommunicationSettings(int baudrate, int databits, int stopbits, int parity, int flow_control, int event_type)
    {
        super.setCommunicationSettings(baudrate, databits, stopbits, parity, flow_control, event_type);
    }

    /**
     * getName - Get Name of meter.
     * 
     * @return name of meter
     */
    public String getName()
    {
        return "One Touch UltraSmart";
    }

    /**
     * getCompanyId - Get Company Id
     * 
     * @return id of company
     */
    public int getCompanyId()
    {
        return OneTouchMeter.LIFESCAN_COMPANY;
    }

    /**
     * getDeviceClassName - Get class name of device
     */
    public String getDeviceClassName()
    {
        return "ggc.meter.device.onetouch.OneTouchUltraSmart";
    }

    /**
     * getDeviceId - Get Device Id, within MgrCompany class Should be
     * implemented by device class.
     * 
     * @return id of device within company
     */
    public int getDeviceId()
    {
        return OneTouchMeter.METER_LIFESCAN_ONE_TOUCH_ULTRASMART;
    }

    /**
     * getIconName - Get Icon of meter
     * 
     * @return icon name
     */
    public String getIconName()
    {
        return "ls_ot_ultrasmart.jpg";
    }

    /**
     * getInstructions - get instructions for device
     * 
     * @return instructions for reading data 
     */
    public String getInstructions()
    {
        return "INSTRUCTIONS_LIFESCAN_ON";
    }
    
    
    /**
     * Maximum of records that device can store
     */
    public int getMaxMemoryRecords()
    {
        return 5000;
    }

    /**
     * getShortName - Get short name of meter.
     * 
     * @return short name of meter
     */
    public String getShortName()
    {
        return "UltraSmart";
    }
    
    /** 
     * getImplementationStatus
     */
    public int getImplementationStatus()
    {
        return DeviceImplementationStatus.IMPLEMENTATION_TESTING;
    }
    
    /** 
     * getComment
     */
    public String getComment()
    {
        return null;
    }
    
    /**
     * OT Ultasmart version of readInfo() overloaded method.
     */
    public void readInfo()
    {
        //DeviceIdentification di = this.output_writer.getDeviceIdentification();
        
        try
        {
            //this.output_writer.setSubStatus(ic.getMessage("READING_SERIAL_NR_SETTINGS"));
            
            // getting meter serial number
            write("D".getBytes());
            waitTime(100);
            write("M".getBytes());
            waitTime(100);
            write("@".getBytes());
            waitTime(100);
            byte eol[] = new byte[] { 0x0d, 0x0A };
            write(eol);
            waitTime(100);

            String line = this.readLine();
            System.out.println("Serial number: " + line);
            //di.device_serial_number = line;
        }
        catch (Exception ex)
        {
            System.out.println("Exception: " + ex);
            ex.printStackTrace();
        }
        finally
        {
            //this.output_writer.setSpecialProgress(4);
        }
    }
    
    /**
     * This is method for reading configuration
     * 
     * @throws PlugInBaseException
     */
    public void readConfiguration() throws PlugInBaseException
    {
    }

    /**
     * Reads dump of data from the OT UltraSmart device.
     * 
     * The interface is the combination of old LifeScan syntax, using DMx commands,
     * except that DMP does not work any more and it was replaced by
     * binary protocol. 
     * Records are followed by one byte checksum, using CRC-7 algorithm.
     * The binary protocol is NOT the same as for OT Mini, etc.
     * The binary data are BigEndian, or read from right to the left.
     */
    public void readDeviceDataFull()
    {
        
        readInfo();
        
        System.out.println("reading device data");
        
        List<byte[]> dataRecords = new ArrayList<byte[]>();

        try
        {
            // reading meter settings
            readInfo();
            
            // retrieving records from the meter
            short recNo = 0;
            int packetSize = 7;
            do
            {
                LinkedList<Byte> commList = new LinkedList<Byte>();
                commList.add((byte) 0x48);
                commList.add((byte) 0x52);
                // record number reversed
                commList.add((byte) 0x00); 
                commList.add((byte) recNo);
                // computing checksum
                byte checksum = calcCheckSum(commList);
                System.out.printf("computed checksum: %h \n", checksum);
                // add escape characters if needed
                commList = escape10s(commList);
                // adding STX
                commList.addFirst((byte) 0x02);
                commList.addFirst((byte) 0x10);
                // adding ETX
                commList.add((byte) 0x10);
                commList.add((byte) 0x03);
                // add checksum
                commList.add(checksum);
                // writing command to the port
                Byte[] command = commList.toArray(new Byte[0]);
                for (Byte b : command)
                {
                    write(b);
                }
//                byte eol[] = new byte[] { 0x0d, 0x0A };
//                write(eol);
                byte resp[] = this.readLineBytes();
                packetSize = resp.length;
                if (packetSize == 7)
                {
                    // do nothing - this is end of data
                }
                else if (packetSize < 17)
                {
                    System.out.println("received record no: " + recNo + " data packet too short, discarding");
                }
                else
                {
                    dataRecords.add(resp);
                }

                System.out.println("received record no: " + recNo + " data size: " + resp.length);
                
                recNo++;

            } while (packetSize != 7);

        }
        catch (Exception ex)
        {
            System.out.println("Exception: " + ex);
            ex.printStackTrace();
        }
       
        // now process all retrieved records and insert them to the database
        processRecords(dataRecords);
        
        System.out.println("done");
        
        this.output_writer.setSpecialProgress(100);
        this.output_writer.setSubStatus(null);
        this.output_writer.endOutput();
    }
    
    /**
     * This is method for reading partial data from device. All reading from actual device should be done from 
     * here. Reading can be done directly here, or event can be used to read data.
     */
    public void readDeviceDataPartitial() throws PlugInBaseException
    {
        
    }

    /**
     * This method processes the retrieved records
     * and writes them to the database.
     * 
     * @param records a list of records
     */
    public void processRecords(List<byte[]> records)
    {
        Iterator<byte[]> itr = records.iterator();
        short record = 0;
        
        while (itr.hasNext())
        {
            System.out.println("Processing record #" + record++);
            byte[] completeArr = itr.next();            
            // stripping the comm bytes and checksum byte
            //byte[] recordArr = Arrays.copyOfRange(completeArr, 2, completeArr.length-3);
            byte[] recordArr = new byte[completeArr.length-5];
            for(int i=2; i<completeArr.length-3; i++)
            {
                recordArr[i-2] = completeArr[i];
            }
            // stripping 0x10 escape characters.
            List<Byte> recordList = strip10s(recordArr);
            Byte[] recordArray = recordList.toArray(new Byte[0]);
            
            short typeByte = 0xFF; // masking the sign bit
            typeByte &= recordArray[10].byteValue();                        
            if (typeByte == 0x00)
            {
                System.out.printf("record type is: %h  - BG: ", typeByte);
                processBGRecord(recordArr);
            }
            else if (typeByte == 0x54 || typeByte == 0x50)
            {
                System.out.printf("record type is: %h  - Food\n", typeByte);
            }
            else if (typeByte == 0xb4)
            {
                System.out.printf("record type is: %h - Exercise\n", typeByte);
            }
            else if (typeByte == 0x2c)
            {
                System.out.printf("record type is: %h - Insulin\n", typeByte);
            }
            else
            {
                System.out.printf("record type is: %h - Unknown\n", typeByte);
            }
        }
        
    }
    
    /**
     * Processes the Blood Glucose data record
     * 
     * @param recordArray
     */
    private void processBGRecord(byte[] recordArray)
    {
        // Byte[] timeDateArr = Arrays.copyOfRange(recordArray, 2, 5);
        Byte[] timeDateArr = new Byte[4];
        // for(int i=2; i<=5; i++)
        // {
        // timeDateArr[i-2] = completeArr[i];
        // }
        for (int i = 2; i <= 4; i++)
        {
            timeDateArr[i - 2] = recordArray[i];
        }
        GregorianCalendar dateTime = (GregorianCalendar) parseDateTime(timeDateArr);
        byte bgByte = recordArray[5];
        int bgMg = bgByte & 0xFF;
        float bgMmol = OutputUtil.getInstance().getBGValueDifferent(OutputUtil.BG_MGDL, bgMg);
        //double bgMmol = ((double) bgMg) / 18.0;
        System.out.printf("%.1f", bgMmol);
        System.out.println(", timestamp: " + dateTime.getTime());

        // create GGC internal entry record
        MeterValuesEntry mve = new MeterValuesEntry();
        mve.setBgUnit(DataAccessMeter.BG_MGDL);
        // changed by andy
        //ATechDate atd = new ATechDate(ATechDate.FORMAT_DATE_AND_TIME_MIN, dateTime);
        //mve.setDateTimeObject(atd);
        
        mve.setDateTimeObject(tzu.getCorrectedDateTime(new ATechDate(dateTime.getTimeInMillis())));

        
        mve.setBgValue(Integer.toString(bgMg));
        
        this.output_writer.writeData(mve);

    }
    
    @SuppressWarnings("unused")
    private void processExerciseRecord(byte[] recordArray)
    {
        // J = Junk
        // D = Duration
        // T = Type
        // JJJJ JJJJ JJJJ JDDD DDDD DDTT
        // 0000 0000 0011 1111 1111 2222
        // 0123 4567 8901 2345 6789 0123
        
        // Create reverse order bitset from the arrat=y.
        //BitSet recordBits = new ExtendedBitSet(recordArray, true);
        
        // in bitset
        // T => 0,1;
        // D => 2-9;
    }
    
    
    /**
     * Computes the CRC checksum byte.
     * 
     * @param dataPacket
     * @return
     */
    private final byte calcCheckSum(List<Byte> dataPacket)
    {
        int crc = 0x7F;
        
        for (Byte dataByte : dataPacket)
        {
            byte b = (byte)dataByte.byteValue();
            crc = lookupArr[crc & 0xFF];
            crc ^= b;
        }
        crc = lookupArr[crc & 0xFF];
        crc ^= 0;

        return (byte) crc;
    }

      
    /**
     * Parses date and time from byte array to Calendar object
     * 
     * @param dateArr
     * @return
     */
    private Calendar parseDateTime(Byte[] dateArr)
    {
        byte[] reversedDateArr = new byte[4];
        reversedDateArr[0] = 0x00;
        reversedDateArr[1] = dateArr[2];
        reversedDateArr[2] = dateArr[1];
        reversedDateArr[3] = dateArr[0];
        
        // timestamp is in minutes from January 01, 2000 00:00
        int minutes = makeIntFromByte4(reversedDateArr, false);
        Calendar calendar = Calendar.getInstance();
        calendar.set(2000, 0, 1, 0, 0, 0);
        calendar.add(Calendar.MINUTE, minutes);
        
        return calendar;
       
    }
    
    /**
     * Creates a java signed integer from byte array of unsigned bytes.
     * 
     * @param b
     * @param littleEndian
     * @return
     */
    public static final int makeIntFromByte4(byte[] b, boolean littleEndian) {
        int time = 0;
        if (littleEndian)
        {
            time |= (b[0] & 0xff);
            time |= ((b[1] & 0xff) << 0x08);
            time |= ((b[2] & 0xff) << 0x10);
            time |= ((b[3] & 0xff) << 0x18); 
        }
        else
        {
            time = (b[0]& 0xff << 24 | (b[1] & 0xff) << 16 | (b[2] & 0xff) << 8 | (b[3] & 0xff));
        }       
        return time;
    }
    
    /**
     * Strips communication escape characters from the data.
     * 
     * @param inArray
     * @return
     */
    private static List<Byte> strip10s(byte[] inArray)
    {
        List<Byte> tempList = new ArrayList<Byte>();
        for (int i = 0; i < inArray.length; i++)
        {
            if (inArray[i] == 0x10 && inArray[i + 1] == 0x10)
            {
                i++;
            }
            else
            {
                tempList.add(new Byte(inArray[i]));
            }
        }
        return tempList;            
    }
    
    /**
     * Add communication escape characters to the data, if needed.
     * 
     * @param inArray
     * @return
     */
    private static LinkedList<Byte> escape10s(LinkedList<Byte> inArray)
    {
        LinkedList<Byte> tempList = new LinkedList<Byte>();
        for (Byte packet : inArray)
        {
            tempList.add(packet);
            if (packet == 0x10)
            {
                tempList.add(new Byte((byte)0x10));
            }
        }
        return tempList;            
    }


}