package ggc.nutri.db.datalayer;

import java.util.ArrayList;
import java.util.Map;

import org.hibernate.Session;
import org.hibernate.Transaction;

import com.atech.db.hibernate.DatabaseObjectHibernate;
import com.atech.db.hibernate.transfer.BackupRestoreObject;
import com.atech.graphics.components.tree.CheckBoxTreeNodeInterface;
import com.atech.i18n.I18nControlAbstract;

import ggc.core.db.hibernate.food.MealH;
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
 *  Filename:     Meal
 *  Description:  This is DataLayer file (data file, with methods to work with database or in 
 *                this case Hibernate). This one is used for MealH. 
 *                This one is also BackupRestoreObject.
 * 
 *  Author: andyrozman {andy@atech-software.com}  
 */

public class Meal extends MealH implements DatabaseObjectHibernate, BackupRestoreObject, DAOObject
{

    private static final long serialVersionUID = 3028931713064051809L;
    private boolean debug = false;
    ArrayList<MealPart> parts_lst = new ArrayList<MealPart>();
    // ArrayList<MealPart> parts_lst = new ArrayList<MealPart>();

    boolean selected = false;
    I18nControlAbstract ic = null; // DataAccessNutri.getInstance().
                                   // getI18nControlInstance();
    boolean meal_empty = false;


    /**
     * Constructor
     * 
     * @param ic
     */
    public Meal(I18nControlAbstract ic)
    {
        this.ic = ic;
        this.meal_empty = true;
    }


    /**
     * Constructor
     * 
     * @param meal_empty
     */
    public Meal(boolean meal_empty)
    {
        this.meal_empty = meal_empty;
        ic = DataAccessNutri.getInstance().getI18nControlInstance();
    }


    /**
     * Constructor
     */
    public Meal()
    {
        this.setId(0L);
        this.setName("");
        this.setNameI18n("");
        this.setGroupId(0);
        this.setDescription("");
        this.setParts("");
        this.setNutritions("");
        this.setExtended("");
        this.setComment("");

        ic = DataAccessNutri.getInstance().getI18nControlInstance();

        loadParts();
        loadValues();
    }


    /**
     * Constructor
     * 
     * @param ch
     */
    public Meal(MealH ch)
    {
        ic = DataAccessNutri.getInstance().getI18nControlInstance();

        this.setId(ch.getId());
        this.setName(ch.getName());
        this.setNameI18n(ch.getNameI18n());
        this.setGroupId(ch.getGroupId());
        this.setDescription(ch.getDescription());
        this.setParts(ch.getParts());
        this.setNutritions(ch.getNutritions());
        this.setExtended(ch.getExtended());
        this.setComment(ch.getComment());

        // loadParts();
        // loadValues();
    }


    private void loadParts()
    {
        /*
         * StringTokenizer strtok = new StringTokenizer(this.getParts(), ";");
         * while (strtok.hasMoreTokens())
         * {
         * }
         */
    }


    private void loadValues()
    {
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
     * To String
     * 
     * @see MealH#toString()
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

        MealH ch = new MealH();

        ch.setId(this.getId());
        ch.setName(this.getName());
        ch.setNameI18n(this.getNameI18n());
        ch.setGroupId(this.getGroupId());
        ch.setDescription(this.getDescription());
        ch.setParts(this.getParts());
        ch.setNutritions(this.getNutritions());
        ch.setExtended(this.getExtended());
        ch.setComment(this.getComment());

        Long id = (Long) sess.save(ch);

        tx.commit();

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

        MealH ch = (MealH) sess.get(MealH.class, new Long(this.getId()));

        ch.setId(this.getId());
        ch.setName(this.getName());
        ch.setNameI18n(this.getNameI18n());
        ch.setGroupId(this.getGroupId());
        ch.setDescription(this.getDescription());
        ch.setParts(this.getParts());
        ch.setNutritions(this.getNutritions());
        ch.setExtended(this.getExtended());
        ch.setComment(this.getComment());

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
        /*
         * Transaction tx = sess.beginTransaction();
         * MealFoodDescriptionH ch =
         * (MealFoodDescriptionH)sess.get(MealFoodDescriptionH.class, new
         * Long(this.getId()));
         * sess.delete(ch); tx.commit();
         * return true;
         */
        return false;

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
        // System.out.println("Not implemented:
        // FoodDescription::DbHasChildren");
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

        MealH ch = (MealH) sess.get(MealH.class, new Long(this.getId()));

        this.setId(ch.getId());
        this.setName(ch.getName());
        this.setNameI18n(ch.getNameI18n());
        this.setGroupId(ch.getGroupId());
        this.setDescription(ch.getDescription());
        this.setParts(ch.getParts());
        this.setNutritions(ch.getNutritions());
        this.setExtended(ch.getExtended());
        this.setComment(ch.getComment());

        return true;
    }


    /**
     * getObjectName - returns name of DatabaseObject
     * 
     * @return name of object (not Hibernate object)
     */
    public String getObjectName()
    {
        return "Meal";
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
     * Get Target Name
     */
    public String getTargetName()
    {
        return ic.getMessage("MEALS");
    }


    /**
     * Get Class Name
     * 
     * @see com.atech.db.hibernate.transfer.BackupRestoreBase#getClassName()
     */
    public String getClassName()
    {
        return "ggc.core.db.hibernate.food.MealH";
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
     * Has Children
     * 
     * @return 
     */
    public boolean hasChildren()
    {
        return false;
    }


    /**
     * Is Selected
     * 
     * @return true if selected
     */
    public boolean isSelected()
    {
        return selected;
    }


    /**
     * Set Selected
     * 
     * @param _selected true if selected
     */
    public void setSelected(boolean _selected)
    {
        this.selected = _selected;
    }


    /**
     * Is Object Collection
     * 
     * @return true if it has children
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
