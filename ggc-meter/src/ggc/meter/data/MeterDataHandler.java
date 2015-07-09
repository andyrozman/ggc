package ggc.meter.data;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import ggc.core.db.hibernate.pump.PumpDataExtendedH;
import ggc.meter.data.db.GGCMeterDb;
import ggc.meter.device.MeterInterface;
import ggc.meter.util.DataAccessMeter;
import ggc.plugin.data.DeviceDataHandler;
import ggc.plugin.data.DeviceValuesEntry;
import ggc.plugin.data.DeviceValuesTableModel;
import ggc.plugin.gui.DeviceDisplayDataDialog;
import ggc.plugin.util.DataAccessPlugInBase;

/**
 *  Application:   GGC - GNU Gluco Control
 *  Plug-in:       Meter Tool (support for Meter devices)
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
 *  Filename:     MeterDataHandler
 *  Description:  Data Handler for Meter Tool
 * 
 *  Author: Andy {andy@atech-software.com}
 */

public class MeterDataHandler extends DeviceDataHandler
{

    MeterValuesExtTableModel m_model2;
    private static final Log LOG = LogFactory.getLog(MeterDataHandler.class);

    private HashMap<Long, HashMap<MeterValuesEntryDataType, Object>> timeMarksWithChangedData;


    /**
     * Constructor
     * 
     * @param da
     */
    public MeterDataHandler(DataAccessPlugInBase da)
    {
        super(da);
    }


    /**
     * Execute export Db
     *
     * @see ggc.plugin.data.DeviceDataHandler#executeExportDb()
     */
    @Override
    public void executeExportDb()
    {
        GGCMeterDb db = ((DataAccessMeter) m_da).getDb();

        // 1. get list of selected entries
        HashMap<String, ArrayList<DeviceValuesEntry>> dataList = getDeviceValuesTableModel().getCheckedDVE();
        this.setCustomStatus(1, 5);

        // 2. prepare counters
        this.elementCountAll = 0;
        this.elementCountAll += (dataList.get("ADD").size() * 2);
        this.elementCountAll += (dataList.get("EDIT").size() * 2);

        // 3. add meter entries always add them to old table too
        timeMarksWithChangedData = null;
        timeMarksWithChangedData = new HashMap<Long, HashMap<MeterValuesEntryDataType, Object>>();

        String[] dbCommands = { "ADD", "EDIT" };

        for (String dbCommand : dbCommands)
        {
            ArrayList<DeviceValuesEntry> dveList = dataList.get(dbCommand);

            for (DeviceValuesEntry dve : dveList)
            {
                MeterValuesEntry mve = (MeterValuesEntry) dve;

                HashMap<MeterValuesEntryDataType, Object> changedData = null;

                if (this.old_data.containsKey(mve.getSpecialId()))
                {
                    MeterValuesEntry mveOld = (MeterValuesEntry) this.old_data.get(mve.getSpecialId());

                    changedData = mveOld.getChangedData(mve);

                    boolean changed = mveOld.mergeNewData(changedData);

                    if (changed)
                    {
                        db.edit(mveOld);
                    }
                    else
                    {
                        LOG.warn("No data changed: " + changedData);
                    }
                }
                else
                {
                    changedData = mve.getChangedData();

                    db.add(mve);
                    this.old_data.put(mve.getSpecialId(), mve);
                }

                addChangedDataToMarks(mve.getDateTime(), changedData);

                this.setCustomStatus(2, 0);
            }
        }

        // 4. get pump data (for specific items)
        Hashtable<String, PumpDataExtendedH> pumpData = db.getPumpData(timeMarksWithChangedData);
        this.setCustomStatus(1, 10);


        // 5. add/edit pump items

        for (Map.Entry<Long, HashMap<MeterValuesEntryDataType, Object>> timeData : timeMarksWithChangedData.entrySet())
        {
            for (Map.Entry<MeterValuesEntryDataType, Object> mvedt : timeData.getValue().entrySet())
            {
                String key = timeData.getKey() + "_" + mvedt.getKey().pumpExtCode;
                if (pumpData.containsKey(key))
                {
                    // edit
                    PumpDataExtendedH pdeh = pumpData.get(key);
                    pdeh.setValue(getValueAsString(mvedt.getValue()));
                    pdeh.setExtended("SOURCE=" + m_da.getSourceDevice());
                    pdeh.setChanged(System.currentTimeMillis());

                    db.editHibernate(pdeh);
                }
                else
                {
                    // add
                    PumpDataExtendedH pdeh = new PumpDataExtendedH();

                    pdeh.setDt_info(timeData.getKey() * 100);
                    pdeh.setType(mvedt.getKey().pumpExtCode);
                    pdeh.setValue(getValueAsString(mvedt.getValue()));
                    pdeh.setPerson_id((int) m_da.getCurrentUserId());
                    pdeh.setExtended("SOURCE=" + m_da.getSourceDevice());
                    pdeh.setChanged(System.currentTimeMillis());

                    db.addHibernate(pdeh);
                }
            }
        }

        setCustomStatus(3, 0);
        this.m_dvtm.setFilter(DeviceDisplayDataDialog.FILTER_NEW_CHANGED, true);

    }


    private String getValueAsString(Object val)
    {
        if (val instanceof String)
        {
            return (String) val;
        }
        else if (val instanceof Float)
        {
            return m_da.getFormatedValue((Float) val, 1);
        }
        else if (val instanceof Integer)
        {
            return m_da.getFormatedValue((Integer) val, 0);
        }

        return null;
    }


    private void addChangedDataToMarks(long dateTime, HashMap<MeterValuesEntryDataType, Object> changedData)
    {
        if (this.timeMarksWithChangedData.containsKey(dateTime))
        {
            this.timeMarksWithChangedData.get(dateTime).putAll(changedData);
        }
        else
        {
            this.timeMarksWithChangedData.put(dateTime, changedData);
        }
    }


    int current_static_stat = 0;
    int element_current = 0;
    int elementCountAll = 0;


    private void setCustomStatus(int type, int number)
    {

        // type: 1 - just add, 2 - calculate *must be less than 100, 3 - finish
        if (type == 1)
        {
            this.current_static_stat = number;
            this.setStatus(current_static_stat);
        }
        else if (type == 2)
        {
            element_current++;

            float f = element_current * 1.0f / elementCountAll * 90.0f;
            f += current_static_stat;

            int proc = (int) f;

            if (proc == 100)
            {
                proc = 99;
            }

            this.setStatus(proc);
        }
        else if (type == 3)
        {
            this.setStatus(100);
            this.export_dialog.setReadingFinished();
        }

    }


    /**
     * Create Device Values Table Model
     */
    @Override
    public void createDeviceValuesTableModel()
    {
        this.m_model = new MeterValuesTableModel(this, m_da.getSourceDevice());
        this.m_model2 = new MeterValuesExtTableModel(this, m_da.getSourceDevice());
    }


    /**
     * Get Device Values Table Model (for this tool we override main method)
     * 
     * @return DeviceValuesTableModel instance (or derivate thereof)
     */
    @Override
    public DeviceValuesTableModel getDeviceValuesTableModel()
    {
        if (m_model == null)
        {
            createDeviceValuesTableModel();
        }

        MeterInterface mi = (MeterInterface) m_da.getSelectedDeviceInstance();

        if (mi.getInterfaceTypeForMeter() == MeterInterface.METER_INTERFACE_SIMPLE)
            return m_model;
        else
            return m_model2;
    }


    /** 
     * Set Reading Finished
     */
    public void setReadingFinished()
    {
    }

}
