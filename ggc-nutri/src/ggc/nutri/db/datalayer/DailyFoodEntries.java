package ggc.nutri.db.datalayer;

import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.StringTokenizer;

import com.atech.i18n.I18nControlAbstract;
import ggc.nutri.util.DataAccessNutri;

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
 *  Filename:     DailyFoodEntries  
 *  Description:  This is collection class which is used for collection DailyFoodEntry 
 *                instances used by calcultator for dailyvalue entry food use.
 * 
 *  Author: andyrozman {andy@atech-software.com}  
 */

public class DailyFoodEntries implements DAOObject // implements
                                                   // SelectableInterface
{

    String text_idx;
    DataAccessNutri m_da = DataAccessNutri.getInstance();
    I18nControlAbstract ic = m_da.getI18nControlInstance();
    GlycemicNutrients glyc_nutr = null;

    boolean root_entry = false;
    boolean print_mode = false;

    ArrayList<DailyFoodEntry> entries = null;


    /**
     * Default constructor
     */
    public DailyFoodEntries()
    {
        entries = new ArrayList<DailyFoodEntry>();
    }


    /**
     * Constructor
     * 
     * @param meals_ids
     */
    public DailyFoodEntries(String meals_ids)
    {
        entries = new ArrayList<DailyFoodEntry>();
        StringTokenizer strtok = new StringTokenizer(meals_ids, ";");

        while (strtok.hasMoreTokens())
        {
            addDailyFoodEntry(new DailyFoodEntry(strtok.nextToken()));
        }
    }


    /**
     * Constructor
     * 
     * @param meals_ids
     * @param print_mode
     */
    public DailyFoodEntries(String meals_ids, boolean print_mode)
    {
        this.print_mode = print_mode;

        entries = new ArrayList<DailyFoodEntry>();
        StringTokenizer strtok = new StringTokenizer(meals_ids, ";");

        while (strtok.hasMoreTokens())
        {
            addDailyFoodEntry(new DailyFoodEntry(strtok.nextToken() /*
                                                                     * ,
                                                                     * print_mode
                                                                     */));
        }
    }


    /**
     * Add Daily Food Entry
     * 
     * @param dfe
     */
    public void addDailyFoodEntry(DailyFoodEntry dfe)
    {
        if (dfe.hasChildren())
        {
            if (this.print_mode)
            {
                this.entries.add(dfe);
            }
            else
            {
                this.entries.addAll(dfe.getChildren());
            }
        }
        else
        {
            this.entries.add(dfe);
        }
    }


    /**
     * Get Calculated Nutrients
     * 
     * @return
     */
    public ArrayList<MealNutrition> getCalculatedNutrients()
    {

        // System.out.println("calcultedNutrients ");

        Hashtable<String, MealNutrition> ht = new Hashtable<String, MealNutrition>();

        // System.out.println("Entries: \n" + this.entries);

        for (int i = 0; i < this.entries.size(); i++)
        {
            ArrayList<MealNutrition> lst = this.entries.get(i).getNutrients();

            // System.out.println("Lst: " + lst);

            for (int j = 0; j < lst.size(); j++)
            {
                MealNutrition mn = lst.get(j);

                if (mn.getId() >= 4000 && mn.getId() <= 4005)
                {
                    // glycemic nutrients

                    // TODO: Implement this

                }
                else
                {
                    // non-glycemic nutrients

                    if (!ht.containsKey("" + mn.getId()))
                    {
                        mn.clearSum();

                        float cm = mn.getCalculatedAmount();

                        // System.out.println("Calculated amount: " + cm);
                        mn.addAmountToSum(cm);

                        ht.put("" + mn.getId(), mn);
                    }
                    else
                    {
                        ht.get("" + mn.getId()).addAmountToSum(mn.getCalculatedAmount());
                    }
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
    public DailyFoodEntry getElement(int index)
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
        return "DailyFoodEntries [count=" + this.entries.size() + "]";
    }

}
