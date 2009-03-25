package ggc.pump.util;

import ggc.plugin.list.BaseListEntry;
import ggc.plugin.util.DataAccessPlugInBase;
import ggc.pump.data.PumpValuesEntry;
import ggc.pump.data.db.GGCPumpDb;
import ggc.pump.data.defs.PumpAdditionalDataType;
import ggc.pump.data.defs.PumpAlarms;
import ggc.pump.data.defs.PumpBasalSubType;
import ggc.pump.data.defs.PumpBaseType;
import ggc.pump.data.defs.PumpBolusType;
import ggc.pump.data.defs.PumpErrors;
import ggc.pump.data.defs.PumpEvents;
import ggc.pump.data.defs.PumpReport;
import ggc.pump.manager.PumpManager;

import java.util.ArrayList;
import java.util.Hashtable;

import com.atech.db.hibernate.HibernateDb;
import com.atech.graphics.components.about.CreditsEntry;
import com.atech.graphics.components.about.CreditsGroup;
import com.atech.graphics.components.about.FeaturesEntry;
import com.atech.graphics.components.about.FeaturesGroup;
import com.atech.graphics.components.about.LibraryInfoEntry;
import com.atech.i18n.I18nControlAbstract;

/**
 *  Application:   GGC - GNU Gluco Control
 *  Plug-in:       Pump Tool (support for Pump devices)
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
 *  Filename:     DataAccessPump  
 *  Description:  Contains instances of utilities, private (plugin specific) data,
 *                help classes, ...
 * 
 *  Author: Andy {andy@atech-software.com}
 */


public class DataAccessPump extends DataAccessPlugInBase
{

    /**
     * PlugIn Version
     */
    public static final String PLUGIN_VERSION = "0.2.1";

    private static DataAccessPump s_da = null; // This is handle to unique 

    private PumpManager m_pumpManager = null;

    PumpBaseType m_pump_base_type = null;
    PumpBolusType m_pump_bolus_type = null;
    PumpBasalSubType m_pump_basal_type = null;
    PumpReport m_pump_report = null;
    PumpAdditionalDataType m_pump_add_type = null;
    PumpAlarms m_pump_alarms = null;
    PumpEvents m_pump_events = null;
    PumpErrors m_pump_errors = null;
    //    GGCPumpDb m_db = null;
  
    long selected_person_id = 0;

    // ********************************************************
    // ******      Constructors and Access methods        *****
    // ********************************************************

    //   Constructor:  DataAccess
    /**
     *
     *  This is DataAccess constructor; Since classes use Singleton Pattern,
     *  constructor is protected and can be accessed only with getInstance() 
     *  method.<br><br>
     *
     */
    private DataAccessPump()
    {
    	super(I18nControl.getInstance());
    } 

    
    /**
     * Init Special
     */
    public void initSpecial()
    {
        //this.loadTimeZones();
        //loadPumpsTable();
        checkPrerequisites();
        
        this.createWebListerContext();
        this.createPlugInAboutContext();
        this.createPlugInVersion();
        this.createPlugInDataRetrievalContext();
        
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
    public static DataAccessPump getInstance()
    {
        if (s_da == null)
            s_da = new DataAccessPump();
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
     static public DataAccess getInstance()
     {
     return m_da;
     }
     */

    //  Method:       deleteInstance
    /**
     *  This method sets handle to DataAccess to null and deletes the instance. <br><br>
     */
    public void deleteInstance()
    {
        m_i18n = null;
    }

/* 
    public void loadPumpsTable()
    {

    	metersUrl = new Hashtable<String,String>();
        metersNames = new ArrayList<String>();
    	
    	
    	
        metersNames.add("Abbott Diabetes Care");
        metersUrl.put("Abbott Diabetes Care", "abbott.html");
        metersNames.add("Bayer Diagnostics");
        metersUrl.put("Bayer Diagnostics", "bayer.html");
        metersNames.add("Diabetic Supply of Suncoast");
        metersUrl.put("Diabetic Supply of Suncoast", "suncoast.html");
        metersNames.add("Diagnostic Devices");
        metersUrl.put("Diagnostic Devices", "prodigy.html");
        metersNames.add("Arkray USA (formerly Hypoguard)");
        metersUrl.put("Arkray USA (formerly Hypoguard)", "arkray.html");
        metersNames.add("HealthPia America");
        metersUrl.put("HealthPia America", "healthpia.html");
        metersNames.add("Home Diagnostics");
        metersUrl.put("Home Diagnostics", "home_diganostics.html");
        metersNames.add("Lifescan");
        metersUrl.put("Lifescan", "lifescan.html");
        metersNames.add("Nova Biomedical");
        metersUrl.put("Nova Biomedical", "nova_biomedical.html");
        metersNames.add("Roche Diagnostics");
        metersUrl.put("Roche Diagnostics", "roche.html");
        metersNames.add("Sanvita");
        metersUrl.put("Sanvita", "sanvita.html");
        metersNames.add("U.S. Diagnostics");
        metersUrl.put("U.S. Diagnostics", "us_diagnostics.html");
        metersNames.add("WaveSense");
        metersUrl.put("WaveSense", "wavesense.html");

    }
  */  
    

    
    
    // ********************************************************
    // ******             Init  Methods                   *****    
    // ********************************************************
    


    /**
     * Init All Objects
     */
    public void initAllObjects()
    {
        this.m_pump_base_type = new PumpBaseType();
        this.m_pump_bolus_type = new PumpBolusType(); 
        this.m_pump_basal_type = new PumpBasalSubType(); 
        this.m_pump_report = new PumpReport();
        this.m_pump_alarms = new PumpAlarms();
        this.m_pump_events = new PumpEvents();
        this.m_pump_add_type = new PumpAdditionalDataType();
        this.m_pump_errors = new PumpErrors();
    
    }


    /** 
     * Get Application Name
     */
    public String getApplicationName()
    {
    	return "GGC_PumpTool";
    }
    
    
    /**
     * Create About Context for plugin
     */
    public void createPlugInAboutContext()
    {
        I18nControlAbstract ic = this.getI18nControlInstance();
        //this.about_title = ic.getMessage("PUMP_PLUGIN_ABOUT");
        this.about_image_name = "/icons/about_logo.gif";

        this.about_plugin_copyright_from = 2008;
        //this.about_plugin_name = ic.getMessage("PUMP_PLUGIN");
        
        
        // libraries
        ArrayList<LibraryInfoEntry> lst_libs = new ArrayList<LibraryInfoEntry>();
        
        lst_libs.add(new LibraryInfoEntry("Atech-Tools", "0.2.x", "www.atech-software.com", "LGPL", "Helper Library for Swing/Hibernate/...", "Copyright (c) 2006-2008 Atech Software Ltd. All rights reserved."));
        lst_libs.add(new LibraryInfoEntry("Apache Commons Lang", "2.4", "commons.apache.org/lang/", "Apache", "Helper methods for java.lang library"));
        lst_libs.add(new LibraryInfoEntry("Apache Commons Logging", "1.0.4", "commons.apache.org/logging/", "Apache", "Logger and all around wrapper for logging utilities"));
        lst_libs.add(new LibraryInfoEntry("dom4j", "1.6.1", "http://www.dom4j.org/", "BSD", "Framework for Xml manipulation"));
        lst_libs.add(new LibraryInfoEntry("RXTXcomm", "2.1.7", "www.rxtx.org", "LGPL", "Comm API"));
        lst_libs.add(new LibraryInfoEntry("XML Pull Parser", "3.1.1.4c", "http://www.extreme.indiana.edu/xgws/xsoap/xpp/", "Indiana University Extreme! Lab Software License", "Xml parser for processing xml document", "Copyright (c) 2002 Extreme! Lab, Indiana University. All rights reserved."));

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
        fg.addFeaturesEntry(new FeaturesEntry("Base Pump Tools Framework (partitial)"));
        fg.addFeaturesEntry(new FeaturesEntry("Various output types"));
        fg.addFeaturesEntry(new FeaturesEntry("Manual data entry (also additional data entry)"));
//        fg.addFeaturesEntry(new FeaturesEntry("Communication Framework"));
        fg.addFeaturesEntry(new FeaturesEntry("About dialog"));
        
        lst_features.add(fg);
        
        
//        fg = new FeaturesGroup(ic.getMessage("SUPPORTED_DEVICES"));
//        fg.addFeaturesEntry(new FeaturesEntry("Ascensia/Bayer"));
//        fg.addFeaturesEntry(new FeaturesEntry("Accu-chek/Roche"));
//        fg.addFeaturesEntry(new FeaturesEntry("LifeScan: Ultra, Ultra2, Profile, Easy, UltraSmart"));
        
//        lst_features.add(fg);
        
        
        fg = new FeaturesGroup(ic.getMessage("NOT_IMPLEMENTED_FEATURES"));
        //fg.addFeaturesEntry(new FeaturesEntry("Graphical Interface (GGC integration) [Ready]"));
        fg.addFeaturesEntry(new FeaturesEntry("Configuration [Ready]"));
        fg.addFeaturesEntry(new FeaturesEntry("List of pumps [Ready]"));
        fg.addFeaturesEntry(new FeaturesEntry("Reading data"));
        fg.addFeaturesEntry(new FeaturesEntry("Profiles"));
        fg.addFeaturesEntry(new FeaturesEntry("Graphs"));
        
        lst_features.add(fg);

        
        fg = new FeaturesGroup(ic.getMessage("PLANNED_DEVICES"));
        fg.addFeaturesEntry(new FeaturesEntry("Minimed (in 2009)"));
        fg.addFeaturesEntry(new FeaturesEntry("Roche (in 2009)"));
        
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
        this.weblister_items.add(new BaseListEntry("Minimed", "/pumps/minimed.html", BaseListEntry.STATUS_IMPLEMENTING));
        this.weblister_items.add(new BaseListEntry("Roche", "/pumps/roche.html", BaseListEntry.STATUS_IMPLEMENTING));
        this.weblister_items.add(new BaseListEntry("Sooil", "/pumps/sooil.html", BaseListEntry.STATUS_NOTPLANNED));
        
        
        this.weblister_title = "Meters List";
        this.weblister_desc = "No Description";
        
        
//        public BaseListEntry(String name, String page, int status)
        
        /*
        metersUrl = new Hashtable<String,String>();
        metersNames = new ArrayList<String>();
        
        
        
        metersNames.add("Abbott Diabetes Care");
        metersUrl.put("Abbott Diabetes Care", "abbott.html");
        metersNames.add("Bayer Diagnostics");
        metersUrl.put("Bayer Diagnostics", "bayer.html");
        metersNames.add("Diabetic Supply of Suncoast");
        metersUrl.put("Diabetic Supply of Suncoast", "suncoast.html");
        metersNames.add("Diagnostic Devices");
        metersUrl.put("Diagnostic Devices", "prodigy.html");
        metersNames.add("Arkray USA (formerly Hypoguard)");
        metersUrl.put("Arkray USA (formerly Hypoguard)", "arkray.html");
        metersNames.add("HealthPia America");
        metersUrl.put("HealthPia America", "healthpia.html");
        metersNames.add("Home Diagnostics");
        metersUrl.put("Home Diagnostics", "home_diganostics.html");
        metersNames.add("Lifescan");
        metersUrl.put("Lifescan", "lifescan.html");
        metersNames.add("Nova Biomedical");
        metersUrl.put("Nova Biomedical", "nova_biomedical.html");
        metersNames.add("Roche Diagnostics");
        metersUrl.put("Roche Diagnostics", "roche.html");
        metersNames.add("Sanvita");
        metersUrl.put("Sanvita", "sanvita.html");
        metersNames.add("U.S. Diagnostics");
        metersUrl.put("U.S. Diagnostics", "us_diagnostics.html");
        metersNames.add("WaveSense");
        metersUrl.put("WaveSense", "wavesense.html");
        */
    }

    


    // ********************************************************
    // ******                   Pumps                    *****    
    // ********************************************************
/*
    public PumpManager getPumpManager()
    {
        return this.m_pumpManager;
    }
*/

    
    // ********************************************************
    // ******                  Version                    *****    
    // ********************************************************
    
    
    /**
     * Create Plugin Version
     */
    public void createPlugInVersion()
    {
        this.plugin_version = DataAccessPump.PLUGIN_VERSION;
    }
    
    


    // ********************************************************
    // ******          Dates and Times Handling           *****    
    // ********************************************************


    /**
     * Get Start Year
     */
    public int getStartYear()
    {
        return 1980;
    }


    
    // ********************************************************
    // ******                   Db                        *****    
    // ********************************************************
    
    GGCPumpDb m_db;
    HibernateDb hdb;
    
    /**
     * Create Db
     * 
     * @param db
     */
    public void createDb(HibernateDb db)
    {
        this.m_db = new GGCPumpDb(db);
        this.hdb = db;
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
     * Get Hibernate Db
     */
    public HibernateDb getHibernateDb()
    {
        return this.hdb;
    }

    
    /**
     * Create Configuration Context for plugin
     */
    @Override
    public void createConfigurationContext()
    {
        // TODO Auto-generated method stub
        
    }


    /**
     * Create Device Configuration for plugin
     */    
    @Override
    public void createDeviceConfiguration()
    {
        // TODO Auto-generated method stub
        
    }


    /**
     * Create About Context for plugin
     */
    @Override
    public void createPlugInDataRetrievalContext()
    {
        m_entry_type = new PumpValuesEntry(true);
    }

    /**
     * Load Manager instance
     */
    @Override
    public void loadManager()
    {
        this.m_manager = this.m_pumpManager;
    }


    /**
     * Load Device Data Handler
     */
    @Override
    public void loadDeviceDataHandler()
    {
        // TODO Auto-generated method stub
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
    public long getSelectedPersonId()
    {
        return this.selected_person_id;
    }
    
    
    /**
     * Set Selected Persons Id
     * 
     * @param id 
     */
    public void setSelectedPersonId(long id)
    {
        this.selected_person_id = id;
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


    @Override
    public void loadPlugIns()
    {
        // TODO Auto-generated method stub
        
    }

    
    
    
    
    // configuration settings
    
    public float getMaxBasalValue()
    {
        return 8.0f;
    }
    
    public float getBasalStep()
    {
        return 0.1f;
    }
    
    
}
