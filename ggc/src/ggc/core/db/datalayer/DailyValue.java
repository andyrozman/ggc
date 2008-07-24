/*
 *  GGC - GNU Gluco Control
 *
 *  A pure java app to help you manage your diabetes.
 *
 *  See AUTHORS for copyright information.
 *
 *  This program is free software; you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation; either version 2 of the License, or
 *  (at your option) any later version.
 *
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with this program; if not, write to the Free Software
 *  Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA
 *
 *  Filename: DailyValue
 *  Purpose:  This is backup class for DailyValuesH
 *
 *  Author:   andyrozman  {andy@atech-software.com}
 */


package ggc.core.db.datalayer;

import ggc.core.util.I18nControl;

import java.util.ArrayList;

import org.hibernate.Session;

import com.atech.db.hibernate.DatabaseObjectHibernate;
import com.atech.db.hibernate.transfer.BackupRestoreObject;
import com.atech.graphics.components.tree.CheckBoxTreeNodeInterface;
import com.atech.i18n.I18nControlAbstract;


public class DailyValue implements BackupRestoreObject, DatabaseObjectHibernate
{



    private boolean selected = false;
    I18nControl ic = null; // (I18nControl)DataAccess.getInstance().getI18nControlInstance();
    
    /* 
     * getTargetName
     */
    public String getTargetName()
    {
	return ic.getMessage("DAILY_VALUES");
    }

    /* 
     * getName
     */
    public String getName()
    {
	return this.getTargetName();
    }


    public String getClassName()
    {
        return "ggc.core.db.hibernate.DayValueH";
    }
    
    
    public DailyValue(I18nControlAbstract ic)
    {
	this.ic = (I18nControl)ic;
    }


    public String toString()
    {
	return this.getTargetName();
    }
    

    
    
    
    
    //---
    //---  BackupRestoreObject
    //---
    
    
    /* 
     * getChildren
     */
    public ArrayList<CheckBoxTreeNodeInterface> getChildren()
    {
	return null;
    }

    /* 
     * isSelected
     */
    public boolean isSelected()
    {
	return selected;
    }

    /* 
     * setSelected
     */
    public void setSelected(boolean newValue)
    {
	this.selected = newValue;
    }
    
    
    public boolean isCollection()
    {
	return false;
    }
    
    
    public boolean hasChildren()
    {
	return false;
    }
    
    

    
    
    
    /* 
     * DbAdd
     */
    public String DbAdd(Session sess) throws Exception
    {
        // TODO Auto-generated method stub
        return null;
    }

    /* 
     * DbDelete
     */
    public boolean DbDelete(Session sess) throws Exception
    {
        // TODO Auto-generated method stub
        return false;
    }

    /* 
     * DbEdit
     */
    public boolean DbEdit(Session sess) throws Exception
    {
        // TODO Auto-generated method stub
        return false;
    }

    /* 
     * DbGet
     */
    public boolean DbGet(Session sess) throws Exception
    {
        // TODO Auto-generated method stub
        return false;
    }

    /* 
     * DbHasChildren
     */
    public boolean DbHasChildren(Session sess) throws Exception
    {
        return false;
    }

    /* 
     * getAction
     */
    public int getAction()
    {
        return 0;
    }

    /* 
     * getObjectName
     */
    public String getObjectName()
    {
        return "DailyValue";
    }

    /* 
     * isDebugMode
     */
    public boolean isDebugMode()
    {
        return false;
    }
    
    
    
    
    
    
}


