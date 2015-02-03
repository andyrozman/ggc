package ggc.meter.data.db;

import ggc.core.db.hibernate.DayValueH;
import ggc.core.db.hibernate.pump.PumpDataExtendedH;
import ggc.meter.data.MeterDataReader;
import ggc.meter.data.MeterValuesEntry;
import ggc.meter.data.MeterValuesEntrySpecial;
import ggc.meter.util.DataAccessMeter;
import ggc.plugin.data.DeviceValuesEntryInterface;
import ggc.plugin.db.PluginDb;

import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Iterator;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;

import com.atech.db.hibernate.HibernateDb;

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
 *  Filename:  ###---###  
 *  Description:
 * 
 *  Author: Andy {andy@atech-software.com}
 */

public class GGCMeterDb extends PluginDb
{
    private static Log log = LogFactory.getLog(GGCMeterDb.class);
    DataAccessMeter m_da = DataAccessMeter.getInstance();

    /**
     * Constructor
     * 
     * @param db
     */
    public GGCMeterDb(HibernateDb db)
    {
        super(db);

        // getAllElementsCount();
    }

    /**
     * Get All Elements Count
     * 
     * @return
     */
    public int getAllElementsCount()
    {
        try
        {
            Integer in = null;
            int sum_all = 0;

            Criteria criteria = this.getSession().createCriteria(DayValueH.class);
            criteria.add(Restrictions.eq("person_id", (int) m_da.getCurrentUserId()));
            criteria.add(Restrictions.or(
                Restrictions.or(Restrictions.gt("bg", 0), Restrictions.like("extended", "%URINE%")),
                Restrictions.gt("ch", 0.0f)));
            // criteria.createCriteria("person_id",
            // (int)dataAccess.getCurrentUserId());
            criteria.setProjection(Projections.rowCount());
            in = (Integer) criteria.list().get(0);
            sum_all = in.intValue();

            log.debug("Old Meter Data in Db: " + in.intValue());

            return sum_all;
        }
        catch (Exception ex)
        {
            log.error("getAllElementsCount: " + ex, ex);
            ex.printStackTrace();
            return 0;
        }
    }

    /**
     * Get Meter Values
     * 
     * @param mdr MeterDataReader instance for writing progress on reading this data
     * 
     * @return
     */
    public Hashtable<String, DeviceValuesEntryInterface> getMeterValues(MeterDataReader mdr)
    {
        // Hashtable<String,DayValueH>
        Hashtable<String, DeviceValuesEntryInterface> ht = new Hashtable<String, DeviceValuesEntryInterface>();

        log.info("getMeterValues()");

        // mdr.se

        mdr.writeStatus(-1);

        try
        {

            log.debug("getMeterValues() - Process");

            Query q = this.getSession().createQuery(
                " SELECT dv from ggc.core.db.hibernate.DayValueH as dv "
                        + " WHERE ((dv.bg>0) OR (dv.extended LIKE '%URINE%') OR (dv.ch>0)) AND person_id="
                        + m_da.getCurrentUserId() + " ORDER BY dv.dt_info ASC");

            // System.out.println("Found elements: " + q.list().size());

            Iterator<?> it = q.list().iterator();

            int counter = 0;

            mdr.writeStatus(counter);

            while (it.hasNext())
            {
                counter++;
                // DayValueH gv = (DayValueH)it.next();

                MeterValuesEntry mves = new MeterValuesEntry((DayValueH) it.next());

                ArrayList<MeterValuesEntrySpecial> lst = mves.getDataEntriesAsMVESpecial();

                for (int i = 0; i < lst.size(); i++)
                {

                    MeterValuesEntry mve = new MeterValuesEntry(lst.get(i));

                    // System.out.println("MVEOLD: " + mve.getSpecialId());

                    if (!ht.containsKey(mve.getSpecialId()))
                    {
                        ht.put("" + mve.getSpecialId(), mve);
                    }

                }

                mdr.writeStatus(counter);
            }

            mdr.writeStatus(-2);
        }
        catch (Exception ex)
        {
            log.error("getMeterValues.Exception: " + ex, ex);
            ex.printStackTrace();
        }

        return ht;
    }

    /**
     * Get Pump Data
     * 
     * @param time_marks
     * @return
     */
    public Hashtable<String, PumpDataExtendedH> getPumpData(Hashtable<Long, String> time_marks)
    {
        Hashtable<String, PumpDataExtendedH> pump_data = new Hashtable<String, PumpDataExtendedH>();

        MeterValuesEntry mve = new MeterValuesEntry();

        if (time_marks.size() == 0)
            return pump_data;

        try
        {
            log.debug("getPumpData() - Process");

            Query q = this.getSession().createQuery(
                " SELECT dv FROM ggc.core.db.hibernate.pump.PumpDataExtendedH as dv " + " WHERE dv.person_id="
                        + m_da.getCurrentUserId() + " AND dv.dt_info IN " + getDataListForSQL(time_marks)
                        + " AND dv.type IN " + getDataListForSQL(mve.getAllowedPumpMappedTypes())
                        + " ORDER BY dv.dt_info");

            Iterator<?> it = q.list().iterator();

            while (it.hasNext())
            {
                PumpDataExtendedH pde = (PumpDataExtendedH) it.next();

                int time = (int) (pde.getDt_info() / 100);
                pump_data.put(time + "_" + pde.getType(), pde);
            }

        }
        catch (Exception ex)
        {
            log.error("Error getting pump data: " + ex, ex);
        }

        return pump_data;
    }

    /**
     * Get Meter Data (with help of time marks)
     * 
     * @param time_marks
     * @return
     */
    public Hashtable<Long, MeterValuesEntry> getMeterData(Hashtable<Long, String> time_marks)
    {

        Hashtable<Long, MeterValuesEntry> meter_data = new Hashtable<Long, MeterValuesEntry>();

        if (time_marks.size() == 0)
            return meter_data;

        try
        {
            log.debug("getMeterData() - Process");

            Query q = this.getSession().createQuery(
                " SELECT dv from ggc.core.db.hibernate.DayValueH as dv " + " WHERE person_id="
                        + m_da.getCurrentUserId() + " AND dv.dt_info IN " + getDataListForSQL(time_marks)
                        + " ORDER BY dv.dt_info ASC");

            Iterator<?> it = q.list().iterator();

            while (it.hasNext())
            {
                MeterValuesEntry mve = new MeterValuesEntry((DayValueH) it.next());
                meter_data.put(mve.getDateTime(), mve);
            }

        }
        catch (Exception ex)
        {
            log.error("Error getting meter data: " + ex, ex);
        }

        return meter_data;

    }

    private String getDataListForSQL(Hashtable<?, ?> ht)
    {
        StringBuffer sb = new StringBuffer();
        sb.append(" (");

        for (Enumeration<?> en = ht.keys(); en.hasMoreElements();)
        {
            sb.append(en.nextElement() + ", ");
        }

        // sb.append(")");

        return sb.substring(0, sb.length() - 2) + ")";
    }

}
