package ggc.meter.util;

import ggc.meter.data.MeterDataHandler;
import ggc.meter.data.MeterDataReader;
import ggc.meter.data.cfg.MeterConfigurationDefinition;
import ggc.meter.data.db.GGCMeterDb;
import ggc.meter.device.MeterInterface;
import ggc.meter.manager.MeterManager;
import ggc.plugin.cfg.DeviceConfiguration;
import ggc.plugin.list.BaseListEntry;
import ggc.plugin.util.DataAccessPlugInBase;

import java.util.ArrayList;

import javax.swing.JFrame;

import com.atech.graphics.components.about.CreditsEntry;
import com.atech.graphics.components.about.CreditsGroup;
import com.atech.graphics.components.about.FeaturesEntry;
import com.atech.graphics.components.about.FeaturesGroup;
import com.atech.graphics.components.about.LibraryInfoEntry;
import com.atech.i18n.I18nControlAbstract;
import com.atech.i18n.mgr.LanguageManager;

/**
 *  Application:   GGC - GNU Gluco Control
 *  Plug-in:       Meter Tool (support for Meter devices)
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
 *  Filename:     DataAccessMeter  
 *  Description:  Static data class used by Meter Plugin.
 * 
 *  Author: Andy {andy@atech-software.com}
 */


public class DataAccessMeter extends DataAccessPlugInBase
{

    /**
     * PlugIn Version
     */
    public static final String PLUGIN_VERSION = "1.1.7";
    
    private static DataAccessMeter s_da = null; // This is handle to unique 

    private MeterManager m_meterManager = null;



    JFrame m_main = null;        
        
        
        
        

    // ********************************************************
    // ******      Constructors and Access methods        *****    
    // ********************************************************

    //   Constructor:  DataAccessMeter
    /**
     *
     *  This is DataAccessMeter constructor; Since classes use Singleton Pattern,
     *  constructor is protected and can be accessed only with getInstance() 
     *  method.<br><br>
     *
     */
    private DataAccessMeter(JFrame frame, LanguageManager lm)
    {
    	super(lm, new GGCMeterICRunner());
        this.m_main = frame;
        initSpecial();
    } 

    
    /** 
     * Init Special - All methods that we support should be called here
     */
    public void initSpecial()
    {
        //System.out.println("init special");
        checkPrerequisites();
        createWebListerContext();
        createPlugInAboutContext();
        createConfigurationContext();
        createPlugInVersion();
        loadDeviceDataHandler();
        //loadManager();
        loadReadingStatuses();
        createPlugInDataRetrievalContext();
        loadWebLister();
        createOldDataReader();
        this.loadConverters();
    }
    
    
    
    //  Method:       getInstance
    //  Author:       Andy
    /**
     *
     *  This method returns reference to OmniI18nControl object created, or if no 
     *  object was created yet, it creates one.<br><br>
     *
     *  @return Reference to OmniI18nControl object
     * 
     */
    public static DataAccessMeter getInstance()
    {
        //if (s_da == null)
        //    s_da = new DataAccessMeter(null);
        return s_da;
    }


    /**
     * Create Instance
     * 
     * @param lm
     * @return
     */
    public static DataAccessMeter createInstance(LanguageManager lm)
    {
        if (s_da == null)
            s_da = new DataAccessMeter(null, lm);
        return s_da;
    }
 
    
/*    
    public void loadConfigIcons()
    {
        config_icons = new ImageIcon[5];
        config_icons[0] = new ImageIcon(getImage("/icons/cfg_general.png", m_main));
        config_icons[1] = new ImageIcon(getImage("/icons/cfg_medical.png", m_main));
        config_icons[2] = new ImageIcon(getImage("/icons/cfg_print.png", m_main));
        config_icons[3] = new ImageIcon(getImage("/icons/cfg_meter.png", m_main));
        config_icons[4] = new ImageIcon(getImage("/icons/cfg_general.png", m_main));
        
    }
*/
  



    /**
     *  This method sets handle to DataAccessMeter to null and deletes the instance. <br><br>
     */
    public void deleteInstance()
    {
        super.m_i18n = null;
    }

 

    // ********************************************************
    // ******            About Methods                    *****    
    // ********************************************************
    
    /**
     * Create About Context for plugin
     */
    public void createPlugInAboutContext()
    {
        I18nControlAbstract ic = getI18nControlInstance();
        
        //about_title = ic.getMessage("METER_PLUGIN_ABOUT");
        about_image_name = "/icons/about_meter.jpg";
//        about_image_name = "/icons/about_logo.gif";
        about_plugin_copyright_from = 2006;
        //about_plugin_name = ic.getMessage("METER_PLUGIN");
        
        ArrayList<LibraryInfoEntry> lst_libs = new ArrayList<LibraryInfoEntry>();
        lst_libs.add(new LibraryInfoEntry("Atech-Tools", "0.2.x", "www.atech-software.com", "LGPL", "Helper Library for Swing/Hibernate/...", "Copyright (c) 2006-2008 Atech Software Ltd. All rights reserved."));
        lst_libs.add(new LibraryInfoEntry("Apache Commons Lang", "2.4", "commons.apache.org/lang/", "Apache", "Helper methods for java.lang library"));
        lst_libs.add(new LibraryInfoEntry("Apache Commons Logging", "1.0.4", "commons.apache.org/logging/", "Apache", "Logger and all around wrapper for logging utilities"));
        lst_libs.add(new LibraryInfoEntry("dom4j", "1.6.1", "http://www.dom4j.org/", "BSD", "Framework for Xml manipulation"));
        lst_libs.add(new LibraryInfoEntry("RXTXcomm", "2.1.7", "www.rxtx.org", "LGPL", "Comm API"));
        lst_libs.add(new LibraryInfoEntry("XML Pull Parser", "3.1.1.4c", "http://www.extreme.indiana.edu/xgws/xsoap/xpp/", "Indiana University Extreme! Lab Software License", "Xml parser for processing xml document", "Copyright (c) 2002 Extreme! Lab, Indiana University. All rights reserved."));
        plugin_libraries = lst_libs;

        ArrayList<CreditsGroup> lst_credits = new ArrayList<CreditsGroup>();
        CreditsGroup cg = new CreditsGroup(ic.getMessage("DEVELOPERS_DESC"));
        cg.addCreditsEntry(new CreditsEntry("Aleksander Rozman (Andy)", "andy@atech-software.com", "Full framework and support for Ascensia, Roche, LifeScan devices"));
        cg.addCreditsEntry(new CreditsEntry("Alexander Balaban", "abalaban1@yahoo.ca", "Support for OT UltraSmart"));
        lst_credits.add(cg);
        cg = new CreditsGroup(ic.getMessage("HELPERS_DESC"));
        cg.addCreditsEntry(new CreditsEntry("Rafael Ziherl (RAF)", "", "Supplied hardware for Roche development"));
        lst_credits.add(cg);
        
        plugin_developers = lst_credits;
        
        
        // features
        ArrayList<FeaturesGroup> lst_features = new ArrayList<FeaturesGroup>();

        
        FeaturesGroup fg = new FeaturesGroup(ic.getMessage("IMPLEMENTED_FEATURES"));
        fg.addFeaturesEntry(new FeaturesEntry("Base Meter Tools Framework"));
        fg.addFeaturesEntry(new FeaturesEntry("Various output types"));
        fg.addFeaturesEntry(new FeaturesEntry("Communication Framework"));
        fg.addFeaturesEntry(new FeaturesEntry("Graphical Interface (GGC integration)"));
        fg.addFeaturesEntry(new FeaturesEntry("About dialog"));
        fg.addFeaturesEntry(new FeaturesEntry("List of meters"));
        fg.addFeaturesEntry(new FeaturesEntry("Configuration"));
        
        lst_features.add(fg);
        
        
        fg = new FeaturesGroup(ic.getMessage("SUPPORTED_DEVICES"));
        fg.addFeaturesEntry(new FeaturesEntry("Ascensia/Bayer"));
        fg.addFeaturesEntry(new FeaturesEntry("Accu-chek/Roche"));
        fg.addFeaturesEntry(new FeaturesEntry("LifeScan: Ultra, Ultra2, Profile, Easy, UltraSmart"));
        
        lst_features.add(fg);
        
        
        //fg = new FeaturesGroup(ic.getMessage("NOT_IMPLEMENTED_FEATURES"));
        //fg.addFeaturesEntry(new FeaturesEntry("Configuration"));
        
        //lst_features.add(fg);

        
        fg = new FeaturesGroup(ic.getMessage("PLANNED_DEVICES"));
        //fg.addFeaturesEntry(new FeaturesEntry("LifeScan (end of 2008)"));
        fg.addFeaturesEntry(new FeaturesEntry("Abbott (in 2009)"));
        fg.addFeaturesEntry(new FeaturesEntry("???"));
        
        lst_features.add(fg);
        
        
        this.plugin_features = lst_features;
        
        
    }

    
    /** 
     * Get About Image Size - Define about image size
     */
    public int[] getAboutImageSize()
    {
        int[] sz = new int[2];
        sz[0] = 200;
        sz[1] = 125;
        
        return sz;
    }
    
    
    // ********************************************************
    // ******         Web Lister Methods                  *****    
    // ********************************************************
    
    /**
     * Create WebLister (for List) Context for plugin
     */
    public void createWebListerContext()
    {
        
        this.loadWebLister();
        
        I18nControlAbstract ic = getI18nControlInstance();
        
        weblister_items = new ArrayList<BaseListEntry>();
        weblister_items.add(new BaseListEntry("Abbott Diabetes Care", "/meters/abbott.html", 4));
        weblister_items.add(new BaseListEntry("Arkray USA (formerly Hypoguard)", "/meters/arkray.html", 5));
        weblister_items.add(new BaseListEntry("Bayer Diagnostics", "/meters/bayer.html", 1));
        weblister_items.add(new BaseListEntry("Diabetic Supply of Suncoast", "/meters/dsos.html", 5));
        weblister_items.add(new BaseListEntry("Diagnostic Devices", "/meters/prodigy.html", 5));
        weblister_items.add(new BaseListEntry("HealthPia America", "/meters/healthpia.html", 5));
        weblister_items.add(new BaseListEntry("Home Diagnostics", "/meters/home_diagnostics.html", 5));
        weblister_items.add(new BaseListEntry("Lifescan", "/meters/lifescan.html", 4));
        weblister_items.add(new BaseListEntry("Nova Biomedical", "/meters/nova_biomedical.html", 5));
        weblister_items.add(new BaseListEntry("Roche Diagnostics", "/meters/roche.html", 2));
        weblister_items.add(new BaseListEntry("Sanvita", "/meters/sanvita.html", 5));
        weblister_items.add(new BaseListEntry("U.S. Diagnostics", "/meters/us_diagnostics.html", 5));
        weblister_items.add(new BaseListEntry("WaveSense", "/meters/wavesense.html", 5));
        
        //weblister_title = ic.getMessage("METERS_LIST_WEB");
        weblister_desc = ic.getMessage("METERS_LIST_WEB_DESC");
    }
        
    
    
    // ********************************************************
    // ******         Abstract Methods                    *****    
    // ********************************************************
    

    /** 
     * Get Application Name
     */
    public String getApplicationName()
    {
        return "GGC_MeterTool";
    }
    
    
    /** 
     * Check Prerequisites for Plugin
     */
    public void checkPrerequisites()
    {
    }
    
    
    
    // ********************************************************
    // ******                  Version                    *****    
    // ********************************************************
    
    /**
     * Create Plugin Version
     */
    public void createPlugInVersion()
    {
        this.plugin_version = DataAccessMeter.PLUGIN_VERSION;
    }
    
    

    // ********************************************************
    // ******                   Manager                   *****    
    // ********************************************************

    /**
     * Get Device Manager
     * 
     * @return
     */
    public MeterManager getMeterManager()
    {
        return this.m_meterManager;
    }


    
    // ********************************************************
    // ******                   Db                        *****    
    // ********************************************************
    
    /**
     * The m_db.
     */
    GGCMeterDb m_db;
    

    /**
     * Create Custom Db
     * 
     * This is for plug-in specific database implementation
     */
    public void createCustomDb()
    {
        this.m_db = new GGCMeterDb(this.hdb);
    }
    

    /**
     * Get Db
     * 
     * @return
     */
    public GGCMeterDb getDb()
    {
        return this.m_db;
    }
    
    
    
    // ********************************************************
    // ******              Configuration                  *****    
    // ********************************************************
    
    
    /**
     * Create Configuration Context for plugin
     */
    @Override
    public void createConfigurationContext()
    {
        this.device_config_def = new MeterConfigurationDefinition();
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
     * Create Data Retrieval Context for Plug-in
     * 
     * @see ggc.plugin.util.DataAccessPlugInBase#createPlugInDataRetrievalContext()
     */
    @Override
    public void createPlugInDataRetrievalContext()
    {
        
        loadBasePluginTranslations();
        
        this.columns_table = new String[5];
        this.columns_table[0] = this.i18n_plugin.getMessage("DATETIME"); 
        this.columns_table[1] = this.i18n_plugin.getMessage("BG_MMOLL");
        this.columns_table[2] = this.i18n_plugin.getMessage("BG_MGDL");
        this.columns_table[3] = this.i18n_plugin.getMessage("STATUS");
        this.columns_table[4] = ""; //this.i18n_plugin.getMessage("");
        
        this.column_widths_table = new int[5];
        this.column_widths_table[0] = 100;
        this.column_widths_table[1] = 50;
        this.column_widths_table[2] = 50;
        this.column_widths_table[3] = 50;
        this.column_widths_table[4] = 50;
        
    }


    /**
     * Load Device Manager
     * 
     * @see ggc.plugin.util.DataAccessPlugInBase#loadManager()
     */
    @Override
    public void loadManager()
    {
        this.m_manager = MeterManager.getInstance();
    }


    /**
     * Load Device Data Handler
     * 
     * @see ggc.plugin.util.DataAccessPlugInBase#loadDeviceDataHandler()
     */
    @Override
    public void loadDeviceDataHandler()
    {
        this.m_ddh = new MeterDataHandler(this);
    }


    /**
     * Get Images for Devices
     * 
     * @see ggc.plugin.util.DataAccessPlugInBase#getDeviceImagesRoot()
     */
    @Override
    public String getDeviceImagesRoot()
    {
        return "/icons/meters/";
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
     * Create Old Data Reader
     */
    public void createOldDataReader()
    {
        this.m_old_data_reader = new MeterDataReader(this);
    }
    

    /**
     * Is Data Download Screen Wide
     * 
     * @return
     */
    public boolean isDataDownloadSceenWide()
    {
        MeterInterface mi = (MeterInterface)this.getSelectedDeviceInstance();
        
        if (mi.getInterfaceTypeForMeter()==MeterInterface.METER_INTERFACE_SIMPLE)
            return false;
        else
            return true;
    }


    @Override
    public void initAllObjects()
    {
        // TODO Auto-generated method stub
        
    }    
    
    
}
