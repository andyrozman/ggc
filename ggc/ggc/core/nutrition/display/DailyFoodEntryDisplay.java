package ggc.core.nutrition.display;

import ggc.core.db.datalayer.DailyFoodEntry;
import ggc.core.db.datalayer.MealPart;

import com.atech.graphics.components.ATTableData;
import com.atech.i18n.I18nControlAbstract;

public class DailyFoodEntryDisplay extends ATTableData
{

    //private String type_desc;
    //private String id;
    //private String description;
    //private String amount;

    public static String[] weight_type_description = null;

    //MealPart meal_part = null;
    
    DailyFoodEntry daily_food_entry = null; 

    public DailyFoodEntryDisplay(I18nControlAbstract ic)
    {
	super(ic);
	initStatic();
    }
/*
    public DailyFoodEntryDisplay(I18nControlAbstract ic, String full)
    {
	super(ic);
	// System.out.println("Nutr: " + full);
	int index = full.indexOf("=");
	this.id = full.substring(0, index);
	// this.value = full.substring(index+1);
	initStatic();
    }
*/
    public DailyFoodEntryDisplay(I18nControlAbstract ic, DailyFoodEntry part)
    {
	super(ic);
	initStatic();
	
	this.daily_food_entry = part;

	/*
	this.type_desc = MealPartsDisplay.type_description[part.getType() - 1];
	this.id = "" + part.getId();
	this.description = part.getName();
	this.amount = "" + part.getAmount();

	this.meal_part = part;
	*/
	// String[] col = { "TYPE", "ID", "DESCRIPTION", "AMOUNT" };

    }



    /*
     * public void setNutritionDefinition(NutritionDefinition def) { this.id = "" +
     * def.getId(); // this.name = def.getName(); //this.value =
     * def.get.getTag(); // this.weight_unit = def.getWeight_unit(); }
     */

    public void initStatic()
    {
	if (DailyFoodEntryDisplay.weight_type_description == null)
	{
	    DailyFoodEntryDisplay.weight_type_description = new String[3];
	    DailyFoodEntryDisplay.weight_type_description[0] = ic.getMessage("WEIGHT_LBL2");
	    DailyFoodEntryDisplay.weight_type_description[1] = ic.getMessage("HOME_WEIGHT");
	    DailyFoodEntryDisplay.weight_type_description[2] = ic.getMessage("AMOUNT_LBL");
	}

    }

    public void init()
    {
	String[] col = { "NAME", "WEIGHT_TYPE", "HOME_WEIGHT", "AMOUNT_LBL" };
	float[] col_size = { 0.5f, 0.2f, 0.2f, 0.1f };

	init(col, col_size);
    }

    
    public void resetWeightValues(DailyFoodEntry dfe)
    {
	this.daily_food_entry.resetWeightValues(dfe);
    }
    
    
    /*
    public String getId()
    {
	return this.id;
    }

    public void setAmount(float amount)
    {
	this.amount = "" + amount;
	this.meal_part.setAmount(amount);
    }

    public String getSaveData()
    {
	return this.meal_part.getType() + ":" + this.meal_part.getId() + "="
		+ this.amount;
    }
    */

    public DailyFoodEntry getDailyFoodEntry()
    {
	return this.daily_food_entry;
    }
    
    
    public String getStringForDb()
    {
	return this.daily_food_entry.getValueString();
    }
    
    
    public String getColumnValue(int column)
    {
	//String[] col = { "NAME", "WEIGHT_TYPE", "HOME_WEIGHT", "AMOUNT_LBL" };

	
	switch (column)
	{
	    case 1:
		return DailyFoodEntryDisplay.weight_type_description[this.daily_food_entry.getWeightType()-1];
	    
	    case 2:
		return this.daily_food_entry.getHomeWeightDescription();
		
	    case 3:
		return this.daily_food_entry.getAmountString();

	    case 0:
	    default:
		return this.daily_food_entry.getName();

		/*
		 * case 1: return this.id; case 2: return this.description; case
		 * 3: return this.amount;
		 * 
		 * case 0: default: return "" + this.type_desc;
		 */

	}
    }
}
