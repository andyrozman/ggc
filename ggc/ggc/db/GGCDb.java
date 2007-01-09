/*
 *  GGC - GNU Gluco Control
 *
 *  A pure java app to help you manage your diabetes.
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
 *  Author:   andyrozman
 */

// WORK IN PROGRESS, PLEASE DO NOT TOUCH
// 
// Methods are added to this class, whenever we make changes to our existing database,
// either add methods for handling data or adding new tables.
// 
// andyrozman


package ggc.db;

import java.io.FileInputStream;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Enumeration;
import java.util.GregorianCalendar;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Properties;
import java.util.StringTokenizer;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory; 
import org.hibernate.Hibernate;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;
import org.hibernate.tool.hbm2ddl.*;

import ggc.GGC;
import ggc.data.DailyValues;
import ggc.data.DailyValuesRow;
import ggc.data.HbA1cValues;
import ggc.data.MonthlyValues;
import ggc.data.WeeklyValues;
import ggc.db.datalayer.FoodDescription;
import ggc.db.datalayer.FoodGroup;
import ggc.db.hibernate.ColorSchemeH;
import ggc.db.hibernate.DatabaseObjectHibernate;
import ggc.db.hibernate.DayValueH;
import ggc.db.hibernate.FoodDescriptionH;
import ggc.db.hibernate.FoodGroupH;
import ggc.db.hibernate.SettingsMainH;
import ggc.gui.nutrition.GGCTreeRoot;
import ggc.util.DataAccess;


public class GGCDb
{
    public static final int DB_CONFIG_LOADED = 1;
    public static final int DB_INITIALIZED = 2;
    public static final int DB_STARTED = 3;

    private boolean debug = true;

    private static Log s_logger = LogFactory.getLog(GGCDb.class); 
    private Session m_session = null;
    private SessionFactory sessions = null;
    private int m_errorCode = 0;
    private String m_errorDesc = "";
    private String m_addId = "";


    private Configuration m_cfg = null;
    private DataAccess m_da; 

    private int m_loadStatus = 0;


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
        m_cfg = getConfiguration();
        m_da = da;
        m_loadStatus = DB_CONFIG_LOADED;
    }


    public GGCDb()
    {
        m_cfg = getConfiguration();
        m_loadStatus = DB_CONFIG_LOADED;
    }



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
        if (db_hib_dialect.equals("org.hibernate.dialect.HSQLDialect"))
        {
            try
            {
                getSession().connection().createStatement().execute("SHUTDOWN");
            }
            catch(Exception ex)
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


    public void loadStaticData()
    {
        m_da.m_nutrition_treeroot = new GGCTreeRoot(1, this);
        m_loadStatus = DB_STARTED;
    }


    public void displayError(String source, Exception ex)
    {

        System.out.println("Exception ["+ source + "]: " + ex);

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



    //
    //  BASIC METHODS
    //



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
            catch(SQLException ex)
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
            catch(Exception ex)
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

        if (debug)
            System.out.println("addHibernate::" + obj.toString());

        try
        {
            Session sess = getSession();
            Transaction tx = sess.beginTransaction();

            Long val = (Long)sess.save(obj);
            tx.commit();

            return val.longValue();
        }
        catch(Exception ex)
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
            catch(Exception ex)
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
	catch(Exception ex)
	{
	    System.out.println("Exception on editHibernate: " + ex);
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
            catch(Exception ex)
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
            catch(Exception ex)
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


    public Configuration getConfiguration()
    {


        try
        {

            Properties props = new Properties();

            FileInputStream in = new FileInputStream("../data/GGC_Config.properties");
            props.load(in);

	    //System.out.println(in);


            db_num = new Integer(props.getProperty("SELECTED_DB"));
            db_conn_name = props.getProperty("DB"+db_num+"_CONN_NAME");

	    System.out.println("Loading Db Configuration #"+ db_num + ": " + db_conn_name);

            db_hib_dialect = props.getProperty("DB"+db_num+"_HIBERNATE_DIALECT");


            db_driver_class = props.getProperty("DB"+db_num+"_CONN_DRIVER_CLASS");
            db_conn_url = props.getProperty("DB"+db_num+"_CONN_URL");
            db_conn_username = props.getProperty("DB"+db_num+"_CONN_USERNAME");
            db_conn_password = props.getProperty("DB"+db_num+"_CONN_PASSWORD");


            Configuration cfg = new Configuration()
                            .addResource("GGC_Nutrition.hbm.xml")
                            .addResource("GGC_Main.hbm.xml")
                            //.addResource("InternalData.hbm.xml")
			    //.addResource("ParishData.hbm.xml")
                            //.addResource("Person.hbm.xml")
                            //.addResource("Planner.hbm.xml")
                            //.addResource("Misc.hbm.xml")

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

            in.close();

            return cfg;
        }
        catch (Exception ex)
        {
            ex.printStackTrace();
        }
        return null;
    }


    // *************************************************************
    // ****                     SETTINGS                        ****
    // *************************************************************

    /**
     * We load all config data (including schemes)
     */
    public void loadConfigData()
    {
        Session sess = getSession();
        SettingsMainH seti = (SettingsMainH)sess.get(SettingsMainH.class, new Long(1));

        // load schemes
        loadColorSchemes(sess);

        // sets settings
        m_da.getSettings().setSettings(seti);

        // sets active color scheme
        m_da.getSettings().setColorSchemeObject(seti.getColor_scheme());
    }


    /**
     * We save just config, schemes save must be called separately
     */
    public void saveConfigData()
    {
        editHibernate(m_da.getSettings().getSettings());
        // save config
        //DataAccess.notImplemented("GGCDb::saveConfigData()");
    }

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

    private void saveColorSchemes(Session sess)
    {
        DataAccess.notImplemented("GGCDb::saveColorSchemes()");
    }



    // *************************************************************
    // ****                NUTRITION DATA                       ****
    // *************************************************************
    
    public ArrayList getFoodGroups()
    {

        ArrayList<FoodGroupH> list = new ArrayList<FoodGroupH>();

        Query q = getSession().createQuery("select pst from ggc.db.hibernate.FoodGroupH as pst");

        Iterator it = q.iterate();

        while (it.hasNext())
        {
            FoodGroupH eh = (FoodGroupH)it.next();
            list.add(new FoodGroup(eh));
        }

        return list;

    }


    public ArrayList<FoodDescription> getFoodDescriptions()
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



    // *************************************************************
    // ****                   GGC Main Data                     ****
    // ****      Comment: Were implemnted in DataBaseHandler    ****  
    // *************************************************************


    public HbA1cValues getHbA1c(GregorianCalendar day)
    {
	//System.out.println("Hibernate: getHbA1c() B1 Stat:" + m_loadStatus);

    	if (m_loadStatus == DB_CONFIG_LOADED)
    	    return null;
    
    	System.out.println("Hibernate: getHbA1c()");
    	HbA1cValues hbVal = new HbA1cValues();

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
            DayValueH dv = (DayValueH)it.next();
            hbVal.addDayValueRow(new DailyValuesRow(dv));
	    }

	    hbVal.processDayValues();

	} 
	catch (Exception e) 
	{
	    System.out.println(e);
	}

	return hbVal;
    }



    public DailyValues getDayStats(GregorianCalendar day)
    {

    	if (m_loadStatus == DB_CONFIG_LOADED)
    	    return null;

        if (debug)
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


    public WeeklyValues getDayStatsRange(GregorianCalendar start, GregorianCalendar end)
    {

        if (m_loadStatus == DB_CONFIG_LOADED)
            return null;

        if (debug)
            System.out.println("Hibernate: getDayStatsRange()");

        WeeklyValues wv = new WeeklyValues();
            
        try 
        {
            //System.out.println("Start " + start);

            SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
            String sDay = m_da.getDateTimeStringFromGregorianCalendar(start, 1);
            //sdf.format(start.getTime());
            String eDay = m_da.getDateTimeStringFromGregorianCalendar(end, 1);
            //sdf.format(end.getTime());

            if (debug)
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



    public MonthlyValues getMonthlyValues(int year, int month)
    {

        if (m_loadStatus == DB_CONFIG_LOADED)
            return null;

        if (debug)
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

        System.out.println("Hibernate: saveDayStats()");


        if (dV.hasChanged()) 
        {
            if (debug)
                System.out.println("SDS: Has changed");

            Session sess = getSession();

            try
            {

                // deleted entries

                if (dV.hasDeletedItems()) 
                {
                    if (debug)
                        System.out.println("SDS: Deleted");

                    Transaction tx = sess.beginTransaction();

                    ArrayList list = dV.getDeletedItems();
                    for (int i=0; i<list.size(); i++) 
                    {
                        DayValueH d = (DayValueH)list.get(i);
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
                        if (debug)
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

                        if (debug)
                            System.out.println("  Changed");

                        DayValueH dvh = dwr.getHibernateObject();
                        sess.update(dvh);

                        tx.commit();
                    }

                } // for

            }
            catch(Exception ex)
            {
                System.out.println("saveDayStats: " + ex);
            }

        } // hasChanged
        else
        {
            if (debug)
                System.out.println("SDS: Has not changed");
        }

    }

    public boolean dateTimeExists(long datetime)
    {
        if (m_loadStatus == DB_CONFIG_LOADED)
            return false;

        if (debug)
            System.out.println("Hibernate: dateTimeExists()");

        DailyValues dV = new DailyValues();

        try 
        {
            Query q = getSession().createQuery("SELECT dv from " + 
                               "ggc.db.hibernate.DayValueH as dv " +
                               "WHERE dv.dt_info = " + datetime );

            return (q.list().size()==1);
        } 
        catch (Exception e) 
        {
            System.err.println(e);
            return false;
        }

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


