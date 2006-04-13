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

import ggc.db.hibernate.ColorSchemeH;
import ggc.db.hibernate.SettingsMainH;

public class GGCProperties //extends GGCPropertiesHelper 
{
    //private static GGCProperties singleton = null;
    private SettingsMainH m_settings = null;
    private ColorSchemeH m_colors = null;
    private DataAccess m_da = null;
    private Hashtable m_color_schemes;

    public GGCProperties(DataAccess da) 
    {
	m_da = da;
        m_color_schemes = new Hashtable();
        this.m_settings = new SettingsMainH(I18nControl.getInstance().getMessage("UNNAMED_USER"), 
                       "Insulin 1", "Ins1", "Insulin 2", "Ins2", 0, "No port available", 
                       2, 60.0f, 200.0f, 80.0f, 120.0f, 
		       3.0f, 20.0f, 4.4f, 14.0f,
		       2, "blueMetalthemepack.zip",
                       0, 0, 0, 0, 0, 0, 0, 
		       "", 1100, 1800, 2100, 1);

        this.m_colors = new ColorSchemeH(
        "Default Scheme", 0, -65485, -6750208, -163654, -81409, -1184275, -16724788, 
        -6710785, -16776961, -6711040, -16724941);

    }



    public void loadSettings(SettingsMainH settings)
    {
        this.m_settings = settings;
    }

    public SettingsMainH saveSettings()
    {
        return this.m_settings;
    }

    public void setColorSchemeObject(int id)
    {
        this.m_colors = (ColorSchemeH)this.m_color_schemes.get("" + id);
    }

    public void loadColorSchemes(Hashtable table)
    {
        this.m_color_schemes = table;
    }

    public Hashtable saveColorScemes()
    {
        return this.m_color_schemes;
    }


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


    public int getBGUnit()
    {
        return this.m_settings.getBg_unit();
    }

    public String getBGUnitString()
    {
        int unit = getBGUnit();

        if (unit==1) 
            return "mg/dl";
        else if (unit==2) 
            return "mmol/l";
        else
            return m_da.getI18nInstance().getMessage("UNKNOWN");

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
