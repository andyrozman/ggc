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

package ggc.gui.nutrition;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Iterator;

import ggc.db.datalayer.FoodDescription;
import ggc.db.datalayer.FoodGroup;
import ggc.db.GGCDb;
import ggc.util.DataAccess;

public class GGCTreeRoot 
{

    public static final int TREE_ROOT_NUTRITION = 1;
    public static final int TREE_ROOT_MEALS = 2;

    private int m_type = 1;

    public ArrayList m_foodGroups = null;
    public Hashtable<String, ArrayList<FoodDescription>> m_foodDescByGroup = null;



    public GGCTreeRoot(int type, GGCDb db) 
    {
        m_type = type;

        //GGCDb db = DataAccess.getInstance().getDb();

        if (type==1)
        {
	    m_foodGroups = db.getFoodGroups();
	    Iterator it = m_foodGroups.iterator();

	    m_foodDescByGroup = new Hashtable<String, ArrayList<FoodDescription>>();

	    while (it.hasNext())
	    {
		FoodGroup fg = (FoodGroup)it.next();
		m_foodDescByGroup.put(""+fg.getId(), new ArrayList<FoodDescription>());
	    }

	    
	    ArrayList<FoodDescription> list = db.getFoodDescriptions();
	    it = list.iterator();

	    while (it.hasNext())
	    {
		FoodDescription fd = (FoodDescription)it.next();

		ArrayList<FoodDescription> al = m_foodDescByGroup.get(""+fd.getFood_group_id());
		al.add(fd);
	    }

        }
        else
        {
	    // meals -- Not implemented yet
        }
        
    }


    public GGCTreeRoot(int type) 
    {
        m_type = type;

        GGCDb db = DataAccess.getInstance().getDb();

        if (type==1)
        {
	    m_foodGroups = db.getFoodGroups();
	    Iterator it = m_foodGroups.iterator();

	    m_foodDescByGroup = new Hashtable<String, ArrayList<FoodDescription>>();

	    while (it.hasNext())
	    {
		FoodGroup fg = (FoodGroup)it.next();
		m_foodDescByGroup.put(""+fg.getId(), new ArrayList<FoodDescription>());
	    }

	    
	    ArrayList<FoodDescription> list = db.getFoodDescriptions();
	    it = list.iterator();

	    while (it.hasNext())
	    {
		FoodDescription fd = (FoodDescription)it.next();

		ArrayList<FoodDescription> al = m_foodDescByGroup.get(""+fd.getFood_group_id());
		al.add(fd);
	    }

        }
        else
        {
	    // meals -- Not implemented yet
        }
        
    }

    public String toString()
    {

	if (m_type==1)
            return DataAccess.getInstance().m_i18n.getMessage("NUTRITION_DATA");
        else
            return DataAccess.getInstance().m_i18n.getMessage("MEALS");

    }


}
