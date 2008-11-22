
package ggc.meter.device.accuchek;

import ggc.meter.device.AbstractXmlMeter;
import ggc.plugin.device.DeviceIdentification;
import ggc.plugin.device.PlugInBaseException;
import ggc.plugin.manager.DeviceImplementationStatus;
import ggc.plugin.output.OutputWriter;
import ggc.plugin.protocol.ConnectionProtocols;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileFilter;
import java.io.FileReader;

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
 *  Filename:     AccuChekSmartPix  
 *  Description:  Super class for all AccuChek devices supported through SmartPix device
 * 
 *  Author: Andy {andy@atech-software.com}
 */


public abstract class AccuChekSmartPix extends AbstractXmlMeter //mlProtocol //implements SelectableInterface
{
    
    
    /**
     * Constructor
     */
    public AccuChekSmartPix()
    {
    }

    
    /**
     * Constructor
     * 
     * @param drive_letter
     * @param writer
     */
    public AccuChekSmartPix(String drive_letter, OutputWriter writer)
    {
        this.setConnectionPort(drive_letter);
        this.output_writer = writer; 
        this.output_writer.getOutputUtil().setMaxMemoryRecords(this.getMaxMemoryRecords());
        
        
//        this.xml_processor = new AccuChekSmartPixProcessor(this.output_writer);
        
        this.setMeterType("Roche", this.getName());
        
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
        return "SmartPix";
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
     * getInstructions - get instructions for device
     * 
     * @return instructions for reading data 
     */
/*    public String getInstructions()
    {
        return "INSTRUCTIONS_ACCUCHEK_SMART_PIX";
    }
  */
    
    
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
     * getImplementationStatus - Get Implementation Status 
     * 
     * @return implementation status as number
     * @see ggc.plugin.manager.DeviceImplementationStatus
     */
    public int getImplementationStatus() 
    {
        return DeviceImplementationStatus.IMPLEMENTATION_TESTING;
    }
    
    
    /**
     * getDeviceClassName - Get Class name of device implementation, used by Reflection at later time
     * 
     * @return class name as string
     */
    public String getDeviceClassName()
    {
        return "ggc.meter.device.accuchek.AccuChekSmartPix";
    }
    
    
    /**
     * getDeviceSpecialComment - special comment for device (this is needed in case that we need to display
     *    special comment about device (for example pix device, doesn't display anything till the end, which
     *    would be nice if user knew. 
     */
    public String getDeviceSpecialComment()
    {
        return "DEVICE_PIX_SPECIAL_COMMENT";
    }
    
    
    
    
    /**
     * Process Xml - This differs for Meter or/and Pump
     * @param file
     */
    public abstract void processXml(File file);    
    




    /** 
     * readDeviceDataFull
     */
    public void readDeviceDataFull() throws PlugInBaseException
    {
        // write preliminary device identification, based on class
        DeviceIdentification di = this.output_writer.getDeviceIdentification();
        
        di.company = "Accu-Chek";
        di.device_selected = "SmartPix Device Reader";
        
        di.device_identified = "Accu-Chek " + this.getName() + " [not identified]";

        this.output_writer.writeDeviceIdentification();
        
        // start working
        String drv = this.getConnectionPort();
        String cmd = drv + "\\TRG\\";

        this.writeStatus("PIX_ABORT_AUTOSCAN");
        
        //System.out.println("Abort auto scan");
        
        this.output_writer.setSpecialProgress(5);
        
        // abort auto scan
        File f = new File(cmd + "TRG09.PNG");
        f.setLastModified(System.currentTimeMillis());
        
        f = new File(cmd + "TRG03.PNG");
        f.setLastModified(System.currentTimeMillis());
        

        //this.writeStatus("PIX_READING");
        this.output_writer.setSpecialProgress(10);
        
        // read device  
        f = new File(cmd + "TRG09.PNG");
        f.setLastModified(System.currentTimeMillis());
        
        f = new File(cmd + "TRG00.PNG");
        f.setLastModified(System.currentTimeMillis());
        
        boolean found = false;
        sleep(2000);
        
        int count_el = 0;
        
        
        do
        {

            if (this.isDeviceStopped())
            {
                this.setDeviceStopped();
                found = true;
            }
            
            int st = readStatusFromConfig(drv);
            
            if (st==1)
            {
                this.writeStatus("PIX_UNRECOVERABLE_ERROR");
                this.output_writer.setSpecialProgress(100);

                //System.out.println("Unrecoverable error - Aborting");
                return;
            }
            else if (st==2)
            {
                
                this.writeStatus("PIX_FINISHED_READING");
                this.output_writer.setSpecialProgress(90);

                //System.out.println("Finished reading");
                return;
            }
            else if (st==4)
            {
                count_el += this.getNrOfElementsFor1s();
                //System.out.println("Reading elements: " + count_el);
                
                
                float procs_x = (count_el*(1.0f))/this.getMaxMemoryRecords();
                
                //int procs = (int)(procs_x * 100.0f);
                
                //System.out.println("Procents full: " + procs);
                //float procs_calc = 0.007f * procs;
                
                int pro_calc = (int)((0.2f + (0.007f * (procs_x * 100.0f)))*100.0f);
                
                //System.out.println("Procents: " + pro_calc);
                
                //this.writeStatus(String.format("PIX_READING_ELEMENT", pro_calc + " %"));
                this.writeStatus("PIX_READING_ELEMENT"); //, pro_calc + " %"));
                this.output_writer.setSpecialProgress(pro_calc);

            }
            else if (st==20)
            {
                this.writeStatus("PIX_DEVICE_NOT_FOUND");
                this.output_writer.setSpecialProgress(100);

                //System.out.println("Unrecoverable error - Aborting");
                return;
            }
            else if (st>99)
            {
                if (st==101)
                {
                    this.writeStatus("PIX_FINISHED_REPORT_READY");
                    //System.out.println("Finished reading. Report ready." );
                    this.output_writer.setSpecialProgress(95);

                    
                    File f1 = new File(drv + "\\REPORT\\XML");
                    
                    File[] fls = f1.listFiles(new FileFilter()
                    {

                        public boolean accept(File file)
                        {
                            return ((file.getName().toUpperCase().contains(".XML")) &&
                                    (file.getName().startsWith("G")));
                        }}
                    );
                    
                    
                    //processXml(fls[0]);
                    processXml(fls[0]);

                    this.output_writer.setSpecialProgress(100);
                    this.output_writer.setSubStatus(null);
                    
                    return;
                    
                }
                else
                {
                    this.writeStatus("PIX_FINISHED_REPORT_READY");
                    this.output_writer.setSpecialProgress(95);

                    return;
                }
            }
                
            
            sleep(1000);
            
            
        } while(found!=true);
        
        this.setDeviceStopped();
        //this.output_writer.setSubStatus(null);
        
        //System.out.println("We got out !!!!");
    }
    
    

    /** 
     * test
     */
    public void test()
    {
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
     * @throws PlugInBaseException
     */
    public void readConfiguration() throws PlugInBaseException
    {
    }
    

    /**
     * This is for reading device information. This should be used only if normal dump doesn't retrieve this
     * information (most dumps do). 
     * @throws PlugInBaseException
     */
    public void readInfo() throws PlugInBaseException
    {
    }
    
    
    
    
    
    private boolean isDeviceStopped()
    {
        if (this.output_writer.isReadingStopped())
            return true;
        
        return false;
        
    }
    
    
    private void setDeviceStopped()
    {
        this.output_writer.setSubStatus(null);
        this.output_writer.setSpecialProgress(100);
        this.output_writer.endOutput();
    }
    
    
    private void writeStatus(String text_i18n)
    {
        writeStatus(text_i18n, true);
    }
    
    
    private void writeStatus(String text_i18n, boolean process)
    {
        String tx = "";
        
        if (process)
            tx = ic.getMessage(text_i18n);
        else
            tx = text_i18n;
        
        this.output_writer.setSubStatus(tx);
//x        System.out.println(tx);
        // write log
        
    }
    

    /**
     * getNrOfElementsFor1s - How many elements are read in 1s (which is our refresh time)
     * @return number of elements
     */
    public abstract int getNrOfElementsFor1s();

    
    
    
    private void sleep(int ms)
    {
        try
        {
            Thread.sleep(ms);
        }
        catch(Exception ex)
        {
            
        }
        
    }

    private boolean device_found = false;
    
    
    // 0 = no status
    // 1 = error_found
    // 2 = finished
    private int readStatusFromConfig(String drive)
    {
        try
        {
            //boolean error_found = false;
            //boolean image_found = false;
            
            
            BufferedReader br = new BufferedReader(new FileReader(new File(drive + "\\REPORT\\SCAN.HTM")));
            
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
                    //System.out.println("Image: " + line);
                    if (line.contains("Scanning.gif"))
                    {
                        this.writeStatus("PIX_SCANNING");
                        this.output_writer.setSpecialProgress(15);
                        //System.out.println("Scanning for device");
                        return 0;
                    }
                    else if (line.contains("CrReport.png"))
                    {
                        this.writeStatus("PIX_CREATING_REPORT");
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
                    //System.out.println("L: " + line);
                    
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
    
    
    
    
    
    
    
    
    /**
     * hasSpecialProgressStatus - in most cases we read data directly from device, in this case we have 
     *    normal progress status, but with some special devices we calculate progress through other means.
     * @return true is progress status is special
     */
    public boolean hasSpecialProgressStatus()
    {
        return true;
    }
    
    
    
    
    
    
    
    
    /** 
     * Get Connection Protocol
     */
    public int getConnectionProtocol()
    {
        return ConnectionProtocols.PROTOCOL_MASS_STORAGE_XML;
    }
    
    
}
