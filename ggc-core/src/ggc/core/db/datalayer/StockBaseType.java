package ggc.core.db.datalayer;

import java.util.ArrayList;

import org.hibernate.Session;

import ggc.core.db.hibernate.StockTypeH;

import com.atech.db.hibernate.DatabaseObjectHibernate;
import com.atech.db.hibernate.transfer.BackupRestoreObject;
import com.atech.graphics.components.tree.CheckBoxTreeNodeInterface;
import com.atech.graphics.dialogs.selector.ColumnSorter;
import com.atech.graphics.dialogs.selector.SelectableInterface;

public class StockBaseType extends StockTypeH implements SelectableInterface, DatabaseObjectHibernate, BackupRestoreObject 
{

    public StockBaseType()
    {
    }
    
	public StockBaseType(StockTypeH sth)
	{
		this.setId(sth.getId());
		this.setName(sth.getName());
		this.setName_i18n(sth.getName_i18n());
	}
	
	
	@Override
	public boolean isCollection() 
	{
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void setSelected(boolean selected) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public boolean isSelected() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public String getClassName() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ArrayList<CheckBoxTreeNodeInterface> getNodeChildren() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean hasNodeChildren() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public String getTargetName() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int getTableVersion() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public String dbExport(int table_version) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String dbExport() throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String dbExportHeader(int table_version) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String dbExportHeader() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void dbImport(int table_version, String value_entry)
			throws Exception {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void dbImport(int table_version, String value_entry,
			Object[] parameters) throws Exception {
		// TODO Auto-generated method stub
		
	}

	@Override
	public String getBackupFile() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getBackupClassName() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean hasToBeCleaned() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public String getObjectUniqueId() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String DbAdd(Session sess) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean DbEdit(Session sess) throws Exception {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean DbDelete(Session sess) throws Exception {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean DbHasChildren(Session sess) throws Exception {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean DbGet(Session sess) throws Exception {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public String getObjectName() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean isDebugMode() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public int getAction() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public long getItemId() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public String getShortDescription() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int getColumnCount() 
	{
		return 2;
	}

	@Override
	public String getColumnName(int num) 
	{
		switch(num)
		{
			case 1:
				return "ID";
				
			default:
				return "NAME";
		}
	}

	@Override
	public String getColumnValue(int num) 
	{
		switch(num)
		{
			case 1:
				return "" + this.getId();
				
			default:
				return this.getName();
		}
	}

	@Override
	public Object getColumnValueObject(int num) 
	{
		switch(num)
		{
			case 1:
				return this.getId();
				
			default:
				return this.getName();
		}
	}

	@Override
	public int getColumnWidth(int num, int width) 
	{
        switch (num)
        {
            case 0:
                return (int) (width * 20);
            default:
                return (int) (width * 80);
        } 
	}

	@Override
	public boolean isFound(String text) 
	{
	    if (text.length()==0)
	        return true;
	    else
	        return this.getName().contains(text);
	}

	@Override
	public boolean isFound(int value) 
	{
		return true;
	}

	@Override
	public boolean isFound(int from, int till, int state) 
	{
		return true;
	}

	@Override
	public void setSearchContext() 
	{
	}

    private ColumnSorter columnSorter = null;
	
	
	@Override
	public void setColumnSorter(ColumnSorter cs) 
	{
	    this.columnSorter = cs;
	}

	@Override
	public int compareTo(SelectableInterface o) 
	{
        return this.columnSorter.compareObjects(this, o);
	}

}
