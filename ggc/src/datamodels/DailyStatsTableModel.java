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

package datamodels;


import util.GGCProperties;

import javax.swing.table.AbstractTableModel;
import java.text.SimpleDateFormat;


public class DailyStatsTableModel extends AbstractTableModel
{
    DailyValues dayData;
    GGCProperties props = GGCProperties.getInstance();

    public DailyStatsTableModel(DailyValues dayData)
    {
        this.dayData = dayData;
        fireTableChanged(null);
    }

    public void setDailyValues(DailyValues dayData)
    {
        this.dayData = dayData;
        fireTableChanged(null);
    }

    public int getColumnCount()
    {
        return dayData.getColumnCount();
    }

    public int getRowCount()
    {
        return dayData.getRowCount();
    }

    public Object getValueAt(int row, int column)
    {
        Object o = dayData.getValueAt(row, column);
        if (o != null && column == 0) {
            SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
            return sdf.format(o);
        }

        return o;
    }

    public String getColumnName(int column)
    {
        if (column == 2)
            return props.getIns1Abbr();
        if (column == 3)
            return props.getIns2Abbr();

        return dayData.getColumnName(column);
    }

    public Class getColumnClass(int c)
    {
        Object o = getValueAt(0, c);
        if (o != null)
            return o.getClass();
        else
            return null;
        //return getValueAt(0,c).getClass();
    }

    public boolean isCellEditable(int row, int col)
    {
        return true;
    }

    public void setValueAt(Object aValue, int row, int column)
    {
        dayData.setValueAt(aValue, row, column);
        fireTableChanged(null);
    }
}