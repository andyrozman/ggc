/*
 * GGC - GNU Gluco Control
 * 
 * A pure Java application to help you manage your diabetes.
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
 * Filename: GGCDb Purpose: This is main datalayer file. It contains all methods
 * for initialization of Hibernate framework, for adding/updating/deleting data
 * from database (hibernate). It also contains all methods for mass readings of
 * data from hibernate.
 * 
 * Author: andyrozman {andy@atech-software.com}
 */

// WORK IN PROGRESS, PLEASE DO NOT TOUCH
// 
// Methods are added to this class, whenever we make changes to our existing
// database,
// either add methods for handling data or adding new tables.
// 
// andyrozman

package ggc.core.db;

import ggc.core.data.DailyValues;
import ggc.core.data.DailyValuesRow;
import ggc.core.data.HbA1cValues;
import ggc.core.data.MonthlyValues;
import ggc.core.data.WeeklyValues;
import ggc.core.db.datalayer.FoodDescription;
import ggc.core.db.datalayer.FoodGroup;
import ggc.core.db.datalayer.Meal;
import ggc.core.db.datalayer.MealGroup;
import ggc.core.db.datalayer.NutritionDefinition;
import ggc.core.db.datalayer.NutritionHomeWeightType;
import ggc.core.db.datalayer.Settings;
import ggc.core.db.hibernate.ColorSchemeH;
import ggc.core.db.hibernate.DayValueH;
import ggc.core.db.hibernate.FoodDescriptionH;
import ggc.core.db.hibernate.FoodGroupH;
import ggc.core.db.hibernate.FoodUserDescriptionH;
import ggc.core.db.hibernate.FoodUserGroupH;
import ggc.core.db.hibernate.GlucoValueH;
import ggc.core.db.hibernate.MealGroupH;
import ggc.core.db.hibernate.MealH;
import ggc.core.db.hibernate.NutritionDefinitionH;
import ggc.core.db.hibernate.NutritionHomeWeightTypeH;
import ggc.core.db.hibernate.SettingsH;
import ggc.core.nutrition.GGCTreeRoot;
import ggc.core.util.DataAccess;

import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.StringTokenizer;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;
import org.hibernate.tool.hbm2ddl.SchemaExport;

import com.atech.db.hibernate.DatabaseObjectHibernate;
import com.atech.graphics.dialogs.selector.SelectableInterface;

public class GGCDb // implements DbCheckInterface
{
    public static final int DB_CONFIG_LOADED = 1;
    public static final int DB_INITIALIZED = 2;
    public static final int DB_STARTED = 3;

    private boolean debug = true;
    // x private boolean db_debug = false;

    private static Log log = LogFactory.getLog(GGCDb.class);
    private Session m_session = null;
    private Session m_session_2 = null;
    private SessionFactory sessions = null;
    private int m_errorCode = 0;
    private String m_errorDesc = "";
    private String m_addId = "";

    GGCDbConfig hib_config = null;

    private Configuration m_cfg = null;
    private DataAccess m_da;

    private int m_loadStatus = 0;

    // GLOBAL DATA
    public Hashtable<String, NutritionDefinition> nutrition_defs = null;
    public Hashtable<String, NutritionHomeWeightType> homeweight_defs = null;
    public ArrayList<SelectableInterface> nutrition_defs_list = null;
    public ArrayList<SelectableInterface> homeweight_defs_list = null;

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

    public GGCDb(DataAccess da)
    {
        m_cfg = createConfiguration();
        m_da = da;
        m_loadStatus = DB_CONFIG_LOADED;
        // debugConfig();
    }

    public GGCDb()
    {
        m_cfg = createConfiguration();
        m_loadStatus = DB_CONFIG_LOADED;
        // debugConfig();
    }

    public Configuration getConfiguration()
    {
        return this.m_cfg;
    }

    /*
     * private void debugConfig() { /
     * System.out.println("Debug Configuration:");
     * 
     * //this.m_cfg.g //this.m_cfg.
     * 
     * Iterator it = this.m_cfg.getClassMappings();
     * 
     * //m_cfg.get
     * 
     * while (it.hasNext()) { org.hibernate.mapping.RootClass rc =
     * (org.hibernate.mapping.RootClass)it.next();
     * //System.out.println(it.next()); // exploreRootClass(rc); }
     */
    // }

    public void initDb()
    {
        openHibernateSimple();
    }

    public boolean isDbStarted()
    {
        return (this.m_loadStatus == DB_STARTED);
    }

    public void closeDb()
    {
        if (this.hib_config.getHibernateDialect().equals("org.hibernate.dialect.HSQLDialect"))
        {
            try
            {
                getSession().connection().createStatement().execute("SHUTDOWN");
            }
            catch (Exception ex)
            {
                System.out.println("closeDb:Exception> " + ex);
            }
        }
        getSession().close();
        m_session = null;
        m_loadStatus = DB_CONFIG_LOADED;
    }

    public GGCDbConfig getHibernateConfiguration()
    {
        return this.hib_config;
    }

    public void openHibernateSimple()
    {
        logInfo("openHibernateSimple", "Start");
        // getStartStatus();
        sessions = m_cfg.buildSessionFactory();
        // getStartStatus();

        m_session = sessions.openSession();
        m_session_2 = sessions.openSession();

        m_loadStatus = DB_INITIALIZED;
        logInfo("openHibernateSimple", "End");
    }

    /*
     * public void getStartStatus() { Map mpp = m_cfg.getSqlResultSetMappings();
     * 
     * System.out.println("isEmpty: " + mpp.isEmpty());
     * 
     * //log.debug(arg0);
     * 
     * }
     */

    public int getLoadStatus()
    {
        return m_loadStatus;
    }

    public void displayError(String source, Exception ex)
    {

        System.out.println("Exception [" + source + "]: " + ex);
        log.error("Exception [" + source + "]: " + ex, ex);

        if (debug)
        {
            System.out.println("Exception [" + source + "]: " + ex.getMessage());
            ex.printStackTrace();
        }

    }

    private void logException(String source, Exception ex)
    {
        log.error(source + "::Exception: " + ex.getMessage(), ex);
    }

    private void logDebug(String source, String action)
    {
        log.debug(source + " - " + action);
    }

    private void logInfo(String source, String action)
    {
        log.info(source + " - " + action);
    }

    private void logInfo(String source)
    {
        log.info(source + " - Process");
    }

    public Session getSession()
    {
        return getSession(1);
    }

    public Session getSession(int session_nr)
    {
        if (session_nr == 1)
        {
            m_session.clear();
            return m_session;
        }
        else
        {
            m_session_2.clear();
            return m_session_2;
        }
    }

    public void createDatabase()
    {
        logInfo("createDatabase", "Start");

        new SchemaExport(m_cfg).create(true, true);

        logInfo("openHibernateSimple", "End");
    }

    // *************************************************************
    // **** SETTINGS ****
    // *************************************************************

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

    // *************************************************************
    // **** SETTINGS ****
    // *************************************************************

    public Configuration createConfiguration()
    {
        logInfo("createConfiguration()");
        this.hib_config = new GGCDbConfig(true);
        return this.hib_config.getConfiguration();
    }

    /*
     * public Configuration createConfiguration() {
     * 
     * try {
     * 
     * Properties props = new Properties();
     * 
     * boolean config_read = false;
     * 
     * try { FileInputStream in = new
     * FileInputStream("../data/GGC_Config.properties"); props.load(in);
     * in.close();
     * 
     * db_num = new Integer(props.getProperty("SELECTED_DB")); db_conn_name =
     * props.getProperty("DB"+db_num+"_CONN_NAME");
     * 
     * config_read = true; } catch (Exception ex) {
     * 
     * }
     * 
     * 
     * 
     * if (config_read) { log.info("GGCDb: Loading Db Configuration #"+ db_num +
     * ": " + db_conn_name);
     * 
     * db_hib_dialect = props.getProperty("DB"+db_num+"_HIBERNATE_DIALECT");
     * 
     * 
     * db_driver_class = props.getProperty("DB"+db_num+"_CONN_DRIVER_CLASS");
     * db_conn_url = props.getProperty("DB"+db_num+"_CONN_URL");
     * db_conn_username = props.getProperty("DB"+db_num+"_CONN_USERNAME");
     * db_conn_password = props.getProperty("DB"+db_num+"_CONN_PASSWORD"); }
     * else { // we had trouble reading config so we use default database
     * 
     * db_num = 0; db_conn_name = "Internal Database";
     * 
     * 
     * log.info("GGCDb: Database configuration not found. Using default database."
     * ); log.info("GGCDb: Loading Db Configuration #"+ db_num + ": " +
     * db_conn_name);
     * 
     * db_hib_dialect = "org.hibernate.dialect.HSQLDialect"; db_driver_class =
     * "org.hsqldb.jdbcDriver"; db_conn_url = "jdbc:hsqldb:file:../data/ggc_db";
     * db_conn_username = "sa"; db_conn_password = ""; }
     * 
     * 
     * 
     * Configuration cfg = new Configuration()
     * .addResource("GGC_Nutrition.hbm.xml") .addResource("GGC_Main.hbm.xml")
     * .addResource("GGC_Other.hbm.xml") .addResource("GGC_Pump.hbm.xml")
     * 
     * .setProperty("hibernate.dialect", db_hib_dialect)
     * .setProperty("hibernate.connection.driver_class", db_driver_class)
     * .setProperty("hibernate.connection.url", db_conn_url)
     * .setProperty("hibernate.connection.username", db_conn_username)
     * .setProperty("hibernate.connection.password", db_conn_password)
     * .setProperty("hibernate.connection.charSet", "utf-8")
     * .setProperty("hibernate.use_outer_join", "true"); //
     * .setProperty("hibernate.show_sql", "true") /
     * .setProperty("hibernate.c3p0.min_size", "5")
     * .setProperty("hibernate.c3p0.max_size", "20")
     * .setProperty("hibernate.c3p0.timeout", "1800")
     * .setProperty("hibernate.c3p0.max_statements", "50");
     */
    /*
     * 
     * 
     * // System.out.println("Config loaded.");
     * 
     * 
     * return m_cfg; } catch (Exception ex) {
     * log.error("Loading GGCConfiguration Exception: " + ex.getMessage(), ex);
     * //ex.printStackTrace(); } return null; }
     */

    // *************************************************************
    // **** DATABASE INIT METHODS ****
    // *************************************************************

    public void loadStaticData()
    {
        m_loadStatus = DB_STARTED;
    }

    public void loadNutritionDbBase()
    {
        this.loadNutritionDefinitions();
        this.loadHomeWeights();
    }

    public void loadNutritionDb1()
    {
        // tree root, now in static data
        m_da.tree_roots.put("1", new GGCTreeRoot(GGCTreeRoot.TREE_USDA_NUTRITION, this));

        // m_da.m_nutrition_treeroot = new GGCTreeRoot(1, this);

    }

    public void loadNutritionDb2()
    {
        m_da.tree_roots.put("2", new GGCTreeRoot(GGCTreeRoot.TREE_USER_NUTRITION, this));
    }

    public void loadMealsDb()
    {
        m_da.tree_roots.put("3", new GGCTreeRoot(GGCTreeRoot.TREE_MEALS, this));
    }

    // *************************************************************
    // **** SETTINGS ****
    // *************************************************************

    /*
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
            // log.error("Exception on loadConfigData: " + ex.getMessage(), ex);
            logException("loadConfigData()", ex);

        }
    }

    @SuppressWarnings("unchecked")
    public void loadConfigDataEntries()
    {

        logInfo("loadConfigDataEntries()");

        try
        {
            Session sess = getSession(2);

            Hashtable<String, Settings> table = new Hashtable<String, Settings>();

            Query q = sess.createQuery("select cfg from ggc.core.db.hibernate.SettingsH as cfg");

            Iterator it = q.iterate();

            while (it.hasNext())
            {
                SettingsH eh = (SettingsH) it.next();
                table.put(eh.getKey(), new Settings(eh));
            }

            m_da.getConfigurationManager().checkConfiguration(table);
        }
        catch (Exception ex)
        {
            // log.error("Exception on loadConfigDataEntries: " +
            // ex.getMessage(), ex);
            logException("loadConfigDataEntries()", ex);
        }

    }

    /*
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

            m_da.getSettings().setColorSchemes(table, false);
        }
        catch (Exception ex)
        {
            // log.error("Exception on loadColorSchemes: " + ex.getMessage(),
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
    @SuppressWarnings("unchecked")
    public ArrayList<FoodGroup> getUSDAFoodGroups()
    {

        logInfo("getUSDAFoodGroups()");

        ArrayList<FoodGroup> list = new ArrayList<FoodGroup>();

        try
        {

            Query q = getSession(2).createQuery("select pst from ggc.core.db.hibernate.FoodGroupH as pst");

            Iterator it = q.iterate();

            while (it.hasNext())
            {
                FoodGroupH eh = (FoodGroupH) it.next();
                list.add(new FoodGroup(eh));
            }

        }
        catch (Exception ex)
        {
            logException("getUSDAFoodGroups()", ex);
        }

        return list;

    }

    @SuppressWarnings("unchecked")
    public ArrayList<FoodGroup> getUserFoodGroups()
    {
        logInfo("getUserFoodGroups()");

        ArrayList<FoodGroup> list = new ArrayList<FoodGroup>();

        try
        {

            Query q = getSession(2).createQuery("select pst from ggc.core.db.hibernate.FoodUserGroupH as pst");

            Iterator it = q.iterate();

            while (it.hasNext())
            {
                FoodUserGroupH eh = (FoodUserGroupH) it.next();
                list.add(new FoodGroup(eh));
            }

            return list;
        }
        catch (Exception ex)
        {
            logException("getUserFoodGroups()", ex);

            // log.error("Exception on getloadConfigData: " + ex.getMessage(),
            // ex);
        }

        return list;
    }

    @SuppressWarnings("unchecked")
    public ArrayList<MealGroup> getMealGroups()
    {

        logInfo("getMealGroups()");

        ArrayList<MealGroup> list = new ArrayList<MealGroup>();

        try
        {

            Query q = getSession(2).createQuery("select pst from ggc.core.db.hibernate.MealGroupH as pst");

            Iterator it = q.iterate();

            while (it.hasNext())
            {
                MealGroupH eh = (MealGroupH) it.next();
                list.add(new MealGroup(eh));
            }

        }
        catch (Exception ex)
        {
            logException("getMealGroups()", ex);
        }

        return list;
    }

    @SuppressWarnings("unchecked")
    public ArrayList<FoodDescription> getUSDAFoodDescriptions()
    {
        logInfo("getUSDAFoodDescriptions()");

        ArrayList<FoodDescription> list = new ArrayList<FoodDescription>();

        try
        {

            Query q = getSession(2).createQuery("select pst from ggc.core.db.hibernate.FoodDescriptionH as pst");

            Iterator it = q.iterate();

            while (it.hasNext())
            {
                FoodDescriptionH eh = (FoodDescriptionH) it.next();
                list.add(new FoodDescription(eh));
            }

            return list;
        }
        catch (Exception ex)
        {
            logException("getUSDAFoodDescriptions()", ex);
        }

        return list;
    }

    @SuppressWarnings("unchecked")
    public ArrayList<FoodDescription> getUserFoodDescriptions()
    {

        logInfo("getUserFoodDescriptions()");
        ArrayList<FoodDescription> list = new ArrayList<FoodDescription>();

        try
        {

            Query q = getSession(2).createQuery(
                "select pst from ggc.core.db.hibernate.FoodUserDescriptionH as pst order by pst.group_id, pst.name");

            Iterator it = q.iterate();

            while (it.hasNext())
            {
                FoodUserDescriptionH eh = (FoodUserDescriptionH) it.next();
                list.add(new FoodDescription(eh));
            }

            // System.out.println("Loaded Food Descriptions: " + list.size());

        }
        catch (Exception ex)
        {
            logException("getUserFoodDescriptions()", ex);
        }

        return list;

    }

    @SuppressWarnings("unchecked")
    // public Hashtable<String, Meal> getMeals()
    public ArrayList<Meal> getMeals()
    {
        logInfo("getMeals()");

        ArrayList<Meal> list = new ArrayList<Meal>();

        try
        {

            Query q = getSession(2).createQuery(
                "select pst from ggc.core.db.hibernate.MealH as pst order by pst.group_id, pst.name");

            Iterator it = q.iterate();

            while (it.hasNext())
            {
                MealH eh = (MealH) it.next();
                // list.put("" + eh.getId(), new Meal(eh));
                list.add(new Meal(eh));
            }

            return list;
        }
        catch (Exception ex)
        {
            logException("getMeals()", ex);
        }

        return list;

    }

    @SuppressWarnings("unchecked")
    public void loadNutritionDefinitions()
    {

        logInfo("loadNutritionDefinitions()");

        try
        {

            int[] ids = { 4000, 4001, 4002, 4003, 4004, 4005 };
            String[] tags = { "GI", "GL", "GI_MIN", "GI_MAX", "GL_MIN", "GL_MAX" };

            Hashtable<String, NutritionDefinition> nut_defs = new Hashtable<String, NutritionDefinition>();
            ArrayList<SelectableInterface> nut_defs_lst = new ArrayList<SelectableInterface>();

            Query q = getSession(2).createQuery("select pst from ggc.core.db.hibernate.NutritionDefinitionH as pst");

            Iterator it = q.iterate();

            while (it.hasNext())
            {
                NutritionDefinitionH eh = (NutritionDefinitionH) it.next();

                NutritionDefinition fnd = new NutritionDefinition(eh);
                nut_defs.put("" + fnd.getId(), fnd);
                nut_defs_lst.add(fnd);

            }

            // static nutrition - not in database yet
            // needs to be added to init

            // GI = 4000, GL = 4001, GI_MIN = 4002, GI_MAX = 4003,
            // GL_MIN = 4004, GL_MAX = 4005

            String[] units = { "gi", "gl", "gi", "gi", "gl", "gl" };
            String[] name = { "Glycemic Index", "Glycemic Load", "Glycemic Index (Min)", "Glycemic Index (Max)",
                             "Glycemic Load (Min)", "Glycemic Load (Max)" };

            for (int i = 0; i < ids.length; i++)
            {
                if (nut_defs.containsKey("" + ids[i]))
                    continue;

                NutritionDefinitionH eh = new NutritionDefinitionH(units[i], tags[i], name[i], "0", 1);
                eh.setId(ids[i]);

                NutritionDefinition ndef = new NutritionDefinition(eh);

                nut_defs.put("" + eh.getId(), ndef);
                nut_defs_lst.add(ndef);

            }

            this.nutrition_defs = nut_defs;
            this.nutrition_defs_list = nut_defs_lst;
        }
        catch (Exception ex)
        {
            logException("loadNutritionDefinitions()", ex);
        }

    }

    @SuppressWarnings("unchecked")
    public void loadHomeWeights()
    {

        logInfo("loadHomeWeights()");

        try
        {
            Hashtable<String, NutritionHomeWeightType> nut_hw = new Hashtable<String, NutritionHomeWeightType>();
            ArrayList<SelectableInterface> nut_hw_lst = new ArrayList<SelectableInterface>();

            Query q = getSession(2)
                    .createQuery("select pst from ggc.core.db.hibernate.NutritionHomeWeightTypeH as pst");

            Iterator it = q.iterate();

            while (it.hasNext())
            {
                NutritionHomeWeightTypeH eh = (NutritionHomeWeightTypeH) it.next();

                NutritionHomeWeightType fnd = new NutritionHomeWeightType(eh);
                nut_hw.put("" + fnd.getId(), fnd);
                nut_hw_lst.add(fnd);
            }

            int[] ids = { 4000, 4001, 4002, 4003, 4004, 4005, 4006, 4007, 4008, 4009, 4010, 4011, 4012, 4013, 4014,
                         4015, 4016, 4017, 4018, 4019 };
            String[] names = { "PORTION", "PORTION,_BIG", "PORTION,_MEDIUM", "PORTION,_SMALL", "SPOON", "SPOON,_BIG",
                              "STEAK,_SMALL", "STEAK,_SMALLER", "ITEMS", "SMALLER", "ITEM,_SMALLER",
                              "ITEM,_MEDIUM_SIZE", "ITEMS,_BIGGER", "SPOON,_SMALL", "ITEM,_SMALL", "CAN_(_100_G_)",
                              "HEADS", "ROOT,_MEDIUM", "SIZE", "FRUIT,_MEDIUM_SIZE" };

            // static (need to be done in init)

            for (int i = 0; i < ids.length; i++)
            {
                if (nut_hw.containsKey("" + ids[i]))
                    continue;

                NutritionHomeWeightTypeH eh = new NutritionHomeWeightTypeH(names[i], 1);
                eh.setId(ids[i]);

                NutritionHomeWeightType fnd = new NutritionHomeWeightType(eh);

                nut_hw.put("" + fnd.getId(), fnd);
                nut_hw_lst.add(fnd);
            }

            this.homeweight_defs = nut_hw;
            this.homeweight_defs_list = nut_hw_lst;

        }
        catch (Exception ex)
        {
            logException("loadHomeWeights()", ex);
        }

    }

    // *************************************************************
    // **** GGC Main Data ****
    // **** Comment: Were implemnted in DataBaseHandler ****
    // *************************************************************

    HbA1cValues hba1c_object = null;

    public HbA1cValues getHbA1c(GregorianCalendar day, boolean force)
    {
        if (this.hba1c_object == null)
        {
            hba1c_object = loadHbA1c(day);
        }
        else
        {
            if (force)
                hba1c_object = loadHbA1c(day);
        }

        return this.hba1c_object;

    }

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
                "SELECT dv from " + "ggc.core.db.hibernate.DayValueH as dv " + "WHERE dv.bg <> 0 AND dv.dt_info >=  "
                        + sDay + " AND dv.dt_info <= " + eDay + " ORDER BY dv.dt_info");

            Iterator it = q.list().iterator();

            while (it.hasNext())
            {
                DayValueH dv = (DayValueH) it.next();
                hbVal.addDayValueRow(new DailyValuesRow(dv));
            }

            hbVal.processDayValues();

            logDebug("getHbA1c()", "Readings: " + hbVal.getDayCount() + " " + hbVal.getReadings());

        }
        catch (Exception ex)
        {
            logException("getHbA1c()", ex);
        }

        return hbVal;
    }

    @SuppressWarnings("unchecked")
    public DailyValues getDayStats(GregorianCalendar day)
    {

        if (m_loadStatus == DB_CONFIG_LOADED)
            return null;

        logInfo("getDayStats()");

        DailyValues dV = new DailyValues();

        try
        {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
            String sDay = sdf.format(day.getTime());

            Query q = getSession().createQuery(
                "SELECT dv from " + "ggc.core.db.hibernate.DayValueH as dv " + "WHERE dv.dt_info >=  " + sDay
                        + "0000 AND dv.dt_info <= " + sDay + "2359 ORDER BY dv.dt_info");

            Iterator it = q.list().iterator();

            while (it.hasNext())
            {
                DayValueH dv = (DayValueH) it.next();

                DailyValuesRow dVR = new DailyValuesRow(dv);
                dV.setNewRow(dVR);
            }

        }
        catch (Exception ex)
        {
            logException("getDayStats()", ex);
        }

        return dV;
    }

    @SuppressWarnings("unchecked")
    public WeeklyValues getDayStatsRange(GregorianCalendar start, GregorianCalendar end)
    {

        if (m_loadStatus == DB_CONFIG_LOADED)
            return null;

        logInfo("getDayStatsRange()");

        WeeklyValues wv = new WeeklyValues();

        try
        {
            String sDay = m_da.getDateTimeStringFromGregorianCalendar(start, 1);
            String eDay = m_da.getDateTimeStringFromGregorianCalendar(end, 1);

            logDebug("getDayStatsRange()", sDay + " - " + eDay);

            Query q = getSession().createQuery(
                "SELECT dv from " + "ggc.core.db.hibernate.DayValueH as dv " + "WHERE dv.dt_info >=  " + sDay
                        + "0000 AND dv.dt_info <= " + eDay + "2359 ORDER BY dv.dt_info");

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

    @SuppressWarnings("unchecked")
    public MonthlyValues getMonthlyValues(int year, int month)
    {

        if (m_loadStatus == DB_CONFIG_LOADED)
            return null;

        logInfo("getMonthlyValues()");

        MonthlyValues mv = new MonthlyValues(year, month);

        try
        {
            String days = year + "" + m_da.getLeadingZero(month, 2);

            Query q = getSession().createQuery(
                "SELECT dv from " + "ggc.core.db.hibernate.DayValueH as dv " + "WHERE dv.dt_info >=  " + days
                        + "010000 AND dv.dt_info <= " + days + "312359 ORDER BY dv.dt_info");

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
                    DailyValuesRow dwr = dV.getRowAt(i);

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

            return (q.list().size() == 1);
        }
        catch (Exception ex)
        {
            logException("dateTimeExists()", ex);
            return false;
        }

    }

    
    
    
    
    
    
    
    // *************************************************************
    // **** NUTRITION DATA ****
    // *************************************************************

    public ArrayList<SelectableInterface> getNutritionHomeWeights()
    {
        return this.homeweight_defs_list;
    }

    public NutritionHomeWeightType getNutritionHomeWeight(long id)
    {
        return this.homeweight_defs.get("" + id);
    }

    public ArrayList<SelectableInterface> getNutritionDefinitions()
    {
        return this.nutrition_defs_list;
    }

    
    
    
    
    // *************************************************************
    // ****                      TOOLS Db METHODS               ****
    // *************************************************************
    
/*    
    <class name="ggc.core.db.hibernate.GlucoValueH" table="data_dayvalues"  >
    <id name="id" type="long" unsaved-value="0">
        <generator class="org.hibernate.id.AssignedIncrementGenerator"/>
    </id>            
    <property name="dt_info" type="long" not-null="true"/>    
    <property name="bg"  type="int" />
    <property name="person_id"  type="int" not-null="true" />
    <property name="changed" type="long" not-null="false" />    
</class>
*/
    
    public Hashtable<String,GlucoValueH> getMeterValues()
    {

        Hashtable<String,GlucoValueH> ht = new Hashtable<String,GlucoValueH>(); 
        
        logInfo("getMeterValues()");

        try
        {

            logDebug("getMeterValues()", "Process");

            Query q = getSession(2).createQuery(
                "SELECT dv from ggc.core.db.hibernate.GlucoValueH as dv " + 
                "WHERE (dv.bg>0) and person_id=" + m_da.current_user_id + 
                " ORDER BY dv.dt_info");

            //System.out.println("Found elements: " + q.list().size());
            
            Iterator<?> it = q.list().iterator();

            while (it.hasNext())
            {
                GlucoValueH gv = (GlucoValueH)it.next();
                ht.put("" + gv.getDt_info(), gv);
                
            //    System.out.println("Dt: " + gv.getDt_info());
            }

        }
        catch (Exception ex)
        {
            logException("getMeterValues()", ex);
            ex.printStackTrace();
        }

        return ht;
    }
    
    
    
    
    
    // *************************************************************
    // **** U T I L S ****
    // *************************************************************

    public String changeCase(String in)
    {

        StringTokenizer stok = new StringTokenizer(in, " ");

        boolean first = true;
        String out = "";

        while (stok.hasMoreTokens())
        {
            if (!first)
                out += " ";

            out += changeCaseWord(stok.nextToken());
            first = false;
        }

        return out;

    }

    public String changeCaseWord(String in)
    {

        String t = "";

        t = in.substring(0, 1).toUpperCase();
        t += in.substring(1).toLowerCase();

        return t;

    }

    public void showByte(byte[] in)
    {

        for (int i = 0; i < in.length; i++)
        {
            System.out.println((char) in[i] + " " + in[i]);
        }

    }

    public void debugOut(String source, Exception ex)
    {

        this.m_errorCode = 1;
        this.m_errorDesc = ex.getMessage();

        if (debug)
            System.out.println("  " + source + "::Exception: " + ex);

        if (debug)
            ex.printStackTrace();

    }

}