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
 *  Filename: prefRenderingQualityPane.java
 *  Purpose:  Setting the rendering quality of the application
 *
 *  Author:   schultd
 */

package gui.prefPane;


import view.DailyGraphView;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ItemEvent;


public class prefRenderingQualityPane extends AbstractPrefOptionsPanel
{
    private JComboBox comboAntiAliasing;
    private JComboBox comboColorRendering;
    private JComboBox comboDithering;
    private JComboBox comboFractionalMetrics;
    private JComboBox comboInterpolation;
    private JComboBox comboTextAntiAliasing;
    private JComboBox comboRendering;

    private DailyGraphView dgv;

    private JPanel testingPanel;

    public prefRenderingQualityPane()
    {
        init();
    }

    private void init()
    {
        JPanel a = new JPanel(new GridLayout(0, 1));
        a.add(new JLabel("AntiAliasing:"));
        a.add(new JLabel("ColorRendering:"));
        a.add(new JLabel("Dithering:"));
        a.add(new JLabel("FractionalMetrics:"));
        a.add(new JLabel("Interpolation:"));
        a.add(new JLabel("TextAntiAliasing:"));
        a.add(new JLabel("Rendering:"));

        JPanel b = new JPanel(new GridLayout(0, 1));

        Object[] o1 = {RenderingHints.VALUE_ANTIALIAS_DEFAULT, RenderingHints.VALUE_ANTIALIAS_OFF, RenderingHints.VALUE_ANTIALIAS_ON};
        b.add(comboAntiAliasing = new JComboBox(o1));

        Object[] o2 = {RenderingHints.VALUE_COLOR_RENDER_DEFAULT, RenderingHints.VALUE_COLOR_RENDER_QUALITY, RenderingHints.VALUE_COLOR_RENDER_SPEED};
        b.add(comboColorRendering = new JComboBox(o2));

        Object[] o3 = {RenderingHints.VALUE_DITHER_DEFAULT, RenderingHints.VALUE_DITHER_DISABLE, RenderingHints.VALUE_DITHER_ENABLE};
        b.add(comboDithering = new JComboBox(o3));

        Object[] o4 = {RenderingHints.VALUE_FRACTIONALMETRICS_DEFAULT, RenderingHints.VALUE_FRACTIONALMETRICS_OFF, RenderingHints.VALUE_FRACTIONALMETRICS_ON};
        b.add(comboFractionalMetrics = new JComboBox(o4));

        Object[] o5 = {RenderingHints.VALUE_INTERPOLATION_BICUBIC, RenderingHints.VALUE_INTERPOLATION_BILINEAR, RenderingHints.VALUE_INTERPOLATION_NEAREST_NEIGHBOR};
        b.add(comboInterpolation = new JComboBox(o5));

        Object[] o6 = {RenderingHints.VALUE_TEXT_ANTIALIAS_DEFAULT, RenderingHints.VALUE_TEXT_ANTIALIAS_OFF, RenderingHints.VALUE_TEXT_ANTIALIAS_ON};
        b.add(comboTextAntiAliasing = new JComboBox(o6));

        Object[] o7 = {RenderingHints.VALUE_RENDER_DEFAULT, RenderingHints.VALUE_RENDER_QUALITY, RenderingHints.VALUE_RENDER_SPEED};
        b.add(comboRendering = new JComboBox(o7));

        Box myBox = Box.createHorizontalBox();
        myBox.add(a);
        myBox.add(b);

        setLayout(new BorderLayout());

        comboAntiAliasing.setSelectedIndex(props.getAntiAliasing());
        comboColorRendering.setSelectedIndex(props.getColorRendering());
        comboDithering.setSelectedIndex(props.getDithering());
        comboFractionalMetrics.setSelectedIndex(props.getFractionalMetrics());
        comboInterpolation.setSelectedIndex(props.getInterpolation());
        comboTextAntiAliasing.setSelectedIndex(props.getTextAntiAliasing());
        comboRendering.setSelectedIndex(props.getRendering());

        comboAntiAliasing.addItemListener(this);
        comboColorRendering.addItemListener(this);
        comboDithering.addItemListener(this);
        comboFractionalMetrics.addItemListener(this);
        comboInterpolation.addItemListener(this);
        comboTextAntiAliasing.addItemListener(this);
        comboRendering.addItemListener(this);

        add(myBox, BorderLayout.NORTH);

        testingPanel = new JPanel();
        //testingPanel.setBackground(Color.white);

        dgv = new DailyGraphView();

        testingPanel.add(dgv, BorderLayout.CENTER);
        add(dgv, BorderLayout.CENTER);
    }

    public void saveProps()
    {
        props.set("AntiAliasing", comboAntiAliasing.getSelectedIndex());
        props.set("ColorRendering", comboColorRendering.getSelectedIndex());
        props.set("Dithering", comboDithering.getSelectedIndex());
        props.set("FractionalMetrics", comboFractionalMetrics.getSelectedIndex());
        props.set("Interpolation", comboInterpolation.getSelectedIndex());
        props.set("TextAntiAliasing", comboTextAntiAliasing.getSelectedIndex());
        props.set("Rendering", comboRendering.getSelectedIndex());
    }

    public void itemStateChanged(ItemEvent e)
    {
        saveProps();
        changed = true;
        dgv.setNewRenderingQuality();
        dgv.repaint();
    }

}
