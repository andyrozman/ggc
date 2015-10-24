package ggc.shared.fooddesc;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.StringTokenizer;

import javax.swing.*;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.atech.graphics.components.JDecimalTextField;
import com.atech.graphics.dialogs.TransferDialog;
import com.atech.help.HelpCapable;
import com.atech.i18n.I18nControlAbstract;
import com.atech.utils.ATDataAccessAbstract;
import com.atech.utils.ATSwingUtils;

import ggc.core.data.DailyValuesRow;
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
 *  Filename:     DailyRowMealsDialog
 *  Description:  Dialog for adding foods entries to dailyrow
 * 
 *  Author: andyrozman {andy@atech-software.com}  
 */

public class FoodDescriptionDialog extends TransferDialog /* JDialog */implements ActionListener, HelpCapable
{

    private static final long serialVersionUID = 6763016271693781911L;

    private static final Logger LOG = LoggerFactory.getLogger(FoodDescriptionDialog.class);
    private I18nControlAbstract m_ic = DataAccess.getInstance().getI18nControlInstance();
    // private DataAccess dataAccess = DataAccess.getInstance();
    // private GGCProperties props = dataAccess.getSettings();

    JLabel label_title;
    JDecimalTextField ftf_ch;
    JTextArea text_area;

    // DateTimeComponent dtc;
    // JButton AddButton;
    // String sDate = null;
    // DailyValues dV = null;
    DailyValuesRow m_dailyValuesRow = null;

    // NumberFormat bg_displayFormat, bg_editFormat;

    JComponent components[] = new JComponent[9];

    // Font f_normal = dataAccess.getFont(DataAccess.FONT_NORMAL);
    // Font f_bold = dataAccess.getFont(DataAccess.FONT_NORMAL);
    boolean in_process;
    boolean debug = true;
    JButton help_button = null;
    JPanel main_panel = null;
    private ATDataAccessAbstract m_da = null;

    String food_desc;
    String food_ch;

    boolean transfer_mode = false;
    Component parent;


    // private Container m_parent = null;

    /**
     * Constructor
     * 
     * @param row
     * @param dialog
     */
    public FoodDescriptionDialog(DailyValuesRow row, JDialog dialog)
    {
        super(dialog); // , "", true);

        parent = dialog;
        this.m_da = DataAccess.getInstance();
        // m_parent = dialog;
        this.m_dailyValuesRow = row;

        init();
        load();
        m_da.centerJDialog(this);

        this.setVisible(true);
    }


    /**
     * Constructor
     * 
     * @param food_desc 
     * @param ch_value 
     * @param dialog
     */
    public FoodDescriptionDialog(String food_desc, float ch_value, JDialog dialog)
    {
        super(dialog); // , "", true);

        parent = dialog;
        this.m_da = DataAccess.getInstance();
        // m_parent = dialog;
        // this.m_dailyValuesRow = row;

        init();

        this.text_area.setText(food_desc);
        this.ftf_ch.setValue(ch_value);
        transfer_mode = true;

        // load();
        m_da.centerJDialog(this);

        this.setVisible(true);
    }


    /**
     * Constructor
     * 
     * @param parent
     */
    public FoodDescriptionDialog(JFrame parent)
    {
        super(parent); // , "", true);

        this.parent = parent;
        // m_parent = dialog;
        // this.m_dailyValuesRow = row;
        transfer_mode = true;
        init();
        // load();
        // dataAccess.centerJDialog(this);

    }


    /**
     * Constructor
     * 
     * @param parent
     */
    public FoodDescriptionDialog(JDialog parent)
    {
        super(parent); // , "", true);

        this.parent = parent;
        // m_parent = dialog;
        // this.m_dailyValuesRow = row;
        transfer_mode = true;
        // init();
        // load();
        // dataAccess.centerJDialog(this);

    }


    private void load()
    {

        if (this.transfer_mode)
        {
            this.text_area.setText(this.food_desc);
            this.ftf_ch.setValue(m_da.getFloatValueFromString(this.food_ch, 0.0f));
        }
        else
        {
            this.text_area.setText(this.m_dailyValuesRow.getFoodDescription());
            this.ftf_ch.setValue(m_da.getFloatValueFromString(this.m_dailyValuesRow.getFoodDescriptionCH(), 0.0f));
        }

        /*
         * String ch = this.m_dailyValuesRow.getFoodDescriptionCH();
         * if (ch!=null)
         * {
         * System.out.println("CH:" + ch);
         * ch = ch.replace(DataAccess.false_decimal, DataAccess.real_decimal);
         * ch = ch.replace(",", ".");
         * try
         * {
         * float f = Float.parseFloat(ch);
         * System.out.println("CH:" + f);
         * this.ftf_ch.setValue(f);
         * }
         * catch(Exception ex)
         * {
         * System.out.println("load ex: " + ex);
         * }
         * }
         * }
         */

    }


    private void save()
    {

        // System.out.println(this.ftf_ch.getValue());
        // System.out.println(this.ftf_ch.getCurrentValue());

        float ch = m_da.getFloatValue(this.ftf_ch.getCurrentValue());
        // ch = ch.replace(DataAccess.false_decimal, DataAccess.real_decimal);

        // System.out.println("Save.Float= " + ch);

        String val = null;
        if (ch > 0)
        {
            val = "" + ch;
        }
        else
        {
            val = "";
        }

        if (transfer_mode)
        {
            this.food_desc = this.text_area.getText();
            this.food_ch = val;
        }
        else
        {
            this.m_dailyValuesRow.setFoodDescription(this.text_area.getText());
            this.m_dailyValuesRow.setFoodDescriptionCH(val); // DataAccess.Decimal2Format.format(ch));
        }

    }


    private void init()
    {
        m_da.addComponent(this);

        ATSwingUtils.initLibrary();

        int x = 0;
        int y = 0;
        int width = 400;
        int height = 340;

        /*
         * Rectangle bnd = m_parent.getBounds();
         * x = (bnd.width / 2) + bnd.x - (width / 2);
         * y = (bnd.height / 2) + bnd.y - (height / 2);
         */

        this.setBounds(x, y, width, height);

        m_da.centerJDialog(this);

        JPanel panel = new JPanel();
        panel.setBounds(0, 0, width, height);
        panel.setLayout(null);

        main_panel = panel;

        this.getContentPane().add(panel);

        setTitle(m_ic.getMessage("FOOD_BY_DESCRIPTION_TITLE"));
        label_title = ATSwingUtils.getTitleLabel(m_ic.getMessage("FOOD_BY_DESCRIPTION_TITLE"), 0, 15, 400, 35, panel,
            ATSwingUtils.FONT_BIG_BOLD);

        ATSwingUtils.getLabel(m_ic.getMessage("DESCRIBE_FOODS"), 40, 65, 310, 50, panel);

        this.text_area = new JTextArea();
        this.text_area.setLineWrap(true);
        JScrollPane scr = new JScrollPane(text_area);
        scr.setHorizontalScrollBarPolicy(
            ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER | ScrollPaneConstants.VERTICAL_SCROLLBAR_AS_NEEDED);
        scr.setBounds(40, 115, 310, 75);
        panel.add(scr);

        ATSwingUtils.getLabel(m_ic.getMessage("CH_LONG") + ":", 40, 210, 100, 25, panel);

        this.ftf_ch = ATSwingUtils.getNumericTextField(2, 2, new Float(0.0f), 160, 210, 55, 25, panel);

        ATSwingUtils.getButton(m_ic.getMessage("CALCULATE"), 230, 210, 120, 25, panel, ATSwingUtils.FONT_NORMAL, null,
            "calculate", this, m_da);

        ATSwingUtils.getButton(m_ic.getMessage("OK"), 30, 260, 110, 25, panel, ATSwingUtils.FONT_NORMAL, "ok.png", "ok",
            this, m_da);

        ATSwingUtils.getButton(m_ic.getMessage("CANCEL"), 145, 260, 110, 25, panel, ATSwingUtils.FONT_NORMAL,
            "cancel.png", "cancel", this, m_da);

        help_button = ATSwingUtils.createHelpButtonByBounds(260, 260, 110, 25, this, ATSwingUtils.FONT_NORMAL, m_da);
        panel.add(help_button);
        m_da.enableHelp(this);

    }


    private void calculateCH()
    {
        String txt = this.text_area.getText();

        if (txt.length() == 0 || !txt.contains("["))
        {
            JOptionPane.showMessageDialog(this, m_ic.getMessage("NO_DESCRIPTIONS_WITH_CH"), m_ic.getMessage("WARNING"),
                JOptionPane.WARNING_MESSAGE);
        }
        else
        {
            float sum = this.calculationInternal(this.text_area.getText());
            // System.out.println("doing calcualtion: " + sum);

            this.ftf_ch.setValue(new Float(sum));
        }

    }


    private float calculationInternal(String text)
    {
        float sum = 0.0f;

        StringTokenizer strtok = new StringTokenizer(text, "[");
        strtok.nextToken();

        while (strtok.hasMoreTokens())
        {
            String t = strtok.nextToken();

            if (t.indexOf("]") == -1)
            {
                continue;
            }

            t = t.substring(0, t.indexOf("]"));

            t = t.replace(",", "."); // DataAccess.false_decimal,
                                     // DataAccess.real_decimal);

            if (t.length() == 0)
            {
                continue;
            }
            else if (t.contains("x") || t.contains("*"))
            {
                String parts[] = null;

                if (t.contains("*"))
                {
                    t = t.replace('*', 'x');
                }

                parts = t.split("x");

                sum += m_da.getFloatValueFromString(parts[0], 0.0f) * m_da.getFloatValueFromString(parts[1], 0.0f);
            }
            else
            {
                sum += m_da.getFloatValueFromString(t, 0.0f);
            }

            /*
             * try
             * {
             * float f = Float.parseFloat(t);
             * // System.out.println("entry: " + f);
             * sum += f;
             * }
             * catch(Exception ex)
             * {
             * LOG.error("Error on parse: [token=" + t + ",exception=" + ex +
             * "]", ex );
             * //System.out.println("Ex: " + ex);
             * }
             */
        }

        return sum;

    }

    boolean was_action = false;


    /**
     * Invoked when an action occurs.
     */
    public void actionPerformed(ActionEvent e)
    {
        String action = e.getActionCommand();

        if (action.equals("cancel"))
        {
            was_action = false;
            removeAndClose();
        }
        else if (action.equals("ok"))
        {
            save();
            was_action = true;
            removeAndClose();
        }
        else if (action.equals("calculate"))
        {
            calculateCH();
        }
        else
        {
            LOG.error("DailyRowMealsDialog::unknown command: " + action);
            // System.out.println("DailyRowMealsDialog::unknown command: " +
            // action);
        }

    }


    private void removeAndClose()
    {
        m_da.removeComponent(this);
        m_da.getCurrentComponent().requestFocus();
        this.dispose();

        /*
         * if (da_local!=null)
         * {
         * da_local.removeComponent(this);
         * //this.setVisible(false);
         * this.dispose();
         * }
         * else
         * {
         * this.dispose();
         * }
         */
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
        return "GGC_Tools_FoodDescription";
    }


    /**
     * Get Result Values
     * 
     * @return
     */
    @Override
    public Object[] getResultValues()
    {
        return this.getResultValuesString();
    }


    /**
     * Get Result Values String
     * 
     * @return
     */
    @Override
    public String[] getResultValuesString()
    {
        String[] ob = new String[2];
        ob[0] = food_desc;
        ob[1] = food_ch;
        return ob;
    }

    /**
     * Input Parameter: Parent Dialog
     */
    public static final int PARAMETER_PARENT_DIALOG = 0;

    /**
     * Input Parameter: Data Access
     */
    public static final int PARAMETER_DATA_ACCESS = 1;

    /**
     * Input Parameter: Food Description
     */
    public static final int PARAMETER_FOOD_DESC = 2;

    /**
     * Input Parameter: Food Ch
     */
    public static final int PARAMETER_FOOD_CH = 3;


    /**
     * Set Input Parameters
     * 
     * @param ip
     */
    @Override
    public void setInputParameters(Object[] ip)
    {
        this.was_action = false;
        JDialog di = (JDialog) ip[0];
        this.parent = di;

        m_da = (ATDataAccessAbstract) ip[1];
        food_desc = (String) ip[2];
        food_ch = (String) ip[3];

        // dataAccess.addComponent(this);
        // dataAccess.centerJDialog(this, di);

        init();
        load();
    }


    /**
     * Show Dialog
     * 
     * @param visible
     */
    @Override
    public void showDialog(boolean visible)
    {
        super.setVisible(visible);
    }


    /**
     * Was Action
     * 
     * @return
     */
    @Override
    public boolean wasAction()
    {
        return was_action;
    }


    /**
     * Get Input Parameters Count
     * 
     * @return
     */
    @Override
    public int getInputParametersCount()
    {
        return 4;
    }

}
