package ggc.pump.util;

import ggc.plugin.cfg.DeviceConfiguration;
import ggc.plugin.list.BaseListEntry;
import ggc.plugin.util.DataAccessPlugInBase;
import ggc.pump.data.PumpDataHandler;
import ggc.pump.data.PumpDataReader;
import ggc.pump.data.PumpValuesEntry;
import ggc.pump.data.cfg.PumpConfigurationDefinition;
import ggc.pump.data.db.GGCPumpDb;
import ggc.pump.data.defs.PumpAdditionalDataType;
import ggc.pump.data.defs.PumpAlarms;
import ggc.pump.data.defs.PumpBasalSubType;
import ggc.pump.data.defs.PumpBaseType;
import ggc.pump.data.defs.PumpBolusType;
import ggc.pump.data.defs.PumpErrors;
import ggc.pump.data.defs.PumpEvents;
import ggc.pump.data.defs.PumpReport;
import ggc.pump.graph.PumpGraphContext;
import ggc.pump.manager.PumpManager;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.StringTokenizer;

import com.atech.graphics.components.about.CreditsEntry;
import com.atech.graphics.components.about.CreditsGroup;
import com.atech.graphics.components.about.FeaturesEntry;
import com.atech.graphics.components.about.FeaturesGroup;
import com.atech.graphics.components.about.LibraryInfoEntry;
import com.atech.i18n.I18nControlAbstract;
import com.atech.i18n.mgr.LanguageManager;

/**
 * Application: GGC - GNU Gluco Control Plug-in: Pump Tool (support for Pump
 * devices)
 *
 * See AUTHORS for copyright information.
 *
 * This program is free software; you can redistribute it and/or modify it under
 * the terms of the GNU General Public License as published by the Free Software
 * Foundation; either version 2 of the License, or (at your option) any later
 * version.
 *
 * This program is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU General Public License for more
 * details.
 *
 * You should have received a copy of the GNU General Public License along with
 * this program; if not, write to the Free Software Foundation, Inc., 59 Temple
 * Place, Suite 330, Boston, MA 02111-1307 USA
 *
 * Filename: DataAccessPump Description: Contains instances of utilities,
 * private (plugin specific) data, help classes, ...
 *
 * Author: Andy {andy@atech-software.com}
 */

public class DataAccessPump extends DataAccessPlugInBase
{

    /**
     * PlugIn Version
     */
    public static final String PLUGIN_VERSION = "1.3.4";

    private static DataAccessPump s_da = null; // This is handle to unique

    private PumpManager m_pumpManager = null;

    /**
     * The m_pump_base_type.
     */
    PumpBaseType m_pump_base_type = null;

    /**
     * The m_pump_bolus_type.
     */
    PumpBolusType m_pump_bolus_type = null;

    /**
     * The m_pump_basal_type.
     */
    PumpBasalSubType m_pump_basal_type = null;

    /**
     * The m_pump_report.
     */
    PumpReport m_pump_report = null;

    /**
     * The m_pump_add_type.
     */
    PumpAdditionalDataType m_pump_add_type = null;

    /**
     * The m_pump_alarms.
     */
    PumpAlarms m_pump_alarms = null;

    /**
     * The m_pump_events.
     */
    PumpEvents m_pump_events = null;

    /**
     * The m_pump_errors.
     */
    PumpErrors m_pump_errors = null;

    // GGCPumpDb m_db = null;

    // DecimalHandler dec_handler = new DecimalHandler(3);

    // Hashtable<String,String> sorters = new Hashtable<String,String>();

    /**
     * The selected_person_id.
     */
    // long selected_person_id = 0;

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
    private DataAccessPump(LanguageManager lm)
    {
        super(lm, new GGCPumpICRunner());
    }

    /**
     * Init Special
     */
    @Override
    public void initSpecial()
    {
        // this.loadTimeZones();
        // loadPumpsTable();
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
        this.loadSorters();
        this.loadGraphContext();
    }

    // Method: getInstance
    // Author: Andy
    /**
     *
     * This method returns reference to DataAccessPump object created, or if no
     * object was created yet, it creates one.<br>
     * <br>
     *
     * @return Reference to DataAccessPump object
     *
     */
    public static DataAccessPump getInstance()
    {
        // if (s_da == null)
        // s_da = new DataAccessPump();
        return s_da;
    }

    /**
     * Create Instance
     *
     * @param lm
     * @return
     */
    public static DataAccessPump createInstance(LanguageManager lm)
    {
        if (s_da == null)
        {
            s_da = new DataAccessPump(lm);
        }
        return s_da;
    }

    /**
     * Get Pump Base Types
     *
     * @return
     */
    public PumpBaseType getPumpBaseTypes()
    {
        return this.m_pump_base_type;
    }

    /**
     * Get Bolus Sub Types
     *
     * @return
     */
    public PumpBolusType getBolusSubTypes()
    {
        return m_pump_bolus_type;
    }

    /**
     * Get Basal Sub Types
     *
     * @return
     */
    public PumpBasalSubType getBasalSubTypes()
    {
        return m_pump_basal_type;
    }

    /**
     * Get Pump Report Types
     *
     * @return
     */
    public PumpReport getPumpReportTypes()
    {
        return this.m_pump_report;
    }

    /**
     * Get Pump Alarm Types
     *
     * @return
     */
    public PumpAlarms getPumpAlarmTypes()
    {
        return this.m_pump_alarms;
    }

    /**
     * Get Pump Error Types
     *
     * @return
     */
    public PumpErrors getPumpErrorTypes()
    {
        return this.m_pump_errors;
    }

    /**
     * Get Pump Events Types
     *
     * @return
     */
    public PumpEvents getPumpEventTypes()
    {
        return this.m_pump_events;
    }

    /**
     * Get Additional Types
     *
     * @return
     */
    public PumpAdditionalDataType getAdditionalTypes()
    {
        return this.m_pump_add_type;
    }

    /*
     * static public DataAccess getInstance() { return m_da; }
     */

    // Method: deleteInstance
    /**
     * This method sets handle to DataAccess to null and deletes the instance. <br>
     * <br>
     */
    public void deleteInstance()
    {
        m_i18n = null;
        i18n_plugin = null;
    }

    /*
     * public void loadPumpsTable() { metersUrl = new
     * Hashtable<String,String>(); metersNames = new ArrayList<String>();
     * metersNames.add("Abbott Diabetes Care");
     * metersUrl.put("Abbott Diabetes Care", "abbott.html");
     * metersNames.add("Bayer Diagnostics"); metersUrl.put("Bayer Diagnostics",
     * "bayer.html"); metersNames.add("Diabetic Supply of Suncoast");
     * metersUrl.put("Diabetic Supply of Suncoast", "suncoast.html");
     * metersNames.add("Diagnostic Devices");
     * metersUrl.put("Diagnostic Devices", "prodigy.html");
     * metersNames.add("Arkray USA (formerly Hypoguard)");
     * metersUrl.put("Arkray USA (formerly Hypoguard)", "arkray.html");
     * metersNames.add("HealthPia America"); metersUrl.put("HealthPia America",
     * "healthpia.html"); metersNames.add("Home Diagnostics");
     * metersUrl.put("Home Diagnostics", "home_diganostics.html");
     * metersNames.add("Lifescan"); metersUrl.put("Lifescan", "lifescan.html");
     * metersNames.add("Nova Biomedical"); metersUrl.put("Nova Biomedical",
     * "nova_biomedical.html"); metersNames.add("Roche Diagnostics");
     * metersUrl.put("Roche Diagnostics", "roche.html");
     * metersNames.add("Sanvita"); metersUrl.put("Sanvita", "sanvita.html");
     * metersNames.add("U.S. Diagnostics"); metersUrl.put("U.S. Diagnostics",
     * "us_diagnostics.html"); metersNames.add("WaveSense");
     * metersUrl.put("WaveSense", "wavesense.html"); }
     */

    // ********************************************************
    // ****** Init Methods *****
    // ********************************************************

    /**
     * Init All Objects
     */
    @Override
    public void initAllObjects()
    {
        //this.m_pump_base_type = new PumpBaseType();
        this.m_pump_bolus_type = new PumpBolusType();
        this.m_pump_basal_type = new PumpBasalSubType();
        this.m_pump_report = new PumpReport();
        // this.m_pump_alarms = new PumpAlarms();
        this.m_pump_events = new PumpEvents();
        this.m_pump_add_type = new PumpAdditionalDataType();
        this.m_pump_errors = new PumpErrors();

    }

    /**
     * Get Application Name
     */
    @Override
    public String getApplicationName()
    {
        return "GGC_PumpTool";
    }

    /**
     * Create About Context for plugin
     */
    @Override
    public void createPlugInAboutContext()
    {

        I18nControlAbstract ic = this.getI18nControlInstance();
        // this.about_title = ic.getMessage("PUMP_PLUGIN_ABOUT");
        this.about_image_name = "/icons/pumps_about.jpg";

        this.about_plugin_copyright_from = 2008;
        // this.about_plugin_name = ic.getMessage("PUMP_PLUGIN");

        // libraries
        ArrayList<LibraryInfoEntry> lst_libs = new ArrayList<LibraryInfoEntry>();

        lst_libs.add(new LibraryInfoEntry("Atech-Tools", "0.3.x", "www.atech-software.com", "LGPL",
                "Helper Library for Swing/Hibernate/...",
                "Copyright (c) 2006-2008 Atech Software Ltd. All rights reserved."));
        lst_libs.add(new LibraryInfoEntry("Apache Commons Lang", "2.4", "commons.apache.org/lang/", "Apache",
                "Helper methods for java.lang library"));
        lst_libs.add(new LibraryInfoEntry("Apache Commons Logging", "1.0.4", "commons.apache.org/logging/", "Apache",
                "Logger and all around wrapper for logging utilities"));
        lst_libs.add(new LibraryInfoEntry("dom4j", "1.6.1", "http://www.dom4j.org/", "BSD",
                "Framework for Xml manipulation"));
        lst_libs.add(new LibraryInfoEntry("RXTXcomm", "2.1.7", "www.rxtx.org", "LGPL", "Comm API"));
        lst_libs.add(new LibraryInfoEntry("XML Pull Parser", "3.1.1.4c",
                "http://www.extreme.indiana.edu/xgws/xsoap/xpp/", "Indiana University Extreme! Lab Software License",
                "Xml parser for processing xml document",
                "Copyright (c) 2002 Extreme! Lab, Indiana University. All rights reserved."));

        this.plugin_libraries = lst_libs;

        // developers and other credits
        ArrayList<CreditsGroup> lst_credits = new ArrayList<CreditsGroup>();

        CreditsGroup cg = new CreditsGroup(ic.getMessage("DEVELOPERS_DESC"));
        cg.addCreditsEntry(new CreditsEntry("Aleksander Rozman (Andy)", "andy@atech-software.com",
                "Framework, About, Outputs")); // and support for Ascensia &
                                               // Roche devices"));
        lst_credits.add(cg);

        // cg = new CreditsGroup(ic.getMessage("HELPERS_DESC"));
        // cg.addCreditsEntry(new CreditsEntry("Rafael Ziherl (RAF)", "",
        // "Supplied hardware for Roche development"));
        // lst_credits.add(cg);

        this.plugin_developers = lst_credits;

        // features
        ArrayList<FeaturesGroup> lst_features = new ArrayList<FeaturesGroup>();

        FeaturesGroup fg = new FeaturesGroup(ic.getMessage("IMPLEMENTED_FEATURES"));
        fg.addFeaturesEntry(new FeaturesEntry("Base Pump Tools Framework"));
        fg.addFeaturesEntry(new FeaturesEntry("Various output types"));
        fg.addFeaturesEntry(new FeaturesEntry("Communication Framework"));
        fg.addFeaturesEntry(new FeaturesEntry("Reading data"));
        fg.addFeaturesEntry(new FeaturesEntry("Configuration"));
        fg.addFeaturesEntry(new FeaturesEntry("List of pumps"));
        fg.addFeaturesEntry(new FeaturesEntry("Manual data entry (also additional data entry)"));
        fg.addFeaturesEntry(new FeaturesEntry("Profiles (partitial)"));
        fg.addFeaturesEntry(new FeaturesEntry("About dialog"));

        lst_features.add(fg);

        fg = new FeaturesGroup(ic.getMessage("SUPPORTED_DEVICES"));
        // fg.addFeaturesEntry(new
        // FeaturesEntry("Roche (partitialy, Basal Pattern History is not fully supported due to incomplete export of SmartPix device)"));
        fg.addFeaturesEntry(new FeaturesEntry("Dana (only works on Windows and Linux)"));
        // fg.addFeaturesEntry(new FeaturesEntry("Accu-chek/Roche"));
        // fg.addFeaturesEntry(new
        // FeaturesEntry("LifeScan: Ultra, Ultra2, Profile, Easy, UltraSmart"));

        lst_features.add(fg);

        fg = new FeaturesGroup(ic.getMessage("NOT_IMPLEMENTED_FEATURES"));
        // fg.addFeaturesEntry(new
        // FeaturesEntry("Graphical Interface (GGC integration) [Ready]"));
        // fg.addFeaturesEntry(new FeaturesEntry("Configuration [Ready]"));
        fg.addFeaturesEntry(new FeaturesEntry("Profiles (graphical edit)"));
        fg.addFeaturesEntry(new FeaturesEntry("Graphs"));

        lst_features.add(fg);

        fg = new FeaturesGroup(ic.getMessage("PLANNED_DEVICES"));
        fg.addFeaturesEntry(new FeaturesEntry("Minimed (in 2010)"));
        fg.addFeaturesEntry(new FeaturesEntry("Roche (in 2010)"));
        // fg.addFeaturesEntry(new FeaturesEntry("Dana (in 2009/2010)"));

        lst_features.add(fg);

        this.plugin_features = lst_features;

    }

    /**
     * Create WebLister (for List) Context for plugin
     */
    @Override
    public void createWebListerContext()
    {

        this.weblister_items = new ArrayList<BaseListEntry>();

        this.weblister_items.add(new BaseListEntry("Animas", "/pumps/animas.html", BaseListEntry.STATUS_PLANNED));
        this.weblister_items.add(new BaseListEntry("Deltec", "/pumps/deltec.html", BaseListEntry.STATUS_NOTPLANNED));
        this.weblister_items.add(new BaseListEntry("Insulet", "/pumps/insulet.html", BaseListEntry.STATUS_NOTPLANNED));
        this.weblister_items
                .add(new BaseListEntry("Minimed", "/pumps/minimed.html", BaseListEntry.STATUS_IMPLEMENTING));
        this.weblister_items.add(new BaseListEntry("Roche", "/pumps/roche.html", BaseListEntry.STATUS_IMPLEMENTING));
        this.weblister_items.add(new BaseListEntry("Sooil", "/pumps/sooil.html", BaseListEntry.STATUS_IMPLEMENTING));

        this.weblister_title = this.m_i18n.getMessage("DEVICE_LIST_WEB");
        this.weblister_desc = this.m_i18n.getMessage("DEVICE_LIST_WEB_DESC");

        // public BaseListEntry(String name, String page, int status)

        /*
         * metersUrl = new Hashtable<String,String>(); metersNames = new
         * ArrayList<String>(); metersNames.add("Abbott Diabetes Care");
         * metersUrl.put("Abbott Diabetes Care", "abbott.html");
         * metersNames.add("Bayer Diagnostics");
         * metersUrl.put("Bayer Diagnostics", "bayer.html");
         * metersNames.add("Diabetic Supply of Suncoast");
         * metersUrl.put("Diabetic Supply of Suncoast", "suncoast.html");
         * metersNames.add("Diagnostic Devices");
         * metersUrl.put("Diagnostic Devices", "prodigy.html");
         * metersNames.add("Arkray USA (formerly Hypoguard)");
         * metersUrl.put("Arkray USA (formerly Hypoguard)", "arkray.html");
         * metersNames.add("HealthPia America");
         * metersUrl.put("HealthPia America", "healthpia.html");
         * metersNames.add("Home Diagnostics");
         * metersUrl.put("Home Diagnostics", "home_diganostics.html");
         * metersNames.add("Lifescan"); metersUrl.put("Lifescan",
         * "lifescan.html"); metersNames.add("Nova Biomedical");
         * metersUrl.put("Nova Biomedical", "nova_biomedical.html");
         * metersNames.add("Roche Diagnostics");
         * metersUrl.put("Roche Diagnostics", "roche.html");
         * metersNames.add("Sanvita"); metersUrl.put("Sanvita", "sanvita.html");
         * metersNames.add("U.S. Diagnostics");
         * metersUrl.put("U.S. Diagnostics", "us_diagnostics.html");
         * metersNames.add("WaveSense"); metersUrl.put("WaveSense",
         * "wavesense.html");
         */
    }

    // ********************************************************
    // ****** Pumps *****
    // ********************************************************
    /*
     * public PumpManager getPumpManager() { return this.m_pumpManager; }
     */

    // ********************************************************
    // ****** Version *****
    // ********************************************************

    /**
     * Create Plugin Version
     */
    @Override
    public void createPlugInVersion()
    {
        this.plugin_version = DataAccessPump.PLUGIN_VERSION;
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
        return 1980;
    }

    // ********************************************************
    // ****** Db *****
    // ********************************************************

    /**
     * The m_db.
     */
    GGCPumpDb m_db;

    /**
     * Create Custom Db
     *
     * This is for plug-in specific database implementation
     */
    @Override
    public void createCustomDb()
    {
        this.m_db = new GGCPumpDb(this.hdb);
    }

    /**
     * Get Db
     *
     * @return
     */
    public GGCPumpDb getDb()
    {
        return this.m_db;
    }

    /**
     * Create Configuration Context for plugin
     */
    @Override
    public void createConfigurationContext()
    {
        this.device_config_def = new PumpConfigurationDefinition();
    }

    /**
     * Create Device Configuration for plugin
     */
    @Override
    public void createDeviceConfiguration()
    {
        this.device_config = new DeviceConfiguration(this);
    }

    /**
     * Create About Context for plugin
     */
    @Override
    public void createPlugInDataRetrievalContext()
    {
        loadBasePluginTranslations();

        m_entry_type = new PumpValuesEntry(true);

        this.data_download_screen_wide = true;

        this.columns_table = new String[7];
        this.columns_table[0] = i18n_plugin.getMessage("DATETIME");
        this.columns_table[1] = i18n_plugin.getMessage("ENTRY_TYPE");
        this.columns_table[2] = i18n_plugin.getMessage("BASE_TYPE");
        this.columns_table[3] = i18n_plugin.getMessage("SUB_TYPE");
        this.columns_table[4] = i18n_plugin.getMessage("VALUE");
        this.columns_table[5] = i18n_plugin.getMessage("STATUS");
        this.columns_table[6] = ""; // i18n_plugin.getMessage("");

        this.column_widths_table = new int[7];
        this.column_widths_table[0] = 100;
        this.column_widths_table[1] = 30;
        this.column_widths_table[2] = 50;
        this.column_widths_table[3] = 50;
        this.column_widths_table[4] = 105; // value
        this.column_widths_table[5] = 40;
        this.column_widths_table[6] = 25;

        // 25, 10, 20
    }

    /**
     * Load Manager instance
     */
    @Override
    public void loadManager()
    {
        this.m_pumpManager = PumpManager.getInstance();
        this.m_manager = this.m_pumpManager;
    }

    /**
     * Load Device Data Handler
     */
    @Override
    public void loadDeviceDataHandler()
    {
        this.m_ddh = new PumpDataHandler(this);
    }

    /**
     * Get Images for Devices (must end with backslash)
     *
     * @return String with images path
     */
    @Override
    public String getDeviceImagesRoot()
    {
        return "/icons/pumps/";
    }

    /**
     * Get Selected Persons Id
     *
     * @return
     */
    /*
     * public long getSelectedPersonId() { return this.selected_person_id; } /**
     * Set Selected Persons Id
     * 
     * @param id
     */
    /*
     * public void setSelectedPersonId(long id) { this.selected_person_id = id;
     * }
     */

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
     * Load PlugIns
     */
    @Override
    public void loadPlugIns()
    {
        // TODO Auto-generated method stub

    }

    /**
     * Get About Image Size - Define about image size
     */
    @Override
    public int[] getAboutImageSize()
    {
        int[] sz = new int[2];
        sz[0] = 400;
        sz[1] = 200;

        return sz;
    }

    // configuration settings

    /**
     * Gets the max basal value.
     *
     * @return the max basal value
     */
    public float getMaxBasalValue()
    {
        // TODO read from pump
        return 8.0f;
    }

    /**
     * Gets the max bolus value.
     *
     * @return the max bolus value
     */
    public float getMaxBolusValue()
    {
        // TODO read from pump
        return 25.0f;
    }

    /**
     * Gets the basal step.
     *
     * @return the basal step
     */
    public float getBasalStep()
    {
        // TODO read from pump
        return 0.05f;
    }

    /**
     * Gets the bolus step.
     *
     * @return the bolus step
     */
    public float getBolusStep()
    {
        // TODO read from pump
        return 0.1f;
    }

    /**
     * Get Bolus Precision
     *
     * @return
     */
    public int getBolusPrecision()
    {
        return 1;
    }

    /**
     * Get Basal Precision
     *
     * @return
     */
    public int getBasalPrecision()
    {
        return 2;
    }

    /**
     * Create Old Data Reader
     */
    @Override
    public void createOldDataReader()
    {
        this.m_old_data_reader = new PumpDataReader(this);
    }

    /**
     * Get Formated Bolus Value
     *
     * @param val
     * @return
     */
    public String getFormatedBolusValue(float val)
    {
        return this.getDecimalHandler().getDecimalAsString(val, this.getBolusPrecision());
    }

    /**
     * Get Formated Bolus Value
     *
     * @param val
     * @return
     */
    public String getFormatedBasalValue(float val)
    {
        return this.getDecimalHandler().getDecimalAsString(val, this.getBasalPrecision());
    }

    /**
     * Get Decimal Handler
     *
     * @return
     */
    /*
     * public DecimalHandler getDecimalHandler() { return this.dec_handler; }
     */

    /**
     * Get Parsed Values
     *
     * @param val
     * @return
     */
    public String[] getParsedValues(String val)
    {
        ArrayList<String> lst = new ArrayList<String>();

        StringTokenizer strtok = new StringTokenizer(val, ";");

        while (strtok.hasMoreTokens())
        {
            String tk = strtok.nextToken();
            lst.add(tk.substring(tk.indexOf("=") + 1));
        }

        String ia[] = new String[lst.size()];
        return lst.toArray(ia);
    }

    /**
     * Sleep MS
     *
     * @param ms
     */
    public void sleepMs(long ms)
    {
        try
        {
            Thread.sleep(ms);
        }
        catch (Exception ex)
        {
        }
    }

    @Override
    public void loadSorters()
    {
        sorters.put("Profile", "DESC");
    }

    /**
     * Get Max Decimals that will be used by DecimalHandler
     *
     * @return
     */
    @Override
    public int getMaxDecimalsUsedByDecimalHandler()
    {
        return 3;
    }

    /**
     * Get Name of Plugin (for internal use)
     * 
     * @return
     */
    @Override
    public String getPluginName()
    {
        return "GGC Pump Plugin";
    }

    /**
     * Load Graph Context
     */
    public void loadGraphContext()
    {
        this.graph_context = new PumpGraphContext();
    }

}
