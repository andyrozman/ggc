package ggc.nutri.dialogs;

import ggc.nutri.display.HomeWeightDataDisplay;
import ggc.nutri.display.NutritionDataDisplay;
import ggc.nutri.util.DataAccessNutri;

import java.awt.Component;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.TitledBorder;

import com.atech.graphics.components.JDecimalTextField;
import com.atech.graphics.dialogs.selector.SelectableInterface;
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
 *  Filename:     FoodPartMainSelectorDialog
 *  Description:  Selector for Nutritions and Home Weights, when defining User Foods.
 * 
 *  Author: andyrozman {andy@atech-software.com}  
 */

public class FoodPartMainSelectorDialog extends JDialog implements ActionListener, KeyListener, HelpCapable
{

    private static final long serialVersionUID = -1193693566584753748L;
    JTextField tf_selected;
    JComboBox cb_type;
    JLabel label_item, label_item_type, label_item_desc;
    Font font_normal, font_normal_b;
    JButton button_select, help_button;
    JPanel panel2;

    private DataAccessNutri m_da = null;
    private I18nControlAbstract ic = null;

    SelectableInterface action_object;
    int action_object_type = 0;
    long input_id = 0L;

    String[] type;
    String m_except = null;

    JDecimalTextField amountField;
    JDecimalTextField weightField;

    NutritionDataDisplay nutrition_data;
    HomeWeightDataDisplay home_weight_data;

    /**
     * Selector Nutrition
     */
    public static final int SELECTOR_NUTRITION = 1;
    /**
     * Selector Home Weight
     */
    public static final int SELECTOR_HOME_WEIGHT = 2;

    private int selector_type = 0;
    boolean action_done = false;

    /**
     * Constructor
     * 
     * @param parent 
     * @param type
     * @param except
     */
    public FoodPartMainSelectorDialog(JDialog parent, int type, String except)
    {
        super(parent, "", true);

        m_da = DataAccessNutri.getInstance();
        ic = m_da.getI18nControlInstance();

        if (type == FoodPartMainSelectorDialog.SELECTOR_NUTRITION)
        {
            this.setTitle(ic.getMessage("NUTRITION_SELECTOR"));
        }
        else
        {
            this.setTitle(ic.getMessage("HOME_WEIGHT_SELECTOR"));
        }

        // this.input_id = meal_id;
        this.selector_type = type;

        m_except = except;

        init();

        this.setVisible(true);
    }

    /**
     * Constructor
     * 
     * @param parent 
     * @param type
     * @param except
     */
    public FoodPartMainSelectorDialog(JFrame parent, int type, String except)
    {
        super(parent, "", true);

        m_da = DataAccessNutri.getInstance();
        ic = m_da.getI18nControlInstance();

        if (type == FoodPartMainSelectorDialog.SELECTOR_NUTRITION)
        {
            this.setTitle(ic.getMessage("NUTRITION_SELECTOR"));
        }
        else
        {
            this.setTitle(ic.getMessage("HOME_WEIGHT_SELECTOR"));
        }

        // this.input_id = meal_id;
        this.selector_type = type;

        m_except = except;

        init();

        this.setVisible(true);
    }

    /**
     * Constructor
     * 
     * @param parent 
     * @param ndd
     */
    public FoodPartMainSelectorDialog(JFrame parent, NutritionDataDisplay ndd)
    {
        super(parent, "", true);

        m_da = DataAccessNutri.getInstance();
        ic = m_da.getI18nControlInstance();

        this.setTitle(ic.getMessage("NUTRITION_SELECTOR"));
        this.selector_type = FoodPartMainSelectorDialog.SELECTOR_NUTRITION;

        this.nutrition_data = ndd;
        init();

        loadNutrition();

        this.setVisible(true);
    }

    /**
     * Constructor
     * 
     * @param parent 
     * @param ndd
     */
    public FoodPartMainSelectorDialog(JDialog parent, NutritionDataDisplay ndd)
    {
        super(parent, "", true);

        m_da = DataAccessNutri.getInstance();
        ic = m_da.getI18nControlInstance();

        this.setTitle(ic.getMessage("NUTRITION_SELECTOR"));
        this.selector_type = FoodPartMainSelectorDialog.SELECTOR_NUTRITION;

        this.nutrition_data = ndd;
        init();

        loadNutrition();

        this.setVisible(true);
    }

    /**
     * Constructor
     * 
     * @param parent 
     * @param hwd
     */
    public FoodPartMainSelectorDialog(JDialog parent, HomeWeightDataDisplay hwd)
    {
        super(parent, "", true);

        m_da = DataAccessNutri.getInstance();
        ic = m_da.getI18nControlInstance();

        this.setTitle(ic.getMessage("HOME_WEIGHT_SELECTOR"));

        this.selector_type = FoodPartMainSelectorDialog.SELECTOR_HOME_WEIGHT;

        // this.nutrition_data = ndd;
        this.home_weight_data = hwd;
        init();

        // loadNutrition();
        loadHomeWeighs();

        this.setVisible(true);
    }

    /**
     * Constructor
     * 
     * @param parent 
     * @param hwd
     */
    public FoodPartMainSelectorDialog(JFrame parent, HomeWeightDataDisplay hwd)
    {
        super(parent, "", true);

        m_da = DataAccessNutri.getInstance();
        ic = m_da.getI18nControlInstance();

        this.setTitle(ic.getMessage("HOME_WEIGHT_SELECTOR"));

        this.selector_type = FoodPartMainSelectorDialog.SELECTOR_HOME_WEIGHT;

        // this.nutrition_data = ndd;
        this.home_weight_data = hwd;
        init();

        // loadNutrition();
        loadHomeWeighs();

        this.setVisible(true);
    }

    private void loadNutrition()
    {
        this.button_select.setEnabled(false);
        this.label_item.setText(this.nutrition_data.getName() + " (" + this.nutrition_data.getWeightUnit() + ")");

        // System.out.println("Amount input: " +
        // this.nutrition_data.getAmount());

        this.amountField.setValue(new Double(this.nutrition_data.getAmount()));
    }

    private void loadHomeWeighs()
    {
        this.button_select.setEnabled(false);
        this.label_item.setText(this.home_weight_data.getName());

        // System.out.println("Amount input: " +
        // this.nutrition_data.getAmount());

        this.amountField.setValue(new Double(this.home_weight_data.getAmount()));
        this.weightField.setValue(new Double(this.home_weight_data.getWeight()));
    }

    private void init()
    {
        ATSwingUtils.initLibrary();

        int button_y;
        int panel_height;
        String panel_title;

        if (this.selector_type == FoodPartMainSelectorDialog.SELECTOR_HOME_WEIGHT)
        {
            this.setBounds(160, 100, 300, 355);
            button_y = 275;
            panel_height = 110;
            panel_title = ic.getMessage("AMOUNT_WEIGHT");
        }
        else
        {
            this.setBounds(160, 100, 300, 305);
            button_y = 230;
            panel_height = 65;
            panel_title = ic.getMessage("AMOUNT_LBL");
        }

        this.setLayout(null);

        action_done = false;
        /*
         * type = new String[3]; type[0] = i18nControlAbstract.getMessage("USDA_NUTRITION");
         * type[1] = i18nControlAbstract.getMessage("USER_NUTRITION"); type[2] =
         * i18nControlAbstract.getMessage("MEAL");
         */

        font_normal_b = ATSwingUtils.getFont(ATSwingUtils.FONT_NORMAL_BOLD);
        font_normal = ATSwingUtils.getFont(ATSwingUtils.FONT_NORMAL);

        JPanel panel = new JPanel();
        panel.setLayout(null);
        panel.setBounds(0, 0, 300, 440);

        // sub panel - selector


        // sub panel - selected item

        panel2 = new JPanel();
        panel2.setBorder(new TitledBorder(ic.getMessage("SELECTED_ITEM")));
        panel2.setLayout(null);
        panel2.setBounds(10, 10, 270, 135);
        panel.add(panel2, null);

        button_select = new JButton(ic.getMessage("SELECT_ITEM"));
        button_select.setActionCommand("select_item");
        button_select.setBounds(30, 35, 210, 25);
        button_select.addActionListener(this);
        panel2.add(button_select, null);

        /*
         * label = new JLabel(i18nControlAbstract.getMessage("NUTRITION_TYPE")+ ":");
         * label.setBounds(20, 25, 220, 25); label.setFont(font_normal_b);
         * panel2.add(label, null);
         * label_item_type = new JLabel(i18nControlAbstract.getMessage("NONE"));
         * label_item_type.setBounds(20, 45, 220, 25);
         * label_item_type.setFont(font_normal); panel2.add(label_item_type,
         * null);
         */

        label_item_desc = new JLabel(ic.getMessage("SELECTED_ITEM") + ":");
        label_item_desc.setBounds(20, 75, 220, 25);
        label_item_desc.setFont(font_normal_b);
        panel2.add(label_item_desc, null);

        label_item = new JLabel(ic.getMessage(ic.getMessage("NONE")));
        label_item.setBounds(20, 95, 220, 25);
        label_item.setFont(font_normal);
        panel2.add(label_item, null);

        JPanel panel3 = new JPanel();
        panel3.setBorder(new TitledBorder(panel_title));
        panel3.setLayout(null);
        panel3.setBounds(10, 150, 270, panel_height);
        panel.add(panel3, null);

        JLabel label = new JLabel(ic.getMessage("AMOUNT_LBL") + ":");
        label.setBounds(20, 25, 100, 25);
        panel3.add(label, null);

        amountField = new JDecimalTextField(new Double(1.0d), 2);
        amountField.setBounds(140, 25, 100, 25);
        amountField.addKeyListener(this);
        panel3.add(amountField);

        if (this.selector_type == FoodPartMainSelectorDialog.SELECTOR_HOME_WEIGHT)
        {

            label = new JLabel(ic.getMessage("WEIGHT") + ":");
            label.setBounds(20, 65, 100, 25);
            panel3.add(label, null);

            this.weightField = new JDecimalTextField(new Double(1.0d), 2);
            this.weightField.setBounds(140, 65, 100, 25);
            this.weightField.addKeyListener(this);
            panel3.add(this.weightField);

            /*
             * this.weightField = new JFormattedTextField( new
             * DefaultFormatterFactory( new
             * NumberFormatter(amountDisplayFormat), new
             * NumberFormatter(amountDisplayFormat), new
             * NumberFormatter(amountEditFormat)));
             * this.weightField.setValue(new Double(1.0d));
             * this.weightField.setColumns(4); this.weightField.setBounds(140,
             * 65, 100, 25);
             * this.weightField.setFocusLostBehavior(JFormattedTextField
             * .COMMIT_OR_REVERT); this.weightField.addKeyListener(this);
             * panel3.add(this.weightField);
             */
        }

        ATSwingUtils.getButton("   " + ic.getMessage("OK"), 20, button_y, 110, 25, panel, ATSwingUtils.FONT_NORMAL,
            "ok.png", "ok", this, m_da);

        ATSwingUtils.getButton("   " + ic.getMessage("CANCEL"), 135, button_y, 110, 25, panel,
            ATSwingUtils.FONT_NORMAL, "cancel.png", "cancel", this, m_da);

        help_button = ATSwingUtils.createHelpIconByBounds(250, button_y, 25, 25, panel, ATSwingUtils.FONT_NORMAL, m_da);
        panel.add(help_button);

        /*
         * JButton button = new JButton(i18nControlAbstract.getMessage("OK"));
         * button.setActionCommand("ok");
         * button.setBounds(65, button_y, 80, 25);
         * button.addActionListener(this);
         * panel.add(button, null);
         * button = new JButton(i18nControlAbstract.getMessage("CANCEL"));
         * button.setActionCommand("cancel");
         * button.setBounds(160, button_y, 80, 25);
         * button.addActionListener(this);
         * panel.add(button);
         */

        this.add(panel, null);

        m_da.enableHelp(this);

        loadTypeSpecific();
    }

    private void loadTypeSpecific()
    {
        if (this.selector_type == FoodPartMainSelectorDialog.SELECTOR_NUTRITION)
        {
            panel2.setBorder(new TitledBorder(ic.getMessage("SELECTED_NUTRITION")));
            label_item_desc.setText(ic.getMessage("SELECTED_NUTRITION") + ":");
            button_select.setText(ic.getMessage("SELECT_NUTRITION"));
        }
        else
        {
            panel2.setBorder(new TitledBorder(ic.getMessage("SELECTED_HOME_WEIGHT")));
            label_item_desc.setText(ic.getMessage("SELECTED_HOME_WEIGHT") + ":");
            button_select.setText(ic.getMessage("SELECT_HOME_WEIGHT"));
        }

    }

    /**
     * Was Action
     * 
     * @return
     */
    public boolean wasAction()
    {
        return this.action_object != null || this.action_done;
    }

    /**
     * Get Selected Object Type
     * 
     * @return
     */
    public int getSelectedObjectType()
    {
        return this.action_object_type;
    }

    /**
     * Get Selected Object
     * 
     * @return
     */
    public Object getSelectedObject()
    {
        return this.action_object;
    }

    /**
     * Get Amount Value
     * 
     * @return
     */
    public float getAmountValue()
    {
        return ATSwingUtils.getJFormatedTextValueFloat(this.amountField);
    }

    /**
     * Get Weight Value
     * 
     * @return
     */
    public float getWeightValue()
    {
        return ATSwingUtils.getJFormatedTextValueFloat(this.weightField);
    }

    /**
     * Action Performed
     * 
     * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
     */
    public void actionPerformed(ActionEvent e)
    {
        String cmd = e.getActionCommand();

        if (cmd.equals("ok"))
        {
            cmdOk();
        }
        else if (cmd.equals("cancel"))
        {
            this.dispose();
        }
        else if (cmd.equals("select_item"))
        {
            FoodPartSelectorDialog fpsd = new FoodPartSelectorDialog(this, m_da, this.selector_type, this.m_except);

            if (fpsd.wasAction())
            {
                this.action_object = fpsd.getSelectedObject();
                this.label_item.setText(this.action_object.getShortDescription());
            }
        }

    }

    private void cmdOk()
    {
        this.action_done = true;
        this.dispose();
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
     * Invoked when a key has been released. See the class description for
     * {@link KeyEvent} for a definition of a key released event.
     */
    public void keyReleased(KeyEvent e)
    {
        if (e.getKeyCode() == KeyEvent.VK_ENTER)
        {
            cmdOk();
        }
    }

    public Component getComponent()
    {
        return this;
    }

    public JButton getHelpButton()
    {
        return this.help_button;
    }

    public String getHelpId()
    {
        if (this.selector_type == FoodPartMainSelectorDialog.SELECTOR_HOME_WEIGHT)
            return "GGC_Food_User_Select_HomeWeight_Main";
        else
            return "GGC_Food_User_Select_Nutrition_Main";
    }

}
