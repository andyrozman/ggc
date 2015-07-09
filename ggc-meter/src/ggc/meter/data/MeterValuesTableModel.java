package ggc.meter.data;

import ggc.meter.util.DataAccessMeter;
import ggc.plugin.data.DeviceDataHandler;
import ggc.plugin.data.DeviceValuesEntry;
import ggc.plugin.data.DeviceValuesEntryInterface;
import ggc.plugin.data.DeviceValuesTableModel;
import ggc.plugin.data.enums.DeviceEntryStatus;


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
 *  Filename:     MeterValuesTableModel
 *  Description:  MeterValues Table Model
 * 
 *  Author: Andy {andy@atech-software.com}
 */

public class MeterValuesTableModel extends DeviceValuesTableModel
{

    private static final long serialVersionUID = 7198690314603156531L;


    /**
     * Constructor
     * 
     * @param ddh DeviceDataHandler instance
     */
    public MeterValuesTableModel(DeviceDataHandler ddh, String source)
    {
        super(DataAccessMeter.getInstance(), ddh, source);
        this.debug = true;
    }


    /**
     * Get Value At
     * @see javax.swing.table.TableModel#getValueAt(int, int)
     */
    @Override
    public Object getValueAt(int row, int column)
    {
        MeterValuesEntry mve = (MeterValuesEntry) this.displayed_dl_data.get(row);

        switch (column)
        {
            case 0:
                return mve.getDateTimeObject().getDateTimeString();

            case 1:
                return mve.getBgOriginal();

            case 2:
                return mve.getBgMmolL();

            case 3:
                return mve.getStatusType();

            case 4:
                return mve.getChecked();

            default:
                return "";
        }

    }


    /**
     * Process Device Value Entry
     *
     * @param dve DeviceValuesEntry instance
     */
    @Override
    public void processDeviceValueEntry(DeviceValuesEntryInterface dve)
    {
        if (this.deviceDataHandler.hasOldData())
        {
            if (!this.deviceDataHandler.getOldData().containsKey("" + dve.getSpecialId()))
            {
                dve.setStatusType(DeviceEntryStatus.New);
                dve.setObjectStatus(DeviceValuesEntry.OBJECT_STATUS_NEW);
            }
            else
            {

                MeterValuesEntry mve_old = (MeterValuesEntry) this.deviceDataHandler.getOldData().get(
                        dve.getSpecialId());

                MeterValuesEntry mve2 = (MeterValuesEntry) dve;

                DeviceEntryStatus status = mve_old.getImportStatus(mve2);

                mve2.setStatusType(status);
                mve2.object_status = (status == DeviceEntryStatus.Changed) ?
                        DeviceValuesEntry.OBJECT_STATUS_EDIT : DeviceValuesEntry.OBJECT_STATUS_OLD;
            }
        }
        else
        {
            dve.setStatusType(DeviceEntryStatus.New);
            dve.setObjectStatus(DeviceValuesEntry.OBJECT_STATUS_NEW);
        }
    }


    @Override
    protected void initColumns()
    {
        addColumn(0, "DATETIME", 100, false);
        addColumn(1, "BG_MMOLL", 50, false);
        addColumn(2, "BG_MGDL", 50, false);
        addColumn(3, "STATUS", 50, false);
        addColumn(4, "", 50, true);
    }

}
