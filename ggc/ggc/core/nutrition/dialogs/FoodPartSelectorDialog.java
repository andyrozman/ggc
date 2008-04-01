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
 
import ggc.db.datalayer.NutritionDefinition;
import ggc.db.datalayer.NutritionHomeWeightType;
import ggc.util.DataAccess;

import java.awt.event.ItemEvent;

import com.atech.graphics.dialogs.selector.SelectorAbstractDialog;



public class FoodPartSelectorDialog extends SelectorAbstractDialog
{

    private DataAccess m_da = null;
    
    public static final int SELECTOR_NUTRITION = 1;
    public static final int SELECTOR_HOME_WEIGHT = 2;
    
    
    public FoodPartSelectorDialog(DataAccess da, int type, String except) 
    {
        super(da.getParent(), da.getI18nControlInstance(), type, except, true);
    }

  
    
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

     
    


    /* (non-Javadoc)
     * @see com.atech.graphics.components.selector.SelectorAbstractDialog#getFullData()
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
    
    
    public void setHelpContext()
    {
	m_da.getHelpContext().getMainHelpBroker().enableHelpOnButton(this.getHelpButton(), this.getHelpId(), null);
	m_da.getHelpContext().getMainHelpBroker().enableHelpKey(this.getRootPane(), this.getHelpId(), null);
    }
    


    /* (non-Javadoc)
     * @see com.atech.graphics.components.selector.SelectorAbstractDialog#checkAndExecuteActionEdit()
     */
    @Override
    public void checkAndExecuteActionEdit()
    {
	// TODO Auto-generated method stub
	System.out.println("checkAndExecuteActionEdit()");
	
    }


    /* (non-Javadoc)
     * @see com.atech.graphics.components.selector.SelectorAbstractDialog#checkAndExecuteActionNew()
     */
    @Override
    public void checkAndExecuteActionNew()
    {
	// TODO Auto-generated method stub
	System.out.println("checkAndExecuteActionNew()");
    }


    /* (non-Javadoc)
     * @see com.atech.graphics.components.selector.SelectorAbstractDialog#checkAndExecuteActionSelect()
     */
    @Override
    public void checkAndExecuteActionSelect()
    {
        if (table!=null)
        {
//            System.out.println("in select action");
            if (table.getSelectedRow()==-1)
                return;

            this.selected_object = list.get(table.getSelectedRow());
//            System.out.println("in select action: selected:" + this.selected_object);
            
            this.dispose();
        }
	
    }



    /* (non-Javadoc)
     * @see com.atech.graphics.components.selector.SelectorAbstractDialog#itemStateChanged(java.awt.event.ItemEvent)
     */
    @Override
    public void itemStateChanged(ItemEvent e)
    {
	// TODO Auto-generated method stub
	System.out.println("itemStateChanged()");
	
    }
    
    
    
    
}
