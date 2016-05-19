package ggc.gui.cfg.panels;

import java.awt.*;
import java.awt.event.ItemEvent;

import javax.swing.*;

import ggc.core.data.defs.GlucoseUnitType;
import ggc.gui.cfg.PropertiesDialog;
import ggc.gui.cfg.graph.DailyGraphView;

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
 *  Filename:     PrefRenderingQualityPane
 *  Description:  Setting the rendering quality of the application (graphs)
 * 
 *  Author: schultd
 *          andyrozman {andy@atech-software.com}  
 */

public class PrefRenderingQualityPane extends AbstractPrefOptionsPanel
{

    private static final long serialVersionUID = 5354608555593397390L;

    private JComboBox comboAntiAliasing, comboColorRendering, comboDithering, comboFractionalMetrics,
            comboInterpolation, comboTextAntiAliasing, comboRendering;

    private DailyGraphView dgv;
    private JPanel testingPanel;
    private Box box; // Moved Box out of init().

    private GlucoseUnitType glucoseUnitType;


    /**
     * Constructor
     * 
     * @param dia
     */
    public PrefRenderingQualityPane(PropertiesDialog dia)
    {
        super(dia);
        init();
    }


    private void init() // Did a total rewrite of init() to get a better grip of
                        // what is happening in the code as well as updating the
                        // graphs in the preferences section correctly.
    {

        glucoseUnitType = configurationManagerWrapper.getGlucoseUnit();

        setLayout(new BorderLayout());
        // this.setBorder(new
        // TitledBorder(i18nControl.getMessage("RENDERING_QUALITY")));

        JPanel a = new JPanel(new GridLayout(7, 2));
        a.setBorder(BorderFactory.createTitledBorder(i18nControl.getMessage("RENDERING_QUALITY")));

        Object[] o1 = { RenderingHints.VALUE_ANTIALIAS_DEFAULT, RenderingHints.VALUE_ANTIALIAS_OFF,
                        RenderingHints.VALUE_ANTIALIAS_ON };

        Object[] o2 = { RenderingHints.VALUE_COLOR_RENDER_DEFAULT, RenderingHints.VALUE_COLOR_RENDER_QUALITY,
                        RenderingHints.VALUE_COLOR_RENDER_SPEED };

        Object[] o3 = { RenderingHints.VALUE_DITHER_DEFAULT, RenderingHints.VALUE_DITHER_DISABLE,
                        RenderingHints.VALUE_DITHER_ENABLE };

        Object[] o4 = { RenderingHints.VALUE_FRACTIONALMETRICS_DEFAULT, RenderingHints.VALUE_FRACTIONALMETRICS_OFF,
                        RenderingHints.VALUE_FRACTIONALMETRICS_ON };

        Object[] o5 = { RenderingHints.VALUE_INTERPOLATION_BICUBIC, RenderingHints.VALUE_INTERPOLATION_BILINEAR,
                        RenderingHints.VALUE_INTERPOLATION_NEAREST_NEIGHBOR };

        Object[] o6 = { RenderingHints.VALUE_TEXT_ANTIALIAS_DEFAULT, RenderingHints.VALUE_TEXT_ANTIALIAS_OFF,
                        RenderingHints.VALUE_TEXT_ANTIALIAS_ON };

        Object[] o7 = { RenderingHints.VALUE_RENDER_DEFAULT, RenderingHints.VALUE_RENDER_QUALITY,
                        RenderingHints.VALUE_RENDER_SPEED };

        a.add(new JLabel(i18nControl.getMessage("ANTIALIASING") + ":"), BorderLayout.WEST);
        a.add(comboAntiAliasing = new JComboBox(o1));

        a.add(new JLabel(i18nControl.getMessage("COLOR_RENDERING") + ":"), BorderLayout.WEST);
        a.add(comboColorRendering = new JComboBox(o2));

        a.add(new JLabel(i18nControl.getMessage("DITHERING") + ":"), BorderLayout.WEST);
        a.add(comboDithering = new JComboBox(o3));

        a.add(new JLabel(i18nControl.getMessage("FRACTIONAL_METRICS") + ":"), BorderLayout.WEST);
        a.add(comboFractionalMetrics = new JComboBox(o4));

        a.add(new JLabel(i18nControl.getMessage("INTERPOLATION") + ":"), BorderLayout.WEST);
        a.add(comboInterpolation = new JComboBox(o5));

        a.add(new JLabel(i18nControl.getMessage("TEXT_ANTIALIASING") + ":"), BorderLayout.WEST);
        a.add(comboTextAntiAliasing = new JComboBox(o6));

        a.add(new JLabel(i18nControl.getMessage("RENDERING") + ":"), BorderLayout.WEST);
        a.add(comboRendering = new JComboBox(o7));

        comboAntiAliasing.setSelectedIndex(configurationManagerWrapper.getAntiAliasing());
        comboColorRendering.setSelectedIndex(configurationManagerWrapper.getColorRendering());
        comboDithering.setSelectedIndex(configurationManagerWrapper.getDithering());
        comboFractionalMetrics.setSelectedIndex(configurationManagerWrapper.getFractionalMetrics());
        comboInterpolation.setSelectedIndex(configurationManagerWrapper.getInterpolation());
        comboTextAntiAliasing.setSelectedIndex(configurationManagerWrapper.getTextAntiAliasing());
        comboRendering.setSelectedIndex(configurationManagerWrapper.getRendering());

        comboAntiAliasing.addItemListener(this);
        comboColorRendering.addItemListener(this);
        comboDithering.addItemListener(this);
        comboFractionalMetrics.addItemListener(this);
        comboInterpolation.addItemListener(this);
        comboTextAntiAliasing.addItemListener(this);
        comboRendering.addItemListener(this);

        testingPanel = new JPanel(new BorderLayout());
        testingPanel.setBorder(BorderFactory.createTitledBorder(i18nControl.getMessage("RENDERING_QUALITY")));
        // testingPanel.setBackground(Color.white);

        dgv = new DailyGraphView(ggcProperties.getSelectedColorScheme(), PrefFontsAndColorPane.createDailyGraphValues(),
                glucoseUnitType);
        testingPanel.setPreferredSize(new Dimension(150, 170));
        testingPanel.add(dgv, BorderLayout.CENTER);

        box = Box.createVerticalBox();
        box.add(a);
        // box.add(b);
        box.add(testingPanel);
        add(box);
    }


    public void updateGlucoseUnitType(GlucoseUnitType unitType)
    {
        this.glucoseUnitType = unitType;
    }


    /**
     * updateGraphView() updates the graphs in the preferences section to reflect
     * changes in mmol/l or mg/dl.
     * @author henrik
     */
    public void updateGraphView()
    {
        box.remove(testingPanel);
        testingPanel = new JPanel(new BorderLayout());
        dgv = new DailyGraphView(ggcProperties.getSelectedColorScheme(), PrefFontsAndColorPane.createDailyGraphValues(),
                glucoseUnitType);
        testingPanel.setPreferredSize(new Dimension(150, 170));
        testingPanel.add(dgv, BorderLayout.CENTER);
        box.add(testingPanel);
        box.validate();
        box.repaint();
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public void saveProps()
    {
        configurationManagerWrapper.setAntiAliasing(comboAntiAliasing.getSelectedIndex());
        configurationManagerWrapper.setColorRendering(comboColorRendering.getSelectedIndex());
        configurationManagerWrapper.setDithering(comboDithering.getSelectedIndex());
        configurationManagerWrapper.setFractionalMetrics(comboFractionalMetrics.getSelectedIndex());
        configurationManagerWrapper.setInterpolation(comboInterpolation.getSelectedIndex());
        configurationManagerWrapper.setTextAntiAliasing(comboTextAntiAliasing.getSelectedIndex());
        configurationManagerWrapper.setRendering(comboRendering.getSelectedIndex());
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public void itemStateChanged(ItemEvent e)
    {
        changed = true;
        dgv.settingsChanged();
    }


    /**
     * {@inheritDoc}
     */
    public String getHelpId()
    {
        return "GGC_Prefs_Rendering";
    }

}
