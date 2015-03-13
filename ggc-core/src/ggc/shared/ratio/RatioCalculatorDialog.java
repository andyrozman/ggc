package ggc.shared.ratio;

import ggc.core.data.cfg.ConfigurationManager;
import ggc.core.util.DataAccess;

import java.awt.Component;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.border.TitledBorder;

import com.atech.graphics.components.JDecimalTextField;
import com.atech.help.HelpCapable;
import com.atech.i18n.I18nControlAbstract;
import com.atech.utils.ATDataAccessAbstract;
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
 *  Filename:     RatioCalculatorDialog  
 *  Description:  Ratio Calculator dialog
 * 
 *  Author: andyrozman {andy@atech-software.com}  
 */

public class RatioCalculatorDialog extends JDialog implements ActionListener, HelpCapable, ItemListener
{

    private static final long serialVersionUID = -1240982985415603758L;

    JComboBox cb_type;
    JDecimalTextField dtf_tdd;
    JLabel db_data_status, lb_ins_carb, lb_ins_bg, lb_ch_bg;
    JPanel[] panels;
    JComboBox cb_time_range, cb_icarb_rule, cb_sens_rule;
    int selected_type = 0;
    JButton[] buttons;
    JButton help_button = null;

    private DataAccess m_da = DataAccess.getInstance();
    private I18nControlAbstract m_ic = m_da.getI18nControlInstance();

    int[] carb_rule = { 500, 450, 300 };
    int[] sens_rule = { 1800, 1500 };

    String[] carb_rule_desc = { "RULE_500", "RULE_450", "RULE_300" };
    String[] sens_rule_desc = { "RULE_1800", "RULE_1500" };

    private boolean m_action_done = false;

    float[] result = null;

    private boolean calculate_only = false;

    // boolean in_action = false;

    // private GGCProperties props = m_da.getSettings();

    // private boolean m_action_done = false;

    JLabel label_title = new JLabel();

    boolean in_process;
    // boolean debug = true;
    // JPanel main_panel = null;

    // private Container m_parent = null;

    ConfigurationManager config_manager = null;

    /**
     * Constructor
     * 
     * @param dialog
     */
    public RatioCalculatorDialog(JFrame dialog)
    {
        super(dialog, "", true);

        // m_parent = dialog;

        setTitle(m_ic.getMessage("RATIO_CALCULATOR"));
        label_title.setText(m_ic.getMessage("RATIO_CALCULATOR"));
        this.config_manager = m_da.getConfigurationManager();

        init();

        this.setVisible(true);

    }

    /**
     * Constructor
     * 
     * @param dialog
     */
    public RatioCalculatorDialog(JDialog dialog)
    {
        super(dialog, "", true);

        // m_parent = dialog;

        setTitle(m_ic.getMessage("RATIO_CALCULATOR"));
        label_title.setText(m_ic.getMessage("RATIO_CALCULATOR"));
        this.config_manager = m_da.getConfigurationManager();

        init();

        this.setVisible(true);

    }

    /**
     * Constructor
     * 
     */
    public RatioCalculatorDialog()
    {
        super();

        // m_parent = dialog;

        this.config_manager = m_da.getConfigurationManager();

        init();

        cb_type.setSelectedIndex(0);
        calculate_only = true;
        // this.setVisible(true);

    }

    /**
     * Get Ratios 
     * 
     * @param tdd
     * @param procent
     * @return
     */
    public float[] getRatios(float tdd, float procent)
    {
        float f = tdd * (procent / 100.0f);
        this.dtf_tdd.setValue(f);

        calculate();

        return this.result;
    }

    private void init()
    {
        int width = 500;
        int height = 625;

        ATSwingUtils.initLibrary();

        m_da.addComponent(this);

        /*
         * Rectangle bnd = m_parent.getBounds();
         * x = (bnd.width/2) + bnd.x - (width/2);
         * y = (bnd.height/2) + bnd.y - (height/2);
         * this.setBounds(x, y, width, height);
         */
        JPanel panel = new JPanel();
        panel.setBounds(0, 0, width, height);
        panel.setLayout(null);

        // main_panel = panel;

        this.getContentPane().add(panel);

        label_title.setFont(ATSwingUtils.getFont(ATSwingUtils.FONT_BIG_BOLD));
        label_title.setHorizontalAlignment(SwingConstants.CENTER);
        label_title.setBounds(0, 15, width, 35);
        panel.add(label_title);

        // JPanel p1 = new JPanel();
        // p1.setBounds(arg0)

        JPanel p1 = ATSwingUtils.getPanel(30, 70, 430, 55, null, new TitledBorder(m_ic.getMessage("TYPE_SOURCE_DATA")),
            panel);

        ATSwingUtils.getLabel(m_ic.getMessage("TYPE_SOURCE_DATA") + ":", 20, 20, 150, 25, p1);

        String[] src_data = { m_ic.getMessage("TYPE_SOURCE_MANUAL"), m_ic.getMessage("TYPE_SOURCE_DB") };

        cb_type = ATSwingUtils.getComboBox(src_data, 180, 20, 130, 25, p1, ATSwingUtils.FONT_NORMAL);
        cb_type.setActionCommand("cb_type");
        cb_type.addItemListener(this);

        panels = new JPanel[5];

        // manual entry
        JPanel p2 = ATSwingUtils.getPanel(30, 130, 430, 55, null,
            new TitledBorder(m_ic.getMessage("TYPE_SOURCE_MANUAL")), panel);

        ATSwingUtils.getLabel(m_ic.getMessage("TDD_FOR_CALCULATION") + ":", 20, 20, 250, 25, p2);
        dtf_tdd = ATSwingUtils.getNumericTextField(4, 1, this.config_manager.getFloatValue("LAST_TDD"), 290, 20, 60,
            25, p2);
        p2.setVisible(true);
        panels[0] = p2;

        int startx = 185;

        // db entry
        JPanel p3 = ATSwingUtils.getPanel(30, 130, 430, 170, null, new TitledBorder(m_ic.getMessage("TYPE_SOURCE_DB")),
            panel);
        ATSwingUtils.getLabel(m_ic.getMessage("RATIO_TIME_SELECT_DESC"), 20, 20, 400, 75, p3, ATSwingUtils.FONT_NORMAL);
        ATSwingUtils.getLabel(m_ic.getMessage("SELECT_RANGE") + ":", 20, 100, 150, 25, p3);
        String range_el[] = { m_ic.getMessage("1_WEEK"), m_ic.getMessage("2_WEEKS"), m_ic.getMessage("3_WEEKS"),
                             m_ic.getMessage("1_MONTH") };
        cb_time_range = ATSwingUtils.getComboBox(range_el, 180, 100, 120, 25, p3, ATSwingUtils.FONT_NORMAL);
        ATSwingUtils.getLabel(m_ic.getMessage("DB_DATA_STATUS") + ":", 20, 130, 150, 25, p3);
        db_data_status = ATSwingUtils.getLabel(m_ic.getMessage("DB_DATA_NOT_READY"), 180, 130, 150, 25, p3);
        ATSwingUtils.getButton(m_ic.getMessage("GET_DATA"), 280, 130, 120, 25, p3, ATSwingUtils.FONT_NORMAL, null,
            "get_db_data", this, m_da);

        p3.setVisible(false);
        panels[1] = p3;
        // int startx = 300;

        JPanel p4 = ATSwingUtils.getPanel(30, startx + 5, 430, 60, null,
            new TitledBorder(m_ic.getMessage("INSULIN_CARB_RATIO")), panel);
        ATSwingUtils.getLabel(m_ic.getMessage("SELECT_RULE") + ":", 20, 20, 160, 25, p4, ATSwingUtils.FONT_NORMAL);
        Object o1[] = { m_ic.getMessage("RULE_500"), m_ic.getMessage("RULE_450"), m_ic.getMessage("RULE_300") };
        cb_icarb_rule = ATSwingUtils.getComboBox(o1, 150, 20, 250, 25, p4, ATSwingUtils.FONT_NORMAL);
        cb_icarb_rule.setSelectedItem(m_ic.getMessage(this.config_manager.getStringValue("INS_CARB_RULE")));
        panels[2] = p4;

        JPanel p5 = ATSwingUtils.getPanel(30, startx + 70, 430, 60, null,
            new TitledBorder(m_ic.getMessage("SENSITIVITY_FACTOR")), panel);
        ATSwingUtils.getLabel(m_ic.getMessage("SELECT_RULE") + ":", 20, 20, 160, 25, p5, ATSwingUtils.FONT_NORMAL);
        Object o2[] = { m_ic.getMessage("RULE_1800"), m_ic.getMessage("RULE_1500") };
        cb_sens_rule = ATSwingUtils.getComboBox(o2, 150, 20, 250, 25, p5, ATSwingUtils.FONT_NORMAL);
        cb_sens_rule.setSelectedItem(m_ic.getMessage(this.config_manager.getStringValue("SENSITIVITY_RULE")));
        panels[3] = p5;

        JPanel p6 = ATSwingUtils.getPanel(30, startx + 135, 430, 110, null,
            new TitledBorder(m_ic.getMessage("CALCULATION")), panel);

        ATSwingUtils.getLabel(m_ic.getMessage("INSULIN_CARB_RATIO") + ":", 20, 20, 160, 25, p6,
            ATSwingUtils.FONT_NORMAL);

        lb_ins_carb = ATSwingUtils.getLabel(
            String.format(m_ic.getMessage("INS_CH_RATIO_PROC"), DataAccess.getFloatAsString(0.0f, 2)), 130, 20, 160,
            25, p6, ATSwingUtils.FONT_NORMAL);

        ATSwingUtils.getLabel(m_ic.getMessage("INS_BG_RATIO") + ":", 20, 45, 160, 25, p6, ATSwingUtils.FONT_NORMAL);

        lb_ins_bg = ATSwingUtils.getLabel(
            String.format(m_ic.getMessage("INS_BG_RATIO_PROC"), DataAccess.getFloatAsString(0.0f, 2),
                m_da.getBGMeasurmentTypeString()), 130, 45, 300, 25, p6, ATSwingUtils.FONT_NORMAL);

        ATSwingUtils.getLabel(m_ic.getMessage("CH_BG_RATIO") + ":", 20, 70, 160, 25, p6, ATSwingUtils.FONT_NORMAL);

        lb_ch_bg = ATSwingUtils.getLabel(
            String.format(m_ic.getMessage("CH_BG_RATIO_PROC"), m_da.getBGMeasurmentTypeString(),
                DataAccess.getFloatAsString(0.0f, 1)), 130, 70, 300, 25, p6, ATSwingUtils.FONT_NORMAL);

        ATSwingUtils.getButton("", 390, 20, 30, 30, p6, ATSwingUtils.FONT_NORMAL, "calculator.png", "calculate", this,
            m_da);

        panels[4] = p6;

        buttons = new JButton[3];

        buttons[0] = ATSwingUtils.getButton("  " + m_ic.getMessage("OK"), 40, startx + 255, 130, 25, panel,
            ATSwingUtils.FONT_NORMAL, "ok.png", "ok", this, m_da);

        buttons[1] = ATSwingUtils.getButton("  " + m_ic.getMessage("CANCEL"), 180, startx + 255, 130, 25, panel,
            ATSwingUtils.FONT_NORMAL, "cancel.png", "cancel", this, m_da);

        help_button = ATSwingUtils.createHelpButtonByBounds(320, startx + 255, 130, 25, this, ATSwingUtils.FONT_NORMAL, m_da);
        buttons[2] = help_button;
        panel.add(help_button);

        m_da.enableHelp(this);

        /*
         * this.cb_icarb_rule = new JComboBox(o1);
         * this.cb_icarb_rule.setBounds(140, 240, 210, 25);
         * panel.add(this.cb_icarb_rule);
         */

        // addLabel(m_ic.getMessage("1_UNIT_INSULIN") + ":", 270, panel);

        this.setBounds(0, 0, width, startx + 215 + 110);
        m_da.centerJDialog(this);
    }

    /**
     * Invoked when an action occurs.
     */
    public void actionPerformed(ActionEvent e)
    {
        String action = e.getActionCommand();

        if (action.equals("cancel"))
        {
            this.dispose();
            m_da.removeComponent(this);
        }
        else if (action.equals("ok"))
        {
            if (result == null)
            {
                ATSwingUtils.showWarningDialog(this, "RATIOS_NOT_DONE", m_ic);
                return;
            }
            else
            {
                this.m_action_done = true;
                m_da.removeComponent(this);
                this.dispose();
            }
        }
        else if (action.equals("get_db_data"))
        {
            m_da.showDialog(this, ATDataAccessAbstract.DIALOG_WARNING, "RATIO_DB_IMPORT_NA");
        }
        else if (action.equals("calculate"))
        {
            calculate();
        }
        else
        {
            System.out.println("RatioDialog::unknown command: " + action);
        }

    }

    /**
     * Check Data Ready
     * 
     * @return
     */
    public boolean checkDataReady()
    {
        if (this.selected_type == 0)
        {
            if (m_da.getFloatValue(this.dtf_tdd.getValue()) <= 0)
            {

                ATSwingUtils.showErrorDialog(this, "TDD_MUST_BE_GREATER_THAN_ZERO", m_ic);
                return false;
            }
            else
                return true;
        }
        else
        {
            // FIXME not implemented
        }
        return false;
    }

    private void calculate()
    {
        if (checkDataReady())
        {
            if (this.selected_type == 0)
            {
                calculate(m_da.getFloatValue(this.dtf_tdd.getValue()));
            }
            else
            {
                ATSwingUtils.showErrorDialog(this, "RATIO_CALCULATOR_NOT_IMPLEMENTED", m_ic);
                // FIXME not implemented
            }
        }
    }

    private void calculate(float tdd)
    {

        result = new float[4];

        float carb_r = carb_rule[cb_icarb_rule.getSelectedIndex()];

        result[0] = carb_r / tdd;

        float sens_r = sens_rule[cb_sens_rule.getSelectedIndex()];

        if (m_da.getBGMeasurmentType() == DataAccess.BG_MMOL)
        {
            sens_r = sens_r / 18.0f;
        }

        result[1] = sens_r / tdd;
        result[2] = result[0] / result[1];
        result[3] = tdd;

        lb_ins_carb.setText(String.format(m_ic.getMessage("INS_CH_RATIO_PROC"),
            DataAccess.getFloatAsString(result[0], 2)));

        lb_ins_bg.setText(String.format(m_ic.getMessage("INS_BG_RATIO_PROC"),
            DataAccess.getFloatAsString(result[1], 2), m_da.getBGMeasurmentTypeString()));

        lb_ch_bg.setText(String.format(m_ic.getMessage("CH_BG_RATIO_PROC"), m_da.getBGMeasurmentTypeString(),
            DataAccess.getFloatAsString(result[2], 1)));

        if (!calculate_only)
        {
            this.config_manager.setFloatValue("LAST_TDD", tdd);
            this.config_manager.setStringValue("INS_CARB_RULE",
                getBaseStringEntry(this.carb_rule_desc, (String) this.cb_icarb_rule.getSelectedItem(), "RULE_500"));
            this.config_manager.setStringValue("SENSITIVITY_RULE",
                getBaseStringEntry(this.sens_rule_desc, (String) this.cb_sens_rule.getSelectedItem(), "RULE_1800"));

            this.config_manager.saveConfig();
        }
    }

    private String getBaseStringEntry(String[] array, String current, String default_value)
    {
        for (String element : array)
        {
            String p = m_ic.getMessage(element);

            if (p.equals(current))
                return element;
        }

        return default_value;

    }

    /*
     * private boolean isFieldSet(String text)
     * {
     * if ((text == null) || (text.trim().length()==0))
     * return false;
     * else
     * return true;
     * }
     */
    /**
     * Action Succesfull
     * 
     * @return
     */
    public boolean actionSuccesful()
    {
        return this.m_action_done;
    }

    /**
     * Get Results
     * 
     * @return array of floats
     */
    public float[] getResults()
    {
        return this.result;
    }

    /*
     * private void fixDecimals()
     * {
     * if (m_da.isEmptyOrUnset(BGField.getText()))
     * return;
     * String s = BGField.getText().trim().replace(",", ".");
     * if (this.cob_bg_type.getSelectedIndex()==1)
     * {
     * try
     * {
     * //System.out.println(s);
     * float f = Float.parseFloat(s);
     * String ss = DataAccess.MmolDecimalFormat.format(f);
     * ss = ss.replace(",", ".");
     * this.BGField.setText(ss);
     * }
     * catch(Exception ex)
     * {
     * System.out.println("fixDecimals: " + ex);
     * }
     * }
     * //MmolDecimalFormat
     * }
     */
    /*
     * public String checkDecimalFields(String field)
     * {
     * field = field.replace(',', '.');
     * return field;
     * }
     */

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
        return this.help_button;
    }

    /** 
     * getHelpId - get id for Help
     */
    public String getHelpId()
    {
        return "GGC_Ratio_Calculator";
    }

    /** 
     * itemStateChanged
     */
    public void itemStateChanged(ItemEvent ie)
    {
        JComboBox cb = (JComboBox) ie.getSource();

        if (cb.getActionCommand().equals("cb_type"))
        {
            setEntryType(cb_type.getSelectedIndex());
        }

        // String source = ie.getSource();

    }

    private void setEntryType(int type)
    {
        if (this.selected_type == type)
            return;

        this.selected_type = type;

        int current = 0;

        if (type == 0)
        {
            panels[0].setVisible(true);
            panels[1].setVisible(false);
            current = 185;
        }
        else
        {
            panels[1].setVisible(true);
            panels[0].setVisible(false);
            current = 300;
        }

        int yx[] = { 5, 70, 135 };

        for (int i = 2, j = 0; i < panels.length; i++, j++)
        {
            Rectangle r = panels[i].getBounds();
            r.y = current + yx[j];
            panels[i].setBounds(r);
        }

        for (JButton button : buttons)
        {
            Rectangle r = button.getBounds();
            r.y = current + 255;
            button.setBounds(r);
        }

        this.setBounds(0, 0, 500, current + 215 + 110);
        m_da.centerJDialog(this);

    }

}
