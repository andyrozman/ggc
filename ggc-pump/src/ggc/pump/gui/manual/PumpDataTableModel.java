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
 *  Filename: DailyStatsTableModel.java
 *  Purpose:  A TableModel for the DailyStats-Table. It is the bridge between
 *            the table and the data behind it in the DailyValues Class.
 *
 *  Author:   schultd
 */

package ggc.pump.gui.manual;


import ggc.pump.data.PumpValuesDay;
import ggc.pump.util.DataAccessPump;

import javax.swing.table.AbstractTableModel;


public class PumpDataTableModel extends AbstractTableModel
{
    
    private static final long serialVersionUID = 412835707138372687L;

    PumpValuesDay dayData;
    
    DataAccessPump m_da = DataAccessPump.getInstance();

    /*
    Object objects[] = 
    { 
        new Long(0L), 
        new String(""),
        new Float(0.0d),
        new Float(0.0d),
        new Float(0.0d),
        new String(""),
        new String(""),
        new String("")
    };*/

    Object objects[] = 
    { 
        new String(""), 
        new String(""),
        new String(""),
        new String(""),
        new String(""),
        new String(""),
        new String(""),
        new String("")
    };


    public PumpDataTableModel(PumpValuesDay dayData)
    {
        this.dayData = dayData;
        fireTableChanged(null);
    }

    public PumpValuesDay getDailyValues()
    {
        return this.dayData;
    }

    public void setDailyValues(PumpValuesDay dayData)
    {
        this.dayData = dayData;
        fireTableChanged(null);
    }

    public int getColumnCount()
    {
        if (dayData == null)
            return 0;

        return dayData.getColumnCount();
    }

    public int getRowCount()
    {
        if (dayData == null)
            return 0;

        return dayData.getRowCount();
    }

    public Object getValueAt(int row, int column)
    {
        Object o = dayData.getValueAt(row, column);

	
        if (o != null && column == 0) 
        {
            return m_da.getDateTimeAsTimeString(((Long)o).longValue());
            //System.out.println("DailyStatsTableModel: " + o);
            //SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
            //return sdf.format(o);
        } 
        
        return o;
    }

    @Override
    public String getColumnName(int column)
    {
        /*if (column == 2)
            return m_da.getSettings().getIns1Abbr();
        if (column == 3)
            return m_da.getSettings().getIns2Abbr();
*/
        return dayData.getColumnName(column);
    }

    @Override
    public Class<?> getColumnClass(int c)
    {
        return this.objects[c].getClass();

        /*
        Object o = getValueAt(0, c);
        if (o != null)
            return o.getClass();
        else
            return null;
            */
        //return getValueAt(0,c).getClass();
    }

    @Override
    public boolean isCellEditable(int row, int col)
    {
        //return true;
	return false;
    }

    /*
    @Override
    public void setValueAt(Object aValue, int row, int column)
    {
        dayData.setValueAt(aValue, row, column);
        fireTableChanged(null);
    }
    */
}