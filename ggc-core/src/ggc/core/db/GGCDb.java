package ggc.core.db;

import java.text.SimpleDateFormat;
import java.util.*;

import org.hibernate.Criteria;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.hibernate.tool.hbm2ddl.SchemaExport;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.atech.db.hibernate.HibernateConfiguration;
import com.atech.db.hibernate.HibernateDb;
import com.atech.utils.ATDataAccessAbstract;
import com.atech.utils.data.ATechDate;

import ggc.core.data.*;
import ggc.core.data.cfg.ConfigurationManager;
import ggc.core.db.datalayer.Settings;
import ggc.core.db.dto.StocktakingDTO;
import ggc.core.db.hibernate.*;
import ggc.core.util.DataAccess;

/**
 *  Application:   GGC - GNU Gluco Control
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
 *  Filename:     GGCDb  
 *  Description:  Class for working with database (Hibernate)
 * 
 *  Author: andyrozman {andy@atech-software.com}  
 */

public class GGCDb extends HibernateDb // implements DbCheckInterface
// HibernateDb
{

    // public static final int DB_CONFIG_LOADED = 1;
    // public static final int DB_INITIALIZED = 2;
    // public static final int DB_STARTED = 3;

    private boolean debug = true;
    // x private boolean db_debug = false;

    private static final Logger LOG = LoggerFactory.getLogger(GGCDb.class);
    // private Session m_session = null;
    // private Session m_session_2 = null;
    // private SessionFactory sessions = null;
    // private int m_errorCode = 0;
    // private String m_errorDesc = "";
    // private String m_addId = "";

    protected GGCDbConfig hib_config = null;
    protected Configuration m_cfg = null;

    private DataAccess m_da;

    private int m_loadStatus = 0;

    /*
     * public ArrayList<MeterCompanyH> meter_companies = null; public
     * Hashtable<String,ArrayList<MeterH>> meters_by_cmp = null; public
     * Hashtable<String,MeterH> meters_full = null;
     */


    // ---
    // --- DB Settings
    // ---
    /*
     * protected int db_num = 0; protected String db_hib_dialect = null;
     * protected String db_driver_class = null; protected String db_conn_name =
     * null; protected String db_conn_url = null; protected String
     * db_conn_username = null; protected String db_conn_password = null;
     */

    /**
     * Constructor 
     * 
     * @param da
     */
    public GGCDb(DataAccess da)
    {
        /* m_cfg = */createConfiguration();
        m_da = da;

        // System.out.println("GGCDb");
        // System.out.println("dataAccess: " + dataAccess);
        // System.out.println("dataAccess.getSettings(): " +
        // dataAccess.getSettings());

        m_loadStatus = DB_CONFIG_LOADED;
        // debugConfig();
    }


    /**
     * Constructor
     */
    public GGCDb()
    {
        /* m_cfg = */
        createConfiguration();
        m_loadStatus = DB_CONFIG_LOADED;
        // debugConfig();
    }


    /**
     * Get Configuration
     */
    @Override
    public Configuration getConfiguration()
    {
        return this.m_cfg;
    }


    /*
     * private void debugConfig() { /
     * System.out.println("Debug Configuration:");
     * //this.m_cfg.g //this.m_cfg.
     * Iterator it = this.m_cfg.getClassMappings();
     * //m_cfg.get
     * while (it.hasNext()) { org.hibernate.mapping.RootClass rc =
     * (org.hibernate.mapping.RootClass)it.next();
     * //System.out.println(it.next()); // exploreRootClass(rc); }
     */
    // }

    /**
     * Init Db
     */
    @Override
    public void initDb()
    {
        openHibernateSimple();
    }


    /**
     * Is Db Started
     */
    @Override
    public boolean isDbStarted()
    {
        return this.m_loadStatus == DB_STARTED;
    }


    /** 
     * Close Db
     */
    @Override
    public void closeDb()
    {
        this.hib_config.closeDb();
        m_loadStatus = DB_CONFIG_LOADED;
    }


    /** 
     * Get Hibernate Configuration
     */
    @Override
    public GGCDbConfig getHibernateConfiguration()
    {
        return this.hib_config;
    }


    /** 
     * Open Hibernate Simple
     */
    @Override
    public void openHibernateSimple()
    {
        this.hib_config.createSessionFactory();
        /*
         * logInfo("openHibernateSimple", "Start");
         * // getStartStatus();
         * sessions = m_cfg.buildSessionFactory();
         * // getStartStatus();
         * m_session = sessions.openSession();
         * m_session_2 = sessions.openSession();
         * m_loadStatus = DB_INITIALIZED;
         * logInfo("openHibernateSimple", "End");
         */
        m_loadStatus = DB_INITIALIZED;
    }


    /**
     * Get Load Status
     */
    @Override
    public int getLoadStatus()
    {
        return m_loadStatus;
    }


    /** 
     * Display Error
     */
    @Override
    public void displayError(String source, Exception ex)
    {

        System.out.println("Exception [" + source + "]: " + ex);
        LOG.error("Exception [" + source + "]: " + ex, ex);

        if (debug)
        {
            System.out.println("Exception [" + source + "]: " + ex.getMessage());
            ex.printStackTrace();
        }

    }


    protected void logException(String source, Exception ex)
    {
        LOG.error(source + "::Exception: " + ex.getMessage(), ex);
    }


    protected void logDebug(String source, String action)
    {
        LOG.debug(source + " - " + action);
    }


    protected void logInfo(String source, String action)
    {
        LOG.info(source + " - " + action);
    }


    protected void logInfo(String source)
    {
        LOG.info(source + " - Process");
    }


    /**
     * Get Session
     */
    @Override
    public Session getSession()
    {
        return getSession(1);
    }


    /**
     * Get Session
     * 
     * @param session_nr 
     * @return 
     */
    public Session getSession(int session_nr)
    {
        return this.hib_config.getSession(session_nr);
        /*
         * if (session_nr == 1)
         * {
         * m_session.clear();
         * return m_session;
         * }
         * else
         * {
         * m_session_2.clear();
         * return m_session_2;
         * }
         */
    }


    /**
     * Create Database
     */
    @Override
    public void createDatabase()
    {
        logInfo("createDatabase", "Process");
        new SchemaExport(this.getHibernateConfiguration().getConfiguration()).create(true, true);
    }

    // *************************************************************
    // **** SETTINGS ****
    // *************************************************************

    // ---
    // --- BASIC METHODS (Hibernate and DataLayer processing)
    // ---
    /*
     * public boolean add(Object obj)
     * {
     * if (obj instanceof DatabaseObjectHibernate)
     * {
     * DatabaseObjectHibernate doh = (DatabaseObjectHibernate) obj;
     * LOG.info(doh.getObjectName() + "::DbAdd");
     * try
     * {
     * String id = doh.DbAdd(getSession()); // getSession());
     * this.m_addId = id;
     * return true;
     * }
     * catch (SQLException ex)
     * {
     * setError(1, ex.getMessage(), doh.getObjectName());
     * LOG.error("SQLException on add: " + ex, ex);
     * Exception eee = ex.getNextException();
     * if (eee != null)
     * {
     * LOG.error("Nested Exception on add: " + eee.getMessage(), eee);
     * }
     * return false;
     * }
     * catch (Exception ex)
     * {
     * setError(1, ex.getMessage(), doh.getObjectName());
     * LOG.error("Exception on add: " + ex, ex);
     * return false;
     * }
     * }
     * else
     * {
     * setError(-2, "Object is not DatabaseObjectHibernate instance", "GGCDb");
     * LOG.error("Internal error on add: " + obj);
     * return false;
     * }
     * }
     * public long addHibernate(Object obj)
     * {
     * return addHibernate(obj,1);
     * }
     * // this method is used for direct use with hibernate objects (unlike use
     * // with our
     * // datalayer classes)
     * public long addHibernate(Object obj, int session_id)
     * {
     * LOG.info("addHibernate::" + obj.toString());
     * try
     * {
     * Session sess = getSession(session_id);
     * Transaction tx = sess.beginTransaction();
     * Long val = (Long) sess.save(obj);
     * tx.commit();
     * return val.longValue();
     * }
     * catch (Exception ex)
     * {
     * LOG.error("Exception on addHibernate: " + ex, ex);
     * return -1;
     * }
     * }
     * public boolean edit(Object obj)
     * {
     * if (obj instanceof DatabaseObjectHibernate)
     * {
     * DatabaseObjectHibernate doh = (DatabaseObjectHibernate) obj;
     * LOG.info(doh.getObjectName() + "::DbEdit");
     * try
     * {
     * doh.DbEdit(getSession());
     * return true;
     * }
     * catch (SQLException ex)
     * {
     * setError(1, ex.getMessage(), doh.getObjectName());
     * LOG.error("SQLException on edit: " + ex, ex);
     * Exception eee = ex.getNextException();
     * if (eee != null)
     * {
     * LOG.error("Nested Exception on edit: " + eee.getMessage(), eee);
     * }
     * return false;
     * }
     * catch (Exception ex)
     * {
     * setError(1, ex.getMessage(), doh.getObjectName());
     * LOG.error("Exception on edit: " + ex, ex);
     * return false;
     * }
     * }
     * else
     * {
     * setError(-2, "Object is not DatabaseObjectHibernate instance", "GGCDb");
     * LOG.error("Internal error on edit: " + obj);
     * return false;
     * }
     * }
     * public boolean editHibernate(Object obj)
     * {
     * return editHibernate(obj,1);
     * }
     * // this method is used for direct use with hibernate objects (unlike use
     * // with our
     * // datalayer classes)
     * public boolean editHibernate(Object obj, int session_id)
     * {
     * LOG.info("editHibernate::" + obj.toString());
     * try
     * {
     * Session sess = getSession(session_id);
     * Transaction tx = sess.beginTransaction();
     * sess.update(obj);
     * tx.commit();
     * return true;
     * }
     * catch (Exception ex)
     * {
     * LOG.error("Exception on editHibernate: " + ex, ex);
     * // ex.printStackTrace();
     * return false;
     * }
     * }
     * public boolean deleteHibernate(Object obj)
     * {
     * LOG.info("deleteHibernate::" + obj.toString());
     * try
     * {
     * Session sess = getSession();
     * Transaction tx = sess.beginTransaction();
     * sess.delete(obj);
     * tx.commit();
     * return true;
     * }
     * catch (Exception ex)
     * {
     * LOG.error("Exception on deleteHibernate: " + ex, ex);
     * // ex.printStackTrace();
     * return false;
     * }
     * }
     * public boolean get(Object obj)
     * {
     * if (obj instanceof DatabaseObjectHibernate)
     * {
     * DatabaseObjectHibernate doh = (DatabaseObjectHibernate) obj;
     * LOG.info(doh.getObjectName() + "::DbGet");
     * try
     * {
     * doh.DbGet(getSession());
     * return true;
     * }
     * catch (SQLException ex)
     * {
     * setError(1, ex.getMessage(), doh.getObjectName());
     * LOG.error("SQLException on get: " + ex, ex);
     * Exception eee = ex.getNextException();
     * if (eee != null)
     * {
     * LOG.error("Nested Exception on get: " + eee.getMessage(), eee);
     * }
     * return false;
     * }
     * catch (Exception ex)
     * {
     * setError(1, ex.getMessage(), doh.getObjectName());
     * LOG.error("Exception on get: " + ex, ex);
     * return false;
     * }
     * }
     * else
     * {
     * setError(-2, "Object is not DatabaseObjectHibernate instance", "GGCDb");
     * LOG.error("Internal error on get: " + obj);
     * return false;
     * }
     * }
     * public boolean delete(Object obj)
     * {
     * if (obj instanceof DatabaseObjectHibernate)
     * {
     * DatabaseObjectHibernate doh = (DatabaseObjectHibernate) obj;
     * LOG.info(doh.getObjectName() + "::DbDelete");
     * try
     * {
     * if (doh.DbHasChildren(getSession()))
     * {
     * setError(-3, "Object has children object", doh.getObjectName());
     * LOG.error(doh.getObjectName() + " had Children objects");
     * return false;
     * }
     * doh.DbDelete(getSession());
     * return true;
     * }
     * catch (SQLException ex)
     * {
     * setError(1, ex.getMessage(), doh.getObjectName());
     * LOG.error("SQLException on delete: " + ex, ex);
     * Exception eee = ex.getNextException();
     * if (eee != null)
     * {
     * LOG.error("Nested Exception on delete: " + eee.getMessage(), eee);
     * }
     * return false;
     * }
     * catch (Exception ex)
     * {
     * setError(1, ex.getMessage(), doh.getObjectName());
     * LOG.error("Exception on delete: " + ex, ex);
     * return false;
     * }
     * }
     * else
     * {
     * setError(-2, "Object is not DatabaseObjectHibernate instance", "GGCDb");
     * LOG.error("Internal error on delete: " + obj);
     * return false;
     * }
     * }
     * public String addGetId()
     * {
     * return this.m_addId;
     * }
     * public int getErrorCode()
     * {
     * return this.m_errorCode;
     * }
     * public String getErrorDescription()
     * {
     * return this.m_errorDesc;
     * }
     * public void setError(int code, String desc, String source)
     * {
     * this.m_errorCode = code;
     * this.m_errorDesc = source + " : " + desc;
     * }
     */


    // *************************************************************
    // **** SETTINGS ****
    // *************************************************************

    /** 
     * Create Configuration
     */
    @Override
    public HibernateConfiguration createConfiguration()
    {
        logInfo("createConfiguration()");
        this.hib_config = new GGCDbConfig(true);
        this.hib_config.getConfiguration();

        // cache_db = new GGCDbCache(this);

        return this.hib_config;
    }

    /**
     * Get Db Cache
     * 
     * @return
     */
    /*
     * public GGCDbCache getDbCache()
     * {
     * return cache_db;
     * }
     */


    // *************************************************************
    // **** DATABASE INIT METHODS ****
    // *************************************************************

    /** 
     * Load Static Data
     */
    @Override
    public void loadStaticData()
    {
        m_loadStatus = DB_STARTED;
    }


    // *************************************************************
    // **** SETTINGS ****
    // *************************************************************

    /**
     * We load all config data (including schemes)
     */
    public void loadConfigData()
    {
        logInfo("loadConfigData()");

        try
        {
            Session sess = getSession();

            // load schemes
            loadColorSchemes(sess);

            // sets settings
            loadConfigDataEntries();

        }
        catch (Exception ex)
        {
            // LOG.error("Exception on loadConfigData: " + ex.getMessage(), ex);
            logException("loadConfigData()", ex);

        }
    }


    private void loadConfigDataEntries()
    {

        logInfo("loadConfigDataEntries()");

        try
        {
            Session sess = getSession(2);

            Hashtable<String, Settings> table = new Hashtable<String, Settings>();

            Query q = sess.createQuery(
                "select cfg from ggc.core.db.hibernate.SettingsH as cfg where cfg.person_id=" + m_da.current_user_id);

            Iterator it = q.iterate();

            while (it.hasNext())
            {
                SettingsH eh = (SettingsH) it.next();
                table.put(eh.getKey(), new Settings(eh));
            }

            m_da.getConfigurationManager().checkConfiguration(table, this);
        }
        catch (Exception ex)
        {
            // LOG.error("Exception on loadConfigDataEntries: " +
            // ex.getMessage(), ex);
            logException("loadConfigDataEntries()", ex);
        }

    }


    @SuppressWarnings("unchecked")
    public Hashtable<String, String> getExtendedRationEntries()
    {

        logInfo("loadConfigDataEntries()");

        try
        {
            Session sess = getSession(1);

            Hashtable<String, String> table = new Hashtable<String, String>();

            Query q = sess.createQuery(
                "SELECT cfg FROM ggc.core.db.hibernate.SettingsH as cfg WHERE cfg.key LIKE 'EXTENDED_RATIO%' AND cfg.person_id="
                        + m_da.current_user_id);

            Iterator<SettingsH> it = q.iterate();

            while (it.hasNext())
            {
                SettingsH eh = it.next();
                table.put(eh.getKey(), eh.getValue());
            }

            // dataAccess.getConfigurationManager().checkConfiguration(table,
            // this);

            return table;
        }
        catch (Exception ex)
        {
            // LOG.error("Exception on loadConfigDataEntries: " +
            // ex.getMessage(), ex);
            logException("loadConfigDataEntries()", ex);
            return null;
        }

    }


    /*
     * @SuppressWarnings("unchecked")
     * public Hashtable<String,String> getExtendedRatioData()
     * {
     * if (m_loadStatus == DB_CONFIG_LOADED)
     * return null;
     * LOG.info("getExtendedRatioData() - Process");
     * Hashtable<String,String> ht = new Hashtable<String,String>();
     * String sql = "";
     * try
     * {
     * sql =
     * "SELECT st FROM ggc.core.db.hibernate.SettingsH as st WHERE st.key LIKE 'EXTENDED_RATIO%' AND st.person_id="
     * + dataAccess.current_user_id ;
     * sql += " ORDER BY st.key";
     * Query q = getSession().createQuery(sql);
     * Iterator<SettingsH> it = q.list().iterator();
     * while (it.hasNext())
     * {
     * SettingsH st = it.next();
     * ht.put(st.getKey(), st.getValue());
     * }
     * }
     * catch (Exception ex)
     * {
     * LOG.error("getExtendedRatioData()::Exception: " + ex.getMessage(), ex);
     * }
     * return ht;
     * }
     */

    public boolean saveExtendedRatioEntries(ExtendedRatioCollection coll)
    {
        // Collections.sort(coll);

        // delete current settings

        String sql = "DELETE FROM ggc.core.db.hibernate.SettingsH as st WHERE st.key LIKE 'EXTENDED_RATIO%' AND st.person_id="
                + m_da.current_user_id;

        Query q = getSession().createQuery(sql);
        q.executeUpdate();

        // add new settings

        // --- sort

        if (coll.size() == 0)
            return true;

        ConfigurationManager cfg_mgr = this.m_da.getConfigurationManager();

        cfg_mgr.addNewValue("EXTENDED_RATIO_COUNT", "" + coll.size(), 1, this, false);

        for (int i = 0; i < coll.size(); i++)
        {
            cfg_mgr.addNewValue("EXTENDED_RATIO_" + (i + 1), coll.get(i).getSaveData(), 1, this, false);
        }

        return true;
    }


    /**
     * Save Config Data (without schemes)
     * We save just config, schemes save must be called separately
     */
    public void saveConfigData()
    {
        logInfo("saveConfigDataEntries()");
        m_da.getConfigurationManager().saveConfig();
    }


    @SuppressWarnings("unchecked")
    private void loadColorSchemes(Session sess)
    {
        try
        {

            logInfo("loadColorSchemes()");

            Hashtable<String, ColorSchemeH> table = new Hashtable<String, ColorSchemeH>();

            Query q = sess.createQuery("select pst from ggc.core.db.hibernate.ColorSchemeH as pst");

            Iterator it = q.iterate();

            while (it.hasNext())
            {
                ColorSchemeH eh = (ColorSchemeH) it.next();
                table.put(eh.getName(), eh);
            }

            // System.out.println("dataAccess: " + dataAccess);
            // System.out.println("dataAccess.getSettings(): " +
            // dataAccess.getSettings());

            m_da.getSettings().setColorSchemes(table, false);
        }
        catch (Exception ex)
        {
            // LOG.error("Exception on loadColorSchemes: " + ex.getMessage(),
            // ex);
            logException("loadColorSchemes()", ex);
        }

    }

    /*
     * private void saveColorSchemes(Session sess) {
     * DataAccess.notImplemented("GGCDb::saveColorSchemes()"); }
     */

    // *************************************************************
    // **** NUTRITION DATA ****
    // *************************************************************

    // *************************************************************
    // **** GGC Main Data ****
    // **** Comment: Were implemnted in DataBaseHandler ****
    // *************************************************************

    HbA1cValues hba1c_object = null;


    /**
     * Get HbA1c
     * 
     * @param day
     * @param force
     * @return
     */
    public HbA1cValues getHbA1c(GregorianCalendar day, boolean force)
    {
        if (this.hba1c_object == null)
        {
            hba1c_object = loadHbA1c(day);
        }
        else
        {
            if (force)
            {
                hba1c_object = loadHbA1c(day);
            }
        }

        return this.hba1c_object;

    }


    /**
     * Load HbA1c
     * 
     * @param day
     * @return
     */
    @SuppressWarnings("unchecked")
    public HbA1cValues loadHbA1c(GregorianCalendar day)
    {
        // System.out.println("Hibernate: getHbA1c() B1 Stat:" + m_loadStatus);

        if (m_loadStatus == DB_CONFIG_LOADED)
            return null;

        logInfo("getHbA1c()");

        HbA1cValues hbVal = new HbA1cValues();

        try
        {
            GregorianCalendar gc1 = (GregorianCalendar) day.clone();
            gc1.add(Calendar.MONTH, -3);

            SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
            String eDay = sdf.format(day.getTime()) + "2359";
            String sDay = sdf.format(gc1.getTime()) + "0000";

            Query q = getSession().createQuery(
                "SELECT dv from " + "ggc.core.db.hibernate.DayValueH as dv " + "WHERE dv.bg > 0 AND dv.dt_info >=  "
                        + sDay + " AND dv.dt_info <= " + eDay + " ORDER BY dv.dt_info");

            Iterator it = q.list().iterator();

            while (it.hasNext())
            {
                DayValueH dv = (DayValueH) it.next();
                hbVal.addDayValueRow(new DailyValuesRow(dv));
            }

            hbVal.processDayValues();

            logDebug("getHbA1c()", "Readings: " + hbVal.getDayCount() + " " + hbVal.getReadings());

            // System.out.println("Avg BG: " + hbVal.getAvgBGForMethod3());

        }
        catch (Exception ex)
        {
            logException("getHbA1c()", ex);
        }

        return hbVal;
    }


    /**
     * Get Day Stats
     * 
     * @param day
     * @return
     */
    @SuppressWarnings("unchecked")
    public DailyValues getDayStats(GregorianCalendar day)
    {

        if (m_loadStatus == DB_CONFIG_LOADED)
            return null;

        logInfo("getDayStats()");

        DailyValues dV = new DailyValues();
        // dV.setDate(dataAccess.getDateTimeFromDateObject(day.getTime()) /
        // 10000);

        dV.setDate(ATechDate.getATDateTimeFromGC(day, ATechDate.FORMAT_DATE_ONLY));

        try
        {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
            String sDay = sdf.format(day.getTime());

            Query q = getSession().createQuery("SELECT dv from " + "ggc.core.db.hibernate.DayValueH as dv "
                    + "WHERE dv.dt_info >=  " + sDay + "0000 AND dv.dt_info <= " + sDay + "2359 ORDER BY dv.dt_info");

            Iterator it = q.list().iterator();

            while (it.hasNext())
            {
                DayValueH dv = (DayValueH) it.next();

                DailyValuesRow dVR = new DailyValuesRow(dv);
                dV.addRow(dVR);
            }

        }
        catch (Exception ex)
        {
            logException("getDayStats()", ex);
        }

        return dV;
    }


    /**
     * Get Day Stats Range (WeeklyValues)
     * 
     * @param start
     * @param end
     * @return
     */
    @SuppressWarnings("unchecked")
    public WeeklyValues getDayStatsRange(GregorianCalendar start, GregorianCalendar end)
    {

        if (m_loadStatus == DB_CONFIG_LOADED)
            return null;

        logInfo("getDayStatsRange()");

        WeeklyValues wv = new WeeklyValues();

        try
        {
            String sDay = "" + ATechDate.getATDateTimeFromGC(start, ATechDate.FORMAT_DATE_ONLY);
            String eDay = "" + ATechDate.getATDateTimeFromGC(end, ATechDate.FORMAT_DATE_ONLY);
            // String sDay =
            // dataAccess.getDateTimeStringFromGregorianCalendar(start,
            // 1);
            // String eDay =
            // dataAccess.getDateTimeStringFromGregorianCalendar(end,
            // 1);

            logDebug("getDayStatsRange()", sDay + " - " + eDay);

            Query q = getSession().createQuery("SELECT dv from " + "ggc.core.db.hibernate.DayValueH as dv "
                    + "WHERE dv.dt_info >=  " + sDay + "0000 AND dv.dt_info <= " + eDay + "2359 ORDER BY dv.dt_info");

            Iterator it = q.list().iterator();

            while (it.hasNext())
            {
                DayValueH dv = (DayValueH) it.next();

                DailyValuesRow dVR = new DailyValuesRow(dv);
                wv.addDayValueRow(dVR);
            }

        }
        catch (Exception ex)
        {
            logException("getDayStatsRange()", ex);
        }

        return wv;
    }


    /**
     * Get Day Values Range (ArrayList<DailyValuesRow>)
     * 
     * @param start
     * @param end
     * @return
     */
    @SuppressWarnings("unchecked")
    public ArrayList<DailyValuesRow> getDayValuesRange(GregorianCalendar start, GregorianCalendar end)
    {

        logInfo("getDayValuesRange()");

        ArrayList<DailyValuesRow> lst = new ArrayList<DailyValuesRow>();

        // WeeklyValues wv = new WeeklyValues();

        try
        {
            String sDay = "" + ATechDate.getATDateTimeFromGC(start, ATechDate.FORMAT_DATE_ONLY);
            String eDay = "" + ATechDate.getATDateTimeFromGC(end, ATechDate.FORMAT_DATE_ONLY);

            // String sDay =
            // dataAccess.getDateTimeStringFromGregorianCalendar(start,
            // 1);
            // String eDay =
            // dataAccess.getDateTimeStringFromGregorianCalendar(end,
            // 1);

            logDebug("getDayStatsRange()", sDay + " - " + eDay);

            Query q = getSession().createQuery("SELECT dv from " + "ggc.core.db.hibernate.DayValueH as dv "
                    + "WHERE dv.dt_info >=  " + sDay + "0000 AND dv.dt_info <= " + eDay + "2359 ORDER BY dv.dt_info");

            // System.out.println("SELECT dv from " +
            // "ggc.core.db.hibernate.DayValueH as dv " + "WHERE dv.dt_info <= "
            // + sDay + "0000 AND dv.dt_info <= " + eDay + "2359 ORDER BY
            // dv.dt_info");

            Iterator it = q.list().iterator();

            while (it.hasNext())
            {
                DayValueH dv = (DayValueH) it.next();

                DailyValuesRow dVR = new DailyValuesRow(dv);
                lst.add(dVR);
            }

        }
        catch (Exception ex)
        {
            logException("getDayStatsRange()", ex);
        }

        return lst;
    }


    /**
     * Get Monthly Values
     * 
     * @param year
     * @param month
     * @return
     */
    @SuppressWarnings("unchecked")
    public MonthlyValues getMonthlyValues(int year, int month)
    {

        if (m_loadStatus == DB_CONFIG_LOADED)
            return null;

        logInfo("getMonthlyValues()");

        MonthlyValues mv = new MonthlyValues(year, month);

        try
        {
            String days = year + "" + ATDataAccessAbstract.getLeadingZero(month, 2);

            Query q = getSession()
                    .createQuery("SELECT dv from " + "ggc.core.db.hibernate.DayValueH as dv " + "WHERE dv.dt_info >=  "
                            + days + "010000 AND dv.dt_info <= " + days + "312359 ORDER BY dv.dt_info");

            Iterator it = q.list().iterator();

            while (it.hasNext())
            {
                DayValueH dv = (DayValueH) it.next();

                DailyValuesRow dVR = new DailyValuesRow(dv);
                mv.addDayValueRow(dVR);
            }

        }
        catch (Exception ex)
        {
            logException("getMonthlyValues()", ex);
        }

        return mv;

    }


    /**
     * Get DayValuesData
     * 
     * @param from
     * @param till
     * @return
     */
    @SuppressWarnings("unchecked")
    public DayValuesData getDayValuesData(long from, long till)
    {

        if (m_loadStatus == DB_CONFIG_LOADED)
            return null;

        logInfo("getDayValuesData()");

        DayValuesData dvd = new DayValuesData(from, till);

        try
        {
            Query q = getSession().createQuery("SELECT dv from " + "ggc.core.db.hibernate.DayValueH as dv "
                    + "WHERE dv.dt_info >=  " + from + "0000 AND dv.dt_info <= " + till + "2359 ORDER BY dv.dt_info");

            Iterator it = q.list().iterator();

            while (it.hasNext())
            {
                DayValueH dv = (DayValueH) it.next();

                DailyValuesRow dVR = new DailyValuesRow(dv);
                dvd.addDayValueRow(dVR);
            }

        }
        catch (Exception ex)
        {
            logException("getDayValuesData()", ex);
        }

        return dvd;

    }


    /**
     * Save Day Stats
     * 
     * @param dV
     */
    public void saveDayStats(DailyValues dV)
    {

        if (dV.hasChanged())
        {
            logInfo("saveDayStats()");

            logDebug("saveDayStats()", "Data has changed");

            Session sess = getSession();

            try
            {

                // deleted entries

                if (dV.hasDeletedItems())
                {
                    logDebug("saveDayStats()", "Removing deleted entry");

                    Transaction tx = sess.beginTransaction();

                    ArrayList<DayValueH> list = dV.getDeletedItems();
                    for (int i = 0; i < list.size(); i++)
                    {
                        DayValueH d = list.get(i);
                        sess.delete(d);
                        tx.commit();
                    }

                }

                // see if any of elements were changed or added

                for (int i = 0; i < dV.getRowCount(); i++)
                {
                    DailyValuesRow dwr = dV.getRow(i);

                    if (dwr.isNew())
                    {
                        logDebug("saveDayStats()", "Adding new entry");

                        Transaction tx = sess.beginTransaction();

                        DayValueH dvh = dwr.getHibernateObject();
                        Long l = (Long) sess.save(dvh);

                        dvh.setId(l.longValue());
                        tx.commit();
                    }
                    else if (dwr.hasChanged())
                    {
                        Transaction tx = sess.beginTransaction();

                        logDebug("saveDayStats()", "Changing entry");

                        DayValueH dvh = dwr.getHibernateObject();
                        sess.update(dvh);

                        tx.commit();
                    }

                } // for

            }
            catch (Exception ex)
            {
                logException("saveDayStats()", ex);
            }

        } // hasChanged
        else
        {
            logDebug("saveDayStats()", "No entries changed");
        }

    }


    /**
     * DateTime Exists
     * 
     * @param datetime
     * @return
     */
    public boolean dateTimeExists(long datetime)
    {
        if (m_loadStatus == DB_CONFIG_LOADED)
            return false;

        // if (db_debug)
        // System.out.println("Hibernate: dateTimeExists()");

        try
        {
            Query q = getSession().createQuery(
                "SELECT dv from " + "ggc.core.db.hibernate.DayValueH as dv " + "WHERE dv.dt_info = " + datetime);

            return q.list().size() == 1;
        }
        catch (Exception ex)
        {
            logException("dateTimeExists()", ex);
            return false;
        }

    }


    // *************************************************************
    // **** S t o c k s ****
    // *************************************************************

    public List<StockSubTypeH> getStockTypes()
    {
        List<StockSubTypeH> list = new ArrayList<StockSubTypeH>();

        if (isDbNotRunning())
            return list;

        try
        {
            Query q = getSession().createQuery("SELECT dv from ggc.core.db.hibernate.StockSubTypeH as dv");

            Iterator it = q.list().iterator();

            while (it.hasNext())
            {
                list.add((StockSubTypeH) it.next());
            }

        }
        catch (Exception ex)
        {
            LOG.error("getStockTypes()::Exception: " + ex.getMessage(), ex);
        }

        return list;
    }


    public StocktakingDTO getLatestStocktakingDTO()
    {
        StocktakingDTO dto = new StocktakingDTO();

        StocktakingH sth = getLastStocktaking();

        dto.setStocktakingH(sth);

        LOG.debug("Last stocktaking : " + sth);

        // FIXME

        if (sth != null)
        {

        }

        return dto;

    }


    private StocktakingH getLastStocktaking()
    {
        try
        {
            Integer in = null;
            int sum_all = 0;

            Criteria criteria = this.getSession().createCriteria(StocktakingH.class);
            criteria.add(Restrictions.eq("personId", (int) m_da.getCurrentUserId()));
            criteria.setProjection(Projections.max("datetime"));

            Object o = criteria.uniqueResult();

            System.out.println("Object: " + o);

            if (o != null)
            {
                try
                {
                    Query q = getSession().createQuery("SELECT dv from ggc.core.db.hibernate.StocktakingH as dv "
                            + "where datetime=" + m_da.getLongValue(o));

                    Iterator it = q.list().iterator();

                    if (it.hasNext())
                    {
                        return (StocktakingH) it.next();
                    }

                }
                catch (Exception ex)
                {
                    LOG.error("getLastStocktaking()::Exception: " + ex.getMessage(), ex);
                }
            }

            return null;

            // if (criteria.list() == null)
            // {
            // System.out.println("Not found.");
            // }
            // else
            // {
            // System.out.println("Not found." + criteria.list().size());
            // Integer maxAge = (Integer)criteria.uniqueResult();
            // System.out.println("MaxAge: " + maxAge);
            // }
            //
            // return null;

            // Integer maxAge = (Integer)criteria.uniqueResult();

            //
            //
            // Query q = getSession().createQuery(
            // "SELECT stt from ggc.core.db.hibernate.StocktakingH as stt " +
            // "WHERE datetime = " +
            // "( SELECT max(datetime) FROM ggc.core.db.hibernate.StocktakingH
            // )");
            //
            // Iterator it = q.list().iterator();
            //
            // if (it.hasNext())
            // {
            // return (StocktakingH)it.next();
            // }
            // else
            // {
            // return null;
            // }

            //
            //
            //
            // Criteria criteria =
            // this.getSession().createCriteria(StocktakingH.class);
            // criteria.add(Restrictions.eq("personId", (int)
            // dataAccess.getCurrentUserId()));
            // criteria.setProjection(Projections.max(""));
            //
            //
            //
            // criteria.add(Restrictions.or(
            // Restrictions.or(Restrictions.gt("bg", 0),
            // Restrictions.like("extended", "%URINE%")),
            // Restrictions.gt("ch", 0.0f)));
            // // criteria.createCriteria("person_id",
            // // (int)dataAccess.getCurrentUserId());
            // criteria.setProjection(Projections.rowCount());
            // in = (Integer) criteria.list().get(0);
            // sum_all = in.intValue();
            //
            // LOG.debug("Old Meter Data in Db: " + in.intValue());
            //
            // return sum_all;
        }
        catch (Exception ex)
        {
            LOG.error("getAllElementsCount: " + ex, ex);
            ex.printStackTrace();
            return null;
        }

    }


    public boolean isStockSubTypeUsed(StockSubTypeH stockType)
    {
        Criteria criteria = this.getSession().createCriteria(StockH.class);
        criteria.add(Restrictions.eq("personId", stockType.getPersonId()));
        criteria.add(Restrictions.eq("stockSubtype", stockType));
        criteria.setProjection(Projections.rowCount());

        Object o = criteria.uniqueResult();

        System.out.println("Stoc Tyoe: " + stockType.getName());
        System.out.println("o: " + o);

        if (o != null)
        {
            long count = m_da.getLongValue(o);

            System.out.println("Count: " + count);

            return count > 0;
        }

        return false;
    }

    // // OLD ONE
    // public ArrayList<StocksH> getStocks(int type, int history)
    // {
    //
    // // FIXME
    // if (m_loadStatus == DB_CONFIG_LOADED)
    // return null;
    //
    // LOG.info("getStocks() - Process");
    //
    // ArrayList<StocksH> list = new ArrayList<StocksH>();
    // String sql = "";
    //
    // try
    // {
    // sql = "SELECT sv FROM ggc.core.db.hibernate.StocksH as sv ";
    //
    // if (type != -1 || history != -1)
    // {
    // sql += " WHERE ";
    // }
    //
    // sql += " ORDER BY sv.dt_stock";
    //
    // Query q = getSession().createQuery(sql);
    //
    // Iterator<StocksH> it = q.list().iterator();
    //
    // while (it.hasNext())
    // {
    // StocksH sv = it.next();
    // // hbVal.addDayValueRow(new DailyValuesRow(dv));
    // list.add(sv);
    // }
    //
    // // hbVal.processDayValues();
    //
    // // logDebug("getHbA1c()", "Readings: " + hbVal.getDayCount() + " " +
    // // hbVal.getReadings());
    //
    // // System.out.println("Avg BG: " + hbVal.getAvgBGForMethod3());
    //
    // }
    // catch (Exception ex)
    // {
    // logException("getHbA1c()", ex);
    // }
    //
    // return list;
    // }

    // // OLD ONE
    // public ArrayList<StockBaseType> getStockBaseTypes()
    // {
    //
    // if (m_loadStatus == DB_CONFIG_LOADED)
    // return null;
    //
    // LOG.info("getStockBaseTypes() - Process");
    //
    // ArrayList<StockBaseType> list = new ArrayList<StockBaseType>();
    // String sql = "";
    //
    // try
    // {
    // sql = "SELECT sv FROM ggc.core.db.hibernate.StockTypeH as sv ";
    // sql += " ORDER BY sv.id";
    //
    // Query q = getSession().createQuery(sql);
    //
    // Iterator<StockTypeH> it = q.list().iterator();
    //
    // while (it.hasNext())
    // {
    // StockTypeH sv = it.next();
    // list.add(new StockBaseType(sv));
    // }
    //
    // }
    // catch (Exception ex)
    // {
    // LOG.error("getStockBaseTypes()::Exception: " + ex.getMessage(), ex);
    // }
    //
    // return list;
    //
    // }

    // *************************************************************
    // **** TOOLS Db METHODS ****
    // *************************************************************


    /*
     * <class name="ggc.core.db.hibernate.GlucoValueH" table="data_dayvalues" >
     * <id name="id" type="long" unsaved-value="0">
     * <generator class="org.hibernate.id.AssignedIncrementGenerator"/>
     * </id>
     * <property name="dt_info" type="long" not-null="true"/>
     * <property name="bg" type="int" />
     * <property name="person_id" type="int" not-null="true" />
     * <property name="changed" type="long" not-null="false" />
     * </class>
     */

    /**
     * Get Meter Values
     * 
     * @return
     */
    public Hashtable<String, DayValueH> getMeterValues()
    {

        Hashtable<String, DayValueH> ht = new Hashtable<String, DayValueH>();

        logInfo("getMeterValues()");

        try
        {

            logDebug("getMeterValues()", "Process");

            Query q = getSession(2).createQuery("SELECT dv from ggc.core.db.hibernate.DayValueH as dv "
                    + "WHERE (dv.bg>0) and person_id=" + m_da.current_user_id + " ORDER BY dv.dt_info");

            // System.out.println("Found elements: " + q.list().size());

            Iterator<?> it = q.list().iterator();

            while (it.hasNext())
            {
                DayValueH gv = (DayValueH) it.next();
                ht.put("" + gv.getDt_info(), gv);
            }

        }
        catch (Exception ex)
        {
            logException("getMeterValues()", ex);
            ex.printStackTrace();
        }

        return ht;
    }


    private boolean isDbRunning()
    {
        System.out.println("Is running: " + m_loadStatus);
        return (m_loadStatus == DB_STARTED);
    }


    private boolean isDbNotRunning()
    {
        return !(m_loadStatus == DB_STARTED);
    }

    // *************************************************************
    // **** S T O C K S ****
    // *************************************************************


    // *************************************************************
    // **** U T I L S ****
    // *************************************************************
    /*
     * public String changeCase(String in)
     * {
     * StringTokenizer stok = new StringTokenizer(in, " ");
     * boolean first = true;
     * String out = "";
     * while (stok.hasMoreTokens())
     * {
     * if (!first)
     * out += " ";
     * out += changeCaseWord(stok.nextToken());
     * first = false;
     * }
     * return out;
     * }
     * public String changeCaseWord(String in)
     * {
     * String t = "";
     * t = in.substring(0, 1).toUpperCase();
     * t += in.substring(1).toLowerCase();
     * return t;
     * }
     * public void showByte(byte[] in)
     * {
     * for (int i = 0; i < in.length; i++)
     * {
     * System.out.println((char) in[i] + " " + in[i]);
     * }
     * }
     */

    /**
     * Debug Out
     */
    @Override
    public void debugOut(String source, Exception ex)
    {

        this.m_errorCode = 1;
        this.m_errorDesc = ex.getMessage();

        if (debug)
        {
            System.out.println("  " + source + "::Exception: " + ex);
        }

        if (debug)
        {
            ex.printStackTrace();
        }

    }


    /**
     * Get Application Db Name
     */
    @Override
    public String getApplicationDbName()
    {
        return "ggc";
    }

}
