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


package ggc.db;

import java.util.GregorianCalendar;

import ggc.gui.MainFrame;
import ggc.gui.StatusBar;
import ggc.gui.little.GGCLittle;
import ggc.gui.little.StatusBarL;
import ggc.util.DataAccess;


public class GGCDbLoader extends Thread
{


    // 1 - init
    // 2 - load configuration
    // 3 - load doctors data
    // 4 - load statistics for display, apointments
    // 5 - load nutrition(1) root data
    // 6 - load nutrition(2) root data
    // 7 - load meals root data

    // 99 - loading complete


    DataAccess m_da = null;
    StatusBar m_bar = null;
    StatusBarL m_barL = null;
    public boolean real_run = false;

    public boolean run_once = false;

    public GGCDbLoader(DataAccess da, StatusBar bar)
    {
        m_da = da;
        m_bar = bar;
//        System.out.println("GGCDbLoader inited");
    }


    public GGCDbLoader(DataAccess da, StatusBarL bar2)
    {
        m_da = da;
        m_barL = bar2;
    }


    @Override
    public void run()
    {

        if (run_once) 
            return;

        run_once = true;

        /*
        try 
        { 
            Thread.sleep(2000); 
        } 
        catch(InterruptedException ex) 
        { 
        }
*/
        GGCDb db = new GGCDb(m_da);

        if (m_bar!=null)
            m_bar.setDatabaseName(db.db_conn_name);
        else
            m_barL.setDatabaseName(db.db_conn_name);


        db.initDb();

        setDbStatus(StatusBar.DB_INIT_OK); 


        db.loadConfigData();


        db.loadStaticData();
    	db.loadNutritionDb1();
    	db.loadImplementedMeterData();

        setDbStatus(StatusBar.DB_LOAD);

        m_da.m_db = db;

        if (m_da.getParent()!=null)
            m_da.loadDailySettings(new GregorianCalendar(), true);
        else
            m_da.loadDailySettingsLittle(new GregorianCalendar(), true);

        //m_bar.setDatabaseName(db.db_conn_name);

        if (m_da.getParent()!=null)
        {
            // GGC
            MainFrame mf = m_da.getParent();
            mf.setDbActions(true);
            m_da.loadSettingsFromDb();
            mf.informationPanel.refreshPanels();
            mf.statusPanel.setStatusMessage(m_da.getI18nInstance().getMessage("READY"));
        }
        else
        {
            /// GGC Little
            GGCLittle mf = m_da.getParentLittle();
            mf.setDbActions(true);
            m_da.loadSettingsFromDb();
            mf.informationPanel.dailyStats.model.setDailyValues(m_da.getDayStats(new GregorianCalendar()));
            mf.informationPanel.refreshPanels();
            mf.statusPanel.setStatusMessage(m_da.getI18nInstance().getMessage("READY"));
        }

    }

    public void setDbStatus(int status)
    {
        if (m_bar!=null)
            m_bar.setDbStatus(status);
        else
            m_barL.setDbStatus(status);
    }


}


