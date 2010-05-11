package ggc.plugin.device.impl.accuchek;

import ggc.plugin.output.OutputWriter;
import ggc.plugin.util.DataAccessPlugInBase;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;

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


public class AccuChekSmartPixReaderV3 extends AccuChekSmartPixReaderAbstract
{

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
    }
    
    
    /**
     * 
     */
    public AccuChekSmartPixReaderV3()
    {
        super();
    }
    
    
    public void readDevice()
    {
        try
        {
            BufferedWriter out = new BufferedWriter(new OutputStreamWriter(
                new FileOutputStream(new File("/media/SMART_PIX/file.txt")),"ISO-8859-1"));
            
            out.write("SL42-B RemoteControl File\n" + "Command=Abort");
            out.flush();
            out.close();
        }
        catch(Exception ex)
        {
            System.out.println("Exception: " + ex);
        }
            
    }

    
    /**
     * Read Device
     * 
     * @param command
     */
    public void readDevice(String command)
    {
        try
        {
            System.out.println("Send " + command);
            BufferedWriter out = new BufferedWriter(new OutputStreamWriter(
                new FileOutputStream(new File("/media/SMART_PIX/file.txt")),"ISO-8859-1"));
            
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
    
    
    
    /**
     * Test
     * 
     * @param args
     */
    public static void main(String[] args)
    {
        AccuChekSmartPixReaderV3 aa = new AccuChekSmartPixReaderV3();
        
        aa.readDevice("Abort");
        aa.sleep(5000);
        
        aa.readDevice("ReadDevice");
        aa.sleep(2000);

        /*
        aa.readDevice("Autoscan");
        aa.sleep(10000);
        
        aa.readDevice("Abort");
        aa.sleep(10000);

        aa.readDevice("Autoscan");
        aa.sleep(10000);
        */
    }
    
    
}
