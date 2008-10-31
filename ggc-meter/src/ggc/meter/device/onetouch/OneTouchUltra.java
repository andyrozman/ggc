package ggc.meter.device.onetouch;

import ggc.plugin.output.OutputWriter;
import gnu.io.SerialPortEvent;

import java.io.IOException;


public class OneTouchUltra extends OneTouchMeter
{
   
    /*
    public OneTouchUltra()
    {
    }*/


    public OneTouchUltra(String portName, OutputWriter writer)
    {
        super(20001, portName, writer);
        
//        end_string = (new Character((char)13)).toString();
        
        //this.writer = new GGCFileOutputWriter();
        this.m_output_writer.writeHeader();

        //this.setMeterType("Ascensia", "Contour");
    
        
        //this.serialPort.
        
        this.serialPort.notifyOnOutputEmpty(true);
        this.serialPort.notifyOnBreakInterrupt(true);
        this.serialPort.notifyOnDataAvailable(true);
        
  /*      
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
    */    
        
    }
    
    
    
    public void loadInitialData()
    {
        System.out.println("loadInitionalData");

        //waitTime(5000);
        
        try
        {
            /*
            boolean run = true;
            
            while(run)
            {
                System.out.print(".");
                write("DM?".getBytes());
                int c = this.read();
                if (c!=-1)
                    run=false;
            }
            
//          this.write("X".getBytes());
//          System.out.println(this.readLine());
            

        //    for(int i = 0; i < "\021\r".length(); i++)
//                this.write("\021\r".substring(i, i + 1).getBytes());
            
//            write(reconvert("11"));
//            write(reconvert("0d"));
//            write(reconvert("0a"));
         
            */
/*            System.out.println(reconvert("11"));
            
                write(reconvert("11"));
                waitTime(100);
                write(reconvert("0d"));
                waitTime(100);
                write(reconvert("0a"));
                waitTime(100);

  */          
            
            write("D".getBytes());
            waitTime(100);
            write("M".getBytes());
            waitTime(100);
            write("?".getBytes());
            waitTime(500);
            
            String line;
            
            while((line=this.readLine())==null)
            {
                
            }
            
            System.out.println("Serial Number: " + line);
            //System.out.println("Serial Number: " + this.readLine());
            //System.out.println("Serial Number: " + this.readLine());
            
            
            write("D".getBytes());
            waitTime(100);
            write("M".getBytes());
            waitTime(100);
            write("P".getBytes());
            waitTime(100);
            
            //write("DM?".getBytes());
            
            //write("DMP".getBytes());
            
/*
            this.device_running = true;
            
            */
            /*
            write(5);  // ENQ
            waitTime(1000); 
            write(6);  // ACK
  */
            //String line;

            System.out.println("loadInitialData()::");
         
            //powerOn();
            /* Z
            int ch;
            boolean running = true;
            while(running)
            {
                ch = this.read();
                System.out.print((char)ch + " int: " + ch);
            }
            /*
            while ((ch = this.read()) != -1) && (!isDeviceStopped(line)))
            {
                System.out.print((char)ch);
            }*/
            
            
            System.out.println("Out");
            //String line;    
            
            while (((line = this.readLine()) != null) && (!isDeviceStopped(line)))
            {
                System.out.println("D: " + line);
                //processData(line.trim());
                //sendToProcess(line);
                //write(6);
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
    
    /* x
    private int powerOn2()
    {
        /*
        this.serialPort.setInputBufferSize(16384);
        this.serialPort.setOutputBufferSize(1024);
        this.serialPort.setFlowControlMode(0);
        this.serialPort.setRTS(true);
        this.serialPort.setDTR(true);
        */
      /* x  
        return -1;
    }*/
    
    
    /*
    
D: P "SUN","05/25/08","05:01:16   ","  085 ", 00 0836

D: P "SUN","05/25/08","03:03:32   ","  064 ", 00 0831

D: P "SUN","05/25/08","00:44:00   ","  088 ", 00 0834

D: P "SAT","05/24/08","23:00:40   ","  089 ", 00 0827

D: P "SAT","05/24/08","20:33:00   ","  082 ", 00 081F

D: P "SAT","05/24/08","15:55:08   ","  055 ", 00 082F

D: P "SAT","05/24/08","10:26:24   ","  104 ", 00 0821

D: P "SAT","05/24/08","06:47:48   ","  183 ", 00 0836

D: P "SAT","05/24/08","00:25:32   ","  226 ", 00 0823

D: P "FRI","05/23/08","20:54:48   ","  136 ", 00 0826

D: P "FRI","05/23/08","19:55:24   ","  123 ", 00 0825

D: P "FRI","05/23/08","13:01:24   ","  091 ", 00 081A

D: P "FRI","05/23/08","08:28:16   ","  148 ", 00 082B

D: P "FRI","05/23/08","07:35:48   ","  132 ", 00 0826

D: P "FRI","05/23/08","05:38:40   ","  115 ", 00 0820

D: P "FRI","05/23/08","00:30:32   ","  250 ", 00 0814

D: P "THU","05/22/08","18:21:24   ","  301 ", 00 082A

D: P "THU","05/22/08","16:28:16   ","  037 ", 00 0836

D: P "THU","05/22/08","11:30:40   ","  229 ", 00 082A

D: P "THU","05/22/08","06:02:40   ","  053 ", 00 0828

D: P "WED","05/21/08","23:14:40   ","  115 ", 00 0817

D: P "WED","05/21/08","19:04:48   ","  150 ", 00 0822

D: P "WED","05/21/08","12:48:16   ","  050 ", 00 081D

D: P "WED","05/21/08","05:36:08   ","  056 ", 00 0823

D: P "TUE","05/20/08","19:43:08   ","  078 ", 00 0837

D: P "TUE","05/20/08","14:09:32   ","  105 ", 00 0828

D: P "TUE","05/20/08","05:35:32   ","  120 ", 00 0824

D: P "TUE","05/20/08","02:01:08   ","  159 ", 00 0829

D: P "MON","05/19/08","22:31:16   ","  138 ", 00 082E

D: P "MON","05/19/08","18:06:48   ","  059 ", 00 083C

D: P "MON","05/19/08","12:05:40   ","  183 ", 00 082B

D: P "MON","05/19/08","05:36:00   ","  126 ", 00 082A

D: P "MON","05/19/08","04:21:24   ","  033 ", 00 0826

D: P "MON","05/19/08","02:55:08   ","  067 ", 00 0834

D: P "MON","05/19/08","01:30:48   ","  170 ", 00 082B

D: P "SUN","05/18/08","23:11:48   ","  279 ", 00 0843

D: P "SUN","05/18/08","20:03:40   ","  209 ", 00 0832

D: P "SUN","05/18/08","18:07:16   ","  220 ", 00 0839

D: P "SUN","05/18/08","06:55:24   ","  046 ", 00 083E

D: P "SUN","05/18/08","00:22:08   ","  133 ", 00 0831

D: P "SAT","05/17/08","21:09:24   ","  202 ", 00 0825

D: P "SAT","05/17/08","17:21:08   ","  084 ", 00 082E

D: P "SAT","05/17/08","15:05:48   ","  093 ", 00 0832

D: P "SAT","05/17/08","07:51:08   ","  067 ", 00 0831

D: P "FRI","05/16/08","23:22:24   ","  105 ", 00 081C

D: P "FRI","05/16/08","16:27:40   ","  137 ", 00 0826

D: P "FRI","05/16/08","12:16:08   ","  138 ", 00 0825

D: P "FRI","05/16/08","05:42:32   ","  041 ", 00 081C

D: P "FRI","05/16/08","00:51:08   ","  102 ", 00 0818

D: P "THU","05/15/08","18:35:08   ","  057 ", 00 083B

D: P "THU","05/15/08","11:23:48   ","  113 ", 00 082E

D: P "THU","05/15/08","05:48:48   ","  072 ", 00 083C

D: P "THU","05/15/08","00:26:48   ","  085 ", 00 0837

D: P "WED","05/14/08","20:34:16   ","  100 ", 00 0815

D: P "WED","05/14/08","12:07:32   ","  181 ", 00 081D

D: P "WED","05/14/08","05:26:48   ","  039 ", 00 0829

D: P "TUE","05/13/08","19:45:56   ","  161 ", 00 0837

D: P "TUE","05/13/08","12:05:00   ","  082 ", 00 0823

D: P "TUE","05/13/08","05:26:48   ","  125 ", 00 0832

D: P "MON","05/12/08","23:38:40   ","  158 ", 00 082E

D: P "MON","05/12/08","18:28:16   ","  053 ", 00 082E

D: P "MON","05/12/08","11:22:24   ","  148 ", 00 0825

D: P "MON","05/12/08","05:55:48   ","  116 ", 00 082F

D: P "SUN","05/11/08","23:27:24   ","  157 ", 00 0838

D: P "SUN","05/11/08","17:44:24   ","  115 ", 00 0834

D: P "SUN","05/11/08","15:25:08   ","  099 ", 00 083E

D: P "SUN","05/11/08","10:29:08   ","  120 ", 00 082E

D: P "SUN","05/11/08","08:46:48   ","  091 ", 00 083F

D: P "SUN","05/11/08","06:28:56   ","  063 ", 00 083B

D: P "SUN","05/11/08","00:49:40   ","  076 ", 00 0835

D: P "SAT","05/10/08","20:29:16   ","  212 ", 00 0821

D: P "SAT","05/10/08","18:44:48   ","  131 ", 00 082A

D: P "SAT","05/10/08","12:57:00   ","  077 ", 00 0825

D: P "SAT","05/10/08","10:51:48   ","  097 ", 00 082B

D: P "SAT","05/10/08","10:08:00   ","  082 ", 00 081B

D: P "SAT","05/10/08","09:06:16   ","  077 ", 00 082C

D: P "SAT","05/10/08","08:08:08   ","  133 ", 00 0827

D: P "SAT","05/10/08","05:48:48   ","  065 ", 00 0830

D: P "FRI","05/09/08","23:44:08   ","  040 ", 00 0822

D: P "FRI","05/09/08","15:29:00   ","  109 ", 00 0824

D: P "FRI","05/09/08","11:41:16   ","  094 ", 00 0824

D: P "FRI","05/09/08","06:20:16   ","  111 ", 00 081B

D: P "FRI","05/09/08","00:08:32   ","  044 ", 00 081E

D: P "THU","05/08/08","21:01:32   ","  115 ", 00 0828

D: P "THU","05/08/08","13:03:24   ","  094 ", 00 0832

D: P "THU","05/08/08","05:54:40   ","  073 ", 00 0834

D: P "WED","05/07/08","21:37:16   ","  061 ", 00 0821

D: P "WED","05/07/08","12:37:00   ","  124 ", 00 081A

D: P "WED","05/07/08","11:11:16   ","  121 ", 00 0815

D: P "WED","05/07/08","06:22:00   ","  058 ", 00 081D

D: P "TUE","05/06/08","22:29:24   ","  097 ", 00 0838

D: P "TUE","05/06/08","13:54:32   ","  157 ", 00 0832

D: P "TUE","05/06/08","06:19:08   ","  097 ", 00 083B

D: P "MON","05/05/08","22:25:56   ","  078 ", 00 0833

D: P "MON","05/05/08","19:23:48   ","  057 ", 00 0835

D: P "MON","05/05/08","15:26:48   ","  130 ", 00 082C

D: P "MON","05/05/08","06:18:40   ","  112 ", 00 0825

D: P "SUN","05/04/08","23:45:08   ","  104 ", 00 0834
    
    */
    
    /*
    private int powerOn22() throws Exception
    {
        this.write(17);
        
        this.write("\rDM?".getBytes());
        
        return 0; */
        /*
        int x = 0;

        SendCommand("DM?\r");
        while (this.readLine() == null) 
        {
            SendCommand("DM?");
            x++;
            if (x == 5) 
                return 0;
        }
        return 1; */         
//    }
    
  /*  
    public void powerOn() throws Exception
    {
        writeCmd(17);
        writeCmd("\rDMF");

    }
    */
    
    protected void writeCmd(short line[])
    throws IOException, InterruptedException
    {
    for(int c = 0; c < line.length; c++)
        writeCmd(line[c]);

    }

protected void writeCmd(int c)
    throws IOException, InterruptedException
{
    //addDebug(c, D_WR);
    this.write(c);
    this.waitTime(commandPause);

}

protected void writeCmd(String line)
    throws IOException, InterruptedException
{
    for(int c = 0; c < line.length(); c++)
    {
//        addDebug(line.charAt(c), D_WR);
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
        int com_curr =0;
        
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
    
    private boolean isDeviceStopped(String vals)
    {
     //   if (!this.device_running)
     //       return true;
        
        return false;
    }
    

    public void sendToProcess(String line)
    {
        System.out.println(line);
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

    
    
    public byte[] reconvert(String _strHex) {

        if (_strHex == null)
        {
            System.out.println("#error HexString: reconvert null?");
            
            return null;
        }
        String strHex = _strHex.toUpperCase();

        int iLen = strHex.length();

        if ((iLen % 2) != 0)
        {
            
//            System.out.println("#error HexString: iLen="+iLen);
            iLen -=1;
        }

        byte[] buffer = new byte[iLen / 2];

        for (int i = 0; i < iLen/2; i++) {
            
            int ic1, ic2;

            char c1 = strHex.charAt(2 * i);
            if (Character.isDigit(c1))
                ic1 = c1 - '0';
            else
                ic1 = c1 - 'A' + 10;

            char c2 = strHex.charAt(2 * i + 1);
            if (Character.isDigit(c2))
                ic2 = c2 - '0';
            else
                ic2 = c2 - 'A' + (char) 10;

            buffer[i] = (byte)(ic1 * 16 + ic2);
        }

        return buffer;
        
    }
    
    
    
    @Override
    public void serialEvent(SerialPortEvent event)
    {

        //System.out.println("InEvent");

    //boolean debug = false;

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
           
            try
            {
                int newData = 2;
                byte[] bt = new byte[1024];
            //portInputStream.read(bt);

                while ((newData=portInputStream.read(bt))>0)
                {
                    System.out.println(bt);
                    System.out.println(newData);
                }
            }
            catch(Exception ex)
            {
                System.out.println("Exception trown: " + ex);
                
            }
              
                
            
          }



        // If break event append BREAK RECEIVED message.
        case SerialPortEvent.BI:
            System.out.println("recievied break");
//            setDeviceStopped();
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


    public int getMaxMemoryRecords()
    {
        return 1;
    }
   
    
    public void readDeviceDataFull()
    {
    }



    public String getName()
    {
        return "One Touch Ultra";
    }

}