package ggc.plugin.data;

import ggc.plugin.util.DataAccessPlugInBase;

import java.util.ArrayList;
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
 *  Filename:  ###---###  
 *  Description:
 * 
 *  Author: Andy {andy@atech-software.com}
 */

public class DeviceValuesDay 
{
	//DataAccessPump da = DataAccessPump.getInstance();
	
    // TODO: Make whole class
	
	
	private ArrayList<DeviceValuesEntry> list = null;
	private Hashtable<String,DeviceValuesEntry> table = null;
	
	DataAccessPlugInBase m_da;
    //private I18nControl m_ic = I18nControl.getInstance();
/*
    private String[] column_names = { 
                        m_ic.getMessage("TIME"),
                        m_ic.getMessage("BASE_TYPE"),
                        m_ic.getMessage("SUB_TYPE"),
                        m_ic.getMessage("VALUE"),
                        m_ic.getMessage("ADDITIONAL"),
                        m_ic.getMessage("COMMENT") };
	*/
	
	
	public DeviceValuesDay(DataAccessPlugInBase da)
	{
	    this.m_da = da;
	    list = new ArrayList<DeviceValuesEntry>();
	    table = new Hashtable<String,DeviceValuesEntry>();
	}
	
	
	public void addEntry(DeviceValuesEntry pve)
	{
	    this.list.add(pve);
	    
	    ATechDate atd = new ATechDate(pve.getDateTimeFormat(), pve.getDateTime());
	    
	    if (!this.table.containsKey(atd.getTimeString()))
	    {
	        this.table.put(atd.getTimeString(), pve);
	    }
	    
	}
	
	public boolean isEntryAvailable(long datetime)
	{
	    ATechDate atd = new ATechDate(m_da.getDataEntryObject().getDateTimeFormat(), datetime);
	    return this.table.containsKey(atd.getTimeString());
	}
	
	public DeviceValuesEntry getEntry(long dt)
	{
        ATechDate atd = new ATechDate(m_da.getDataEntryObject().getDateTimeFormat(), dt);
        return this.table.get(atd.getTimeString());
	}
	
	
	
	
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
	
	
	
	public int getColumnCount()
	{
	    return this.m_da.getColumnsManual().length;
	}
	
	public int getRowCount()
	{
	    //System.out.println("row_count(valDay)=" + this.list);
	    return this.list.size();
	}
	
    public DeviceValuesEntry getRowAt(int index)
    {
        return this.list.get(index);
    }
	
	
	public String getColumnName(int column)
	{
	    return this.m_da.getColumnsManual()[column];
	}
	
	public Object getValueAt(int row, int column)
	{
	    //System.out.println("column: " + column + ",row=" + row);
	    return this.list.get(row).getColumnValue(column);
	    //return "";
	}
/*	
    m_ic.getMessage("DATE_TIME"),
    m_ic.getMessage("BG"),
    m_ic.getMessage("INS_1"),
    m_ic.getMessage("INS_2"),
    m_ic.getMessage("BU"),
    m_ic.getMessage("ACTIVITY"),
    m_ic.getMessage("URINE"),
    m_ic.getMessage("COMMENT") };
*/	
	
}	
