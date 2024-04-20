package ggc.plugin.util;

import java.io.File;
import java.text.DecimalFormat;
import java.util.*;

import javax.swing.*;

import com.atech.app.data.about.ModuleInfoEntry;
import org.apache.commons.collections4.CollectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.atech.app.data.about.CreditsGroup;
import com.atech.app.data.about.FeaturesGroup;
import com.atech.app.data.about.LibraryInfoEntry;
import com.atech.app.defs.AppPluginDefinition;
import com.atech.data.user_data_dir.UserDataDirectory;
import com.atech.db.hibernate.HibernateDb;
import com.atech.graphics.dialogs.selector.SelectableInterface;
import com.atech.graphics.graphs.v2.data.GraphContext;
import com.atech.graphics.graphs.v2.data.GraphDbDataRetriever;
import com.atech.i18n.I18nControlAbstract;
import com.atech.i18n.tool.simple.data.TranslationToolConfigurationDto;
import com.atech.plugin.PlugInServer;
import com.atech.update.startup.os.OSUtil;
import com.atech.utils.ATDataAccessAPDAbstract;
import com.atech.utils.ATSwingUtils;
import com.sun.jna.NativeLibrary;

import ggc.core.data.Converter_mgdL_mmolL;
import ggc.core.data.cfg.ConfigurationManager;
import ggc.core.data.defs.GlucoseUnitType;
import ggc.core.plugins.GGCPluginType;
import ggc.core.util.GGCI18nControl;
import ggc.plugin.cfg.DeviceConfigEntry;
import ggc.plugin.cfg.DeviceConfiguration;
import ggc.plugin.cfg.DeviceConfigurationDefinition;
import ggc.plugin.cfg.DeviceConfigurationDialog;
import ggc.plugin.data.DeviceDataHandler;
import ggc.plugin.data.DeviceValuesEntry;
import ggc.plugin.data.enums.DeviceEntryStatus;
import ggc.plugin.data.enums.DownloaderFilterType;
import ggc.plugin.db.PluginDb;
import ggc.plugin.defs.DevicePluginDefinitionAbstract;
import ggc.plugin.device.DeviceInterface;
import ggc.plugin.device.DownloadSupportType;
import ggc.plugin.device.v2.DeviceDefinition;
import ggc.plugin.device.v2.DeviceInstanceWithHandler;
import ggc.plugin.graph.PluginGraphDefinition;
import ggc.plugin.graph.util.PlugInGraphContext;
import ggc.plugin.gui.DeviceSpecialConfigPanelAbstract;
import ggc.plugin.gui.OldDataReaderAbstract;
import ggc.plugin.list.BaseListEntry;
import ggc.plugin.manager.DeviceManager;
import ggc.plugin.report.PluginReportDefinition;

// TODO: Auto-generated Javadoc
/**
 *  Application:   GGC - GNU Gluco Control
 *  Plug-in:       GGC PlugIn Base (base class for all plugins)
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
 *  Filename:     DataAccessPlugInBase
 *  Description:  This is superclass for all DataAccess classes for each plugin. Here are defined
 *                plugin specific features.
 *
 *  Author: Andy {andy@atech-software.com}
 */

public abstract class DataAccessPlugInBase extends ATDataAccessAPDAbstract
{

    private static final Logger LOG = LoggerFactory.getLogger(DataAccessPlugInBase.class);

    protected I18nControlPlugin i18n_plugin = null;

    /**
     * Plugin Version
     */
    public String plugin_version = "1.1.0";

    /**
     * Decimal format: 0 decimal places
     */
    public static DecimalFormat Decimal0Format = new DecimalFormat("#0");

    /**
     * Decimal format: 1 decimal places
     */
    public static DecimalFormat Decimal1Format = new DecimalFormat("#0.0");

    /**
     * Decimal format: 2 decimal places
     */
    public static DecimalFormat Decimal2Format = new DecimalFormat("#0.00");

    /**
     * Decimal format: 3 decimal places
     */
    public static DecimalFormat Decimal3Format = new DecimalFormat("#0.000");

    public static DecimalFormat DecimalFormaters[] = { Decimal0Format, Decimal1Format, Decimal2Format, Decimal3Format };

    /**
     * Which BG unit is configured: BG_MGDL = mg/dl, BG_MMOL = mmol/l
     */
    // public int m_BG_unit = BG_MMOL;

    public GlucoseUnitType glucoseUnitType;

    // this are special configs for devices V1 (should be removed at later time)
    public static Map<String, DeviceSpecialConfigPanelAbstract> special_configs = new HashMap<String, DeviceSpecialConfigPanelAbstract>();

    // this are special configs for devices V2
    public static Map<String, DeviceSpecialConfigPanelAbstract> specialConfigPanels = new HashMap<String, DeviceSpecialConfigPanelAbstract>();

    protected HibernateDb hibernateDb;
    protected PluginDb pluginDb;

    private PlugInDeviceUtil plugin_device_util = null;
    protected ConfigurationManager config_manager;

    /**
     * Yes/No Option
     */
    public static String[] yes_no_option = null;

    // web lister
    /**
     * The web_lister_cfg.
     */
    protected Hashtable<String, String> web_lister_cfg;

    /**
     * The m_manager.
     */
    protected DeviceManager m_manager = null;

    /**
     * The device_config_def.
     */
    protected DeviceConfigurationDefinition device_config_def;

    /**
     * The device_config.
     */
    protected DeviceConfiguration device_config;

    /**
     * The m_entry_type.
     */
    protected DeviceValuesEntry m_entry_type;

    /**
     * The columns_manual.
     */
    protected String columns_manual[] = null;

    /**
     * The column_widths_manual.
     */
    protected int column_widths_manual[] = null;

    /**
     * The entry_statuses.
     */
    public static String[] entry_statuses = null;

    /**
     * The reading_statuses.
     */
    public static String[] reading_statuses = null;

    /**
     * The filtering_states.
     */
    public static String[] filtering_states = null;

    protected boolean data_download_screen_wide = false;

    /**
     * The pluginServer.
     */
    PlugInServer pluginServer;

    /**
     * Entry Status Icons
     */
    public static String entry_status_icons[] = { "led_gray.gif", "led_green.gif", "led_yellow.gif", "led_red.gif" };

    /**
     * Icon images for each entry status
     */
    public static ImageIcon entry_status_iconimage[] = null;

    /**
     * The deviceDataHandler.
     */
    protected DeviceDataHandler deviceDataHandler;

    // protected int current_user_id = 0;

    protected OldDataReaderAbstract m_old_data_reader = null;

    protected I18nControlAbstract m_parent_i18n = null;

    protected PlugInGraphContext graph_context = null;

    protected GGCI18nControl i18n = null;

    // protected static GGCI18nControl i18nStatic = null;

    protected DeviceInstanceWithHandler selectedDeviceInstanceV2;
    protected DeviceInterface selectedDeviceInstanceV1;
    private String deviceSource;
    protected GraphContext graphContext;
    protected DevicePluginDefinitionAbstract pluginDefinition;
    private DeviceConfigurationDialog deviceConfigurationDialog;
    protected UserDataDirectory userDataDirectory;
    protected OSUtil osUtil = new OSUtil();


    // protected DevicePluginDefinitionAbstract devicePluginDefinition;

    // ********************************************************
    // ****** Constructors and Access methods *****
    // ********************************************************

    // Constructor: DataAccessPlugInBase
    /**
     *
     *  This is DataAccessPlugInBase constructor; Since classes use Singleton Pattern,
     *  constructor is protected and can be accessed only with getInstance()
     *  method.<br><br>
     *
     *  @param pluginDefinition AppPluginDefinition instance
     *
     */
    public DataAccessPlugInBase(AppPluginDefinition pluginDefinition)
    {
        super(pluginDefinition);

        this.pluginDefinition = (DevicePluginDefinitionAbstract) pluginDefinition;

        this.i18n_plugin = new I18nControlPlugin(pluginDefinition.getLanguageManager(),
                pluginDefinition.getI18nControlRunner(), this.getPluginType());
        this.i18n = new GGCI18nControl(this.getPluginType());

        this.userDataDirectory = UserDataDirectory.getInstance();

        initBase();
        initSpecial();

        // New
        prepareGraphContext();
    }


    /**
     * This initialization for parts which are started from here (no local configuration from
     * plugin required)
     */
    public void initBase()
    {
        this.helpEnabled = true;
        this.pluginDefinition.setDataAccess(this);
        this.pluginDefinition.registerDeviceHandlers();

        loadIcons();
        this.loadWebLister();

        DownloaderFilterType.translateKeywords(this.getI18nControlInstance());
    }


    @Override
    protected void initDataDefinitionManager()
    {

    }


    public GGCPluginType getPluginType()
    {
        return this.pluginDefinition.getPluginType();
    }


    /**
     * Get PlugIn Server Instance
     *
     * @param server PlugIn server instance
     */
    public void setPlugInServerInstance(PlugInServer server)
    {
        this.pluginServer = server;
    }


    /**
     * Set PlugIn Server Instance
     *
     * @return plugIn server instance
     */
    public PlugInServer getPlugInServerInstance()
    {
        return this.pluginServer;
    }


    // ********************************************************
    // ****** Init Methods *****
    // ********************************************************

    /**
     * Get Application Name
     */
    @Override
    public String getApplicationName()
    {
        return this.pluginDefinition.getPluginName();
    }


    /**
     * Get Actions Prefix for plugin (e.g. pumps_)
     */
    public String getPluginActionsPrefix()
    {
        return this.pluginDefinition.getPluginActionsPrefix();
    }


    /**
     * Check Prerequisites for Plugin
     */
    @Override
    public void checkPrerequisites()
    {
    }


    /**
     * Get Images Root
     */
    @Override
    public String getImagesRoot()
    {
        return "/icons/";
    }


    /**
     * Get Images for Devices
     *
     * @see ggc.plugin.util.DataAccessPlugInBase#getDeviceImagesRoot()
     * @return String with images path
     */
    public abstract String getDeviceImagesRoot();


    // ********************************************************
    // ****** About Methods *****
    // ********************************************************

    /**
     * Get About Dialog image name
     *
     * @return image name
     */
    public String getAboutImageName()
    {
        return this.pluginDefinition.getAboutImagePath();
    }


    /**
     * Get About Image Size
     *
     * @return image size
     */
    public int[] getAboutImageSize()
    {
        return this.pluginDefinition.getAboutImageSize();
    }


    /**
     * Get About Plugin Copyright
     *
     * @return copyright string
     */
    public String getAboutPluginCopyright()
    {
        int from = pluginDefinition.getCopyrightFrom();
        int till = pluginDefinition.getCopyrightTill();

        if (from == till)
        {
            return "" + from;
        }
        else
        {
            return from + "-" + till;
        }
    }


    /**
     * Get PlugIn Libraries
     *
     * @return plugin libraries
     */
    public List<LibraryInfoEntry> getPlugInLibraries()
    {
        List<LibraryInfoEntry> libraries = new ArrayList<>();

        if (includeBasePluginLibraries()) {
            libraries.addAll(getBaseLibraries());
        }

        List<LibraryInfoEntry> pluginLibraries = pluginDefinition.getPluginLibraries();

        if (CollectionUtils.isNotEmpty(pluginLibraries))
        {
            libraries.addAll(pluginLibraries);
        }

        return libraries;
    }

    public boolean includeBasePluginLibraries() {
        return true;
    }


    /**
     * Get Plugin Developers
     *
     * @return credits for plugin/app
     */
    public List<CreditsGroup> getPlugInDevelopers()
    {
        return this.pluginDefinition.getCredits();
    }


    /**
     * Get Plugin Features
     *
     * @return feature groups
     */
    public List<FeaturesGroup> getPlugInFeatures()
    {
        return this.pluginDefinition.getFeatures();
    }


    // ********************************************************
    // ****** Web Lister Methods *****
    // ********************************************************

    /**
     * Load WebLister Configuration
     */
    public void loadWebLister()
    {
        this.web_lister_cfg = this.getConfiguration( //
                userDataDirectory.getParsedUserDataPath("%USER_DATA_DIR%/tools/WebLister.properties"));
    }


    /**
     * Get WebLister Port, return port on which WebLister is residing
     *
     * @return number of port
     */
    public int getWebListerPort()
    {
        return getIntValueFromString(this.web_lister_cfg.get("http.port"), 4444);
    }


    /**
     * Get WebLister Description
     * 
     * @return description
     */
    public String getWebListerDescription()
    {
        return pluginDefinition.getWebListerDescription();
    }


    /**
     * Get Web Lister Items
     *
     * @return weblister items
     */
    public List<BaseListEntry> getWebListerItems()
    {
        return this.pluginDefinition.getWebListerItems();
    }


    // ********************************************************
    // ****** Devices and Configuration *****
    // ********************************************************

    /**
     * Create Configuration Context for plugin
     */
    public abstract void createConfigurationContext();


    /**
     * Get Device Configuration Definition
     *
     * @return DeviceConfigurationDefinition instance
     */
    public DeviceConfigurationDefinition getDeviceConfigurationDefinition()
    {
        return device_config_def;
    }


    /**
     * Get Device COnfiguration
     *
     * @return DeviceConfiguration instance
     */
    public DeviceConfiguration getDeviceConfiguration()
    {
        if (this.device_config == null)
        {
            createDeviceConfiguration();
        }

        return this.device_config;
    }


    /**
     * Create Device Configuration for plugin
     */
    public abstract void createDeviceConfiguration();


    // **********************************************************
    // ****** Empty definitions (not used by GGC plugins) *****
    // **********************************************************

    @Override
    public void loadPlugIns()
    {
    }


    @Override
    public void loadBackupRestoreCollection()
    {
    }


    // ********************************************************
    // ****** Version *****
    // ********************************************************

    /**
     * Get Plugin Version
     *
     * @return version as string
     */
    public String getPlugInVersion()
    {
        return this.pluginDefinition.getPluginVersion();
    }


    // ********************************************************
    // ****** BG Measurement Type *****
    // ********************************************************

    /**
     * Get BG Measurment Type
     *
     * @return type as int of measurement type
     */
    public GlucoseUnitType getGlucoseUnitType()
    {
        return this.glucoseUnitType;
    }


    /**
     * Set BG Measurment Type
     *
     * @param type as int of measurement type
     */
    public void setGlucoseUnitType(GlucoseUnitType type)
    {
        this.glucoseUnitType = type;
    }


    /**
     * Return BG values as it should be displayed (what we have configured for our display
     * GlucoseUnitType).
     *
     * @param bgValue BG value
     *
     * @return get correct (converted) BG value
     */
    public float getDisplayedBG(String bgValue)
    {
        return getDisplayedBG(Float.parseFloat(bgValue.replace(",", ".")));
    }


    /**
     * Return BG values as it should be displayed (what we have configured for our display
     * GlucoseUnitType).
     *
     * @param bgValue BG value
     *
     * @return get correct (converted) BG value
     */
    public float getDisplayedBG(Number bgValue)
    {
        switch (this.glucoseUnitType)
        {
            case mmol_L:
                return this.getBGConverter().getValueByType(GlucoseUnitType.mg_dL, GlucoseUnitType.mmol_L, bgValue);

            case None:
            case mg_dL:
            default:
                return bgValue.floatValue();
        }
    }


    /**
     * Get Displayed BG String
     *
     * @param bgValue BG value
     *
     * @return get correct (converted) BG value
     */
    public String getDisplayedBGString(String bgValue)
    {
        float val = 0.0f;

        if (bgValue != null && bgValue.length() != 0)
        {
            try
            {
                val = Float.parseFloat(bgValue.replace(",", "."));
                val = getDisplayedBG(val);
            }
            catch (Exception ex)
            {
                LOG.error("Error when converting BG:" + ex.getMessage(), ex);
            }
        }

        return getFormattedBGValue(this.glucoseUnitType, val);

    }


    /**
     * Get displayed BG value as String
     * 
     * @param bgValue BG value
     *
     * @return get correct (converted) BG value
     */
    public String getDisplayedBGString(Float bgValue)
    {
        float val = 0.0f;

        if (bgValue != null)
        {
            val = getDisplayedBG(bgValue);
        }

        return getFormattedBGValue(this.glucoseUnitType, val);
    }


    /**
     * Get BG Value from default type (mg/dL)
     *
     * @param outputType output type for Glucose Unit
     * @param bgValue BG value
     *
     * @return get correct (converted) BG value
     */
    public Float getBGValueFromDefault(GlucoseUnitType outputType, Number bgValue)
    {
        if (outputType == GlucoseUnitType.mg_dL)
        {
            return bgValue.floatValue();
        }
        else
        {
            return this.getBGConverter().getValueByType(GlucoseUnitType.mg_dL, outputType, bgValue);
        }
    }


    /**
     * Get BG Value By Type
     *
     * @param inputType input type for Glucose Unit
     * @param outputType output type for Glucose Unit
     * @param bgValue BG value
     *
     * @return get correct (converted) BG value
     */
    public Float getBGValueByType(GlucoseUnitType inputType, GlucoseUnitType outputType, Number bgValue)
    {
        return this.getBGConverter().getValueByType(inputType, outputType, bgValue);
    }


    /**
     * Get Displayed BG from default
     *
     * @param bgValue BG value
     *
     * @return formated value
     */
    public String getDisplayedBGFromDefault(Number bgValue)
    {
        if (this.glucoseUnitType == GlucoseUnitType.mg_dL)
            return DecimalFormaters[0].format(bgValue);
        else
        {
            return DecimalFormaters[1].format(getBGValueFromDefault(GlucoseUnitType.mmol_L, bgValue));
        }
    }


    /**
     * Get Formatted BG Value (it just formats current value as specified glucoseUnitType)
     *
     * @param inputType input type for Glucose Unit
     * @param bgValue BG value
     *
     * @return formated value
     */
    public String getFormattedBGValue(GlucoseUnitType inputType, Number bgValue)
    {
        if (glucoseUnitType == GlucoseUnitType.mg_dL)
            return DecimalFormaters[0].format(bgValue);
        else
            return DecimalFormaters[1].format(bgValue);
    }


    /**
     * Get BG Value Different
     *
     * @param inputType current type
     * @param bgValue BG value
     * @return converted BG value
     */
    public float getBGValueDifferent(GlucoseUnitType inputType, float bgValue)
    {
        return this.getBGConverter().getValueByType(inputType,
            (inputType == GlucoseUnitType.mmol_L) ? GlucoseUnitType.mg_dL : GlucoseUnitType.mmol_L, bgValue);
    }


    public String getFormatedValue(Number value, int decimals)
    {
        return DecimalFormaters[decimals].format(value);
    }


    public String getFormatedValueUS(Number value, int decimals)
    {
        return DecimalFormaters[decimals].format(value).replace(",", ".");
    }


    // ********************************************************
    // ****** Database *****
    // ********************************************************

    /**
     * Create Db
     *
     * @param db
     */
    public void createDb(HibernateDb db)
    {
        this.hibernateDb = db;
        createCustomDb();
    }


    public HibernateDb getHibernateDb()
    {
        return this.hibernateDb;
    }


    /**
     * Get HibernateDb instance (for working with database in plugin)
     */
    public PluginDb getPlugInDb()
    {
        return this.pluginDb;
    }


    /**
     * Create Custom Db
     *
     * This is for plug-in specific database implementation
     */
    public abstract void createCustomDb();


    // ********************************************************
    // ****** Database *****
    // ********************************************************

    /**
     * Get Device Manager
     *
     * @return DeviceManager instance
     */
    public DeviceManager getManager()
    {
        return this.m_manager;
    }


    /**
     * Load Manager instance
     */
    public abstract void loadManager();


    // ********************************************************
    // ****** Data Retrieval *****
    // ********************************************************

    /**
     * Create PlugIn Data Retrieval Context for plugin
     */
    public abstract void createPlugInDataRetrievalContext();


    /**
     * Get Columns for Manual Entry
     * @return
     */
    public String[] getColumnsManual()
    {
        return this.columns_manual;
    }


    /**
     * Get Columns Width for Manual Entry
     * @return
     */
    public int[] getColumnsWidthManual()
    {
        return this.column_widths_manual;
    }


    /**
     * Get Entry Statuses
     *
     * @return
     */
    public String[] getEntryStatuses()
    {
        return this.entry_statuses;
    }


    /**
     * Get Data Entry Object
     *
     * @return
     */
    public DeviceValuesEntry getDataEntryObject()
    {
        return m_entry_type;
    }


    /**
     * Load Device Data Handler
     */
    public abstract void loadDeviceDataHandler();


    /**
     * Get Device Data Handler
     *
     * @return DeviceDataHandler instance
     */
    public DeviceDataHandler getDeviceDataHandler()
    {
        return this.deviceDataHandler;
    }


    /**
     * Get Reading Statuses
     *
     * @return
     */
    public String[] getReadingStatuses()
    {
        return reading_statuses;
    }


    /**
     * Get Filtering States
     *
     * @return
     */
    public String[] getFilteringStates()
    {
        return this.filtering_states;
    }


    /**
     * Get Start Year
     *
     * @see com.atech.utils.ATDataAccessAbstract#getStartYear()
     */
    @Override
    public int getStartYear()
    {
        return 1970;
    }


    /**
     * Load Graph Config Properties
     *
     * @see com.atech.utils.ATDataAccessAbstract#loadGraphConfigProperties()
     */
    @Override
    public void loadGraphConfigProperties()
    {
    }


    /**
     * Sleep MS
     *
     * @param ms
     */
    public void sleepMS(int ms)
    {
        try
        {
            Thread.sleep(ms);
        }
        catch (Exception ex)
        {}
    }


    /**
     * Check if specified exception was caused by UnsatisfiedLinkError
     *
     * @param ex1
     * @return
     */
    public static boolean checkUnsatisfiedLink(Exception ex1)
    {
        Throwable ex = ex1.getCause();

        if (ex != null)
        {
            String ex_txt = ex.toString();

            if (ex_txt.contains("java.lang.UnsatisfiedLinkError"))
                return true;
        }

        return false;
    }


    /**
     * Get Extended data for UnsatisfiedLinkError (if we add new libraries that need native
     * parts, this is the method to extend it).
     *
     * @param ex1
     * @return String[], it contains following data: [0] = library name, [1] = native file name, [2] = short os name
     */
    public String[] getUnsatisfiedLinkData(Exception ex1)
    {
        Throwable ex = ex1.getCause();
        String ex_txt = ex.toString();
        String[] ret = new String[3];

        if (ex_txt.contains("rxtxSerial"))
        {
            ret[0] = "Rxtx";
            ret[1] = "rxtxSerial";
        }
        else
        {
            ret[0] = "Unknown";
            ret[1] = "Unknown";
        }

        ret[2] = osUtil.getShortOSName();

        return ret;

    }


    /**
     * Check Native Library
     *
     * @param native_dll_file
     * @return
     */
    public boolean checkNativeLibrary(String native_dll_file)
    {
        // FIXME

        if (native_dll_file.contains("rxtx"))
        {
            LOG.debug("We are using new version of Rxtx (packed in NrJavaSerial), which already contains .dll files.");
            return true;
        }

        try
        {
            LOG.debug("checkNativeLibrary: " + native_dll_file);
            // RXTX
            System.out.println("File: " + native_dll_file + "\nPath: " + System.getProperty("java.library.path"));
            NativeLibrary.addSearchPath(native_dll_file, System.getProperty("java.library.path"));
            NativeLibrary.getInstance(native_dll_file);

            // System.out.println("Found");
            return true;
        }
        catch (UnsatisfiedLinkError ex)
        {
            LOG.warn("checkNativeLibrary: Not found: " + ex, ex);
            return false;
        }
        catch (Exception ex)
        {
            LOG.warn("checkNativeLibrary: Not found: " + ex, ex);
            return false;
        }
    }


    /**
     * Load Special Parameters
     *
     * @see com.atech.utils.ATDataAccessAbstract#loadSpecialParameters()
     */
    @Override
    public void loadSpecialParameters()
    {
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
     * Create Old Data Reader
     */
    public void createOldDataReader()
    {
    }


    /**
     * Get Old Data Reader (instance of OldDataReaderAbstract which can read data already in database -
     *    for comparison purposes)
     *
     * @return
     */
    public OldDataReaderAbstract getOldDataReader()
    {
        return this.m_old_data_reader;
    }


    // /**
    // * Get Current User Id
    // *
    // * @return
    // */
    // @Override
    // public int getCurrentUserId()
    // {
    // return this.current_user_id;
    // }

    // /**
    // * Get Current User Id
    // *
    // * @return
    // */
    //
    // public int getCurrentUserIdAsInt()
    // {
    // return (int) this.current_user_id;
    // }
    //
    //
    // /**
    // * Set Current User Id
    // *
    // * @param user_id
    // */
    // @Override
    // public void setCurrentUserId(long user_id)
    // {
    // this.current_user_id = user_id;
    // }

    /**
     * Is Data Download Screen Wide
     *
     * @return
     */
    public boolean isDataDownloadScreenWide()
    {
        return this.data_download_screen_wide;
    }


    /**
     * Yes/No option
     *
     * @param val
     * @return
     */
    public String getYesNoOption(boolean val)
    {
        if (val)
            return DataAccessPlugInBase.yes_no_option[1];
        else
            return DataAccessPlugInBase.yes_no_option[0];

    }


    /**
     * Get Download Status For Selected Device
     *   0 = No status
     *   1 = Device not selected
     *   2 = Device doesn't support Download
     *
     *  10 = Download from device supported
     *  11 = Download from fileexport supported
     *  12 = 10 + 11
     *
     * @return
     */
    public DownloadSupportType getDownloadStatus()
    {
        Object pi = getSelectedDeviceInstance();

        if (pi == null)
        {
            return DownloadSupportType.NotSupportedByGGC;
        }
        else
        {
            return this.selectedDeviceInstanceV2 != null ? this.selectedDeviceInstanceV2.getDownloadSupportType()
                    : this.selectedDeviceInstanceV1.getDownloadSupportType();
        }

    }


    public void resetSelectedDeviceInstance()
    {
        this.selectedDeviceInstanceV1 = null;
        this.selectedDeviceInstanceV2 = null;
        this.device_config = null;
        getSelectedDeviceInstance();
    }


    /**
     * Get Selected Device Instance
     *
     * @return
     */
    public Object getSelectedDeviceInstance()
    {
        if (this.selectedDeviceInstanceV1 == null && this.selectedDeviceInstanceV2 == null)
        {
            DeviceConfigEntry dce = getDeviceConfiguration().getSelectedDeviceInstance();

            // System.out.println("DeviceConfigEntry [" + this.getPluginName() +
            // "]: " + dce);

            if (dce == null)
                return null;
            else
            {
                this.selectedDeviceInstanceV2 = this.getManager().getDeviceV2(dce.device_company, dce.device_device);

                if (this.selectedDeviceInstanceV2 == null)
                {
                    this.selectedDeviceInstanceV1 = this.getManager()
                            .getDeviceV1(dce.device_company, dce.device_device);
                }

                this.deviceSource = dce.device_company + " " + dce.device_device;
                // System.out.println("Source: " )
            }
        }

        if (this.selectedDeviceInstanceV1 == null && this.selectedDeviceInstanceV2 == null)
        {
            return null;
        }
        else
        {
            return this.selectedDeviceInstanceV2 != null ? this.selectedDeviceInstanceV2
                    : this.selectedDeviceInstanceV1;
        }

    }


    public DeviceConfigEntry getSelectedDeviceConfigEntry()
    {
        return getDeviceConfiguration().getSelectedDeviceInstance();
    }


    public DeviceDefinition getSelectedDeviceDefinition()
    {
        DeviceConfigEntry dce = getSelectedDeviceConfigEntry();

        // System.out.println("Selected Device: " + dce.device_company + " " +
        // dce.device_device);

        DeviceInstanceWithHandler deviceV2 = this.getManager().getDeviceV2(dce.device_company, dce.device_device);

        // System.out.println("DeviceInstance: " + deviceV2);

        return deviceV2.getDeviceDefinitionBase();

    }


    public boolean isSelectedDeviceOldType()
    {
        return (this.selectedDeviceInstanceV1 != null);
    }


    public boolean isSelectedDeviceNewType()
    {
        return (this.selectedDeviceInstanceV2 != null);
    }


    public String getCompanyNameForSelectedDevice()
    {
        if (isSelectedDeviceOldType())
        {
            return this.selectedDeviceInstanceV1.getDeviceCompany().getName();
        }
        else
        {
            return this.selectedDeviceInstanceV2.getCompany().getName();
        }
    }


    public String getDeviceNameForSelectedDevice()
    {
        if (isSelectedDeviceOldType())
        {
            return this.selectedDeviceInstanceV1.getName();
        }
        else
        {
            return this.selectedDeviceInstanceV2.getName();
        }
    }


    /**
     * Get Source Device
     *
     * @return
     */
    public String getSourceDevice()
    {
        return deviceSource;
    }


    /**
     * Get Configuration Manager
     *
     * @return
     */
    public ConfigurationManager getConfigurationManager()
    {
        return this.config_manager;
    }


    /**
     * Set Configuration Manager
     *
     * @param conf_mgr
     */
    public void setConfigurationManager(ConfigurationManager conf_mgr)
    {
        this.config_manager = conf_mgr;
    }


    /**
     * Get Max Decimals that will be used by DecimalHandler
     *
     * @return
     */
    @Override
    public int getMaxDecimalsUsedByDecimalHandler()
    {
        return 2;
    }


    /**
     * Get Parent I18nControl Instance. This will be used where translation will be taken
     * from parent (Core). For example in printing.
     *
     * @return
     */
    public I18nControlAbstract getParentI18nControlInstance()
    {
        return this.m_parent_i18n;
    }


    /**
     * Set Parent I18nControl Instance. This will be used where translation will be taken
     * from parent (Core). For example in printing.
     *
     * @param ic
     *
     */
    public void setParentI18nControlInstance(I18nControlAbstract ic)
    {
        this.m_parent_i18n = ic;
    }


    /**
     * Get PlugIn Device Util
     *
     * @return
     */
    public PlugInDeviceUtil getPluginDeviceUtil()
    {
        if (plugin_device_util == null)
        {
            plugin_device_util = new PlugInDeviceUtil(this);
        }

        return this.plugin_device_util;
    }


    public I18nControlPlugin getI18nControlInstanceBase()
    {
        if (this.i18n_plugin == null)
        {
            this.i18n_plugin = new I18nControlPlugin(this.languageManager, this.i18nControlRunner, this.getPluginType());
        }

        return this.i18n_plugin;
    }


    @Override
    public GGCI18nControl getI18nControlInstance()
    {
        return this.i18n;
    }

    private boolean baseTranslationsLoaded = false;


    /**
     * Load Base PlugIn Translations
     */
    public void loadBasePluginTranslations()
    {
        if (baseTranslationsLoaded)
            return;

        GGCI18nControl ic = this.getI18nControlInstance();

        yes_no_option = new String[2];
        yes_no_option[0] = ic.getMessage("NO");
        yes_no_option[1] = ic.getMessage("YES");

        entry_statuses = new String[4];
        entry_statuses[0] = ic.getMessage("UNKNOWN");
        entry_statuses[1] = ic.getMessage("NEW");
        entry_statuses[2] = ic.getMessage("CHANGED");
        entry_statuses[3] = ic.getMessage("OLD");

        // months
        months[0] = ic.getMessage("JANUARY");
        months[1] = ic.getMessage("FEBRUARY");
        months[2] = ic.getMessage("MARCH");
        months[3] = ic.getMessage("APRIL");
        months[4] = ic.getMessage("MAY");
        months[5] = ic.getMessage("JUNE");
        months[6] = ic.getMessage("JULY");
        months[7] = ic.getMessage("AUGUST");
        months[8] = ic.getMessage("SEPTEMBER");
        months[9] = ic.getMessage("OCTOBER");
        months[10] = ic.getMessage("NOVEMBER");
        months[11] = ic.getMessage("DECEMBER");

        // days
        days[0] = ic.getMessage("MONDAY");
        days[1] = ic.getMessage("TUESDAY");
        days[2] = ic.getMessage("WEDNESDAY");
        days[3] = ic.getMessage("THURSDAY");
        days[4] = ic.getMessage("FRIDAY");
        days[5] = ic.getMessage("SATURDAY");
        days[6] = ic.getMessage("SUNDAY");

        gcDays[0] = ic.getMessage("SUNDAY");
        gcDays[1] = ic.getMessage("MONDAY");
        gcDays[2] = ic.getMessage("TUESDAY");
        gcDays[3] = ic.getMessage("WEDNESDAY");
        gcDays[4] = ic.getMessage("THURSDAY");
        gcDays[5] = ic.getMessage("FRIDAY");
        gcDays[6] = ic.getMessage("SATURDAY");

        reading_statuses = new String[7];

        reading_statuses[0] = ic.getMessage("STATUS_NONE");
        reading_statuses[1] = ic.getMessage("STATUS_READY");
        reading_statuses[2] = ic.getMessage("STATUS_DOWNLOADING");
        reading_statuses[3] = ic.getMessage("STATUS_STOPPED_DEVICE");
        reading_statuses[4] = ic.getMessage("STATUS_STOPPED_USER");
        reading_statuses[5] = ic.getMessage("STATUS_DOWNLOAD_FINISHED");
        reading_statuses[6] = String.format(ic.getMessage("STATUS_READER_ERROR"),
            i18n_plugin.getMessage("DEVICE_NAME_BIG"));

        this.filtering_states = new String[7];

        filtering_states[0] = ic.getMessage("FILTER_ALL");
        filtering_states[1] = ic.getMessage("FILTER_NEW");
        filtering_states[2] = ic.getMessage("FILTER_CHANGED");
        filtering_states[3] = ic.getMessage("FILTER_EXISTING");
        filtering_states[4] = ic.getMessage("FILTER_UNKNOWN");
        filtering_states[5] = ic.getMessage("FILTER_NEW_CHANGED");
        filtering_states[6] = ic.getMessage("FILTER_ALL_BUT_EXISTING");

        baseTranslationsLoaded = true;
    }


    /**
     * Path Resolver
     *
     * @param path
     * @return
     */
    public String pathResolver(String path)
    {
        return path.replace('\\', File.separatorChar);
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
        return (Converter_mgdL_mmolL) this.getConverter("BG");

    }


    /**
     * Init all Objects
     */
    public void initAllObjects()
    {
    }


    /**
     * Load Icons
     */
    public void loadIcons()
    {
        JDialog d = new JDialog();
        DeviceEntryStatus.loadIcons(d, this);

        if (entry_status_iconimage == null)
        {

            entry_status_iconimage = new ImageIcon[4];

            for (int i = 0; i < entry_status_icons.length; i++)
            {
                entry_status_iconimage[i] = ATSwingUtils.getImageIcon(entry_status_icons[i], 8, 8, d, this);
            }
        }

        d = null;
    }


    /**
     * Get Name of Plugin (for internal use)
     * @return
     */
    public String getPluginName() {
        return pluginDefinition.getPluginName();
    }


    public List<LibraryInfoEntry> getBaseLibraries()
    {
        int currentYear = (new GregorianCalendar()).get(Calendar.YEAR);

        // libraries
        ArrayList<LibraryInfoEntry> lst_libs = new ArrayList<LibraryInfoEntry>();

        lst_libs.add(new LibraryInfoEntry("Atech Tools",
                "0.8.8",
                "https://github.com/andyrozman/atech-tools",
                "LGPL (v2.1)",
                "Java Helper Library for Swing/Hibernate/SQL...",
                "Copyright (c) 2006-" + currentYear + " Atech Software Ltd. All rights reserved."));

        lst_libs.add(new LibraryInfoEntry("dom4j",
                "1.6.1",
                "http://www.dom4j.org/",
                "BSD",
                "Framework for Xml manipulation"));

        lst_libs.add(new LibraryInfoEntry("Neuron Robotics Java Serial",
                "5.2.1",
                "https://github.com/NeuronRobotics/nrjavaserial",
                "LGPL (v2.1) / RXTX 2.1",
                "Serial Communication API"));

        lst_libs.add(new LibraryInfoEntry("IBM Com Api", //
                "1.3", //
                "https://www.ibm.com", //
                "LGPL", //
                "Comm API (used for BT devices for now)"));

        lst_libs.add(new LibraryInfoEntry("Jaxen", //
                "1.1.6", //
                "http://jaxen.codehaus.org/", //
                "Custom", //
                "Jaxen is a universal Java XPath engine."));

        lst_libs.add(new LibraryInfoEntry("HID for Java",
                "0.3.1",
                "https://github.com/gary-rowe/hid4java",
                "MIT",
                "Library for connection to USB HID devices."));

        lst_libs.add(new LibraryInfoEntry("LibAums for Usb4java",
                "0.4.1",
                "https://github.com/andyrozman/libaums-usb4java",
                "Apache (v2)",
                "Library for USB SCSI intrface linked with Usb4Java."));

        lst_libs.add(new LibraryInfoEntry("Java Native Access",
                "5.5.6",
                "https://github.com/java-native-access",
                "LGPL (2.1)",
                "Access to Native libraries."));

        lst_libs.add(new LibraryInfoEntry("Pygmy Http Server",
                "0.4.3",
                "https://github.com/andyrozman/pygmy-httpd/",
                "Artistic",
                "Small Web Server"));

        lst_libs.add(new LibraryInfoEntry("iTextPdf",
                "5.5.13.3",
                "http://itextpdf.com/",
                "Affero GPL v3",
                "Library for PDF creation (printing)"));

        lst_libs.add(new LibraryInfoEntry("JFreeChart",
                "1.0.13",
                "http://https://www.jfree.org/jfreechart/",
                "LGPL (v3)",
                "Library for Graphs"));

        return lst_libs;

    }


    public int getIndexOfElementInArray(String[] descriptions, String description)
    {
        for (int i = 0; i < descriptions.length; i++)
        {
            if (descriptions[i].equals(description))
                return i;
        }

        return 0;
    }


    public abstract void prepareGraphContext();


    /**
     * Get Graph Context
     *
     * @return
     */
    public GraphContext getGraphContext()
    {
        return this.graphContext;
    }


    public int compareTo(SelectableInterface selectableInterface)
    {
        return 0;
    }


    public void prepareManagers()
    {
    }


    public PluginReportDefinition getReportsDefinition()
    {
        return this.pluginDefinition.getReportsDefinition();
    }


    public PluginGraphDefinition getGraphsDefinition()
    {
        return this.pluginDefinition.getGraphsDefinition();
    }


    public DevicePluginDefinitionAbstract getPluginDefinition()
    {
        return this.pluginDefinition;
    }


    @Override
    public GraphDbDataRetriever getGraphDbDataRetriever()
    {
        return this.getPlugInDb();
    }


    /**
     * Get Graph Context
     *
     * @return
     */
    // public PlugInGraphContext getGraphContext()
    // {
    // return this.graph_context;
    // }

    // ********************************************************
    // ****** New Implementations *****
    // ********************************************************

    /**
     * This is for loading simple class that it's ouside GGC scope. Mostly intended for testing
     * purposes (before adding code directly to GGC). This is intended for Constructors with
     * no parameters.
     *
     * @param className
     * @return
     */
    protected boolean loadExternalLibrary(String className)
    {
        try
        {
            Class<?> c = Class.forName(className);

            c.newInstance();

            return true;
        }
        catch (ClassNotFoundException ex)
        {
            LOG.debug("Class {} could not be found. {}", className, ex.getMessage(), ex);
        }
        catch (InstantiationException ex)
        {
            LOG.error("Class {} could not be loaded.", className, ex.getMessage(), ex);
        }
        catch (IllegalAccessException ex)
        {
            LOG.error("Class {} could not be found and/or loaded.", className, ex.getMessage(), ex);
        }
        catch (Exception ex)
        {
            LOG.error("Class {} could not be loaded. Exception: {}", className, ex.getMessage(), ex);
        }

        return false;

    }


    @Override
    public void initObserverManager()
    {
    }


    /**
     * Get Float As String
     *
     * @param f
     * @param decimalPlaces
     * @return
     */
    public String getFloatAsString(float f, String decimalPlaces)
    {
        return getFloatAsString(f, Integer.parseInt(decimalPlaces));
    }


    /**
     * Get Float As String
     *
     * @param f
     * @param decimalPlaces
     * @return
     */
    public String getFloatAsString(float f, int decimalPlaces)
    {
        return getDecimalHandler().getDecimalNumberAsString(f, decimalPlaces);
    }


    @Override
    protected void initInternalSettings()
    {

    }


    @Override
    public TranslationToolConfigurationDto getTranslationToolConfiguration()
    {
        return null;
    }


    @Override
    public void saveTranslationToolConfiguration(TranslationToolConfigurationDto configuration)
    {

    }


    public void setDeviceConfigurationDialog(DeviceConfigurationDialog deviceConfigurationDialog)
    {
        this.deviceConfigurationDialog = deviceConfigurationDialog;
    }


    public DeviceConfigurationDialog getDeviceConfigurationDialog()
    {
        return deviceConfigurationDialog;
    }


    public static void sleep(long delay)
    {
        try
        {
            Thread.sleep(delay);
        }
        catch (InterruptedException e)
        {

        }
    }


    public ModuleInfoEntry getBaseModule() {
        return ModuleInfoEntry.builder()
                .name(i18n.getMessage("BASE_PLUGIN_NAME"))
                .version(new ggc.plugin.defs.Version().getVersion())
                .description(i18n.getMessage("BASE_PLUGIN_DESCRIPTION"))
                .build();
    }

    public abstract ModuleInfoEntry getPluginModule();


}
