package ggc.core.data;

import javax.swing.table.AbstractTableModel;

import com.atech.utils.data.ATechDate;
import com.atech.utils.data.ATechDateType;

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
 *  Filename:     DailyStatsTableModel
 *  Description:  A TableModel for the DailyStats-Table. It is the bridge between
 *                the table and the data behind it in the DailyValues Class.
 * 
 *  Author: schultd
 *          Andy {andy@atech-software.com}  
 */

public class DailyStatsTableModel extends AbstractTableModel
{

    private static final long serialVersionUID = -8281136305573121781L;

    DailyValues dayData;


    public DailyStatsTableModel(DailyValues dayData)
    {
        this.dayData = dayData;
        fireTableChanged(null);
    }


    public DailyValues getDailyValues()
    {
        return this.dayData;
    }


    public void setDailyValues(DailyValues dayData)
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
            return ATechDate.getTimeString(ATechDateType.DateAndTimeMin, ((Long) o).longValue());

        return o;
    }


    @Override
    public String getColumnName(int column)
    {
        return dayData.getColumnName(column);
    }


    @Override
    public Class<?> getColumnClass(int c)
    {
        return String.class;
    }


    @Override
    public boolean isCellEditable(int row, int col)
    {
        return false;
    }

}
