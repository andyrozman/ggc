package ggc.pump.db;

import java.util.*;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Expression;
import org.hibernate.criterion.Property;
import org.hibernate.criterion.Restrictions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.atech.db.hibernate.HibernateDb;
import com.atech.graphics.graphs.v2.data.GraphDefinitionDto;
import com.atech.graphics.graphs.v2.data.GraphTimeDataCollection;
import com.atech.utils.data.ATechDate;
import com.atech.utils.data.ATechDateType;
import com.atech.utils.data.CodeEnum;

import ggc.core.db.hibernate.cgms.CGMSDataH;
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
import ggc.plugin.report.data.cgms.CGMSDayData;
import ggc.pump.data.PumpDataReader;
import ggc.pump.data.PumpValuesEntry;
import ggc.pump.data.PumpValuesEntryExt;
import ggc.pump.data.PumpValuesEntryProfile;
import ggc.pump.data.defs.PumpAdditionalDataType;
import ggc.pump.data.defs.PumpBaseType;
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

    private static final Logger LOG = LoggerFactory.getLogger(GGCPumpDb.class);


    /**
     * Constructor
     *
     * @param db
     */
    public GGCPumpDb(HibernateDb db)
    {
        super(db, DataAccessPump.getInstance());

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
        LOG.info("getPumpDayStats()");

        // long dt = ATechDate.getATDateTimeFromGC(gc,
        // ATechDate.FORMAT_DATE_ONLY);

        DeviceValuesDay dV = new DeviceValuesDay(dataAccess);
        dV.setDateTimeGC(gc);

        String sql = "";

        try
        {
            List<?> dataList = getListOfDatabaseObjectsRange(PumpDataH.class, gc, gc, null, null);

            // sql = "SELECT dv from " + "ggc.core.db.hibernate.pump.PumpDataH
            // as dv " + "WHERE dv.dt_info >= " + dt
            // + "000000 AND dv.dt_info <= " + dt + "235959 ORDER BY
            // dv.dt_info";
            //
            // Query q = this.db.getSession().createQuery(sql);
            //
            // Iterator<?> it = q.list().iterator();

            for (Object dataEntry : dataList)
            {
                PumpDataH pdh = (PumpDataH) dataEntry;
                PumpValuesEntry dv = new PumpValuesEntry(pdh);

                dV.addEntry(dv);
            }

            ArrayList<PumpValuesEntryExt> lst_ext = getDailyPumpValuesExtended(gc);
            mergeDailyPumpData(dV, lst_ext);
        }
        catch (Exception ex)
        {
            // LOG.debug("Sql: " + sql);
            LOG.error("getDailyPumpValues(). Exception: " + ex, ex);
        }

        return dV;

        // return null;
    }


    /**
     * Get Pump Values Range (with Extended, without filtering)
     *
     * @param from
     * @param to
     * @return
     */
    public DeviceValuesRange getRangePumpValues(GregorianCalendar from, GregorianCalendar to)
    {
        LOG.info("getRangePumpValues()");

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
     * Get Pump Values Range (with Extended, with filtering)
     *
     * @param from
     * @param to
     * @return
     */
    // public DeviceValuesRange getRangePumpValues(GregorianCalendar from,
    // GregorianCalendar to, String filterBase,
    // String filterExtended)
    // {
    // LOG.info("getRangePumpValues()");
    //
    // DeviceValuesRange dvr = new
    // DeviceValuesRange(DataAccessPump.getInstance(), from, to);
    //
    // List<PumpDataH> listPumpData = getRangePumpValuesRaw(from, to,
    // filterBase);
    //
    // for (PumpDataH pdh : listPumpData)
    // {
    // PumpValuesEntry dv = new PumpValuesEntry(pdh);
    // dvr.addEntry(dv);
    // }
    //
    // List<PumpDataExtendedH> lst_ext = getRangePumpValuesExtendedRaw(from, to,
    // filterExtended);
    // mergeRangePumpData(dvr, lst_ext);
    //
    // return dvr;
    // }

    /**
     * Get Pump Values Range (with Extended, with filtering)
     *
     * @param from
     * @param to
     * @return
     */
    public DeviceValuesRange getRangePumpValues(GregorianCalendar from, GregorianCalendar to,
            List<PumpBaseType> filterBase, List<PumpAdditionalDataType> filterExtended)
    {
        LOG.info("getRangePumpValues()");

        DeviceValuesRange dvr = new DeviceValuesRange(DataAccessPump.getInstance(), from, to);

        List<PumpDataH> listPumpData = getRangePumpValuesRaw(from, to, filterBase);

        for (PumpDataH pdh : listPumpData)
        {
            PumpValuesEntry dv = new PumpValuesEntry(pdh);
            dvr.addEntry(dv);
        }

        List<PumpDataExtendedH> lst_ext = getRangePumpValuesExtendedRaw(from, to, filterExtended);
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
    public List<CGMSDayData> getRangeCGMSValues(GregorianCalendar from, GregorianCalendar to)
    {
        LOG.info("getRangeCGMSValues()");

        List<CGMSDayData> outData = new ArrayList<CGMSDayData>();

        List<CGMSDataH> listCGMSData = getRangeCGMSValuesRaw(from, to, Arrays.asList(Restrictions.eq("baseType", 1)));

        for (CGMSDataH pdh : listCGMSData)
        {
            CGMSDayData dv = new CGMSDayData(pdh);
            outData.add(dv);
        }

        return outData;
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

    // public List<PumpDataH> getRangePumpValuesRaw(GregorianCalendar from,
    // GregorianCalendar to, String filter)
    // {
    // LOG.info(String.format("getRangePumpValuesRaw(%s)", filter == null ? "" :
    // filter));
    //
    // long dt_from = ATechDate.getATDateTimeFromGC(from,
    // ATechDate.FORMAT_DATE_ONLY);
    // long dt_to = ATechDate.getATDateTimeFromGC(to,
    // ATechDate.FORMAT_DATE_ONLY);
    //
    // String sql = "";
    //
    // List<PumpDataH> listPumpData = new ArrayList<PumpDataH>();
    //
    // try
    // {
    // sql = "SELECT dv from " + //
    // "ggc.core.db.hibernate.pump.PumpDataH as dv " + //
    // "WHERE dv.dt_info >= " + dt_from + "000000 AND dv.dt_info <= " + //
    // dt_to + "235959 ";
    //
    // if (filter != null)
    // {
    // sql += " AND " + filter;
    // }
    //
    // sql += " ORDER BY dv.dt_info";
    //
    // Query q = this.db.getSession().createQuery(sql);
    //
    // Iterator<?> it = q.list().iterator();
    //
    // while (it.hasNext())
    // {
    // PumpDataH pdh = (PumpDataH) it.next();
    // listPumpData.add(pdh);
    // }
    //
    // }
    // catch (Exception ex)
    // {
    // LOG.debug("Sql: " + sql);
    // LOG.error("getDayStats(). Exception: " + ex, ex);
    // }
    //
    // LOG.debug("Found " + listPumpData.size() + " entries.");
    //
    // return listPumpData;
    // }


    public List<PumpDataH> getRangePumpValuesRaw(GregorianCalendar from, GregorianCalendar to,
            List<PumpBaseType> filterBase)
    {
        LOG.info(String.format("getRangePumpValuesRaw(%s)", CollectionUtils.isEmpty(filterBase) ? ""
                : dataAccess.createStringRepresentationOfCollection(filterBase, ", ")));

        List<PumpDataH> listPumpData = new ArrayList<PumpDataH>();

        try
        {

            List<?> dataList = getListOfDatabaseObjectsRange(PumpDataH.class, from, to, "baseType", filterBase);

            for (Object dataEntry : dataList)
            {
                PumpDataH pdh = (PumpDataH) dataEntry;
                listPumpData.add(pdh);
            }

        }
        catch (Exception ex)
        {
            // LOG.debug("Sql: " + sql);
            LOG.error("getDayStats(). Exception: " + ex, ex);
        }

        LOG.debug("Found " + listPumpData.size() + " entries.");

        return listPumpData;
    }


    private List getListOfDatabaseObjectsRange(Class clazz, GregorianCalendar from, GregorianCalendar to,
            String propertyToFilter, List<? extends CodeEnum> filter) throws Exception
    {

        Criteria criteria = null;
        try
        {
            criteria = this.db.getSession().createCriteria(clazz);
            criteria.add(Restrictions.between("dtInfo", getDate(from, true), getDate(to, false)));
            criteria.add(Restrictions.eq("personId", (int) dataAccess.getCurrentUserId()));

            if ((CollectionUtils.isNotEmpty(filter)) && (StringUtils.isNotBlank(propertyToFilter)))
            {
                criteria.add(createOrCriterionsForList(propertyToFilter, filter));
            }

            criteria.addOrder(Property.forName("dtInfo").asc());

            return criteria.list();
        }
        catch (Exception ex)
        {
            LOG.error("Criteria: " + criteria);
            LOG.error("Exception when getting data: {}", ex.getMessage(), ex);
            throw ex;
        }
    }


    private Criterion createOrCriterionsForList(String propertyName, List<? extends CodeEnum> list)
    {
        Criterion criterion = null;

        for (CodeEnum codeEnum : list)
        {
            if (criterion == null)
                criterion = Expression.eq(propertyName, codeEnum.getCode());
            else
                criterion = Restrictions.or(criterion, Expression.eq(propertyName, codeEnum.getCode()));
        }

        return criterion;
    }


    private long getDate(GregorianCalendar gregorianCalendar, boolean isBegin)
    {
        long dt = ATechDate.getATDateTimeFromGC(gregorianCalendar, ATechDateType.DateOnly) * 1000000;

        if (!isBegin)
        {
            dt += 235959;
        }

        return dt;
    }


    /**
     * Get Daily Pump Values Extended
     *
     * @param gc calendar instance
     * @return list of PumpValuesEntryExt
     */
    public ArrayList<PumpValuesEntryExt> getDailyPumpValuesExtended(GregorianCalendar gc)
    {
        LOG.info("getDailyPumpValuesExtended() - Run");

        // long dt = ATechDate.getATDateTimeFromGC(gc,
        // ATechDate.FORMAT_DATE_ONLY);

        ArrayList<PumpValuesEntryExt> lst = new ArrayList<PumpValuesEntryExt>();
        // String sql = "";

        try
        {

            // Iterator<?> it =
            // getListOfDatabaseObjectsRange(PumpDataExtendedH.class, gc, gc,
            // null, null).iterator();

            List<?> dataList = getListOfDatabaseObjectsRange(PumpDataExtendedH.class, gc, gc, null, null);

            // SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
            // String sDay = sdf.format(day.getTime());

            // sql = "SELECT dv from " + //
            // "ggc.core.db.hibernate.pump.PumpDataExtendedH as dv " + //
            // "WHERE dv.dt_info >= " + dt + "000000 AND dv.dt_info <= " + //
            // dt + "235959 ORDER BY dv.dt_info";
            //
            // Query q = this.db.getSession().createQuery(sql);
            //
            // Iterator<?> it = q.list().iterator();

            // for()

            for (Object dataEntry : dataList)
            {
                PumpDataExtendedH pdh = (PumpDataExtendedH) dataEntry;

                PumpValuesEntryExt dv = new PumpValuesEntryExt(pdh);
                lst.add(dv);
            }

        }
        catch (Exception ex)
        {
            // LOG.debug("Sql: " + sql);
            LOG.error("getDailyPumpValuesExtended(). Exception: " + ex, ex);
        }

        return lst;

    }


    public List<PumpValuesEntry> getRangePumpBasalValues(GregorianCalendar from, GregorianCalendar to)
    {
        // String filter = "dv.base_type=1";
        return convertPumpDataRawEntries(getRangePumpValuesRaw(from, to, Arrays.asList(PumpBaseType.Basal)));
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


    public List<PumpDataExtendedH> getRangePumpValuesExtendedRaw(GregorianCalendar from, GregorianCalendar to)
    {
        return getRangePumpValuesExtendedRaw(from, to, null);
    }


    // /**
    // * Get Daily Pump Values Extended
    // *
    // * @param from
    // * @param to
    // * @return
    // */
    // public List<PumpDataExtendedH>
    // getRangePumpValuesExtendedRaw(GregorianCalendar from, GregorianCalendar
    // to,
    // String filter)
    // {
    // LOG.info("getRangePumpValuesExtendedRaw() - Run");
    //
    // long dt_from = ATechDate.getATDateTimeFromGC(from,
    // ATechDate.FORMAT_DATE_ONLY);
    // long dt_to = ATechDate.getATDateTimeFromGC(to,
    // ATechDate.FORMAT_DATE_ONLY);
    //
    // List<PumpDataExtendedH> lst = new ArrayList<PumpDataExtendedH>();
    //
    // String sql = "";
    //
    // try
    // {
    // sql = "SELECT dv from " + "ggc.core.db.hibernate.pump.PumpDataExtendedH
    // as dv " + //
    // "WHERE dv.dt_info >= " + dt_from + "000000 AND dv.dt_info <= " + dt_to +
    // "235959";
    //
    // if (filter != null)
    // {
    // sql += " AND " + filter;
    // }
    //
    // sql += " ORDER BY dv.dt_info";
    //
    // Query q = this.db.getSession().createQuery(sql);
    //
    // Iterator<?> it = q.list().iterator();
    //
    // while (it.hasNext())
    // {
    // PumpDataExtendedH pdh = (PumpDataExtendedH) it.next();
    // lst.add(pdh);
    // }
    //
    // }
    // catch (Exception ex)
    // {
    // LOG.debug("Sql: " + sql);
    // LOG.error("getRangePumpValuesExtendedRaw(). Exception: " + ex, ex);
    // }
    //
    // return lst;
    //
    // }

    /**
     * Get Daily Pump Values Extended
     *
     * @param from
     * @param to
     * @return
     */
    public List<PumpDataExtendedH> getRangePumpValuesExtendedRaw(GregorianCalendar from, GregorianCalendar to,
            List<PumpAdditionalDataType> filter)
    {
        LOG.info("getRangePumpValuesExtendedRaw() - Run");

        List<PumpDataExtendedH> lst = new ArrayList<PumpDataExtendedH>();

        try
        {
            List<?> dataList = getListOfDatabaseObjectsRange(PumpDataExtendedH.class, from, to, "type", filter);

            // sql = "SELECT dv from " +
            // "ggc.core.db.hibernate.pump.PumpDataExtendedH as dv " + //
            // "WHERE dv.dt_info >= " + dt_from + "000000 AND dv.dt_info <= " +
            // dt_to + "235959";
            //
            // if (filter != null)
            // {
            // sql += " AND " + filter;
            // }
            //
            // sql += " ORDER BY dv.dt_info";
            //
            // Query q = this.db.getSession().createQuery(sql);
            //
            // Iterator<?> it = q.list().iterator();

            for (Object dataEntry : dataList)
            {
                PumpDataExtendedH pdh = (PumpDataExtendedH) dataEntry;
                lst.add(pdh);
            }

        }
        catch (Exception ex)
        {
            // LOG.debug("Sql: " + sql);
            LOG.error("getRangePumpValuesExtendedRaw(). Exception: " + ex, ex);
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

            // System.out.println(pvex.getDtInfo());

            if (dV.isEntryAvailable(pvex.getDtInfo()))
            {
                PumpValuesEntry pve = (PumpValuesEntry) dV.getEntry(pvex.getDtInfo());
                pve.addAdditionalData(pvex);
            }
            else
            {
                PumpValuesEntry pve = new PumpValuesEntry();
                pve.setDateTimeObject(new ATechDate(ATechDateType.DateAndTimeSec, pvex.getDtInfo()));
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
     * @param listExtended
     */
    public void mergeRangePumpData(DeviceValuesRange dvr, List<PumpDataExtendedH> listExtended)
    {
        for (PumpDataExtendedH pdex : listExtended)
        {
            PumpValuesEntryExt pvex = new PumpValuesEntryExt(pdex);

            DeviceValuesDay dvd;

            if (dvr.isDayEntryAvailable(pvex.getDtInfo()))
            {
                // System.out.println("DeviceValuesDay Found");
                dvd = dvr.getDayEntry(pvex.getDtInfo());
            }
            else
            {
                // System.out.println("DeviceValuesDay Created");
                ATechDate atd = new ATechDate(ATechDateType.DateAndTimeSec, pvex.getDtInfo());
                dvd = new DeviceValuesDay(dataAccess, atd.getGregorianCalendar());
                dvr.addEntry(dvd);
            }

            if (dvd.isEntryAvailable(pvex.getDtInfo()))
            {
                // System.out.println("PumpValuesEntry Found");

                PumpValuesEntry pve = (PumpValuesEntry) dvd.getEntry(pvex.getDtInfo());
                pve.addAdditionalData(pvex);
            }
            else
            {
                // System.out.println("PumpValuesEntry Created");
                PumpValuesEntry pve = new PumpValuesEntry();
                pve.setDateTimeObject(new ATechDate(ATechDateType.DateAndTimeSec, pvex.getDtInfo()));
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
        LOG.info("getProfiles() - Run");

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
            LOG.debug("Sql: " + sql);
            LOG.error("getProfiles(). Exception: " + ex, ex);
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
        LOG.info("getProfileForDayAndTime() - Run");

        String sql = "";

        long dt = ATechDate.getATDateTimeFromGC(gc, ATechDateType.DateAndTimeSec);

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
            LOG.debug("Sql: " + sql);
            LOG.error("getProfileForDayAndTime(). Exception: " + ex, ex);
        }

        return null;

    }

    // getHibernateData


    /**
     * Get Profiles
     *
     * @return
     */
    public List<PumpProfile> getProfilesForRange(GregorianCalendar gcFrom, GregorianCalendar gcTill)
    {
        LOG.info("getProfilesForRange() - Run");

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
            LOG.debug("Sql: " + sql);
            LOG.error("getProfilesForRange(). Exception: " + ex, ex);
        }

        return listProfiles;

    }


    private String prepareSqlForProfiles(GregorianCalendar gcFrom, GregorianCalendar gcTill)
    {
        String sql = " SELECT dv " //
                + " from ggc.core.db.hibernate.pump.PumpProfileH as dv " + " where dv.personId="
                + dataAccess.getCurrentUserId() //
                + " and dv.activeFrom <> 0 and dv.activeTill <> 0" + //
                " and ( ";

        int days = dataAccess.getDaysInInterval(gcFrom, gcTill);

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
                sb.append("(dv.activeFrom <= " + dt + " and dv.activeTill >=" + dt + ") or ");
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

        // Criteria criteria = this.getSession().createCriteria(PumpDataH.class);

        // addPersonAndSourceDevice(criteria);

        // criteria.add(Restrictions.eq("person_id", (int) dataAccess.getCurrentUserId()));
        // criteria.add(Restrictions.like("extended", "%" + dataAccess.getSourceDevice() + "%"));
        // criteria.setProjection(Projections.rowCount());
        // in = (Integer) criteria.list().get(0);

        in = getAllElementsCount(PumpDataH.class, null);
        sum_all = in;

        LOG.debug("  Pump Data : " + in);

        // criteria = this.getSession().createCriteria(PumpDataExtendedH.class);
        // addPersonAndSourceDevice(criteria);
        // //criteria.add(Restrictions.eq("person_id", (int) dataAccess.getCurrentUserId()));
        // //criteria.add(Restrictions.like("extended", "%" + dataAccess.getSourceDevice() + "%"));
        // criteria.setProjection(Projections.rowCount());
        // in = (Integer) criteria.list().get(0);

        in = getAllElementsCount(PumpDataExtendedH.class, null);
        sum_all += in;

        LOG.debug("  Pump Extended Data : " + in);

        // criteria = this.getSession().createCriteria(PumpProfileH.class);
        // addPersonAndSourceDevice(criteria);
        //// criteria.add(Restrictions.eq("person_id", (int) dataAccess.getCurrentUserId()));
        //// criteria.add(Restrictions.like("extended", "%" + dataAccess.getSourceDevice() + "%"));
        // criteria.setProjection(Projections.rowCount());
        // in = (Integer) criteria.list().get(0);

        in = getAllElementsCount(PumpProfileH.class, null);
        sum_all += in;

        LOG.debug("  Pump Profiles : " + in);

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

        // FIXME
        String sql = "";

        Hashtable<String, DeviceValuesEntryInterface> dt = new Hashtable<String, DeviceValuesEntryInterface>();

        try
        {
            int counter = 0;

            sql = "SELECT dv  " //
                    + "from ggc.core.db.hibernate.pump.PumpDataH as dv " //
                    + "WHERE dv.extended like '%" + dataAccess.getSourceDevice() + "%' " //
                    + "and dv.personId=" + dataAccess.getCurrentUserId() //
                    + " ORDER BY dv.dtInfo ";

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
            // "WHERE dv.dt_info >= "
            // + dt_from + "000000 and dv.person_id=" +
            // dataAccess.getCurrentUserId() + " ORDER BY dv.dt_info ";

            sql = "SELECT dv from " //
                    + "ggc.core.db.hibernate.pump.PumpDataExtendedH as dv " + "WHERE dv.extended like '%"
                    + dataAccess.getSourceDevice() + "%' " + "and dv.personId=" + dataAccess.getCurrentUserId()
                    + " ORDER BY dv.dtInfo ";

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

            sql = "SELECT dv " + //
                    " from ggc.core.db.hibernate.pump.PumpProfileH as dv " + //
                    " WHERE dv.extended like '%" + dataAccess.getSourceDevice() + "%' " + //
                    " and dv.personId=" + dataAccess.getCurrentUserId() + //
                    " ORDER BY dv.activeFrom ";

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
            LOG.error("Exception on getPumpValues.\nsql:" + sql + "\nEx: " + ex, ex);
        }

        return dt;
    }


    // TODO this might be obsolote, use getGraphTimeData (not defined yet)
    public GraphValuesCollection getGraphData(GregorianCalendar gc_from, GregorianCalendar gc_to, int filter)
    {
        GraphValuesCollection gvc = new GraphValuesCollection(gc_from, gc_to);

        DeviceValuesRange dvr = getRangePumpValues(gc_from, gc_to);
        List<DeviceValuesEntry> list = dvr.getAllEntries();

        for (int i = 0; i < list.size(); i++)
        {
            DeviceValuesEntry dve = list.get(i);

            if (dve instanceof GraphValuesCapable)
            {
                gvc.addCollection(((GraphValuesCapable) dve).getGraphValues());
            }

        }

        return gvc;
    }


    public GraphTimeDataCollection getGraphTimeData(GregorianCalendar gcFrom, GregorianCalendar gcTill,
            GraphDefinitionDto definitionDto)
    {
        return null;
    }
}
