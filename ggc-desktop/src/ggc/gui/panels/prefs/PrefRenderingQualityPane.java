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

package ggc.gui.panels.prefs;

import ggc.core.util.DataAccess;
import ggc.gui.dialogs.PropertiesDialog;
import ggc.gui.graphs.DailyGraphView;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.RenderingHints;
import java.awt.event.ItemEvent;
import java.util.Vector;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.ListSelectionModel;
import javax.swing.border.TitledBorder;

import com.atech.help.HelpCapable;

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

public class PrefRenderingQualityPane extends AbstractPrefOptionsPanel implements HelpCapable
{

    private static final long serialVersionUID = 5354608555593397390L;

    private JComboBox comboAntiAliasing, comboColorRendering, comboDithering, comboFractionalMetrics,
            comboInterpolation, comboTextAntiAliasing, comboRendering;

    private DailyGraphView dgv;
    private JPanel testingPanel;
    private Box box; //Moved Box out of init().

    /**
     * Constructor
     * 
     * @param dia
     */
    public PrefRenderingQualityPane(PropertiesDialog dia)
    {
        super(dia);
        init();
        // m_da.enableHelp(this);
    }

    private void init() //Did a total rewrite of init() to get a better grip of what is happening in the code as well as updating the graphs in the preferences section correctly.
    {
        setLayout(new BorderLayout());
        //this.setBorder(new TitledBorder(m_ic.getMessage("RENDERING_QUALITY")));
        
        JPanel a = new JPanel(new GridLayout(7, 2));
        a.setBorder(BorderFactory.createTitledBorder(m_ic.getMessage("RENDERING_QUALITY")));
        
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
        
        a.add(new JLabel(m_ic.getMessage("ANTIALIASING") + ":"), BorderLayout.WEST);
        a.add(comboAntiAliasing = new JComboBox(o1));
        
        a.add(new JLabel(m_ic.getMessage("COLOR_RENDERING") + ":"), BorderLayout.WEST);
        a.add(comboColorRendering = new JComboBox(o2));

        a.add(new JLabel(m_ic.getMessage("DITHERING") + ":"), BorderLayout.WEST);
        a.add(comboDithering = new JComboBox(o3));
        
        a.add(new JLabel(m_ic.getMessage("FRACTIONAL_METRICS") + ":"), BorderLayout.WEST);
        a.add(comboFractionalMetrics = new JComboBox(o4));
        
        
        a.add(new JLabel(m_ic.getMessage("INTERPOLATION") + ":"), BorderLayout.WEST);
        a.add(comboInterpolation = new JComboBox(o5));
        
        a.add(new JLabel(m_ic.getMessage("TEXT_ANTIALIASING") + ":"), BorderLayout.WEST);
        a.add(comboTextAntiAliasing = new JComboBox(o6));
        
        a.add(new JLabel(m_ic.getMessage("RENDERING") + ":"), BorderLayout.WEST);
        a.add(comboRendering = new JComboBox(o7));

        //JPanel b = new JPanel(new GridLayout(7, 2));
        //a.setBorder(BorderFactory.createTitledBorder(m_ic.getMessage("RENDERING_QUALITY")));

       // JPanel c = new JPanel(new GridLayout(0, 1));
       // c.add(new JLabel("  haloooo                        ")); //fill empty space to the right of JComboBox'es
        


        comboAntiAliasing.setSelectedIndex(m_da.getSettings().getAntiAliasing());
        comboColorRendering.setSelectedIndex(m_da.getSettings().getColorRendering());
        comboDithering.setSelectedIndex(m_da.getSettings().getDithering());
        comboFractionalMetrics.setSelectedIndex(m_da.getSettings().getFractionalMetrics());
        comboInterpolation.setSelectedIndex(m_da.getSettings().getInterpolation());
        comboTextAntiAliasing.setSelectedIndex(m_da.getSettings().getTextAntiAliasing());
        comboRendering.setSelectedIndex(m_da.getSettings().getRendering());

        comboAntiAliasing.addItemListener(this);
        comboColorRendering.addItemListener(this);
        comboDithering.addItemListener(this);
        comboFractionalMetrics.addItemListener(this);
        comboInterpolation.addItemListener(this);
        comboTextAntiAliasing.addItemListener(this);
        comboRendering.addItemListener(this);


        testingPanel =new JPanel(new BorderLayout());
        testingPanel.setBorder(BorderFactory.createTitledBorder(m_ic.getMessage("RENDERING_QUALITY")));
        // testingPanel.setBackground(Color.white);

        dgv = new DailyGraphView(m_da.getSettings().getSelectedColorScheme(), PrefFontsAndColorPane.createDailyGraphValues());
        testingPanel.setPreferredSize(new Dimension(150, 170));
        testingPanel.add(dgv, BorderLayout.CENTER);
        
        box = Box.createVerticalBox();
        box.add(a);
        //box.add(b);
        box.add(testingPanel);
        add(box);
    }

    /**
     * updateGraphView() updates the graphs in the preferences section to reflect
     * changes in mmol/l or mg/dl.
     * @author henrik
     */
    public void updateGraphView()
    {
        box.remove(testingPanel);
        testingPanel =new JPanel(new BorderLayout());
        dgv = new DailyGraphView(m_da.getSettings().getSelectedColorScheme(), PrefFontsAndColorPane.createDailyGraphValues());
        testingPanel.setPreferredSize(new Dimension(150, 170));
        testingPanel.add(dgv, BorderLayout.CENTER);
        box.add(testingPanel);
        box.validate();
        box.repaint();
    }

    /**
     * Save Properties
     * 
     * @see ggc.gui.panels.prefs.AbstractPrefOptionsPanel#saveProps()
     */
    @Override
    public void saveProps()
    {
        this.settings.setAntiAliasing(comboAntiAliasing.getSelectedIndex());
        this.settings.setColorRendering(comboColorRendering.getSelectedIndex());
        this.settings.setDithering(comboDithering.getSelectedIndex());
        this.settings.setFractionalMetrics(comboFractionalMetrics.getSelectedIndex());
        this.settings.setInterpolation(comboInterpolation.getSelectedIndex());
        this.settings.setTextAntiAliasing(comboTextAntiAliasing.getSelectedIndex());
        this.settings.setRendering(comboRendering.getSelectedIndex());
    }

    
    /**
     * Item State Changed
     * 
     * @see ggc.gui.panels.prefs.AbstractPrefOptionsPanel#itemStateChanged(java.awt.event.ItemEvent)
     */
    @Override
    public void itemStateChanged(ItemEvent e)
    {
        // saveProps();
        changed = true;
        dgv.settingsChanged();
    }

    // ****************************************************************
    // ****** HelpCapable Implementation *****
    // ****************************************************************

    /**
     * getComponent - get component to which to attach help context
     */
    public Component getComponent()
    {
        return this.getRootPane();
    }

    /**
     * getHelpButton - get Help button
     */
    public JButton getHelpButton()
    {
        return this.parent.getHelpButton();
    }

    /**
     * getHelpId - get id for Help
     */
    public String getHelpId()
    {
        return "GGC_Prefs_Rendering";
    }

}
