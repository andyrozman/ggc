package ggc.nutri.db.datalayer;

import java.util.ArrayList;
import java.util.Map;

import org.hibernate.Session;
import org.hibernate.Transaction;

import com.atech.db.hibernate.DatabaseObjectHibernate;
import com.atech.db.hibernate.transfer.BackupRestoreObject;
import com.atech.graphics.components.tree.CheckBoxTreeNodeInterface;
import com.atech.i18n.I18nControlAbstract;

import ggc.core.db.hibernate.food.FoodGroupH;
import ggc.core.db.hibernate.food.FoodUserGroupH;
import ggc.nutri.db.GGCDbCache;
import ggc.nutri.util.DataAccessNutri;

/**
 *  Application:   GGC - GNU Gluco Control
 *
 *  See AUTHORS for copyright information.
 * 
 *  This program is free software; you can redistribute it and/or modify it under
 *  the terms of the GNU General Public License as published by the Free Software
 *  Foundation; either version 2 of the License, or (at your option) any later
 *  version.
 * 
 *  This program is distributed in the hope that it will be useful, but WITHOUT
 *  ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 *  FOR A PARTICULAR PURPOSE. See the GNU General Public License for more
 *  details.
 * 
 *  You should have received a copy of the GNU General Public License along with
 *  this program; if not, write to the Free Software Foundation, Inc., 59 Temple
 *  Place, Suite 330, Boston, MA 02111-1307 USA
 * 
 *  Filename:     FoodGroup
 *  Description:  This is DataLayer file (data file, with methods to work with database or in 
 *                this case Hibernate). This one is used for FoodGroupH and FoodUserGroupH. 
 *                This one is also BackupRestoreObject.
 * 
 *  Author: andyrozman {andy@atech-software.com}  
 */

public class FoodGroup implements DatabaseObjectHibernate, BackupRestoreObject
{

    private boolean debug = false;

    FoodGroupH group_db1 = null;
    FoodUserGroupH group_db2 = null;

    int group_type = 0;
    long id;

    private boolean selected = false;

    /**
     * Children - Groups
     */
    public ArrayList<FoodGroup> children_group = null; // new
                                                       // ArrayList<FoodGroup>();

    /**
     * Children - All
     */
    public ArrayList<Object> children = null; // new ArrayList<Object>();

    /**
     * Children - Foods
     */
    public ArrayList<FoodDescription> children_food = null; // new
                                                            // ArrayList<Meal>();

    // public ArrayList<MealGroup> children_group = null; //new
    // ArrayList<MealGroup>();
    // public ArrayList<Meal> children_meal = null; //new ArrayList<Meal>();

    // public ArrayList<Object> children = null; //new ArrayList<Object>();

    private int object_load_status = 0;

    boolean empty = false;
    I18nControlAbstract ic = null; // DataAccessNutri.getInstance().


    // getI18nControlInstance();

    /**
     * Constructor
     * 
     * @param ic
     */
    public FoodGroup(I18nControlAbstract ic)
    {
        this.empty = true;
        this.ic = ic;
    }


    /**
     * Constructor
     * 
     * @param type
     */
    public FoodGroup(int type)
    {
        ic = DataAccessNutri.getInstance().getI18nControlInstance();
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


    /**
     * Constructor
     * 
     * @param ch
     */
    public FoodGroup(FoodGroupH ch)
    {
        this.group_db1 = ch;
        group_type = 1;
    }


    /**
     * Constructor
     * 
     * @param ch
     */
    public FoodGroup(FoodUserGroupH ch)
    {
        this.group_db2 = ch;
        group_type = 2;
    }


    /**
     * Get Group Type
     * 
     * @return
     */
    public int getGroupType()
    {
        return this.group_type;
    }


    /**
     * Get Hibernate Object
     * 
     * @return
     */
    public Object getHibernateObject()
    {
        if (group_type == 1)
            return this.group_db1;
        else
            return this.group_db2;
    }


    /**
     * Get Short Description
     * 
     * @return
     */
    public String getShortDescription()
    {
        return this.getDescription();
    }


    /**
     * Get Id
     * 
     * @return
     */
    public long getId()
    {
        if (this.group_db1 == null && this.group_db2 == null)
            return this.id;

        if (group_type == 1)
            return this.group_db1.getId();
        else
            return this.group_db2.getId();
    }


    /**
     * Set Id
     * 
     * @param id
     */
    public void setId(int id)
    {
        setId((long) id);
    }


    /**
     * Set Id
     * 
     * @param id
     */
    public void setId(long id)
    {
        if (this.group_db1 == null && this.group_db2 == null)
        {
            this.id = id;
        }
        else
        {
            if (group_type == 1)
            {
                this.group_db1.setId(id);
            }
            else
            {
                this.group_db2.setId(id);
            }
        }
    }


    /**
     * Get Name
     * 
     * @return
     */
    public String getName()
    {
        if (group_type == 1)
            return this.group_db1.getName();
        else
            return this.group_db2.getName();
    }


    /**
     * Set Name
     * 
     * @param name
     */
    public void setName(String name)
    {
        if (group_type == 1)
        {
            this.group_db1.setName(name);
        }
        else
        {
            this.group_db2.setName(name);
        }
    }


    /**
     * Get Name I18n
     * 
     * @return
     */
    public String getName_i18n()
    {
        if (group_type == 1)
            return this.group_db1.getName_i18n();
        else
            return this.group_db2.getName_i18n();
    }


    /**
     * Set Name I18n
     * 
     * @param name
     */
    public void setName_i18n(String name)
    {
        if (group_type == 1)
        {
            this.group_db1.setName_i18n(name);
        }
        else
        {
            this.group_db2.setName_i18n(name);
        }
    }


    /**
     * Set Description
     * 
     * @param name
     */
    public void setDescription(String name)
    {
        if (group_type == 1)
        {
            this.group_db1.setDescription(name);
        }
        else
        {
            this.group_db2.setDescription(name);
        }
    }


    /**
     * Get Description
     * 
     * @return
     */
    public String getDescription()
    {
        if (group_type == 1)
            return this.group_db1.getDescription();
        else
            return this.group_db2.getDescription();
    }


    /**
     * Get Group Children Count
     * 
     * @return
     */
    public int getGroupChildrenCount()
    {
        checkLoadStatus();
        return this.children_group.size();
    }


    /**
     * Get Child Count
     * 
     * @return
     */
    public int getChildCount()
    {
        checkLoadStatus();
        return this.children.size();
    }


    /**
     * Has Group Children
     * 
     * @return
     */
    public boolean hasGroupChildren()
    {
        checkLoadStatus();
        return getGroupChildrenCount() != 0;
    }


    /**
     * Has Children
     * 
     * @return 
     */
    public boolean hasChildren()
    {
        checkLoadStatus();
        return getChildCount() != 0;
    }


    /**
     * Add Child
     * 
     * @param fg
     */
    public void addChild(FoodGroup fg)
    {
        // checkLoadStatus();

        if (this.children == null)
        {
            this.children_group = new ArrayList<FoodGroup>();
            this.children_food = new ArrayList<FoodDescription>();
            this.children = new ArrayList<Object>();
        }

        children_group.add(fg);
        children.add(fg);
    }


    /**
     * Add Child
     * 
     * @param fd
     */
    public void addChild(FoodDescription fd)
    {
        if (this.children_food == null)
        {
            if (this.children_group == null)
            {
                this.children_group = new ArrayList<FoodGroup>();
            }

            if (this.children == null)
            {
                this.children = new ArrayList<Object>();
            }

            this.children_food = new ArrayList<FoodDescription>();
        }

        children_food.add(fd);
        children.add(fd);
    }


    /**
     * Remove Child
     * 
     * @param fg
     */
    public void removeChild(FoodGroup fg)
    {
        children_group.remove(fg);
        children.remove(fg);
    }


    /**
     * Remove Child
     * 
     * @param fd
     */
    public void removeChild(FoodDescription fd)
    {
        children.remove(fd);
        children_food.remove(fd);
    }


    /**
     * Get Group Child
     * 
     * @param index
     * @return
     */
    public Object getGroupChild(int index)
    {
        checkLoadStatus();
        return this.children_group.get(index);
    }


    /**
     * Get Child
     * 
     * @param index
     * @return
     */
    public Object getChild(int index)
    {
        checkLoadStatus();
        return this.children.get(index);
    }


    /**
     * Find Group Child
     * 
     * @param child
     * @return
     */
    public int findGroupChild(Object child)
    {
        checkLoadStatus();
        return this.children_group.indexOf(child);
    }


    /**
     * Find Child
     * 
     * @param child
     * @return
     */
    public int findChild(Object child)
    {
        checkLoadStatus();
        return this.children.indexOf(child);
    }


    /**
     * Get Parent Id
     * 
     * @return
     */
    public long getParentId()
    {
        if (this.group_type == 1)
            return 0L;
        else
            return this.group_db2.getParent_id();
    }


    /**
     * Set Parent Id
     * 
     * @param parent_id
     */
    public void setParentId(long parent_id)
    {
        if (this.group_type == 2)
        {
            this.group_db2.setParent_id(parent_id);
        }
    }


    /**
     * To String
     * 
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString()
    {
        if (this.empty)
            return this.getTargetName();
        else
            return this.getName();
    }


    /**
     * Get Long Description
     * 
     * @return
     */
    public String getLongDescription()
    {
        return "FoodGroup [id=" + this.getId() + ",name=" + this.getName() + ",parent_id=" + this.getParentId() + "]";
    }


    /**
     * Load Children
     */
    public void loadChildren()
    {
        GGCDbCache cache = DataAccessNutri.getInstance().getDbCache();

        this.children_group = cache.getChildrenFoodGroup(this.group_type, this.getId());
        this.children_food = cache.getChildrenFoods(this.group_type, this.getId());

        if (children == null)
        {
            this.children = new ArrayList<Object>();
        }

        // if (children_group!=null)
        this.children.addAll(this.children_group);
        this.children.addAll(this.children_food);

        this.object_load_status = GGCDbCache.OBJECT_LOADED_STATUS_CHILDREN_LOADED;
        // if (cache.getChildrenFoodGroup(type, parent_id))

    }


    /**
     * Check Load Status
     */
    public void checkLoadStatus()
    {
        if (this.object_load_status != GGCDbCache.OBJECT_LOADED_STATUS_CHILDREN_LOADED)
        {
            loadChildren();
        }
    }


    /**
     * Get Load Status
     * 
     * @return
     */
    public int getLoadStatus()
    {
        return this.object_load_status;
    }


    // ---
    // --- DatabaseObjectHibernate
    // ---

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
        Long _id;

        if (this.group_type == 1)
        {
            _id = (Long) sess.save(this.group_db1);
            this.group_db1.setId(_id);
        }
        else
        {
            _id = (Long) sess.save(this.group_db2);
            this.group_db2.setId(_id);
        }

        tx.commit();

        return "" + _id.longValue();
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


    /**
     * DbHasChildren - Shows if this entry has any children object, this is
     * needed for delete
     * 
     * @param sess Hibernate Session object
     * @throws Exception (HibernateException) with error
     * @return true if action done or Exception if not
     */
    public boolean DbHasChildren(Session sess) throws Exception
    {
        /**
         * System.out.println("Not implemented: FoodGroup::DbHasChildren");
         * return true;
         */
        return true;
    }


    /**
     * DbGet - Loads this object. Id must be set.
     * 
     * @param sess Hibernate Session object
     * @throws Exception (HibernateException) with error
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

        return false;
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

    /**
     * getTargetName
     */
    public String getTargetName()
    {
        return ic.getMessage("USER_FOOD_GROUPS");
    }


    /**
     * Get Class Name
     * 
     * @see com.atech.db.hibernate.transfer.BackupRestoreBase#getClassName()
     */
    public String getClassName()
    {
        return "ggc.core.db.hibernate.food.FoodUserGroupH";
    }


    /**
     * getChildren
     * 
     * @return 
     */
    public ArrayList<CheckBoxTreeNodeInterface> getChildren()
    {
        return null;
    }


    /**
     * isSelected
     */
    public boolean isSelected()
    {
        return selected;
    }


    /**
     * setSelected
     */
    public void setSelected(boolean newValue)
    {
        this.selected = newValue;
    }


    /**
     * Is Collection
     * 
     * @see com.atech.db.hibernate.transfer.BackupRestoreBase#isCollection()
     */
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

    /**
     * Table Version
     */
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
     * @return
     */
    public String dbExportHeader()
    {
        return this.dbExportHeader(this.TABLE_VERSION);
    }


    /**
     * {@inheritDoc}
     */
    public void dbImport(int tableVersion, String valueEntry) throws Exception
    {
        dbImport(tableVersion, valueEntry, (Object[]) null);
    }


    /**
     * {@inheritDoc}
     */
    public void dbImport(int tableVersion, String valueEntry, Object[] parameters) throws Exception
    {
    }


    /**
     * {@inheritDoc}
     */
    public void dbImport(int tableVersion, String valueEntry, Map<String, String> headers) throws Exception
    {
    }


    /**
     * {@inheritDoc}
     */
    public boolean isNewImport()
    {
        return false;
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


    /**
     * Has To Be Clean - if table needs to be cleaned before import
     * 
     * @return true if we need to clean
     */
    public boolean hasToBeCleaned()
    {
        return true;
    }


    /**
     * Get Node Children
     */
    public ArrayList<CheckBoxTreeNodeInterface> getNodeChildren()
    {
        return null;
    }


    /**
     * Has Node Children
     */
    public boolean hasNodeChildren()
    {
        return false;
    }

}
