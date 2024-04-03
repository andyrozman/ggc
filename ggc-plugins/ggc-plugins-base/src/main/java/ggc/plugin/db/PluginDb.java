package ggc.plugin.db;

import java.security.InvalidParameterException;
import java.util.*;

import org.apache.commons.collections.CollectionUtils;
import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.atech.db.hibernate.DatabaseObjectHibernate;
import com.atech.db.hibernate.HibernateDb;
import com.atech.db.hibernate.HibernateObject;
import com.atech.graphics.graphs.v2.data.GraphDbDataRetriever;
import com.atech.utils.data.ATechDate;
import com.atech.utils.data.ATechDateType;

import ggc.core.db.hibernate.cgms.CGMSDataH;
import ggc.plugin.util.DataAccessPlugInBase;

/**
 *  Application:   GGC - GNU Gluco Control
 *  Plug-in:       GGC PlugIn Base (base class for all plugins)
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
 *  Filename:     PluginDb  
 *  Description:  This is master class for using Db instance within plug-in. In most cases, we 
 *                would want data to be handled by outside authority (GGC), but in some cases
 *                we wouldn't want that.
 * 
 *  Author: Andy {andy@atech-software.com}
 */

public abstract class PluginDb implements GraphDbDataRetriever
{

    private static final Logger LOG = LoggerFactory.getLogger(PluginDb.class);

    protected HibernateDb db;
    protected DataAccessPlugInBase dataAccess = null;


    /**
     * Constructor
     * 
     * @param db
     */
    public PluginDb(HibernateDb db, DataAccessPlugInBase dataAccess)
    {
        this.db = db;
        this.dataAccess = dataAccess;
    }


    /**
     * Get Session
     * 
     * @return session instance
     */
    public Session getSession()
    {
        return this.db.getSession();
    }


    // ---
    // --- BASIC METHODS (Hibernate and DataLayer processing)
    // ---

    /**
     * Add entry to database
     * 
     * @param obj object we try to write (it must be DatabaseObjectHibernate)
     * @return true if successful
     */
    public boolean add(Object obj)
    {
        return this.db.add(obj);
    }


    /**
     * Commit entry to database
     * 
     * @param obj object we try to write (it must be DatabaseObjectHibernate)
     * @return true if successful
     */
    public boolean commit(Object obj)
    {
        if (obj instanceof DatabaseObjectHibernate)
        {
            if (((DatabaseObjectHibernate) obj).getObjectUniqueId().equals("0"))
                return this.add(obj);
            else
                return this.edit(obj);

        }
        else
            return false;
    }


    /**
     * Add hibernate entry to database
     * 
     * @param obj object we try to write (it must be Hibernate object (raw))
     * @return true if successful
     */
    public long addHibernate(Object obj)
    {
        return this.db.addHibernate(obj);
    }


    /**
     * Edit entry to database
     * 
     * @param obj object we try to write (it must be DatabaseObjectHibernate)
     * @return true if successful
     */
    public boolean edit(Object obj)
    {
        return this.db.edit(obj);
    }


    /**
     * Edit hibernate entry in database
     * 
     * @param obj object we try to write (it must be Hibernate object (raw))
     * @return true if successful
     */
    public boolean editHibernate(Object obj)
    {
        return this.db.editHibernate(obj);
    }


    /**
     * Delete hibernate entry to database
     * 
     * @param obj object we try to write (it must be Hibernate object (raw))
     * @return true if successful
     */
    public boolean deleteHibernate(Object obj)
    {
        return this.db.deleteHibernate(obj);
    }


    /**
     * Get entry from database
     * 
     * @param obj object we try to write (it must be DatabaseObjectHibernate)
     * @return true if successful
     */
    public boolean get(Object obj)
    {
        return this.db.get(obj);
    }


    /**
     * Delete entry from database
     * 
     * @param obj object we try to write (it must be DatabaseObjectHibernate)
     * @return true if successful
     */
    public boolean delete(Object obj)
    {
        return this.db.delete(obj);
    }


    /**
     * Get Id from add action
     * @return
     */
    public String addGetId()
    {
        return this.db.addGetId();
    }


    /**
     * Get Error Code
     * 
     * @return error code
     */
    public int getErrorCode()
    {
        return this.db.getErrorCode();
    }


    /**
     * Get Error Description
     * 
     * @return description of error
     */
    public String getErrorDescription()
    {
        return this.db.getErrorDescription();
    }


    /**
     * Set Error
     * 
     * @param code error code
     * @param desc error description
     * @param source source of error
     */
    public void setError(int code, String desc, String source)
    {
        this.db.setError(code, desc, source);
    }


    public Criterion getRangeCriteria(GregorianCalendar from, GregorianCalendar to, ATechDateType dateType,
            String fromParameter, String toParameter)
    {
        long dtFrom = ATechDate.getATDateTimeFromGC(from, ATechDateType.DateOnly);
        long dtTill = ATechDate.getATDateTimeFromGC(to, ATechDateType.DateOnly);

        if (dateType == ATechDateType.DateAndTimeMin)
        {
            dtFrom *= 10000;
            dtTill *= 10000;

            dtTill += 2359L;
        }
        else if (dateType == ATechDateType.DateAndTimeSec)
        {
            dtFrom *= 1000000;
            dtTill *= 1000000;

            dtTill += 235959L;
        }
        else if (dateType != ATechDateType.DateOnly)
        {
            throw new InvalidParameterException();
        }

        return Restrictions.and(Restrictions.ge("dtInfo", dtFrom), Restrictions.le("dtInfo", dtTill));
    }


    public Criterion getPersonCriterion()
    {
        return Restrictions.eq("personId", (int) dataAccess.getCurrentUserId());
    }


    public Criterion getSourceDeviceCriterion()
    {
        return Restrictions.like("extended", "%" + dataAccess.getSourceDevice() + "%");
    }


    public void addPersonAndSourceDevice(Criteria criteria)
    {
        criteria.add(getPersonCriterion());
        criteria.add(getSourceDeviceCriterion());
    }


    public List<CGMSDataH> getRangeCGMSValuesRaw(GregorianCalendar from, GregorianCalendar to,
            List<? extends Criterion> criterions)
    {
        LOG.info(String.format("getRangeCGMSValuesRaw(%s)", CollectionUtils.isEmpty(criterions) ? "" : criterions));

        List<CGMSDataH> listCGMSData = new ArrayList<CGMSDataH>();

        List<Criterion> criteria = new ArrayList<Criterion>();

        criteria.add(getRangeCriteria(from, to, ATechDateType.DateOnly, "dtInfo", "dtInfo"));

        if (CollectionUtils.isNotEmpty(criterions))
        {
            criteria.addAll(criterions);
        }

        try
        {
            listCGMSData = getHibernateData(CGMSDataH.class, criteria, Arrays.asList(Order.asc("dtInfo")));
        }
        catch (Exception ex)
        {
            LOG.error("getRangeCGMSValuesRaw(). Exception: " + ex, ex);
        }

        LOG.debug("Found " + listCGMSData.size() + " entries.");

        return listCGMSData;
    }


    public List<CGMSDataH> getRangeCGMSValuesRawOld(GregorianCalendar from, GregorianCalendar to, String filter)
    {
        LOG.info(String.format("getRangeCGMSValuesRaw(%s)", filter == null ? "" : filter));

        long dt_from = ATechDate.getATDateTimeFromGC(from, ATechDate.FORMAT_DATE_ONLY);
        long dt_to = ATechDate.getATDateTimeFromGC(to, ATechDate.FORMAT_DATE_ONLY);

        String sql = "";

        List<CGMSDataH> listCGMSData = new ArrayList<CGMSDataH>();

        try
        {
            sql = " SELECT dv from ggc.core.db.hibernate.cgms.CGMSDataH as dv " + //
                    " WHERE dv.dt_info >=  " + dt_from + " AND dv.dt_info <= " + dt_to;

            if (filter != null)
            {
                sql += " AND " + filter;
            }

            sql += " ORDER BY dv.dt_info";

            Query q = this.db.getSession().createQuery(sql);

            Iterator<?> it = q.list().iterator();

            while (it.hasNext())
            {
                CGMSDataH pdh = (CGMSDataH) it.next();
                listCGMSData.add(pdh);
            }

        }
        catch (Exception ex)
        {
            LOG.debug("Sql: " + sql);
            LOG.error("getRangeCGMSValuesRaw(). Exception: " + ex, ex);
        }

        LOG.debug("Found " + listCGMSData.size() + " entries.");

        return listCGMSData;
    }


    public <E extends HibernateObject> List<E> getHibernateData(Class<E> clazz, //
            List<? extends Criterion> criterionList, //
            int sessionNumber)
    {
        return getHibernateData(clazz, criterionList, null, sessionNumber);
    }


    public <E extends HibernateObject> List<E> getHibernateData(Class<E> clazz, //
            List<? extends Criterion> criterionList)
    {
        return getHibernateData(clazz, criterionList, null, 1);
    }


    public <E extends HibernateObject> List<E> getHibernateData(Class<E> clazz, //
            List<? extends Criterion> criterionList, //
            List<Order> orderList)
    {
        return getHibernateData(clazz, criterionList, orderList, 1);
    }


    public <E extends HibernateObject> List<E> getHibernateData(Class<E> clazz, //
            List<? extends Criterion> criterionList, //
            List<Order> orderList, //
            int sessionNumber)
    {
        return this.db.getHibernateData(clazz, criterionList, orderList, sessionNumber);
    }


    /**
     * Get All Elements Count
     *
     * @return
     */
    public int getAllElementsCount(Class<? extends HibernateObject> hibernateClazz, //
            List<? extends Criterion> criterionList)
    {
        try
        {
            int sum_all = 0;

            Criteria criteria = this.getSession().createCriteria(hibernateClazz);
            addPersonAndSourceDevice(criteria);
            // criteria.add(getPersonCriterion());
            // criteria.add(Restrictions.like("extended", "%" + dataAccess.getSourceDevice() +
            // "%"));

            if (CollectionUtils.isNotEmpty(criterionList))
            {
                for (Criterion criterion : criterionList)
                {
                    criteria.add(criterion);
                }
            }

            criteria.setProjection(Projections.rowCount());
            Integer in = (Integer) criteria.list().get(0);
            return in.intValue();
        }
        catch (Exception ex)
        {
            LOG.error("getAllElementsCount: " + ex, ex);
            ex.printStackTrace();
            return 0;
        }
    }

}
