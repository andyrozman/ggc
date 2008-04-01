/*
 *  GGC - GNU Gluco Control
 *
 *  A pure Java application to help you manage your diabetes.
 *
 *  See AUTHORS for copyright information.
 *
 *  This program is free software; you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation; either version 2 of the License, or
 *  (at your option) any later version.
 *
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with this program; if not, write to the Free Software
 *  Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA
 *
 *  Filename: GGCDb
 *  Purpose:  This is main datalayer file. It contains all methods for initialization of
 *      Hibernate framework, for adding/updating/deleting data from database (hibernate).
 *      It also contains all methods for mass readings of data from hibernate. 
 *
 *  Author:   andyrozman  {andy@atech-software.com}
 */

// WORK IN PROGRESS, PLEASE DO NOT TOUCH
// 
// Methods are added to this class, whenever we make changes to our existing database,
// either add methods for handling data or adding new tables.
// 
// andyrozman


package ggc.db;

import ggc.data.DailyValues;
import ggc.data.DailyValuesRow;
import ggc.data.HbA1cValues;
import ggc.data.MonthlyValues;
import ggc.data.WeeklyValues;
import ggc.db.datalayer.FoodDescription;
import ggc.db.datalayer.FoodGroup;
import ggc.db.datalayer.Meal;
import ggc.db.datalayer.MealGroup;
import ggc.db.datalayer.NutritionDefinition;
import ggc.db.datalayer.NutritionHomeWeightType;
import ggc.db.datalayer.Settings;
import ggc.db.hibernate.ColorSchemeH;
import ggc.db.hibernate.DatabaseObjectHibernate;
import ggc.db.hibernate.DayValueH;
import ggc.db.hibernate.FoodDescriptionH;
import ggc.db.hibernate.FoodGroupH;
import ggc.db.hibernate.FoodUserDescriptionH;
import ggc.db.hibernate.FoodUserGroupH;
import ggc.db.hibernate.MealGroupH;
import ggc.db.hibernate.MealH;
import ggc.db.hibernate.NutritionDefinitionH;
import ggc.db.hibernate.NutritionHomeWeightTypeH;
import ggc.db.hibernate.SettingsH;
import ggc.core.nutrition.GGCTreeRoot;
import ggc.util.DataAccess;

import java.io.FileInputStream;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Properties;
import java.util.StringTokenizer;

import com.atech.graphics.dialogs.selector.SelectableInterface;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;
import org.hibernate.tool.hbm2ddl.SchemaExport;


public class GGCDb
{
    public static final int DB_CONFIG_LOADED = 1;
    public static final int DB_INITIALIZED = 2;
    public static final int DB_STARTED = 3;

    private boolean debug = true;
    private boolean db_debug = false;

    private static Log log = LogFactory.getLog(GGCDb.class); 
    private Session m_session = null;
    private SessionFactory sessions = null;
    private int m_errorCode = 0;
    private String m_errorDesc = "";
    private String m_addId = "";


    private Configuration m_cfg = null;
    private DataAccess m_da; 

    private int m_loadStatus = 0;

    // GLOBAL DATA
    public Hashtable<String,NutritionDefinition> nutrition_defs = null;
    public Hashtable<String,NutritionHomeWeightType> homeweight_defs = null;
    public ArrayList<SelectableInterface> nutrition_defs_list = null;
    public ArrayList<SelectableInterface> homeweight_defs_list = null;

    /*
    public ArrayList<MeterCompanyH> meter_companies = null;
    public Hashtable<String,ArrayList<MeterH>> meters_by_cmp = null;
    public Hashtable<String,MeterH> meters_full = null;
*/





    // ---
    // ---  DB Settings
    // ---
    protected int db_num = 0;
    protected String db_hib_dialect = null; 
    protected String db_driver_class = null;
    protected String db_conn_name = null;
    protected String db_conn_url = null;
    protected String db_conn_username = null;
    protected String db_conn_password = null;


    public GGCDb(DataAccess da)
    {
        m_cfg = createConfiguration();
        m_da = da;
        m_loadStatus = DB_CONFIG_LOADED;
//	debugConfig();
    }


    public GGCDb()
    {
        m_cfg = createConfiguration();
        m_loadStatus = DB_CONFIG_LOADED;
//	debugConfig();
    }

    public Configuration getConfiguration()
    {
        return this.m_cfg;
    }

/*
    private void debugConfig()
    {
/*
	System.out.println("Debug Configuration:");

        //this.m_cfg.g
        //this.m_cfg.

        Iterator it = this.m_cfg.getClassMappings();

        //m_cfg.get
        
        while (it.hasNext())
        {
            org.hibernate.mapping.RootClass rc = (org.hibernate.mapping.RootClass)it.next();
            //System.out.println(it.next());
//	    exploreRootClass(rc);
        }

*/
//    }



    public void initDb()
    {
        openHibernateSimple();
    }

    public boolean isDbStarted()
    {
        return(this.m_loadStatus == DB_STARTED);
    }

    public void closeDb()
    {
        if (db_hib_dialect.equals("org.hibernate.dialect.HSQLDialect"))
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


    public void openHibernateSimple()
    {
        sessions = m_cfg.buildSessionFactory();
        m_session = sessions.openSession();
        m_loadStatus = DB_INITIALIZED;
    }


    public int getLoadStatus()
    {
        return m_loadStatus;
    }


    

    public void displayError(String source, Exception ex)
    {

        System.out.println("Exception ["+ source + "]: " + ex);
        log.error("Exception [" + source + "]: " + ex, ex);

        if (debug)
        {
            System.out.println("Exception ["+ source +"]: " + ex.getMessage());
            ex.printStackTrace();
        }

    }


    public Session getSession()
    {
        m_session.clear();
        return m_session;
    }


    public void createDatabase()
    {
        new SchemaExport(m_cfg).create(true, true);
    }

    
    
    
    

    // *************************************************************
    // ****                     SETTINGS                        ****
    // *************************************************************

    //---
    //---  BASIC METHODS (Hibernate and DataLayer processing)
    //---



    public boolean add(Object obj)
    {

        if (obj instanceof DatabaseObjectHibernate)
        {
            DatabaseObjectHibernate doh = (DatabaseObjectHibernate)obj;

            if (doh.isDebugMode())
                System.out.println(doh.getObjectName()+"::DbAdd");

            try
            {
                String id = doh.DbAdd(getSession()); //getSession());
                this.m_addId = id;
                return true;
            }
            catch (SQLException ex)
            {
                setError(1, ex.getMessage(), doh.getObjectName());
                System.out.println("SQLException on add: " + ex);
                ex.printStackTrace();
                Exception eee = ex.getNextException();

                if (eee!=null)
                {
                    eee.printStackTrace();
                    System.out.println(eee);
                }
                //System.exit(1);
                return false;
            }
            catch (Exception ex)
            {
                setError(1, ex.getMessage(), doh.getObjectName());
                System.out.println("Exception on add: " + ex);
                ex.printStackTrace();
/*
                Exception eee = ex.getNextException();

                if (eee!=null)
                {
                    eee.printStackTrace();
                    System.out.println(eee);
                }
*/

                //              System.exit(1);
                return false;
            }

        }
        else
        {
            setError(-2, "Object is not DatabaseObjectHibernate instance", "ZisDb");
            System.out.println("Internal error on add: " + obj);
            return false;
        }

    }


    // this method is used for direct use with hibernate objects (unlike use with our 
    // datalayer classes)
    public long addHibernate(Object obj)
    {

        //if (debug)
        //    System.out.println("addHibernate::" + obj.toString());

        try
        {
            Session sess = getSession();
            Transaction tx = sess.beginTransaction();

            Long val = (Long)sess.save(obj);
            tx.commit();

            return val.longValue();
        }
        catch (Exception ex)
        {
            System.out.println("Exception on addHibernate: " + ex);
            ex.printStackTrace();
            return -1;
        }

    }



    public boolean edit(Object obj)
    {

        //System.out.println("edit");

        if (obj instanceof DatabaseObjectHibernate)
        {
            DatabaseObjectHibernate doh = (DatabaseObjectHibernate)obj;


            if (doh.isDebugMode())
                System.out.println(doh.getObjectName()+"::DbEdit");

            try
            {
                doh.DbEdit(getSession()); 
                return true;
            }
            catch (Exception ex)
            {
                setError(1, ex.getMessage(), doh.getObjectName());
                return false;
            }
        }
        else
        {
            setError(-2, "Object is not DatabaseObjectHibernate instance", "ZisDb");
            System.out.println("Internal error on edit: " + obj);
            return false;
        }

    }


    // this method is used for direct use with hibernate objects (unlike use with our 
    // datalayer classes)
    public boolean editHibernate(Object obj)
    {

        if (debug)
            System.out.println("editHibernate::" + obj.toString());

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
            System.out.println("Exception on editHibernate: " + ex);
            ex.printStackTrace();
            return false;
        }

    }


    public boolean deleteHibernate(Object obj)
    {

        if (debug)
            System.out.println("deleteHibernate::" + obj.toString());

        try
        {
            Session sess = getSession();
            Transaction tx = sess.beginTransaction();

            //sess..update(obj);
            sess.delete(obj);

            tx.commit();

            return true;
        }
        catch (Exception ex)
        {
            System.out.println("Exception on deleteHibernate: " + ex);
            ex.printStackTrace();
            return false;
        }

    }




    public boolean get(Object obj)
    {

        if (obj instanceof DatabaseObjectHibernate)
        {
            DatabaseObjectHibernate doh = (DatabaseObjectHibernate)obj;

            if (doh.isDebugMode())
                System.out.println(doh.getObjectName()+"::DbGet");

            try
            {
                doh.DbGet(getSession());
                return true;
            }
            catch (Exception ex)
            {
                setError(1, ex.getMessage(), doh.getObjectName());
                return false;
            }

        }
        else
        {
            setError(-2, "Object is not DatabaseObjectHibernate instance", "ZisDb");
            System.out.println("Internal error on get: " + obj);
            return false;
        }

    }




    public boolean delete(Object obj)
    {

        if (obj instanceof DatabaseObjectHibernate)
        {
            DatabaseObjectHibernate doh = (DatabaseObjectHibernate)obj;


            if (doh.isDebugMode())
                System.out.println(doh.getObjectName()+"::DbDelete");

            try
            {

                if (doh.DbHasChildren(getSession()))
                {
                    setError(-3, "Object has children object", doh.getObjectName());
                    return false;
                }

                doh.DbDelete(getSession());

                return true;
            }
            catch (Exception ex)
            {
                setError(1, ex.getMessage(), doh.getObjectName());
                return false;
            }

        }
        else
        {
            setError(-2, "Object is not DatabaseObjectHibernate instance", "ZisDb");
            System.out.println("Internal error on delete: " + obj);
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
    // ****                     SETTINGS                        ****
    // *************************************************************


    public Configuration createConfiguration()
    {


        try
        {

            Properties props = new Properties();

            boolean config_read = false;

            try
            {
                FileInputStream in = new FileInputStream("../data/GGC_Config.properties");
                props.load(in);
                in.close();

                db_num = new Integer(props.getProperty("SELECTED_DB"));
                db_conn_name = props.getProperty("DB"+db_num+"_CONN_NAME");

                config_read = true;
            }
            catch (Exception ex)
            {

            }

            //System.out.println(in);


            //db_num = new Integer(props.getProperty("SELECTED_DB"));
            //db_conn_name = props.getProperty("DB"+db_num+"_CONN_NAME");


            if (config_read)
            {
                System.out.println("GGCDb: Loading Db Configuration #"+ db_num + ": " + db_conn_name);

                db_hib_dialect = props.getProperty("DB"+db_num+"_HIBERNATE_DIALECT");


                db_driver_class = props.getProperty("DB"+db_num+"_CONN_DRIVER_CLASS");
                db_conn_url = props.getProperty("DB"+db_num+"_CONN_URL");
                db_conn_username = props.getProperty("DB"+db_num+"_CONN_USERNAME");
                db_conn_password = props.getProperty("DB"+db_num+"_CONN_PASSWORD");
            }
            else
            {
                // we had trouble reading config so we use default database

                db_num = 0;
                db_conn_name = "Internal Database";

                System.out.println("GGCDb: Database configuration not found. Using default database.");
                System.out.println("GGCDb: Loading Db Configuration #"+ db_num + ": " + db_conn_name);

                db_hib_dialect = "org.hibernate.dialect.HSQLDialect";
                db_driver_class = "org.hsqldb.jdbcDriver";
                db_conn_url = "jdbc:hsqldb:file:../data/ggc_db";
                db_conn_username = "sa";
                db_conn_password = "";
            }



            Configuration cfg = new Configuration()
                                .addResource("GGC_Nutrition.hbm.xml")
                                .addResource("GGC_Main.hbm.xml")
                                .addResource("GGC_Other.hbm.xml")

                                .setProperty("hibernate.dialect", db_hib_dialect)
                                .setProperty("hibernate.connection.driver_class", db_driver_class)
                                .setProperty("hibernate.connection.url", db_conn_url)
                                .setProperty("hibernate.connection.username", db_conn_username)
                                .setProperty("hibernate.connection.password", db_conn_password)
                                .setProperty("hibernate.connection.charSet", "utf-8")
                                .setProperty("hibernate.use_outer_join", "true");
//	      .setProperty("hibernate.show_sql", "true")
/*                            .setProperty("hibernate.c3p0.min_size", "5")
                            .setProperty("hibernate.c3p0.max_size", "20")
                            .setProperty("hibernate.c3p0.timeout", "1800")
                            .setProperty("hibernate.c3p0.max_statements", "50"); */


//	    System.out.println("Config loaded.");

            return cfg;
        }
        catch (Exception ex)
        {
            ex.printStackTrace();
        }
        return null;
    }


    // *************************************************************
    // ****               DATABASE INIT METHODS                 ****
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
	
        //m_da.m_nutrition_treeroot = new GGCTreeRoot(1, this);

    }

    
    public void loadNutritionDb2()
    {
	m_da.tree_roots.put("2", new GGCTreeRoot(GGCTreeRoot.TREE_USER_NUTRITION, this));
    }
    
    
    public void loadMealsDb()
    {
	m_da.tree_roots.put("3", new GGCTreeRoot(GGCTreeRoot.TREE_MEALS, this));
    }
    
    /*
    @SuppressWarnings("unchecked")
    public void loadImplementedMeterData()
    {

        // loading implemented meters

        Hashtable<String, ArrayList<MeterH>> table = new Hashtable<String, ArrayList<MeterH>>();

        Query q = this.getSession().createQuery("select pst from ggc.db.hibernate.MeterH as pst where pst.implementation_id>0 order by pst.company_id, pst.name asc");

        //org.hibernate.collections.Iterator ittt = null;
        //org.hibernate.util.

        
        Iterator it = q.iterate();

        System.out.println("Meter implementations: " + q.list().size());

        String company_id = null;

        ArrayList<MeterH> mtrs = null;

        meters_full = new Hashtable<String,MeterH>();

        while (it.hasNext())
        {
            MeterH mh = (MeterH)it.next();

            if (company_id==null)
            {
                mtrs = new ArrayList<MeterH>();
                company_id = "" + mh.getCompany_id();
//		mtrs = new Hashtable<String,MeterH>();
            }

            if (!company_id.equals(""+mh.getCompany_id()))
            {
                table.put(company_id, mtrs);
                mtrs = new ArrayList<MeterH>();
                company_id = "" + mh.getCompany_id();
            }

            mtrs.add(mh);

            if (!meters_full.contains(""+mh.getId()))
            {
                this.meters_full.put(""+mh.getId(), mh);
            }
        }

        table.put(company_id, mtrs);
        this.meters_by_cmp = table;


        StringBuffer buf = new StringBuffer();
        boolean first = true;

        for (Enumeration<String> en=table.keys(); en.hasMoreElements(); )
        {
            String key = en.nextElement();

            if (first)
            {
                first = false;
            }
            else
                buf.append(" OR ");

            buf.append("pst.id=");
            buf.append(key);
        }

        System.out.println("Where : " + buf.toString());


        // resolve meter names
        Query q2 = this.getSession().createQuery("select pst from ggc.db.hibernate.MeterCompanyH as pst where " + buf.toString() + " order by pst.id asc");

        Iterator it2 = q2.iterate();

        ArrayList<MeterCompanyH> mtrs_cmp = new ArrayList<MeterCompanyH>();

        while (it2.hasNext())
        {
            MeterCompanyH mc = (MeterCompanyH)it2.next();
            mtrs_cmp.add(mc);
        }

        this.meter_companies = mtrs_cmp;

    }
*/



    // *************************************************************
    // ****                     SETTINGS                        ****
    // *************************************************************

    /**
     * We load all config data (including schemes)
     */
    public void loadConfigData()
    {

        Session sess = getSession();
        //SettingsMainH seti = (SettingsMainH)sess.get(SettingsMainH.class, new Long(1));


        // load schemes
        loadColorSchemes(sess);

        // sets settings
        loadConfigDataEntries();

        /*
            // sets settings
            m_da.getSettings().setSettings(seti);
    
            // sets active color scheme
            m_da.getSettings().setColorSchemeObject(seti.getColor_scheme());
        */
    }

    @SuppressWarnings("unchecked")
    public void loadConfigDataEntries()
    {

        Session sess = getSession();

        Hashtable<String,Settings> table = new Hashtable<String,Settings>();


        Query q = sess.createQuery("select cfg from ggc.db.hibernate.SettingsH as cfg");

        Iterator it = q.iterate();

        while (it.hasNext())
        {
            SettingsH eh = (SettingsH)it.next();
            table.put(eh.getKey(), new Settings(eh));
        }

        m_da.getConfigurationManager().checkConfiguration(table);

    }   


    public void checkConfigDataEntries()
    {
        /*
        <property name="name" type="string"  length="50"/>    
        <property name="ins1_name" type="string" length="20" />
        <property name="ins1_abbr" type="string" length="10" />
        <property name="ins2_name" type="string" length="20" />
        <property name="ins2_abbr" type="string" length="10" />
        <property name="meter_type"  type="int" />
        <property name="meter_port"  type="string" length="50" />
        <property name="bg_unit"  type="int" />  <!-- 1= mmol/l, 2=mg/dl -->
        <property name="bg1_low"  type="float"  />
        <property name="bg1_high"  type="float"  />
        <property name="bg1_target_low"  type="float"  />
        <property name="bg1_target_high"  type="float"  />
        <property name="bg2_low"  type="float"  />
        <property name="bg2_high"  type="float"  />
        <property name="bg2_target_low"  type="float"  />
        <property name="bg2_target_high"  type="float"  />
        <property name="laf_type" type="int"  /> <!-- 1= class specified, 2=skinlf -->   
        <property name="laf_name" type="string" length="60"  /> <!-- 1= class, 2= file of skin -->   
    
        <property name="render_rendering"  type="int" />
        <property name="render_dithering"  type="int" />
        <property name="render_interpolation"  type="int" />
        <property name="render_antialiasing"  type="int" />
        <property name="render_textantialiasing"  type="int" />
        <property name="render_colorrendering"  type="int" />
        <property name="render_fractionalmetrics"  type="int" />
    
        <property name="print_pdf_viewer_path"  type="string" length="100" />
        <property name="print_lunch_start_time"  type="int" />
        <property name="print_dinner_start_time"  type="int" />
        <property name="print_night_start_time"  type="int" />
        <property name="print_empty_value"  type="string" length="10" />
    
        <property name="color_scheme"  type="string" length="50" />
        */

    }



    /**
     * We save just config, schemes save must be called separately
     */
    public void saveConfigData()
    {
        m_da.getConfigurationManager().saveConfig();
        //editHibernate(m_da.getSettings().getSettings());
        // save config
        //DataAccess.notImplemented("GGCDb::saveConfigData()");
    }

    @SuppressWarnings("unchecked")
    private void loadColorSchemes(Session sess)
    {
        Hashtable<String, ColorSchemeH> table = new Hashtable<String, ColorSchemeH>();

        Query q = sess.createQuery("select pst from ggc.db.hibernate.ColorSchemeH as pst");

        Iterator it = q.iterate();

        while (it.hasNext())
        {
            ColorSchemeH eh = (ColorSchemeH)it.next();
            table.put(eh.getName(), eh);
        }

        m_da.getSettings().setColorSchemes(table, false);

    }

    /*
    private void saveColorSchemes(Session sess)
    {
        DataAccess.notImplemented("GGCDb::saveColorSchemes()");
    }
    */


    // *************************************************************
    // ****                METERS                       ****
    // *************************************************************

    /*
    public MeterH getMeterById(int id)
    {
        if (id<=0)
            return null;
        else
            return this.meters_full.get(""+id);
    }


    public MeterCompanyH getMeterCompanyById(int id)
    {
        for (int i=0; i<this.meter_companies.size(); i++)
        {
            if (meter_companies.get(i).getId()==id)
            {
                return meter_companies.get(i);
            }
        }

        return null;
    }
*/


    // *************************************************************
    // ****                NUTRITION DATA                       ****
    // *************************************************************

    @SuppressWarnings("unchecked")
    public ArrayList<FoodGroup> getUSDAFoodGroups()
    {

        ArrayList<FoodGroup> list = new ArrayList<FoodGroup>();

        Query q = getSession().createQuery("select pst from ggc.db.hibernate.FoodGroupH as pst");

        Iterator it = q.iterate();

        while (it.hasNext())
        {
            FoodGroupH eh = (FoodGroupH)it.next();
            list.add(new FoodGroup(eh));
        }

        return list;

    }

    @SuppressWarnings("unchecked")
    public ArrayList<FoodGroup> getUserFoodGroups()
    {

        ArrayList<FoodGroup> list = new ArrayList<FoodGroup>();

        Query q = getSession().createQuery("select pst from ggc.db.hibernate.FoodUserGroupH as pst");

        Iterator it = q.iterate();

        while (it.hasNext())
        {
            FoodUserGroupH eh = (FoodUserGroupH)it.next();
            list.add(new FoodGroup(eh));
        }

        return list;

    }
    
    @SuppressWarnings("unchecked")
    public ArrayList<MealGroup> getMealGroups()
    {

        ArrayList<MealGroup> list = new ArrayList<MealGroup>();

        Query q = getSession().createQuery("select pst from ggc.db.hibernate.MealGroupH as pst");

        Iterator it = q.iterate();

        while (it.hasNext())
        {
            MealGroupH eh = (MealGroupH)it.next();
            list.add(new MealGroup(eh));
        }

        return list;

    }
    
    
    @SuppressWarnings("unchecked")
    public ArrayList<FoodDescription> getUSDAFoodDescriptions()
    {

        ArrayList<FoodDescription> list = new ArrayList<FoodDescription>();

        Query q = getSession().createQuery("select pst from ggc.db.hibernate.FoodDescriptionH as pst");

        Iterator it = q.iterate();

        while (it.hasNext())
        {
            FoodDescriptionH eh = (FoodDescriptionH)it.next();
            list.add(new FoodDescription(eh));
        }

        return list;

    }


    @SuppressWarnings("unchecked")
    public ArrayList<FoodDescription> getUserFoodDescriptions()
    {

        ArrayList<FoodDescription> list = new ArrayList<FoodDescription>();

        Query q = getSession().createQuery("select pst from ggc.db.hibernate.FoodUserDescriptionH as pst order by pst.group_id, pst.name");

        Iterator it = q.iterate();

        while (it.hasNext())
        {
            FoodUserDescriptionH eh = (FoodUserDescriptionH)it.next();
            list.add(new FoodDescription(eh));
        }

        System.out.println("Loaded Food Descriptions: " + list.size()); 
        
        return list;

    }

    
    @SuppressWarnings("unchecked")
    //public Hashtable<String, Meal> getMeals()
    public ArrayList<Meal> getMeals()
    {

	//Hashtable<String, Meal> list = new Hashtable<String, Meal>();
        //ArrayList<FoodDescription> list = new ArrayList<FoodDescription>();
	ArrayList<Meal> list = new ArrayList<Meal>();

        Query q = getSession().createQuery("select pst from ggc.db.hibernate.MealH as pst order by pst.group_id, pst.name");

        Iterator it = q.iterate();

        while (it.hasNext())
        {
            MealH eh = (MealH)it.next();
            //list.put("" + eh.getId(), new Meal(eh));
            list.add(new Meal(eh));
        }

        return list;

    }
    
    
    
    @SuppressWarnings("unchecked")
    public void loadNutritionDefinitions()
    {
	
	System.out.println("loadNutritionDefinitions() !!!!!!!!!!!!!!!!!!!!!");

        Hashtable<String,NutritionDefinition> nut_defs = new Hashtable<String,NutritionDefinition>();
        ArrayList<SelectableInterface> nut_defs_lst = new ArrayList<SelectableInterface>();

        Query q = getSession().createQuery("select pst from ggc.db.hibernate.NutritionDefinitionH as pst");

        Iterator it = q.iterate();

        while (it.hasNext())
        {
            NutritionDefinitionH eh = (NutritionDefinitionH)it.next();

            NutritionDefinition fnd = new NutritionDefinition(eh);
            nut_defs.put("" + fnd.getId(), fnd);
            nut_defs_lst.add(fnd);
        }

        
        // static nutrition - not in database yet
        
	// GI = 4000, GL = 4001, GI_MIN = 4002, GI_MAX = 4003, 
        // GL_MIN = 4004, GL_MAX = 4005
        
        int[] ids = { 4000, 4001, 4002, 4003, 4004, 4005 };
        String[] tags = { "GI", "GL", "GI_MIN", "GI_MAX", "GL_MIN", "GL_MAX" };
        String[] units = { "gi", "gl", "gi", "gi", "gl", "gl" };
        String[] name = { "Glycemic Index", "Glycemic Load", "Glycemic Index (Min)", "Glycemic Index (Max)", "Glycemic Load (Min)", "Glycemic Load (Max)" }; 

        for(int i=0; i<ids.length; i++)
        {
            NutritionDefinitionH eh = new NutritionDefinitionH(units[i], tags[i], name[i], "0", 1);
            eh.setId(ids[i]);
            
            NutritionDefinition ndef = new NutritionDefinition(eh);
            
            nut_defs.put("" + eh.getId(), ndef);
            nut_defs_lst.add(ndef);
        }
        
        this.nutrition_defs = nut_defs;
        this.nutrition_defs_list = nut_defs_lst;

    }


    @SuppressWarnings("unchecked")
    public void loadHomeWeights()
    {

        Hashtable<String,NutritionHomeWeightType> nut_hw = new Hashtable<String,NutritionHomeWeightType>();
        ArrayList<SelectableInterface> nut_hw_lst = new ArrayList<SelectableInterface>();

        Query q = getSession().createQuery("select pst from ggc.db.hibernate.NutritionHomeWeightTypeH as pst");

        Iterator it = q.iterate();

        while (it.hasNext())
        {
            NutritionHomeWeightTypeH eh = (NutritionHomeWeightTypeH)it.next();

            NutritionHomeWeightType fnd = new NutritionHomeWeightType(eh);
            nut_hw.put(""+fnd.getId(), fnd);
            nut_hw_lst.add(fnd);
        }

        this.homeweight_defs = nut_hw;
        this.homeweight_defs_list = nut_hw_lst;

    }


    
    
    

/*
    public ArrayList<FoodHomeWeight> getFoodHomeWeight(long id)
    {

    ArrayList<FoodHomeWeight> list = new ArrayList<FoodHomeWeight>();

    Query q = getSession().createQuery("select pst from ggc.db.hibernate.FoodHomeWeightH as pst where pst.food_number=" + id);

    Iterator it = q.iterate();

    while (it.hasNext())
    {
        FoodHomeWeightH eh = (FoodHomeWeightH)it.next();
        list.add(new FoodHomeWeight(eh));
    }

    return list;

    }
*/



    // *************************************************************
    // ****                   GGC Main Data                     ****
    // ****      Comment: Were implemnted in DataBaseHandler    ****  
    // *************************************************************

    
    @SuppressWarnings("unchecked")
    public HbA1cValues getHbA1c(GregorianCalendar day)
    {
        //System.out.println("Hibernate: getHbA1c() B1 Stat:" + m_loadStatus);

        if (m_loadStatus == DB_CONFIG_LOADED)
            return null;

        if (db_debug)
            System.out.println("Hibernate: getHbA1c()");


        //System.out.println("Hibernate: getHbA1c()");

        HbA1cValues hbVal = new HbA1cValues();

        //System.out.println("getHbA1c(): readings: " + hbVal.getDayCount() + " " + hbVal.getReadings());

        try
        {
            GregorianCalendar gc1 = (GregorianCalendar)day.clone();
            gc1.add(Calendar.DAY_OF_MONTH, -6);

            SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
            String eDay = sdf.format(day.getTime()) + "2359";
            String sDay = sdf.format(gc1.getTime()) + "0000";

            Query q = getSession().createQuery("SELECT dv from " + 
                                               "ggc.db.hibernate.DayValueH as dv " +
                                               "WHERE dv.bg <> 0 AND dv.dt_info >=  " + 
                                               sDay + " AND dv.dt_info <= " + eDay + 
                                               " ORDER BY dv.dt_info");

            Iterator it = q.list().iterator();

            while (it.hasNext())
            {
                System.out.println("Found");
                DayValueH dv = (DayValueH)it.next();
                hbVal.addDayValueRow(new DailyValuesRow(dv));
            }

            hbVal.processDayValues();

            //System.out.println("getHbA1c(): readings: " +hbVal.getDayCount() + " " + hbVal.getReadings());

        }
        catch (Exception e)
        {
            System.out.println(e);
        }

        return hbVal;
    }


    @SuppressWarnings("unchecked")
    public DailyValues getDayStats(GregorianCalendar day)
    {

        if (m_loadStatus == DB_CONFIG_LOADED)
            return null;

        if (db_debug)
            System.out.println("Hibernate: getDayStats()");

        DailyValues dV = new DailyValues();

        try
        {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
            String sDay = sdf.format(day.getTime());

            Query q = getSession().createQuery("SELECT dv from " + 
                                               "ggc.db.hibernate.DayValueH as dv " +
                                               "WHERE dv.dt_info >=  " + 
                                               sDay + "0000 AND dv.dt_info <= " + sDay + 
                                               "2359 ORDER BY dv.dt_info");

            Iterator it = q.list().iterator();

            while (it.hasNext())
            {
                DayValueH dv = (DayValueH)it.next();

                DailyValuesRow dVR = new DailyValuesRow(dv);
                dV.setNewRow(dVR);
            }

        }
        catch (Exception e)
        {
            System.err.println(e);
        }

        return dV;
    }

    
    @SuppressWarnings("unchecked")
    public WeeklyValues getDayStatsRange(GregorianCalendar start, GregorianCalendar end)
    {

        if (m_loadStatus == DB_CONFIG_LOADED)
            return null;

        if (db_debug)
            System.out.println("Hibernate: getDayStatsRange()");

        WeeklyValues wv = new WeeklyValues();

        try
        {
            //System.out.println("Start " + start);

//x            SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
            String sDay = m_da.getDateTimeStringFromGregorianCalendar(start, 1);
            //sdf.format(start.getTime());
            String eDay = m_da.getDateTimeStringFromGregorianCalendar(end, 1);
            //sdf.format(end.getTime());

            if (db_debug)
                System.out.println("getDatStatsRange: "  + sDay + " - " + eDay);

            Query q = getSession().createQuery("SELECT dv from " + 
                                               "ggc.db.hibernate.DayValueH as dv " +
                                               "WHERE dv.dt_info >=  " + 
                                               sDay + "0000 AND dv.dt_info <= " + eDay + 
                                               "2359 ORDER BY dv.dt_info");

            Iterator it = q.list().iterator();

            while (it.hasNext())
            {
                DayValueH dv = (DayValueH)it.next();

                DailyValuesRow dVR = new DailyValuesRow(dv);
                wv.addDayValueRow(dVR);
            }

        }
        catch (Exception e)
        {
            System.err.println("getDayRange:" + e);
        }

        return wv;
    }


    @SuppressWarnings("unchecked")
    public MonthlyValues getMonthlyValues(int year, int month)
    {

        if (m_loadStatus == DB_CONFIG_LOADED)
            return null;

        if (db_debug)
            System.out.println("Hibernate: getMonthlyValues()");

        MonthlyValues mv = new MonthlyValues(year, month);

        try
        {
            String days = year + "" + m_da.getLeadingZero(month, 2);

            Query q = getSession().createQuery("SELECT dv from " + 
                                               "ggc.db.hibernate.DayValueH as dv " +
                                               "WHERE dv.dt_info >=  " + 
                                               days + "010000 AND dv.dt_info <= " + days + 
                                               "312359 ORDER BY dv.dt_info");

            Iterator it = q.list().iterator();

            while (it.hasNext())
            {
                DayValueH dv = (DayValueH)it.next();

                DailyValuesRow dVR = new DailyValuesRow(dv);
                mv.addDayValueRow(dVR);

                //dV.setNewRow(dVR);
            }

        }
        catch (Exception e)
        {
            System.err.println("getMonthlyValues:" + e);
        }

        return mv;

    }



    public void saveDayStats(DailyValues dV)
    {

        if (db_debug)
            System.out.println("Hibernate: saveDayStats()");


        if (dV.hasChanged())
        {
            if (db_debug)
                System.out.println("SDS: Has changed");

            Session sess = getSession();

            try
            {

                // deleted entries

                if (dV.hasDeletedItems())
                {
                    if (db_debug)
                        System.out.println("SDS: Deleted");

                    Transaction tx = sess.beginTransaction();

                    ArrayList<DayValueH> list = dV.getDeletedItems();
                    for (int i=0; i<list.size(); i++)
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
                        if (db_debug)
                            System.out.println("  New");

                        Transaction tx = sess.beginTransaction();

                        DayValueH dvh = dwr.getHibernateObject();
                        Long l = (Long)sess.save(dvh);

                        dvh.setId(l.longValue());
                        tx.commit();
                    }
                    else if (dwr.hasChanged())
                    {
                        Transaction tx = sess.beginTransaction();

                        if (db_debug)
                            System.out.println("  Changed");

                        DayValueH dvh = dwr.getHibernateObject();
                        sess.update(dvh);

                        tx.commit();
                    }

                } // for

            }
            catch (Exception ex)
            {
                System.out.println("saveDayStats: " + ex);
            }

        } // hasChanged
        else
        {
            if (db_debug)
                System.out.println("SDS: Has not changed");
        }

    }

    public boolean dateTimeExists(long datetime)
    {
        if (m_loadStatus == DB_CONFIG_LOADED)
            return false;

        if (db_debug)
            System.out.println("Hibernate: dateTimeExists()");

        try
        {
            Query q = getSession().createQuery("SELECT dv from " + 
                                               "ggc.db.hibernate.DayValueH as dv " +
                                               "WHERE dv.dt_info = " + datetime );

            return(q.list().size()==1);
        }
        catch (Exception e)
        {
            System.err.println(e);
            return false;
        }

    }


    // *************************************************************
    // ****                NUTRITION DATA                       ****
    // *************************************************************
    
    
    public ArrayList<SelectableInterface> getNutritionHomeWeights()
    {
	System.out.println("hw list: " + this.homeweight_defs_list.size());
	return this.homeweight_defs_list;
    }
    
    
    public ArrayList<SelectableInterface> getNutritionDefinitions()
    {
	return this.nutrition_defs_list;
    }
    

    // *************************************************************
    // ****                       U T I L S                     ****
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

        t = in.substring(0,1).toUpperCase();
        t += in.substring(1).toLowerCase();

        return t;

    }


    public void showByte(byte[] in)
    {

        for (int i=0;i<in.length; i++)
        {
            System.out.println((char)in[i] + " " + in[i]);
        }

    }



    public void debugOut(String source, Exception ex)
    {

        this.m_errorCode = 1;
        this.m_errorDesc = ex.getMessage();

        if (debug)
            System.out.println("  " + source + "::Exception: "+ex);

        if (debug)
            ex.printStackTrace();


    }


}


