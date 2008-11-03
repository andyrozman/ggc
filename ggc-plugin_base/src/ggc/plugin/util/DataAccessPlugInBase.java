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

import ggc.plugin.list.BaseListEntry;

import java.awt.Color;
import java.awt.Font;
import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Hashtable;
import java.util.Vector;

import javax.swing.ImageIcon;
import javax.swing.JFrame;

import com.atech.db.hibernate.HibernateDb;
import com.atech.graphics.components.about.CreditsGroup;
import com.atech.graphics.components.about.FeaturesGroup;
import com.atech.graphics.components.about.LibraryInfoEntry;
import com.atech.i18n.I18nControlAbstract;
import com.atech.utils.ATDataAccessAbstract;


public abstract class DataAccessPlugInBase extends ATDataAccessAbstract
{

    public String web_server_port = "444";

    public static final String pathPrefix = ".";
    public Font fonts[] = null;



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

    
    
    // ********************************************************
    // ******             Init Methods                    *****    
    // ********************************************************
    


    public abstract String getApplicationName();
    
    
    
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
    // ******            About Methods                    *****    
    // ********************************************************
    
    
    public abstract void createPlugInAboutContext();
    
    protected String about_title;
    protected String about_image_name;
    protected String about_plugin_name;
    protected int about_plugin_copyright_from;
    protected ArrayList<LibraryInfoEntry> plugin_libraries;
    protected ArrayList<CreditsGroup> plugin_developers;
    protected ArrayList<FeaturesGroup> plugin_features; 
    
    public String getAboutTitle()
    {
        return this.about_title;
    }
    
    public String getAboutImageName()
    {
        return this.about_image_name;
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
    
    public String getAboutPlugInName()
    {
        return this.about_plugin_name;
    }
    
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

    public HibernateDb getHibernateDb()
    {
        return null;
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


    /*
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
/* xa
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
*/

    public int getStartYear()
    {
        return 1980;
    }







    public static void notImplemented(String source)
    {
        System.out.println("Not Implemented: " + source);
    }




}
