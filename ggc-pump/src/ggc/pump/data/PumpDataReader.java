package ggc.pump.data;

import java.util.Hashtable;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import ggc.plugin.data.DeviceValuesEntryInterface;
import ggc.plugin.gui.OldDataReaderAbstract;
import ggc.pump.db.GGCPumpDb;
import ggc.pump.util.DataAccessPump;

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
 *  Filename:     PumpDataReader
 *  Description:  Pump Data Reader for old data
 * 
 *  Author: Andy {andy@atech-software.com}
 */

public class PumpDataReader extends OldDataReaderAbstract
{

    private static Log log = LogFactory.getLog(PumpDataReader.class);

    GGCPumpDb db = null;
    DataAccessPump m_da = null;


    /**
     * Constructor
     * 
     * @param da
     */
    public PumpDataReader(DataAccessPump da)
    {
        super(da);
        m_da = da;
    }


    /**
     * Get Max Entries
     */
    @Override
    public void getMaxEntries()
    {
        db = m_da.getDb();
        this.all_entries = db.getAllElementsCount();
    }


    /**
     * Read Old entries
     */
    @Override
    public Hashtable<String, DeviceValuesEntryInterface> readOldEntries()
    {
        return db.getPumpValues(this);
    }

    float db_reading = 0.0f;
    int current_entry = 0;


    /**
     * Write status of reading
     * this must be implemebted by all children, since some children will require more reading into database, 
     * and readings should be handled separately. Working with database is about 40% of progress (with one reading)
     * 
     * current_entry: 
     *   -1 = not started
     *   0 = db read finished, nothing read
     * 
     * @param centry
     */
    @Override
    public void writeStatus(int centry)
    {
        if (centry == -1)
        {
            db_reading += 20.0f;
            log.debug("Old Data reading progress +20% [" + m_da.getApplicationName() + "]");
        }
        else
        {
            this.current_entry = centry;
        }

        int proc = this.getElementProcent(current_entry);

        float proc_total = (proc * 1.0f + db_reading) / 160.0f * 100.0f;
        int proc_total_i = (int) proc_total;

        this.m_drr.setOldDataReadingProgress(proc_total_i);

        /*
         * try
         * {
         * Thread.sleep(1);
         * }
         * catch(InterruptedException ex) {}
         */

    }

}
