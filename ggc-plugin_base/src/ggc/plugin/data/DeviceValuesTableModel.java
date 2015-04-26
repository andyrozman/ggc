package ggc.plugin.data;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Hashtable;
import java.util.List;

import javax.swing.table.AbstractTableModel;

import com.atech.db.hibernate.DatabaseObjectHibernate;
import com.atech.graphics.components.MultiLineTooltip;
import com.atech.graphics.components.MultiLineTooltipModel;

import ggc.plugin.data.enums.DeviceEntryStatus;
import ggc.plugin.gui.DeviceDisplayDataDialog;
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

public abstract class DeviceValuesTableModel extends AbstractTableModel implements MultiLineTooltipModel
{

    private static final long serialVersionUID = -6542265335372702616L;
    protected ArrayList<DeviceValuesEntryInterface> dl_data;
    protected ArrayList<DeviceValuesEntryInterface> displayed_dl_data;
    protected DeviceDataHandler deviceDataHandler = null;
    protected int current_filter = DeviceDisplayDataDialog.FILTER_NEW_CHANGED;
    protected DataAccessPlugInBase m_da;
    protected String device_source;
    protected boolean debug = false;
    protected List<ColumnSettings> columns;
    protected int checkableColumn;


    /**
     * Constructor
     * 
     * @param da
     * @param ddh
     */
    public DeviceValuesTableModel(DataAccessPlugInBase da, DeviceDataHandler ddh, String source)
    {
        this.deviceDataHandler = ddh;
        this.deviceDataHandler.setDeviceValuesTableModel(this);
        this.m_da = da;
        this.displayed_dl_data = new ArrayList<DeviceValuesEntryInterface>();
        this.dl_data = new ArrayList<DeviceValuesEntryInterface>();
        this.device_source = source;
        initColumns();
        determineCheckableColumn();
        // this.dayData = dayData;
        fireTableChanged(null);
        // dayData.addGlucoValueEventListener(this);
    }


    protected abstract void initColumns();


    /**
     * Clear Data
     */
    public void clearData()
    {
        this.displayed_dl_data.clear();
        this.dl_data.clear();
        fireTableChanged(null);
    }


    /** 
     * Set Value At
     */
    @Override
    public void setValueAt(Object aValue, int row, int column)
    {
        if (checkableColumn == column)
        {
            Boolean b = (Boolean) aValue;
            this.displayed_dl_data.get(row).setChecked(b.booleanValue());
        }
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
        return columns.get(column).columnWidth * width;
    }


    /**
     * Select All
     */
    public void selectAll()
    {
        setSelectors(true);
    }


    /**
     * Deselect All
     */
    public void deselectAll()
    {
        setSelectors(false);
    }


    private void setSelectors(boolean select)
    {
        for (int i = 0; i < this.displayed_dl_data.size(); i++)
        {
            this.displayed_dl_data.get(i).setChecked(select);
        }

        this.fireTableDataChanged();
    }


    /**
     * set Filter
     * 
     * @param filter
     */
    public void setFilter(int filter)
    {
        this.setFilter(filter, false);
    }


    /**
     * Set Filter
     * 
     * @param filter
     * @param force 
     */
    public void setFilter(int filter, boolean force)
    {
        // System.out.println("Set FILTER !!!!!!!!!!!!!!!!!!!!!! " + filter);

        if (this.current_filter == filter && !force)
            return;

        this.current_filter = filter;

        this.displayed_dl_data.clear();

        for (int i = 0; i < this.dl_data.size(); i++)
        {
            DeviceValuesEntryInterface mve = this.dl_data.get(i);

            if (shouldBeDisplayed(mve.getStatusType()))
            {
                this.displayed_dl_data.add(mve);
            }
        }

        Collections.sort(displayed_dl_data);

        this.fireTableDataChanged();

    }


    /**
     * Should be displayed filter
     * 
     * @param status
     * @return
     */
//    public boolean shouldBeDisplayed(int status)
//    {
//        switch (this.current_filter)
//        {
//            case DeviceDisplayDataDialog.FILTER_ALL:
//                return true;
//
//            case DeviceDisplayDataDialog.FILTER_NEW:
//                return status == DeviceValuesEntryInterface.STATUS_NEW;
//
//            case DeviceDisplayDataDialog.FILTER_CHANGED:
//                return status == DeviceValuesEntryInterface.STATUS_CHANGED;
//
//            case DeviceDisplayDataDialog.FILTER_EXISTING:
//                return status == DeviceValuesEntryInterface.STATUS_OLD;
//
//            case DeviceDisplayDataDialog.FILTER_UNKNOWN:
//                return status == DeviceValuesEntryInterface.STATUS_UNKNOWN;
//
//            case DeviceDisplayDataDialog.FILTER_NEW_CHANGED:
//                return status == DeviceValuesEntryInterface.STATUS_NEW
//                        || status == DeviceValuesEntryInterface.STATUS_CHANGED;
//
//            case DeviceDisplayDataDialog.FILTER_ALL_BUT_EXISTING:
//                return status != DeviceValuesEntryInterface.STATUS_OLD;
//        }
//        return false;
//
//    }


    public boolean shouldBeDisplayed(DeviceEntryStatus status)
    {
        switch (this.current_filter)
        {
            case DeviceDisplayDataDialog.FILTER_ALL:
                return true;

            case DeviceDisplayDataDialog.FILTER_NEW:
                return status == DeviceEntryStatus.New;

            case DeviceDisplayDataDialog.FILTER_CHANGED:
                return status == DeviceEntryStatus.Changed;

            case DeviceDisplayDataDialog.FILTER_EXISTING:
                return status == DeviceEntryStatus.Old;

            case DeviceDisplayDataDialog.FILTER_UNKNOWN:
                return status == DeviceEntryStatus.Unknown;

            case DeviceDisplayDataDialog.FILTER_NEW_CHANGED:
                return status == DeviceEntryStatus.New || status == DeviceEntryStatus.Changed;

            case DeviceDisplayDataDialog.FILTER_ALL_BUT_EXISTING:
                return status != DeviceEntryStatus.Old;
        }
        return false;

    }



    /**
     * Get Row Count
     * 
     * @see javax.swing.table.TableModel#getRowCount()
     */
    public int getRowCount()
    {
        if (this.displayed_dl_data == null)
            return 0;
        else
            return this.displayed_dl_data.size();
    }


    /**
     * Get Value At
     * @see javax.swing.table.TableModel#getValueAt(int, int)
     */
    public Object getValueAt(int row, int column)
    {
        return this.displayed_dl_data.get(row).getTableColumnValue(column);
    }


    /**
     * Add Entry
     * 
     * @param mve DeviceValuesEntry instance
     */
    public void addEntry(DeviceValuesEntryInterface mve)
    {
        processDeviceValueEntry(mve);
        this.dl_data.add(mve);

        if (this.shouldBeDisplayed(mve.getStatusType()))
        {
            this.displayed_dl_data.add(mve);
            Collections.sort(displayed_dl_data);
        }
        this.fireTableDataChanged();
    }


    /**
     * Process Device Value Entry
     * 
     * @param dve DeviceValuesEntryInterface instance
     */
    public void processDeviceValueEntry(DeviceValuesEntryInterface dve)
    {

        if (this.deviceDataHandler.hasOldData())
        {

            if (debug)
            {
                System.out.println("DVE: " + dve.getSpecialId());
            }

            if (!this.deviceDataHandler.getOldData().containsKey("" + dve.getSpecialId()))
            {
                dve.setStatusType(DeviceEntryStatus.New); //DeviceValuesEntryInterface.STATUS_NEW);
                dve.setObjectStatus(DeviceValuesEntryInterface.OBJECT_STATUS_NEW);
            }
            else
            {

                DeviceValuesEntryInterface dve_old = (DeviceValuesEntryInterface) this.deviceDataHandler.getOldData()
                        .get(dve.getSpecialId());

                // System.out.println("Old: " + dve_old.getValue());
                dve.prepareEntry_v2();
                // System.out.println("New: " + dve.getValue());

                if (dve_old.getValue().equals(dve.getValue()))
                {
                    dve.setStatusType(DeviceEntryStatus.Old); // DeviceValuesEntryInterface.STATUS_OLD);
                    dve.setObjectStatus(DeviceValuesEntryInterface.OBJECT_STATUS_OLD);
                    dve.setId(dve_old.getId());
                }
                else
                {
                    dve.setStatusType(DeviceEntryStatus.Changed); //DeviceValuesEntryInterface.STATUS_CHANGED);
                    dve.setObjectStatus(DeviceValuesEntryInterface.OBJECT_STATUS_EDIT);
                    // dve.entry_object = mve_old.getHibernateObject();

                    //if (debug)
                    {
                        System.out.println("!!! OLD ID: " + dve_old.getId());
                        System.out.println("Data: \nnew_value=" + dve.getValue() + "\nold_value=" + dve_old.getValue());
                    }

                    dve.setId(dve_old.getId());
                }
            }
        }
        else
        {
            dve.setStatusType(DeviceEntryStatus.New); // DeviceValuesEntryInterface.STATUS_NEW);
            dve.setObjectStatus(DeviceValuesEntryInterface.OBJECT_STATUS_NEW);
        }

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
        // return getValueAt(0,c).getClass();
    }


    /**
     * Is Cell Editable
     * 
     * @see javax.swing.table.AbstractTableModel#isCellEditable(int, int)
     */
    @Override
    public boolean isCellEditable(int row, int col)
    {
        return (col == checkableColumn);
    }


    /**
     * Get Checked DOH (DatabaseObjectHibernate) objects. This are packed in Hashtable, with keys ADD and EDIT
     * which contain list with DatabaseObjectHibernate objects.
     * 
     * @return
     */
    public Hashtable<String, ArrayList<DatabaseObjectHibernate>> getCheckedDOHObjects()
    {
        Hashtable<String, ArrayList<DatabaseObjectHibernate>> table = new Hashtable<String, ArrayList<DatabaseObjectHibernate>>();

        ArrayList<DatabaseObjectHibernate> list_add = new ArrayList<DatabaseObjectHibernate>();
        ArrayList<DatabaseObjectHibernate> list_edit = new ArrayList<DatabaseObjectHibernate>();

        // System.out.println("getCheckedDOHObjects()");

        for (int i = 0; i < this.displayed_dl_data.size(); i++)
        {

            if (this.displayed_dl_data.get(i).getChecked())
            {
                DeviceValuesEntryInterface dvei = this.displayed_dl_data.get(i);
                // list.add((DatabaseObjectHibernate)this.displayed_dl_data.get(i));

                // System.out.println("Checked: " + dvei);

                if (dvei.getObjectStatus() == DeviceValuesEntry.OBJECT_STATUS_NEW)
                {
                    debug("Checked[add]: " + dvei);

                    list_add.add(dvei);
                }
                else if (dvei.getObjectStatus() == DeviceValuesEntry.OBJECT_STATUS_EDIT)
                {
                    debug("Checked[edit]: " + dvei);
                    list_edit.add(dvei);
                }
                else
                {
                    debug("Checked[" + dvei.getObjectStatus() + "]: " + dvei);
                }

            }
        }

        debug("To: Add: " + list_add.size() + ", Edit: " + list_edit.size());

        table.put("ADD", list_add);
        table.put("EDIT", list_edit);

        return table;
    }


    public void debug(String message)
    {
        if (debug)
        {
            System.out.println(message);
        }
    }


    /**
     * Get Checked DeviceValuesEntries [V2]
     * 
     * @return Hashtable<String,ArrayList<?>>
     */
    public Hashtable<String, ArrayList<DeviceValuesEntry>> getCheckedDVE()
    {

        Hashtable<String, ArrayList<DeviceValuesEntry>> ht = new Hashtable<String, ArrayList<DeviceValuesEntry>>();

        ht.put("ADD", new ArrayList<DeviceValuesEntry>());
        ht.put("EDIT", new ArrayList<DeviceValuesEntry>());

        for (int i = 0; i < this.dl_data.size(); i++)
        {
            DeviceValuesEntry dve = (DeviceValuesEntry) this.dl_data.get(i);

            if (!dve.getChecked())
            {
                continue;
            }

            dve.prepareEntry();

            if (dve.getObjectStatus() == DeviceValuesEntry.OBJECT_STATUS_NEW)
            {
                ht.get("ADD").add(dve);
            }
            else if (dve.getObjectStatus() == DeviceValuesEntry.OBJECT_STATUS_EDIT)
            {
                ht.get("EDIT").add(dve);
            }
        }

        return ht;
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
        DeviceValuesEntryInterface o = displayed_dl_data.get(row);

        if (o.hasMultiLineToolTip())
            return ((MultiLineTooltip) o).getMultiLineToolTip(column);
        else
        {
            if (o.getTableColumnValue(column) instanceof String)
                return (String) o.getTableColumnValue(column);
            else
                return o.getTableColumnValue(column).toString();
        }
    }


    /**
     * Get Column Count
     *
     * @see javax.swing.table.TableModel#getColumnCount()
     */
    public int getColumnCount()
    {
        return this.columns.size();
    }


    /**
     * Get Column Name
     *
     * @see javax.swing.table.AbstractTableModel#getColumnName(int)
     */
    @Override
    public String getColumnName(int index)
    {
        return this.columns.get(index).columnName;
    }


    public int getColumnWidth(int index)
    {
        return this.columns.get(index).columnWidth;
    }


    public int getCheckableColumn()
    {
        return this.checkableColumn;
    }


    private void determineCheckableColumn()
    {
        for (ColumnSettings cs : this.columns)
        {
            if (cs.checkableColumn)
            {
                this.checkableColumn = cs.index;
                break;
            }
        }
    }


    protected void addColumn(int index, String columnName, int columnWidth, boolean checkableColumn)
    {
        if (columns == null)
        {
            columns = new ArrayList<ColumnSettings>();
        }

        columns.add(new ColumnSettings(index, this.m_da.getI18nControlInstance().getMessage(columnName), columnWidth,
                checkableColumn));
    }

    protected class ColumnSettings
    {

        int index;
        String columnName;
        int columnWidth;
        boolean checkableColumn;


        public ColumnSettings(int index, String columnName, int columnWidth)
        {
            this(index, columnName, columnWidth, false);
        }


        public ColumnSettings(int index, String columnName, int columnWidth, boolean checkableColumn)
        {
            this.index = index;
            this.columnName = columnName;
            this.columnWidth = columnWidth;
            this.checkableColumn = checkableColumn;
        }
    }

}
