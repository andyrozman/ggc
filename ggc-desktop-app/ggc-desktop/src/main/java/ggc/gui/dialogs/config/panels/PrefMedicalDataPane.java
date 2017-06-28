package ggc.gui.dialogs.config.panels;

import java.awt.*;
import java.awt.event.ItemEvent;

import javax.swing.*;
import javax.swing.border.TitledBorder;

import com.atech.i18n.I18nControlAbstract;

import ggc.core.data.defs.GlucoseUnitType;
import ggc.core.util.DataAccess;
import ggc.gui.dialogs.config.PropertiesDialog;

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

public class PrefMedicalDataPane extends AbstractPrefOptionsPanel
{

    private static final long serialVersionUID = 4645565336749757311L;

    // insulins
    private JTextField fieldIns1Name, fieldIns2Name, tb_ins3_name, tb_pump_insulin; // ,
                                                                                    // fieldIns1Abbr,
                                                                                    // fieldIns2Abbr;

    // bg (mg/dl)
    private JTextField fieldHighBG1, fieldLowBG1, fieldTargetHighBG1, fieldTargetLowBG1;

    // bg (mmol/l)
    private JTextField fieldHighBG2, fieldLowBG2, fieldTargetHighBG2, fieldTargetLowBG2;

    private JComboBox cb_ins1_type, cb_ins2_type, cb_ins3_type, cb_ratio_type;

    I18nControlAbstract ic = DataAccess.getInstance().getI18nControlInstance();

    private String[] insulin_settings = { i18nControl.getMessage("NOT_USED"), //
                                          i18nControl.getMessage("BASAL_INSULIN"), //
                                          i18nControl.getMessage("BOLUS_INSULIN") };

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
    }


    private void init()
    {
        setLayout(new BorderLayout());

        JPanel a = new JPanel(new GridLayout(0, 2));
        a.setBorder(new TitledBorder(i18nControl.getMessage("INSULIN_SETTINGS")));
        a.add(new JLabel(i18nControl.getMessage("INSULIN") + " 1 " + i18nControl.getMessage("NAME") + ":"));
        a.add(fieldIns1Name = new JTextField(configurationManagerWrapper.getIns1Name(), 20));
        a.add(new JLabel(i18nControl.getMessage("INSULIN") + " 1 " + i18nControl.getMessage("SETTINGS") + ":"));
        a.add(this.cb_ins1_type = new JComboBox(this.insulin_settings));
        this.cb_ins1_type.setSelectedIndex(configurationManagerWrapper.getIns1Type());

        a.add(new JLabel(i18nControl.getMessage("INSULIN") + " 2 " + i18nControl.getMessage("NAME") + ":"));
        a.add(fieldIns2Name = new JTextField(configurationManagerWrapper.getIns2Name(), 20));
        a.add(new JLabel(i18nControl.getMessage("INSULIN") + " 2 " + i18nControl.getMessage("SETTINGS") + ":"));

        a.add(this.cb_ins2_type = new JComboBox(this.insulin_settings));
        this.cb_ins2_type.setSelectedIndex(configurationManagerWrapper.getIns2Type());

        a.add(new JLabel(i18nControl.getMessage("INSULIN") + " 3 " + i18nControl.getMessage("NAME") + ":"));
        a.add(this.tb_ins3_name = new JTextField(configurationManagerWrapper.getIns3Name(), 20));
        a.add(new JLabel(i18nControl.getMessage("INSULIN") + " 3 " + i18nControl.getMessage("SETTINGS") + ":"));
        a.add(this.cb_ins3_type = new JComboBox(this.insulin_settings));
        this.cb_ins3_type.setSelectedIndex(configurationManagerWrapper.getIns3Type());

        a.add(new JLabel(i18nControl.getMessage("PUMP_INSULIN") + ":"));
        a.add(this.tb_pump_insulin = new JTextField(configurationManagerWrapper.getPumpInsulin(), 20));

        fieldIns1Name.getDocument().addDocumentListener(this);
        fieldIns2Name.getDocument().addDocumentListener(this);
        tb_pump_insulin.getDocument().addDocumentListener(this);
        tb_ins3_name.getDocument().addDocumentListener(this);

        JLabel l;

        // bg 1
        JPanel c = new JPanel(new GridLayout(0, 3));
        c.setBorder(new TitledBorder(i18nControl.getMessage("BLOOD_GLUCOSE_SETTINGS_1")));
        c.add(new JLabel());
        c.add(l = new JLabel("mg/dL"));
        l.setHorizontalAlignment(SwingConstants.CENTER);
        c.add(l = new JLabel("mmol/L"));
        l.setHorizontalAlignment(SwingConstants.CENTER);

        c.add(new JLabel(i18nControl.getMessage("HIGH_BG") + ":"));
        c.add(fieldHighBG1 = new JTextField("" + configurationManagerWrapper.getBG1High(), 10));
        c.add(fieldHighBG2 = new JTextField("" + configurationManagerWrapper.getBG2High(), 10));

        c.add(new JLabel(i18nControl.getMessage("LOW_BG") + ":"));
        c.add(fieldLowBG1 = new JTextField("" + configurationManagerWrapper.getBG1Low(), 10));
        c.add(fieldLowBG2 = new JTextField("" + configurationManagerWrapper.getBG2Low(), 10));

        c.add(new JLabel(i18nControl.getMessage("TARGET_HIGH_BG") + ":"));
        c.add(fieldTargetHighBG1 = new JTextField("" + configurationManagerWrapper.getBG1TargetHigh(), 10));
        c.add(fieldTargetHighBG2 = new JTextField("" + configurationManagerWrapper.getBG2TargetHigh(), 10));

        c.add(new JLabel(i18nControl.getMessage("TARGET_LOW_BG") + ":"));
        c.add(fieldTargetLowBG1 = new JTextField("" + configurationManagerWrapper.getBG1TargetLow(), 10));
        c.add(fieldTargetLowBG2 = new JTextField("" + configurationManagerWrapper.getBG2TargetLow(), 10));

        fieldHighBG1.getDocument().addDocumentListener(this);
        fieldLowBG1.getDocument().addDocumentListener(this);
        fieldTargetHighBG1.getDocument().addDocumentListener(this);
        fieldTargetLowBG1.getDocument().addDocumentListener(this);
        fieldHighBG2.getDocument().addDocumentListener(this);
        fieldLowBG2.getDocument().addDocumentListener(this);
        fieldTargetHighBG2.getDocument().addDocumentListener(this);
        fieldTargetLowBG2.getDocument().addDocumentListener(this);

        // bg unit
        JPanel d = new JPanel(new GridLayout(0, 2));
        d.setBorder(new TitledBorder(i18nControl.getMessage("BLOOD_GLUCOSE_UNIT_SETTING")));
        d.add(new JLabel(i18nControl.getMessage("BG_UNIT") + ":"));
        d.add(cbUnit = new JComboBox(GlucoseUnitType.getDescriptions())); // dataAccess.bg_units_config

        GlucoseUnitType glucoseUnitType = configurationManagerWrapper.getGlucoseUnit();

        if ((glucoseUnitType != null)
                && (glucoseUnitType == GlucoseUnitType.mg_dL || glucoseUnitType == GlucoseUnitType.mmol_L))
        {
            cbUnit.setSelectedIndex(glucoseUnitType.getCode() - 1);
        }
        else
        {
            cbUnit.setSelectedIndex(0);
        }

        cbUnit.addItemListener(this);

        // ratio type

        String[] rat_type = { ic.getMessage("RATIO_MODE_BASE"), ic.getMessage("RATIO_MODE_EXTENDED") };

        JPanel e = new JPanel(new GridLayout(0, 2));
        e.setBorder(new TitledBorder(i18nControl.getMessage("RATIO_TYPE_SETTING")));
        e.add(new JLabel(i18nControl.getMessage("RATIO_TYPE") + ":"));
        e.add(cb_ratio_type = new JComboBox(rat_type));

        if (configurationManagerWrapper.getRatioMode().equals("")
                || configurationManagerWrapper.getRatioMode().equals("Base"))
        {
            cb_ratio_type.setSelectedIndex(0);
        }
        else
        {
            cb_ratio_type.setSelectedIndex(1);
        }

        Box i = Box.createVerticalBox();
        i.add(a);
        i.add(c);
        i.add(d);
        i.add(e);

        add(i, BorderLayout.NORTH);

    }


    /**
     * {@inheritDoc}
     */
    @Override
    public void itemStateChanged(ItemEvent e)
    {
        changed = true;
        parent.updateGlucoseUnitType(GlucoseUnitType.getByCode(cbUnit.getSelectedIndex() + 1));
    }


    /**
     * {@inheritDoc}
     */
    @Override
    public void saveProps()
    {
        // insulins
        configurationManagerWrapper.setIns1Name(fieldIns1Name.getText());
        configurationManagerWrapper.setIns1Type(this.cb_ins1_type.getSelectedIndex());

        configurationManagerWrapper.setIns2Name(fieldIns2Name.getText());
        configurationManagerWrapper.setIns2Type(this.cb_ins2_type.getSelectedIndex());

        configurationManagerWrapper.setIns3Name(this.tb_ins3_name.getText());
        configurationManagerWrapper.setIns3Type(this.cb_ins3_type.getSelectedIndex());

        configurationManagerWrapper.setPumpInsulin(this.tb_pump_insulin.getText());

        // bg 1
        configurationManagerWrapper.setBG1High(dataAccess.getFloatValue(fieldHighBG1.getText()));
        configurationManagerWrapper.setBG1Low(dataAccess.getFloatValue(fieldLowBG1.getText()));
        configurationManagerWrapper.setBG1TargetHigh(dataAccess.getFloatValue(fieldTargetHighBG1.getText()));
        configurationManagerWrapper.setBG1TargetLow(dataAccess.getFloatValue(fieldTargetLowBG1.getText()));

        // bg 2
        configurationManagerWrapper.setBG2High(dataAccess.getFloatValue(fieldHighBG2.getText()));
        configurationManagerWrapper.setBG2Low(dataAccess.getFloatValue(fieldLowBG2.getText()));
        configurationManagerWrapper.setBG2TargetHigh(dataAccess.getFloatValue(fieldTargetHighBG2.getText()));
        configurationManagerWrapper.setBG2TargetLow(dataAccess.getFloatValue(fieldTargetLowBG2.getText()));

        // unit
        configurationManagerWrapper.setGlucoseUnit(GlucoseUnitType.getByCode(cbUnit.getSelectedIndex() + 1));

        // ratio mode
        if (this.cb_ratio_type.getSelectedIndex() == 0)
        {
            configurationManagerWrapper.setRatioMode("Base");
        }
        else
        {
            configurationManagerWrapper.setRatioMode("Extended");
        }

    }


    /**
     * {@inheritDoc}
     */
    public String getHelpId()
    {
        return "GGC_Prefs_MedicalData";
    }

}
