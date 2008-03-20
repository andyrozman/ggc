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
 *  Filename: NutritionDefinition
 *  Purpose:  This is datalayer file (data file, with methods to work with database or in 
 *      this case Hibernate). 
 *      This one is used for nutritions definition.
 *
 *  Author:   andyrozman  {andy@atech-software.com}
 */


package ggc.db.datalayer;

import ggc.db.hibernate.DatabaseObjectHibernate;
import ggc.db.hibernate.NutritionDefinitionH;
import ggc.util.DataAccess;

import java.util.Comparator;

import com.atech.graphics.dialogs.selector.ColumnSorter;
import com.atech.graphics.dialogs.selector.SelectableInterface;
import com.atech.i18n.I18nControlAbstract;

import org.hibernate.Session;
import org.hibernate.Transaction;


public class NutritionDefinition extends NutritionDefinitionH implements DatabaseObjectHibernate, SelectableInterface, Comparator<NutritionDefinition>
{

    public I18nControlAbstract ic = null;


    public boolean debug = false;
    String text_idx = "";


    public NutritionDefinition()
    {
        this.setId(0);
	this.setTag("");
	this.setName("");
	this.setWeight_unit("");
	this.setDecimal_places("");
	
	//ic = DataAccess
	ic = DataAccess.getInstance().getI18nInstance();
	
	this.setSearchContext();
    }


    public NutritionDefinition(NutritionDefinitionH ch)
    {
	this.setId(ch.getId());
	this.setTag(ch.getTag());
	this.setName(ch.getName());
	this.setWeight_unit(ch.getWeight_unit());
	this.setDecimal_places(ch.getDecimal_places());
	ic = DataAccess.getInstance().getI18nInstance();
	this.setSearchContext();
    }


    public String getShortDescription()
    {
        return this.getName() + " (" + this.getWeight_unit() + ")";
    }


    @Override
    public String toString()
    {
        return this.getShortDescription();
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

	NutritionDefinitionH ch = new NutritionDefinitionH();

	ch.setId(this.getId());
	ch.setTag(this.getTag());
	ch.setName(this.getName());
	ch.setWeight_unit(this.getWeight_unit());
	ch.setDecimal_places(this.getDecimal_places());

        Long id = (Long)sess.save(ch);

        tx.commit();

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

	NutritionDefinitionH ch = (NutritionDefinitionH)sess.get(NutritionDefinitionH.class, new Long(this.getId()));

	ch.setId(this.getId());
	ch.setTag(this.getTag());
	ch.setName(this.getName());
	ch.setWeight_unit(this.getWeight_unit());
	ch.setDecimal_places(this.getDecimal_places());

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

	NutritionDefinitionH ch = (NutritionDefinitionH)sess.get(NutritionDefinitionH.class, new Long(this.getId()));

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
        System.out.println("Not implemented: FoodDescription::DbHasChildren");
        return true;
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

	NutritionDefinitionH ch = (NutritionDefinitionH)sess.get(NutritionDefinitionH.class, new Long(this.getId()));

	this.setId(ch.getId());
	this.setTag(ch.getTag());
	this.setName(ch.getName());
	this.setWeight_unit(ch.getWeight_unit());
	this.setDecimal_places(ch.getDecimal_places());

	this.setSearchContext();

        return true;
    }



    /**
     * getObjectName - returns name of DatabaseObject
     * 
     * @return name of object (not Hibernate object)
     */
    public String getObjectName()
    {
        return "Food Nutrition Definition";
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

    

    /* (non-Javadoc)
     * @see com.atech.graphics.components.selector.SelectableInterface#getColumnCount()
     */
    public int getColumnCount()
    {
	return 4;
    }


    /* (non-Javadoc)
     * @see com.atech.graphics.components.selector.SelectableInterface#getColumnName(int)
     */
    public String getColumnName(int num)
    {
	//this.getId();
	/*this.getName();
	this.getTag();
	this.getWeight_unit(); */
	switch(num)
	{
	    case 4:
		return ic.getMessage("TAG");
		
	    case 3:
		return ic.getMessage("WEIGHT_UNIT");

	    case 2:
		return ic.getMessage("NAME");

	    default:
		return ic.getMessage("ID");
		
	}
	
    }


    /* (non-Javadoc)
     * @see com.atech.graphics.components.selector.SelectableInterface#getColumnValue(int)
     */
    public String getColumnValue(int num)
    {

	//this.getId();
	/*this.getName();
	this.getTag();
	this.getWeight_unit(); */
	switch(num)
	{
	    case 4:
		return this.getTag();
		
	    case 3:
		return this.getWeight_unit();

	    case 2:
		return this.getName(); 

	    default:
		return "" + this.getId();
		
	}
	
    }


    /* (non-Javadoc)
     * @see com.atech.graphics.components.selector.SelectableInterface#getColumnValueObject(int)
     */
    public Object getColumnValueObject(int num)
    {
	switch(num)
	{
	    case 4:
		return this.getTag();
		
	    case 3:
		return this.getWeight_unit();

	    case 2:
		return this.getName();
		
	    default:
		return this.getId();
		
	}
    }


    /* (non-Javadoc)
     * @see com.atech.graphics.components.selector.SelectableInterface#getColumnWidth(int, int)
     */
    public int getColumnWidth(int num, int width)
    {
	switch(num)
	{
            case 4:
                return(int)(width*20);
            case 3:
                return(int)(width*20);
            case 2:
                return(int)(width*40);
            default:
                return(int)(width*20);
		
	}
    }


    /* (non-Javadoc)
     * @see com.atech.graphics.components.selector.SelectableInterface#getItemId()
     */
    public long getItemId()
    {
	return this.getId();
    }


    /* (non-Javadoc)
     * @see com.atech.graphics.components.selector.SelectableInterface#isFound(int, int, int)
     */
    public boolean isFound(int from, int till, int state)
    {
	return true;
    }


    /* (non-Javadoc)
     * @see com.atech.graphics.components.selector.SelectableInterface#isFound(int)
     */
    public boolean isFound(int value)
    {
	return true;
    }


    /* (non-Javadoc)
     * @see com.atech.graphics.components.selector.SelectableInterface#isFound(java.lang.String)
     */
    public boolean isFound(String text)
    {
        if ((this.text_idx.indexOf(text.toUpperCase())!=-1) || (text.length()==0))
            return true;
        else
            return false;
    }




    /* (non-Javadoc)
     * @see com.atech.graphics.components.selector.SelectableInterface#setSearchContext()
     */
    public void setSearchContext()
    {
        this.text_idx = this.getName().toUpperCase();
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
    
    
    
    /**
     * compare (MealPartsDisplay, MealPartsDisplay)
     * 
     * Compares its two arguments for order. Returns a negative integer, zero, or a positive integer as the first argument is less than, equal to, or greater than the second.
     * 
     * The implementor must ensure that sgn(compare(x, y)) == -sgn(compare(y, x)) for all x and y. (This implies that compare(x, y) must throw an exception if and only if compare(y, x) throws an exception.)
     * 
     * The implementor must also ensure that the relation is transitive: ((compare(x, y)>0) && (compare(y, z)>0)) implies compare(x, z)>0.
     * 
     * Finally, the implementer must ensure that compare(x, y)==0 implies that sgn(compare(x, z))==sgn(compare(y, z)) for all z.
     * 
     * It is generally the case, but not strictly required that (compare(x, y)==0) == (x.equals(y)). Generally speaking, any comparator that violates this condition should clearly indicate this fact. The recommended language is "Note: this comparator imposes orderings that are inconsistent with equals."
     * 
     * @param mpd1 - the first object to be compared.       
     * @param mpd2 - the second object to be compared.       
     *  
     * @see java.util.Comparator#compare(java.lang.Object, java.lang.Object)
     * 
     * @return a negative integer, zero, or a positive integer as the first argument is less than, equal to, or greater than the second.
     */
    public int compare(NutritionDefinition mnd1, NutritionDefinition mnd2)
    {
	long id1 = mnd1.getId();
	long id2 = mnd2.getId();
	    
	return (int)(id1-id2);
    }
    
    
}


