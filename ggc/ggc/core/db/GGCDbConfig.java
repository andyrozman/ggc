
package ggc.core.db;

import com.atech.db.hibernate.HibernateConfiguration;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class GGCDbConfig extends HibernateConfiguration
{

    private static Log log = LogFactory.getLog(GGCDbConfig.class);
    
    private String[] db_files = { 
	        "GGC_Main.hbm.xml",
	    	"GGC_Nutrition.hbm.xml",
	        "GGC_Other.hbm.xml",
	        "GGC_Pump.hbm.xml",
	        "GGC_CGM.hbm.xml"
    };

    
    
    public GGCDbConfig(boolean val)
    {
	super(val);
    }
    
    
    /* 
     * getDbName
     */
    public String getDbName()
    {
	return "GGCDb";
    }

    /* 
     * getConfigurationFile
     */
    @Override
    public String getConfigurationFile()
    {
	return "../data/GGC_Config.properties";
    }

    /* 
     * getResourceFiles
     */
    @Override
    public String[] getResourceFiles()
    {
	return db_files;
    }

    /* 
     * loadDefaultDatabase
     */
    @Override
    public void loadDefaultDatabase()
    {
	System.out.println("load");
        db_num = 0;
        db_conn_name = "Internal Db (H2)";

        log.info("GGCDb: Database configuration not found. Using default database.");
        log.info("GGCDb: Loading Db Configuration #"+ db_num + ": " + db_conn_name);

        db_hib_dialect = "org.hibernate.dialect.H2Dialect";
        db_driver_class = "org.h2.Driver";
        db_conn_url = "jdbc:h2:../data/db/ggc_db";
        db_conn_username = "sa";
        db_conn_password = "";
    }

    /* 
     * isCheckEnabled
     */
    @Override
    public boolean isCheckEnabled()
    {
	return true;
    }


    /* 
     * getDbInfoReportFilename
     */
    public String getDbInfoReportFilename()
    {
	return "../data/db_info.txt";
    }
    
    
}
