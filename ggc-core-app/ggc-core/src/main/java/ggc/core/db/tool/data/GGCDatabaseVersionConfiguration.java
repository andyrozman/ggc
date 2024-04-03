package ggc.core.db.tool.data;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.atech.db.hibernate.tool.data.DatabaseTableConfiguration;
import com.atech.db.hibernate.tool.data.DatabaseVersionConfiguration;

public enum GGCDatabaseVersionConfiguration implements DatabaseVersionConfiguration
{
    GGC_DB_V7("7"), // current
    GGC_DB_V8("8"), // future

    ;

    static Map<String, GGCDatabaseVersionConfiguration> keyMap;
    static Map<GGCDatabaseVersionConfiguration, List<GGCDatabaseTableConfiguration>> tableDbVersionMap;

    String dbVersionText;

    static
    {
        keyMap = new HashMap<String, GGCDatabaseVersionConfiguration>();
        tableDbVersionMap = new HashMap<GGCDatabaseVersionConfiguration, List<GGCDatabaseTableConfiguration>>();

        for (GGCDatabaseVersionConfiguration cfg : values())
        {
            keyMap.put(cfg.dbVersionText, cfg);
        }

        tableDbVersionMap.put(GGC_DB_V7, Arrays.asList(GGCDatabaseTableConfiguration.DayValueH));

    }


    private static List<GGCDatabaseTableConfiguration> getTablesForDbVersion(
            GGCDatabaseVersionConfiguration configuration)
    {
        List<GGCDatabaseTableConfiguration> list = null;

        if (configuration == GGC_DB_V7 || configuration == GGC_DB_V8)
        {
            list = Arrays.asList(GGCDatabaseTableConfiguration.DayValueH, //
                GGCDatabaseTableConfiguration.DayValueH, //
                GGCDatabaseTableConfiguration.DayValueH, //
                GGCDatabaseTableConfiguration.DayValueH, //
                GGCDatabaseTableConfiguration.DayValueH, //
                GGCDatabaseTableConfiguration.DayValueH, //
                GGCDatabaseTableConfiguration.DayValueH, //
                GGCDatabaseTableConfiguration.DayValueH, //
                GGCDatabaseTableConfiguration.DayValueH, //
                GGCDatabaseTableConfiguration.DayValueH, //
                GGCDatabaseTableConfiguration.DayValueH, //
                GGCDatabaseTableConfiguration.DayValueH, //
                GGCDatabaseTableConfiguration.DayValueH, //
                GGCDatabaseTableConfiguration.DayValueH, //
                GGCDatabaseTableConfiguration.DayValueH, //
                GGCDatabaseTableConfiguration.DayValueH //
            );
        }

        if (configuration == GGC_DB_V8)
        {
            list.addAll(Arrays.asList(GGCDatabaseTableConfiguration.DayValueH));
        }

        return list;

    }


    GGCDatabaseVersionConfiguration(String key)
    {
        dbVersionText = key;
    }


    public List<DatabaseTableConfiguration> getTablesForDatabase(String key)
    {
        // FIXME
        GGCDatabaseVersionConfiguration config = null;

        switch (config)
        {

        }
        return null;
    }


    public String getVersion()
    {
        return this.dbVersionText;
    }

}
