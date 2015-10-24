package ggc.plugin.data;

import java.util.*;

import com.atech.utils.data.ATechDate;
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
 *  Filename:     DeviceValuesDay
 *  Description:  Device Values Day, with table constructs for Daily Overview table
 *
 *  Author: Andy {andy@atech-software.com}
 */

public class DeviceValuesDay
{

    private List<DeviceValuesEntry> allEntries = null;
    private Hashtable<String, DeviceValuesEntry> entriesIndexedByHour = null;
    private Hashtable<Integer, List<DeviceValuesEntry>> entriesGroupedByHour = null;

    DataAccessPlugInBase dataAccessPluginBase;
    GregorianCalendar gcToday;
    boolean useIndex = false; // we can index files for easy access, but this
                              // might not work
                              // for each instance of this object


    /**
     * Constructor
     *
     * @param da
     */
    public DeviceValuesDay(DataAccessPlugInBase da)
    {
        this(da, null, true);
    }


    /**
     * Constructor
     *
     * @param da
     * @param gc
     */
    public DeviceValuesDay(DataAccessPlugInBase da, GregorianCalendar gc)
    {
        this(da, gc, true);
    }


    /**
     * Constructor
     *
     * @param da
     * @param gc
     * @param use_index
     */
    public DeviceValuesDay(DataAccessPlugInBase da, GregorianCalendar gc, boolean use_index)
    {
        this.dataAccessPluginBase = da;
        allEntries = new ArrayList<DeviceValuesEntry>();
        this.useIndex = use_index;

        if (use_index)
        {
            entriesIndexedByHour = new Hashtable<String, DeviceValuesEntry>();
        }
        this.gcToday = gc;
    }


    /**
     * Add List
     *
     * @param lst
     */
    public void addList(ArrayList<? extends DeviceValuesEntry> lst)
    {
        for (DeviceValuesEntry dve : lst)
        {
            this.addEntry(dve, false);
        }

        this.sort();
    }


    public void addEntry(DeviceValuesEntry pve)
    {
        addEntry(pve, true);
    }


    /**
     * Add Entry
     *
     * @param pve
     *            DeviceValuesEntry instance (or derivate)
     */
    public void addEntry(DeviceValuesEntry pve, boolean doSort)
    {
        this.allEntries.add(pve);

        if (doSort)
        {
            this.sort();
        }

        if (this.useIndex)
        {
            ATechDate atd = new ATechDate(pve.getDateTimeFormat(), pve.getDateTime());

            if (!this.entriesIndexedByHour.containsKey(atd.getTimeString()))
            {
                this.entriesIndexedByHour.put(atd.getTimeString(), pve);
            }
        }

    }


    /**
     * Remove Entry
     *
     * @param index
     */
    public void removeEntry(int index)
    {
        DeviceValuesEntryInterface dvei = this.allEntries.get(index);
        this.allEntries.remove(index);

        if (this.useIndex)
        {
            ATechDate atd = new ATechDate(dvei.getDateTimeFormat(), dvei.getDateTime());

            if (!this.entriesIndexedByHour.containsKey(atd.getTimeString()))
            {
                this.entriesIndexedByHour.remove(dvei);
            }
        }

    }


    /**
     * Is Entry Available
     *
     * @param datetime
     * @return
     */
    public boolean isEntryAvailable(long datetime)
    {
        if (!this.useIndex)
            return false;

        ATechDate atd = new ATechDate(dataAccessPluginBase.getDataEntryObject().getDateTimeFormat(), datetime);
        return this.entriesIndexedByHour.containsKey(atd.getTimeString());
    }


    /**
     * Get Entry
     *
     * @param dt
     * @return
     */
    public DeviceValuesEntry getEntry(long dt)
    {
        if (!this.useIndex)
            return null;

        ATechDate atd = new ATechDate(dataAccessPluginBase.getDataEntryObject().getDateTimeFormat(), dt);
        return this.entriesIndexedByHour.get(atd.getTimeString());
    }


    /**
     * Set Date Time GC
     *
     * @param gc
     */
    public void setDateTimeGC(GregorianCalendar gc)
    {
        this.gcToday = gc;
    }


    /**
     * Set Date Time GC
     *
     * @return
     */
    public GregorianCalendar getDateTimeGC()
    {
        return this.gcToday;
    }


    /**
     * Get Column Width
     *
     * @param column
     * @param width
     * @return
     */
    public int getColumnWidth(int column, int width)
    {
        float mult;
        switch (column)
        {
            case 0:
                mult = 0.1f;

            default:
                mult = 0.2f;
        }

        return (int) (mult * width);
    }


    /**
     * Get Column Count
     *
     * @return
     */
    public int getColumnCount()
    {
        return this.dataAccessPluginBase.getColumnsManual().length;
    }


    /**
     * Get Row Count
     *
     * @return
     */
    public int getRowCount()
    {
        if (this.allEntries == null)
            return 0;
        else
            return this.allEntries.size();
    }


    /**
     * Get Row At
     *
     * @param index
     * @return
     */
    public DeviceValuesEntry getRowAt(int index)
    {
        return this.allEntries.get(index);
    }


    /**
     * Get Column Name
     *
     * @param column
     * @return
     */
    public String getColumnName(int column)
    {
        return this.dataAccessPluginBase.getColumnsManual()[column];
    }


    /**
     * Get Value At
     *
     * @param row
     * @param column
     * @return
     */
    public Object getValueAt(int row, int column)
    {
        return this.allEntries.get(row).getColumnValue(column);
    }


    /**
     * Sort of elements
     */
    public void sort()
    {
        Collections.sort(allEntries); // , new DeviceValuesEntry());
    }


    /**
     * Sort of elements
     *
     * @param dve
     */
    public void sort(Comparator<DeviceValuesEntry> dve)
    {
        Collections.sort(allEntries, dve); // , new DeviceValuesEntry());
    }


    /**
     * Get List
     *
     * @return
     */
    public List<DeviceValuesEntry> getList()
    {
        return this.allEntries;
    }


    /**
     * Group entries by Hour
     */
    public void prepareHourlyEntries()
    {
        if (entriesGroupedByHour != null)
            return;

        this.sort();

        entriesGroupedByHour = new Hashtable<Integer, List<DeviceValuesEntry>>();

        for (int i = 0; i < 24; i++)
        {
            entriesGroupedByHour.put(i, new ArrayList<DeviceValuesEntry>());
        }

        for (DeviceValuesEntry dve : this.allEntries)
        {
            ATechDate atd = new ATechDate(dve.getDateTimeFormat(), dve.getDateTime());
            entriesGroupedByHour.get(atd.getHourOfDay()).add(dve);
        }
    }


    public List<DeviceValuesEntry> getEntriesForHour(int hour)
    {
        return entriesGroupedByHour.get(hour);
    }

}
