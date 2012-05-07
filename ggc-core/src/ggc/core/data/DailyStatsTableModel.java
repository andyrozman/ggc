package ggc.core.data;

import ggc.core.util.DataAccess;

import javax.swing.table.AbstractTableModel;

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
    DataAccess m_da = DataAccess.getInstance();

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


    /**
     * Constructor
     * 
     * @param dayData
     */
    public DailyStatsTableModel(DailyValues dayData)
    {
        this.dayData = dayData;
        fireTableChanged(null);
    }

    /**
     * Get Daily Values
     * 
     * @return
     */
    public DailyValues getDailyValues()
    {
        return this.dayData;
    }

    /**
     * Set Daily Values
     * 
     * @param dayData
     */
    public void setDailyValues(DailyValues dayData)
    {
        this.dayData = dayData;
        fireTableChanged(null);
    }

    /**
     * Get Column Count
     * 
     * @see javax.swing.table.TableModel#getColumnCount()
     */
    public int getColumnCount()
    {
        if (dayData == null)
            return 0;

        return dayData.getColumnCount();
    }

    /**
     * Get Row Count
     * 
     * @see javax.swing.table.TableModel#getRowCount()
     */
    public int getRowCount()
    {
        if (dayData == null)
            return 0;

        return dayData.getRowCount();
    }

    /**
     * Get Value At
     * 
     * @see javax.swing.table.TableModel#getValueAt(int, int)
     */
    public Object getValueAt(int row, int column)
    {
        Object o = dayData.getValueAt(row, column);

	
        if (o != null && column == 0) 
        {
            return DataAccess.getDateTimeAsTimeString(((Long)o).longValue());
        } 
        
        return o;
    }

    /**
     * Get Column Name
     * 
     * @see javax.swing.table.AbstractTableModel#getColumnName(int)
     */
    @Override
    public String getColumnName(int column)
    {
        /*
        if (column == 2)
            return m_da.getSettings().getIns1Abbr();
        if (column == 3)
            return m_da.getSettings().getIns2Abbr();
*/
        return dayData.getColumnName(column);
    }

    /**
     * Get Column Class
     * 
     * @see javax.swing.table.AbstractTableModel#getColumnClass(int)
     */
    @Override
    public Class<?> getColumnClass(int c)
    {
        return this.objects[c].getClass();
    }

    /**
     * Is Cell Editable
     * 
     * @see javax.swing.table.AbstractTableModel#isCellEditable(int, int)
     */
    @Override
    public boolean isCellEditable(int row, int col)
    {
        //return true;
        return false;
    }

    
}