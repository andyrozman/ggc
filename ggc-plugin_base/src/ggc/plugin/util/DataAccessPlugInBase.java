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
 *  Filename: DataAccessMeter
 *  Purpose:  Used for utility works and static data handling (this is singelton
 *      class which holds all our definitions, so that we don't need to create them
 *      again for each class.      
 *
 *  Author:   andyrozman
 */

package ggc.plugin.util;

import ggc.plugin.cfg.DeviceConfiguration;
import ggc.plugin.cfg.DeviceConfigurationDefinition;
import ggc.plugin.data.DeviceDataHandler;
import ggc.plugin.data.DeviceValuesEntry;
import ggc.plugin.list.BaseListEntry;
import ggc.plugin.manager.DeviceManager;

import java.awt.Color;
import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.GregorianCalendar;
import java.util.Hashtable;
import java.util.Vector;

import javax.swing.JFrame;

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
 *  Filename:  ###---###  
 *  Description:
 * 
 *  Author: Andy {andy@atech-software.com}
 */


public abstract class DataAccessPlugInBase extends ATDataAccessAbstract
{
    
    //public String web_server_port = "444";

    public String plugin_version = "0.2.1";

//xa    private GGCProperties m_settings = null;

    //private DbToolApplicationGGC m_configFile = null;
//    private ConfigurationManager m_cfgMgr = null;

    

    public static DecimalFormat MmolDecimalFormat = new DecimalFormat("#0.0");
    public static DecimalFormat Decimal0Format = new DecimalFormat("#0");
    public static DecimalFormat Decimal1Format = new DecimalFormat("#0.0");
    public static DecimalFormat Decimal2Format = new DecimalFormat("#0.00");
    public static DecimalFormat Decimal3Format = new DecimalFormat("#0.000");

    /**
     * Which BG unit is used: BG_MGDL = mg/dl, BG_MMOL = mmol/l
     */
    public int m_BG_unit = BG_MMOL;

    public String[] availableLanguages = { "English", "Deutsch", "Slovenski", };

    public String[] avLangPostfix = { "en", "de", "si", };

    public String[] bg_units = { "mg/dl", "mmol/l" };

    public Hashtable<String,String> timeZones;

    public Vector<String> time_zones_vector;
    
    JFrame m_main = null;        
        

    // about
    //protected String about_title;
    protected String about_image_name;
    //protected String about_plugin_name;
    protected int about_plugin_copyright_from;
    protected ArrayList<LibraryInfoEntry> plugin_libraries;
    protected ArrayList<CreditsGroup> plugin_developers;
    protected ArrayList<FeaturesGroup> plugin_features; 
    
    
    

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

    // FIXME should be abstract
/*    public void initSpecial()
    {
        System.out.println("initSpecial - Base");

        this.loadTimeZones();
        
        loadConfigIcons();
        
        checkPrerequisites();
        this.createWebListerContext();
    }*/
    
    
    
    public void loadConfigIcons()
    {
/*        config_icons = new ImageIcon[5];
        config_icons[0] = new ImageIcon(getImage("/icons/cfg_general.png", m_main));
        config_icons[1] = new ImageIcon(getImage("/icons/cfg_medical.png", m_main));
        config_icons[2] = new ImageIcon(getImage("/icons/cfg_print.png", m_main));
        config_icons[3] = new ImageIcon(getImage("/icons/cfg_meter.png", m_main));
        config_icons[4] = new ImageIcon(getImage("/icons/cfg_general.png", m_main));
  */      
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
    
    
    /*
    public String getAboutTitle()
    {
        return this.about_title;
    }*/
    
    public String getAboutImageName()
    {
        return this.about_image_name;
    }

    public int[] getAboutImageSize()
    {
        int[] sz = new int[2];
        sz[0] = 500;
        sz[1] = 125;
        
        return sz;
    }
    
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
    
    /*
    public String getAboutPlugInName()
    {
        return this.about_plugin_name;
    }*/
    
    public ArrayList<LibraryInfoEntry> getPlugInLibraries()
    {
        return this.plugin_libraries;
    }
    
    public ArrayList<CreditsGroup> getPlugInDevelopers()
    {
        return this.plugin_developers;
    }
    
    public ArrayList<FeaturesGroup> getPlugInFeatures()
    {
        return this.plugin_features;
    }
    

    
    // ********************************************************
    // ******         Web Lister Methods                  *****    
    // ********************************************************

    
    
    public String weblister_title;
    public String weblister_desc;
    public ArrayList<BaseListEntry> weblister_items;
    
    /**
     * Create WebLister (for List) Context for plugin
     */
    public abstract void createWebListerContext();
    
    
    public String getWebListerTitle()
    {
        return this.weblister_title;
    }

    
    public String getWebListerDescription()
    {
        return this.weblister_desc;
    }
    

    public ArrayList<BaseListEntry> getWebListerItems()
    {
        return weblister_items;
    }
    
    
    public ArrayList<String> getWebListerItemsTitles()
    {
        return null;
    }
    
    public ArrayList<String> getWebListerItemsStatuses()
    {
        return null;
    }
    

    // ********************************************************
    // ******                Configuration                *****    
    // ********************************************************
    
    /**
     * Create Configuration Context for plugin
     */
    public abstract void createConfigurationContext();
    
    
    protected DeviceConfigurationDefinition device_config_def;
    
    public DeviceConfigurationDefinition getDeviceConfigurationDefinition()
    {
        return device_config_def;
    }
    
    
    protected DeviceConfiguration device_config;
    
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
    
    public String getPlugInVersion()
    {
        return this.plugin_version;
    }
    
    /**
     * Create Plugin Version
     */
    public abstract void createPlugInVersion();
    
    
    // ********************************************************
    // ******                  Settings                   *****    
    // ********************************************************

    
    public Color getColor(int color)
    {
        return new Color(color);
    }

    // ********************************************************
    // ******             BG Measurement Type             *****    
    // ********************************************************

    public static final int BG_MGDL = 1;

    public static final int BG_MMOL = 2;

    public int getBGMeasurmentType()
    {
        return this.m_BG_unit;
    }

    public void setBGMeasurmentType(int type)
    {
        this.m_BG_unit = type;
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
    
    

    public float getBGValueByType(int input_type, int output_type, float bg_value)
    {
        
        if (input_type==output_type)
            return bg_value;
        else
        {
            if (output_type==DataAccessPlugInBase.BG_MGDL)
            {
                return bg_value * DataAccessPlugInBase.MGDL_TO_MMOL_FACTOR;
            }
            else
            {
                return bg_value * DataAccessPlugInBase.MMOL_TO_MGDL_FACTOR;
            }
        }

    }


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

        
        
        
        if (input_type==output_type)
            return bg_value;
        else
        {
            if (output_type==DataAccessPlugInBase.BG_MGDL)
            {
                return bg_value * DataAccessPlugInBase.MGDL_TO_MMOL_FACTOR;
            }
            else
            {
                return bg_value * DataAccessPlugInBase.MMOL_TO_MGDL_FACTOR;
            }
        }

    }
    
    
    
    
    public float getBGValueDifferent(int type, float bg_value)
    {

            if (type==DataAccessPlugInBase.BG_MGDL)
            {
                return bg_value * DataAccessPlugInBase.MGDL_TO_MMOL_FACTOR;
            }
            else
            {
                return bg_value * DataAccessPlugInBase.MMOL_TO_MGDL_FACTOR;
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

    protected DeviceManager m_manager = null;
    
    
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
    
    
    protected DeviceValuesEntry m_entry_type;
    protected String columns_manual[] = null;
    protected int column_widths_manual[] = null;

    protected String columns_table[] = null;
    protected int column_widths_table[] = null;
    
    protected String[] entry_statuses = null;
    protected String[] reading_statuses = null;
    protected String[] filtering_states = null;
    
    
    /**
     * Create About Context for plugin
     */
    public abstract void createPlugInDataRetrievalContext();
    

    public String[] getColumnsManual()
    {
        return this.columns_manual;
    }

    public int[] getColumnsWidthManual()
    {
        return this.column_widths_manual;
    }

    
    public String[] getColumnsTable()
    {
        return this.columns_table;
    }

    public int[] getColumnsWidthTable()
    {
        return this.column_widths_table;
    }
    
    
    public String[] getEntryStatuses()
    {
        return this.entry_statuses;
    }
    
    public DeviceValuesEntry getDataEntryObject()
    {
        return m_entry_type;
    }
    
    
    protected DeviceDataHandler m_ddh;

    /**
     * Load Device Data Handler
     */
    public abstract void loadDeviceDataHandler();
    
    public DeviceDataHandler getDeviceDataHandler()
    {
        return this.m_ddh;
    }
    

    public String[] getReadingStatuses()
    {
        return reading_statuses;
    }
    
    public String[] getFilteringStates()
    {
        return this.filtering_states;
    }
    
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
    

    public int getStartYear()
    {
        return 1980;
    }







    public static void notImplemented(String source)
    {
        System.out.println("Not Implemented: " + source);
    }




}
