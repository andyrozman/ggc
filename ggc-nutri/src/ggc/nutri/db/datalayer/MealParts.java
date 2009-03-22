package ggc.nutri.db.datalayer;

import ggc.core.util.DataAccess;
import ggc.nutri.util.I18nControl;

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
 *  Filename:     MealParts  
 *  Description:  Meal Part Collection
 * 
 *  Author: andyrozman {andy@atech-software.com}  
 */

public class MealParts // implements SelectableInterface
{

    I18nControl ic = I18nControl.getInstance();
    //private boolean debug = false;
    DataAccess m_da = DataAccess.getInstance();
    GlycemicNutrients glyc_nutr = null;
    boolean root_entry = false;
    ArrayList<MealPart> entries = null;

    /**
     * Constructor
     */
    public MealParts()
    {
        entries = new ArrayList<MealPart>();
    }

    
    /**
     * Constructor
     * 
     * @param meals_ids
     */
    public MealParts(String meals_ids)
    {
        StringTokenizer strtok = new StringTokenizer(meals_ids, ";");
        
        while (strtok.hasMoreTokens())
        {
            addMealPart(new MealPart(strtok.nextToken()));
        }
    }
    
    
    /**
     * Add Meal Part
     * 
     * @param dfe
     */
    public void addMealPart(MealPart dfe)
    {
        /*
         * if (dfe.hasChildren()) { System.out.println("hasChildren: true");
         * this.entries.addAll(dfe.getChildren()); } else {
         * System.out.println("hasChildren: false");
         */
        this.entries.add(dfe);
        // }
    }

    /**
     * Get Calculated Nutrients
     * 
     * @return
     */
    public ArrayList<MealNutrition> getCalculatedNutrients()
    {

        //System.out.println("calcultedNutrients ");

        Hashtable<String, MealNutrition> ht = new Hashtable<String, MealNutrition>();

        for (int i = 0; i < this.entries.size(); i++)
        {
            ArrayList<MealNutrition> lst = this.entries.get(i).getNutrients();

            //System.out.println("Lst: " + lst);

            for (int j = 0; j < lst.size(); j++)
            {
                MealNutrition mn = lst.get(j);

                if (!ht.containsKey("" + mn.getId()))
                {
                    mn.clearSum();

                    float cm = mn.getCalculatedAmount();

                    //System.out.println("Calculated amount: " + cm);
                    mn.addAmountToSum(cm);

                    ht.put("" + mn.getId(), mn);
                }
                else
                {
                    ht.get("" + mn.getId()).addAmountToSum(mn.getCalculatedAmount());
                }
            }
        }

        ArrayList<MealNutrition> out_lst = new ArrayList<MealNutrition>();

        for (Enumeration<String> en = ht.keys(); en.hasMoreElements();)
        {
            out_lst.add(ht.get(en.nextElement()));
        }

        return out_lst;
    }

    
    /**
     * Get Elements Count
     * 
     * @return
     */
    public int getElementsCount()
    {
        return this.entries.size();
    }
    
    
    /**
     * Get Element
     * 
     * @param index
     * @return
     */
    public MealPart getElement(int index)
    {
        return this.entries.get(index);
    }
    
    
    /**
     * To String
     * 
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString()
    {
        return "MealParts [count=" + this.entries + "]"; 
    }

}
