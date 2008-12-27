package ggc.gui.panels.prefs;

import ggc.gui.dialogs.PropertiesDialog;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.GridLayout;

import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.TitledBorder;

import com.atech.help.HelpCapable;

/**
 * Application: GGC - GNU Gluco Control
 * 
 * See AUTHORS for copyright information.
 * 
 * This program is free software; you can redistribute it and/or modify it under
 * the terms of the GNU General Public License as published by the Free Software
 * Foundation; either version 2 of the License, or (at your option) any later
 * version.
 * 
 * This program is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU General Public License for more
 * details.
 * 
 * You should have received a copy of the GNU General Public License along with
 * this program; if not, write to the Free Software Foundation, Inc., 59 Temple
 * Place, Suite 330, Boston, MA 02111-1307 USA
 * 
 * Filename:     PrefMedicalDataPane 
 * Description:  Medical Options such as insulin names and BG-Values.
 * 
 *  Author: schultd
 *          andyrozman {andy@atech-software.com}  
 */

public class PrefMedicalDataPane extends AbstractPrefOptionsPanel implements HelpCapable
{
    
    private static final long serialVersionUID = 4645565336749757311L;

    // insulins
    private JTextField fieldIns1Name, fieldIns2Name, fieldIns1Abbr, fieldIns2Abbr;

    // bg (mg/dl)
    private JTextField fieldHighBG1, fieldLowBG1, fieldTargetHighBG1, fieldTargetLowBG1;

    // bg (mmol/l)
    private JTextField fieldHighBG2, fieldLowBG2, fieldTargetHighBG2, fieldTargetLowBG2;

    JComboBox cbUnit = null;

    /**
     * Constructor
     * 
     * @param dia
     */
    public PrefMedicalDataPane(PropertiesDialog dia)
    {
        super(dia);
        init();
        // m_da.enableHelp(this);
    }

    private void init()
    {

        setLayout(new BorderLayout());

        // JPanel ful = new JPanel(new GridLayout(4,0));

        JPanel a = new JPanel(new GridLayout(0, 2));
        a.setBorder(new TitledBorder(m_ic.getMessage("INSULIN_SETTINGS")));
        a.add(new JLabel(m_ic.getMessage("INSULIN") + " 1 " + m_ic.getMessage("NAME") + ":"));
        a.add(fieldIns1Name = new JTextField(m_da.getSettings().getIns1Name(), 20));
        a.add(new JLabel(m_ic.getMessage("INSULIN") + " 1 " + m_ic.getMessage("ABBR") + ":"));
        a.add(fieldIns1Abbr = new JTextField(m_da.getSettings().getIns1Abbr(), 20));
        a.add(new JLabel(m_ic.getMessage("INSULIN") + " 2 " + m_ic.getMessage("NAME") + ":"));
        a.add(fieldIns2Name = new JTextField(m_da.getSettings().getIns2Name(), 20));
        a.add(new JLabel(m_ic.getMessage("INSULIN") + " 2 " + m_ic.getMessage("ABBR") + ":"));
        a.add(fieldIns2Abbr = new JTextField(m_da.getSettings().getIns2Abbr(), 20));

        fieldIns1Name.getDocument().addDocumentListener(this);
        fieldIns2Name.getDocument().addDocumentListener(this);
        fieldIns1Abbr.getDocument().addDocumentListener(this);
        fieldIns2Abbr.getDocument().addDocumentListener(this);

        // bg 1
        JPanel c = new JPanel(new GridLayout(0, 2));
        c.setBorder(new TitledBorder(m_ic.getMessage("BLOOD_GLUCOSE_SETTINGS_1")));
        c.add(new JLabel(m_ic.getMessage("HIGH_BG") + ":"));
        c.add(fieldHighBG1 = new JTextField("" + m_da.getSettings().getBG1_High(), 10));
        c.add(new JLabel(m_ic.getMessage("LOW_BG") + ":"));
        c.add(fieldLowBG1 = new JTextField("" + m_da.getSettings().getBG1_Low(), 10));
        c.add(new JLabel(m_ic.getMessage("TARGET_HIGH_BG") + ":"));
        c.add(fieldTargetHighBG1 = new JTextField("" + m_da.getSettings().getBG1_TargetHigh(), 10));
        c.add(new JLabel(m_ic.getMessage("TARGET_LOW_BG") + ":"));
        c.add(fieldTargetLowBG1 = new JTextField("" + m_da.getSettings().getBG1_TargetLow(), 10));

        fieldHighBG1.getDocument().addDocumentListener(this);
        fieldLowBG1.getDocument().addDocumentListener(this);
        fieldTargetHighBG1.getDocument().addDocumentListener(this);
        fieldTargetLowBG1.getDocument().addDocumentListener(this);

        // bg 2
        JPanel b = new JPanel(new GridLayout(0, 2));
        b.setBorder(new TitledBorder(m_ic.getMessage("BLOOD_GLUCOSE_SETTINGS_2")));
        b.add(new JLabel(m_ic.getMessage("HIGH_BG") + ":"));
        b.add(fieldHighBG2 = new JTextField("" + m_da.getSettings().getBG2_High(), 10));
        b.add(new JLabel(m_ic.getMessage("LOW_BG") + ":"));
        b.add(fieldLowBG2 = new JTextField("" + m_da.getSettings().getBG2_Low(), 10));
        b.add(new JLabel(m_ic.getMessage("TARGET_HIGH_BG") + ":"));
        b.add(fieldTargetHighBG2 = new JTextField("" + m_da.getSettings().getBG2_TargetHigh(), 10));
        b.add(new JLabel(m_ic.getMessage("TARGET_LOW_BG") + ":"));
        b.add(fieldTargetLowBG2 = new JTextField("" + m_da.getSettings().getBG2_TargetLow(), 10));

        fieldHighBG2.getDocument().addDocumentListener(this);
        fieldLowBG2.getDocument().addDocumentListener(this);
        fieldTargetHighBG2.getDocument().addDocumentListener(this);
        fieldTargetLowBG2.getDocument().addDocumentListener(this);

        // bg unit
        JPanel d = new JPanel(new GridLayout(0, 2));
        d.setBorder(new TitledBorder(m_ic.getMessage("BLOOD_GLUCOSE_UNIT_SETTING")));
        d.add(new JLabel(m_ic.getMessage("BG_UNIT") + ":"));
        d.add(cbUnit = new JComboBox(m_da.bg_units));
        cbUnit.setSelectedIndex(settings.getBG_unit() - 1);

        Box i = Box.createVerticalBox();
        i.add(a);
        i.add(d);
        i.add(c);
        i.add(b);

        add(i, BorderLayout.NORTH);

    }

    /**
     * Save Properties
     * 
     * @see ggc.gui.panels.prefs.AbstractPrefOptionsPanel#saveProps()
     */
    @Override
    public void saveProps()
    {
        // insulins
        settings.setIns1Name(fieldIns1Name.getText());
        settings.setIns2Name(fieldIns2Name.getText());
        settings.setIns1Abbr(fieldIns1Abbr.getText());
        settings.setIns2Abbr(fieldIns2Abbr.getText());

        // bg 1
        settings.setBG1_High(m_da.getFloatValue(fieldHighBG1.getText()));
        settings.setBG1_Low(m_da.getFloatValue(fieldLowBG1.getText()));
        settings.setBG1_TargetHigh(m_da.getFloatValue(fieldTargetHighBG1.getText()));
        settings.setBG1_TargetLow(m_da.getFloatValue(fieldTargetLowBG1.getText()));

        // bg 2
        settings.setBG2_High(m_da.getFloatValue(fieldHighBG2.getText()));
        settings.setBG2_Low(m_da.getFloatValue(fieldLowBG2.getText()));
        settings.setBG2_TargetHigh(m_da.getFloatValue(fieldTargetHighBG2.getText()));
        settings.setBG2_TargetLow(m_da.getFloatValue(fieldTargetLowBG2.getText()));

        // unit
        settings.setBG_unit(cbUnit.getSelectedIndex() + 1);

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
        return "pages.GGC_Prefs_MedicalData";
    }

}
