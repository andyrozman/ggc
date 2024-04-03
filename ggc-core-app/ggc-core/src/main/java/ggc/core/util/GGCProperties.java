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

import java.util.Hashtable;
import java.util.Map;

import ggc.core.data.cfg.ConfigurationManagerWrapper;
import ggc.core.db.GGCDb;
import ggc.core.db.hibernate.settings.ColorSchemeH;
import ggc.core.db.tool.DbToolApplicationGGC;

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

public class GGCProperties // implements GraphConfigProperties // extends
// GGCPropertiesHelper
{

    // private boolean changed_db = false;
    private boolean changed_config = false;
    // private boolean changed_scheme = false;
    // private ConfigurationManager m_cfg_mgr = null;
    private Map<String, ColorSchemeH> m_color_schemes = null;

    private ColorSchemeH m_colors = null;
    DbToolApplicationGGC m_config = null;
    private DataAccess m_da = null;
    ConfigurationManagerWrapper configurationManagerWrapper;


    /**
     * Constructor
     * 
     * @param da
     * @param config
     * @param configurationManagerWrapper
     */
    public GGCProperties(DataAccess da, DbToolApplicationGGC config,
            ConfigurationManagerWrapper configurationManagerWrapper)
    {
        this.m_da = da;
        this.m_config = config;
        // this.m_cfg_mgr = cfg_mgr;
        this.m_color_schemes = new Hashtable<String, ColorSchemeH>();
        this.configurationManagerWrapper = configurationManagerWrapper;

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
        this(settings.m_da, settings.m_config, settings.configurationManagerWrapper);
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
        // setAntiAliasing(settings.getAntiAliasing());
        // setBG1_High(settings.getBG1_High());
        // setBG1_Low(settings.getBG1_Low());
        // setBG1_TargetHigh(settings.getBG1_TargetHigh());
        // setBG1_TargetLow(settings.getBG1_TargetLow());
        // setBG2_High(settings.getBG2_High());
        // setBG2_Low(settings.getBG2_Low());
        // setBG2_TargetHigh(settings.getBG2_TargetHigh());
        // setBG2_TargetLow(settings.getBG2_TargetLow());
        // setBG_unit(settings.getBG_unit());
        // setColorRendering(settings.getColorRendering());
        // setColorSchemes((Hashtable<String, ColorSchemeH>)
        // settings.m_color_schemes.clone(), false);
        // setColorSchemeObject(settings.getSelectedColorScheme());
        // setDithering(settings.getDithering());
        // setFractionalMetrics(settings.getFractionalMetrics());
        // setIns1Abbr(settings.getIns1Abbr());
        // setIns1Name(settings.getIns1Name());
        // setIns2Abbr(settings.getIns2Abbr());
        // setIns2Name(settings.getIns2Name());
        // setInterpolation(settings.getInterpolation());
        // setLanguage(settings.getLanguage());
        // setExternalPdfVieverPath(settings.getExternalPdfVieverPath());
        // setExternalPdfVieverParameters(settings.getExternalPdfVieverParameters());
        // setUseExternalPdfViewer(settings.getUseExternalPdfViewer());
        // setPrintDinnerStartTime(settings.getPrintDinnerStartTime());
        // setPrintEmptyValue(settings.getPrintEmptyValue());
        // setPrintLunchStartTime(settings.getPrintLunchStartTime());
        // setPrintNightStartTime(settings.getPrintNightStartTime());
        // setRendering(settings.getRendering());
        // setTextAntiAliasing(settings.getTextAntiAliasing());
        // setUserName(settings.getUserName());

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

    // ---
    // --- Medical Data (Insulins and BG)
    // ---


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
     * Set Color Scheme Object
     * 
     * @param name
     */
    public void setColorSchemeObject(String name)
    {
        ColorSchemeH cs = this.m_color_schemes.get(name);

        if (cs != null && !cs.equals(m_colors))
        {
            this.configurationManagerWrapper.setSelectedColorScheme(name);
            this.m_colors = cs;
        }
    }


    /**
     * Set Color Schemes
     * @param table
     * @param isnew
     */
    public void setColorSchemes(Map<String, ColorSchemeH> table, boolean isnew)
    {
        this.m_color_schemes = table;
        // this.changed_scheme = isnew;
    }


    /**
     * Get Color Schemes
     * @return
     */
    public Map<String, ColorSchemeH> getColorSchemes()
    {
        return this.m_color_schemes;
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
        m_da.setGlucoseUnitType(this.configurationManagerWrapper.getGlucoseUnit());
        this.setColorSchemeObject(this.configurationManagerWrapper.getSelectedColorScheme());
    }


    public void load(GGCDb db)
    {
        db.loadConfigData();
        m_da.setGlucoseUnitType(this.configurationManagerWrapper.getGlucoseUnit());
        this.setColorSchemeObject(this.configurationManagerWrapper.getSelectedColorScheme());
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
         * System.out.println("save Scheme"); //dataAccess.m_db.s }
         */

        this.configurationManagerWrapper.saveConfig();

        if (changed_config || this.m_config.hasChanged())
        {
            // System.out.println("save Config REAL");
            this.m_config.saveConfig();
        }

        m_da.setGlucoseUnitType(this.configurationManagerWrapper.getGlucoseUnit());

    }

}
