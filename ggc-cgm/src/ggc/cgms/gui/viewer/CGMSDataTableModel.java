package ggc.cgms.gui.viewer;

import javax.swing.table.AbstractTableModel;

import com.atech.graphics.components.MultiLineTooltip;
import com.atech.graphics.components.MultiLineTooltipModel;
import com.atech.i18n.I18nControlAbstract;

import ggc.cgms.util.DataAccessCGMS;
import ggc.plugin.data.DeviceValuesDay;
import ggc.plugin.data.DeviceValuesEntry;

/**
 *  Application:   GGC - GNU Gluco Control
 *  Plug-in:       Pump Tool (support for Pump devices)
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
 *  Filename:     Dexcom 7
 *  Description:  Dexcom 7 implementation (just settings)
 *
 *  Author: Andy {andy@atech-software.com}
 */

public class CGMSDataTableModel extends AbstractTableModel implements MultiLineTooltipModel
{

    private static final long serialVersionUID = -8127006259458810208L;

    DeviceValuesDay dayData;

    private DataAccessCGMS m_da = DataAccessCGMS.getInstance();
    private I18nControlAbstract m_ic = m_da.getI18nControlInstance();

    Object objects[] = { new String(""), new String(""), new String(""), new String(""), };

    private String[] column_names = { m_ic.getMessage("TIME"), m_ic.getMessage("ENTRY_TYPE"),
                                     m_ic.getMessage("READING"), m_ic.getMessage("COMMENT") };


    /**
     * Constructor
     * 
     * @param dayData
     */
    public CGMSDataTableModel(DeviceValuesDay dayData)
    {
        setDailyValues(dayData);
    }


    /**
     * Get Daily Values
     * 
     * @return
     */
    public DeviceValuesDay getDailyValues()
    {
        return this.dayData;
    }


    /**
     * Set Daily Values
     * 
     * @param dayData
     */
    public void setDailyValues(DeviceValuesDay dayData)
    {
        this.dayData = dayData;
        fireTableChanged(null);
    }


    /**
     * Get Row Count
     */
    public int getRowCount()
    {
        if (dayData == null)
            return 0;

        return dayData.getRowCount();
    }


    /**
     * Get Value At
     */
    public Object getValueAt(int row, int column)
    {
        return dayData.getValueAt(row, column);
    }


    /** 
     * Get Column Name
     */
    @Override
    public String getColumnName(int column)
    {
        return this.column_names[column];
    }


    /**
     * Get Column Class
     */
    @Override
    public Class<?> getColumnClass(int c)
    {
        return this.objects[c].getClass();
    }


    /**
     * Is Cell Editable
     */
    @Override
    public boolean isCellEditable(int row, int col)
    {
        return false;
    }


    /** 
     * get ToolTip Value
     */
    public String getToolTipValue(int row, int column)
    {
        DeviceValuesEntry o = dayData.getRowAt(row);

        if (o instanceof MultiLineTooltip)
            return ((MultiLineTooltip) o).getMultiLineToolTip(column);
        else
            return (String) o.getColumnValue(column);
    }


    public int getColumnCount()
    {
        return this.column_names.length;
    }

}
