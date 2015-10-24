package ggc.cgms.data;

import java.util.Hashtable;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ggc.cgms.data.db.GGC_CGMSDb;
import ggc.cgms.util.DataAccessCGMS;
import ggc.plugin.data.DeviceValuesEntryInterface;
import ggc.plugin.gui.OldDataReaderAbstract;

/**
 *  Application:   GGC - GNU Gluco Control
 *  Plug-in:       CGMS Tool (support for CGMS devices)
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
 *  Filename:     CGMSDataReader
 *  Description:  CGMS Data Reader for old data
 * 
 *  Author: Andy {andy@atech-software.com}
 */

public class CGMSDataReader extends OldDataReaderAbstract
{

    private static final Logger LOG = LoggerFactory.getLogger(CGMSDataReader.class);

    GGC_CGMSDb db = null;
    DataAccessCGMS m_da = null;


    /**
     * Constructor
     * 
     * @param da
     */
    public CGMSDataReader(DataAccessCGMS da)
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
        // DataAccessCGMS.notImplemented("CGMSDataReader::getMaxEntries()");
        db = m_da.getDb();
        this.all_entries = db.getAllElementsCount();
    }


    /**
     * Read Old entries
     */
    @Override
    public Hashtable<String, DeviceValuesEntryInterface> readOldEntries()
    {
        return db.getCGMSValues(this);
        // DataAccessCGMS.notImplemented("CGMSDataReader::readOldEntries()");
        // return null;
        // return db.getPumpValues(this);
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
            LOG.debug("Old Data reading progress +20% [" + m_da.getApplicationName() + "]");
        }
        else
        {
            this.current_entry = centry;
        }

        int proc = this.getElementProcent(current_entry);

        float proc_total = (proc * 1.0f + db_reading) / 120.0f * 100.0f;
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
