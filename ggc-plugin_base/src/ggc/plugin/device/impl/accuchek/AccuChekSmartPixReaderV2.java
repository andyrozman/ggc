package ggc.plugin.device.impl.accuchek;

import ggc.plugin.output.OutputWriter;
import ggc.plugin.util.DataAccessPlugInBase;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;

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
 *  Filename:     ----  
 *  Description:  ----
 * 
 *  Author: Andy {andy@atech-software.com}
 */


public class AccuChekSmartPixReaderV2 extends AccuChekSmartPixReaderAbstract
{
    
    private boolean device_found = false;

    /**
     * Constructor 
     * 
     * @param da
     * @param ow
     * @param par
     */
    public AccuChekSmartPixReaderV2(DataAccessPlugInBase da, OutputWriter ow, AccuChekSmartPix par)
    {
        super(da, ow, par);
        this.drive_path = par.getRootDrive();
        //System.out.println("Constructor [Smart Pix Reader v2] !");
    }
    
    
    public void readDevice()
    {
        
//        System.out.println("Read Device [Smart Pix Reader v2] !");
        
        // start working
        String drv = this.parent.getConnectionPort();
        String cmd = drv + "\\TRG\\";
        
        cmd = m_da.pathResolver(cmd);

//        System.out.println("Cmd: " + cmd);
        
        
        this.setStatus(5, "PIX_ABORT_AUTOSCAN");
        
        
        // abort auto scan
        File f = new File(cmd + "TRG09.PNG");
//        System.out.println("TRG09: " +  f.exists());
        
//        f.setLastModified(System.currentTimeMillis());
        writeToFile(f);
        
        
        f = new File(cmd + "TRG03.PNG");
        writeToFile(f);
//        f.setLastModified(System.currentTimeMillis());
        

        //this.writeStatus("PIX_READING");
        this.output_writer.setSpecialProgress(10);
        
        // read device  
        f = new File(cmd + "TRG09.PNG");
        writeToFile(f);
//      f.setLastModified(System.currentTimeMillis());
        
        f = new File(cmd + "TRG00.PNG");
        writeToFile(f);
//      f.setLastModified(System.currentTimeMillis());
        
        boolean found = false;
        m_da.sleepMS(2000);
        
        int count_el = 0;
        
        
        do
        {

            if (this.parent.isDeviceStopped())
            {
                this.parent.setDeviceStopped();
                found = true;
            }
            
            int st = readStatusFromConfig(drv);
            
//            System.out.println("Status: " + st);
            
            if (st==1)
            {
                this.parent.writeStatus("PIX_UNRECOVERABLE_ERROR");
                this.output_writer.setSpecialProgress(100);

                //System.out.println("Unrecoverable error - Aborting");
                return;
            }
            else if (st==2)
            {
                
                this.parent.writeStatus("PIX_FINISHED_READING");
                this.output_writer.setSpecialProgress(90);

                //System.out.println("Finished reading");
                return;
            }
            else if (st==4)
            {
                count_el += this.parent.getNrOfElementsFor1s();
                
                float procs_x = (count_el*(1.0f))/this.parent.getMaxMemoryRecords();
                
                int pro_calc = (int)((0.2f + (0.007f * (procs_x * 100.0f)))*100.0f);
 
                this.parent.writeStatus("PIX_READING_ELEMENT"); //, pro_calc + " %"));
                this.output_writer.setSpecialProgress(pro_calc);

            }
            /*else if (st==20)
            {
                this.writeStatus("PIX_DEVICE_NOT_FOUND");
                this.output_writer.setSpecialProgress(100);

                //System.out.println("Unrecoverable error - Aborting");
                return;
            }*/
            else if ((st>99) || (st==20))
            {
                if ((st==101) || (st==20))
                {
                    
                    this.parent.readXmlFileFromDevice();
                    
/*                    
                    this.parent.writeStatus("PIX_FINISHED_REPORT_READY");
                    //System.out.println("Finished reading. Report ready." );
                    this.output_writer.setSpecialProgress(95);

                    
                    File f1 = new File(m_da.pathResolver(drv + "\\REPORT\\XML"));
                    
                    File[] fls = f1.listFiles(new FileFilter()
                    {

                        public boolean accept(File file)
                        {
                            return ((file.getName().toUpperCase().contains(".XML")) &&
                                    (file.getName().startsWith(parent.getFirstLetterForReport())));
                        }}
                    );
                    
                    
                    //processXml(fls[0]);
                    this.parent.processXml(fls[0]);

                    this.output_writer.setSpecialProgress(100);
                    this.output_writer.setSubStatus(null);
  */                  
                    return;
                    
                }
                else
                {
                    this.parent.writeStatus("PIX_FINISHED_REPORT_READY");
                    this.output_writer.setSpecialProgress(95);

                    return;
                }
            }
                
            
            m_da.sleepMS(1000);
            
            
        } while(found!=true);
        
        this.parent.setDeviceStopped();
        
    }
    
    private void writeToFile(File file)
    {
        try
        {
        //BufferedWriter bw = new BufferedWriter(new FileWriter(file));
        //bw.newLine();
            
            BufferedReader bw = new BufferedReader(new FileReader(file));
            bw.readLine();
        }
        catch(Exception ex)
        {
            System.out.println("Problem writing to file: " + ex);
        }
        
    }
    
    
    
    // 0 = no status
    // 1 = error_found
    // 2 = finished
    private int readStatusFromConfig(String drive)
    {
        try
        {
            //boolean error_found = false;
            //boolean image_found = false;
            
//            System.out.println("Scan path: " + m_da.pathResolver(drive + "\\REPORT\\SCAN.HTM"));
            
            BufferedReader br = new BufferedReader(new FileReader(new File(m_da.pathResolver(drive + "\\REPORT\\SCAN.HTM"))));
            
            String line = "";
            
            boolean reports[] = { false, false, false };
            int rep_count = 0;
            
            
            while ((line = br.readLine())!= null)
            {
                
                if (line.contains("Error.htm"))
                {
                    return 1;
                }
                else if (line.contains("img/"))
                {
//                    System.out.println("Image: " + line);
                    if (line.contains("Scanning.gif"))
                    {
                        this.parent.writeStatus("PIX_SCANNING");
                        this.output_writer.setSpecialProgress(15);
                        //System.out.println("Scanning for device");
                        return 0;
                    }
                    else if (line.contains("CrReport.png"))
                    {
                        this.parent.writeStatus("PIX_CREATING_REPORT");
                        this.output_writer.setSpecialProgress(90);

                        //System.out.println("Finished reading - Creating report");
                    }
                    else if (line.contains("rd_"))
                    {
                        device_found = true;
                        //System.out.println("Reading from meter.");
                        return 4;
                    }
                    else
                    {
                        System.out.println("Unknown image: " + line);
                    }
                    
                    
                    return 0;
                }
                else if (line.contains("ReportPresent "))
                {
//                    System.out.println("L: " + line);
                    
                    if (line.contains("parent.BgReportPresent = "))
                    {
                        reports[0] = getBooleanStatus(line);
                        rep_count++;
                    }
                    else if (line.contains("parent.IpReportPresent = "))
                    {
                        reports[1] = getBooleanStatus(line);
                        rep_count++;
                    }
                    else if (line.contains("parent.MgReportPresent = "))
                    {
                        reports[2] = getBooleanStatus(line);
                        rep_count++;
                    }
                    
                    
               
                    
                    if (rep_count==3)
                    {
                        int rs = 0;
                        
                        if (reports[0])
                            rs += 1;
                        else if (reports[1])
                            rs += 2;
                        else if (reports[2])
                            rs += 4;

                        //System.out.println("Rs: " + rs);
                        
                        if (this.device_found)
                        {
                            return (100 + rs);
                        }
                        else
                            return 20;
                    }
                    
                }
                
            }
            
            //return 2;
            
            return 0;    
        }
        catch(Exception ex)
        {
            System.out.println("Exception: " + ex);
            return 1;
        }
    }
    
    
    private boolean getBooleanStatus(String text)
    {
        String val = text.substring(text.indexOf("=")+2, text.indexOf(";"));
        
        try
        {
            //System.out.println("val: '" + val + "'");
            boolean b = Boolean.parseBoolean(val);
            //System.out.println("b: " + b);
            return b;
        }
        catch(Exception ex)
        {
            System.out.println("Error with status.\n" + text);
            return false;
        }
        
    }
    

    
    public void preInitDevice()
    {
    }


    
    @Override
    public boolean hasPreInit()
    {
        return false;
    }
    
    
    
    
}
