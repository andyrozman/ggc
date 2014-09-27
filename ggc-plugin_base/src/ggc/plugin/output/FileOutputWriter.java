package ggc.plugin.output;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;

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
 *  Filename:     FileOutputWriter  
 *  Description:  Output Writer for writing to File.
 *  
 *  Author: Andy {andy@atech-software.com}
 */

public class FileOutputWriter extends AbstractOutputWriter
{

    BufferedWriter bw;

    /**
     * Constructor
     * 
     * @param fileName
     */
    public FileOutputWriter(String fileName)
    {
        super();

        try
        {
            bw = new BufferedWriter(new FileWriter(new File(fileName)));
        }
        catch (Exception ex)
        {
            System.out.println("Error opening file:" + ex);
        }
    }

    /**
     * Write Data to OutputWriter
     * 
     * @param data OutputWriterData instance
     */
    public void writeData(OutputWriterData data)
    {
        data.setOutputType(OutputWriterType.FILE);
        writeToFile(data.getDataAsString());
    }

    /**
     * Write Header
     */
    @Override
    public void writeHeader()
    {
        // header
        String dta = "=======================================================\n";
        dta += "==             Meter Tool Data Dump                  ==\n";
        dta += "=======================================================\n";

        writeToFile(dta);
    }

    /**
     * Write Device Identification
     */
    public void writeDeviceIdentification()
    {
        writeToFile(this.getDeviceIdentification().getInformation("; "));
    }

    private void writeToFile(String values)
    {
        try
        {
            bw.write(values);
            bw.flush();
        }
        catch (Exception ex)
        {
            System.out.println("Write to file failed: " + ex);
        }
    }

    /**
     * End Output
     */
    @Override
    public void endOutput()
    {
        try
        {
            bw.flush();
            bw.close();
        }
        catch (Exception ex)
        {
            System.out.println("Closing file failed: " + ex);
        }
    }

}
