package ggc.plugin.gui;

import ggc.plugin.cfg.DeviceConfigEntry;
import ggc.plugin.device.DeviceInterface;
import ggc.plugin.util.DataAccessPlugInBase;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 *  Application:   GGC - GNU Gluco Control
 *  Plug-in:       Base PlugIn 
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
 *  Filename:     DeviceReaderRunner
 *  Description:  This is separate thread class to get current data from database in order to 
 *                compare it later.
 * 
 *  Author: Andy {andy@atech-software.com}
 */


//Try to assess possibility of super-classing


public abstract class OldDataReaderAbstract 
{

    private static Log log = LogFactory.getLog(OldDataReaderAbstract.class);
    
    DeviceReaderRunner m_drr;
    
    boolean running = true;
    DataAccessPlugInBase m_da;
    
    protected int all_entries = 0;
    
    
    /**
     * Constructor
     * 
     * @param da 
     */
    public OldDataReaderAbstract(DataAccessPlugInBase da)
    {
        //this.m_drr = drr;
        this.m_da = da;
    }
    
    
    public void setDeviceReadRunner(DeviceReaderRunner drr)
    {
        this.m_drr = drr;
        getMaxEntries();
    }
    
    
    
    /**
     * Get Max Entries
     */
    public abstract void getMaxEntries();

    
    /**
     * Read Old entries
     */
    public abstract void readOldEntries();
    

    /**
     * Write status of reading
     * 
     * @param current_entry
     */
    public void writeStatus(int current_entry)
    {
        float ee = ((float)current_entry)/(1.0f*this.all_entries);
        ee *= 100;
        
        int ee_i = (int)ee;
        
        this.m_drr.setOldDataReadingProgress(ee_i);
        log.debug("Old Data reading progress [" + m_da.getApplicationName() +  "]: " + ee_i );
    }
    
    
}