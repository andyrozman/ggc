package ggc.nutri.db.datalayer;

import java.util.ArrayList;
import java.util.Map;

import org.hibernate.Session;
import org.hibernate.Transaction;

import com.atech.db.hibernate.DatabaseObjectHibernate;
import com.atech.db.hibernate.transfer.BackupRestoreObject;
import com.atech.graphics.components.tree.CheckBoxTreeNodeInterface;
import com.atech.i18n.I18nControlAbstract;

import ggc.core.db.hibernate.food.FoodDescriptionH;
import ggc.core.db.hibernate.food.FoodUserDescriptionH;
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
 *  Filename:     FoodDescription
 *  Description:  This is DataLayer file (data file, with methods to work with database or in 
 *                this case Hibernate). This one is used for FoodDescriptionH and FoodUserDescriptionH. 
 *                This one is also BackupRestoreObject.
 * 
 *  Author: andyrozman {andy@atech-software.com}  
 */

public class FoodDescription implements DatabaseObjectHibernate, BackupRestoreObject, DAOObject
{

    private boolean debug = false;
    private int type = 1;

    private FoodDescriptionH m_food_desc1;
    private FoodUserDescriptionH m_food_desc2;
    private long id;

    boolean selected = false;
    I18nControlAbstract ic = null; // DataAccessNutri.getInstance().
    // getI18nControlInstance();
    boolean food_empty = false;


    /**
     * Constructor
     * 
     * @param ic
     */
    public FoodDescription(I18nControlAbstract ic)
    {
        this.ic = ic;
        this.food_empty = true;
    }


    /**
     * Constructor
     * 
     * @param food_empty
     */
    public FoodDescription(boolean food_empty)
    {
        this.food_empty = food_empty;
        ic = DataAccessNutri.getInstance().getI18nControlInstance();
    }


    /**
     * Constructor
     * 
     * @param type
     */
    public FoodDescription(int type)
    {
        this.type = type;

        if (type == 1)
        {
            this.m_food_desc1 = new FoodDescriptionH();
        }
        else
        {
            this.m_food_desc2 = new FoodUserDescriptionH();
        }

        ic = DataAccessNutri.getInstance().getI18nControlInstance();

        /**
         * this.setId(0); this.setFood_group_id(0); this.setName("");
         * this.setI18n_name(""); this.setRefuse(0.0f); this.setNutritions("");
         * this.setHomeWeights(""); this.setRefuse(0.0f);
         */
    }


    /**
     * Constructor
     * 
     * @param ch
     */
    public FoodDescription(FoodDescriptionH ch)
    {
        this.m_food_desc1 = ch;
        this.type = 1;

        ic = DataAccessNutri.getInstance().getI18nControlInstance();
        /**
         * this.setId(ch.getId()); this.setFood_group_id(ch.getFood_group_id());
         * this.setName(ch.getName()); this.setI18n_name(ch.getI18n_name()); //
         * this.setCH_g(ch.getCH_g()); //
         * this.setEnergy_kcal(ch.getEnergy_kcal()); //
         * this.setEnergy_kJ(ch.getEnergy_kJ()); //
         * this.setFat_g(ch.getFat_g()); this.setRefuse(ch.getRefuse());
         * this.setHomeWeights(ch.getHomeWeights());
         * this.setNutritions(ch.getNutritions());
         * 
         * // this.setSugar_g(ch.getSugar_g());
         */
    }


    /**
     * Constructor
     * 
     * @param ch
     */
    public FoodDescription(FoodUserDescriptionH ch)
    {
        this.m_food_desc2 = ch;
        this.type = 2;

        ic = DataAccessNutri.getInstance().getI18nControlInstance();
    }


    /**
     * Get Hibernate Object
     * @return
     */
    public Object getHibernateObject()
    {
        if (this.type == 1)
            return this.m_food_desc1;
        else
            return this.m_food_desc2;
    }


    /**
     * Get Short Description
     * 
     * @return
     */
    public String getShortDescription()
    {
        return this.getName();
        // return "Food [id=" + getId() + ",name=" + this.getName() +
        // ",parent_id=" + this.getGroupId() + "]";
    }


    /**
     * Get Id
     * 
     * @return
     */
    public long getId()
    {
        if (this.m_food_desc1 == null && this.m_food_desc2 == null)
            return this.id;

        if (type == 1)
            return this.m_food_desc1.getId();
        else
            return this.m_food_desc2.getId();
    }


    /**
     * Set Id
     * 
     * @param id
     */
    public void setId(long id)
    {
        if (this.m_food_desc1 == null && this.m_food_desc2 == null)
        {
            this.id = id;
        }

        if (type == 1)
        {
            this.m_food_desc1.setId(id);
        }
        else
        {
            this.m_food_desc2.setId(id);
        }
    }


    /**
     * Get Group Id
     * @return
     */
    public long getGroup_id()
    {
        if (type == 1)
            return this.m_food_desc1.getGroupId();
        else
            return this.m_food_desc2.getGroupId();
    }


    /**
     * Set Group Id
     * 
     * @param group_id
     */
    public void setGroup_id(long group_id)
    {
        if (type == 1)
        {
            this.m_food_desc1.setGroupId(group_id);
        }
        else
        {
            this.m_food_desc2.setGroupId(group_id);
        }
    }


    /**
     * Get Name
     * 
     * @return
     */
    public String getName()
    {
        if (type == 1)
            return this.m_food_desc1.getName();
        else
            return this.m_food_desc2.getName();
    }


    /**
     * Set Name
     * 
     * @param name
     */
    public void setName(String name)
    {
        if (type == 1)
        {
            this.m_food_desc1.setName(name);
        }
        else
        {
            this.m_food_desc2.setName(name);
        }
    }


    /**
     * Get Name I18n
     * 
     * @return
     */
    public String getName_i18n()
    {
        if (type == 1)
            return this.m_food_desc1.getNameI18n();
        else
            return this.m_food_desc2.getNameI18n();
    }


    /**
     * Set Name I18n
     * 
     * @param name_i18n
     */
    public void setName_i18n(String name_i18n)
    {
        if (type == 1)
        {
            this.m_food_desc1.setNameI18n(name_i18n);
        }
        else
        {
            this.m_food_desc2.setNameI18n(name_i18n);
        }
    }


    /**
     * Get Refuse
     * 
     * @return
     */
    public float getRefuse()
    {
        if (type == 1)
            return this.m_food_desc1.getRefuse();
        else
            return this.m_food_desc2.getRefuse();
    }


    /**
     * Set Refuse
     * 
     * @param refuse
     */
    public void setRefuse(float refuse)
    {
        if (type == 1)
        {
            this.m_food_desc1.setRefuse(refuse);
        }
        else
        {
            this.m_food_desc2.setRefuse(refuse);
        }
    }


    /**
     * Get Nutritions
     * 
     * @return
     */
    public String getNutritions()
    {
        if (type == 1)
            return this.m_food_desc1.getNutritions();
        else
            return this.m_food_desc2.getNutritions();
    }


    /**
     * Set Nutritions
     * 
     * @param nutritions
     */
    public void setNutritions(String nutritions)
    {
        if (type == 1)
        {
            this.m_food_desc1.setNutritions(nutritions);
        }
        else
        {
            this.m_food_desc2.setNutritions(nutritions);
        }
    }


    /**
     * Get Home Weights
     * 
     * @return
     */
    public String getHome_weights()
    {
        if (type == 1)
            return this.m_food_desc1.getHomeWeights();
        else
            return this.m_food_desc2.getHomeWeights();
    }


    /**
     * Set Home Weights
     * 
     * @param home_weights
     */
    public void setHome_weights(String home_weights)
    {
        if (type == 1)
        {
            this.m_food_desc1.setHomeWeights(home_weights);
        }
        else
        {
            this.m_food_desc2.setHomeWeights(home_weights);
        }
    }


    /**
     * Get Description
     * 
     * @return
     */
    public String getDescription()
    {
        if (type == 2)
            return this.m_food_desc2.getDescription();
        else
            return null;
    }


    /**
     * Set Description
     * 
     * @param description
     */
    public void setDescription(String description)
    {
        if (type == 2)
        {
            this.m_food_desc2.setDescription(description);
        }
    }


    /**
     * Get Food Type
     * 
     * @return
     */
    public int getFoodType()
    {
        return type;
    }


    /**
     * To String
     * 
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString()
    {
        if (this.food_empty)
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
     * 
     * @throws Exception (HibernateException) with error
     * 
     * @return id in type of String
     */
    public String DbAdd(Session sess) throws Exception
    {

        Transaction tx = sess.beginTransaction();

        Long _id;

        if (this.type == 1)
        {
            _id = (Long) sess.save(this.m_food_desc1);
        }
        else
        {
            _id = (Long) sess.save(this.m_food_desc2);
        }

        tx.commit();

        return "" + _id.longValue();

    }


    /**
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

        if (this.type == 1)
        {
            sess.update(this.m_food_desc1);
        }
        else
        {
            sess.update(this.m_food_desc2);
        }

        tx.commit();

        return true;

    }


    /**
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

        if (this.type == 1)
        {
            sess.delete(this.m_food_desc1);
        }
        else
        {
            sess.delete(this.m_food_desc2);
        }

        tx.commit();

        return true;

    }


    /**
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
        // System.out.println("Not implemented:
        // FoodDescription::DbHasChildren");
        return true;
    }


    /**
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

        if (type == 1)
        {
            this.m_food_desc1 = (FoodDescriptionH) sess.get(FoodDescriptionH.class, new Long(this.getId()));
        }
        else
        {
            this.m_food_desc2 = (FoodUserDescriptionH) sess.get(FoodUserDescriptionH.class, new Long(this.getId()));
        }

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
        return ic.getMessage("FOODS");
    }


    /**
     * Get Class Name
     * 
     * @see com.atech.db.hibernate.transfer.BackupRestoreBase#getClassName()
     */
    public String getClassName()
    {
        return "ggc.core.db.hibernate.food.FoodUserDescriptionH";
    }


    /**
     * Get Children
     */
    public ArrayList<CheckBoxTreeNodeInterface> getNodeChildren()
    {
        return null;
    }


    /**
     * Has Children
     */
    public boolean hasNodeChildren()
    {
        return false;
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

}
