package ggc.nutri.util;

import java.util.ArrayList;

import javax.swing.*;

import com.atech.graphics.components.about.*;
import com.atech.i18n.I18nControlAbstract;
import com.atech.i18n.mgr.LanguageManager;

import ggc.core.db.GGCDb;
import ggc.core.plugins.GGCPluginType;
import ggc.nutri.db.GGCDbCache;
import ggc.nutri.db.GGCDbNutri;
import ggc.plugin.cfg.DeviceConfiguration;
import ggc.plugin.list.BaseListEntry;
import ggc.plugin.util.DataAccessPlugInBase;

// TODO: Auto-generated Javadoc
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
 *  Filename:     DataAccessNutri
 *  Description:  Static data class used by Nutrition Plugin.
 * 
 *  Author: Andy {andy@atech-software.com}
 */

// FIXME needs little refactoring and deleting of stuff
public class DataAccessNutri extends DataAccessPlugInBase
{

    /**
     * PlugIn Version
     */
    public static final String PLUGIN_VERSION = "1.3.5";

    private static DataAccessNutri s_da = null; // This is handle to unique

    /**
     * The m_main.
     */
    JFrame m_main = null;


    // ********************************************************
    // ****** Constructors and Access methods *****
    // ********************************************************

    // Constructor: DataAccessMeter
    /**
     *
     *  This is DataAccessMeter constructor; Since classes use Singleton Pattern,
     *  constructor is protected and can be accessed only with getInstance() 
     *  method.<br><br>
     *
     */
    private DataAccessNutri(JFrame frame, LanguageManager lm)
    {
        super(lm, new GGCNutriICRunner());
        this.m_main = frame;
        initSpecial();
    }


    /**
     * Set Values From Data Access
     */
    public void setValuesFromDataAccess()
    {

    }


    /** 
     * Init Special - All methods that we support should be called here
     */
    @Override
    public void initSpecial()
    {
        // System.out.println("init special");
        //checkPrerequisites();
        //createWebListerContext();
        //createPlugInAboutContext();
        //createConfigurationContext();
        createPlugInVersion();
        //loadDeviceDataHandler();
        //loadManager();
        //loadReadingStatuses();
        //createPlugInDataRetrievalContext();
        //loadWebLister();
    }


    // Method: getInstance
    // Author: Andy
    /**
     *
     *  This method returns reference to OmniI18nControl object created, or if no 
     *  object was created yet, it creates one.<br><br>
     *
     *  @return Reference to OmniI18nControl object
     * 
     */
    public static DataAccessNutri getInstance()
    {
        // if (s_da == null)
        // s_da = new DataAccessNutri(null);
        return s_da;
    }


    /**
     * Create Instance 
     * 
     * @param lm
     * @return
     */
    public static DataAccessNutri createInstance(LanguageManager lm)
    {
        if (s_da == null)
        {
            s_da = new DataAccessNutri(null, lm);
        }
        return s_da;
    }


    /*
     * public void loadConfigIcons()
     * {
     * config_icons = new ImageIcon[5];
     * config_icons[0] = new ImageIcon(getImage("/icons/cfg_general.png",
     * m_main));
     * config_icons[1] = new ImageIcon(getImage("/icons/cfg_medical.png",
     * m_main));
     * config_icons[2] = new ImageIcon(getImage("/icons/cfg_print.png",
     * m_main));
     * config_icons[3] = new ImageIcon(getImage("/icons/cfg_meter.png",
     * m_main));
     * config_icons[4] = new ImageIcon(getImage("/icons/cfg_general.png",
     * m_main));
     * }
     */

    /**
     *  This method sets handle to DataAccessMeter to null and deletes the instance. <br><br>
     */
    public void deleteInstance()
    {
        super.m_i18n = null;
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
//        I18nControlAbstract ic = getI18nControlInstance();
//
//        // about_title = i18nControlAbstract.getMessage("METER_PLUGIN_ABOUT");
//        about_image_name = "/icons/about_meter.jpg";
//        // about_image_name = "/icons/about_logo.gif";
//        about_plugin_copyright_from = 2010;
//        // about_plugin_name = i18nControlAbstract.getMessage("METER_PLUGIN");
//
//        ArrayList<LibraryInfoEntry> lst_libs = new ArrayList<LibraryInfoEntry>();
//        lst_libs.add(new LibraryInfoEntry("Atech-Tools", "0.2.x", "www.atech-software.com", "LGPL",
//                "Helper Library for Swing/Hibernate/...",
//                "Copyright (c) 2006-2008 Atech Software Ltd. All rights reserved."));
//        lst_libs.add(new LibraryInfoEntry("Apache Commons Lang", "2.4", "commons.apache.org/lang/", "Apache",
//                "Helper methods for java.lang library"));
//        lst_libs.add(new LibraryInfoEntry("Apache Commons Logging", "1.0.4", "commons.apache.org/logging/", "Apache",
//                "Logger and all around wrapper for logging utilities"));
//        lst_libs.add(new LibraryInfoEntry("dom4j", "1.6.1", "http://www.dom4j.org/", "BSD",
//                "Framework for Xml manipulation"));
//        lst_libs.add(new LibraryInfoEntry("RXTXcomm", "2.1.7", "www.rxtx.org", "LGPL", "Comm API"));
//        lst_libs.add(new LibraryInfoEntry("XML Pull Parser", "3.1.1.4c",
//                "http://www.extreme.indiana.edu/xgws/xsoap/xpp/", "Indiana University Extreme! Lab Software License",
//                "Xml parser for processing xml document",
//                "Copyright (c) 2002 Extreme! Lab, Indiana University. All rights reserved."));
//        plugin_libraries = lst_libs;
//
//        ArrayList<CreditsGroup> lst_credits = new ArrayList<CreditsGroup>();
//        CreditsGroup cg = new CreditsGroup(ic.getMessage("DEVELOPERS_DESC"));
//        cg.addCreditsEntry(new CreditsEntry("Aleksander Rozman (Andy)", "andy@atech-software.com",
//                "Full framework and support for Ascensia, Roche, LifeScan devices"));
//        cg.addCreditsEntry(new CreditsEntry("Alexander Balaban", "abalaban1@yahoo.ca", "Support for OT UltraSmart"));
//        lst_credits.add(cg);
//        cg = new CreditsGroup(ic.getMessage("HELPERS_DESC"));
//        cg.addCreditsEntry(new CreditsEntry("Rafael Ziherl (RAF)", "", "Supplied hardware for Roche development"));
//        lst_credits.add(cg);
//
//        plugin_developers = lst_credits;
//
//        // features
//        ArrayList<FeaturesGroup> lst_features = new ArrayList<FeaturesGroup>();
//
//        FeaturesGroup fg = new FeaturesGroup(ic.getMessage("IMPLEMENTED_FEATURES"));
//        fg.addFeaturesEntry(new FeaturesEntry("Base Meter Tools Framework"));
//        fg.addFeaturesEntry(new FeaturesEntry("Various output types"));
//        fg.addFeaturesEntry(new FeaturesEntry("Communication Framework"));
//        fg.addFeaturesEntry(new FeaturesEntry("Graphical Interface (GGC integration)"));
//        fg.addFeaturesEntry(new FeaturesEntry("About dialog"));
//        fg.addFeaturesEntry(new FeaturesEntry("List of meters"));
//        fg.addFeaturesEntry(new FeaturesEntry("Configuration"));
//
//        lst_features.add(fg);
//
//        fg = new FeaturesGroup(ic.getMessage("SUPPORTED_DEVICES"));
//        fg.addFeaturesEntry(new FeaturesEntry("Ascensia/Bayer"));
//        fg.addFeaturesEntry(new FeaturesEntry("Accu-chek/Roche"));
//        fg.addFeaturesEntry(new FeaturesEntry("LifeScan: Ultra, Ultra2, Profile, Easy, UltraSmart"));
//
//        lst_features.add(fg);
//
//        // fg = new
//        // FeaturesGroup(i18nControlAbstract.getMessage("NOT_IMPLEMENTED_FEATURES"));
//        // fg.addFeaturesEntry(new FeaturesEntry("Configuration"));
//
//        // lst_features.add(fg);
//
//        fg = new FeaturesGroup(ic.getMessage("PLANNED_DEVICES"));
//        // fg.addFeaturesEntry(new FeaturesEntry("LifeScan (end of 2008)"));
//        fg.addFeaturesEntry(new FeaturesEntry("Abbott (in 2009)"));
//        fg.addFeaturesEntry(new FeaturesEntry("???"));
//
//        lst_features.add(fg);
//
//        this.plugin_features = lst_features;

    }


    /** 
     * Get About Image Size - Define about image size
     */
    @Override
    public int[] getAboutImageSize()
    {
        int[] sz = new int[2];
        sz[0] = 200;
        sz[1] = 125;

        return sz;
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
    }


    // ********************************************************
    // ****** Abstract Methods *****
    // ********************************************************

    @Override
    public void registerDeviceHandlers()
    {
    }


    @Override
    public GGCPluginType getPluginType()
    {
        return GGCPluginType.NutritionToolPlugin;
    }


    /**
     * Get Application Name
     */
    @Override
    public String getApplicationName()
    {
        return "GGC_NutriTool";
    }


    /** 
     * Check Prerequisites for Plugin
     */
    @Override
    public void checkPrerequisites()
    {
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
        this.plugin_version = DataAccessNutri.PLUGIN_VERSION;
    }


    // ********************************************************
    // ****** Manager *****
    // ********************************************************

    // ********************************************************
    // ****** Configuration *****
    // ********************************************************

    /**
     * Create Configuration Context for plugin
     */
    @Override
    public void createConfigurationContext()
    {
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
    }


    /**
     * Load Device Manager
     * 
     * @see ggc.plugin.util.DataAccessPlugInBase#loadManager()
     */
    @Override
    public void loadManager()
    {
    }


    /**
     * Load Device Data Handler
     * 
     * @see ggc.plugin.util.DataAccessPlugInBase#loadDeviceDataHandler()
     */
    @Override
    public void loadDeviceDataHandler()
    {
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
     * Gets the db.
     * 
     * @return the db
     */
    public GGCDb getDb()
    {
        // return DataAccessNutri.getInstance().getDb();
        return null;
    }

    /**
     * The db_nutri.
     */
    GGCDbNutri db_nutri = null;


    /**
     * Gets the nutri db.
     * 
     * @return the nutri db
     */
    public GGCDbNutri getNutriDb()
    {
        return db_nutri;
    }


    /**
     * Sets the nutri db
     * 
     * @param db the new nutri db
     */
    public void setNutriDb(GGCDbNutri db)
    {
        db_nutri = db;
    }


    /**
     * Gets the db cache
     * 
     * @return the db cache
     */
    public GGCDbCache getDbCache()
    {
        return db_nutri.getDbCache();
    }


    /** 
     * Load PlugIns
     */
    @Override
    public void loadPlugIns()
    {
    }


    /**
     * Create Custom Db
     * 
     * This is for plug-in specific database implementation
     */
    @Override
    public void createCustomDb()
    {
    }


    /**
     * Create Old Data Reader
     */
    @Override
    public void createOldDataReader()
    {
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
        return "GGC Nutrition Plugin";
    }

}
