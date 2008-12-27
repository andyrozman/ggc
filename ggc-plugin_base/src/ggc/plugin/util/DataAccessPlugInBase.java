package ggc.plugin.util;

import ggc.plugin.cfg.DeviceConfiguration;
import ggc.plugin.cfg.DeviceConfigurationDefinition;
import ggc.plugin.data.DeviceDataHandler;
import ggc.plugin.data.DeviceValuesEntry;
import ggc.plugin.list.BaseListEntry;
import ggc.plugin.manager.DeviceManager;
import ggc.plugin.output.OutputUtil;

import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.GregorianCalendar;
import java.util.Hashtable;

import com.atech.db.hibernate.HibernateDb;
import com.atech.graphics.components.about.CreditsGroup;
import com.atech.graphics.components.about.FeaturesGroup;
import com.atech.graphics.components.about.LibraryInfoEntry;
import com.atech.i18n.I18nControlAbstract;
import com.atech.utils.ATDataAccessAbstract;

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


public abstract class DataAccessPlugInBase extends ATDataAccessAbstract
{
    
    /**
     * Plugin Version
     */
    public String plugin_version = "0.2.5";


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

    //public String[] availableLanguages = { "English", "Deutsch", "Slovenski", };

    //public String[] avLangPostfix = { "en", "de", "si", };

    //public String[] bg_units = { "mg/dl", "mmol/l" };

    //public Hashtable<String,String> timeZones;

    //public Vector<String> time_zones_vector;
    
    //JFrame m_main = null;        
        

    // about
    //protected String about_title;
    protected String about_image_name;
    //protected String about_plugin_name;
    protected int about_plugin_copyright_from;
    protected ArrayList<LibraryInfoEntry> plugin_libraries;
    protected ArrayList<CreditsGroup> plugin_developers;
    protected ArrayList<FeaturesGroup> plugin_features; 

    // web lister
    protected Hashtable<String,String> web_lister_cfg;
    protected String weblister_title;
    protected String weblister_desc;
    protected ArrayList<BaseListEntry> weblister_items;

    
    protected DeviceManager m_manager = null;

    protected DeviceConfigurationDefinition device_config_def;
    protected DeviceConfiguration device_config;
    
    
    protected DeviceValuesEntry m_entry_type;
    protected String columns_manual[] = null;
    protected int column_widths_manual[] = null;
    protected String columns_table[] = null;
    protected int column_widths_table[] = null;
    protected String[] entry_statuses = null;
    protected String[] reading_statuses = null;
    protected String[] filtering_states = null;
    
    
    protected DeviceDataHandler m_ddh;
    

    // ********************************************************
    // ******      Constructors and Access methods        *****    
    // ********************************************************

    //   Constructor:  DataAccessMeter
    /**
     *
     *  This is DataAccessMeter constructor; Since classes use Singleton Pattern,
     *  constructor is protected and can be accessed only with getInstance() 
     *  method.<br><br>
     *  @param i18n I18nControlAbstract instance
     *
     */
    public DataAccessPlugInBase(I18nControlAbstract i18n)
    {
    	super(i18n); //I18nControl.getInstance());

        initSpecial();
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
        
        if (input_type==output_type)
            return bg_value;
        else
        {
            if (output_type==DataAccessPlugInBase.BG_MGDL)
            {
                return bg_value * MGDL_TO_MMOL_FACTOR;
            }
            else
            {
                return bg_value * MMOL_TO_MGDL_FACTOR;
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
     * Get HibernateDb instance (for working with database in plugin)
     */
    public HibernateDb getHibernateDb()
    {
        return null;
    }


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
     * Create About Context for plugin
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
        reading_statuses = new String[7];
        
        reading_statuses[0] = m_i18n.getMessage("STATUS_NONE");
        reading_statuses[1] = m_i18n.getMessage("STATUS_READY");
        reading_statuses[2] = m_i18n.getMessage("STATUS_DOWNLOADING");
        reading_statuses[3] = m_i18n.getMessage("STATUS_STOPPED_DEVICE");
        reading_statuses[4] = m_i18n.getMessage("STATUS_STOPPED_USER");
        reading_statuses[5] = m_i18n.getMessage("STATUS_DOWNLOAD_FINISHED");
        reading_statuses[6] = String.format(m_i18n.getMessage("STATUS_READER_ERROR"), m_i18n.getMessage("DEVICE_NAME_BIG"));

        
        this.filtering_states = new String[7];
        
        filtering_states[0] = m_i18n.getMessage("FILTER_ALL"); 
        filtering_states[1] = m_i18n.getMessage("FILTER_NEW");
        filtering_states[2] = m_i18n.getMessage("FILTER_CHANGED"); 
        filtering_states[3] = m_i18n.getMessage("FILTER_EXISTING");
        filtering_states[4] = m_i18n.getMessage("FILTER_UNKNOWN");
        filtering_states[5] = m_i18n.getMessage("FILTER_NEW_CHANGED");
        filtering_states[6] = m_i18n.getMessage("FILTER_ALL_BUT_EXISTING");
        
        
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
    @Override
    public void loadGraphConfigProperties()
    {
    }
    

}