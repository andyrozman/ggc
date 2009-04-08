package ggc.plugin.data;

import ggc.plugin.util.DataAccessPlugInBase;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.GregorianCalendar;
import java.util.Hashtable;

import org.jfree.util.Log;

import com.atech.utils.ATechDate;

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
 *  Filename:     DeviceValuesDay  
 *  Description:  Device Values Day, with table constructs for Daily Overview table
 * 
 *  Author: Andy {andy@atech-software.com}
 */

public class DeviceValuesRange 
{
	
	//private ArrayList<DeviceValuesEntry> list = null;
	private Hashtable<String,DeviceValuesDay> hash_table = null;
	DataAccessPlugInBase m_da;
	GregorianCalendar gc_from;
	GregorianCalendar gc_to;
	DeviceValuesEntry dve;

	
	/**
	 * Constructor
	 * 
	 * @param da
	 */
	public DeviceValuesRange(DataAccessPlugInBase da, GregorianCalendar from, GregorianCalendar to)
	{
	    this.m_da = da;
	    //list = new ArrayList<DeviceValuesEntry>();
	    hash_table = new Hashtable<String,DeviceValuesDay>();
	    this.gc_from = from;
	    this.gc_to = to;
	    dve = m_da.getDataEntryObject();
	}
	
	
	/**
	 * Add Entry
	 * 
	 * @param pve DeviceValuesEntry instance (or derivate)
	 */
	public void addEntry(DeviceValuesEntry pve)
	{
	    ATechDate atd = new ATechDate(pve.getDateTimeFormat(), pve.getDateTime());
	    
	    if (!this.hash_table.containsKey(atd.getDateFilenameString()))
	    {
	        DeviceValuesDay dvd = new DeviceValuesDay(this.m_da, atd.getGregorianCalendar());
	        dvd.addEntry(pve);
	        
	        this.hash_table.put(atd.getDateFilenameString(), dvd);
	    }
	    else
	    {
	        this.hash_table.get(atd.getDateFilenameString()).addEntry(pve);
	    }
	}
	
	
    /**
     * Add Entry
     * 
     * @param pve DeviceValuesEntry instance (or derivate)
     */
    public void addEntry(DeviceValuesDay dvd)
    {
        
        ATechDate atd = new ATechDate(dve.getDateTimeFormat(), dvd.getDateTimeGC());
        
        if (!this.hash_table.containsKey(atd.getDateFilenameString()))
        {
            this.hash_table.put(atd.getDateFilenameString(), dvd);
        }
        else
        {
            //Log.debug(message)
            System.out.println("addEntry problem (DeviceValuesDay)");
        }
    }
	

    public boolean isDayEntryAvailable(long dt)
    {
        ATechDate atd = new ATechDate(dve.getDateTimeFormat(), dt);
        return (this.hash_table.containsKey(atd.getDateFilenameString()));
    }
    
    
	
}	
