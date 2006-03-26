package com.atech.db.tool;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.Iterator;

import ggc.db.datalayer.FoodDescription;
import ggc.db.datalayer.FoodGroup;
import ggc.db.datalayer.GGCDb;
import ggc.util.DataAccess;

public class DbToolTreeRoot 
{

    public final static int ROOT_SINGLE = 1;
    public final static int ROOT_MULTIPLE = 2;

    public int type = ROOT_SINGLE;

    public ArrayList m_appGroup = null;
    public DbToolApplicationInterface m_app = null;

    public Hashtable m_appGroup_table = null;
    public ArrayList m_app_list = null;

    public DbToolAccess m_da = null;


    public DbToolTreeRoot(DbToolAccess da) 
    {

	m_da = da;
	//m_appGroup = m_da.getApplicationDatas();

/*
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
*/
        
    }

    public void loadData()
    {
	m_appGroup = m_da.getApplicationDatas();
	type = ROOT_MULTIPLE;
    }

    public void loadData(DbToolApplicationInterface intr)
    {
	//m_appGroup = new ArrayList();
	//m_appGroup.add(intr);

	m_app = intr;
	m_app_list = getListOfDatabases(intr);

	type = ROOT_SINGLE;
    }


    public ArrayList getListOfDatabases(DbToolApplicationInterface intr)
    {
	m_da.loadConfig(intr);
	return m_da.getListOfDatabases();
    }


    public String toString()
    {
	if (type==ROOT_SINGLE)
	    return m_app.getApplicationName();
	else
            return m_da.m_i18n.getMessage("HIBERNATE_DATABASE_APPLICATION");
    }


}
