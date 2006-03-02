package ggc.db.hibernate;

import java.sql.Connection;
import java.sql.PreparedStatement;

import org.hibernate.Session;



public interface DatabaseObjectHibernate
{


    /**
     * DbAdd - Add this object to database
     * 
     * @param sess Hibernate Session object
     * @throws Exception (HibernateException) with error
     * @return id in type of String
     */
    public abstract String DbAdd(Session sess) throws Exception;


    /**
     * DbEdit - Edit this object in database
     * 
     * @param sess Hibernate Session object
     * @throws Exception (HibernateException) with error
     * @return true if action done or Exception if not
     */
    public abstract boolean DbEdit(Session sess) throws Exception;


    /**
     * DbDelete - Delete this object in database
     * 
     * @param sess Hibernate Session object
     * @throws Exception (HibernateException) with error
     * @return true if action done or Exception if not
     */
    public abstract boolean DbDelete(Session sess) throws Exception;


    /**
     * DbHasChildren - Shows if this entry has any children object, this is needed for delete
     * 
     * 
     * @param sess Hibernate Session object
     * @throws Exception (HibernateException) with error
     * @return true if action done or Exception if not
     */
    public abstract boolean DbHasChildren(Session sess) throws Exception;


    /**
     * DbGet - Loads this object. Id must be set.
     * 
     * 
     * @param sess Hibernate Session object
     * @throws Exception (HibernateException) with error
     * @return true if action done or Exception if not
     */
    public abstract boolean DbGet(Session sess) throws Exception;


    /**
     * getObjectName - returns name of DatabaseObject
     * 
     * @return name of object (not Hibernate object)
     */
    public abstract String getObjectName();


    /**
     * isDebugMode - returns debug mode of object
     * 
     * @return true if object in debug mode
     */
    public abstract boolean isDebugMode();



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
    public abstract int getAction();



}


