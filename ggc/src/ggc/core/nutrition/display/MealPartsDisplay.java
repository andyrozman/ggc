package ggc.core.nutrition.display;

import ggc.core.db.datalayer.DailyFoodEntry;
import ggc.core.db.datalayer.MealPart;

import com.atech.graphics.components.ATTableData;
import com.atech.i18n.I18nControlAbstract;

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
 *  Filename:     MealPartsDisplay  
 *  Description:  Meal Parts Display
 * 
 *  Author: andyrozman {andy@atech-software.com}  
 */


public class MealPartsDisplay extends ATTableData
    {

	private String type_desc;
	private String id;
	private String description;
	private String amount;
	
	/**
	 * Type Description
	 */
	public static String[] type_description = null;
	
	MealPart meal_part = null;
	DailyFoodEntry daily_food_entry = null;

	
	/**
	 * Constructor
	 * 
	 * @param ic
	 */
	public MealPartsDisplay(I18nControlAbstract ic)
	{
	    super(ic);
	    initStatic();
	}


	/**
	 * Constructor
	 * 
	 * @param ic
	 * @param full
	 */
	public MealPartsDisplay(I18nControlAbstract ic, String full)
	{
	    super(ic);
	    int index = full.indexOf("=");
	    this.id = full.substring(0, index);
//	    this.value = full.substring(index+1);
	    initStatic();
	}


	/**
	 * Constructor
	 * 
	 * @param ic
	 * @param part
	 */
	public MealPartsDisplay(I18nControlAbstract ic, MealPart part)
	{
	    super(ic);
	    initStatic();

	    this.type_desc = MealPartsDisplay.type_description[part.getType()-1];
	    this.id = "" + part.getId();
	    this.description = part.getName();
	    this.amount = "" + part.getAmount();
	    
	    this.meal_part = part;
	}
	
	
	/**
	 * Constructor
	 * 
	 * @param ic
	 * @param part
	 */
	public MealPartsDisplay(I18nControlAbstract ic, DailyFoodEntry part)
	{
	    super(ic);
	    initStatic();

	    this.type_desc = MealPartsDisplay.type_description[part.getFoodType()-1];
	    this.id = "" + part.getItemId();
	    this.description = part.getName();
	    this.amount = "" + part.getAmountString();
	    
	    //this.meal_part = part;
	    daily_food_entry = part;
//	    String[] col = { "TYPE", "ID", "DESCRIPTION", "AMOUNT" };

	    
	}
	
	
	
	/**
	 * Get Meal Part
	 * 
	 * @return
	 */
	public MealPart getMealPart()
	{
	    return this.meal_part;
	}
	
	/**
	 * Get Daily Food Entry
	 * 
	 * @return
	 */
	public DailyFoodEntry getDailyFoodEntry()
	{
	    return this.daily_food_entry;
	}
	
	
	private void initStatic()
	{
	    if (MealPartsDisplay.type_description==null)
	    {
    		MealPartsDisplay.type_description = new String[3];
    		MealPartsDisplay.type_description[0] = ic.getMessage("USDA_NUTRITION");
    		MealPartsDisplay.type_description[1] = ic.getMessage("USER_NUTRITION");
    		MealPartsDisplay.type_description[2] = ic.getMessage("MEAL");
	    }
	    
	}
	

	/**
	 * Init
	 * 
	 * @see com.atech.graphics.components.ATTableData#init()
	 */
	public void init()
	{
	    String[] col = { "MEAL_TYPE", "DESCRIPTION", "AMOUNT_LBL" };
	    float[] col_size = { 0.25f, 0.65f, 0.1f  };

	    init(col, col_size);
	}

	/**
	 * Get Id
	 * 
	 * @return
	 */
	public String getId()
	{
	    return this.id;
	}

	/**
	 * Set Amount
	 * 
	 * @param amount
	 */
	public void setAmount(float amount)
	{
	    this.amount = "" + amount;
	    this.meal_part.setAmount(amount);
	}
	
	
	
	

	/**
	 * Get Column Value
	 * 
	 * @see com.atech.graphics.components.ATTableData#getColumnValue(int)
	 */
	public String getColumnValue(int column)
	{
	    switch(column)
	    {
		case 1:
		    return this.description;
		case 2:
		    return this.amount;

		case 0:
		default:
		    return "" + this.type_desc;

		
		/*
		case 1:
		    return this.id;
		case 2:
		    return this.description;
		case 3:
		    return this.amount;

		case 0:
		default:
		    return "" + this.type_desc;
		    */

	    }
	}
	
	
    /**
     * Get Save Data
     * 
     * @return
     */
    public String getSaveData()
    {
        return this.meal_part.getType() + ":" + this.meal_part.getId() + "=" + this.amount;
    }
	
	
	
}
