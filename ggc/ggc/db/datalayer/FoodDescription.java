package ggc.db.datalayer;

import org.hibernate.Session;
import org.hibernate.Transaction;

import ggc.db.hibernate.DatabaseObjectHibernate;
import ggc.db.hibernate.FoodDescriptionH;



public class FoodDescription extends FoodDescriptionH implements DatabaseObjectHibernate
{

    public boolean debug = true;


    public FoodDescription()
    {
        this.setId(0);
	this.setFood_group_id(0);
	this.setDescription("");
	this.setShort_description("");
	this.setCH_g(0.0f);
	this.setEnergy_kcal(0.0f);
	this.setEnergy_kJ(0.0f);
	this.setFat_g(0.0f);
	this.setRefuse(0.0f);
	this.setSugar_g(0.0f);
    }


    public FoodDescription(FoodDescriptionH ch)
    {
	this.setId(ch.getId());
	this.setFood_group_id(ch.getFood_group_id());
	this.setDescription(ch.getDescription());
	this.setShort_description(ch.getShort_description());
	this.setCH_g(ch.getCH_g());
	this.setEnergy_kcal(ch.getEnergy_kcal());
	this.setEnergy_kJ(ch.getEnergy_kJ());
	this.setFat_g(ch.getFat_g());
	this.setRefuse(ch.getRefuse());
	this.setSugar_g(ch.getSugar_g());
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

	FoodDescriptionH ch = (FoodDescriptionH)sess.get(FoodDescriptionH.class, new Long(this.getId()));

	ch.setId(this.getId());
	ch.setFood_group_id(this.getFood_group_id());
	ch.setDescription(this.getDescription());
	ch.setShort_description(this.getShort_description());
	ch.setCH_g(this.getCH_g());
	ch.setEnergy_kcal(this.getEnergy_kcal());
	ch.setEnergy_kJ(this.getEnergy_kJ());
	ch.setFat_g(this.getFat_g());
	ch.setRefuse(this.getRefuse());
	ch.setSugar_g(this.getSugar_g());

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

	FoodDescriptionH ch = (FoodDescriptionH)sess.get(FoodDescriptionH.class, new Long(this.getId()));

	ch.setId(this.getId());
	ch.setFood_group_id(this.getFood_group_id());
	ch.setDescription(this.getDescription());
	ch.setShort_description(this.getShort_description());
	ch.setCH_g(this.getCH_g());
	ch.setEnergy_kcal(this.getEnergy_kcal());
	ch.setEnergy_kJ(this.getEnergy_kJ());
	ch.setFat_g(this.getFat_g());
	ch.setRefuse(this.getRefuse());
	ch.setSugar_g(this.getSugar_g());

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

	FoodDescriptionH ch = (FoodDescriptionH)sess.get(FoodDescriptionH.class, new Long(this.getId()));

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

	FoodDescriptionH ch = (FoodDescriptionH)sess.get(FoodDescriptionH.class, new Long(this.getId()));

	this.setId(ch.getId());
	this.setFood_group_id(ch.getFood_group_id());
	this.setDescription(ch.getDescription());
	this.setShort_description(ch.getShort_description());
	this.setCH_g(ch.getCH_g());
	this.setEnergy_kcal(ch.getEnergy_kcal());
	this.setEnergy_kJ(ch.getEnergy_kJ());
	this.setFat_g(ch.getFat_g());
	this.setRefuse(ch.getRefuse());
	this.setSugar_g(ch.getSugar_g());

        return true;
    }



    /**
     * getObjectName - returns name of DatabaseObject
     * 
     * @return name of object (not Hibernate object)
     */
    public String getObjectName()
    {
        return "Food Description";
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


