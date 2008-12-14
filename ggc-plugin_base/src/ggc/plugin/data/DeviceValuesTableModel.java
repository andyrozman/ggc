package ggc.plugin.data;

import ggc.core.db.hibernate.GGCHibernateObject;
import ggc.plugin.gui.DeviceDisplayDataDialog;
import ggc.plugin.util.DataAccessPlugInBase;

import java.util.ArrayList;
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
    protected ArrayList<DeviceValuesEntry> dl_data;
    protected ArrayList<DeviceValuesEntry> displayed_dl_data;
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
        this.displayed_dl_data = new ArrayList<DeviceValuesEntry>();
        this.dl_data = new ArrayList<DeviceValuesEntry>();
        // this.dayData = dayData;
        fireTableChanged(null);
        // dayData.addGlucoValueEventListener(this);
    }

    /**
     * Get Column Count
     * 
     * @see javax.swing.table.TableModel#getColumnCount()
     */
    public int getColumnCount()
    {
        return 5;
    }

    /**
     * Is Boolean
     * 
     * @param column column index
     * @return true if column type is boolean
     */
    public boolean isBoolean(int column)
    {
        if (column == 4)
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
        // TODO
        if (column == 4)
            return true;
        else
            return false;

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
        // TODO
        if (column == 0)
        {
            return 100; // third column is bigger
        }
        else
        {
            return 50;
        }

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
            DeviceValuesEntry mve = this.dl_data.get(i);
            
            if (shouldBeDisplayed(mve.getStatus()))
            {
                this.displayed_dl_data.add(mve);
            }
        }
        
        this.fireTableDataChanged();
        
    }
    
    
    /**
     * Should be displayed filter
     * 
     * @param status
     * @return
     */
    public abstract boolean shouldBeDisplayed(int status);

    
    /**
     * Get Row Count
     * 
     * @see javax.swing.table.TableModel#getRowCount()
     */
    public int getRowCount()
    {
        return this.displayed_dl_data.size();
    }

    
    /**
     * Get Value At
     * @see javax.swing.table.TableModel#getValueAt(int, int)
     */
    public abstract Object getValueAt(int row, int column);


    /**
     * Add Entry
     * 
     * @param mve DeviceValuesEntry instance
     */
    public void addEntry(DeviceValuesEntry mve)
    {
        processDeviceValueEntry(mve);
        this.dl_data.add(mve);
        
        if (this.shouldBeDisplayed(mve.getStatus()))
        {
            this.displayed_dl_data.add(mve);
        }
        this.fireTableDataChanged();
    }

    
    /**
     * Process Device Value Entry
     * 
     * @param mve DeviceValuesEntry instance
     */
    public abstract void processDeviceValueEntry(DeviceValuesEntry mve);
    
    
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
            DeviceValuesEntry mve = this.dl_data.get(i);
            
            if (!mve.getChecked())
                continue;
            
            mve.prepareEntry();
            
            if (mve.object_status==DeviceValuesEntry.OBJECT_STATUS_NEW)
            {
                //addToArray(ht.get("ADD").addAll(mve.getDbObjects());
                addToArray(ht.get("ADD"), mve.getDbObjects());
            }
            else if (mve.object_status==DeviceValuesEntry.OBJECT_STATUS_EDIT)
            {
                addToArray(ht.get("EDIT"), mve.getDbObjects());
            }
        }
        
        return ht;
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
    @Override
    public boolean isCellEditable(int row, int col)
    {
        if (col == 4)
            return true;
        else
            return false;
    }

 

}
