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
 
import ggc.core.db.datalayer.HomeWeightSpecial;
import ggc.core.db.datalayer.NutritionHomeWeightType;
import ggc.core.util.DataAccess;

import java.awt.event.ItemEvent;
import java.util.ArrayList;

import com.atech.graphics.dialogs.selector.SelectableInterface;
import com.atech.graphics.dialogs.selector.SelectorAbstractDialog;



public class HWSelectorDialog extends SelectorAbstractDialog
{

    private DataAccess m_da = null;
    
    public static final int SELECTOR_HOME_WEIGHT = 1;
    
    private String hws = null;
    
    
    public HWSelectorDialog(DataAccess da, String hws) 
    {
        super(da.getParent(), da.getNutriI18nControl(), SELECTOR_HOME_WEIGHT, false);
        this.hws = hws;
        this.init();
        this.showDialog();
    }

  
    
    public void initSelectorValuesForType()
    {
	//if (this.getSelectorType() == FoodPartSelectorDialog.SELECTOR_HOME_WEIGHT)
	{
            setSelectorObject(new HomeWeightSpecial());
            setSelectorName(ic.getMessage("SELECTOR_NUTRITION_DEFINITION"));
            setHelpStringId("ggc.food.user.select.nutrition");
            setNewItemString(ic.getMessage("NEW_NUTR_DEF"));
    	    setAllowedActions(SelectorAbstractDialog.SELECTOR_ACTION_CANCEL_AND_SELECT);
    	    this.getDescriptions().put("DESC_1", ic.getMessage("NUTRITION_NAME"));
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
	    this.full = getFullList();
	}
	else
	    System.out.println("getFullData(): type not handled (" + this.getSelectorType() + ")");
    }
    
    
    public void setHelpContext()
    {
	m_da.getHelpContext().getMainHelpBroker().enableHelpOnButton(this.getHelpButton(), this.getHelpId(), null);
	m_da.getHelpContext().getMainHelpBroker().enableHelpKey(this.getRootPane(), this.getHelpId(), null);
    }
    

    public ArrayList<SelectableInterface> getFullList()
    {
	ArrayList<SelectableInterface> lst = new ArrayList<SelectableInterface>();
	
	if ((this.hws==null) || (hws.trim().length()==0))
	    return lst;
	
	String[] hw_elems = m_da.splitString(this.hws.trim(), ";");
	
	for(int i=0; i<hw_elems.length; i++)
	{
	    String prt[] = m_da.splitString(hw_elems[i], "=");
	    
	    long id = Long.parseLong(prt[0]);
	    
	    NutritionHomeWeightType nhwt = m_da.getDb().getNutritionHomeWeight(id);
	    
	    
	    
	    HomeWeightSpecial hws_el = null; 
	    
	    if (prt[1].contains("*"))
	    {
		// we have amount
		String val_am[] = m_da.splitString(prt[1], "*");
		
		hws_el = new HomeWeightSpecial(id, nhwt.getName(), val_am[0], val_am[1]);
	    }
	    else
	    {
		hws_el = new HomeWeightSpecial(id, nhwt.getName(), prt[1]);
	    }
	    
	    lst.add(hws_el);
	    
	    
	}
	
	return lst;
	
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
