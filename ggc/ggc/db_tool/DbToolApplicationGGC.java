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
	return "GGC_Config.properties";
    }

    public String toString()
    {
	return getApplicationName();
    }

}
