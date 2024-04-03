package ggc.plugin.device.impl.accuchek;

import java.io.File;

import javax.swing.*;
import javax.swing.filechooser.FileFilter;

import ggc.plugin.data.GGCPlugInFileReaderContext;
import ggc.plugin.output.OutputWriter;
import ggc.plugin.protocol.XmlProtocolFile;
import ggc.plugin.util.DataAccessPlugInBase;

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
 *  Filename:     FRC_AccuChekSmartPixXml 
 *  Description:  Reader for AccuChek Smart Pix file
 * 
 *  Author: Andy {andy@atech-software.com}
 */

public class FRC_AccuChekSmartPixXml extends XmlProtocolFile implements GGCPlugInFileReaderContext
{

    AccuChekSmartPix pix;


    /**
     * Constructor (for DeviceV2)
     *
     * @param da
     * @param pix_
     */
    public FRC_AccuChekSmartPixXml(DataAccessPlugInBase da, AccuChekSmartPix pix_)
    {
        super(da);
        this.pix = pix_;
    }


    /**
     * Constructor
     * 
     * @param da
     * @param ow 
     * @param pix_ 
     */
    public FRC_AccuChekSmartPixXml(DataAccessPlugInBase da, OutputWriter ow, AccuChekSmartPix pix_)
    {
        super(da, ow);
        this.pix = pix_;
    }


    public String getFileDescription()
    {
        return "Accu-Chek SmartPix Xml";
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
        return ".xml";
    }


    public String getFullFileDescription()
    {
        return "Accu-Chek SmartPix Xml (XML)";
    }


    public boolean hasSpecialSelectorDialog()
    {
        return false;
    }


    public void readFile(String filename)
    {
        try
        {
            pix.processXml(new File(filename));
        }
        catch (Exception ex)
        {
            System.out.println("Error reading file: " + ex);
        }

    }

    int i = 0;
    String tmp_time;


    public FileFilter getFileFilter()
    {

        return new FileFilter()
        {

            @Override
            public boolean accept(File f)
            {
                if (f.isDirectory())
                    return true;

                // System.out.println(f.getName());
                // System.out.println(f.getName().toLowerCase().endsWith(getFileExtension()));

                // System.out.println(f.getName());
                // System.out.println(f.getName());

                return f.getName().toLowerCase().endsWith(getFileExtension())
                        && f.getName().toLowerCase().startsWith(pix.getFirstLetterForReport().toLowerCase());
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


    public void setOutputWriter(OutputWriter ow)
    {
        this.output_writer = ow;
        this.pix.setOutputWriter(ow);
    }

}
