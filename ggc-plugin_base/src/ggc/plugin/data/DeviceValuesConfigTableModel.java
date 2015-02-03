package ggc.plugin.data;

import ggc.plugin.util.DataAccessPlugInBase;

import java.util.ArrayList;
import java.util.Collections;

import javax.swing.table.AbstractTableModel;

import com.atech.graphics.components.MultiLineTooltip;
import com.atech.graphics.components.MultiLineTooltipModel;

/**
 *  Application:   GGC - GNU Gluco Control
 *  Plug-in:       GGC PlugIn Base (base class for all plugins)
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
 *  Filename:     DeviceValuesTableModel  
 *  Description:  Model for table of Device values
 * 
 *  Author: Andy {andy@atech-software.com}
 */

public class DeviceValuesConfigTableModel extends AbstractTableModel implements MultiLineTooltipModel
{

    private static final long serialVersionUID = -896566044265728328L;
    protected DeviceDataHandler m_ddh = null;
    protected DataAccessPlugInBase m_da;
    protected String device_source;
    protected ArrayList<DeviceValueConfigEntryInterface> data = null;

    /**
     * Constructor
     * 
     * @param da
     * @param source 
     */
    public DeviceValuesConfigTableModel(DataAccessPlugInBase da, String source)
    {
        // this.m_ddh = ddh;
        this.m_da = da;
        this.device_source = source;
        this.data = new ArrayList<DeviceValueConfigEntryInterface>();
        fireTableChanged(null);
    }

    /**
     * Clear Data
     */
    public void clearData()
    {
        this.data.clear();
        fireTableChanged(null);
    }

    /**
     * Get Column Count
     * 
     * @see javax.swing.table.TableModel#getColumnCount()
     */
    public int getColumnCount()
    {
        return 2;
    }

    /**
     * Is Boolean
     * 
     * @param column column index
     * @return true if column type is boolean
     */
    public boolean isBoolean(int column)
    {
        return false;
    }

    /**
     * Is Editable Column
     * 
     * @param column column index
     * @return true if column is editable
     */
    public boolean isEditableColumn(int column)
    {
        return false;
    }

    /** 
     * Set Value At
     */
    @Override
    public void setValueAt(Object aValue, int row, int column)
    {
    }

    /**
     * Get Column Width
     * 
     * @param column column index
     * @param width width for column
     * @return calculated size of column
     */
    public int getColumnWidth(int column, int width)
    {
        return (int) (50.0f * width);
    }

    /**
     * Get Row Count
     * 
     * @see javax.swing.table.TableModel#getRowCount()
     */
    public int getRowCount()
    {
        return this.data.size();
    }

    /**
     * Get Value At
     * @see javax.swing.table.TableModel#getValueAt(int, int)
     */
    public Object getValueAt(int row, int column)
    {
        return this.data.get(row).getColumnValue(column);
    }

    /**
     * Add Entry
     * 
     * @param mve DeviceValuesEntry instance
     */
    public void addEntry(DeviceValueConfigEntryInterface mve)
    {
        this.data.add(mve);
        Collections.sort(data);
        this.fireTableDataChanged();
    }

    /**
     * Get Column Name
     * 
     * @see javax.swing.table.AbstractTableModel#getColumnName(int)
     */
    @Override
    public String getColumnName(int column)
    {
        if (column == 0)
            return m_da.getI18nControlInstance().getMessage("SETTING_GROUP");
        else
            return m_da.getI18nControlInstance().getMessage("VALUE");
    }

    /**
     * Get Column Class
     * 
     * @see javax.swing.table.AbstractTableModel#getColumnClass(int)
     */
    @Override
    public Class<?> getColumnClass(int c)
    {
        Object o = getValueAt(0, c);
        if (o != null)
            return o.getClass();
        else
            return null;
    }

    /**
     * Is Cell Editable
     * 
     * @see javax.swing.table.AbstractTableModel#isCellEditable(int, int)
     */
    @Override
    public boolean isCellEditable(int row, int col)
    {
        return false;
    }

    /** 
     * get ToolTip Value
     * 
     * @param row 
     * @param column 
     * @return 
     */
    public String getToolTipValue(int row, int column)
    {
        DeviceValueConfigEntryInterface o = data.get(row);

        if (o.hasMultiLineToolTip())
            return ((MultiLineTooltip) o).getMultiLineToolTip(column);
        else
            return (String) o.getColumnValue(column);
    }

}
