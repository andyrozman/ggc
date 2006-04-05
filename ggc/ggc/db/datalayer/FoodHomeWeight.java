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
 *      This one is used for food's home wiights.
 *
 *  Author:   andyrozman
 */

package ggc.db.datalayer;

import org.hibernate.Session;
import org.hibernate.Transaction;

import ggc.db.hibernate.DatabaseObjectHibernate;
import ggc.db.hibernate.FoodHomeWeightH;


public class FoodHomeWeight extends FoodHomeWeightH implements DatabaseObjectHibernate
{

    public boolean debug = false;


    public FoodHomeWeight()
    {
	this.setId(0L);
	this.setAmount(0.0f);
	this.setFood_number(0L);
	this.setMsr_desc("");
	this.setSequence(0);
	this.setWeight_g(0.0f);
    }


    public FoodHomeWeight(FoodHomeWeightH ch)
    {
	this.setId(ch.getId());
	this.setAmount(ch.getAmount());
	this.setFood_number(ch.getFood_number());
	this.setMsr_desc(ch.getMsr_desc());
	this.setSequence(ch.getSequence());
	this.setWeight_g(ch.getWeight_g());
    }


    public String getShortDescription()
    {
        //return this.getDescription();
	return "unknown";
    }


    public String toString()
    {
        //return this.getShortDescription();
	return "unknown";
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

        FoodHomeWeightH ch = new FoodHomeWeightH();

	//ch.setId(this.getId());
	ch.setAmount(this.getAmount());
	ch.setFood_number(this.getFood_number());
	ch.setMsr_desc(this.getMsr_desc());
	ch.setSequence(this.getSequence());
	ch.setWeight_g(this.getWeight_g());

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

	FoodHomeWeightH ch = (FoodHomeWeightH)sess.get(FoodHomeWeightH.class, new Long(this.getId()));

	ch.setAmount(this.getAmount());
	ch.setFood_number(this.getFood_number());
	ch.setMsr_desc(this.getMsr_desc());
	ch.setSequence(this.getSequence());
	ch.setWeight_g(this.getWeight_g());

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

	FoodHomeWeightH ch = (FoodHomeWeightH)sess.get(FoodHomeWeightH.class, new Long(this.getId()));

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

	FoodHomeWeightH ch = (FoodHomeWeightH)sess.get(FoodHomeWeightH.class, new Long(this.getId()));

	this.setAmount(ch.getAmount());
	this.setFood_number(ch.getFood_number());
	this.setMsr_desc(ch.getMsr_desc());
	this.setSequence(ch.getSequence());
	this.setWeight_g(ch.getWeight_g());

        return true;
    }



    /**
     * getObjectName - returns name of DatabaseObject
     * 
     * @return name of object (not Hibernate object)
     */
    public String getObjectName()
    {
        return "Food Home Weight";
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

