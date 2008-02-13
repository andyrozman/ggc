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
 *  Filename: Meal
 *  Purpose:  This is datalayer file (data file, with methods to work with database or in 
 *      this case Hibernate). 
 *      This one is used for description of meal.
 *
 *  Author:   andyrozman  {andy@atech-software.com}
 */


package ggc.db.datalayer;

import ggc.db.hibernate.DatabaseObjectHibernate;
import ggc.db.hibernate.MealH;

import java.util.ArrayList;
import java.util.StringTokenizer;

import org.hibernate.Session;
import org.hibernate.Transaction;


public class Meal extends MealH implements DatabaseObjectHibernate
{

    public boolean debug = false;
    ArrayList<MealPart> parts_lst = new ArrayList<MealPart>();
    //ArrayList<MealPart> parts_lst = new ArrayList<MealPart>();
    


    public Meal()
    {
	this.setId(0L);
	this.setName("");
	this.setGroup_id(0);
	this.setDescription("");
	this.setParts("");
	this.setValues("");
	this.setExtended("");
	this.setComment("");
	
	loadParts();
	loadValues();
    }


    public Meal(MealH ch)
    {
	this.setId(ch.getId());
	this.setName(ch.getName());
	this.setGroup_id(ch.getGroup_id());
	this.setDescription(ch.getDescription());
	this.setParts(ch.getParts());
	this.setValues(ch.getValues());
	this.setExtended(ch.getExtended());
	this.setComment(ch.getComment());

	loadParts();
	loadValues();
    }


    private void loadParts()
    {
	StringTokenizer strtok = new StringTokenizer(this.getParts(), ";");
	
	while (strtok.hasMoreTokens())
	{
	    
	}
	    
	
    }
    
    private void loadValues()
    {
	
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

	MealH ch = new MealH();

	ch.setId(this.getId());
	ch.setName(this.getName());
	ch.setGroup_id(this.getGroup_id());
	ch.setDescription(this.getDescription());
	ch.setParts(this.getParts());
	ch.setValues(this.getValues());
	ch.setExtended(this.getExtended());
	ch.setComment(this.getComment());

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

	MealH ch = (MealH)sess.get(MealH.class, new Long(this.getId()));

	ch.setId(this.getId());
	ch.setName(this.getName());
	ch.setGroup_id(this.getGroup_id());
	ch.setDescription(this.getDescription());
	ch.setParts(this.getParts());
	ch.setValues(this.getValues());
	ch.setExtended(this.getExtended());
	ch.setComment(this.getComment());

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
/*
        Transaction tx = sess.beginTransaction();

	MealFoodDescriptionH ch = (MealFoodDescriptionH)sess.get(MealFoodDescriptionH.class, new Long(this.getId()));

        sess.delete(ch);
        tx.commit();

        return true;
*/
	return false;

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
        //System.out.println("Not implemented: FoodDescription::DbHasChildren");
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

	MealH ch = (MealH)sess.get(MealH.class, new Long(this.getId()));

	this.setId(ch.getId());
	this.setName(ch.getName());
	this.setGroup_id(ch.getGroup_id());
	this.setDescription(ch.getDescription());
	this.setParts(ch.getParts());
	this.setValues(ch.getValues());
	this.setExtended(ch.getExtended());
	this.setComment(ch.getComment());

        return true;
    }



    /**
     * getObjectName - returns name of DatabaseObject
     * 
     * @return name of object (not Hibernate object)
     */
    public String getObjectName()
    {
        return "Meal Food Description";
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

}


