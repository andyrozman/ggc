package ggc.core.nutrition.display;

import ggc.core.db.datalayer.MealPart;

import com.atech.graphics.components.ATTableData;
import com.atech.i18n.I18nControlAbstract;

public class MealFoodDisplay extends ATTableData
{

    private String type_desc;
    private String id;
    private String description;
    private String amount;

    public static String[] type_description = null;

    MealPart meal_part = null;

    public MealFoodDisplay(I18nControlAbstract ic)
    {
	super(ic);
	initStatic();
    }

    public MealFoodDisplay(I18nControlAbstract ic, String full)
    {
	super(ic);
	// System.out.println("Nutr: " + full);
	int index = full.indexOf("=");
	this.id = full.substring(0, index);
	// this.value = full.substring(index+1);
	initStatic();
    }

    public MealFoodDisplay(I18nControlAbstract ic, MealPart part)
    {
	super(ic);
	initStatic();

	this.type_desc = MealPartsDisplay.type_description[part.getType() - 1];
	this.id = "" + part.getId();
	this.description = part.getName();
	this.amount = "" + part.getAmount();

	this.meal_part = part;

	// String[] col = { "TYPE", "ID", "DESCRIPTION", "AMOUNT" };

    }

    public MealPart getMealPart()
    {
	return this.meal_part;
    }

    /*
     * public void setNutritionDefinition(NutritionDefinition def) { this.id = "" +
     * def.getId(); // this.name = def.getName(); //this.value =
     * def.get.getTag(); // this.weight_unit = def.getWeight_unit(); }
     */

    public void initStatic()
    {
	if (MealPartsDisplay.type_description == null)
	{

	    MealPartsDisplay.type_description = new String[3];
	    MealPartsDisplay.type_description[0] = ic
		    .getMessage("USDA_NUTRITION");
	    MealPartsDisplay.type_description[1] = ic
		    .getMessage("USER_NUTRITION");
	    MealPartsDisplay.type_description[2] = ic.getMessage("MEAL");
	}

    }

    @Override
    public void init()
    {
	String[] col = { "NAME", "WEIGHT_TYPE", "HOME_WEIGHT", "AMOUNT_LBL" };
	float[] col_size = { 0.5f, 0.2f, 0.2f, 0.1f };

	init(col, col_size);
    }

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

    @Override
    public String getColumnValue(int column)
    {
	switch (column)
	{
	    case 1:
		return this.description;
	    case 2:
		return this.amount;

	    case 0:
	    default:
		return "" + this.type_desc;

		/*
		 * case 1: return this.id; case 2: return this.description; case
		 * 3: return this.amount;
		 * 
		 * case 0: default: return "" + this.type_desc;
		 */

	}
    }
}
