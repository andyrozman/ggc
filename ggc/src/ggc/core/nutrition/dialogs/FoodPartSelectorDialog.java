package ggc.core.nutrition.dialogs;
 
import ggc.core.db.datalayer.NutritionDefinition;
import ggc.core.db.datalayer.NutritionHomeWeightType;
import ggc.core.util.DataAccess;

import java.awt.event.ItemEvent;

import com.atech.graphics.dialogs.selector.SelectorAbstractDialog;


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
 *  Filename:     FoodPartSelectorDialog
 *  Description:  Selector component for Nutrition and HomeWeight parts...
 * 
 *  Author: andyrozman {andy@atech-software.com}  
 */

public class FoodPartSelectorDialog extends SelectorAbstractDialog
{

    private static final long serialVersionUID = 7262008735572008874L;
    private DataAccess m_da = null;
    
    /**
     * Selector: Nutrition
     */
    public static final int SELECTOR_NUTRITION = 1;
    
    /**
     * Selector: Home Weight
     */
    public static final int SELECTOR_HOME_WEIGHT = 2;
    
    
    /**
     * Constructor
     * 
     * @param da
     * @param type
     * @param except
     */
    public FoodPartSelectorDialog(DataAccess da, int type, String except) 
    {
        super(da.getParent(), da.getNutriI18nControl(), type, except, true);
        this.showDialog();
    }

  
    
    /**
     * Init Selector Values For Type
     */
    public void initSelectorValuesForType()
    {
    	if (this.getSelectorType() == FoodPartSelectorDialog.SELECTOR_NUTRITION)
    	{
                setSelectorObject(new NutritionDefinition());
                setSelectorName(ic.getMessage("SELECTOR_NUTRITION_DEFINITION"));
                setHelpStringId("ggc.food.user.select.nutrition");
                setNewItemString(ic.getMessage("NEW_NUTR_DEF"));
        	    setAllowedActions(SelectorAbstractDialog.SELECTOR_ACTION_CANCEL_AND_SELECT);
        	    this.getDescriptions().put("DESC_1", ic.getMessage("NUTRITION_NAME"));
        	    setFilterType(SelectorAbstractDialog.SELECTOR_FILTER_TEXT);
    	}
    	else if (this.getSelectorType() == FoodPartSelectorDialog.SELECTOR_HOME_WEIGHT)
    	{
                setSelectorObject(new NutritionHomeWeightType());
                setSelectorName(ic.getMessage("SELECTOR_HOME_WEIGHT"));
                setHelpStringId("ggc.food.user.select.home_weight");
                setNewItemString(ic.getMessage("NEW_HOME_WEIGHT"));
        	    setAllowedActions(SelectorAbstractDialog.SELECTOR_ACTION_CANCEL_AND_SELECT);
        	    this.getDescriptions().put("DESC_1", ic.getMessage("HOME_WEIGHT_NAME"));
        	    setFilterType(SelectorAbstractDialog.SELECTOR_FILTER_TEXT);
    	}
	
    }

     
    


    /**
     * Get Full Data
     * @see com.atech.graphics.dialogs.selector.SelectorAbstractDialog#getFullData()
     */
    public void getFullData()
    {
    	this.m_da = DataAccess.getInstance();
    	
    	if (this.getSelectorType()==FoodPartSelectorDialog.SELECTOR_NUTRITION)
    	{
    	    this.full = this.m_da.getDb().getNutritionDefinitions();
    	}
    	else if (this.getSelectorType()==FoodPartSelectorDialog.SELECTOR_HOME_WEIGHT)
    	{
    	    this.full = this.m_da.getDb().getNutritionHomeWeights();
    	}
    	else
    	    System.out.println("getFullData(): type not handled (" + this.getSelectorType() + ")");
    }
    
    
    /**
     * Set Help Context
     */
    public void setHelpContext()
    {
    	m_da.getHelpContext().getMainHelpBroker().enableHelpOnButton(this.getHelpButton(), this.getHelpId(), null);
    	m_da.getHelpContext().getMainHelpBroker().enableHelpKey(this.getRootPane(), this.getHelpId(), null);
    }
    


    /**
     * Check and Execute Action: Edit
     */
    @Override
    public void checkAndExecuteActionEdit()
    {
        System.out.println("checkAndExecuteActionEdit()");
    }


    /**
     * Check and Execute Action: New
     */
    @Override
    public void checkAndExecuteActionNew()
    {
        System.out.println("checkAndExecuteActionNew()");
    }


    /**
     * Check and Execute Action: Select
     */
    @Override
    public void checkAndExecuteActionSelect()
    {
        if (table!=null)
        {
            if (table.getSelectedRow()==-1)
                return;

            this.selected_object = list.get(table.getSelectedRow());
            this.dispose();
        }
	
    }


    /**
     * Item State Changed
     * 
     * @see com.atech.graphics.dialogs.selector.SelectorAbstractDialog#itemStateChanged(java.awt.event.ItemEvent)
     */
    @Override
    public void itemStateChanged(ItemEvent e)
    {
    }
    
    
}
