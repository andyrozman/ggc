package ggc.core.util;

import ggc.core.data.Converter_mgdL_mmolL;
import ggc.core.data.DailyValues;
import ggc.core.data.ExtendedDailyValue;
import ggc.core.data.HbA1cValues;
import ggc.core.data.WeeklyValues;
import ggc.core.data.cfg.ConfigurationManager;
import ggc.core.db.GGCDb;
import ggc.core.db.GGCDbLoader;
import ggc.core.db.datalayer.DailyValue;
import ggc.core.db.datalayer.Settings;
import ggc.core.db.datalayer.SettingsColorScheme;
import ggc.core.db.tool.DbToolApplicationGGC;
import ggc.core.plugins.CGMSPlugIn;
import ggc.core.plugins.MetersPlugIn;
import ggc.core.plugins.NutriPlugIn;
import ggc.core.plugins.PumpsPlugIn;

import java.awt.Color;
import java.awt.Component;
import java.io.File;
import java.io.FileInputStream;
import java.text.DecimalFormat;
import java.util.Calendar;
import java.util.Enumeration;
import java.util.GregorianCalendar;
import java.util.Hashtable;
import java.util.Properties;

import javax.swing.ImageIcon;
import javax.swing.JFrame;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import pygmy.core.Server;

import com.atech.db.hibernate.HibernateDb;
import com.atech.db.hibernate.transfer.BackupRestoreCollection;
import com.atech.help.HelpContext;
import com.atech.i18n.mgr.LanguageManager;
import com.atech.misc.refresh.EventObserverInterface;
import com.atech.misc.refresh.EventSource;
import com.atech.plugin.PlugInClient;
import com.atech.utils.ATDataAccessLMAbstract;
import com.atech.utils.Rounding;
import com.atech.utils.logs.RedirectScreen;

/**
 *  Application:   GGC - GNU Gluco Control
 *
 *  See AUTHORS for copyright information.
 * 
 *  This program is free software; you can redistribute it and/or modify it under
 *  the terms of the GNU General Public License as published by the Free Software
 *  Foundation; either version 2 of the License, or (at your option) any later
 *  version.
 * 
 *  This program is distributed in the hope that it will be useful, but WITHOUT
 *  ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 *  FOR A PARTICULAR PURPOSE. See the GNU General Public License for more
 *  details.
 * 
 *  You should have received a copy of the GNU General Public License along with
 *  this program; if not, write to the Free Software Foundation, Inc., 59 Temple
 *  Place, Suite 330, Boston, MA 02111-1307 USA
 * 
 *  Filename:     DataAccess
 *  Description:  This class is singelton instance and contains most of utility 
 *                methods and also hold instances of several important classes
 *                used through whole application it also hold most of "static" data.      
 * 
 *  Author: andyrozman {andy@atech-software.com}  
 */


public class DataAccess extends ATDataAccessLMAbstract 
{

    /**
     * Core Version
     */
    public static String CORE_VERSION = "0.4.18";
    
    /**
     * Current Db Version
     */
    public String current_db_version = "7";

    
    /**
     * At later time this will be determined by user management part
     */
    public long current_user_id = 1;
    
    private static Log log = LogFactory.getLog(DataAccess.class);

    private Hashtable<String,EventSource> observables = null;

    
    private static DataAccess s_da = null; // This is handle to unique singelton instance

    private GGCDb m_db = null;

    private Component m_main = null;


    // daily and weekly data
    private GregorianCalendar m_date = null, m_dateStart = null;

    private HbA1cValues m_HbA1c = null;
    private DailyValues m_dvalues = null;
    private WeeklyValues m_dRangeValues = null;

    private GGCProperties m_settings = null;
    private DbToolApplicationGGC m_configFile = null;
    private ConfigurationManager m_cfgMgr = null;

    
    /**
     * Decimal with zero decimals
     */
    public static DecimalFormat Decimal0Format = new DecimalFormat("#0");

    /**
     * Decimal with 1 decimal
     */
    public static DecimalFormat Decimal1Format = new DecimalFormat("#0.0");

    /**
     * Decimal with 2 decimal
     */
    public static DecimalFormat Decimal2Format = new DecimalFormat("#0.00");

    /**
     * Decimal with 3 decimal
     */
    public static DecimalFormat Decimal3Format = new DecimalFormat("#0.000");

    /**
     * Which BG unit is used: BG_MGDL = mg/dl, BG_MMOL = mmol/l
     */
//    public int m_BG_unit = BG_MGDL;

    private String[] availableLanguages = { "English", "Deutsch", "Slovenski", "Fran\u00e7ais", "Language Tool" };
  
    /**
     * Available Language Extensions (posfixes)
     */
    public String[] avLangPostfix = { "en", "de", "si", "fr", "lt" };

    
    //public Locale[] realLocales = { Locale.ENGLISH, Locale.GERMANY, Locale.
    
    
    /**
     * BG Units
     */
    public String[] bg_units = { "", "mg/dl", "mmol/l" };

    
    /**
     * BG Units for configuration
     */
    public String[] bg_units_config = { "mg/dl", "mmol/l" };
    
//    public Hashtable<String, String> timeZones;

    //public ArrayList<Container> parents_list;

    /**
     * Config Icons 
     */
    public ImageIcon config_icons[] = null;

    /**
     * Plug In - Meter Tool
     */
    public static final String PLUGIN_METERS = "MetersPlugIn";

    /**
     * Plug In - Pump Tool
     */
    public static final String PLUGIN_PUMPS = "PumpsPlugIn";

    /**
     * Plug In - CGMS Tool
     */
    public static final String PLUGIN_CGMS = "CGMSPlugIn";


    /**
     * Plug In - Nutrition Tool
     */
    public static final String PLUGIN_NUTRITION = "NutritionPlugIn";
    
    
    /**
     * GGC Mode: Pen/Injection
     */
    public static final int GGC_MODE_PEN_INJECTION = 0;
    
    
    /**
     * GGC Mode: Pump
     */
    public static final int GGC_MODE_PUMP = 1;

    
    /**
     * Converter: BG     
     */
    public static final String CONVERTER_BG = "BG";
    
    
    
    
    private int current_person_id = 1;
    //NutriI18nControl m_nutri_i18n = NutriI18nControl.getInstance();
    
    /**
     * Developer Version
     */
    public boolean developer_version = false;

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
    private DataAccess()
    {
        super(new LanguageManager(new GGCLanguageManagerRunner()), new GGCCoreICRunner());
        initSpecial();
    }

    
    /**
     * Init Special
     */
    @Override
    public void initSpecial()
    {
        doTest();
        this.initObservable();
        
        //System.out.println("init Special");
        //this.tree_roots = new Hashtable<String, GGCTreeRoot>();

        // Help Context Init
        HelpContext hc = new HelpContext("../data/help/en/GGC.hs");
        this.setHelpContext(hc);
        
        
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

        this.loadGraphConfigProperties();        
        this.startWebServer();
        this.loadSpecialParameters();
        this.loadConverters();
        
        /*
        System.out.println(Locale.getAvailableLocales());
        
        Locale[] lcls = Locale.getAvailableLocales();
        
        for(int i=0; i<lcls.length; i++)
        {
            System.out.println(lcls[i].getDisplayName() + "," + lcls[i].getISO3Country() + "," + lcls[i].getISO3Language());
        }
        */
        
    }    

    
    /**
     * Run After Db Load
     */
    public void runAfterDbLoad()
    {
        loadSpecialParameters();        
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
    public static DataAccess getInstance()
    {
        if (s_da == null)
            s_da = new DataAccess();
        return s_da;
    }

    /**
     * Create Instance
     * 
     * @param main
     * @return
     */
    public static DataAccess createInstance(Component main)
    {
        s_da = null;

        if (s_da == null)
        {
            //System.out.println("!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!  " + main);
            // GGCDb db = new GGCDb();

            
            
            //System.out.println("create new Instance");
            s_da = new DataAccess();
            //System.out.println("setParent");
            s_da.setParent(main);
            //System.out.println("setMainParent");
            s_da.setMainParent((JFrame)main);
            // System.out.println("addComponet");
            // s_da.addComponent(main);
        }

        return s_da;
    }

    /**
     * Create Instance
     * 
     * @param main
     * @return
     */
/*    public static DataAccess createInstance(GGCLittle main)
    {
        if (s_da == null)
        {
            // GGCDb db = new GGCDb();
            s_da = new DataAccess();
            s_da.setParent(main);
            s_da.setMainParent(main);
        }

        return s_da;
    }
*/
    /*
     * static public DataAccess getInstance() { return m_da; }
     */

    // Method: deleteInstance
    /**
     * This method sets handle to DataAccess to null and deletes the instance. <br>
     * <br>
     */
    public static void deleteInstance()
    {
        // m_i18n = null;
        DataAccess.s_da = null;
    }

    /**
     * Start Db
     */
    public void startDb() //StatusBar bar)
    {
        GGCDbLoader loader = new GGCDbLoader(this);
        loader.start();
    }

    /**
     * Start Db
     * 
     * @param bar2
     */
/*    public void startDb(StatusBarL bar2)
    {
        GGCDbLoader loader = new GGCDbLoader(this, bar2);
        loader.start();
     }
*/
    /**
     * Get Db
     * @return
     */
    public GGCDb getDb()
    {
        return m_db;
    }

    /**
     * Set Db
     * 
     * @param db
     */
    public void setDb(GGCDb db)
    {
        this.m_db = db;
    }


    
    /**
     * Is Pen/Injection Mode
     * 
     * @return
     */
    public boolean isPenInjectionMode()
    {
        return (getSoftwareMode()==GGC_MODE_PEN_INJECTION);
    }
    
    
    /**
     * Is Pump Mode
     * 
     * @return
     */
    public boolean isPumpMode()
    {
        return (getSoftwareMode()==GGC_MODE_PUMP);
    }
    
    
    /**
     * Get Software Mode
     * 
     * @return
     */
    public int getSoftwareMode()
    {
        //System.out.println("Sw Mode: " + this.m_cfgMgr.getIntValue("SW_MODE"));
        return this.m_cfgMgr.getIntValue("SW_MODE");
    }
    
    /**
     * Get Software Mode Description
     * 
     * @return
     */
    public String getSoftwareModeDescription()
    {
        //System.out.println("Sw Mode Desc: " + this.m_cfgMgr.getStringValue("SW_MODE_DESC"));

        return this.m_cfgMgr.getStringValue("SW_MODE_DESC");
    }
    
    
    // ********************************************************
    // ****** Static Methods *****
    // ********************************************************

    /**
     * Get Float As String
     * 
     * @param f
     * @param decimal_places
     * @return
     */
    public static String getFloatAsString(float f, String decimal_places)
    {
        return DataAccess.getFloatAsString(f, Integer.parseInt(decimal_places));
    }

    /**
     * Get Float As String
     * 
     * @param f
     * @param decimal_places
     * @return
     */
    public static String getFloatAsString(float f, int decimal_places)
    {
        switch (decimal_places)
        {
        case 1:
            return DataAccess.Decimal1Format.format(f);

        case 2:
            return DataAccess.Decimal2Format.format(f);

        case 3:
            return DataAccess.Decimal3Format.format(f);

        default:
            return DataAccess.Decimal0Format.format(f);
        }
    }

    // ********************************************************
    // ****** Abstract Methods *****
    // ********************************************************

    /**
     * Get Application Name
     * 
     * @return
     */
    @Override
    public String getApplicationName()
    {
        return "GGC";
    }

    /**
     * Check Prerequisites
     */
    @Override
    public void checkPrerequisites()
    {
        // check that ../data/temp exists (needed for printing)

        File f = new File("../data/temp/");
        if (!f.exists())
        {
            f.mkdir();
        }
    }

    /**
     * Get Images Root (Must have ending back-slash)
     * 
     * @return
     */
    @Override
    public String getImagesRoot()
    {
        return "/icons/";
    }

    /**
     * Load Backup Restore Collection
     */
    @Override
    public void loadBackupRestoreCollection()
    {

        BackupRestoreCollection brc_full = new BackupRestoreCollection("GGC_BACKUP", this.m_i18n);
        brc_full.addNodeChild(new DailyValue(this.m_i18n));


        BackupRestoreCollection brc1 = new BackupRestoreCollection("CONFIGURATION", this.m_i18n);
        brc1.addNodeChild(new Settings(this.m_i18n));
        brc1.addNodeChild(new SettingsColorScheme(this.m_i18n));
        brc_full.addNodeChild(brc1);
        
        
        //for(int i=0; i<)
        
        for(Enumeration<String> en = this.plugins.keys(); en.hasMoreElements(); )
        {
            PlugInClient pic = this.plugins.get(en.nextElement());
            
            BackupRestoreCollection brc = pic.getBackupObjects();
            
            if (brc!=null)
                brc_full.addNodeChild(brc);
        }
        
        
        /*
        BackupRestoreCollection brc_nut = new BackupRestoreCollection("NUTRITION_OBJECTS", this.m_i18n);
        brc_nut.addNodeChild(new FoodGroup(this.m_i18n));
        brc_nut.addNodeChild(new FoodDescription(this.m_i18n));
        brc_nut.addNodeChild(new MealGroup(this.m_i18n));
        brc_nut.addNodeChild(new Meal(this.m_i18n));
        brc.addNodeChild(brc_nut);

        brc_nut = new BackupRestoreCollection("PUMP_TOOL", this.m_i18n);
        brc_nut.addNodeChild(new PumpData(this.m_i18n));
        brc_nut.addNodeChild(new PumpDataExtended(this.m_i18n));
        brc_nut.addNodeChild(new PumpProfile(this.m_i18n));
        brc.addNodeChild(brc_nut);
        */
        
        this.backup_restore_collection = brc_full;
    }

    
    /** 
     * Get BackupRestoreCollection
     */
    public BackupRestoreCollection getBackupRestoreCollection()
    {
        BackupRestoreCollection brc_full = new BackupRestoreCollection("GGC_BACKUP", this.m_i18n);
        brc_full.addNodeChild(new DailyValue(this.m_i18n));


        BackupRestoreCollection brc1 = new BackupRestoreCollection("CONFIGURATION", this.m_i18n);
        brc1.addNodeChild(new Settings(this.m_i18n));
        brc1.addNodeChild(new SettingsColorScheme(this.m_i18n));
        brc_full.addNodeChild(brc1);
        
        
        //for(int i=0; i<)
        
        for(Enumeration<String> en = this.plugins.keys(); en.hasMoreElements(); )
        {
            PlugInClient pic = this.plugins.get(en.nextElement());
            
            if (pic.isBackupRestoreEnabled())
                brc_full.addNodeChild(pic.getBackupObjects());
            
/*            BackupRestoreCollection brc = pic.getBackupObjects();
            
            if (brc!=null)
                brc_full.addNodeChild(brc); */
        }
        

        return brc_full;
        
        
//        return null;
    }
    
    
    
    /**
     * Load Graph Config Properties
     */
    @Override
    public void loadGraphConfigProperties()
    {
        this.graph_config = this.m_settings;
    }
    
    
    // ********************************************************
    // ****** Icons *****
    // ********************************************************

    private void loadIcons()
    {
        config_icons = new ImageIcon[7];
        config_icons[0] = new ImageIcon(getImage("/icons/cfg_mode.png", m_main));
        config_icons[1] = new ImageIcon(getImage("/icons/cfg_general.png", m_main));
        config_icons[2] = new ImageIcon(getImage("/icons/cfg_medical.png", m_main));
        config_icons[3] = new ImageIcon(getImage("/icons/cfg_colors.png", m_main));
        config_icons[4] = new ImageIcon(getImage("/icons/cfg_render.png", m_main));
        config_icons[5] = new ImageIcon(getImage("/icons/cfg_print.png", m_main));
        config_icons[6] = new ImageIcon(getImage("/icons/cfg_lang.png", m_main));
        // config_icons[4] = new ImageIcon(getImage("/icons/cfg_meter.png",
        // m_main));

    }

    // ********************************************************
    // ****** Db *****
    // ********************************************************

    /**
     * Get Db Config (DbToolApplicationGGC)
     * 
     * @return
     */
    public DbToolApplicationGGC getDbConfig()
    {
        return this.m_configFile;
    }

    

    // ********************************************************
    // ****** Settings *****
    // ********************************************************

    /**
     * Get Settings
     * 
     * @return
     */
    public GGCProperties getSettings()
    {
        return this.m_settings;
    }

    /**
     * Load Settings from Db
     */
    public void loadSettingsFromDb()
    {
        this.m_settings.load();
    }

    /**
     * Get Color
     * 
     * @param color
     * @return
     */
    public Color getColor(int color)
    {
        return new Color(color);
    }

    /**
     * Get Configuration Manager (Db)
     * 
     * @return
     */
    public ConfigurationManager getConfigurationManager()
    {
        return this.m_cfgMgr;
    }

    
    /**
     * Init PlugIns
     */
    public void initPlugIns()
    {
        
        /*
        log.debug("init Plugins: Meter Tool");
        addPlugIn(DataAccess.PLUGIN_METERS, new MetersPlugIn(this.m_main, this.m_i18n));

        log.debug("init Plugins: Pumps Tool");
        addPlugIn(DataAccess.PLUGIN_PUMPS, new PumpsPlugIn(this.m_main, this));

        log.debug("init Plugins: CGMS Tool");
        addPlugIn(DataAccess.PLUGIN_CGMS, new CGMSPlugIn(this.m_main, this.m_i18n));

        log.debug("init Plugins: Nutrition Tool");
        addPlugIn(DataAccess.PLUGIN_NUTRITION, new NutriPlugIn(this.m_main, this.m_i18n));
        */
    }
    
    
    
    /**
     * Get Hibernate Db
     */
    public HibernateDb getHibernateDb()
    {
        return this.m_db;
    }
    
    
    // ********************************************************
    // ******         Observer/Observable                 *****
    // ********************************************************
    
    /**
     * Observable: Panels
     */
    public static final int OBSERVABLE_PANELS = 1;

    
    /**
     * Observable: Status
     */
    public static final int OBSERVABLE_STATUS = 2;
    
    
    
    /**
     * Init Observable
     */
    public void initObservable()
    {
        observables = new Hashtable<String,EventSource>();

        observables.put("" + OBSERVABLE_PANELS, new EventSource());
        observables.put("" + OBSERVABLE_STATUS, new EventSource());
    }

    /**
     * Start To Observe
     */
    public void startToObserve()
    {
        /*
        // starts the event thread
        Thread thread = new Thread(observables.get("1"));
        thread.start();

        thread = new Thread(observables.get("2"));
        thread.start();
        */
        
    }
    
    
    /**
     * Add Observer 
     * 
     * @param observable_id
     * @param inst
     */
    public void addObserver(int observable_id, EventObserverInterface inst)
    {
        observables.get("" + observable_id).addObserver(inst);
    }
    
    /**
     * Set Change On Event Source
     * 
     * @param type
     * @param value
     */
    public void setChangeOnEventSource(int type, int value)
    {
        observables.get("" + type).sendChangeNotification(value); 
    }
    
    /**
     * Set Change On Event Source
     * 
     * @param type
     * @param value
     */
    public void setChangeOnEventSource(int type, String value)
    {
        observables.get("" + type).sendChangeNotification(value); 
    }
    
    
    
    
    // ********************************************************
    // ****** Language *****
    // ********************************************************

    /**
     * Get Available Languages
     * 
     * @return
     */
    public String[] getAvailableLanguages()
    {
        return this.availableLanguages;
    }

    /**
     * Get Selected Language Index
     * 
     * @return
     */
    public int getSelectedLanguageIndex()
    {
        return this.getLanguageIndex(this.getSettings().getLanguage());
    }

    /**
     * Get Language Index
     * 
     * @param postfix
     * @return
     */
    public int getLanguageIndex(String postfix)
    {
        // System.out.println(postfix);

        for (int i = 0; i < this.avLangPostfix.length; i++)
        {
            if (this.avLangPostfix[i].equals(postfix))
                return i;
            
        }

        return 0;
    }

    /**
     * Get Language Index By Name
     * 
     * @param name
     * @return
     */
    public int getLanguageIndexByName(String name)
    {
        // stem.out.println(name);

        for (int i = 0; i < this.availableLanguages.length; i++)
        {
            if (this.availableLanguages[i].equals(name))
                return i;
        }

        return 0;
    }

    // ********************************************************
    // ****** BG Measurement Type *****
    // ********************************************************

    /**
     * BG: mg/dL
     */
    public static final int BG_MGDL = 1;

    /**
     * BG: mmol/L
     */
    public static final int BG_MMOL = 2;

    /**
     * Get Measurment Type
     * 
     * @return
     */
    public int getBGMeasurmentType()
    {
        return this.m_settings.getBG_unit();
    }

    //String[] bg_types = { "", "mg/dL", "mmol/L"};
    
    /**
     * Get Measurment Type
     * 
     * @return
     */
    public String getBGMeasurmentTypeString()
    {
        return this.bg_units[getBGMeasurmentType()];
    }
    
    
    /**
     * Set Measurment Type
     * 
     * @param type 
     */
    public void setBGMeasurmentType(int type)
    {
        
        //this.m_BG_unit = type;
    }

    private static final float MGDL_TO_MMOL_FACTOR = 0.0555f;

    private static final float MMOL_TO_MGDL_FACTOR = 18.016f;

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
        switch (this.getBGMeasurmentType())
        {
        case BG_MMOL:
            return this.converters.get("BG").getValueDifferent(Converter_mgdL_mmolL.UNIT_mg_dL, dbValue);
            // this POS should return a float rounded to 3 decimal places,
            // if I understand the docu correctly
//            return (new BigDecimal(dbValue * MGDL_TO_MMOL_FACTOR, new MathContext(3, RoundingMode.HALF_UP))
//                    .floatValue());
        case BG_MGDL:
        default:
            return dbValue;
        }
    }

    /**
     * Get BG Value
     * 
     * @param bg_value
     * @return
     */
    public float getBGValue(float bg_value)
    {
        switch (this.getBGMeasurmentType())
        {
        case BG_MMOL:
            return (bg_value * MGDL_TO_MMOL_FACTOR);
        case BG_MGDL:
        default:
            return bg_value;
        }

    }

    /**
     * Get BG Value By Type
     * 
     * @param type
     * @param bg_value
     * @return
     */
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

    /**
     * Get BG Value By Type
     * 
     * @param input_type
     * @param output_type
     * @param bg_value
     * @return
     */
    public float getBGValueByType(int input_type, int output_type, float bg_value)
    {

        if (input_type == output_type)
            return bg_value;
        else
        {
            if (output_type == DataAccess.BG_MGDL)
            {
                return bg_value * DataAccess.MGDL_TO_MMOL_FACTOR;
            }
            else
            {
                return bg_value * DataAccess.MMOL_TO_MGDL_FACTOR;
            }
        }

    }

    /**
     * Get BG Value Different
     * 
     * @param type
     * @param bg_value
     * @return
     */
    public float getBGValueDifferent(int type, float bg_value)
    {

        if (type == DataAccess.BG_MGDL)
        {
            return bg_value * DataAccess.MGDL_TO_MMOL_FACTOR;
        }
        else
        {
            return bg_value * DataAccess.MMOL_TO_MGDL_FACTOR;
        }

    }


    // ********************************************************
    // ****** Parent handling (for UIs) *****
    // ********************************************************
    
    
    /**
     * Set Parent
     * 
     * @param main
     */
    public void setParent(Component main)
    {
        m_main = main;
        loadIcons();
    }

    /**
     * Set Parent
     * 
     * @param main
     */
/*    public void setParent(GGCLittle main)
    {
        m_main_little = main;
    }*/

    /**
     * Get Parent
     */
    @Override
    public Component getParent()
    {
        return m_main;
    }

    /**
     * Get Parent Little
     * 
     * @return 
     */
/*    public GGCLittle getParentLittle()
    {
        return m_main_little;
    }
*/

    /**
     * Utils
     */
/*
    @Override
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

    /**
     * Get Current Person Id
     * 
     * @return
     */
    public int getCurrentPersonId()
    {
        return this.current_person_id;
    }

    // ********************************************************
    // ****** I18n Utils *****
    // ********************************************************


    /**
     * Get Nutrition I18n Control
     * 
     * @return
     */
/*    public NutriI18nControl getNutriI18nControl()
    {
        return this.m_nutri_i18n;
    }
*/
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

    /**
     * Get Look & Feel Data
     * 
     * @return
     */
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
            System.out.println("DataAccess::getLFData::Exception> " + ex);
            return null;
        }
    }

    // ********************************************************
    // ****** Component Id *****
    // ********************************************************

    private long component_id_last;

    /**
     * Get New Component Id
     * 
     * @return
     */
    public String getNewComponentId()
    {
        component_id_last++;
        return "" + this.component_id_last;
    }

    // ********************************************************
    // ****** Options *****
    // ********************************************************

    /**
     * Load Options
     */
    public void loadOptions()
    {
        this.options_yes_no = new String[2];
        this.options_yes_no[0] = m_i18n.getMessage("YES");
        this.options_yes_no[1] = m_i18n.getMessage("NO");
    }

    // ********************************************************
    // ****** Dates and Times Handling *****
    // ********************************************************

 
    /**
     * Get Start Year
     */
    @Override
    public int getStartYear()
    {
        return 1800;
    }

    
    
    /**
     * Load Daily Settings
     * 
     * @param day
     * @param force
     */
    public synchronized void loadDailySettings(GregorianCalendar day, boolean force)
    {
        if ((m_db == null) || (m_db.getLoadStatus() < 2))
            return;

        if ((isSameDay(day)) && (!force))
            return;

        //System.out.println("Reload daily settings (force:" + force + ")");
        log.debug("Reload daily settings (force:" + force + ")");

        m_date = day;
        m_HbA1c = m_db.getHbA1c(day, force);
        m_dvalues = m_db.getDayStats(day);

        m_dateStart = (GregorianCalendar) day.clone();
        m_dateStart.add(Calendar.DAY_OF_MONTH, -6);

        m_dRangeValues = m_db.getDayStatsRange(m_dateStart, m_date);
    }


    /**
     * Load Daily Settings (Little)
     * 
     * @param day
     * @param force
     */
    public synchronized void loadDailySettingsLittle(GregorianCalendar day, boolean force)
    {
        if ((m_db == null) || (m_db.getLoadStatus() < 2))
            return;

        if ((isSameDay(day)) && (!force))
            return;

        //System.out.println("(Re)Load daily settings Little - (force:" + force + ")");
        log.debug("(Re)Load daily settings Little - (force:" + force + ")");

        m_date = day;
        // m_HbA1c = m_db.getHbA1c(day);
        m_dvalues = m_db.getDayStats(day);

        // m_dateStart = (GregorianCalendar) day.clone();
        // m_dateStart.add(GregorianCalendar.DAY_OF_MONTH, -6);
        // m_dateEnd = day;

        // m_dRangeValues = m_db.getDayStatsRange(m_dateStart, m_date);
    }

    /**
     * Get HbA1c
     * 
     * @param day
     * @return
     */
    public HbA1cValues getHbA1c(GregorianCalendar day)
    {
        // System.out.println("DA::getHbA1c");
        // if (!isSameDay(day))
        loadDailySettings(day, false);

        // if (m_HbA1c==null)
        // return new HbA1cValues();
        // else
        return m_HbA1c;
    }

    /**
     * Get Day Stats
     * 
     * @param day
     * @return
     */
    public DailyValues getDayStats(GregorianCalendar day)
    {
        // System.out.println("DA::getDayStats");

        // if (!isSameDay(day))
        loadDailySettings(day, false);

        return m_dvalues;
    }

    /**
     * Get Day Stats Range
     * 
     * @param start
     * @param end
     * @return
     */
    public WeeklyValues getDayStatsRange(GregorianCalendar start, GregorianCalendar end)
    {
        // System.out.println("DA::getDayStatsRange");

        // we load dialy if not loaded
        if (this.m_date == null)
            loadDailySettings(end, false);

        if ((isSameDay(start, this.m_dateStart)) && (isSameDay(m_date, end)))
        {
            // System.out.println("Same day");
            return m_dRangeValues;
        }
        else
        {
            // System.out.println("other range");
            return m_db.getDayStatsRange(start, end);
        }
    }

    /**
     * Is Same Day (if we compare current day with day we got stats for main display from)
     * 
     * @param gc
     * @return
     */
    public boolean isSameDay(GregorianCalendar gc)
    {
        return isSameDay(m_date, gc);
    }

    /**
     * Is Database Initialized
     * 
     * @return
     */
    public boolean isDatabaseInitialized()
    {
        if ((m_db == null) || (m_db.getLoadStatus() < 2))
            return false;
        else
            return true;
    }

    /**
     * Is Same Day (GregorianCalendars)
     * 
     * @param gc1
     * @param gc2 
     * @return
     */
    public boolean isSameDay(GregorianCalendar gc1, GregorianCalendar gc2)
    {

        if ((gc1 == null) || (gc2 == null))
        {
            return false;
        }
        else
        {

            if ((gc1.get(Calendar.DAY_OF_MONTH) == gc2.get(Calendar.DAY_OF_MONTH))
                    && (gc1.get(Calendar.MONTH) == gc2.get(Calendar.MONTH))
                    && (gc1.get(Calendar.YEAR) == gc2.get(Calendar.YEAR)))
            {
                return true;
            }
            else
            {
                return false;
            }

        }
    }

    
    /**
     * Start Internal Web Server
     */
    public void startWebServer()
    {
        try
        {
            
/*            Properties p = new Properties();
            
            p.put("http.port", "444");
            p.put("handler", "chain");
    
            p.put("chain.chain", "root plug_pump plug_cgm"); 
            p.put("chain.class", "pygmy.handlers.DefaultChainHandler");
    
            p.put("root.class", "pygmy.handlers.ResourceHandler");
            p.put("root.url-prefix", "/meters/");
            p.put("root.resourceMount", "/html/meters");
    
            p.put("plug_pump.class", "pygmy.handlers.ResourceHandler");
            p.put("plug_pump.url-prefix", "/pumps/");
            p.put("plug_pump.resourceMount", "/html/pumps");
    
            p.put("plug_cgm.class", "pygmy.handlers.ResourceHandler");
            p.put("plug_cgm.url-prefix", "/cgms/");
            p.put("plug_cgm.resourceMount", "/html/cgms");
    
            p.put("mime.html", "text/html");
            p.put("mime.zip", "application/x-zip-compressed");
            p.put("mime.gif", "image/gif");
            p.put("mime.jpeg", "image/jpeg");
            p.put("mime.jpg", "image/jpeg"); */
            
            //System.out.println("Start internal web server");
            
            log.info("Start internal Web Server");
            
            String[] cnf = { "-config", "../data/tools/WebLister.properties" };
            
            Server web_server = new Server(cnf);
            web_server.start();
            
        }
        catch(Exception ex)
        {
            System.out.println("Error starting WebServer on 444. Ex: " + ex);
        }
        
    }
    
    

    /**
     * Console message Not Implemented
     * @param source
     */
    public static void notImplemented(String source)
    {
        System.out.println("Not Implemented: " + source);
    }


    
    /**
     * Load Special Parameters
     * 
     * @see com.atech.utils.ATDataAccessAbstract#loadSpecialParameters()
     */
    public void loadSpecialParameters()
    {
        this.special_parameters = new Hashtable<String,String>();
        this.special_parameters.put("BG", "" + this.m_settings.getBG_unit());
        //this.m_BG_unit = this.m_settings.getBG_unit();
    }


    

    /**
     * This method is intended to load additional Language info. Either special langauge configuration
     * or special data required for real Locale handling.
     */
    @Override
    public void loadLanguageInfo()
    {
        // TODO Auto-generated method stub
    }
    

    
    /**
     * Get Selected Lang Index (will be deprecated) ??!!
     */
    public int getSelectedLangIndex()
    {
        return 0;
    }

    
    /**
     * Set Selected Lang Index (will be deprecated) ??!!
     */
    public void setSelectedLangIndex(int index)
    {
        
    }
    

    /**
     * Insulin - Pen/Injection 
     */
    public static final int INSULIN_PEN_INJECTION = 0;
    
    /**
     * Insulin - Pump 
     */
    public static final int INSULIN_PUMP = 1;
    
    /**
     * @param mode
     * @param value
     * @return
     */
    public float getCorrectDecimalValueForInsulinFloat(int mode, float value)
    {
        // 1, 0.5, 0.1
        // 1, 0.5, 0.1, 0.05, 0.01, 0.005, 0.001
        return value;
    }
    
    /**
     * @param mode
     * @param value
     * @return
     */
    public String getCorrectDecimalValueForInsulinString(int mode, float value)
    {
        // 1, 0.5, 0.1
        //return value;
        return null;
    }


    /** 
     * Load PlugIns
     */
    @Override
    public void loadPlugIns()
    {
    }
    

    /**
     * Insulin Dose: Basal
     */
    public static final int INSULIN_DOSE_BASAL = 1;
    
    /**
     * Insulin Dose: Bolus
     */
    public static final int INSULIN_DOSE_BOLUS = 2;
    
    
    /**
     * Get Insulin Precision
     * 
     * @param mode
     * @param type
     * @return
     */
    public float getInsulinPrecision(int mode, int type)
    {
        if (mode==INSULIN_PEN_INJECTION)
        {
            if (type==INSULIN_DOSE_BASAL)
            {
                return this.m_cfgMgr.getFloatValue("PEN_BASAL_PRECISSION");
            }
            else
            {
                return this.m_cfgMgr.getFloatValue("PEN_BOLUS_PRECISSION");
            }
        }
        else
        {
            if (type==INSULIN_DOSE_BASAL)
            {
                return this.m_cfgMgr.getFloatValue("PUMP_BASAL_PRECISSION");
            }
            else
            {
                return this.m_cfgMgr.getFloatValue("PUMP_BOLUS_PRECISSION");
            }
        }
        
    }
    

    /**
     * Get Insulin Precision String
     * 
     * @param mode
     * @param type
     * @return
     */
    public String getInsulinPrecisionString(int mode, int type)
    {
        if (mode==INSULIN_PEN_INJECTION)
        {
            if (type==INSULIN_DOSE_BASAL)
            {
                return this.m_cfgMgr.getStringValue("PEN_BASAL_PRECISSION");
            }
            else
            {
                return this.m_cfgMgr.getStringValue("PEN_BOLUS_PRECISSION");
            }
        }
        else
        {
            if (type==INSULIN_DOSE_BASAL)
            {
                return this.m_cfgMgr.getStringValue("PUMP_BASAL_PRECISSION");
            }
            else
            {
                return this.m_cfgMgr.getStringValue("PUMP_BOLUS_PRECISSION");
            }
        }
        
    }
    
    
    
    /**
     * Reformat Insulin Amount To CorrectValue
     * 
     * @param mode
     * @param type
     * @param input_val
     * @return
     */
    public double reformatInsulinAmountToCorrectValue(int mode, int type, float input_val)
    {
        String prec = getInsulinPrecisionString(mode, type);
        
        //System.out.println("Precision: " + prec);
        
        return Rounding.specialRounding(input_val, prec);
    }
    
    
    /**
     * Reformat Insulin Amount To CorrectValue String
     * 
     * @param mode
     * @param type
     * @param input_val
     * @return
     */
    public String reformatInsulinAmountToCorrectValueString(int mode, int type, float input_val)
    {
        String prec = getInsulinPrecisionString(mode, type);
        return Rounding.specialRoundingString(input_val, prec);
    }
    
    
    /**
     * Get Max Values
     * 
     * @param mode
     * @param type
     * @return
     */
    public float getMaxValues(int mode, int type)
    {
        if (mode==INSULIN_PEN_INJECTION)
        {
            if (type==INSULIN_DOSE_BASAL)
            {
                return this.m_cfgMgr.getFloatValue("PEN_MAX_BASAL");
            }
            else
            {
                return this.m_cfgMgr.getFloatValue("PEN_MAX_BOLUS");
            }
        }
        else
        {
            if (type==INSULIN_DOSE_BASAL)
            {
                return this.m_cfgMgr.getFloatValue("PUMP_MAX_BASAL");
            }
            else
            {
                return this.m_cfgMgr.getFloatValue("PUMP_MAX_BOLUS");
            }
        }
        
    }
    
    /**
     * For misc tests
     */
    public void doTest()
    {
        
        /*
        //ColorUIResource cui = (ColorUIResource) UIManager.getLookAndFeel()
        //.getDefaults().get("textText");        
        
        
        UIDefaults dd = UIManager.getLookAndFeel().getDefaults();
        
        System.out.println(dd);
        
        for(Enumeration en = dd.keys(); en.hasMoreElements(); )
        {
            String key = (String)en.nextElement();
            
            if (key.contains("Table"))
                System.out.println(key);
        }
        
        //ComponentUI cui = dd.getUI(new JTable());
        
        Color c = dd.getColor("TableHeader.background");
        c = Color.blue;
        
        
   
        
        */
    }
    

    
    /**
     * Get Max Decimals that will be used by DecimalHandler
     * 
     * @return
     */
    public int getMaxDecimalsUsedByDecimalHandler()
    {
        return 3;
    }

    
    public void loadExtendedHandlers()
    {
        this.extended_handlers.put("DailyValuesRow", new ExtendedDailyValue(this));
    }
    
    
    @Override
    public void loadConverters()
    {
        this.converters.put("BG", new Converter_mgdL_mmolL());
    }
    
    
    /**
     * Get BG Converter
     * 
     * @return
     */
    public Converter_mgdL_mmolL getBGConverter()
    {
        return (Converter_mgdL_mmolL)this.getConverter("BG");
        
    }
    

}
