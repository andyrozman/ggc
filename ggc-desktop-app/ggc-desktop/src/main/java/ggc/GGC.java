package ggc;

import java.awt.*;

import com.atech.utils.java.VersionInterface;
import ggc.core.util.Version;
import lombok.extern.slf4j.Slf4j;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;

import com.atech.data.user_data_dir.UserDataDirectory;
import com.atech.db.hibernate.check.DbCheckReport;
import com.atech.utils.Log4jUtil;

import ggc.core.db.GGCDbConfig;
import ggc.core.util.DataAccess;
import ggc.core.util.GGCUserDataDirectoryContext;
import ggc.gui.main.MainFrame;
import org.hibernate.tool.hbm2ddl.SchemaUpdate;

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
@Slf4j
public class GGC {

    private static GGC s_theApp;
    private static MainFrame s_mainWindow;
    static UserDataDirectory userDataDirectory;


    /**
     * Checks if Db is correct version
     * 
     * @return
     */
    public static boolean isDbOk() {

        GGCDbConfig conf = new GGCDbConfig(true);

        DbCheckReport dcr = new DbCheckReport(conf.getDbInfoReportFilename(), DataAccess.getInstance()
                .getI18nControlInstance());

        if (dcr.canApplicationStart())
            return true;
        else
        {
            dcr.showError();
            return false;
        }

    }


    public static void initLogging() {
        //String PATTERN = "%d{HH:mm:ss,SSS} %5p [%c{1}:%L] - %m%n";
        String PATTERN = "%d{HH:mm:ss,SSS} %5p [%c{1}:%L] - %m%n";

        Log4jUtil.initLogger("GGC");

        Logger.getRootLogger().addAppender(Log4jUtil.createConsoleAppender(Level.DEBUG, PATTERN));

        Logger.getRootLogger().addAppender(
            Log4jUtil.createDailyZippedRollingFileAppender(Level.DEBUG, PATTERN, "yyyy-MM-dd"));

        filterLogging();
    }

    private static void logApplicationStart() {
        Version versionInterface = new ggc.core.util.Version();
        log.info("=============== Starting GGC ({} - {}) ===============", versionInterface.getVersion(), versionInterface.getBuildTime());
    }

    private static void filterLogging() {
        // GGC
        Logger.getLogger("ggc.core").setLevel(Level.INFO);
        Logger.getLogger("ggc.core.db.GGCDb").setLevel(Level.INFO);

        // Atech-Tools
        Logger.getLogger("com.atech.data.user_data_dir").setLevel(Level.INFO);

        // Pygmy
        Logger.getLogger("pygmy").setLevel(Level.INFO);

        // Hibernate
        Logger.getLogger("org.hibernate").setLevel(Level.INFO);
        Logger.getLogger("org.hibernate.SQL").setLevel(Level.INFO);
        Logger.getLogger("org.hibernate.type").setLevel(Level.INFO);

        // log schema export/update
        Logger.getLogger("org.hibernate.tool.hbm2ddl").setLevel(Level.INFO);
        Logger.getLogger("org.hibernate.tool.hbm2ddl.TableMetadata").setLevel(Level.WARN);
        Logger.getLogger("org.hibernate.tool.hbm2java").setLevel(Level.INFO);

        // Limit display of logging for Hibernate
        Logger.getLogger("net.sf.ehcache").setLevel(Level.ERROR);
        Logger.getLogger("org.hibernate.cfg.SettingsFactoryWithException").setLevel(Level.WARN);
        Logger.getLogger("org.hibernate.transaction").setLevel(Level.WARN);
        Logger.getLogger("org.hibernate.cfg.SettingsFactory").setLevel(Level.WARN);
        Logger.getLogger("org.hibernate.hql.ast.ASTQueryTranslatorFactory").setLevel(Level.WARN);
        Logger.getLogger("org.hibernate.impl").setLevel(Level.WARN);
    }


    /**
     * Main startup method
     * @param args
     */
    public static void main(String[] args) {

        initUserDataDirectory();

        initLogging();

        logApplicationStart();

        checkAndMigrateOrInstallDataDirectory();

        boolean dev = false;

        if (args.length > 0)
        {
            dev = true;
        }

        if (!dev)
        {
            if (!GGC.isDbOk()) {
                DataAccess.deleteInstance();
                return;
            }
        }

        //System.setProperty("java.util.Arrays.useLegacyMergeSort", "true");

        s_theApp = new GGC();
        s_theApp.init(dev);
    }

    private static void initUserDataDirectory() {
        userDataDirectory = UserDataDirectory.createInstance(new GGCUserDataDirectoryContext());
    }


    private static void checkAndMigrateOrInstallDataDirectory() {
        userDataDirectory.migrateAndValidateData();
    }


    /**
     * Init
     * 
     * @param dev
     */
    public void init(boolean dev)
    {
        s_mainWindow = new MainFrame("GGC - GNU Gluco Control", dev);

        if (dev)
        {
            s_mainWindow.setBounds(0, 30, 800, 600);
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
