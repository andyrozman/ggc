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
 *  Filename: NutritionTreeModel
 *  Purpose:  This is tree model for displaying nutrition information.
 *
 *  Author:   andyrozman
 */


package ggc.plugin.list;


import java.util.Vector;

import javax.swing.event.TreeModelEvent;
import javax.swing.event.TreeModelListener;
import javax.swing.tree.TreeModel;
import javax.swing.tree.TreePath;

public class BaseListModel implements TreeModel 
{

    private boolean m_debug = false;
    private Vector<TreeModelListener> treeModelListeners = new Vector<TreeModelListener>();
    private BaseListRoot rootObj = null;

    

    public BaseListModel(BaseListRoot rt) 
    {
        rootObj = rt;
    }


    public void debug(String deb)
    {
        if (m_debug)
            System.out.println(deb);
    }



    //////////////// Fire events //////////////////////////////////////////////

    /**
     * The only event raised by this model is TreeStructureChanged with the
     * root as path, i.e. the whole tree has changed.
     */
    protected void fireTreeStructureChanged(BaseListRoot oldRoot) 
    {
        int len = treeModelListeners.size();
        TreeModelEvent e = new TreeModelEvent(this, 
                                              new Object[] {oldRoot});
        for (int i = 0; i < len; i++) 
        {
            (treeModelListeners.elementAt(i)).treeStructureChanged(e);
        }
    }


    //////////////// TreeModel interface implementation ///////////////////////

    /**
     * Adds a listener for the TreeModelEvent posted after the tree changes.
     */
    public void addTreeModelListener(TreeModelListener l) 
    {
        treeModelListeners.addElement(l);
    }


    /**
     * Returns the child of parent at index index in the parent's child array.
     */
    public Object getChild(Object parent, int index) 
    {

        debug("getChild: " + index);

        if (parent instanceof BaseListRoot)
        {
        	return rootObj.children.get(index);
        }
    	else if (parent instanceof String)
    	{
    		return null;
    	}
    	else
    	    return null;
    
    }

    /**
     * Returns the number of children of parent.
     */
    public int getChildCount(Object parent) 
    {
        debug("Parent (getChildCount()): " + parent);

        if (parent instanceof BaseListRoot)
        {
        	return rootObj.children.size();
        }
        else
        	return 0;
    
        }

    /**
     * Returns the index of child in parent.
     */
    public int getIndexOfChild(Object parent, Object child) 
    {
        debug("getIndexofChild: ");

        if (parent instanceof BaseListRoot)
        {
        	return rootObj.children.indexOf(child);
        }
        else
        	return -1;

    }

    /**
     * Returns the root of the tree.
     */
    public Object getRoot() 
    {
        return rootObj;
    }

    /**
     * Returns true if node is a leaf.
     */
    public boolean isLeaf(Object node) 
    {
    	return (getChildCount(node)==0);
    }


    /**
     * Removes a listener previously added with addTreeModelListener().
     */
    public void removeTreeModelListener(TreeModelListener l) 
    {
        treeModelListeners.removeElement(l);
    }

    /**
     * Messaged when the user has altered the value for the item
     * identified by path to newValue.  Not used by this model.
     */
    public void valueForPathChanged(TreePath path, Object newValue) 
    {
        System.out.println("*** valueForPathChanged : " + path + " --> " + newValue);
    }

}
