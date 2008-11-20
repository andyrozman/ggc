package ggc.meter.device.onetouch;

import ggc.meter.data.MeterValuesEntry;
import ggc.meter.device.AbstractSerialMeter;
import ggc.meter.manager.company.LifeScan;
import ggc.meter.util.DataAccessMeter;
import ggc.plugin.device.DeviceIdentification;
import ggc.plugin.device.PlugInBaseException;
import ggc.plugin.manager.company.AbstractDeviceCompany;
import ggc.plugin.output.AbstractOutputWriter;
import ggc.plugin.output.OutputWriter;
import ggc.plugin.protocol.SerialProtocol;
import gnu.io.SerialPort;
import gnu.io.SerialPortEvent;

import java.io.IOException;
import java.util.ArrayList;
import java.util.StringTokenizer;

import com.atech.utils.ATechDate;
import com.atech.utils.HexUtils;
import com.atech.utils.TimeZoneUtil;


public abstract class OneTouchMeter2 extends AbstractSerialMeter
{
    
    
    
    
    
    protected boolean device_running = true;
    protected ArrayList<MeterValuesEntry> data = null;
//    protected OutputWriter m_output_writer;
    public TimeZoneUtil tzu = TimeZoneUtil.getInstance();
    //public int meter_type = 20000;
    private int entries_max = 0;
    private int entries_current = 0;
    private int reading_status = 0;
    
    private int info_tokens;
    private String date_order;
    
    
    
    /**
     * Constructor
     */
    public OneTouchMeter2()
    {
    }
    
    /**
     * Constructor for device manager
     * 
     * @param cmp
     */
    public OneTouchMeter2(AbstractDeviceCompany cmp)
    {
        super(cmp);
    }
    
    
    
    public OneTouchMeter2(String portName, OutputWriter writer)
    {
        super(DataAccessMeter.getInstance());
        
        this.setCommunicationSettings( 
                  9600,
                  SerialPort.DATABITS_8, 
                  SerialPort.STOPBITS_1, 
                  SerialPort.PARITY_NONE,
                  SerialPort.FLOWCONTROL_NONE, 
                  SerialProtocol.SERIAL_EVENT_BREAK_INTERRUPT|SerialProtocol.SERIAL_EVENT_OUTPUT_EMPTY);
                
        // output writer, this is how data is returned (for testing new devices, we can use Consol
        this.output_writer = writer; 
        this.output_writer.getOutputUtil().setMaxMemoryRecords(this.getMaxMemoryRecords());
        
        // set meter type (this will be deprecated in future, but it's needed for now
        this.setMeterType("OneTouch", this.getName());

        // set device company (needed for now, will also be deprecated)
        this.setDeviceCompany(new LifeScan());
        

        // settting serial port in com library
        try
        {
            this.setSerialPort(portName);
    
            if (!this.open())
            {
                this.m_status = 1;
            }

            this.output_writer.writeHeader();
            
        }
        catch(Exception ex)
        {
            System.out.println("OneTouchMeter -> Error adding listener: " + ex);
            ex.printStackTrace();
        }
        
        if (this.getDeviceId()==OneTouchMeter.METER_LIFESCAN_ONE_TOUCH_ULTRA)
        {
            this.info_tokens = 3;
            this.date_order = "MDY";
        }
        else
        {
            this.info_tokens = 8;
        }
        
    }


    
    
    /* 
     * getComment
     */
    public String getComment()
    {
        // TODO Auto-generated method stub
        return null;
    }



    /* 
     * getImplementationStatus
     */
    public int getImplementationStatus()
    {
        // TODO Auto-generated method stub
        return 0;
    }

    /* 
     * getInstructions
     */
    public String getInstructions()
    {
        // TODO Auto-generated method stub
        return null;
    }


    HexUtils hex_utils = new HexUtils();
    
    
    String rec_bef = "02" + "0A" + "00" + "05" + "1F";
    String rec_af = "03" + "38" + "AA";
    
    String ack_pc = "02" + "06" + "07" + "03" + "FC" + "72";


    public void readDeviceDataFull()
    {
        
        System.out.println("loadInitionalData");

        //waitTime(5000);
        
        //this.output_writer.
        
        
        try
        {
            HexUtils hu = new HexUtils();

            String ret;
            byte[] reta;
            
            
            cmdDisconnectAcknowledge();
            

            
            
            // read sw version and sw creation date
            System.out.println("PC-> read sw version and sw creation date");
            String read_sw_ver_create = "02" + "09" + "00" + 
                                        "05" + "0D" + "02" +
                                        "03" + "DA" + "71";
            
            write(hu.reconvert(read_sw_ver_create));
            
            //reta = this.readLineBytes();
            
            
            //System.out.println("Acknowledge Meter: " + this.readLine());
            String sw_dd = tryToConvert(this.readLineBytes(), 6+6, 3, false);

            int idx = sw_dd.indexOf("/") -2;
            
            
            //System.out.println("Sw Ver: " + sw_dd.substring(0, idx ) + " date: " + sw_dd.substring(idx));
            
            
            //System.out.println("Value Meter: " + this.readLine());


            System.out.println("PC-> Acknowledge");
            
            write(hu.reconvert(ack_pc));

            
            // read serial number
            System.out.println("PC-> read serial nr");

            String read_serial_nr = "02" + "12" + "00" + "05" + // STX Len Link
            "0B" + "02" + "00" + "00" + "00" + "00" + "84" + "6A" + "E8" + "73" + "00" +  // CM1-CM12
            "03" + "9B" + "EA"; // ETX CRC-L CRC-H
            
            write(hu.reconvert(read_serial_nr));
            
            String sn = tryToConvert(this.readLineBytes(), 6+5, 3, false);
            
            System.out.println("S/N: " + sn);
            
            System.out.println("PC-> Acknowledge");
            write(hu.reconvert(ack_pc));
            
            
            // read entry 501 to return count
            
            
            System.out.println("PC-> read record 501 to receive nr");
            
            write(hu.reconvert(rec_bef + "F5" + "01" + rec_af));
            
            //ret = this.readLine();
            
            reta = this.readLineBytes();
            
            String els = tryToConvert(reta, 6+5, 3, true);
            
            reta = getByteSubArray(reta, 6+5, 3, 2);
            
            this.showByteArray(reta);
            
//            System.out.println("10: " + Integer.parseInt("10", 16));
//            System.out.println("Val: " + Integer.toString(10, 16));
            
            int nr = (reta[1] * 255) + reta[0];
            
            System.out.println("Entries: " + nr);
            
            System.out.println("PC-> Acknowledge");
            write(hu.reconvert(ack_pc));
          
            
            //readEntry(260);
            cmdDisconnectAcknowledge();
            
            readEntry(1);

            readEntry(2);
            

        }
        catch(Exception ex)
        {
            System.out.println("Exception: " + ex);
            ex.printStackTrace();
            
        }
        
        if (this.isDeviceFinished())
        {
        	this.output_writer.endOutput();
        }
        
        
        
        System.out.println("Reading finsihed");

        
        
    }

    
    private void cmdDisconnectAcknowledge() throws IOException
    {
        System.out.println("PC-> Disconnect");
        String disconect = "02" + "06" + "08" + "03" + "C2" + "62";
        
        write(hex_utils.reconvert(disconect));
        
        System.out.println("Disconected and acknowledged: " + this.readLine());
    }
    
    
    
    private void readEntry(int number) throws IOException
    {
        int num_nr = number-1;
        
        String nr1= "";
        String nr2= "00";

        //System.out.println("10: " + Integer.parseInt("10", 16));
        //System.out.println("Val: " + Integer.toString(10, 16));
        
        
        if (num_nr>255)
        {
            nr2 = "01";
            num_nr = num_nr - 255;
        }

        nr1 = Integer.toHexString(num_nr);
        
        if (nr1.length()==1)
            nr1 = "0" + nr1;
        
        System.out.println(nr1 + " " + nr2);
        
        
        //nr1 = Integer.to
//        String rec_bef = "02" + "0A" + "03" + "05" + "1F";
//        String rec_af = "03" + "38" + "AA";
        
        
        System.out.println("PC-> read record #" + number);
        
//        write(hex_utils.reconvert(rec_bef + nr1 + nr2 + "03" + "4B5F"));   //rec_af));
  
        if (number==1)
            write(hex_utils.reconvert("02" + "0A" + "03" + "05" + "1F" + "0000" + "03" + "4B5F"));   //rec_af));
        else if (number==2)
            write(hex_utils.reconvert("02" + "0A" + "00" + "05" + "1F" + "0100" + "03" + "9BA6"));   //rec_af));
        
        
        //ret = this.readLine();
        
        byte[] reta = this.readLineBytes();
        
        //String els = tryToConvert(reta, 6+5, 3, true);
        
        //reta = getByteSubArray(reta, 6+5, 3, 2);
        
        this.showByteArray(reta);
        
        //System.out.println("10: " + Integer.parseInt("10", 16));
        
        //System.out.println("Val: " + Integer.toString(10, 16));
        
  //      int nr = (reta[1] * 255) + reta[0];
        
        
        
        
        System.out.println("PC-> Acknowledge");
        
        write(hex_utils.reconvert(ack_pc));
        
        
    }
    
    
    private short crc_calculate_crc (short initial_crc, int[] buffer, int length)
    {
        //short index = 0;
        short crc = initial_crc;
        if (buffer != null)
        {
            for (int index = 0; index < length; index++)
            {
                crc = (short)((char)(crc >> 8) | (short)(crc << 8));
                crc ^= buffer [index];
                crc ^= (char)(crc & 0xff) >> 4;
                //crc ^= (short)((short)(crc << 8) << 4);
                crc ^= (short)((crc << 8) << 4);
                crc ^= (short)(((crc & 0xff) << 4) << 1);
//                crc ^= (short)((short)((crc & 0xff) << 4) << 1);
            }
        }
        return crc;
    }    

    
    private short crc_calculate_crc (short initial_crc, String buffer, int length)
    {
        //short index = 0;
        short crc = initial_crc;
        if (buffer != null)
        {
            for (int index = 0; index < length; index+=2)
            {
                crc = (short)((char)(crc >> 8) | (short)(crc << 8));
                crc ^= buffer.charAt(index) + buffer.charAt(index+1); // [index];
                crc ^= (char)(crc & 0xff) >> 4;
                //crc ^= (short)((short)(crc << 8) << 4);
                crc ^= (short)((crc << 8) << 4);
                crc ^= (short)(((crc & 0xff) << 4) << 1);
//                crc ^= (short)((short)((crc & 0xff) << 4) << 1);
            }
        }
        return crc;
    }    
    
    public int crc_calc(int initial_crc, int[] buffer, int length)
    {
        int crc = 0; // = initial_crc;
        
        // char ser_data;
        
        for (int index = 0; index < length; index++)
        {
            crc  = (char)(crc >> 8) | (crc << 8);
            crc ^= (char)buffer [index]; //ser_data;
            crc ^= (char)(crc & 0xff) >> 4;
            crc ^= (crc << 8) << 4;
            crc ^= ((crc & 0xff) << 4) << 1;
        }
        return crc;
        
    }
    
    
    
    public void test_crc()
    {
        int test_crc[] = {0x02,0x06,0x06,0x03 };
        
        System.out.println("Correct decoding is 0x41CD: " + Integer.parseInt("41CD", 16));
        
        
        int crc = crc_calculate_crc( (short)0xffff, test_crc, 4 );  
        
        int crc2 = crc_calculate_crc( (short)0xffff, "02060603", 8 );
        
        System.out.println("Crc: " + crc + " " + Integer.toHexString(crc));

        System.out.println("Crc2: " + crc2 + " " + Integer.toHexString(crc2));
     
        int crc3 = this.crc_calc(0xffff, test_crc, 4);
        
        System.out.println("Crc3: " + crc3 + " " + Integer.toHexString(crc3));
        
        Crc16 cr = new Crc16();
        
        cr.reset();
        
        cr.update(0x02);
        cr.update(0x06);
        cr.update(0x06);
        cr.update(0x03);
        
        System.out.println("Crc16: " + cr.getValueInt() + " " + Integer.toHexString((int)cr.getValue()));
        
        FCS16 f1 = new FCS16();
        
        f1.reset();
        
        f1.update(0x02);
        f1.update(0x06);
        f1.update(0x06);
        f1.update(0x03);
        
        System.out.println("FCS16: " + f1.getValue() + " " + Integer.toHexString((int)f1.getValue()));
        
    }
    
    

    
    /*

    unsigned short crc_calculate_crc (unsigned short initial_crc, const unsigned char *buffer, unsigned short length)
    {
    unsigned short index = 0;
    unsigned short crc = initial_crc;
    if (buffer != NULL)
    {
    for (index = 0; index < length; index++)
    {
    crc = (unsigned short)((unsigned char)(crc >> 8) | (unsigned short)(crc << 8));
    crc ^= buffer [index];
    crc ^= (unsigned char)(crc & 0xff) >> 4;
    crc ^= (unsigned short)((unsigned short)(crc << 8) << 4);
    crc ^= (unsigned short)((unsigned short)((crc & 0xff) << 4) << 1);
    }
    }
    return (crc);
    }    



     */
    
    
    private boolean isDeviceFinished()
    {
    	return (this.entries_current==this.entries_max);
    }
    
    
    private void showByteArray(byte[] arr)
    {
        System.out.println("Byte array: ");
        
        for(int i=0; i<arr.length; i++)
        {
            System.out.print(arr[i] + " ");
        }
        
    }
    

    private String tryToConvert(byte[] arr, int start, int end, boolean display)
    {
        //System.out.println();
        StringBuilder sb = new StringBuilder();
        
        for(int i=start; i<(arr.length-end); i++)
        {
            //System.out.print(arr[i] + " ");
            sb.append((char)arr[i]);
        }
        
        String ret = sb.toString();
        
        if (display)
            System.out.println(ret);
        
        return ret;
    }
    

    
    private byte[] getByteSubArray(byte[] arr, int start, int end, int length)
    {
        //System.out.println();
        //StringBuilder sb = new StringBuilder();
        byte[] arr_out = new byte[length];
        int j=0;
        
        for(int i=start; i<(arr.length-end); i++)
        {
            arr_out[j] = arr[i];
            //System.out.print(arr[i] + " ");
            //sb.append((char)arr[i]);
            j++;
        }
        
        //String ret = sb.toString();
        
        //if (display)
        //    System.out.println(ret);
        
        return arr_out;
    }
    
    
 
    /**
     * This is method for reading partitial data from device. All reading from actual device should be done from 
     * here. Reading can be done directly here, or event can be used to read data.
     */
    public void readDeviceDataPartitial() throws PlugInBaseException
    {
        
    }


    /** 
     * This is method for reading configuration
     * 
     * @throws MeterExceptions
     */
    public void readConfiguration() throws PlugInBaseException
    {
    }
    

    /**
     * This is for reading device information. This should be used only if normal dump doesn't retrieve this
     * information (most dumps do). 
     * @throws MeterExceptions
     */
    public void readInfo() throws PlugInBaseException
    {
    }
    
    
    
    
    private boolean isDeviceStopped(String vals)
    {
    	if ((vals == null) ||
    	    ((this.reading_status==1) && (vals.length()==0)) ||
            (!this.device_running) ||
            (this.output_writer.isReadingStopped()))
    		return true;
    	
        return false;
    }
    
    
    
    
    private void processEntry(String entry)
    {
        if ((entry==null) || (entry.length()==0))
            return;
        
        
        StringTokenizer strtok = new StringTokenizer(entry, ",");
        
        System.out.println("tokens: " + strtok.countTokens());
        
        if (strtok.countTokens()==this.info_tokens) // we can have different info tokens for different meters
        {
        	if (this.reading_status==0) // info can be read only before reading of data, if we receive entry 
        		this.readInfo(strtok);  // with same nr of parameters, this is error
        	else
        		setDeviceStopped();
        	
        }
        else if (strtok.countTokens()==5)
        {
            this.readBGEntry(strtok, entry);
        }
        else
        {
//        	System.out.println("wrong oaraomn: ");
        	setDeviceStopped();
        }
        
        
        
    }
    
    
    
    
    
    public void setDeviceStopped()
    {
        this.device_running = false;
        this.output_writer.endOutput();
    }
    
    
    public void readInfo(StringTokenizer strtok)
    {
/*        P nnn,“ MeterSN ”,“MG/DL ”
        (1) (2) (3)
        (1) Number of datalog records to follow (0 – 150)
        (2) Meter serial number (9 characters)
        (3) Unit of measure for glucose values */
        
        //strtok 
        
        String num_x = strtok.nextToken(); // 1 token: number of entries
        num_x = num_x.substring(2).trim();
        
        this.entries_max = Integer.parseInt(num_x);
        
        
        String dev = strtok.nextToken(); // 2. token: device id
        
        DeviceIdentification di = new DeviceIdentification(ic);
        di.device_serial_number = dev;
        di.company = "OneTouch";
        di.device_selected = "Ultra";
        
        this.output_writer.setDeviceIdentification(di);
        this.output_writer.writeDeviceIdentification();

        
        if (this.getDeviceId()!=OneTouchMeter.METER_LIFESCAN_ONE_TOUCH_ULTRA)
        {
            //'P nnn,"MeterSN","ENGL. ","M.D.Y. ","AM/PM","MG/DL","!min","!max" cksm' 
            //"M.D.Y. " or "D.M.Y. "
            strtok.nextToken(); // 3
            
            String dx = this.getParameterValue(strtok.nextToken()); // 4. token: date
            
            if (dx.equals("M.D.Y."))
                this.date_order = "MDY";
            else
                this.date_order = "DMY";
        }
        
        
        reading_status = 1;
    }

    
    
    
    protected String getParameterValue(String val)
    {
        String d = val.substring(1, val.length()-1);
        return d.trim();
    }
    

    public void readBGEntry(StringTokenizer strtok, String entry)
    {
        try
        {
            System.out.println("BG: " + entry);
            /*
            P “dow”,“mm/dd/yy”,“hh:mm:30 ”,“xxxxx ”, n cksm<CR><LF>
            (4) (5) (6) (7)
            (4) Day of week (SUN, MON, TUE, WED, THU, FRI, SAT)
            (5) Date of reading
            (6) Time of reading (If two or more readings were taken within the same minute, they will be
            separated by 8 second intervals)
            (7) Result format:
            “ nnn ” - blood test result (mg/dL)
            “ HIGH ” - blood test result >600 mg/dL
            “C nnn ” - control solution test result (mg/dL)
            “CHIGH ” - control solution test result >600 mg/dL
            
            
            (12) Result format: (Profile) 
                 "  nnn " - blood test result (mg/dL)
                 "MMnn.n" - blood test result (mmol/dL)
                 " HIGH " - blood test result > 600 mg/dL
                 "! nnn" - check strip test result (mg/dL)
                 "! nn.n " - check strip test result (mmol/L)
                 "!HIGH " - check strip test result > 600 mg/dL
                 "C nnn " - control solution test result (mg/dL)
                 "C nn.n" - control solution test result (mmol/L)
                 "CHIGH " - control solution test result > 600 mg/dL
                 "I 000 " to "I 150 " - CARB records
                 "I 00.0 " to "I 20.0 " - BOLUS insulin records
                 "I  00 " to "I  99 " - all other insulin records             
            
      glucose value formatting (there are language variations): (OT2)
        If the datalog record had a checksum error, the last character
          is forced to be a question mark (e.g., '...," nnn?",...') All
          data in a record so flagged must be considered "suspect".
        If glucose > 600 mg/dL, '...," HIGH ",...' is transmitted.
        If UNITS are mmol/L, '...,"MMnn.n ",...' is transmitted.
        If a checkstrip reading, ',,,,"! nnn ",...' (mg/dL) or
          '...,"! n.n ",...' (mmol/L) is transmitted.
        If flagged as control solution, '...,"C nnn ",...' (mg/dL) or
          '...,"C nn.n ",...' (mmol/L) is transmitted.  (The 'C' becomes
          a 'K' if the languages is SVENS or DEUTS.)             
            
            */
            
            strtok.nextToken();
            String date = strtok.nextToken();
            String time = strtok.nextToken();
            
            String res = this.getParameterValue(strtok.nextToken());
            
            //if ((res.contains("CHIGH")) || (res.contains("C ")))
            if ((res.startsWith("C")) ||  // control solution
                (res.startsWith("!")) ||  // check strip
                (res.startsWith("I")) ||  // insulin or other data
                (res.startsWith("K")))    // control solution (on OT2 when language is SVENS or DEUTSCH)
            {
            	this.entries_current++;
            }
            else
            {
            	this.entries_current++;

            	MeterValuesEntry mve = new MeterValuesEntry();
                mve.setBgUnit(DataAccessMeter.BG_MGDL);
                mve.setDateTime(getDateTime(date, time));
                
                
                if (res.contains("HIGH"))
                {
                    mve.setBgValue("600");
                    mve.addParameter("RESULT", "High");
                }
                else
                {
                    if (res.contains("?"))
                    {
                        res = m_da.replaceExpression(res, "?", " ").trim();
                        mve.addParameter("RESULT", "Suspect Entry");
                    }
                    
                    if (res.contains("MM"))
                    {
                        // mmol value, this is not supported by ultra, but some other meters support this
                        res = m_da.replaceExpression(res, "MM", " ").trim();
                        
                        try
                        {
                            mve.setBgValue("" + m_da.getBGValueByType(DataAccessMeter.BG_MMOL, DataAccessMeter.BG_MGDL, res));
                        }
                        catch(Exception ex)
                        {}
                        
                    }
                    else
                        mve.setBgValue(res);
                }
                
                this.output_writer.writeData(mve);
            }
        }
        catch(Exception ex)
        {
            System.out.println("Exception: " + ex);
            System.out.println("Entry: " + entry);
            ex.printStackTrace();
        }
        
    }
    
    
    private ATechDate getDateTime(String date, String time)
    {
        // “mm/dd/yy”,“hh:mm:30 ”
        
        date = this.getParameterValue(date); 
        time = this.getParameterValue(time); 
        
       
        String dt = "";
        
        StringTokenizer st = new StringTokenizer(date, "/");
        
        String m="",d="",y="";
        
        if (this.date_order.equals("MDY"))
        {
            m = st.nextToken();
            d = st.nextToken();
            y = st.nextToken();
        }
        else
        {
            d = st.nextToken();
            m = st.nextToken();
            y = st.nextToken();
        }
        		
        
        try
        {
            int year = Integer.parseInt(y);
            
            if (year<100)
            {
                if (year>70)
                    dt += "19" + m_da.getLeadingZero(year, 2);
                else
                    dt += "20" + m_da.getLeadingZero(year, 2);
            }
            else
                dt += year;
            
        }
        catch(Exception ex)
        {
            
        }
        
        dt += m;
        dt += d;

        if (time.contains(" "))
        {

            st = new StringTokenizer(time, ":");
            
            String hr = st.nextToken();
            String min = st.nextToken();
            
            int hr_s = Integer.parseInt(hr);
            
            if (time.contains("AM"))
            {
                // am
                if (hr_s==12)
                {
                    dt += "00";
                }
                else
                {
                    dt += m_da.getLeadingZero(hr_s, 2);
                }
                
                
            }
            else
            {
                // pm
                if (hr_s==12)
                {
                    dt += "12";
                }
                else
                {
                    hr_s += 12;
                    dt += m_da.getLeadingZero(hr_s, 2);
                }
                
            }
            
            dt += min;
            
        }
        else
        {
            st = new StringTokenizer(time, ":");
            
            dt += st.nextToken();
            dt += st.nextToken();
        }
        
        return tzu.getCorrectedDateTime(new ATechDate(Long.parseLong(dt)));
        
    }

    
    //private void 
    
    
    
    /**
     * Returns short name for meter (for example OT Ultra, would return "Ultra")
     * 
     * @return short name of meter
     */
    public abstract String getShortName();
    
    
    
    
    
    /**
     * We don't use serial event for reading data, because process takes too long, we use serial event just 
     * to determine if device is stopped (interrupted) 
     */
    @Override
    public void serialEvent(SerialPortEvent event)
    {


        // Determine type of event.
        switch (event.getEventType()) 
        {
    
            // If break event append BREAK RECEIVED message.
            case SerialPortEvent.BI:
                System.out.println("recievied break");
                this.output_writer.setStatus(AbstractOutputWriter.STATUS_STOPPED_DEVICE);
                //setDeviceStopped();
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
    
    
    @SuppressWarnings("unused")
    private class OldOTProcessor extends OneTouchMeter //AbstractSerialMeter
    {
        
        
        public OldOTProcessor(String portName, OutputWriter writer)
        {
            super(portName, writer);
        }

        protected void writeCmd(short line[]) throws IOException, InterruptedException
        {
            for (int c = 0; c < line.length; c++)
                writeCmd(line[c]);

        }

        protected void writeCmd(int c) throws IOException, InterruptedException
        {
            // addDebug(c, D_WR);
            this.write(c);
            this.waitTime(commandPause);

        }

        protected void writeCmd(String line) throws IOException, InterruptedException
        {
            for (int c = 0; c < line.length(); c++)
            {
                // addDebug(line.charAt(c), D_WR);
                this.write(line.charAt(c));
                this.waitTime(commandPause);
            }

            this.waitTime(commandPause);
        }

        int commandPause = 100;

        void SendCommand(String command) throws Exception
        {
            int x;
            char c;

            x = command.length();
            c = command.charAt(0);
            int com_curr = 0;

            if (c == 'D')
            {
                while (x > 0)
                {
                    c = command.charAt(com_curr);
                    this.write(c);
                    waitTime(100);
                    x--;
                    com_curr++;
                }
            }
            else
                System.out.println("Invalid Command: " + command);
            return;
        }

        public int getCompanyId()
        {
            return 0;
        }

        public int getMaxMemoryRecords()
        {
            return 0;
        }

        public String getDeviceClassName()
        {
            return null;
        }

        public int getDeviceId()
        {
            return 0;
        }

        public String getIconName()
        {
            return null;
        }

        public String getName()
        {
            return null;
        }

        @Override
        public String getShortName()
        {
            // TODO Auto-generated method stub
            return null;
        }




        /*
        char *ReadLine(void) {
            int nBytes, x=0;
            char cComm, *line;
            struct timeval last, now;
            line = malloc(80);
            gettimeofday(&last, NULL);

            while ( 1 ) {
                nBytes = read(fd_ComX, &cComm, 1);
                    if ( (nBytes>0) && (cComm!=EOF) ) {
                    *line = cComm;
                    line++;
                    x++;
                    gettimeofday(&last, NULL);
                    if (cComm == '\n') break;
                }
                gettimeofday(&now, NULL);
                if ((last.tv_sec + 2) < now.tv_sec) break;
            }
            line = line - x;
            return line;
        } */
        
        
        
    }
    
    
    
}