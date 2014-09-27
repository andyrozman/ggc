package ggc.core.plugins;

import ggc.core.db.GGCDb;
import ggc.core.db.hibernate.DayValueH;

import java.util.Hashtable;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.atech.db.DbDataReaderAbstract;

/**
 *  Application:   GGC - GNU Gluco Control
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
 *  Filename:     GGCDataReader  
 *  Description:  Class for reading data in separate thread
 * 
 *  Author: andyrozman {andy@atech-software.com}  
 */

public class GGCDataReader extends DbDataReaderAbstract
{

    private boolean running = true;
    private GGCDb db = null;
    int type = 0;
    Hashtable<String, DayValueH> data_meter = null;
    private static Log log = LogFactory.getLog(GGCDataReader.class);

    /**
     * Data: None
     */
    public static final int DATA_NONE = 0;

    /**
     * Data: Meter
     */
    public static final int DATA_METER = 1;

    /**
     * Constructor
     * 
     * @param db
     * @param type
     */
    public GGCDataReader(GGCDb db, int type)
    {
        this.db = db;
        this.type = type;
        this.data_meter = new Hashtable<String, DayValueH>();
        this.setStatus(DbDataReaderAbstract.STATUS_READY);
    }

    /** 
     * Run - method for running thread
     */
    @Override
    public void run()
    {

        while (running)
        {
            // System.out.println("run.running");
            log.info("GGCDataReader - Started");

            try
            {
                this.setStatus(DbDataReaderAbstract.STATUS_READING);

                this.data_meter = this.db.getMeterValues();

                this.setStatus(DbDataReaderAbstract.STATUS_FINISHED_READING);

            }
            catch (Exception ex)
            {
                this.setStatus(DbDataReaderAbstract.STATUS_FINISHED_READING_ERROR);
                // System.out.println("Exception: " + ex);
                log.error("GGCDataReader Exception: " + ex, ex);
                // ex.printStackTrace();
                running = false;
            }

            running = false;

        } // while

        log.info("GGCDataReader - Finished");
        // System.out.println("Exited runner");
    }

    /**
     * Get Data - returns data
     * 
     * @return data as Object
     */
    @Override
    public Object getData()
    {
        return this.data_meter;
    }

    /**
     * Get Type Of Data - returns type of data
     * 
     * @return int as type
     */
    @Override
    public int getTypeOfData()
    {
        return this.type;
    }

}
