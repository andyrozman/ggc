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

package ggc.util;

import java.awt.Color;
import java.util.Hashtable;


import ggc.db.tool.DbToolApplicationGGC;
import ggc.db.hibernate.ColorSchemeH;
import ggc.db.hibernate.SettingsMainH;

public class GGCProperties //extends GGCPropertiesHelper 
{
    //private static GGCProperties singleton = null;
    private SettingsMainH m_settings = null;
    private ColorSchemeH m_colors = null;
    private DataAccess m_da = null;
    private Hashtable<String, ColorSchemeH> m_color_schemes;

    DbToolApplicationGGC m_config = null;

    private boolean changed_db = false;
    private boolean changed_config = false;
    private boolean changed_scheme = false;


    public GGCProperties(DataAccess da, DbToolApplicationGGC config) 
    {
        this.m_da = da;
        this.m_config = config;
        this.m_color_schemes = new Hashtable<String, ColorSchemeH>();
        this.m_settings = new SettingsMainH(I18nControl.getInstance().getMessage("UNNAMED_USER"), 
                       "Insulin 1", "Ins1", "Insulin 2", "Ins2", 0, "No port available", 
                                            2, 60.0f, 200.0f, 80.0f, 120.0f, 
                                            3.0f, 20.0f, 4.4f, 14.0f,
                                            2, "blueMetalthemepack.zip",
                                            0, 0, 0, 0, 0, 0, 0, 
                                            "", 1100, 1800, 2100, "", "Default Scheme");

            //public SettingsMainH(String name, String ins1_name, String ins1_abbr, String ins2_name, String ins2_abbr, int meter_type, String meter_port, int bg_unit, float bg1_low, float bg1_high, float bg1_target_low, float bg1_target_high, float bg2_low, float bg2_high, float bg2_target_low, float bg2_target_high, int laf_type, String laf_name, int render_rendering, int render_dithering, int render_interpolation, int render_antialiasing, int render_textantialiasing, int render_colorrendering, int render_fractionalmetrics, String print_pdf_viewer_path, int print_lunch_start_time, int print_dinner_start_time, int print_night_start_time, String print_empty_value, String color_scheme) {

        this.m_colors = new ColorSchemeH(
        "Default Scheme", 0, -65485, -6750208, -163654, -81409, -1184275, -16724788, 
        -6710785, -16776961, -6711040, -16724941);

    }



    public void setSettings(SettingsMainH settings)
    {
        this.m_settings = settings;
    }

    public SettingsMainH getSettings()
    {
        return this.m_settings;
    }

    public void setColorSchemeObject(String name)
    {
        ColorSchemeH cs = this.m_color_schemes.get(name);

        if (!cs.equals(m_colors)) 
        {
            this.m_colors = this.m_color_schemes.get(name);
            this.m_settings.setColor_scheme(name);
            this.changed_db = true;
        }
    }

    public void setColorSchemes(Hashtable<String, ColorSchemeH> table, boolean isnew)
    {
        this.m_color_schemes = table;
        this.changed_scheme = isnew;
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
        return this.m_settings.getName();
    }

    public void setUserName(String value)
    {
        if (!this.m_settings.getName().equals(value)) 
        {
            this.m_settings.setName(value);
            changed_db = true;
        }
    }


    // ---
    // ---  Medical Data (Insulins and BG)
    // ---

    public String getIns1Name() 
    {
        return this.m_settings.getIns1_name();
    }

    public void setIns1Name(String value)
    {
        if (!this.m_settings.getIns1_name().equals(value)) 
        {
            this.m_settings.setIns1_name(value);
            changed_db = true;
        }
    }

    public String getIns2Name() 
    {
        return this.m_settings.getIns2_name();
    }

    public void setIns2Name(String value)
    {
        if (!this.m_settings.getIns2_name().equals(value)) 
        {
            this.m_settings.setIns2_name(value);
            changed_db = true;
        }
    }


    public String getIns1Abbr() 
    {
	return this.m_settings.getIns1_abbr();
    }


    public void setIns1Abbr(String value)
    {
        if (!this.m_settings.getIns1_abbr().equals(value)) 
        {
            this.m_settings.setIns1_abbr(value);
            changed_db = true;
        }
    }



    public String getIns2Abbr() 
    {
	return this.m_settings.getIns2_abbr();
    }

    public void setIns2Abbr(String value)
    {
	if (!this.m_settings.getIns2_abbr().equals(value)) 
	{
	    this.m_settings.setIns2_abbr(value);
	    changed_db = true;
	}
    }


    // BG settings

    public int getBG_unit()
    {
	return this.m_settings.getBg_unit();
    }


    public void setBG_unit(int bgunit)
    {
        if (this.m_settings.getBg_unit()!= bgunit) 
        {
            this.m_settings.setBg_unit(bgunit);
            changed_db = true;
        }
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




    public float getBG_High() 
    {
	if (this.m_settings.getBg_unit()==1)
	    return this.m_settings.getBg1_high();
	else
	    return this.m_settings.getBg2_high();
    }


    public float getBG1_High() 
    {
	return this.m_settings.getBg1_high();
    }

    public void setBG1_High(float value)
    {
	if (this.m_settings.getBg1_high()!= value) 
	{
	    this.m_settings.setBg1_high(value);
	    changed_db = true;
	}
    }


    public float getBG2_High() 
    {
	return this.m_settings.getBg2_high();
    }

    public void setBG2_High(float value)
    {
	if (this.m_settings.getBg2_high()!= value) 
	{
	    this.m_settings.setBg2_high(value);
	    changed_db = true;
	}
    }


    public float getBG_Low() 
    {
	if (this.m_settings.getBg_unit()==1)
	    return this.m_settings.getBg1_low();
	else
	    return this.m_settings.getBg2_low();
    }


    public float getBG1_Low() 
    {
	return this.m_settings.getBg1_low();
    }

    public void setBG1_Low(float value)
    {
	if (this.m_settings.getBg1_low()!= value) 
	{
	    this.m_settings.setBg1_low(value);
	    changed_db = true;
	}
    }


    public float getBG2_Low() 
    {
	return this.m_settings.getBg2_low();
    }

    public void setBG2_Low(float value)
    {
	if (this.m_settings.getBg2_low()!= value) 
	{
	    this.m_settings.setBg2_low(value);
	    changed_db = true;
	}
    }


    public float getBG_TargetHigh() 
    {
	if (this.m_settings.getBg_unit()==1)
	    return this.m_settings.getBg1_target_high();
	else
	    return this.m_settings.getBg2_target_high();
    }

    public float getBG1_TargetHigh() 
    {
	return this.m_settings.getBg1_target_high();
    }


    public void setBG1_TargetHigh(float value)
    {
	if (this.m_settings.getBg1_target_high()!= value) 
	{
	    this.m_settings.setBg1_target_high(value);
	    changed_db = true;
	}
    }


    public float getBG2_TargetHigh() 
    {
	return this.m_settings.getBg2_target_high();
    }

    public void setBG2_TargetHigh(float value)
    {
	if (this.m_settings.getBg2_target_high()!= value) 
	{
	    this.m_settings.setBg2_target_high(value);
	    changed_db = true;
	}
    }


    public float getBG_TargetLow() 
    {
	if (this.m_settings.getBg_unit()==1)
	    return this.m_settings.getBg1_target_low();
	else
	    return this.m_settings.getBg2_target_low();
    }

    public float getBG1_TargetLow() 
    {
	return this.m_settings.getBg1_target_low();
    }


    public void setBG1_TargetLow(float value)
    {
	if (this.m_settings.getBg1_target_low()!= value) 
	{
	    this.m_settings.setBg1_target_low(value);
	    changed_db = true;
	}
    }


    public float getBG2_TargetLow() 
    {
	return this.m_settings.getBg2_target_low();
    }


    public void setBG2_TargetLow(float value)
    {
	if (this.m_settings.getBg2_target_low()!= value) 
	{
	    this.m_settings.setBg2_target_low(value);
	    changed_db = true;
	}
    }







    //
    // rendering stuff
    //

    public int getRendering() 
    {
        return this.m_settings.getRender_rendering();
    }

    public void setRendering(int value) 
    {
        if (this.m_settings.getRender_rendering()!=value) 
        {
            this.m_settings.setRender_rendering(value);
            changed_db = true;
        }
    }

    public int getAntiAliasing() 
    {
        return this.m_settings.getRender_antialiasing();
    }

    public void setAntiAliasing(int value) 
    {
        if (this.m_settings.getRender_antialiasing()!=value) 
        {
            this.m_settings.setRender_antialiasing(value);
            changed_db = true;
        }
    }

    
    public int getColorRendering() 
    {
        return this.m_settings.getRender_colorrendering();
    }

    public void setColorRendering(int value) 
    {
        if (this.m_settings.getRender_colorrendering()!=value) 
        {
            this.m_settings.setRender_colorrendering(value);
            changed_db = true;
        }
    }
    
    public int getDithering() 
    {
        return this.m_settings.getRender_dithering();
    }

    public void setDithering(int value) 
    {
        if (this.m_settings.getRender_dithering()!=value) 
        {
            this.m_settings.setRender_dithering(value);
            changed_db = true;
        }
    }
    
    public int getFractionalMetrics() 
    {
        return this.m_settings.getRender_fractionalmetrics();
    }

    public void setFractionalMetrics(int value) 
    {
        if (this.m_settings.getRender_fractionalmetrics()!=value) 
        {
            this.m_settings.setRender_fractionalmetrics(value);
            changed_db = true;
        }
    }

    
    public int getInterpolation() 
    {
        return this.m_settings.getRender_interpolation();
    }

    public void setInterpolation(int value) 
    {
        if (this.m_settings.getRender_interpolation()!=value) 
        {
            this.m_settings.setRender_interpolation(value);
            changed_db = true;
        }
    }

    
    public int getTextAntiAliasing() 
    {
        return this.m_settings.getRender_textantialiasing();
    }

    public void setTextAntiAliasing(int value) 
    {
        if (this.m_settings.getRender_textantialiasing()!=value) 
        {
            this.m_settings.setRender_textantialiasing(value);
            changed_db = true;
        }
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
	return this.m_settings.getMeter_type();
    }

    public void setMeterType(int value) 
    {
	if (this.m_settings.getMeter_type()!= value) 
	{
	    this.m_settings.setMeter_type(value);
	    changed_db = true;
	}
    }

    public String getMeterTypeString() 
    {
	return m_da.getMeterManager().meter_names[this.m_settings.getMeter_type()];
    }


    public String getMeterPort() 
    {
	return this.m_settings.getMeter_port();
    }


    public void setMeterPort(String value) 
    {
	if (!this.m_settings.getMeter_port().equals(value)) 
	{
	    this.m_settings.setMeter_port(value);
	    changed_db = true;
	}
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


    public void Tt()
    {
        //this.m_settings.
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
    }

    public void reload()
    {
	load();
    }

    public void save()
    {
	System.out.println("save");

	// fix
	if (changed_scheme) 
	{
	    System.out.println("save Scheme");
	    //m_da.m_db.s
	}

	if (changed_db) 
	{
	    System.out.println("save Db");
        try
        {
            m_da.m_db.saveConfigData();
        }
        catch (NullPointerException e)
        {
            System.err.println(I18nControl.getInstance().getMessage("NO_DATABASE_FOUND"));
        }
	}

	if (changed_config) 
	{
	    System.out.println("save Config");
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
