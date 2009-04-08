package ggc.plugin.data;

import ggc.plugin.util.DataAccessPlugInBase;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.GregorianCalendar;
import java.util.Hashtable;

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

public class DeviceValuesDay 
{
	
	private ArrayList<DeviceValuesEntry> list = null;
	private Hashtable<String,DeviceValuesEntry> table = null;
	DataAccessPlugInBase m_da;
	GregorianCalendar gc_today;

	
	/**
	 * Constructor
	 * 
	 * @param da
	 */
	public DeviceValuesDay(DataAccessPlugInBase da)
	{
	    this.m_da = da;
	    list = new ArrayList<DeviceValuesEntry>();
	    table = new Hashtable<String,DeviceValuesEntry>();
	}
	

    /**
     * Constructor
     * 
     * @param da
     */
    public DeviceValuesDay(DataAccessPlugInBase da, GregorianCalendar gc)
    {
        this.m_da = da;
        list = new ArrayList<DeviceValuesEntry>();
        table = new Hashtable<String,DeviceValuesEntry>();
        this.gc_today = gc;
    }
	
	
	
	/**
	 * Add Entry
	 * 
	 * @param pve DeviceValuesEntry instance (or derivate)
	 */
	public void addEntry(DeviceValuesEntry pve)
	{
	    this.list.add(pve);
	    
	    ATechDate atd = new ATechDate(pve.getDateTimeFormat(), pve.getDateTime());
	    
	    if (!this.table.containsKey(atd.getTimeString()))
	    {
	        this.table.put(atd.getTimeString(), pve);
	    }
	    
	}
	
	/**
	 * Is Entry Available
	 * 
	 * @param datetime
	 * @return
	 */
	public boolean isEntryAvailable(long datetime)
	{
	    ATechDate atd = new ATechDate(m_da.getDataEntryObject().getDateTimeFormat(), datetime);
	    return this.table.containsKey(atd.getTimeString());
	}
	
	/**
	 * Get Entry
	 * 
	 * @param dt
	 * @return
	 */
	public DeviceValuesEntry getEntry(long dt)
	{
        ATechDate atd = new ATechDate(m_da.getDataEntryObject().getDateTimeFormat(), dt);
        return this.table.get(atd.getTimeString());
	}
	
	
	/**
	 * Set Date Time GC
	 * @param gc
	 */
	public void setDateTimeGC(GregorianCalendar gc)
	{
	    this.gc_today = gc;
	}

	
    /**
     * Set Date Time GC
     * @param gc
     */
    public GregorianCalendar getDateTimeGC()
    {
        return this.gc_today;
    }
	
	
	
    /**
     * Get Column Width
     * 
     * @param column
     * @param width
     * @return
     */
    public int getColumnWidth(int column, int width)
    {
        float mult;
        switch(column)
        {
            case 0:
                mult=0.1f;
                
                
                    
            default:
                mult=0.2f;
        }

        return (int)(mult*width);
    }
	
	
	
	/**
	 * Get Column Count
	 * 
	 * @return
	 */
	public int getColumnCount()
	{
	    return this.m_da.getColumnsManual().length;
	}
	
	/**
	 * Get Row Count
	 * 
	 * @return
	 */
	public int getRowCount()
	{
	    if (this.list==null)
	        return 0;
	    else
	        return this.list.size();
	}
	
    /**
     * Get Row At
     * 
     * @param index
     * @return
     */
    public DeviceValuesEntry getRowAt(int index)
    {
        return this.list.get(index);
    }
	
	
	/**
	 * Get Column Name
	 * 
	 * @param column
	 * @return
	 */
	public String getColumnName(int column)
	{
	    return this.m_da.getColumnsManual()[column];
	}
	
	/**
	 * Get Value At
	 * 
	 * @param row
	 * @param column
	 * @return
	 */
	public Object getValueAt(int row, int column)
	{
	    return this.list.get(row).getColumnValue(column);
	}

	
	/**
	 * Sort of elements
	 */
	public void sort()
	{
	    Collections.sort(list); //, new DeviceValuesEntry()); 
	}
	
	
    /**
     * Sort of elements
     * 
     * @param dve 
     */
    public void sort(Comparator<DeviceValuesEntry> dve)
    {
        Collections.sort(list, dve); //, new DeviceValuesEntry()); 
    } 
	
    /**
     * Get List
     * 
     * @return
     */
    public ArrayList<DeviceValuesEntry> getList()
    {
        return this.list;
    }
	
}	
