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
 * Filename: FoodGroup Purpose: This is datalayer file (data file, with methods
 * to work with database or in this case Hibernate). This one is used for
 * description of food groups.
 * 
 * Author: andyrozman {andy@atech-software.com}
 */

package ggc.core.db.datalayer;

import ggc.core.db.hibernate.FoodGroupH;
import ggc.core.db.hibernate.FoodUserGroupH;
import ggc.core.util.DataAccess;

import java.util.ArrayList;

import com.atech.db.hibernate.DatabaseObjectHibernate;
import com.atech.db.hibernate.transfer.BackupRestoreObject;
import com.atech.graphics.components.tree.CheckBoxTreeNodeInterface;
import com.atech.i18n.I18nControlAbstract;

import org.hibernate.Session;
import org.hibernate.Transaction;

public class FoodGroup implements DatabaseObjectHibernate, BackupRestoreObject
// extends FoodGroupH implements DatabaseObjectHibernate
{

    public boolean debug = false;

    // ggc.core.db.hibernate.FoodGroupH
    // ggc.core.db.hibernate.FoodUserGroupH;

    FoodGroupH group_db1 = null;
    FoodUserGroupH group_db2 = null;

    int group_type = 0;
    long id;

    private boolean selected = false;

    ArrayList<FoodGroup> children_group = new ArrayList<FoodGroup>();
    ArrayList<Object> children = new ArrayList<Object>();

    boolean empty = false;
    I18nControlAbstract ic = null; // DataAccess.getInstance().

    // getI18nControlInstance();

    /*
     * public FoodGroup() { this.setId(0); this.setDescription(""); }
     */

    public FoodGroup(I18nControlAbstract ic)
    {
        this.empty = true;
        this.ic = ic;
    }

    public FoodGroup(int type)
    {
        ic = DataAccess.getInstance().getI18nControlInstance();
        group_type = type;

        if (type == 1)
        {
            this.group_db1 = new FoodGroupH();
            // this.group_db1
        }
        else
        {
            this.group_db2 = new FoodUserGroupH();
            this.group_db2.setParent_id(-1);
        }

    }

    public FoodGroup(FoodGroupH ch)
    {
        this.group_db1 = ch;
        group_type = 1;
    }

    public FoodGroup(FoodUserGroupH ch)
    {
        this.group_db2 = ch;
        group_type = 2;
    }

    public int getGroupType()
    {
        return this.group_type;
    }

    public Object getHibernateObject()
    {
        if (group_type == 1)
            return this.group_db1;
        else
            return this.group_db2;
    }

    public String getShortDescription()
    {
        return this.getDescription();
    }

    public long getId()
    {
        if ((this.group_db1 == null) && (this.group_db2 == null))
        {
            return this.id;
        }

        if (group_type == 1)
            return this.group_db1.getId();
        else
            return this.group_db2.getId();
    }

    public void setId(int id)
    {
        setId((long) id);
    }

    public void setId(long id)
    {
        if ((this.group_db1 == null) && (this.group_db2 == null))
            this.id = id;
        else
        {
            if (group_type == 1)
                this.group_db1.setId(id);
            else
                this.group_db2.setId(id);
        }
    }

    public String getName()
    {
        if (group_type == 1)
            return this.group_db1.getName();
        else
            return this.group_db2.getName();
    }

    public void setName(String name)
    {
        if (group_type == 1)
            this.group_db1.setName(name);
        else
            this.group_db2.setName(name);
    }

    public String getName_i18n()
    {
        if (group_type == 1)
            return this.group_db1.getName_i18n();
        else
            return this.group_db2.getName_i18n();
    }

    public void setName_i18n(String name)
    {
        if (group_type == 1)
            this.group_db1.setName_i18n(name);
        else
            this.group_db2.setName_i18n(name);
    }

    public void setDescription(String name)
    {
        if (group_type == 1)
            this.group_db1.setDescription(name);
        else
            this.group_db2.setDescription(name);
    }

    public String getDescription()
    {
        if (group_type == 1)
            return this.group_db1.getDescription();
        else
            return this.group_db2.getDescription();
    }

    public int getGroupChildrenCount()
    {
        return this.children_group.size();
    }

    public int getChildCount()
    {
        return this.children.size();
    }

    public boolean hasGroupChildren()
    {
        return (getGroupChildrenCount() != 0);
    }

    public boolean hasChildren()
    {
        return (getChildCount() != 0);
    }

    public void addChild(FoodGroup fg)
    {
        children_group.add(fg);
        children.add(fg);
    }

    public void addChild(FoodDescription fd)
    {
        children.add(fd);
    }

    public void removeChild(FoodGroup fg)
    {
        children_group.remove(fg);
        children.remove(fg);
    }

    public void removeChild(FoodDescription fd)
    {
        children.remove(fd);
    }

    public Object getGroupChild(int index)
    {
        return this.children_group.get(index);
    }

    public Object getChild(int index)
    {
        return this.children.get(index);
    }

    public int findGroupChild(Object child)
    {
        return this.children_group.indexOf(child);
    }

    public int findChild(Object child)
    {
        return this.children.indexOf(child);
    }

    public long getParentId()
    {
        if (this.group_type == 1)
            return 0L;
        else
            return this.group_db2.getParent_id();
    }

    public void setParentId(long parent_id)
    {
        if (this.group_type == 2)
            this.group_db2.setParent_id(parent_id);
    }

    @Override
    public String toString()
    {
        if (this.empty)
            return this.getTargetName();
        else
            return this.getName();
    }

    public String getLongDescription()
    {
        return "FoodGroup [id=" + this.getId() + ",name=" + this.getName() + ",parent_id=" + this.getParentId() + "]";
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
        Long id;

        if (this.group_type == 1)
        {
            id = (Long) sess.save(this.group_db1);
            this.group_db1.setId(id);
        }
        else
        {
            id = (Long) sess.save(this.group_db2);
            this.group_db2.setId(id);
        }

        tx.commit();

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

        if (this.group_type == 1)
        {
            sess.update(this.group_db1);
        }
        else
        {
            sess.update(this.group_db2);
        }

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

        if (this.group_type == 1)
        {
            sess.delete(this.group_db1);
        }
        else
        {
            sess.delete(this.group_db2);
        }

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
        /*
         * System.out.println("Not implemented: FoodGroup::DbHasChildren");
         * return true;
         */
        return true;
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
        if (this.group_type == 1)
        {
            this.group_db1 = (FoodGroupH) sess.get(FoodGroupH.class, new Long(this.getId()));
            sess.update(this.group_db1);
        }
        else
        {
            sess.update(this.group_db2);
        }

        /*
         * FoodGroupH ch = (FoodGroupH)sess.get(FoodGroupH.class, new
         * Long(this.getId()));
         * 
         * this.setId(ch.getId()); this.setDescription(ch.getDescription());
         * this.setDescription_i18n(ch.getDescription_i18n());
         * 
         * return true;
         */

        return false;
    }

    /*
     * getObjectName - returns name of DatabaseObject
     * 
     * @return name of object (not Hibernate object)
     */
    public String getObjectName()
    {
        return "Food Group";
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
        return ic.getMessage("USER_FOOD_GROUPS");
    }

    public String getClassName()
    {
        return "ggc.core.db.hibernate.FoodUserGroupH";
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

    
    /**
     * getObjectUniqueId - get id of object
     * @return unique object id
     */
    public String getObjectUniqueId()
    {
        return "" + this.getId();
    }

    
    public int TABLE_VERSION = 1;
    
    
    /**
     * getTableVersion - returns version of table
     * 
     * @return version information
     */
    public int getTableVersion()
    {
        return this.TABLE_VERSION;
    }
    
    
    /**
     * dbExport - returns export String, for current version 
     *
     * @return line that will be exported
     * @throws Exception if export for table is not supported
     */
    public String dbExport(int table_version) throws Exception
    {
        // TODO
        return null;
    }

    
    /**
     * dbExport - returns export String, for current version 
     *
     * @return line that will be exported
     * @throws Exception if export for table is not supported
     */
    public String dbExport() throws Exception
    {
        return dbExport(this.TABLE_VERSION);
    }
    
    
    /**
     * dbExportHeader - header for export file
     * 
     * @param table_version
     * @return
     */
    public String dbExportHeader(int table_version)
    {
        // TODO
        return null;
    }
    

    /**
     * dbExportHeader - header for export file
     * 
     * @param table_version
     * @return
     */
    public String dbExportHeader()
    {
        return this.dbExportHeader(this.TABLE_VERSION);
    }
    
    
    /**
     * dbImport - processes input entry to right fields
     * 
     * @param table_version version of table
     * @param value_entry whole import line
     * @throws Exception if import for selected table version is not supported or it fails
     */
    public void dbImport(int table_version, String value_entry) throws Exception
    {
        // TODO
    }
    
    
    
    /**
     * getBackupFile - name of backup file (base part)
     * 
     * @return
     */
    public String getBackupFile()
    {
        // TODO
        return "DayValueH";
    }
    
    /**
     * getBackupClassName - name of class which will be updated/restored
     * 
     * @return
     */
    public String getBackupClassName()
    {
        // TODO
        return "";
    }
    
    
}
