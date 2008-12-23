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
 *  Filename: JFAbstractGraphView.java
 *  Purpose:  Common methods and variables for all graph views.
 *
 *  Author:   rumbi
 *  
 */

package ggc.core.data.graph;

import ggc.core.db.hibernate.ColorSchemeH;
import ggc.core.util.DataAccess;
import ggc.core.util.GGCProperties;
import ggc.core.util.I18nControl;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.RenderingHints;
import java.awt.RenderingHints.Key;
import java.util.HashMap;

import javax.swing.JPanel;

import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.IntervalMarker;
import org.jfree.chart.plot.XYPlot;
import org.jfree.ui.Layer;

import com.atech.graphics.graphs.GraphUtil;

/**
 * This is a replacement for AbstractGraphView using JFreeChart. It contains
 * parts common to all graphs.
 * 
 * @author rumbi
 */
public class GGCGraphUtil extends GraphUtil
{
    /**
     * 
     */
    private static final long serialVersionUID = -1579716091265096686L;
    Color backgroundColor = Color.WHITE;
    int BGUnit = DataAccess.BG_MGDL;
    JFreeChart chart;
    GGCProperties settings;  
    
    ChartPanel chartPanel;
    //DataAccess dataAccessInst = DataAccess.getInstance();
    //GGCProperties settings = dataAccessInst.getSettings();
    ColorSchemeH colorScheme; // = settings.getSelectedColorScheme();
    float maxBG = 200;
    float minBG = 50;
    float BGDiff = maxBG - minBG;
    //RenderingHints renderingHints;

    I18nControl translator = I18nControl.getInstance();
    String unitLabel = "mg/dl";
    
    DataAccess m_da_local;

    public static GGCGraphUtil s_graph_util; 
    
    
    /**
     * Does some basic preparation needed by all graphs. Should be called from
     * all constructors and every subclass.
     */
    private GGCGraphUtil()
    {
        super(DataAccess.getInstance());


        //getRenderingQuality();
    }

    
    public void initLocal()
    {
        //System.out.println("GGCGraphUtil");
        
        m_da_local = DataAccess.getInstance();
        
        settings = m_da_local.getSettings(); //DataAccess.getSettings();
        
        colorScheme = settings.getSelectedColorScheme();
        BGUnit = settings.getBG_unit();

        switch (BGUnit)
        {
            case DataAccess.BG_MMOL:
                maxBG = 11.1f;
                minBG = 2.775f;
                unitLabel = "mmol/l";
                break;

            case DataAccess.BG_MGDL:
            default:
                maxBG = 200;
                minBG = 50;
                unitLabel = "mg/dl";
                break;
        }
        BGDiff = maxBG - minBG;

        getRenderingQuality();
        
    }
    
    
    public static GGCGraphUtil getInstance()
    {
        if (GGCGraphUtil.s_graph_util==null)
            GGCGraphUtil.s_graph_util = new GGCGraphUtil();
        
        return GGCGraphUtil.s_graph_util;
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
        {
            return;
        }

        IntervalMarker lowBGMarker;
        IntervalMarker targetBGMarker;

        //System.out.println("colorScheme: " + this.colorScheme);
        //System.out.println("settings: " + this.settings);
        //System.out.println("m_da_local: " + this.m_da_local);
        
        switch (this.settings.getBG_unit())
        {
        case DataAccess.BG_MMOL:
            lowBGMarker = new IntervalMarker(0, settings.getBG2_TargetLow(), 
                m_da_local.getColor(colorScheme.getColor_bg_low()));
            targetBGMarker = new IntervalMarker(settings.getBG2_TargetLow(), settings.getBG2_TargetHigh(),
                m_da_local.getColor(colorScheme.getColor_bg_target()));
            break;
        case DataAccess.BG_MGDL:
        default:
            lowBGMarker = new IntervalMarker(0, settings.getBG1_TargetLow(), m_da_local.getColor(colorScheme.getColor_bg_low()));
            targetBGMarker = new IntervalMarker(settings.getBG1_TargetLow(), settings.getBG1_TargetHigh(),
                m_da_local.getColor(colorScheme.getColor_bg_target()));
            break;
        }

        plot.clearRangeMarkers();
        plot.addRangeMarker(lowBGMarker, Layer.BACKGROUND);
        plot.addRangeMarker(targetBGMarker, Layer.BACKGROUND);
        plot.setBackgroundPaint(m_da_local.getColor(colorScheme.getColor_bg_high()));
    }
    
    public ColorSchemeH getColorScheme()
    {
        return this.colorScheme;
    }

    
    public String getUnitLabel()
    {
        return this.unitLabel;
    }
    
}
