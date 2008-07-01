package ggc.core.nutrition.display;

import ggc.core.db.datalayer.MealNutrition;
import ggc.core.db.datalayer.NutritionDefinition;
import ggc.core.util.DataAccess;

import java.util.Comparator;

import com.atech.graphics.components.ATTableData;
import com.atech.i18n.I18nControlAbstract;

public class MealNutritionsDisplay extends ATTableData implements Comparator<MealNutritionsDisplay>
{

    private String id;
    private String value;
    private String name;
    private String weight_unit;
    private String decimal_places;

    float calculated_value;

    // private float amount = 1.0f;

    public MealNutritionsDisplay(I18nControlAbstract ic)
    {
        super(ic);
    }

    /*
     * public MealNutritionsDisplay(I18nControlAbstract ic, String full) {
     * super(ic);
     * 
     * int index = full.indexOf("="); this.id = full.substring(0, index);
     * 
     * String val = full.substring(index+1);
     * 
     * if (val.indexOf("*")>-1) { index = val.indexOf("*"); this.amount =
     * Float.parseFloat(val.substring(0, index));
     * 
     * String ww = val.substring(index+1);
     * 
     * if (ww.startsWith(".")) ww = "0" + ww;
     * 
     * this.weight = Float.parseFloat(ww); } else { this.weight =
     * Float.parseFloat(val); } }
     */

    public MealNutritionsDisplay(I18nControlAbstract ic, MealNutrition mn)
    {
        super(ic);
        this.id = "" + mn.getId();
        this.value = "" + mn.getAmountSum(); // proc v1 .getCalculatedAmount();
        // this.calculated_value = mn.getAmount();

    }

    public void setNutritionDefinition(NutritionDefinition def)
    {
        this.id = "" + def.getId();
        this.name = def.getResolvedName(); // .getName();
        // this.value = def.get.getTag();
        this.weight_unit = def.getWeight_unit();
        this.decimal_places = def.getDecimal_places();
    }

    public void init()
    {
        /*
         * String[] cols = { "ID", "NUTRITION", "AMOUNT", "UNITS" }; float[]
         * cols_size = { 0.1f, 0.5f, 0.2f, 0.2f };
         * 
         * init(cols, cols_size);
         */

        String[] cols = { "NUTRITION", "AMOUNT_LBL", "UNITS" };
        float[] cols_size = { 0.6f, 0.2f, 0.2f };

        init(cols, cols_size);

    }

    public String getValue()
    {
        float fl = Float.parseFloat(this.value);
        return DataAccess.getFloatAsString(fl, this.decimal_places);
    }

    public String getColumnValue(int column)
    {

        switch (column)
        {
        case 1:
            return this.getValue();
        case 2:
            return this.weight_unit;

        case 0:
        default:
            return this.name;

        }

        /*
         * switch(column) { case 1: return this.name; case 2: return this.value;
         * case 3: return this.weight_unit;
         * 
         * case 0: default: return "" + this.id; }
         */
    }

    /*
     * (non-Javadoc)
     * 
     * @see java.util.Comparator#compare(java.lang.Object, java.lang.Object)
     */
    public int compare(MealNutritionsDisplay mnd1, MealNutritionsDisplay mnd2)
    {
        return mnd1.name.compareTo(mnd2.name);
    }

    public String getSaveData()
    {
        return this.id + "=" + this.getValue();
    }

    public String getId()
    {
        return this.id;
    }

    public String toString()
    {
        return getSaveData();
    }

}
