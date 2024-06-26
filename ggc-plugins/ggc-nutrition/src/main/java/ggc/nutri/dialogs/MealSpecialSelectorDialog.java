package ggc.nutri.dialogs;

import com.atech.utils.ATSwingUtils;
import ggc.nutri.db.datalayer.DailyFoodEntry;
import ggc.nutri.db.datalayer.FoodDescription;
import ggc.nutri.db.datalayer.HomeWeightSpecial;
import ggc.nutri.db.datalayer.Meal;
import ggc.nutri.util.DataAccessNutri;

import java.awt.Component;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.Hashtable;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.TitledBorder;

import com.atech.graphics.components.JDecimalTextField;
import com.atech.help.HelpCapable;
import com.atech.i18n.I18nControlAbstract;
import com.atech.utils.ATDataAccessAbstract;

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
 *  Filename:     MealSpecialSelectorDialog
 *  Description:  Selector for single food
 * 
 *  Author: andyrozman {andy@atech-software.com}  
 */

public class MealSpecialSelectorDialog extends JDialog implements ActionListener, ItemListener, KeyListener,
        HelpCapable
{

    private static final long serialVersionUID = 3869683885823162849L;
    private DataAccessNutri m_da = null;
    private I18nControlAbstract ic = null;

    JTextField tf_selected;
    JComboBox cb_type, cb_weight_type;
    JLabel label_item, label_item_type, label_amount;
    JLabel label_home_weight, label_home_weight_item;
    Font font_normal, font_normal_b;
    JButton button_select, button_hw_select, help_button;
    JPanel panel3, amount_panel;
    JDecimalTextField amountField;

    Hashtable<String, JButton> buttons = new Hashtable<String, JButton>();
    int current_weight_type = 0;
    String[] wt_types_1 = null;
    String[] wt_types_2 = null;
    HomeWeightSpecial hws_selected = null;
    Object action_object;
    int action_object_type = 0;
    long input_id = 0L;
    String[] type;

    DailyFoodEntry daily_food_entry;
    FoodDescription dsc_food;
    Meal dsc_meal;

    /**
     * Constructor
     * 
     * @param parent 
     * @param meal_id
     */
    public MealSpecialSelectorDialog(JDialog parent, long meal_id)
    {
        super(parent, "", true);

        m_da = DataAccessNutri.getInstance();
        ic = m_da.getI18nControlInstance();

        this.setTitle(ic.getMessage("MEALS_FOODS_SELECTOR"));
        this.input_id = meal_id;

        init();

        this.setVisible(true);
    }

    /**
     * Constructor
     * 
     * @param parent 
     * @param meal_id
     */
    public MealSpecialSelectorDialog(JFrame parent, long meal_id)
    {
        super(parent, "", true);

        m_da = DataAccessNutri.getInstance();
        ic = m_da.getI18nControlInstance();

        this.setTitle(ic.getMessage("MEALS_FOODS_SELECTOR"));
        this.input_id = meal_id;

        init();

        this.setVisible(true);
    }

    /**
     * Constructor
     * 
     * @param parent 
     * @param part
     */
    public MealSpecialSelectorDialog(JDialog parent, DailyFoodEntry part)
    {
        super(parent, "", true);

        m_da = DataAccessNutri.getInstance();
        ic = m_da.getI18nControlInstance();

        this.setTitle(ic.getMessage("MEALS_FOODS_SELECTOR"));

        this.daily_food_entry = part;
        init();

        loadMeal();

        this.setVisible(true);
    }

    /**
     * Constructor
     * 
     * @param parent 
     * @param part
     */
    public MealSpecialSelectorDialog(JFrame parent, DailyFoodEntry part)
    {
        super(parent, "", true);

        m_da = DataAccessNutri.getInstance();
        ic = m_da.getI18nControlInstance();

        this.setTitle(ic.getMessage("MEALS_FOODS_SELECTOR"));

        this.daily_food_entry = part;
        init();

        loadMeal();

        this.setVisible(true);
    }

    private void loadMeal()
    {
        this.cb_type.setSelectedIndex(this.daily_food_entry.getNutritionFoodType() - 1);
        this.cb_type.setEnabled(false);
        this.button_select.setEnabled(false);

        this.action_object_type = this.daily_food_entry.getNutritionFoodType() - 1;

        if (this.cb_type.getSelectedIndex() < 2)
        {
            this.dsc_food = this.daily_food_entry.getFoodObject();
            this.label_item.setText(this.dsc_food.getName());
            // this.action_object = fd;
        }
        else
        {
            this.dsc_meal = this.daily_food_entry.getMealObject();
            // Meal m = this.meal_part.getMealObject();
            this.label_item.setText(this.dsc_meal.getName());

            // this.action_object = m;
        }

        this.label_item_type.setText("" + this.cb_type.getSelectedItem());

        setEnabledWithType();

        if (this.daily_food_entry.getFoodObject() != null)
        {
            this.cb_weight_type.setSelectedIndex(this.daily_food_entry.getAmountType() - 1);

            if (this.daily_food_entry.getAmountType() == DailyFoodEntry.WEIGHT_TYPE_HOME_WEIGHT)
            {
                this.hws_selected = this.daily_food_entry.getHomeWeightSpecialObject();
                this.label_home_weight_item.setText(this.hws_selected.getName());
            }
        }
        else
        {
            this.cb_weight_type.setSelectedIndex(0);
        }

        this.amountField.setValue(this.daily_food_entry.getAmount());

    }

    private void init()
    {
        ATSwingUtils.initLibrary();

        this.setLayout(null);
        this.setBounds(160, 100, 300, 540);

        type = new String[3];
        type[0] = ic.getMessage("USDA_NUTRITION");
        type[1] = ic.getMessage("USER_NUTRITION");
        type[2] = ic.getMessage("MEAL");

        wt_types_1 = new String[2];
        wt_types_1[0] = ic.getMessage("WEIGHT_LBL2");
        wt_types_1[1] = ic.getMessage("HOME_WEIGHTS_LBL");

        wt_types_2 = new String[1];
        wt_types_2[0] = ic.getMessage("AMOUNT_LBL");

        font_normal_b = ATSwingUtils.getFont(ATSwingUtils.FONT_NORMAL_BOLD);
        font_normal = ATSwingUtils.getFont(ATSwingUtils.FONT_NORMAL);

        JPanel panel = new JPanel();
        panel.setLayout(null);
        panel.setBounds(0, 0, 300, 560);

        // sub panel - selector

        JPanel panel1 = new JPanel();
        panel1.setBorder(new TitledBorder(ic.getMessage("SELECTOR")));
        panel1.setLayout(null);
        panel1.setBounds(10, 10, 270, 140);
        panel.add(panel1, null);

        // new javax.swing.border.TitledBorder("Some");

        // javax.swing.border.

        JLabel label = new JLabel(ic.getMessage("NUTRITION_TYPE_FOR_SELECTOR"));
        label.setBounds(30, 20, 220, 25);
        label.setFont(font_normal_b);
        panel1.add(label, null);

        cb_type = new JComboBox(type);
        cb_type.setBounds(30, 50, 210, 25);
        panel1.add(cb_type);

        button_select = new JButton(ic.getMessage("SELECT_ITEM"));
        button_select.setActionCommand("select_item");
        button_select.setBounds(30, 95, 210, 25);
        button_select.addActionListener(this);
        panel1.add(button_select, null);

        // if (meal_part!=null)
        // button.setEnabled(false);

        // sub panel - selected item

        JPanel panel2 = new JPanel();
        panel2.setBorder(new TitledBorder(ic.getMessage("SELECTED_ITEM")));
        panel2.setLayout(null);
        panel2.setBounds(10, 160, 270, 135);
        panel.add(panel2, null);

        label = new JLabel(ic.getMessage("NUTRITION_TYPE") + ":");
        label.setBounds(20, 25, 220, 25);
        label.setFont(font_normal_b);
        panel2.add(label, null);

        label_item_type = new JLabel(ic.getMessage("NONE"));
        label_item_type.setBounds(20, 45, 220, 25);
        label_item_type.setFont(font_normal);
        panel2.add(label_item_type, null);

        label = new JLabel(ic.getMessage("SELECTED_ITEM") + ":");
        label.setBounds(20, 75, 220, 25);
        label.setFont(font_normal_b);
        panel2.add(label, null);

        label_item = new JLabel(ic.getMessage(ic.getMessage("NONE")));
        label_item.setBounds(20, 95, 220, 25);
        label_item.setFont(font_normal);
        panel2.add(label_item, null);

        panel3 = new JPanel();
        panel3.setBorder(new TitledBorder(ic.getMessage("AMOUNT_LBL")));
        panel3.setLayout(null);
        panel3.setBounds(10, 310, 270, 110);
        panel.add(panel3, null);

        amount_panel = panel3;

        label = new JLabel(ic.getMessage("WEIGHT_TYPE") + ":");
        label.setBounds(20, 25, 100, 25);
        panel3.add(label, null);

        cb_weight_type = new JComboBox(this.wt_types_1);
        cb_weight_type.setBounds(130, 25, 125, 25);
        cb_weight_type.addItemListener(this);
        cb_weight_type.setEnabled(false);
        panel3.add(cb_weight_type, null);

        label_amount = new JLabel(ic.getMessage("WEIGHT") + ":");
        label_amount.setBounds(20, 65, 100, 25);
        panel3.add(label_amount, null);

        // JFormattedTextField jftf = JFormattedTextField(percentFormat);
        /*
         * amountDisplayFormat = NumberFormat.getNumberInstance();
         * amountDisplayFormat.setMinimumFractionDigits(1); amountEditFormat =
         * NumberFormat.getNumberInstance();
         * amountEditFormat.setMinimumFractionDigits(1);
         */

        amountField = new JDecimalTextField(new Double(1.0d), 1);
        amountField.setBounds(130, 65, 100, 25);
        amountField.setEnabled(false);
        amountField.addKeyListener(this);
        panel3.add(amountField);

        /*
         * amountField = new JFormattedTextField( new DefaultFormatterFactory(
         * new NumberFormatter(amountDisplayFormat), new
         * NumberFormatter(amountDisplayFormat), new
         * NumberFormatter(amountEditFormat))); amountField.setValue(new
         * Double(1.0d)); amountField.setColumns(4); amountField.setBounds(130,
         * 65, 100, 25);
         * amountField.setFocusLostBehavior(JFormattedTextField.COMMIT_OR_REVERT
         * ); amountField.setEnabled(false); panel3.add(amountField);
         */

        label_home_weight = new JLabel(ic.getMessage("HOME_WEIGHT") + ":");
        label_home_weight.setBounds(20, 65, 100, 25);
        label_home_weight.setVisible(false);
        panel3.add(label_home_weight, null);

        button_hw_select = new JButton(ic.getMessage("SELECT"));
        button_hw_select.setBounds(155, 65, 100, 20);
        button_hw_select.setActionCommand("hw_select");
        button_hw_select.addActionListener(this);
        button_hw_select.setVisible(false);
        panel3.add(button_hw_select, null);

        label_home_weight_item = new JLabel(ic.getMessage("NO_ITEM_SELECTED"));
        label_home_weight_item.setBounds(20, 90, 255, 25);
        label_home_weight_item.setFont(this.font_normal);
        label_home_weight_item.setVisible(false);
        panel3.add(label_home_weight_item, null);

        /*
         * percentDisplayFormat = NumberFormat.getPercentInstance();
         * percentDisplayFormat.setMinimumFractionDigits(2); percentEditFormat =
         * NumberFormat.getNumberInstance();
         * percentEditFormat.setMinimumFractionDigits(2);
         */

        // amountField.addPropertyChangeListener("value", this);

        JButton button = new JButton("   " + ic.getMessage("OK"));
        button.setActionCommand("ok");
        button.setIcon(ATSwingUtils.getImageIcon_22x22("ok.png", this, m_da));
        button.setBounds(25, 460, 115, 25);
        button.addActionListener(this);
        panel.add(button, null);

        this.buttons.put("ok", button);

        button = new JButton("   " + ic.getMessage("CANCEL"));
        button.setActionCommand("cancel");
        button.setIcon(ATSwingUtils.getImageIcon_22x22("cancel.png", this, m_da));
        button.setBounds(150, 460, 115, 25);
        button.addActionListener(this);
        panel.add(button);

        this.buttons.put("cancel", button);

        help_button = ATSwingUtils.createHelpButtonByBounds(150, 430, 115, 25, this, ATSwingUtils.FONT_NORMAL, m_da);
        panel.add(help_button);

        this.buttons.put("help", button);

        m_da.enableHelp(this);

        this.add(panel, null);
    }

    /**
     * Set Weight Type
     * 
     * @param type
     */
    public void setWeightType(int type)
    {
        this.setWeightType(type, false);
    }

    /**
     * Set Weight Type
     * 
     * @param type
     * @param forced 
     */
    public void setWeightType(int type, boolean forced)
    {
        if (current_weight_type == type && !forced)
            return;

        if (type == 0)
        {
            this.label_amount.setText(ic.getMessage("WEIGHT"));

            current_weight_type = type;
            this.setBounds(160, 100, 300, 540);

            this.panel3.setSize(270, 110);
            // 460, 430

            this.buttons.get("ok").setLocation(25, 460);
            this.buttons.get("cancel").setLocation(150, 460);
            this.buttons.get("help").setLocation(150, 430);

            label_home_weight.setVisible(false);
            label_home_weight_item.setVisible(false);
            button_hw_select.setVisible(false);

            amountField.setBounds(130, 65, 100, 25);
            label_amount.setBounds(20, 65, 100, 25);

        }
        else
        {
            this.label_amount.setText(ic.getMessage("AMOUNT_LBL"));
            current_weight_type = type;
            this.setBounds(160, 100, 300, 600);
            this.panel3.setSize(270, 165);

            this.buttons.get("ok").setLocation(25, 520);
            this.buttons.get("cancel").setLocation(150, 520);
            this.buttons.get("help").setLocation(150, 490);

            label_home_weight.setVisible(true);
            label_home_weight_item.setVisible(true);
            button_hw_select.setVisible(true);

            label_amount.setBounds(20, 125, 100, 25);
            amountField.setBounds(130, 125, 100, 25);
        }

    }

    /**
     * Set Enabled With Type
     */
    private void setEnabledWithType()
    {
        if (this.isAnyObjectSelected())
        {
            cb_weight_type.setEnabled(true);
            amountField.setEnabled(true);
        }

        if (this.dsc_meal != null)
        {
            // this.cb_weight_type.removeAllItems(); //.setModel(new
            // DeafultComboBoxModel());
            // this.cb_weight_type.setModel(new ComboBoxModel(this.wt_types_2));
            reFillItems(this.wt_types_2);
            setWeightType(0, true);
            this.label_amount.setText(ic.getMessage("AMOUNT_LBL"));
        }
        else
        {
            reFillItems(this.wt_types_1);
            setWeightType(this.cb_weight_type.getSelectedIndex(), true);
        }
    }

    private void reFillItems(Object[] items)
    {
        /*
         * this.cb_weight_type.removeAllItems();
         * for(int i =0; i<items.length; i++ ) {
         * this.cb_weight_type.addItem(items[i]); }
         */

        this.amount_panel.remove(cb_weight_type);

        cb_weight_type = new JComboBox(items);
        cb_weight_type.setBounds(130, 25, 125, 25);
        cb_weight_type.addItemListener(this);
        // cb_weight_type.setEnabled(false);
        this.amount_panel.add(cb_weight_type, null);

        this.amount_panel.repaint();

    }

    /**
     * itemStateChanged
     */
    public void itemStateChanged(ItemEvent arg0)
    {
        setWeightType(this.cb_weight_type.getSelectedIndex());
    }

    /**
     * Was Action
     * 
     * @return
     */
    public boolean wasAction()
    {
        return this.action_done;
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

    private boolean action_done = false;

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
            this.action_done = false;
            this.dispose();
        }
        else if (cmd.equals("select_item"))
        {
            // System.out.println("Select item");

            NutritionTreeDialog ntd = new NutritionTreeDialog(this, m_da, this.cb_type.getSelectedIndex() + 1, true);

            if (ntd.wasAction())
            {
                /*
                 * //XXX: Removed because it's not needed, but I might be wrong,
                 * so it should stay here for a while
                 * if (this.cb_type.getSelectedIndex()==2) { Meal m =
                 * (Meal)ntd.getSelectedObject();
                 * if (m.getId() == this.input_id) {
                 * JOptionPane.showMessageDialog(this,
                 * i18nControlAbstract.getMessage("CANT_SELECT_CIRCULAR_MEAL"),
                 * i18nControlAbstract.getMessage("WARNING"),JOptionPane.WARNING_MESSAGE);
                 * return; } }
                 */

                this.label_item_type.setText("" + this.cb_type.getSelectedItem());

                this.action_object_type = this.cb_type.getSelectedIndex() + 1;
                this.action_object = ntd.getSelectedObject();

                // System.out.println("ao: " + action_object);

                if (this.cb_type.getSelectedIndex() < 2)
                {
                    this.dsc_food = (FoodDescription) this.action_object;
                    this.dsc_meal = null;
                    this.label_item.setText(this.dsc_food.getName());
                }
                else
                {
                    this.dsc_meal = (Meal) this.action_object;
                    this.dsc_food = null;
                    this.label_item.setText(this.dsc_meal.getName());
                }

                this.setEnabledWithType();
            }
        }
        else if (cmd.equals("hw_select"))
        {
            /*
             * if (!this.isAnyObjectSelected()) {
             * System.out.println("No Object selected"); }
             */
            HWSelectorDialog hwsd = new HWSelectorDialog(this, m_da, this.getHomeWeightOfObjects());

            if (hwsd.wasAction())
            {
                this.hws_selected = (HomeWeightSpecial) hwsd.getSelectedObject();
                this.label_home_weight_item.setText(this.hws_selected.getShortDescription());
            }

        }
        else
        {
            System.out.println("MealSpecialSelector: unknown command:" + cmd);
        }

    }

    private void cmdOk()
    {
        this.action_done = true;
        this.dispose();
    }

    /**
     * Is Any Object Selected
     * 
     * @return
     */
    public boolean isAnyObjectSelected()
    {
        if (this.dsc_food != null || this.dsc_meal != null)
            return true;
        else
            return false;
    }

    /**
     * Get Home Weight Of Objects
     * 
     * @return
     */
    public String getHomeWeightOfObjects()
    {
        if (this.dsc_food != null)
            return this.dsc_food.getHome_weights();

        if (this.dsc_meal != null)
            return null;

        return null;
    }

    /**
     * Get Daily Food Entry
     * 
     * @return
     */
    public DailyFoodEntry getDailyFoodEntry()
    {
        // System.out.println("getDailyFoodEntry");

        if (!this.isAnyObjectSelected() || !action_done)
            return null;

        // System.out.println("getDailyFoodEntry: after");

        // DailyFoodEntry dfe = null;
        if (this.dsc_food != null)
        {

            // System.out.println("in dsc_food");

            if (this.cb_weight_type.getSelectedIndex() == 0)
                // System.out.println("wt = 0");
                return new DailyFoodEntry(this.dsc_food, this.getAmountValue());
            else
            {
                if (this.hws_selected != null)
                    // System.out.println("hws != null");
                    return new DailyFoodEntry(this.dsc_food, this.hws_selected, this.getAmountValue());
                else
                    // System.out.println("hws == NULL");
                    return null;
            }

        }
        else
            // System.out.println("in dsc_meal");
            return new DailyFoodEntry(this.dsc_meal, this.getAmountValue());

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

    /** 
     * keyPressed
     */
    public void keyPressed(KeyEvent arg0)
    {
    }

    /** 
     * keyTyped
     */
    public void keyTyped(KeyEvent ke)
    {
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
        return "GGC_MFS_Item_Selector";
    }

}
