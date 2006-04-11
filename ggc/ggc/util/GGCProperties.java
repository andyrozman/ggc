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
import ggc.db.hibernate.SettingsMainH;
import ggc.db.hibernate.ColorSchemeH;

public class GGCProperties //extends GGCPropertiesHelper 
{
    //private static GGCProperties singleton = null;
    private SettingsMainH m_settings = null;
    private ColorSchemeH m_colors = null;
    private DataAccess m_da = null;

    public GGCProperties(DataAccess da) 
    {
	m_da = da;
        this.m_settings = new SettingsMainH(I18nControl.getInstance().getMessage("UNNAMED_USER"), 
                       "Insulin 1", "Ins1", "Insulin 2", "Ins2", 0, "No port available", 
                       2, 60.0f, 200.0f, 80.0f, 120.0f, 
		       3.0f, 20.0f, 4.4f, 14.0f,
		       2, "blueMetalthemepack.zip",
                       0, 0, 0, 0, 0, 0, 0, 
		       "", 1100, 1800, 2100, 1);
/*
	public SettingsMainH(String name, String ins1_name, String ins1_abbr, 
			     String ins2_name, String ins2_abbr, int meter_type, 
			     String meter_port, int bg_unit, float bg1_low, 
			     float bg1_high, float bg1_target_low, float bg1_target_high, 
			     float bg2_low, float bg2_high, float bg2_target_low, 
			     float bg2_target_high, int laf_type, 
			     String laf_name, int render_rendering, 
			     int render_dithering, int render_interpolation, 
			     int render_antialiasing, int render_textantialiasing, 
			     int render_colorrendering, int render_fractionalmetrics, 
			     String pdf_display_software_path, int lunch_start_time, 
			     int dinner_start_time, int night_start_time, 
			     int color_scheme) {
*/

        this.m_colors = new ColorSchemeH(
        "Default Scheme", 0, -65485, -6750208, -163654, -81409, -1184275, -16724788, 
        -6710785, -16776961, -6711040, -16724941);
        

        /*
        setDefault("ColorTargetBG", "-1184275");
        setDefault("ColorBG", "-65485");
        setDefault("ColorAvgBG", "-6750208");
        setDefault("ColorHighBG", "-81409");
        setDefault("ColorLowBG", "-163654");
        setDefault("ColorBU", "-16724941");
        setDefault("ColorIns1", "-6710785");
        setDefault("ColorIns2", "-16776961");
        setDefault("ColorIns", "-16724788");
        setDefault("ColorInsPerBU", "-6711040");



        super.read();
        // setDefault("DBName", "glucodb");
        setDefault("UserName", I18nControl.getInstance().getMessage("UNNAMED_USER"));
        setDefault("Ins1Name", "Insulin 1");
        setDefault("Ins2Name", "Insulin 2");
        setDefault("Ins1Abbr", "Ins1");
        setDefault("Ins2Abbr", "Ins2");
        setDefault("HighBG", "200");
        setDefault("LowBG", "60");
        setDefault("TargetHighBG", "120");
        setDefault("TargetLowBG", "80");

        setDefault("Rendering", "0");
        setDefault("AntiAliasing", "0");
        setDefault("ColorRendering", "0");
        setDefault("Dithering", "0");
        setDefault("FractionalMetrics", "0");
        setDefault("Interpolation", "0");
        setDefault("TextAntiAliasing", "0");

        setDefault("ColorTargetBG", "-1184275");
        setDefault("ColorBG", "-65485");
        setDefault("ColorAvgBG", "-6750208");
        setDefault("ColorHighBG", "-81409");
        setDefault("ColorLowBG", "-163654");
        setDefault("ColorBU", "-16724941");
        setDefault("ColorIns1", "-6710785");
        setDefault("ColorIns2", "-16776961");
        setDefault("ColorIns", "-16724788");
        setDefault("ColorInsPerBU", "-6711040");

        setDefault("MeterType", "GlucoCard");
        setDefault("MeterPort", "none");
        setDefault("Meter", "GlucoCard;EuroFlash;FreeStyle");

        setDefault("Language", "en");



        super.read();
        // setDefault("DBName", "glucodb");
        setDefault("UserName", I18nControl.getInstance().getMessage("UNNAMED_USER"));
        setDefault("Ins1Name", "Insulin 1");
        setDefault("Ins2Name", "Insulin 2");
        setDefault("Ins1Abbr", "Ins1");
        setDefault("Ins2Abbr", "Ins2");
        setDefault("HighBG", "200");
        setDefault("LowBG", "60");
        setDefault("TargetHighBG", "120");
        setDefault("TargetLowBG", "80");


        
        */
    }

/*
    public static GGCProperties getInstance() 
    {
        if (singleton == null)
            singleton = new GGCProperties();
        return singleton;
    }
    */


    public String getUserName() 
    {
	return this.m_settings.getName();
    }

    public String getIns1Name() 
    {
	return this.m_settings.getIns1_name();
    }

    public String getIns2Name() 
    {
	return this.m_settings.getIns2_name();
    }

    public String getIns1Abbr() 
    {
	return this.m_settings.getIns1_abbr();
    }

    public int getMeasurmentTypeBg()
    {
	return this.m_settings.getBg_unit();
    }


    public String getIns2Abbr() 
    {
	return this.m_settings.getIns2_abbr();
    }


    public float getHighBG() 
    {
	if (this.m_settings.getBg_unit()==1)
	    return this.m_settings.getBg1_high();
	else
            return this.m_settings.getBg2_high();
    }

    public String getHighBGAsString() 
    {
	return "" + getHighBG();
    }

    public float getLowBG() 
    {
	if (this.m_settings.getBg_unit()==1)
	    return this.m_settings.getBg1_low();
	else
	    return this.m_settings.getBg2_low();
    }

    public String getLowBGAsString() 
    {
	return "" + this.getLowBG();
    }

    public float getTargetHighBG() 
    {
	if (this.m_settings.getBg_unit()==1)
	    return this.m_settings.getBg1_target_high();
	else
	    return this.m_settings.getBg2_target_high();
    }

    public String getTargetHighBGAsString() 
    {
	return "" + this.getTargetHighBG();
    }

    public float getTargetLowBG() 
    {
	if (this.m_settings.getBg_unit()==1)
	    return this.m_settings.getBg1_target_low();
	else
	    return this.m_settings.getBg2_target_low();
    }

    public String getTargetLowBGAsString() 
    {
	return "" + this.getTargetLowBG();
    }


    public int getRendering() 
    {
	return this.m_settings.getRender_rendering();
    }

    public int getAntiAliasing() 
    {
	return this.m_settings.getRender_antialiasing();
    }

    public int getColorRendering() 
    {
	return this.m_settings.getRender_colorrendering();
    }

    public int getDithering() 
    {
	return this.m_settings.getRender_dithering();
    }

    public int getFractionalMetrics() 
    {
	return this.m_settings.getRender_fractionalmetrics();
    }

    public int getInterpolation() 
    {
	return this.m_settings.getRender_interpolation();
    }

    public int getTextAntiAliasing() 
    {
	return this.m_settings.getRender_textantialiasing();
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

    public int getMeterType() 
    {
	return this.m_settings.getMeter_type();
    }


    public String getMeterTypeString() 
    {
	return m_da.getMeterManager().meter_names[this.m_settings.getMeter_type()];
    }


    public String getMeterPort() 
    {
	return this.m_settings.getMeter_port();
    }

    public String getLanguage() 
    {
	return null;
    }

    public Color getColor(int key)
    {
	return new Color(key);
    }


}
