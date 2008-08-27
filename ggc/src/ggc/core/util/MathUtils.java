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
 *  Filename: MathUtils.java
 *  Purpose:  Contains helper methods to carry out small calculations
 *
 *  Author:   rumbi
 *  
 */

package ggc.core.util;

/**
 * Carry out a few small calculations. Cannot be instantiated.
 * 
 * @author rumbi
 */
public abstract class MathUtils
{
    /**
     * Will calculate the average of the passed
     * <code>{@link Number numbers}</code>.
     * 
     * @param <T>
     *            The subclass of <code>{@link Number}</code> to be averaged.
     * @param dataSet
     *            An array containing the <code>{@link Number numbers}</code> to
     *            be averaged.
     * @return The average.
     */
    public static <T extends Number> double getAverage(T... dataSet)
    {
        double result = 0;

        for (T d : dataSet)
        {
            result = add(d, result);
        }

        result /= dataSet.length;

        return result;
    }

    /**
     * Adds two subclasses of <code>{@link Number}</code> and returns the sum as
     * a double.
     * 
     * @param <T>
     *            The subclass of <code>{@link Number}</code> to be added.
     * @param numA
     *            The first summand.
     * @param numB
     *            The second summand.
     * @return The sum of <code>numA</code> and <code>numB</code>.
     */
    public static <T extends Number> double add(T numA, T numB)
    {
        double a = numA.doubleValue();
        double b = numB.doubleValue();
        double result = a + b;

        if (((a > 0) && (b > 0) && (result < 0)) || ((a < 0) && (b < 0) && (result > 0)))
        {
            throw new ArithmeticException("Sum was too large to be contained in a double: " + a + " " + b);
        }

        return result;
    }
}
