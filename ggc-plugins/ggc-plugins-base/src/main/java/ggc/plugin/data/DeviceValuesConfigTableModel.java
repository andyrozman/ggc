package ggc.plugin.data;

import java.util.ArrayList;
import java.util.Collections;

import javax.swing.table.AbstractTableModel;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import ggc.plugin.util.DataAccessPlugInBase;

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

public class DeviceValuesConfigTableModel extends AbstractTableModel // implements
                                                                     // MultiLineTooltipModel
{

    private static final long serialVersionUID = -896566044265728328L;

    private static final Logger LOG = LoggerFactory.getLogger(DeviceValuesConfigTableModel.class);
    // protected DeviceDataHandler deviceDataHandler = null;
    protected DataAccessPlugInBase m_da;
    // protected String deviceSource;
    protected ArrayList<DeviceValueConfigEntryInterface> data = null;
    DeviceValueConfigEntryInterface deviceValueConfigEntry;


    /**
     * Constructor
     * 
     * @param da
     */
    public <E extends DeviceValueConfigEntryInterface> DeviceValuesConfigTableModel(DataAccessPlugInBase da,
            Class<E> clazz) // , String source)
    {
        // this.deviceDataHandler = ddh;
        this.m_da = da;
        // this.deviceSource = source;

        try
        {
            deviceValueConfigEntry = clazz.newInstance();
        }
        catch (Exception e)
        {
            LOG.error("Can't instantiate. {}", e.getMessage());
        }

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
        return deviceValueConfigEntry.getColumnCount();
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
        float columnWidth = 100.0f / (deviceValueConfigEntry.getColumnCount() * 1.0f);
        return (int) (columnWidth * width);
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


    public int getIndex(int row)
    {
        return this.data.get(row).getIndex();
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
        return m_da.getI18nControlInstance().getMessage(deviceValueConfigEntry.getColumnName(column));

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
    // public String getToolTipValue(int row, int column)
    // {
    // DeviceValueConfigEntryInterface o = data.get(row);
    //
    // if (o.hasMultiLineToolTip())
    // return ((MultiLineTooltip) o).getMultiLineToolTip(column);
    // else
    // return (String) o.getColumnValue(column);
    // }

    // DeviceValuesConfigTableModel

}
