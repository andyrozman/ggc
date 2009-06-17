package ggc.meter.data;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import ggc.meter.data.db.GGCMeterDb;
import ggc.meter.util.DataAccessMeter;
import ggc.plugin.gui.OldDataReaderAbstract;
import ggc.plugin.util.DataAccessPlugInBase;

/**
 *  Application:   GGC - GNU Gluco Control
 *  Plug-in:       Pump Tool (support for Pump devices)
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
 *  Filename:     MeterDataReader
 *  Description:  Meter Data Reader for old data
 * 
 *  Author: Andy {andy@atech-software.com}
 */

public class MeterDataReader extends OldDataReaderAbstract
{
    private static Log log = LogFactory.getLog(MeterDataReader.class);
    
    GGCMeterDb db = null;
    DataAccessMeter m_da = DataAccessMeter.getInstance();
    
    /**
     * Constructor
     * 
     * @param da
     */
    public MeterDataReader(DataAccessPlugInBase da)
    {
        super(da);
        db = m_da.getDb();
    }
    
    /**
     * Get Max Entries
     */
    @Override
    public void getMaxEntries()
    {
        db.getAllElementsCount();
    }

    /**
     * Read Old entries
     */
    @Override
    public void readOldEntries()
    {
        db.getMeterValues(this);
        // TODO Auto-generated method stub
    }
    

    /**
     * Write status of reading
     * this must be implemebted by all children, since some children will require more reading into database, 
     * and readings should be handled separately. Working with database is about 40% of progress (with one reading)
     * 
     * current_entry: 
     *   -1 = not started
     *   0 = db read finished, nothing read
     *   
     * 
     * 
     * @param current_entry
     */
    public void writeStatus(int current_entry)
    {
        if (current_entry==-1)
        {
            this.m_drr.setOldDataReadingProgress(0);
            log.debug("Old Data reading progress [" + m_da.getApplicationName() +  "]: 0% not started");
        }
        else if (current_entry==0)
        {
            this.m_drr.setOldDataReadingProgress(40);
            log.debug("Old Data reading progress [" + m_da.getApplicationName() +  "]: 40% read from database");
        }
        else
        {
            int proc = this.getElementProcent(current_entry);
            
            float proc_total = ((proc + 40.0f) * 1.0f) / 140.0f;  
            
            int proc_total_i = (int)proc_total;
            log.debug("Old Data reading progress [" + m_da.getApplicationName() +  "]: " + proc_total_i + " %" );
        }
    }
    
    

}



