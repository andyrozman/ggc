package ggc.gui.panels.prefs;

import ggc.core.data.cfg.ConfigurationManager;
import ggc.core.util.DataAccess;
import ggc.gui.dialogs.PropertiesDialog;

import java.awt.Component;
import java.awt.event.ItemEvent;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JPanel;
import javax.swing.border.TitledBorder;

import com.atech.graphics.components.JDecimalTextField;
import com.atech.help.HelpCapable;
import com.atech.utils.ATSwingUtils;

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
 *  Description:  Mode Preferences: Software mode and Pen/Injection and Pen settings
 * 
 *  Author: andyrozman {andy@atech-software.com}  
 */

public class PrefModePane extends AbstractPrefOptionsPanel implements HelpCapable
{
    private static final long serialVersionUID = 2814137743820948729L;

    JDecimalTextField[] num_fields;
    JComboBox[] cb_fields;

    private final static int PEN_MAX_BASAL = 0;
    private final static int PEN_MAX_BOLUS = 1;

    private final static int PUMP_MAX_BASAL = 2;
    private final static int PUMP_MAX_BOLUS = 3;

    private final static int PUMP_UNIT_MIN = 4;
    private final static int PUMP_UNIT_MAX = 5;
    private final static int PUMP_UNIT_STEP = 6;

    private final static int PUMP_PROC_MIN = 7;
    private final static int PUMP_PROC_MAX = 8;
    private final static int PUMP_PROC_STEP = 9;

    private final static int SW_MODE = 0;

    private final static int PEN_BASAL_PRECISSION = 1;
    private final static int PEN_BOLUS_PRECISSION = 2;

    private final static int PUMP_BASAL_PRECISSION = 3;
    private final static int PUMP_BOLUS_PRECISSION = 4;

    private final static int PUMP_TBR_TYPE = 5;

    ConfigurationManager config_manager;

    /**
     * Constructor
     * 
     * @param dia
     */
    public PrefModePane(PropertiesDialog dia)
    {
        super(dia);
        config_manager = DataAccess.getInstance().getConfigurationManager();
        ATSwingUtils.initLibrary();
        init();
        // m_da.enableHelp(this);
        // parent = this;
    }

    private void init()
    {
        this.setLayout(null);

        num_fields = new JDecimalTextField[10];
        cb_fields = new JComboBox[6];

        // application mode

        JPanel p1 = new JPanel(); // new GridLayout(2,3));
        p1.setBounds(10, 5, 490, 120);
        p1.setLayout(null);
        p1.setBorder(new TitledBorder(m_ic.getMessage("APPLICATION_MODE")));

        ATSwingUtils
                .getLabel(m_ic.getMessage("APPLICATION_MODE") + ": ", 20, 30, 130, 25, p1, ATSwingUtils.FONT_NORMAL);

        String[] modes = { m_ic.getMessage("PEN_INJECTION_MODE"), m_ic.getMessage("PUMP_MODE") };

        cb_fields[SW_MODE] = ATSwingUtils.getComboBox(modes, 180, 30, 200, 25, p1, ATSwingUtils.FONT_NORMAL);
        cb_fields[SW_MODE].setSelectedIndex(config_manager.getIntValue("SW_MODE"));

        ATSwingUtils.getLabel(m_ic.getMessage("APPLICATION_MODE_DESC"), 20, 60, 440, 50, p1, ATSwingUtils.FONT_NORMAL);

        this.add(p1);

        // pen/injection settings

        JPanel p2 = new JPanel();
        p2.setBounds(10, 130, 490, 95);
        p2.setLayout(null);
        p2.setBorder(new TitledBorder(m_ic.getMessage("PEN_INJECTION_MODE_SETTINGS")));

        String[] pen_precission = { "2", "1", "0.5" };

        ATSwingUtils
                .getLabel(m_ic.getMessage("BASAL_PRECISSION") + ": ", 20, 27, 130, 25, p2, ATSwingUtils.FONT_NORMAL);

        cb_fields[PEN_BASAL_PRECISSION] = ATSwingUtils.getComboBox(pen_precission, 180, 25, 80, 25, p2,
            ATSwingUtils.FONT_NORMAL);
        cb_fields[PEN_BASAL_PRECISSION].setSelectedItem(config_manager.getStringValue("PEN_BASAL_PRECISSION"));

        ATSwingUtils
                .getLabel(m_ic.getMessage("BOLUS_PRECISSION") + ": ", 20, 57, 130, 25, p2, ATSwingUtils.FONT_NORMAL);

        cb_fields[PEN_BOLUS_PRECISSION] = ATSwingUtils.getComboBox(pen_precission, 180, 55, 80, 25, p2,
            ATSwingUtils.FONT_NORMAL);
        cb_fields[PEN_BOLUS_PRECISSION].setSelectedItem(config_manager.getStringValue("PEN_BOLUS_PRECISSION"));

        ATSwingUtils.getLabel(m_ic.getMessage("MAX_BASAL") + ": ", 290, 27, 130, 25, p2, ATSwingUtils.FONT_NORMAL);

        num_fields[PEN_MAX_BASAL] = ATSwingUtils.getNumericTextField(3, 1,
            config_manager.getFloatValue("PEN_MAX_BASAL"), 400, 25, 60, 25, p2);

        ATSwingUtils.getLabel(m_ic.getMessage("MAX_BOLUS") + ": ", 290, 57, 130, 25, p2, ATSwingUtils.FONT_NORMAL);

        num_fields[PEN_MAX_BOLUS] = ATSwingUtils.getNumericTextField(3, 1,
            config_manager.getFloatValue("PUMP_MAX_BOLUS"), 400, 55, 60, 25, p2);

        this.add(p2);

        // pump settings

        JPanel p3 = new JPanel(/* new GridLayout(2, 1) */);
        p3.setBorder(new TitledBorder(m_ic.getMessage("PUMP_MODE_SETTINGS")));
        p3.setBounds(10, 230, 490, 180);
        p3.setLayout(null);

        String[] pump_precission = { "1", "0.5", "0.25", "0.1", "0.025" };

        // pump basal precission
        ATSwingUtils
                .getLabel(m_ic.getMessage("BASAL_PRECISSION") + ": ", 20, 27, 130, 25, p3, ATSwingUtils.FONT_NORMAL);

        cb_fields[PUMP_BASAL_PRECISSION] = ATSwingUtils.getComboBox(pump_precission, 180, 25, 80, 25, p3,
            ATSwingUtils.FONT_NORMAL);
        cb_fields[PUMP_BASAL_PRECISSION].setSelectedItem(config_manager.getStringValue("PUMP_BASAL_PRECISSION"));

        // pump bolus precission
        ATSwingUtils
                .getLabel(m_ic.getMessage("BOLUS_PRECISSION") + ": ", 20, 57, 130, 25, p3, ATSwingUtils.FONT_NORMAL);

        cb_fields[PUMP_BOLUS_PRECISSION] = ATSwingUtils.getComboBox(pump_precission, 180, 55, 80, 25, p3,
            ATSwingUtils.FONT_NORMAL);
        cb_fields[PUMP_BOLUS_PRECISSION].setSelectedItem(config_manager.getStringValue("PUMP_BOLUS_PRECISSION"));

        // pump max basal
        ATSwingUtils.getLabel(m_ic.getMessage("MAX_BASAL") + ": ", 290, 27, 130, 25, p3, ATSwingUtils.FONT_NORMAL);

        num_fields[PUMP_MAX_BASAL] = ATSwingUtils.getNumericTextField(3, 1,
            config_manager.getFloatValue("PUMP_MAX_BASAL"), 400, 25, 60, 25, p3);

        // pump max bolus
        ATSwingUtils.getLabel(m_ic.getMessage("MAX_BOLUS") + ": ", 290, 57, 130, 25, p3, ATSwingUtils.FONT_NORMAL);

        num_fields[PUMP_MAX_BOLUS] = ATSwingUtils.getNumericTextField(3, 1,
            config_manager.getFloatValue("PUMP_MAX_BOLUS"), 400, 55, 60, 25, p3);

        // pump tbr type selector
        ATSwingUtils.getLabel(m_ic.getMessage("TBR_TYPE_SELECTOR") + ": ", 20, 87, 200, 25, p3,
            ATSwingUtils.FONT_NORMAL);

        String[] tbr_type = { m_ic.getMessage("TBR_TYPE_BOTH"), m_ic.getMessage("TBR_TYPE_UNIT"),
                             m_ic.getMessage("TBR_TYPE_PROC") };

        cb_fields[PUMP_TBR_TYPE] = ATSwingUtils.getComboBox(tbr_type, 200, 87, 120, 25, p3, ATSwingUtils.FONT_NORMAL);
        cb_fields[PUMP_TBR_TYPE].setSelectedIndex(config_manager.getIntValue("PUMP_TBR_TYPE"));

        ATSwingUtils.getLabel(m_ic.getMessage("TBR_TYPE_UNIT") + ": ", 20, 117, 200, 25, p3, ATSwingUtils.FONT_NORMAL);

        // pump min unit
        ATSwingUtils.getLabel(m_ic.getMessage("TASK_MIN") + ": ", 110, 117, 200, 25, p3, ATSwingUtils.FONT_NORMAL);

        num_fields[PUMP_UNIT_MIN] = ATSwingUtils.getNumericTextField(3, 1,
            config_manager.getFloatValue("PUMP_UNIT_MIN"), 160, 117, 40, 25, p3);

        // pump max unit
        ATSwingUtils.getLabel(m_ic.getMessage("TASK_MAX") + ": ", 230, 117, 200, 25, p3, ATSwingUtils.FONT_NORMAL);

        num_fields[PUMP_UNIT_MAX] = ATSwingUtils.getNumericTextField(3, 1,
            config_manager.getFloatValue("PUMP_UNIT_MAX"), 280, 117, 40, 25, p3);

        // pump step unit
        ATSwingUtils.getLabel(m_ic.getMessage("TASK_STEP") + ": ", 350, 117, 200, 25, p3, ATSwingUtils.FONT_NORMAL);

        num_fields[PUMP_UNIT_STEP] = ATSwingUtils.getNumericTextField(3, 1,
            config_manager.getFloatValue("PUMP_UNIT_STEP"), 420, 117, 40, 25, p3);

        ATSwingUtils.getLabel(m_ic.getMessage("TBR_TYPE_PROC") + ": ", 20, 147, 200, 25, p3, ATSwingUtils.FONT_NORMAL);

        ATSwingUtils.getLabel(m_ic.getMessage("TASK_MIN") + ": ", 110, 147, 200, 25, p3, ATSwingUtils.FONT_NORMAL);

        // min proc
        num_fields[PUMP_PROC_MIN] = ATSwingUtils.getNumericTextField(3, 1,
            config_manager.getFloatValue("PUMP_PROC_MIN"), 160, 147, 40, 25, p3);

        ATSwingUtils.getLabel(m_ic.getMessage("TASK_MAX") + ": ", 230, 147, 200, 25, p3, ATSwingUtils.FONT_NORMAL);

        // max proc
        num_fields[PUMP_PROC_MAX] = ATSwingUtils.getNumericTextField(3, 1,
            config_manager.getFloatValue("PUMP_PROC_MAX"), 280, 147, 40, 25, p3);

        ATSwingUtils.getLabel(m_ic.getMessage("TASK_STEP") + ": ", 350, 147, 200, 25, p3, ATSwingUtils.FONT_NORMAL);

        // step proc
        num_fields[PUMP_PROC_STEP] = ATSwingUtils.getNumericTextField(3, 1,
            config_manager.getFloatValue("PUMP_PROC_STEP"), 420, 147, 40, 25, p3);

        this.add(p3);

    }

    boolean in_change = false;

    /**
     * Item State Changed
     * 
     * @see ggc.gui.panels.prefs.AbstractPrefOptionsPanel#itemStateChanged(java.awt.event.ItemEvent)
     */
    @Override
    public void itemStateChanged(ItemEvent e)
    {
        /*
         * if (in_change)
         * return;
         * else
         * in_change = true;
         * JComboBox cb = (JComboBox) e.getSource();
         * int index = cb.getSelectedIndex();
         * if (this.cb_lf_type.equals(cb))
         * this.cb_lf_type_class.setSelectedIndex(index);
         * else
         * this.cb_lf_type.setSelectedIndex(index);
         * boolean skin = m_dbc.isSkinLFSelected(index);
         * // this.tf_lf.setEnabled(skin);
         * this.b_browse.setEnabled(skin);
         * in_change = false;
         */
    }

    /**
     * Save Properties
     * 
     * @see ggc.gui.panels.prefs.AbstractPrefOptionsPanel#saveProps()
     */
    @Override
    public void saveProps()
    {

        config_manager.setFloatValue("PUMP_PROC_MIN", m_da.getFloatValue(num_fields[PUMP_PROC_MIN].getValue()));
        config_manager.setFloatValue("PUMP_PROC_MAX", m_da.getFloatValue(num_fields[PUMP_PROC_MAX].getValue()));
        config_manager.setFloatValue("PUMP_PROC_STEP", m_da.getFloatValue(num_fields[PUMP_PROC_STEP].getValue()));

        config_manager.setFloatValue("PUMP_UNIT_MIN", m_da.getFloatValue(num_fields[PUMP_UNIT_MIN].getValue()));
        config_manager.setFloatValue("PUMP_UNIT_MAX", m_da.getFloatValue(num_fields[PUMP_UNIT_MAX].getValue()));
        config_manager.setFloatValue("PUMP_UNIT_STEP", m_da.getFloatValue(num_fields[PUMP_UNIT_STEP].getValue()));

        config_manager.setFloatValue("PEN_MAX_BASAL", m_da.getFloatValue(num_fields[PEN_MAX_BASAL].getValue()));
        config_manager.setFloatValue("PEN_MAX_BOLUS", m_da.getFloatValue(num_fields[PEN_MAX_BOLUS].getValue()));
        config_manager.setFloatValue("PUMP_MAX_BASAL", m_da.getFloatValue(num_fields[PUMP_MAX_BASAL].getValue()));
        config_manager.setFloatValue("PUMP_MAX_BOLUS", m_da.getFloatValue(num_fields[PUMP_MAX_BOLUS].getValue()));

        System.out.println("Mode: " + cb_fields[SW_MODE].getSelectedIndex());

        config_manager.setIntValue("SW_MODE", cb_fields[SW_MODE].getSelectedIndex());

        if (cb_fields[SW_MODE].getSelectedIndex() == 0)
        {
            config_manager.setStringValue("SW_MODE_DESC", "PEN_INJECTION_MODE");
        }
        else
        {
            config_manager.setStringValue("SW_MODE_DESC", "PUMP_MODE");
        }

        config_manager.setStringValue("PEN_BASAL_PRECISSION",
            (String) cb_fields[PEN_BASAL_PRECISSION].getSelectedItem());
        config_manager.setStringValue("PEN_BOLUS_PRECISSION",
            (String) cb_fields[PEN_BOLUS_PRECISSION].getSelectedItem());
        config_manager.setStringValue("PUMP_BASAL_PRECISSION",
            (String) cb_fields[PUMP_BASAL_PRECISSION].getSelectedItem());
        config_manager.setStringValue("PUMP_BOLUS_PRECISSION",
            (String) cb_fields[PUMP_BOLUS_PRECISSION].getSelectedItem());
        config_manager.setIntValue("PUMP_TBR_TYPE", cb_fields[PUMP_TBR_TYPE].getSelectedIndex());

        // key, value).getFloatValue("PUMP_PROC_MAX")
        // num_fields[PUMP_PROC_MAX] =
        // config_manager.getFloatValue("PUMP_PROC_MAX"),

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
        return "GGC_Prefs_Mode";
    }

}
