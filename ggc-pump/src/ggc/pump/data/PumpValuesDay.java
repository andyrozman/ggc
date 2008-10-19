/*
 *  GGC - GNU Gluco Control
 *
 *  A pure java app to help you manage your diabetes.
 *
 *  See AUTHORS for copyright information.
 *
 *  This program is free software; you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation; either version 2 of the License, or
 *  (at your option) any later version.
 *
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with this program; if not, write to the Free Software
 *  Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA
 *
 *  Filename: DailyValuesRow.java
 *  Purpose:  One row in the DailyValues Data Model.
 *
 *  Author:   Andy Rozman {andy@atech-software.com}
 */

package ggc.pump.data;

import ggc.pump.util.DataAccessPump;

import java.util.ArrayList;
import java.util.Hashtable;

import com.atech.utils.ATechDate;



public class PumpValuesDay 
{
	DataAccessPump da = DataAccessPump.getInstance();
	
    // TODO: Make whole class
	
	
	private ArrayList<PumpValuesEntry> list = null;
	private Hashtable<String,PumpValuesEntry> table = null;
	
	
	public PumpValuesDay()
	{
	    list = new ArrayList<PumpValuesEntry>();
	    table = new Hashtable<String,PumpValuesEntry>();
	}
	
	
	public void addEntry(PumpValuesEntry pve)
	{
	    this.list.add(pve);
	    
	    ATechDate atd = new ATechDate(ATechDate.DT_DATETIME, pve.getDt_info());
	    
	    if (!this.table.containsKey(atd.getDateString()))
	    {
	        this.table.put(atd.getDateString(), pve);
	    }
	    
	}
	
	public void addExtendedEntries(ArrayList<PumpValuesEntryExt> lst)
	{
	    // TODO
	}
	
	
	
	public int getColumnCount()
	{
	    return 8;
	}
	
	public int getRowCount()
	{
	    return this.list.size();
	}
	
	public String getColumnName(int column)
	{
	    return null;
	}
	
	public Object getValueAt(int column, int row)
	{
	    return null;
	}
	
	
	
}	
