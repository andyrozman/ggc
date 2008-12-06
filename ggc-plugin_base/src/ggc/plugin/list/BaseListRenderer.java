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

import java.awt.Color;
import java.awt.Component;

import javax.swing.JTree;
import javax.swing.tree.DefaultTreeCellRenderer;

/**
 *  Application:   GGC - GNU Gluco Control
 *  Plug-in:       GGC PlugIn Base (base class for all plugins)
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
 *  Filename:     BaseListRenderer
 *  Description:  Tree Renderer
 * 
 *  Author: Andy {andy@atech-software.com}
 */


public class BaseListRenderer extends DefaultTreeCellRenderer
{

    private static final long serialVersionUID = -5179522965356580379L;

    private static Color[] list_status_color = { 
                                                Color.black,
                                                Color.green,
                                                Color.magenta,
                                                Color.blue,
                                                Color.orange,
                                                Color.red
    }; 
    
    
    
    /**
     * Constructor
     */
    public BaseListRenderer()
    {
    }

    /**
     * Get Tree Cell Renderer Component
     * 
     * @see javax.swing.tree.DefaultTreeCellRenderer#getTreeCellRendererComponent(javax.swing.JTree, java.lang.Object, boolean, boolean, boolean, int, boolean)
     */
    public Component getTreeCellRendererComponent(JTree tree, Object value, boolean sel, boolean expanded,
            boolean leaf, int row, boolean _hasFocus)
    {

        super.getTreeCellRendererComponent(tree, value, sel, expanded, leaf, row, _hasFocus);
        
        if (leaf)
        {
            BaseListEntry ble = (BaseListEntry)value;
            this.setForeground(list_status_color[ble.status]);
        }

        return this;
    }

}
