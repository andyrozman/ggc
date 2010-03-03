package ggc.pump.device.cozmo;

import ggc.plugin.device.impl.CoPilot;
import ggc.plugin.util.DataAccessPlugInBase;

import java.io.File;

import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.filechooser.FileFilter;

import com.atech.utils.file.FileReaderContext;

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
 *  Filename:     Dexcom 7 Plus  
 *  Description:  Dexcom 7 Plus implementation (just settings)
 * 
 *  Author: Andy {andy@atech-software.com}
 */

public class FRC_CoPilotXMLPump extends CoPilot implements FileReaderContext
{

    
    
    /**
     * @param da
     */
    public FRC_CoPilotXMLPump(DataAccessPlugInBase da)
    {
        super(da);
    }

    public String getFileDescription()
    {
        return "DM3 Dexcom Software Export";
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
        return ".txt";
    }

    public String getFullFileDescription()
    {
        return "DM3 Dexcom Software Export (TXT)";
    }
    
    
    public boolean hasSpecialSelectorDialog()
    {
        return false;
    }

    
    /*
    public voidreadFile(String filename)
    {
        try
        {
            cvtm = (CGMSValuesTableModel)m_da.getDeviceDataHandler().getDeviceValuesTableModel();
            
            BufferedReader br = new BufferedReader(new FileReader(filename));
            
            String line = null;
            
            while ((line=br.readLine()) != null)
            {
                processLine(line);
            }
        
        }
        catch (Exception ex)
        {
            ex.printStackTrace();
        }
        System.out.println("i = " + i + " lisr: " + list.size());
        
    }
    */
    
    int i = 0;
    String tmp_time;
    
    
    /*
    private void addEntry(CGMSValuesSubEntry entry)
    {
        this.list.add(entry);
        this.cvtm.addEntry(entry); 
    }*/
    
    

    public FileFilter getFileFilter()
    {
        
        return new FileFilter() 
        {

            @Override
            public boolean accept(File f)
            {
                if (f.isDirectory())
                    return true;
                
                return (f.getName().toLowerCase().endsWith(getFileExtension()));
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
    
    
    
    
    public String toString()
    {
        return this.getFullFileDescription();
    }
    
    
    
    
    

}
