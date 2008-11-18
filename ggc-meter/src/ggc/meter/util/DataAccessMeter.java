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

package ggc.meter.util;

import ggc.meter.data.cfg.MeterConfigurationDefinition;
import ggc.meter.manager.MeterManager;
import ggc.plugin.cfg.DeviceConfiguration;
import ggc.plugin.list.BaseListEntry;
import ggc.plugin.util.DataAccessPlugInBase;

import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Vector;

import javax.swing.ImageIcon;
import javax.swing.JFrame;

import com.atech.graphics.components.about.CreditsEntry;
import com.atech.graphics.components.about.CreditsGroup;
import com.atech.graphics.components.about.FeaturesEntry;
import com.atech.graphics.components.about.FeaturesGroup;
import com.atech.graphics.components.about.LibraryInfoEntry;
import com.atech.i18n.I18nControlAbstract;


public class DataAccessMeter extends DataAccessPlugInBase
{

    private static DataAccessMeter s_da = null; // This is handle to unique 

    private MeterManager m_meterManager = null;

    public Hashtable<String,String> timeZones;
    public Vector<String> time_zones_vector;
    

    public ImageIcon config_icons[] = null;
    public String[] options_yes_no = null;

    JFrame m_main = null;        
        
        
    public Hashtable<String,String> metersUrl;
    public ArrayList<String> metersNames;
        
        

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
    private DataAccessMeter(JFrame frame)
    {
    	super(I18nControl.getInstance());
        this.m_main = frame;
    } 

    
    public void initSpecial()
    {
        loadTimeZones();
        checkPrerequisites();
        createWebListerContext();
        createPlugInAboutContext();
        createConfigurationContext();
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
        if (s_da == null)
            s_da = new DataAccessMeter(null);
        return s_da;
    }

    public static DataAccessMeter createInstance(JFrame main)
    {
        if (s_da == null)
        {
            //GGCDb db = new GGCDb();
            s_da = new DataAccessMeter(main);
//x            s_da.setParent(main);
        }

        return s_da;
    }

 
    
    
    public void loadConfigIcons()
    {
        config_icons = new ImageIcon[5];
        config_icons[0] = new ImageIcon(getImage("/icons/cfg_general.png", m_main));
        config_icons[1] = new ImageIcon(getImage("/icons/cfg_medical.png", m_main));
        config_icons[2] = new ImageIcon(getImage("/icons/cfg_print.png", m_main));
        config_icons[3] = new ImageIcon(getImage("/icons/cfg_meter.png", m_main));
        config_icons[4] = new ImageIcon(getImage("/icons/cfg_general.png", m_main));
        
    }

  



    //  Method:       deleteInstance
    /**
     *  This method sets handle to DataAccessMeter to null and deletes the instance. <br><br>
     */
    public void deleteInstance()
    {
        super.m_i18n = null;
    }

 

    
    public void createPlugInAboutContext()
    {
        I18nControlAbstract ic = getI18nControlInstance();
        
        about_title = ic.getMessage("METER_PLUGIN_ABOUT");
        about_image_name = "/icons/about_meter.jpg";
//        about_image_name = "/icons/about_logo.gif";
        about_plugin_copyright_from = 2006;
        about_plugin_name = ic.getMessage("METER_PLUGIN");
        
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
        cg.addCreditsEntry(new CreditsEntry("Aleksander Rozman (Andy)", "andy@atech-software.com", "Framework and support for Ascensia & Roche devices"));
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
        fg.addFeaturesEntry(new FeaturesEntry("List of meters (work in progress)"));
        fg.addFeaturesEntry(new FeaturesEntry("Simple configuration"));
        
        lst_features.add(fg);
        
        
        fg = new FeaturesGroup(ic.getMessage("SUPPORTED_DEVICES"));
        fg.addFeaturesEntry(new FeaturesEntry("Ascensia/Bayer"));
        fg.addFeaturesEntry(new FeaturesEntry("Accu-chek/Roche"));
        fg.addFeaturesEntry(new FeaturesEntry("LifeScan (work in progress)"));
        
        lst_features.add(fg);
        
        
        fg = new FeaturesGroup(ic.getMessage("NOT_IMPLEMENTED_FEATURES"));
        fg.addFeaturesEntry(new FeaturesEntry("Configuration"));
        
        lst_features.add(fg);

        
        fg = new FeaturesGroup(ic.getMessage("PLANNED_DEVICES"));
        fg.addFeaturesEntry(new FeaturesEntry("LifeScan (end of 2008)"));
        fg.addFeaturesEntry(new FeaturesEntry("Abbott (in 2009)"));
        fg.addFeaturesEntry(new FeaturesEntry("???"));
        
        lst_features.add(fg);
        
        
        this.plugin_features = lst_features;
        
        
    }

    public void createWebListerContext()
    {
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
        
        weblister_title = ic.getMessage("METERS_LIST_WEB");
        weblister_desc = ic.getMessage("METERS_LIST_WEB_DESC");
    }
        
    
    public String getApplicationName()
    {
    	return "GGC_MeterTool";
    }
    
    
    public String getImagesRoot()
    {
    	return "/icons/";
    }
    
    public int[] getAboutImageSize()
    {
        int[] sz = new int[2];
        sz[0] = 200;
        sz[1] = 125;
        
        return sz;
    }
    
    

    // ********************************************************
    // ******                   Meters                    *****    
    // ********************************************************

    public MeterManager getMeterManager()
    {
        return this.m_meterManager;
    }


    public void loadTimeZones()
    {
        this.timeZones = new Hashtable<String,String>();

        // Posible needed enchancment. We should probably list all ID's as values. On windows default ID can be different 
        // as in this table. We should add this names, if we encounter problems.

        addTimeZoneEntry("(GMT+13:00) Nuku'alofa", "Pacific/Tongatapu");
        addTimeZoneEntry("(GMT+12:00) Fiji, Kamchatka, Marshall Is.", "Pacific/Fiji");
        addTimeZoneEntry("(GMT+12:00) Auckland, Wellington", "Pacific/Auckland");
        addTimeZoneEntry("(GMT+11:00) Magadan, Solomon Is., New Caledonia", "Asia/Magadan");
        addTimeZoneEntry("(GMT+10:00) Vladivostok", "Asia/Vladivostok");
        addTimeZoneEntry("(GMT+10:00) Hobart", "Australia/Hobart");
        addTimeZoneEntry("(GMT+10:00) Guam, Port Moresby", "Pacific/Guam");
        addTimeZoneEntry("(GMT+10:00) Canberra, Melbourne, Sydney", "Australia/Sydney");
        addTimeZoneEntry("(GMT+10:00) Brisbane", "Australia/Brisbane");
        addTimeZoneEntry("(GMT+09:30) Adelaide", "Australia/Adelaide");
        addTimeZoneEntry("(GMT+09:00) Yakutsk", "Asia/Yakutsk");
        addTimeZoneEntry("(GMT+09:00) Seoul", "Asia/Seoul");
        addTimeZoneEntry("(GMT+09:00) Osaka, Sapporo, Tokyo", "Asia/Tokyo");
        addTimeZoneEntry("(GMT+08:00) Taipei", "Asia/Taipei");
        addTimeZoneEntry("(GMT+08:00) Perth", "Australia/Perth");
        addTimeZoneEntry("(GMT+08:00) Kuala Lumpur, Singapore", "Asia/Kuala_Lumpur");
        addTimeZoneEntry("(GMT+08:00) Irkutsk, Ulaan Bataar", "Asia/Irkutsk");
        addTimeZoneEntry("(GMT+08:00) Beijing, Chongqing, Hong Kong, Urumqi", "Asia/Hong_Kong");
        addTimeZoneEntry("(GMT+07:00) Krasnoyarsk", "Asia/Krasnoyarsk");
        addTimeZoneEntry("(GMT+07:00) Bangkok, Hanoi, Jakarta", "Asia/Bangkok");
        addTimeZoneEntry("(GMT+06:30) Rangoon", "Asia/Rangoon");
        addTimeZoneEntry("(GMT+06:00) Sri Jayawardenepura", "Asia/Colombo");
        addTimeZoneEntry("(GMT+06:00) Astana, Dhaka", "Asia/Dhaka");
        addTimeZoneEntry("(GMT+06:00) Almaty, Novosibirsk", "Asia/Almaty");
        addTimeZoneEntry("(GMT+05:45) Kathmandu", "Asia/Katmandu");
        addTimeZoneEntry("(GMT+05:30) Chennai, Kolkata, Mumbai, New Delhi", "Asia/Calcutta");
        addTimeZoneEntry("(GMT+05:00) Islamabad, Karachi, Tashkent", "Asia/Karachi");
        addTimeZoneEntry("(GMT+05:00) Ekaterinburg", "Asia/Yekaterinburg");
        addTimeZoneEntry("(GMT+04:30) Kabul", "Asia/Kabul");
        addTimeZoneEntry("(GMT+04:00) Baku, Tbilisi, Yerevan", "Asia/Baku");
        addTimeZoneEntry("(GMT+04:00) Abu Dhabi, Muscat", "Asia/Dubai");
        addTimeZoneEntry("(GMT+03:30) Tehran", "Asia/Tehran");
        addTimeZoneEntry("(GMT+03:00) Nairobi", "Africa/Nairobi");
        addTimeZoneEntry("(GMT+03:00) Moscow, St. Petersburg, Volgograd", "Europe/Moscow");
        addTimeZoneEntry("(GMT+03:00) Kuwait, Riyadh", "Asia/Kuwait");
        addTimeZoneEntry("(GMT+03:00) Baghdad", "Asia/Baghdad");
        addTimeZoneEntry("(GMT+02:00) Jerusalem", "Asia/Jerusalem");
        addTimeZoneEntry("(GMT+02:00) Helsinki, Kyiv, Riga, Sofia, Tallinn, Vilnius", "Europe/Helsinki");
        addTimeZoneEntry("(GMT+02:00) Harare, Pretoria", "Africa/Harare");
        addTimeZoneEntry("(GMT+02:00) Cairo", "Africa/Cairo");
        addTimeZoneEntry("(GMT+02:00) Bucharest", "Europe/Bucharest");
        addTimeZoneEntry("(GMT+02:00) Athens, Istanbul, Minsk", "Europe/Athens");
        addTimeZoneEntry("(GMT+01:00) West Central Africa", "Africa/Lagos");
        addTimeZoneEntry("(GMT+01:00) Sarajevo, Skopje, Warsaw, Zagreb", "Europe/Warsaw");
        addTimeZoneEntry("(GMT+01:00) Brussels, Copenhagen, Madrid, Paris", "Europe/Brussels");
        addTimeZoneEntry("(GMT+01:00) Belgrade, Bratislava, Budapest, Ljubljana, Prague", "Europe/Prague,Europe/Belgrade");
        addTimeZoneEntry("(GMT+01:00) Amsterdam, Berlin, Bern, Rome, Stockholm, Vienna", "Europe/Amsterdam");
        addTimeZoneEntry("(GMT) Casablanca, Monrovia", "Africa/Casablanca");
        addTimeZoneEntry("(GMT) Greenwich Mean Time : Dublin, Edinburgh, Lisbon, London", "Europe/Dublin");
        addTimeZoneEntry("(GMT-01:00) Azores", "Atlantic/Azores");
        addTimeZoneEntry("(GMT-01:00) Cape Verde Is.", "Atlantic/Cape_Verde");
        addTimeZoneEntry("(GMT-02:00) Mid-Atlantic", "Atlantic/South_Georgia");
        addTimeZoneEntry("(GMT-03:00) Brasilia", "America/Sao_Paulo");
        addTimeZoneEntry("(GMT-03:00) Buenos Aires, Georgetown", "America/Buenos_Aires");
        addTimeZoneEntry("(GMT-03:00) Greenland", "America/Thule");
        addTimeZoneEntry("(GMT-03:30) Newfoundland", "America/St_Johns");
        addTimeZoneEntry("(GMT-04:00) Atlantic Time (Canada)", "America/Halifax");
        addTimeZoneEntry("(GMT-04:00) Caracas, La Paz", "America/Caracas");
        addTimeZoneEntry("(GMT-04:00) Santiago", "America/Santiago");
        addTimeZoneEntry("(GMT-05:00) Bogota, Lima, Quito", "America/Bogota");
       addTimeZoneEntry("(GMT-05:00) Eastern Time (US & Canada)", " America/New_York");
       addTimeZoneEntry("(GMT-05:00) Indiana (East)", "America/Indianapolis");
       addTimeZoneEntry("(GMT-06:00) Central America", "America/Costa_Rica");
       addTimeZoneEntry("(GMT-06:00) Central Time (US & Canada)", "America/Chicago");
       addTimeZoneEntry("(GMT-06:00) Guadalajara, Mexico City, Monterrey", "America/Mexico_City");
       addTimeZoneEntry("(GMT-06:00) Saskatchewan", "America/Winnipeg");
       addTimeZoneEntry("(GMT-07:00) Arizona", "America/Phoenix");
       addTimeZoneEntry("(GMT-07:00) Chihuahua, La Paz, Mazatlan", "America/Tegucigalpa");
       addTimeZoneEntry("(GMT-07:00) Mountain Time (US & Canada)", "America/Denver");
       addTimeZoneEntry("(GMT-08:00) Pacific Time (US & Canada); Tijuana", "America/Los_Angeles");
       addTimeZoneEntry("(GMT-09:00) Alaska", "America/Anchorage");
       addTimeZoneEntry("(GMT-10:00) Hawaii", "Pacific/Honolulu");
       addTimeZoneEntry("(GMT-11:00) Midway Island, Samoa", "Pacific/Apia");
       addTimeZoneEntry("(GMT-12:00) International Date Line West", "MIT");

       
       this.time_zones_vector = new Vector<String>();
       
       for(Enumeration<String> en= this.timeZones.keys(); en.hasMoreElements(); )
       {
           this.time_zones_vector.add(en.nextElement());
       }
       
       
    }


    public void addTimeZoneEntry(String long_desc, String keycode)
    {
        //SimpleConfigurationTZDialog.time_zones.put(long_desc, keycode);
        //SimpleConfigurationTZDialog.time_zones_vector.add(long_desc);
    }

    
    // ********************************************************
    // ******              Configuration                  *****    
    // ********************************************************
    
    

    @Override
    public void createConfigurationContext()
    {
        this.device_config_def = new MeterConfigurationDefinition();
    }


    @Override
    public void createDeviceConfiguration()
    {
        this.device_config = new DeviceConfiguration(this);
    }
    
    
    



    
    




}
