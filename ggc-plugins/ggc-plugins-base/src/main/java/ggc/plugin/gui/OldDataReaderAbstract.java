package ggc.plugin.gui;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ggc.plugin.data.DeviceValuesEntryInterface;
import ggc.plugin.util.DataAccessPlugInBase;

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
 *  Filename:     DeviceReaderRunner
 *  Description:  This is separate thread class to get current data from database in order to 
 *                compare it later.
 * 
 *  Author: Andy {andy@atech-software.com}
 */

// Try to assess possibility of super-classing

public abstract class OldDataReaderAbstract
{

    private static final Logger LOG = LoggerFactory.getLogger(OldDataReaderAbstract.class);

    protected DeviceReaderRunner deviceReaderRunner;
    protected DataAccessPlugInBase dataAccess;

    protected int allEntries = 0;
    protected int currentEntry = 0;


    /**
     * Constructor
     * 
     * @param da 
     */
    public OldDataReaderAbstract(DataAccessPlugInBase da)
    {
        this.dataAccess = da;
    }


    /**
     * Set DeviceReaderRunner instance
     * 
     * @param drr
     */
    public void setDeviceReadRunner(DeviceReaderRunner drr)
    {
        this.deviceReaderRunner = drr;
        getMaxEntries();
    }


    /**
     * Get Max Entries
     */
    public abstract void getMaxEntries();


    /**
     * Read Old entries (data is returned in form of Hashtable<String,Object>. What is stored there will depend
     * from plugin to plugin)
     * @return 
     */
    public abstract Map<String, DeviceValuesEntryInterface> readOldEntries();


    /**
     * Write status of reading
     * this must be implemebted by all children, since some children will require more reading into database, 
     * and readings should be handled separately. Working with database is about 40% of progress (with one reading
     * 
     * @param current_entry
     */
    public void writeStatus(int current_entry)
    {
        // System.out.println("Progress: " + current_entry + "/" +
        // this.allEntries + " = ");

        float ee = current_entry / (1.0f * this.allEntries);
        ee *= 100;

        int ee_i = (int) ee;

        this.deviceReaderRunner.setOldDataReadingProgress(ee_i);
        LOG.debug("Old Data reading progress [" + dataAccess.getApplicationName() + "]: " + ee_i);
        // System.out.println("Progress: " + current_entry + "/" +
        // this.allEntries + " = " + ee_i);
    }


    /**
     * Get Element Procent (this determines procent of reading by comparing data to full set)
     * @param current_entry
     * @return
     */
    public int getElementProcent(int current_entry)
    {
        float ee = current_entry / (1.0f * this.allEntries);
        ee *= 100.0f;

        int ee_i = (int) ee;
        // System.out.println("Element Progress: " + current_entry + "/" +
        // this.allEntries + " = " + ee_i);
        currentEntry = current_entry;
        return ee_i;
    }


    /**
     * Finish reading
     */
    public void finishReading()
    {
        if (currentEntry != this.allEntries)
        {
            LOG.warn("It seems that not all data was read (" + this.currentEntry + "/" + this.allEntries + ")");
            this.deviceReaderRunner.setOldDataReadingProgress(100);
        }
        else if (allEntries == 0)
        {
            LOG.debug("Database was empty. Nothing was read.");
            this.deviceReaderRunner.setOldDataReadingProgress(100);
        }
    }

}
