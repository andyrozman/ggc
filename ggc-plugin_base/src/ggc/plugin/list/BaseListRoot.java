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
 *  Filename: GGCTreeRoot
 *  Purpose:  Used for holding tree information for nutrition and meals
 *
 *  Author:   andyrozman
 */

package ggc.plugin.list;

import ggc.plugin.util.DataAccessPlugInBase;

import java.util.ArrayList;

public class BaseListRoot 
{

    public ArrayList<String> children = null;
    DataAccessPlugInBase m_da;
    

    public BaseListRoot(DataAccessPlugInBase da) 
    {
        m_da = da;
        this.children = m_da.getWebListerItemsTitles();
    }
    
    
    @Override
    public String toString()
    {
    	return m_da.getWebListerTitle();
    }


}
