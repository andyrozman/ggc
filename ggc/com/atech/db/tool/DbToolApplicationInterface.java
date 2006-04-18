package com.atech.db.tool;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Hashtable;

public interface DbToolApplicationInterface 
{

    String getApplicationName();

    String getApplicationDatabaseConfig();

    void initStaticDbs();

    void loadConfig();

    void saveConfig();

    int getFirstAvailableDatabase();

    Hashtable getStaticDatabases();

    Hashtable getCustomDatabases();

    Hashtable getAllDatabases();

    DatabaseSettings getDatabase(int index);

    DatabaseSettings getSelectedDatabase();


}
