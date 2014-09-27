package ggc.core.db.datalayer;

import ggc.core.db.hibernate.StockSubTypeH;

import java.util.ArrayList;

import org.hibernate.Session;

import com.atech.db.hibernate.DatabaseObjectHibernate;
import com.atech.db.hibernate.transfer.BackupRestoreObject;
import com.atech.graphics.components.tree.CheckBoxTreeNodeInterface;
import com.atech.graphics.dialogs.selector.ColumnSorter;
import com.atech.graphics.dialogs.selector.SelectableInterface;

public class StockSubType extends StockSubTypeH implements SelectableInterface, DatabaseObjectHibernate,
        BackupRestoreObject
{

    public long getItemId()
    {
        // TODO Auto-generated method stub
        return 0;
    }

    public String getShortDescription()
    {
        // TODO Auto-generated method stub
        return null;
    }

    public int getColumnCount()
    {
        // TODO Auto-generated method stub
        return 0;
    }

    public String getColumnName(int num)
    {
        // TODO Auto-generated method stub
        return null;
    }

    public String getColumnValue(int num)
    {
        // TODO Auto-generated method stub
        return null;
    }

    public Object getColumnValueObject(int num)
    {
        // TODO Auto-generated method stub
        return null;
    }

    public int getColumnWidth(int num, int width)
    {
        // TODO Auto-generated method stub
        return 0;
    }

    public boolean isFound(String text)
    {
        // TODO Auto-generated method stub
        return false;
    }

    public boolean isFound(int value)
    {
        // TODO Auto-generated method stub
        return false;
    }

    public boolean isFound(int from, int till, int state)
    {
        // TODO Auto-generated method stub
        return false;
    }

    public void setSearchContext()
    {
        // TODO Auto-generated method stub

    }

    public void setColumnSorter(ColumnSorter cs)
    {
        // TODO Auto-generated method stub

    }

    public int compareTo(SelectableInterface o)
    {
        // TODO Auto-generated method stub
        return 0;
    }

    public boolean isCollection()
    {
        // TODO Auto-generated method stub
        return false;
    }

    public void setSelected(boolean selected)
    {
        // TODO Auto-generated method stub

    }

    public boolean isSelected()
    {
        // TODO Auto-generated method stub
        return false;
    }

    public String getClassName()
    {
        // TODO Auto-generated method stub
        return null;
    }

    public ArrayList<CheckBoxTreeNodeInterface> getNodeChildren()
    {
        // TODO Auto-generated method stub
        return null;
    }

    public boolean hasNodeChildren()
    {
        // TODO Auto-generated method stub
        return false;
    }

    public String getTargetName()
    {
        // TODO Auto-generated method stub
        return null;
    }

    public int getTableVersion()
    {
        return 1;
    }

    public String dbExport(int table_version) throws Exception
    {
        // TODO Auto-generated method stub
        return null;
    }

    public String dbExport() throws Exception
    {
        // TODO Auto-generated method stub
        return null;
    }

    public String dbExportHeader(int table_version)
    {
        // TODO Auto-generated method stub
        return null;
    }

    public String dbExportHeader()
    {
        // TODO Auto-generated method stub
        return null;
    }

    public void dbImport(int table_version, String value_entry) throws Exception
    {
        // TODO Auto-generated method stub

    }

    public void dbImport(int table_version, String value_entry, Object[] parameters) throws Exception
    {
        // TODO Auto-generated method stub

    }

    public String getBackupFile()
    {
        // TODO Auto-generated method stub
        return null;
    }

    public String getBackupClassName()
    {
        // TODO Auto-generated method stub
        return null;
    }

    public boolean hasToBeCleaned()
    {
        return false;
    }

    public String getObjectUniqueId()
    {
        // TODO Auto-generated method stub
        return null;
    }

    public String DbAdd(Session sess) throws Exception
    {
        // TODO Auto-generated method stub
        return null;
    }

    public boolean DbEdit(Session sess) throws Exception
    {
        // TODO Auto-generated method stub
        return false;
    }

    public boolean DbDelete(Session sess) throws Exception
    {
        // TODO Auto-generated method stub
        return false;
    }

    public boolean DbHasChildren(Session sess) throws Exception
    {
        // TODO Auto-generated method stub
        return false;
    }

    public boolean DbGet(Session sess) throws Exception
    {
        // TODO Auto-generated method stub
        return false;
    }

    public String getObjectName()
    {
        // TODO Auto-generated method stub
        return null;
    }

    public boolean isDebugMode()
    {
        return false;
    }

    public int getAction()
    {
        return 0;
    }

}
