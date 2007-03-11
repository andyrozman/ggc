package ggc.db.tool;

import java.io.BufferedWriter;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.GregorianCalendar;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Properties;

import javax.swing.UIManager;

import com.atech.db.tool.DatabaseSettings;
import com.atech.db.tool.DbToolApplicationInterface;

/*
New methods :
    public String[] getAllDatabasesNamesPlusAsArray()
    public String[] getAllDatabasesNamesAsArray()
    public int getSelectedDatabaseIndex()

    //setChanged();
    //getChanged();

    hasChanged();
    setSelectedDatabaseIndex(int);

*/


public class DbToolApplicationGGC implements DbToolApplicationInterface
{

    public int selected_db = 0;
    public String selected_lang = "en";


    Hashtable config_db_values = null;


    // LF
    String selected_LF_Class = "com.l2fprod.gui.plaf.skin.SkinLookAndFeel"; // class
    String selected_LF_Name = "SkinLF"; // name
    String skinLFSelected = "blueMetalthemepack.zip";

    Object[] availableLF = null;
    Object[] availableLFClass = null;
    Hashtable<String,String> availableLF_full = null;
    int skinlf_LF = 0;


    private Hashtable<String, DatabaseSettings> staticDatabases;
    private Hashtable<String, DatabaseSettings> customDatabases;
    private Hashtable<String, DatabaseSettings> allDatabases;

    private boolean m_changed = false;


    public DbToolApplicationGGC()
    {
	this.staticDatabases = new Hashtable<String, DatabaseSettings>();
	this.customDatabases = new Hashtable<String, DatabaseSettings>();
	this.allDatabases = new Hashtable<String, DatabaseSettings>();
	initStaticDbs();
	loadAvailableLFs();
    }



    public void loadAvailableLFs()
    {

	availableLF_full = new Hashtable<String,String>();
	UIManager.LookAndFeelInfo[] info = UIManager.getInstalledLookAndFeels();

	availableLF = new Object[info.length+1];
	availableLFClass = new Object[info.length+1];

	//ring selectedLF = null;
	//String subSelectedLF = null;

	int i;
	for (i=0; i<info.length; i++) 
	{
	    String name = info[i].getName();
	    String className = info[i].getClassName();

	    availableLF_full.put(name, className);
	    availableLF[i] = name;
	    availableLFClass[i] = className;

	    //System.out.println(humanReadableName);
	}     

	availableLF_full.put("SkinLF", "com.l2fprod.gui.plaf.skin.SkinLookAndFeel");
	availableLF[i] = "SkinLF";
	availableLFClass[i] = "com.l2fprod.gui.plaf.skin.SkinLookAndFeel";
	skinlf_LF = i;


    }


    public Object[] getAvailableLFs()
    {
	return availableLF;
    }

    public Object[] getAvailableLFsClass()
    {
	return this.availableLFClass;
    }


    public int getSelectedLFIndex()
    {
	for(int i=0; i<this.availableLFClass.length; i++)
	{
	    if (this.availableLFClass[i].equals(this.selected_LF_Class))
	    {
		return i;
	    }
	}

	return this.skinlf_LF;

    }


    public void setSelectedLF(int index, String skin)
    {

	if (this.getSkinLFIndex()!=index)
	{
	    this.selected_LF_Class = (String)this.availableLFClass[index]; // class
	    this.selected_LF_Name = (String)this.availableLF[index]; // name
	    this.m_changed = true;
	}

	if (!skin.equals(this.skinLFSelected))
	{
	    this.skinLFSelected = skin;
	    this.m_changed = true;
	}

    }



    public String getSelectedLFSkin()
    {
	return this.skinLFSelected;
    }


    public int getSkinLFIndex()
    {
	return this.skinlf_LF;
    }


    public boolean isSkinLFSelected()
    {
	return isSkinLFSelected(getSelectedLFIndex());
    }


    public boolean isSkinLFSelected(int index)
    {
	return (this.skinlf_LF == index);
    }


    private void setDefaultLF()
    {
	this.selected_LF_Class = "com.l2fprod.gui.plaf.skin.SkinLookAndFeel"; // class
	this.selected_LF_Name = "SkinLF"; // name
	this.skinLFSelected = "blueMetalthemepack.zip";

	this.m_changed = true;
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

	Properties props = new Properties();

	boolean config_loaded = true;

	try
	{
	    FileInputStream in = new FileInputStream(getApplicationDatabaseConfig());
	    props.load(in);
	}
	catch(Exception ex)
	{
	    config_loaded = false;
	}


	if (config_loaded)
	{

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
			//System.out.println("!!!!!!!!!!!!!!!!! " + (String)props.get(str));
			skinLFSelected = (String)props.get(str);
		    } 
		    else if (str.equals("SELECTED_DB")) 
		    {
			selected_db = Integer.parseInt((String)props.get(str));
		    }
		    else if (str.equals("SELECTED_LANG")) 
		    {
			selected_lang = (String)props.get(str);
		    }
		    else 
			System.out.println("DbToolApplicationGGC:loadConfig:: Unknown parameter : '" + str +"'");

		}

	    }

	}
	else
	{

	    // we don't have config, we try to create basic one

	    System.out.println("DbToolApplicationGGC: Config file not found. Creating new config file with default settings.");

	    try
	    {
		addDatabaseSetting("DB0_CONN_NAME", "Internal Database");
		addDatabaseSetting("DB0_DB_NAME", "HypersonicSQL File");
		addDatabaseSetting("DB0_CONN_DRIVER_CLASS", "org.hsqldb.jdbcDriver");
		addDatabaseSetting("DB0_CONN_URL", "jdbc:hsqldb:file:../data/ggc_db");
		addDatabaseSetting("DB0_CONN_USERNAME", "sa");
		addDatabaseSetting("DB0_CONN_PASSWORD", "");
		addDatabaseSetting("DB0_HIBERNATE_DIALECT", "org.hibernate.dialect.HSQLDialect");
	    }
	    catch(Exception ex)
	    {
		System.out.println("Exception on create default config: " + ex);
	    }

	    selected_db = 0;
	    selected_lang = "en";

	    saveConfig();

	}

    }

    private String getCurrentTimeAsUserReadableString()
    {
	GregorianCalendar gc = new GregorianCalendar();
	gc.setTimeInMillis(System.currentTimeMillis());

	return gc.get(GregorianCalendar.DAY_OF_MONTH) + "." + 
	    (gc.get(GregorianCalendar.MONTH) +1) + "." +
	    gc.get(GregorianCalendar.YEAR) + "  " + 
	    gc.get(GregorianCalendar.HOUR_OF_DAY) + ":" + 
	    gc.get(GregorianCalendar.MINUTE) + ":" +
	    gc.get(GregorianCalendar.SECOND);

    }


    public void saveConfig()
    {

	System.out.println("SAVEEEEEEEEEEEEE !!!");

	try
	{

	    //Properties props = new Properties();
	    BufferedWriter bw = new BufferedWriter(new FileWriter(getApplicationDatabaseConfig()));

	    bw.write("#\n" +
		     "# GGC_Config (Settings for GGC)\n" + 
		     "#" + getCurrentTimeAsUserReadableString() + "\n" + 
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
		DatabaseSettings dbs = this.allDatabases.get(""+i);
		dbs.write(bw);
	    }


	    bw.write("\n\n#\n# Look and Feel Settings\n#\n\n");
	    bw.write("LF_NAME=" + selected_LF_Name +"\n");

	    //props.put("LF_NAME", selected_LF_Name);

	    selected_LF_Class = availableLF_full.get(selected_LF_Name);

	    bw.write("LF_CLASS=" + selected_LF_Class +"\n");

	    //props.put("LF_CLASS", selected_LF_Name);
	    bw.write("SKINLF_SELECTED=" + skinLFSelected +"\n");
	    //props.put("SKINLF_SELECTED", skinLFSelected); 


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

    public String[] getAllDatabasesNamesAsArray()
    {
	String[] arr = new String[this.allDatabases.size()];
	
	for(int i=0; i<this.allDatabases.size(); i++)
	{
	    arr[i] = this.allDatabases.get("" + i).name;

	}

	return arr;

    }


    public String[] getAllDatabasesNamesPlusAsArray()
    {
	String[] arr = new String[this.allDatabases.size()];

	for(int i=0; i<this.allDatabases.size(); i++)
	{
	    arr[i] = i + " - " + this.allDatabases.get("" + i).name;

	}

	return arr;

    }



    public DatabaseSettings getDatabase(int index)
    {
	return null;
    }

    public DatabaseSettings getSelectedDatabase()
    {
	return null;
    }

    public int getSelectedDatabaseIndex()
    {
	return this.selected_db;
    }


    public void addDatabaseSetting(String setting, String value)
    {
	int dbnum = Integer.parseInt(setting.substring(2,3));

//	if (dbnum<this.getFirstAvailableDatabase()) 
//	    return;

	if (this.customDatabases.containsKey(""+dbnum)) 
	{
	    // we have database
	    DatabaseSettings dbs = this.customDatabases.get(""+dbnum);
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


    public boolean hasChanged()
    {
	return this.m_changed;
    }


    public void setSelectedDatabaseIndex(int index)
    {
	if (this.selected_db!=index)
	{
	    this.selected_db = index;
	    this.m_changed = true;
	}

    }





    @Override
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
