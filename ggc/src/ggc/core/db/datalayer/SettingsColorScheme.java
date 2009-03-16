package ggc.core.db.datalayer;

import ggc.core.db.hibernate.ColorSchemeH;
import ggc.core.util.DataAccess;
import ggc.core.util.I18nControl;

import java.util.ArrayList;

import org.hibernate.Session;
import org.hibernate.Transaction;

import com.atech.db.hibernate.DatabaseObjectHibernate;
import com.atech.db.hibernate.transfer.BackupRestoreObject;
import com.atech.graphics.components.tree.CheckBoxTreeNodeInterface;
import com.atech.i18n.I18nControlAbstract;


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
 *  Filename:     SettingsColorScheme
 *  Description:  This is DataLayer file (data file, with methods to work with database or in 
 *                this case Hibernate). This one is used for ColorSchemes. This one is also 
 *                BackupRestoreObject.
 * 
 *  Author: andyrozman {andy@atech-software.com}  
 */


public class SettingsColorScheme extends ColorSchemeH implements DatabaseObjectHibernate, BackupRestoreObject
{

    private static final long serialVersionUID = 3245362572378355284L;

    private boolean debug = false;

    // public boolean edited = false;
    // public boolean added = false;

    private I18nControlAbstract ic;
    private boolean backup_object = false;

    /**
     * Constructor
     */
    public SettingsColorScheme()
    {
    }

    /**
     * Constructor
     * 
     * @param ic
     */
    public SettingsColorScheme(I18nControlAbstract ic)
    {
        this.ic = (I18nControl) ic;
        this.backup_object = true;
    }

    /**
     * Constructor
     * 
     * @param ch
     */
    public SettingsColorScheme(ColorSchemeH ch)
    {
        this.setId(ch.getId());
        this.setName(ch.getName());
        this.setCustom_type(ch.getCustom_type());
        this.setColor_bg(ch.getColor_bg());
        this.setColor_bg_avg(ch.getColor_bg_avg());
        this.setColor_bg_low(ch.getColor_bg_low());
        this.setColor_bg_high(ch.getColor_bg_high());
        this.setColor_bg_target(ch.getColor_bg_target());
        this.setColor_ins(ch.getColor_ins());
        this.setColor_ins1(ch.getColor_ins1());
        this.setColor_ins2(ch.getColor_ins2());
        this.setColor_ins_perbu(ch.getColor_ins_perbu());
        this.setColor_ch(ch.getColor_ch());
        
        //this.setSelected()
    }

    /**
     * Get Short Description
     * 
     * @return
     */
    public String getShortDescription()
    {
        return "ColorScheme [id=" + this.getId() + ";name=" + this.getName() + "]";
    }

    /**
     * To String
     * 
     * @see ggc.core.db.hibernate.ColorSchemeH#toString()
     */
    @Override
    public String toString()
    {
        if (backup_object)
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

        ColorSchemeH ch = new ColorSchemeH();

        ch.setId(this.getId());
        ch.setName(this.getName());
        ch.setCustom_type(this.getCustom_type());
        ch.setColor_bg(this.getColor_bg());
        ch.setColor_bg_avg(this.getColor_bg_avg());
        ch.setColor_bg_low(this.getColor_bg_low());
        ch.setColor_bg_high(this.getColor_bg_high());
        ch.setColor_bg_target(this.getColor_bg_target());
        ch.setColor_ins(this.getColor_ins());
        ch.setColor_ins1(this.getColor_ins1());
        ch.setColor_ins2(this.getColor_ins2());
        ch.setColor_ins_perbu(this.getColor_ins_perbu());
        ch.setColor_ch(this.getColor_ch());
        
        Long id = (Long) sess.save(ch);

        tx.commit();
        
        ch.setId(id.longValue());
        
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

        ColorSchemeH ch = (ColorSchemeH) sess.get(ColorSchemeH.class, new Long(this.getId()));

        ch.setId(this.getId());
        ch.setName(this.getName());
        ch.setCustom_type(this.getCustom_type());
        ch.setColor_bg(this.getColor_bg());
        ch.setColor_bg_avg(this.getColor_bg_avg());
        ch.setColor_bg_low(this.getColor_bg_low());
        ch.setColor_bg_high(this.getColor_bg_high());
        ch.setColor_bg_target(this.getColor_bg_target());
        ch.setColor_ins(this.getColor_ins());
        ch.setColor_ins1(this.getColor_ins1());
        ch.setColor_ins2(this.getColor_ins2());
        ch.setColor_ins_perbu(this.getColor_ins_perbu());
        ch.setColor_ch(this.getColor_ch());
        
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

        ColorSchemeH ch = (ColorSchemeH) sess.get(ColorSchemeH.class, new Long(this.getId()));

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

        ColorSchemeH ch = (ColorSchemeH) sess.get(ColorSchemeH.class, new Long(this.getId()));

        this.setId(ch.getId());
        this.setName(ch.getName());
        this.setCustom_type(ch.getCustom_type());
        this.setColor_bg(ch.getColor_bg());
        this.setColor_bg_avg(ch.getColor_bg_avg());
        this.setColor_bg_low(ch.getColor_bg_low());
        this.setColor_bg_high(ch.getColor_bg_high());
        this.setColor_bg_target(ch.getColor_bg_target());
        this.setColor_ins(ch.getColor_ins());
        this.setColor_ins1(ch.getColor_ins1());
        this.setColor_ins2(ch.getColor_ins2());
        this.setColor_ins_perbu(ch.getColor_ins_perbu());
        this.setColor_ch(ch.getColor_ch());
        
        return true;
    }

    /**
     * getObjectName - returns name of DatabaseObject
     * 
     * @return name of object (not Hibernate object)
     */
    public String getObjectName()
    {
        return "ColorScheme";
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
     * 0 = no action, 1 = add action 2 = edit action 3 = delete action 
     * 
     * This is used mainly for objects, contained in lists and dialogs, used for 
     * processing by higher classes (classes calling selectors, wizards, etc...
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

    private boolean selected = false;

    /**
     * getTargetName
     */
    public String getTargetName()
    {
        return ic.getMessage("COLOR_SCHEMES");
    }

    
    /**
     * Get Class Name
     * 
     * @see com.atech.db.hibernate.transfer.BackupRestoreBase#getClassName()
     */
    public String getClassName()
    {
        return "ggc.core.db.hibernate.ColorSchemeH";
    }


    // ---
    // --- BackupRestoreObject
    // ---
    
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
     * Has Children
     * 
     * @return 
     */
    public boolean hasChildren()
    {
        return false;
    }

    /**
     * getObjectUniqueId - get id of object
     * 
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
     * @throws Exception
     *             if export for table is not supported
     */
    public String dbExport(int table_version) throws Exception
    {
        // version is ignored for now

        StringBuffer sb = new StringBuffer();

        sb.append(this.getId());
        sb.append("|");
        sb.append(this.getName());
        sb.append("|");
        sb.append(this.getCustom_type());
        sb.append("|");
        sb.append(this.getColor_bg());
        sb.append("|");
        sb.append(this.getColor_bg_avg());
        sb.append("|");
        sb.append(this.getColor_bg_low());
        sb.append("|");
        sb.append(this.getColor_bg_high());
        sb.append("|");
        sb.append(this.getColor_bg_target());
        sb.append("|");
        sb.append(this.getColor_ins());
        sb.append("|");
        sb.append(this.getColor_ins1());
        sb.append("|");
        sb.append(this.getColor_ins2());
        sb.append("|");
        sb.append(this.getColor_ins_perbu());
        sb.append("|");
        sb.append(this.getColor_ch());

        return sb.toString();
    }

    /**
     * dbExport - returns export String, for current version
     * 
     * @return line that will be exported
     * @throws Exception
     *             if export for table is not supported
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
        return "; Columns: id|name|custom_type|color_bg|color_bg_avg|color_bg_low|color_bg_high|color_bg_target|color_ins|color_ins1|color_ins2|color_ins_perbu|color_ch\n" + 
               "; Table version: " + getTableVersion() + "\n";
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
     * dbImport - processes input entry to right fields
     * 
     * @param table_version
     *            version of table
     * @param value_entry
     *            whole import line
     * @throws Exception
     *             if import for selected table version is not supported or it
     *             fails
     */
    public void dbImport(int table_version, String value_entry) throws Exception
    {
        dbImport(table_version, value_entry, null);
    }
    
    
    /**
     * dbImport - processes input entry to right fields
     * 
     * @param table_version
     *            version of table
     * @param value_entry
     *            whole import line
     * @throws Exception
     *             if import for selected table version is not supported or it
     *             fails
     */
    public void dbImport(int table_version, String value_entry, Object[] special) throws Exception
    {
        // version is ignored for now

        DataAccess da = DataAccess.getInstance();

        value_entry = DataAccess.getInstance().replaceExpression(value_entry, "||", "| |");
        String[] arr = da.splitString(value_entry, "|"); 
       
        //System.out.println(value_entry);
        //System.out.println("arr: " + arr);
        
        //if (special==null)
            this.setId(da.getLongValueFromString(arr[0]));
        //else
        //    this.setId(0);
        
        this.setName(arr[1]);
        this.setCustom_type(da.getIntValueFromString(arr[2]));
        this.setColor_bg(da.getIntValueFromString(arr[3]));
        this.setColor_bg_avg(da.getIntValueFromString(arr[4]));
        this.setColor_bg_low(da.getIntValueFromString(arr[5]));
        this.setColor_bg_high(da.getIntValueFromString(arr[6]));
        this.setColor_bg_target(da.getIntValueFromString(arr[7]));
        this.setColor_ins(da.getIntValueFromString(arr[8]));
        this.setColor_ins1(da.getIntValueFromString(arr[9]));
        this.setColor_ins2(da.getIntValueFromString(arr[10]));
        this.setColor_ins_perbu(da.getIntValueFromString(arr[11]));
        this.setColor_ch(da.getIntValueFromString(arr[12]));
        
    }

    /**
     * getBackupFile - name of backup file (base part)
     * 
     * @return
     */
    public String getBackupFile()
    {
        return "ColorSchemeH";
    }

    /**
     * getBackupClassName - name of class which will be updated/restored
     * 
     * @return
     */
    public String getBackupClassName()
    {
        return "ggc.core.db.hibernate.ColorSchemeH";
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
