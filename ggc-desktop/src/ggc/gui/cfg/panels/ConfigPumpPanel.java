package ggc.gui.cfg.panels;

import javax.swing.*;
import javax.swing.border.TitledBorder;

import com.atech.graphics.components.JDecimalTextField;
import com.atech.utils.ATSwingUtils;
import ggc.gui.cfg.PropertiesDialog;

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
 *  Filename:     PrefModePane
 *  Description:  Mode Preferences: Software mode and Pen/Injection and Pen ggcProperties
 * 
 *  Author: andyrozman {andy@atech-software.com}  
 */

public class ConfigPumpPanel extends AbstractPrefOptionsPanel
{

    private static final long serialVersionUID = 2814137743820948729L;

    JDecimalTextField[] numericFields;
    JComboBox[] comboBoxFields;

    // numeric field
    private final static int PUMP_MAX_BASAL = 0;
    private final static int PUMP_MAX_BOLUS = 1;

    private final static int PUMP_UNIT_MIN = 2;
    private final static int PUMP_UNIT_MAX = 3;
    private final static int PUMP_UNIT_STEP = 4;

    private final static int PUMP_PROC_MIN = 5;
    private final static int PUMP_PROC_MAX = 6;
    private final static int PUMP_PROC_STEP = 7;

    // combo box fields
    private final static int PUMP_BASAL_PRECISION = 0;
    private final static int PUMP_BOLUS_PRECISION = 1;

    private final static int PUMP_TBR_TYPE = 2;


    /**
     * Constructor
     *
     * @param parent parent dialog
     */
    public ConfigPumpPanel(PropertiesDialog parent)
    {
        super(parent);
        ATSwingUtils.initLibrary();
        init();
    }


    private void init()
    {
        this.setLayout(null);

        numericFields = new JDecimalTextField[8];
        comboBoxFields = new JComboBox[3];

        // pump settings

        JPanel p3 = new JPanel(/* new GridLayout(2, 1) */);
        p3.setBorder(new TitledBorder(i18nControl.getMessage("PUMP_MODE_SETTINGS")));
        p3.setBounds(10, 5, 490, 180);
        p3.setLayout(null);

        String[] pump_precission = { "1", "0.5", "0.25", "0.1", "0.025" };

        // pump basal precision
        ATSwingUtils.getLabel(i18nControl.getMessage("BASAL_PRECISSION") + ": ", 20, 27, 130, 25, p3,
            ATSwingUtils.FONT_NORMAL);

        comboBoxFields[PUMP_BASAL_PRECISION] = ATSwingUtils.getComboBox(pump_precission, 180, 25, 80, 25, p3,
            ATSwingUtils.FONT_NORMAL);
        comboBoxFields[PUMP_BASAL_PRECISION].setSelectedItem(configurationManagerWrapper.getPumpBasalPrecision());

        // pump bolus precission
        ATSwingUtils.getLabel(i18nControl.getMessage("BOLUS_PRECISSION") + ": ", 20, 57, 130, 25, p3,
            ATSwingUtils.FONT_NORMAL);

        comboBoxFields[PUMP_BOLUS_PRECISION] = ATSwingUtils.getComboBox(pump_precission, 180, 55, 80, 25, p3,
            ATSwingUtils.FONT_NORMAL);
        comboBoxFields[PUMP_BOLUS_PRECISION].setSelectedItem(configurationManagerWrapper.getPumpBolusPrecision());

        // pump max basal
        ATSwingUtils.getLabel(i18nControl.getMessage("MAX_BASAL") + ": ", 290, 27, 130, 25, p3,
            ATSwingUtils.FONT_NORMAL);

        numericFields[PUMP_MAX_BASAL] = ATSwingUtils.getNumericTextField(3, 1,
            configurationManagerWrapper.getPumpMaxBasal(), 400, 25, 60, 25, p3);

        // pump max bolus
        ATSwingUtils.getLabel(i18nControl.getMessage("MAX_BOLUS") + ": ", 290, 57, 130, 25, p3,
            ATSwingUtils.FONT_NORMAL);

        numericFields[PUMP_MAX_BOLUS] = ATSwingUtils.getNumericTextField(3, 1,
            configurationManagerWrapper.getPumpMaxBolus(), 400, 55, 60, 25, p3);

        // pump tbr type selector
        ATSwingUtils.getLabel(i18nControl.getMessage("TBR_TYPE_SELECTOR") + ": ", 20, 87, 200, 25, p3,
            ATSwingUtils.FONT_NORMAL);

        String[] tbr_type = { i18nControl.getMessage("TBR_TYPE_BOTH"), i18nControl.getMessage("TBR_TYPE_UNIT"),
                              i18nControl.getMessage("TBR_TYPE_PROC") };

        comboBoxFields[PUMP_TBR_TYPE] = ATSwingUtils.getComboBox(tbr_type, 200, 87, 120, 25, p3,
            ATSwingUtils.FONT_NORMAL);
        comboBoxFields[PUMP_TBR_TYPE].setSelectedIndex(configurationManagerWrapper.getPumpTBRType());

        ATSwingUtils.getLabel(i18nControl.getMessage("TBR_TYPE_UNIT") + ": ", 20, 117, 200, 25, p3,
            ATSwingUtils.FONT_NORMAL);

        // pump min unit
        ATSwingUtils.getLabel(i18nControl.getMessage("TASK_MIN") + ": ", 110, 117, 200, 25, p3,
            ATSwingUtils.FONT_NORMAL);

        numericFields[PUMP_UNIT_MIN] = ATSwingUtils.getNumericTextField(3, 1,
            configurationManagerWrapper.getTBRUnitMin(), 160, 117, 40, 25, p3);

        // pump max unit
        ATSwingUtils.getLabel(i18nControl.getMessage("TASK_MAX") + ": ", 230, 117, 200, 25, p3,
            ATSwingUtils.FONT_NORMAL);

        numericFields[PUMP_UNIT_MAX] = ATSwingUtils.getNumericTextField(3, 1,
            configurationManagerWrapper.getTBRUnitMax(), 280, 117, 40, 25, p3);

        // pump step unit
        ATSwingUtils.getLabel(i18nControl.getMessage("TASK_STEP") + ": ", 350, 117, 200, 25, p3,
            ATSwingUtils.FONT_NORMAL);

        numericFields[PUMP_UNIT_STEP] = ATSwingUtils.getNumericTextField(3, 1,
            configurationManagerWrapper.getTBRUnitStep(), 420, 117, 40, 25, p3);

        ATSwingUtils.getLabel(i18nControl.getMessage("TBR_TYPE_PROC") + ": ", 20, 147, 200, 25, p3,
            ATSwingUtils.FONT_NORMAL);

        ATSwingUtils.getLabel(i18nControl.getMessage("TASK_MIN") + ": ", 110, 147, 200, 25, p3,
            ATSwingUtils.FONT_NORMAL);

        // min proc
        numericFields[PUMP_PROC_MIN] = ATSwingUtils.getNumericTextField(3, 1,
            configurationManagerWrapper.getTBRProcentMin(), 160, 147, 40, 25, p3);

        ATSwingUtils.getLabel(i18nControl.getMessage("TASK_MAX") + ": ", 230, 147, 200, 25, p3,
            ATSwingUtils.FONT_NORMAL);

        // max proc
        numericFields[PUMP_PROC_MAX] = ATSwingUtils.getNumericTextField(3, 1,
            configurationManagerWrapper.getTBRProcentMax(), 280, 147, 40, 25, p3);

        ATSwingUtils.getLabel(i18nControl.getMessage("TASK_STEP") + ": ", 350, 147, 200, 25, p3,
            ATSwingUtils.FONT_NORMAL);

        // step proc
        numericFields[PUMP_PROC_STEP] = ATSwingUtils.getNumericTextField(3, 1,
            configurationManagerWrapper.getTBRProcentStep(), 420, 147, 40, 25, p3);

        this.add(p3);

        this.registerForChange(this.comboBoxFields);
        this.registerForChange(this.numericFields);

    }


    /**
     * {@inheritDoc}
     */
    @Override
    public void saveProps()
    {
        configurationManagerWrapper.setTBRProcentMin(getFloatValue(numericFields[PUMP_PROC_MIN].getValue()));
        configurationManagerWrapper.setTBRProcentMax(getFloatValue(numericFields[PUMP_PROC_MAX].getValue()));
        configurationManagerWrapper.setTBRProcentStep(getFloatValue(numericFields[PUMP_PROC_STEP].getValue()));

        configurationManagerWrapper.setTBRUnitMin(getFloatValue(numericFields[PUMP_UNIT_MIN].getValue()));
        configurationManagerWrapper.setTBRUnitMax(getFloatValue(numericFields[PUMP_UNIT_MAX].getValue()));
        configurationManagerWrapper.setTBRUnitStep(getFloatValue(numericFields[PUMP_UNIT_STEP].getValue()));

        configurationManagerWrapper.setPumpMaxBasal(getFloatValue(numericFields[PUMP_MAX_BASAL].getValue()));
        configurationManagerWrapper.setPumpMaxBolus(getFloatValue(numericFields[PUMP_MAX_BOLUS].getValue()));

        configurationManagerWrapper
                .setPumpBasalPrecision((String) comboBoxFields[PUMP_BASAL_PRECISION].getSelectedItem());
        configurationManagerWrapper
                .setPumpBolusPrecision((String) comboBoxFields[PUMP_BOLUS_PRECISION].getSelectedItem());
        configurationManagerWrapper.setPumpTBRType(comboBoxFields[PUMP_TBR_TYPE].getSelectedIndex());
    }


    private float getFloatValue(Object value)
    {
        return dataAccess.getFloatValue(value);
    }


    /**
     * {@inheritDoc}
     */
    public String getHelpId()
    {
        // FIXME
        return "GGC_Prefs_Mode";
    }

}
