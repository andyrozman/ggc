package ggc;

import ggc.core.db.GGCDbConfig;
import ggc.core.util.DataAccess;
import ggc.doc.DocMainFrame;

import java.awt.Dimension;
import java.awt.Toolkit;

import com.atech.db.hibernate.check.DbCheckReport;


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
 *  Filename:     GGC  
 *  Description:  Main startup file
 * 
 *  Author: schultd
 *          Andy {andy@atech-software.com}  
 */


public class GGCDocEdition
{
    // fields
    private static GGCDocEdition s_theApp;
    private static DocMainFrame s_mainWindow;

    // Version information
    /**
     * Version of application
     */
    //public static String s_version = "0.4.6.4"; //3.16";
    
    
    /**
     * Full Version
     */
    public static String full_version = "v" + DataAccess.CORE_VERSION;
    
    /**
     * Version Date 
     */
    //public static String version_date = "15th March 2009";

    /**
     * Checks if Db is correct version
     * 
     * @return
     */
    public static boolean isDbOk()
    {
        GGCDbConfig conf = new GGCDbConfig(true);

        DbCheckReport dcr = new DbCheckReport(conf.getDbInfoReportFilename(), DataAccess.getInstance().getI18nControlInstance());

        if (dcr.canApplicationStart())
        {
            return true;
        }
        else
        {
            dcr.showError();
            return false;
        }

    }

    /**
     * Main startup method
     * @param args
     */
    public static void main(String[] args)
    {

        

        boolean dev = false;

        if (args.length > 0)
            dev = true;

        
        if (!dev)
        {
            if (!GGCDocEdition.isDbOk())
                return;            

            DataAccess.deleteInstance();
        }
        

        s_theApp = new GGCDocEdition();
        s_theApp.init(dev);
    }

    /**
     * Init
     * 
     * @param dev
     */
    public void init(boolean dev)
    {
        s_mainWindow = new DocMainFrame("  GNU Gluco Control - Doctor's Edition", dev);

        if (dev)
        {
            s_mainWindow.setBounds(0, 0, 800, 600);
            s_mainWindow.setVisible(true);
        }
        else
        {
            Toolkit theKit = s_mainWindow.getToolkit();
            Dimension wndSize = theKit.getScreenSize();
    
            int x, y;
            x = wndSize.width / 2 - 400;
            y = wndSize.height / 2 - 300;
    
            s_mainWindow.setBounds(x, y, 800, 600);
            s_mainWindow.setVisible(true);
        }
    }

}