package ggc.cgms.gui.viewer;

import java.util.ArrayList;
import java.util.List;

import com.atech.graphics.components.MultiLineTooltip;
import com.atech.graphics.components.jtable.TableModelWithToolTip;
import com.atech.i18n.I18nControlAbstract;

import ggc.cgms.data.CGMSValuesSubEntry;
import ggc.cgms.data.defs.CGMSViewerFilter;
import ggc.cgms.util.DataAccessCGMS;

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

public class CGMSDataTableModel extends TableModelWithToolTip // implements MultiLineTooltipModel
{

    private static final long serialVersionUID = -8127006259458810208L;

    private DataAccessCGMS m_da = DataAccessCGMS.getInstance();
    private I18nControlAbstract m_ic = m_da.getI18nControlInstance();

    private String[] column_names = { m_ic.getMessage("TIME"), m_ic.getMessage("ENTRY_TYPE"),
                                     m_ic.getMessage("ENTRY_VALUE"), m_ic.getMessage("COMMENT") };

    //List<CGMSValuesSubEntry> dayDataList;
    List<CGMSValuesSubEntry> dayDataListFull;
    List<CGMSValuesSubEntry> dayDataListFiltered;



    /**
     * Constructor
     * 
     * @param dayDataList
     */
    public CGMSDataTableModel(List<CGMSValuesSubEntry> dayDataList)
    {
        this.dayDataListFiltered = new ArrayList<CGMSValuesSubEntry>();
        setDayDataList(dayDataList, CGMSViewerFilter.All);
    }


    public void setDayDataList(List<CGMSValuesSubEntry> dayDataList, CGMSViewerFilter filter)
    {
        this.dayDataListFull = dayDataList;
        filterData(filter);
    }

    public void filterData(CGMSViewerFilter filter)
    {
        List<CGMSValuesSubEntry> tempData = new ArrayList<CGMSValuesSubEntry>();

        for (CGMSValuesSubEntry cgmsValuesSubEntry : this.dayDataListFull)
        {
            if (cgmsValuesSubEntry.isItemFiltered(filter))
                tempData.add(cgmsValuesSubEntry);
        }

        this.dayDataListFiltered.clear();
        this.dayDataListFiltered.addAll(tempData);

        fireTableChanged(null);
    }


    /**
     * Get Row Count
     */
    public int getRowCount()
    {
        if (dayDataListFiltered == null)
            return 0;

        return dayDataListFiltered.size();
    }


    /**
     * Get Value At
     */
    public Object getValueAt(int row, int column)
    {
        return dayDataListFiltered.get(row).getColumnValue(column);
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
        return String.class;
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
        CGMSValuesSubEntry o = dayDataListFiltered.get(row);

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
