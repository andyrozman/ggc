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
 *  Filename: DataAccess
 *  Purpose:  Used for utility works and static data handling (this is singelton
 *      class which holds all our definitions, so that we don't need to create them
 *      again for each class.      
 *
 *  Author:   andyrozman  {andy@atech-software.com}
 */

using System;
using System.Collections.Generic;
using System.ComponentModel;
using System.Globalization;
using GGCMobileNET.Data.Db;
using System.Text;
using log4net;


namespace GGCMobileNET.Data.Utils
{



public class DataAccessMobile //extends ATDataAccessAbstract
{

    
    public String current_db_version = "7";

    
    /**
     * At later time this will be determined by user management part
     */
    public long current_user_id = 1;

    private readonly ILog log = LogManager.GetLogger(typeof(DataAccessMobile));

    public const String pathPrefix = ".";

    private static DataAccessMobile s_da = null; // This is handle to unique

    private GGCDbMobile m_db = null;

    public GGCDbMobile Db
    {
        get { return m_db; }
        set { m_db = value; }
    }

    // xa public Font[] fonts = null;

    // daily and weekly data
    private DateTime m_date, m_dateStart;

    /*
    public static DecimalFormat MmolDecimalFormat = new DecimalFormat("#0.0");
    public static DecimalFormat Decimal0Format = new DecimalFormat("#0");
    public static DecimalFormat Decimal2Format = new DecimalFormat("#0.00");
    public static DecimalFormat Decimal3Format = new DecimalFormat("#0.000");
    */

    /**
     * Which BG unit is used: BG_MGDL = mg/dl, BG_MMOL = mmol/l
     */
    public int m_BG_unit = BG_MGDL;

    public String[] availableLanguages = { "English", "Deutsch", "Slovenski", "Fran\u00e7ais" };

    public String[] avLangPostfix = { "en", "de", "si", "fr" };

    public String[] bg_units = { "mg/dl", "mmol/l" };

    public Dictionary<String,String> timeZones;

    public List<Container> parents_list;

    //public ImageIcon[] config_icons = null;


    //protected Collator m_collator = null;

    
    public String[] days = new String[7];
    public String[] months = new String[12];
    
//XA    protected I18nControl m_i18n = null; // ATI18nControl.getInstance();

    
    
    /*
     * { new ImageIcon("/icons/cfg_general.png"), new
     * ImageIcon("/icons/cfg_medical.png"), new
     * ImageIcon("icons/cfg_colors.png"), new ImageIcon("icons/cfg_render.png"),
     * new ImageIcon("icons/cfg_meter.png"), new
     * ImageIcon("icons/cfg_print.png") };
     */
    /*
     * public ImageIcon config_icons[] = { new ImageIcon("images/cfg_db.gif"),
     * new ImageIcon("images/cfg_look.gif"), new
     * ImageIcon("images/cfg_myparish.gif"), new
     * ImageIcon("images/cfg_masses.gif"), new
     * ImageIcon("images/cfg_users.gif"), new ImageIcon("images/cfg_lang.gif"),
     * new ImageIcon("images/cfg_web.gif"), null };
     * 
     * public String config_types[] = { m_ic.getMessage("GENERAL"),
     * m_ic.getMessage("MEDICAL_DATA"), m_ic.getMessage("COLORS_AND_FONTS"),
     * m_ic.getMessage("RENDERING_QUALITY"),
     * m_ic.getMessage("METER_CONFIGURATION"), m_ic.getMessage("PRINTING") };
     */
    public String[] options_yes_no = null;

    // ********************************************************
    // ****** Constructors and Access methods *****
    // ********************************************************

    // Constructor: DataAccess
    /**
     * 
     * This is DataAccess constructor; Since classes use Singleton Pattern,
     * constructor is protected and can be accessed only with getInstance()
     * method.<br>
     * <br>
     * 
     */
    private DataAccessMobile()
    {
// XA        this.m_i18n = I18nControl.Instance;
// XA        loadArraysTranslation();
        //loadFonts();
        //m_settings_ht = new Hashtable<String, String>();
        //this.m_collator = this.m_i18n.getCollationDefintion();
        
        this.m_db = new GGCDbMobile(this);

        CreateLogger();


/*        //System.out.println("init Special");
        this.tree_roots = new Hashtable<String, GGCTreeRoot>();

        //System.out.println("config File");
        this.m_configFile = new DbToolApplicationGGC();
        this.m_configFile.loadConfig();

        //System.out.println("configuratioon manager");
        m_cfgMgr = new ConfigurationManager(this);

        //System.out.println("m_settings");
        this.m_settings = new GGCProperties(this, this.m_configFile, m_cfgMgr);
        
        //System.out.println("m_set: " + this.m_settings);
        
        loadOptions();

        if (!(new File("../data/debug.txt").exists()))
        {
            new RedirectScreen();
        }
        
        startWebServer();
  */      
    }    
    
    // Method: getInstance
    // Author: Andy
    /**
     * 
     * This method returns reference to OmniI18nControl object created, or if no
     * object was created yet, it creates one.<br>
     * <br>
     * 
     * @return Reference to OmniI18nControl object
     * 
     */
    public static DataAccessMobile Instance
    {
        get
        {
        if (s_da == null)
            s_da = new DataAccessMobile();
        return s_da;
        }
    }


    public void CreateLogger()
    {
        // Rolling File Appender, with size 4 Mb to file DispoApp_DataLayer.log
        log4net.Appender.RollingFileAppender rfa = new log4net.Appender.RollingFileAppender();
        rfa.AppendToFile = true;
        rfa.RollingStyle = log4net.Appender.RollingFileAppender.RollingMode.Size;
        rfa.MaxFileSize = 4000000;
        rfa.MaxSizeRollBackups = 100;
        rfa.File = ".\\GGCMobileNET.log";
        rfa.Layout = new log4net.Layout.PatternLayout("[%d] %-5p - %m%n");
        rfa.ImmediateFlush = true;
        rfa.StaticLogFileName = false;

        // Filter (Show from INFO - FATAL)
        log4net.Filter.LevelRangeFilter lrf = new log4net.Filter.LevelRangeFilter();
        lrf.LevelMax = log4net.Core.Level.Fatal;
        //lrf.LevelMin = log4net.Core.Level.Info;
        lrf.LevelMin = log4net.Core.Level.Debug;
        rfa.AddFilter(lrf);
        rfa.ActivateOptions();

        // Set logger
        log4net.Config.BasicConfigurator.Configure(rfa);
        rfa.ActivateOptions();

        log.Info("");
        log.Info("DataManager Startup - [" + DateTime.Now + "]");

    }



    /*
    public static DataAccessMobile createInstance(Frame main)
    {
        s_da = null;

        if (s_da == null)
        {
            //System.out.println("!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!  " + main);
            // GGCDb db = new GGCDb();

            
            
            System.out.println("create new Instance");
            s_da = new DataAccessMobile();
            //System.out.println("setParent");
            //xa s_da.setParent(main);
            //System.out.println("setMainParent");
            //xa s_da.setMainParent(main);
            // System.out.println("addComponet");
            // s_da.addComponent(main);
        }

        return s_da;
    }
*/


    // Method: deleteInstance
    /**
     * This method sets handle to DataAccess to null and deletes the instance. <br>
     * <br>
     */
    public static void deleteInstance()
    {
        // m_i18n = null;
        DataAccessMobile.s_da = null;
    }

    /*
    public void startDb(StatusBar bar)
    {
        GGCDbLoader loader = new GGCDbLoader(this, bar);
        loader.start();
    }

    public void startDb(StatusBarL bar2)
    {
        GGCDbLoader loader = new GGCDbLoader(this, bar2);
        loader.start();
    }
*/
    public GGCDbMobile getDb()
    {
        return m_db;
    }

    public void setDb(GGCDbMobile db)
    {
        this.m_db = db;
    }

    
    
    
    public void loadArraysTranslation()
    {
        /* XA
        // months
        months[0] = m_i18n.getMessage("JANUARY");
        months[1] = m_i18n.getMessage("FEBRUARY");
        months[2] = m_i18n.getMessage("MARCH");
        months[3] = m_i18n.getMessage("APRIL");
        months[4] = m_i18n.getMessage("MAY");
        months[5] = m_i18n.getMessage("JUNE");
        months[6] = m_i18n.getMessage("JULY");
        months[7] = m_i18n.getMessage("AUGUST");
        months[8] = m_i18n.getMessage("SEPTEMBER");
        months[9] = m_i18n.getMessage("OCTOBER");
        months[10] = m_i18n.getMessage("NOVEMBER");
        months[11] = m_i18n.getMessage("DECEMBER");

        // days
        days[0] = m_i18n.getMessage("MONDAY");
        days[1] = m_i18n.getMessage("TUESDAY");
        days[2] = m_i18n.getMessage("WEDNESDAY");
        days[3] = m_i18n.getMessage("THURSDAY");
        days[4] = m_i18n.getMessage("FRIDAY");
        days[5] = m_i18n.getMessage("SATURDAY");
        days[6] = m_i18n.getMessage("SUNDAY");
        */
    }
    
    
    
    // ********************************************************
    // ****** Static Methods *****
    // ********************************************************

    public static String GetFloatAsString(float f, String decimal_places)
    {
        return DataAccessMobile.GetFloatAsString(f, Convert.ToInt32(decimal_places));
    }

    public static String GetFloatAsString(float f, int decimal_places)
    {

        switch (decimal_places)
        {
            case 1:
                return String.Format("{0:00.#}", f);

            case 2:
                return String.Format("{0:00.##}", f);

            case 3:
                return String.Format("{0:00.###}", f);

            default:
                return String.Format("{0:00}", f);
        }


        return null;
        /* XA
        switch (decimal_places)
        {
        case 1:
            return DataAccessMobile.MmolDecimalFormat.format(f);

        case 2:
            return DataAccessMobile.Decimal2Format.format(f);

        case 3:
            return DataAccessMobile.Decimal3Format.format(f);

        default:
            return DataAccessMobile.Decimal0Format.format(f);
        }
         */
    }

    // ********************************************************
    // ****** Abstract Methods *****
    // ********************************************************


    public String getImagesRoot()
    {
        return "/icons/";
    }


    // ********************************************************
    // ****** Icons *****
    // ********************************************************

    private void loadIcons()
    {
        /*
        config_icons = new ImageIcon[6];
        config_icons[0] = new ImageIcon(getImage("/icons/cfg_general.png", m_main));
        config_icons[1] = new ImageIcon(getImage("/icons/cfg_medical.png", m_main));
        config_icons[2] = new ImageIcon(getImage("/icons/cfg_colors.png", m_main));
        config_icons[3] = new ImageIcon(getImage("/icons/cfg_render.png", m_main));
        config_icons[4] = new ImageIcon(getImage("/icons/cfg_print.png", m_main));
        */
        // config_icons[4] = new ImageIcon(getImage("/icons/cfg_meter.png",
        // m_main));

    }

    // ********************************************************
    // ****** Db *****
    // ********************************************************



    // ********************************************************
    // ****** Settings *****
    // ********************************************************
/*
    public GGCProperties getSettings()
    {
        return this.m_settings;
    }

    public void loadSettingsFromDb()
    {
        this.m_settings.load();
    }

    public Color getColor(int color)
    {
        return new Color(color);
    }

    public ConfigurationManager getConfigurationManager()
    {
        return this.m_cfgMgr;
    }
*/
    
    // ********************************************************
    // ****** Language *****
    // ********************************************************

    public String[] getAvailableLanguages()
    {
        return this.availableLanguages;
    }

    public int getSelectedLanguageIndex()
    {
        // TODO: selected_lang 
        //return this.getLanguageIndex(this.getSettings().getLanguage());
        return 0;
    }

    public int getLanguageIndex(String postfix)
    {
        // System.out.println(postfix);

        for (int i = 0; i < this.avLangPostfix.Length; i++)
        {
            if (this.avLangPostfix[i] == postfix)
                return i;
            
        }

        return 0;
    }

    public int getLanguageIndexByName(String name)
    {
        // stem.out.println(name);

        for (int i = 0; i < this.availableLanguages.Length; i++)
        {
            if (this.availableLanguages[i] ==name)
                return i;
        }

        return 0;
    }

    // ********************************************************
    // ****** BG Measurement Type *****
    // ********************************************************

    public const int BG_MGDL = 1;
    public const int BG_MMOL = 2;

    public int getBGMeasurmentType()
    {
        return this.m_BG_unit;
    }

    public void setBGMeasurmentType(int type)
    {
        this.m_BG_unit = type;
    }

    public const float MGDL_TO_MMOL_FACTOR = 0.0555f;
    public const float MMOL_TO_MGDL_FACTOR = 18.016f;

    /**
     * Depending on the return value of <code>getBGMeasurmentType()</code>,
     * either return the mg/dl or the mmol/l value of the database's value.
     * Default is mg/dl.
     * 
     * @param dbValue
     *            - The database's value (in float)
     * @return the BG in either mg/dl or mmol/l
     */
    public float getDisplayedBG(float dbValue)
    {
        switch (this.m_BG_unit)
        {
        case BG_MMOL:
            // this POS should return a float rounded to 3 decimal places,
            // if I understand the docu correctly
            return (dbValue * MGDL_TO_MMOL_FACTOR);
        case BG_MGDL:
        default:
            return dbValue;
        }
    }

    public float getBGValue(float bg_value)
    {
        switch (this.m_BG_unit)
        {
        case BG_MMOL:
            return (bg_value * MGDL_TO_MMOL_FACTOR);
        case BG_MGDL:
        default:
            return bg_value;
        }

    }

    public float getBGValueByType(int type, float bg_value)
    {
        switch (type)
        {
        case BG_MMOL:
            return (bg_value * MGDL_TO_MMOL_FACTOR);
        case BG_MGDL:
        default:
            return bg_value;
        }

    }

    public float getBGValueByType(int input_type, int output_type, float bg_value)
    {

        if (input_type == output_type)
            return bg_value;
        else
        {
            if (output_type == DataAccessMobile.BG_MGDL)
            {
                return bg_value * DataAccessMobile.MGDL_TO_MMOL_FACTOR;
            }
            else
            {
                return bg_value * DataAccessMobile.MMOL_TO_MGDL_FACTOR;
            }
        }

    }

    public float getBGValueDifferent(int type, float bg_value)
    {

        if (type == DataAccessMobile.BG_MGDL)
        {
            return bg_value * DataAccessMobile.MGDL_TO_MMOL_FACTOR;
        }
        else
        {
            return bg_value * DataAccessMobile.MMOL_TO_MGDL_FACTOR;
        }

    }

    // ********************************************************
    // ****** Fonts *****
    // ********************************************************

    /*
     * public static final int FONT_BIG_BOLD = 0; public static final int
     * FONT_NORMAL = 1; public static final int FONT_NORMAL_BOLD = 2;
     * 
     * public void loadFonts() { fonts = new Font[3]; fonts[0] = new
     * Font("SansSerif", Font.BOLD, 22); fonts[1] = new Font("SansSerif",
     * Font.PLAIN, 12); fonts[2] = new Font("SansSerif", Font.BOLD, 12); }
     * 
     * public Font getFont(int font_id) { return fonts[font_id]; }
     */

    // ********************************************************
    // ****** Parent handling (for UIs) *****
    // ********************************************************
/*
    public void setParent(MainFrame main)
    {
        m_main = main;
        loadIcons();
    }


    @Override
    public MainFrame getParent()
    {
        return m_main;
    }

*/

    /* XA
    public I18nControl getI18nControlInstance() 
    { 
        return this.m_i18n;
    }*/
     

    /**
     * Utils
     */

    /*
    public Image getImage(String filename, Component cmp)
    {
        Image img;

        InputStream is = this.getClass().getResourceAsStream(filename);

        // System.out.println("getImage: " + filename);

        if (is == null)
            System.out.println("Error reading image: " + filename);

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        try
        {
            int c;
            while ((c = is.read()) >= 0)
                baos.write(c);

            // JDialog.getT
            // JFrame.getToolkit();
            img = cmp.getToolkit().createImage(baos.toByteArray());
        }
        catch (IOException ex)
        {
            ex.printStackTrace();
            return null;
        }
        return img;
    }
    */
    // ********************************************************
    // ****** Person Id / Login *****
    // ********************************************************

    private int current_person_id = 1;

    public int getCurrentPersonId()
    {
        return this.current_person_id;
    }

    // ********************************************************
    // ****** I18n Utils *****
    // ********************************************************


    // ********************************************************
    // ****** Look and Feel *****
    // ********************************************************

    /*
     * public void loadAvailableLFs() {
     * 
     * availableLF_full = new Hashtable<String,String>();
     * UIManager.LookAndFeelInfo[] info = UIManager.getInstalledLookAndFeels();
     * 
     * availableLF = new Object[info.length+1];
     * 
     * //ring selectedLF = null; //String subSelectedLF = null;
     * 
     * int i; for (i=0; i<info.length; i++) { String name = info[i].getName();
     * String className = info[i].getClassName();
     * 
     * availableLF_full.put(name, className); availableLF[i] = name;
     * 
     * //System.out.println(humanReadableName); }
     * 
     * availableLF_full.put("SkinLF",
     * "com.l2fprod.gui.plaf.skin.SkinLookAndFeel"); availableLF[i] = "SkinLF";
     * 
     * }
     * 
     * public Object[] getAvailableLFs() { return availableLF; }
     * 
     * 
     * public static String[] getLFData() { String out[] = new String[2];
     * 
     * try { Properties props = new Properties();
     * 
     * FileInputStream in = new FileInputStream(pathPrefix +
     * "/data/PIS_Config.properties"); props.load(in);
     * 
     * out[0] = (String)props.get("LF_CLASS"); out[1] =
     * (String)props.get("SKINLF_SELECTED");
     * 
     * return out;
     * 
     * } catch(Exception ex) {
     * System.out.println("DataAccess::getLFData::Exception> " + ex); return
     * null; } }
     */

    /*
    public static String[] getLFData()
    {
        String out[] = new String[2];

        try
        {
            Properties props = new Properties();

            FileInputStream in = new FileInputStream("../data/GGC_Config.properties");
            props.load(in);

            out[0] = (String) props.get("LF_CLASS");
            out[1] = (String) props.get("SKINLF_SELECTED");

            return out;

        }
        catch (Exception ex)
        {
            Console.WriteLine("DataAccess::getLFData::Exception> " + ex);
            return null;
        }
    }
    */
    // ********************************************************
    // ****** Component Id *****
    // ********************************************************

    private long component_id_last;

    public String getNewComponentId()
    {
        component_id_last++;

        return "" + this.component_id_last;

    }

    // ********************************************************
    // ****** Options *****
    // ********************************************************

    public void loadOptions()
    {
        /* XA
        this.options_yes_no = new String[2];
        this.options_yes_no[0] = m_i18n.getMessage("YES");
        this.options_yes_no[1] = m_i18n.getMessage("NO");
         */
    }

    // ********************************************************
    // ****** Dates and Times Handling *****
    // ********************************************************

    public String getCurrentDateString()
    {
        return DateTime.Now.ToString("dd.MM.yyyy");

        /*GregorianCalendar();
        return gc.get(Calendar.DAY_OF_MONTH) + "." + (gc.get(Calendar.MONTH) + 1) + "." + gc.get(Calendar.YEAR); */
    }

    public String[] getMonthsArray()
    {
        return null;
        /* XA
        String[] arr = new String[12];

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
        */
    }

    public String getDateString(int date)
    {
        //Calendar c = new Calendar();

        // 20051012

        int year = date / 10000;
        int months = date - (year * 10000);

        months = months / 100;

        int days = date - (year * 10000) - (months * 100);

        if (year == 0)
            return getLeadingZero(days, 2) + "/" + getLeadingZero(months, 2);
        else
            return getLeadingZero(days, 2) + "/" + getLeadingZero(months, 2) + "/" + year;
    }

    public String getTimeString(int time)
    {
        int hours = time / 100;
        int min = time - hours * 100;

        return getLeadingZero(hours, 2) + ":" + getLeadingZero(min, 2);
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

    public const int DT_DATETIME = 1;
    public const int DT_DATE = 2;
    public const int DT_TIME = 3;

    public String getDateTimeString(long dt, int ret_type)
    {

        int y = (int) (dt / 100000000L);
        dt -= y * 100000000L;

        int m = (int) (dt / 1000000L);
        dt -= m * 1000000L;

        int d = (int) (dt / 10000L);
        dt -= d * 10000L;

        int h = (int) (dt / 100L);
        dt -= h * 100L;

        int min = (int) dt;

        if (ret_type == DT_DATETIME)
            return getLeadingZero(d, 2) + "." + getLeadingZero(m, 2) + "." + y + "  " + getLeadingZero(h, 2) + ":"
                    + getLeadingZero(min, 2);
        else if (ret_type == DT_DATE)
            return getLeadingZero(d, 2) + "." + getLeadingZero(m, 2) + "." + y;
        else
            return getLeadingZero(h, 2) + ":" + getLeadingZero(min, 2);

    }

    public DateTime getDateTimeAsDateObject(long dt)
    {

        // Date dt_obj = new Date();
        //DateTime gc = new DateTime();

        int y = (int) (dt / 100000000L);
        dt -= y * 100000000L;

        int m = (int) (dt / 1000000L);
        dt -= m * 1000000L;

        int d = (int) (dt / 10000L);
        dt -= d * 10000L;

        int h = (int) (dt / 100L);
        dt -= h * 100L;

        int min = (int) dt;


//        DateTime dt = new DateTime(y, m, d, h, min, 0);

        return new DateTime(y, m, d, h, min, 0);
        
        /*
        gc.Day = .set(Calendar.DATE, d);
        gc.set(Calendar.MONTH, m - 1);
        gc.set(Calendar.YEAR, y);
        gc.set(Calendar.HOUR_OF_DAY, h);
        gc.set(Calendar.MINUTE, min);
        */


    }


    public long getDateTimeLong(long dt, int ret_type)
    {

        int y = (int) (dt / 100000000L);
        dt -= y * 100000000L;

        int m = (int) (dt / 1000000L);
        dt -= m * 1000000L;

        int d = (int) (dt / 10000L);
        dt -= d * 10000L;

        int h = (int) (dt / 100L);
        dt -= h * 100L;

        int min = (int) dt;

        if (ret_type == DT_DATETIME)
        {
            return Convert.ToInt64(y + getLeadingZero(m, 2) + getLeadingZero(d, 2) + getLeadingZero(h, 2)
                    + getLeadingZero(min, 2));
        }
        else if (ret_type == DT_DATE)
        {
            return Convert.ToInt32(getLeadingZero(d, 2) + getLeadingZero(m, 2) + y);
        }
        else
            return Convert.ToInt32(getLeadingZero(h, 2) + getLeadingZero(min, 2));

    }

    public long getDateTimeFromDateObject(DateTime dt)
    {

        //GregorianCalendar gc = new GregorianCalendar();
        //gc.setTime(dt);

        String dx = "";

        dx += "" + dt.Year;
        dx += "" + getLeadingZero(dt.Month, 2);
        dx += "" + getLeadingZero(dt.Day, 2);
        dx += "" + getLeadingZero(dt.Hour, 2);
        dx += "" + getLeadingZero(dt.Minute, 2);
        /*
                dx += "" +gc.get(Calendar.YEAR);
                dx += "" + getLeadingZero(gc.get(Calendar.MONTH + 1), 2);
                dx += "" + getLeadingZero(gc.get(Calendar.DAY_OF_MONTH), 2);
                dx += "" + getLeadingZero(gc.get(Calendar.HOUR_OF_DAY), 2);
                dx += "" + getLeadingZero(gc.get(Calendar.MINUTE), 2);
        */
        return Convert.ToInt64(dx);

    }

    // 1 = Db Date: yyyyMMdd
    // 2 = Db Full: yyyyMMddHHMM (24h format)
    public String getDateTimeStringFromDateTimeObject(DateTime dt, int type)
    {
        String st = "";

        if (dt.Year < 1000)
        {
            st += dt.Year +1900;
        }
        else
        {
            st += dt.Year; // gc.get(Calendar.YEAR);
        }

        st += getLeadingZero(dt.Month, 2);
        st += getLeadingZero(dt.Day, 2);

        if (type == 2)
        {
            st += getLeadingZero(dt.Hour, 2);
            st += getLeadingZero(dt.Minute, 2);
        }

        // System.out.println(st);

        return st;
    }

    public String getDateTimeString(int date, int time)
    {
        return getDateString(date) + " " + getTimeString(time);
    }

    public int getStartYear()
    {
        return 1970;
    }


    public bool isSameDay(DateTime day)
    {
        return isSameDay(m_date, day);
    }

    /*
    public boolean isDatabaseInitialized()
    {
        if ((m_db == null) || (m_db.getLoadStatus() < 2))
            return false;
        else
            return true;
    }*/

    public bool isSameDay(DateTime gc1, DateTime gc2)
    {

        if ((gc1 == null) || (gc2 == null))
        {
            return false;
        }
        else
        {

            if ((gc1.Day == gc2.Day) &&
                (gc1.Month == gc2.Month) &&
                (gc1.Year == gc2.Year))
            {
                return true;
            }
            else
            {
                return false;
            }

        }
    }

    
    
    /*
    public GregorianCalendar getGregorianCalendar(Date date)
    {
        GregorianCalendar gc = new GregorianCalendar();
        gc.setTime(date);

        return gc;
    }*/

    public static void notImplemented(String source)
    {
        Console.WriteLine("Not Implemented: " + source);
    }

    
    
    
    // **************************************************************************
    // ****                    String handling Methods                       ****
    // **************************************************************************
    
    
    
    public String getLeadingZero(int number, int places)
    {
        String nn = "" + number;

        while (nn.Length < places)
        {
            nn = "0" + nn;
        }

        return nn;
    }

    public String getLeadingZero(String number, int places)
    {
        number = number.Trim();

        while (number.Length < places)
        {
            number = "0" + number;
        }

        return number;
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
    public String replaceExpression(String input, String replace,
            String replacement)
    {

        int idx;
        if ((idx = input.IndexOf(replace)) == -1)
        {
            return input;
        }

        bool finished = false;
        
        while(!finished)
        {
        
            StringBuilder returning = new StringBuilder();
    
            while (idx != -1)
            {
                returning.Append(input.Substring(0, idx));
                returning.Append(replacement);
                input = input.Substring(idx + replace.Length);
                idx = input.IndexOf(replace);
            }
            returning.Append(input);
            
            input = returning.ToString();
            
            if ((idx = input.IndexOf(replace))==-1)
            {
                finished = true;
            }

        }

        return input;

    }
    


    public String parseExpression(String inv, String expression, String replace)
    {

        StringBuilder buffer;

        int idx=inv.IndexOf(expression);
        
        if (replace==null)
            replace ="";
        
        if (idx==-1)
            return inv;

        buffer = new StringBuilder();
        
        while (idx!=-1)
        {
            buffer.Append(inv.Substring(0,idx));
            buffer.Append(replace);

            inv = inv.Substring(idx+expression.Length);
            
            idx=inv.IndexOf(expression);
        }

        buffer.Append(inv);

        return buffer.ToString();

    }



    public String parseExpressionFull(String inv, String expression, String replace)
    {

        String buffer;

        int idx=inv.IndexOf(expression);
        
        if (replace==null)
            replace ="";
        
        if (idx==-1)
            return inv;

        buffer = "";
        
        if (idx!=-1)
        {
            
            buffer = inv.Substring(0,idx) + replace + inv.Substring(idx+expression.Length);
            
            idx=inv.IndexOf(expression);

            if (idx!=-1) 
                buffer = parseExpressionFull(buffer,expression,replace);

        }

        return buffer;

    }
    
    
    public bool isEmptyOrUnset(String val)
    {
        if ((val == null) || (val.Trim().Length==0))
        {
            return true;
        }
        else
            return false;
    }

    
    public static bool isFound(String text, String search_str)
    {

        if ((search_str.Trim().Length == 0) || (text.Trim().Length == 0))
            return true;

        return text.Trim().IndexOf(search_str.Trim()) != -1;
    }


    public String[] splitString(String input, String delimiter)
    {
        
        String[] res = null;

        

        if (!input.Contains(delimiter))
        {
            res = new String[1];
            res[0] = input;
        }
        else
        {

            //String[] arr = 
            return input.Split(delimiter.ToCharArray());
            /*
            StringTokenizer strtok = new StringTokenizer(input, delimiter);

            res = new String[strtok.countTokens()];
            int i = 0;

            while (strtok.hasMoreTokens())
            {
                res[i] = strtok.nextToken().trim();
                i++;
            }
             */
        }

        return res;

    }
    
    
    
    
    // ********************************************************
    // ****** Get Values From Object *****
    // ********************************************************
    /*
    public float getFloatValue(Object aValue)
    {
        float outv = 0.0f;

        // System.out.println("getFloatValue: ");

        if (aValue == null)
            return outv;

        // System.out.println("getFloatValue: NOT NULL");

        if (aValue.GetType()==typeof(float))
        {
            try
            {
                Float f = (Float) aValue;
                outv = f.floatValue();
            }
            catch (Exception ex)
            {
            }
        }
        else if (aValue instanceof Double)
        {
            try
            {
                Double f = (Double) aValue;
                outv = f.floatValue();
            }
            catch (Exception ex)
            {
            }
        }
        else if (aValue instanceof Integer)
        {
            try
            {
                Integer f = (Integer) aValue;
                outv = f.floatValue();
            }
            catch (Exception ex)
            {
            }
        }
        else if (aValue instanceof String)
        {
            String s = (String) aValue;
            if (s.length() > 0)
            {
                try
                {
                    outv = Float.parseFloat(s);
                }
                catch (Exception ex)
                {
                }
            }
        }

        return out;
    }
    
    
    
    public int getIntValue(Object aValue)
    {
        int out = 0;

        if (aValue == null)
            return out;

        if (aValue instanceof Integer)
        {
            try
            {
                Integer i = (Integer) aValue;
                out = i.intValue();
            }
            catch (Exception ex)
            {
            }
        }
        else if (aValue instanceof String)
        {
            String s = (String) aValue;
            if (s.length() > 0)
            {
                try
                {
                    out = Integer.parseInt(s);
                }
                catch (Exception ex)
                {
                }
            }
        }

        return out;
    }

    public long getLongValue(Object aValue)
    {
        long out = 0L;

        if (aValue == null)
            return out;

        if (aValue instanceof Long)
        {
            try
            {
                Long i = (Long) aValue;
                out = i.longValue();
            }
            catch (Exception ex)
            {
            }
        }
        else if (aValue instanceof String)
        {
            String s = (String) aValue;
            if (s.length() > 0)
            {
                try
                {
                    out = Long.parseLong(s);
                }
                catch (Exception ex)
                {
                }
            }
        }

        return out;
    }

    // ********************************************************
    // ****** Get Values From String *****
    // ********************************************************

    public float getFloatValueFromString(String aValue)
    {
        return this.getFloatValueFromString(aValue, 0.0f);
    }

    public float getFloatValueFromString(String aValue, float def_value)
    {
        float out = def_value;

        try
        {
            out = Float.parseFloat(aValue);
        }
        catch (Exception ex)
        {
            System.out.println("Error on parsing string to get float [" + aValue + "]:"
                    + ex);
        }

        return out;
    }

    public int getIntValueFromString(String aValue)
    {
        return this.getIntValueFromString(aValue, 0);
    }

    public int getIntValueFromString(String aValue, int def_value)
    {
        int out = def_value;

        try
        {
            out = Integer.parseInt(aValue);
        }
        catch (Exception ex)
        {
            System.out.println("Error on parsing string to get int [" + aValue + "]:"
                    + ex);
        }

        return out;
    }

    public long getLongValueFromString(String aValue)
    {
        return this.getLongValueFromString(aValue, 0L);
    }

    public long getLongValueFromString(String aValue, long def_value)
    {
        long out = def_value;

        try
        {
            out = Long.parseLong(aValue);
        }
        catch (Exception ex)
        {
            System.out.println("Error on parsing string to get long [" + aValue + "]:"
                    + ex);
        }

        return out;
    }
    */
    
}
}