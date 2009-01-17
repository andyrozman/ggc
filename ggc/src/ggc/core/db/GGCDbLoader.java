package ggc.core.db;


/*
 *  GGC - GNU Gluco Control
 *
 *  A pure Java application to help you manage your diabetes.
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
 *  Filename: GGCDbLoader
 *  
 *  Purpose:  This is GGCDb Loader. It help system to load all needed data for 
 *      GGC Database Session.
 *
 *  Author:   andyrozman  {andy@atech-software.com}
 */



import ggc.core.util.DataAccess;
import ggc.gui.MainFrame;
import ggc.gui.StatusBar;
import ggc.gui.little.GGCLittle;
import ggc.gui.little.StatusBarL;

import java.io.File;
import java.util.GregorianCalendar;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 *  Application:   GGC - GNU Gluco Control
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
 *  Filename:     GGCDbLoader  
 *  Description:  This is GGCDb Loader. It help system to load all needed data for 
 *                GGC Database Session.
 * 
 *  Author: andyrozman {andy@atech-software.com}  
 */


public class GGCDbLoader extends Thread
{

    // red status
    // 1 - init
    // yellow
    // 2 - load configuration
    // 3 - load statistics for display, apointments
    // blue
    // 4 - load doctors data
    // 5 - load nutrition(1) root data
    // 6 - load nutrition(2) root data
    // 7 - load meals root data

    // 99 - loading complete
    // green

    private static Log log = LogFactory.getLog(GGCDbLoader.class);

    DataAccess m_da = null;
    StatusBar m_bar = null;
    StatusBarL m_barL = null;
    //private boolean real_run = false;
    private boolean run_once = false;

    
    //public boolean part_start = true;
    /**
     * Part start. When this is enables, we don't load Food, Doctor's data
     */
    public boolean part_start = false;
    
    //public boolean debug = false;
    
    
    /**
     * Constructor
     * 
     * @param da
     */
    public GGCDbLoader(DataAccess da)
    {
        m_da = da;
        System.out.println("GGCDbLoader in development mode");
//        part_start = true;
    }
    
    
    
    /**
     * Constructor
     * 
     * @param da
     * @param bar
     */
    public GGCDbLoader(DataAccess da, StatusBar bar)
    {
        m_da = da;
        m_bar = bar;
//        System.out.println("GGCDbLoader inited");
    }


    /**
     * Constructor
     * 
     * @param da
     * @param bar2
     */
    public GGCDbLoader(DataAccess da, StatusBarL bar2)
    {
        m_da = da;
        m_barL = bar2;
    }


    /**
     * Run (Thread)
     * 
     * @see java.lang.Thread#run()
     */
    @Override
    public void run()
    {

        if (run_once) 
            return;

        run_once = true;

        
        if (new File("../data/debug_no_food.txt").exists())
            part_start = true;
        		
        
        // 1 - init
        
        long start_time = System.currentTimeMillis();
        
        GGCDb db = new GGCDb(m_da);

        //if (!part_start)
        {
            if (m_bar!=null)
                m_bar.setDatabaseName(db.getHibernateConfiguration().getConnectionName());
            else
                m_barL.setDatabaseName(db.getHibernateConfiguration().getConnectionName());
        }


        db.initDb();

        setDbStatus(StatusBar.DB_INIT_DONE); 

        
        // 2 - load configuration
        
        db.loadConfigData();
        db.loadStaticData();
        m_da.setDb(db);
        
        // 3 - init plugins
        m_da.initPlugIns();
        
        // 3 - load daily data for display, appointments
        
        if (m_da.getParent()!=null)
            m_da.loadDailySettings(new GregorianCalendar(), true);
        else
            m_da.loadDailySettingsLittle(new GregorianCalendar(), true);

            
        if (m_da.getParent()!=null)
        {
            // GGC
            MainFrame mf = m_da.getParent();
            //mf.setDbActions(true);
            m_da.loadSettingsFromDb();
            mf.informationPanel.refreshPanels();
            mf.statusPanel.setStatusMessage(m_da.getI18nControlInstance().getMessage("READY"));
        }
        else
        {
            /// GGC Little
            GGCLittle mf = m_da.getParentLittle();
            //mf.setDbActions(true);
            m_da.loadSettingsFromDb();
            mf.getInformationPanel().dailyStats.getTableModel().setDailyValues(m_da.getDayStats(new GregorianCalendar()));
            mf.getInformationPanel().refreshPanels();
            mf.getStatusPanel().setStatusMessage(m_da.getI18nControlInstance().getMessage("READY"));
        }

        setDbStatus(StatusBar.DB_BASE_DONE); 
        
   
        if (!part_start)
        {
        
            // 4 - load doctors data
            // TODO: in version 0.4
           
            // 5 - load nutrition(1) root data
            db.loadNutritionDbBase();
            db.loadNutritionDb1();
            
            // 6 - load nutrition(2) root data
            db.loadNutritionDb2();
            
            
            // 7 - load meals root data
            db.loadMealsDb();

            setDbStatus(StatusBar.DB_LOADED);

        }
        else
        {
            db.loadNutritionDbBase();
            setDbStatus(StatusBar.DB_LOADED);
        }
        
        
        
/*        
        if (part_start)
        {
            db.loadNutritionDb1();
            db.loadNutritionDb2();
            db.loadMealsDb();
            
        }
        else
        {
            db.loadConfigData();
    
            db.loadStaticData();
            db.loadNutritionDb1();
//            db.loadImplementedMeterData();
        }
*/
    	long dif = System.currentTimeMillis() - start_time;

    	//System.out.println("We needed "  + (dif/1000) + " seconds to startup.");
    	log.debug("We needed "  + (dif/1000) + " seconds to startup.");
    }

    /**
     * Set Db Status
     * 
     * @param status
     */
    public void setDbStatus(int status)
    {
        //if (part_start)
        //    return;
	
        if (m_bar!=null)
        {
            m_bar.setDbStatus(status);
            ((MainFrame)m_da.getMainParent()).setMenusByDbLoad(status);
        }
        else
        {
            m_barL.setDbStatus(status);
            
            // TODO: version 0.4, when fixing GGC Little
            //setMenusByDbLoad(int status)
        }
    }


}


