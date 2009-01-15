package ggc;

import ggc.core.db.GGCDbConfig;
import ggc.core.util.DataAccess;
import ggc.core.util.I18nControl;
import ggc.gui.MainFrame;

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


public class GGC
{
    // fields
    private static GGC s_theApp;
    private static MainFrame s_mainWindow;

    // Version information
    /**
     * Version of application
     */
    public static String s_version = "0.3.15";
    
    
    /**
     * Full Version
     */
    public static String full_version = "v" + s_version;
    
    /**
     * Version Date 
     */
    public static String version_date = "10th January 2009";

    /**
     * Checks if Db is correct version
     * 
     * @return
     */
    public static boolean isDbOk()
    {
        GGCDbConfig conf = new GGCDbConfig(true);

        DbCheckReport dcr = new DbCheckReport(conf.getDbInfoReportFilename(), I18nControl.getInstance());

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

        if (!GGC.isDbOk())
            return;

        boolean dev = false;

        if (args.length > 0)
            dev = true;

        DataAccess.deleteInstance();

        s_theApp = new GGC();
        s_theApp.init(dev);
    }

    /**
     * Init
     * 
     * @param dev
     */
    public void init(boolean dev)
    {
        s_mainWindow = new MainFrame("GGC - GNU Gluco Control", dev);
        Toolkit theKit = s_mainWindow.getToolkit();
        Dimension wndSize = theKit.getScreenSize();

        int x, y;
        x = wndSize.width / 2 - 400;
        y = wndSize.height / 2 - 300;

        s_mainWindow.setBounds(x, y, 800, 600);
        s_mainWindow.setVisible(true);
    }

}