package ggc.gui.little.data;

import javax.swing.table.AbstractTableModel;

import ggc.core.data.DailyValues;
import ggc.core.data.DataValuesInterface;

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

public class DailyStatsTableLittleModel extends AbstractTableModel
{

    private static final long serialVersionUID = -8281136305573121781L;

    DailyValues dayDataPen;

    DataValuesInterface data;


    public DailyStatsTableLittleModel()
    {
    }


    // public DailyValues getDailyValuesPen()
    // {
    // return this.dayDataPen;
    // }

    public void setDataValues(DataValuesInterface dayData)
    {
        this.data = dayData;
        fireTableChanged(null);
    }


    public int getColumnCount()
    {
        if (data == null)
            return 0;

        return data.getColumnCount();
    }


    public int getRowCount()
    {
        if (data == null)
            return 0;

        return data.getRowCount();
    }


    public Object getValueAt(int row, int column)
    {
        if (data == null)
            return null;

        return data.getValueAt(row, column);
    }


    @Override
    public String getColumnName(int column)
    {
        if (data == null)
            return null;

        return data.getColumnName(column);
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
