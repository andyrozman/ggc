package com.atech.db.tool;

import java.awt.Component;
import java.awt.Font;
import java.io.*;
import java.lang.reflect.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.Enumeration;
import java.util.GregorianCalendar;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Locale;
import java.util.Properties;

import javax.swing.ImageIcon;
import javax.swing.JComponent;
import javax.swing.UIManager;

import ggc.datamodels.DailyValues;
import ggc.datamodels.HbA1cValues;
import ggc.db.datalayer.GGCDb;
import ggc.db.datalayer.GGCDbLoader;
//import ggc.db.db_tool.DbToolApplicationGGC;
//import ggc.gui.StatusBar;
import ggc.nutrition.GGCTreeRoot;
import ggc.util.I18nControl;

public class DbToolAccess
{

    // LF
    Hashtable availableLF_full = null;
    Object[]  availableLF = null;
    Object[]  availableLang = null;
//    private LanguageInfo m_lang_info = null;

    String selectedLF = null;
    String subSelectedLF = null;

    // Config file
    Hashtable config_db_values = null;
    public int selected_db = -1;
    public int selected_lang = 1;
    public String selected_LF_Class = null; // class
    public String selected_LF_Name = null; // name
    public String skinLFSelected = null;
    String allDbs[] = null;


    public static String pathPrefix = "./";

    public I18nControl m_i18n = I18nControl.getInstance();

    static private DbToolAccess m_da = null;   // This is handle to unique 
                                             // singelton instance

    //public GGCDb m_db = null;
    public Component m_main = null;

    public Font fonts[] = null;

    public DbToolTreeRoot m_databases_treeroot = null;
    //public GGCTreeRoot m_meals_treeroot = null;


    public Date m_date = null;
    public HbA1cValues m_HbA1c = null;
    public DailyValues m_dvalues = null;

    public Hashtable m_tableOfDatabases = new Hashtable();
    public ArrayList m_listOfDatabases = null;

    public Object[] m_availableDatabases = null;

    public ArrayList listOfClasses = new ArrayList();

    public DatabaseDefinitions m_dataDefs = null;


    // ********************************************************
    // ******      Constructors and Access methods        *****    
    // ********************************************************



    //   Constructor:  DataAccess
    /**
     *
     *  This is DbToolAccess constructor; Since classes use Singleton Pattern,
     *  constructor is protected and can be accessed only with getInstance() 
     *  method.<br><br>
     *
     */ 
    private DbToolAccess()
    {

        //m_db = db;
//        loadConfig();
        loadFonts();

	m_dataDefs = new DatabaseDefinitions();
	m_databases_treeroot =  new DbToolTreeRoot(this);

	//loadApplicationData();

    } 




    //  Method:       getInstance
    //  Author:       Andy
    /**
     *
     *  This method returns reference to DbToolAccess object created, or if no 
     *  object was created yet, it creates one.<br><br>
     *
     *  @return Reference to DbToolAccess object
     * 
     */ 
    static public DbToolAccess getInstance()
    {
        if (m_da == null)
            m_da = new DbToolAccess();

        return m_da;
    }



    static public DbToolAccess createInstance(Component main)
    {
        if (m_da == null)
        {
            m_da = new DbToolAccess();
            m_da.setParent(main);
        }

        return m_da;
    }


/*
    static public DataAccess getInstance()
    {
        return m_da;
    }
*/


    public Object[] getAvailableDatabases()
    {
	//System.out.println("getAvailableDatabases() " + m_availableDatabases);
	return m_availableDatabases;
    }


    public void createAvailableDatabases(int number)
    {
	m_availableDatabases = new Object[number];
    }


    public void addAvailableDatabase(int index, DatabaseDefObject obj)
    {
	m_availableDatabases[index] = obj;
    }



    //  Method:       deleteInstance
    /**
     *  This method sets handle to DataAccess to null and deletes the instance. <br><br>
     */ 
    public void deleteInstance()
    {

        m_i18n=null;

    }




    // ********************************************************
    // ******                   Fonts                     *****    
    // ********************************************************

    public static final int FONT_BIG_BOLD = 0;
    public static final int FONT_NORMAL = 1;
    public static final int FONT_NORMAL_BOLD = 2;

    public void loadFonts()
    {
        fonts = new Font[3];
        fonts[0] = new Font("SansSerif", Font.BOLD, 22);
        fonts[1] = new Font("SansSerif", Font.PLAIN, 12);
        fonts[2] = new Font("SansSerif", Font.BOLD, 12);
    }


    public Font getFont(int font_id)
    {
        return fonts[font_id];
    }



    // ********************************************************
    // ******          Parent handling (for UIs)          *****    
    // ********************************************************


    public void setParent(Component main)
    {
        m_main = main;
    }


    public Component getParent()
    {
        return m_main;
    }


    // ********************************************************
    // ******               Look and Feel                 *****    
    // ********************************************************

/*
    public void loadAvailableLFs()
    {

        availableLF_full = new Hashtable<String,String>();
        UIManager.LookAndFeelInfo[] info = UIManager.getInstalledLookAndFeels();

        availableLF = new Object[info.length+1];

        //ring selectedLF = null;
        //String subSelectedLF = null;

        int i;
        for (i=0; i<info.length; i++) 
        {
            String name = info[i].getName();
            String className = info[i].getClassName();

            availableLF_full.put(name, className);
            availableLF[i] = name;

            //System.out.println(humanReadableName);
        }     

        availableLF_full.put("SkinLF", "com.l2fprod.gui.plaf.skin.SkinLookAndFeel");
        availableLF[i] = "SkinLF";

    }

    public Object[] getAvailableLFs()
    {
        return availableLF;
    }


    public static String[] getLFData()
    {
        String out[] = new String[2];

        try
        {
            Properties props = new Properties();

            FileInputStream in = new FileInputStream(pathPrefix  + "/data/PIS_Config.properties");
            props.load(in);

            out[0] = (String)props.get("LF_CLASS");
            out[1] = (String)props.get("SKINLF_SELECTED");

            return out;

        }
        catch(Exception ex)
        {
            System.out.println("DataAccess::getLFData::Exception> " + ex);
            return null;
        }
    }
*/
    // ********************************************************
    // ******                  Languages                  *****    
    // ********************************************************


    public ArrayList getApplicationDatas()
    {
	return listOfClasses;
    }


    public void loadApplicationData()
    {

	File f = new File(".");

	try
	{
	    //System.out.println("Root: " + f.getCanonicalPath());
	    processDirectory(f.getCanonicalPath(), f, false);
	}
	catch(Exception ex)
	{
	    System.out.println("Exception: " + ex);
	}

    }


    public void processDirectory(String root, File f, boolean display)
    {
	File fl[] = f.listFiles();

	for (int i=0; i<fl.length; i++)
	{
	    //System.out.println(fl[i]);

	    if (fl[i].isDirectory())
	    {
		processDirectory(root, fl[i], false);
	    }
	    else
	    {
		String file = fl[i].getName();

		if (file.endsWith(".class"))
		{
		    try
		    {
			String can = fl[i].getCanonicalPath();

			if (can.contains("$"))
			    continue;

			can = can.substring(root.length()+1);
			can = replaceExpression(can, File.separator, ".");
			can = can.substring(0, can.length()-6);

			try
			{
			    //System.out.println("class: " +  can);

			    Class c = Class.forName(can);
			    if (getCorrectInterface(c))
			    {
				DbToolApplicationInterface obj = (DbToolApplicationInterface)c.newInstance();
				listOfClasses.add(obj);
			    }
			}
			catch(java.lang.NoClassDefFoundError ex) { }
			catch(java.lang.ExceptionInInitializerError ex) { }
			catch(Exception ex) { }

		    }
		    catch(Exception ex)
		    {
			System.out.println("  Ex:" + ex);
		    }
		    
		}
		else if (file.endsWith(".jar"))
		{
		    System.out.println("JAR: " +  file);//fl[i]);
		}
		//System.out.println("file: " +  file);//fl[i]);
	    }
	}
    }


    public boolean getCorrectInterface(Class c) //Object o) 
    {
	Class[] theInterfaces = c.getInterfaces();
	for (int i = 0; i < theInterfaces.length; i++) 
	{
	    String interfaceName = theInterfaces[i].getName();

	    if (interfaceName.equals("com.atech.db.tool.DbToolApplicationInterface"))
	    {
//		System.out.println("Found Interface: "  + interfaceName);
		return true;
	    }
	    else
		return false;

	}

	return false;
    }	



/*
    public void loadLanguageInfo()
    {

        try
        {

            Properties props = new Properties();

            FileInputStream in = new FileInputStream(pathPrefix + "/data/lang/PIS_Languages.properties");
            props.load(in);

            m_lang_info = new LanguageInfo();
            m_lang_info.loadData(props);

        }
        catch(Exception ex)
        {
            System.out.println("DataAccess::loadLanguageInfo::Exception> " + ex);
        }

    }


    public LanguageInfo getLanguageInfo()
    {
        return m_lang_info;
    }



    public Object[] getAvailableLanguages()
    {
        return m_lang_info.availableLang;
    }


    public int getSelectedLangIndex()
    {
        return selected_lang;
    }


    public static String getSelectedLocale()
    {
        String locale = "SI";

        try
        {
            Properties props = new Properties();

            FileInputStream in = new FileInputStream(pathPrefix  + "/data/PIS_Config.properties");
            props.load(in);

            int sel_lang = 1;

            if (props.containsKey("SELECTED_LANG"))
            {
                sel_lang = Integer.parseInt((String)props.get("SELECTED_LANG"));
                System.out.println("Sel lang: " + sel_lang);
            }


            //props = new Properties();
            props.clear();

            in = null;
            in = new FileInputStream(pathPrefix  + "/data/lang/PIS_Languages.properties");
            props.load(in);

            if (props.containsKey("LANG_" + sel_lang + "_LOCALE"))
            {
                locale = (String)props.get("LANG_" + sel_lang + "_LOCALE");
            }

//            System.out.println("Locale: " + locale);

        }
        catch(Exception ex)
        {
            System.out.println("DataAccess::getSelectedLocale::Exception> " + ex);
        }

        return locale;

    }
*/


    // ********************************************************
    // ******            Config File Handling             *****    
    // ********************************************************


    public void loadConfig(DbToolApplicationInterface dtai)
    {
        //Hashtable config_db_values = null;
        //int selected_db = -1;
        //String selected_LF_Class = null; // class
        //String selected_LF_Name = null; // name
        //String skinLFSelected = null;

        config_db_values = new Hashtable();

        try
        {
            Properties props = new Properties();

            FileInputStream in = new FileInputStream(pathPrefix + dtai.getApplicationDatabaseConfig());
            props.load(in);


            for(Enumeration en = props.keys(); en.hasMoreElements(); )
            {
                String  str = (String)en.nextElement();

                if (str.startsWith("DB")) 
                {
                    config_db_values.put(str, (String)props.get(str));
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
                        skinLFSelected = (String)props.get(str);
                    }
                    else if (str.equals("SELECTED_DB")) 
                    {
                        selected_db = Integer.parseInt((String)props.get(str));
                    }
                    else if (str.equals("SELECTED_LANG")) 
                    {
                        selected_lang = Integer.parseInt((String)props.get(str));
                    }
                    else 
                        System.out.println("DataAccess:loadConfig:: Unknown parameter : " + str);

                }

            }

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

        }
        catch(Exception ex)
        {
            System.out.println("DataAccess::loadConfig::Exception> " + ex);
        }

    }


    public ArrayList getListOfDatabases()
    {
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
    }



    public void saveConfig()
    {

	/*
        try
        {

            //Properties props = new Properties();
            BufferedWriter bw = new BufferedWriter(new FileWriter(pathPrefix  + "/data/PIS_Config.properties"));


            bw.write("#\n" +
                     "# ZISConfig (Settings for ZIS)\n" +
                     "#\n"+
                     "# Don't edit by hand\n" +
                     "#\n\n"+
                     "#\n# Databases settings\n#\n");


            int count_db = 0;

            for (int i=0; i<20; i++) 
            {
                if (config_db_values.containsKey("DB"+i+"_CONN_NAME")) 
                {
                    String con_name = config_db_values.get("DB"+i+"_CONN_NAME");
                    bw.write("\n#\n# Database #" + i +" - " + con_name + "\n#\n");
                    count_db++;
                    bw.write("DB" + i + "_CONN_NAME=" + con_name +"\n");
                    bw.write("DB" + i + "_CONN_DRIVER_CLASS=" + config_db_values.get("DB"+i+"_CONN_DRIVER_CLASS") +"\n");
                    bw.write("DB" + i + "_CONN_URL=" + config_db_values.get("DB"+i+"_CONN_URL") +"\n");
                    bw.write("DB" + i + "_CONN_USERNAME=" + config_db_values.get("DB"+i+"_CONN_USERNAME") +"\n");
                    bw.write("DB" + i + "_CONN_PASSWORD=" + config_db_values.get("DB"+i+"_CONN_PASSWORD") +"\n");
                    bw.write("DB" + i + "_HIBERNATE_DIALECT=" + config_db_values.get("DB"+i+"_HIBERNATE_DIALECT") +"\n");

//                    list.add(i+" - " + config_db_values.get("DB"+i+"_CONN_NAME"));
                }

                if ((count_db*6)>=config_db_values.size()) 
                    break;

            }

/*
            for(Enumeration en=config_db_values.keys(); en.hasMoreElements(); )
            {
                String key = (String)en.nextElement();
                bw.write(key + "=" + config_db_values.get(key)+"\n");
            }
            */
/*
            bw.write("\n\n#\n# Look and Feel Settings\n#\n\n");
            bw.write("LF_NAME=" + selected_LF_Name +"\n");

            //props.put("LF_NAME", selected_LF_Name);

            selected_LF_Class = availableLF_full.get(selected_LF_Name);

            bw.write("LF_CLASS=" + selected_LF_Class +"\n");

            //props.put("LF_CLASS", selected_LF_Name);
            bw.write("SKINLF_SELECTED=" + skinLFSelected +"\n");
            //props.put("SKINLF_SELECTED", skinLFSelected);
            bw.write("\n\n#\n# Db Selector\n#\n\n");

            bw.write("SELECTED_DB=" + selected_db +"\n");
            //props.put("SELECTED_DB", ""+selected_db);

            bw.write("SELECTED_LANG=" + selected_lang +"\n");


//            FileOutputStream out = new FileOutputStream("./ZISOut.properties");

            bw.close();
            //props.s

            //props.store(out, " Settings for ZIS version 0.2.3 or higher (please DON'T edit this file by hand!!)");

        }
        catch(Exception ex)
        {
            System.out.println("DataAccess::saveConfig::Exception> " + ex);
            ex.printStackTrace();
        }
*/

    }
    

    public String[] getAvailableDbs()
    {
        return allDbs;
    }


    public int getSelectedDbIndex()
    {
        for (int i=0; i<allDbs.length; i++)
        {
            if (allDbs[i].startsWith(this.selected_db + " - "))
                return i;
        }
        return 0;
    }




    public String[] getMonthsArray()
    {

        String arr[] = new String[12];

        arr[0] = m_i18n.getMessage("JANUARY");
        arr[1] = m_i18n.getMessage("FEBRUARY");
        arr[2] = m_i18n.getMessage("MARCH");
        arr[3] = m_i18n.getMessage("APRIL");
        arr[4] = m_i18n.getMessage("MAY");
        arr[5] = m_i18n.getMessage("JUNE");
        arr[6] = m_i18n.getMessage("JULY");
        arr[7] = m_i18n.getMessage("AUGUST");
        arr[8] = m_i18n.getMessage("SEPTEMBER");
        arr[9] = m_i18n.getMessage("OCTOBER");
        arr[10] = m_i18n.getMessage("NOVEMBER");
        arr[11] = m_i18n.getMessage("DECEMBER");

        return arr;

    }


    public String getDateString(int date)
    {

        // 20051012

        int year = date/10000;
        int months = date - (year*10000);

        months = months/100;

        int days = date - (year*10000) - (months*100);

        if (year==0)
        {
            return getLeadingZero(days,2) + "/" + getLeadingZero(months,2);
        }
        else
            return getLeadingZero(days,2) + "/" + getLeadingZero(months,2) + "/" + year;

    }


    public String getTimeString(int time)
    {

        int hours = time/100;

        int min = time - hours*100;

        return getLeadingZero(hours,2) + ":" + getLeadingZero(min,2);

    }

    public String getDateTimeString(long date)
    {
        return getDateTimeString(date, 1);
    }


    public String getDateTimeAsDateString(long date)
    {
        return getDateTimeString(date, 2);
    }


    public String getDateTimeAsTimeString(long date)
    {
        return getDateTimeString(date, 3);
    }


    // ret_type = 1 (Date and time)
    // ret_type = 2 (Date)
    // ret_type = 3 (Time)

    public final static int DT_DATETIME = 1;
    public final static int DT_DATE = 2;
    public final static int DT_TIME = 3;



    public String getDateTimeString(long dt, int ret_type)
    {


        int y = (int)(dt/100000000L);
        dt -= y*100000000L;

        int m = (int)(dt/1000000L);
        dt -= m*1000000L;

        int d = (int)(dt/10000L);
        dt -= d*10000L;

        int h = (int)(dt/100L);
        dt -= h*100L;

        int min = (int)dt;


        if (ret_type==DT_DATETIME)
        {
            return getLeadingZero(d,2) + "/" + getLeadingZero(m,2) + "/" + y + "  " + getLeadingZero(h,2) + ":" + getLeadingZero(min,2);
        }
        else if (ret_type==DT_DATE)
        {
            return getLeadingZero(d,2) + "/" + getLeadingZero(m,2) + "/" + y;
        }
        else
            return getLeadingZero(h,2) + ":" + getLeadingZero(min,2);

    }


    public Date getDateTimeAsDateObject(long dt)
    {

	//Date dt_obj = new Date();
	GregorianCalendar gc = new GregorianCalendar();
	

	int y = (int)(dt/100000000L);
	dt -= y*100000000L;

	int m = (int)(dt/1000000L);
	dt -= m*1000000L;

	int d = (int)(dt/10000L);
	dt -= d*10000L;

	int h = (int)(dt/100L);
	dt -= h*100L;

	int min = (int)dt;

	gc.set(GregorianCalendar.DATE, d);
	gc.set(GregorianCalendar.MONTH, m-1);
	gc.set(GregorianCalendar.YEAR, y);

	gc.set(GregorianCalendar.HOUR_OF_DAY, h);
	gc.set(GregorianCalendar.MINUTE, min);

/*
	dt_obj.setHours(h);
	dt_obj.setMinutes(min);

	dt_obj.setDate(d);
	dt_obj.setMonth(m);
	dt_obj.setYear(y);

	return dt_obj;
	*/

	return gc.getTime();

    }



    public long getDateTimeFromDateObject(Date dt)
    {

	GregorianCalendar gc = new GregorianCalendar();
	gc.setTime(dt);

	String dx = "";

	dx += "" + gc.get(GregorianCalendar.YEAR);
	dx += "" + getLeadingZero(gc.get(GregorianCalendar.MONTH+1), 2);
	dx += "" + getLeadingZero(gc.get(GregorianCalendar.DAY_OF_MONTH), 2);
	dx += "" + getLeadingZero(gc.get(GregorianCalendar.HOUR_OF_DAY), 2);
	dx += "" + getLeadingZero(gc.get(GregorianCalendar.MINUTE), 2);

	return Long.parseLong(dx);

    }





    public String getDateTimeString(int date, int time)
    {
        return getDateString(date)+" " + getTimeString(time);
    }


    public String getLeadingZero(int number, int places)
    {
        String nn = ""+number;

        while (nn.length()<places)
        {
            nn = "0"+nn;
        }

        return nn;
    }

    public String getLeadingZero(String number, int places)
    {
	number = number.trim();

	while (number.length()<places)
	{
	    number = "0"+number;
	}

	return number;
    }



    public int getStartYear()
    {
        // FIX set in Db
        return 1800;
    }



    public float getFloatValue(Object aValue)
    {
	float out = 0.0f;

	if (aValue==null)
	    return out;

	if (aValue instanceof Float)
	{
	    try
	    {
		Float f = (Float)aValue;
		out = f.floatValue();
	    }
	    catch(Exception ex) {}
	}
	else if (aValue instanceof String)
	{
	    String s = (String)aValue;
	    if (s.length()>0)
	    {
		try
		{
		    out = Float.parseFloat(s);
		}
		catch(Exception ex) {}
	    }
	}

	return out;
    }


    public int getIntValue(Object aValue)
    {
	int out = 0;

	if (aValue==null)
	    return out;

	if (aValue instanceof Integer)
	{
	    try
	    {
		Integer i = (Integer)aValue;
		out = i.intValue();
	    }
	    catch(Exception ex) {}
	}
	else if (aValue instanceof String)
	{
	    String s = (String)aValue;
	    if (s.length()>0)
	    {
		try
		{
		    out = Integer.parseInt(s);
		}
		catch(Exception ex) {}
	    }
	}

	return out;
    }


    public long getLongValue(Object aValue)
    {
	long out = 0L;

	if (aValue==null)
	    return out;

	if (aValue instanceof Long)
	{
	    try
	    {
		Long i = (Long)aValue;
		out = i.longValue();
	    }
	    catch(Exception ex) {}
	}
	else if (aValue instanceof String)
	{
	    String s = (String)aValue;
	    if (s.length()>0)
	    {
		try
		{
		    out = Long.parseLong(s);
		}
		catch(Exception ex) {}
	    }
	}

	return out;
    }



/*
    public Date m_date = null;
    public HbA1cValues m_HbA1c = null;
    public DailyValues m_dvalues = null;
*/



    public static void notImplemented(String source)
    {
        System.out.println("Not Implemented: " + source);
    }




    /**
     * For replacing strings.<br>
     * 
     * @param input   Input String
     * @param replace What to seatch for.
     * @param replacement  What to replace with.
     * 
     * @return Parsed string.
     */
    public String replaceExpression(String input, String replace, String replacement)
    {

        int idx;
        if ((idx=input.indexOf(replace))==-1)
        {
            return input;
        }

        StringBuffer returning = new StringBuffer();

        while (idx!=-1)
        {
            returning.append(input.substring(0, idx));
            returning.append(replacement);
            input = input.substring(idx+replace.length());
            idx = input.indexOf(replace);
        }
        returning.append(input);
        
        return returning.toString();

    }


}

