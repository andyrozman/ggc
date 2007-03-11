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
 *  Filename: FoodDescription
 *  Purpose:  This is datalayer file (data file, with methods to work with database or in 
 *      this case Hibernate). 
 *      This one is used for description of food groups.
 *
 *  Author:   andyrozman
 */


package ggc.db.datalayer;

import org.hibernate.Session;
import org.hibernate.Transaction;

import ggc.db.hibernate.DatabaseObjectHibernate;
import ggc.db.hibernate.SettingsH;



public class Settings extends SettingsH implements DatabaseObjectHibernate
{

    public boolean debug = false;

    public static final int MEAL_GROUP_MEALS = 1;
    public static final int MEAL_GROUP_NUTRITION = 2;

    public boolean edited = false;
    public boolean added = false;


    public Settings()
    {
    }


    public Settings(SettingsH ch)
    {
	this.setId(ch.getId());
	this.setKey(ch.getKey());
	this.setValue(ch.getValue());
	this.setType(ch.getType());
	this.setDescription(ch.getDescription());
    }


    public String getShortDescription()
    {
        //return this.getDescription();
	return "Settings [Key=" + this.getKey()+ ";Value=" + this.getValue() + "]";
    }


    @Override
    public String toString()
    {
	//return this.getDescription();
        return this.getShortDescription();
    }


    public void setElementEdited()
    {
	this.edited = true;
    }

    public void setElementAdded()
    {
	this.added = true;
    }

    public boolean isElementEdited()
    {
	return this.edited;
    }

    public boolean isElementAdded()
    {
	return this.added;
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

	SettingsH ch = new SettingsH();

	ch.setKey(this.getKey());
	ch.setValue(this.getValue());
	ch.setType(this.getType());
	ch.setDescription(this.getDescription());

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

	SettingsH ch = (SettingsH)sess.get(SettingsH.class, new Long(this.getId()));

	ch.setId(this.getId());
	ch.setKey(this.getKey());
	ch.setValue(this.getValue());
	ch.setType(this.getType());
	ch.setDescription(this.getDescription());

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

	SettingsH ch = (SettingsH)sess.get(SettingsH.class, new Long(this.getId()));

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

	SettingsH ch = (SettingsH)sess.get(SettingsH.class, new Long(this.getId()));

	this.setId(ch.getId());
	this.setKey(ch.getKey());
	this.setValue(ch.getValue());
	this.setType(ch.getType());
	this.setDescription(ch.getDescription());

        return true;
    }



    /**
     * getObjectName - returns name of DatabaseObject
     * 
     * @return name of object (not Hibernate object)
     */
    public String getObjectName()
    {
        return "Settings";
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


