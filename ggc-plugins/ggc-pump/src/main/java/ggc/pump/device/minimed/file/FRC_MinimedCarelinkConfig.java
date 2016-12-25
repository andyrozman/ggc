package main.java.ggc.pump.device.minimed.file;

import java.io.File;

import javax.swing.*;
import javax.swing.filechooser.FileFilter;

import ggc.plugin.data.GGCPlugInFileReaderContext;
import ggc.plugin.device.impl.minimed.file.MinimedCareLink;
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
 *  Filename:     FRC_MinimedCarelink  
 *  Description:  Minimed Carelink File Handler
 * 
 *  Author: Andy {andy@atech-software.com}
 */

public class FRC_MinimedCarelinkConfig extends XmlProtocolFile implements GGCPlugInFileReaderContext
{

    // ArrayList<CGMSValuesSubEntry> list = new ArrayList<CGMSValuesSubEntry>();
    // CGMSValuesTableModel cvtm = null;

    /**
     * Constructor
     * 
     * @param da
     * @param ow 
     */
    public FRC_MinimedCarelinkConfig(DataAccessPlugInBase da, OutputWriter ow)
    {
        super(da, ow);
    }

    public String getFileDescription()
    {
        return "Carelink Export File";
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
        return ".csv";
    }

    public String getFullFileDescription()
    {
        return "Carelink Export File (CSV)";
    }

    public boolean hasSpecialSelectorDialog()
    {
        return false;
    }

    public void readFile(String filename)
    {
        try
        {
            MinimedCareLinkPump mcl = new MinimedCareLinkPump(m_da, this.output_writer,
                    MinimedCareLink.READ_DEVICE_CONFIG_DATA);
            mcl.parseExportFile(new File(filename));
        }
        catch (Exception ex)
        {
            ex.printStackTrace();
        }
        // System.out.println("i = " + i + " lisr: " + list.size());

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

    public void setOutputWriter(OutputWriter ow)
    {
        this.output_writer = ow;
    }

}
