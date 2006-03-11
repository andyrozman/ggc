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

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Properties;
import java.util.StringTokenizer;

import java.io.FileInputStream;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory; 
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;
import org.hibernate.tool.hbm2ddl.*;


import ggc.db.hibernate.DatabaseObjectHibernate;
import ggc.db.datalayer.FoodGroup;
import ggc.db.datalayer.FoodDescription;
import ggc.db.hibernate.FoodGroupH;
import ggc.db.hibernate.FoodDescriptionH;



public class GGCDb
{

    private boolean debug = false;

    private static Log logger = LogFactory.getLog(GGCDb.class); 
    private Session m_session = null;
    private SessionFactory sessions = null;
    private int m_errorCode = 0;
    private String m_errorDesc = "";
    private String m_addId = "";


    Configuration m_cfg = null;


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


    public GGCDb()
    {
	m_cfg = getConfiguration();
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

            FileInputStream in = new FileInputStream("./GGC_Config.properties");
            props.load(in);

	    System.out.println(in);


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
                            //.addResource("Config.hbm.xml")
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
    // ****                   E V E N T S                       ****
    // *************************************************************

/*
    public ArrayList<Selectable> getPublicEvents()
    {

        ArrayList<Selectable> list = new ArrayList<Selectable>();

        Query q = getSession().createQuery("select pst from com.atech.inf_sys.zisdb.db.EventsH as pst where pst.type<19");

        Iterator it = q.iterate();

        while (it.hasNext())
        {
            EventsH eh = (EventsH)it.next();
            list.add(new Events(eh));
        }

        return list;

    }

    public ArrayList<Selectable> getEventsByType(int type)
    {

        ArrayList<Selectable> list = new ArrayList<Selectable>();

        Query q = getSession().createQuery("select pst from com.atech.inf_sys.zisdb.db.EventsH as pst where pst.type=" + type);

        Iterator it = q.iterate();

        while (it.hasNext())
        {
            EventsH eh = (EventsH)it.next();
            list.add(new Events(eh));
        }

        return list;

    }




    public ArrayList<Selectable> getEventPerformers()
    {
    //    DataAccess.notImplemented("ZISDb::getEventPerformers");


        ArrayList<Selectable> list = new ArrayList<Selectable>();
        String sql ="select pst from com.atech.inf_sys.zisdb.db.internal.InternalPersonH as pst where pst.dead=0 and (pst.type<=2 or pst.type>=6)";


        //for (int i=0; i<search.length; i++) 
        {

            Query q = getSession().createQuery(sql);

            Iterator it = q.iterate();

            while (it.hasNext())
            {
                InternalPersonH ob = (InternalPersonH)it.next();

                //if (ob instanceof PriestH) 
                {
                    list.add(new InternalPerson(ob));
                }
                //else if (ob instanceof FriarH) 
                //{
                //    list.add(new PersonEventPerformer((FriarH)ob));
                //}
            }


        }

        return list;


//        return null;
    }



    public ArrayList<Selectable> getSelectedEventPerformers()
    {

        return this.m_sel_pperson;

    }



    public ArrayList<PersonEvent> getEventPersons(long id)
    {

        ArrayList<PersonEvent> pcs = new ArrayList<PersonEvent>();


        if (id<=0)
        {
            System.out.println("Event is not set. Search stopped");
            return pcs;
        }

        Query q = getSession().createQuery("select pst from com.atech.inf_sys.zisdb.db.person.PersonEventH as pst where pst.event.id=" + id);

        if (q.list().size()>0)
        {

            Iterator it = q.iterate();

            while (it.hasNext())
            {

                PersonEventH pr = (PersonEventH)it.next();

                pcs.add(new PersonEvent(pr));

            }

        }

        System.out.println("Found " + pcs.size() + " person's for event.");

        return pcs;

    }



    public ArrayList<PersonEvent> getReligiousPersonalEvents(long person_id)
    {
        ArrayList<PersonEvent> pcs = new ArrayList<PersonEvent>();

        if(person_id <= 0)
        {
            System.out.println("Person is not set. Search stopped");
            return pcs;
        }

        //System.out.println((new StringBuilder()).append("Person ID: ").append(person_id).toString());

        Query q = getSession().createQuery("select pst from com.atech.inf_sys.zisdb.db.person.PersonEventH as pst where pst.person.id=" + person_id + " and pst.type<20 order by pst.type asc");

        if(q.list().size() > 0)
        {

            Iterator it = q.iterate();

            while (it.hasNext()) 
            {
                pcs.add(new PersonEvent((PersonEventH)it.next()));
            }

        }

        System.out.println("Found "+"(" + q.list().size() +") person's events (Religious).");
        return pcs;

    }



    public ArrayList<PersonEvent> getCivilPersonalEvents(long person_id)
    {

        ArrayList<PersonEvent> pcs = new ArrayList<PersonEvent>();

        if(person_id <= 0)
        {
            System.out.println("Person is not set. Search stopped");
            return pcs;
        }

        Query q = getSession().createQuery("select pst from com.atech.inf_sys.zisdb.db.person.PersonEventH as pst where pst.person.id=" + person_id + " and pst.type>19 order by pst.type asc");


        if(q.list().size() > 0)
        {

            Iterator it = q.iterate();

            while (it.hasNext()) 
            {
                pcs.add(new PersonEvent((PersonEventH)it.next()));
            }

        }

        System.out.println("Found "+"(" + q.list().size() +") person's events (civil).");

        return pcs;

    }





    public PersonEvent getPersonalEvent(long person_id, long event_id)
    {

        //ArrayList<PersonEvent> pcs = new ArrayList<PersonEvent>();

        if ((person_id<=0) || (event_id<=0))
        {
            //System.out.println("Person is not set. Search stopped");
            return null;
        }

        //PersonEventH pp = new PersonEventH();
        

        Query q = getSession().createQuery("select pst from com.atech.inf_sys.zisdb.db.person.PersonEventH as pst where pst.person.id=" + person_id + " and pst.event.id=" +  event_id);


        if (q.list().size() > 0)
        {

            Iterator it = q.iterate();

            while (it.hasNext()) 
            {
                return new PersonEvent((PersonEventH)it.next());
            }

        }

        //System.out.println("Found "+"(" + q.list().size() +") person's events (civil).");

        //return pcs;

        return null;

    }


    public PersonEvent getPersonalEventWithoutPerson(long person_id, long event_id)
    {

        //ArrayList<PersonEvent> pcs = new ArrayList<PersonEvent>();

        if(person_id <= 0)
        {
            //System.out.println("Person is not set. Search stopped");
            return null;
        }

        //PersonEventH pp = new PersonEventH();


        Query q = getSession().createQuery("select pst from com.atech.inf_sys.zisdb.db.person.PersonEventH as pst where pst.event.id=" +  event_id);


        if (q.list().size() > 0)
        {

            Iterator it = q.iterate();

            while (it.hasNext()) 
            {
                PersonEventH pe = (PersonEventH)it.next();

                if (pe.getPerson().getId()!=person_id)
                {
                    return new PersonEvent(pe);
                }
            }

        }

        //System.out.println("Found "+"(" + q.list().size() +") person's events (civil).");

        //return pcs;

        return null;

    }






    // type = 1, normal events, 2 = marridge(s)
/*
    public ArrayList<PersonEvent> getEventByPerson(long id, int type)
    {

        ArrayList<PersonEvent> pcs = new ArrayList<PersonEvent>();


        if (id<=0)
        {
            System.out.println("Event is not set. Search stopped");
            return pcs;
        }

        Query q = getSession().createQuery("select pst from com.atech.inf_sys.zisdb.db.person.PersonEventH as pst where pst.event_id=" + id);

        if (q.list().size()>0)
        {

            Iterator it = q.iterate();

            while (it.hasNext())
            {

                PersonEventH pr = (PersonEventH)it.next();

                pcs.add(new PersonEvent(pr));

            }

        }

        System.out.println("Found " + pcs.size() + " person's for event.");

        return pcs;

    }
*/







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


