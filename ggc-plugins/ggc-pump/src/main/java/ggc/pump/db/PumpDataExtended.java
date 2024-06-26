package ggc.pump.db;

import java.util.ArrayList;
import java.util.Map;

import org.hibernate.Session;
import org.hibernate.Transaction;

import com.atech.db.hibernate.DatabaseObjectHibernate;
import com.atech.db.hibernate.transfer.BackupRestoreObject;
import com.atech.graphics.components.tree.CheckBoxTreeNodeInterface;
import com.atech.i18n.I18nControlAbstract;
import com.atech.utils.ATDataAccessAbstract;

import ggc.core.db.hibernate.pump.PumpDataExtendedH;
import ggc.core.util.DataAccess;
import ggc.pump.util.DataAccessPump;

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
 *                File is not YET DataLayer File at least not active
 * 
 *  Author: andyrozman {andy@atech-software.com}  
 */

// TODO: DL

public class PumpDataExtended extends PumpDataExtendedH implements BackupRestoreObject, DatabaseObjectHibernate
{

    private static final long serialVersionUID = 5635575801093325353L;
    private boolean selected = false;
    I18nControlAbstract ic = null; // (I18nControl)DataAccess.getInstance().getI18nControlInstance();


    /**
     * Constructor
     */
    public PumpDataExtended()
    {
    }


    /**
     * Constructor
     * 
     * @param ch
     */
    public PumpDataExtended(PumpDataExtendedH ch)
    {
        this.setId(ch.getId());
        this.setDtInfo(ch.getDtInfo());
        this.setType(ch.getType());
        this.setValue(ch.getValue());
        this.setExtended(ch.getExtended());
        this.setPersonId(ch.getPersonId());
        this.setComment(ch.getComment());
        this.setChanged(ch.getChanged());
    }


    /**
     * Constructor
     * 
     * @param _ic
     */
    public PumpDataExtended(I18nControlAbstract _ic)
    {
        this.ic = _ic;
    }


    // ---
    // --- BackupRestoreObject
    // ---

    /**
     * Get Target Name
     * 
     * @see com.atech.db.hibernate.transfer.BackupRestoreBase#getTargetName()
     */
    public String getTargetName()
    {
        return DataAccessPump.getInstance().getI18nControlInstance().getMessage("PUMP_DATA_EXTENDED");
    }


    /** 
     * Get Name
     * @return 
     */
    public String getName()
    {
        return this.getTargetName();
    }


    /**
     * getBackupClassName - name of class which will be updated/restored
     * 
     * @return
     */
    public String getClassName()
    {
        return "ggc.core.db.hibernate.pump.PumpDataExtendedH";
    }


    /**
     * To String
     * 
     * @see java.lang.Object#toString()
     */
    @Override
    public String toString()
    {
        return this.getTargetName();
    }


    /** 
     * getChildren
     */
    public ArrayList<CheckBoxTreeNodeInterface> getNodeChildren()
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
     * Is Object Collection
     * 
     * @return true if it has children
     */
    public boolean isCollection()
    {
        return false;
    }


    /**
     * Has Children
     */
    public boolean hasNodeChildren()
    {
        return false;
    }


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

        PumpDataExtendedH ch = new PumpDataExtendedH();

        ch.setId(this.getId());
        ch.setDtInfo(this.getDtInfo());
        ch.setType(this.getType());
        ch.setValue(this.getValue());
        ch.setExtended(this.getExtended());
        ch.setPersonId(this.getPersonId());
        ch.setComment(this.getComment());
        ch.setChanged(System.currentTimeMillis());

        Long id = (Long) sess.save(ch);
        tx.commit();
        ch.setId(id.longValue());

        return "" + id.longValue();
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

        PumpDataExtendedH ch = (PumpDataExtendedH) sess.get(PumpDataExtendedH.class, new Long(this.getId()));

        sess.delete(ch);
        tx.commit();

        return true;
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

        PumpDataExtendedH ch = (PumpDataExtendedH) sess.get(PumpDataExtendedH.class, new Long(this.getId()));

        ch.setId(this.getId());
        ch.setDtInfo(this.getDtInfo());
        ch.setType(this.getType());
        ch.setValue(this.getValue());
        ch.setExtended(this.getExtended());
        ch.setPersonId(this.getPersonId());
        ch.setComment(this.getComment());
        ch.setChanged(System.currentTimeMillis());

        sess.update(ch);
        tx.commit();

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
        PumpDataExtendedH ch = (PumpDataExtendedH) sess.get(PumpDataExtendedH.class, new Long(this.getId()));

        this.setId(ch.getId());
        this.setDtInfo(ch.getDtInfo());
        this.setType(ch.getType());
        this.setValue(ch.getValue());
        this.setExtended(ch.getExtended());
        this.setPersonId(ch.getPersonId());
        this.setComment(ch.getComment());
        this.setChanged(ch.getChanged());

        return true;
    }


    /**
     * DbHasChildren - Shows if this entry has any children object, this is needed for delete
     * 
     * @param sess Hibernate Session object
     * @throws Exception (HibernateException) with error
     * @return true if action done or Exception if not
     */
    public boolean DbHasChildren(Session sess) throws Exception
    {
        return false;
    }


    /** 
     * getAction
     */
    public int getAction()
    {
        return 0;
    }

    /**
     * Table Version
     */
    public int TABLE_VERSION = 1;


    /**
     * Get Table Version - returns version of table
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
        // version is ignored for now

        StringBuffer sb = new StringBuffer();

        sb.append(this.getId());
        sb.append("|");
        sb.append(this.getDtInfo());
        sb.append("|");
        sb.append(this.getType());
        sb.append("|");
        sb.append(this.getValue());
        sb.append("|");
        sb.append(this.getExtended());
        sb.append("|");
        sb.append(this.getPersonId());
        sb.append("|");
        sb.append(this.getComment());
        sb.append("|");
        sb.append(this.getChanged());
        sb.append("\n");

        return sb.toString();
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
        return "; Columns: id|dt_info|type|value|extended|person_id|comment|changed\n" + "; Table version: "
                + getTableVersion() + "\n";
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
    public void dbImport(int table_version, String value_entry) throws Exception
    {
        dbImport(table_version, value_entry, (Object[]) null);
    }


    /**
     * {@inheritDoc}
     */
    public void dbImport(int table_version, String value_entry, Object[] parameters) throws Exception
    {
        DataAccess da = DataAccess.getInstance();

        value_entry = ATDataAccessAbstract.replaceExpression(value_entry, "||", "| |");
        String[] arr = da.splitString(value_entry, "|");

        this.setId(da.getLongValueFromString(arr[0]));
        this.setDtInfo(da.getLongValueFromString(arr[1]));
        this.setType(da.getIntValueFromString(arr[2]));
        this.setValue(arr[3]);
        this.setExtended(arr[4]);
        this.setPersonId(da.getIntValueFromString(arr[5]));
        this.setComment(arr[6]);
        this.setChanged(da.getLongValueFromString(arr[7]));
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
        return "PumpDataExtendedH";
    }


    /**
     * getBackupClassName - name of class which will be updated/restored
     * 
     * @return
     */
    public String getBackupClassName()
    {
        return "ggc.core.db.hibernate.pump.PumpDataExtendedH";
    }


    /** 
     * getObjectName
     */
    public String getObjectName()
    {
        return "PumpDataExtended";
    }


    /** 
     * isDebugMode
     */
    public boolean isDebugMode()
    {
        return false;
    }


    /**
     * getObjectUniqueId - get id of object
     * @return unique object id
     */
    public String getObjectUniqueId()
    {
        return "";
    }


    /**
     * Has To Be Clean - if table needs to be cleaned before import
     * 
     * @return true if we need to clean
     */
    public boolean hasToBeCleaned()
    {
        return false;
    }

}
