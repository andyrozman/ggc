package ggc.plugin.data;

import ggc.core.db.hibernate.GGCHibernateObject;
import ggc.plugin.gui.DeviceDisplayDataDialog;
import ggc.plugin.util.DataAccessPlugInBase;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Hashtable;

import javax.swing.table.AbstractTableModel;


/**
 *  Application:   GGC - GNU Gluco Control
 *  Plug-in:       CGMS Tool (support for CGMS devices)
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


public abstract class DeviceValuesTableModel extends AbstractTableModel 
{

    private static final long serialVersionUID = -6542265335372702616L;
    protected ArrayList<DeviceValuesEntryInterface> dl_data;
    protected ArrayList<DeviceValuesEntryInterface> displayed_dl_data;
    protected DeviceDataHandler m_ddh = null;
    protected int current_filter = DeviceDisplayDataDialog.FILTER_NEW_CHANGED;
    protected DataAccessPlugInBase m_da;


    /**
     * Constructor
     * 
     * @param da
     * @param ddh
     */
    public DeviceValuesTableModel(DataAccessPlugInBase da, DeviceDataHandler ddh)
    {
        this.m_ddh = ddh;
        this.m_da = da;
        this.displayed_dl_data = new ArrayList<DeviceValuesEntryInterface>();
        this.dl_data = new ArrayList<DeviceValuesEntryInterface>();
        // this.dayData = dayData;
        fireTableChanged(null);
        // dayData.addGlucoValueEventListener(this);
    }

    
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
     * Get Column Count
     * 
     * @see javax.swing.table.TableModel#getColumnCount()
     */
    public int getColumnCount()
    {
        return m_da.getColumnsWidthTable().length;
    }

    
    /**
     * Is Boolean
     * 
     * @param column column index
     * @return true if column type is boolean
     */
    public boolean isBoolean(int column)
    {
        if (column == this.getCheckableColumn())
            return true;
        else
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
        if (column == this.getCheckableColumn())
            return true;
        else
            return false;
    }

    
    /** 
     * Set Value At
     */
    public void setValueAt(Object aValue, int row, int column)
    {
        if (this.getCheckableColumn()==column)
        {
            Boolean b = (Boolean) aValue;
            this.displayed_dl_data.get(row).setChecked(b.booleanValue());
        }
        //System.out.println("set Value: rw=" + row + ",column=" + column + ",value=" + aValue);
        // dayData.setValueAt(aValue, row, column);
        // fireTableChanged(null);
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
        return m_da.getColumnsWidthTable()[column] * width;
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
     * Set Filter
     * 
     * @param filter
     */
    public void setFilter(int filter)
    {
        if (this.current_filter==filter)
            return;
        
        this.current_filter = filter;
        
        this.displayed_dl_data.clear();
        
        for(int i=0; i< this.dl_data.size(); i++)
        {
            DeviceValuesEntryInterface mve = this.dl_data.get(i);
            
            if (shouldBeDisplayed(mve.getStatus()))
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
    public boolean shouldBeDisplayed(int status)
    {
        switch (this.current_filter)
        {
            case DeviceDisplayDataDialog.FILTER_ALL:
                return true;
                
            case DeviceDisplayDataDialog.FILTER_NEW:
                return (status == DeviceValuesEntryInterface.STATUS_NEW);
    
            case DeviceDisplayDataDialog.FILTER_CHANGED:
                return (status == DeviceValuesEntryInterface.STATUS_CHANGED);
                
            case DeviceDisplayDataDialog.FILTER_EXISTING:
                return (status == DeviceValuesEntryInterface.STATUS_OLD);
                
            case DeviceDisplayDataDialog.FILTER_UNKNOWN:
                return (status == DeviceValuesEntryInterface.STATUS_UNKNOWN);
                
            case DeviceDisplayDataDialog.FILTER_NEW_CHANGED:
                return ((status == DeviceValuesEntryInterface.STATUS_NEW) ||
                        (status == DeviceValuesEntryInterface.STATUS_CHANGED));
                
            case DeviceDisplayDataDialog.FILTER_ALL_BUT_EXISTING:
                return (status != DeviceValuesEntryInterface.STATUS_OLD);
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
        if (this.displayed_dl_data==null)
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
     * Get Checkable Column (one column if checkable, all others are non-editable)
     * 
     * @return
     */
    public abstract int getCheckableColumn();
    
    
    /**
     * Add Entry
     * 
     * @param mve DeviceValuesEntry instance
     */
    public void addEntry(DeviceValuesEntryInterface mve)
    {
        processDeviceValueEntry(mve);
        this.dl_data.add(mve);
        
        if (this.shouldBeDisplayed(mve.getStatus()))
        {
            this.displayed_dl_data.add(mve);
            Collections.sort(displayed_dl_data);
        }
        this.fireTableDataChanged();
    }

    
    /**
     * Process Device Value Entry
     * 
     * @param mve DeviceValuesEntry instance
     */
    public abstract void processDeviceValueEntry(DeviceValuesEntryInterface mve);
    
    
    /**
     * Get Checked Entries
     * 
     * @return Hashtable<String,ArrayList<?>>
     */
    public Hashtable<String,ArrayList<?>> getCheckedEntries()
    {
        
        Hashtable<String,ArrayList<?>> ht = new Hashtable<String,ArrayList<?>>();
        
        ht.put("ADD", getEmptyArrayList());
        ht.put("EDIT", getEmptyArrayList());
        
        
        for(int i=0; i<this.dl_data.size(); i++)
        {
            DeviceValuesEntryInterface mve = this.dl_data.get(i);
            
            if (!mve.getChecked())
                continue;
            
            mve.prepareEntry();
            
            if (mve.getObjectStatus()==DeviceValuesEntry.OBJECT_STATUS_NEW)
            {
                addToArray(ht.get("ADD"), mve.getDbObjects());
            }
            else if (mve.getObjectStatus()==DeviceValuesEntry.OBJECT_STATUS_EDIT)
            {
                addToArray(ht.get("EDIT"), mve.getDbObjects());
            }
        }
        
        return ht;
    }

    
    
    /**
     * Export Checked Data
     */
    public void exportCheckedData()
    {
        
        for(int i=0; i<this.dl_data.size(); i++)
        {
            DeviceValuesEntryInterface mve = this.dl_data.get(i);
            
            if (!mve.getChecked())
                continue;
            
            // TODO
            
            mve.prepareEntry();
            
            if (mve.getObjectStatus()==DeviceValuesEntry.OBJECT_STATUS_NEW)
            {
                //addToArray(ht.get("ADD"), mve.getDbObjects());
            }
            else if (mve.getObjectStatus()==DeviceValuesEntry.OBJECT_STATUS_EDIT)
            {
                //addToArray(ht.get("EDIT"), mve.getDbObjects());
            }
        }
        
        //return ht;
    }
    
    
    
    
    /**
     * Get Empty ArrayList
     * 
     * @return
     */
    public abstract ArrayList<? extends GGCHibernateObject> getEmptyArrayList();
    
    
    /**
     * Add To Array 
     * 
     * @param lst
     * @param source
     */
    public abstract void addToArray(ArrayList<?> lst, ArrayList<?> source);
    
    
    
    
    /**
     * Get Column Name
     * 
     * @see javax.swing.table.AbstractTableModel#getColumnName(int)
     */
    @Override
    public String getColumnName(int column)
    {
        return this.m_da.getColumnsTable()[column];
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
    public boolean isCellEditable(int row, int col)
    {
        if (col == this.getCheckableColumn())
            return true;
        else
            return false;
    }

 
    
    

}
