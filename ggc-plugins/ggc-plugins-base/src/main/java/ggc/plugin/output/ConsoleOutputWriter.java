package ggc.plugin.output;

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
 *  Filename:     ConsoleOutputWriter  
 *  Description:  Output Writer for writing to Console.
 * 
 *  Author: Andy {andy@atech-software.com}
 */

public class ConsoleOutputWriter extends AbstractOutputWriter
{

    /**
     * Constructor
     */
    public ConsoleOutputWriter()
    {
        super();
    }


    /**
     * Write Data to OutputWriter
     * 
     * @param data OutputWriterData instance
     */
    public void writeData(OutputWriterData data)
    {
        data.setOutputType(OutputWriterType.CONSOLE);
        System.out.println(data.getDataAsString());
    }


    public void writeConfigurationData(OutputWriterConfigData configData)
    {
        System.out.println("[" + configData.getGroup().getName() + "]   " + configData.getDataKey() + " = "
                + configData.getDataValue());
    }


    /**
     * Write Header
     */
    @Override
    public void writeHeader()
    {
        // header
        System.out.println("=======================================================");
        System.out.println("==             " + this.getPluginName() + " Data Dump                  ==");
        System.out.println("=======================================================");
    }


    /**
     * Write Device Identification
     */
    public void writeDeviceIdentification()
    {
        System.out.println(this.getDeviceIdentification().getInformation("* "));
    }


    /**
     * End Output
     */
    @Override
    public void endOutput()
    {
        System.out.println("=======================================================");
    }

}
