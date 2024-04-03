package ggc.core.db.datalayer;

import java.util.ArrayList;
import java.util.Map;

import org.hibernate.Session;

import com.atech.db.hibernate.DatabaseObjectHibernate;
import com.atech.db.hibernate.transfer.BackupRestoreObject;
import com.atech.graphics.components.tree.CheckBoxTreeNodeInterface;
import com.atech.graphics.dialogs.selector.ColumnSorter;
import com.atech.graphics.dialogs.selector.SelectableInterface;

import ggc.core.db.hibernate.StockSubTypeH;

public class StockBaseType extends StockSubTypeH
        implements SelectableInterface, DatabaseObjectHibernate, BackupRestoreObject
{

    public StockBaseType()
    {
    }


    public StockBaseType(StockSubTypeH sth)
    {
        // this.setId(sth.getId());
        // this.setName(sth.getName());
        // this.setNameI18n(sth.getNameI18n());
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
        return "StockBaseType";
    }


    public int getTableVersion()
    {
        // TODO Auto-generated method stub
        return 0;
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


    public void dbImport(int tableVersion, String valueEntry, Map<String, String> headers) throws Exception
    {

    }


    public boolean isNewImport()
    {
        return false;
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
        // TODO Auto-generated method stub
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
        // TODO Auto-generated method stub
        return false;
    }


    public int getAction()
    {
        // TODO Auto-generated method stub
        return 0;
    }


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
        return 2;
    }


    public String getColumnName(int num)
    {
        switch (num)
        {
            case 1:
                return "ID";

            default:
                return "NAME";
        }
    }


    public String getColumnValue(int num)
    {
        switch (num)
        {
            case 1:
                return "" + this.getId();

            default:
                return this.getName();
        }
    }


    public Object getColumnValueObject(int num)
    {
        switch (num)
        {
            case 1:
                return this.getId();

            default:
                return this.getName();
        }
    }


    public int getColumnWidth(int num, int width)
    {
        switch (num)
        {
            case 0:
                return width * 20;
            default:
                return width * 80;
        }
    }


    public boolean isFound(String text)
    {
        if (text.length() == 0)
            return true;
        else
            return this.getName().contains(text);
    }


    public boolean isFound(int value)
    {
        return true;
    }


    public boolean isFound(int from, int till, int state)
    {
        return true;
    }


    public void setSearchContext()
    {
    }

    private ColumnSorter columnSorter = null;


    public void setColumnSorter(ColumnSorter cs)
    {
        this.columnSorter = cs;
    }


    public int compareTo(SelectableInterface o)
    {
        return this.columnSorter.compareObjects(this, o);
    }

}
