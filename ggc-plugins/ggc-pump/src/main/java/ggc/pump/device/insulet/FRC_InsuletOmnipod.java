package main.java.ggc.pump.device.insulet;

import java.io.File;

import javax.swing.*;
import javax.swing.filechooser.FileFilter;

import ggc.plugin.data.GGCPlugInFileReaderContext;
import ggc.plugin.device.DownloadSupportType;
import ggc.plugin.output.OutputWriter;

/**
 *  Application:   GGC - GNU Gluco Control
 *  Plug-in:       CGMS Tool (support for Pump devices)
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
 *  Filename:     FRC_MinimedCarelink  
 *  Description:  Minimed Carelink File Handler
 * 
 *  Author: Andy {andy@atech-software.com}
 */

public class FRC_InsuletOmnipod implements GGCPlugInFileReaderContext
{

    OutputWriter outputWriter;
    DownloadSupportType supportType;


    /**
     * Constructor
     *
     * @param supportType
     */
    public FRC_InsuletOmnipod(DownloadSupportType supportType)
    {
        this.supportType = supportType;
    }


    public String getFileDescription()
    {
        return "Omnipod Backup File";
    }


    /**
     * Get File Download Panel
     * 
     * @return
     */
    public JPanel getFileDownloadPanel()
    {
        return null;
    }


    public String getFileExtension()
    {
        return ".ibf";
    }


    public String getFullFileDescription()
    {
        return "Omnipod Backup File (IBF)";
    }


    public boolean hasSpecialSelectorDialog()
    {
        return false;
    }


    public void readFile(String filename)
    {
        try
        {
            InsuletReader reader = new InsuletReader(filename, outputWriter);

            if (this.supportType == DownloadSupportType.DownloadConfigFile)
            {
                reader.readConfiguration();
            }
            else
            {
                reader.readData();
            }

        }
        catch (Exception ex)
        {
            ex.printStackTrace();
        }

    }


    public FileFilter getFileFilter()
    {

        return new FileFilter()
        {

            @Override
            public boolean accept(File f)
            {
                if (f.isDirectory())
                    return true;

                return f.getName().toLowerCase().endsWith(getFileExtension());
            }


            @Override
            public String getDescription()
            {
                return getFileDescription() + " (" + getFileExtension() + ")";
            }

        };

    }


    public void goToNextDialog(JDialog currentDialog)
    {
    }


    @Override
    public String toString()
    {
        return this.getFullFileDescription();
    }


    public void setOutputWriter(OutputWriter outputWriter)
    {
        this.outputWriter = outputWriter;
    }

}
