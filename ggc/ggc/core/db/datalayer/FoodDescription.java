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
 *      This one is used for description of food.
 *
 *  Author:   andyrozman  {andy@atech-software.com}
 */


package ggc.core.db.datalayer;

import ggc.core.db.hibernate.DatabaseObjectHibernate;
import ggc.core.db.hibernate.FoodDescriptionH;
import ggc.core.db.hibernate.FoodUserDescriptionH;
import ggc.core.util.DataAccess;

import java.util.ArrayList;

import com.atech.db.hibernate.transfer.BackupRestoreObject;
import com.atech.graphics.components.tree.CheckBoxTreeNodeInterface;
import com.atech.i18n.I18nControlAbstract;

import org.hibernate.Session;
import org.hibernate.Transaction;


public class FoodDescription implements DatabaseObjectHibernate, BackupRestoreObject 
//extends FoodDescriptionH implements DatabaseObjectHibernate
{

    public boolean debug = false;
    public int type = 1;

    private FoodDescriptionH m_food_desc1; 
    private FoodUserDescriptionH m_food_desc2;
    private long id;
    
    boolean selected = false;
    I18nControlAbstract ic = null; //DataAccess.getInstance().getI18nControlInstance();
    boolean food_empty = false;
    
    
    public FoodDescription(I18nControlAbstract ic)
    {
	this.ic = ic;
	this.food_empty = true;
    }
    
    public FoodDescription(boolean food_empty)
    {
	this.food_empty = food_empty;
	ic = DataAccess.getInstance().getI18nControlInstance();
    }
    
    public FoodDescription(int type)
    {
	this.type = type;
	
	if (type == 1)
	    this.m_food_desc1 = new FoodDescriptionH();
	else
	    this.m_food_desc2 = new FoodUserDescriptionH();
	
	ic = DataAccess.getInstance().getI18nControlInstance();
	
	
	/*
        this.setId(0);
	this.setFood_group_id(0);
	this.setName("");
	this.setI18n_name("");
	this.setRefuse(0.0f);
	this.setNutritions("");
	this.setHome_weights("");
	this.setRefuse(0.0f); */
    }


    public FoodDescription(FoodDescriptionH ch)
    {
	this.m_food_desc1 = ch;
	this.type = 1;
	
	ic = DataAccess.getInstance().getI18nControlInstance();
	/*
	this.setId(ch.getId());
	this.setFood_group_id(ch.getFood_group_id());
	this.setName(ch.getName());
	this.setI18n_name(ch.getI18n_name());
//	this.setCH_g(ch.getCH_g());
//	this.setEnergy_kcal(ch.getEnergy_kcal());
//	this.setEnergy_kJ(ch.getEnergy_kJ());
//	this.setFat_g(ch.getFat_g());
	this.setRefuse(ch.getRefuse());
	this.setHome_weights(ch.getHome_weights());
	this.setNutritions(ch.getNutritions());

//	this.setSugar_g(ch.getSugar_g()); */
    }

    public FoodDescription(FoodUserDescriptionH ch)
    {
	this.m_food_desc2 = ch;
	this.type = 2;
	
	ic = DataAccess.getInstance().getI18nControlInstance();
    }
    
    
    public Object getHibernateObject()
    {
	if (this.type==1)
	    return this.m_food_desc1;
	else
	    return this.m_food_desc2;
    }
    
    

    public String getShortDescription()
    {
        return this.getName();
    }

    
    
    public long getId() 
    {
	if ((this.m_food_desc1==null) && (this.m_food_desc2==null))
	{
	    return this.id;
	}
	
	if (type==1)
	    return this.m_food_desc1.getId();
	else
	    return this.m_food_desc2.getId();
    }

    public void setId(long id) 
    {
	if ((this.m_food_desc1==null) && (this.m_food_desc2==null))
	{
	    this.id = id;
	}

	if (type==1)
	    this.m_food_desc1.setId(id);
	else
	    this.m_food_desc2.setId(id);
    }

    public long getGroup_id() 
    {
	if (type==1)
	    return this.m_food_desc1.getGroup_id();
	else
	    return this.m_food_desc2.getGroup_id();
    }

    public void setGroup_id(long group_id) 
    {
	if (type==1)
	    this.m_food_desc1.setGroup_id(group_id);
	else
	    this.m_food_desc2.setGroup_id(group_id);
    }

    public String getName() 
    {
	if (type==1)
	    return this.m_food_desc1.getName();
	else
	    return this.m_food_desc2.getName();
    }

    public void setName(String name) 
    {
	if (type==1)
	    this.m_food_desc1.setName(name);
	else
	    this.m_food_desc2.setName(name);
    }

    public String getName_i18n() 
    {
	if (type==1)
	    return this.m_food_desc1.getName_i18n();
	else
	    return this.m_food_desc2.getName_i18n();
    }

    public void setName_i18n(String name_i18n) 
    {
	if (type==1)
	    this.m_food_desc1.setName_i18n(name_i18n);
	else
	    this.m_food_desc2.setName_i18n(name_i18n);
    }

    public float getRefuse() 
    {
	if (type==1)
	    return this.m_food_desc1.getRefuse();
	else
	    return this.m_food_desc2.getRefuse();
    }

    public void setRefuse(float refuse) 
    {
	if (type==1)
	    this.m_food_desc1.setRefuse(refuse);
	else
	    this.m_food_desc2.setRefuse(refuse);
    }

    public String getNutritions() 
    {
	if (type==1)
	    return this.m_food_desc1.getNutritions();
	else
	    return this.m_food_desc2.getNutritions();
    }

    public void setNutritions(String nutritions) 
    {
	if (type==1)
	    this.m_food_desc1.setNutritions(nutritions);
	else
	    this.m_food_desc2.setNutritions(nutritions);
    }

    public String getHome_weights() 
    {
	if (type==1)
	    return this.m_food_desc1.getHome_weights();
	else
	    return this.m_food_desc2.getHome_weights();
    }

    public void setHome_weights(String home_weights) 
    {
	if (type==1)
	    this.m_food_desc1.setHome_weights(home_weights);
	else
	    this.m_food_desc2.setHome_weights(home_weights);
    }
    
    
    public String getDescription()
    {
	if (type==2)
	    return this.m_food_desc2.getDescription();
	else
	    return null;
    }
    
    
    public void setDescription(String description)
    {
	if (type==2)
	    this.m_food_desc2.setDescription(description);
    }
    
    
    public int getFoodType()
    {
	return type;
    }

    @Override
    public String toString()
    {
	if (this.food_empty)
	    return this.getTargetName();
	else
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

        Long id;
        
        if (this.type == 1)
            id = (Long)sess.save(this.m_food_desc1);
        else
            id = (Long)sess.save(this.m_food_desc2);

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
        
        if (this.type == 1)
            sess.update(this.m_food_desc1); 
        else
            sess.update(this.m_food_desc2);

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

        if (this.type == 1)
            sess.delete(this.m_food_desc1); 
        else
            sess.delete(this.m_food_desc2);
	
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
	
	if (type==1)
	    this.m_food_desc1 = (FoodDescriptionH)sess.get(FoodDescriptionH.class, new Long(this.getId()));
	else
	    this.m_food_desc2 = (FoodUserDescriptionH)sess.get(FoodUserDescriptionH.class, new Long(this.getId()));
	
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

    
    //---
    //---  BackupRestoreObject
    //---
    

    /* 
     * getTargetName
     */
    public String getTargetName()
    {
	return ic.getMessage("FOODS");
    }
    
    
    
    /* 
     * getChildren
     */
    public ArrayList<CheckBoxTreeNodeInterface> getChildren()
    {
	return null;
    }

    
    /* 
     * hasChildren
     */
    public boolean hasChildren()
    {
	return false;
    }
    
    
    /* 
     * isSelected
     */
    public boolean isSelected()
    {
	return selected;
    }

    /* 
     * setSelected
     */
    public void setSelected(boolean newValue)
    {
	this.selected = newValue;
    }
    
    
    public boolean isCollection()
    {
	return false;
    }
    
    
    
}


