package ggc.meter.data;

import com.atech.i18n.I18nControlAbstract;

import ggc.meter.util.DataAccessMeter;
import ggc.plugin.data.DeviceDataHandler;
import ggc.plugin.data.DeviceValuesEntry;
import ggc.plugin.data.DeviceValuesEntryInterface;
import ggc.plugin.data.DeviceValuesTableModel;

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

public class MeterValuesExtTableModel extends DeviceValuesTableModel
{

    private I18nControlAbstract m_ic = DataAccessMeter.getInstance().getI18nControlInstance();

    private static final long serialVersionUID = -660580365600276458L;


    /**
     * Constructor
     * 
     * @param ddh DeviceDataHandler instance
     */
    public MeterValuesExtTableModel(DeviceDataHandler ddh, String source)
    {
        super(DataAccessMeter.getInstance(), ddh, source);
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
                return mve.getExtendedTypeDescription();

            case 2:
                return mve.getExtendedTypeValue(true);

            case 3:
                return mve.getStatusType();

            case 4:
                return new Boolean(mve.getChecked());

            default:
                return "";
        }

    }


//    /**
//     * Process Device Value Entry
//     *
//     * @param mve DeviceValuesEntry instance
//     */
//    @Override
//    @SuppressWarnings("deprecation")
//    public void processDeviceValueEntry(DeviceValuesEntryInterface mve)
//    {
//
//        if (this.deviceDataHandler.hasOldData())
//        {
//            // System.out.println("OLD Data" );
//            if (!this.deviceDataHandler.getOldData().containsKey("" + mve.getSpecialId()))
//            {
//
//                mve.setStatus(DeviceValuesEntry.STATUS_NEW);
//                mve.setObjectStatus(DeviceValuesEntry.OBJECT_STATUS_NEW);
//            }
//            else
//            {
//
//                MeterValuesEntry mve2 = (MeterValuesEntry) mve;
//                MeterValuesEntry mve_old = (MeterValuesEntry) this.deviceDataHandler.getOldData().get(
//                    mve.getSpecialId());
//
//                mve_old.prepareEntry_v2();
//
//                if (mve_old.getValueFull().equals(mve2.getValueFull()))
//                {
//                    mve2.setStatus(DeviceValuesEntry.STATUS_OLD);
//                    mve2.object_status = DeviceValuesEntry.OBJECT_STATUS_OLD;
//                }
//                else
//                {
//                    mve2.setStatus(DeviceValuesEntry.STATUS_CHANGED);
//                    mve2.object_status = DeviceValuesEntry.OBJECT_STATUS_EDIT;
//                    mve2.entry_object = mve_old.getHibernateObject();
//                }
//            }
//        }
//        else
//        {
//            mve.setStatus(DeviceValuesEntry.STATUS_NEW);
//        }
//
//    }


    @Override
    protected void initColumns()
    {
        addColumn(0, "DATETIME", 120, false);
        addColumn(1, "ENTRY_TYPE", 150, false);
        addColumn(2, "VALUE", 200, false);
        addColumn(3, "STATUS", 90, false);
        addColumn(4, "", 50, true);
    }

}
