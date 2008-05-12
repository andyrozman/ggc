
package ggc.core.nutrition.display;

import ggc.core.db.datalayer.NutritionDefinition;
import ggc.core.util.DataAccess;

import com.atech.graphics.components.ATTableData;
import com.atech.i18n.I18nControlAbstract;

public class NutritionDataDisplay extends ATTableData
    {

	private String id;
	private String value;
	private String name;
	private String weight_unit;
	private String decimal_places;


	public NutritionDataDisplay(I18nControlAbstract ic)
	{
	    super(ic);
	}


	public NutritionDataDisplay(I18nControlAbstract ic, String full)
	{
	    super(ic);
	//    System.out.println("Nutr: " + full);
	    int index = full.indexOf("=");
	    this.id = full.substring(0, index);
	    this.value = full.substring(index+1);
	}


	public NutritionDataDisplay(I18nControlAbstract ic, NutritionDefinition def, float value)
	{
	    super(ic);
	    setNutritionDefinition(def);
	    this.decimal_places = def.getDecimal_places();
	    this.setAmount(value);
	}
	
	
	
	
	public void setNutritionDefinition(NutritionDefinition def)
	{
	    if (def==null)
	    	return;
	    
	    this.id = "" + def.getId();
	    this.name = def.getName();
	    //this.value = def.get.getTag();
	    this.weight_unit = def.getWeight_unit();
	}


	public void init()
	{
	    String[] col = { "ID", "NUTRITION", "AMOUNT_LBL", "UNITS" };
	    float[] col_size = { 0.1f, 0.5f, 0.2f, 0.2f  };

	    init(col, col_size);
	}

	public String getId()
	{
	    return this.id;
	}


	public String getColumnValue(int column)
	{
	    switch(column)
	    {
		case 1:
		    return this.name;
		case 2:
		    return this.value;
		case 3:
		    return this.weight_unit;

		case 0:
		default:
		    return "" + this.id;

	    }
	}
	
	public String getName()
	{
	    return this.name;
	}
	
	public float getAmount()
	{

	    //return DataAccess.getFloatAsString(this.value, this.decimal_places);
	    float f = 0.0f;
	    try
	    {
		f = Float.parseFloat(this.value.replace(',', '.'));
	    }
	    catch(Exception ex)
	    {
		System.out.println("Float parse ex: " + ex);
	    }

	    return f;
	}
	
	public String getWeightUnit()
	{
	    return this.weight_unit;
	}
	
	
	public void setAmount(float val)
	{
	    this.value = DataAccess.getFloatAsString(val, this.decimal_places);
	}
	
	public String getSaveData()
	{
	    return this.id + "=" + this.value;
	}
	
	
    }
