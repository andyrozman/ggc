package ggc.plugin.db;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.GregorianCalendar;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;

import com.atech.db.hibernate.DatabaseObjectHibernate;
import com.atech.db.hibernate.HibernateDb;
import com.atech.utils.data.ATechDate;
import ggc.core.db.hibernate.cgms.CGMSDataH;

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

public class PluginDb
{

    private static Log log = LogFactory.getLog(PluginDb.class);
    protected HibernateDb db;

    private int m_errorCode = 0;
    private String m_errorDesc = "";
    private String m_addId = "";


    /**
     * Constructor
     * 
     * @param db
     */
    public PluginDb(HibernateDb db)
    {
        this.db = db;
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

        if (obj instanceof DatabaseObjectHibernate)
        {
            DatabaseObjectHibernate doh = (DatabaseObjectHibernate) obj;

            log.info(doh.getObjectName() + "::DbAdd");

            try
            {
                String id = doh.DbAdd(getSession()); // getSession());
                this.m_addId = id;
                // System.out.println("Add:" + doh);
                return true;
            }
            catch (SQLException ex)
            {
                setError(1, ex.getMessage(), doh.getObjectName());
                log.error("SQLException on add: " + ex, ex);
                Exception eee = ex.getNextException();

                if (eee != null)
                {
                    log.error("Nested Exception on add: " + eee.getMessage(), eee);
                }
                return false;
            }
            catch (Exception ex)
            {
                setError(1, ex.getMessage(), doh.getObjectName());
                log.error("Exception on add: " + ex, ex);
                return false;
            }

        }
        else
        {
            setError(-2, "Object is not DatabaseObjectHibernate instance", "GGCDb");

            log.error("Internal error on add: " + obj);
            return false;
        }

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

        log.info("addHibernate::" + obj.toString());

        try
        {
            Session sess = getSession();
            Transaction tx = sess.beginTransaction();

            Long val = (Long) sess.save(obj);
            tx.commit();

            return val.longValue();
        }
        catch (Exception ex)
        {
            log.error("Exception on addHibernate: " + ex, ex);
            return -1;
        }

    }


    /**
     * Edit entry to database
     * 
     * @param obj object we try to write (it must be DatabaseObjectHibernate)
     * @return true if successful
     */
    public boolean edit(Object obj)
    {

        if (obj instanceof DatabaseObjectHibernate)
        {
            DatabaseObjectHibernate doh = (DatabaseObjectHibernate) obj;

            log.info(doh.getObjectName() + "::DbEdit");

            try
            {
                doh.DbEdit(getSession());
                // System.out.println("Edit:" + doh);
                return true;
            }
            catch (SQLException ex)
            {
                setError(1, ex.getMessage(), doh.getObjectName());
                log.error("SQLException on edit: " + ex, ex);
                Exception eee = ex.getNextException();

                if (eee != null)
                {
                    log.error("Nested Exception on edit: " + eee.getMessage(), eee);
                }
                return false;
            }
            catch (Exception ex)
            {
                setError(1, ex.getMessage(), doh.getObjectName());
                log.error("Exception on edit: " + ex, ex);
                return false;
            }
        }
        else
        {
            setError(-2, "Object is not DatabaseObjectHibernate instance", "GGCDb");
            log.error("Internal error on edit: " + obj);
            return false;
        }

    }


    /**
     * Edit hibernate entry in database
     * 
     * @param obj object we try to write (it must be Hibernate object (raw))
     * @return true if successful
     */
    public boolean editHibernate(Object obj)
    {

        log.info("editHibernate::" + obj.toString());

        try
        {
            Session sess = getSession();
            Transaction tx = sess.beginTransaction();

            sess.update(obj);

            tx.commit();

            return true;
        }
        catch (Exception ex)
        {
            log.error("Exception on editHibernate: " + ex, ex);
            // ex.printStackTrace();
            return false;
        }

    }


    /**
     * Delete hibernate entry to database
     * 
     * @param obj object we try to write (it must be Hibernate object (raw))
     * @return true if successful
     */
    public boolean deleteHibernate(Object obj)
    {

        log.info("deleteHibernate::" + obj.toString());

        try
        {
            Session sess = getSession();
            Transaction tx = sess.beginTransaction();

            sess.delete(obj);

            tx.commit();

            return true;
        }
        catch (Exception ex)
        {
            log.error("Exception on deleteHibernate: " + ex, ex);
            // ex.printStackTrace();
            return false;
        }

    }


    /**
     * Get entry from database
     * 
     * @param obj object we try to write (it must be DatabaseObjectHibernate)
     * @return true if successful
     */
    public boolean get(Object obj)
    {

        if (obj instanceof DatabaseObjectHibernate)
        {
            DatabaseObjectHibernate doh = (DatabaseObjectHibernate) obj;

            log.info(doh.getObjectName() + "::DbGet");

            try
            {
                doh.DbGet(getSession());
                return true;
            }
            catch (SQLException ex)
            {
                setError(1, ex.getMessage(), doh.getObjectName());
                log.error("SQLException on get: " + ex, ex);
                Exception eee = ex.getNextException();

                if (eee != null)
                {
                    log.error("Nested Exception on get: " + eee.getMessage(), eee);
                }
                return false;
            }
            catch (Exception ex)
            {
                setError(1, ex.getMessage(), doh.getObjectName());
                log.error("Exception on get: " + ex, ex);
                return false;
            }

        }
        else
        {
            setError(-2, "Object is not DatabaseObjectHibernate instance", "GGCDb");
            log.error("Internal error on get: " + obj);
            return false;
        }

    }


    /**
     * Delete entry from database
     * 
     * @param obj object we try to write (it must be DatabaseObjectHibernate)
     * @return true if successful
     */
    public boolean delete(Object obj)
    {

        if (obj instanceof DatabaseObjectHibernate)
        {
            DatabaseObjectHibernate doh = (DatabaseObjectHibernate) obj;

            log.info(doh.getObjectName() + "::DbDelete");

            try
            {

                if (doh.DbHasChildren(getSession()))
                {
                    setError(-3, "Object has children object", doh.getObjectName());
                    log.error(doh.getObjectName() + " had Children objects");
                    return false;
                }

                doh.DbDelete(getSession());

                return true;
            }
            catch (SQLException ex)
            {
                setError(1, ex.getMessage(), doh.getObjectName());
                log.error("SQLException on delete: " + ex, ex);
                Exception eee = ex.getNextException();

                if (eee != null)
                {
                    log.error("Nested Exception on delete: " + eee.getMessage(), eee);
                }
                return false;
            }
            catch (Exception ex)
            {
                setError(1, ex.getMessage(), doh.getObjectName());
                log.error("Exception on delete: " + ex, ex);
                return false;
            }

        }
        else
        {
            setError(-2, "Object is not DatabaseObjectHibernate instance", "GGCDb");
            log.error("Internal error on delete: " + obj);
            return false;
        }

    }


    /**
     * Get Id from add action
     * @return
     */
    public String addGetId()
    {
        return this.m_addId;
    }


    /**
     * Get Error Code
     * 
     * @return error code
     */
    public int getErrorCode()
    {
        return this.m_errorCode;
    }


    /**
     * Get Error Description
     * 
     * @return description of error
     */
    public String getErrorDescription()
    {
        return this.m_errorDesc;
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
        this.m_errorCode = code;
        this.m_errorDesc = source + " : " + desc;
    }


    public List<CGMSDataH> getRangeCGMSValuesRaw(GregorianCalendar from, GregorianCalendar to, String filter)
    {
        log.info(String.format("getRangeCGMSValuesRaw(%s)", filter == null ? "" : filter));

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
            log.debug("Sql: " + sql);
            log.error("getRangeCGMSValuesRaw(). Exception: " + ex, ex);
        }

        log.debug("Found " + listCGMSData.size() + " entries.");

        return listCGMSData;
    }

}
