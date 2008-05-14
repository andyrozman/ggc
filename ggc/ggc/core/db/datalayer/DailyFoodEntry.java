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
 *  Filename: NutritionHomeWeightType
 *  Purpose:  This is datalayer file (data file, with methods to work with database or in 
 *      this case Hibernate). 
 *      This one is used for Food's Home Weights.
 *
 *  Author:   andyrozman  {andy@atech-software.com}
 */

package ggc.core.db.datalayer;

import ggc.core.nutrition.GGCTreeRoot;
import ggc.core.util.DataAccess;
import ggc.core.util.NutriI18nControl;

import java.util.ArrayList;
import java.util.StringTokenizer;

import com.atech.graphics.dialogs.selector.ColumnSorter;
import com.atech.graphics.dialogs.selector.SelectableInterface;


public class DailyFoodEntry implements SelectableInterface
{

    NutriI18nControl ic = NutriI18nControl.getInstance();

    public boolean debug = false;
    String text_idx;
    DataAccess m_da = DataAccess.getInstance();

    long id;
    String name;
    //String amount;
    String weight;
    
    public static final int AMOUNT_WEIGHT = 1;
    public static final int AMOUNT_HOME_WEIGHT = 2;
    public static final int AMOUNT_AMOUNT = 3;
    
    
    // 1=usda food, 2=user food, 3=meal
    public int nutrition_food_type = 0;
    public long nutrition_food_id = 0L;
    private long home_weight_id = 0L;
    public int amount_type = 0;
    public float amount = 0.0f;
    public String[] amount_type_str = { "", "W", "HW", "A"};
    
    
    public FoodDescription m_food = null;
    public Meal m_meal = null;
    
    public HomeWeightSpecial m_home_weight_special = null;
    
    public static final int WEIGHT_TYPE_WEIGHT = 1;
    public static final int WEIGHT_TYPE_HOME_WEIGHT = 2;
    public static final int WEIGHT_TYPE_AMOUNT = 3;
    
    private float calculated_multiplier = 0.0f;

    private ArrayList<MealNutrition> nutritions = null; 
    
    
    
    // food + weight
    public DailyFoodEntry(/*int food_type,*/ FoodDescription fd, float weight)  
    {
	
	//this(food_type, food_id, weight_type, 0L, amount);
	this(fd.getFoodType(), fd, null, DailyFoodEntry.AMOUNT_WEIGHT, null, weight);
    }
    
    // food + home weight
    public DailyFoodEntry(/*int food_type,*/ FoodDescription fd, HomeWeightSpecial hw, float amount)  
    {
	//this(food_type, food_id, weight_type, 0L, amount);
	this(fd.getFoodType(), fd, null, DailyFoodEntry.AMOUNT_HOME_WEIGHT, hw, amount);
    }
    

    // meal + amount
    public DailyFoodEntry(/*int food_type,*/ Meal ml, float amount)  
    {
	//this(food_type, food_id, weight_type, 0L, amount);
	this(3, null, ml, DailyFoodEntry.AMOUNT_AMOUNT, null, amount);
    }
    
    /*
    public DailyFoodEntryValues(int food_type, long food_id, int weight_type, float amount)
    {
	this(food_type, food_id, weight_type, 0L, amount);
    }
    
    
    public DailyFoodEntryValues(int food_type, long food_id, int weight_type, long hw_id, float amount)
    {
	    this.nutrition_food_type = 0;
	    this.nutrition_food_id = 0L;
	    this.home_weight_id = hw_id;
	    this.amount_type = weight_type;
	    this.amount = amount;
    }*/

    
    public DailyFoodEntry(int food_type, FoodDescription fd, Meal ml, int amount_type, HomeWeightSpecial hws, float amount)
    {
	this.nutrition_food_type = food_type;
	
	if (fd==null)
	{
	    this.nutrition_food_id = ml.getId();
	    this.m_meal = ml;
	}
	else
	{
	    this.nutrition_food_id = fd.getId();
	    this.m_food = fd;
	}

	if (hws!=null)
	{
	    this.m_home_weight_special = hws;
	    this.home_weight_id = hws.id;
	}
	else
	{
	    this.home_weight_id = 0;
	}
	
	this.amount_type = amount_type; 
	this.amount = amount;
	
    }

    
    
    
    public DailyFoodEntry(String val)
    {
	String v1[] = m_da.splitString(val, "=");
	
	String foods[] = m_da.splitString(v1[0], ":");
	
	this.nutrition_food_type = m_da.getIntValueFromString(foods[0]);
	this.nutrition_food_id = m_da.getLongValueFromString(foods[1]);
	
	String ids[] = m_da.splitString(v1[1], "%");
	
	
	// FIX
	if (ids.length==2)
	{
	    this.amount_type = this.getAmountType(ids[0]);
	    this.amount = m_da.getFloatValueFromString(ids[1]);
	    this.home_weight_id = -1;
	}
	else
	{
	    this.amount_type = this.getAmountType(ids[0]);
	    this.home_weight_id = m_da.getLongValueFromString(ids[1]);
	    this.amount = m_da.getFloatValueFromString(ids[2]);
	}
	
	loadObjects();
	//calculateMultiplier();
	
    }
    

    public void loadObjects()
    {
	//new GGCTreeRoot(1);
	
	
	if (this.nutrition_food_type==GGCTreeRoot.TREE_MEALS)
	{
	    this.m_meal = m_da.tree_roots.get("3").m_meals_ht.get("" + this.nutrition_food_id);
	}
	else
	{
	    this.m_food = m_da.tree_roots.get("" + this.nutrition_food_type).m_foods_ht.get("" + this.nutrition_food_id);

	    if (this.amount_type==DailyFoodEntry.AMOUNT_HOME_WEIGHT)
	    {
		loadHomeWeight();
		
	    }
	}

    }
    
    public void resetWeightValues(DailyFoodEntry dfe)
    {
	this.amount_type = dfe.amount_type;
	this.amount = dfe.amount;
	this.m_home_weight_special = dfe.m_home_weight_special;
	this.home_weight_id = dfe.home_weight_id;
	
	this.calculated_multiplier = 0.0f;
    }
    
    
    public String getName()
    {
	if (this.m_food!=null)
	{
	    return this.m_food.getName();
	}
	else
	{
	    return this.m_meal.getName();
	}
    }
    
    public int getFoodType()
    {
	if (this.m_food!=null)
	{
	    return this.m_food.getFoodType();
	}
	else
	{
	    return 3; //this.m_meal.getName();
	}
    }

    
    public int getWeightType()
    {
	return this.amount_type;
    }
    
    
    public String getWeightTypeString()
    {
	return this.amount_type_str[this.amount_type];
    }
    
    public String getHomeWeightDescription()
    {
	if (this.amount_type==DailyFoodEntry.WEIGHT_TYPE_HOME_WEIGHT)
	{
	    return this.m_home_weight_special.getName();
	}
	else
	    return "";
    }
    
    
    public String getAmountString()
    {
	return DataAccess.Decimal2Format.format(this.amount);
    }
    
    private void loadHomeWeight()
    {
	StringTokenizer strtok = new StringTokenizer(this.m_food.getHome_weights(), ";");
	
	while(strtok.hasMoreTokens())
	{
	    String tk = strtok.nextToken();
	    
	    if (tk.startsWith(this.home_weight_id + "="))
	    {
		this.m_home_weight_special = new HomeWeightSpecial(tk);
	    }
	    
	}
	
    }
    
    
    private void calculateMultiplier()
    {
	if (this.amount_type==DailyFoodEntry.WEIGHT_TYPE_WEIGHT)
	{
	    this.calculated_multiplier = this.amount/100.0f;
	}
	else if (this.amount_type==DailyFoodEntry.WEIGHT_TYPE_HOME_WEIGHT)
	{
	    this.calculated_multiplier = this.m_home_weight_special.getCalculatedWeight() /100.0f;
	}
	else
	{
	    this.calculated_multiplier = this.amount;
	}
    }
    
    public float getMultiplier()
    {
	if (this.calculated_multiplier == 0.0f)
	    this.calculateMultiplier();
	
	return this.calculated_multiplier;
    }
    
    
    public String getShortDescription()
    {
        //return this.getDescription();
	return this.name + " [" + this.amount +" x " + this.weight + " g]"; 
    }

    
    private int getAmountType(String type)
    {
	for(int i=1; i<this.amount_type_str.length; i++ )
	{
	    if (this.amount_type_str[i].equals(type))
		return i;
	}
	
	return -1;
    }
    

    @Override
    public String toString()
    {
        //return this.getShortDescription();
	return getShortDescription();
    }
/*
    public String getName()
    {
	return this.name;
    }
  */  
    
    public String getValueString()
    {
	StringBuffer sb = new StringBuffer();
	
	sb.append(this.nutrition_food_type);
	sb.append(":");
	sb.append(this.nutrition_food_id); 
	sb.append("=");
	sb.append(this.amount_type_str[this.amount_type]);
	sb.append("%");
	
	if (this.amount_type==DailyFoodEntry.AMOUNT_HOME_WEIGHT)
	{
	    sb.append(this.home_weight_id);
	    sb.append("%");
	    sb.append(this.amount);
	}
	else
	{
	    sb.append(this.amount);
	}
	
	return sb.toString();
	
    }
    

    private void loadNutritions()
    {
	String nutr;
	
	if ((this.nutrition_food_type == GGCTreeRoot.TREE_USDA_NUTRITION) ||
            (this.nutrition_food_type == GGCTreeRoot.TREE_USER_NUTRITION))
	{
	    nutr = this.m_food.getNutritions();
	}
	else
	{
	    nutr = this.m_meal.getNutritions();
	}
	
	this.nutritions = new ArrayList<MealNutrition>();
	
	StringTokenizer strtok = new StringTokenizer(nutr, ";");
	
	while(strtok.hasMoreTokens())
	{
	    MealNutrition mn = new MealNutrition(strtok.nextToken(), true);
	    this.nutritions.add(mn);
	}
	
    }
    
    
    public ArrayList<MealNutrition> getNutritions()
    {
	if (this.nutritions == null)
	    loadNutritions();
	
	return this.nutritions;
    }
    
    

    /**
     * getObjectName - returns name of DatabaseObject
     * 
     * @return name of object (not Hibernate object)
     */
    public String getObjectName()
    {
        return "Home Weight Type";
    }



    /**
     * isDebugMode - returns debug mode of object
     * 
     * @return true if object in debug mode
     */
    public boolean isDebugMode()
    {
        return debug;
    }



    /**
     * getAction - returns action that should be done on object
     *    0 = no action
     *    1 = add action
     *    2 = edit action
     *    3 = delete action
     *    This is used mainly for objects, contained in lists and dialogs, used for 
     *    processing by higher classes (classes calling selectors, wizards, etc...
     * 
     * @return number of action
     */
    public int getAction()
    {
        return 0;
    }


    //---
    //---  SelectorInterface
    //---
    


    /* 
     * getColumnCount
     */
    public int getColumnCount()
    {
	return 4;
    }


    /* 
     * getColumnName
     */
    public String getColumnName(int num)
    {
	switch(num)
	{
	    case 4:
		return ic.getMessage("WGHT_PER_AMOUNT");
		
	    case 3:
		return ic.getMessage("AMOUNT_LBL");
		
	    case 2:
		return ic.getMessage("NAME");

	    default:
		return ic.getMessage("ID");
		
	}
	/*
	switch(num)
	{
	    case 4:
		return ic.getMessage("TRANSLATED");
		
	    case 3:
		return ic.getMessage("USER_DEFINED");
		
	    case 2:
		return ic.getMessage("NAME");

	    default:
		return ic.getMessage("ID");
		
	}*/
	
    }


    /* 
     * getColumnValue
     */
    public String getColumnValue(int num)
    {
	switch(num)
	{
	    case 4:
		return this.weight;
	    
	    case 3:
		return "" + this.amount;
		
	    case 2:
		return this.name;

	    default:
		return "" + this.id;
		
	} 
	
	/*
	switch(num)
	{
	    
	    
	    case 4:
		return ic.getPartitialTranslation(this.getName(), "_");
	    
	    case 3:
		return getYesNo(this.getStatic_entry());
		
	    case 2:
		return this.getName();

	    default:
		return "" + this.getItemId();
		
	}*/
	
    }


    /* 
     * getColumnValueObject
     */
    public Object getColumnValueObject(int num)
    {
	switch(num)
	{
	    case 4:
		return this.weight;
	    
	    case 3:
		return this.amount;
		
	    case 2:
		return this.name;

	    default:
		return new Long(this.id);
		
	}
    }


    /* 
     * getColumnWidth
     */
    public int getColumnWidth(int num, int width)
    {
	
	switch(num)
	{
            case 4:
                return(int)(width*20);
            case 3:
                return(int)(width*15);
            case 2:
                return(int)(width*50);
            default:
                return(int)(width*15);
		
	} 
	
	/*
	
	switch(num)
	{
            case 4:
                return(int)(width*40);
            case 3:
                return(int)(width*10);
            case 2:
                return(int)(width*40);
            default:
                return(int)(width*10);
		
	} */
	
	
    }


    /* 
     * getItemId
     */
    public long getItemId()
    {
	return this.id;
    }


    /* 
     * isFound
     */
    public boolean isFound(int from, int till, int state)
    {
	return true;
    }


    /* 
     * isFound
     */
    public boolean isFound(int value)
    {
	return true;
    }


    /* 
     * isFound
     */
    public boolean isFound(String text)
    {
        if ((this.text_idx.indexOf(text.toUpperCase())!=-1) || (text.length()==0))
            return true;
        else
            return false;
    }



    /* 
     * setSearchContext
     */
    public void setSearchContext()
    {
	text_idx = this.name.toUpperCase();
    }
    
    
    //---
    //---  Column sorting 
    //---


    private ColumnSorter columnSorter = null;


    /**
     * setColumnSorter - sets class that will help with column sorting
     * 
     * @param cs ColumnSorter instance
     */
    public void setColumnSorter(ColumnSorter cs)
    {
	this.columnSorter = cs;
    }


    /**
     * Compares this object with the specified object for order.  Returns a
     * negative integer, zero, or a positive integer as this object is less
     * than, equal to, or greater than the specified object.
     *
     * <p>The implementor must ensure <tt>sgn(x.compareTo(y)) ==
     * -sgn(y.compareTo(x))</tt> for all <tt>x</tt> and <tt>y</tt>.  (This
     * implies that <tt>x.compareTo(y)</tt> must throw an exception iff
     * <tt>y.compareTo(x)</tt> throws an exception.)
     *
     * <p>The implementor must also ensure that the relation is transitive:
     * <tt>(x.compareTo(y)&gt;0 &amp;&amp; y.compareTo(z)&gt;0)</tt> implies
     * <tt>x.compareTo(z)&gt;0</tt>.
     *
     * <p>Finally, the implementor must ensure that <tt>x.compareTo(y)==0</tt>
     * implies that <tt>sgn(x.compareTo(z)) == sgn(y.compareTo(z))</tt>, for
     * all <tt>z</tt>.
     *
     * <p>It is strongly recommended, but <i>not</i> strictly required that
     * <tt>(x.compareTo(y)==0) == (x.equals(y))</tt>.  Generally speaking, any
     * class that implements the <tt>Comparable</tt> interface and violates
     * this condition should clearly indicate this fact.  The recommended
     * language is "Note: this class has a natural ordering that is
     * inconsistent with equals."
     *
     * <p>In the foregoing description, the notation
     * <tt>sgn(</tt><i>expression</i><tt>)</tt> designates the mathematical
     * <i>signum</i> function, which is defined to return one of <tt>-1</tt>,
     * <tt>0</tt>, or <tt>1</tt> according to whether the value of
     * <i>expression</i> is negative, zero or positive.
     *
     * @param   o the object to be compared.
     * @return  a negative integer, zero, or a positive integer as this object
     *		is less than, equal to, or greater than the specified object.
     *
     * @throws ClassCastException if the specified object's type prevents it
     *         from being compared to this object.
     */
    public int compareTo(Object o)
    {

	if (o instanceof SelectableInterface)
	{
	    return this.columnSorter.compareObjects(this, (SelectableInterface)o);
	}
	else
	    throw new ClassCastException();

    }
    
    
}


