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
 *  Filename: GlucoTableModel.java
 *  Purpose:  The Model behind the "readFromMeter" Table. It is the bridge
 *            between the table and the data behind.
 *
 *  Author:   schultd
 */

package ggc.meter.data;


import ggc.data.event.GlucoValueEvent;
import ggc.data.event.GlucoValueEventListener;
import ggc.util.I18nControl;

import java.text.SimpleDateFormat;

import javax.swing.table.AbstractTableModel;


public class GlucoTableModel extends AbstractTableModel implements GlucoValueEventListener
{
	static final long serialVersionUID = 0;

    private I18nControl m_ic = I18nControl.getInstance();
//x    private DataAccess m_da = DataAccess.getInstance();

    GlucoValues dayData;
    //GGCProperties props = GGCProperties.getInstance();

    private String[] column_names = 
    {
		m_ic.getMessage("DATETIME"), 
		m_ic.getMessage("BG"), 
		m_ic.getMessage("INS1"), 
		m_ic.getMessage("INS2"), 
		m_ic.getMessage("BE"), 
		m_ic.getMessage("ACT"), 
		m_ic.getMessage("COMMENT") 
	};


    public GlucoTableModel(GlucoValues dayData)
    {
        this.dayData = dayData;
        //fireTableChanged(null);
        dayData.addGlucoValueEventListener(this);
    }

    public int getColumnCount()
    {
        return 7;
    }

    public int getRowCount()
    {
        return dayData.getRowCount();
    }

    public Object getValueAt(int row, int column)
    {
        Object o = dayData.getValueAt(row, column);
        if (o != null && column == 0) 
        {
            SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy HH:mm");
            return sdf.format(o);
        }

        return o;
    }

    @Override
    public String getColumnName(int column)
    {
        return column_names[column];
    }

    @Override
    public Class<?> getColumnClass(int c)
    {
        Object o = getValueAt(0, c);
        if (o != null)
            return o.getClass();
        else
            return null;
        //return getValueAt(0,c).getClass();
    }

    @Override
    public boolean isCellEditable(int row, int col)
    {
        return true;
    }

    @Override
    public void setValueAt(Object aValue, int row, int column)
    {
        dayData.setValueAt(aValue, row, column);
        fireTableChanged(null);
    }

    /**
     * @see event.GlucoValueEventListener#glucoValuesChanged(GlucoValueEvent)
     */
    public void glucoValuesChanged(GlucoValueEvent event)
    {
        switch (event.getType()) 
        {
            case GlucoValueEvent.INSERT:
                fireTableRowsInserted(event.getFirstRow(), event.getLastRow());
                break;
            case GlucoValueEvent.DELETE:
                fireTableRowsDeleted(event.getFirstRow(), event.getLastRow());
                break;
            case GlucoValueEvent.UPDATE:
                fireTableCellUpdated(event.getFirstRow(), event.getColumn());
                break;
        }
    }

    /**
     * Returns the dayData.
     * @return GlucoValues
     */
    public GlucoValues getDayData()
    {
        return dayData;
    }

}