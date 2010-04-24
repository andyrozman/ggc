package ggc.cgms.util;

import ggc.cgms.data.CGMSDataHandler;
import ggc.cgms.data.CGMSDataReader;
import ggc.cgms.data.CGMSValuesEntry;
import ggc.cgms.data.cfg.CGMSConfigurationDefinition;
import ggc.cgms.data.db.GGC_CGMSDb;
import ggc.cgms.manager.CGMSManager;
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
    public static final String PLUGIN_VERSION = "1.0.1";


    private static DataAccessCGMS s_da = null; // This is handle to unique 

    private CGMSManager m_cgms_manager = null;

    /**
     * Value Types
     */
    public static String[] value_types = null;

    
    /**
     * Value Type
     */
    public static String[] value_type = null;

    

    // ********************************************************
    // ******      Constructors and Access methods        *****    
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
    public void initSpecial()
    {
        checkPrerequisites();
        
        this.createWebListerContext();
        this.createPlugInAboutContext();
        this.createConfigurationContext();
        this.createPlugInVersion();
        loadDeviceDataHandler();
        //loadManager();
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
        //if (s_da == null)
        //    s_da = new DataAccessCGM();
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
            s_da = new DataAccessCGMS(lm);
        return s_da;
    }
     

    //  Method:       deleteInstance
    /**
     *  This method sets handle to DataAccess to null and deletes the instance. <br><br>
     */
    public void deleteInstance()
    {
        m_i18n = null;
    }

 
    
    


    
    
    
    
    // ********************************************************
    // ******         Abstract Methods                    *****    
    // ********************************************************
    

    /** 
     * Get Application Name
     */
    public String getApplicationName()
    {
    	return "GGC_CGMSTool";
    }
    
    
    /** 
     * Check Prerequisites for Plugin
     */
    public void checkPrerequisites()
    {
    }
    
    
    
    
    // ********************************************************
    // ******                   Manager                   *****    
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
    // ******          Parent handling (for UIs)          *****    
    // ********************************************************

    /**
     *  Utils
     */

/*
    public Image getImage(String filename, Component cmp)
    {
        Image img;

        InputStream is = this.getClass().getResourceAsStream(filename);

        if (is==null)
            System.out.println("Error reading image: "+filename);

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        try
        {
            int c;
            while ((c = is.read()) >=0)
            baos.write(c);


            //JDialog.getT
            //JFrame.getToolkit();
            
            if (cmp==null)
            	cmp = new JLabel();
            
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
    // ******          Dates and Times Handling           *****    
    // ********************************************************

    public String getCurrentDateString()
    {
        GregorianCalendar gc = new GregorianCalendar();
        return gc.get(Calendar.DAY_OF_MONTH) + "."
                + (gc.get(Calendar.MONTH) + 1) + "." + gc.get(Calendar.YEAR);
    }


    // ********************************************************
    // ******                   Database                  *****    
    // ********************************************************


    GGC_CGMSDb m_db;
    
    
    
    /**
     * Create Custom Db
     * 
     * This is for plug-in specific database implementation
     */
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
    // ******                Configuration                *****    
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
    // ******            About Methods                    *****    
    // ********************************************************
    
    /**
     * Create About Context for plugin
     */
    @Override
    public void createPlugInAboutContext()
    {
        I18nControlAbstract ic = this.getI18nControlInstance();
        //this.about_title = ic.getMessage("PUMP_PLUGIN_ABOUT");
        this.about_image_name = "/icons/cgms_about.jpg";

        this.about_plugin_copyright_from = 2009;
        //this.about_plugin_name = ic.getMessage("PUMP_PLUGIN");
        
        
        // libraries
        ArrayList<LibraryInfoEntry> lst_libs = new ArrayList<LibraryInfoEntry>();
        
        lst_libs.add(new LibraryInfoEntry("Atech-Tools", "0.3.x", "www.atech-software.com", "LGPL", "Helper Library for Swing/Hibernate/...", "Copyright (c) 2006-2008 Atech Software Ltd. All rights reserved."));
        lst_libs.add(new LibraryInfoEntry("Apache Commons Lang", "2.4", "commons.apache.org/lang/", "Apache", "Helper methods for java.lang library"));
        lst_libs.add(new LibraryInfoEntry("Apache Commons Logging", "1.0.4", "commons.apache.org/logging/", "Apache", "Logger and all around wrapper for logging utilities"));
        lst_libs.add(new LibraryInfoEntry("dom4j", "1.6.1", "http://www.dom4j.org/", "BSD", "Framework for Xml manipulation"));
        //lst_libs.add(new LibraryInfoEntry("RXTXcomm", "2.1.7", "www.rxtx.org", "LGPL", "Comm API"));
        //lst_libs.add(new LibraryInfoEntry("XML Pull Parser", "3.1.1.4c", "http://www.extreme.indiana.edu/xgws/xsoap/xpp/", "Indiana University Extreme! Lab Software License", "Xml parser for processing xml document", "Copyright (c) 2002 Extreme! Lab, Indiana University. All rights reserved."));

        this.plugin_libraries = lst_libs;

        // developers and other credits
        ArrayList<CreditsGroup> lst_credits = new ArrayList<CreditsGroup>();

        CreditsGroup cg = new CreditsGroup(ic.getMessage("DEVELOPERS_DESC"));
        cg.addCreditsEntry(new CreditsEntry("Aleksander Rozman (Andy)", "andy@atech-software.com", "Framework, About, Outputs")); // and support for Ascensia & Roche devices"));
        lst_credits.add(cg);

        //cg = new CreditsGroup(ic.getMessage("HELPERS_DESC"));
        //cg.addCreditsEntry(new CreditsEntry("Rafael Ziherl (RAF)", "", "Supplied hardware for Roche development"));
        //lst_credits.add(cg);
                
        this.plugin_developers = lst_credits;

        
        // features
        ArrayList<FeaturesGroup> lst_features = new ArrayList<FeaturesGroup>();

        
        FeaturesGroup fg = new FeaturesGroup(ic.getMessage("IMPLEMENTED_FEATURES"));
        fg.addFeaturesEntry(new FeaturesEntry("Base CGMS Tools Framework"));
        fg.addFeaturesEntry(new FeaturesEntry("Various output types"));
        fg.addFeaturesEntry(new FeaturesEntry("Communication Framework"));
        fg.addFeaturesEntry(new FeaturesEntry("Reading data"));
        fg.addFeaturesEntry(new FeaturesEntry("Configuration"));
        fg.addFeaturesEntry(new FeaturesEntry("List of CGMS"));
        fg.addFeaturesEntry(new FeaturesEntry("About dialog"));
        
        lst_features.add(fg);
        
        
        fg = new FeaturesGroup(ic.getMessage("SUPPORTED_DEVICES"));
        fg.addFeaturesEntry(new FeaturesEntry("Dexcom (file import only)"));
        
        //fg.addFeaturesEntry(new FeaturesEntry("Roche (partitialy, Basal Pattern History is not fully supported due to incomplete export of SmartPix device)"));
        //fg.addFeaturesEntry(new FeaturesEntry("Dana (only works on Windows and Linux)"));
//        fg.addFeaturesEntry(new FeaturesEntry("Accu-chek/Roche"));
//        fg.addFeaturesEntry(new FeaturesEntry("LifeScan: Ultra, Ultra2, Profile, Easy, UltraSmart"));
        
        lst_features.add(fg);
        
        
        fg = new FeaturesGroup(ic.getMessage("NOT_IMPLEMENTED_FEATURES"));

        
        //fg.addFeaturesEntry(new FeaturesEntry("Graphical Interface (GGC integration) [Ready]"));
        //fg.addFeaturesEntry(new FeaturesEntry("Configuration [Ready]"));
        //fg.addFeaturesEntry(new FeaturesEntry("Profiles"));
        fg.addFeaturesEntry(new FeaturesEntry("Graphs"));
        
        lst_features.add(fg);

        
        fg = new FeaturesGroup(ic.getMessage("PLANNED_DEVICES"));
        fg.addFeaturesEntry(new FeaturesEntry("Minimed RealTime (??)"));
        fg.addFeaturesEntry(new FeaturesEntry("Freestyle Navigator (??)"));
        //fg.addFeaturesEntry(new FeaturesEntry("Dana (in 2009/2010)"));
        
        lst_features.add(fg);
        
        
        this.plugin_features = lst_features;
    }

    
    // ********************************************************
    // ******                  Version                    *****    
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
    // ******         Web Lister Methods                  *****    
    // ********************************************************
    
    /**
     * Create WebLister (for List) Context for plugin
     */
    @Override
    public void createWebListerContext()
    {
        //this.loadWebLister();
        
        //I18nControlAbstract ic = getI18nControlInstance();
        
        this.weblister_items = new ArrayList<BaseListEntry>();
        this.weblister_items.add(new BaseListEntry("Abbott Diabetes Care", "/cgms/abbott.html", BaseListEntry.STATUS_PLANNED));
        this.weblister_items.add(new BaseListEntry("Dexcom", "/cgms/dexcom.html", BaseListEntry.STATUS_PLANNED));
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
        this.column_widths_table[4] = 110;   // value
        //this.column_widths_table[5] = 40;
        //this.column_widths_table[6] = 25;
        
        DataAccessCGMS.value_types = new String[3];
        DataAccessCGMS.value_types[0] = "";
        DataAccessCGMS.value_types[1] = this.i18n_plugin.getMessage("CGMS_READINGS");
        DataAccessCGMS.value_types[2] = this.i18n_plugin.getMessage("CALIBRATION_READINGS");

        
        DataAccessCGMS.value_type = new String[3];
        DataAccessCGMS.value_type[0] = "";
        DataAccessCGMS.value_type[1] = this.i18n_plugin.getMessage("CGMS_READING");
        DataAccessCGMS.value_type[2] = this.i18n_plugin.getMessage("CALIBRATION_READINGS");
        
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
    public void loadSpecialParameters()
    {
        this.special_parameters = new Hashtable<String,String>();
        this.special_parameters.put("BG", "" + this.getBGMeasurmentType());
    }

    
    
    /** 
     * Get About Image Size - Define about image size
     */
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
    
    
}
