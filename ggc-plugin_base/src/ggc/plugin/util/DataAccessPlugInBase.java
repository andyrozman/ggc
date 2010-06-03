package ggc.plugin.util;

import ggc.core.data.Converter_mgdL_mmolL;
import ggc.core.data.cfg.ConfigurationManager;
import ggc.plugin.cfg.DeviceConfigEntry;
import ggc.plugin.cfg.DeviceConfiguration;
import ggc.plugin.cfg.DeviceConfigurationDefinition;
import ggc.plugin.data.DeviceDataHandler;
import ggc.plugin.data.DeviceValuesEntry;
import ggc.plugin.device.DeviceInterface;
import ggc.plugin.gui.OldDataReaderAbstract;
import ggc.plugin.list.BaseListEntry;
import ggc.plugin.manager.DeviceManager;
import ggc.plugin.manager.company.AbstractDeviceCompany;
import ggc.plugin.output.OutputUtil;

import java.io.File;
import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.GregorianCalendar;
import java.util.Hashtable;

import javax.swing.ImageIcon;
import javax.swing.JDialog;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.atech.db.hibernate.HibernateDb;
import com.atech.graphics.components.about.CreditsGroup;
import com.atech.graphics.components.about.FeaturesGroup;
import com.atech.graphics.components.about.LibraryInfoEntry;
import com.atech.i18n.I18nControlAbstract;
import com.atech.i18n.I18nControlRunner;
import com.atech.i18n.mgr.LanguageManager;
import com.atech.plugin.PlugInServer;
import com.atech.update.startup.os.OSUtil;
import com.atech.utils.ATDataAccessLMAbstract;
import com.sun.jna.NativeLibrary;

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


public abstract class DataAccessPlugInBase extends ATDataAccessLMAbstract
{
    
    
    protected I18nControlPlugin i18n_plugin = null;
    
    /**
     * Plugin Version
     */
    public String plugin_version = "0.4.5";


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

    /**
     * Which BG unit is used: BG_MGDL = mg/dl, BG_MMOL = mmol/l
     */
    public int m_BG_unit = BG_MMOL;

    
    
    
    /**
     * The hdb.
     */
    protected HibernateDb hdb;

    
    private PlugInDeviceUtil plugin_device_util = null;
    protected ConfigurationManager config_manager;
    private static Log log = LogFactory.getLog(DataAccessPlugInBase.class);
    
        
    /**
     * Yes/No Option
     */
    public static String[] yes_no_option = null;

    // about
    //protected String about_title;
    /**
     * The about_image_name.
     */
    protected String about_image_name;
    //protected String about_plugin_name;
    /**
     * The about_plugin_copyright_from.
     */
    protected int about_plugin_copyright_from;
    
    /**
     * The plugin_libraries.
     */
    protected ArrayList<LibraryInfoEntry> plugin_libraries;
    
    /**
     * The plugin_developers.
     */
    protected ArrayList<CreditsGroup> plugin_developers;
    
    /**
     * The plugin_features.
     */
    protected ArrayList<FeaturesGroup> plugin_features; 

    // web lister
    /**
     * The web_lister_cfg.
     */
    protected Hashtable<String,String> web_lister_cfg;
    
    /**
     * The weblister_title.
     */
    protected String weblister_title;
    
    /**
     * The weblister_desc.
     */
    protected String weblister_desc;
    
    /**
     * The weblister_items.
     */
    protected ArrayList<BaseListEntry> weblister_items;

    
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
     * The columns_table.
     */
    protected String columns_table[] = null;
    
    /**
     * The column_widths_table.
     */
    protected int column_widths_table[] = null;
    
    /**
     * The entry_statuses.
     */
    protected String[] entry_statuses = null;
    
    /**
     * The reading_statuses.
     */
    protected String[] reading_statuses = null;
    
    /**
     * The filtering_states.
     */
    protected String[] filtering_states = null;
    
    
    protected boolean data_download_screen_wide = false;

    
    /**
     * The plugin_server.
     */
    PlugInServer plugin_server;

    
    /**
     * Entry Status Icons 
     */
    public static String entry_status_icons[] = 
    {
         "led_gray.gif",
         "led_green.gif",
         "led_yellow.gif",
         "led_red.gif"
    };

    
    
    /**
     * Icon images for each entry status
     */
    public static ImageIcon entry_status_iconimage[] = null;
    
    
    /**
     * The m_ddh.
     */
    protected DeviceDataHandler m_ddh;

    
    protected long current_user_id = 0;

    
    protected OldDataReaderAbstract m_old_data_reader = null;

    
    protected I18nControlAbstract m_parent_i18n = null;    
    

    // ********************************************************
    // ******      Constructors and Access methods        *****    
    // ********************************************************

    //   Constructor:  DataAccessMeter
    /**
     *
     *  This is DataAccessPlugInBase constructor; Since classes use Singleton Pattern,
     *  constructor is protected and can be accessed only with getInstance() 
     *  method.<br><br>
     *  
     *  @param lm language manager 
     *  @param icr i18n control runner
     *
     */
    public DataAccessPlugInBase(LanguageManager lm, I18nControlRunner icr)
    {
    	super(lm, icr); //I18nControl.getInstance());

    	this.i18n_plugin = new I18nControlPlugin(lm, icr);
    	loadIcons();
        initSpecial();
    } 


    /**
     * Get PlugIn Server Instance
     * 
     * @param server
     */
    public void setPlugInServerInstance(PlugInServer server)
    {
        this.plugin_server = server;
    }
    

    /**
     * Set PlugIn Server Instance
     * 
     * @return 
     */
    public PlugInServer getPlugInServerInstance()
    {
        return this.plugin_server;
    }
    
    
    
    
    // ********************************************************
    // ******             Init Methods                    *****    
    // ********************************************************
    

    /** 
     * Get Application Name
     */
    public abstract String getApplicationName();
    
    
    /** 
     * Check Prerequisites for Plugin
     */
    public void checkPrerequisites()
    {
    }
    
    
    /** 
     * Get Images Root
     */
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
    
    
    /** 
     * loadBackupRestoreCollection
     */
    public void loadBackupRestoreCollection()
    {
    }
    
    
    // ********************************************************
    // ******            About Methods                    *****    
    // ********************************************************

    
    /**
     * Create About Context for plugin
     */
    public abstract void createPlugInAboutContext();
    
    
    /**
     * Get About Dialog image name
     * 
     * @return
     */
    public String getAboutImageName()
    {
        return this.about_image_name;
    }

    /**
     * Get About Image Size
     * 
     * @return
     */
    public int[] getAboutImageSize()
    {
        int[] sz = new int[2];
        sz[0] = 500;
        sz[1] = 125;
        
        return sz;
    }
    
    /**
     * Get About Plugin Copyright
     * 
     * @return
     */
    public String getAboutPluginCopyright()
    {
        int till = new GregorianCalendar().get(GregorianCalendar.YEAR);
        
        if (this.about_plugin_copyright_from==till)
        {
            return "" + till; 
        }
        else
        {
            return this.about_plugin_copyright_from + "-" + till;
        }
        
    }
    
    
    /**
     * Get PlugIn Libraries
     * 
     * @return
     */
    public ArrayList<LibraryInfoEntry> getPlugInLibraries()
    {
        return this.plugin_libraries;
    }
    
    /**
     * Get Plugin Developers
     * 
     * @return
     */
    public ArrayList<CreditsGroup> getPlugInDevelopers()
    {
        return this.plugin_developers;
    }
    
    /**
     * Get Plugin Features
     * 
     * @return
     */
    public ArrayList<FeaturesGroup> getPlugInFeatures()
    {
        return this.plugin_features;
    }
    

    
    // ********************************************************
    // ******         Web Lister Methods                  *****    
    // ********************************************************

    
    /**
     * Load WebLister Configuration
     */
    public void loadWebLister()
    {
        this.web_lister_cfg = this.getConfiguration("../data/tools/WebLister.properties");
    }


    /**
     * Get WebLister Port, return poer on which WebLister is residing
     * 
     * @return number of port 
     */
    public int getWebListerPort()
    {
        return this.getIntValueFromString(this.web_lister_cfg.get("http.port"), 4444);
    }
    
    
    /**
     * Create WebLister (for List) Context for plugin
     */
    public abstract void createWebListerContext();
    
    
    /**
     * Get Web Lister Title
     * 
     * @return web lister title
     */
    public String getWebListerTitle()
    {
        return this.weblister_title;
    }

    
    /**
     * Get Web Lister Description
     * 
     * @return description text
     */
    public String getWebListerDescription()
    {
        return this.weblister_desc;
    }
    

    /**
     * Get Web Lister Items
     * 
     * @return
     */
    public ArrayList<BaseListEntry> getWebListerItems()
    {
        return weblister_items;
    }
    

    // ********************************************************
    // ******                Configuration                *****    
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
        if (this.device_config==null)
            createDeviceConfiguration();
        
        return this.device_config;
    }
    
    
    /**
     * Create Device Configuration for plugin
     */
    public abstract void createDeviceConfiguration();

    
    // ********************************************************
    // ******                  Version                    *****    
    // ********************************************************

    
    /**
     * Get Plugin Version
     * 
     * @return version as string
     */
    public String getPlugInVersion()
    {
        return this.plugin_version;
    }
    
    
    /**
     * Create Plugin Version
     */
    public abstract void createPlugInVersion();
    
    
    // ********************************************************
    // ******             BG Measurement Type             *****    
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
     * Get BG Measurment Type
     * 
     * @return type as int of measurement type
     */
    public int getBGMeasurmentType()
    {
        return this.m_BG_unit;
    }

    
    /**
     * Set BG Measurment Type
     * 
     * @param type as int of measurement type
     */
    public void setBGMeasurmentType(int type)
    {
        this.m_BG_unit = type;
    }

    
    /**
     * Get BG Measurment Type Name
     * 
     * @return type as string
     */
    public String getBGMeasurmentTypeName()
    {
        return OutputUtil.unitsName[this.m_BG_unit];
    }
    
    
    /**
     * Get BG Measurment Type Name
     * 
     * @param type type index 
     * @return type as string
     */
    public String getBGTypeName(int type)
    {
        return OutputUtil.unitsName[type];
    }
    

    /**
     * Get BG Type Name Static
     * 
     * @param type measurment type
     * @return name as string
     */
    public static String getBGTypeNameStatic(int type)
    {
        return OutputUtil.unitsName[type];
    }

    private static final float MGDL_TO_MMOL_FACTOR = 0.0555f;

    private static final float MMOL_TO_MGDL_FACTOR = 18.016f;

    
    /**
     * Depending on the return value of <code>getBGMeasurmentType()</code>, either
     * return the mg/dl or the mmol/l value of the database's value. Default is mg/dl.
     * @param dbValue - The database's value (in float)
     * @return the BG in either mg/dl or mmol/l
     */
    public float getDisplayedBG(float dbValue)
    {
        switch (this.m_BG_unit)
        {
        case BG_MMOL:
            // this POS should return a float rounded to 3 decimal places,
            // if I understand the docu correctly
            return (new BigDecimal(dbValue * MGDL_TO_MMOL_FACTOR,
                    new MathContext(3, RoundingMode.HALF_UP)).floatValue());
        case BG_MGDL:
        default:
            return dbValue;
        }
    }

    /**
     * Get Displayed BG String 
     * 
     * @param bgValue
     * @return
     */
    public String getDisplayedBGString(String bgValue)
    {
        float val = 0.0f;
        
        if ((bgValue!=null) && (bgValue.length()!=0))
        {
            try
            {
                val = Float.parseFloat(bgValue);
                val = getDisplayedBG(val);
            }
            catch(Exception ex)
            {}
        }

        if (this.m_BG_unit==BG_MGDL)
            return DataAccessPlugInBase.Decimal0Format.format(val);
        else
            return DataAccessPlugInBase.Decimal1Format.format(val);
            
    }
    
    
    /**
     * Get BG Value (converted to current display type)
     * 
     * @param bg_value bg values
     * @return bg_value
     */
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


    /**
     * Get BG Value by type
     * 
     * @param type input type
     * @param bg_value bg value to convert
     * @return converted value
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
     * Get BG Value by type
     * 
     * @param input_type input type
     * @param output_type output type
     * @param bg_value bg value to convert
     * @return converted value
     */
    public float getBGValueByType(int input_type, int output_type, float bg_value)
    {
        //System.out.println("BG: " + bg_value);
        //System.out.println("input: " + input_type + ",output=" + output_type);
        if (input_type==output_type)
            return bg_value;
        else
        {
            if (output_type==DataAccessPlugInBase.BG_MGDL)
            {
                //System.out.println("BG-2 [mgDL]: " + bg_value * MGDL_TO_MMOL_FACTOR);
                return bg_value * MMOL_TO_MGDL_FACTOR;
            }
            else
            {
                //System.out.println("BG-2 [mmol]: " + bg_value * MMOL_TO_MGDL_FACTOR);
                return bg_value * MGDL_TO_MMOL_FACTOR;
            }
        }

    }

    
    /**
     * Get BG Value In Selected Format
     * 
     * @param bg_value bg value to convert
     * @return converted value
     */
    public float getBGValueInSelectedFormat(int bg_value)
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
    

    /**
     * Get BG Value by Type
     * 
     * @param input_type input type
     * @param output_type output type
     * @param bg_value_s bg value to convert (as String)
     * @return converted value
     */
    public float getBGValueByType(int input_type, int output_type, String bg_value_s)
    {
        
        float bg_value = 0.0f;
        
        try
        {
            bg_value = Float.parseFloat(bg_value_s.replace(',', '.'));
        }
        catch(Exception ex)
        {
        }
        
        return getBGValueByType(input_type, output_type, bg_value);

    }
    
    
    /**
     * Get BG Value Different
     * 
     * @param type current type
     * @param bg_value BG value
     * @return converted BG value
     */
    public float getBGValueDifferent(int type, float bg_value)
    {

        if (type==DataAccessPlugInBase.BG_MGDL)
        {
            return bg_value * MGDL_TO_MMOL_FACTOR;
        }
        else
        {
            return bg_value * MMOL_TO_MGDL_FACTOR;
        }
    }

    
    // ********************************************************
    // ******                   Database                  *****    
    // ********************************************************
    
    
    
    /**
     * Create Db
     * 
     * @param db
     */
    public void createDb(HibernateDb db)
    {
        this.hdb = db;
        createCustomDb();
    }

    
    /** 
     * Get HibernateDb instance (for working with database in plugin)
     */
    public HibernateDb getHibernateDb()
    {
        return this.hdb;
    }
    
    
    /**
     * Create Custom Db
     * 
     * This is for plug-in specific database implementation
     */
    public abstract void createCustomDb();
    
    


    // ********************************************************
    // ******                   Database                  *****    
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
    // ******             Data Retrieval                  *****    
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
     * Get Columns for Retrieval Entry
     * 
     * @return
     */
    public String[] getColumnsTable()
    {
        return this.columns_table;
    }

    
    /**
     * Get Columns Width for Retrieval Entry
     * 
     * @return
     */
    public int[] getColumnsWidthTable()
    {
        return this.column_widths_table;
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
        return this.m_ddh;
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
     * Load Reading Statuses
     */
    public void loadReadingStatuses()
    {
         
        
    }
    

    /**
     * Get Start Year
     * 
     * @see com.atech.utils.ATDataAccessAbstract#getStartYear()
     */
    public int getStartYear()
    {
        return 1970;
    }

    
    /**
     * Load Graph Config Properties
     * 
     * @see com.atech.utils.ATDataAccessAbstract#loadGraphConfigProperties()
     */
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
        catch(Exception ex)
        {
        }
    }
    
    
    /**
     * Check if specified exception was caused by UnsatisfiedLinkError
     * 
     * @param ex1
     * @return
     */
    public boolean checkUnsatisfiedLink(Exception ex1)
    {
        Throwable ex = ex1.getCause();
        
        if (ex!=null)
        {
            String ex_txt = ex.toString();
            
            if (ex_txt.contains("java.lang.UnsatisfiedLinkError"))
            {
                return true;
            }
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
        
        ret[2] = OSUtil.getShortOSName();
        
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
        try 
        {
            log.debug("checkNativeLibrary: " + native_dll_file);
            // RXTX
            System.out.println("File: " + native_dll_file + "\nPath: " + System.getProperty("java.library.path"));
            NativeLibrary.addSearchPath(native_dll_file, System.getProperty("java.library.path"));
            NativeLibrary.getInstance(native_dll_file);
            
            //System.out.println("Found");
            return true;
        } 
        catch (UnsatisfiedLinkError ex) 
        {
            log.warn("checkNativeLibrary: Not found: " + ex, ex);
            return false;
        }
        catch(Exception ex)
        {
            log.warn("checkNativeLibrary: Not found: " + ex, ex);
            return false;
        }
    } 
    
    
    /**
     * Load Special Parameters
     * 
     * @see com.atech.utils.ATDataAccessAbstract#loadSpecialParameters()
     */
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
     * Get Selected Lang Index
     */
    public int getSelectedLangIndex()
    {
        return 0;
    }

    
    /** 
     * Set Selected Lang Index
     */
    public void setSelectedLangIndex(int index)
    {
        
    }
    
    
    /**
     * Create Old Data Reader
     */
    public abstract void createOldDataReader();
    
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
    
    
    /**
     * Get Current User Id
     * 
     * @return
     */
    public long getCurrentUserId()
    {
        return this.current_user_id;
    }
    
    /**
     * Set Current User Id
     * 
     * @param user_id
     */
    public void setCurrentUserId(long user_id)
    {
        this.current_user_id = user_id;
    }
    
 
    
    /**
     * Is Data Download Screen Wide
     * 
     * @return
     */
    public boolean isDataDownloadSceenWide()
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
    public int getDownloadStatus()
    {
        
        DeviceInterface pi = getSelectedDeviceInstance();
        
        if (pi==null)
            return 1;
        else
        {
            return pi.getDownloadSupportType();
            
            /*
            if (pi.getDownloadSupportType()==DownloadSupportType.DOWNLOAD_YES)
                return 10;
            else
                return 2;
                */
        }
        
    }
    

    /**
     * Get Selected Device Instance
     * 
     * @return
     */
    public DeviceInterface getSelectedDeviceInstance()
    {
        DeviceConfigEntry dce = getDeviceConfiguration().getSelectedDeviceInstance();
        
        if (dce==null)
            return null;
        else
        {
            AbstractDeviceCompany adc = this.getManager().getCompany(dce.device_company);
            
            if (adc==null)
                return null;
                
            
            return (DeviceInterface)adc.getDevice(dce.device_device);
        }
    }
    
    
    
    /**
     * Get Source Device
     * 
     * @return
     */
    public String getSourceDevice()
    {
        return getSelectedDeviceInstance().getDeviceSourceName();
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
        if (plugin_device_util==null)
            plugin_device_util = new PlugInDeviceUtil(this);
        
        return this.plugin_device_util;
    }
 
    
    public I18nControlPlugin getI18nControlInstance()
    {
        if (this.i18n_plugin==null)
        {
            this.i18n_plugin = new I18nControlPlugin(this.lang_mgr, this.m_icr); 
        }
        
        return this.i18n_plugin;
    }
    
 
    /**
     * Load Base PlugIn Translations
     */
    public void loadBasePluginTranslations()
    {
        
        yes_no_option = new String[2];
        yes_no_option[0] = this.getI18nControlInstance().getMessage("NO");
        yes_no_option[1] = this.i18n_plugin.getMessage("YES");
            
        entry_statuses = new String[4];
        entry_statuses[0] = this.i18n_plugin.getMessage("UNKNOWN");
        entry_statuses[1] = this.i18n_plugin.getMessage("NEW");
        entry_statuses[2] = this.i18n_plugin.getMessage("CHANGED");
        entry_statuses[3] = this.i18n_plugin.getMessage("OLD");
        
        // months
        months[0] = i18n_plugin.getMessage("JANUARY");
        months[1] = i18n_plugin.getMessage("FEBRUARY");
        months[2] = i18n_plugin.getMessage("MARCH");
        months[3] = i18n_plugin.getMessage("APRIL");
        months[4] = i18n_plugin.getMessage("MAY");
        months[5] = i18n_plugin.getMessage("JUNE");
        months[6] = i18n_plugin.getMessage("JULY");
        months[7] = i18n_plugin.getMessage("AUGUST");
        months[8] = i18n_plugin.getMessage("SEPTEMBER");
        months[9] = i18n_plugin.getMessage("OCTOBER");
        months[10] = i18n_plugin.getMessage("NOVEMBER");
        months[11] = i18n_plugin.getMessage("DECEMBER");

        // days
        days[0] = i18n_plugin.getMessage("MONDAY");
        days[1] = i18n_plugin.getMessage("TUESDAY");
        days[2] = i18n_plugin.getMessage("WEDNESDAY");
        days[3] = i18n_plugin.getMessage("THURSDAY");
        days[4] = i18n_plugin.getMessage("FRIDAY");
        days[5] = i18n_plugin.getMessage("SATURDAY");
        days[6] = i18n_plugin.getMessage("SUNDAY");

        
       reading_statuses = new String[7];
        
        reading_statuses[0] = i18n_plugin.getMessage("STATUS_NONE");
        reading_statuses[1] = i18n_plugin.getMessage("STATUS_READY");
        reading_statuses[2] = i18n_plugin.getMessage("STATUS_DOWNLOADING");
        reading_statuses[3] = i18n_plugin.getMessage("STATUS_STOPPED_DEVICE");
        reading_statuses[4] = i18n_plugin.getMessage("STATUS_STOPPED_USER");
        reading_statuses[5] = i18n_plugin.getMessage("STATUS_DOWNLOAD_FINISHED");
        reading_statuses[6] = String.format(i18n_plugin.getMessage("STATUS_READER_ERROR"), i18n_plugin.getMessage("DEVICE_NAME_BIG"));

        
        this.filtering_states = new String[7];
        
        filtering_states[0] = i18n_plugin.getMessage("FILTER_ALL"); 
        filtering_states[1] = i18n_plugin.getMessage("FILTER_NEW");
        filtering_states[2] = i18n_plugin.getMessage("FILTER_CHANGED"); 
        filtering_states[3] = i18n_plugin.getMessage("FILTER_EXISTING");
        filtering_states[4] = i18n_plugin.getMessage("FILTER_UNKNOWN");
        filtering_states[5] = i18n_plugin.getMessage("FILTER_NEW_CHANGED");
        filtering_states[6] = i18n_plugin.getMessage("FILTER_ALL_BUT_EXISTING");
        
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
        return (Converter_mgdL_mmolL)this.getConverter("BG");
        
    }
 
    
    /**
     * Init all Objects
     */
    public abstract void initAllObjects();
    
 
    
    /**
     * Load Icons
     */
    public void loadIcons()
    {
        if (entry_status_iconimage!=null)
            return;
        
        entry_status_iconimage = new ImageIcon[4];
        
        JDialog d = new JDialog();
        
        for(int i=0; i< entry_status_icons.length; i++)
        {
            entry_status_iconimage[i] = getImageIcon(entry_status_icons[i], 8, 8, d); 
        }
    }
    
    

}