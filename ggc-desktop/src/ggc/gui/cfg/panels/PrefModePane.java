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

public class PrefModePane extends AbstractPrefOptionsPanel
{

    private static final long serialVersionUID = 2814137743820948729L;

    JDecimalTextField[] numericFields;
    JComboBox[] comboBoxFields;

    private final static int PEN_MAX_BASAL = 0;
    private final static int PEN_MAX_BOLUS = 1;

    private final static int SW_MODE = 0;

    private final static int PEN_BASAL_PRECISSION = 1;
    private final static int PEN_BOLUS_PRECISSION = 2;


    /**
     * Constructor
     * 
     * @param dia
     */
    public PrefModePane(PropertiesDialog dia)
    {
        super(dia);
        ATSwingUtils.initLibrary();
        init();
    }


    private void init()
    {
        this.setLayout(null);

        numericFields = new JDecimalTextField[2];
        comboBoxFields = new JComboBox[3];

        // application mode

        JPanel p1 = new JPanel(); // new GridLayout(2,3));
        p1.setBounds(10, 5, 490, 120);
        p1.setLayout(null);
        p1.setBorder(new TitledBorder(i18nControl.getMessage("APPLICATION_MODE")));

        ATSwingUtils.getLabel(i18nControl.getMessage("APPLICATION_MODE") + ": ", 20, 30, 130, 25, p1,
            ATSwingUtils.FONT_NORMAL);

        String[] modes = { i18nControl.getMessage("PEN_INJECTION_MODE"), i18nControl.getMessage("PUMP_MODE") };

        comboBoxFields[SW_MODE] = ATSwingUtils.getComboBox(modes, 180, 30, 200, 25, p1, ATSwingUtils.FONT_NORMAL);
        comboBoxFields[SW_MODE].setSelectedIndex(configurationManagerWrapper.getSoftwareMode());

        ATSwingUtils.getLabel(i18nControl.getMessage("APPLICATION_MODE_DESC"), 20, 60, 440, 50, p1,
            ATSwingUtils.FONT_NORMAL);

        this.add(p1);

        // pen/injection settings

        JPanel p2 = new JPanel();
        p2.setBounds(10, 130, 490, 95);
        p2.setLayout(null);
        p2.setBorder(new TitledBorder(i18nControl.getMessage("PEN_INJECTION_MODE_SETTINGS")));

        String[] pen_precission = { "2", "1", "0.5" };

        ATSwingUtils.getLabel(i18nControl.getMessage("BASAL_PRECISSION") + ": ", 20, 27, 130, 25, p2,
            ATSwingUtils.FONT_NORMAL);

        comboBoxFields[PEN_BASAL_PRECISSION] = ATSwingUtils.getComboBox(pen_precission, 180, 25, 80, 25, p2,
            ATSwingUtils.FONT_NORMAL);
        comboBoxFields[PEN_BASAL_PRECISSION].setSelectedItem(configurationManagerWrapper.getPenBasalPrecision());

        ATSwingUtils.getLabel(i18nControl.getMessage("BOLUS_PRECISSION") + ": ", 20, 57, 130, 25, p2,
            ATSwingUtils.FONT_NORMAL);

        comboBoxFields[PEN_BOLUS_PRECISSION] = ATSwingUtils.getComboBox(pen_precission, 180, 55, 80, 25, p2,
            ATSwingUtils.FONT_NORMAL);
        comboBoxFields[PEN_BOLUS_PRECISSION].setSelectedItem(configurationManagerWrapper.getPenBolusPrecision());

        ATSwingUtils.getLabel(i18nControl.getMessage("MAX_BASAL") + ": ", 290, 27, 130, 25, p2,
            ATSwingUtils.FONT_NORMAL);

        numericFields[PEN_MAX_BASAL] = ATSwingUtils.getNumericTextField(3, 1,
            configurationManagerWrapper.getPenMaxBasal(), 400, 25, 60, 25, p2);

        ATSwingUtils.getLabel(i18nControl.getMessage("MAX_BOLUS") + ": ", 290, 57, 130, 25, p2,
            ATSwingUtils.FONT_NORMAL);

        numericFields[PEN_MAX_BOLUS] = ATSwingUtils.getNumericTextField(3, 1,
            configurationManagerWrapper.getPenMaxBolus(), 400, 55, 60, 25, p2);

        this.add(p2);

        this.registerForChange(this.comboBoxFields);
        this.registerForChange(this.numericFields);
    }

    boolean in_change = false;


    /**
     * {@inheritDoc}
     */
    @Override
    public void saveProps()
    {
        // mode
        // System.out.println("Mode: " +
        // comboBoxFields[SW_MODE].getSelectedIndex());

        configurationManagerWrapper.setSoftwareMode(comboBoxFields[SW_MODE].getSelectedIndex());

        if (comboBoxFields[SW_MODE].getSelectedIndex() == 0)
        {
            configurationManagerWrapper.setSoftwareModeDescription("PEN_INJECTION_MODE");
        }
        else
        {
            configurationManagerWrapper.setSoftwareModeDescription("PUMP_MODE");
        }

        // pen settings
        configurationManagerWrapper
                .setPenBasalPrecision((String) comboBoxFields[PEN_BASAL_PRECISSION].getSelectedItem());
        configurationManagerWrapper
                .setPenBolusPrecision((String) comboBoxFields[PEN_BOLUS_PRECISSION].getSelectedItem());

        configurationManagerWrapper.setPenMaxBasal(dataAccess.getFloatValue(numericFields[PEN_MAX_BASAL].getValue()));
        configurationManagerWrapper.setPenMaxBolus(dataAccess.getFloatValue(numericFields[PEN_MAX_BOLUS].getValue()));

    }


    /**
     * {@inheritDoc}
     */
    public String getHelpId()
    {
        return "GGC_Prefs_Mode";
    }

}
