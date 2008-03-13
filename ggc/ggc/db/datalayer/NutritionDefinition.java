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

import com.atech.graphics.components.selector.ColumnSorter;
import com.atech.graphics.components.selector.SelectableInterface;
import com.atech.i18n.I18nControlAbstract;

import org.hibernate.Session;
import org.hibernate.Transaction;


public class NutritionDefinition extends NutritionDefinitionH implements DatabaseObjectHibernate, SelectableInterface
{

    public I18nControlAbstract ic = null;


    public boolean debug = false;


    public NutritionDefinition()
    {
        this.setId(0);
	this.setTag("");
	this.setName("");
	this.setWeight_unit("");
	this.setDecimal_places("");
	
	//ic = DataAccess
	ic = DataAccess.getInstance().getI18nInstance();
    }


    public NutritionDefinition(NutritionDefinitionH ch)
    {
	this.setId(ch.getId());
	this.setTag(ch.getTag());
	this.setName(ch.getName());
	this.setWeight_unit(ch.getWeight_unit());
	this.setDecimal_places(ch.getDecimal_places());
	ic = DataAccess.getInstance().getI18nInstance();
    }


    public String getShortDescription()
    {
        return this.getName();
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
     * @see com.atech.graphics.components.selector.SelectableInterface#compareTo(java.lang.Object)
     */
    public int compareTo(Object o)
    {
	// TODO Auto-generated method stub
	return 0;
    }


    /* (non-Javadoc)
     * @see com.atech.graphics.components.selector.SelectableInterface#getColumnCount()
     */
    public int getColumnCount()
    {
	return 3;
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
	    case 1:
		return ic.getMessage("TAG");
		
	    case 2:
		return ic.getMessage("WEIGHT_UNIT");

	    default:
	    case 0:
		return ic.getMessage("NAME");
		
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
	    case 1:
		return this.getTag();
		
	    case 2:
		return this.getWeight_unit();

	    default:
	    case 0:
		return this.getName(); 
		
	}
	
    }


    /* (non-Javadoc)
     * @see com.atech.graphics.components.selector.SelectableInterface#getColumnValueObject(int)
     */
    public Object getColumnValueObject(int num)
    {
	switch(num)
	{
	    case 1:
		return this.getTag();
		
	    case 2:
		return this.getWeight_unit();

	    default:
	    case 0:
		return this.getName(); 
		
	}
    }


    /* (non-Javadoc)
     * @see com.atech.graphics.components.selector.SelectableInterface#getColumnWidth(int, int)
     */
    public int getColumnWidth(int num, int width)
    {
	switch(num)
	{
            case 1:
                return(int)(width*20);
            case 2:
                return(int)(width*40);
            default:
                return(int)(width*40);
		
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
	// TODO Auto-generated method stub
	return false;
    }


    /* (non-Javadoc)
     * @see com.atech.graphics.components.selector.SelectableInterface#isFound(int)
     */
    public boolean isFound(int value)
    {
	// TODO Auto-generated method stub
	return false;
    }


    /* (non-Javadoc)
     * @see com.atech.graphics.components.selector.SelectableInterface#isFound(java.lang.String)
     */
    public boolean isFound(String text)
    {
	// TODO Auto-generated method stub
	return false;
    }


    /* (non-Javadoc)
     * @see com.atech.graphics.components.selector.SelectableInterface#setColumnSorter(com.atech.graphics.components.selector.ColumnSorter)
     */
    public void setColumnSorter(ColumnSorter cs)
    {
	// TODO Auto-generated method stub
	
    }


    /* (non-Javadoc)
     * @see com.atech.graphics.components.selector.SelectableInterface#setSearchContext()
     */
    public void setSearchContext()
    {
	// TODO Auto-generated method stub
	
    }
    
    
    
    
}


