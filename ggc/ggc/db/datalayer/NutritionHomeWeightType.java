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

package ggc.db.datalayer;

import ggc.db.hibernate.DatabaseObjectHibernate;
import ggc.db.hibernate.NutritionHomeWeightTypeH;
import ggc.core.util.I18nControl;

import com.atech.graphics.dialogs.selector.ColumnSorter;
import com.atech.graphics.dialogs.selector.SelectableInterface;

import org.hibernate.Session;
import org.hibernate.Transaction;


public class NutritionHomeWeightType extends NutritionHomeWeightTypeH implements DatabaseObjectHibernate, SelectableInterface
{


    I18nControl ic = I18nControl.getInstance(); 

    public boolean debug = false;
    String text_idx;


    public NutritionHomeWeightType()
    {
	this.setId(0L);
	this.setName("");
	setSearchContext();
    }


    public NutritionHomeWeightType(NutritionHomeWeightTypeH ch)
    {
	this.setId(ch.getId());
	this.setName(ch.getName());
	setSearchContext();
    }


    public String getShortDescription()
    {
        //return this.getDescription();
	return ic.getMessage(this.getName());
    }


    @Override
    public String toString()
    {
        //return this.getShortDescription();
	return getShortDescription();
    }


    //---
    //---  DatabaseObjectHibernate
    //---


    /**
     * DbAdd - Add this object to database
     * 
     * @param sess Hibernate Session object
     * @throws Exception (HibernateException) with error
     * @return id in type of String
     */
    public String DbAdd(Session sess) throws Exception
    {
        
        Transaction tx = sess.beginTransaction();

        NutritionHomeWeightTypeH ch = new NutritionHomeWeightTypeH();

//	ch.setId(this.getId());
	ch.setName(this.getName());

        Long id = (Long)sess.save(ch);

        tx.commit();

	this.setId(id.longValue());

        return ""+id.longValue();
    }



    /**
     * DbEdit - Edit this object in database
     * 
     * @param sess Hibernate Session object
     * @throws Exception (HibernateException) with error
     * @return true if action done or Exception if not
     */
    public boolean DbEdit(Session sess) throws Exception
    {

        Transaction tx = sess.beginTransaction();

	NutritionHomeWeightTypeH ch = (NutritionHomeWeightTypeH)sess.get(NutritionHomeWeightTypeH.class, new Long(this.getId()));

	ch.setId(this.getId());
	ch.setName(this.getName());

        sess.update(ch);
        tx.commit();

        return true;

    }



    /**
     * DbDelete - Delete this object in database
     * 
     * @param sess Hibernate Session object
     * @throws Exception (HibernateException) with error
     * @return true if action done or Exception if not
     */
    public boolean DbDelete(Session sess) throws Exception
    {

        Transaction tx = sess.beginTransaction();

	NutritionHomeWeightTypeH ch = (NutritionHomeWeightTypeH)sess.get(NutritionHomeWeightTypeH.class, new Long(this.getId()));

        sess.delete(ch);
        tx.commit();

        return true;

    }



    /**
     * DbHasChildren - Shows if this entry has any children object, this is needed for delete
     * 
     * 
     * @param sess Hibernate Session object
     * @throws Exception (HibernateException) with error
     * @return true if action done or Exception if not
     */
    public boolean DbHasChildren(Session sess) throws Exception
    {
        //System.out.println("Not implemented: FoodGroup::DbHasChildren");
        return false;
    }



    /**
     * DbGet - Loads this object. Id must be set.
     * 
     * 
     * @param sess Hibernate Session object
     * @throws Exception (HibernateException) with error
     * @return true if action done or Exception if not
     */
    public boolean DbGet(Session sess) throws Exception
    {

	NutritionHomeWeightTypeH ch = (NutritionHomeWeightTypeH)sess.get(NutritionHomeWeightTypeH.class, new Long(this.getId()));

	this.setId(ch.getId());
	this.setName(ch.getName());

        return true;
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
	return 3;
    }


    /* 
     * getColumnName
     */
    public String getColumnName(int num)
    {
	switch(num)
	{
	    case 3:
		return ic.getMessage("USER_DEFINED");
		
	    case 2:
		return ic.getMessage("NAME");

	    default:
		return ic.getMessage("ID");
		
	}
    }


    /* 
     * getColumnValue
     */
    public String getColumnValue(int num)
    {
	switch(num)
	{
	    case 3:
		return getYesNo(this.getStatic_entry());
		
	    case 2:
		return this.getName();

	    default:
		return "" + this.getItemId();
		
	}
    }

    
    private String getYesNo(int value)
    {
	if (value == 1)
	    return ic.getMessage("NO");
	else
	    return ic.getMessage("YES");
    }
    

    /* 
     * getColumnValueObject
     */
    public Object getColumnValueObject(int num)
    {
	switch(num)
	{
	    case 3:
		return getYesNo(this.getStatic_entry());
		
	    case 2:
		return this.getName();

	    default:
		return new Long(this.getItemId());
		
	}
    }


    /* 
     * getColumnWidth
     */
    public int getColumnWidth(int num, int width)
    {
	
	switch(num)
	{
            case 3:
                return(int)(width*20);
            case 2:
                return(int)(width*60);
            default:
                return(int)(width*20);
		
	}
    }


    /* 
     * getItemId
     */
    public long getItemId()
    {
	return this.getId();
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
	text_idx = this.getName().toUpperCase();
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


