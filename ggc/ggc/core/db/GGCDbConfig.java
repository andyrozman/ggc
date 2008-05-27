
package ggc.core.db;

import com.atech.db.hibernate.HibernateConfiguration;

import org.hibernate.cfg.Configuration;

public class GGCDbConfig extends HibernateConfiguration
{

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
	// TODO Auto-generated method stub
	return null;
    }

    /* 
     * getResourceFiles
     */
    @Override
    public String[] getResourceFiles()
    {
	// TODO Auto-generated method stub
	return null;
    }

    /* 
     * loadDefaultDatabase
     */
    @Override
    public void loadDefaultDatabase()
    {
	// TODO Auto-generated method stub
	
    }

    /* 
     * isCheckEnabled
     */
    @Override
    public boolean isCheckEnabled()
    {
	// TODO Auto-generated method stub
	return false;
    }

    
}
