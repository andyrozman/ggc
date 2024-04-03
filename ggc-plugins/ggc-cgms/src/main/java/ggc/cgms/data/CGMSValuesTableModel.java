package ggc.cgms.data;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.atech.utils.data.ATechDate;

import ggc.cgms.util.DataAccessCGMS;
import ggc.plugin.data.DeviceDataHandler;
import ggc.plugin.data.DeviceValuesEntryInterface;
import ggc.plugin.data.DeviceValuesTableModel;

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
 *  Filename:     CGMValuesTableModel  
 *  Description:  Model for table of CGMS values
 * 
 *  Author: Andy {andy@atech-software.com}
 */

public class CGMSValuesTableModel extends DeviceValuesTableModel implements CGMSDataCapable
{

    private static final long serialVersionUID = 2881771615052748327L;

    private static final Logger LOG = LoggerFactory.getLogger(CGMSValuesTableModel.class);

    Map<String, CGMSValuesEntry> dataTable = null;
    String old_key = null;
    CGMSValuesEntry current_main = null;


    /**
     * Constructor
     * 
     * @param ddh DeviceDataHandler instance
     * @param source 
     */
    public CGMSValuesTableModel(DeviceDataHandler ddh, String source)
    {
        super(DataAccessCGMS.getInstance(), ddh, source);
        dataTable = new HashMap<String, CGMSValuesEntry>();
    }


    @Override
    protected void initColumns()
    {
        addColumn(0, "DATETIME", 110, false);
        addColumn(1, "ENTRY_TYPE", 150, false);
        addColumn(2, "READING", 60, false);
        addColumn(3, "STATUS", 110, false);
        addColumn(4, "", 50, true);
    }


    /**
     * Add Entry
     *
     * @param mve DeviceValuesEntry instance
     */
    @Override
    public void addEntry(DeviceValuesEntryInterface mve)
    {
        if (mve instanceof CGMSValuesSubEntry)
        {

            CGMSValuesSubEntry se = (CGMSValuesSubEntry) mve;

            String key = se.date + "_" + se.getType();

            // System.out.println("SE: " + key);

            if (old_key == null)
            {
                old_key = key;
                this.current_main = new CGMSValuesEntry();
            }

            if (!old_key.equals(key))
            {
                this.current_main.sortSubs();

                addEntryAndProcess(old_key, this.current_main);

                this.current_main = new CGMSValuesEntry();

            }

            if (current_main.isEmpty())
            {
                this.current_main.setDateTimeObject(new ATechDate(ATechDate.FORMAT_DATE_AND_TIME_S, se.datetime));
                this.current_main.setDate(se.date);

                this.current_main.setEmpty(false);
                this.current_main.setType(se.getType());
                this.current_main.addSubEntry(se);

                old_key = key;
            }
            else
            {
                this.current_main.addSubEntry(se);
            }
        }
        else if (mve instanceof CGMSValuesExtendedEntry)
        {
            addEntryAndProcess(mve);
            // processDeviceValueEntry(mve);
            // this.dl_data.add(mve);
            //
            // if (this.shouldBeDisplayedOld(mve.getStatusType()))
            // {
            // this.displayed_dl_data.add(mve);
            // Collections.sort(displayed_dl_data);
            //
            // this.fireTableDataChanged();
            // }
        }
        else
        {
            LOG.warn("Unsupported database type: " + mve);
        }

    }


    private void addEntryAndProcess(DeviceValuesEntryInterface mve)
    {
        processDeviceValueEntry(mve);
        this.dl_data.add(mve);

        if (this.shouldBeDisplayed(mve.getStatusType()))
        {
            this.displayed_dl_data.add(mve);
            Collections.sort(displayed_dl_data);

            this.fireTableDataChanged();
        }

    }


    private void addEntryAndProcess(String key, CGMSValuesEntry mve)
    {
        if (dataTable.containsKey(key))
        {
            CGMSValuesEntry mveOld = dataTable.get(key);

            mveOld.addSubEntries(mve.getSubEntryList());
            // processDeviceValueEntry(mveOld);
            //
            // if (!this.shouldBeDisplayedOld(mve.getStatusType()))
            // {
            // if (this.displayed_dl_data.contains(mveOld))
            // {
            // this.displayed_dl_data.clear();
            //
            // for (DeviceValuesEntryInterface entryInt : dl_data)
            // {
            // this.displayed_dl_data.add(entryInt);
            // }
            //
            // Collections.sort(displayed_dl_data);
            //
            // this.fireTableDataChanged();
            // }
            // }
        }
        else
        {
            addEntryAndProcess(mve);
            dataTable.put(key, mve);

            // processDeviceValueEntry(mve);
            // this.dl_data.add(mve);
            // dataTable.put(key, mve);
            //
            // if (this.shouldBeDisplayedOld(mve.getStatusType()))
            // {
            // this.displayed_dl_data.add(mve);
            // Collections.sort(displayed_dl_data);
            //
            // this.fireTableDataChanged();
            // }
        }

    }


    /**
     * Finish Reading
     */
    public void finishReading()
    {
        processDeviceValueEntry(this.current_main);
        this.dl_data.add(this.current_main);

        if (this.shouldBeDisplayed(this.current_main.getStatusType()))
        {
            this.displayed_dl_data.add(this.current_main);
            Collections.sort(displayed_dl_data);
        }
        this.fireTableDataChanged();
    }

}
