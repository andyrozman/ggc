package ggc.nutri.db.datalayer;

import com.atech.graphics.dialogs.selector.ColumnSorter;
import com.atech.graphics.dialogs.selector.SelectableInterface;
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
 *  Filename:     HomeWeightSpecial
 *  Description:  Home Weight handling - Special
 * 
 *  Author: andyrozman {andy@atech-software.com}  
 */

public class HomeWeightSpecial implements SelectableInterface, DAOObject
{

    DataAccessNutri m_da = DataAccessNutri.getInstance();
    I18nControlAbstract ic = m_da.getI18nControlInstance();

    private boolean debug = false;
    String text_idx;

    long id;
    String name;
    String amount;
    String weight;
    float calculated_weight = 0.0f;


    /**
     * Constructor
     */
    public HomeWeightSpecial()
    {

    }


    /**
     * Constructor
     * 
     * @param id
     * @param name
     * @param weight
     */
    public HomeWeightSpecial(long id, String name, String weight)
    {
        this(id, name, "1", weight);
    }


    /**
     * Constructor
     * 
     * @param hw_string
     */
    public HomeWeightSpecial(String hw_string)
    {
        // System.out.println("Hw: " + hw_string);
        String t1[] = m_da.splitString(hw_string, "=");

        this.id = m_da.getLongValueFromString(t1[0]);

        NutritionHomeWeightType nhwt = m_da.getDbCache().homeweight_defs.get("" + id);
        this.name = nhwt.getResolvedName();

        if (t1[1].contains("*"))
        {
            String t2[] = m_da.splitString(t1[1], "*");

            this.amount = t2[0];
            this.weight = t2[1];
        }
        else
        {
            this.amount = "1";
            this.weight = t1[1];
        }

    }


    /**
     * Constructor
     * 
     * @param id
     * @param name
     * @param amount
     * @param weight
     */
    public HomeWeightSpecial(long id, String name, String amount, String weight)
    {
        this.id = id;
        this.name = ic.getPartitialTranslation(name, "_");
        this.amount = amount;
        this.weight = weight;

        setSearchContext();
    }


    /*
     * public NutritionHomeWeightType(NutritionHomeWeightTypeH ch) {
     * this.setId(ch.getId()); this.setName(ch.getName()); setSearchContext(); }
     */

    private void calculateWeight()
    {
        float am = 1.0f;

        if (!this.amount.equals("1"))
        {
            am = m_da.getFloatValueFromString(this.amount, 1.0f);
        }

        float we = m_da.getFloatValueFromString(this.weight, 1.0f);

        this.calculated_weight = am * we;

    }


    /**
     * Get Calculated Weight
     * 
     * @return
     */
    public float getCalculatedWeight()
    {
        if (this.calculated_weight == 0.0f)
        {
            calculateWeight();
        }

        return this.calculated_weight;
    }


    /**
     * Get Short Description
     * 
     * @see com.atech.graphics.dialogs.selector.SelectableInterface#getShortDescription()
     */
    public String getShortDescription()
    {
        // return this.getDescription();
        return this.name + " [" + this.amount + " x " + this.weight + " g]";
    }


    /**
     * To String
     * 
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString()
    {
        return getShortDescription();
    }


    /**
     * Get Name
     * 
     * @return
     */
    public String getName()
    {
        return this.name;
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
     * getAction - returns action that should be done on object 0 = no action 1
     * = add action 2 = edit action 3 = delete action This is used mainly for
     * objects, contained in lists and dialogs, used for processing by higher
     * classes (classes calling selectors, wizards, etc...
     * 
     * @return number of action
     */
    public int getAction()
    {
        return 0;
    }


    // ---
    // --- SelectorInterface
    // ---

    /**
     * Get Column Count
     */
    public int getColumnCount()
    {
        return 4;
    }


    /**
     * Get Column Name
     */
    public String getColumnName(int num)
    {
        switch (num)
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
    }


    /**
     * Get Column Value
     */
    public String getColumnValue(int num)
    {
        switch (num)
        {
            case 4:
                return this.weight;

            case 3:
                return this.amount;

            case 2:
                return this.name;

            default:
                return "" + this.id;

        }

    }


    /**
     * Get Column Value Object
     */
    public Object getColumnValueObject(int num)
    {
        switch (num)
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


    /**
     * Get Column Width
     */
    public int getColumnWidth(int num, int width)
    {

        switch (num)
        {
            case 4:
                return width * 20;
            case 3:
                return width * 15;
            case 2:
                return width * 50;
            default:
                return width * 15;

        } /*
           * switch(num) { case 4: return(int)(width40); case 3:
           * return(int)(width10); case 2: return(int)(width40); default:
           * return(int)(width10);
           * }
           */

    }


    /**
     * Get Item Id
     */
    public long getItemId()
    {
        return this.id;
    }


    /**
     * Is Found
     */
    public boolean isFound(int from, int till, int state)
    {
        return true;
    }


    /**
     * Is Found
     */
    public boolean isFound(int value)
    {
        return true;
    }


    /**
     * Is Found
     */
    public boolean isFound(String text)
    {
        if (this.text_idx.indexOf(text.toUpperCase()) != -1 || text.length() == 0)
            return true;
        else
            return false;
    }


    /**
     * Set Search Context
     */
    public void setSearchContext()
    {
        text_idx = this.name.toUpperCase();
    }

    // ---
    // --- Column sorting
    // ---

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
     * Compares this object with the specified object for order. Returns a
     * negative integer, zero, or a positive integer as this object is less
     * than, equal to, or greater than the specified object.
     * 
     * <p>The implementor must ensure <tt>sgn(x.compareTo(y)) ==
     * -sgn(y.compareTo(x))</tt> for all <tt>x</tt> and <tt>y</tt>. (This
     * implies that <tt>x.compareTo(y)</tt> must throw an exception iff
     * <tt>y.compareTo(x)</tt> throws an exception.)
     * 
     * <p>The implementor must also ensure that the relation is transitive:
     * <tt>(x.compareTo(y)&gt;0 &amp;&amp; y.compareTo(z)&gt;0)</tt> implies
     * <tt>x.compareTo(z)&gt;0</tt>.
     * 
     * <p>Finally, the implementor must ensure that <tt>x.compareTo(y)==0</tt>
     * implies that <tt>sgn(x.compareTo(z)) == sgn(y.compareTo(z))</tt>, for all
     * <tt>z</tt>.
     * 
     * <p>It is strongly recommended, but <i>not</i> strictly required that
     * <tt>(x.compareTo(y)==0) == (x.equals(y))</tt>. Generally speaking, any
     * class that implements the <tt>Comparable</tt> interface and violates this
     * condition should clearly indicate this fact. The recommended language is
     * "Note: this class has a natural ordering that is inconsistent with
     * equals."
     * 
     * <p>In the foregoing description, the notation
     * <tt>sgn(</tt><i>expression</i><tt>)</tt> designates the mathematical
     * <i>signum</i> function, which is defined to return one of <tt>-1</tt>,
     * <tt>0</tt>, or <tt>1</tt> according to whether the value of
     * <i>expression</i> is negative, zero or positive.
     * 
     * @param o the object to be compared.
     * 
     * @return a negative integer, zero, or a positive integer as this object is
     * less than, equal to, or greater than the specified object.
     * 
     * @throws ClassCastException if the specified object's type prevents it
     * from being compared to this object.
     */
    public int compareTo(SelectableInterface o)
    {
        /*
         * if (o instanceof SelectableInterface) { return
         * this.columnSorter.compareObjects(this, (SelectableInterface)o); }
         * else throw new ClassCastException();
         */

        return this.columnSorter.compareObjects(this, o);

    }

}
