package ggc.cgms.data.db;

import java.util.Arrays;
import java.util.GregorianCalendar;
import java.util.Hashtable;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.atech.db.hibernate.HibernateDb;
import com.atech.graphics.graphs.v2.data.GraphDbDataRetriever;
import com.atech.graphics.graphs.v2.data.GraphDefinitionDto;
import com.atech.graphics.graphs.v2.data.GraphTimeDataCollection;
import com.atech.utils.data.ATechDate;
import com.atech.utils.data.ATechDateType;

import ggc.cgms.data.CGMSDataReader;
import ggc.cgms.data.CGMSValuesEntry;
import ggc.cgms.util.DataAccessCGMS;
import ggc.core.db.hibernate.cgms.CGMSDataH;
import ggc.plugin.data.DeviceValuesDay;
import ggc.plugin.data.DeviceValuesEntryInterface;
import ggc.plugin.db.PluginDb;

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
 *  Filename:      GGC_CGMSDb
 *  Description:   CGMS database handler
 * 
 *  Author: Andy {andy@atech-software.com}
 */

public class GGC_CGMSDb extends PluginDb implements GraphDbDataRetriever
{

    private static final Logger LOG = LoggerFactory.getLogger(GGC_CGMSDb.class);


    /**
     * Constructor
     * 
     * @param db
     */
    public GGC_CGMSDb(HibernateDb db)
    {
        super(db, DataAccessCGMS.getInstance());
        // LOG.debug("Created CGMSDb");
        // getAllElementsCount();
    }


    /**
     * Get Daily Pump Values
     * 
     * @param gc
     * @return
     */
    public DeviceValuesDay getDailyCGMSValues(GregorianCalendar gc)
    {
        LOG.info("getDailyCGMSValues()");

        long dt = ATechDate.getATDateTimeFromGC(gc, ATechDateType.DateOnly);
        // long dt = 20070323;

        DeviceValuesDay dV = new DeviceValuesDay(dataAccess);
        dV.setDateTimeGC(gc);

        String sql = "";

        try
        {
            List<CGMSDataH> dataList = getHibernateData(CGMSDataH.class, //
                Arrays.asList( //
                    Restrictions.eq("personId", this.dataAccess.getCurrentUserId()), //
                    Restrictions.eq("dtInfo", dt)) //
                , Arrays.asList(Order.asc("dtInfo")));

            for (CGMSDataH cgmsDataH : dataList)
            {
                CGMSValuesEntry dv = new CGMSValuesEntry(cgmsDataH);

                dV.addList(dv.getSubEntryList());
            }

        }
        catch (Exception ex)
        {
            LOG.debug("Sql: " + sql);
            LOG.error("getCGMSStats(). Exception: " + ex, ex);
        }

        return dV;

        // return null;
    }

    // getHibernateData


    /**
     * Get Pump Values Range
     * 
     * @param from 
     * @param to 
     * @return
     */
    // public DeviceValuesRange getRangePumpValues(GregorianCalendar from, GregorianCalendar to)
    // {
    //
    //
    //
    // ATDataAccessAbstract.notImplemented("GGC_CGMSDb::getRangePumpValues()");
    // return null;
    //
    // /*
    // * log.info("getPumpDayStats()");
    // * long dt_from = ATechDate.getATDateTimeFromGC(from,
    // * ATechDate.FORMAT_DATE_ONLY);
    // * long dt_to = ATechDate.getATDateTimeFromGC(to,
    // * ATechDate.FORMAT_DATE_ONLY);
    // * String sql = "";
    // * DeviceValuesRange dvr = new
    // * DeviceValuesRange(DataAccessPump.getInstance(), from, to);
    // * try
    // * {
    // * sql = "SELECT dv from " +
    // * "ggc.core.db.hibernate.pump.PumpDataH as dv " +
    // * "WHERE dv.dt_info >= " + dt_from
    // * + "000000 AND dv.dt_info <= " + dt_to + "235959 ORDER BY dv.dt_info";
    // * //System.out.println("SQL: " + sql);
    // * Query q = this.db.getSession().createQuery(sql);
    // * Iterator<?> it = q.list().iterator();
    // * while (it.hasNext())
    // * {
    // * PumpDataH pdh = (PumpDataH)it.next();
    // * PumpValuesEntry dv = new PumpValuesEntry(pdh);
    // * dvr.addEntry(dv);
    // * }
    // * ArrayList<PumpValuesEntryExt> lst_ext =
    // * getRangePumpValuesExtended(from, to);
    // * mergeRangePumpData(dvr, lst_ext);
    // * // ArrayList<PumpValuesEntryExt> lst_ext =
    // * getDailyPumpValuesExtended(gc);
    // * // mergeDailyPumpData(dV, lst_ext);
    // * }
    // * catch (Exception ex)
    // * {
    // * log.debug("Sql: " + sql);
    // * log.error("getDayStats(). Exception: " + ex, ex);
    // * }
    // * return dvr;
    // */
    // }

    /**
     * Get All Elements Count
     * 
     * @return
     */
    public int getAllElementsCount()
    {
        return getAllElementsCount(CGMSDataH.class, null);
    }


    /**
     * Get Pump Values
     * @param pdr
     * @return
     */
    public Hashtable<String, DeviceValuesEntryInterface> getCGMSValues(CGMSDataReader pdr)
    {
        String sql = "";

        Hashtable<String, DeviceValuesEntryInterface> dt = new Hashtable<String, DeviceValuesEntryInterface>();

        try
        {
            int counter = 0;

            List<CGMSDataH> dataList = getHibernateData(CGMSDataH.class, //
                Arrays.asList( //
                    Restrictions.eq("personId", this.dataAccess.getCurrentUserId()), //
                    Restrictions.like("extended", "%" + dataAccess.getSourceDevice() + "%")) //
                , Arrays.asList(Order.asc("dtInfo")));

            pdr.writeStatus(-1);

            for (CGMSDataH cgmsDataH : dataList)
            {
                counter++;

                CGMSValuesEntry pve = new CGMSValuesEntry(cgmsDataH);
                dt.put(pve.getSpecialId(), pve);
                pdr.writeStatus(counter);
            }

            pdr.finishReading();
        }
        catch (Exception ex)
        {
            // System.out.println("Exception on getCGMSValues: " + ex);
            LOG.error("Exception on getCGMSValues.\nsql:" + sql + "\nEx: " + ex, ex);
        }

        return dt;
    }


    public GraphTimeDataCollection getGraphTimeData(GregorianCalendar gcFrom, GregorianCalendar gcTill,
            GraphDefinitionDto definitionDto)
    {
        // TODO types, for now we get only glucose data

        GraphTimeDataCollection collection = new GraphTimeDataCollection();

        List<ATechDate> dates = dataAccess.getDatesList(gcFrom, gcTill, true);

        Criteria criteria = null;

        try
        {
            criteria = getSession().createCriteria(CGMSDataH.class)
                    .add(Restrictions.eq("personId", (int) dataAccess.getCurrentUserId()))
                    .add(Restrictions.eq("baseType", 1))
                    .add(Restrictions.and(Restrictions.ge("dtInfo", dates.get(0).getATDateTimeAsLong()),
                        Restrictions.le("dtInfo", dates.get(1).getATDateTimeAsLong())));

            List listData = criteria.list();

            for (Object cgg : listData)
            {
                CGMSDataH pdh = (CGMSDataH) cgg;
                CGMSValuesEntry dv = new CGMSValuesEntry(pdh);

                collection.put(dv.getDateTimeObject().getATDateTimeAsLong(), dv.getGraphSubEntryList());
            }

        }
        catch (Exception ex)
        {
            LOG.debug("Criteria: " + criteria);
            LOG.error("getGraphTimeData(). Exception: " + ex, ex);
        }

        return collection;
    }
}
