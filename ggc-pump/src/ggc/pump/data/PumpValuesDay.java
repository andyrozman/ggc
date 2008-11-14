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

import ggc.pump.gui.manual.PumpDataTypeComponent;
import ggc.pump.util.DataAccessPump;
import ggc.pump.util.I18nControl;

import java.util.ArrayList;
import java.util.Hashtable;

import com.atech.utils.ATechDate;



public class PumpValuesDay 
{
	DataAccessPump da = DataAccessPump.getInstance();
	
    // TODO: Make whole class
	
	
	private ArrayList<PumpValuesEntry> list = null;
	private Hashtable<String,PumpValuesEntry> table = null;
	
	
    private I18nControl m_ic = I18nControl.getInstance();

    private String[] column_names = { 
                        m_ic.getMessage("TIME"),
                        m_ic.getMessage("BASE_TYPE"),
                        m_ic.getMessage("SUB_TYPE"),
                        m_ic.getMessage("VALUE"),
                        m_ic.getMessage("ADDITIONAL"),
                        m_ic.getMessage("COMMENT") };
	
	
	
	public PumpValuesDay()
	{
	    list = new ArrayList<PumpValuesEntry>();
	    table = new Hashtable<String,PumpValuesEntry>();
	}
	
	
	public void addEntry(PumpValuesEntry pve)
	{
	    this.list.add(pve);
	    
	    ATechDate atd = new ATechDate(ATechDate.FORMAT_DATE_AND_TIME_S, pve.getDt_info());
	    
	    if (!this.table.containsKey(atd.getTimeString()))
	    {
	        this.table.put(atd.getTimeString(), pve);
	    }
	    
	}
	
	public boolean isEntryAvailable(long datetime)
	{
	    ATechDate atd = new ATechDate(ATechDate.FORMAT_DATE_AND_TIME_S, datetime);
	    return this.table.containsKey(atd.getTimeString());
	}
	
	public PumpValuesEntry getEntry(long dt)
	{
        ATechDate atd = new ATechDate(ATechDate.FORMAT_DATE_AND_TIME_S, dt);
        return this.table.get(atd.getTimeString());
	}
	
	
	
	public void addExtendedEntries(ArrayList<PumpValuesEntryExt> lst)
	{
	    // TODO
	    for(int i=0; i<lst.size(); i++)
	    {
	        PumpValuesEntryExt ext = lst.get(i);
	        
	        if (isEntryAvailable(ext.getDt_info()))
	        {
	            PumpValuesEntry pve = getEntry(ext.getDt_info());
	            pve.addAdditionalData(ext);
	        }
	        else
	        {
	            PumpValuesEntry pve = new PumpValuesEntry();
	            pve.setDt_info(ext.getDt_info());
	            pve.setBase_type(PumpDataTypeComponent.TYPE_ADDITIONAL_DATA);
	            
	            pve.addAdditionalData(ext);
	            this.addEntry(pve);
	        }
	    } // for
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
	    return this.column_names.length;
	}
	
	public int getRowCount()
	{
	    //System.out.println("row_count(valDay)=" + this.list);
	    return this.list.size();
	}
	
    public PumpValuesEntry getRowAt(int index)
    {
        return this.list.get(index);
    }
	
	
	public String getColumnName(int column)
	{
	    return this.column_names[column];
	    //return null;
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
