package ggc.db.db_tool;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Iterator;

import ggc.db.datalayer.FoodDescription;
import ggc.db.datalayer.FoodGroup;
import ggc.db.datalayer.GGCDb;
import ggc.util.DataAccess;

public class GGCTreeRoot 
{

    public static final int TREE_ROOT_NUTRITION = 1;
    public static final int TREE_ROOT_MEALS = 2;

    private int m_type = 1;

    public ArrayList m_foodGroups = null;
    public Hashtable m_foodDescByGroup = null;



    public GGCTreeRoot(int type, GGCDb db) 
    {
        m_type = type;

        //GGCDb db = DataAccess.getInstance().getDb();

        if (type==1)
        {
	    m_foodGroups = db.getFoodGroups();
	    Iterator it = m_foodGroups.iterator();

	    m_foodDescByGroup = new Hashtable();

	    while (it.hasNext())
	    {
		FoodGroup fg = (FoodGroup)it.next();
		m_foodDescByGroup.put(""+fg.getId(), new ArrayList());
	    }

	    
	    ArrayList list = db.getFoodDescriptions();
	    it = list.iterator();

	    while (it.hasNext())
	    {
		FoodDescription fd = (FoodDescription)it.next();

		ArrayList al = (ArrayList)m_foodDescByGroup.get(""+fd.getFood_group_id());
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

	    m_foodDescByGroup = new Hashtable();

	    while (it.hasNext())
	    {
		FoodGroup fg = (FoodGroup)it.next();
		m_foodDescByGroup.put(""+fg.getId(), new ArrayList());
	    }

	    
	    ArrayList list = db.getFoodDescriptions();
	    it = list.iterator();

	    while (it.hasNext())
	    {
		FoodDescription fd = (FoodDescription)it.next();

		ArrayList al = (ArrayList)m_foodDescByGroup.get(""+fd.getFood_group_id());
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
