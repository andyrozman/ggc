package ggc.gui.dialogs.ratio;

import java.awt.*;
import java.awt.event.*;

import javax.swing.*;

import com.atech.graphics.components.JDecimalTextField;
import com.atech.help.HelpCapable;
import com.atech.i18n.I18nControlAbstract;
import com.atech.utils.ATSwingUtils;

import ggc.core.data.cfg.ConfigurationManagerWrapper;
import ggc.core.util.DataAccess;

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
 *  Filename:     RatioBaseDialog  
 *  Description:  This is dialog for setting Base ratio (one for whole day). This is usable
 *                for pen/injection therapy, for pump therapy you will need to use extended 
 *                dialog.
 * 
 *  Author: Andy {andy@atech-software.com}  
 */

public class RatioBaseDialog extends JDialog implements ActionListener, KeyListener, HelpCapable, FocusListener
{

    private static final long serialVersionUID = -1240982985415603758L;
    JDecimalTextField dtf_ch_ins, dtf_ins_bg, dtf_bg_ch;

    boolean in_action = false;

    private DataAccess m_da = DataAccess.getInstance();
    private I18nControlAbstract m_ic = m_da.getI18nControlInstance();

    private boolean m_actionDone = false;

    JLabel label_title = new JLabel();

    boolean in_process;
    boolean debug = true;
    JButton help_button = null;
    JPanel main_panel = null;
    RatioEntry ratio_entry = null;

    ConfigurationManagerWrapper configurationManagerWrapper = m_da.getConfigurationManagerWrapper();


    /**
     * Constructor
     * 
     * @param dialog
     */
    public RatioBaseDialog(JFrame dialog)
    {
        super(dialog, "", true);

        // m_parent = dialog;
        m_da.addComponent(this);

        init();
        load();

        this.setVisible(true);

    }


    /**
     * Constructor
     * 
     * @param dialog
     * @param re 
     */
    public RatioBaseDialog(JFrame dialog, RatioEntry re)
    {
        super(dialog, "", true);

        // m_parent = dialog;
        m_da.addComponent(this);

        init();
        load();

        this.setVisible(true);

    }


    /**
     * Load data
     */
    private void load()
    {
        this.dtf_ch_ins.setValue(this.configurationManagerWrapper.getRatioCHInsulin());
        this.dtf_ins_bg.setValue(this.configurationManagerWrapper.getRatioBGInsulin());
        calculateRatio(RATIO_BG_CH);
    }


    /**
     * Save data
     */
    private void save()
    {
        this.configurationManagerWrapper.setRatioCHInsulin(m_da.getFloatValue(this.dtf_ch_ins.getCurrentValue()));
        this.configurationManagerWrapper.setRatioBGInsulin(m_da.getFloatValue(this.dtf_ins_bg.getCurrentValue()));

        this.configurationManagerWrapper.saveConfig();
    }


    private void init()
    {

        ATSwingUtils.initLibrary();

        /*
         * int x = 0;
         * int y = 0;
         * int width = 400;
         * int height = 500;
         * Rectangle bnd = m_parent.getBounds();
         * x = (bnd.width/2) + bnd.x - (width/2);
         * y = (bnd.height/2) + bnd.y - (height/2);
         */

        this.setBounds(0, 0, 300, 340);

        m_da.centerJDialog(this);

        JPanel panel = new JPanel();
        panel.setBounds(0, 0, 300, 400);
        panel.setLayout(null);

        main_panel = panel;

        this.getContentPane().add(panel);

        label_title = ATSwingUtils.getTitleLabel("", 0, 15, 300, 35, panel, ATSwingUtils.FONT_BIG_BOLD);

        setTitle(m_ic.getMessage("RATIO_BASE"));
        label_title.setText(m_ic.getMessage("RATIO_BASE"));

        ATSwingUtils.getLabel(m_ic.getMessage("INSULIN_CARB_RATIO"), 30, 80, 150, 25, panel,
            ATSwingUtils.FONT_NORMAL_BOLD);
        dtf_ch_ins = ATSwingUtils.getNumericTextField(2, new Float(0.0f), 180, 80, 80, 25, panel);
        dtf_ch_ins.addFocusListener(this);
        dtf_ch_ins.addKeyListener(this);

        ATSwingUtils.getLabel(m_ic.getMessage("SENSITIVITY_FACTOR_LONG"), 30, 120, 150, 45, panel,
            ATSwingUtils.FONT_NORMAL_BOLD);
        dtf_ins_bg = ATSwingUtils.getNumericTextField(2, new Float(0.0f), 180, 130, 80, 25, panel);
        dtf_ins_bg.addFocusListener(this);
        dtf_ins_bg.addKeyListener(this);

        ATSwingUtils.getLabel(m_ic.getMessage("BG_OH_RATIO"), 30, 180, 150, 25, panel, ATSwingUtils.FONT_NORMAL_BOLD);
        dtf_bg_ch = ATSwingUtils.getNumericTextField(2, new Float(0.0f), 180, 180, 80, 25, panel);
        dtf_bg_ch.addFocusListener(this);
        dtf_bg_ch.addKeyListener(this);

        ATSwingUtils.getButton("", 30, 220, 30, 30, panel, ATSwingUtils.FONT_NORMAL, "calculator.png", "calculator",
            this, m_da);

        ATSwingUtils.getButton("  " + m_ic.getMessage("OK"), 30, 260, 110, 25, panel, ATSwingUtils.FONT_NORMAL,
            "ok.png", "ok", this, m_da);

        ATSwingUtils.getButton("  " + m_ic.getMessage("CANCEL"), 150, 260, 110, 25, panel, ATSwingUtils.FONT_NORMAL,
            "cancel.png", "cancel", this, m_da);

        help_button = ATSwingUtils.createHelpButtonByBounds(150, 230, 110, 25, this, ATSwingUtils.FONT_NORMAL, m_da);

        panel.add(help_button);

        m_da.enableHelp(this);

    }

    private static final int RATIO_CH_INSULIN = 1;
    private static final int RATIO_BG_INSULIN = 2;
    private static final int RATIO_BG_CH = 3;


    private void calculateRatio(Object obj)
    {
        if (obj.equals(this.dtf_ch_ins))
        {
            calculateRatio(RATIO_CH_INSULIN);
        }
        else if (obj.equals(this.dtf_ins_bg))
        {
            calculateRatio(RATIO_BG_INSULIN);
        }
        else if (obj.equals(this.dtf_bg_ch))
        {
            calculateRatio(RATIO_BG_CH);
        }
    }


    private void calculateRatio(int type)
    {
        // System.out.println("calculate Ratio: " + type);

        float v1 = this.m_da.getFloatValue(this.dtf_ch_ins.getCurrentValue());
        float v2 = this.m_da.getFloatValue(this.dtf_ins_bg.getCurrentValue());
        float v3 = this.m_da.getFloatValue(this.dtf_bg_ch.getCurrentValue());

        if (type == RATIO_CH_INSULIN)
        {
            if (checkSet(v1, v2))
            {
                float v4 = v1 / v2;
                // System.out.println("calculate Ratio [type=" + type +
                // ",check=1,2;value=" + v4);
                this.dtf_bg_ch.setValue(new Float(v4));
            }
            else if (checkSet(v1, v3))
            {
                float v4 = v1 / v3;
                // System.out.println("calculate Ratio [type=" + type +
                // ",check=1,3;value=" + v4);
                this.dtf_ins_bg.setValue(new Float(v4));
            }
            // else
            // System.out.println("calculate Ratio [type=" + type +
            // ",check NO");

        }
        else if (type == RATIO_BG_INSULIN)
        {
            if (checkSet(v2, v1))
            {
                float v4 = v1 / v2;
                // System.out.println("calculate Ratio [type=" + type +
                // ",check=2,1;value=" + v4);
                this.dtf_bg_ch.setValue(new Float(v4));
            }
            else if (checkSet(v2, v3))
            {
                float v4 = v2 * v3;
                // System.out.println("calculate Ratio [type=" + type +
                // ",check=2,3;value=" + v4);
                this.dtf_ch_ins.setValue(new Float(v4));
            }
            // else
            // System.out.println("calculate Ratio [type=" + type +
            // ",check NO");
        }
        else
        {
            if (checkSet(v1, v2))
            {
                float v4 = v1 / v2;
                // System.out.println("calculate Ratio [type=" + type +
                // ",check=3,1;value=" + v4);
                this.dtf_bg_ch.setValue(v4);
            }
            else if (checkSet(v3, v2))
            {
                float v4 = v2 * v3;
                // System.out.println("calculate Ratio [type=" + type +
                // ",check=3,2;value=" + v4);
                this.dtf_ch_ins.setValue(new Float(v4));
            }
            // else
            // System.out.println("calculate Ratio [type=" + type +
            // ",check NO");
        }

        // JDecimalTextField dtf_ch_ins, dtf_ins_bg, dtf_bg_ch;

    }


    private boolean checkSet(float v1, float v2)
    {
        // System.out.println("checkSet [v1=" + v1 + ",v2=" + v2 + "]");
        if (v1 != 0.0f && v2 != 0.0f)
            return true;
        else
            return false;
    }


    /**
     * Invoked when an action occurs.
     */
    public void actionPerformed(ActionEvent e)
    {
        String action = e.getActionCommand();

        if (action.equals("cancel"))
        {
            m_da.removeComponent(this);
            this.dispose();
        }
        else if (action.equals("ok"))
        {
            float v1 = this.m_da.getFloatValue(this.dtf_ch_ins.getCurrentValue());
            float v2 = this.m_da.getFloatValue(this.dtf_ins_bg.getCurrentValue());

            if (this.checkSet(v1, v2))
            {
                m_da.removeComponent(this);
                cmdOk();
                this.dispose();
            }
            else
            {
                JOptionPane.showMessageDialog(this, m_ic.getMessage("FILL_RATIO_ENTRIES"), m_ic.getMessage("ERROR"),
                    JOptionPane.ERROR_MESSAGE);
            }

        }
        else if (action.equals("calculator"))
        {
            RatioCalculatorDialog rcd = new RatioCalculatorDialog(this);

            if (rcd.actionSuccesful())
            {
                float[] res = rcd.getResults();

                this.dtf_ch_ins.setValue(res[0]);
                this.dtf_ins_bg.setValue(res[1]);
                this.dtf_bg_ch.setValue(res[2]);
            }
        }
        else
        {
            System.out.println("RatioBaseDialog::unknown command: " + action);
        }

    }


    /*
     * String button_command[] = { "update_ch",
     * i18nControl.getMessage("UPDATE_FROM_FOOD"),
     * "edit_food", i18nControl.getMessage("EDIT_FOOD"),
     * "ok", i18nControl.getMessage("OK"),
     * "cancel", i18nControl.getMessage("CANCEL"),
     * // "help", i18nControl.getMessage("HELP")
     */

    private void cmdOk()
    {
        this.save();
    }


    /*
     * public boolean isFieldSet(String text)
     * {
     * if ((text == null) || (text.trim().length()==0))
     * return false;
     * else
     * return true;
     * }
     */

    /**
     * Action Succesful
     * 
     * @return
     */
    public boolean actionSuccesful()
    {
        return m_actionDone;
    }


    /**
     * @see java.awt.event.KeyListener#keyTyped(java.awt.event.KeyEvent)
     */
    public void keyTyped(KeyEvent e)
    {
    }


    /**
     * @see java.awt.event.KeyListener#keyPressed(java.awt.event.KeyEvent)
     */
    public void keyPressed(KeyEvent e)
    {
    }


    /**
     * Invoked when a key has been released.
     * See the class description for {@link KeyEvent} for a definition of
     * a key released event.
     */
    public void keyReleased(KeyEvent e)
    {

        // System.out.println("key released [" + in_action + "]");

        if (in_action)
            return;

        in_action = true;

        // System.out.println("key released [" + in_action + "]");
        calculateRatio(e.getSource());

        in_action = false;

        // System.out.println("key released [" + in_action + "]");

        /*
         * if (e.getKeyCode() == KeyEvent.VK_ENTER)
         * {
         * cmdOk();
         * }
         */

    }


    /** 
     * Focus Lost
     */
    public void focusLost(FocusEvent fe)
    {
        if (in_action)
            return;

        in_action = true;
        calculateRatio(fe.getSource());
        in_action = false;
    }


    /** 
     * Focus Gained
     */
    public void focusGained(FocusEvent fe)
    {
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
        return this.help_button;
    }


    /** 
     * getHelpId - get id for Help
     */
    public String getHelpId()
    {
        return "GGC_Ratio_Base";
    }

}
