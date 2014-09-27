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

import java.util.Hashtable;

import com.atech.graphics.graphs.GraphConfigProperties;

/**
 *  Application:   GGC - GNU Gluco Control
 *
 *  See AUTHORS for copyright information.
 * 
 *  This program is free software; you can redistribute it and/or modify it under
 *  the terms of the GNU General Public License as published by the Free Software
 *  Foundation; either version 2 of the License, or (at your option) any later
 *  version.
 * 
 *  This program is distributed in the hope that it will be useful, but WITHOUT
 *  ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 *  FOR A PARTICULAR PURPOSE. See the GNU General Public License for more
 *  details.
 * 
 *  You should have received a copy of the GNU General Public License along with
 *  this program; if not, write to the Free Software Foundation, Inc., 59 Temple
 *  Place, Suite 330, Boston, MA 02111-1307 USA
 * 
 *  Filename:     GGCProperties  
 *  Description:  GGC Properties contains all configuration options that are used in
 *                configuration. In older version of GGC this options were written
 *                to file, but from 0.2.x this options are written in database
 *                for which we call ConfigurationManager
 * 
 *  Author:  schultd
 *  Author:  andyrozman {andy@atech-software.com}  
 */

public class GGCProperties implements GraphConfigProperties // extends
                                                            // GGCPropertiesHelper
{
    // private boolean changed_db = false;
    private boolean changed_config = false;
    // private boolean changed_scheme = false;
    private ConfigurationManager m_cfg_mgr = null;
    private Hashtable<String, ColorSchemeH> m_color_schemes = null;

    private ColorSchemeH m_colors = null;
    DbToolApplicationGGC m_config = null;
    private DataAccess m_da = null;

    /**
     * Constructor
     * 
     * @param da
     * @param config
     * @param cfg_mgr
     */
    public GGCProperties(DataAccess da, DbToolApplicationGGC config, ConfigurationManager cfg_mgr)
    {
        this.m_da = da;
        this.m_config = config;
        this.m_cfg_mgr = cfg_mgr;
        this.m_color_schemes = new Hashtable<String, ColorSchemeH>();
        /*
         * this.m_settings = new
         * SettingsMainH(I18nControl.getInstance().getMessage("UNNAMED_USER"),
         * "Insulin 1", "Ins1", "Insulin 2", "Ins2", 0, "No port available", 2,
         * 60.0f, 200.0f, 80.0f, 120.0f, 3.0f, 20.0f, 4.4f, 14.0f, 2,
         * "blueMetalthemepack.zip", 0, 0, 0, 0, 0, 0, 0, "", 1100, 1800, 2100,
         * "", "Default Scheme");
         */
        // public SettingsMainH(String name, String ins1_name, String ins1_abbr,
        // String ins2_name, String ins2_abbr, int meter_type, String
        // meter_port, int bg_unit, float bg1_low, float bg1_high, float
        // bg1_target_low, float bg1_target_high, float bg2_low, float bg2_high,
        // float bg2_target_low, float bg2_target_high, int laf_type, String
        // laf_name, int render_rendering, int render_dithering, int
        // render_interpolation, int render_antialiasing, int
        // render_textantialiasing, int render_colorrendering, int
        // render_fractionalmetrics, String print_pdf_viewer_path, int
        // print_lunch_start_time, int print_dinner_start_time, int
        // print_night_start_time, String print_empty_value, String
        // color_scheme) {
        this.m_colors = new ColorSchemeH("Default Scheme", 0, -65485, -6750208, -163654, -81409, -1184275, -16724788,
                -6710785, -16776961, -6711040, -16724941);

    }

    /**
     * Initialize this instance with the settings of the supplied instance.
     * 
     * @param settings
     *            The <code>{@link GGCProperties}</code> to be copied.
     */
    public GGCProperties(GGCProperties settings)
    {
        this(settings.m_da, settings.m_config, settings.m_cfg_mgr);
        copySettings(settings);
    }

    /**
     * Copy the settings of the supplied instance to this instance.
     * 
     * @param settings
     *            The <code>{@link GGCProperties}</code> to be copied.
     */
    @SuppressWarnings("unchecked")
    public void copySettings(GGCProperties settings)
    {
        setAntiAliasing(settings.getAntiAliasing());
        setBG1_High(settings.getBG1_High());
        setBG1_Low(settings.getBG1_Low());
        setBG1_TargetHigh(settings.getBG1_TargetHigh());
        setBG1_TargetLow(settings.getBG1_TargetLow());
        setBG2_High(settings.getBG2_High());
        setBG2_Low(settings.getBG2_Low());
        setBG2_TargetHigh(settings.getBG2_TargetHigh());
        setBG2_TargetLow(settings.getBG2_TargetLow());
        setBG_unit(settings.getBG_unit());
        setColorRendering(settings.getColorRendering());
        setColorSchemes((Hashtable<String, ColorSchemeH>) settings.m_color_schemes.clone(), false);
        setColorSchemeObject(settings.getSelectedColorSchemeInCfg());
        setDithering(settings.getDithering());
        setFractionalMetrics(settings.getFractionalMetrics());
        setIns1Abbr(settings.getIns1Abbr());
        setIns1Name(settings.getIns1Name());
        setIns2Abbr(settings.getIns2Abbr());
        setIns2Name(settings.getIns2Name());
        setInterpolation(settings.getInterpolation());
        setLanguage(settings.getLanguage());
        setExternalPdfVieverPath(settings.getExternalPdfVieverPath());
        setExternalPdfVieverParameters(settings.getExternalPdfVieverParameters());
        setUseExternalPdfViewer(settings.getUseExternalPdfViewer());
        setPrintDinnerStartTime(settings.getPrintDinnerStartTime());
        setPrintEmptyValue(settings.getPrintEmptyValue());
        setPrintLunchStartTime(settings.getPrintLunchStartTime());
        setPrintNightStartTime(settings.getPrintNightStartTime());
        setRendering(settings.getRendering());
        setTextAntiAliasing(settings.getTextAntiAliasing());
        setUserName(settings.getUserName());
    }

    // ---
    // --- General Data
    // ---

    /**
     * Get Language
     * 
     * @return
     */
    public String getLanguage()
    {
        return this.m_config.getSelectedLanguage();
    }

    /**
     * Set Language
     * 
     * @param name
     */
    public void setLanguage(String name)
    {
        int idx = m_da.getLanguageIndexByName(name);
        String post = m_da.avLangPostfix[idx];

        if (!this.m_config.getSelectedLanguage().equals(post))
        {
            this.m_config.setSelectedLanguage(post);
            this.changed_config = true;
        }
    }

    /**
     * Get User's Name
     * 
     * @return
     */
    public String getUserName()
    {
        return this.m_cfg_mgr.getStringValue("NAME");
    }

    /**
     * Set User's Name
     * 
     * @param value
     */
    public void setUserName(String value)
    {
        this.m_cfg_mgr.setStringValue("NAME", value);
    }

    // ---
    // --- Medical Data (Insulins and BG)
    // ---

    /**
     * Get BG: Target High
     * 
     * @return 
     */
    public float getBG_TargetHigh()
    {
        if (this.getBG_unit() == 1)
            return this.getBG1_TargetHigh();
        else
            return this.getBG2_TargetHigh();
    }

    /**
     * Get BG: Target Low
     * 
     * @return 
     */
    public float getBG_TargetLow()
    {
        if (this.getBG_unit() == 1)
            return this.getBG1_TargetLow();
        else
            return this.getBG2_TargetLow();
    }

    /**
     * Get BG: High
     * 
     * @return 
     */
    public float getBG_High()
    {
        if (this.getBG_unit() == 1)
            return this.getBG1_High();
        else
            return this.getBG2_High();
    }

    /**
     * Get BG: Low
     * 
     * @return 
     */
    public float getBG_Low()
    {
        if (this.getBG_unit() == 1)
            return this.getBG1_Low();
        else
            return this.getBG2_Low();
    }

    /**
     * Get BG Unit
     * 
     * @return 
     */
    public int getBG_unit()
    {
        return this.m_cfg_mgr.getIntValue("BG_UNIT");
    }

    /**
     * Set BG Unit
     * 
     * @param bgunit
     */
    public void setBG_unit(int bgunit)
    {
        this.m_cfg_mgr.setIntValue("BG_UNIT", bgunit);
    }

    /**
     * Get BG Unit String
     * 
     * @return 
     */
    public String getBG_unitString()
    {
        int unit = getBG_unit();

        if (unit == 1)
            return "mg/dl";
        else if (unit == 2)
            return "mmol/l";
        else
            return m_da.getI18nControlInstance().getMessage("UNKNOWN");

    }

    /**
     * Get BG 1 (mg/dL): High
     * 
     * @return
     */
    public float getBG1_High()
    {
        return this.m_cfg_mgr.getFloatValue("BG1_HIGH");
    }

    /**
     * Set BG 1 (mg/dL): High
     * 
     * @param value 
     */
    public void setBG1_High(float value)
    {
        this.m_cfg_mgr.setFloatValue("BG1_HIGH", value);
    }

    /**
     * Get BG 1 (mg/dL): Low
     * 
     * @return
     */
    public float getBG1_Low()
    {
        return this.m_cfg_mgr.getFloatValue("BG1_LOW");
    }

    /**
     * Set BG 1 (mg/dL): Low
     * 
     * @param value 
     */
    public void setBG1_Low(float value)
    {
        this.m_cfg_mgr.setFloatValue("BG1_LOW", value);
    }

    /**
     * Get BG 1 (mg/dL): Target High
     * 
     * @return
     */
    public float getBG1_TargetHigh()
    {
        return this.m_cfg_mgr.getFloatValue("BG1_TARGET_HIGH");
    }

    /**
     * Set BG 1 (mg/dL): Target High
     * 
     * @param value 
     */
    public void setBG1_TargetHigh(float value)
    {
        this.m_cfg_mgr.setFloatValue("BG1_TARGET_HIGH", value);
    }

    /**
     * Get BG 1 (mg/dL): Target Low
     * 
     * @return
     */
    public float getBG1_TargetLow()
    {
        return this.m_cfg_mgr.getFloatValue("BG1_TARGET_LOW");
    }

    /**
     * Set BG 1 (mg/dL): Target Low
     * 
     * @param value 
     */
    public void setBG1_TargetLow(float value)
    {
        this.m_cfg_mgr.setFloatValue("BG1_TARGET_LOW", value);
    }

    /**
     * Get BG 2 (mmol/L): High
     * 
     * @return
     */
    public float getBG2_High()
    {
        return this.m_cfg_mgr.getFloatValue("BG2_HIGH");
    }

    /**
     * Set BG 2 (mmol/L): High
     * 
     * @param value 
     */
    public void setBG2_High(float value)
    {
        this.m_cfg_mgr.setFloatValue("BG2_HIGH", value);
    }

    /**
     * Get BG 2 (mmol/L): Low
     * 
     * @return
     */
    public float getBG2_Low()
    {
        return this.m_cfg_mgr.getFloatValue("BG2_LOW");
    }

    /**
     * Set BG 2 (mmol/L): Low
     * 
     * @param value 
     */
    public void setBG2_Low(float value)
    {
        this.m_cfg_mgr.setFloatValue("BG2_LOW", value);
    }

    /**
     * Get BG 2 (mmol/L): Taget High
     * 
     * @return
     */
    public float getBG2_TargetHigh()
    {
        return this.m_cfg_mgr.getFloatValue("BG2_TARGET_HIGH");
    }

    /**
     * Set BG 2 (mmol/L): Target High
     * 
     * @param value 
     */
    public void setBG2_TargetHigh(float value)
    {
        this.m_cfg_mgr.setFloatValue("BG2_TARGET_HIGH", value);
    }

    /**
     * Get BG 2 (mmol/L): Target Low
     * 
     * @return
     */
    public float getBG2_TargetLow()
    {
        return this.m_cfg_mgr.getFloatValue("BG2_TARGET_LOW");
    }

    /**
     * Set BG 2 (mmol/L): Target Low
     * 
     * @param value 
     */
    public void setBG2_TargetLow(float value)
    {
        this.m_cfg_mgr.setFloatValue("BG2_TARGET_LOW", value);
    }

    /**
     * Get Insulin 1 Abbreviation
     * 
     * @return
     */
    public String getIns1Abbr()
    {
        return this.m_cfg_mgr.getStringValue("INS1_ABBR");
    }

    /**
     * Set Insulin 1 Abbreviation
     * 
     * @param value 
     */
    public void setIns1Abbr(String value)
    {
        this.m_cfg_mgr.setStringValue("INS1_ABBR", value);
    }

    /**
     * Get Insulin 1 Name
     * 
     * @return
     */
    public String getIns1Name()
    {
        return this.m_cfg_mgr.getStringValue("INS1_NAME");
    }

    /**
     * Set Insulin 1 Name
     * 
     * @param value 
     */
    public void setIns1Name(String value)
    {
        this.m_cfg_mgr.setStringValue("INS1_NAME", value);
    }

    /**
     * Get Insulin 1 Type
     * 
     * @return
     */
    public int getIns1Type()
    {
        return this.m_cfg_mgr.getIntValue("INS1_TYPE");
    }

    /**
     * Set Insulin 1 Type
     * 
     * @param ins 
     */
    public void setIns1Type(int ins)
    {
        this.m_cfg_mgr.setIntValue("INS1_TYPE", ins);
    }

    /**
     * Get Insulin 2 Abbreviation
     * 
     * @return
     */
    public String getIns2Abbr()
    {
        return this.m_cfg_mgr.getStringValue("INS2_ABBR");
    }

    /**
     * Set Insulin 2 Abbreviation
     * 
     * @param value 
     */
    public void setIns2Abbr(String value)
    {
        this.m_cfg_mgr.setStringValue("INS2_ABBR", value);
    }

    /**
     * Get Insulin 2 Name
     * 
     * @return
     */
    public String getIns2Name()
    {
        return this.m_cfg_mgr.getStringValue("INS2_NAME");
    }

    /**
     * Set Insulin 2 Name
     * 
     * @param value 
     */
    public void setIns2Name(String value)
    {
        this.m_cfg_mgr.setStringValue("INS2_NAME", value);
    }

    /**
     * Get Insulin 2 Type
     * 
     * @return
     */
    public int getIns2Type()
    {
        return this.m_cfg_mgr.getIntValue("INS2_TYPE");
    }

    /**
     * Set Insulin 2 Type
     * 
     * @param ins 
     */
    public void setIns2Type(int ins)
    {
        this.m_cfg_mgr.setIntValue("INS2_TYPE", ins);
    }

    /**
     * Get Insulin 3 Abbreviation
     * 
     * @return
     */
    public String getIns3Abbr()
    {
        return this.m_cfg_mgr.getStringValue("INS3_ABBR");
    }

    /**
     * Set Insulin 3 Abbreviation
     * 
     * @param value 
     */
    public void setIns3Abbr(String value)
    {
        this.m_cfg_mgr.setStringValue("INS3_ABBR", value);
    }

    /**
     * Get Insulin 3 Name
     * 
     * @return
     */
    public String getIns3Name()
    {
        return this.m_cfg_mgr.getStringValue("INS3_NAME");
    }

    /**
     * Set Insulin 3 Name
     * 
     * @param value 
     */
    public void setIns3Name(String value)
    {
        this.m_cfg_mgr.setStringValue("INS3_NAME", value);
    }

    /**
     * Get Insulin 3 Type
     * 
     * @return
     */
    public int getIns3Type()
    {
        return this.m_cfg_mgr.getIntValue("INS3_TYPE");
    }

    /**
     * Set Insulin 3 Type
     * 
     * @param ins 
     */
    public void setIns3Type(int ins)
    {
        this.m_cfg_mgr.setIntValue("INS3_TYPE", ins);
    }

    /**
     * Get Pump Insulin Name
     * 
     * @return
     */
    public String getPumpInsulin()
    {
        return this.m_cfg_mgr.getStringValue("PUMP_INSULIN");
    }

    /**
     * Set Pump Insulin Name
     * 
     * @param value 
     */
    public void setPumpInsulin(String value)
    {
        this.m_cfg_mgr.setStringValue("PUMP_INSULIN", value);
    }

    // ---
    // --- Graphs
    // ---

    /** 
     * Get AntiAliasing
     */
    public int getAntiAliasing()
    {
        return this.m_cfg_mgr.getIntValue("RENDER_ANTIALIASING");
    }

    /** 
     * Set AntiAliasing
     * 
     * @param value 
     */
    public void setAntiAliasing(int value)
    {
        this.m_cfg_mgr.setIntValue("RENDER_ANTIALIASING", value);
    }

    /** 
     * Get Color Rendering
     */
    public int getColorRendering()
    {
        return this.m_cfg_mgr.getIntValue("RENDER_COLOR_RENDERING");
    }

    /** 
     * Set Color Rendering
     * 
     * @param value 
     */
    public void setColorRendering(int value)
    {
        this.m_cfg_mgr.setIntValue("RENDER_COLOR_RENDERING", value);
    }

    /** 
     * Get Dithering
     */
    public int getDithering()
    {
        return this.m_cfg_mgr.getIntValue("RENDER_DITHERING");
    }

    /** 
     * Set Dithering
     * 
     * @param value 
     */
    public void setDithering(int value)
    {
        this.m_cfg_mgr.setIntValue("RENDER_DITHERING", value);
    }

    /** 
     * Get Fractional Meetrics
     */
    public int getFractionalMetrics()
    {
        return this.m_cfg_mgr.getIntValue("RENDER_FRACTIONAL_METRICS");
    }

    /** 
     * Set Fractional Meetrics
     * 
     * @param value 
     */
    public void setFractionalMetrics(int value)
    {
        this.m_cfg_mgr.setIntValue("RENDER_FRACTIONAL_METRICS", value);
    }

    /** 
     * Get Interpolation
     */
    public int getInterpolation()
    {
        return this.m_cfg_mgr.getIntValue("RENDER_INTERPOLATION");
    }

    /** 
     * Set Interpolation
     * 
     * @param value 
     */
    public void setInterpolation(int value)
    {
        this.m_cfg_mgr.setIntValue("RENDER_INTERPOLATION", value);
    }

    /** 
     * Get Rendering
     */
    public int getRendering()
    {
        return this.m_cfg_mgr.getIntValue("RENDER_RENDERING");
    }

    /** 
     * Set Rendering
     * 
     * @param value 
     */
    public void setRendering(int value)
    {
        this.m_cfg_mgr.setIntValue("RENDER_RENDERING", value);
    }

    /** 
     * Get Text Antialiasing
     */
    public int getTextAntiAliasing()
    {
        return this.m_cfg_mgr.getIntValue("RENDER_TEXT_ANTIALIASING");
    }

    /** 
     * Set Text Antialiasing
     * 
     * @param value 
     */
    public void setTextAntiAliasing(int value)
    {
        this.m_cfg_mgr.setIntValue("RENDER_TEXT_ANTIALIASING", value);
    }

    /**
     * Get Selected Color Scheme
     * 
     * @return
     */
    public ColorSchemeH getSelectedColorScheme()
    {
        return this.m_colors;
    }

    /**
     * Get Selected Color Scheme In Cfg
     * 
     * @return
     */
    public String getSelectedColorSchemeInCfg()
    {
        return this.m_cfg_mgr.getStringValue("SELECTED_COLOR_SCHEME");
    }

    /**
     * Set Selected Color Scheme In Cfg
     * 
     * @param value 
     */
    public void setSelectedColorSchemeInCfg(String value)
    {
        this.m_cfg_mgr.setStringValue("SELECTED_COLOR_SCHEME", value);
    }

    /**
     * Set Color Scheme Object
     * 
     * @param name
     */
    public void setColorSchemeObject(String name)
    {
        ColorSchemeH cs = this.m_color_schemes.get(name);

        if (cs != null && !cs.equals(m_colors))
        {
            this.setSelectedColorSchemeInCfg(name);
            this.m_colors = cs;
        }
    }

    /**
     * Set Color Schemes
     * @param table
     * @param isnew
     */
    public void setColorSchemes(Hashtable<String, ColorSchemeH> table, boolean isnew)
    {
        this.m_color_schemes = table;
        // this.changed_scheme = isnew;
    }

    /**
     * Get Color Schemes
     * @return
     */
    public Hashtable<String, ColorSchemeH> getColorSchemes()
    {
        return this.m_color_schemes;
    }

    // ---
    // --- Printing
    // ---

    /**
     * Get PDF Viewer Path
     * 
     * @return
     */
    public String getExternalPdfVieverPath()
    {
        return this.m_cfg_mgr.getStringValue("PRINT_PDF_VIEWER_PATH");
    }

    /**
     * Set PDF Viewer Path
     * 
     * @param value 
     */
    public void setExternalPdfVieverPath(String value)
    {
        this.m_cfg_mgr.setStringValue("PRINT_PDF_VIEWER_PATH", value);
    }

    /**
     * Get PDF Viewer Path
     * 
     * @return
     */
    public String getExternalPdfVieverParameters()
    {
        return this.m_cfg_mgr.getStringValue("PRINT_PDF_VIEWER_PARAMETERS");
    }

    /**
     * Set PDF Viewer Path
     * 
     * @param value 
     */
    public void setExternalPdfVieverParameters(String value)
    {
        this.m_cfg_mgr.setStringValue("PRINT_PDF_VIEWER_PARAMETERS", value);
    }

    /**
     * Get PDF Viewer Path
     * 
     * @return
     */
    public boolean getUseExternalPdfViewer()
    {
        return this.m_cfg_mgr.getBooleanValue("PRINT_USE_EXTERNAL_PDF_VIEWER");
    }

    /**
     * Set PDF Viewer Path
     * 
     * @param value 
     */
    public void setUseExternalPdfViewer(boolean value)
    {
        this.m_cfg_mgr.setBooleanValue("PRINT_USE_EXTERNAL_PDF_VIEWER", value);
    }

    /**
     * Get Print Empty Value
     * 
     * @return 
     */
    public String getPrintEmptyValue()
    {
        return this.m_cfg_mgr.getStringValue("PRINT_EMPTY_VALUE");
    }

    /**
     * Set Print Empty Value
     * 
     * @param value 
     */
    public void setPrintEmptyValue(String value)
    {
        this.m_cfg_mgr.setStringValue("PRINT_EMPTY_VALUE", value);
    }

    /**
     * Get Print Start Time: Lunch
     * 
     * @return
     */
    public int getPrintLunchStartTime()
    {
        return this.m_cfg_mgr.getIntValue("PRINT_LUNCH_START_TIME");
    }

    /**
     * Set Print Start Time: Lunch
     * 
     * @param value 
     */
    public void setPrintLunchStartTime(int value)
    {
        this.m_cfg_mgr.setIntValue("PRINT_LUNCH_START_TIME", value);
    }

    /**
     * Get Print Start Time: Dinner
     * 
     * @return
     */
    public int getPrintDinnerStartTime()
    {
        return this.m_cfg_mgr.getIntValue("PRINT_DINNER_START_TIME");
    }

    /**
     * Set Print Start Time: Dinner
     * 
     * @param value 
     */
    public void setPrintDinnerStartTime(int value)
    {
        this.m_cfg_mgr.setIntValue("PRINT_DINNER_START_TIME", value);
    }

    /**
     * Get Print Start Time: Night
     * 
     * @return
     */
    public int getPrintNightStartTime()
    {
        return this.m_cfg_mgr.getIntValue("PRINT_NIGHT_START_TIME");
    }

    /**
     * Set Print Start Time: Night
     * 
     * @param value 
     */
    public void setPrintNightStartTime(int value)
    {
        this.m_cfg_mgr.setIntValue("PRINT_NIGHT_START_TIME", value);
    }

    // ---
    // --- Ratios
    // ---

    /**
     * Get Ratio CH/Insulin
     * 
     * @return
     */
    public float getRatio_CH_Insulin()
    {
        return this.m_cfg_mgr.getFloatValue("RATIO_CH_INSULIN");
    }

    /**
     * Set Ratio CH/Insulin
     * 
     * @param val 
     */
    public void setRatio_CH_Insulin(float val)
    {
        this.m_cfg_mgr.setFloatValue("RATIO_CH_INSULIN", val);
    }

    /**
     * Get Ratio BG/Insulin
     * 
     * @return
     */
    public float getRatio_BG_Insulin()
    {
        return this.m_cfg_mgr.getFloatValue("RATIO_BG_INSULIN");
    }

    /**
     * Set Ratio BG/Insulin
     * 
     * @param val 
     */
    public void setRatio_BG_Insulin(float val)
    {
        this.m_cfg_mgr.setFloatValue("RATIO_BG_INSULIN", val);
    }

    /**
     * Get Ratio Mode
     * 
     * @return
     */
    public String getRatioMode()
    {
        return this.m_cfg_mgr.getStringValue("RATIO_MODE");
    }

    /**
     * Set Ratio Mode
     * 
     * @param val 
     */
    public void setRatioMode(String val)
    {
        this.m_cfg_mgr.setStringValue("RATIO_MODE", val);
    }

    // ---
    // --- Load/Save methods
    // ---

    /**
     * Load 
     */
    public void load()
    {
        m_da.getDb().loadConfigData();
        m_da.setBGMeasurmentType(this.getBG_unit());
        this.setColorSchemeObject(this.m_cfg_mgr.getStringValue("SELECTED_COLOR_SCHEME"));
    }

    /**
     * Reload
     */
    public void reload()
    {
        load();
    }

    /**
     * Save
     */
    public void save()
    {
        // System.out.println("save");

        // fix
        /*
         * if (changed_scheme) {
         * DataAccess.notImplemented("GGCProperties:save():: changed scheme");
         * System.out.println("save Scheme"); //m_da.m_db.s }
         */

        this.m_cfg_mgr.saveConfig();

        if (changed_config || this.m_config.hasChanged())
        {
            // System.out.println("save Config REAL");
            this.m_config.saveConfig();
        }

        m_da.setBGMeasurmentType(this.getBG_unit());

    }

}
