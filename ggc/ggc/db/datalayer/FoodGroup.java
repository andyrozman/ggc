package ggc.db.datalayer;

import org.hibernate.Session;
import org.hibernate.Transaction;

import ggc.db.hibernate.DatabaseObjectHibernate;
import ggc.db.hibernate.FoodGroupH;



public class FoodGroup extends FoodGroupH implements DatabaseObjectHibernate
{

    public boolean debug = false;


    public FoodGroup()
    {
        this.setId(0);
	this.setDescription("");
    }


    public FoodGroup(FoodGroupH ch)
    {
        this.setId(ch.getId());
	this.setDescription(ch.getDescription());
    }


    public String getShortDescription()
    {
        return this.getDescription();
    }


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

        FoodGroupH ch = new FoodGroupH();

        ch.setId(this.getId());
	ch.setDescription(this.getDescription());

        Integer id = (Integer)sess.save(ch);

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

	FoodGroupH ch = (FoodGroupH)sess.get(FoodGroupH.class, new Integer(this.getId()));

	ch.setId(this.getId());
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

	FoodGroupH ch = (FoodGroupH)sess.get(FoodGroupH.class, new Integer(this.getId()));

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
        System.out.println("Not implemented: FoodGroup::DbHasChildren");
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

	FoodGroupH ch = (FoodGroupH)sess.get(FoodGroupH.class, new Integer(this.getId()));

	this.setId(ch.getId());
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
        return "Food Group";
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


