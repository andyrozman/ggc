package ggc.plugin.graph.view;

import java.awt.*;
import java.awt.RenderingHints.Key;
import java.util.HashMap;

import javax.swing.*;

import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.IntervalMarker;
import org.jfree.chart.plot.XYPlot;
import org.jfree.ui.Layer;

import com.atech.i18n.I18nControlAbstract;

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
 *  Filename:     JFAbstractGraphView
 *  Description:  This is a replacement for AbstractGraphView using JFreeChart. It 
 *                contains parts common to all graphs.
 * 
 *  @author rumbi  
 */

public abstract class JFAbstractGraphView extends JPanel
{

    private static final long serialVersionUID = -1579716091265096686L;
    Color backgroundColor = Color.WHITE;
    int BGUnit = DataAccess.BG_MGDL;
    JFreeChart m_chart;

    ChartPanel chartPanel;
    DataAccess dataAccessInst = DataAccess.getInstance();
    GGCProperties settings = dataAccessInst.getSettings();
    ColorSchemeH colorScheme = settings.getSelectedColorScheme();
    ConfigurationManagerWrapper configurationManagerWrapper = dataAccessInst.getConfigurationManagerWrapper();
    float maxBG = 200;
    float minBG = 50;
    float BGDiff = maxBG - minBG;
    RenderingHints renderingHints;

    I18nControlAbstract translator = dataAccessInst.getI18nControlInstance();
    String unitLabel = "mg/dl";
    GlucoseUnitType glucoseUnitType;


    /**
     * Does some basic preparation needed by all graphs. Should be called from
     * all constructors and every subclass.
     */
    public JFAbstractGraphView()
    {
        glucoseUnitType = dataAccessInst.getGlucoseUnitType();

        switch (glucoseUnitType)
        {
            case mmol_L:
                maxBG = 11.1f;
                minBG = 2.775f;
                unitLabel = "mmol/l";
                break;

            case mg_dL:
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
     * Sets the basic layout on the passed <code>{@link JFreeChart}</code>.
     * 
     * @param chart
     *            The <code>{@link JFreeChart}</code> object that will contain
     *            the chart.
     */
    protected abstract void drawFramework(JFreeChart chart);


    /**
     * Draws the data onto the passed <code>{@link JFreeChart}</code>.
     * 
     * @param chart
     *            The <code>{@link JFreeChart}</code> object that will contain
     *            the chart.
     */
    protected abstract void drawValues(JFreeChart chart);


    /**
     * Gets the current rendering quality settings from the
     * <code>{@link DataAccess}</code>.
     */
    private void getRenderingQuality()
    {
        HashMap<Key, Object> hintsMap = new HashMap<Key, Object>();
        switch (configurationManagerWrapper.getAntiAliasing())
        {
            case 1:
                hintsMap.put(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_OFF);
                break;
            case 2:
                hintsMap.put(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                break;
            default:
                hintsMap.put(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_DEFAULT);
        }

        switch (configurationManagerWrapper.getColorRendering())
        {
            case 1:
                hintsMap.put(RenderingHints.KEY_COLOR_RENDERING, RenderingHints.VALUE_COLOR_RENDER_QUALITY);
                break;
            case 2:
                hintsMap.put(RenderingHints.KEY_COLOR_RENDERING, RenderingHints.VALUE_COLOR_RENDER_SPEED);
                break;
            default:
                hintsMap.put(RenderingHints.KEY_COLOR_RENDERING, RenderingHints.VALUE_COLOR_RENDER_DEFAULT);
        }

        switch (configurationManagerWrapper.getDithering())
        {
            case 1:
                hintsMap.put(RenderingHints.KEY_DITHERING, RenderingHints.VALUE_DITHER_DISABLE);
                break;
            case 2:
                hintsMap.put(RenderingHints.KEY_DITHERING, RenderingHints.VALUE_DITHER_ENABLE);
                break;
            default:
                hintsMap.put(RenderingHints.KEY_DITHERING, RenderingHints.VALUE_DITHER_DEFAULT);
        }

        switch (configurationManagerWrapper.getFractionalMetrics())
        {
            case 1:
                hintsMap.put(RenderingHints.KEY_FRACTIONALMETRICS, RenderingHints.VALUE_FRACTIONALMETRICS_OFF);
                break;
            case 2:
                hintsMap.put(RenderingHints.KEY_FRACTIONALMETRICS, RenderingHints.VALUE_FRACTIONALMETRICS_ON);
                break;
            default:
                hintsMap.put(RenderingHints.KEY_FRACTIONALMETRICS, RenderingHints.VALUE_FRACTIONALMETRICS_DEFAULT);
        }

        switch (configurationManagerWrapper.getInterpolation())
        {
            case 1:
                hintsMap.put(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BICUBIC);
                break;
            case 2:
                hintsMap.put(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
                break;
            default:
                hintsMap.put(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_NEAREST_NEIGHBOR);
        }

        switch (configurationManagerWrapper.getRendering())
        {
            case 1:
                hintsMap.put(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
                break;
            case 2:
                hintsMap.put(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_DEFAULT);
                break;
            default:
                hintsMap.put(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_DEFAULT);
        }

        switch (configurationManagerWrapper.getTextAntiAliasing())
        {
            case 1:
                hintsMap.put(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_OFF);
                break;
            case 2:
                hintsMap.put(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
                break;
            default:
                hintsMap.put(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_DEFAULT);
        }

        renderingHints = new RenderingHints(hintsMap);
    }


    /**
     * Re-draws the graph and sets the <code>{@link JPanel JPanel's}</code> size.
     */
    public void redraw()
    {
        if (m_chart == null || chartPanel == null)
            return;

        drawFramework(m_chart);
        drawValues(m_chart);

        setPreferredSize(new Dimension(chartPanel.getMinimumDrawWidth(), chartPanel.getMinimumDrawHeight()));
    }


    /**
     * Repaint
     * 
     * @see java.awt.Component#repaint()
     */
    @Override
    public void repaint()
    {
        redraw();
    }


    /**
     * Should be called whenever the settings were changed.
     */
    public void settingsChanged()
    {
        settings = dataAccessInst.getSettings();
        colorScheme = settings.getSelectedColorScheme();
        getRenderingQuality();
        redraw();
    }


    /**
     * Apply passed <code>{@link GGCProperties settings}</code>.
     * 
     * @param newSettings
     *            The <code>{@link GGCProperties settings}</code> to apply.
     */
    public void applySettings(GGCProperties newSettings)
    {
        settings = newSettings;
        colorScheme = settings.getSelectedColorScheme();
        getRenderingQuality();
        redraw();
    }


    /**
     * Apply passed <code>{@link ColorSchemeH color scheme}</code>.
     * 
     * @param newScheme
     *            The <code>{@link ColorSchemeH color scheme}</code> to apply.
     */
    public void applyColorScheme(ColorSchemeH newScheme)
    {
        colorScheme = newScheme;
        redraw();
    }


    /**
     * Adds <code>{@link IntervalMarker IntervalMarkers}</code> to the passed
     * <code>{@link XYPlot}</code> to highlight the low, target and high BG
     * zones.
     * 
     * @param plot
     *            The <code>{@link XYPlot}</code> to apply the markers to.
     */
    void applyMarkers(XYPlot plot)
    {
        if (plot == null)
            return;

        IntervalMarker lowBGMarker;
        IntervalMarker targetBGMarker;

        switch (BGUnit)
        {
            case DataAccess.BG_MMOL:
                lowBGMarker = new IntervalMarker(0, configurationManagerWrapper.getBG2TargetLow(),
                        dataAccessInst.getColor(colorScheme.getColor_bg_low()));
                targetBGMarker = new IntervalMarker(configurationManagerWrapper.getBG2TargetLow(),
                        configurationManagerWrapper.getBG2TargetHigh(),
                        dataAccessInst.getColor(colorScheme.getColor_bg_target()));
                break;
            case DataAccess.BG_MGDL:
            default:
                lowBGMarker = new IntervalMarker(0, configurationManagerWrapper.getBG1TargetLow(),
                        dataAccessInst.getColor(colorScheme.getColor_bg_low()));
                targetBGMarker = new IntervalMarker(configurationManagerWrapper.getBG1TargetLow(),
                        configurationManagerWrapper.getBG1TargetHigh(),
                        dataAccessInst.getColor(colorScheme.getColor_bg_target()));
                break;
        }

        plot.clearRangeMarkers();
        plot.addRangeMarker(lowBGMarker, Layer.BACKGROUND);
        plot.addRangeMarker(targetBGMarker, Layer.BACKGROUND);
        plot.setBackgroundPaint(dataAccessInst.getColor(colorScheme.getColor_bg_high()));
    }
}
