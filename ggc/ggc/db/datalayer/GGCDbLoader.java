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
 *  Filename: GGCDb
 *  Purpose:  This is main datalayer file. It contains all methods for initialization of
 *      Hibernate framework, for adding/updating/deleting data from database (hibernate).
 *      It also contains all methods for mass readings of data from hibernate. 
 *
 *  Author:   andyrozman
 */

// WORK IN PROGRESS, PLEASE DO NOT TOUCH
// 
// Methods are added to this class, whenever we make changes to our existing database,
// either add methods for handling data or adding new tables.
// 
// andyrozman


package ggc.db.datalayer;

import java.io.FileInputStream;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Properties;
import java.util.StringTokenizer;

import ggc.db.HibernateHandler;
import ggc.gui.MainFrame;
import ggc.gui.StatusBar;
import ggc.util.DataAccess;





public class GGCDbLoader extends Thread
{

    DataAccess m_da = null;
    StatusBar m_bar = null;

    public GGCDbLoader(DataAccess da, StatusBar bar)
    {
        m_da = da;
        m_bar = bar;
//        System.out.println("GGCDbLoader inited");
    }

    public void run()
    {
//        System.out.println("GGCDbLoader: Create");

        try { Thread.sleep(2000); } catch(Exception ex) { }

        GGCDb db = new GGCDb(m_da);
        m_bar.setDatabaseName(db.db_conn_name);
//        System.out.println("GGCDbLoader: Init");
        db.initDb();
        //m_da.m_db = db;
        m_bar.setDbStatus(StatusBar.DB_INIT_OK); 

//        System.out.println("GGCDbLoader: Static Data");
        db.loadStaticData();
        m_bar.setDbStatus(StatusBar.DB_LOAD);
        m_da.m_db = db;

	//try { Thread.sleep(2000); } catch(Exception ex) { }

	m_da.loadDailySettings(new java.util.Date(System.currentTimeMillis()));
	HibernateHandler.getInstance().connected = true;

	m_bar.setDatabaseName(db.db_conn_name);

	MainFrame mf = m_da.getParent();
	mf.setDbActions(true);
	mf.informationPanel.refreshPanels();
    }


}

