package ggc.core.db.datalayer;

import java.util.ArrayList;

import org.hibernate.Session;

import ggc.core.db.hibernate.StockSubTypeH;

import com.atech.db.hibernate.DatabaseObjectHibernate;
import com.atech.db.hibernate.transfer.BackupRestoreObject;
import com.atech.graphics.components.tree.CheckBoxTreeNodeInterface;
import com.atech.graphics.dialogs.selector.ColumnSorter;
import com.atech.graphics.dialogs.selector.SelectableInterface;

public class StockSubType extends StockSubTypeH implements SelectableInterface, DatabaseObjectHibernate, BackupRestoreObject
{

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
	public int getColumnCount() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public String getColumnName(int num) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getColumnValue(int num) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Object getColumnValueObject(int num) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int getColumnWidth(int num, int width) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public boolean isFound(String text) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean isFound(int value) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean isFound(int from, int till, int state) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void setSearchContext() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setColumnSorter(ColumnSorter cs) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public int compareTo(SelectableInterface o) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public boolean isCollection() {
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
	public int getTableVersion() 
	{
		return 1;
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
			throws Exception 
	{
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
	public boolean hasToBeCleaned() 
	{
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
	public String getObjectName() 
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean isDebugMode() 
	{
		return false;
	}

	@Override
	public int getAction() 
	{
		return 0;
	}

}
