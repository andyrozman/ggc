package ggc.nutri.dialogs;
 
import ggc.nutri.db.datalayer.HomeWeightSpecial;
import ggc.nutri.db.datalayer.NutritionHomeWeightType;
import ggc.nutri.util.DataAccessNutri;

import java.awt.event.ItemEvent;
import java.util.ArrayList;

import javax.swing.JDialog;
import javax.swing.JFrame;

import com.atech.graphics.dialogs.selector.SelectableInterface;
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
 *  Filename:     HWSelectorDialog
 *  Description:  Selector for Home Weights present in specific food or meal
 * 
 *  Author: andyrozman {andy@atech-software.com}  
 */


public class HWSelectorDialog extends SelectorAbstractDialog
{

    /**
     * 
     */
    private static final long serialVersionUID = -3282479938902100574L;

    private DataAccessNutri m_da = null;
    
    /**
     * Selector: Home Weight
     */
    public static final int SELECTOR_HOME_WEIGHT = 1;
    
    private String hws = null;
    
    
    /**
     * Constructor
     * 
     * @param parent 
     * @param da
     * @param hws
     */
    public HWSelectorDialog(JDialog parent, DataAccessNutri da, String hws) 
    {
        super(parent, da, SELECTOR_HOME_WEIGHT, false);
        this.hws = hws;
        this.init();
        this.showDialog();
    }

    /**
     * Constructor
     * 
     * @param parent 
     * @param da
     * @param hws
     */
    public HWSelectorDialog(JFrame parent, DataAccessNutri da, String hws) 
    {
        super(parent, da, SELECTOR_HOME_WEIGHT, false);
        this.hws = hws;
        this.init();
        this.showDialog();
    }

    
    /**
     * Init Selector Values For Type
     */
    public void initSelectorValuesForType()
    {
        setSelectorObject(new HomeWeightSpecial());
        setSelectorName(ic.getMessage("SELECTOR_HOME_WEIGHT"));
        setHelpStringId("GGC_Food_User_Select_HomeWeight_Main");
        setNewItemString(ic.getMessage("NEW_NUTR_DEF"));
	    setAllowedActions(SelectorAbstractDialog.SELECTOR_ACTION_CANCEL_AND_SELECT);
	    this.getDescriptions().put("DESC_1", ic.getMessage("HOME_WEIGHT_NAME"));
	    setFilterType(SelectorAbstractDialog.SELECTOR_FILTER_TEXT);
	    this.setHelpEnabled(true);
    }

     
    


    /**
     * Get Full Data
     * 
     * @see com.atech.graphics.dialogs.selector.SelectorAbstractDialog#getFullData()
     */
    public void getFullData()
    {
    	this.m_da = DataAccessNutri.getInstance();
    	
    	if (this.getSelectorType()==FoodPartSelectorDialog.SELECTOR_NUTRITION)
    	{
    	    this.full = getFullList();
    	}
    	else
    	    System.out.println("getFullData(): type not handled (" + this.getSelectorType() + ")");
    }
    
    
    

    /**
     * Get Full List
     * 
     * @return
     */
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
    	    
    	    NutritionHomeWeightType nhwt = m_da.getNutriDb().getNutritionHomeWeight(id);
    	    
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
    
    
    /**
     * Check and Execute Action: Edit
     */
    @Override
    public void checkAndExecuteActionEdit(SelectableInterface si)
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
        System.out.println("itemStateChanged()");
    }
    
    
    
    
}
