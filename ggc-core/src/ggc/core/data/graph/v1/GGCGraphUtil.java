package ggc.core.data.graph.v1;

import java.awt.*;
import java.util.HashMap;
import java.util.Map;

import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.IntervalMarker;
import org.jfree.chart.plot.XYPlot;
import org.jfree.ui.Layer;

import com.atech.graphics.graphs.GraphUtil;
import com.atech.utils.ATDataAccessAbstract;

import ggc.core.data.cfg.ConfigurationManagerWrapper;
import ggc.core.data.defs.GlucoseUnitType;
import ggc.core.db.hibernate.ColorSchemeH;
import ggc.core.util.DataAccess;
import ggc.core.util.GGCProperties;

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
 *  Filename:     GGCGraphUtil
 *  Description:  This is utility for creating graphs, it extends abstract GraphUtil
 *                It has same function as AbstractGraphView from old framework.
 *                Most of parts are copied from Rumbi's JFAbstractGraphView.
 *                
 *  Author: andyrozman {andy@atech-software.com}  
 */

public class GGCGraphUtil extends GraphUtil
{

    /**
     * Graph Setting: Background color
     */
    public Color backgroundColor = Color.WHITE;
    // int BGUnit = DataAccess.BG_MGDL;
    JFreeChart chart;
    GGCProperties settings;

    ChartPanel chartPanel;
    // DataAccess dataAccessInst = DataAccess.getInstance();
    // GGCProperties settings = dataAccessInst.getSettings();
    ColorSchemeH colorScheme; // = settings.getSelectedColorScheme();
    float maxBG = 200;
    float minBG = 50;
    float BGDiff = maxBG - minBG;
    // RenderingHints renderingHints;

    // private I18nControlAbstract i18nControl =
    // DataAccess.getInstance().getI18nControlInstance();

    String unitLabel; // removed ="mg/dl" to reflect changes made in properties

    DataAccess m_da_local;

    ConfigurationManagerWrapper configurationManagerWrapper;
    GlucoseUnitType glucoseUnitType;

    /**
     * Static instance of Graph Util
     */
    public static Map<ATDataAccessAbstract, GGCGraphUtil> s_graph_util = new HashMap<ATDataAccessAbstract, GGCGraphUtil>();


    /**
     * Does some basic preparation needed by all graphs. Should be called from
     * all constructors and every subclass.
     */
    private GGCGraphUtil(ATDataAccessAbstract da)
    {
        super(da);

        // getRenderingQuality();
    }


    /**
     * Get Instance (Singelton)
     *
     * @param da
     * @return GGCGraphUtil instance
     */
    public static GGCGraphUtil getInstance(ATDataAccessAbstract da)
    {
        if (!GGCGraphUtil.s_graph_util.containsKey(da))
        {
            GGCGraphUtil gu = new GGCGraphUtil(da);
            s_graph_util.put(da, gu);
            return gu;
        }

        return GGCGraphUtil.s_graph_util.get(da);
    }


    /**
     * Init Local
     * 
     * @see com.atech.graphics.graphs.GraphUtil#initLocal()
     */
    @Override
    public void initLocal()
    {
        // System.out.println("GGCGraphUtil");

        m_da_local = DataAccess.getInstance();

        settings = m_da_local.getSettings(); // DataAccess.getSettings();

        colorScheme = settings.getSelectedColorScheme();
        configurationManagerWrapper = m_da_local.getConfigurationManagerWrapper();
        glucoseUnitType = m_da_local.getGlucoseUnitType();

        switch (glucoseUnitType)
        {

            case mmol_L:
                maxBG = 11.1f;
                minBG = 2.775f;
                unitLabel = "mmol/l";
                break;

            case mg_dL:
            case None:
            default:
                maxBG = 200;
                minBG = 50;
                unitLabel = "mg/dl";
                break;
        }

        BGDiff = maxBG - minBG;

        getRenderingQuality();

    }


    /**
     * Adds <code>{@link IntervalMarker IntervalMarkers}</code> to the passed
     * <code>{@link XYPlot}</code> to highlight the low, target and high BG
     * zones.
     * 
     * @param plot
     *            The <code>{@link XYPlot}</code> to apply the markers to.
     */
    public void applyMarkers(XYPlot plot)
    {
        if (plot == null)
            return;

        IntervalMarker lowBGMarker;
        IntervalMarker targetBGMarker;

        // System.out.println("colorScheme: " + this.colorScheme);
        // System.out.println("settings: " + this.settings);
        // System.out.println("m_da_local: " + this.m_da_local);

        switch (glucoseUnitType)
        {
            case mmol_L:
                lowBGMarker = new IntervalMarker(0, this.configurationManagerWrapper.getBG2TargetLow(),
                        m_da_local.getColor(colorScheme.getColor_bg_low()));
                targetBGMarker = new IntervalMarker(this.configurationManagerWrapper.getBG2TargetLow(),
                        this.configurationManagerWrapper.getBG2TargetHigh(),
                        m_da_local.getColor(colorScheme.getColor_bg_target()));
                break;

            case mg_dL:
            default:
                lowBGMarker = new IntervalMarker(0, this.configurationManagerWrapper.getBG1TargetLow(),
                        m_da_local.getColor(colorScheme.getColor_bg_low()));
                targetBGMarker = new IntervalMarker(this.configurationManagerWrapper.getBG1TargetLow(),
                        this.configurationManagerWrapper.getBG1TargetHigh(),
                        m_da_local.getColor(colorScheme.getColor_bg_target()));
                break;
        }

        plot.clearRangeMarkers();
        plot.addRangeMarker(lowBGMarker, Layer.BACKGROUND);
        plot.addRangeMarker(targetBGMarker, Layer.BACKGROUND);
        plot.setBackgroundPaint(m_da_local.getColor(colorScheme.getColor_bg_high()));
    }


    /**
     * Get Color Scheme
     * 
     * @return
     */
    public ColorSchemeH getColorScheme()
    {
        return this.colorScheme;
    }


    /**
     * Get Unit Label
     * 
     * @return Label of unit in mmol/l or mg/dl.
     */
    public String getUnitLabel()
    {
        this.unitLabel = glucoseUnitType.getTranslation();
        return this.unitLabel;
    }

}
