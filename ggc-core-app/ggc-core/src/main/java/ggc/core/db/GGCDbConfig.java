package ggc.core.db;

/*
 * GGC - GNU Gluco Control
 * 
 * A pure Java application to help you manage your diabetes.
 * 
 * See AUTHORS for copyright information.
 * 
 * This program is free software; you can redistribute it and/or modify it under
 * the terms of the GNU General Public License as published by the Free Software
 * Foundation; either version 2 of the License, or (at your option) any later
 * version.
 * 
 * This program is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU General Public License for more
 * details.
 * 
 * You should have received a copy of the GNU General Public License along with
 * this program; if not, write to the Free Software Foundation, Inc., 59 Temple
 * Place, Suite 330, Boston, MA 02111-1307 USA
 * 
 * Filename: GGCDbConfig 
 *
 * Purpose: This is configuration file for GGC Database. This class is used for all external
 *      and internal handling of database. It contains main methods for accessing Hibernate
 *      Framework.
 * 
 * Author: andyrozman {andy@atech-software.com}
 */

import javax.swing.*;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.atech.db.hibernate.HibernateConfiguration;

import ggc.core.util.DataAccess;

import java.util.HashMap;

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
 *  Filename:     GGCDbConfig 
 *  Description:  This is configuration file for GGC Database. This class is used for 
 *                all external and internal handling of database. It contains main 
 *                methods for accessing Hibernate Framework.
 * 
 *  Author: andyrozman {andy@atech-software.com}  
 */

public class GGCDbConfig extends HibernateConfiguration
{

    private static final Logger LOG = LoggerFactory.getLogger(GGCDbConfig.class);

    private static String[] db_files = { "DbInfo.hbm.xml", //
                                  "GGC_Main.hbm.xml", //
                                  "GGC_Nutrition.hbm.xml", //
                                  "GGC_Other.hbm.xml", //
                                  "GGC_Pump.hbm.xml", //
                                  "GGC_CGMS.hbm.xml" };

    private static String[] db_files_debug = { "DbInfo.hbm.xml", //
                                        "GGC_Main.hbm.xml", //
                                        "com/atech/db/hibernate/hbm/User.hbm.xml", //
                                        "GGC_Nutrition.hbm.xml", //
                                        "GGC_Other.hbm.xml", //
                                        "GGC_Pump.hbm.xml", //
                                        "GGC_CGMS.hbm.xml", //
                                        "GGC_New.hbm.xml" };


    /**
     * Constructor
     * 
     * @param val
     */
    public GGCDbConfig(boolean val)
    {
        super(val);
    }


    public GGCDbConfig(String val)
    {
        super(val);
    }


    /**
     * Get Db Name
     * 
     * @return
     */
    public String getDbName()
    {
        return "GGCDb";
    }


    /**
     * Get Configuration File
     */
    @Override
    public String getConfigurationFile()
    {
        return "../data/GGC_Config.properties";
    }


    /**
     * Get Resource Files
     */
    @Override
    public String[] getResourceFiles()
    {
        return (DataAccess.developerMode) ? db_files_debug : db_files;
    }


    /**
     * Load Default Database
     * 
     * @param config_found
     */
    @Override
    public void loadDefaultDatabase(boolean config_found)
    {
        // System.out.println("load");
        db_num = 0;
        db_conn_name = "Internal Db (H2)";

        if (!config_found)
        {
            LOG.info("GGCDb: Database configuration not found. Using default database.");
        }
        LOG.info("GGCDb: Loading Db Configuration #" + db_num + ": " + db_conn_name);

        db_hib_dialect = "org.hibernate.dialect.H2Dialect";
        db_driver_class = "org.h2.Driver";
        db_conn_url = "jdbc:h2:../data/db/ggc_db";
        db_conn_username = "sa";
        db_conn_password = "";
    }


    /**
     * Is Check Enabled
     * 
     * @see com.atech.db.hibernate.check.DbCheckInterface#isCheckEnabled()
     */
    @Override
    public boolean isCheckEnabled()
    {
        return true;
    }


    /**
     * Get DbInfo Configuration
     * 
     * @see com.atech.db.hibernate.check.DbCheckInterface#getDbInfoConfiguration()
     */
    public String getDbInfoReportFilename()
    {
        return "../data/db_info.txt";
    }


    /**
     * Get Number Of Sessions
     * 
     * @see com.atech.db.hibernate.HibernateConfiguration#getNumberOfSessions()
     */
    @Override
    public int getNumberOfSessions()
    {
        return 2;
    }


    @Override
    public boolean canShemaBeAutomaticallyChanged()
    {
        return false;
    }


    @Override
    public String getShemaChangeType()
    {
        return null;
    }


    @Override
    public void initVersionToResourceFileMapping()
    {
        this.defaultVersion = 7;
        this.versionToResourceMap = new HashMap<String, Integer>();
        this.versionToResourceMap.put("17f84910f226c9d9bf6c8218fc824c6d2a01d05b", 7); // 0.8 -19.6.
        this.versionToResourceMap.put("2916075f381e81835b94ad03dc89622ceaa1dd57", 8); // not released, just for testing
    }


    public static void main(String[] args)
    {
        // ChecksumSHA1 checksumSHA1 = new ChecksumSHA1();

        JFrame jFrame = new JFrame();

        DataAccess da = DataAccess.createInstance(jFrame);

        da.setDeveloperMode(false);

        GGCDbConfig dbConfig = new GGCDbConfig(true);

        String sha1 = dbConfig.getResourcesSHA1();

        System.out.println("SHA1: normal: " + sha1);
    }

}
