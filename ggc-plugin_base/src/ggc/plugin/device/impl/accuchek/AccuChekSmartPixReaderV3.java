package ggc.plugin.device.impl.accuchek;

import ggc.plugin.output.OutputWriter;
import ggc.plugin.util.DataAccessPlugInBase;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.OutputStreamWriter;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;


/**
 *  Application:   GGC - GNU Gluco Control
 *  Plug-in:       GGC PlugIn Base (base class for all plugins)
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
 *  Filename:     AccuChekSmartPixReaderV3  
 *  Description:  Reader for Device, AccuChek Smart Pix v3 or higher
 * 
 *  Author: Andy {andy@atech-software.com}
 */


public class AccuChekSmartPixReaderV3 extends AccuChekSmartPixReaderAbstract
{

    private static Log log = LogFactory.getLog(AccuChekSmartPixReaderV3.class);
    //String drive_path = "/media/SMART_PIX/";
    int max_retry = 5;
    
    
    
    /**
     * Constructor 
     * 
     * @param da
     * @param ow
     * @param par
     */
    public AccuChekSmartPixReaderV3(DataAccessPlugInBase da, OutputWriter ow, AccuChekSmartPix par)
    {
        super(da, ow, par);
        this.drive_path = par.getRootDrive();
    }
    
    
    /**
     * 
     */
/*    public AccuChekSmartPixReaderV3()
    {
        super();
        //System.out.println("Constructor [Smart Pix Reader v3] !");
    }
  */  
    
    public void readDevice()
    {
        
        //System.out.println("Read Device [Smart Pix Reader v3] !");
        
        try
        {

            boolean status = false;
            boolean finished = false;
            int count = 0;
            
            while(count < max_retry)
            {
                this.sendCommandToDevice("Abort");
                
                status = readStatusUntilState("NOSCAN", 5000);
                
                if (status)
                {
//                    System.out.println("We are in correct status !");
                    count = max_retry;
                    finished = true;
                }
                else
                    count++;
                
            }
            sleep(2000);
            
            
            if (!checkFinished(finished, 10, "PIX_ABORT_AUTOSCAN"))
                return;
            
            
            finished = false;
            count = 0;
            while(count < max_retry)
            {
                this.sendCommandToDevice("ReadDevice");
                sleep(2000);
                this.readStatus();
                
                status = readStatusUntilState("SCAN", 5000);
                
                if (status)
                {
//                    System.out.println("We are getting ready to read !");
                    count = max_retry;
                    finished = true;
                }
                else
                    count++;
                
            }

            
            if (!checkFinished(finished, 15, null))
                return;
            
            
            
            
            status = readStatusUntilState("SCAN", "FOUND", 20000);
            
            if (!checkFinished(status, 20, "PIX_READING_ELEMENT"))
                return;
            //System.out.println("Device Found !");
            
            
            status = readStatusUntilState("SCAN", "REQUEST", 120000);
            
            if (!checkFinished(status, 85, "PIX_FINISHED_READING"))
                return;
            
            
            
            //System.out.println("Report is beeing created !");
            
            status = readStatusUntilState("NOSCAN", "REPORT", 10000);
//            System.out.println("Report Created !");

            if (!checkFinished(status, 95, "PIX_FINISHED_REPORT_READY"))
                return;
            else
            {
                this.parent.readXmlFileFromDevice();
            }
            
            
            
            //this.setStatus(95, "PIX_FINISHED_REPORT_READY");
            
            ///boolean status = readStatusUntilState("", 5000000);
            
            
            
            
        }
        catch(Exception ex)
        {
            log.error("Error on ReadDevice(). Exception: " + ex, ex);
        }
            
    }

    
    private boolean checkFinished(boolean check_var, int status, String status_msg)
    {
        
        if (this.parent.isDeviceStopped())
        {
            this.setStatus(100, "PIX_ERROR_INIT_DEVICE");
            this.parent.setDeviceStopped();
            return false;
        }
        
        
        if (!check_var)
        {
            this.setStatus(100, "PIX_ERROR_INIT_DEVICE");
            return false;
        }
        else
        {
            this.setStatus(status, status_msg);
            return true;
        }
        
        
    }
    
    
    
    
    
    
    /**
     * Read Device
     * 
     * @param command
     */
    public void sendCommandToDevice(String command)
    {
        try
        {
//            System.out.println("Send " + command);
            BufferedWriter out = new BufferedWriter(new OutputStreamWriter(
                new FileOutputStream(new File(m_da.pathResolver(drive_path + "/" + System.currentTimeMillis() + ".txt"))),"ISO-8859-1"));
            
            out.write("SL42-B RemoteControl File\n" + "Command=" + command);
            out.flush();
            out.close();
        }
        catch(Exception ex)
        {
            System.out.println("Exception: " + ex);
        }
        
        
    }
    
    
    /**
     * Sleep
     * 
     * @param ms
     */
    public void sleep(long ms)
    {
        try
        {
            Thread.sleep(ms);
        }
        catch(Exception ex){}
        
        
    }
    
    
    
    
    
    
    private boolean readStatusUntilState(String state, long timeout_ms)
    {
        ///Misc/STATUS.TXT
        long end_time = System.currentTimeMillis() + timeout_ms;
        
        while(System.currentTimeMillis() < end_time)
        {
            String st = readStatus();
            if (st.equals(state))
                return true;
            else
                sleep(250);
            
            if (this.parent.isDeviceStopped())
            {
                this.parent.setDeviceStopped();
                return false;
            }
        }
        
        return false;
    }
    

    
    private boolean readStatusUntilState(String state1, String state2, long timeout_ms)
    {
        ///Misc/STATUS.TXT
        long end_time = System.currentTimeMillis() + timeout_ms;
        int i=0;
        
        boolean reading_data = false;
        
        if ((state1.equals("SCAN")) && (state2.equals("REQUEST")))
            reading_data = true;
        
        
        while(System.currentTimeMillis() < end_time)
        {
            String st[] = readStatuses();
            
            //if ((st.length==2) && (st[1].equals(state1)) && (st[2].contains(state2)))
            if ((st[1].equals(state1)) && (st[2].contains(state2)))
                return true;
            else
                sleep(250);
            
            if (this.parent.isDeviceStopped())
            {
                this.parent.setDeviceStopped();
                return false;
            }
            
            i++;
            
            if ((i%4==0) && (reading_data))
            {
                incrementProgress();
            }
            
            
        }
        
        return false;
    }
    
    
    int count_el = 0;

    
    private void incrementProgress()
    {
        count_el += this.parent.getNrOfElementsFor1s();
        
        float procs_x = (count_el*(1.0f))/this.parent.getMaxMemoryRecords();
        
        int pro_calc = (int)((0.2f + (0.0055f * (procs_x * 100.0f)))*100.0f);
        //int pro_calc = (int)((0.2f + (0.007f * (procs_x * 100.0f)))*100.0f);
        
        this.parent.writeStatus("PIX_READING_ELEMENT"); //, pro_calc + " %"));
        this.output_writer.setSpecialProgress(pro_calc);
    }
    
    
    private String readStatus()
    {
        String line, last_line=null;
        
        BufferedReader br=null;
        try
        {
            br = new BufferedReader(new FileReader(m_da.pathResolver(this.drive_path + "/MISC/STATUS.TXT")));
            
            while((line=br.readLine())!=null)
            {
                last_line = line;
            }

            String[] arr = last_line.split(" ");
            
            
//            System.out.println("Status: " + arr[1] + "  [" + last_line + "]"); //last_line);
            return arr[1];
        }
        catch(Exception ex)
        {
            log.error("Error on reading status. Ex.: " + ex, ex);
        }
        finally
        {
            try
            {
            br.close();
            }
            catch(Exception ex){}
        }
        
        return null;
    }
    

    
    private String[] readStatuses()
    {
        String line, last_line=null;
        
        BufferedReader br=null;
        try
        {
            br = new BufferedReader(new FileReader(m_da.pathResolver(this.drive_path + "/MISC/STATUS.TXT")));
            
            while((line=br.readLine())!=null)
            {
                last_line = line;
            }

            String[] arr = last_line.split(" ");
            
            
//            System.out.println("Statuses: " + arr[1] + "  [" + last_line + "]"); //last_line);
            return arr;
        }
        catch(Exception ex)
        {
            log.error("Error on reading statuses. Ex.: " + ex, ex);
            //System.out.println("Exception: " + ex);
        }
        finally
        {
            try
            {
            br.close();
            }
            catch(Exception ex){}
        }
        
        return null;
    }
    
    
    
    public void preInitDevice()
    {
        this.sendCommandToDevice("Abort");
        readStatusUntilState("NOSCAN", 5000);
    }
    
    
    
    
}
