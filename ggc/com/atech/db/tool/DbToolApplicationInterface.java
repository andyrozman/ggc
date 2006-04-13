package com.atech.db.tool;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Hashtable;

public interface DbToolApplicationInterface 
{

    public String getApplicationName();

    public String getApplicationDatabaseConfig();

    public void initStaticDbs();

    public void loadConfig();

    public void saveConfig();

    public int getFirstAvailableDatabase();

    public Hashtable getStaticDatabases();

    public Hashtable getCustomDatabases();

    public Hashtable getAllDatabases();

    public DatabaseSettings getDatabase(int index);

    public DatabaseSettings getSelectedDatabase();



}
