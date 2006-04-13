package ggc.db.db_tool;

import com.atech.db.tool.DbToolApplicationInterface;
import java.util.ArrayList;
import java.util.Iterator;

public class DbToolApplicationGGC implements DbToolApplicationInterface
{

    public String getApplicationName()
    {
	return "GNU Gluco Control";
    }	

    public String getApplicationDatabaseConfig()
    {
	return "../data/GGC_Config.properties";
    }

    public void loadConfig()
    {
    }

    public void saveConfig()
    {
    }

    public int getFirstAvailableDatabase()
    {
	return 0;
    }

    public ArrayList getStaticDatabases()
    {
	return null;
    }

    public String toString()
    {
	return getApplicationName();
    }

}
