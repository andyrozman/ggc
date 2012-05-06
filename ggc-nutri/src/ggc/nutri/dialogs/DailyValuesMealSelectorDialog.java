package ggc.nutri.dialogs;

import ggc.nutri.panels.PanelMealSelector;
import ggc.nutri.util.DataAccessNutri;

import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JTextField;

import com.atech.graphics.dialogs.TransferDialog;
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
 *  Filename:     DailyValuesMealSelectorDialog
 *  Description:  Main Dialog used for selecting meals and foods for Daily Values.
 *  
 *  Author: andyrozman {andy@atech-software.com}  
 */

public class DailyValuesMealSelectorDialog extends TransferDialog implements ActionListener
{

    private static final long serialVersionUID = 9105408177791270640L;
    JTextField tf_selected;
    JComboBox cb_type;
    Font font_normal, font_normal_b;
    JButton button_select;

    private DataAccessNutri m_da = null;
    private I18nControlAbstract ic = null;

    String[] type;

    String meals_ids = null;
    PanelMealSelector panel_meal_selector = null;

    /**
     * Constructor
     * 
     * @param da
     * @param meals_id
     */
    public DailyValuesMealSelectorDialog(ATDataAccessAbstract da, String meals_id)
    {
        super((JDialog)da.getCurrentComponent());

        m_da = DataAccessNutri.getInstance();
        ic = m_da.getI18nControlInstance();
        this.meals_ids = meals_id;
        init();

        this.setVisible(true);
    }

    /**
     * Constructor
     * 
     * @param da
     * @param dialog 
     * @param meals_id
     */
    public DailyValuesMealSelectorDialog(ATDataAccessAbstract da, JDialog dialog, String meals_id)
    {
        super(dialog);

        m_da = DataAccessNutri.getInstance();
        ic = m_da.getI18nControlInstance();
        this.meals_ids = meals_id;
        init();

        this.setVisible(true);
    }
    
     
    
    
    
    
    private void init()
    {
        this.setTitle(ic.getMessage("MEALS_FOODS_SELECTOR_DAILY"));
        this.setBounds(160, 100, 550, 500);
        this.panel_meal_selector = new PanelMealSelector(this, this, this.meals_ids);
        this.add(this.panel_meal_selector, null);
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
            action_done = true;
            this.dispose();
        }
        else if (cmd.equals("cancel"))
        {
            action_done = false;
            // System.out.println("Cancel");
            this.dispose();
        }
        /*else if (cmd.equals("add_food"))
        {
            MealSpecialSelectorDialog mssd = new MealSpecialSelectorDialog(m_da, 0L);
            DailyFoodEntryDisplay dfed = new DailyFoodEntryDisplay(ic, mssd.getDailyFoodEntry());
            
            this.panel_meal_selector.addFoodPart(dfed);
        }
            else if (cmd.equals("edit_food"))
            {
                
                if (this.panel_meal_selector.getFoodTable().getSelectedRowCount()==0)
                {
                    JOptionPane.showConfirmDialog(this, ic.getMessage("SELECT_ITEM_FIRST"), ic.getMessage("ERROR"), JOptionPane.CLOSED_OPTION);
                    return;
                }

                NutritionDataDisplay ndd = this.list_nutritions.get(this.table_1.getSelectedRow());

                FoodPartMainSelectorDialog fpmsd = new FoodPartMainSelectorDialog(m_da, ndd);            
                
                if (fpmsd.wasAction())
                {
            	System.out.println("Returned value: " + fpmsd.getAmountValue());
            	
            	ndd.setAmount(fpmsd.getAmountValue());
            	this.createModel(this.list_nutritions, this.table_1, this.ndd);
                }
                
            }
            else if (action.equals("remove_nutrition"))
            {
                if (this.table_1.getSelectedRowCount()==0)
                {
                    JOptionPane.showConfirmDialog(this, ic.getMessage("SELECT_ITEM_FIRST"), ic.getMessage("ERROR"), JOptionPane.CLOSED_OPTION);
                    return;
                }

                int ii = JOptionPane.showConfirmDialog(this, ic.getMessage("ARE_YOU_SURE_DELETE"), ic.getMessage("ERROR"), JOptionPane.YES_NO_OPTION);

                if (ii==JOptionPane.YES_OPTION)
                {
                    NutritionDataDisplay ndd = this.list_nutritions.get(this.table_1.getSelectedRow());
            	
                    this.list_nutritions.remove(ndd);
            	this.createModel(this.list_nutritions, this.table_1, this.ndd);
                }
                
            }*/

        /*else if (cmd.equals("select_item"))
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
        }*/
        else
            System.out.println("DailyValuesMealSelectorDialog::unknown command = " + cmd);

    }

    /**
     * Was Action
     * 
     * @see com.atech.graphics.dialogs.TransferDialog#wasAction()
     */
    public boolean wasAction()
    {
        return this.action_done;
    }

    /**
     * Get String For Db
     * 
     * @return
     */
    public String getStringForDb()
    {
        return this.panel_meal_selector.getStringForDb();
    }

    /**
     * Get CH Sum
     * 
     * @return
     */
    public String getCHSum()
    {
        return this.panel_meal_selector.getCHSumString();
    }

    /**
     * Get Result Values
     * 
     * @see com.atech.graphics.dialogs.TransferDialog#getResultValues()
     */
    public Object[] getResultValues()
    {
        Object[] objs = new Object[2];
        objs[0] = this.getStringForDb();
        objs[1] = this.getCHSum();

        return objs;
    }

    /**
     * Get Result Values as String
     * 
     * @see com.atech.graphics.dialogs.TransferDialog#getResultValuesString()
     */
    public String[] getResultValuesString()
    {
        String[] objs = new String[2];
        objs[0] = this.getStringForDb();
        objs[1] = this.getCHSum();

        return objs;
    }

    
    /**
     * Set Input Paramters
     * 
     * @param ip
     */
    @Override
    public void setInputParameters(Object[] ip)
    {
        // TODO Auto-generated method stub
        
    }

    
    /**
     * Show Dialog
     * 
     * @param visible
     */
    @Override
    public void showDialog(boolean visible)
    {
        // TODO Auto-generated method stub
        
    }


    /**
     * Get Input Parameters Count
     * 
     * @return
     */
    @Override
    public int getInputParametersCount()
    {
        return 0;
    }

    /*
    public static void main(String[] args)
    {
        new DailyValuesMealSelectorDialog(DataAccessNutri.getInstance(), null);
    }*/

}
