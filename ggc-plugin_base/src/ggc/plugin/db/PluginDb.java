package ggc.plugin.db;

import java.sql.SQLException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.Session;
import org.hibernate.Transaction;

import com.atech.db.hibernate.DatabaseObjectHibernate;
import com.atech.db.hibernate.HibernateDb;

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
 *  Filename:  ###---###  
 *  Description:
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
    
    
    public PluginDb(HibernateDb db)
    {
        this.db = db;
    }
    

    
    public Session getSession()
    {
        return this.db.getSession();
    }

    
    
    
    
    // ---
    // --- BASIC METHODS (Hibernate and DataLayer processing)
    // ---

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

    
    public boolean commit(Object obj)
    {
        if (obj instanceof DatabaseObjectHibernate)
        {
            if (((DatabaseObjectHibernate)obj).getObjectUniqueId().equals("0"))
            {
                return this.add(obj);
            }
            else
            {
                return this.edit(obj);
            }
            
        }
        else
            return false;
    }
    
    
    
    // this method is used for direct use with hibernate objects (unlike use
    // with our
    // datalayer classes)
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

    public boolean edit(Object obj)
    {

        if (obj instanceof DatabaseObjectHibernate)
        {
            DatabaseObjectHibernate doh = (DatabaseObjectHibernate) obj;

            log.info(doh.getObjectName() + "::DbEdit");

            try
            {
                doh.DbEdit(getSession());
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

    
    
    // this method is used for direct use with hibernate objects (unlike use
    // with our
    // datalayer classes)
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

    public String addGetId()
    {
        return this.m_addId;
    }

    public int getErrorCode()
    {
        return this.m_errorCode;
    }

    public String getErrorDescription()
    {
        return this.m_errorDesc;
    }

    public void setError(int code, String desc, String source)
    {
        this.m_errorCode = code;
        this.m_errorDesc = source + " : " + desc;
    }
    
    
    

}