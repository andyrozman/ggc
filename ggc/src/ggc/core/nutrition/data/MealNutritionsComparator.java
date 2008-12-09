package ggc.core.nutrition.data;

import ggc.core.nutrition.display.MealNutritionsDisplay;

import java.util.Comparator;

/**
 * Application: GGC - GNU Gluco Control
 * 
 * See AUTHORS for copyright information.
 * 
 * This program is free software; you can redistribute it and/or modify it under
 * the terms of the GNU General Public License as published by the Free Software
 * Foundation; either version 2 of the License, or (at your option) any later
 * version.
 * 
 * This program is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU General Public License for more
 * details.
 * 
 * You should have received a copy of the GNU General Public License along with
 * this program; if not, write to the Free Software Foundation, Inc., 59 Temple
 * Place, Suite 330, Boston, MA 02111-1307 USA
 * 
 * Filename: MealNutritionsComparator Description: Meal Nutritions Comparator.
 * 
 * Author: Andy {andy@atech-software.com}
 */

public class MealNutritionsComparator implements Comparator<MealNutritionsDisplay>
{

    /**
     * compare (MealPartsDisplay, MealPartsDisplay)
     * 
     * Compares its two arguments for order. Returns a negative integer, zero,
     * or a positive integer as the first argument is less than, equal to, or
     * greater than the second.
     * 
     * The implementor must ensure that sgn(compare(x, y)) == -sgn(compare(y,
     * x)) for all x and y. (This implies that compare(x, y) must throw an
     * exception if and only if compare(y, x) throws an exception.)
     * 
     * The implementor must also ensure that the relation is transitive:
     * ((compare(x, y)>0) && (compare(y, z)>0)) implies compare(x, z)>0.
     * 
     * Finally, the implementer must ensure that compare(x, y)==0 implies that
     * sgn(compare(x, z))==sgn(compare(y, z)) for all z.
     * 
     * It is generally the case, but not strictly required that (compare(x,
     * y)==0) == (x.equals(y)). Generally speaking, any comparator that violates
     * this condition should clearly indicate this fact. The recommended
     * language is
     * "Note: this comparator imposes orderings that are inconsistent with equals."
     * 
     * @param mpd1
     *            - the first object to be compared.
     * @param mpd2
     *            - the second object to be compared.
     * 
     * @see java.util.Comparator#compare(java.lang.Object, java.lang.Object)
     * 
     * @return a negative integer, zero, or a positive integer as the first
     *         argument is less than, equal to, or greater than the second.
     */
    public int compare(MealNutritionsDisplay mnd1, MealNutritionsDisplay mnd2)
    {
        long id1 = Long.parseLong(mnd1.getId());
        long id2 = Long.parseLong(mnd2.getId());

        return (int) (id1 - id2);
    }

}
