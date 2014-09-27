package ggc.pump.data;

import ggc.core.db.hibernate.GGCHibernateObject;
import ggc.plugin.data.DeviceDataHandler;
import ggc.plugin.data.DeviceValuesTableModel;
import ggc.pump.util.DataAccessPump;

import java.util.ArrayList;

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
 *  Filename:     PumpValuesTableModel  
 *  Description:  Model for table of Pump values
 * 
 *  Author: Andy {andy@atech-software.com}
 */

// FIX: Remove unused methods...

public class PumpValuesTableModel extends DeviceValuesTableModel
{

    private static final long serialVersionUID = -3199123443953228082L;

    // private I18nControl m_ic = I18nControl.getInstance();
    // x private DataAccessMeter m_da = DataAccessMeter.getInstance();

    /**
     * Constructor
     * 
     * @param ddh 
     * @param source 
     */
    public PumpValuesTableModel(DeviceDataHandler ddh, String source)
    {
        super(DataAccessPump.getInstance(), ddh, source);

    }

    /**
     * Get Checkable Column (one column if checkable, all others are non-editable)
     * 
     * @return
     */
    @Override
    public int getCheckableColumn()
    {
        return 6;
    }

    /**
     * Process Pump Values Entry
     * 
     * @param mve
     */
    public void processPumpValuesEntry(PumpValuesEntry mve)
    {
        // FIXME need to fix this...
        /*
         * //System.out.println("processMeterValuesEntry");
         * if (old_data!=null)
         * {
         * //System.out.println("oldData != null");
         * long dt = mve.getDt_info(); //.getDateTime();
         * //System.out.println("Dt='" + dt + "'");
         * //System.out.println("Found: " + old_data.containsKey("" + dt));
         * if (!old_data.containsKey("" + dt))
         * {
         * // System.out.println("not Contains");
         * mve.status = PumpValuesEntry.STATUS_NEW;
         * mve.object_status = PumpValuesEntry.OBJECT_STATUS_NEW;
         * }
         * else
         * {
         * // System.out.println("Found !!!");
         * DayValueH gvh = old_data.get("" + dt);
         * // int vl = Integer.parseInt(mve.getBGValue(OutputUtil.BG_MGDL));
         * int vl = 1;
         * //if (((vl-1) >= gvh.getBg()) && (gvh.getBg() <= (vl+1)))
         * if (gvh.getBg()==vl)
         * {
         * mve.status = PumpValuesEntry.STATUS_OLD;
         * mve.object_status = PumpValuesEntry.OBJECT_STATUS_OLD;
         * }
         * else
         * {
         * mve.status = PumpValuesEntry.STATUS_CHANGED;
         * mve.object_status = PumpValuesEntry.OBJECT_STATUS_EDIT;
         * mve.entry_object = gvh;
         * //System.out.println("Changed: " + gvh.getId());
         * }
         * //gvh.getBg()
         * }
         * }
         * else
         * {
         * System.out.println("oldData == null");
         * mve.status = PumpValuesEntry.STATUS_NEW;
         * }
         */
    }

    /**
     * Add To Array 
     * 
     * @param lst
     * @param source
     */
    @Override
    public void addToArray(ArrayList<?> lst, ArrayList<?> source)
    {
    }

    /**
     * Get Empty ArrayList
     * 
     * @return
     */
    @Override
    public ArrayList<? extends GGCHibernateObject> getEmptyArrayList()
    {
        return new ArrayList<GGCHibernateObject>();
    }

    /**
     * Process Device Value Entry
     * 
     * @param mve DeviceValuesEntry instance
     */
    /*
     * @Override
     * public void processDeviceValueEntry(DeviceValuesEntryInterface mve)
     * {
     * System.out.println("processDeviceValuesEntry: Pump");
     * //dvei.getObjectStatus()==DeviceValuesEntry.OBJECT_STATUS_NEW
     * mve.setObjectStatus(DeviceValuesEntry.OBJECT_STATUS_NEW);
     * /*
     * //System.out.println("processMeterValuesEntry");
     * if (old_data!=null)
     * {
     * //System.out.println("oldData != null");
     * long dt = mve.getDt_info(); //.getDateTime();
     * //System.out.println("Dt='" + dt + "'");
     * //System.out.println("Found: " + old_data.containsKey("" + dt));
     * if (!old_data.containsKey("" + dt))
     * {
     * // System.out.println("not Contains");
     * mve.status = PumpValuesEntry.STATUS_NEW;
     * mve.object_status = PumpValuesEntry.OBJECT_STATUS_NEW;
     * }
     * else
     * {
     * // System.out.println("Found !!!");
     * DayValueH gvh = old_data.get("" + dt);
     * // int vl = Integer.parseInt(mve.getBGValue(OutputUtil.BG_MGDL));
     * int vl = 1;
     * //if (((vl-1) >= gvh.getBg()) && (gvh.getBg() <= (vl+1)))
     * if (gvh.getBg()==vl)
     * {
     * mve.status = PumpValuesEntry.STATUS_OLD;
     * mve.object_status = PumpValuesEntry.OBJECT_STATUS_OLD;
     * }
     * else
     * {
     * mve.status = PumpValuesEntry.STATUS_CHANGED;
     * mve.object_status = PumpValuesEntry.OBJECT_STATUS_EDIT;
     * mve.entry_object = gvh;
     * //System.out.println("Changed: " + gvh.getId());
     * }
     * //gvh.getBg()
     * }
     * }
     * else
     * {
     * System.out.println("oldData == null");
     * mve.status = PumpValuesEntry.STATUS_NEW;
     * }
     */

    // }

}
