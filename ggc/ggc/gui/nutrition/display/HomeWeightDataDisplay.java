
package ggc.gui.nutrition.display;

import com.atech.graphics.components.ATTableData;
import com.atech.i18n.I18nControlAbstract;



public class HomeWeightDataDisplay extends ATTableData
{

	private String id;
	private String weight_type;
	private float amount = 1.0f;
	private float weight;


	public HomeWeightDataDisplay(I18nControlAbstract ic)
	{
	    super(ic);
	}


	public HomeWeightDataDisplay(I18nControlAbstract ic, String full)
	{
	    super(ic);

	    int index = full.indexOf("=");
	    this.id = full.substring(0, index);

	    String val = full.substring(index+1);

	    if (val.indexOf("*")>-1)
	    {
		index = val.indexOf("*");
		this.amount = Float.parseFloat(val.substring(0, index));

		String ww = val.substring(index+1);

		if (ww.startsWith("."))
		    ww = "0" + ww;

		this.weight = Float.parseFloat(ww);
	    }
	    else
	    {
		this.weight = Float.parseFloat(val);
	    }
	}



	public void init()
	{
	    String[] cols = { /*"ID",*/ "WEIGHT_TYPE", "AMOUNT", "WEIGHT" };
	    float[] cols_size = { /*0.1f,*/ 0.5f, 0.25f, 0.25f  };

	    init(cols, cols_size);
	}



	public String getColumnValue(int column)
	{
	    switch(column)
	    {
		case 1:
		    return "" + this.amount;

		case 2:
		    return "" + this.weight;

		case 0:
		default:
		    return this.weight_type;


	    }
	}


	public String getId()
	{
	    return this.id;
	}

	public void setHomeWeightDefinition(String name)
	{
	    this.weight_type = ic.getMessage(name);
	}

    }
