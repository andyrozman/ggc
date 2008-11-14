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
 *  Filename: DataAccess
 *  Purpose:  Used for utility works and static data handling (this is singelton
 *      class which holds all our definitions, so that we don't need to create them
 *      again for each class.      
 *
 *  Author:   andyrozman
 */

package ggc.pump.util;

import ggc.plugin.list.BaseListEntry;
import ggc.plugin.util.DataAccessPlugInBase;
import ggc.pump.data.db.GGCPumpDb;
import ggc.pump.data.defs.PumpAdditionalDataType;
import ggc.pump.data.defs.PumpBasalSubType;
import ggc.pump.data.defs.PumpBaseType;
import ggc.pump.data.defs.PumpBolusType;
import ggc.pump.data.defs.PumpReport;
import ggc.pump.manager.PumpManager;

import java.util.ArrayList;
import java.util.Hashtable;

import javax.swing.JFrame;

import com.atech.db.hibernate.HibernateDb;
import com.atech.graphics.components.about.CreditsEntry;
import com.atech.graphics.components.about.CreditsGroup;
import com.atech.graphics.components.about.FeaturesEntry;
import com.atech.graphics.components.about.FeaturesGroup;
import com.atech.graphics.components.about.LibraryInfoEntry;
import com.atech.i18n.I18nControlAbstract;


public class DataAccessPump extends DataAccessPlugInBase
{


    private static DataAccessPump s_da = null; // This is handle to unique 

    private PumpManager m_pumpManager = null;

    PumpBaseType m_pump_base_type = null;
    PumpBolusType m_pump_bolus_type = null;
    PumpBasalSubType m_pump_basal_type = null;
    PumpReport m_pump_report = null;
    PumpAdditionalDataType m_pump_add_type = null;
//    GGCPumpDb m_db = null;
        

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

    
    public void initSpecial()
    {
        //this.loadTimeZones();
        //loadPumpsTable();
        checkPrerequisites();
        
        this.createWebListerContext();
        this.createPlugInAboutContext();
        
        
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

    public static DataAccessPump createInstance(JFrame main)
    {
        if (s_da == null)
        {
            //GGCDb db = new GGCDb();
            s_da = new DataAccessPump();
//x            s_da.setParent(main);
        }

        return s_da;
    }

 
    
    public PumpBaseType getPumpBaseType()
    {
        return this.m_pump_base_type;
    }
    
    public PumpBolusType getBolusSubType()
    { 
        return m_pump_bolus_type;
    }
    
    public PumpBasalSubType getBasalSubType()
    {
        return m_pump_basal_type;
    }

    public PumpReport getPumpReportTypes()
    { 
        return this.m_pump_report;
    }
    
    public PumpAdditionalDataType getAdditionalType()
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
    


    public void initAllObjects()
    {
        this.m_pump_base_type = new PumpBaseType();
        this.m_pump_bolus_type = new PumpBolusType(); 
        this.m_pump_basal_type = new PumpBasalSubType(); 
        this.m_pump_report = new PumpReport();
        this.m_pump_add_type = new PumpAdditionalDataType();
    }

    
    

    public String getApplicationName()
    {
    	return "GGC_PumpTool";
    }
    
    
    
    public void createPlugInAboutContext()
    {
        I18nControlAbstract ic = this.getI18nControlInstance();
        this.about_title = ic.getMessage("PUMP_PLUGIN_ABOUT");
        this.about_image_name = "/icons/about_logo.gif";

        this.about_plugin_copyright_from = 2008;
        this.about_plugin_name = ic.getMessage("PUMP_PLUGIN");
        
        
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
        cg.addCreditsEntry(new CreditsEntry("Aleksander Rozman (Andy)", "andy@atech-software.com", "Framework")); // and support for Ascensia & Roche devices"));
        lst_credits.add(cg);

        //cg = new CreditsGroup(ic.getMessage("HELPERS_DESC"));
        //cg.addCreditsEntry(new CreditsEntry("Rafael Ziherl (RAF)", "", "Supplied hardware for Roche development"));
        lst_credits.add(cg);
                
        this.plugin_developers = lst_credits;

        
        // features
        ArrayList<FeaturesGroup> lst_features = new ArrayList<FeaturesGroup>();

        
        FeaturesGroup fg = new FeaturesGroup(ic.getMessage("not implemented"));
        fg.addFeaturesEntry(new FeaturesEntry("test"));
        
        lst_features.add(fg);
        
        this.plugin_features = lst_features;
        
    }
    

    
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

    public PumpManager getPumpManager()
    {
        return this.m_pumpManager;
    }


    // ********************************************************
    // ******                   Pumps                    *****    
    // ********************************************************

    public void loadTimeZones()
    {
        this.timeZones = new Hashtable<String,String>();

        // Posible needed enchancment. We should probably list all ID's as values. On windows default ID can be different 
        // as in this table. We should add this names, if we encounter problems.

        this.timeZones.put("(GMT+13:00) Nuku'alofa", "Pacific/Tongatapu");
        this.timeZones.put("(GMT+12:00) Fiji, Kamchatka, Marshall Is.", "Pacific/Fiji");
        this.timeZones.put("(GMT+12:00) Auckland, Wellington", "Pacific/Auckland");
        this.timeZones.put("(GMT+11:00) Magadan, Solomon Is., New Caledonia", "Asia/Magadan");
        this.timeZones.put("(GMT+10:00) Vladivostok", "Asia/Vladivostok");
        this.timeZones.put("(GMT+10:00) Hobart", "Australia/Hobart");
        this.timeZones.put("(GMT+10:00) Guam, Port Moresby", "Pacific/Guam");
        this.timeZones.put("(GMT+10:00) Canberra, Melbourne, Sydney", "Australia/Sydney");
        this.timeZones.put("(GMT+10:00) Brisbane", "Australia/Brisbane");
        this.timeZones.put("(GMT+09:30) Adelaide", "Australia/Adelaide");
        this.timeZones.put("(GMT+09:00) Yakutsk", "Asia/Yakutsk");
        this.timeZones.put("(GMT+09:00) Seoul", "Asia/Seoul");
        this.timeZones.put("(GMT+09:00) Osaka, Sapporo, Tokyo", "Asia/Tokyo");
        this.timeZones.put("(GMT+08:00) Taipei", "Asia/Taipei");
        this.timeZones.put("(GMT+08:00) Perth", "Australia/Perth");
        this.timeZones.put("(GMT+08:00) Kuala Lumpur, Singapore", "Asia/Kuala_Lumpur");
        this.timeZones.put("(GMT+08:00) Irkutsk, Ulaan Bataar", "Asia/Irkutsk");
        this.timeZones.put("(GMT+08:00) Beijing, Chongqing, Hong Kong, Urumqi", "Asia/Hong_Kong");
        this.timeZones.put("(GMT+07:00) Krasnoyarsk", "Asia/Krasnoyarsk");
        this.timeZones.put("(GMT+07:00) Bangkok, Hanoi, Jakarta", "Asia/Bangkok");
        this.timeZones.put("(GMT+06:30) Rangoon", "Asia/Rangoon");
        this.timeZones.put("(GMT+06:00) Sri Jayawardenepura", "Asia/Colombo");
        this.timeZones.put("(GMT+06:00) Astana, Dhaka", "Asia/Dhaka");
        this.timeZones.put("(GMT+06:00) Almaty, Novosibirsk", "Asia/Almaty");
        this.timeZones.put("(GMT+05:45) Kathmandu", "Asia/Katmandu");
        this.timeZones.put("(GMT+05:30) Chennai, Kolkata, Mumbai, New Delhi", "Asia/Calcutta");
        this.timeZones.put("(GMT+05:00) Islamabad, Karachi, Tashkent", "Asia/Karachi");
        this.timeZones.put("(GMT+05:00) Ekaterinburg", "Asia/Yekaterinburg");
        this.timeZones.put("(GMT+04:30) Kabul", "Asia/Kabul");
        this.timeZones.put("(GMT+04:00) Baku, Tbilisi, Yerevan", "Asia/Baku");
        this.timeZones.put("(GMT+04:00) Abu Dhabi, Muscat", "Asia/Dubai");
        this.timeZones.put("(GMT+03:30) Tehran", "Asia/Tehran");
        this.timeZones.put("(GMT+03:00) Nairobi", "Africa/Nairobi");
        this.timeZones.put("(GMT+03:00) Moscow, St. Petersburg, Volgograd", "Europe/Moscow");
        this.timeZones.put("(GMT+03:00) Kuwait, Riyadh", "Asia/Kuwait");
        this.timeZones.put("(GMT+03:00) Baghdad", "Asia/Baghdad");
        this.timeZones.put("(GMT+02:00) Jerusalem", "Asia/Jerusalem");
        this.timeZones.put("(GMT+02:00) Helsinki, Kyiv, Riga, Sofia, Tallinn, Vilnius", "Europe/Helsinki");
        this.timeZones.put("(GMT+02:00) Harare, Pretoria", "Africa/Harare");
        this.timeZones.put("(GMT+02:00) Cairo", "Africa/Cairo");
        this.timeZones.put("(GMT+02:00) Bucharest", "Europe/Bucharest");
        this.timeZones.put("(GMT+02:00) Athens, Istanbul, Minsk", "Europe/Athens");
        this.timeZones.put("(GMT+01:00) West Central Africa", "Africa/Lagos");
        this.timeZones.put("(GMT+01:00) Sarajevo, Skopje, Warsaw, Zagreb", "Europe/Warsaw");
        this.timeZones.put("(GMT+01:00) Brussels, Copenhagen, Madrid, Paris", "Europe/Brussels");
        this.timeZones.put("(GMT+01:00) Belgrade, Bratislava, Budapest, Ljubljana, Prague", "Europe/Prague,Europe/Belgrade");
        this.timeZones.put("(GMT+01:00) Amsterdam, Berlin, Bern, Rome, Stockholm, Vienna", "Europe/Amsterdam");
        this.timeZones.put("(GMT) Casablanca, Monrovia", "Africa/Casablanca");
        this.timeZones.put("(GMT) Greenwich Mean Time : Dublin, Edinburgh, Lisbon, London", "Europe/Dublin");
        this.timeZones.put("(GMT-01:00) Azores", "Atlantic/Azores");
        this.timeZones.put("(GMT-01:00) Cape Verde Is.", "Atlantic/Cape_Verde");
        this.timeZones.put("(GMT-02:00) Mid-Atlantic", "Atlantic/South_Georgia");
        this.timeZones.put("(GMT-03:00) Brasilia", "America/Sao_Paulo");
        this.timeZones.put("(GMT-03:00) Buenos Aires, Georgetown", "America/Buenos_Aires");
        this.timeZones.put("(GMT-03:00) Greenland", "America/Thule");
        this.timeZones.put("(GMT-03:30) Newfoundland", "America/St_Johns");
        this.timeZones.put("(GMT-04:00) Atlantic Time (Canada)", "America/Halifax");
        this.timeZones.put("(GMT-04:00) Caracas, La Paz", "America/Caracas");
        this.timeZones.put("(GMT-04:00) Santiago", "America/Santiago");
        this.timeZones.put("(GMT-05:00) Bogota, Lima, Quito", "America/Bogota");
        this.timeZones.put("(GMT-05:00) Eastern Time (US & Canada)", " America/New_York");
        this.timeZones.put("(GMT-05:00) Indiana (East)", "America/Indianapolis");
        this.timeZones.put("(GMT-06:00) Central America", "America/Costa_Rica");
       this.timeZones.put("(GMT-06:00) Central Time (US & Canada)", "America/Chicago");
       this.timeZones.put("(GMT-06:00) Guadalajara, Mexico City, Monterrey", "America/Mexico_City");
       this.timeZones.put("(GMT-06:00) Saskatchewan", "America/Winnipeg");
       this.timeZones.put("(GMT-07:00) Arizona", "America/Phoenix");
       this.timeZones.put("(GMT-07:00) Chihuahua, La Paz, Mazatlan", "America/Tegucigalpa");
       this.timeZones.put("(GMT-07:00) Mountain Time (US & Canada)", "America/Denver");
       this.timeZones.put("(GMT-08:00) Pacific Time (US & Canada); Tijuana", "America/Los_Angeles");
       this.timeZones.put("(GMT-09:00) Alaska", "America/Anchorage");
       this.timeZones.put("(GMT-10:00) Hawaii", "Pacific/Honolulu");
       this.timeZones.put("(GMT-11:00) Midway Island, Samoa", "Pacific/Apia");
       this.timeZones.put("(GMT-12:00) International Date Line West", "MIT");

    }








    // ********************************************************
    // ******          Dates and Times Handling           *****    
    // ********************************************************


    public int getStartYear()
    {
        return 1980;
    }


    
    // ********************************************************
    // ******                   Db                        *****    
    // ********************************************************
    
    GGCPumpDb m_db;
    
    
    public void createDb(HibernateDb db)
    {
        this.m_db = new GGCPumpDb(db);
        
    }
    

    public GGCPumpDb getDb()
    {
        return this.m_db;
    }


    
    public HibernateDb getHibernateDb()
    {
        return null;
    }

    
}
