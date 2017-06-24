package ggc.meter.data.db;

import java.util.*;

import org.hibernate.Query;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.atech.db.hibernate.HibernateDb;
import com.atech.graphics.graphs.v2.data.GraphDefinitionDto;
import com.atech.graphics.graphs.v2.data.GraphTimeDataCollection;

import ggc.core.db.hibernate.pen.DayValueH;
import ggc.core.db.hibernate.pump.PumpDataExtendedH;
import ggc.meter.data.MeterDataReader;
import ggc.meter.data.MeterValuesEntry;
import ggc.meter.data.MeterValuesEntryDataType;
import ggc.meter.util.DataAccessMeter;
import ggc.plugin.data.DeviceValuesEntryInterface;
import ggc.plugin.db.PluginDb;

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
 *  Filename:     GGCMeterDb
 *  Description:  Database handler for Meter Plugin
 * 
 *  Author: Andy {andy@atech-software.com}
 */

public class GGCMeterDb extends PluginDb
{

    private static final Logger LOG = LoggerFactory.getLogger(GGCMeterDb.class);
    DataAccessMeter m_da = DataAccessMeter.getInstance();


    /**
     * Constructor
     * 
     * @param db
     */
    public GGCMeterDb(HibernateDb db)
    {
        super(db, DataAccessMeter.getInstance());
    }


    /**
     * Get All Elements Count
     * 
     * @return
     */
    public int getAllElementsCount()
    {

        int sumAll = getAllElementsCount(DayValueH.class, //
            Arrays.asList(
                Restrictions.or(Restrictions.or(Restrictions.gt("bg", 0), Restrictions.like("extended", "%URINE%")),
                    Restrictions.gt("ch", 0.0f))));

        LOG.debug("Old Meter Data in Db: " + sumAll);

        return sumAll;
    }


    /**
     * Get Meter Values
     * 
     * @param mdr MeterDataReader instance for writing progress on reading this data
     * 
     * @return
     */
    public Map<String, DeviceValuesEntryInterface> getMeterValues(MeterDataReader mdr)
    {
        Map<String, DeviceValuesEntryInterface> ht = new HashMap<String, DeviceValuesEntryInterface>();

        LOG.info("getMeterValues()");

        mdr.writeStatus(-1);

        try
        {
            LOG.debug("getMeterValues() - Process");

            List<DayValueH> hibernateData = getHibernateData(DayValueH.class, //
                Arrays.asList(Restrictions.eq("personId", (int) m_da.getCurrentUserId()), //
                    Restrictions.or(
                        Restrictions.or(Restrictions.gt("bg", 0), //
                            Restrictions.like("extended", "%URINE%")), //
                        Restrictions.gt("ch", 0.0f)), //
                    Restrictions.like("extended", "%" + m_da.getSourceDevice() + "%")), //
                Arrays.asList(Order.asc("dtInfo")));

            int counter = 0;

            mdr.writeStatus(counter);

            for (DayValueH dayValueH : hibernateData)
            {
                counter++;

                MeterValuesEntry mves = new MeterValuesEntry(dayValueH);

                ht.put(mves.getSpecialId(), mves);

                mdr.writeStatus(counter);
            }

            mdr.writeStatus(-2);
        }
        catch (Exception ex)
        {
            LOG.error("getMeterValues.Exception: " + ex, ex);
            ex.printStackTrace();
        }

        System.out.println("Old records: " + ht.size());

        return ht;
    }


    /**
     * Get Pump Data
     * 
     * @param timeMarks
     * @return
     */
    public Map<String, PumpDataExtendedH> getPumpData(Map<Long, HashMap<MeterValuesEntryDataType, Object>> timeMarks)
    {
        Hashtable<String, PumpDataExtendedH> pumpData = new Hashtable<String, PumpDataExtendedH>();

        if (timeMarks.size() == 0)
            return pumpData;

        try
        {
            LOG.debug("getPumpData() - Process");

            // FIXME
            Query q = this.getSession().createQuery(
                " SELECT dv FROM ggc.core.db.hibernate.pump.PumpDataExtendedH as dv " + " WHERE dv.person_id="
                        + m_da.getCurrentUserId() + " AND dv.dt_info IN " + getDataListForSQL(timeMarks)
                        + " AND dv.type IN " + getDataListForSQL(MeterValuesEntryDataType.getAllowedPumpTypes())
                        + " ORDER BY dv.dt_info");

            Iterator<?> it = q.list().iterator();

            while (it.hasNext())
            {
                PumpDataExtendedH pde = (PumpDataExtendedH) it.next();

                int time = (int) (pde.getDtInfo() / 100); // same time as in
                                                          // DailyValueH
                pumpData.put(time + "_" + pde.getType(), pde);
            }

        }
        catch (Exception ex)
        {
            LOG.error("Error getting pump data: " + ex, ex);
        }

        return pumpData;
    }


    /**
     * Get Meter Data (with help of time marks)
     * 
     * @param timeMarks
     * @return
     * @deprecated 
     */
    public Map<Long, MeterValuesEntry> getMeterData(Hashtable<Long, String> timeMarks)
    {

        Map<Long, MeterValuesEntry> meter_data = new HashMap<Long, MeterValuesEntry>();

        if (timeMarks.size() == 0)
            return meter_data;

        try
        {
            LOG.debug("getMeterData() - Process");

            Query q = this.getSession().createQuery( //
                " SELECT dv from ggc.core.db.hibernate.pen.DayValueH as dv " + //
                        " WHERE person_id=" + m_da.getCurrentUserId() + //
                        " AND dv.dt_info IN " + getDataListForSQL(timeMarks) + //
                        " ORDER BY dv.dt_info ASC");

            Iterator<?> it = q.list().iterator();

            while (it.hasNext())
            {
                MeterValuesEntry mve = new MeterValuesEntry((DayValueH) it.next());
                meter_data.put(mve.getDateTime(), mve);
            }

        }
        catch (Exception ex)
        {
            LOG.error("Error getting meter data: " + ex, ex);
        }

        return meter_data;

    }


    // private String getDataListForSQL(Map<?, ?> ht)
    // {
    // StringBuffer sb = new StringBuffer();
    // sb.append(" (");
    //
    // for (Enumeration<?> en = ht.keys(); en.hasMoreElements();)
    // {
    // sb.append(en.nextElement() + ", ");
    // }
    //
    // return sb.substring(0, sb.length() - 2) + ")";
    // }

    private String getDataListForSQL(Map<?, ?> ht)
    {
        StringBuffer sb = new StringBuffer();
        sb.append(" (");

        for (Object key : ht.keySet())
        {
            sb.append(key + ", ");
        }

        return sb.substring(0, sb.length() - 2) + ")";
    }


    public GraphTimeDataCollection getGraphTimeData(GregorianCalendar gcFrom, GregorianCalendar gcTill,
            GraphDefinitionDto definitionDto)
    {
        return null;
    }
}
