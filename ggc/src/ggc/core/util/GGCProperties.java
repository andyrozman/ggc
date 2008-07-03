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
 *  Author:   andyrozman  {andy@atech-software.com}
 *
 */

package ggc.core.util;

import ggc.core.data.cfg.ConfigurationManager;
import ggc.core.db.hibernate.ColorSchemeH;
import ggc.core.db.tool.DbToolApplicationGGC;

import java.awt.Color;
import java.util.Hashtable;

public class GGCProperties //extends GGCPropertiesHelper 
{
    //private static GGCProperties singleton = null;
    //private SettingsMainH m_settings = null;
    private ColorSchemeH m_colors = null;
    private DataAccess m_da = null;
    private Hashtable<String, ColorSchemeH> m_color_schemes;

    DbToolApplicationGGC m_config = null;

    //private boolean changed_db = false;
    private boolean changed_config = false;
    //private boolean changed_scheme = false;

    private ConfigurationManager m_cfg_mgr = null;

    public GGCProperties(DataAccess da, DbToolApplicationGGC config, ConfigurationManager cfg_mgr) 
    {
        this.m_da = da;
        this.m_config = config;
	this.m_cfg_mgr = cfg_mgr;
        this.m_color_schemes = new Hashtable<String, ColorSchemeH>();
/*        this.m_settings = new SettingsMainH(I18nControl.getInstance().getMessage("UNNAMED_USER"), 
                       "Insulin 1", "Ins1", "Insulin 2", "Ins2", 0, "No port available", 
                                            2, 60.0f, 200.0f, 80.0f, 120.0f, 
                                            3.0f, 20.0f, 4.4f, 14.0f,
                                            2, "blueMetalthemepack.zip",
                                            0, 0, 0, 0, 0, 0, 0, 
                                            "", 1100, 1800, 2100, "", "Default Scheme");
*/
            //public SettingsMainH(String name, String ins1_name, String ins1_abbr, String ins2_name, String ins2_abbr, int meter_type, String meter_port, int bg_unit, float bg1_low, float bg1_high, float bg1_target_low, float bg1_target_high, float bg2_low, float bg2_high, float bg2_target_low, float bg2_target_high, int laf_type, String laf_name, int render_rendering, int render_dithering, int render_interpolation, int render_antialiasing, int render_textantialiasing, int render_colorrendering, int render_fractionalmetrics, String print_pdf_viewer_path, int print_lunch_start_time, int print_dinner_start_time, int print_night_start_time, String print_empty_value, String color_scheme) {

        this.m_colors = new ColorSchemeH(
        "Default Scheme", 0, -65485, -6750208, -163654, -81409, -1184275, -16724788, 
        -6710785, -16776961, -6711040, -16724941);

    }



    public void setColorSchemeObject(String name)
    {
        ColorSchemeH cs = this.m_color_schemes.get(name);

        if (!cs.equals(m_colors)) 
        {
	    this.setSelectedColorSchemeInCfg(name);
            this.m_colors = this.m_color_schemes.get(name);
        }
    }

    public void setColorSchemes(Hashtable<String, ColorSchemeH> table, boolean isnew)
    {
        this.m_color_schemes = table;
        //this.changed_scheme = isnew;
    }

    public Hashtable<String, ColorSchemeH> getColorSchemes()
    {
        return this.m_color_schemes;
    }

    // ---
    // ---  General Data
    // ---

    public String getUserName() 
    {
	return this.m_cfg_mgr.getStringValue("NAME");
    }

    public void setUserName(String value)
    {
	this.m_cfg_mgr.setStringValue("NAME", value);
    }


    // ---
    // ---  Medical Data (Insulins and BG)
    // ---

    public String getIns1Name() 
    {
	return this.m_cfg_mgr.getStringValue("INS1_NAME");
    }

    public void setIns1Name(String value)
    {
	this.m_cfg_mgr.setStringValue("INS1_NAME", value);
    }

    public String getIns2Name() 
    {
	return this.m_cfg_mgr.getStringValue("INS2_NAME");
    }

    public void setIns2Name(String value)
    {
	this.m_cfg_mgr.setStringValue("INS2_NAME", value);
    }


    public String getIns1Abbr() 
    {
	return this.m_cfg_mgr.getStringValue("INS1_ABBR");
    }


    public void setIns1Abbr(String value)
    {
	this.m_cfg_mgr.setStringValue("INS1_ABBR", value);
    }



    public String getIns2Abbr() 
    {
	return this.m_cfg_mgr.getStringValue("INS2_ABBR");
    }

    public void setIns2Abbr(String value)
    {
	this.m_cfg_mgr.setStringValue("INS2_ABBR", value);
    }


    // BG settings

    public int getBG_unit()
    {
	return this.m_cfg_mgr.getIntValue("BG_UNIT");
    }


    public void setBG_unit(int bgunit)
    {
	this.m_cfg_mgr.setIntValue("BG_UNIT", bgunit);
    }


    public String getBG_unitString()
    {
	int unit = getBG_unit();

	if (unit==1) 
	    return "mg/dl";
	else if (unit==2) 
	    return "mmol/l";
	else
	    return m_da.getI18nControlInstance().getMessage("UNKNOWN");

    }




    public float getBG_High() 
    {
	if (this.getBG_unit()==1)
	    return this.getBG1_High();
	else
	    return this.getBG2_High();
    }


    public float getBG1_High() 
    {
	return this.m_cfg_mgr.getFloatValue("BG1_HIGH");
    }

    public void setBG1_High(float value)
    {
	this.m_cfg_mgr.setFloatValue("BG1_HIGH", value);
    }


    public float getBG2_High() 
    {
	return this.m_cfg_mgr.getFloatValue("BG2_HIGH");
    }

    public void setBG2_High(float value)
    {
	this.m_cfg_mgr.setFloatValue("BG2_HIGH", value);
    }


    public float getBG_Low() 
    {
	if (this.getBG_unit()==1)
	    return this.getBG1_Low();
	else
	    return this.getBG2_Low();
    }


    public float getBG1_Low() 
    {
	return this.m_cfg_mgr.getFloatValue("BG1_LOW");
    }

    public void setBG1_Low(float value)
    {
	this.m_cfg_mgr.setFloatValue("BG1_LOW", value);
    }


    public float getBG2_Low() 
    {
	return this.m_cfg_mgr.getFloatValue("BG2_LOW");
    }

    public void setBG2_Low(float value)
    {
	this.m_cfg_mgr.setFloatValue("BG2_LOW", value);
    }


    public float getBG_TargetHigh() 
    {
	if (this.getBG_unit()==1)
	    return this.getBG1_TargetHigh();
	else
	    return this.getBG2_TargetHigh();
    }

    public float getBG1_TargetHigh() 
    {
	return this.m_cfg_mgr.getFloatValue("BG1_TARGET_HIGH");
    }


    public void setBG1_TargetHigh(float value)
    {
	this.m_cfg_mgr.setFloatValue("BG1_TARGET_HIGH", value);
    }


    public float getBG2_TargetHigh() 
    {
	return this.m_cfg_mgr.getFloatValue("BG2_TARGET_HIGH");
    }

    public void setBG2_TargetHigh(float value)
    {
	this.m_cfg_mgr.setFloatValue("BG2_TARGET_HIGH", value);
    }


    public float getBG_TargetLow() 
    {
	if (this.getBG_unit()==1)
	    return this.getBG1_TargetLow();
	else
	    return this.getBG2_TargetLow();
    }

    public float getBG1_TargetLow() 
    {
	return this.m_cfg_mgr.getFloatValue("BG1_TARGET_LOW");
    }


    public void setBG1_TargetLow(float value)
    {
	this.m_cfg_mgr.setFloatValue("BG1_TARGET_LOW", value);
    }


    public float getBG2_TargetLow() 
    {
	return this.m_cfg_mgr.getFloatValue("BG2_TARGET_LOW");
    }


    public void setBG2_TargetLow(float value)
    {
	this.m_cfg_mgr.setFloatValue("BG2_TARGET_LOW", value);
    }







    //
    // rendering stuff
    //

    public int getRendering() 
    {
	return this.m_cfg_mgr.getIntValue("RENDER_RENDERING");
    }

    public void setRendering(int value) 
    {
	this.m_cfg_mgr.setIntValue("RENDER_RENDERING", value);
    }

    public int getAntiAliasing() 
    {
	return this.m_cfg_mgr.getIntValue("RENDER_ANTIALIASING");
    }

    public void setAntiAliasing(int value) 
    {
	this.m_cfg_mgr.setIntValue("RENDER_ANTIALIASING", value);
    }

    
    public int getColorRendering() 
    {
	return this.m_cfg_mgr.getIntValue("RENDER_COLOR_RENDERING");
    }

    public void setColorRendering(int value) 
    {
	this.m_cfg_mgr.setIntValue("RENDER_COLOR_RENDERING", value);
    }
    
    public int getDithering() 
    {
	return this.m_cfg_mgr.getIntValue("RENDER_DITHERING");
    }

    public void setDithering(int value) 
    {
	this.m_cfg_mgr.setIntValue("RENDER_DITHERING", value);
    }
    
    public int getFractionalMetrics() 
    {
	return this.m_cfg_mgr.getIntValue("RENDER_FRACTIONAL_METRICS");
    }

    public void setFractionalMetrics(int value) 
    {
	this.m_cfg_mgr.setIntValue("RENDER_FRACTIONAL_METRICS", value);
    }

    
    public int getInterpolation() 
    {
	return this.m_cfg_mgr.getIntValue("RENDER_INTERPOLATION");
    }

    public void setInterpolation(int value) 
    {
	this.m_cfg_mgr.setIntValue("RENDER_INTERPOLATION", value);
    }

    
    public int getTextAntiAliasing() 
    {
	return this.m_cfg_mgr.getIntValue("RENDER_TEXT_ANTIALIASING");
    }

    public void setTextAntiAliasing(int value) 
    {
	this.m_cfg_mgr.setIntValue("RENDER_TEXT_ANTIALIASING", value);
    }
    

    // colors

    public ColorSchemeH getSelectedColorScheme() 
    {
        return this.m_colors;
    }

    /*
    public ColorSchemeH setSelectedColorScheme() 
    {
	return this.m_colors;
    }
    */

    public String getSelectedColorSchemeInCfg() 
    {
        return this.m_cfg_mgr.getStringValue("SELECTED_COLOR_SCHEME");
    }

    public void setSelectedColorSchemeInCfg(String value) 
    {
        this.m_cfg_mgr.setStringValue("SELECTED_COLOR_SCHEME", value);
    }




    public Color getColorTargetBG() 
    {
        return getColor(this.m_colors.getColor_bg_target());
    }

    public Color getColorBG() 
    {
        return getColor(this.m_colors.getColor_bg());
    }

    /*
    public Color getColorByName(String identifier) 
    {
        return getColor(identifier);
    }*/

    public Color getColorHighBG() 
    {
	return getColor(this.m_colors.getColor_bg_high());
    }

    public Color getColorLowBG() 
    {
	return getColor(this.m_colors.getColor_bg_low());
    }

    public Color getColorAvgBG() 
    {
	return getColor(this.m_colors.getColor_bg_avg());
    }

    public Color getColorBU() 
    {
	return getColor(this.m_colors.getColor_ch());
    }

    public Color getColorIns1() 
    {
	return getColor(this.m_colors.getColor_ins1());
    }

    public Color getColorIns2() 
    {
	return getColor(this.m_colors.getColor_ins2());
    }

    public Color getColorIns() 
    {
	return getColor(this.m_colors.getColor_ins());
    }

    public Color getColorInsPerBU() 
    {
	return getColor(this.m_colors.getColor_ins_perbu());
    }


    // meter

    public int getMeterType() 
    {
	return this.m_cfg_mgr.getIntValue("METER_TYPE");
    }

    public void setMeterType(int value) 
    {
	this.m_cfg_mgr.setIntValue("METER_TYPE", value);
    }

    public String getMeterTypeString() 
    {
	    return m_da.getI18nControlInstance().getMessage("NONE");
/*
	if (this.getMeterType()==-1)
	{
	    return m_da.getI18nInstance().getMessage("NONE");
	}
	else
            return m_da.getMeterManager().meter_names[this.getMeterType()];
            */
    }


    public String getMeterPort() 
    {
	return this.m_cfg_mgr.getStringValue("METER_PORT");
    }


    public void setMeterPort(String value) 
    {
	this.m_cfg_mgr.setStringValue("METER_PORT", value);
    }


    public boolean getMeterDaylightSavingsFix()
    {
        return this.m_cfg_mgr.getBooleanValue("METER_DAYLIGHTSAVING_TIME_FIX");
    }

    public void setMeterDaylightSavingsFix(boolean value)
    {
        this.m_cfg_mgr.setBooleanValue("METER_DAYLIGHTSAVING_TIME_FIX", value);
    }

    public String getTimeZone()
    {
        return this.m_cfg_mgr.getStringValue("TIMEZONE");
    }
    
    public void setTimeZone(String value)
    {
        this.m_cfg_mgr.setStringValue("TIMEZONE", value);
    }


    // ---
    // ---  Language methods
    // ---


    public String getLanguage() 
    {
        return this.m_config.selected_lang;
    }

    public void setLanguage(String name)
    {
        int idx = m_da.getLanguageIndexByName(name);
        String post = m_da.avLangPostfix[idx];

        if (!this.m_config.selected_lang.equals(post)) 
        {
            this.m_config.selected_lang = post;
            this.changed_config = true;
        }
    }


    // ---
    // ---  Printing methods
    // ---


    public String getPdfVieverPath() 
    {
	return this.m_cfg_mgr.getStringValue("PRINT_PDF_VIEWER_PATH");
    }

    public void setPdfVieverPath(String value) 
    {
	this.m_cfg_mgr.setStringValue("PRINT_PDF_VIEWER_PATH", value);
    }


    public String getPrintEmptyValue() 
    {
	return this.m_cfg_mgr.getStringValue("PRINT_EMPTY_VALUE");
    }

    public void setPrintEmptyValue(String value) 
    {
	this.m_cfg_mgr.setStringValue("PRINT_EMPTY_VALUE", value);
    }


    public int getPrintLunchStartTime() 
    {
	return this.m_cfg_mgr.getIntValue("PRINT_LUNCH_START_TIME");
    }

    public void setPrintLunchStartTime(int value) 
    {
	this.m_cfg_mgr.setIntValue("PRINT_LUNCH_START_TIME", value);
    }


    public int getPrintDinnerStartTime() 
    {
	return this.m_cfg_mgr.getIntValue("PRINT_DINNER_START_TIME");
    }

    public void setPrintDinnerStartTime(int value) 
    {
	this.m_cfg_mgr.setIntValue("PRINT_DINNER_START_TIME", value);
    }
    

    public int getPrintNightStartTime() 
    {
	return this.m_cfg_mgr.getIntValue("PRINT_NIGHT_START_TIME");
    }

    public void setPrintNightStartTime(int value) 
    {
	this.m_cfg_mgr.setIntValue("PRINT_NIGHT_START_TIME", value);
    }


    // ---
    // ---  Utility methods
    // ---

    
    public Color getColor(int key)
    {
	return new Color(key);
    }


    // ---
    // ---  Load/Save methods
    // ---


    public void load()
    {
	m_da.getDb().loadConfigData();
	m_da.setBGMeasurmentType(this.getBG_unit());
	this.setColorSchemeObject(this.m_cfg_mgr.getStringValue("SELECTED_COLOR_SCHEME"));
    }

    public void reload()
    {
	load();
    }

    public void save()
    {
	//System.out.println("save");

	// fix
	/*if (changed_scheme) 
	{
	    DataAccess.notImplemented("GGCProperties:save():: changed scheme");
	    System.out.println("save Scheme");
	    //m_da.m_db.s
	}*/

	this.m_cfg_mgr.saveConfig();

	if ((changed_config) || (this.m_config.hasChanged()))
	{
	    //System.out.println("save Config REAL");
	    this.m_config.saveConfig();
	}

	m_da.setBGMeasurmentType(this.getBG_unit());

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