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

import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.awt.Image;
import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Enumeration;
import java.util.GregorianCalendar;
import java.util.Hashtable;
import java.util.Properties;
import java.util.Vector;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;

import com.atech.i18n.I18nControlAbstract;
import com.atech.utils.ATDataAccessAbstract;


public abstract class DataAccessPlugInBase extends ATDataAccessAbstract
{


    public static final String pathPrefix = ".";

    //public I18nControl m_i18n = null;

//    private static DataAccessMeter s_da = null; // This is handle to unique 


    public Font fonts[] = null;



//xa    private GGCProperties m_settings = null;

    //private DbToolApplicationGGC m_configFile = null;
//    private ConfigurationManager m_cfgMgr = null;


    public static DecimalFormat MmolDecimalFormat = new DecimalFormat("#0.0");

    /**
     * Which BG unit is used: BG_MGDL = mg/dl, BG_MMOL = mmol/l
     */
    public int m_BG_unit = BG_MGDL;

    public String[] availableLanguages = { "English", "Deutsch", "Slovenski", };

    public String[] avLangPostfix = { "en", "de", "si", };

    public String[] bg_units = { "mg/dl", "mmol/l" };

    public Hashtable<String,String> timeZones;

    public Vector<String> time_zones_vector;
    
//    public static DecimalFormat MmolDecimalFormat = new DecimalFormat("#0.0");

    public ImageIcon config_icons[] = null;
/*
    {
	    new ImageIcon("/icons/cfg_general.png"), 
	    new ImageIcon("/icons/cfg_medical.png"), 
	    new ImageIcon("icons/cfg_colors.png"), 
	    new ImageIcon("icons/cfg_render.png"), 
	    new ImageIcon("icons/cfg_meter.png"), 
	    new ImageIcon("icons/cfg_print.png")
	};
*/
/*	public ImageIcon config_icons[] = {
	    new ImageIcon("images/cfg_db.gif"), 
	    new ImageIcon("images/cfg_look.gif"), 
	    new ImageIcon("images/cfg_myparish.gif"), 
	    new ImageIcon("images/cfg_masses.gif"), 
	    new ImageIcon("images/cfg_users.gif"), 
	    new ImageIcon("images/cfg_lang.gif"), 
	    new ImageIcon("images/cfg_web.gif"), 
	    null
	};

    public String config_types[] = { 
	m_ic.getMessage("GENERAL"),
	m_ic.getMessage("MEDICAL_DATA"),
	m_ic.getMessage("COLORS_AND_FONTS"),
	m_ic.getMessage("RENDERING_QUALITY"),
	m_ic.getMessage("METER_CONFIGURATION"),
	m_ic.getMessage("PRINTING")
    };

*/
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
    public DataAccessPlugInBase(I18nControlAbstract i18n)
    {
    	super(i18n); //I18nControl.getInstance());

    	this.loadTimeZones();
        
        loadMetersTable();
        loadConfigIcons();
        
        checkPrerequisites();

    } 

    // FIXME should be abstract
    public void initSpecial()
    {
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
/*    public static DataAccessMeter getInstance()
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
*/
 
    
    
    public void loadConfigIcons()
    {
        config_icons = new ImageIcon[5];
        config_icons[0] = new ImageIcon(getImage("/icons/cfg_general.png", m_main));
        config_icons[1] = new ImageIcon(getImage("/icons/cfg_medical.png", m_main));
        config_icons[2] = new ImageIcon(getImage("/icons/cfg_print.png", m_main));
        config_icons[3] = new ImageIcon(getImage("/icons/cfg_meter.png", m_main));
        config_icons[4] = new ImageIcon(getImage("/icons/cfg_general.png", m_main));
        
    }

  


    /*
     static public DataAccessMeter getInstance()
     {
     return m_da;
     }
     */

    //  Method:       deleteInstance
    /**
     *  This method sets handle to DataAccessMeter to null and deletes the instance. <br><br>
     */
    public void deleteInstance()
    {
        m_i18n = null;
    }

 
    public void loadMetersTable()
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
    
    

    /*
    public void startDb(StatusBarL bar2)
    {
        GGCDbLoader loader = new GGCDbLoader(this, bar2);
        loader.start();
    } */
/*
    public GGCDb getDb()
    {
        return m_db;
    }
*/

    
    public String weblister_title;
    
    
    public abstract void createWebListerItems();
    
    
    public String getWebListerTitle()
    {
        return this.weblister_title;
    }

    
    public String getWebListerDescription()
    {
     //   return this.weblister_title;
        return null;
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
    // ******         Abstract Methods                    *****    
    // ********************************************************
    


    public String getApplicationName()
    {
    	return "GGC_MeterTool";
    }
    
    
    
    public void checkPrerequisites()
    {
    }
    
    
    public String getImagesRoot()
    {
    	return "/icons/";
    }
    
    
    public void loadBackupRestoreCollection()
    {
    }
    
    
    
    
    
    // ********************************************************
    // ******                   Fonts                     *****    
    // ********************************************************

    public static final int FONT_BIG_BOLD = 0;

    public static final int FONT_NORMAL = 1;

    public static final int FONT_NORMAL_BOLD = 2;

    public void loadFonts()
    {
        fonts = new Font[3];
        fonts[0] = new Font("SansSerif", Font.BOLD, 22);
        fonts[1] = new Font("SansSerif", Font.PLAIN, 12);
        fonts[2] = new Font("SansSerif", Font.BOLD, 12);
    }

    public Font getFont(int font_id)
    {
        return fonts[font_id];
    }
    
    
    
    // ********************************************************
    // ******                 Icons                       *****    
    // ********************************************************




    // ********************************************************
    // ******                    Db                       *****    
    // ********************************************************



    // ********************************************************
    // ******                   Meters                    *****    
    // ********************************************************


    // ********************************************************
    // ******                   Meters                    *****    
    // ********************************************************

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
    // ******                  Settings                   *****    
    // ********************************************************
/*
    public GGCProperties getSettings()
    {
        return this.m_settings;
    }

    public void loadSettingsFromDb()
    {
        this.m_settings.load();
    }
*/
    public Color getColor(int color)
    {
        return new Color(color);
    }
/*
    public ConfigurationManager getConfigurationManager()
    {
	return this.m_cfgMgr;
    }
*/

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
    // ******          Parent handling (for UIs)          *****    
    // ********************************************************



    /**
     *  Utils
     */


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



    // ********************************************************
    // ******               Look and Feel                 *****    
    // ********************************************************


    public static String[] getLFData()
    {
        String out[] = new String[2];

        try
        {
            Properties props = new Properties();

            FileInputStream in = new FileInputStream("../data/GGC_Config.properties");
            props.load(in);

            out[0] = (String)props.get("LF_CLASS");
            out[1] = (String)props.get("SKINLF_SELECTED");

            return out;

        }
        catch(Exception ex)
        {
            System.out.println("DataAccessMeter::getLFData::Exception> " + ex);
            return null;
        }
    }




    // ********************************************************
    // ******          Dates and Times Handling           *****    
    // ********************************************************

    public String getCurrentDateString()
    {
        GregorianCalendar gc = new GregorianCalendar();
        return gc.get(Calendar.DAY_OF_MONTH) + "."
                + (gc.get(Calendar.MONTH) + 1) + "." + gc.get(Calendar.YEAR);
    }

    public String[] getMonthsArray()
    {
        String arr[] = new String[12];

        arr[0] = m_i18n.getMessage("JANUARY");
        arr[1] = m_i18n.getMessage("FEBRUARY");
        arr[2] = m_i18n.getMessage("MARCH");
        arr[3] = m_i18n.getMessage("APRIL");
        arr[4] = m_i18n.getMessage("MAY");
        arr[5] = m_i18n.getMessage("JUNE");
        arr[6] = m_i18n.getMessage("JULY");
        arr[7] = m_i18n.getMessage("AUGUST");
        arr[8] = m_i18n.getMessage("SEPTEMBER");
        arr[9] = m_i18n.getMessage("OCTOBER");
        arr[10] = m_i18n.getMessage("NOVEMBER");
        arr[11] = m_i18n.getMessage("DECEMBER");

        return arr;

    }

    public String getDateString(int date)
    {
        // 20051012

        int year = date / 10000;
        int months = date - (year * 10000);

        months = months / 100;

        int days = date - (year * 10000) - (months * 100);

        if (year == 0)
            return getLeadingZero(days, 2) + "/" + getLeadingZero(months, 2);
        else
            return getLeadingZero(days, 2) + "/" + getLeadingZero(months, 2)
                    + "/" + year;
    }

    public String getTimeString(int time)
    {
        int hours = time / 100;
        int min = time - hours * 100;

        return getLeadingZero(hours, 2) + ":" + getLeadingZero(min, 2);
    }

    public String getDateTimeString(long date)
    {
        return getDateTimeString(date, 1);
    }

    public String getDateTimeAsDateString(long date)
    {
        return getDateTimeString(date, 2);
    }

    public String getDateTimeAsTimeString(long date)
    {
        return getDateTimeString(date, 3);
    }

    // ret_type = 1 (Date and time)
    // ret_type = 2 (Date)
    // ret_type = 3 (Time)

    public static final int DT_DATETIME = 1;

    public static final int DT_DATE = 2;

    public static final int DT_TIME = 3;

    public String getDateTimeString(long dt, int ret_type)
    {

        int y = (int) (dt / 100000000L);
        dt -= y * 100000000L;

        int m = (int) (dt / 1000000L);
        dt -= m * 1000000L;

        int d = (int) (dt / 10000L);
        dt -= d * 10000L;

        int h = (int) (dt / 100L);
        dt -= h * 100L;

        int min = (int) dt;

        if (ret_type == DT_DATETIME)
            return getLeadingZero(d, 2) + "." + getLeadingZero(m, 2) + "." + y
                    + "  " + getLeadingZero(h, 2) + ":"
                    + getLeadingZero(min, 2);
        else if (ret_type == DT_DATE)
            return getLeadingZero(d, 2) + "." + getLeadingZero(m, 2) + "." + y;
        else
            return getLeadingZero(h, 2) + ":" + getLeadingZero(min, 2);

    }

    public Date getDateTimeAsDateObject(long dt)
    {

        //Date dt_obj = new Date();
        GregorianCalendar gc = new GregorianCalendar();

        int y = (int) (dt / 100000000L);
        dt -= y * 100000000L;

        int m = (int) (dt / 1000000L);
        dt -= m * 1000000L;

        int d = (int) (dt / 10000L);
        dt -= d * 10000L;

        int h = (int) (dt / 100L);
        dt -= h * 100L;

        int min = (int) dt;

        gc.set(Calendar.DATE, d);
        gc.set(Calendar.MONTH, m - 1);
        gc.set(Calendar.YEAR, y);
        gc.set(Calendar.HOUR_OF_DAY, h);
        gc.set(Calendar.MINUTE, min);

        /*
         dt_obj.setHours(h);
         dt_obj.setMinutes(min);

         dt_obj.setDate(d);
         dt_obj.setMonth(m);
         dt_obj.setYear(y);

         return dt_obj;
         */

        return gc.getTime();

    }

    public long getDateTimeLong(long dt, int ret_type)
    {

        int y = (int) (dt / 100000000L);
        dt -= y * 100000000L;

        int m = (int) (dt / 1000000L);
        dt -= m * 1000000L;

        int d = (int) (dt / 10000L);
        dt -= d * 10000L;

        int h = (int) (dt / 100L);
        dt -= h * 100L;

        int min = (int) dt;

        if (ret_type == DT_DATETIME)
        {
            return Integer.parseInt(y + getLeadingZero(m, 2)
                    + getLeadingZero(d, 2) + getLeadingZero(h, 2)
                    + getLeadingZero(min, 2));
        } else if (ret_type == DT_DATE)
        {
            return Integer.parseInt(getLeadingZero(d, 2) + getLeadingZero(m, 2)
                    + y);
        } else
            return Integer.parseInt(getLeadingZero(h, 2)
                    + getLeadingZero(min, 2));

    }

    public long getDateTimeFromDateObject(Date dt)
    {

        GregorianCalendar gc = new GregorianCalendar();
        gc.setTime(dt);

        String dx = "";

        dx += "" + gc.get(Calendar.YEAR);
        dx += "" + getLeadingZero(gc.get(Calendar.MONTH + 1), 2);
        dx += "" + getLeadingZero(gc.get(Calendar.DAY_OF_MONTH), 2);
        dx += "" + getLeadingZero(gc.get(Calendar.HOUR_OF_DAY), 2);
        dx += "" + getLeadingZero(gc.get(Calendar.MINUTE), 2);

        return Long.parseLong(dx);

    }

    // 1 = Db Date: yyyyMMdd
    // 2 = Db Full: yyyyMMddHHMM (24h format)
    public String getDateTimeStringFromGregorianCalendar(GregorianCalendar gc,
            int type)
    {
        String st = "";

        if (gc.get(Calendar.YEAR) < 1000)
        {
            st += gc.get(Calendar.YEAR) + 1900;
        } else
        {
            st += gc.get(Calendar.YEAR);
        }

        st += getLeadingZero(gc.get(Calendar.MONTH) + 1, 2);
        st += getLeadingZero(gc.get(Calendar.DAY_OF_MONTH), 2);

        if (type == 2)
        {
            st += getLeadingZero(gc.get(Calendar.HOUR_OF_DAY), 2);
            st += getLeadingZero(gc.get(Calendar.MINUTE), 2);
        }

        //System.out.println(st);

        return st;
    }

    public String getDateTimeString(int date, int time)
    {
        return getDateString(date) + " " + getTimeString(time);
    }

    public String getLeadingZero(int number, int places)
    {
        String nn = "" + number;

        while (nn.length() < places)
        {
            nn = "0" + nn;
        }

        return nn;
    }

    public String getLeadingZero(String number, int places)
    {
        number = number.trim();

        while (number.length() < places)
        {
            number = "0" + number;
        }

        return number;
    }

    public int getStartYear()
    {
        return 1800;
    }

    public float getFloatValue(Object aValue)
    {
        float out = 0.0f;

        if (aValue == null)
            return out;

        if (aValue instanceof Float)
        {
            try
            {
                Float f = (Float) aValue;
                out = f.floatValue();
            } catch (Exception ex)
            {
            }
        } else if (aValue instanceof String)
        {
            String s = (String) aValue;
            if (s.length() > 0)
            {
                try
                {
                    out = Float.parseFloat(s);
                } catch (Exception ex)
                {
                }
            }
        }

        return out;
    }

    public int getIntValue(Object aValue)
    {
        int out = 0;

        if (aValue == null)
            return out;

        if (aValue instanceof Integer)
        {
            try
            {
                Integer i = (Integer) aValue;
                out = i.intValue();
            } catch (Exception ex)
            {
            }
        } else if (aValue instanceof String)
        {
            String s = (String) aValue;
            if (s.length() > 0)
            {
                try
                {
                    out = Integer.parseInt(s);
                } catch (Exception ex)
                {
                }
            }
        }

        return out;
    }

    public long getLongValue(Object aValue)
    {
        long out = 0L;

        if (aValue == null)
            return out;

        if (aValue instanceof Long)
        {
            try
            {
                Long i = (Long) aValue;
                out = i.longValue();
            } catch (Exception ex)
            {
            }
        } else if (aValue instanceof String)
        {
            String s = (String) aValue;
            if (s.length() > 0)
            {
                try
                {
                    out = Long.parseLong(s);
                } catch (Exception ex)
                {
                }
            }
        }

        return out;
    }





    public GregorianCalendar getGregorianCalendar(Date date)
    {
        GregorianCalendar gc = new GregorianCalendar();
        gc.setTime(date);

        return gc;
    }

    public static void notImplemented(String source)
    {
        System.out.println("Not Implemented: " + source);
    }




}
