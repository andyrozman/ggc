package ggc.core.db.tool;

import java.io.BufferedWriter;

import com.atech.db.hibernate.HibernateConfiguration;
import com.atech.db.hibernate.tool.app.DbToolApplicationInitDb;
import com.atech.db.hibernate.tool.app.DbToolApplicationInterface;
import com.atech.db.hibernate.tool.app.DbToolApplicationLAF;
import com.atech.db.hibernate.tool.data.DatabaseConfiguration;

import ggc.core.db.GGCDb;
import ggc.core.db.GGCDbConfig;
import ggc.core.db.tool.init.DbToolApplicationInitDbGGC;

/*
 New methods :
 public String[] getAllDatabasesNamesPlusAsArray()
 public String[] getAllDatabasesNamesAsArray()
 public int getSelectedDatabaseIndex()

 //setChanged();
 //getChanged();

 hasChanged();
 setSelectedDatabaseIndex(int);

 */

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
 *  Filename:     DbToolApplicationGGC  
 *  Description:  DbTool Application Interface for GGC
 * 
 *  Author: andyrozman {andy@atech-software.com}  
 */

public class DbToolApplicationGGC extends DbToolApplicationLAF implements DbToolApplicationInterface
{

    private boolean m_changed = false;
    private HibernateConfiguration hibernateConfiguration;
    DbToolApplicationInitDb dbToolApplicationInitDb;


    /**
     * Constuctor
     */
    public DbToolApplicationGGC()
    {
        super();

    }


    @Override
    public void initDefaults()
    {

    }


    /**
     * Init Static Dbs
     */
    public void initStaticDbs()
    {
        // load all static database info
    }


    /**
     * Get Application Name
     * 
     * @return
     */
    public String getApplicationName()
    {
        return "GNU Gluco Control";
    }


    /**
     * Get Application Database Config
     * 
     * @return
     */
    public String getApplicationDatabaseConfig()
    {
        return "../data/GGC_Config.properties";
    }


    @Override
    public void addDefaultApplicationDatabase()
    {
        addDatabaseSetting("DB0_CONN_NAME", "Internal Database");
        addDatabaseSetting("DB0_DB_NAME", "HypersonicSQL File");
        addDatabaseSetting("DB0_CONN_DRIVER_CLASS", "org.hsqldb.jdbcDriver");
        addDatabaseSetting("DB0_CONN_URL", "jdbc:hsqldb:file:../data/ggc_db");
        addDatabaseSetting("DB0_CONN_USERNAME", "sa");
        addDatabaseSetting("DB0_CONN_PASSWORD", "");
        addDatabaseSetting("DB0_HIBERNATE_DIALECT", "org.hibernate.dialect.HSQLDialect");
    }


    @Override
    public void loadApplicationSpecific(String key, String value)
    {

    }


    @Override
    public void saveApplicationSpecific(BufferedWriter bw)
    {

    }


    @Override
    public String getConfigFileComment()
    {
        return null;
    }


    /**
     * Get Selected Database
     * 
     * @return
     */
    public DatabaseConfiguration getSelectedDatabase()
    {
        return null;
    }


    public boolean doesApplicationHaveDbInfo()
    {
        return true;
    }


    public boolean doesApplicationSupportFillDb()
    {
        return false;
    }


    public DbToolApplicationInitDb getInitDbInstance()
    {
        if (this.dbToolApplicationInitDb == null)
            this.dbToolApplicationInitDb = new DbToolApplicationInitDbGGC(this);

        return this.dbToolApplicationInitDb;
    }


    public String getCurrentDatabaseVersion()
    {
        return GGCDb.CURRENT_DB_VERSION;
    }


    public boolean doesApplicationSupportInit()
    {
        return true;
    }


    public HibernateConfiguration getHibernateConfiguration()
    {
        if (this.hibernateConfiguration == null)
        {
            this.hibernateConfiguration = new GGCDbConfig("");
        }

        return this.hibernateConfiguration;
    }


    // NEW
    /**
     * 
     */
    public void test()
    {
        /*
         * ArrayList list = new ArrayList();
         * int num = (int)(config_db_values.size()/7);
         * for (int i=0; i<num; i++)
         * {
         * DatabaseSettings ds = new DatabaseSettings();
         * ds.number = i;
         * ds.name = (String)config_db_values.get("DB" +i +"_CONN_NAME");
         * ds.db_name = (String)config_db_values.get("DB" +i +"_DB_NAME");
         * ds.driver = (String)config_db_values.get("DB" +i +"_CONN_DRIVER");
         * ds.url = (String)config_db_values.get("DB" +i +"_CONN_URL");
         * //ds.port = config_db_values.get("DB" +i +"_CONN_NAME");
         * ds.dialect = (String)config_db_values.get("DB" +i
         * +"_HIBERNATE_DIALECT");
         * ds.username = (String)config_db_values.get("DB" +i
         * +"_CONN_USERNAME");
         * ds.password = (String)config_db_values.get("DB" +i
         * +"_CONN_PASSWORD");
         * if (this.selected_db==i)
         * {
         * ds.isDefault = true;
         * }
         * list.add(ds);
         * }
         * return list;
         */
    }

}
