/*
 *  GGC - GNU Gluco Control
 *
 *  A pure java app to help you manage your diabetes.
 *
 *  See AUTHORS for copyright information.
 *
 *  This program is free software; you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation; either version 2 of the License, or
 *  (at your option) any later version.
 *
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with this program; if not, write to the Free Software
 *  Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA
 *
 *  Filename: NutritionTreeDialog
 *  Purpose:  Main class for displaying nutrition information.
 *
 *  Author:   andyrozman
 */

package ggc.core.nutrition.dialogs;
 
import ggc.core.db.datalayer.FoodDescription;
import ggc.core.db.datalayer.Meal;
import ggc.core.db.datalayer.MealPart;
import ggc.core.nutrition.NutritionTreeDialog;
import ggc.core.util.DataAccess;

import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.text.NumberFormat;
import java.util.Hashtable;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JFormattedTextField;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.TitledBorder;
import javax.swing.text.DefaultFormatterFactory;
import javax.swing.text.NumberFormatter;

import com.atech.i18n.I18nControlAbstract;



public class MealSpecialSelectorDialog extends JDialog implements ActionListener, ItemListener
{

    //private JPanel mainPane;
    //private JTree tree;
    


    JTextField tf_selected;
    JComboBox cb_type;
    JLabel label_item, label_item_type, label_amount;
    Font font_normal, font_normal_b;
    JButton button_select;

    JComboBox cb_weight_type;
    String[] wt_types = null;

    
    JLabel label_home_weight, label_home_weight_item; 
    JButton button_hw_select;
    
    
    JPanel panel3;
    Hashtable<String,JButton> buttons = new Hashtable<String,JButton>(); 
    
    int current_weight_type = 0;
    
    
    private DataAccess m_da = null;
    private I18nControlAbstract ic = null;
    
    Object action_object;
    int action_object_type = 0;
    long input_id = 0L;
    
    String[] type;  

    private NumberFormat amountDisplayFormat;
    private NumberFormat amountEditFormat;    
    JFormattedTextField amountField;
    
    MealPart meal_part;
    
    public MealSpecialSelectorDialog(DataAccess da, long meal_id) 
    {
        super(da.getParent(), "", true);

        m_da = da;
        ic = m_da.getNutriI18nControl();

	this.setTitle(ic.getMessage("MEALS_FOODS_SELECTOR"));
	this.input_id = meal_id;
        
        init();
 
        this.setVisible(true);
    }

    
    public MealSpecialSelectorDialog(DataAccess da, MealPart part) 
    {
        super(da.getParent(), "", true);

        m_da = da;
        ic = m_da.getNutriI18nControl();

	this.setTitle(ic.getMessage("MEALS_FOODS_SELECTOR"));
        
        this.meal_part = part;
        init();
        
        loadMeal();
 
        this.setVisible(true);
    }
    
    
    private void loadMeal()
    {
	this.cb_type.setSelectedIndex(this.meal_part.getType()-1);
	this.cb_type.setEnabled(false);
	this.button_select.setEnabled(false);

	this.action_object_type = (this.meal_part.getType()-1);
	
	if (this.cb_type.getSelectedIndex() < 2)
	{
	    FoodDescription fd = this.meal_part.getFoodObject();
	    this.label_item.setText(fd.getName());
	    
	    this.action_object = fd;
	}
	else
	{
	    Meal m = this.meal_part.getMealObject();
	    this.label_item.setText(m.getName());

	    this.action_object = m;
	}
	
	this.label_item_type.setText("" + this.cb_type.getSelectedItem());
	
        this.amountField.setValue(new Double(this.meal_part.getAmount()));
	
    }
    
    
    public void init()
    {
	
	this.setLayout(null);
        this.setBounds(160, 100, 300, 540);

	type = new String[3];
	type[0] = ic.getMessage("USDA_NUTRITION");
	type[1] = ic.getMessage("USER_NUTRITION");
	type[2] = ic.getMessage("MEAL");
	
	wt_types = new String[2];
	wt_types[0] = ic.getMessage("WEIGHT_LBL2");
	wt_types[1] = ic.getMessage("HOME_WEIGHTS_LBL");
	
        font_normal_b = m_da.getFont(DataAccess.FONT_NORMAL_BOLD);
        font_normal = m_da.getFont(DataAccess.FONT_NORMAL);
	
	
        JPanel panel = new JPanel();
        panel.setLayout(null);
        panel.setBounds(0, 0, 300, 560);

        
        // sub panel - selector
        
        JPanel panel1 = new JPanel();
        panel1.setBorder(new TitledBorder(ic.getMessage("SELECTOR")));
        panel1.setLayout(null);
        panel1.setBounds(10, 10, 270, 140);
        panel.add(panel1, null);
        
        //new javax.swing.border.TitledBorder("Some");
        
        //javax.swing.border.
        
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
        
        //if (meal_part!=null)
        //    button.setEnabled(false);

        // sub panel - selected item
        
        JPanel panel2 = new JPanel();
        panel2.setBorder(new TitledBorder(ic.getMessage("SELECTED_ITEM")));
        panel2.setLayout(null);
        panel2.setBounds(10, 160, 270, 135);
        panel.add(panel2, null);
        
        
        label = new JLabel(ic.getMessage("NUTRITION_TYPE")+ ":");
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
        
        label_item = new JLabel(ic.getMessage(ic.getMessage("NONE")) );
        label_item.setBounds(20, 95, 220, 25);
        label_item.setFont(font_normal);
        panel2.add(label_item, null);

        
        

        panel3 = new JPanel();
        panel3.setBorder(new TitledBorder(ic.getMessage("AMOUNT_LBL")));
        panel3.setLayout(null);
        panel3.setBounds(10, 310, 270, 110);
        panel.add(panel3, null);
        

        label = new JLabel(ic.getMessage("WEIGHT_TYPE") + ":");
        label.setBounds(20, 25, 100, 25);
        panel3.add(label, null);
        
        cb_weight_type = new JComboBox(this.wt_types);
        cb_weight_type.setBounds(130, 25, 125, 25);
        cb_weight_type.addItemListener(this);
        panel3.add(cb_weight_type, null);
        
        label_amount = new JLabel(ic.getMessage("WEIGHT") + ":");
        label_amount.setBounds(20, 65, 100, 25);
        panel3.add(label_amount, null);
        
        
        //JFormattedTextField jftf = JFormattedTextField(percentFormat);

        amountDisplayFormat = NumberFormat.getNumberInstance();
        amountDisplayFormat.setMinimumFractionDigits(1);
        amountEditFormat = NumberFormat.getNumberInstance();        
        amountEditFormat.setMinimumFractionDigits(1);
        
        
        amountField = new JFormattedTextField(
                new DefaultFormatterFactory(
                    new NumberFormatter(amountDisplayFormat),
                    new NumberFormatter(amountDisplayFormat),
                    new NumberFormatter(amountEditFormat)));
        amountField.setValue(new Double(1.0d));
        amountField.setColumns(4);
        amountField.setBounds(130, 65, 100, 25);
        amountField.setFocusLostBehavior(JFormattedTextField.COMMIT_OR_REVERT);
        panel3.add(amountField);

        
        label_home_weight = new JLabel(ic.getMessage("HOME_WEIGHT") + ":");
        label_home_weight.setBounds(20, 65, 100, 25);
        label_home_weight.setVisible(false);
        panel3.add(label_home_weight, null);
        
        
        button_hw_select = new JButton(ic.getMessage("SELECT"));
        button_hw_select.setBounds(155, 65, 100, 20);
        button_hw_select.setVisible(false);
        panel3.add(button_hw_select, null);
        
        label_home_weight_item = new JLabel(ic.getMessage("NO_ITEM_SELECTED"));
        label_home_weight_item.setBounds(20, 90, 255, 25);
        label_home_weight_item.setFont(this.font_normal);
        label_home_weight_item.setVisible(false);
        panel3.add(label_home_weight_item, null);
        
        

        
        
        
      /*
        percentDisplayFormat = NumberFormat.getPercentInstance();
        percentDisplayFormat.setMinimumFractionDigits(2);
        percentEditFormat = NumberFormat.getNumberInstance();
        percentEditFormat.setMinimumFractionDigits(2);
        */        
        
        //amountField.addPropertyChangeListener("value", this);        
        
        JButton button = new JButton("   " + ic.getMessage("OK"));
        button.setActionCommand("ok");
        button.setIcon(m_da.getImageIcon_22x22("ok.png", this));
        button.setBounds(25, 460, 115, 25);
        button.addActionListener(this);
        panel.add(button, null);
        
        this.buttons.put("ok", button);
        
        button = new JButton("   " + ic.getMessage("CANCEL"));
        button.setActionCommand("cancel");
        button.setIcon(m_da.getImageIcon_22x22("cancel.png", this));
        button.setBounds(150, 460, 115, 25);
        button.addActionListener(this);
        panel.add(button);
        
        this.buttons.put("cancel", button);
        
        
        button = m_da.createHelpButtonByBounds(150, 430, 115, 25, this);
        panel.add(button);

        this.buttons.put("help", button);
        
        
        this.add(panel, null);
    }
    
    public void setWeightType(int type)
    {
	if (current_weight_type==type)
	    return;
	
	
	if (type==0)
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
    

    /* 
     * itemStateChanged
     */
    public void itemStateChanged(ItemEvent arg0)
    {
	setWeightType(this.cb_weight_type.getSelectedIndex());
    }
    
    

    
    public boolean wasAction()
    {
	return (this.action_object!=null);
    }

    public int getSelectedObjectType()
    {
	return this.action_object_type;
    }
    
    public Object getSelectedObject()
    {
	return this.action_object;
    }
    
    public float getAmountValue()
    {
	Object o = this.amountField.getValue();
	
	if (o instanceof Long)
	{
	    //System.out.println("Amount(long): " + this.amountField.getValue());
	    Long l = (Long)o;
	    return l.floatValue();
	}
	else
	{
	
	    //System.out.println("Amount(double): " + this.amountField.getValue());
	
	    Double d = (Double)this.amountField.getValue();
	    return d.floatValue();
	}
    }
    

    public void actionPerformed(ActionEvent e)
    {
	String cmd = e.getActionCommand();
	
	if (cmd.equals("ok"))
	{
	    this.dispose();
	}
	else if (cmd.equals("cancel"))
	{
	    this.dispose();
	}
	else if (cmd.equals("select_item"))
	{
	    //System.out.println("Select item");
	    
	    NutritionTreeDialog ntd = new NutritionTreeDialog(m_da, this.cb_type.getSelectedIndex()+1, true);
	    
	    if (ntd.wasAction())
	    {
		
		if (this.cb_type.getSelectedIndex()==2)
		{
		    Meal m = (Meal)ntd.getSelectedObject();

		    if (m.getId() == this.input_id)
		    {
			JOptionPane.showMessageDialog(this, ic.getMessage("CANT_SELECT_CIRCULAR_MEAL"), ic.getMessage("WARNING"),JOptionPane.WARNING_MESSAGE);
			return;
		    }
		}
		
		
		this.label_item_type.setText("" + this.cb_type.getSelectedItem());
		
		this.action_object_type = (this.cb_type.getSelectedIndex() + 1);
		this.action_object = (ntd.getSelectedObject());
		
		if (this.cb_type.getSelectedIndex() < 2)
		{
		    FoodDescription fd = (FoodDescription)this.action_object;
		    this.label_item.setText(fd.getName());
		}
		else
		{
		    Meal m = (Meal)this.action_object;
		    this.label_item.setText(m.getName());
		}
	    }
	}
	    
    }

}
