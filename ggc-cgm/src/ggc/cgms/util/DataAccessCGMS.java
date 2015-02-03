package ggc.cgms.util;

import ggc.cgms.data.CGMSDataHandler;
import ggc.cgms.data.CGMSDataReader;
import ggc.cgms.data.CGMSValuesEntry;
import ggc.cgms.data.ExtendedCGMSValuesExtendedEntry;
import ggc.cgms.data.cfg.CGMSConfigurationDefinition;
import ggc.cgms.data.db.GGC_CGMSDb;
import ggc.cgms.manager.CGMSManager;
import ggc.core.plugins.GGCPluginType;
import ggc.plugin.cfg.DeviceConfiguration;
import ggc.plugin.list.BaseListEntry;
import ggc.plugin.util.DataAccessPlugInBase;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Hashtable;

import com.atech.graphics.components.about.CreditsEntry;
import com.atech.graphics.components.about.CreditsGroup;
import com.atech.graphics.components.about.FeaturesEntry;
import com.atech.graphics.components.about.FeaturesGroup;
import com.atech.graphics.components.about.LibraryInfoEntry;
import com.atech.i18n.I18nControlAbstract;
import com.atech.i18n.mgr.LanguageManager;

/**
 *  Application:   GGC - GNU Gluco Control
 *  Plug-in:       CGMS Tool (support for CGMS devices)
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
 *  Filename:     DataAccessCGMS  
 *  Description:  Singelton class containing all data used through plugin
 * 
 *  Author: Andy {andy@atech-software.com}
 */

public class DataAccessCGMS extends DataAccessPlugInBase
{

    /**
     * PlugIn Version
     */
    public static final String PLUGIN_VERSION = "1.2.0"; // 1.0.2

    private static final String EXTENDED_HANDLER_CGMSValuesExtendedEntry = "CGMSValuesExtendedEntry";

    private static DataAccessCGMS s_da = null; // This is handle to unique

    private CGMSManager m_cgms_manager = null;

    /**
     * Value Types
     */
    // public static String[] value_types = null;

    /**
     * Value Type
     */
    public static String[] value_type = null;

    // ********************************************************
    // ****** Constructors and Access methods *****
    // ********************************************************

    /**
     *
     *  This is DataAccess constructor; Since classes use Singleton Pattern,
     *  constructor is protected and can be accessed only with getInstance() 
     *  method.<br><br>
     *
     */
    private DataAccessCGMS(LanguageManager lm)
    {
        super(lm, new GGC_CGMS_ICRunner());
    }

    /** 
     * Init Special - All methods that we support should be called here
     */
    @Override
    public void initSpecial()
    {
        checkPrerequisites();

        this.createWebListerContext();
        this.createPlugInAboutContext();
        this.createConfigurationContext();
        this.createPlugInVersion();
        loadDeviceDataHandler();
        // loadManager();
        loadReadingStatuses();
        this.createPlugInDataRetrievalContext();
        this.createDeviceConfiguration();
        this.createOldDataReader();
        loadWebLister();
        this.loadConverters();
    }

    /**
     *
     *  This method returns reference to DataAccessCGM object created, or if no 
     *  object was created yet, it creates one.<br><br>
     *
     *  @return Reference to DataAccessCGM instance
     */
    public static DataAccessCGMS getInstance()
    {
        // if (s_da == null)
        // s_da = new DataAccessCGM();
        return s_da;
    }

    /**
     * Create Instance
     * 
     * @param lm
     * @return
     */
    public static DataAccessCGMS createInstance(LanguageManager lm)
    {
        if (s_da == null)
        {
            s_da = new DataAccessCGMS(lm);
        }
        return s_da;
    }

    // Method: deleteInstance
    /**
     *  This method sets handle to DataAccess to null and deletes the instance. <br><br>
     */
    public void deleteInstance()
    {
        m_i18n = null;
    }

    // ********************************************************
    // ****** Abstract Methods *****
    // ********************************************************

    @Override
    public GGCPluginType getPluginType()
    {
        return GGCPluginType.CGMSToolPlugin;
    }


    /** 
     * Get Application Name
     */
    @Override
    public String getApplicationName()
    {
        return "GGC_CGMSTool";
    }

    /** 
     * Check Prerequisites for Plugin
     */
    @Override
    public void checkPrerequisites()
    {
    }

    // ********************************************************
    // ****** Manager *****
    // ********************************************************

    /**
     * Get Device Manager
     * 
     * @return
     */
    public CGMSManager getCGMManager()
    {
        return this.m_cgms_manager;
    }

    // ********************************************************
    // ****** Parent handling (for UIs) *****
    // ********************************************************

    /**
     *  Utils
     */

    /*
     * public Image getImage(String filename, Component cmp)
     * {
     * Image img;
     * InputStream is = this.getClass().getResourceAsStream(filename);
     * if (is==null)
     * System.out.println("Error reading image: "+filename);
     * ByteArrayOutputStream baos = new ByteArrayOutputStream();
     * try
     * {
     * int c;
     * while ((c = is.read()) >=0)
     * baos.write(c);
     * //JDialog.getT
     * //JFrame.getToolkit();
     * if (cmp==null)
     * cmp = new JLabel();
     * img = cmp.getToolkit().createImage(baos.toByteArray());
     * }
     * catch (IOException ex)
     * {
     * ex.printStackTrace();
     * return null;
     * }
     * return img;
     * }
     */

    // ********************************************************
    // ****** Dates and Times Handling *****
    // ********************************************************

    @Override
    public String getCurrentDateString()
    {
        GregorianCalendar gc = new GregorianCalendar();
        return gc.get(Calendar.DAY_OF_MONTH) + "." + (gc.get(Calendar.MONTH) + 1) + "." + gc.get(Calendar.YEAR);
    }

    // ********************************************************
    // ****** Database *****
    // ********************************************************

    GGC_CGMSDb m_db;

    /**
     * Create Custom Db
     * 
     * This is for plug-in specific database implementation
     */
    @Override
    public void createCustomDb()
    {
        this.m_db = new GGC_CGMSDb(this.hdb);
    }

    /**
     * Get Db
     * 
     * @return
     */
    public GGC_CGMSDb getDb()
    {
        return this.m_db;
    }

    // ********************************************************
    // ****** Configuration *****
    // ********************************************************

    /**
     * Create Configuration Context for plugin
     */
    @Override
    public void createConfigurationContext()
    {
        this.device_config_def = new CGMSConfigurationDefinition();
    }

    /**
     * Create Device Configuration for plugin
     */
    @Override
    public void createDeviceConfiguration()
    {
        this.device_config = new DeviceConfiguration(this);
    }

    // ********************************************************
    // ****** About Methods *****
    // ********************************************************

    /**
     * Create About Context for plugin
     */
    @Override
    public void createPlugInAboutContext()
    {
        I18nControlAbstract ic = this.getI18nControlInstance();
        // this.about_title = i18nControlAbstract.getMessage("PUMP_PLUGIN_ABOUT");
        this.about_image_name = "/icons/cgms_about.jpg";

        this.about_plugin_copyright_from = 2009;
        // this.about_plugin_name = i18nControlAbstract.getMessage("PUMP_PLUGIN");

        // libraries
        ArrayList<LibraryInfoEntry> lst_libs = new ArrayList<LibraryInfoEntry>();

        lst_libs.addAll(getBaseLibraries());

        this.plugin_libraries = lst_libs;

        // developers and other credits
        ArrayList<CreditsGroup> lst_credits = new ArrayList<CreditsGroup>();

        CreditsGroup cg = new CreditsGroup(ic.getMessage("DEVELOPERS_DESC"));
        cg.addCreditsEntry(new CreditsEntry("Aleksander Rozman (Andy)", "andy@atech-software.com",
                "Framework, About, Outputs")); // and support for Ascensia &
                                               // Roche devices"));
        lst_credits.add(cg);



        this.plugin_developers = lst_credits;

        // features
        ArrayList<FeaturesGroup> lst_features = new ArrayList<FeaturesGroup>();

        FeaturesGroup fg = new FeaturesGroup(ic.getMessage("IMPLEMENTED_FEATURES"));
        fg.addFeaturesEntry(new FeaturesEntry("Base CGMS Tools Framework"));
        fg.addFeaturesEntry(new FeaturesEntry("Various output types"));
        fg.addFeaturesEntry(new FeaturesEntry("Communication Framework"));
        fg.addFeaturesEntry(new FeaturesEntry("Reading data"));
        fg.addFeaturesEntry(new FeaturesEntry("Configuration"));
        fg.addFeaturesEntry(new FeaturesEntry("List of CGMSes"));
        fg.addFeaturesEntry(new FeaturesEntry("About dialog"));

        lst_features.add(fg);

        fg = new FeaturesGroup(ic.getMessage("SUPPORTED_DEVICES"));
        fg.addFeaturesEntry(new FeaturesEntry("Dexcom (file imports from DM3 App)"));
        fg.addFeaturesEntry(new FeaturesEntry("Dexcom G4"));

        lst_features.add(fg);

        fg = new FeaturesGroup(ic.getMessage("NOT_IMPLEMENTED_FEATURES"));
        fg.addFeaturesEntry(new FeaturesEntry("Graphs"));
        fg.addFeaturesEntry(new FeaturesEntry("Printing"));

        lst_features.add(fg);

        fg = new FeaturesGroup(ic.getMessage("PLANNED_DEVICES"));
        fg.addFeaturesEntry(new FeaturesEntry("Minimed RealTime (??)"));
        fg.addFeaturesEntry(new FeaturesEntry("Freestyle Navigator (??)"));

        lst_features.add(fg);

        this.plugin_features = lst_features;
    }

    // ********************************************************
    // ****** Version *****
    // ********************************************************

    /**
     * Create Plugin Version
     */
    @Override
    public void createPlugInVersion()
    {
        this.plugin_version = DataAccessCGMS.PLUGIN_VERSION;
    }

    // ********************************************************
    // ****** Web Lister Methods *****
    // ********************************************************

    /**
     * Create WebLister (for List) Context for plugin
     */
    @Override
    public void createWebListerContext()
    {
        this.weblister_items = new ArrayList<BaseListEntry>();
        this.weblister_items.add(new BaseListEntry("Abbott Diabetes Care", "/cgms/abbott.html",
                BaseListEntry.STATUS_NOTPLANNED));
        this.weblister_items.add(new BaseListEntry("Dexcom", "/cgms/dexcom.html", BaseListEntry.STATUS_PART_IMPLEMENTED));
        this.weblister_items.add(new BaseListEntry("Minimed", "/cgms/minimed.html", BaseListEntry.STATUS_PLANNED));

        this.weblister_title = this.m_i18n.getMessage("DEVICE_LIST_WEB");
        this.weblister_desc = this.m_i18n.getMessage("DEVICE_LIST_WEB_DESC");
    }

    /**
     * Create About Context for plugin
     */
    @Override
    public void createPlugInDataRetrievalContext()
    {
        loadBasePluginTranslations();

        m_entry_type = new CGMSValuesEntry();

        this.data_download_screen_wide = false;

        this.columns_table = new String[5];
        this.columns_table[0] = this.i18n_plugin.getMessage("DATETIME");
        this.columns_table[1] = this.i18n_plugin.getMessage("ENTRY_TYPE");
        this.columns_table[2] = this.i18n_plugin.getMessage("READING");
        this.columns_table[3] = this.i18n_plugin.getMessage("STATUS");
        this.columns_table[4] = "";

        this.column_widths_table = new int[5];
        this.column_widths_table[0] = 110;
        this.column_widths_table[1] = 150;
        this.column_widths_table[2] = 60;
        this.column_widths_table[3] = 50;
        this.column_widths_table[4] = 110; // value
        // this.column_widths_table[5] = 40;
        // this.column_widths_table[6] = 25;

        /*
         * DataAccessCGMS.value_types = new String[6];
         * DataAccessCGMS.value_types[0] = "";
         * DataAccessCGMS.value_types[1] =
         * this.i18n_plugin.getMessage("CGMS_READINGS");
         * DataAccessCGMS.value_types[2] =
         * this.i18n_plugin.getMessage("CALIBRATION_READINGS");
         * DataAccessCGMS.value_types[3] =
         * this.i18n_plugin.getMessage("CGMS_DATA_EVENT");
         * DataAccessCGMS.value_types[4] =
         * this.i18n_plugin.getMessage("CGMS_DATA_ALARM");
         * DataAccessCGMS.value_types[5] =
         * this.i18n_plugin.getMessage("CGMS_DATA_ERROR");
         * DataAccessCGMS.value_types[6] =
         * this.i18n_plugin.getMessage("CGMS_READING_TREND");
         */

        // DataAccessCGMS.value_types[6] =
        // this.i18n_plugin.getMessage("CALIBRATION_READINGS");

        DataAccessCGMS.value_type = new String[7];
        DataAccessCGMS.value_type[0] = "";
        DataAccessCGMS.value_type[1] = this.i18n_plugin.getMessage("CGMS_READING");
        DataAccessCGMS.value_type[2] = this.i18n_plugin.getMessage("CALIBRATION_READINGS");
        DataAccessCGMS.value_type[4] = this.i18n_plugin.getMessage("CGMS_DATA_EVENT");
        DataAccessCGMS.value_type[3] = this.i18n_plugin.getMessage("CGMS_DATA_ALARM");
        DataAccessCGMS.value_type[5] = this.i18n_plugin.getMessage("CGMS_DATA_ERROR");
        DataAccessCGMS.value_type[6] = this.i18n_plugin.getMessage("CGMS_READING_TREND");

    }

    /**
     * Load Manager instance
     */
    @Override
    public void loadManager()
    {
        this.m_cgms_manager = CGMSManager.getInstance();
        this.m_manager = this.m_cgms_manager;
    }

    /**
     * Load Device Data Handler
     */
    @Override
    public void loadDeviceDataHandler()
    {
        this.m_ddh = new CGMSDataHandler(this);
    }

    @Override
    public void loadExtendedHandlers()
    {
        this.addExtendedHandler(DataAccessCGMS.EXTENDED_HANDLER_CGMSValuesExtendedEntry,
            new ExtendedCGMSValuesExtendedEntry(this));
    }

    /**
     * Get Images for Devices
     * 
     * @see ggc.plugin.util.DataAccessPlugInBase#getDeviceImagesRoot()
     * @return String with images path 
     */
    @Override
    public String getDeviceImagesRoot()
    {
        return "/icons/cgms/";
    }

    /**
     * Load PlugIns
     */
    @Override
    public void loadPlugIns()
    {
    }

    /**
     * Create Old Data Reader
     */
    @Override
    public void createOldDataReader()
    {
        // FIXME
        this.m_old_data_reader = new CGMSDataReader(this);
    }

    /**
     * Load Special Parameters
     * 
     * @see com.atech.utils.ATDataAccessAbstract#loadSpecialParameters()
     */
    @Override
    public void loadSpecialParameters()
    {
        this.special_parameters = new Hashtable<String, String>();
        this.special_parameters.put("BG", "" + this.getBGMeasurmentType());
    }

    /** 
     * Get About Image Size - Define about image size
     */
    @Override
    public int[] getAboutImageSize()
    {
        int[] sz = new int[2];
        sz[0] = 400;
        sz[1] = 203;

        return sz;
    }

    @Override
    public void initAllObjects()
    {
    }

    /**
     * Get Name of Plugin (for internal use)
     * @return
     */
    @Override
    public String getPluginName()
    {
        return "GGC CGMS Plugin";
    }

}
