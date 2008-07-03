/*
 * GGC - GNU Gluco Control
 * 
 * A pure java app to help you manage your diabetes.
 * 
 * See AUTHORS for copyright information.
 * 
 * This program is free software; you can redistribute it and/or modify it under
 * the terms of the GNU General Public License as published by the Free Software
 * Foundation; either version 2 of the License, or (at your option) any later
 * version.
 * 
 * This program is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU General Public License for more
 * details.
 * 
 * You should have received a copy of the GNU General Public License along with
 * this program; if not, write to the Free Software Foundation, Inc., 59 Temple
 * Place, Suite 330, Boston, MA 02111-1307 USA
 * 
 * Filename: MealGroup Purpose: This is datalayer file (data file, with methods
 * to work with database or in this case Hibernate). This one is used for
 * description of Meal Groups
 * 
 * Author: andyrozman {andy@atech-software.com}
 */

package ggc.core.db.datalayer;

import ggc.core.db.hibernate.MealGroupH;
import ggc.core.util.DataAccess;

import java.util.ArrayList;

import com.atech.db.hibernate.DatabaseObjectHibernate;
import com.atech.db.hibernate.transfer.BackupRestoreObject;
import com.atech.graphics.components.tree.CheckBoxTreeNodeInterface;
import com.atech.i18n.I18nControlAbstract;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;

public class MealGroup extends MealGroupH implements DatabaseObjectHibernate, BackupRestoreObject
{

    /**
     * 
     */
    private static final long serialVersionUID = 1123099194144366632L;

    public boolean debug = false;

    public static final int MEAL_GROUP_MEALS = 1;
    public static final int MEAL_GROUP_NUTRITION = 2;

    private ArrayList<MealGroup> children_group = new ArrayList<MealGroup>();
    private ArrayList<Object> children = new ArrayList<Object>();

    boolean selected = false;
    I18nControlAbstract ic = null; // DataAccess.getInstance().
                                   // getI18nControlInstance();
    boolean meal_empty = false;

    public MealGroup(I18nControlAbstract ic)
    {
        this.ic = ic;
        this.meal_empty = true;
    }

    public MealGroup(boolean meal_empty)
    {
        this.meal_empty = meal_empty;
        ic = DataAccess.getInstance().getI18nControlInstance();
    }

    public MealGroup()
    {
        ic = DataAccess.getInstance().getI18nControlInstance();
        this.setId(0);
        this.setName("");
        this.setDescription("");
        this.setName_i18n("");
        this.setParent_id(-1);
    }

    public MealGroup(MealGroupH ch)
    {
        ic = DataAccess.getInstance().getI18nControlInstance();
        this.setId(ch.getId());
        this.setName(ch.getName());
        this.setDescription(ch.getDescription());
        this.setName_i18n(ch.getName_i18n());
        this.setParent_id(ch.getParent_id());
    }

    public int getChildCount()
    {
        return this.children.size();
    }

    public boolean hasChildren()
    {
        return (getChildCount() != 0);
    }

    public void addChild(MealGroup fg)
    {
        children_group.add(fg);
        children.add(fg);
    }

    public void addChild(Meal fd)
    {
        children.add(fd);
    }

    public void removeChild(MealGroup fg)
    {
        children_group.remove(fg);
        children.remove(fg);
    }

    public void removeChild(Meal fd)
    {
        children.remove(fd);
    }

    public Object getChild(int index)
    {
        return this.children.get(index);
    }

    public int findChild(Object child)
    {
        return this.children.indexOf(child);
    }

    public int getGroupChildrenCount()
    {
        return this.children_group.size();
    }

    public boolean hasGroupChildren()
    {
        return (getGroupChildrenCount() != 0);
    }

    public Object getGroupChild(int index)
    {
        return this.children_group.get(index);
    }

    public int findGroupChild(Object child)
    {
        return this.children_group.indexOf(child);
    }

    public String getShortDescription()
    {
        return this.getName();
    }

    public String getLongDescription()
    {
        return "MealGroup [id=" + this.getId() + ",name=" + this.getName() + ",parent_id=" + this.getParent_id() + "]";
    }
    
    
    
    @Override
    public String toString()
    {
        if (this.meal_empty)
            return this.getTargetName();
        else
            return this.getShortDescription();
    }

    // ---
    // --- DatabaseObjectHibernate
    // ---

    /*
     * DbAdd - Add this object to database
     * 
     * @param sess Hibernate Session object
     * 
     * @throws Exception (HibernateException) with error
     * 
     * @return id in type of String
     */
    public String DbAdd(Session sess) throws Exception
    {

        Transaction tx = sess.beginTransaction();

        MealGroupH ch = new MealGroupH();

        //ch.setId(this.getId());
        ch.setName(this.getName());
        ch.setDescription(this.getDescription());
        ch.setParent_id(this.getParent_id());
        ch.setName_i18n(this.getName_i18n());

        Long id = (Long) sess.save(ch);

        tx.commit();
        
        this.setId(id);

        return "" + id.longValue();

    }

    /*
     * DbEdit - Edit this object in database
     * 
     * @param sess Hibernate Session object
     * 
     * @throws Exception (HibernateException) with error
     * 
     * @return true if action done or Exception if not
     */
    public boolean DbEdit(Session sess) throws Exception
    {

        Transaction tx = sess.beginTransaction();

        MealGroupH ch = (MealGroupH) sess.get(MealGroupH.class, new Long(this.getId()));

        ch.setName(this.getName());
        ch.setDescription(this.getDescription());
        ch.setParent_id(this.getParent_id());
        ch.setName_i18n(this.getName_i18n());

        sess.update(ch);
        tx.commit();

        return true;

    }

    /*
     * DbDelete - Delete this object in database
     * 
     * @param sess Hibernate Session object
     * 
     * @throws Exception (HibernateException) with error
     * 
     * @return true if action done or Exception if not
     */
    public boolean DbDelete(Session sess) throws Exception
    {

        Transaction tx = sess.beginTransaction();

        MealGroupH ch = (MealGroupH) sess.get(MealGroupH.class, new Long(this.getId()));

        sess.delete(ch);
        tx.commit();

        return true;

    }

    /*
     * DbHasChildren - Shows if this entry has any children object, this is
     * needed for delete
     * 
     * 
     * @param sess Hibernate Session object
     * 
     * @throws Exception (HibernateException) with error
     * 
     * @return true if action done or Exception if not
     */
    public boolean DbHasChildren(Session sess) throws Exception
    {
        Query q = sess.createQuery("select pst from ggc.core.db.hibernate.MealH as pst where pst.meal_group_id="
                + getId());
        int size = q.list().size();

        if (size > 0)
            return true;

        q = sess.createQuery("select pst from ggc.core.db.hibernate.MealGroupH as pst where pst.parent_id=" + getId());
        size = q.list().size();

        if (size > 0)
            return true;
        else
            return false;

    }

    /*
     * DbGet - Loads this object. Id must be set.
     * 
     * 
     * @param sess Hibernate Session object
     * 
     * @throws Exception (HibernateException) with error
     * 
     * @return true if action done or Exception if not
     */
    public boolean DbGet(Session sess) throws Exception
    {

        MealGroupH ch = (MealGroupH) sess.get(MealGroupH.class, new Long(this.getId()));

        this.setId(ch.getId());
        this.setName(ch.getName());
        this.setDescription(ch.getDescription());
        this.setParent_id(ch.getParent_id());
        this.setName_i18n(ch.getName_i18n());

        return true;
    }

    /*
     * getObjectName - returns name of DatabaseObject
     * 
     * @return name of object (not Hibernate object)
     */
    public String getObjectName()
    {
        return "Meal Group";
    }

    /*
     * isDebugMode - returns debug mode of object
     * 
     * @return true if object in debug mode
     */
    public boolean isDebugMode()
    {
        return debug;
    }

    /*
     * getAction - returns action that should be done on object 0 = no action 1
     * = add action 2 = edit action 3 = delete action This is used mainly for
     * objects, contained in lists and dialogs, used for processing by higher
     * classes (classes calling selectors, wizards, etc...
     * 
     * @return number of action
     */
    public int getAction()
    {
        return 0;
    }

    // ---
    // --- BackupRestoreObject
    // ---

    /*
     * getTargetName
     */
    public String getTargetName()
    {
        return ic.getMessage("MEAL_GROUPS");
    }

    /*
     * getChildren
     */
    public ArrayList<CheckBoxTreeNodeInterface> getChildren()
    {
        return null;
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