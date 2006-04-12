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


package ggc.db.datalayer;

import java.io.FileInputStream;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.GregorianCalendar;
import java.util.Enumeration;
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
import ggc.datamodels.DailyValues;
import ggc.datamodels.WeekValues;
import ggc.datamodels.DailyValuesRow;
import ggc.datamodels.HbA1cValues;
import ggc.db.DataBaseHandler;
import ggc.db.datalayer.FoodDescription;
import ggc.db.datalayer.FoodGroup;
import ggc.db.hibernate.DatabaseObjectHibernate;
import ggc.db.hibernate.DayValueH;
import ggc.db.hibernate.FoodDescriptionH;
import ggc.db.hibernate.FoodGroupH;
import ggc.nutrition.GGCTreeRoot;
import ggc.util.DataAccess;


public class GGCDb
{

    private boolean debug = true;

    private static Log logger = LogFactory.getLog(GGCDb.class); 
    private Session m_session = null;
    private SessionFactory sessions = null;
    private int m_errorCode = 0;
    private String m_errorDesc = "";
    private String m_addId = "";


    Configuration m_cfg = null;
    DataAccess m_da; 

    public int m_loadStatus = 0;


    // ---
    // ---  DB Settings
    // ---
    public int db_num = 0;
    public String db_hib_dialect = null; 
    public String db_driver_class = null;
    public String db_conn_name = null;
    public String db_conn_url = null;
    public String db_conn_username = null;
    public String db_conn_password = null;


    public GGCDb(DataAccess da)
    {
	m_cfg = getConfiguration();
        m_da = da;
	m_loadStatus = 1;
    }


    public GGCDb()
    {
	m_cfg = getConfiguration();
	m_loadStatus =1;
    }



    public void initDb()
    {
        openHibernateSimple();
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
    }


    public void openHibernateSimple()
    {
        sessions = m_cfg.buildSessionFactory();
        m_session = sessions.openSession();
	m_loadStatus = 2;
    }


    public int getLoadStatus()
    {
	return m_loadStatus;
    }


    public void loadStaticData()
    {
        m_da.m_nutrition_treeroot = new GGCTreeRoot(1, this);
	m_loadStatus = 3;
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


    public boolean addForce(Object obj)
    {

        if (debug)
            System.out.println("addForce::" + obj.toString());

        try
        {
            Session sess = getSession();
            Transaction tx = sess.beginTransaction();

            sess.save(obj);

            tx.commit();

            return true;
        }
        catch(Exception ex)
        {
            //setError(1, ex.getMessage(), doh.getObjectName());
            System.out.println("Exception on add: " + ex);
            ex.printStackTrace();
            return false;
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


            db_num = Integer.parseInt((String)props.get("SELECTED_DB"));
            db_conn_name = (String)props.get("DB"+db_num+"_CONN_NAME");

	    System.out.println("Loading Db Configuration #"+ db_num + ": " + db_conn_name);

            db_hib_dialect = (String)props.get("DB"+db_num+"_HIBERNATE_DIALECT");


            db_driver_class = (String)props.get("DB"+db_num+"_CONN_DRIVER_CLASS");
            db_conn_url = (String)props.get("DB"+db_num+"_CONN_URL");
            db_conn_username = (String)props.get("DB"+db_num+"_CONN_USERNAME");
            db_conn_password = (String)props.get("DB"+db_num+"_CONN_PASSWORD");


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
                            .setProperty("hibernate.use_outer_join", "true")
//	      .setProperty("hibernate.show_sql", "true")
                            .setProperty("hibernate.c3p0.min_size", "5")
                            .setProperty("hibernate.c3p0.max_size", "20")
                            .setProperty("hibernate.c3p0.timeout", "1800")
                            .setProperty("hibernate.c3p0.max_statements", "50");

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

    public void loadConfigData()
    {
    }

    public void saveConfigData()
    {

    }

    public void saveColorSchemes()
    {
    }

    public void saveConfigToFile()
    {
    }


    // *************************************************************
    // ****                NUTRITION DATA                       ****
    // *************************************************************
    
    public ArrayList getFoodGroups()
    {

	ArrayList list = new ArrayList();

	Query q = getSession().createQuery("select pst from ggc.db.hibernate.FoodGroupH as pst");

	Iterator it = q.iterate();

	while (it.hasNext())
	{
	    FoodGroupH eh = (FoodGroupH)it.next();
	    list.add(new FoodGroup(eh));
	}

	return list;

    }


    public ArrayList getFoodDescriptions()
    {

	ArrayList list = new ArrayList();

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

	if (m_loadStatus<2)
	    return null;

	System.out.println("Hibernate: getHbA1c()");
	HbA1cValues hbVal = new HbA1cValues();

	try 
	{
            GregorianCalendar gc1 = (GregorianCalendar)day.clone();
            gc1.add(GregorianCalendar.DAY_OF_MONTH, -7);

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
		hbVal.addDay(dv.getBg(), 1);
	    }

	} 
	catch (Exception e) 
	{
	    System.out.println(e);
	}

	return hbVal;
    }



    public DailyValues getDayStats(GregorianCalendar day)
    {

	if (m_loadStatus<2)
	    return null;

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


    public WeekValues getDayStatsRange(GregorianCalendar start, GregorianCalendar end)
    {

	if (m_loadStatus<2)
	    return null;

	System.out.println("Hibernate: getDayStatsRange()");

        WeekValues wv = new WeekValues();
            
	//DailyValues dV = new DailyValues();

	try 
	{
            //System.out.println("Start " + start);

	    SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
	    String sDay = m_da.getDateTimeStringFromGregorianCalendar(start, 1);
                //sdf.format(start.getTime());
	    String eDay = m_da.getDateTimeStringFromGregorianCalendar(end, 1);
                //sdf.format(end.getTime());

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
		//dV.setNewRow(dVR);
	    }

	} 
	catch (Exception e) 
	{
	    System.err.println("gteDayRange:" + e);
	}

	return wv;
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


