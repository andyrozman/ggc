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
 *  Filename: I18nControl
 *  Purpose:  Used for internationalization
 *
 *  Author:   andyrozman  {andy@atech-software.com}
 */


package ggc.core.util;


import java.io.FileInputStream;
import java.util.Properties;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory; 

import com.atech.i18n.I18nControlAbstract;

public class NutriI18nControl extends I18nControlAbstract
{

    private static Log s_logger = LogFactory.getLog(NutriI18nControl.class); 
    
    


    static NutriI18nControl s_i18n = null;   // This is handle to unique 
                                                    // singelton instance

/*
    private final static Locale defaultLocale = Locale.ENGLISH;

    private String selected_language = "en";
*/
    //   Constructor:  I18nControl
    /**
     *
     *  This is I18nControl constructor; Since classes use Singleton Pattern,
     *  constructor is protected and can be accessed only with getInstance() 
     *  method.<br><br>
     *
     */ 
    private NutriI18nControl()
    {
        init();
        getSelectedLanguage();
        setLanguage();
    } 


    public void init()
    {
	def_language = "en";
	lang_file_root = "GGC_Nutrition";
    }


    private void getSelectedLanguage()
    {

    	try
    	{
    	    Properties props = new Properties();
    
    	    FileInputStream in = new FileInputStream("../data/GGC_Config.properties");
    	    props.load(in);
    
    	    String tempLang = (String)props.get("SELECTED_LANG");
    
    	    if (tempLang != null)
    		this.selected_language = tempLang;
    	}
    	catch(Exception ex)
    	{
    	    System.out.println("I18nControl: Configuration file not found. Using default langauge ('en')");
    	    s_logger.warn("Configuration file not found. Using default langauge ('en')");
    	}
        
    }

    
    //  Method:       getInstance
    //  Author:       Andy
    /**
     *
     *  This method returns reference to I18nControl object created, or if no 
     *  object was created yet, it creates one.<br><br>
     *
     *  @return Reference to I18nControl object
     * 
     */ 
    public static NutriI18nControl getInstance()
    {
        if (s_i18n == null)
            s_i18n = new NutriI18nControl();
        return s_i18n;
    }

    //  Method:       deleteInstance
    /**
     *
     *  This method sets handle to I18NControl to null and deletes the instance. <br><br>
     *
     */ 
    public static void deleteInstance()
    {
        s_i18n = null;
    }


    /**
     * This method sets the language according to the preferences.<br>
     */
    public void setLanguage() 
    {
        if (selected_language!=null)
            setLanguage(selected_language);
        else
            setLanguage(def_language);
    }


    public String getPartitialTranslation()
    {
	return null;
    }
    
    
    

}
