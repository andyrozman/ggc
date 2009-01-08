package ggc.core.db.datalayer;

import ggc.core.nutrition.GGCTreeRoot;
import ggc.core.util.DataAccess;
import ggc.core.util.NutriI18nControl;

import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.StringTokenizer;

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
 *  Filename:     DailyFoodEntry  
 *  Description:  Daily Food Entry, main object for working with Nutritions
 * 
 *  Author: andyrozman {andy@atech-software.com}  
 */


public class DailyFoodEntry // implements SelectableInterface
{

    NutriI18nControl ic = NutriI18nControl.getInstance();

    private boolean debug = false;
    String text_idx;
    DataAccess m_da = DataAccess.getInstance();
    GlycemicNutrients glyc_nutr = null;

    boolean root_entry = false;

    long id;
    String name;
    // String amount;
    String weight;

    // public static final int AMOUNT_WEIGHT = 1;
    // public static final int AMOUNT_HOME_WEIGHT = 2;
    // public static final int AMOUNT_AMOUNT = 3;

    // 1=usda food, 2=user food, 3=meal
    private int nutrition_food_type = 0;
    private long nutrition_food_id = 0L;
    private long home_weight_id = 0L;
    private int amount_type = 0;
    private float amount = 0.0f;
    private String[] amount_type_str = { "", "W", "HW", "A" };

    private FoodDescription m_food = null;
    private Meal m_meal = null;

    private HomeWeightSpecial m_home_weight_special = null;

    /**
     * Weight Type: Weight
     */
    public static final int WEIGHT_TYPE_WEIGHT = 1;

    /**
     * Weight Type: Home Weight
     */
    public static final int WEIGHT_TYPE_HOME_WEIGHT = 2;

    /**
     * Weight Type: Amount
     */
    public static final int WEIGHT_TYPE_AMOUNT = 3;

    private float calculated_multiplier = 0.0f;

    private ArrayList<MealNutrition> nutrients = null;

    private Hashtable<String, String> multiplier;
    private String component_id;

    ArrayList<DailyFoodEntry> children = null;


    /**
     * Constructor (food + weight)
     * 
     * @param fd
     * @param weight
     */
    public DailyFoodEntry(/* int food_type, */FoodDescription fd, float weight)
    {
        this(fd.getFoodType(), fd, null, DailyFoodEntry.WEIGHT_TYPE_WEIGHT, null, weight);
    }

    /**
     * Constructor (food + home weight)
     * 
     * @param fd
     * @param hw
     * @param amount
     */
    public DailyFoodEntry(/* int food_type, */FoodDescription fd, HomeWeightSpecial hw, float amount)
    {
        this(fd.getFoodType(), fd, null, DailyFoodEntry.WEIGHT_TYPE_HOME_WEIGHT, hw, amount);
    }

    /**
     * Constructor (meal + amount)
     * 
     * @param ml
     * @param amount
     */
    public DailyFoodEntry(/* int food_type, */Meal ml, float amount)
    {
        this(3, null, ml, DailyFoodEntry.WEIGHT_TYPE_AMOUNT, null, amount);
    }

    /**
     * Constructor (many parameters) - Main
     * 
     * @param food_type
     * @param fd
     * @param ml
     * @param amount_type
     * @param hws
     * @param amount
     */
    public DailyFoodEntry(int food_type, FoodDescription fd, Meal ml, int amount_type, HomeWeightSpecial hws,
            float amount)
    {

//        System.out.println("food_type=" + food_type + ",food=" + fd + ",meal=" + ml + ",amount_type=" + amount_type
//                + ",home weight sp: " + hws + ",amount:" + amount);

        this.nutrition_food_type = food_type;

        if (fd == null)
        {
            this.nutrition_food_id = ml.getId();
            this.m_meal = ml;
        }
        else
        {
            this.nutrition_food_id = fd.getId();
            this.m_food = fd;
        }

        if (hws != null)
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

        init();

    }

    /**
     * Daily Food Entry (String)
     * @param val
     */
    public DailyFoodEntry(String val)
    {
        String v1[] = m_da.splitString(val, "=");

        String foods[] = m_da.splitString(v1[0], ":");

        this.nutrition_food_type = m_da.getIntValueFromString(foods[0]);
        this.nutrition_food_id = m_da.getLongValueFromString(foods[1]);

        String ids[] = m_da.splitString(v1[1], "%");

        // FIX
        if (ids.length == 2)
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

        init();

    }

    /**
     * Constructor (String, boolean)
     * 
     * @param val
     * @param direct
     */
    public DailyFoodEntry(String val, boolean direct)
    {
        // 2:103=30.0 ;2:215=100.0

        String v1[] = m_da.splitString(val, "=");

        String foods[] = m_da.splitString(v1[0], ":");

        this.nutrition_food_type = m_da.getIntValueFromString(foods[0]);
        this.nutrition_food_id = m_da.getLongValueFromString(foods[1]);

        this.amount_type = DailyFoodEntry.WEIGHT_TYPE_WEIGHT; // this.
        // getAmountType
        // (ids[0]);
        this.amount = m_da.getFloatValueFromString(v1[1]);
        this.home_weight_id = -1;

        init();
        loadNutrients();
    }

    /**
     * Constructor (String, int)
     * 
     * @param val
     * @param type
     */
    public DailyFoodEntry(String val, int type)
    {
        // 1:122=1.0

        String v1[] = m_da.splitString(val, "=");

        String foods[] = m_da.splitString(v1[0], ":");

        this.nutrition_food_type = m_da.getIntValueFromString(foods[0]);
        this.nutrition_food_id = m_da.getLongValueFromString(foods[1]);

        this.amount_type = DailyFoodEntry.WEIGHT_TYPE_AMOUNT; // this.
        // getAmountType
        // (ids[0]);
        this.amount = m_da.getFloatValueFromString(v1[1]);
        this.home_weight_id = -1;

        init();
        loadNutrients();

        /*
         * StringTokenizer strtok = new StringTokenizer(meal_str, "=");
         * 
         * String meal_id_in = strtok.nextToken(); String amount_in =
         * strtok.nextToken().replace(',', '.');
         * 
         * StringTokenizer strtok2 = new StringTokenizer(meal_id_in, ":");
         * 
         * String type_in = strtok2.nextToken(); String type_id_in =
         * strtok2.nextToken();
         * 
         * this.meal_type = Integer.parseInt(type_in);
         * 
         * loadMealPart(this.meal_type, type_id_in);
         * 
         * this.amount = Float.parseFloat(amount_in);
         */
    }

    /*
     * public DailyFoodEntry(MealPart mpart) { if mpart.getType()== }
     */

    private void init()
    {
        createId();
        loadObjects();
    }

    private void loadObjects()
    {
        // new GGCTreeRoot(1);

        if (this.nutrition_food_type == GGCTreeRoot.TREE_MEALS)
        {
            this.m_meal = m_da.tree_roots.get("3").m_meals_ht.get("" + this.nutrition_food_id);
            processMeal(this.m_meal);
        }
        else
        {
            this.m_food = m_da.tree_roots.get("" + this.nutrition_food_type).m_foods_ht
                    .get("" + this.nutrition_food_id);

            if (this.amount_type == DailyFoodEntry.WEIGHT_TYPE_HOME_WEIGHT)
            {
                loadHomeWeight();

            }
        }

    }

    /**
     * Add Child
     * 
     * @param dfe
     */
    public void addChild(DailyFoodEntry dfe)
    {
        if (this.children == null)
        {
            this.children = new ArrayList<DailyFoodEntry>();
        }

        dfe.addMultiplier(component_id, this.getMultiplier());

        this.children.add(dfe);

    }

    /**
     * Has Children
     * 
     * @return
     */
    public boolean hasChildren()
    {
        if ((children == null) || (children.size() == 0))
            return false;
        else
            return true;
    }

    private void createId()
    {
        this.component_id = m_da.getNewComponentId();
    }

    /**
     * Add Multiplier
     * 
     * @param id
     * @param multiplier
     */
    public void addMultiplier(String id, float multiplier)
    {
        if (this.multiplier == null)
        {
            this.multiplier = new Hashtable<String, String>();
        }

        if (!this.multiplier.containsKey(id))
        {
            this.multiplier.put(id, "" + multiplier);
        }
    }

    /**
     * Add Multipliers
     * 
     * @param multipliers
     */
    public void addMultipliers(Hashtable<String, String> multipliers)
    {
        if (this.multiplier == null)
        {
            this.multiplier = new Hashtable<String, String>();
        }

        for (Enumeration<String> en = multipliers.keys(); en.hasMoreElements();)
        {
            String key = en.nextElement();

            if (!this.multiplier.containsKey(key))
            {
                this.multiplier.put(key, multipliers.get(key));
            }
        }
    }


    /**
     * Get Name
     * 
     * @return
     */
    public String getName()
    {
        if (this.m_food != null)
        {
            return this.m_food.getName();
        }
        else if (this.m_meal!=null)
        {
            return this.m_meal.getName();
        }
        else
            return "";
    }

    
    /**
     * Get Nutrition Food Type
     * 
     * @return
     */
    public int getNutritionFoodType()
    {
        return this.nutrition_food_type;
    }
    
    
    /**
     * Get Food Type
     * 
     * @return
     */
    public int getFoodType()
    {
        if (this.m_food != null)
        {
            return this.m_food.getFoodType();
        }
        else
        {
            return 3; // this.m_meal.getName();
        }
    }

    /**
     * Get Weight Type
     * 
     * @return
     */
    public int getWeightType()
    {
        return this.amount_type;
    }

    /**
     * Get Weight Type String
     * 
     * @return
     */
    public String getWeightTypeString()
    {
        return this.amount_type_str[this.amount_type];
    }

    /**
     * Get Home Weight Description
     * 
     * @return
     */
    public String getHomeWeightDescription()
    {
        
        if (this.amount_type == DailyFoodEntry.WEIGHT_TYPE_HOME_WEIGHT)
        {
            //System.out.println("Weight type HW");
            //System.out.println("HW Special: " + this.m_home_weight_special);
            //System.out.println("HW Special Name: " + this.m_home_weight_special.getName());
            return this.m_home_weight_special.getName();
        }
        else
        {
            //System.out.println("Weight type NO HW");

            return "";
        }
    }

    /**
     * Get Amount String
     * 
     * @return
     */
    public String getAmountString()
    {
        return DataAccess.Decimal2Format.format(this.amount);
    }

    /**
     * Get Amount Single Decimal String
     * 
     * @return
     */
    public String getAmountSingleDecimalString()
    {
        return DataAccess.MmolDecimalFormat.format(this.amount);
    }
    
    
    private void loadHomeWeight()
    {
        //System.out.println("HWs: " + this.m_food.getHome_weights());
        //System.out.println("Looking for:  " + this.home_weight_id);
        
        
        if ((this.m_food.getHome_weights()==null) && (this.m_food.getHome_weights().length()==0))
            return;
        
        StringTokenizer strtok = new StringTokenizer(this.m_food.getHome_weights(), ";");

        while (strtok.hasMoreTokens())
        {
            String tk = strtok.nextToken();

            if (tk.startsWith(this.home_weight_id + "="))
            {
                //System.out.println("FOUND !!!!");
                this.m_home_weight_special = new HomeWeightSpecial(tk);
            }

        }

    }

    
    /**
     * Get Meal CHs
     * 
     * @return
     */
    public float getMealCH()
    {
        float sum = 0.0f;
        //float mult = this
        
        
        if (!hasChildren())
            return sum;
        
        //System.out.println("Meal: " + this.m_meal.getName()+ " children: " + this.children.size());
        
        for(int i=0; i< this.children.size(); i++)
        {
            DailyFoodEntry dfe = this.children.get(i);
            sum += dfe.getNutrientValue(205) * dfe.getMultiplier();
            
            //System.out.println("dfe:" + dfe.amount + ", mult: " + dfe.getMultiplier() + "nut val: " + dfe.getNutrientValue(205));
        }
        //String val = this.m_meal.getNutritions();
        
        //System.out.println(sum);
        
        return sum;
    }
    
    
    private void calculateMultiplier()
    {
        // System.out.println("calculateMultiplier");
        // System.out.println("amount: " + this.amount);

        if (this.amount_type == DailyFoodEntry.WEIGHT_TYPE_WEIGHT)
        {
            this.calculated_multiplier = this.amount / 100.0f;
            this.addMultiplier(this.component_id, this.amount * 0.01f);
        }
        else if (this.amount_type == DailyFoodEntry.WEIGHT_TYPE_HOME_WEIGHT)
        {
            this.calculated_multiplier = (this.m_home_weight_special.getCalculatedWeight() / 100.0f) * amount;
            this.addMultiplier(this.component_id, (this.m_home_weight_special.getCalculatedWeight() * 0.01f * amount));
        }
        else
        {
            this.calculated_multiplier = this.amount;
            this.addMultiplier(this.component_id, 1.0f);

            // XXX: This should be like that, probably... ??!!
            // this.calculated_multiplier = this.amount/100.0f;
        }
    }

    private float getMultiplier()
    {
        if (this.calculated_multiplier == 0.0f)
            this.calculateMultiplier();

        return this.calculated_multiplier;
    }

    private Hashtable<String, String> getMultipliers()
    {
        return this.multiplier;
    }


    /**
     * Get Children
     * 
     * @return
     */
    public ArrayList<DailyFoodEntry> getChildren()
    {
        for (int i = 0; i < this.children.size(); i++)
        {
            this.children.get(i).addMultipliers(this.getMultipliers());
        }

        return this.children;
    }

    /**
     * Get Short Description
     * 
     * @return
     */
    public String getShortDescription()
    {
        return this.getName() + " [" + this.getAmountString() + " x " + this.weight + " g]";
    }

    private int getAmountType(String type)
    {
        for (int i = 1; i < this.amount_type_str.length; i++)
        {
            if (this.amount_type_str[i].equals(type))
                return i;
        }

        return -1;
    }

    
    /**
     * Get Amount Type
     * 
     * @return
     */
    public int getAmountType()
    {
        return this.amount_type;
    }
    
    /**
     * Get Amount
     * 
     * @return
     */
    public float getAmount()
    {
        return this.amount;
    }
    
    /**
     * Get Food Object
     * @return
     */
    public FoodDescription getFoodObject()
    {
        return this.m_food;
    }
    
    /**
     * Get Meal Object
     * 
     * @return
     */
    public Meal getMealObject()
    {
        return this.m_meal;
    }

    
    /**
     * Get Home Weight Special Object
     * 
     * @return
     */
    public HomeWeightSpecial getHomeWeightSpecialObject()
    {
        return this.m_home_weight_special;
    }
    
    
    // TODO: New code
    
    
    
    /**
     * Display Nutritions
     * 
     * @param nuts
     */
    public void displayNutritions(ArrayList<MealNutrition> nuts)
    {
        for (int i = 0; i < nuts.size(); i++)
        {
            System.out.print(nuts.get(i));
        }

    }

    /**
     * Display Nutritions
     */
    public void displayNutritions()
    {
        // ArrayList<MealNutrition> nuts = this.getNutrientsCalculated(); //
        // proc v1

        ArrayList<MealNutrition> nuts = this.getNutrients();

        for (int i = 0; i < nuts.size(); i++)
        {
            System.out.print(nuts.get(i));
        }

    }

    /**
     * To String
     * 
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString()
    {
        // return this.getShortDescription();
        return getShortDescription();
    }


    /**
     * Get Value String
     * 
     * @return
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

        if (this.amount_type == DailyFoodEntry.WEIGHT_TYPE_HOME_WEIGHT)
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

    private void loadNutrients()
    {
        String nutr;
        this.nutrients = new ArrayList<MealNutrition>();

        if ((this.nutrition_food_type == GGCTreeRoot.TREE_USDA_NUTRITION)
                || (this.nutrition_food_type == GGCTreeRoot.TREE_USER_NUTRITION))
        {
            nutr = this.m_food.getNutritions();
            processNutrients(nutr, 1.0f);
        }
        else if (this.m_meal!=null)
        {
            nutr = this.m_meal.getNutritions();
//            processMeal(this.m_meal);
        }
/*        else
        {
            nutr = this.m_meal.getNutritions();
            processMeal(this.m_meal);
        }
*/
        /*
         * StringTokenizer strtok = new StringTokenizer(nutr, ";");
         * 
         * while(strtok.hasMoreTokens()) { MealNutrition mn = new
         * MealNutrition(strtok.nextToken(), true); this.nutritions.add(mn); }
         */
    }

    private void processMeal(Meal meal)
    {
        if (meal==null)
            return;
        
        String ml_parts = meal.getParts();

        String parts[] = m_da.splitString(ml_parts, ";");
        // System.out.println("Meal Parts: " + ml_parts + ",multiplier=" +
        // this.getMultiplier());

        for (int i = 0; i < parts.length; i++)
        {
            DailyFoodEntry dfe = new DailyFoodEntry(parts[i], true);
            // dfe.addMultiplier(component_id, (this.getMultiplier()));
            this.addChild(dfe);

            /*
             * commented out by version 2 of processing
             * //ArrayList<MealNutrition> mn_list = dfe.getNutrients();
             * //Calculated(); //System.out.println("Normal: " + i);
             * //this.displayNutritions(mn_list);
             * 
             * //System.out.println("Calculated: " + i);
             * ArrayList<MealNutrition> mn_list2 =
             * dfe.getNutrientsCalculated(this.getMultiplier()); //Calculated();
             * this.displayNutritions(mn_list2);
             * 
             * 
             * System.out.println("Merge: " + i); mergeNutrientsData(mn_list2);
             * this.displayNutritions();
             */

        }
    }

    /*
     * public void mergeNutrientsData(ArrayList<MealNutrition> mn_list) {
     * //this.nutrients = new ArrayList<MealNutrition>();
     * 
     * out(
     * "!!!!!!!!!!!!!!!!!!!!!!!  MERGEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEE !!!!!!!!!!!!"
     * );
     * 
     * 
     * float mul = getMultiplier();
     * 
     * for(int i=0; i<mn_list.size(); i++) { MealNutrition mn_target =
     * mn_list.get(i);
     * 
     * MealNutrition mn = findNutrient(mn_target.getId());
     * 
     * if (mn==null) { //mn_target.getAmount() // XXX: calc was normal /if
     * (this.root_entry) // ! { System.out.println("Root traget[nw]: \n" +
     * mn_target + " mult: " + mul);
     * //mn_target.addToCalculatedAmount(mn_target.getCalculatedAmount() mul);
     * mn_target.setAmount(mn_target.getCalculatedAmount()); }
     */

    // XXX: outise root
    /*
     * mn_target.setAmount(mn_target.getCalculatedAmount());
     * 
     * this.nutrients.add(mn_target); } else {
     * System.out.println("Root traget: \n" + mn_target + " mult: " + mul); //
     * XXX: calc was normal
     * mn.addToCalculatedAmount(mn_target.getCalculatedAmount() mul);
     * //mn.addToCalculatedAmount(mn_target.getCalculatedAmount()); // XXX: test
     * 1 mn.setAmount(mn.getCalculatedAmount());
     * //mn_target.setAmount(mn_target.getCalculatedAmount());
     * 
     * } } }
     */

    /*  v3.1
    private void mergeGlycemicData(DailyFoodEntry dfe)
    {
        if (dfe.containsGlycemicNutrients())
        {
            if (this.containsGlycemicNutrients())
            {
                // process
            }
            else
            {
                this.glyc_nutr = dfe.getGlycemicNutrients();
            }
        }

    }
*/
    /*
     * private MealNutrition findNutrient(int id) { for(int i=0;
     * i<this.nutrients.size(); i++) { if (this.nutrients.get(i).getId()==id) {
     * return this.nutrients.get(i); } }
     * 
     * return null;
     * 
     * }
     */

    private void processNutrients(String nutr, float value)
    {
        StringTokenizer strtok = new StringTokenizer(nutr, ";");

        // System.out.println("processNutrients::Value: " + value);

        while (strtok.hasMoreTokens())
        {
            MealNutrition mn = new MealNutrition(strtok.nextToken(), true);

            if (mn.isGlycemicNutrient())
            {
                this.addGlycemicNutrient(mn);
            }
            else
            {
                /*
                 * proc v1 //mn.addToCalculatedAmount(mn.getAmount()
                 * this.getMultiplier());
                 * mn.addToCalculatedAmount(mn.getAmount());
                 * //System.out.println(mn);
                 */
                this.nutrients.add(mn);
            }
        }
    }

    /**
     * Get Nutrients
     * 
     * @return
     */
    public ArrayList<MealNutrition> getNutrients()
    {
        if (this.nutrients == null)
            loadNutrients();

        // ArrayList<MealNutrition> temp_lst = new ArrayList<MealNutrition>();
        Hashtable<String, MealNutrition> table = new Hashtable<String, MealNutrition>();
        // force creation of calculated multiplier
        this.getMultiplier();

        // System.out.println(this.getMultipliers());

        for (int i = 0; i < this.nutrients.size(); i++)
        {
            MealNutrition mn = this.nutrients.get(i);
            mn.addMultipliers(this.getMultipliers());
            table.put("" + mn.getId(), mn);
        }
        /*
         * if (this.hasChildren()) { System.out.println("Ch: " + this.children);
         * for(int i=0; i<this.children.size(); i++) { ArrayList<MealNutrition>
         * nutrs = this.children.get(i).getNutrients();
         * 
         * 
         * System.out.println("Child: " + this.children.get(i) + "\n" + nutrs);
         * 
         * 
         * for(int j=0; j<nutrs.size(); j++) { MealNutrition mn = nutrs.get(j);
         * //mn.addMultipliers(this.getMultipliers());
         * 
         * if (table.containsKey("" + mn.getId())) { table.get("" +
         * mn.getId()).addAmountToSum(mn.getCalculatedAmount()); } else {
         * mn.addAmountToSum(mn.getCalculatedAmount()); table.put("" +
         * mn.getId(), mn); } }
         * 
         * }
         * 
         * return this.createList(table);
         */
        // }
        // else
        return this.nutrients;
    }

    
    /**
     * Get Nutrient Value
     * 
     * @param id
     * @return
     */
    public float getNutrientValue(int id)
    {
        ArrayList<MealNutrition> lst = this.getNutrients();
        
        for(int i=0; i<lst.size(); i++)
        {
            if (lst.get(i).getId()==id)
                return lst.get(i).getAmount();
                //return DataAccess.Decimal2Format.format(lst.get(i).getAmount());
        }
        
        return 0.0f; 
    }
    
    
    
    @SuppressWarnings("unused")
    private ArrayList<MealNutrition> createList(Hashtable<String, MealNutrition> table)
    {
        ArrayList<MealNutrition> lst = new ArrayList<MealNutrition>();

        // System.out.println(
        // "++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++");

        for (Enumeration<String> en = table.keys(); en.hasMoreElements();)
        {
            MealNutrition mn = table.get(en.nextElement());
            // System.out.println(mn);
            lst.add(mn);
        }

        return lst;
    }

    
    /**
     * Get Home Weight Multiplier
     * 
     * @return
     */
    public float getHomeWeightMultiplier()
    {
        float f = 1.0f;
        
        if (this.m_home_weight_special!=null)
        {
        
            if (this.m_food!=null)
            {   
                String hw = this.m_food.getHome_weights();
                String vv = hw.substring(hw.indexOf(this.m_home_weight_special.getItemId() + "="));
                
                if (vv.contains(";"))
                    vv = vv.substring(vv.indexOf("=")+1, vv.indexOf(";"));
                else
                    vv = vv.substring(vv.indexOf("=")+1);
                    
                
                try
                {
                    f = Float.parseFloat(vv)/100.0f;
                }
                catch(Exception ex)
                {
                    System.out.println("Exception: " + ex);
                }
                
                
            }
        }
        
        return f;
        
    }
    
    
    /*
     * processing v1 public ArrayList<MealNutrition> getNutrients() { if
     * (this.nutrients == null) loadNutrients();
     * 
     * return this.nutrients; }
     */

    /*
     * processing v1 public ArrayList<MealNutrition> getNutrientsCalculated() {
     * return getNutrientsCalculated(1.0f); }
     */

    /*
     * processing v1 public ArrayList<MealNutrition>
     * getNutrientsCalculated(float calc) { if (this.nutrients == null)
     * loadNutritions();
     * 
     * float mult = this.getMultiplier() calc;
     * 
     * System.out.println("getCalculated: " + this.getMultiplier());
     * 
     * for(int i=0; i<this.nutrients.size(); i++) { MealNutrition mn =
     * this.nutrients.get(i); mn.clearCalculated();
     * //mn.setAmount(mn.getAmount() mult);
     * mn.addToCalculatedAmount(mn.getAmount() mult); }
     * 
     * return this.nutrients; }
     */

    /*
     * proc v1 public void calculateNutrition(MealNutrition mn, float mult) {
     * 
     * if ((mn.getId()>=4000) && (mn.getId()<=4005)) { addGlycemicNutrient(mn);
     * } else { MealNutrition mmn = new MealNutrition(mn);
     * mn.setAmount(mn.getAmount() mult); }
     * 
     * }
     */

    private void addGlycemicNutrient(MealNutrition mn)
    {
        if (!containsGlycemicNutrients())
        {
            this.glyc_nutr = new GlycemicNutrients(mn);
        }
        else
        {
            this.glyc_nutr.addNutrient(mn);
        }
    }

    /**
     * Contains Glycemic Nutrients
     * 
     * @return
     */
    public boolean containsGlycemicNutrients()
    {
        return (this.glyc_nutr != null);
    }

    /**
     * Get Glycemic Nutrients
     * 
     * @return
     */
    public GlycemicNutrients getGlycemicNutrients()
    {
        return this.glyc_nutr;
    }

    /**
     * getObjectName - returns name of DatabaseObject
     * 
     * @return name of object (not Hibernate object)
     */
    public String getObjectName()
    {
        return "Daily Food Entry";
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
     * 0 = no action 1 = add action 2 = edit action 3 = delete action 
     * 
     * This is used mainly for objects, contained in lists and dialogs, 
     * used for processing by higher classes (classes calling selectors, 
     * wizards, etc...
     * 
     * @return number of action
     */
    public int getAction()
    {
        return 0;
    }

    /**
     * getItemId
     * 
     * @return 
     */
    public long getItemId()
    {
        return this.id;
    }

}
