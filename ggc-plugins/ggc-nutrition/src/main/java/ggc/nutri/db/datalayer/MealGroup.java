package ggc.nutri.db.datalayer;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.Transaction;

import com.atech.db.hibernate.DatabaseObjectHibernate;
import com.atech.db.hibernate.transfer.BackupRestoreObject;
import com.atech.graphics.components.tree.CheckBoxTreeNodeInterface;
import com.atech.i18n.I18nControlAbstract;

import ggc.core.db.hibernate.food.MealGroupH;
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
 *  Filename:     MealGroup
 *  Description:  This is DataLayer file (data file, with methods to work with database or in 
 *                this case Hibernate). This one is used for MealGroupH. 
 *                This one is also BackupRestoreObject.
 * 
 *  Author: andyrozman {andy@atech-software.com}  
 */

public class MealGroup extends MealGroupH implements DatabaseObjectHibernate, BackupRestoreObject, DAOObject
{

    private static final long serialVersionUID = 1123099194144366632L;
    private boolean debug = false;
    private int object_load_status = 0;
    boolean selected = false;
    I18nControlAbstract ic = null; // DataAccessNutri.getInstance().
    boolean meal_empty = false;

    /**
     * Meal Group: Meals
     */
    public static final int MEAL_GROUP_MEALS = 1;

    /**
     * Meal Group: Nutrition
     */
    public static final int MEAL_GROUP_NUTRITION = 2;

    /**
     * Children: Group
     */
    public List<MealGroup> children_group = null; // new
                                                  // ArrayList<MealGroup>();

    /**
     * Children: Meals
     */
    public List<Meal> children_meal = null; // new ArrayList<Meal>();

    /**
     * Children: All
     */
    public List<Object> children = null; // new ArrayList<Object>();


    /**
     * Constructor
     * 
     * @param ic
     */
    public MealGroup(I18nControlAbstract ic)
    {
        this.ic = ic;
        this.meal_empty = true;
    }


    /**
     * Constructor
     * 
     * @param meal_empty
     */
    public MealGroup(boolean meal_empty)
    {
        this.meal_empty = meal_empty;
        ic = DataAccessNutri.getInstance().getI18nControlInstance();
    }


    /**
     * Constructor
     */
    public MealGroup()
    {
        ic = DataAccessNutri.getInstance().getI18nControlInstance();
        this.setId(0);
        this.setName("");
        this.setDescription("");
        this.setNameI18n("");
        this.setParentId(-1);
    }


    /**
     * Constructor
     * 
     * @param ch
     */
    public MealGroup(MealGroupH ch)
    {
        ic = DataAccessNutri.getInstance().getI18nControlInstance();
        this.setId(ch.getId());
        this.setName(ch.getName());
        this.setDescription(ch.getDescription());
        this.setNameI18n(ch.getNameI18n());
        this.setParentId(ch.getParentId());
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
    public void addChild(MealGroup fg)
    {
        // checkLoadStatus();

        if (this.children == null)
        {
            this.children_group = new ArrayList<MealGroup>();
            this.children_meal = new ArrayList<Meal>();
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
    public void addChild(Meal fd)
    {
        if (this.children_meal == null)
        {
            if (this.children_group == null)
            {
                this.children_group = new ArrayList<MealGroup>();
            }

            if (this.children == null)
            {
                this.children = new ArrayList<Object>();
            }

            this.children_meal = new ArrayList<Meal>();
        }

        // checkLoadStatus();
        this.children_meal.add(fd);
        children.add(fd);
    }


    /**
     * Remove Child
     * 
     * @param fg
     */
    public void removeChild(MealGroup fg)
    {
        // checkLoadStatus();
        children_group.remove(fg);
        children.remove(fg);
    }


    /**
     * Remove Child
     * 
     * @param fd
     */
    public void removeChild(Meal fd)
    {
        // checkLoadStatus();
        children.remove(fd);
        children_meal.remove(fd);
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
     * Get Short Description
     * 
     * @return
     */
    public String getShortDescription()
    {
        return this.getName();
    }


    /**
     * Get Long Description
     * @return
     */
    public String getLongDescription()
    {
        return "MealGroup [id=" + this.getId() + ",name=" + this.getName() + ",parent_id=" + this.getParentId() + "]";
    }


    /**
     * To String
     * 
     * @see MealGroupH#toString()
     */
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

        MealGroupH ch = new MealGroupH();

        // ch.setId(this.getId());
        ch.setName(this.getName());
        ch.setDescription(this.getDescription());
        ch.setParentId(this.getParentId());
        ch.setNameI18n(this.getNameI18n());

        Long id = (Long) sess.save(ch);

        tx.commit();

        this.setId(id);

        return "" + id.longValue();

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

        MealGroupH ch = (MealGroupH) sess.get(MealGroupH.class, new Long(this.getId()));

        ch.setName(this.getName());
        ch.setDescription(this.getDescription());
        ch.setParentId(this.getParentId());
        ch.setNameI18n(this.getNameI18n());

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

        MealGroupH ch = (MealGroupH) sess.get(MealGroupH.class, new Long(this.getId()));

        sess.delete(ch);
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
        Query q = sess
                .createQuery("select pst from ggc.core.db.hibernate.MealH as pst where pst.meal_group_id=" + getId());
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


    /**
     * DbGet - Loads this object. Id must be set.
     * 
     * @param sess Hibernate Session object
     * @throws Exception (HibernateException) with error
     * @return true if action done or Exception if not
     */
    public boolean DbGet(Session sess) throws Exception
    {

        MealGroupH ch = (MealGroupH) sess.get(MealGroupH.class, new Long(this.getId()));

        this.setId(ch.getId());
        this.setName(ch.getName());
        this.setDescription(ch.getDescription());
        this.setParentId(ch.getParentId());
        this.setNameI18n(ch.getNameI18n());

        return true;
    }


    /**
     * getObjectName - returns name of DatabaseObject
     * 
     * @return name of object (not Hibernate object)
     */
    public String getObjectName()
    {
        return "Meal Group";
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


    /**
     * Load Children
     */
    public void loadChildren()
    {
        GGCDbCache cache = DataAccessNutri.getInstance().getDbCache();

        this.children_group = cache.getChildrenMealGroup(3, this.getId());
        this.children_meal = cache.getChildrenMeals(3, this.getId());

        if (children == null)
        {
            this.children = new ArrayList<Object>();
        }

        this.children.addAll(this.children_group);
        this.children.addAll(this.children_meal);

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
    // --- BackupRestoreObject
    // ---

    /**
     * Get Target Name
     */
    public String getTargetName()
    {
        return ic.getMessage("MEAL_GROUPS");
    }


    /**
     * Get Class Name
     * 
     * @see com.atech.db.hibernate.transfer.BackupRestoreBase#getClassName()
     */
    public String getClassName()
    {
        return "ggc.core.db.hibernate.food.MealGroupH";
    }


    /**
     * Get Children
     * 
     * @return 
     */
    public ArrayList<CheckBoxTreeNodeInterface> getChildren()
    {
        return null;
    }


    /**
     * Is Selected
     */
    public boolean isSelected()
    {
        return selected;
    }


    /**
     * Set Selected
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
     * Get Object Unique Id - get id of object
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
