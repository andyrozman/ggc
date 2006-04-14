package ggc.db.db_tool;

import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Properties;

import com.atech.db.tool.DatabaseSettings;
import com.atech.db.tool.DbToolApplicationInterface;

public class DbToolApplicationGGC implements DbToolApplicationInterface
{

    public int selected_db = 0;
    public String selected_lang = "en";


    Hashtable config_db_values = null;
    //String selected_LF_Class = null; // class
    //String selected_LF_Name = null; // name
    //String skinLFSelected = null;

    private Hashtable staticDatabases;
    private Hashtable customDatabases;
    private Hashtable allDatabases;


    public DbToolApplicationGGC()
    {
	this.staticDatabases = new Hashtable();
	this.customDatabases = new Hashtable();
	this.allDatabases = new Hashtable();
	initStaticDbs();
    }


    public void initStaticDbs()
    {
	// load all static database info
    }

    public String getApplicationName()
    {
	return "GNU Gluco Control";
    }	

    public String getApplicationDatabaseConfig()
    {
	return "../data/GGC_Config.properties";
    }

    public void loadConfig()
    {

	config_db_values = new Hashtable();

	try
	{
	    Properties props = new Properties();

	    FileInputStream in = new FileInputStream(getApplicationDatabaseConfig());
	    props.load(in);


	    for(Enumeration en = props.keys(); en.hasMoreElements(); )
	    {
		String  str = (String)en.nextElement();

		if (str.startsWith("DB")) 
		{
		    addDatabaseSetting(str, (String)props.get(str));
		    //config_db_values.put(str, (String)props.get(str));
		}
		else
		{
/*
		    if (str.equals("LF_NAME")) 
		    {
			selected_LF_Name = (String)props.get(str);
		    }
		    else if (str.equals("LF_CLASS")) 
		    {
			selected_LF_Class = (String)props.get(str);
		    }
		    else if (str.equals("SKINLF_SELECTED")) 
		    {
			skinLFSelected = (String)props.get(str);
		    } */
		    if (str.equals("SELECTED_DB")) 
		    {
			selected_db = Integer.parseInt((String)props.get(str));
		    }
		    else if (str.equals("SELECTED_LANG")) 
		    {
			selected_lang = (String)props.get(str);
		    }
		    else 
			System.out.println("DataAccess:loadConfig:: Unknown parameter : " + str);

		}

	    }

	    /*
	    ArrayList<String> list = new ArrayList<String>();

	    int count_db = 0;

	    list.add("0 - " + m_i18n.getMessage("INTERNAL_DATABASE"));
	    for (int i=1; i<20; i++) 
	    {
		if (config_db_values.containsKey("DB"+i+"_CONN_NAME")) 
		{
		    count_db++;
		    list.add(i+" - " + config_db_values.get("DB"+i+"_CONN_NAME"));
		}

		if ((count_db*6)>=config_db_values.size()) 
		    break;

	    }

	    Iterator it = list.iterator();

	    int j=0;
	    allDbs = new String[list.size()];

	    while (it.hasNext()) 
	    {
		String val = (String)it.next();
		allDbs[j] = val;
		j++;
	    }
	    */

	}
	catch(Exception ex)
	{
	    System.out.println("DataAccess::loadConfig::Exception> " + ex);
	}

    }


    public void saveConfig()
    {

	try
	{

	    //Properties props = new Properties();
	    BufferedWriter bw = new BufferedWriter(new FileWriter(getApplicationDatabaseConfig()));

	    bw.write("#\n" +
		     "# GGC_Config (Settings for GGC)\n" +
		     "#\n"+
		     "# Don't edit by hand\n" +
		     "# Only settings need for application startup are written here. All other info\n"+
		     "#    is stored in database\n" +
		     "#\n\n"+
		     "#\n# Databases settings\n#\n");


	    int count_db = 0;

	    //for (int i=0; i<this.allDatabases.size(); i++)  fix, only non-static db data should be written
	    for (int i=0; i<this.allDatabases.size(); i++) 
	    {
		DatabaseSettings dbs = (DatabaseSettings)this.allDatabases.get(""+i);
		dbs.write(bw);
	    }

/*
	    bw.write("\n\n#\n# Look and Feel Settings\n#\n\n");
	    bw.write("LF_NAME=" + selected_LF_Name +"\n");

	    //props.put("LF_NAME", selected_LF_Name);

	    selected_LF_Class = availableLF_full.get(selected_LF_Name);

	    bw.write("LF_CLASS=" + selected_LF_Class +"\n");

	    //props.put("LF_CLASS", selected_LF_Name);
	    bw.write("SKINLF_SELECTED=" + skinLFSelected +"\n");
	    //props.put("SKINLF_SELECTED", skinLFSelected); */


	    bw.write("\n\n#\n# Db Selector\n#\n");
	    bw.write("SELECTED_DB=" + selected_db +"\n");

	    bw.write("\n\n#\n# Language Selector\n#\n");
	    bw.write("SELECTED_LANG=" + selected_lang +"\n");

	    bw.close();

	}
	catch(Exception ex)
	{
	    System.out.println("DataAccess::saveConfig::Exception> " + ex);
	    ex.printStackTrace();
	}

    }



    public int getFirstAvailableDatabase()
    {
	return 1;
    }

    public Hashtable getStaticDatabases()
    {
	return this.staticDatabases; 
    }


    public Hashtable getCustomDatabases()
    {
	return this.customDatabases;
    }

    public Hashtable getAllDatabases()
    {
	return this.allDatabases;
    }

    public DatabaseSettings getDatabase(int index)
    {
	return null;
    }

    public DatabaseSettings getSelectedDatabase()
    {
	return null;
    }


    public void addDatabaseSetting(String setting, String value)
    {
	int dbnum = Integer.parseInt(setting.substring(2,3));

//	if (dbnum<this.getFirstAvailableDatabase()) 
//	    return;

	if (this.customDatabases.containsKey(""+dbnum)) 
	{
	    // we have database
	    DatabaseSettings dbs = (DatabaseSettings)this.customDatabases.get(""+dbnum);
	    addDatabaseSetting(dbs, setting, value);
	}
	else
	{
	    // new database
	    DatabaseSettings dbs = new DatabaseSettings();
	    dbs.number = dbnum;
	    addDatabaseSetting(dbs, setting, value);
	    this.customDatabases.put(""+dbnum, dbs);
	    this.allDatabases.put(""+dbnum, dbs);
	}

	//System.out.println(dbnum);

    }


    public void addDatabaseSetting(DatabaseSettings ds, String setting, String value)
    {
	String sett = setting.substring(setting.indexOf("_")+1);

	//System.out.println(sett);

	if (sett.equals("CONN_NAME")) 
	{
	    ds.name = value;
	}
	else if (sett.equals("DB_NAME"))
	{
	    ds.db_name = value;
	}
	else if (sett.equals("CONN_DRIVER")) 
	{
	    ds.driver = value;
	}
	else if (sett.equals("CONN_URL")) 
	{
	    ds.url = value;
	}
	else if (sett.equals("HIBERNATE_DIALECT"))
	{
	    ds.dialect = value;
	}
	else if (sett.equals("CONN_USERNAME"))
	{
	    ds.username = value;
	}
	else if (sett.equals("CONN_PASSWORD")) 
	{
	    ds.password = value;
	}
	else if (sett.equals("CONN_DRIVER_CLASS")) 
	{
	    ds.driver_class = value;
	}
	else 
	{
	    System.out.println("Unknown DB keyword in config: " + sett);
	}

    }


    public void test()
    {
	/*
	ArrayList list = new ArrayList();
	int num = (int)(config_db_values.size()/7);

	for (int i=0; i<num; i++)
	{
	    DatabaseSettings ds = new DatabaseSettings();
	    ds.number = i;
	    ds.name = (String)config_db_values.get("DB" +i +"_CONN_NAME");
	    ds.db_name = (String)config_db_values.get("DB" +i +"_DB_NAME");
	    ds.driver = (String)config_db_values.get("DB" +i +"_CONN_DRIVER");
	    ds.url = (String)config_db_values.get("DB" +i +"_CONN_URL");
	    //ds.port = config_db_values.get("DB" +i +"_CONN_NAME");
	    ds.dialect = (String)config_db_values.get("DB" +i +"_HIBERNATE_DIALECT");

	    ds.username = (String)config_db_values.get("DB" +i +"_CONN_USERNAME");
	    ds.password = (String)config_db_values.get("DB" +i +"_CONN_PASSWORD");

	    if (this.selected_db==i)
	    {
		ds.isDefault = true;
	    }

	    list.add(ds);
	}

	return list;

	*/
    }



    public String toString()
    {
	return getApplicationName();
    }


    public static void main(String args[])
    {
	DbToolApplicationGGC apl = new DbToolApplicationGGC();
	apl.loadConfig();
	apl.saveConfig();
    }


}
