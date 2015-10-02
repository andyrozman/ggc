package ggc.pump.data.db;

import java.util.*;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;

import com.atech.db.hibernate.HibernateDb;
import com.atech.utils.data.ATechDate;

import ggc.core.db.hibernate.pump.PumpDataExtendedH;
import ggc.core.db.hibernate.pump.PumpDataH;
import ggc.core.db.hibernate.pump.PumpProfileH;
import ggc.plugin.data.DeviceValuesDay;
import ggc.plugin.data.DeviceValuesEntry;
import ggc.plugin.data.DeviceValuesEntryInterface;
import ggc.plugin.data.DeviceValuesRange;
import ggc.plugin.db.PluginDb;
import ggc.plugin.graph.data.GraphValuesCapable;
import ggc.plugin.graph.data.GraphValuesCollection;
import ggc.plugin.graph.data.PlugInGraphDb;
import ggc.pump.data.PumpDataReader;
import ggc.pump.data.PumpValuesEntry;
import ggc.pump.data.PumpValuesEntryExt;
import ggc.pump.data.PumpValuesEntryProfile;
import ggc.pump.data.defs.PumpBaseType;
import ggc.pump.db.PumpProfile;
import ggc.pump.util.DataAccessPump;

/**
 * Application: GGC - GNU Gluco Control Plug-in: Pump Tool (support for Pump
 * devices)
 *
 * See AUTHORS for copyright information.
 * 
 * This program is free software; you can redistribute it and/or modify it under
 * the terms of the GNU General Public License as published by the Free Software
 * Foundation; either version 2 of the License, or (at your option) any later
 * version.
 * 
 * This program is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU General Public License for more
 * details.
 * 
 * You should have received a copy of the GNU General Public License along with
 * this program; if not, write to the Free Software Foundation, Inc., 59 Temple
 * Place, Suite 330, Boston, MA 02111-1307 USA
 * 
 * Filename: GGCPumpDb Description: Db handler for Pump Plugin
 * 
 * Author: Andy {andy@atech-software.com}
 */

public class GGCPumpDb extends PluginDb implements PlugInGraphDb
{

    private static Log log = LogFactory.getLog(GGCPumpDb.class);
    DataAccessPump m_da = DataAccessPump.getInstance();


    /**
     * Constructor
     *
     * @param db
     */
    public GGCPumpDb(HibernateDb db)
    {
        super(db);

        // getAllElementsCount();
    }


    /**
     * Get Daily Pump Values
     *
     * @param gc
     * @return
     */
    public DeviceValuesDay getDailyPumpValues(GregorianCalendar gc)
    {
        log.info("getPumpDayStats()");

        long dt = ATechDate.getATDateTimeFromGC(gc, ATechDate.FORMAT_DATE_ONLY);

        DeviceValuesDay dV = new DeviceValuesDay(m_da);
        dV.setDateTimeGC(gc);

        String sql = "";

        try
        {
            sql = "SELECT dv from " + "ggc.core.db.hibernate.pump.PumpDataH as dv " + "WHERE dv.dt_info >=  " + dt
                    + "000000 AND dv.dt_info <= " + dt + "235959 ORDER BY dv.dt_info";

            Query q = this.db.getSession().createQuery(sql);

            Iterator<?> it = q.list().iterator();

            while (it.hasNext())
            {
                PumpDataH pdh = (PumpDataH) it.next();
                PumpValuesEntry dv = new PumpValuesEntry(pdh);

                dV.addEntry(dv);
            }

            ArrayList<PumpValuesEntryExt> lst_ext = getDailyPumpValuesExtended(gc);
            mergeDailyPumpData(dV, lst_ext);
        }
        catch (Exception ex)
        {
            log.debug("Sql: " + sql);
            log.error("getDayStats(). Exception: " + ex, ex);
        }

        return dV;

        // return null;
    }


    /**
     * Get Pump Values Range
     *
     * @param from
     * @param to
     * @return
     */
    public DeviceValuesRange getRangePumpValues(GregorianCalendar from, GregorianCalendar to)
    {
        log.info("getRangePumpValues()");

        DeviceValuesRange dvr = new DeviceValuesRange(DataAccessPump.getInstance(), from, to);

        List<PumpDataH> listPumpData = getRangePumpValuesRaw(from, to);

        for (PumpDataH pdh : listPumpData)
        {
            PumpValuesEntry dv = new PumpValuesEntry(pdh);
            dvr.addEntry(dv);
        }

        List<PumpDataExtendedH> lst_ext = getRangePumpValuesExtendedRaw(from, to);
        mergeRangePumpData(dvr, lst_ext);

        return dvr;
    }


    /**
     * Get Pump Values Range
     *
     * @param from
     * @param to
     * @return
     */
    public List<PumpDataH> getRangePumpValuesRaw(GregorianCalendar from, GregorianCalendar to)
    {
        return getRangePumpValuesRaw(from, to, null);
    }


    public List<PumpDataH> getRangePumpValuesRaw(GregorianCalendar from, GregorianCalendar to, String filter)
    {
        log.info(String.format("getRangePumpValuesRaw(%s)", filter == null ? "" : filter));

        long dt_from = ATechDate.getATDateTimeFromGC(from, ATechDate.FORMAT_DATE_ONLY);
        long dt_to = ATechDate.getATDateTimeFromGC(to, ATechDate.FORMAT_DATE_ONLY);

        String sql = "";

        List<PumpDataH> listPumpData = new ArrayList<PumpDataH>();

        try
        {
            sql = "SELECT dv from " + //
                    "ggc.core.db.hibernate.pump.PumpDataH as dv " + //
                    "WHERE dv.dt_info >=  " + dt_from + "000000 AND dv.dt_info <= " + //
                    dt_to + "235959 ";

            if (filter != null)
            {
                sql += " AND " + filter;
            }

            sql += " ORDER BY dv.dt_info";

            Query q = this.db.getSession().createQuery(sql);

            Iterator<?> it = q.list().iterator();

            while (it.hasNext())
            {
                PumpDataH pdh = (PumpDataH) it.next();
                listPumpData.add(pdh);
            }

        }
        catch (Exception ex)
        {
            log.debug("Sql: " + sql);
            log.error("getDayStats(). Exception: " + ex, ex);
        }

        log.debug("Found " + listPumpData.size() + " entries.");

        return listPumpData;
    }


    /**
     * Get Daily Pump Values Extended
     *
     * @param gc
     * @return
     */
    public ArrayList<PumpValuesEntryExt> getDailyPumpValuesExtended(GregorianCalendar gc)
    {
        log.info("getDailyPumpValuesExtended() - Run");

        long dt = ATechDate.getATDateTimeFromGC(gc, ATechDate.FORMAT_DATE_ONLY);

        ArrayList<PumpValuesEntryExt> lst = new ArrayList<PumpValuesEntryExt>();
        String sql = "";

        try
        {
            // SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
            // String sDay = sdf.format(day.getTime());

            sql = "SELECT dv from " + //
                    "ggc.core.db.hibernate.pump.PumpDataExtendedH as dv " + //
                    "WHERE dv.dt_info >=  " + dt + "000000 AND dv.dt_info <= " + //
                    dt + "235959 ORDER BY dv.dt_info";

            Query q = this.db.getSession().createQuery(sql);

            Iterator<?> it = q.list().iterator();

            while (it.hasNext())
            {
                PumpDataExtendedH pdh = (PumpDataExtendedH) it.next();

                PumpValuesEntryExt dv = new PumpValuesEntryExt(pdh);
                lst.add(dv);
            }

        }
        catch (Exception ex)
        {
            log.debug("Sql: " + sql);
            log.error("getDailyPumpValuesExtended(). Exception: " + ex, ex);
        }

        return lst;

    }


    public List<PumpValuesEntry> getRangePumpBasalValues(GregorianCalendar from, GregorianCalendar to)
    {
        String filter = "dv.base_type=1";
        return convertPumpDataRawEntries(getRangePumpValuesRaw(from, to, filter));
    }


    public List<PumpValuesEntry> convertPumpDataRawEntries(List<PumpDataH> dataList)
    {
        List<PumpValuesEntry> outList = new ArrayList<PumpValuesEntry>();

        for (PumpDataH data : dataList)
        {
            outList.add(new PumpValuesEntry(data));
        }

        Collections.sort(outList);

        return outList;
    }


    /**
     * Get Daily Pump Values Extended
     *
     * @param from
     * @param to
     * @return
     */
    public List<PumpDataExtendedH> getRangePumpValuesExtendedRaw(GregorianCalendar from, GregorianCalendar to)
    {
        log.info("getRangePumpValuesExtendedRaw() - Run");

        long dt_from = ATechDate.getATDateTimeFromGC(from, ATechDate.FORMAT_DATE_ONLY);
        long dt_to = ATechDate.getATDateTimeFromGC(to, ATechDate.FORMAT_DATE_ONLY);

        List<PumpDataExtendedH> lst = new ArrayList<PumpDataExtendedH>();

        String sql = "";

        try
        {
            sql = "SELECT dv from " + "ggc.core.db.hibernate.pump.PumpDataExtendedH as dv " + "WHERE dv.dt_info >=  "
                    + dt_from + "000000 AND dv.dt_info <= " + dt_to + "235959 ORDER BY dv.dt_info";

            Query q = this.db.getSession().createQuery(sql);

            Iterator<?> it = q.list().iterator();

            while (it.hasNext())
            {
                PumpDataExtendedH pdh = (PumpDataExtendedH) it.next();
                lst.add(pdh);
            }

        }
        catch (Exception ex)
        {
            log.debug("Sql: " + sql);
            log.error("getRangePumpValuesExtendedRaw(). Exception: " + ex, ex);
        }

        return lst;

    }


    /**
     * Merge Daily Pump Data
     *
     * @param dV
     * @param lst_ext
     */
    public void mergeDailyPumpData(DeviceValuesDay dV, ArrayList<PumpValuesEntryExt> lst_ext)
    {
        for (int i = 0; i < lst_ext.size(); i++)
        {
            PumpValuesEntryExt pvex = lst_ext.get(i);

            // System.out.println(pvex.getDt_info());

            if (dV.isEntryAvailable(pvex.getDt_info()))
            {

                PumpValuesEntry pve = (PumpValuesEntry) dV.getEntry(pvex.getDt_info());
                pve.addAdditionalData(pvex);
            }
            else
            {

                PumpValuesEntry pve = new PumpValuesEntry();
                pve.setDateTimeObject(new ATechDate(ATechDate.FORMAT_DATE_AND_TIME_S, pvex.getDt_info()));
                pve.setBaseType(PumpBaseType.AdditionalData.getCode());

                pve.addAdditionalData(pvex);

                dV.addEntry(pve);
            }
        }
    }


    /**
     * Merge Daily Pump Data
     *
     * @param dvr
     * @param lst_ext
     */
    public void mergeRangePumpData(DeviceValuesRange dvr, List<PumpDataExtendedH> listExtended)
    {
        for (PumpDataExtendedH pdex : listExtended)
        {
            PumpValuesEntryExt pvex = new PumpValuesEntryExt(pdex);

            DeviceValuesDay dvd;

            if (dvr.isDayEntryAvailable(pvex.getDt_info()))
            {
                // System.out.println("DeviceValuesDay Found");
                dvd = dvr.getDayEntry(pvex.getDt_info());
            }
            else
            {
                // System.out.println("DeviceValuesDay Created");
                ATechDate atd = new ATechDate(ATechDate.FORMAT_DATE_AND_TIME_S, pvex.getDt_info());
                dvd = new DeviceValuesDay(m_da, atd.getGregorianCalendar());
                dvr.addEntry(dvd);
            }

            if (dvd.isEntryAvailable(pvex.getDt_info()))
            {
                // System.out.println("PumpValuesEntry Found");

                PumpValuesEntry pve = (PumpValuesEntry) dvd.getEntry(pvex.getDt_info());
                pve.addAdditionalData(pvex);
            }
            else
            {
                // System.out.println("PumpValuesEntry Created");
                PumpValuesEntry pve = new PumpValuesEntry();
                pve.setDateTimeObject(new ATechDate(ATechDate.FORMAT_DATE_AND_TIME_S, pvex.getDt_info()));
                pve.setBaseType(PumpBaseType.AdditionalData.getCode());

                pve.addAdditionalData(pvex);

                dvd.addEntry(pve);
            }
        }

    }


    /**
     * Get Profiles
     *
     * @return
     */
    public ArrayList<PumpProfile> getProfiles()
    {
        log.info("getProfiles() - Run");

        ArrayList<PumpProfile> lst = new ArrayList<PumpProfile>();
        String sql = "";

        try
        {
            sql = "SELECT dv from " + "ggc.core.db.hibernate.pump.PumpProfileH as dv ";

            Query q = this.db.getSession().createQuery(sql);

            Iterator<?> it = q.list().iterator();

            while (it.hasNext())
            {
                PumpProfileH pdh = (PumpProfileH) it.next();
                lst.add(new PumpProfile(pdh));
            }

        }
        catch (Exception ex)
        {
            log.debug("Sql: " + sql);
            log.error("getProfiles(). Exception: " + ex, ex);
        }

        return lst;
    }


    /**
     * Get Profiles
     *
     * @return
     */
    public PumpProfile getProfileForDayAndTime(GregorianCalendar gc)
    {
        // FIXME this doesn't work like it should, we need to get active
        // profile, not all profiles active on specific day
        log.info("getProfileForDayAndTime() - Run");

        String sql = "";

        long dt = ATechDate.getATDateTimeFromGC(gc, ATechDate.FORMAT_DATE_AND_TIME_S);

        try
        {
            sql = prepareSqlForProfiles(gc, gc);

            Query q = this.db.getSession().createQuery(sql);

            Iterator<?> it = q.list().iterator();

            if (it.hasNext())
            {
                PumpProfileH pdh = (PumpProfileH) it.next();
                return new PumpProfile(pdh);
            }

        }
        catch (Exception ex)
        {
            log.debug("Sql: " + sql);
            log.error("getProfileForDayAndTime(). Exception: " + ex, ex);
        }

        return null;

    }


    /**
     * Get Profiles
     *
     * @return
     */
    @Deprecated
    public List<PumpProfile> getProfilesForRangeOld(GregorianCalendar gcFrom, GregorianCalendar gcTill)
    {
        // FIXME this doesn't work like it should, we need to get active
        // profile, not all profiles active on specific day
        log.info("getProfilesForRange() - Run");

        String sql = "";

        List<PumpProfile> listProfiles = new ArrayList<PumpProfile>();

        long dtFrom = ATechDate.getATDateTimeFromGC(gcFrom, ATechDate.FORMAT_DATE_AND_TIME_S);
        long dtTill = ATechDate.getATDateTimeFromGC(gcTill, ATechDate.FORMAT_DATE_AND_TIME_S);

        System.out.println("From: " + dtFrom + ", till: " + dtTill);

        try
        {
            sql = "SELECT dv " + //
                    "from ggc.core.db.hibernate.pump.PumpProfileH as dv " + //
                    "where (dv.active_from > " + dtFrom + //
                    " and dv.active_from <> 0 )" + //
                    " or dv.active_till > " + dtFrom + //
                    // " and dv.active_till < " + dtTill +
                    // " and dv.active_till > " + dtFrom +
                    " or (dv.active_till = 0) " + //
                    " and dv.person_id=" + m_da.getCurrentUserId();

            // " and (dv.active_till < " + dtFrom +

            Query q = this.db.getSession().createQuery(sql);

            Iterator<?> it = q.list().iterator();

            while (it.hasNext())
            {
                PumpProfileH pdh = (PumpProfileH) it.next();
                listProfiles.add(new PumpProfile(pdh));
            }

        }
        catch (Exception ex)
        {
            log.debug("Sql: " + sql);
            log.error("getProfilesForRange(). Exception: " + ex, ex);
        }

        return listProfiles;

    }


    /**
     * Get Profiles
     *
     * @return
     */
    public List<PumpProfile> getProfilesForRange(GregorianCalendar gcFrom, GregorianCalendar gcTill)
    {
        log.info("getProfilesForRange() - Run");

        String sql = "";

        List<PumpProfile> listProfiles = new ArrayList<PumpProfile>();

        try
        {
            sql = prepareSqlForProfiles(gcFrom, gcTill);

            Query q = this.db.getSession().createQuery(sql);

            Iterator<?> it = q.list().iterator();

            while (it.hasNext())
            {
                PumpProfileH pdh = (PumpProfileH) it.next();
                listProfiles.add(new PumpProfile(pdh));
            }

        }
        catch (Exception ex)
        {
            log.debug("Sql: " + sql);
            log.error("getProfilesForRange(). Exception: " + ex, ex);
        }

        return listProfiles;

    }


    private String prepareSqlForProfiles(GregorianCalendar gcFrom, GregorianCalendar gcTill)
    {
        String sql = " SELECT dv " //
                + " from ggc.core.db.hibernate.pump.PumpProfileH as dv "
                + " where dv.person_id="
                + m_da.getCurrentUserId() //
                + " and dv.active_from <> 0 and dv.active_till <> 0" + //
                " and ( ";

        int days = m_da.getDaysInInterval(gcFrom, gcTill);

        StringBuilder sb = new StringBuilder();

        for (int i = 0; i < days; i++)
        {
            GregorianCalendar gc = (GregorianCalendar) gcFrom.clone();
            gc.add(Calendar.DAY_OF_YEAR, i);

            String date = DataAccessPump.getLeadingZero(gc.get(Calendar.YEAR), 2)
                    + DataAccessPump.getLeadingZero(gc.get(Calendar.MONTH) + 1, 2)
                    + DataAccessPump.getLeadingZero(gc.get(Calendar.DAY_OF_MONTH), 2);

            for (int j = 0; j < 24; j++)
            {
                String dt = date + DataAccessPump.getLeadingZero(j, 2) + "0000";
                sb.append("(dv.active_from <= " + dt + " and dv.active_till >=" + dt + ") or ");
            }
        }

        sql += sb.substring(0, sb.length() - 3);
        sql += " ) ";

        return sql;
    }


    /**
     * Get All Elements Count. This will count all elements in database that are from same device (source).
     *
     * @return
     */
    public int getAllElementsCount()
    {
        Integer in = null;
        int sum_all = 0;

        // PumpInterface pe = (PumpInterface)
        // dataAccess.getSelectedDeviceInstance();
        // GregorianCalendar gc = new GregorianCalendar();

        // gc.add(Calendar.MONTH, -1 * howManyMonthsOfDataStored());

        // long dt_from = ATechDate.getATDateTimeFromGC(gc,
        // ATechDate.FORMAT_DATE_ONLY) * 10000;

        Criteria criteria = this.getSession().createCriteria(PumpDataH.class);
        criteria.add(Restrictions.eq("person_id", (int) m_da.getCurrentUserId()));
        criteria.add(Restrictions.like("extended", "%" + m_da.getSourceDevice() + "%"));
        // criteria.add(Restrictions.ge("dt_info", dt_from));
        criteria.setProjection(Projections.rowCount());
        in = (Integer) criteria.list().get(0);
        sum_all = in.intValue();

        log.debug("  Pump Data : " + in.intValue());

        criteria = this.getSession().createCriteria(PumpDataExtendedH.class);
        criteria.add(Restrictions.eq("person_id", (int) m_da.getCurrentUserId()));
        criteria.add(Restrictions.like("extended", "%" + m_da.getSourceDevice() + "%"));
        // criteria.add(Restrictions.ge("dt_info", dt_from));
        // criteria.add(Restrictions.gt("id", minLogID));
        criteria.setProjection(Projections.rowCount());
        in = (Integer) criteria.list().get(0);
        sum_all += in.intValue();

        log.debug("  Pump Extended Data : " + in.intValue());

        criteria = this.getSession().createCriteria(PumpProfileH.class);
        criteria.add(Restrictions.eq("person_id", (int) m_da.getCurrentUserId()));
        criteria.add(Restrictions.like("extended", "%" + m_da.getSourceDevice() + "%"));
        // criteria.add(Restrictions.ge("active_from", dt_from));
        criteria.setProjection(Projections.rowCount());
        in = (Integer) criteria.list().get(0);
        sum_all += in.intValue();

        log.debug("  Pump Profiles : " + in.intValue());

        return sum_all;
    }


    /**
     * Get Pump Values
     *
     * @param pdr
     * @return
     */
    public Hashtable<String, DeviceValuesEntryInterface> getPumpValues(PumpDataReader pdr)
    {
        String sql = "";

        Hashtable<String, DeviceValuesEntryInterface> dt = new Hashtable<String, DeviceValuesEntryInterface>();

        try
        {
            int counter = 0;

            sql = "SELECT dv  " //
                    + "from ggc.core.db.hibernate.pump.PumpDataH as dv " //
                    + "WHERE dv.extended like '%" + m_da.getSourceDevice() + "%' " //
                    + "and dv.person_id=" + m_da.getCurrentUserId() //
                    + " ORDER BY dv.dt_info ";

            Query q = this.db.getSession().createQuery(sql);

            Iterator<?> it = q.list().iterator();

            pdr.writeStatus(-1);

            while (it.hasNext())
            {
                counter++;

                PumpValuesEntry pve = new PumpValuesEntry((PumpDataH) it.next());
                pve.prepareEntry_v2();

                dt.put(pve.getSpecialId(), pve);

                pdr.writeStatus(counter);
            }

            // sql = "SELECT dv from " +
            // "ggc.core.db.hibernate.pump.PumpDataExtendedH as dv " +
            // "WHERE dv.dt_info >=  "
            // + dt_from + "000000 and dv.person_id=" +
            // dataAccess.getCurrentUserId() + " ORDER BY dv.dt_info ";

            sql = "SELECT dv from " //
                    + "ggc.core.db.hibernate.pump.PumpDataExtendedH as dv "
                    + "WHERE dv.extended like '%"
                    + m_da.getSourceDevice() + "%' " + "and dv.person_id="
                    + m_da.getCurrentUserId()
                    + " ORDER BY dv.dt_info ";

            q = this.db.getSession().createQuery(sql);

            it = q.list().iterator();

            pdr.writeStatus(-1);
            // id = "PE_%s_%s";

            while (it.hasNext())
            {
                counter++;

                PumpValuesEntryExt pvex = new PumpValuesEntryExt((PumpDataExtendedH) it.next());
                dt.put(pvex.getSpecialId(), pvex);

                pdr.writeStatus(counter);
            }

            // sql =
            // "SELECT dv from ggc.core.db.hibernate.pump.PumpProfileH as dv " +
            // "WHERE dv.active_from >= "
            // + dt_from + " and dv.person_id=" + dataAccess.getCurrentUserId()
            // + " ORDER BY dv.active_from ";

            sql = "SELECT dv " //
                    + "from ggc.core.db.hibernate.pump.PumpProfileH as dv "
                    + "WHERE dv.extended like '%"
                    + m_da.getSourceDevice() + "%' " + " and dv.person_id="
                    + m_da.getCurrentUserId()
                    + " ORDER BY dv.active_from ";

            q = this.db.getSession().createQuery(sql);

            it = q.list().iterator();

            pdr.writeStatus(-1);
            // id = "PP_%s";

            while (it.hasNext())
            {
                counter++;

                PumpValuesEntryProfile pvep = new PumpValuesEntryProfile((PumpProfileH) it.next());

                dt.put(pvep.getSpecialId(), pvep);

                pdr.writeStatus(counter);
            }

            pdr.finishReading();
            // pdr.writeStatus(counter);

        }
        catch (Exception ex)
        {
            System.out.println("Exception on getPumpValues: " + ex);
            log.error("Exception on getPumpValues.\nsql:" + sql + "\nEx: " + ex, ex);
        }

        return dt;
    }


    public GraphValuesCollection getGraphData(GregorianCalendar gc_from, GregorianCalendar gc_to, int filter)
    {
        GraphValuesCollection gvc = new GraphValuesCollection(gc_from, gc_to);

        DeviceValuesRange dvr = getRangePumpValues(gc_from, gc_to);
        ArrayList<DeviceValuesEntry> list = dvr.getAllEntries();

        for (int i = 0; i < list.size(); i++)
        {
            DeviceValuesEntry dve = list.get(i);

            if (dve instanceof GraphValuesCapable)
            {
                gvc.addCollection(((GraphValuesCapable) dve).getGraphValues());
            }

        }

        // dvr.

        // TODO Auto-generated method stub
        return null;
    }

}
