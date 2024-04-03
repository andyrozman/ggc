package ggc.meter.data;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ggc.meter.data.db.GGCMeterDb;
import ggc.meter.util.DataAccessMeter;
import ggc.plugin.data.DeviceValuesEntryInterface;
import ggc.plugin.gui.OldDataReaderAbstract;

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

    private static final Logger LOG = LoggerFactory.getLogger(MeterDataReader.class);

    GGCMeterDb db = null;
    DataAccessMeter m_da = null;


    /**
     * Constructor
     * 
     * @param da
     */
    public MeterDataReader(DataAccessMeter da)
    {
        super(da);
        m_da = da;
        // db = dataAccess.getDb();

        // System.out.println("db: " )
    }


    /**
     * Get Max Entries
     */
    @Override
    public void getMaxEntries()
    {
        db = m_da.getDb();
        this.allEntries = db.getAllElementsCount();
    }


    /**
     * Read Old entries
     */
    @Override
    public Map<String, DeviceValuesEntryInterface> readOldEntries()
    {
        return db.getMeterValues(this);
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
    @Override
    public void writeStatus(int current_entry)
    {
        if (current_entry == -1)
        {
            this.deviceReaderRunner.setOldDataReadingProgress(0);
            LOG.debug("Old Data reading progress [" + m_da.getApplicationName() + "]: 0% not started");
        }
        else if (current_entry == 0)
        {
            this.deviceReaderRunner.setOldDataReadingProgress(40);
            LOG.debug("Old Data reading progress [" + m_da.getApplicationName() + "]: 40% read from database");
            LOG.debug("Old Data reading progress [" + m_da.getApplicationName()
                    + "]: Started to sort through data (progress will not be displayed)");
        }
        else if (current_entry == -2)
        {
            this.deviceReaderRunner.setOldDataReadingProgress(100);
        }
        else
        {
            int proc = this.getElementProcent(current_entry);

            float proc_total = (proc * 1.0f + 40.0f) / 140.0f * 100.0f;
            int proc_total_i = (int) proc_total;

            // LOG.debug("Old Data reading progress [" +
            // dataAccess.getApplicationName() + "]: " + proc_total_i + " %" );
            this.deviceReaderRunner.setOldDataReadingProgress(proc_total_i);

            /*
             * try
             * {
             * Thread.sleep(1);
             * }
             * catch(InterruptedException ex) {}
             */

        }
    }

}
