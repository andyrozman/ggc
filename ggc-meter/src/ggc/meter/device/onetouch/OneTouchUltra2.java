package ggc.meter.device.onetouch;

import com.atech.utils.data.HexUtils;

import ggc.meter.manager.MeterDevicesIds;
import ggc.plugin.manager.company.AbstractDeviceCompany;
import ggc.plugin.output.OutputWriter;
import ggc.plugin.util.DataAccessPlugInBase;

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
 *  Filename:     OneTouchUltra2
 *  Description:  Support for LifeScan OneTouch Ultra 2 Meter
 * 
 *  Author: Andy {andy@atech-software.com}
 */

// done, not tested
// not done (protocol not same as OT Ultra)
// trying new sollution 

public class OneTouchUltra2 extends OneTouchMeter
{
    // No picture
    
    HexUtils hex_utils = new HexUtils();
    

    /**
     * Constructor used by most classes
     * 
     * @param portName
     * @param writer
     */
    public OneTouchUltra2(String portName, OutputWriter writer)
    {
        super(portName, writer);
    }

    
    /**
     * Constructor
     * 
     * @param comm_parameters
     * @param writer
     * @param da 
     */
    public OneTouchUltra2(String comm_parameters, OutputWriter writer, DataAccessPlugInBase da)
    {
        super(comm_parameters, writer, da);
    }
    
    

    /**
     * Constructor
     */
    public OneTouchUltra2()
    {
        super();
    }
    
    /**
     * Constructor for device manager
     * 
     * @param cmp
     */
    public OneTouchUltra2(AbstractDeviceCompany cmp)
    {
        super(cmp);
    }

    
    /**
     * getName - Get Name of meter. 
     * 
     * @return name of meter
     */
    public String getName()
    {
        return "One Touch Ultra 2";
    }

    
     /**
     * getDeviceClassName - Get class name of device
     */
    public String getDeviceClassName()
    {
        return "ggc.meter.device.onetouch.OneTouchUltra2";
    }

    
    /**
     * getDeviceId - Get Device Id, within MgrCompany class 
     * Should be implemented by device class.
     * 
     * @return id of device within company
     */
    public int getDeviceId()
    {
        return MeterDevicesIds.METER_LIFESCAN_ONE_TOUCH_ULTRA_2;
    }

    
    /**
     * getIconName - Get Icon of meter
     * 
     * @return icon name
     */
    public String getIconName()
    {
        // TODO fill in
        return null;
    }

    
    /**
     * getInstructions - get instructions for device
     * 
     * @return instructions for reading data 
     */
    public String getInstructions()
    {
        return "INSTRUCTIONS_LIFESCAN_OFF";
    }
    

    /**
     * Maximum of records that device can store
     */
    public int getMaxMemoryRecords()
    {
        return 500;
    }

    /**
     * getShortName - Get short name of meter. 
     * 
     * @return short name of meter
     */
    public String getShortName()
    {
        return "Ultra 2";
    }

    
    
    /** 
     * readDeviceDataFull
     */
    public void readDeviceDataFull()
    {
        
        try
        {
            String cmd_prefix = "11" + "0D" + "0A";
            
            String cmd = cmd_prefix + "44" + "4D" + "3F";  // DM?  444d3f
            
            write(hex_utils.reconvert(cmd));
            
            /*
            write("D".getBytes());
            waitTime(100);
            write("M".getBytes());
            waitTime(100);
            write("?".getBytes());
            waitTime(100);
            */
            
            waitTime(1000);
            
            String line;

            //System.out.println("Serial Number: " + this.readLine());
            //System.out.println("Serial Number: " + this.readLine());
            
            
            while((line=this.readLine())==null)
            {
                System.out.println("Serial Number1: " + line);
            }
            
            System.out.println("Serial Number2: " + line);
            //System.out.println("Serial Number: " + this.readLine());
            //System.out.println("Serial Number: " + this.readLine());
            
            
            cmd = cmd_prefix + "44" + "4D" + "50";  // DMP
            
            write(hex_utils.reconvert(cmd));
            

            waitTime(100);
            
            //write()
            
/*            
            write("D".getBytes());
            waitTime(100);
            write("M".getBytes());
            waitTime(100);
            write("P".getBytes());
            waitTime(100);
  */          
    
            
            while (((line = this.readLine()) != null) && (!isDeviceStopped(line)))
            {
                
                System.out.println(line);
                processEntry(line);
                
                System.out.println(this.entries_current + "/" + this.entries_max  );
                
                //if (line==null)
                //    break;
                
            }
            
            this.output_writer.setSpecialProgress(100);
            this.output_writer.setSubStatus(null);
        
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
        
        //this.output_writer.setStatus(100);
        System.out.println("Reading finsihed");
        
    }
    
    
    
    
    
    
    
}