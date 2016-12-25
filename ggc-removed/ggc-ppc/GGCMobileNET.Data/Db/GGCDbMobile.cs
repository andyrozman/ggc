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
 * Filename: GGCDb 
 *
 * Purpose: This is main datalayer file. It contains all methods
 *      for initialization of Hibernate framework, for adding/updating/deleting data
 *      from database (hibernate). It also contains all methods for mass readings of
 *      data from hibernate.
 * 
 * Author: andyrozman {andy@atech-software.com}
 */

// Methods are added to this class, whenever we make changes to our existing
// database, either add methods for handling data or adding new tables.
// 
// andyrozman

using System;
using GGCMobileNET.Data.Tools;
using System.Data;
using log4net;
using GGCMobileNET.Data.Utils;
using System.Globalization;
using GGCMobileNET.Data.Db.Objects;

using System.Windows.Forms;
using System.Data.SqlServerCe;
namespace GGCMobileNET.Data.Db
{


    public class GGCDbMobile // implements DbCheckInterface HibernateDb
    {
        public const int DB_CONFIG_LOADED = 1;
        public const int DB_INITIALIZED = 2;
        public const int DB_STARTED = 3;

        private bool debug = true;
        // x private boolean db_debug = false;

        private int m_errorCode = 0;
        private String m_errorDesc = "";
        //    private String m_addId = "";

        //    GGCDbConfig hib_config = null;

        //    private Configuration m_cfg = null;
        private DataAccessMobile m_da;

        private int m_loadStatus = 0;

        //private IDbConnection m_connection = null;

        private SqlCeConnection m_connection = null;

        public SqlCeConnection Connection
        {
            get { return this.GetConnection(); }
            //set { m_connection = value; }
        }

        public static string ConnectionStringInit = "Data Source=GGCMobileDb.sdf;Password=ggc";
        public static string ConnectionString = "Data Source=GGCMobileDb.sdf;Password=ggc";
        //public static string ConnectionString = GGCDbMobile.ConnectionStringInit + ";Initial Catalog=GGCMobileDb";


        //    private SimpleLogger log = new SimpleLogger();
        private readonly ILog log = LogManager.GetLogger(typeof(GGCDbMobile));

        DummyDAO Util = new DummyDAO();



        // GLOBAL DATA
        //    public Hashtable<String, NutritionDefinition> nutrition_defs = null;
        //    public Hashtable<String, NutritionHomeWeightType> homeweight_defs = null;
        //    public ArrayList<SelectableInterface> nutrition_defs_list = null;
        //    public ArrayList<SelectableInterface> homeweight_defs_list = null;

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

        public GGCDbMobile(DataAccessMobile da)
        {
            /*m_cfg =*/
            createConfiguration();
            m_da = da;

            //        System.out.println("GGCDb");
            //        System.out.println("m_da: " + m_da);
            //        System.out.println("m_da.getSettings(): " + m_da.getSettings());

            createConfiguration();

            m_loadStatus = DB_CONFIG_LOADED;
            // debugConfig();
        }


        public GGCDbMobile()
        {
            /*m_cfg =*/
            //m_da = DataAccessMobile.getInstance();

            createConfiguration();
            m_loadStatus = DB_CONFIG_LOADED;
            // debugConfig();
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


        public SqlCeConnection GetConnection()
        {
            if (this.m_connection == null)
            {
                try
                {
                    SqlCeConnection conn = new SqlCeConnection(GGCDbMobile.ConnectionString);

                    

                    conn.Open();

                    this.m_connection = conn;
                    return this.m_connection;
                }
                catch (Exception ex)
                {
                    MessageBox.Show("Error opening connection: " + ex);
                    return null;
                }
            }
            else
                return this.m_connection;

        }



        public bool isDbStarted()
        {
            return (this.m_loadStatus == DB_STARTED);
        }

        public void closeDb()
        {
            //        this.hib_config.closeDb();
            m_loadStatus = DB_CONFIG_LOADED;
        }


        public void openHibernateSimple()
        {
            //        this.hib_config.createSessionFactory();
            /*        
                    logInfo("openHibernateSimple", "Start");
                    // getStartStatus();
                    sessions = m_cfg.buildSessionFactory();
                    // getStartStatus();

                    m_session = sessions.openSession();
                    m_session_2 = sessions.openSession();

                    m_loadStatus = DB_INITIALIZED;
                    logInfo("openHibernateSimple", "End");
                    */
            m_loadStatus = DB_INITIALIZED;
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


        /*
            public Session getSession()
            {
                return getSession(1);
            }

            public Session getSession(int session_nr)
            {
                return this.hib_config.getSession(session_nr);
                /*
                if (session_nr == 1)
                {
                    m_session.clear();
                    return m_session;
                }
                else
                {
                    m_session_2.clear();
                    return m_session_2;
                }*/
        //  }

        //    ObjectContainer db;


        public void createDatabase()
        {
            //        logInfo("createDatabase", "Process");

            //        new SchemaExport(this.getHibernateConfiguration().getConfiguration()).create(true, true);

            //logInfo("openHibernateSimple", "End");
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

        public void createConfiguration()
        {

            try
            {
                /*
                // H2 (To many files, Too slow to even work)
                Class.forName("org.h2.Driver");
                this.m_connection = DriverManager.getConnection("jdbc:h2:../data/db/ggc_db", "sa", "");
                */

                /*
                // Tiny SQL (no init possible, ex, no functions)
                Class.forName("com.sqlmagic.tinysql.dbfFileDriver");
                this.m_connection = DriverManager.getConnection("jdbc:dbfFile:../data/db2/", "", "");
                */

                /*
                // Small SQL (Works. Very slow)
                Class.forName("smallsql.database.SSDriver");
                this.m_connection = DriverManager.getConnection("jdbc:smallsql:../data/db3", "", "");
                */

                /*
                // HSQL Db  (Works ok, startup and shutdown too slow, memory req. very big) 
                Class.forName("org.hsqldb.jdbcDriver" );
                this.m_connection = DriverManager.getConnection("jdbc:hsqldb:file:../data/db4/ggcdb", "sa", "");
                */

                // db4o
                //this.db=Db4o.openFile("../data/db5/ggcdb");

            }
            catch (Exception ex)
            {
                Console.WriteLine("Exception on creation. " + ex);
                //ex.printStackTrace();
            }


            //        logInfo("createConfiguration()");
            //        this.hib_config = new GGCDbConfig(true);
            //        this.hib_config.getConfiguration();
        }


        /*
        public ObjectContainer getDb()
        {
            ObjectContainer db = Db4o.openFile("../data/db5/ggc.db");  // ggcdb
            return db;
        }*/


        // ---
        // --- BASIC METHODS (Hibernate and DataLayer processing)
        // ---

        public bool AddDAO(DatabaseAccessObject doh)
        {

            log.Info(doh.ObjectName + "::DbAdd");

            try
            {
                return doh.AddDb(this.Connection, null); // getSession());
            }
            catch (Exception ex)
            {
                setError(1, ex.Message, doh.ObjectName);
                log.Error("SQLException on add: " + ex, ex);
                return false;
            }

        }



        public bool EditDAO(DatabaseAccessObject doh)
        {

            log.Info(doh.ObjectName + "::DbEdit");

            try
            {
                doh.EditDb(this.getConnection(), null);
                return true;
            }
            catch (Exception ex)
            {
                setError(1, ex.Message, doh.ObjectName);
                log.Error("Exception on edit: " + ex, ex);
                return false;
            }

        }




        public bool getDAO(DatabaseAccessObject doh)
        {


            log.Info(doh.ObjectName + "::DbGet");

            try
            {
                doh.GetDb(this.getConnection(), null);
                return true;
            }
            catch (Exception ex)
            {
                setError(1, ex.Message, doh.ObjectName);
                log.Error("Exception on get: " + ex, ex);
                return false;
            }


        }

        public bool deleteDAO(DatabaseAccessObject doh)
        {


            log.Info(doh.ObjectName + "::DbDelete");

            try
            {

                if (doh.HasChildren(this.getConnection(), null))
                {
                    setError(-3, "Object has children object", doh.ObjectName);
                    log.Error(doh.ObjectName + " had Children objects");
                    return false;
                }

                doh.DeleteDb(this.getConnection(), null);

                return true;
            }
            catch (Exception ex)
            {
                setError(1, ex.Message, doh.ObjectName);
                log.Error("Exception on delete: " + ex, ex);
                return false;
            }


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
            //        this.loadNutritionDefinitions();
            //        this.loadHomeWeights();
        }

        public void loadNutritionDb1()
        {
            // tree root, now in static data
            //        m_da.tree_roots.put("1", new GGCTreeRoot(GGCTreeRoot.TREE_USDA_NUTRITION, this));

            // m_da.m_nutrition_treeroot = new GGCTreeRoot(1, this);

        }

        public void loadNutritionDb2()
        {
            //        m_da.tree_roots.put("2", new GGCTreeRoot(GGCTreeRoot.TREE_USER_NUTRITION, this));
        }

        public void loadMealsDb()
        {
            //        m_da.tree_roots.put("3", new GGCTreeRoot(GGCTreeRoot.TREE_MEALS, this));
        }


        public IDbConnection getConnection()
        {
            return this.m_connection;
        }



        public void some()
        {
            /*   
               try 
               {
            
                   Statement stmt = this.m_connection.createStatement();
                   ResultSet rs;

                   rs = stmt.executeQuery("SELECT Lname FROM Customers WHERE Snum = 2001");
                   while ( rs.next() ) {
                       String lastName = rs.getString("Lname");
                       Console.WriteLine(lastName);
                   }
               } 
               catch (Exception e) 
               {
                   System.err.println("Got an exception! ");
                   System.err.println(e.getMessage());
               }         
              */

        }





        // *************************************************************
        // **** SETTINGS ****
        // *************************************************************

        /*
         * We load all config data (including schemes)
         */
        /*    public void loadConfigData()
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
        */
        /*
         * We save just config, schemes save must be called separately
         */
        /*    public void saveConfigData()
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

                    System.out.println("m_da: " + m_da);
                    System.out.println("m_da.getSettings(): " + m_da.getSettings());
            
            
                    m_da.getSettings().setColorSchemes(table, false);
                }
                catch (Exception ex)
                {
                    // log.error("Exception on loadColorSchemes: " + ex.getMessage(),
                    // ex);
                    logException("loadColorSchemes()", ex);
                }

            }
        */
        /*
         * private void saveColorSchemes(Session sess) {
         * DataAccess.notImplemented("GGCDb::saveColorSchemes()"); }
         */

        // *************************************************************
        // **** NUTRITION DATA ****
        // *************************************************************
        /*    public ArrayList<FoodGroup> getUSDAFoodGroups()
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
        */
        // *************************************************************
        // **** GGC Main Data ****
        // **** Comment: Were implemnted in DataBaseHandler ****
        // *************************************************************

        /*    
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
            
                    System.out.println("Avg BG: " + hbVal.getAvgBGForMethod3());

                }
                catch (Exception ex)
                {
                    logException("getHbA1c()", ex);
                }

                return hbVal;
            }
        */

        public void InitialInitDb()
        {
            MobileDbInit mdi = new MobileDbInit();
            mdi.InitDb();
        }


        
        public DailyValues GetDayStats(DateTime day)
        {

            DailyValues dV = new DailyValues();
            dV.setDate(m_da.getDateTimeFromDateObject(day));

            try
            {
                // TODO: dd
            
                //SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
                String sDay = m_da.getDateTimeStringFromDateTimeObject(day, 1);

                int count = 0;
            
                String sql = "SELECT * from data_dayvalues " + "WHERE dt_info >=  " + sDay
                            + "0000 AND dt_info <= " + sDay + "2359 "; //ORDER BY dt_info";
            
                //System.out.println("sql: " + sql);

                DataTable dt = this.Util.GetDataTable(sql, "data_dayvalues", log);

                if ((dt == null) || (dt.Rows == null))
                    return dV;

                for (int i = 0; i < dt.Rows.Count; i++)
                {
                    DataRow dr = dt.Rows[i];
                    DayValueDAO af = new DayValueDAO();
                    af.GetDb(dr);

                    dV.setNewRow(new DailyValuesRow(af));
                    count++;
                }

                Console.WriteLine("Entries: " + dV.elementCount() + ", real_count=" + count);

            }
            catch (Exception ex)
            {
                Console.WriteLine("Exception: " + ex);
            }

            return dV;

        }
    

        /*
        public DailyValues getDayStats(GregorianCalendar day)
        {

        
            DailyValues dV = new DailyValues();
            dV.setDate(m_da.getDateTimeFromDateObject(day.getTime()) / 10000);

            ObjectContainer db = getDb();

            try
            {
                // TODO: dd
            
                SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
                String sDay = sdf.format(day.getTime());

                int count = 0;
            
            
            
            
            
            
                long t_s = Long.parseLong(sDay + "0000");
                long t_e = Long.parseLong(sDay + "2359");
            
            

            
            
            
                Query query=db.query();

            
                Constraint c1 = query.descend("dt_info").constrain(new Long(t_s)).greater()
                .or(query.constrain(new Long(t_s)).equal());
            
                Constraint c2 = query.descend("dt_info").constrain(new Long(t_e)).smaller()
                .or(query.constrain(new Long(t_e)).equal());
            
            
                query.constrain(DayValueDAO.class).and(c1).and(c2);
            
            
    //            Constraint c1 = query.descend("dt_info").constrain(new Long(t_s)).greater();
            
            
            
            
                ObjectSet result=query.execute();            
  
                while (result.hasNext())
                {
                    DayValueDAO dvd = (DayValueDAO)result.next();
                    //DailyValuesRow dVR = (DailyValuesRow)dvd;
                    DailyValuesRow dVR = new DailyValuesRow(dvd);
                
                    //dVR.dbGet(rs);
                    dV.setNewRow(dVR);
                    count++;
                }
            
            
            

                System.out.println("Entries: " + dV.elementCount() + ", real_count=" + count);

            }
            catch (Exception ex)
            {
                System.out.println("Exception: " + ex);
                ex.printStackTrace();
            }
            finally
            {
                db.close();
            }

            return dV;

        }
    */



        /*
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

    
            @SuppressWarnings("unchecked")
            public DayValuesData getDayValuesData(long from, long till)
            {

                if (m_loadStatus == DB_CONFIG_LOADED)
                    return null;

                logInfo("getDayValuesData()");

                DayValuesData dvd = new DayValuesData(from, till);
        
                try
                {
                    Query q = getSession().createQuery(
                        "SELECT dv from " + "ggc.core.db.hibernate.DayValueH as dv " + "WHERE dv.dt_info >=  " + from
                                + "0000 AND dv.dt_info <= " + till + "2359 ORDER BY dv.dt_info");

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
          */

        /*    
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

    
          */





        // *************************************************************
        // **** NUTRITION DATA ****
        // *************************************************************

        /*    
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
        */




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




        // *************************************************************
        // **** U T I L S ****
        // *************************************************************

        public String changeCase(String ins)
        {

            //StringTokenizer stok = new StringTokenizer(ins, " ");
            String[] arr = ins.Split(" ".ToCharArray());

            //boolean first = true;
            String outs = "";

            //while (stok.hasMoreTokens())

            for (int i = 0; i < arr.Length; i++)
            {
                if (i == 0) //(!first)
                    outs += " ";

                outs += changeCaseWord(arr[i]); //stok.nextToken());
                //first = false;
            }

            return outs;

        }

        public String changeCaseWord(String ins)
        {

            String t = "";

            t = ins.Substring(0, 1).ToLower();
            t += ins.Substring(1).ToLower();

            return t;

        }

        public void showByte(byte[] inb)
        {

            for (int i = 0; i < inb.Length; i++)
            {
                Console.WriteLine((char)inb[i] + " " + inb[i]);
            }

        }

        public void debugOut(String source, Exception ex)
        {

            this.m_errorCode = 1;
            this.m_errorDesc = ex.Message; //.getMessage();

            if (debug)
                Console.WriteLine("  " + source + "::Exception: " + ex);

            if (debug)
                Console.WriteLine(ex.StackTrace);
            //ex.printStackTrace();

        }

    }
}