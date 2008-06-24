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
 *  Filename: GGCProperties.java
 *  Purpose:  Store all Properties for the GGC app.
 *
 *  Author:   schultd
 * 
 *  Author:   andyrozman (in Db)
 *
 */

package ggc.pump.util;


public class GGCProperties //extends GGCPropertiesHelper 
{
    //private static GGCProperties singleton = null;
    //private SettingsMainH m_settings = null;
//    private ColorSchemeH m_colors = null;
    private DataAccess m_da = null;

//    DbToolApplicationGGC m_config = null;

    //private boolean changed_db = false;
    //private boolean changed_config = false;
    //private boolean changed_scheme = false;


    public GGCProperties(DataAccess da)
    {
    	
    }
    
    /*
    public GGCProperties(DataAccess da, DbToolApplicationGGC config, ConfigurationManager cfg_mgr) 
    {
        this.m_da = da;
        this.m_config = config;
        this.m_cfg_mgr = cfg_mgr;

    }
*/




    // ---
    // ---  General Data
    // ---



    // ---
    // ---  Medical Data (Insulins and BG)
    // ---


    // BG settings

    public int getBG_unit()
    {
    	return 1;
	//return this.m_cfg_mgr.getIntValue("BG_UNIT");
    }


    public void setBG_unit(int bgunit)
    {
	//this.m_cfg_mgr.setIntValue("BG_UNIT", bgunit);
    }


    public String getBG_unitString()
    {
	int unit = getBG_unit();

	if (unit==1) 
	    return "mg/dl";
	else if (unit==2) 
	    return "mmol/l";
	else
	    return m_da.getI18nInstance().getMessage("UNKNOWN");

    }



















    //
    // rendering stuff
    //





    // meter

    public int getPumpType() 
    {
    	return 0;
	//return this.m_cfg_mgr.getIntValue("METER_TYPE");
    }

    public void setPumpType(int value) 
    {
	//this.m_cfg_mgr.setIntValue("METER_TYPE", value);
    }

    public String getPumpTypeString() 
    {
    	
	    return m_da.getI18nInstance().getMessage("NONE");
/*
	if (this.getPumpType()==-1)
	{
	    return m_da.getI18nInstance().getMessage("NONE");
	}
	else
            return m_da.getPumpManager().meter_names[this.getPumpType()];
            */
    }


    public String getPumpPort() 
    {
    	return "";
    	//return this.m_cfg_mgr.getStringValue("METER_PORT");
    }


    public void setPumpPort(String value) 
    {
    	//this.m_cfg_mgr.setStringValue("METER_PORT", value);
    }

/*
 * 
 * Daylight Savings Fix
 *     Winter time fix
 *     Summer time fix
 * 
 * 
 * 
 * 
 */
    
    
    public boolean getPumpDaylightSavingsFix()
    {
    	return false;
        //return this.m_cfg_mgr.getBooleanValue("METER_DAYLIGHTSAVING_TIME_FIX");
    }

    public void setPumpDaylightSavingsFix(boolean value)
    {
        //this.m_cfg_mgr.setBooleanValue("METER_DAYLIGHTSAVING_TIME_FIX", value);
    }

    public String getTimeZone()
    {
    	return "ss";
        //return this.m_cfg_mgr.getStringValue("TIMEZONE");
    }
    
    public void setTimeZone(String value)
    {
        //this.m_cfg_mgr.setStringValue("TIMEZONE", value);
    }


    // ---
    // ---  Language methods
    // ---


    public String getLanguage() 
    {
//        return this.m_config.selected_lang;
    	return null;
    }

    public void setLanguage(String name)
    {
    	/*
        int idx = m_da.getLanguageIndexByName(name);
        String post = m_da.avLangPostfix[idx];

        if (!this.m_config.selected_lang.equals(post)) 
        {
            this.m_config.selected_lang = post;
            this.changed_config = true;
        }
        */
    }



    // ---
    // ---  Utility methods
    // ---

    


    // ---
    // ---  Load/Save methods
    // ---


    public void load()
    {
    	/*
	m_da.getDb().loadConfigData();
	m_da.setBGMeasurmentType(this.getBG_unit());
	this.setColorSchemeObject(this.m_cfg_mgr.getStringValue("SELECTED_COLOR_SCHEME"));
	*/
    }

    public void reload()
    {
	//load();
    }

    public void save()
    {
	//System.out.println("save");

	// fix
	/* if (changed_scheme) 
	{
	    DataAccess.notImplemented("GGCProperties:save():: changed scheme");
	    System.out.println("save Scheme");
	    //m_da.m_db.s
	}*/

    	/*
	this.m_cfg_mgr.saveConfig();

	if ((changed_config) || (this.m_config.hasChanged()))
	{
	    //System.out.println("save Config REAL");
	    this.m_config.saveConfig();
	}

	m_da.setBGMeasurmentType(this.getBG_unit());
*/
    }

/*
    this.name = name;

    this.ins1_name = ins1_name;
    this.ins1_abbr = ins1_abbr;
    this.ins2_name = ins2_name;
    this.ins2_abbr = ins2_abbr;

    this.meter_type = meter_type;
    this.meter_port = meter_port;

    this.bg_unit = bg_unit;
    this.bg1_low = bg1_low;
    this.bg1_high = bg1_high;
    this.bg1_target_low = bg1_target_low;
    this.bg1_target_high = bg1_target_high;
    this.bg2_low = bg2_low;
    this.bg2_high = bg2_high;
    this.bg2_target_low = bg2_target_low;
    this.bg2_target_high = bg2_target_high;

    this.render_rendering = render_rendering;
    this.render_dithering = render_dithering;
    this.render_interpolation = render_interpolation;
    this.render_antialiasing = render_antialiasing;
    this.render_textantialiasing = render_textantialiasing;
    this.render_colorrendering = render_colorrendering;
    this.render_fractionalmetrics = render_fractionalmetrics;
    this.pdf_display_software_path = pdf_display_software_path;
    this.lunch_start_time = lunch_start_time;
    this.dinner_start_time = dinner_start_time;
    this.night_start_time = night_start_time;
    this.color_scheme = color_scheme;
*/




}
